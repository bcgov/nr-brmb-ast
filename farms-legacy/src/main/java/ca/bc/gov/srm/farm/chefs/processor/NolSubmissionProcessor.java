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
import static ca.bc.gov.srm.farm.chefs.forms.ChefsFormConstants.*;
import static ca.bc.gov.srm.farm.chefs.forms.NolFormConstants.*;
import static ca.bc.gov.srm.farm.log.LoggingUtils.*;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JacksonException;

import ca.bc.gov.srm.farm.chefs.database.ChefsFormTypeCodes;
import ca.bc.gov.srm.farm.chefs.database.ChefsSubmissionStatusCodes;
import ca.bc.gov.srm.farm.chefs.resource.nol.NolSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionParentResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionResource;
import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.crm.CrmConstants;
import ca.bc.gov.srm.farm.crm.resource.CrmAccountAnnotationResource;
import ca.bc.gov.srm.farm.crm.resource.CrmAccountResource;
import ca.bc.gov.srm.farm.crm.resource.CrmBenefitResource;
import ca.bc.gov.srm.farm.crm.resource.CrmListResource;
import ca.bc.gov.srm.farm.crm.resource.CrmNolTaskResource;
import ca.bc.gov.srm.farm.crm.resource.CrmQueueResource;
import ca.bc.gov.srm.farm.crm.resource.CrmTaskResource;
import ca.bc.gov.srm.farm.crm.resource.CrmValidationErrorResource;
import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.ScenarioMetaData;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmission;
import ca.bc.gov.srm.farm.domain.codes.ParticipantClassCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.exception.TooManyRequestsException;
import ca.bc.gov.srm.farm.service.CdogsService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.IOUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * @author awilkinson
 */
public class NolSubmissionProcessor extends ChefsSubmissionProcessor<NolSubmissionDataResource> {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  private final CdogsService cdogsService = ServiceFactory.getCdogsService();
  
  private static final String VALID_FORM_TASK_SUBJECT_FORMAT = "%d %s %d";
  
  private static final String VALID_FORM_TASK_DESCRIPTION_FORMAT =
      "Primary Farming Activity: %s\n"
      + "Income Decrease Details: %s\n"
      + "Expense Increase Details: %s";
  
  private static final String INVALID_FORM_TASK_SUBJECT_FORMAT = "%d %s %d";
  
  private static final String INVALID_FORM_TASK_DESCRIPTION_PREFIX_FORMAT =
      "%s %s form was submitted but has validation errors:\n\n"
      + "%s\n"
      + "Participant Name: %s\n"
      + "Telephone: %s\n"
      + "Email: %s\n"
      + "Participant Type: %s\n"
      + "%s: %s\n" // SIN / Business Number
      + "Primary Farming Activity: %s";
  
  public NolSubmissionProcessor(Connection connection, String formUserType) {
    super(ChefsFormTypeCodes.NOL, FORM_SHORT_NAME, FORM_LONG_NAME, formUserType, connection);
  }
  
  private String corporateQueueId;
  private String individualQueueId;
  private String validationQueueId;

  @Override
  protected void processSubmission(String submissionGuid, String submissionResponseStr) {
    logMethodStart(logger);
    
    CrmTaskResource task = null;
    
    try {
      SubmissionParentResource<NolSubmissionDataResource> submissionMetaData = getSubmissionMetaData(submissionResponseStr, NolSubmissionDataResource.class);
      
      if( ! submissionMetaData.getDraft() ) {
        task = processSubmission(submissionMetaData);
      }
    } catch (ServiceException e) {
      if(e.getCause() instanceof TooManyRequestsException) {
        logger.error("TooManyRequestsException: ", e);
      } else if(e.getCause() instanceof JacksonException) {
        task = handleParseError(submissionGuid, e);
      } else {
        task = handleSystemError(submissionGuid, e);
      }
    } 
    
    logMethodEnd(logger, task);
  }
  

  public CrmTaskResource processSubmission(SubmissionParentResource<NolSubmissionDataResource> submissionMetaData) throws ServiceException {
    logMethodStart(logger);
    
    CrmTaskResource newTask = null;
    
    SubmissionResource<NolSubmissionDataResource> submission = submissionMetaData.getSubmission();
    String submissionGuid = submissionMetaData.getSubmissionGuid();
    NolSubmissionDataResource data = submission.getData();
    data.setSubmissionGuid(submissionGuid);

    ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuid);
    
    Integer participantPin = getParticipantPin(data);
    Integer programYear = getProgramYear(data);
    data.setParsedParticipantPin(participantPin);
    data.setParsedProgramYear(programYear);
    
    ChefsSubmissionProcessData chefsSubmissionProcessData = shouldProcessSubmission(submissionGuid, data, submissionRec);

    if ( ! chefsSubmissionProcessData.getProcess() ) {
      newTask = chefsSubmissionProcessData.getValidationErrorTask();
      return newTask;
    }
    
    
    submissionRec = chefsSubmissionProcessData.getChefsSubmission();
    if(submissionRec == null) {
      submissionRec = newSubmissionRecord(submissionGuid);
    }
    
    try {
      Client client = null;
      CrmAccountResource crmAccount = null;
      if(participantPin != null) {
        client = readDAO.readClient(participantPin);
        crmAccount = crmDao.getAccountByPin(participantPin);
      }
      
      List<String> validationErrors = validate(data, client, crmAccount);
      boolean hasErrors = ! validationErrors.isEmpty();
      
      if(hasErrors) {
        CrmTaskResource existingValidationErrorTask = crmDao.getValidationErrorBySubmissionId(submissionGuid);
        if (existingValidationErrorTask == null) {
          logger.debug("Creating validation error task...");
          newTask = createValidationErrorTask(crmAccount, data, validationErrors);
        } else {
          logger.debug("Validation error task already exists: " + existingValidationErrorTask.toString());
          if (existingValidationErrorTask.getStateCode() == CrmConstants.TASK_STATE_CODE_COMPLETED) {
            newTask = createValidationErrorTask(crmAccount, data, validationErrors);
          } else {
            newTask = existingValidationErrorTask;
          }
        }
        submissionRec.setSubmissionStatusCode(ChefsSubmissionStatusCodes.INVALID);
        submissionRec.setValidationTaskGuid(newTask.getActivityId());
        submissionRec = createOrUpdateSubmission(submissionRec);
        
      } else {
        // Validation passed
        
        logger.debug("Creating valid NOL task...");
        newTask = createValidNolTask(data, participantPin, crmAccount);
        
        uploadNolFileToAccount(submissionGuid, newTask.getAccountId());
        
        submissionRec.setSubmissionStatusCode(ChefsSubmissionStatusCodes.PROCESSED);
        submissionRec.setMainTaskGuid(newTask.getActivityId());
        submissionRec = createOrUpdateSubmission(submissionRec);
        
        logger.debug("Creating NOL scenario...");
          assert client != null;
          createNolScenario(client, programYear, submissionRec);
      }
      
    } catch (SQLException e) {
      logger.error("Unexpected error: ", e);
      throw new ServiceException(e);
    }
    
    logMethodEnd(logger);
    return newTask;
  }


  private void createNolScenario(Client client, Integer programYear,
      ChefsSubmission submissionRec) throws ServiceException {
    
    Integer participantPin = client.getParticipantPin();
    Integer submissionId = submissionRec.getSubmissionId();
    String applicationVersion = configUtil.getValue(ConfigurationKeys.APPLICATION_VERSION);
    
    createProgramYearIfNeeded(programYear, participantPin);
    
    try {
      
      List<ScenarioMetaData> programYearMetadata = readDAO.readProgramYearMetadata(participantPin, programYear);
      ScenarioMetaData existingNolScenarioMetadata = ScenarioUtils.findNolScenario(programYearMetadata, programYear);
      
      if(existingNolScenarioMetadata == null) {
        
        ScenarioMetaData receivedScenario = findLatestReceivedScenario(programYearMetadata, programYear);
        Integer receivedScenarioId = receivedScenario.getScenarioId();
        
        Integer scenarioNumber = calculatorDAO.saveScenarioAsNew(
          connection,
          receivedScenarioId,
          ScenarioTypeCodes.USER,
          ScenarioCategoryCodes.NOL,
          applicationVersion,
          user);
        
        connection.commit();
        
        List<ScenarioMetaData> updatedProgramYearMetadata = readDAO.readProgramYearMetadata(participantPin, programYear);
        ScenarioMetaData nolScenario = ScenarioUtils.findScenarioByYearAndNumber(updatedProgramYearMetadata, programYear, scenarioNumber);
        Integer nolScenarioId = nolScenario.getScenarioId();
        chefsDatabaseDao.updateScenarioSubmissionId(connection, receivedScenarioId, submissionId, user);
        chefsDatabaseDao.updateScenarioSubmissionId(connection, nolScenarioId, submissionId, user);
        
      } else {
        
        Integer nolScenarioId = existingNolScenarioMetadata.getScenarioId();
        Integer existingSubmissionId = existingNolScenarioMetadata.getChefsFormSubmissionId();
        if(existingSubmissionId == null) {
          chefsDatabaseDao.updateScenarioSubmissionId(connection, nolScenarioId, submissionId, user);
        } else if( ! existingSubmissionId.equals(submissionId)) {
          logger.warn(String.format("For CHEFS submission ID %d, an NOL scenario already exists with a different submission ID: %d.",
              submissionId, existingSubmissionId));
        }
        
      }
      
      connection.commit();
      
    } catch (SQLException | DataAccessException e) {
      logger.error("Unexpected error: ", e);
      throw new ServiceException(e);
    }
    
  }


  private void createProgramYearIfNeeded(Integer programYear, Integer participantPin) throws ServiceException {
    try {
      List<ScenarioMetaData> programYearMetadata = readDAO.readProgramYearMetadata(participantPin, programYear);
      boolean programYearExists = checkProgramYearExists(programYearMetadata, programYear);
      if( ! programYearExists ) {
        logger.debug("Program year " + programYear + " does not exist for PIN " + participantPin + ". Creating...");
        
        calculatorDAO.createYear(connection, participantPin, programYear, 1, ScenarioTypeCodes.CHEF,
            ScenarioCategoryCodes.CHEF_NOL, user);
        connection.commit();
      }
      
    } catch (SQLException e) {
      logger.error("Unexpected error: ", e);
      throw new ServiceException(e);
    }
  }


  private boolean checkProgramYearExists(List<ScenarioMetaData> programYearMetadata, Integer programYear) {
      return programYearMetadata
        .stream()
        .anyMatch(y -> y.getProgramYear().equals(programYear));
  }


  private ScenarioMetaData findLatestReceivedScenario(List<ScenarioMetaData> programYearMetadata, Integer programYear) {
      return programYearMetadata
        .stream()
        .filter(y -> y.getProgramYear().equals(programYear)
            && y.categoryIsOneOf(
                ScenarioCategoryCodes.LOCAL_DATA_ENTRY,
                ScenarioCategoryCodes.CRA_RECEIVED,
                ScenarioCategoryCodes.NEW_CRA_DATA,
                ScenarioCategoryCodes.CHEF_RECEIVED,
                ScenarioCategoryCodes.CHEF_ADJ,
                ScenarioCategoryCodes.CHEF_CN,
                ScenarioCategoryCodes.CHEF_INTRM,
                ScenarioCategoryCodes.CHEF_SUPP,
                ScenarioCategoryCodes.CHEF_NPP,
                ScenarioCategoryCodes.CHEF_NOL) 
            && y.stateIsOneOf(ScenarioStateCodes.RECEIVED))
        .max(Comparator.comparing(ScenarioMetaData::getProgramYearVersion))
        .orElse(null);
  }
  

  private CrmNolTaskResource createValidNolTask(NolSubmissionDataResource data, Integer participantPin, CrmAccountResource crmAccount) throws ServiceException {
    
    Integer programYear = getProgramYear(data);
    String method = getMethod(data);
    String primaryFarmingActivity = getPrimaryFarmingActivity(data);
    
    String subject = StringUtils.formatWithNullAsEmptyString(VALID_FORM_TASK_SUBJECT_FORMAT,
        programYear,
        FORM_SHORT_NAME,
        participantPin);
    String description = StringUtils.formatWithNullAsEmptyString(VALID_FORM_TASK_DESCRIPTION_FORMAT,
        primaryFarmingActivity,
        data.getIncomeDecreaseDetails(),
        data.getExpenseIncreaseDetails());
    
    String queueId = getQueueIdForParticipantType(data.getParticipantType());

    String accountId = null;
    CrmBenefitResource benefit = null;
    if(crmAccount != null) {
      accountId = crmAccount.getAccountid();
      benefit = getBenefitByAccountIdAndProgramYear(accountId, programYear);
    }
    
    CrmNolTaskResource task = new CrmNolTaskResource();
    task.setSubject(subject);
    task.setDescription(description);
    task.setCr4dd_incomedecreasedescription(data.getIncomeDecreaseDetails());
    task.setCr4dd_expenseincreasedescription(data.getExpenseIncreaseDetails());
    task.setCr4dd_method(method);
    task.setCr4dd_origin(data.getOrigin());
    task.setCr4dd_businessnumber(data.getBusinessNumber());
    task.setCr4dd_sin(data.getSinNumber());
    task.setCr4dd_primaryfarmingactivity(data.getPrimaryFarmingActivity());
    task.setCr4dd_participanttype(data.getParticipantType());
    task.setAccountIdParameter(accountId);
    
    if (benefit != null) {
      task.setVsi_benefit(benefit.getVsi_benefitid());
    }
    return crmDao.createValidNolTask(task, queueId);
  }
  

  private String getQueueIdForParticipantType(String participantType) throws ServiceException {
    String queueId;
    if(FIELD_VALUE_PARTICIPANT_TYPE_INDIVIDUAL.equals(participantType)) {
      queueId = getIndividualQueueId();
    } else {
      queueId = getCorporateQueueId();
    }
    return queueId;
  }
  

  private List<String> validate(NolSubmissionDataResource data, Client client, CrmAccountResource crmAccount) {
    logMethodStart(logger);
    
    List<String> validationErrors = new ArrayList<>();
    
    validateRequiredField(validationErrors, FIELD_NAME_AGRISTABILITY_PIN, getParticipantPin(data));
    validateRequiredField(validationErrors, FIELD_NAME_PARTICIPANT_TYPE, data.getParticipantType());
    
    if(isIndividual(data)) {
      validateRequiredField(validationErrors, FIELD_NAME_SIN_NUMBER, data.getSinNumber());
    } else if(isCorporation(data)) {
      validateRequiredField(validationErrors, FIELD_NAME_BUSINESS_NUMBER, data.getBusinessNumber());
    }
    
    if(crmAccount == null) {
      validationErrors.add("PIN not found in CRM.");
    }
    
    if(client == null) {
      validationErrors.add("PIN not found in BCFARMS.");
      
    } else if(validationErrors.isEmpty()) {
      
      if(client.getParticipantClassCode().equals(ParticipantClassCodes.INDIVIDUAL)) {
        
        if(isIndividual(data)) {
          validateValuesMatch(FIELD_NAME_SIN_NUMBER, data.getSinNumber(), client.getSin(), validationErrors);
        } else {
          validationErrors.add("The participant type \"Corporation\" on the form does not match BCFARMS.");
        }
        
      } else if(client.getParticipantClassCode().equals(ParticipantClassCodes.CORPORATION)) {
        
        if(isCorporation(data)) {
          validateBusinessNumbersMatch(data.getBusinessNumber(), client.getBusinessNumber(), validationErrors);
        } else {
          validationErrors.add("The participant type \"Individual\" on the form does not match BCFARMS.");
        }
      }
    }
    
    logMethodEnd(logger);
    return validationErrors;
  }


  private boolean isIndividual(NolSubmissionDataResource data) {
    return FIELD_VALUE_PARTICIPANT_TYPE_INDIVIDUAL.equals(data.getParticipantType());
  }

  private boolean isCorporation(NolSubmissionDataResource data) {
    return FIELD_VALUE_PARTICIPANT_TYPE_CORPORATION.equals(data.getParticipantType());
  }


  private CrmValidationErrorResource createValidationErrorTask(CrmAccountResource crmAccount, NolSubmissionDataResource data, List<String> validationErrors) throws ServiceException {
    
    Integer participantPin = getParticipantPin(data);
    String method = getMethod(data);
    String primaryFarmingActivity = getPrimaryFarmingActivity(data);
    
    String sinBnFieldName;
    String sinBnValue;
    if(isIndividual(data)) {
      sinBnFieldName = FIELD_NAME_SIN_NUMBER;
      sinBnValue = data.getSinNumber();
    } else {
      sinBnFieldName = FIELD_NAME_BUSINESS_NUMBER;
      if(StringUtils.isNotBlank(data.getBusinessNumber())){
        sinBnValue = data.getBusinessNumber() + BUSINESS_NUMBER_SUFFIX;
      } else {
        sinBnValue = null;
      }
    }
    
    StringBuilder errorsText = new StringBuilder();
    
    for(String error : validationErrors) {
      errorsText.append("- ");
      errorsText.append(error);
      errorsText.append("\n");
    }
    
    String subject = String.format(INVALID_FORM_TASK_SUBJECT_FORMAT,
        getProgramYear(data),
        FORM_SHORT_NAME,
        participantPin);
    
    String description = StringUtils.formatWithNullAsEmptyString(INVALID_FORM_TASK_DESCRIPTION_PREFIX_FORMAT, formUserType,
        FORM_LONG_NAME,
        errorsText,
        data.getGrowerCorporationName(),
        data.getPhoneNumber(),
        data.getEmail(),
        data.getParticipantType(),
        sinBnFieldName,
        sinBnValue,
        primaryFarmingActivity);
    
    
    String accountId = null;
    if(crmAccount != null) {
      accountId = crmAccount.getAccountid();
    }
    
    CrmValidationErrorResource task = new CrmValidationErrorResource();
    task.setAccountIdParameter(accountId);
    task.setSubject(subject);
    task.setDescription(description);
    task.setCr4dd_method(method);
    task.setCr4dd_origin(data.getOrigin());
    task.setCr4dd_participanttype(accountId);
    task.setCr4dd_primaryfarmingactivity(primaryFarmingActivity);
    task.setCr4dd_participanttype(data.getParticipantType());
    
    StringBuilder chefsSubmissionUrl = new StringBuilder();
    chefsSubmissionUrl.append(configUtil.getValue(ConfigurationKeys.CHEFS_UI_URL));
    chefsSubmissionUrl.append(URL_VIEW_CHEFS_SUBMISSION_FORM);
    chefsSubmissionUrl.append(data.getSubmissionGuid());
    task.setCr4dd_chefsurl(chefsSubmissionUrl.toString());
    
    String queueId = getValidationQueueId();
    return crmDao.createValidationErrorTask(task, queueId);
  }
  

  private String getPrimaryFarmingActivity(NolSubmissionDataResource data) {
    String primaryFarmingActivity = data.getPrimaryFarmingActivity();
    if(FIELD_VALUE_PRIMARY_FARMING_ACTIVITY_OTHER.equals(primaryFarmingActivity)) {
      primaryFarmingActivity = FIELD_VALUE_PREFIX_PRIMARY_FARMING_ACTIVITY_OTHER + data.getPrimaryFarmingActivityOther();
    }
    return primaryFarmingActivity;
  }

  private void validateBusinessNumbersMatch(String chefsValue, String farmValue, List<String> validationErrors) {
    
    if (farmValue == null) {
      validationErrors
      .add(FIELD_NAME_BUSINESS_NUMBER + " in BCFARMS does not start with a 9 digit number. Unable to validate.");
      return;
    }
    
    String chefsBn = chefsValue.replaceAll("\\s", "");
    String farmBn = farmValue.trim().replaceAll("\\s", "");
    Pattern pattern = Pattern.compile("^(\\d{9})");
    Matcher matcher = pattern.matcher(farmBn);
    
    if(matcher.find()) {
      String farmFirstNineDigits = matcher.group(0);
      if( ! StringUtils.equal(farmFirstNineDigits, chefsBn) ) {
        validationErrors.add("Field \"" + FIELD_NAME_BUSINESS_NUMBER + "\" with value \"" + chefsBn + BUSINESS_NUMBER_SUFFIX
            + "\" does not match BCFARMS: \"" + farmValue + "\". Note that only the first nine digits are compared.");
      }
    } else {
      validationErrors.add(FIELD_NAME_BUSINESS_NUMBER + " in BCFARMS does not start with a 9 digit number. Unable to validate.");
    }
  }

  private void validateRequiredField(List<String> validationErrors, String field, String value) {
    if(StringUtils.isBlank(value)) {
      validationErrors.add(requiredFieldError(field)); 
    }
  }

  private void validateRequiredField(List<String> validationErrors, String field, Integer value) {
    if(value == null) {
      validationErrors.add(requiredFieldError(field)); 
    }
  }

  private String requiredFieldError(String field) {
    return "Required field is blank: " + field;
  }


  private String getCorporateQueueId() throws ServiceException {
    if(corporateQueueId == null) {
      String queueName = configUtil.getValue(ConfigurationKeys.CRM_QUEUES_NOL_CORPORATE);
      corporateQueueId = queryQueueId(queueName);
    }
    return corporateQueueId;
  }

  private String getIndividualQueueId() throws ServiceException {
    if(individualQueueId == null) {
      String queueName = configUtil.getValue(ConfigurationKeys.CRM_QUEUES_NOL_INDIVIDUAL);
      individualQueueId = queryQueueId(queueName);
    }
    return individualQueueId;
  }

  @Override
  protected String getSystemErrorQueueId() throws ServiceException {
    return getValidationQueueId();
  }
  
  @Override
  protected String getValidationQueueId() throws ServiceException {
    if(validationQueueId == null) {
      String queueName = configUtil.getValue(ConfigurationKeys.CRM_QUEUES_NOL_VALIDATION);
      validationQueueId = queryQueueId(queueName);
    }
    return validationQueueId;
  }

  private String queryQueueId(String queueName) throws ServiceException {
    CrmQueueResource queue = crmDao.getQueueByName(queueName);
    return queue.getQueueId();
  }
  
  private CrmBenefitResource getBenefitByAccountIdAndProgramYear(String accountId, Integer programYear) throws ServiceException {
    CrmListResource<CrmBenefitResource> listResource;
    try {
      listResource = crmDao.getBenefitsByAccountId(accountId);
      List<CrmBenefitResource> benefits = listResource.getList();
      for (CrmBenefitResource benefit : benefits) {
        if (benefit.getVsiParticipantProgramYear().getVsiProgramYear().getVsiYear().equals(programYear)) {
          return benefit;
        }
      }
    } catch (IOException e) {
      logger.error("Unexpected error: ", e);
      logger.error(e.getMessage());
    }
    return null;
  }
  
  public void uploadNolFileToAccount(String submissionGuid, String accountId) throws ServiceException {

    Map<Integer,File> participantPinFileMap = null;
    participantPinFileMap = cdogsService.createCdogsNolFormDocument(submissionGuid, formUserType);
    Map.Entry<Integer,File> entry = participantPinFileMap.entrySet().iterator().next();
    File file = entry.getValue();
    
    CrmAccountAnnotationResource nolEntity = new CrmAccountAnnotationResource();
    nolEntity.setSubject("NOL Form");
    nolEntity.setNotetext("Upload CHEFS NOL Form");
    nolEntity.setFilename(file.getName());
    nolEntity.setIsdocument(true);
    nolEntity.setEntityId(accountId);
    try {
      nolEntity.setDocumentbody(IOUtils.encodeFileToBase64(file));
    } catch (IOException e) {
      logger.error("Unexpected error: ", e);
      throw new ServiceException(e);
    }

    try {
      CrmAccountAnnotationResource result = crmDao.uploadFileToNote(nolEntity);
      logger.debug(result.toString());
    } catch (ServiceException e) {
      logger.error("Unexpected error: ", e);
      throw new ServiceException(e);
    }

  }

  private Integer getParticipantPin(NolSubmissionDataResource data) {
    return data.getAgriStabilityPin();
  }

  private Integer getProgramYear(NolSubmissionDataResource data) {
    return data.getProgramYear();
  }

}
