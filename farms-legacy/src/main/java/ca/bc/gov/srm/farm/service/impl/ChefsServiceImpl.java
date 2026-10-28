/**
 * Copyright (c) 2023,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.service.impl;

import static ca.bc.gov.srm.farm.chefs.ChefsConstants.*;
import static ca.bc.gov.srm.farm.log.LoggingUtils.*;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.chefs.ChefsConfigurationUtil;
import ca.bc.gov.srm.farm.chefs.ChefsFormCredentials;
import ca.bc.gov.srm.farm.chefs.database.ChefsSubmissionStatusCodes;
import ca.bc.gov.srm.farm.chefs.processor.AdjustmentSubmissionProcessor;
import ca.bc.gov.srm.farm.chefs.processor.CashMarginsSubmissionProcessor;
import ca.bc.gov.srm.farm.chefs.processor.ChefsSubmissionProcessor;
import ca.bc.gov.srm.farm.chefs.processor.CoverageSubmissionProcessor;
import ca.bc.gov.srm.farm.chefs.processor.InterimSubmissionProcessor;
import ca.bc.gov.srm.farm.chefs.processor.NolSubmissionProcessor;
import ca.bc.gov.srm.farm.chefs.processor.NppSubmissionProcessor;
import ca.bc.gov.srm.farm.chefs.processor.StatementASubmissionProcessor;
import ca.bc.gov.srm.farm.chefs.processor.SupplementalSubmissionProcessor;
import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationUtility;
import ca.bc.gov.srm.farm.crm.CrmConfigurationUtil;
import ca.bc.gov.srm.farm.crm.CrmRestApiDao;
import ca.bc.gov.srm.farm.dao.ChefsDatabaseDAO;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmission;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.exception.TooManyRequestsException;
import ca.bc.gov.srm.farm.security.BusinessAction;
import ca.bc.gov.srm.farm.service.BaseService;
import ca.bc.gov.srm.farm.service.ChefsService;
import ca.bc.gov.srm.farm.util.DateUtils;

/**
 * @author awilkinson
 */
public class ChefsServiceImpl extends BaseService implements ChefsService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  /** Environments where leftover test submissions are cancelled. */
  private static final List<String> CANCEL_OLD_ERRORS_ENVIRONMENTS =
      Arrays.asList(DEV_ENVIRONMENT, DLVR_ENVIRONMENT, TEST_ENVIRONMENT);

  /** Submissions older than this that are still in an error status are cancelled. */
  private static final int ERROR_SUBMISSION_MONTHS_OLD = 2;

  /** Maximum submissions cancelled per run, to keep the number of CRM requests low. The rest are cancelled on later runs. */
  private static final int MAX_OLD_ERROR_SUBMISSIONS_PER_RUN = 50;

  /**
   * See there are form submissions in CHEFS that need to be processed.
   *
   * @param webadeConnection must be a bare SQL connection
   *
   * @throws ServiceException on exception
   */
  @Override
  public void processSubmissions(final Connection webadeConnection) throws ServiceException {
    logMethodStart(logger);

    try {
      
      CacheFactory.getRequestCache().addItem(CacheKeys.CURRENT_BUSINESS_ACTION, BusinessAction.system());

      @SuppressWarnings("resource")
      Connection connection = webadeConnection;

      cancelOldErrorSubmissions(connection);

      processSubmissions(new NolSubmissionProcessor(connection, USER_TYPE_IDIR));
      processSubmissions(new NolSubmissionProcessor(connection, USER_TYPE_BASIC_BCEID));

      processSubmissions(new InterimSubmissionProcessor(connection, USER_TYPE_IDIR));
      processSubmissions(new InterimSubmissionProcessor(connection, USER_TYPE_BASIC_BCEID));

      processSubmissions(new NppSubmissionProcessor(connection, USER_TYPE_IDIR));
      processSubmissions(new NppSubmissionProcessor(connection, USER_TYPE_BASIC_BCEID));

      processSubmissions(new AdjustmentSubmissionProcessor(connection, USER_TYPE_IDIR));
      processSubmissions(new AdjustmentSubmissionProcessor(connection, USER_TYPE_BASIC_BCEID));

      processSubmissions(new SupplementalSubmissionProcessor(connection, USER_TYPE_IDIR));
      processSubmissions(new SupplementalSubmissionProcessor(connection, USER_TYPE_BASIC_BCEID));

      processSubmissions(new CoverageSubmissionProcessor(connection, USER_TYPE_IDIR));
      processSubmissions(new CoverageSubmissionProcessor(connection, USER_TYPE_BASIC_BCEID));
      
      processSubmissions(new CashMarginsSubmissionProcessor(connection, USER_TYPE_IDIR));
      processSubmissions(new CashMarginsSubmissionProcessor(connection, USER_TYPE_BASIC_BCEID));

      processSubmissions(new StatementASubmissionProcessor(connection, USER_TYPE_IDIR));
      processSubmissions(new StatementASubmissionProcessor(connection, USER_TYPE_BASIC_BCEID));

    } catch (Exception e) {
      if(TooManyRequestsException.findIn(e) != null) {
        // Not a failure. Remaining forms are picked up on the next run.
        logger.warn("CHEFS rate limit reached. Skipping remaining forms until the next run: " + e.getMessage());
        logMethodEnd(logger);
        return;
      }
      logger.error("Error processing submissions: ", e);
      throw new ServiceException(e);
    }
    logMethodEnd(logger);
  }


  /**
   * @throws ServiceException if CHEFS rate limited us, so that the remaining forms are skipped
   */
  private void processSubmissions(ChefsSubmissionProcessor<?> submissionProcessor) throws ServiceException {
    
    ChefsConfigurationUtil chefsConfig = ChefsConfigurationUtil.getInstance();
    
    String formTypeCode = submissionProcessor.getFormTypeCode();
    String formUserType = submissionProcessor.getFormUserType();
    
    ChefsFormCredentials formCredentials = chefsConfig.getFormCredentials(formTypeCode, formUserType);
    String apiKey = formCredentials.getApiKey();

    // The configured value for an API Key is initially set to "placeholder".
    // Submissions for forms not be processed until the form's API Key is updated.
    if( ! API_KEY_PLACEHOLDER.equals(apiKey) ) {
      
      try {
        submissionProcessor.loadSubmissionsFromChefs();
        submissionProcessor.loadSubmissionsFromDatabase();
        submissionProcessor.processSubmissions();
      
      } catch (Exception e) {
        if(TooManyRequestsException.findIn(e) != null) {
          throw new ServiceException(String.format("CHEFS rate limit reached processing %s %s submissions", formTypeCode, formUserType), e);
        }
        logger.error(String.format("Error processing %s %s submissions: ", formTypeCode, formUserType), e);
      }
      
    } else {
      logger.warn(String.format("API Key not set for %s %s. Skipping this form.", formTypeCode, formUserType));
    }
  }


  /**
   * Cancel old submissions left over from testing that are still in an error status, so that they
   * are no longer fetched from CHEFS on every run. Their validation error tasks are completed.
   * Non-production environments only. Errors are logged so that they don't stop form processing.
   */
  private void cancelOldErrorSubmissions(Connection connection) {

    String environment = ConfigurationUtility.getInstance().getValue(ConfigurationKeys.ENVIRONMENT_NAME);
    if( ! CANCEL_OLD_ERRORS_ENVIRONMENTS.contains(environment) ) {
      return;
    }

    try {
      ChefsDatabaseDAO chefsDatabaseDao = new ChefsDatabaseDAO();
      Collection<ChefsSubmission> submissions = chefsDatabaseDao.readOldErrorSubmissions(connection,
          ERROR_SUBMISSION_MONTHS_OLD, MAX_OLD_ERROR_SUBMISSIONS_PER_RUN);
      if(submissions.isEmpty()) {
        return;
      }

      CrmRestApiDao crmDao = new CrmRestApiDao();
      String validationErrorUrl = CrmConfigurationUtil.getInstance().getValidationErrorUrl();

      for(ChefsSubmission submission : submissions) {
        String validationTaskGuid = submission.getValidationTaskGuid();
        if(validationTaskGuid != null) {
          try {
            crmDao.completeTask(validationErrorUrl, validationTaskGuid);
          } catch (ServiceException e) {
            // Cancel the submission anyway, or it would be selected again on every run.
            // The task may already be completed or deleted.
            logger.warn(String.format("Unable to complete validation task %s for old %s error submission %s: %s",
                validationTaskGuid, submission.getFormTypeCode(), submission.getSubmissionGuid(), e.getMessage()));
          }
        }

        String statusCode = submission.getSubmissionStatusCode();
        submission.setSubmissionStatusCode(ChefsSubmissionStatusCodes.CANCELLED);
        chefsDatabaseDao.updateSubmission(connection, submission, SYSTEM_USER);

        logger.info(String.format(
            "Cancelled old error submission %s. Status: %s, Form: %s, BCeID: %s, Created: %s, Validation task: %s",
            submission.getSubmissionGuid(), statusCode, submission.getFormTypeCode(), submission.getBceidFormInd(),
            DateUtils.formatDate(submission.getCreatedDate()), validationTaskGuid));
      }

      logger.info(String.format("Cancelled %d error submissions created more than %d months ago.",
          submissions.size(), ERROR_SUBMISSION_MONTHS_OLD));

    } catch (Exception e) {
      logger.error("Error cancelling old error submissions: ", e);
    }
  }
}
