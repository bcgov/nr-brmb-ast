package ca.bc.gov.srm.farm.service.impl;

import static ca.bc.gov.srm.farm.log.LoggingUtils.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.chefs.ChefsAuthenticationHandler;
import ca.bc.gov.srm.farm.chefs.ChefsConfigurationUtil;
import ca.bc.gov.srm.farm.chefs.ChefsConstants;
import ca.bc.gov.srm.farm.chefs.ChefsFormCredentials;
import ca.bc.gov.srm.farm.chefs.ChefsRestApiDao;
import ca.bc.gov.srm.farm.chefs.database.ChefsFormTypeCodes;
import ca.bc.gov.srm.farm.chefs.database.ChefsSubmissionStatusCodes;
import ca.bc.gov.srm.farm.chefs.resource.adjustment.AdjustmentSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.cashMargin.CashMarginsSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.coverage.CoverageSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.interim.InterimSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.nol.NolSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.npp.NppSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.preflight.PreflightWrapperResource;
import ca.bc.gov.srm.farm.chefs.resource.statementA.StatementASubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionWrapperResource;
import ca.bc.gov.srm.farm.chefs.resource.supplemental.SupplementalSubmissionDataResource;
import ca.bc.gov.srm.farm.crm.CrmConfigurationUtil;
import ca.bc.gov.srm.farm.crm.CrmRestApiDao;
import ca.bc.gov.srm.farm.dao.ChefsDatabaseDAO;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmission;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmssnCrmEntity;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.BaseService;
import ca.bc.gov.srm.farm.service.ChefsFormSubmissionService;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;

public class ChefsFormSubmissionServiceImpl extends BaseService implements ChefsFormSubmissionService {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private static ChefsDatabaseDAO chefsDatabaseDao = new ChefsDatabaseDAO();
  private final ChefsConfigurationUtil chefsConfig = ChefsConfigurationUtil.getInstance();
  private ChefsRestApiDao chefsApiDao = null;
  private ChefsFormCredentials formCredentials = null;
  private CrmConfigurationUtil crmConfig = CrmConfigurationUtil.getInstance();
  private static CrmRestApiDao crmDao = new CrmRestApiDao();

  @SuppressWarnings("resource")
  @Override
  public List<ChefsSubmission> getFormSubmissionsByType(String formType) throws ServiceException {

    logMethodStart(logger);

    Transaction transaction = null;

    transaction = openTransaction();
    Connection conn = (Connection) transaction.getDatastore();

    List<ChefsSubmission> chefsSubmissions = new ArrayList<>();
    try {
      chefsSubmissions = chefsDatabaseDao.readSubmissionsByFormType(conn, formType);
    } catch (DataAccessException e) {
      throw new ServiceException(e);
    }
    
    for (ChefsSubmission submission : chefsSubmissions) {
      String userFormType = getUserFormType(submission.getBceidFormInd());
      submission.setUserFormTypeCode(userFormType);
    }

    logMethodEnd(logger);
    return chefsSubmissions;
  }
  
  private String getUserFormType(String BceidFormInd) {
    if (BceidFormInd == null) {
      return "";
    }
    if (BceidFormInd.equals("Y")) {
      return ChefsConstants.USER_TYPE_BASIC_BCEID;
    } else {
      return ChefsConstants.USER_TYPE_IDIR;
    }
  }

  @SuppressWarnings("resource")
  @Override
  public ChefsSubmission getSubmissionByGuid(String submissionGuid) throws ServiceException {

    logMethodStart(logger);

    Transaction transaction = null;

    transaction = openTransaction();
    Connection conn = (Connection) transaction.getDatastore();

    ChefsSubmission result = null;
    try {
      result = chefsDatabaseDao.readSubmissionByGuid(conn, submissionGuid);
    } catch (DataAccessException e) {
      throw new ServiceException(e);
    }
    logger.warn(result.toString());

    logMethodEnd(logger);
    return result;
  }

  @Override
  public SubmissionWrapperResource<?> getSubmissionWrapperResource(String submissionGuid, String formType, String userType) throws ServiceException {

    formCredentials = chefsConfig.getFormCredentials(formType, userType);
    chefsApiDao = new ChefsRestApiDao(new ChefsAuthenticationHandler(formCredentials));
    String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);

    switch (formType) {
    case ChefsFormTypeCodes.ADJ:
      return chefsApiDao.getSubmissionWrapperResource(submissionUrl, AdjustmentSubmissionDataResource.class);
    case ChefsFormTypeCodes.CM:
      return chefsApiDao.getSubmissionWrapperResource(submissionUrl, CashMarginsSubmissionDataResource.class);
    case ChefsFormTypeCodes.CN:
      return chefsApiDao.getSubmissionWrapperResource(submissionUrl, CoverageSubmissionDataResource.class);
    case ChefsFormTypeCodes.INTERIM:
      return chefsApiDao.getSubmissionWrapperResource(submissionUrl, InterimSubmissionDataResource.class);
    case ChefsFormTypeCodes.NOL:
      return chefsApiDao.getSubmissionWrapperResource(submissionUrl, NolSubmissionDataResource.class);
    case ChefsFormTypeCodes.NPP:
      return chefsApiDao.getSubmissionWrapperResource(submissionUrl, NppSubmissionDataResource.class);
    case ChefsFormTypeCodes.STA:
      return chefsApiDao.getSubmissionWrapperResource(submissionUrl, StatementASubmissionDataResource.class);
    case ChefsFormTypeCodes.SUPP:
      return chefsApiDao.getSubmissionWrapperResource(submissionUrl, SupplementalSubmissionDataResource.class);
    }
    return null;
  }

  @Override
  public String getUserFormType(ChefsSubmission submission) {
    String bceidFormInd = submission.getBceidFormInd();

    if (bceidFormInd == null) {
      return null;
    }
    if (bceidFormInd.equals("Y")) {
      return ChefsConstants.USER_TYPE_BASIC_BCEID;
    }
    return ChefsConstants.USER_TYPE_IDIR;

  }

  @Override
  public String getUserFormTypeFromPreflight(String submissionGuid) throws ServiceException {
    // using arbitrary form type and user type, will working with anything.
    formCredentials = chefsConfig.getFormCredentials(ChefsFormTypeCodes.NPP, ChefsConstants.USER_TYPE_IDIR);
    chefsApiDao = new ChefsRestApiDao(new ChefsAuthenticationHandler(formCredentials));
    String submissionUrl = chefsConfig.getSubmissionOptionsUrl(submissionGuid);

    PreflightWrapperResource<?> preflightWrapperResource;
    try {
      preflightWrapperResource = chefsApiDao.getSubmissionOptionsResource(submissionUrl, PreflightWrapperResource.class);
    } catch (ServiceException e) {
      logger.error("Error getting preflight details: ", e);
      throw new ServiceException(e);
    }

    if (preflightWrapperResource != null) {
      if (preflightWrapperResource.getForm().getIdpHints().size() > 0 && preflightWrapperResource.getForm().getIdpHints().get(0).equals(ChefsConstants.IDPHINTS_BASIC_BCEID)) {
        return ChefsConstants.USER_TYPE_BASIC_BCEID;
      } else {
        return ChefsConstants.USER_TYPE_IDIR;
      }
    }
    return null;
  }

  @Override
  @SuppressWarnings("resource")
  public void updateSubmission(ChefsSubmission submission) throws ServiceException {

    logMethodStart(logger);

    Transaction transaction = null;

    transaction = openTransaction();
    Connection conn = (Connection) transaction.getDatastore();
    
    String user = CurrentUser.getUser().getUserId();

    try {
      chefsDatabaseDao.updateSubmission(conn, submission, user);
    } catch (DataAccessException e) {
      logger.error("Error in updateSubmission: ", e);
      throw new ServiceException(e);
    }

    String validationTaskGuid = submission.getValidationTaskGuid();
    String submissionStatusCode = submission.getSubmissionStatusCode();

    if ((submissionStatusCode.equals(ChefsSubmissionStatusCodes.SUBMITTED) || submissionStatusCode.equals(ChefsSubmissionStatusCodes.CANCELLED))
        && validationTaskGuid != null) {
      logger.debug("Complete validation task validationTaskGuid: " + validationTaskGuid);
      crmDao.completeTask(crmConfig.getValidationErrorUrl(), validationTaskGuid);
    }
    logMethodEnd(logger);
  }
  
  @Override
  @SuppressWarnings("resource")
  public List<ChefsSubmssnCrmEntity> getSubmissionCrmEntityGuidsBySubmissionGuid(String submissionGuid) throws ServiceException {

    logMethodStart(logger);

    Transaction transaction = null;

    transaction = openTransaction();
    Connection conn = (Connection) transaction.getDatastore();

    Map<String, ChefsSubmssnCrmEntity> allRecordsMap = null;
    try {
      allRecordsMap = chefsDatabaseDao.readCrmEntityGuidsBySubmissionGuid(conn, submissionGuid);
    } catch (DataAccessException e) {
      throw new ServiceException(e);
    }
    List<ChefsSubmssnCrmEntity> chefsSubmissions = new ArrayList<>();
    for (Map.Entry<String, ChefsSubmssnCrmEntity> entry : allRecordsMap.entrySet()) {
      chefsSubmissions.add(entry.getValue());
    }

    logMethodEnd(logger);
    return chefsSubmissions;
  }

}