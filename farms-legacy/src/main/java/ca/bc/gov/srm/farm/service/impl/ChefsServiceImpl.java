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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.chefs.ChefsConfigurationUtil;
import ca.bc.gov.srm.farm.chefs.ChefsFormCredentials;
import ca.bc.gov.srm.farm.chefs.processor.AdjustmentSubmissionProcessor;
import ca.bc.gov.srm.farm.chefs.processor.CashMarginsSubmissionProcessor;
import ca.bc.gov.srm.farm.chefs.processor.ChefsSubmissionProcessor;
import ca.bc.gov.srm.farm.chefs.processor.CoverageSubmissionProcessor;
import ca.bc.gov.srm.farm.chefs.processor.InterimSubmissionProcessor;
import ca.bc.gov.srm.farm.chefs.processor.NolSubmissionProcessor;
import ca.bc.gov.srm.farm.chefs.processor.NppSubmissionProcessor;
import ca.bc.gov.srm.farm.chefs.processor.StatementASubmissionProcessor;
import ca.bc.gov.srm.farm.chefs.processor.SupplementalSubmissionProcessor;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.security.BusinessAction;
import ca.bc.gov.srm.farm.service.BaseService;
import ca.bc.gov.srm.farm.service.ChefsService;
import ca.bc.gov.webade.dbpool.WrapperConnection;

/**
 * @author awilkinson
 */
public class ChefsServiceImpl extends BaseService implements ChefsService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

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
      Connection connection = getWrappedConnection(webadeConnection);
      
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
      logger.error("Error processing submissions: ", e);
      throw new ServiceException(e);
    }
    logMethodEnd(logger);
  }


  private void processSubmissions(ChefsSubmissionProcessor<?> submissionProcessor) {
    
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
        logger.error(String.format("Error processing %s %s submissions: ", formTypeCode, formUserType), e);
      }
      
    } else {
      logger.warn(String.format("API Key not set for %s %s. Skipping this form.", formTypeCode, formUserType));
    }
  }


  private Connection getWrappedConnection(final Connection webadeConnection) {
    Connection connection = webadeConnection;

    //
    // If we use the webade connection then we'll run into alot of problems
    // with the clobs and blobs.
    //
    if (webadeConnection instanceof WrapperConnection) {
      connection = ((WrapperConnection) webadeConnection).getWrappedConnection();
    }
    return connection;
  }
  
  
}
