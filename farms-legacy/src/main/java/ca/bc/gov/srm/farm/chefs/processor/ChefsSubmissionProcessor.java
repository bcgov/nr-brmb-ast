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
package ca.bc.gov.srm.farm.chefs.processor;

import static ca.bc.gov.srm.farm.chefs.ChefsConstants.*;
import static ca.bc.gov.srm.farm.chefs.database.ChefsSubmissionStatusCodes.*;
import static ca.bc.gov.srm.farm.chefs.forms.ChefsFormConstants.*;
import static ca.bc.gov.srm.farm.log.LoggingUtils.*;
import static ca.bc.gov.srm.farm.util.ObjectUtils.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.chefs.ChefsAuthenticationHandler;
import ca.bc.gov.srm.farm.chefs.ChefsConfigurationUtil;
import ca.bc.gov.srm.farm.chefs.ChefsConstants;
import ca.bc.gov.srm.farm.chefs.ChefsFormCredentials;
import ca.bc.gov.srm.farm.chefs.ChefsRestApiDao;
import ca.bc.gov.srm.farm.chefs.resource.ChefsResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.ChefsSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionListItemResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionParentResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionWrapperResource;
import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationUtility;
import ca.bc.gov.srm.farm.crm.CrmConstants;
import ca.bc.gov.srm.farm.crm.CrmRestApiDao;
import ca.bc.gov.srm.farm.crm.resource.CrmAccountResource;
import ca.bc.gov.srm.farm.crm.resource.CrmTaskResource;
import ca.bc.gov.srm.farm.crm.resource.CrmValidationErrorResource;
import ca.bc.gov.srm.farm.dao.CalculatorDAO;
import ca.bc.gov.srm.farm.dao.ChefsDatabaseDAO;
import ca.bc.gov.srm.farm.dao.ReadDAO;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmission;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * @author awilkinson
 */
public abstract class ChefsSubmissionProcessor<T extends ChefsResource> {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private static final String ERROR_TASK_SUBJECT_FORMAT = "%s System Error for Submission GUID: %s";
  private static final String ERROR_TASK_DESCRIPTION_PREFIX_FORMAT =
      "A %s form was submitted but an unexpected system error occurred while processing:\n\n%s";
  private static final String DUPLICATE_SUBMISSION_TASK_SUBJECT_FORMAT = "Duplicate form: %d %s %d";

  private static final String INVALID_FORM_TASK_DESCRIPTION_PREFIX_FORMAT =
      "%s %s form was submitted but has previous submissions for this PIN and program year:\n\n"
      + "%s\n"
      + "Environment: %s\n";
  
  protected ConfigurationUtility configUtil;
  protected ChefsConfigurationUtil chefsConfig;
  protected String submissionsUrl;
  protected ChefsRestApiDao restDao;
  protected CrmRestApiDao crmDao;
  protected ChefsDatabaseDAO chefsDatabaseDao = new ChefsDatabaseDAO();
  protected ReadDAO readDAO;
  protected CalculatorDAO calculatorDAO = new CalculatorDAO();
  protected Connection connection;
  
  protected String formTypeCode;
  protected String formShortName;
  protected String formLongName;
  protected String formUserType;
  protected String verifierUserEmail;
  
  protected List<SubmissionListItemResource> submissionItems;
  protected Map<String, SubmissionListItemResource> itemResourceMap;
  protected Map<String, ChefsSubmission> submissionRecordMap;
  
  private String environment;
  private final boolean basicBCeIDFormsEnabled;

  protected String user = SYSTEM_USER;


  protected ChefsSubmissionProcessor(
      String formTypeCode,
      String formShortName,
      String formLongName,
      String formUserType,
      Connection connection) {
    
    this.connection = connection;
    
    this.formTypeCode = formTypeCode;
    this.formShortName = formShortName;
    this.formLongName = formLongName;
    this.formUserType = formUserType;
    
    configUtil = ConfigurationUtility.getInstance();
    chefsConfig = ChefsConfigurationUtil.getInstance();
    ChefsFormCredentials formCredentials = chefsConfig.getFormCredentials(formTypeCode, formUserType);
    submissionsUrl = chefsConfig.getSubmissionsUrl(formCredentials.getFormId());
    basicBCeIDFormsEnabled = configUtil.getBoolean(ConfigurationKeys.CHEFS_BASIC_BCEID_FORMS_ENABLED);
    verifierUserEmail = configUtil.getValue(ConfigurationKeys.FIFO_VERIFIER_USER_EMAIL);
    
    restDao = new ChefsRestApiDao(new ChefsAuthenticationHandler(formCredentials));
    crmDao = new CrmRestApiDao();
    readDAO = new ReadDAO(connection);
  }

  public void loadSubmissionsFromChefs() throws ServiceException {
    
    submissionItems = restDao.getResourceList(submissionsUrl, SubmissionListItemResource.class);
    
    submissionItems = submissionItems.stream()
        .sorted((s1, s2) -> s1.getCreatedAt().compareTo(s2.getCreatedAt()))
        .collect(Collectors.toList());

    itemResourceMap = new HashMap<>();
    for(SubmissionListItemResource itemResource : submissionItems) {
      String submissionGuid = itemResource.getSubmissionGuid();
      itemResourceMap.put(submissionGuid, itemResource);
    }
  }
  
  
  public void loadSubmissionsFromDatabase() throws ServiceException {
    try {
      submissionRecordMap = chefsDatabaseDao.readSubmissionsByGuid(connection, itemResourceMap.keySet());
    } catch (DataAccessException e) {
      logger.error("Error reading submissions from database: ", e);
      throw new ServiceException(e);
    }
  }
  
  
  public void processSubmissions() {
    
    for(SubmissionListItemResource item : submissionItems) {
      
      String submissionGuid = item.getSubmissionGuid();
      
      try {
        
        String submissionUrl = chefsConfig.getSubmissionUrl(submissionGuid);
        String submissionResponseStr = null ;
        
        // Skip deleted submissions
        boolean process = ! item.isDeleted();
        if(process) {
          ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuid);
          
          if(submissionRec != null) {
            String submissionStatusCode = submissionRec.getSubmissionStatusCode();
            process = ! isOneOf(submissionStatusCode, CANCELLED, PROCESSED, OTHER_ENV, DUPLICATE);
          }
        }
        if(process) {
          submissionResponseStr = restDao.get(submissionUrl);
          process = isCorrectEnvironment(submissionResponseStr);
        }
        
        if( process && submissionResponseStr != null ) {
          processSubmission(submissionGuid, submissionResponseStr);
        }
        
      } catch (Exception e) {
        logger.error(String.format("Error processing %s %s submission: %s",
            formTypeCode, formUserType, submissionGuid), e);
      }
    }
  }


  protected CrmTaskResource handleParseError(String submissionGuid, Exception e) {
    return handleError(submissionGuid, e, PARSE_ERROR);
  }

  protected CrmTaskResource handleSystemError(String submissionGuid, Exception e) {
    return handleError(submissionGuid, e, SYSTEM_ERROR);
  }

  private CrmTaskResource handleError(String submissionGuid, Exception e, String submissionStatusCode) {
    logger.error(submissionStatusCode + " processing submission ID " + submissionGuid + ":", e);
    
    CrmTaskResource task = null;
    try {
      
      CrmTaskResource existingValidationErrorTask = crmDao.getValidationErrorBySubmissionId(submissionGuid);
      if (existingValidationErrorTask == null) {
        task = createSystemErrorTask(submissionGuid, e);
        
        updateSubmissionRec(submissionStatusCode, submissionGuid, task);
        
      } else {
        logger.debug(submissionStatusCode + " error task already exists: " + existingValidationErrorTask.toString());
        if (existingValidationErrorTask.getStateCode() == CrmConstants.TASK_STATE_CODE_COMPLETED) {
          task = createSystemErrorTask(submissionGuid, e);
          
          updateSubmissionRec(submissionStatusCode, submissionGuid, task);
        } else {
          task = existingValidationErrorTask;
        }
      }
      
    } catch(Exception e2) {
      logger.error("Error creating/updating record for submission ID " + submissionGuid + ":", e2);
    }
    
    return task;
  }

  private void updateSubmissionRec(String submissionStatusCode, String submissionGuid, CrmTaskResource task)
      throws DataAccessException, SQLException {
    ChefsSubmission submissionRec = chefsDatabaseDao.readSubmissionByGuid(connection, submissionGuid);
    if(submissionRec == null) {
      submissionRec = newSubmissionRecord(submissionGuid);
    }
    submissionRec.setValidationTaskGuid(task.getActivityId());
    submissionRec.setSubmissionStatusCode(submissionStatusCode);
    submissionRec = createOrUpdateSubmission(submissionRec);
  }


  protected abstract void processSubmission(String submissionGuid, String submissionResponseStr);


  private CrmTaskResource createSystemErrorTask(String submissionGuid, Exception e) throws ServiceException {
    
    String subject = StringUtils.formatWithNullAsEmptyString(ERROR_TASK_SUBJECT_FORMAT, formShortName, submissionGuid);
    String description = StringUtils.formatWithNullAsEmptyString(ERROR_TASK_DESCRIPTION_PREFIX_FORMAT, formLongName, e.toString());
    
    CrmValidationErrorResource task = new CrmValidationErrorResource();
    task.setAccountIdParameter(null);
    task.setSubject(subject);
    task.setDescription(description);
    StringBuilder chefsSubmissionUrl = new StringBuilder();
    chefsSubmissionUrl.append(configUtil.getValue(ConfigurationKeys.CHEFS_UI_URL));
    chefsSubmissionUrl.append(URL_VIEW_CHEFS_SUBMISSION_FORM);
    chefsSubmissionUrl.append(submissionGuid);
    task.setCr4dd_chefsurl(chefsSubmissionUrl.toString());
    
    return crmDao.createValidationErrorTask(task, getSystemErrorQueueId());
  }


  protected SubmissionParentResource<T> getSubmissionMetaData(String submissionResponseStr, Class<T> clazz) throws ServiceException {

    ObjectMapper jsonObjectMapper = new ObjectMapper();
    JavaType parametricType = jsonObjectMapper.getTypeFactory().constructParametricType(SubmissionWrapperResource.class,
        clazz);
    
    SubmissionWrapperResource<T> submissionWrapper = null;
    submissionWrapper = restDao.parseResource(submissionResponseStr, parametricType);
    
    SubmissionParentResource<T> submissionMetaData = submissionWrapper.getSubmissionMetaData();
    return submissionMetaData;
  }


  protected ChefsSubmission newSubmissionRecord(String submissionGuid ) {
    ChefsSubmission submissionRec = new ChefsSubmission();
    submissionRec.setSubmissionGuid(submissionGuid);
    submissionRec.setFormTypeCode(formTypeCode);
    submissionRec.setSubmissionStatusCode(SUBMITTED);
    if(formUserType.equals(ChefsConstants.USER_TYPE_BASIC_BCEID)) {
      submissionRec.setBceidFormInd("Y");
    } else {
      submissionRec.setBceidFormInd("N");
    }
    return submissionRec;
  }


  protected ChefsSubmission createOrUpdateSubmission(ChefsSubmission submissionRec) throws DataAccessException, SQLException {
    ChefsSubmission result = submissionRec;
    
    if(submissionRec.getSubmissionId() != null) {
      chefsDatabaseDao.updateSubmission(connection, submissionRec, user);
    } else {
      chefsDatabaseDao.createSubmission(connection, submissionRec, user);
      result = chefsDatabaseDao.readSubmissionByGuid(connection, submissionRec.getSubmissionGuid());
    }
    
    connection.commit();
    
    return result;
  }
  
  protected abstract String getSystemErrorQueueId() throws ServiceException;


  protected ChefsSubmissionProcessData handleAdminSection(String submissionGuid, ChefsSubmissionDataResource data, ChefsSubmission submissionRecParam) throws ServiceException {
    logMethodStart(logger);
    
    ChefsSubmission submissionRec = submissionRecParam;
    
    if(formUserType.equals(USER_TYPE_BASIC_BCEID)) {
      data.setOrigin(FIELD_VALUE_ORIGIN_EXTERNAL);
      
      data.setExternalMethod(FIELD_VALUE_METHOD_CHEFS_FORM);
      
      // If Basic BCeID Forms are enabled for this environment then set the environment
      // to the current one so that submissions will be processed.
      // Once testing in DEV, DLVR, and TEST has been completed,
      // this setting will be turned off so that the forms are only processed in PROD.
      if(basicBCeIDFormsEnabled) {
        data.setEnvironment(getWebADEEnvironment());
      } else {
        // We just need this to be a value that's different
        // from this environment so that it will be treated as
        // belonging to a different environment.
        data.setEnvironment("formTypeDisabled");
      }
    }
    
    boolean process = false;
    // If we have not processed this record before, then check if the
    // Admin Use section of the form has been completed by AgriStability staff.
    // If so, then attempt to process the submission. Otherwise, ignore it for now.
    boolean adminCompleted = checkAdminUseSectionCompleted(data);
    
    if(adminCompleted) {
      
      boolean isForThisEnvironment = checkIsForThisEnvironment(data);
      if(isForThisEnvironment) {
        process = true;
      } else {
        if(submissionRec == null) {
          submissionRec = newSubmissionRecord(submissionGuid);
        }
        submissionRec.setSubmissionStatusCode(OTHER_ENV);
        try {
          submissionRec = createOrUpdateSubmission(submissionRec);
        } catch (SQLException e) {
          throw new ServiceException(e);
        }
      }
    }
    
    ChefsSubmissionProcessData chefsSubmissionProcessData = new ChefsSubmissionProcessData();
    chefsSubmissionProcessData.setProcess(process);
    chefsSubmissionProcessData.setChefsSubmission(submissionRec);

    logMethodEnd(logger, process);
    return chefsSubmissionProcessData;
  }


  protected boolean checkAdminUseSectionCompleted(ChefsSubmissionDataResource data) {
    logMethodStart(logger);
    
    String origin = data.getOrigin();
    String method = getMethod(data);
    String env = data.getEnvironment();
    
    boolean result = StringUtils.isNotBlank(origin)
        && StringUtils.isNotBlank(method)
        && StringUtils.isNotBlank(env);
    
    logMethodEnd(logger, result);
    return result;
  }
  

  protected String getMethod(ChefsSubmissionDataResource data) {
    String method;
    if(FIELD_VALUE_ORIGIN_INTERNAL.equals(data.getOrigin())) {
      method = data.getInternalMethod();
    } else {
      method = data.getExternalMethod();
    }
    return method;
  }

  protected boolean checkIsForThisEnvironment(ChefsSubmissionDataResource data) {
    String webADEEnvironment = getWebADEEnvironment();
    String formEnvironment = data.getEnvironment();
    
    logger.debug("webADEEnvironment: " + webADEEnvironment);
    logger.debug("formEnvironment  : " + formEnvironment);
    
    boolean result = webADEEnvironment.equals(formEnvironment);
    logger.debug("isForThisEnvironment: " + result);
    
    return result;
  }
  
  protected ChefsSubmissionProcessData shouldProcessSubmission(String submissionGuid, ChefsSubmissionDataResource data, ChefsSubmission submissionRec)
      throws ServiceException {

    ChefsSubmissionProcessData submissionProcessData = new ChefsSubmissionProcessData();
    submissionProcessData.setChefsSubmission(submissionRec);
    
    Collection<String> existingSubmissionGuids = null;
    if(submissionRec == null) {
      existingSubmissionGuids = checkForDuplicate(data);
    }
    
    boolean isDuplicate = existingSubmissionGuids != null && ! existingSubmissionGuids.isEmpty();

    if(isDuplicate) {
      
      CrmTaskResource validationErrorTask = createValidationErrorTaskForDuplicate(data, existingSubmissionGuids);
      ChefsSubmission newSubmissionRec = newSubmissionRecord(submissionGuid);
      newSubmissionRec.setSubmissionStatusCode(DUPLICATE);
      submissionProcessData.setChefsSubmission(newSubmissionRec);
      submissionProcessData.setValidationErrorTask(validationErrorTask);
      submissionProcessData.setProcess(false);
      
      try {
        createOrUpdateSubmission(newSubmissionRec);
      } catch (SQLException e) {
        logger.error("SQLException: ", e);
        throw new ServiceException(e);
      }
      
    } else if (submissionRec != null && isOneOf(submissionRec.getSubmissionStatusCode(), INVALID, SYSTEM_ERROR, PARSE_ERROR)) {
      // If the status is Invalid then check if the validation task has been
      // completed.

      logger.debug("Submission record found with status INVALID. Checking if validation task is completed.");

      CrmTaskResource existingValidationTask = crmDao.getValidationErrorTask(submissionRec.getValidationTaskGuid());
      submissionProcessData.setProcess(existingValidationTask.getStateCode() == CrmConstants.TASK_STATE_CODE_COMPLETED);

      logger.debug("Validation task completed: " + submissionProcessData.getProcess());

    } else if (submissionRec == null || isOneOf(submissionRec.getSubmissionStatusCode(), SUBMITTED)) {

      submissionProcessData = handleAdminSection(submissionGuid, data, submissionRec);

    } else {
      // Other statuses are Processed, Cancelled, Not for this environment, so
      // this submission is already processed.
      submissionProcessData.setProcess(false);
    }

    return submissionProcessData;
  }

  private Collection<String> checkForDuplicate(ChefsSubmissionDataResource data) throws DataAccessException {
    
    Collection<String> existingSubmissionGuids = null;
    
    String submissionGuid = data.getSubmissionGuid();
    Integer participantPin = data.getParsedParticipantPin();
    Integer programYear = data.getParsedProgramYear();
    
    if(participantPin != null && programYear != null) {
      existingSubmissionGuids = chefsDatabaseDao.readSubmissionGuidsForPinAndProgramYear(connection, formTypeCode, participantPin, programYear, submissionGuid);
    }
    
    return existingSubmissionGuids;
  }

  private String getWebADEEnvironment() {
    if(environment == null) {
      environment = configUtil.getEnvironment();
      logger.info("getWebADEEnvironment: " + environment);
    }
    return environment;
  }


  public List<SubmissionListItemResource> getSubmissionItems() {
    return submissionItems;
  }

  public void setSubmissionItems(List<SubmissionListItemResource> submissionItems) {
    this.submissionItems = submissionItems;
  }

  public Map<String, SubmissionListItemResource> getItemResourceMap() {
    return itemResourceMap;
  }

  public void setItemResourceMap(Map<String, SubmissionListItemResource> itemResourceMap) {
    this.itemResourceMap = itemResourceMap;
  }

  public Map<String, ChefsSubmission> getSubmissionRecordMap() {
    return submissionRecordMap;
  }

  public void setSubmissionRecordMap(Map<String, ChefsSubmission> submissionRecordMap) {
    this.submissionRecordMap = submissionRecordMap;
  }

  public void setUser(String user) {
    this.user = user;
  }
  
  public boolean isCorrectEnvironment(String submissionResponseStr) throws ServiceException {
    
    String webADEEnvironment = getWebADEEnvironment();
    
    final String PROD_ENVIRONMENT = "PROD";
    final String TEST_ENVIRONMENT = "TEST";
    
    if (webADEEnvironment.equals(PROD_ENVIRONMENT) || webADEEnvironment.equals(TEST_ENVIRONMENT)) {
      return true;
    }
    
    ObjectMapper jsonObjectMapper = new ObjectMapper();
    JavaType parametricType = jsonObjectMapper.getTypeFactory().constructParametricType(SubmissionWrapperResource.class,
        ChefsSubmissionDataResource.class);
    
    SubmissionWrapperResource<ChefsSubmissionDataResource> submissionWrapper = null;
    submissionWrapper = restDao.parseResource(submissionResponseStr, parametricType);

    ChefsSubmissionDataResource adminUseSubmissionDataResource = submissionWrapper.getSubmissionMetaData().getSubmission().getData();
    String chefsFormEnvironment = adminUseSubmissionDataResource.getEnvironment();
    logger.debug("webADEEnvironment = {}, chefsFormEnvironment = {}", webADEEnvironment, chefsFormEnvironment);
    
    return webADEEnvironment.equals(chefsFormEnvironment);
  }

  public String getFormTypeCode() {
    return formTypeCode;
  }

  public String getFormUserType() {
    return formUserType;
  }

  protected void validateValuesMatch(String fieldName, String chefsValue, String farmValue, List<String> validationErrors) {
    String farmValueTrimmed = farmValue;
    if(farmValue != null ) {
      farmValueTrimmed = farmValue.trim();
    }
    String chefsValueTrimmed = chefsValue;
    if(chefsValue != null ) {
      chefsValueTrimmed = chefsValue.trim();
    }
    if (farmValueTrimmed == null || !farmValueTrimmed.equals(chefsValueTrimmed)) {
      validationErrors.add("Field \"" + fieldName + "\" with value \"" + chefsValue + "\" does not match BCFARMS: \""
          + farmValue + "\".");
    }
  }

  protected CrmAccountResource getCrmAccountByParticipantPin(Integer participantPin) throws ServiceException {
    return crmDao.getAccountByPin(participantPin);
  }

  private CrmTaskResource createValidationErrorTaskForDuplicate(ChefsSubmissionDataResource data,
      Collection<String> existingSubmissionGuids) throws ServiceException {

    Integer participantPin = data.getParsedParticipantPin();
    Integer programYear = data.getParsedProgramYear();
    String method = getMethod(data);
    
    StringBuilder submissionsText = new StringBuilder();
    submissionsText.append("Form submissions of this type have been previously submitted for this PIN and program year: ");

    for (String submissionGuid : existingSubmissionGuids) {
      submissionsText.append(submissionGuid);
      submissionsText.append("\n");
    }

    String subject = String.format(DUPLICATE_SUBMISSION_TASK_SUBJECT_FORMAT, programYear,
        formShortName, participantPin, programYear);

    String description = StringUtils.formatWithNullAsEmptyString(INVALID_FORM_TASK_DESCRIPTION_PREFIX_FORMAT,
        formUserType, formLongName, submissionsText, data.getEnvironment());

    CrmAccountResource crmAccount = getCrmAccountByParticipantPin(participantPin);
    String accountId = null;
    if (crmAccount != null) {
      accountId = crmAccount.getAccountid();
    }

    CrmValidationErrorResource task = new CrmValidationErrorResource();
    task.setAccountIdParameter(accountId);
    task.setSubject(subject);
    task.setDescription(description);
    task.setCr4dd_method(method);
    task.setCr4dd_origin(data.getOrigin());
    
    StringBuilder chefsSubmissionUrl = new StringBuilder();
    chefsSubmissionUrl.append(configUtil.getValue(ConfigurationKeys.CHEFS_UI_URL));
    chefsSubmissionUrl.append(URL_VIEW_CHEFS_SUBMISSION_FORM);
    chefsSubmissionUrl.append(data.getSubmissionGuid());
    task.setCr4dd_chefsurl(chefsSubmissionUrl.toString());
    
    String queueId = getValidationQueueId();
    return crmDao.createValidationErrorTask(task, queueId);
  }

  protected abstract String getValidationQueueId() throws ServiceException;

  public String getFormShortName() {
    return formShortName;
  }

  public String getFormLongName() {
    return formLongName;
  }
  
}
