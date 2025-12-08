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
import static ca.bc.gov.srm.farm.chefs.forms.InterimFormConstants.*;
import static ca.bc.gov.srm.farm.log.LoggingUtils.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JacksonException;

import ca.bc.gov.srm.farm.chefs.database.ChefsFormTypeCodes;
import ca.bc.gov.srm.farm.chefs.forms.ChefsFarmTypeCodes;
import ca.bc.gov.srm.farm.chefs.resource.interim.InterimSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionParentResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionResource;
import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.crm.CrmConstants;
import ca.bc.gov.srm.farm.crm.resource.CrmAccountResource;
import ca.bc.gov.srm.farm.crm.resource.CrmQueueResource;
import ca.bc.gov.srm.farm.crm.resource.CrmTaskResource;
import ca.bc.gov.srm.farm.crm.resource.CrmValidationErrorResource;
import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.ScenarioMetaData;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmission;
import ca.bc.gov.srm.farm.domain.codes.ParticipantClassCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.exception.TooManyRequestsException;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ChefsSubmissionProcessorService;
import ca.bc.gov.srm.farm.service.CrmTransferService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.DateUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

public class InterimSubmissionProcessor extends ChefsSubmissionProcessor<InterimSubmissionDataResource> {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  private static final String INVALID_FORM_TASK_SUBJECT_FORMAT = "%d %s %d";

  private static final String INVALID_FORM_TASK_DESCRIPTION_PREFIX_FORMAT = "%s %s form was submitted but has validation errors:\n\n"
      + "%s\n" + "Participant Name: %s\n" + "Telephone: %s\n" + "Email: %s\n";

  public InterimSubmissionProcessor(Connection connection, String formUserType) {
    super(ChefsFormTypeCodes.INTERIM, FORM_SHORT_NAME, FORM_LONG_NAME, formUserType, connection);
  }

  private String validationQueueId;

  @Override
  protected void processSubmission(String submissionGuid, String submissionResponseStr) {
    logMethodStart(logger);

    CrmTaskResource task = null;
    
    try {
      SubmissionParentResource<InterimSubmissionDataResource> submissionMetaData = getSubmissionMetaData(submissionResponseStr,
          InterimSubmissionDataResource.class);

      if (!submissionMetaData.getDraft()) {
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

  public CrmTaskResource processSubmission(SubmissionParentResource<InterimSubmissionDataResource> submissionMetaData)
      throws ServiceException {
    logMethodStart(logger);

    CrmTaskResource newTask = null;

    SubmissionResource<InterimSubmissionDataResource> submission = submissionMetaData.getSubmission();
    String submissionGuid = submissionMetaData.getSubmissionGuid();
    InterimSubmissionDataResource data = submission.getData();
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
      if (participantPin != null) {
        client = getClientByParticipantPin(participantPin);
        crmAccount = getCrmAccountByParticipantPin(participantPin);
      }

      List<String> validationErrors = validate(data, client, crmAccount);
      boolean hasErrors = !validationErrors.isEmpty();

      if (hasErrors) {
        CrmTaskResource existingValidationErrorTask = crmDao.getValidationErrorBySubmissionId(submissionGuid);
        if (existingValidationErrorTask == null) {
          newTask = createValidationErrorTask(crmAccount, data, validationErrors);
        } else {
          logger.debug("Validation error task already exists: " + existingValidationErrorTask.toString());
          if (existingValidationErrorTask.getStateCode() == CrmConstants.TASK_STATE_CODE_COMPLETED) {
            newTask = createValidationErrorTask(crmAccount, data, validationErrors);
          } else {
            newTask = existingValidationErrorTask;
          }
        }
        submissionRec.setSubmissionStatusCode(INVALID);
        submissionRec.setValidationTaskGuid(newTask.getActivityId());
        submissionRec = createOrUpdateSubmission(submissionRec);

      } else {
        // Validation passed

        assert client != null;
        createScenarios(data, client, programYear, submissionRec);
      }

    } catch (SQLException e) {
      logger.error("Unexpected error: ", e);
      throw new ServiceException(e);
    }

    logMethodEnd(logger);
    return newTask;
  }

  private Integer createScenarios(InterimSubmissionDataResource data, Client client, Integer programYear,
      ChefsSubmission submissionRecParam) throws ServiceException {
    
    ChefsSubmission submissionRec = submissionRecParam;

    Integer interimScenarioNumber = null;

    try {

      submissionRec = createOrUpdateSubmission(submissionRec);
      
      ChefsSubmissionProcessorService chefsSubmissionProcessorService = ServiceFactory
          .getChefsSubmissionProcessorService();
  
      interimScenarioNumber = chefsSubmissionProcessorService.createInterimChefsScenario(data, client.getClientId(),
          programYear, getApplicationVersion(), submissionRec.getSubmissionId(), user);


      Integer participantPin = client.getParticipantPin();
      List<ScenarioMetaData> updatedProgramYearMetadata = readDAO.readProgramYearMetadata(participantPin,
          programYear);

      ScenarioMetaData chefScenarioMetaData = ScenarioUtils.findScenarioByCategory(updatedProgramYearMetadata,
          programYear, ScenarioCategoryCodes.CHEF_INTRM, ScenarioTypeCodes.CHEF);
      
      CalculatorService calculatorService = ServiceFactory.getCalculatorService();
      Scenario chefScenario = calculatorService.loadScenario(participantPin, programYear, chefScenarioMetaData.getScenarioNumber());
      
      String reasonForApplying = data.getReasonForApplying();
      
      CrmTransferService crmTransferService = ServiceFactory.getCrmTransferService();
      try {
        crmTransferService.scheduleBenefitTransfer(chefScenario, verifierUserEmail, user, reasonForApplying, formUserType, ChefsFormTypeCodes.INTERIM, null, null);
      } catch (Exception e) {
        logger.error("Unexpected error: ", e);
        throw new ServiceException(e);
      }
      
      submissionRec.setSubmissionStatusCode(PROCESSED);
      submissionRec = createOrUpdateSubmission(submissionRec);
      Integer submissionId = submissionRec.getSubmissionId();
      
      ScenarioMetaData interimScenarioMetaData = ScenarioUtils.findScenarioByYearAndNumber(updatedProgramYearMetadata,
          programYear, interimScenarioNumber);
      Integer interimScenarioId = interimScenarioMetaData.getScenarioId();
      chefsDatabaseDao.updateScenarioSubmissionId(connection, interimScenarioId, submissionId, user);
      connection.commit();

      Scenario scenario = calculatorService.loadScenario(participantPin, programYear, interimScenarioNumber);

      calculatorService.updateScenario(scenario, ScenarioStateCodes.IN_PROGRESS, null,
          ScenarioCategoryCodes.INTERIM, verifierUserEmail, reasonForApplying, formUserType, ChefsFormTypeCodes.INTERIM, null, user);

    } catch (SQLException | DataAccessException e) {
      logger.error("Unexpected error: ", e);
      throw new ServiceException(e);
    }

    return interimScenarioNumber;
  }

  private List<String> validate(InterimSubmissionDataResource data, Client client, CrmAccountResource crmAccount) {
    logMethodStart(logger);

    List<String> validationErrors = new ArrayList<>();

    validateRequiredField(validationErrors, FIELD_NAME_AGRISTABILITY_PIN, getParticipantPin(data));
    validateRequiredField(validationErrors, FIELD_NAME_FISCAL_YEAR_END, getProgramYear(data));

    if (isIndividual(data)) {
      validateRequiredField(validationErrors, FIELD_NAME_SIN_NUMBER, data.getSinNumber());
    } else if (isCorporation(data)) {
      validateRequiredField(validationErrors, FIELD_NAME_BUSINESS_NUMBER, data.getBusinessTaxNumberBn());
    }

    if (crmAccount == null) {
      validationErrors.add("PIN not found in CRM.");
    }

    if (client == null) {
      validationErrors.add("PIN not found in BCFARMS.");

    } else if (validationErrors.isEmpty()) {

      if (client.getParticipantClassCode().equals(ParticipantClassCodes.INDIVIDUAL)) {

        if (isIndividual(data)) {
          validateValuesMatch(FIELD_NAME_SIN_NUMBER, data.getSinNumber(), client.getSin(), validationErrors);
        } else {
          validationErrors.add("The participant type \"Corporation\" on the form does not match BCFARMS.");
        }

      }

      if (client.getParticipantClassCode().equals(ParticipantClassCodes.CORPORATION)) {

        if (isCorporation(data)) {
          validateBusinessNumbersMatch(data.getBusinessTaxNumberBn(), client.getBusinessNumber(), validationErrors);
        } else {
          validationErrors.add("The participant type \"Individual\" on the form does not match BCFARMS.");
        }
      }
      
      if (data.getFarmType() != null && 
         (ChefsFarmTypeCodes.COMMUNAL_ORGANIZATION.equals(data.getFarmType().getValue()) || ChefsFarmTypeCodes.TRUST.equals(data.getFarmType().getValue()))) {
        validateTrustNumbersMatch(data.getTrustNumber(), client.getTrustNumber(), validationErrors);
      } 
    }

    logMethodEnd(logger);
    return validationErrors;
  }

  private boolean isIndividual(InterimSubmissionDataResource data) {
    if (data.getBusinessStructure() != null) {
      return FIELD_VALUE_PARTICIPANT_TYPE_INDIVIDUAL.equals(data.getBusinessStructure());
    } else {
      return ChefsFarmTypeCodes.STATUS_INDIAN_FARMING_ON_A_RESERVE.equals(data.getFarmType().getValue()) || 
          ChefsFarmTypeCodes.INDIVIDUAL.equals(data.getFarmType().getValue());
    }
  }

  private boolean isCorporation(InterimSubmissionDataResource data) {
    if (data.getBusinessStructure() != null) {
      return FIELD_VALUE_PARTICIPANT_TYPE_CORPORATION.equals(data.getBusinessStructure());
    } else {
      return !ChefsFarmTypeCodes.STATUS_INDIAN_FARMING_ON_A_RESERVE.equals(data.getFarmType().getValue())
          && !ChefsFarmTypeCodes.COMMUNAL_ORGANIZATION.equals(data.getFarmType().getValue())
          && !ChefsFarmTypeCodes.TRUST.equals(data.getFarmType().getValue());
    }
  }

  private CrmTaskResource createValidationErrorTask(CrmAccountResource crmAccount, InterimSubmissionDataResource data,
      List<String> validationErrors) throws ServiceException {

    Integer participantPin = getParticipantPin(data);
    String method = getMethod(data);
    
    String businessNumber = null;
    if (StringUtils.isNotBlank(data.getBusinessTaxNumberBn())) {
      businessNumber = data.getBusinessTaxNumberBn() + BUSINESS_NUMBER_SUFFIX;
    }

    StringBuilder errorsText = new StringBuilder();

    for (String error : validationErrors) {
      errorsText.append("- ");
      errorsText.append(error);
      errorsText.append("\n");
    }

    Integer programYear  = getProgramYear(data);
    programYear = (programYear == null) ? DateUtils.getYearFromDate(new Date()) : programYear;
    
    String subject = String.format(INVALID_FORM_TASK_SUBJECT_FORMAT, programYear,
        FORM_SHORT_NAME, participantPin);

    String description = StringUtils.formatWithNullAsEmptyString(INVALID_FORM_TASK_DESCRIPTION_PREFIX_FORMAT, formUserType,
        FORM_LONG_NAME, errorsText, data.getParticipantName(), data.getTelephone(),
        data.getEmail());

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
    task.setCr4dd_participanttype(data.getBusinessStructure());
    task.setCr4dd_businessnumber(businessNumber);
    task.setCr4dd_sinbn(data.getSinNumber());
    
    StringBuilder chefsSubmissionUrl = new StringBuilder();
    chefsSubmissionUrl.append(configUtil.getValue(ConfigurationKeys.CHEFS_UI_URL));
    chefsSubmissionUrl.append(URL_VIEW_CHEFS_SUBMISSION_FORM);
    chefsSubmissionUrl.append(data.getSubmissionGuid());
    task.setCr4dd_chefsurl(chefsSubmissionUrl.toString());
    
    String queueId = getValidationQueueId();
    return crmDao.createValidationErrorTask(task, queueId);
  }

  private void validateBusinessNumbersMatch(String chefsValue, String farmValue, List<String> validationErrors) {

    if (farmValue == null) {
      validationErrors.add(FIELD_NAME_BUSINESS_NUMBER + " in BCFARMS does not start with a 9 digit number. Unable to validate.");
      return;
    }

    String chefsBn = chefsValue.trim().replaceAll("\\s", "");
    String farmBn = farmValue.trim().replaceAll("\\s", "");
    Pattern pattern = Pattern.compile("^(\\d{9})");
    Matcher matcher = pattern.matcher(farmBn);

    if (matcher.find()) {
      String farmFirstNineDigits = matcher.group(0);
      if (!StringUtils.equal(farmFirstNineDigits, chefsBn)) {
        validationErrors.add("Field \"" + FIELD_NAME_BUSINESS_NUMBER + "\" with value \"" + chefsBn
            + BUSINESS_NUMBER_SUFFIX + "\" does not match BCFARMS: \"" + farmValue
            + "\". Note that only the first nine digits are compared.");
      }
    } else {
      validationErrors
          .add(FIELD_NAME_BUSINESS_NUMBER + " in BCFARMS does not start with a 9 digit number. Unable to validate.");
    }
  }

  private void validateTrustNumbersMatch(String chefsValue, String farmValue, List<String> validationErrors) {

    if (farmValue == null) {
      validationErrors.add(FIELD_NAME_TRUST_NUMBER + " in BCFARMS does not start with a 8 digit number. Unable to validate.");
      return;
    }

    String chefsTrustNum = chefsValue.trim().replaceAll("\\s", "");
    String farmTrustNum = farmValue.trim().replaceAll("\\s", "");
    // Trust number is an eight-digit number that begins with the letter "T"
    Pattern pattern = Pattern.compile("^T(\\d{8})");
    Matcher matcher = pattern.matcher(farmTrustNum);

    if (matcher.find()) {
      String farmFirstEightDigits = matcher.group(1);
      if (!StringUtils.equal(farmFirstEightDigits, chefsTrustNum)) {
        validationErrors.add("Field \"" + FIELD_NAME_TRUST_NUMBER + "\" with value \"" + TRUST_NUMBER_PREFIX + chefsTrustNum
            + "\" does not match BCFARMS: \"" + farmValue + "\". Note that only the first eight digits are compared.");
      }
    } else {
      validationErrors.add(FIELD_NAME_TRUST_NUMBER + " in BCFARMS does not start with a 8 digit number. Unable to validate.");
    }
  }

  private void validateRequiredField(List<String> validationErrors, String field, String value) {
    if (StringUtils.isBlank(value)) {
      validationErrors.add(requiredFieldError(field));
    }
  }

  private void validateRequiredField(List<String> validationErrors, String field, Integer value) {
    if (value == null) {
      validationErrors.add(requiredFieldError(field));
    }
  }

  private String requiredFieldError(String field) {
    return "Required field is blank: " + field;
  }

  @Override
  protected String getSystemErrorQueueId() throws ServiceException {
    return getValidationQueueId();
  }

  @Override
  protected String getValidationQueueId() throws ServiceException {
    if (validationQueueId == null) {
      String queueName = configUtil.getValue(ConfigurationKeys.CRM_QUEUES_INTERIM_VALIDATION);
      validationQueueId = queryQueueId(queueName);
    }
    return validationQueueId;
  }

  private String queryQueueId(String queueName) throws ServiceException {
    CrmQueueResource queue = crmDao.getQueueByName(queueName);
    return queue.getQueueId();
  }

  private Client getClientByParticipantPin(Integer participantPin) throws ServiceException {
    Client client = null;
    try {
      if (participantPin != null) {
        client = readDAO.readClient(participantPin);
      }

    } catch (Exception e) {
      logger.error("Unexpected error: ", e);
      throw new ServiceException(e);
    }
    return client;
  }

  private String getApplicationVersion() {
    return configUtil.getValue(ConfigurationKeys.APPLICATION_VERSION);
  }

  private Integer getParticipantPin(InterimSubmissionDataResource data) {
    return data.getAgriStabilityAgriInvestPin();
  }

  private Integer getProgramYear(InterimSubmissionDataResource data) {
    return DateUtils.getYearFromDate(data.getFiscalYearEndDate());
  }

}
