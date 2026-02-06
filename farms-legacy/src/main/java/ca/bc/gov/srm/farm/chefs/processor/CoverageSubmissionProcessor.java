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
import static ca.bc.gov.srm.farm.chefs.forms.CoverageFormConstants.*;
import static ca.bc.gov.srm.farm.log.LoggingUtils.*;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JacksonException;

import ca.bc.gov.srm.farm.chefs.database.ChefsFormTypeCodes;
import ca.bc.gov.srm.farm.chefs.database.ChefsSubmissionStatusCodes;
import ca.bc.gov.srm.farm.chefs.resource.coverage.CoverageSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.coverage.OperationsAffected;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionParentResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionResource;
import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.crm.CrmConstants;
import ca.bc.gov.srm.farm.crm.resource.CrmAccountAnnotationResource;
import ca.bc.gov.srm.farm.crm.resource.CrmAccountResource;
import ca.bc.gov.srm.farm.crm.resource.CrmCoverageTaskResource;
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
import ca.bc.gov.srm.farm.service.CdogsService;
import ca.bc.gov.srm.farm.service.ChefsSubmissionProcessorService;
import ca.bc.gov.srm.farm.service.CrmTransferService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.DateUtils;
import ca.bc.gov.srm.farm.util.IOUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

public class CoverageSubmissionProcessor extends ChefsSubmissionProcessor<CoverageSubmissionDataResource> {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  private final CdogsService cdogsService = ServiceFactory.getCdogsService();

  private static final String VALID_FORM_TASK_SUBJECT_FORMAT = "%d %s %d";

  private static final String VALID_FORM_TASK_DESCRIPTION_FORMAT = "Commodities Farmed: %s";

  private static final String INVALID_FORM_TASK_SUBJECT_FORMAT = "%d %s %d";

  private static final String INVALID_FORM_TASK_DESCRIPTION_PREFIX_FORMAT = "%s %s form was submitted but has validation errors:\n\n" + "%s\n"
      + "Participant Name: %s\n"
      + "Telephone: %s\n"
      + "Email: %s\n";

  public CoverageSubmissionProcessor(Connection connection, String formUserType) {
    super(ChefsFormTypeCodes.CN, FORM_SHORT_NAME, FORM_LONG_NAME, formUserType, connection);
  }

  private String corporateQueueId;
  private String individualQueueId;
  private String validationQueueId;

  @Override
  protected void processSubmission(String submissionGuid, String submissionResponseStr) {
    logMethodStart(logger);

    CrmTaskResource task = null;

    try {
      SubmissionParentResource<CoverageSubmissionDataResource> submissionMetaData = getSubmissionMetaData(submissionResponseStr, CoverageSubmissionDataResource.class);

      if (!submissionMetaData.getDraft()) {
        task = processSubmission(submissionMetaData);
      }

    } catch (ServiceException e) {
      if (e.getCause() instanceof TooManyRequestsException) {
        logger.error("TooManyRequestsException: ", e);
      } else if (e.getCause() instanceof JacksonException) {
        task = handleParseError(submissionGuid, e);
      } else {
        task = handleSystemError(submissionGuid, e);
      }
    }

    logMethodEnd(logger, task);
  }

  public CrmTaskResource processSubmission(SubmissionParentResource<CoverageSubmissionDataResource> submissionMetaData) throws ServiceException {
    logMethodStart(logger);

    CrmTaskResource newTask = null;

    SubmissionResource<CoverageSubmissionDataResource> submission = submissionMetaData.getSubmission();
    String submissionGuid = submissionMetaData.getSubmissionGuid();
    CoverageSubmissionDataResource data = submission.getData();
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

    Client client = null;
    CrmAccountResource crmAccount = null;
    if (participantPin != null) {
      client = getClientByParticipantPin(participantPin);
      crmAccount = getCrmAccountByParticipantPin(participantPin);
    }

    List<String> validationErrors = validate(data, client, crmAccount);
    boolean hasErrors = !validationErrors.isEmpty();

    if (hasErrors) {
      CrmTaskResource existingValidationErrorTask = crmDao.getValidationErrorBySubmissionGuid(submissionGuid);
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

      logger.debug("Creating valid Coverage task...");
      newTask = createValidCoverageTask(data, participantPin, crmAccount);
      
      uploadCoverageFileToAccount(submissionGuid, newTask.getAccountId());
      
      submissionRec.setSubmissionStatusCode(ChefsSubmissionStatusCodes.PROCESSED);
      submissionRec.setMainTaskGuid(newTask.getActivityId());
      submissionRec = createOrUpdateSubmission(submissionRec);
      
      logger.debug("Creating Coverage scenario...");
      assert client != null;
      Integer coverageScenarioNumber = createCoverageScenarios(data, client, programYear, submissionRec);
      logger.debug("coverageScenarioNumber = " + coverageScenarioNumber);
    }

    logMethodEnd(logger);
    return newTask;
  }
  
  public void uploadCoverageFileToAccount(String submissionGuid, String accountId) throws ServiceException {

    Map<Integer,File> participantPinFileMap = null;
    participantPinFileMap = cdogsService.createCdogsCoverageFormDocument(submissionGuid, formUserType);
    Map.Entry<Integer,File> entry = participantPinFileMap.entrySet().iterator().next();
    File file = entry.getValue();
    
    CrmAccountAnnotationResource coverageEntity = new CrmAccountAnnotationResource();
    coverageEntity.setSubject("Coverage Notice Form");
    coverageEntity.setNotetext("Upload CHEFS Coverage Notice Form");
    coverageEntity.setFilename(file.getName());
    coverageEntity.setIsdocument(true);
    coverageEntity.setEntityId(accountId);
    try {
      coverageEntity.setDocumentbody(IOUtils.encodeFileToBase64(file));
    } catch (IOException e) {
      logger.error("Unexpected error: ", e);
      throw new ServiceException(e);
    }

    try {
      CrmAccountAnnotationResource result = crmDao.uploadFileToNote(coverageEntity);
      logger.debug(result.toString());
    } catch (ServiceException e) {
      logger.error("Unexpected error: ", e);
      throw new ServiceException(e);
    }

  }

  private Integer createCoverageScenarios(CoverageSubmissionDataResource data, Client client, Integer programYear, ChefsSubmission submissionRecParam)
      throws ServiceException {

    ChefsSubmission submissionRec = submissionRecParam;
    Integer coverageScenarioNumber = null;

    try {

      Integer participantPin = client.getParticipantPin();
      List<ScenarioMetaData> updatedProgramYearMetadata = readDAO.readProgramYearMetadata(participantPin, programYear);
      
      String municipalityCode = getMunicipalityCodeFromScenarioMetaData(updatedProgramYearMetadata);
      logger.debug("municipalityCode = " + municipalityCode);
      
      ChefsSubmissionProcessorService chefsSubmissionProcessorService = ServiceFactory.getChefsSubmissionProcessorService();
      coverageScenarioNumber = chefsSubmissionProcessorService.createCoverageChefsScenario(data, client.getClientId(), programYear,
          getApplicationVersion(), municipalityCode, user);

      updatedProgramYearMetadata = readDAO.readProgramYearMetadata(participantPin, programYear);

      ScenarioMetaData chefScenarioMetaData = ScenarioUtils.findScenarioByCategory(updatedProgramYearMetadata, programYear,
          ScenarioCategoryCodes.CHEF_CN, ScenarioTypeCodes.CHEF);

      CalculatorService calculatorService = ServiceFactory.getCalculatorService();
      Scenario chefScenario = calculatorService.loadScenario(participantPin, programYear, chefScenarioMetaData.getScenarioNumber());

      String reasonForApplying = null;

      CrmTransferService crmTransferService = ServiceFactory.getCrmTransferService();
      try {
        crmTransferService.scheduleBenefitTransfer(chefScenario, verifierUserEmail, user, null, formUserType,
            ChefsFormTypeCodes.CN, null, null);
      } catch (Exception e) {
        logger.error("Unexpected error: ", e);
        throw new ServiceException(e);
      }

      submissionRec.setSubmissionStatusCode(PROCESSED);
      submissionRec = createOrUpdateSubmission(submissionRec);
      Integer submissionId = submissionRec.getSubmissionId();

      ScenarioMetaData coverageScenarioMetaData = ScenarioUtils.findScenarioByYearAndNumber(updatedProgramYearMetadata, programYear,
          coverageScenarioNumber);
      Integer coverageScenarioId = coverageScenarioMetaData.getScenarioId();
      chefsDatabaseDao.updateScenarioSubmissionId(connection, coverageScenarioId, submissionId, user);
      connection.commit();

      Scenario scenario = calculatorService.loadScenario(participantPin, programYear, coverageScenarioNumber);

      calculatorService.updateScenario(scenario, ScenarioStateCodes.IN_PROGRESS, null, ScenarioCategoryCodes.COVERAGE_NOTICE, verifierUserEmail,
          reasonForApplying, formUserType, ChefsFormTypeCodes.CN, null, user);

    } catch (SQLException | DataAccessException e) {
      logger.error("Unexpected error: ", e);
      throw new ServiceException(e);
    }

    return coverageScenarioNumber;
  }

  private List<String> validate(CoverageSubmissionDataResource data, Client client, CrmAccountResource crmAccount) throws ServiceException {
    logMethodStart(logger);

    List<String> validationErrors = new ArrayList<>();

    Integer participantPin = getParticipantPin(data);
    Integer programYear = getProgramYear(data);
    
    validateRequiredField(validationErrors, FIELD_NAME_AGRISTABILITY_PIN, participantPin);
    validateRequiredField(validationErrors, FIELD_NAME_PROGRAM_YEAR, programYear);
    validateRequiredField(validationErrors, FIELD_NAME_PARTICIPANT_TYPE, data.getBusinessStructure());

    if (participantPin != null && programYear != null) {
      Integer previousProgramYear = programYear - 1;
      List<ScenarioMetaData> scenarioMetaDataList = null;
      try {
        scenarioMetaDataList = readDAO.readProgramYearMetadata(participantPin, previousProgramYear);
      } catch (SQLException e) {
        logger.error("Unexpected error: ", e);
        throw new ServiceException(e);
      }
      if (!ScenarioUtils.programYearHasVerifiedFinal(scenarioMetaDataList, previousProgramYear)) {
        validationErrors.add("No Verified Final found for " + previousProgramYear);
      }
    }

    if (isIndividual(data)) {
      validateRequiredField(validationErrors, FIELD_NAME_SIN_NUMBER, data.getSinNumber());
    } else if (isCorporation(data)) {
      validateRequiredField(validationErrors, FIELD_NAME_BUSINESS_NUMBER, data.getBusinessTaxNumber());
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
          validateBusinessNumbersMatch(data.getBusinessTaxNumber(), client.getBusinessNumber(), validationErrors);
        } else {
          validationErrors.add("The participant type \"Individual\" on the form does not match BCFARMS.");
        }
      }
    }

    logMethodEnd(logger);
    return validationErrors;
  }

  private boolean isIndividual(CoverageSubmissionDataResource data) {
    return FIELD_VALUE_PARTICIPANT_TYPE_INDIVIDUAL.equals(data.getBusinessStructure());
  }

  private boolean isCorporation(CoverageSubmissionDataResource data) {
    return FIELD_VALUE_PARTICIPANT_TYPE_CORPORATION.equals(data.getBusinessStructure());
  }

  private CrmCoverageTaskResource createValidCoverageTask(CoverageSubmissionDataResource data, Integer participantPin, CrmAccountResource crmAccount)
      throws ServiceException {

    String method = getMethod(data);
    
    Integer programYear = getProgramYear(data);
    programYear = (programYear == null) ? DateUtils.getYearFromDate(new Date()) : programYear;

    String subject = StringUtils.formatWithNullAsEmptyString(VALID_FORM_TASK_SUBJECT_FORMAT, programYear, FORM_LONG_NAME,
        participantPin);
    String description = StringUtils.formatWithNullAsEmptyString(VALID_FORM_TASK_DESCRIPTION_FORMAT, String.join(", ", data.getCommoditiesFarmed()));

    String queueId = getQueueIdForParticipantType(data.getBusinessStructure());

    String accountId = null;
    if (crmAccount != null) {
      accountId = crmAccount.getAccountid();
    }

    CrmCoverageTaskResource task = new CrmCoverageTaskResource();
    task.setSubject(subject);
    task.setDescription(description);
    task.setCr4dd_method(method);
    task.setCr4dd_origin(data.getOrigin());
    task.setAccountIdParameter(accountId);
    
    
    boolean ownershipInterestInAnotherFarm = getBooleanFromString(data.getOwnershipInterestInAnotherFarm());
    boolean completed6MonthsOfFarmingActivities = getBooleanFromString(data.getCompleted6MonthsOfFarmingActivities());
    boolean uninsuredCommodities = getBooleanFromString(data.getUninsuredCommodities());
    
    task.setCr4dd_ownershipinterestinanotherfarm(ownershipInterestInAnotherFarm);
    task.setCr4dd_monthsoffarmingactivity(completed6MonthsOfFarmingActivities);
    task.setCr4dd_uninsuredcommodities(uninsuredCommodities);
    
    if ("yes".equalsIgnoreCase(data.getHaveProductionInsurance()) 
        && data.getProductionInsuranceNumbers() != null 
        && data.getProductionInsuranceNumbers().size() > 0) {
      task.setCr4dd_productioninsurance(String.join(", ", data.getProductionInsuranceNumbers()));
    }
    
    if (data.getCommoditiesFarmed() != null  && data.getCommoditiesFarmed().size() > 0) {
      task.setCr4dd_commoditiesfarmed(String.join(", ", data.getCommoditiesFarmed()));
    }
    
    task.setCr4dd_significantchangeorcircumstances(getAllOperationsAffected(data.getOperationsAffected(), data.getSpecifyOperation()));
    
    return crmDao.createValidCoverageTask(task, queueId);
  }

  private CrmTaskResource createValidationErrorTask(CrmAccountResource crmAccount, CoverageSubmissionDataResource data, List<String> validationErrors)
      throws ServiceException {

    Integer participantPin = getParticipantPin(data);
    String method = getMethod(data);

    String businessNumber = null;
    if (StringUtils.isNotBlank(data.getBusinessTaxNumber())) {
      businessNumber = data.getBusinessTaxNumber() + BUSINESS_NUMBER_SUFFIX;
    }

    StringBuilder errorsText = new StringBuilder();

    for (String error : validationErrors) {
      errorsText.append("- ");
      errorsText.append(error);
      errorsText.append("\n");
    }

    Integer programYear = getProgramYear(data);
    programYear = (programYear == null) ? DateUtils.getYearFromDate(new Date()) : programYear;

    String subject = String.format(INVALID_FORM_TASK_SUBJECT_FORMAT, programYear, FORM_LONG_NAME, participantPin);

    String description = StringUtils.formatWithNullAsEmptyString(INVALID_FORM_TASK_DESCRIPTION_PREFIX_FORMAT, formUserType, FORM_LONG_NAME,
        errorsText, data.getParticipantName(), data.getTelephone(), data.getEmail());

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
        validationErrors.add("Field \"" + FIELD_NAME_BUSINESS_NUMBER + "\" with value \"" + chefsBn + BUSINESS_NUMBER_SUFFIX
            + "\" does not match BCFARMS: \"" + farmValue + "\". Note that only the first nine digits are compared.");
      }
    } else {
      validationErrors.add(FIELD_NAME_BUSINESS_NUMBER + " in BCFARMS does not start with a 9 digit number. Unable to validate.");
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
      String queueName = configUtil.getValue(ConfigurationKeys.CRM_QUEUES_COVERAGE_VALIDATION);
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

  private String getQueueIdForParticipantType(String participantType) throws ServiceException {
    String queueId;
    if (FIELD_VALUE_PARTICIPANT_TYPE_INDIVIDUAL.equals(participantType)) {
      queueId = getIndividualQueueId();
    } else {
      queueId = getCorporateQueueId();
    }
    return queueId;
  }

  private String getCorporateQueueId() throws ServiceException {
    if (corporateQueueId == null) {
      String queueName = configUtil.getValue(ConfigurationKeys.CRM_QUEUES_COVERAGE_CORPORATE);
      corporateQueueId = queryQueueId(queueName);
    }
    return corporateQueueId;
  }

  private String getIndividualQueueId() throws ServiceException {
    if (individualQueueId == null) {
      String queueName = configUtil.getValue(ConfigurationKeys.CRM_QUEUES_COVERAGE_INDIVIDUAL);
      individualQueueId = queryQueueId(queueName);
    }
    return individualQueueId;
  }
  
  private String getAllOperationsAffected(OperationsAffected operationsAffected, String specify) {
    
    if (operationsAffected == null) {
      return null;
    }
    
    List<String> operations = new ArrayList<>();
    if (operationsAffected.getDownsizing()) {
      operations.add("Downsizing");
    }
    if (operationsAffected.getExpansion()) {
      operations.add("Expansion");
    }
    if (operationsAffected.getLargeScaleBreedingStockReplacement()) {
      operations.add("Large-scale breeding stock replacement");
    }
    if (operationsAffected.getChangeInCommoditiesOrVarieties()) {
      operations.add("Change in commodities or varieties");
    }
    if (operationsAffected.getChangeInOwnership()) {
      operations.add("Change in ownership");
    }
    if (operationsAffected.getDisasterOrDisease()) {
      operations.add("Disaster or disease");
    }
    if (operationsAffected.getOtherPleaseSpecify()) {
      operations.add("Other: " + specify);
    }

    String operationsString = String.join(", ", operations);
    if (operationsString.length() > 100) {
      return operationsString.substring(0, 100);
    }
    return operationsString;
  }
  
  private boolean getBooleanFromString(String yesNo) {
    if ("yes".equalsIgnoreCase(yesNo)) {
      return true;
    } 
    return false;
  }
  
  private String getMunicipalityCodeFromScenarioMetaData(List<ScenarioMetaData> updatedProgramYearMetadata) {

    String municipalityCode = null;
    for (ScenarioMetaData smd : updatedProgramYearMetadata) {
      if (smd.getMunicipalityCode() != null) {
        municipalityCode = smd.getMunicipalityCode();
        break;
      }
    }
    return municipalityCode;
  }

  private Integer getParticipantPin(CoverageSubmissionDataResource data) {
    return data.getAgriStabilityAgriInvestPin();
  }

  private Integer getProgramYear(CoverageSubmissionDataResource data) {
    Integer programYear = null;
    if (data.getProgramYear() != null && StringUtils.isNotBlank(data.getProgramYear().getValue())) {
      programYear = Integer.valueOf(data.getProgramYear().getValue());
    }
    return programYear;
  }

}
