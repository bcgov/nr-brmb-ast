/**
 * Copyright (c) 2024,
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
import static ca.bc.gov.srm.farm.chefs.forms.StatementAFormConstants.*;
import static ca.bc.gov.srm.farm.log.LoggingUtils.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JacksonException;

import ca.bc.gov.srm.farm.chefs.database.ChefsFormTypeCodes;
import ca.bc.gov.srm.farm.chefs.forms.ChefsFarmTypeCodes;
import ca.bc.gov.srm.farm.chefs.resource.common.CropGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.CustomFeedGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.NurseryGrid;
import ca.bc.gov.srm.farm.chefs.resource.common.OtherPucGrid;
import ca.bc.gov.srm.farm.chefs.resource.statementA.StatementAPartner;
import ca.bc.gov.srm.farm.chefs.resource.statementA.StatementASubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionParentResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionResource;
import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.crm.CrmConstants;
import ca.bc.gov.srm.farm.crm.resource.CrmAccountResource;
import ca.bc.gov.srm.farm.crm.resource.CrmQueueResource;
import ca.bc.gov.srm.farm.crm.resource.CrmTaskResource;
import ca.bc.gov.srm.farm.crm.resource.CrmValidationErrorResource;
import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingOperationPartner;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.ScenarioMetaData;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmission;
import ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.exception.TooManyRequestsException;
import ca.bc.gov.srm.farm.service.BenefitTriageService;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ChefsSubmissionProcessorService;
import ca.bc.gov.srm.farm.service.CrmTransferService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.DataParseUtils;
import ca.bc.gov.srm.farm.util.DateUtils;
import ca.bc.gov.srm.farm.util.MathUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

public class StatementASubmissionProcessor extends ChefsSubmissionProcessor<StatementASubmissionDataResource> {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private static final String INVALID_FORM_TASK_SUBJECT_FORMAT = "%d %s %d";

  private static final String INVALID_FORM_TASK_DESCRIPTION_PREFIX_FORMAT = "%s %s form was submitted but has validation errors:\n\n" + "%s\n"
      + "Corporation Name: %s\n" + "Telephone: %s\n" + "Email: %s\n";

  private static final String DUPLICATE_PRODUCTIVE_UNIT_CODE = "The following productive unit codes have duplicates: %s";

  private static final String MISSING_PRODUCTIVE_UNITS = "No productive units entered";

  private static final String PARTNERSHIP = "partnership";
  private static final String ACCOUNTING_METHOD_CASH = "cash";

  public StatementASubmissionProcessor(Connection connection, String formUserType) {
    super(ChefsFormTypeCodes.STA, FORM_SHORT_NAME, FORM_LONG_NAME, formUserType, connection);
  }

  private String validationQueueId;
  
  private Integer triageImportVersionId; // only used by unit tests

  @Override
  protected void processSubmission(String submissionGuid, String submissionResponseStr) {
    logMethodStart(logger);

    CrmTaskResource task = null;

    try {
      SubmissionParentResource<StatementASubmissionDataResource> submissionMetaData = getSubmissionMetaData(submissionResponseStr,
          StatementASubmissionDataResource.class);

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

  public CrmTaskResource processSubmission(SubmissionParentResource<StatementASubmissionDataResource> submissionMetaData) throws ServiceException {
    logMethodStart(logger);

    CrmTaskResource newTask = null;
    
    this.triageImportVersionId = null; // only used by unit tests

    SubmissionResource<StatementASubmissionDataResource> submission = submissionMetaData.getSubmission();
    String submissionGuid = submissionMetaData.getSubmissionGuid();
    StatementASubmissionDataResource data = submission.getData();
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
      CrmTaskResource existingValidationErrorTask = getValidationErrorTask(submissionGuid);
      if (existingValidationErrorTask == null) {
        newTask = createValidationErrorTask(crmAccount, data, validationErrors, submissionGuid);
      } else {
        logger.debug("Validation error task already exists: " + existingValidationErrorTask.toString());
        if (existingValidationErrorTask.getStateCode() == CrmConstants.TASK_STATE_CODE_COMPLETED) {
          newTask = createValidationErrorTask(crmAccount, data, validationErrors, submissionGuid);
        } else {
          newTask = existingValidationErrorTask;
        }
      }

    } else {
      // Validation passed

      assert client != null;
      createScenario(data, client, programYear, submissionRec);

      populateCrmAccount(participantPin, crmAccount);

      CrmTransferService crmTransferService = ServiceFactory.getCrmTransferService();
      crmAccount = crmTransferService.accountUpdate(connection, crmAccount, client, submissionGuid, formUserType, ChefsFormTypeCodes.STA);
      
      queueBenefitTriage(participantPin, programYear, submissionGuid);
      
      setSubmissionProcessed(submissionGuid, null);
    }

    logMethodEnd(logger);
    return newTask;
  }


  private Integer createScenario(StatementASubmissionDataResource data, Client client, Integer programYear, ChefsSubmission submissionRecParam)
      throws ServiceException {

    ChefsSubmission submissionRec = submissionRecParam;
    Integer submissionId = submissionRec.getSubmissionId();
    Integer scenarioNumber = null;
    try {

      Integer participantPin = client.getParticipantPin();
      List<ScenarioMetaData> updatedProgramYearMetadata = readDAO.readProgramYearMetadata(participantPin, programYear);

      String municipalityCode = getMunicipalityCodeFromScenarioMetaData(updatedProgramYearMetadata);
      logger.debug("municipalityCode = " + municipalityCode);

      ChefsSubmissionProcessorService chefsSubmissionProcessorService = ServiceFactory.getChefsSubmissionProcessorService();
      chefsSubmissionProcessorService.createStatementAChefsScenario(data, client.getClientId(), programYear, getApplicationVersion(),
          municipalityCode, user, ScenarioCategoryCodes.CHEF_STA);

      updatedProgramYearMetadata = readDAO.readProgramYearMetadata(participantPin, programYear);

      ScenarioMetaData chefScenarioMetaData = ScenarioUtils.findScenarioByCategory(updatedProgramYearMetadata, programYear,
          ScenarioCategoryCodes.CHEF_STA, ScenarioTypeCodes.CHEF);
      scenarioNumber = chefScenarioMetaData.getScenarioNumber();

      CalculatorService calculatorService = ServiceFactory.getCalculatorService();
      Scenario scenario = calculatorService.loadScenario(participantPin, programYear, scenarioNumber);

      Integer scenarioId = scenario.getScenarioId();
      updateScenarioSubmissionId(submissionId, scenarioId);


      Date currentDate = new Date();

      // TODO Review for efficency. Reloading the Scenario repeatedly is costly.
      scenario = calculatorService.loadScenario(participantPin, programYear, scenario.getScenarioNumber());
      FarmingYear fy = populateFarmYear(data, scenario, currentDate);
      calculatorService.updateFarmingYear(fy, user);

      scenario = calculatorService.loadScenario(participantPin, programYear, scenario.getScenarioNumber());
      FarmingOperation fo = populateFarmingOperation(data, scenario);
      calculatorService.updateFarmingOperation(fo, user);

      scenario = calculatorService.loadScenario(participantPin, programYear, scenario.getScenarioNumber());
      List<FarmingOperationPartner> partners = new ArrayList<>();
      populatePartners(data, fo, partners);
      if (partners.size() > 0) {
        calculatorService.updateFarmingOperationPartners(scenario, new ArrayList<>(), partners, new ArrayList<>(), user);
      }

      String reasonForApplying = null;
      scenario.setChefsSubmissionGuid(data.getSubmissionGuid());
      calculatorService.updateProgramYearLocalReceivedDates(scenario, currentDate, currentDate, user, verifierUserEmail,
          reasonForApplying, formUserType, ChefsFormTypeCodes.STA, null);

    } catch (SQLException | DataAccessException | ParseException e) {
      logger.error("Unexpected error: ", e);
      throw new ServiceException(e);
    }
    return scenarioNumber;

  }


  private void queueBenefitTriage(Integer participantPin, Integer programYear, String submissionGuid) throws ServiceException {
    
    BenefitTriageService triageService = ServiceFactory.getBenefitTriageService();
    
    String triageJobDescription = String.format("Benefit Triage Calculation for %d PIN %d, %s form submissionGuid: %s",
        programYear, participantPin, formLongName, submissionGuid);
    this.triageImportVersionId = triageService.queueBenefitTriage(triageJobDescription, connection, user);
  }


  private void populatePartners(StatementASubmissionDataResource data, FarmingOperation fo, List<FarmingOperationPartner> partners)
      throws ServiceException {
    if (data.getPartnershipGrid() != null && data.getPartnershipGrid().size() > 0) {
      for (StatementAPartner sap : data.getPartnershipGrid()) {
        if (sap.getPin() != null) {
          FarmingOperationPartner p = new FarmingOperationPartner();
          p.setFarmingOperation(fo);
          p.setParticipantPin(sap.getPin());
          Client client = getClientByParticipantPin(sap.getPin());
          if (client != null) {
            p.setFirstName(client.getOwner().getFirstName());
            p.setLastName(client.getOwner().getLastName());
            p.setCorpName(client.getOwner().getCorpName());
          }
          if (sap.getPercent() > 0) {
            p.setPartnerPercent(BigDecimal.valueOf(sap.getPercent() / 100));
          } else {
            p.setPartnerPercent(BigDecimal.valueOf(0));
          }
          partners.add(p);
        }
      }
    }
  }

  private void populateCrmAccount(Integer participantPin, CrmAccountResource crmAccount) throws ServiceException {
    Client client = getClientByParticipantPin(participantPin);

    crmAccount.setName(client.getOwner().getCorpName());
    crmAccount.setVsi_firstname(client.getOwner().getFirstName());
    crmAccount.setVsi_lastname(client.getOwner().getLastName());
    crmAccount.setEmailaddress1(client.getOwner().getEmailAddress());
    crmAccount.setAddress1_line1(client.getOwner().getAddressLine1());
    crmAccount.setAddress1_city(client.getOwner().getCity());
    crmAccount.setAddress1_stateorprovince(client.getOwner().getProvinceState());
    crmAccount.setAddress1_postalcode(client.getOwner().getPostalCode());
    crmAccount.setTelephone1(client.getOwner().getDaytimePhone());
    crmAccount.setAddress1_country(client.getOwner().getCountry());

    crmAccount.setAddress2_name(client.getContact().getFirstName());
    crmAccount.setAddress1_primarycontactname(client.getContact().getLastName());
    crmAccount.setAddress2_county(client.getContact().getCorpName());
    crmAccount.setAddress2_telephone1(client.getContact().getDaytimePhone());
    crmAccount.setEmailaddress2(client.getContact().getEmailAddress());
    crmAccount.setAddress2_line1(client.getContact().getAddressLine1());
    crmAccount.setAddress2_city(client.getContact().getCity());
    crmAccount.setAddress2_stateorprovince(client.getContact().getProvinceState());
    crmAccount.setAddress2_postalcode(client.getContact().getPostalCode());
    crmAccount.setAddress2_fax(client.getContact().getFaxNumber());
    crmAccount.setAddress2_country(client.getContact().getCountry());
  }

  private FarmingYear populateFarmYear(StatementASubmissionDataResource data, Scenario scenario, Date currentDate) throws ParseException {
    FarmingYear fy = scenario.getFarmingYear();

    fy.setProvinceOfResidence(data.getProvince());
    fy.setProvinceOfMainFarmstead(data.getProvinceTerritoryOfMainFarmstead());
    fy.setPostMarkDate(currentDate);
    fy.setCraStatementAReceivedDate(currentDate);
    fy.setFarmYears(data.getNumberOfYearsFarmed());
    fy.setIsCanSendCobToRep(DataParseUtils.parseBoolean(data.getCopyOfCOB()));
    fy.setIsLastYearFarming(DataParseUtils.parseBoolean(data.getWas2024FinalYearOfFarming()));
    fy.setIsCompletedProdCycle(DataParseUtils.parseBoolean(data.getCompletedProductionCycle()));
    if (!DataParseUtils.parseBoolean(data.getCompletedProductionCycle())) {
      fy.setIsDisaster(DataParseUtils.parseBoolean(data.getUnableToCompleteBecauseOfDisaster()));
    }

    if (PARTNERSHIP.equals(data.getBusinessStructure())) {
      fy.setIsPartnershipMember(true);
    } else {
      fy.setIsSoleProprietor(true);
    }

    switch (data.getFarmType().getValue()) {
    case ChefsFarmTypeCodes.CORPORATION:
      fy.setIsCorporateShareholder(true);
      break;
    case ChefsFarmTypeCodes.CO_OPERATIVE:
      fy.setIsCoopMember(true);
      break;
    case ChefsFarmTypeCodes.MEMBER_OF_A_PARTNERSHIP:
      fy.setIsPartnershipMember(true);
      break;
    }

    return fy;
  }

  private FarmingOperation populateFarmingOperation(StatementASubmissionDataResource data, Scenario scenario) {
    FarmingOperation fo = scenario.getFarmingYear().getFarmingOperations().get(0);

    fo.setFiscalYearStart(data.getFiscalYearStartDate());
    fo.setFiscalYearEnd(data.getFiscalYearEndDate());
    fo.setGrossIncome(data.getTotalIncome());
    fo.setFarmingExpenses(data.getTotalExpenses());
    fo.setInventoryAdjustments(data.getTotalAdjustments());
    fo.setNetIncomeBeforeAdj(data.getNetIncomeBeforeAdjustments());
    fo.setNetIncomeAfterAdj(data.getNetIncomeAfterAdjustments());

    if (ACCOUNTING_METHOD_CASH.equals(data.getAccountingMethod())) {
      fo.setAccountingCode("2");
    } else {
      fo.setAccountingCode("1");
    }
    
    if (PARTNERSHIP.equals(data.getBusinessStructure()) && 
        data.getFarmingOperationPercentage() != null && 
        data.getFarmingOperationPercentage() > 0.0) {
      fo.setPartnershipPercent(data.getFarmingOperationPercentage()/100.00);
    } 
    return fo;
  }

  private List<String> validate(StatementASubmissionDataResource data, Client client, CrmAccountResource crmAccount) {
    logMethodStart(logger);

    List<String> validationErrors = new ArrayList<>();

    validateRequiredField(validationErrors, FIELD_NAME_AGRISTABILITY_PIN, getParticipantPin(data));
    validateRequiredField(validationErrors, FIELD_NAME_FARM_TYPE, data.getFarmType().getLabel());

    String farmType = getFarmType(data);
    validateRequiredField(validationErrors, FIELD_NAME_FARM_TYPE, farmType);

    if (isIndividual(data)) {
      validateRequiredField(validationErrors, FIELD_NAME_SIN_NUMBER, data.getSinNumber());
    }
    
    if (isBusinessNumberRequired(data)) {
      boolean bnValid = validateRequiredField(validationErrors, FIELD_NAME_BUSINESS_NUMBER, data.getBusinessTaxNumber());
      if(bnValid) {
        validateBusinessNumber(data.getBusinessTaxNumber(), validationErrors);
      }
    }

    if (isTrustNumberRequired(data)) {
      boolean tnValid = validateRequiredField(validationErrors, FIELD_NAME_TRUST_NUMBER, data.getTrustNumber());
      if(tnValid) {
        validateTrustNumber(data.getTrustNumber(), validationErrors);
      }
    }

    if (crmAccount == null) {
      validationErrors.add("PIN not found in CRM.");
    }

    if (client == null) {
      validationErrors.add("PIN not found in BCFARMS.");

    } else if (validationErrors.isEmpty()) {

      if (ChefsFarmTypeCodes.STATUS_INDIAN_FARMING_ON_A_RESERVE.equals(data.getFarmType().getValue())) {
        validateValuesMatch(FIELD_NAME_SIN_NUMBER, data.getSinNumber(), client.getSin(), validationErrors);
      } else if (ChefsFarmTypeCodes.COMMUNAL_ORGANIZATION.equals(data.getFarmType().getValue()) || 
          ChefsFarmTypeCodes.TRUST.equals(data.getFarmType().getValue())) {
        validateTrustNumbersMatch(data.getTrustNumber(), client.getTrustNumber(), validationErrors);
      } else {
        validateBusinessNumbersMatch(data.getBusinessTaxNumber(), client.getBusinessNumber(), validationErrors);
      }

      validateProductiveUnits(data, validationErrors);
    }

    logMethodEnd(logger);
    return validationErrors;
  }

  private String getFarmType(StatementASubmissionDataResource data) {
    String farmType = null;
    if(data.getFarmType() != null
        && data.getFarmType().getValue() != null) {
      farmType = data.getFarmType().getValue();
    }
    return farmType;
  }

  private boolean isIndividual(StatementASubmissionDataResource data) {
    String farmType = getFarmType(data);
    return FIELD_VALUE_PARTICIPANT_TYPE_INDIVIDUAL.equals(data.getBusinessStructure())
        || FIELD_VALUE_FARM_TYPE_STATUS_INDIAN.equals(farmType)
        || FIELD_VALUE_FARM_TYPE_INDIVIDUAL.equals(farmType);
  }

  private boolean isBusinessNumberRequired(StatementASubmissionDataResource data) {
    String farmType = getFarmType(data);
    return FIELD_VALUE_PARTICIPANT_TYPE_CORPORATION.equals(data.getBusinessStructure())
        || FIELD_VALUE_FARM_TYPE_CORPORATION.equals(farmType)
        || FIELD_VALUE_FARM_TYPE_COOPERATIVE.equals(farmType)
        || FIELD_VALUE_FARM_TYPE_MEMBER_OF_A_PARTNERSHIP.equals(farmType)
        || FIELD_VALUE_FARM_TYPE_LIMITED_PARTNERSHIP.equals(farmType);
  }

  private boolean isTrustNumberRequired(StatementASubmissionDataResource data) {
    String farmType = getFarmType(data);
    return FIELD_VALUE_FARM_TYPE_COMMUNAL_ORGANIZATION.equals(farmType)
        || FIELD_VALUE_FARM_TYPE_TRUST.equals(farmType);
  }

  private CrmTaskResource createValidationErrorTask(CrmAccountResource crmAccount, StatementASubmissionDataResource data,
      List<String> validationErrors, String submissionGuid) throws ServiceException {

    Integer participantPin = getParticipantPin(data);
    Integer programYear = getProgramYear(data);
    String method = getMethod(data);

    String businessNumber = null;
    if (StringUtils.isNotBlank(data.getBusinessTaxNumber())) {
      businessNumber = data.getBusinessTaxNumber() + BUSINESS_NUMBER_SUFFIX;
    }
    if(StringUtils.isNotBlank(data.getTrustNumber())) {
      businessNumber = TRUST_NUMBER_PREFIX + data.getTrustNumber();
    }

    StringBuilder errorsText = new StringBuilder();

    for (String error : validationErrors) {
      errorsText.append("- ");
      errorsText.append(error);
      errorsText.append("\n");
    }

    String subject = String.format(INVALID_FORM_TASK_SUBJECT_FORMAT, programYear, FORM_SHORT_NAME, participantPin);

    String description = StringUtils.formatWithNullAsEmptyString(INVALID_FORM_TASK_DESCRIPTION_PREFIX_FORMAT, formUserType, FORM_LONG_NAME,
        errorsText, data.getCorporationName(), data.getTelephone(), data.getEmail());

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
    task.setCr4dd_businessnumber(businessNumber);
    task.setCr4dd_sinbn(data.getSinNumber());

    StringBuilder chefsSubmissionUrl = new StringBuilder();
    chefsSubmissionUrl.append(configUtil.getValue(ConfigurationKeys.CHEFS_UI_URL));
    chefsSubmissionUrl.append(URL_VIEW_CHEFS_SUBMISSION_FORM);
    chefsSubmissionUrl.append(data.getSubmissionGuid());
    task.setCr4dd_chefsurl(chefsSubmissionUrl.toString());

    String queueId = getValidationQueueId();
    CrmValidationErrorResource newTask = crmDao.createValidationErrorTask(task, queueId);
    
    setSubmissionInvalid(submissionGuid, newTask);
    
    return newTask;
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
      validationErrors.add(FIELD_NAME_BUSINESS_NUMBER + " in BCFARMS does not start with a 9 digit number. Unable to validate.");
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

  private boolean validateRequiredField(List<String> validationErrors, String field, String value) {
    boolean result = true;
    if (StringUtils.isBlank(value)) {
      validationErrors.add(requiredFieldError(field));
      result = false;
    }
    return result;
  }

  private boolean validateRequiredField(List<String> validationErrors, String field, Integer value) {
    boolean result = true;
    if (value == null) {
      validationErrors.add(requiredFieldError(field));
      result = false;
    }
    return result;
  }

  private void validateProductiveUnits(StatementASubmissionDataResource data, List<String> validationErrors) {

    List<String> berryGrid = extractCodesFromCropGrid(data.getBerryGrid());
    List<String> treeFruitGrid = extractCodesFromCropGrid(data.getTreeFruitGrid());
    List<String> vegetableGrid = extractCodesFromCropGrid(data.getVegetableGrid());
    List<String> grainGrid = extractCodesFromCropGrid(data.getGrainGrid());
    List<String> nurseryGrid = extractCodesFromNurseryGrid(data.getNurseryGrid());
    List<String> neHorticultureGrid = extractCodesFromCropGrid(data.getNeHorticultureGrid());
    List<String> customFedGrid = extractCodesFromCustomFeedGrid(data.getCustomFedGrid());
    List<String> opdGrid = extractCodesFromOtherPucGrid(data.getOpdGrid());

    List<String> allCodes = new ArrayList<>();
    allCodes.addAll(berryGrid);
    allCodes.addAll(treeFruitGrid);
    allCodes.addAll(vegetableGrid);
    allCodes.addAll(grainGrid);
    allCodes.addAll(nurseryGrid);
    allCodes.addAll(neHorticultureGrid);
    allCodes.addAll(customFedGrid);
    allCodes.addAll(opdGrid);

    List<String> duplicateCodes =
        allCodes.stream()
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
        .entrySet().stream()
        .filter(e -> e.getValue() > 1)                 // keep only those occurring > 1
        .map(Map.Entry::getKey)
        .collect(Collectors.toList());

    boolean hasSingleFieldLivestockPucs = 
        MathUtils.isNonZero(data.getNumberOfCowsThatCalved())
        || MathUtils.isNonZero(data.getNumberFeedersUnder9())
        || MathUtils.isNonZero(data.getNumberFeedersOver9())
        || MathUtils.isNonZero(data.getEggsForHatchingLC108())
        || MathUtils.isNonZero(data.getEggsForConsumptionLC109())
        || MathUtils.isNonZero(data.getChickenBroilersLC143())
        || MathUtils.isNonZero(data.getTurkeyBroilersLC144())
        || MathUtils.isNonZero(data.getNumberOfSowsThatFarrowedLC123())
        || MathUtils.isNonZero(data.getNumberOfHogsFedUpTo50LbsLC124())
        || MathUtils.isNonZero(data.getNumberOfHogsFedOver50LbsFeedersLC125());

    if( allCodes.isEmpty() && !hasSingleFieldLivestockPucs ) {
      validationErrors.add(MISSING_PRODUCTIVE_UNITS);
    }

    if( ! duplicateCodes.isEmpty() ) {
      String duplicateCodesString = StringUtils.toCsv(duplicateCodes);
      validationErrors.add(String.format(DUPLICATE_PRODUCTIVE_UNIT_CODE, duplicateCodesString));
    }
  }

  private List<String> extractCodesFromCropGrid(List<? extends CropGrid> grid) {
    @SuppressWarnings("unchecked")
    List<String> emptyList = Collections.EMPTY_LIST;
    return grid == null ? emptyList :
      grid.stream()
        .filter(p -> p.getCommodity() != null
          && StringUtils.isNotBlank(p.getCommodity().getValue())
          && (p.getAcres() != null && p.getAcres() > 0)
        )
        .map(p -> p.getCommodity().getValue())
        .collect(Collectors.toList());
  }

  private List<String> extractCodesFromNurseryGrid(List<? extends NurseryGrid> grid) {
    @SuppressWarnings("unchecked")
    List<String> emptyList = Collections.EMPTY_LIST;
    return grid == null ? emptyList :
      grid.stream()
        .filter(p -> p.getCommodity() != null
            && StringUtils.isNotBlank(p.getCommodity().getValue())
            && (p.getSquareMeters() != null && p.getSquareMeters() > 0)
        )
        .map(p -> p.getCommodity().getValue())
        .collect(Collectors.toList());
  }

  private List<String> extractCodesFromCustomFeedGrid(List<CustomFeedGrid> grid) {
    @SuppressWarnings("unchecked")
    List<String> emptyList = Collections.EMPTY_LIST;
    return grid == null ? emptyList :
      grid.stream()
        .filter(p -> p.getTypeOfAnimalCustomFed() != null
          && StringUtils.isNotBlank(p.getTypeOfAnimalCustomFed().getValue())
          && (p.getNumberOfAnimalsCustomFed() != null && p.getNumberOfAnimalsCustomFed() > 0)
        )
        .map(p -> p.getTypeOfAnimalCustomFed().getValue())
        .collect(Collectors.toList());
  }

  private List<String> extractCodesFromOtherPucGrid(List<OtherPucGrid> grid) {
    @SuppressWarnings("unchecked")
    List<String> emptyList = Collections.EMPTY_LIST;
    return grid == null ? emptyList :
      grid.stream()
        .filter(p -> p.getSelectOtherLivestock() != null
          && StringUtils.isNotBlank(p.getSelectOtherLivestock().getValue())
          && (p.getOtherLivestockNumber() != null && p.getOtherLivestockNumber() > 0)
        )
        .map(p -> p.getSelectOtherLivestock().getValue())
        .collect(Collectors.toList());
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
      String queueName = configUtil.getValue(ConfigurationKeys.CRM_QUEUES_STATEMENT_A_VALIDATION);
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

  private void validateBusinessNumber(String chefsValue, List<String> validationErrors) {

    String chefsBn = chefsValue.trim().replaceAll("\\s", "");

    Pattern pattern = Pattern.compile("^(\\d{9})");
    Matcher matcher = pattern.matcher(chefsBn);

    if (!matcher.find()) {
      validationErrors
          .add(FIELD_NAME_BUSINESS_NUMBER + " in BCFARMS does not start with a 9 digit number. Unable to validate.");
    }
  }

  private void validateTrustNumber(String chefsValue, List<String> validationErrors) {

    String chefsBn = chefsValue.trim().replaceAll("\\s", "");

    Pattern pattern = Pattern.compile("^(\\d{8})");
    Matcher matcher = pattern.matcher(chefsBn);

    if (!matcher.find()) {
      validationErrors
          .add(FIELD_NAME_TRUST_NUMBER + " in BCFARMS does not start with a 8 digit number. Unable to validate.");
    }
  }
  
  private Integer getParticipantPin(StatementASubmissionDataResource data) {
    return data.getAgriStabilityAgriInvestPin();
  }

  private Integer getProgramYear(StatementASubmissionDataResource data) {
    return DateUtils.getYearFromDate(data.getFiscalYearEndDate());
  }

  /**
   * only used by unit tests
   */
  public Integer getTriageImportVersionId() {
    return triageImportVersionId;
  }

}
