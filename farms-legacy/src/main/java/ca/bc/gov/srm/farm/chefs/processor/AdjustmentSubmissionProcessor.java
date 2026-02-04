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
import static ca.bc.gov.srm.farm.chefs.forms.AdjustmentFormConstants.*;
import static ca.bc.gov.srm.farm.chefs.forms.ChefsFormConstants.*;
import static ca.bc.gov.srm.farm.log.LoggingUtils.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JacksonException;

import ca.bc.gov.srm.farm.chefs.database.ChefsFormTypeCodes;
import ca.bc.gov.srm.farm.chefs.resource.adjustment.AdjustmentSubmissionDataResource;
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
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.exception.TooManyRequestsException;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ChefsSubmissionProcessorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.DateUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

public class AdjustmentSubmissionProcessor extends ChefsSubmissionProcessor<AdjustmentSubmissionDataResource> {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private static final String INVALID_FORM_TASK_SUBJECT_FORMAT = "%d %s %d";

  private static final String INVALID_FORM_TASK_DESCRIPTION_PREFIX_FORMAT = "%s %s form was submitted but has validation errors:\n\n"
      + "%s\n" 
      + "Participant Name: %s\n"; 

  public AdjustmentSubmissionProcessor(Connection connection, String formUserType) {
    super(ChefsFormTypeCodes.ADJ, FORM_SHORT_NAME, FORM_LONG_NAME, formUserType, connection);
  }

  private String validationQueueId;

  @Override
  protected void processSubmission(String submissionGuid, String submissionResponseStr) {
    logMethodStart(logger);

    CrmTaskResource task = null;

    try {
      SubmissionParentResource<AdjustmentSubmissionDataResource> submissionMetaData = getSubmissionMetaData(submissionResponseStr,
          AdjustmentSubmissionDataResource.class);

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

  public CrmTaskResource processSubmission(
      SubmissionParentResource<AdjustmentSubmissionDataResource> submissionMetaData) throws ServiceException {
    logMethodStart(logger);

    CrmTaskResource newTask = null;

    SubmissionResource<AdjustmentSubmissionDataResource> submission = submissionMetaData.getSubmission();
    String submissionGuid = submissionMetaData.getSubmissionGuid();
    AdjustmentSubmissionDataResource data = submission.getData();
    data.setSubmissionGuid(submissionGuid);

    ChefsSubmission submissionRec = submissionRecordMap.get(submissionGuid);

    ChefsSubmissionProcessData chefsSubmissionProcessData = shouldProcessSubmission(submissionGuid, data, submissionRec);
    
    if(chefsSubmissionProcessData.getProcess()) {
      
      submissionRec = chefsSubmissionProcessData.getChefsSubmission();
      if (submissionRec == null) {
        submissionRec = newSubmissionRecord(submissionGuid);
      }

      Integer participantPin = data.getAgriStabilityPin();

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

          createScenarios(data, client, submissionRec);
        }

      } catch (SQLException e) {
        logger.error("SQLException: ", e);
        throw new ServiceException(e);
      }
    }

    logMethodEnd(logger);
    return newTask;
  }

  private void createScenarios(AdjustmentSubmissionDataResource data, Client client,
                 ChefsSubmission submissionRecParam) throws ServiceException {

    List<Integer> SelectedYears = data.getSelectedYears();

    ChefsSubmission submissionRec = submissionRecParam;
    submissionRec.setSubmissionStatusCode(SUBMITTED);
    try {
      if(submissionRec.getSubmissionId() == null) {
          submissionRec = createOrUpdateSubmission(submissionRec);
      }
    
      for (int programYear : SelectedYears) {
        logger.debug("Selected Program year:" + SelectedYears);
        createAdjustmentForProgramYear(data, client, programYear, submissionRec);
      }
      
      submissionRec.setSubmissionStatusCode(PROCESSED);
      submissionRec = createOrUpdateSubmission(submissionRec);

    } catch (SQLException e) {
      logger.error("SQLException: ", e);
      throw new ServiceException(e);
    }

  }

  public void createAdjustmentForProgramYear(AdjustmentSubmissionDataResource data, Client client,
                         Integer programYear, ChefsSubmission submissionRecParam) throws ServiceException {

    ChefsSubmission submissionRec = submissionRecParam;
    
    Integer participantPin = client.getParticipantPin();

    List<ScenarioMetaData> programYearMetadata;
    try {
      programYearMetadata = readDAO.readProgramYearMetadata(participantPin, programYear);

    } catch (SQLException e) {
      logger.error("SQLException: ", e);
      throw new ServiceException(e);
    }
    
    ChefsSubmissionProcessorService chefsSubmissionProcessorService = ServiceFactory
        .getChefsSubmissionProcessorService();

    ScenarioMetaData finalOrLatestAdjustment = ScenarioUtils.findVerifiedFinalOrLatestAdjustment(programYearMetadata, programYear);
    Integer scenarioIdToCopy = finalOrLatestAdjustment.getScenarioId();

    Integer adjustmentScenarioNumber = chefsSubmissionProcessorService.createAdjustmentChefsScenario(data,
        client.getClientId(), programYear, scenarioIdToCopy, getApplicationVersion(), user);

    try {

      CalculatorService calculatorService = ServiceFactory.getCalculatorService();
      
      Scenario scenario = calculatorService.loadScenario(participantPin, programYear, adjustmentScenarioNumber);
      scenario.setChefsSubmissionGuid(submissionRec.getSubmissionGuid());
      
      calculatorService.updateScenario(scenario, ScenarioStateCodes.IN_PROGRESS, null,
          ScenarioCategoryCodes.PRODUCER_ADJUSTMENT, verifierUserEmail, null, formUserType, ChefsFormTypeCodes.ADJ, null, user);
      
      Integer submissionId = submissionRec.getSubmissionId();

      Integer adjustmentScenarioId = scenario.getScenarioId();
      chefsDatabaseDao.updateScenarioSubmissionId(connection, adjustmentScenarioId, submissionId, user);
      connection.commit();

    } catch (SQLException e) {
      logger.error("SQLException: ", e);
      throw new ServiceException(e);
    }

  }

  private List<String> validate(AdjustmentSubmissionDataResource data, Client client, CrmAccountResource crmAccount) throws ServiceException {
    logMethodStart(logger);

    List<String> validationErrors = new ArrayList<>();

    validateRequiredField(validationErrors, FIELD_NAME_AGRISTABILITY_PIN, data.getAgriStabilityPin());
    validateRequiredField(validationErrors, FIELD_NAME_PARTICIPANT_TYPE, data.getBusinessStructure());
    validateRequiredField(validationErrors, FIELD_NAME_PROGRAM_YEAR, data.getProgramYear());

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
      
      validateVerifiedFinalScenariosExist(data, validationErrors);
    }
    

    logMethodEnd(logger);
    return validationErrors;
  }

  private boolean isIndividual(AdjustmentSubmissionDataResource data) {
    return FIELD_VALUE_PARTICIPANT_TYPE_INDIVIDUAL.equals(data.getBusinessStructure());
  }

  private boolean isCorporation(AdjustmentSubmissionDataResource data) {
    return FIELD_VALUE_PARTICIPANT_TYPE_CORPORATION.equals(data.getBusinessStructure());
  }

  private CrmTaskResource createValidationErrorTask(CrmAccountResource crmAccount,
      AdjustmentSubmissionDataResource data, List<String> validationErrors) throws ServiceException {

    Integer participantPin = data.getAgriStabilityPin();
    
    List<Integer> selectedYears = data.getSelectedYears();
    Integer subjectYear = (selectedYears.isEmpty()) ? DateUtils.getYearFromDate(new Date()) : selectedYears.get(selectedYears.size()-1);

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
    
    String subject = String.format(INVALID_FORM_TASK_SUBJECT_FORMAT, subjectYear, FORM_SHORT_NAME,
        participantPin);

    String description = StringUtils.formatWithNullAsEmptyString(INVALID_FORM_TASK_DESCRIPTION_PREFIX_FORMAT, formUserType, 
        FORM_LONG_NAME, errorsText, data.getParticipantName());

    String accountId = null;
    if (crmAccount != null) {
      accountId = crmAccount.getAccountid();
    }

    CrmValidationErrorResource task = new CrmValidationErrorResource();
    task.setAccountIdParameter(accountId);
    task.setSubject(subject);
    task.setDescription(description);
    task.setCr4dd_method(getMethod(data));
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

  private void validateVerifiedFinalScenariosExist(AdjustmentSubmissionDataResource data, List<String> validationErrors)
      throws ServiceException {

    List<Integer> selectedYears = data.getSelectedYears();

    List<Integer> yearsWithNoFinal = new ArrayList<>();
    try {

      Integer participantPin = data.getAgriStabilityPin();

      for (int programYear : selectedYears) {

        List<ScenarioMetaData> scenarioMetaDataList = readDAO.readProgramYearMetadata(participantPin, programYear);
        boolean hasVerifiedFinalScenario = ScenarioUtils.programYearHasVerifiedFinal(scenarioMetaDataList, programYear);

        logger.debug(programYear + " hasVerifiedFinalScenario: " + hasVerifiedFinalScenario);
        if ( ! hasVerifiedFinalScenario) {
          yearsWithNoFinal.add(programYear);
        }
      }

    } catch (SQLException e) {
      logger.error("SQLException: ", e);
      throw new ServiceException(e);
    }
    
    if(!yearsWithNoFinal.isEmpty()) {
      validationErrors.add("The following years were selected for reprocessing but do not have a Verified Final scenario: "
          + yearsWithNoFinal.stream().map(String::valueOf)
          .collect(Collectors.joining(",")));
    }

  }

  private void validateBusinessNumbersMatch(String chefsValue, String farmValue, List<String> validationErrors) {

    if (farmValue == null) {
      validationErrors
      .add(FIELD_NAME_BUSINESS_NUMBER + " in BCFARMS does not start with a 9 digit number. Unable to validate.");
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
      String queueName = configUtil.getValue(ConfigurationKeys.CRM_QUEUES_ADJUSTMENT_VALIDATION);
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

}
