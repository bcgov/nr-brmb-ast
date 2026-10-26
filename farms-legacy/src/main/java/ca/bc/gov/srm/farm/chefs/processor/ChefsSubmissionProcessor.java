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

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.chefs.ChefsAuthenticationHandler;
import ca.bc.gov.srm.farm.chefs.ChefsConfigurationUtil;
import ca.bc.gov.srm.farm.chefs.ChefsConstants;
import ca.bc.gov.srm.farm.chefs.ChefsFormCredentials;
import ca.bc.gov.srm.farm.chefs.ChefsRestApiDao;
import ca.bc.gov.srm.farm.chefs.database.ChefsSubmissionStatusCodes;
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
import ca.bc.gov.srm.farm.exception.TooManyRequestsException;
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
  private boolean basicBCeIDFormsEnabled;

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
    verifierUserEmail = configUtil.getValue(ConfigurationKeys.CHEFS_VERIFIER_USER_EMAIL);
    
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
  
  
  /**
   * @throws ServiceException if CHEFS rate limited us. Remaining submissions are
   *         left for the next run, since further requests would also be rejected.
   */
  public void processSubmissions() throws ServiceException {

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
          process = checkEnvironment(submissionGuid, submissionResponseStr);
        }

        if( process ) {
          try {
            processSubmission(submissionGuid, submissionResponseStr);
          } catch (ServiceException e) {
            if(TooManyRequestsException.findIn(e) != null) {
              throw e;
            } else if(e.getCause() instanceof JacksonException) {
              handleParseError(submissionGuid, e);
            } else {
              handleSystemError(submissionGuid, e);
            }
          } catch (RuntimeException e) {
            handleSystemError(submissionGuid, e);
          }
        }
        
      } catch (Exception e) {
        if(TooManyRequestsException.findIn(e) != null) {
          throw new ServiceException(String.format("CHEFS rate limit reached processing %s %s submission: %s",
              formTypeCode, formUserType, submissionGuid), e);
        }
        logger.error(String.format("Error processing %s %s submission: %s",
            formTypeCode, formUserType, submissionGuid), e);
      }
    }
  }


  private boolean checkEnvironment(String submissionGuid, String submissionResponseStr) throws ServiceException {
    if(submissionResponseStr == null) {
      // 404. Most likely deleted after the submission list was read.
      // If so, the next run will see it as deleted and skip it.
      logger.warn(String.format("%s %s submission %s not found in CHEFS. It may have been deleted. Skipping.",
          formTypeCode, formUserType, submissionGuid));
      return false;
    }
    return checkEnvironment(parseSubmissionMetaData(submissionResponseStr));
  }


  /**
   * Decides whether this environment should process the submission, before any form-specific
   * processing is done. A submission for another environment, or a Basic BCeID submission while
   * those forms are disabled, is recorded as OTHER_ENV so that it is not fetched again.
   *
   * @return true if the submission should be processed
   */
  public boolean checkEnvironment(SubmissionParentResource<? extends ChefsSubmissionDataResource> submissionMetaData)
      throws ServiceException {

    // Drafts are not processed. Don't record anything until the form is submitted.
    if(Boolean.TRUE.equals(submissionMetaData.getDraft())) {
      return false;
    }

    ChefsSubmissionDataResource data = submissionMetaData.getSubmission().getData();
    if(isForThisEnvironment(data)) {
      return true;
    }

    // An IDIR form's environment is only final once staff complete the Admin Use section.
    // Until then, check it again on the next run.
    if(isBasicBCeIDForm() || checkAdminUseSectionCompleted(data)) {
      recordOtherEnvironment(submissionMetaData.getSubmissionGuid());
    }
    return false;
  }


  private boolean isForThisEnvironment(ChefsSubmissionDataResource data) {
    if(isBasicBCeIDForm()) {
      // Basic BCeID users can't see the Admin Use section, so its Environment field is always blank.
      return basicBCeIDFormsEnabled;
    }
    if( ! usesEnvironmentField() ) {
      return true;
    }

    String webADEEnvironment = getWebADEEnvironment();
    String chefsFormEnvironment = data.getEnvironment();
    logger.debug("webADEEnvironment = {}, chefsFormEnvironment = {}", webADEEnvironment, chefsFormEnvironment);

    return webADEEnvironment.equals(chefsFormEnvironment);
  }


  /**
   * DEV and DLVR share a CHEFS environment, so staff use the Environment field in the
   * Admin Use section to say which one a submission is for. Every submission in the
   * TEST and PROD CHEFS environments is for that environment, so they ignore the field.
   */
  private boolean usesEnvironmentField() {
    String webADEEnvironment = getWebADEEnvironment();
    return ! (PROD_ENVIRONMENT.equals(webADEEnvironment) || TEST_ENVIRONMENT.equals(webADEEnvironment));
  }


  private boolean isBasicBCeIDForm() {
    return formUserType.equals(USER_TYPE_BASIC_BCEID);
  }


  private void recordOtherEnvironment(String submissionGuid) throws ServiceException {
    ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuid);
    if(submissionRec == null) {
      submissionRec = newSubmissionRecord(submissionGuid);
    } else if( ! isOneOf(submissionRec.getSubmissionStatusCode(), SUBMITTED) ) {
      // Leave error statuses alone so that their validation task handling still applies.
      return;
    }
    submissionRec.setSubmissionStatusCode(OTHER_ENV);
    createOrUpdateSubmission(submissionRec);
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
      
      CrmTaskResource existingValidationErrorTask = getValidationErrorTask(submissionGuid);
      
      if (existingValidationErrorTask == null) {
        task = createSystemErrorTask(submissionGuid, e);
        
        updateSubmissionRec(submissionGuid, submissionStatusCode, task, null);
        
      } else {
        logger.debug(submissionStatusCode + " error task already exists: " + existingValidationErrorTask.toString());
        if (existingValidationErrorTask.getStateCode() == CrmConstants.TASK_STATE_CODE_COMPLETED) {
          task = createSystemErrorTask(submissionGuid, e);
          
          updateSubmissionRec(submissionGuid, submissionStatusCode, task, null);
        } else {
          task = existingValidationErrorTask;
        }
      }
      
    } catch(Exception e2) {
      logger.error("Error creating/updating record for submission ID " + submissionGuid + ":", e2);
    }
    
    return task;
  }

  protected CrmTaskResource getValidationErrorTask(String submissionGuid) throws ServiceException {
    ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuid);
    CrmTaskResource existingValidationErrorTask = null;
    
    // It is possible that the submission record has a validationTaskGuid but the task has been deleted.
    // In that case, crmDao.getValidationErrorTask() will return null.
    if(submissionRec != null && submissionRec.getValidationTaskGuid() != null) {
      existingValidationErrorTask = crmDao.getValidationErrorTask(submissionRec.getValidationTaskGuid());
    }
    return existingValidationErrorTask;
  }

  private void updateSubmissionRec(String submissionGuid, String submissionStatusCode, CrmTaskResource validationErrorTask,
      CrmTaskResource mainTask)
      throws ServiceException {
    
    ChefsSubmission submissionRec = chefsDatabaseDao.readSubmissionByGuid(connection, submissionGuid);
    if(submissionRec == null) {
      submissionRec = newSubmissionRecord(submissionGuid);
    }
    if(validationErrorTask != null) {
      submissionRec.setValidationTaskGuid(validationErrorTask.getActivityId());
    }
    if(mainTask != null) {
      submissionRec.setMainTaskGuid(mainTask.getActivityId());
    }
    submissionRec.setSubmissionStatusCode(submissionStatusCode);
    submissionRec = createOrUpdateSubmission(submissionRec);
  }

  protected void setSubmissionInvalid(String submissionGuid, CrmTaskResource task) throws ServiceException {
    updateSubmissionRec(submissionGuid, ChefsSubmissionStatusCodes.INVALID, task, null);
  }

  protected void setSubmissionProcessed(String submissionGuid, CrmTaskResource task) throws ServiceException {
    updateSubmissionRec(submissionGuid, ChefsSubmissionStatusCodes.PROCESSED, null, task);
  }


  protected abstract void processSubmission(String submissionGuid, String submissionResponseStr) throws ServiceException;


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


  protected ChefsSubmission newSubmissionRecord(String submissionGuid ) throws ServiceException {
    ChefsSubmission submissionRec = new ChefsSubmission();
    submissionRec.setSubmissionGuid(submissionGuid);
    submissionRec.setFormTypeCode(formTypeCode);
    submissionRec.setSubmissionStatusCode(SUBMITTED);
    if(formUserType.equals(ChefsConstants.USER_TYPE_BASIC_BCEID)) {
      submissionRec.setBceidFormInd("Y");
    } else {
      submissionRec.setBceidFormInd("N");
    }
    submissionRec = createOrUpdateSubmission(submissionRec);
    
    if( ! submissionRecordMap.containsKey(submissionGuid) ) {
      submissionRecordMap.put(submissionGuid, submissionRec);
    }
    
    return submissionRec;
  }


  protected ChefsSubmission createOrUpdateSubmission(ChefsSubmission submissionRec) throws ServiceException {
    ChefsSubmission result = submissionRec;
    
    try {
      if(submissionRec.getSubmissionId() != null) {
        chefsDatabaseDao.updateSubmission(connection, submissionRec, user);
      } else {
        chefsDatabaseDao.createSubmission(connection, submissionRec, user);
        result = chefsDatabaseDao.readSubmissionByGuid(connection, submissionRec.getSubmissionGuid());
      }
      
      connection.commit();
      
    } catch (DataAccessException | SQLException e) {
      logger.error("Error creating submission: ", e);
      throw new ServiceException(e);
    }
    
    return result;
  }


  protected void updateScenarioSubmissionId(Integer submissionId, Integer scenarioId) throws DataAccessException, SQLException {
    chefsDatabaseDao.updateScenarioSubmissionId(connection, scenarioId, submissionId, user);
    connection.commit();
  }
  
  
  protected abstract String getSystemErrorQueueId() throws ServiceException;


  protected ChefsSubmissionProcessData handleAdminSection(String submissionGuid, ChefsSubmissionDataResource data, ChefsSubmission submissionRecParam) throws ServiceException {
    logMethodStart(logger);
    
    if(isBasicBCeIDForm()) {
      // Basic BCeID users can't see the Admin Use section, so it is always blank. Fill in its values.
      // checkEnvironment() only lets them through when they are enabled,
      // so they are for this environment.
      data.setOrigin(FIELD_VALUE_ORIGIN_EXTERNAL);
      data.setExternalMethod(FIELD_VALUE_METHOD_CHEFS_FORM);
      data.setEnvironment(getWebADEEnvironment());
    }

    // The environment has already been checked by checkEnvironment().
    // If we have not processed this record before, then check if the
    // Admin Use section of the form has been completed by AgriStability staff.
    // If so, then attempt to process the submission. Otherwise, ignore it for now.
    boolean process = checkAdminUseSectionCompleted(data);

    ChefsSubmissionProcessData chefsSubmissionProcessData = new ChefsSubmissionProcessData();
    chefsSubmissionProcessData.setProcess(process);
    chefsSubmissionProcessData.setChefsSubmission(submissionRecParam);

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
        && ( ! usesEnvironmentField() || StringUtils.isNotBlank(env) );
    
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

  protected ChefsSubmissionProcessData shouldProcessSubmission(String submissionGuid, ChefsSubmissionDataResource data, ChefsSubmission submissionRec)
      throws ServiceException {

    ChefsSubmissionProcessData submissionProcessData = new ChefsSubmissionProcessData();
    submissionProcessData.setChefsSubmission(submissionRec);
    
    Collection<String> existingSubmissionGuids = null;
    if(submissionRec == null) {
      existingSubmissionGuids = checkForDuplicate(data);
    }
    
    boolean isDuplicate = existingSubmissionGuids != null && ! existingSubmissionGuids.isEmpty();
    
    String submissionStatusCode = submissionRec == null ? null : submissionRec.getSubmissionStatusCode();

    if(isDuplicate) {
      
      CrmTaskResource validationErrorTask = createValidationErrorTaskForDuplicate(data, existingSubmissionGuids);
      ChefsSubmission newSubmissionRec = newSubmissionRecord(submissionGuid);
      newSubmissionRec.setSubmissionStatusCode(DUPLICATE);
      submissionProcessData.setChefsSubmission(newSubmissionRec);
      submissionProcessData.setValidationErrorTask(validationErrorTask);
      submissionProcessData.setProcess(false);
      createOrUpdateSubmission(newSubmissionRec);
      
    } else if (submissionRec != null && isOneOf(submissionStatusCode, INVALID, SYSTEM_ERROR, PARSE_ERROR)) {
      // If the status is Invalid then check if the validation task has been
      // completed.

      logger.debug(String.format("Submission record found with status %s. Checking if validation task is completed.", submissionStatusCode));

      CrmTaskResource existingValidationTask = crmDao.getValidationErrorTask(submissionRec.getValidationTaskGuid());
      boolean process = existingValidationTask == null ||
          existingValidationTask.getStateCode() == CrmConstants.TASK_STATE_CODE_COMPLETED;
      submissionProcessData.setProcess(process);

      logger.debug("Validation task completed: " + submissionProcessData.getProcess());

    } else if (submissionRec == null || isOneOf(submissionStatusCode, SUBMITTED)) {

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
    Integer participantPin = data.getParticipantPin();
    Integer programYear = data.getParsedProgramYear();
    
    if(participantPin != null && programYear != null) {
      existingSubmissionGuids = chefsDatabaseDao.readSubmissionGuidsForPinAndProgramYear(connection, formTypeCode, participantPin, programYear, submissionGuid);
    }
    
    return existingSubmissionGuids;
  }

  private String getWebADEEnvironment() {
    if(environment == null) {
      environment = configUtil.getValue(ConfigurationKeys.ENVIRONMENT_NAME);
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
  
  /**
   * @return true if this environment should process the submission. Unlike checkEnvironment(), records nothing.
   */
  public boolean isCorrectEnvironment(String submissionResponseStr) throws ServiceException {
    return isForThisEnvironment(parseSubmissionMetaData(submissionResponseStr).getSubmission().getData());
  }

  /**
   * Parses only the fields common to all forms, so that a submission for another environment
   * is never parsed into a form-specific class that it might not fit.
   */
  private SubmissionParentResource<ChefsSubmissionDataResource> parseSubmissionMetaData(String submissionResponseStr)
      throws ServiceException {
    ObjectMapper jsonObjectMapper = new ObjectMapper();
    JavaType parametricType = jsonObjectMapper.getTypeFactory().constructParametricType(SubmissionWrapperResource.class,
        ChefsSubmissionDataResource.class);

    SubmissionWrapperResource<ChefsSubmissionDataResource> submissionWrapper = restDao.parseResource(submissionResponseStr, parametricType);
    return submissionWrapper.getSubmissionMetaData();
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

    Integer participantPin = data.getParticipantPin();
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

  public void setBasicBCeIDFormsEnabled(boolean basicBCeIDFormsEnabled) {
    this.basicBCeIDFormsEnabled = basicBCeIDFormsEnabled;
  }

}
