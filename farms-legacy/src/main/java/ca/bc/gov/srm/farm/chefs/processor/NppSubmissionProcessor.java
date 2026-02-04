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
import static ca.bc.gov.srm.farm.chefs.forms.NppFormConstants.*;
import static ca.bc.gov.srm.farm.log.LoggingUtils.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JacksonException;

import ca.bc.gov.srm.farm.chefs.database.ChefsFormTypeCodes;
import ca.bc.gov.srm.farm.chefs.database.ChefsSubmissionStatusCodes;
import ca.bc.gov.srm.farm.chefs.resource.npp.NppSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionParentResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionResource;
import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.crm.CrmConstants;
import ca.bc.gov.srm.farm.crm.resource.CrmAccountResource;
import ca.bc.gov.srm.farm.crm.resource.CrmNppTaskResource;
import ca.bc.gov.srm.farm.crm.resource.CrmQueueResource;
import ca.bc.gov.srm.farm.crm.resource.CrmTaskResource;
import ca.bc.gov.srm.farm.crm.resource.CrmValidationErrorResource;
import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.NewParticipant;
import ca.bc.gov.srm.farm.domain.Person;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmission;
import ca.bc.gov.srm.farm.domain.codes.ParticipantClassCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.exception.TooManyRequestsException;
import ca.bc.gov.srm.farm.service.BenefitService;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ChefsSubmissionProcessorService;
import ca.bc.gov.srm.farm.service.CrmTransferService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.DateUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

public class NppSubmissionProcessor extends ChefsSubmissionProcessor<NppSubmissionDataResource> {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private static final String VALID_FORM_TASK_SUBJECT_FORMAT = "%d %s %d";
  private static final String VALID_FORM_LATE_TASK_SUBJECT_FORMAT = "%d LATE %s %d";

  private static final String VALID_FORM_TASK_DESCRIPTION_FORMAT = "Primary Farming Activity: %s";

  private static final String INVALID_FORM_TASK_SUBJECT_FORMAT = "%d %s %d";

  private static final String INVALID_FORM_TASK_DESCRIPTION_PREFIX_FORMAT = "%s %s form was submitted but has validation errors:\n\n"
      + "%s\n"
      + "Environment: %s\n\n"
      + "First Name: %s\n"
      + "Last Name: %s\n"
      + "Corporate Name: %s\n"
      + "Telephone: %s\n"
      + "Email: %s\n"
      + "Participant Type: %s\n"
      + "%s: %s\n"; // SIN / Business Number

  public NppSubmissionProcessor(Connection connection, String formUserType) {
    super(ChefsFormTypeCodes.NPP, FORM_SHORT_NAME, FORM_LONG_NAME, formUserType, connection);
  }

  private String corporateQueueId;
  private String individualQueueId;
  private String validationQueueId;

  @Override
  protected void processSubmission(String submissionGuid, String submissionResponseStr) {
    logMethodStart(logger);

    CrmTaskResource task = null;
    
    try {
      SubmissionParentResource<NppSubmissionDataResource> submissionMetaData = getSubmissionMetaData(submissionResponseStr, NppSubmissionDataResource.class);

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

  public CrmTaskResource processSubmission(SubmissionParentResource<NppSubmissionDataResource> submissionMetaData)
      throws ServiceException {
    logMethodStart(logger);

    CrmTaskResource newTask = null;

    SubmissionResource<NppSubmissionDataResource> submission = submissionMetaData.getSubmission();
    String submissionGuid = submissionMetaData.getSubmissionGuid();
    NppSubmissionDataResource data = submission.getData();
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

      List<String> validationErrors = validate(data, client);
      boolean hasErrors = !validationErrors.isEmpty();

      if (hasErrors) {
        CrmTaskResource existingValidationErrorTask = crmDao.getValidationErrorBySubmissionId(submissionGuid);
        if (existingValidationErrorTask == null) {
          newTask = createValidationErrorTask(crmAccount, data, validationErrors);
        } else {
          logger.debug("Validation error task already exists: "  + existingValidationErrorTask.toString());
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
      
        Objects.requireNonNull(participantPin); // prevent "potential null-pointer access" compiler warning

        submissionRec = createOrUpdateSubmission(submissionRec);
        Integer submissionId = submissionRec.getSubmissionId();
        
        CalculatorService calculatorService = ServiceFactory.getCalculatorService();
        ChefsSubmissionProcessorService chefsSubmissionProcessorService = ServiceFactory.getChefsSubmissionProcessorService();

        boolean createdParticipant = false;
        if (client == null) {
          NewParticipant participant = buildParticipantFromFormData(data);
          calculatorService.createNewParticipant(participant, ScenarioTypeCodes.CHEF, ScenarioCategoryCodes.CHEF_NPP,
              verifierUserEmail, submissionId, user);
          client = participant.getClient();
          createdParticipant = true;
        }
        chefsSubmissionProcessorService.createNppSupplementalData(data, client.getClientId(), programYear,
            getApplicationVersion(), createdParticipant, user);
        
        CrmTransferService crmTransferService = ServiceFactory.getCrmTransferService();
        crmAccount = crmTransferService.accountUpdate(connection, crmAccount, client, submissionGuid, formUserType, ChefsFormTypeCodes.NPP);

        logger.debug("Creating valid NPP task...");
        newTask = createValidNppTask(data, participantPin, crmAccount);
        submissionRec.setMainTaskGuid(newTask.getActivityId());

        try {

          Scenario scenario = calculatorService.loadScenario(participantPin, programYear, 1);

          if (data.getLateParticipant() != null && data.getLateParticipant() == true) {
            calculatorService.saveLateParticipantInd(scenario, scenario.getRevisionCount(), data.getLateParticipant(), user);

            BenefitService benefitService = ServiceFactory.getBenefitService();
            benefitService.calculateBenefit(scenario, user);
          }

          // Jobs are run in order created. Sleep so that the account update runs first.
          TimeUnit.SECONDS.sleep(2);
          
          scenario = calculatorService.loadScenario(participantPin, programYear, 1);
          crmTransferService.scheduleBenefitTransfer(scenario, verifierUserEmail, user);
        } catch (Exception e) {
          logger.error("Unexpected error: ", e);
          throw new ServiceException(e);
        }
        
        submissionRec.setSubmissionStatusCode(ChefsSubmissionStatusCodes.PROCESSED);
        submissionRec = createOrUpdateSubmission(submissionRec);

      }

    } catch (SQLException e) {
      logger.error("Unexpected error: ", e);
      throw new ServiceException(e);
    }

    logMethodEnd(logger, newTask);
    return newTask;
  }

  private NewParticipant buildParticipantFromFormData(NppSubmissionDataResource data) {
    NewParticipant participant = new NewParticipant();
    Client client = new Client();
    participant.setClient(client);

    client.setParticipantPin(getParticipantPin(data));
    client.setSin(data.getSinNumber());
    client.setBusinessNumber(data.getBusinessTaxNumberBn() + BUSINESS_NUMBER_SUFFIX);
    client.setTrustNumber(getPrefixedTrustNumber(data));
    
    if (isIndividual(data)) {
      client.setParticipantClassCode(ParticipantClassCodes.INDIVIDUAL);
    } else {
      client.setParticipantClassCode(ParticipantClassCodes.CORPORATION);
    }

    participant.setProgramYear(getProgramYear(data));
    participant.setMunicipalityCode(data.getMunicipalityCode());

    Person owner = createOwner(data);
    client.setOwner(owner);

    Person contact = createCraContact(data);
    client.setContact(contact);

    List<FarmingOperation> farmingOperations = new ArrayList<>();
    FarmingOperation farmingOperation = buildFarmingOperation(data);
    farmingOperations.add(farmingOperation);

    participant.setFarmingOperations(farmingOperations);

    return participant;
  }

  private Person createOwner(NppSubmissionDataResource data) {
    Person owner = new Person();
    
    if (isIndividual(data)) {
      owner.setFirstName(data.getFirstName());
      owner.setLastName(data.getLastName());
    } else {
      owner.setFirstName(data.getFirstNameCorporateContact());
      owner.setLastName(data.getLastNameCorporateContact());
    }
    owner.setCorpName(data.getCorporationName());
    owner.setCity(data.getTownCity());
    owner.setProvinceState(data.getProvince());
    owner.setPostalCode(data.getPostalCode().replace(" ", ""));
    owner.setDaytimePhone(data.getTelephone());
    owner.setEmailAddress(data.getEmail());

    owner.setAddressLine1(data.getAddress());
    owner.setAddressLine2("");
    return owner;
  }

  private Person createCraContact(NppSubmissionDataResource data) {
    Person craContact = new Person();
    craContact.setFirstName(data.getThirdPartyFirstName());
    craContact.setLastName(data.getThirdPartyLastName());
    craContact.setCorpName(data.getThirdPartyBusinessName());
    craContact.setCity(data.getThirdPartyTownCity());
    craContact.setProvinceState(data.getThirdPartyProvince());
    craContact.setDaytimePhone(data.getThirdPartyTelephone());
    craContact.setFaxNumber(data.getThirdPartyFax());
    craContact.setEmailAddress(data.getThirdPartyEmail());
    if (data.getThirdPartyPostalCode() != null) {
      craContact.setPostalCode(data.getThirdPartyPostalCode().replace(" ", ""));
    } else {
      craContact.setPostalCode(null);
    }

    craContact.setAddressLine1(data.getThirdPartyAddress());
    craContact.setAddressLine2("");
    return craContact;
  }

  private FarmingOperation buildFarmingOperation(NppSubmissionDataResource data) {
    FarmingOperation fo = new FarmingOperation();

    if (data.getAccountingCode() != null && data.getAccountingCode().equals("accrual")) {
      fo.setAccountingCode("1");
    } else {
      fo.setAccountingCode("2");
    }
    fo.setOperationNumber(1);

    FarmingYear fy = new FarmingYear();

    fo.setFarmingYear(fy);
    fo.setFiscalYearStart(getFiscalStartDate(data.getFiscalYearEnd()));
    fo.setFiscalYearEnd(data.getFiscalYearEnd());
    fo.setPartnershipPin(0);
    fo.setPartnershipPercent(1.00);

    return fo;
  }

  private List<String> validate(NppSubmissionDataResource data, Client client) {
    logMethodStart(logger);

    List<String> validationErrors = new ArrayList<>();

    validateRequiredField(validationErrors, FIELD_NAME_AGRISTABILITY_PIN, getParticipantPin(data));
    validateRequiredField(validationErrors, FIELD_NAME_FISCAL_YEAR_END, getProgramYear(data));
    
    if(StringUtils.isBlank(data.getBusinessStructure())) {
      String farmType = getFarmType(data);
      validateRequiredField(validationErrors, FIELD_NAME_FARM_TYPE, farmType);
    }

    if (isIndividual(data)) {
      validateRequiredField(validationErrors, FIELD_NAME_SIN_NUMBER, data.getSinNumber());
    }
    
    if (isBusinessNumberRequired(data)) {
      validateRequiredField(validationErrors, FIELD_NAME_BUSINESS_NUMBER, data.getBusinessTaxNumberBn());
      validateBusinessNumber(data.getBusinessTaxNumberBn(), validationErrors);
    }

    if (isTrustNumberRequired(data)) {
      validateRequiredField(validationErrors, FIELD_NAME_TRUST_NUMBER, data.getTrustNumber());
      validateTrustNumber(data.getTrustNumber(), validationErrors);
    }

    if (client != null) {
      if (client.getParticipantClassCode().equals(ParticipantClassCodes.INDIVIDUAL)) {

        if (isIndividual(data)) {
          validateValuesMatch(FIELD_NAME_SIN_NUMBER, data.getSinNumber(), client.getSin(), validationErrors);
        } else {
          validationErrors.add("The participant type \"Individual\" on the form does not match BCFARMS.");
        }
      }

      if (client.getParticipantClassCode().equals(ParticipantClassCodes.CORPORATION)) {

        if (isBusinessNumberRequired(data)) {
          validateBusinessNumbersMatch(data.getBusinessTaxNumberBn(), client.getBusinessNumber(), validationErrors);
        } else if (isTrustNumberRequired(data)) {
          validateTrustNumbersMatch(data.getTrustNumber(), client.getTrustNumber(), validationErrors);
        } else {
          validationErrors.add("The participant type \"Corporation\" on the form does not match BCFARMS.");
        }
      }
    }

    logMethodEnd(logger);
    return validationErrors;
  }

  private String getFarmType(NppSubmissionDataResource data) {
    String farmType = null;
    if(data.getFarmType() != null
        && data.getFarmType().getValue() != null) {
      farmType = data.getFarmType().getValue();
    }
    return farmType;
  }

  private boolean isIndividual(NppSubmissionDataResource data) {
    String farmType = getFarmType(data);
    return FIELD_VALUE_PARTICIPANT_TYPE_INDIVIDUAL.equals(data.getBusinessStructure())
        || FIELD_VALUE_FARM_TYPE_STATUS_INDIAN.equals(farmType)
        || FIELD_VALUE_FARM_TYPE_INDIVIDUAL.equals(farmType);
  }

  private boolean isBusinessNumberRequired(NppSubmissionDataResource data) {
    String farmType = getFarmType(data);
    return FIELD_VALUE_PARTICIPANT_TYPE_CORPORATION.equals(data.getBusinessStructure())
        || FIELD_VALUE_FARM_TYPE_CORPORATION.equals(farmType)
        || FIELD_VALUE_FARM_TYPE_COOPERATIVE.equals(farmType)
        || FIELD_VALUE_FARM_TYPE_MEMBER_OF_A_PARTNERSHIP.equals(farmType)
        || FIELD_VALUE_FARM_TYPE_LIMITED_PARTNERSHIP.equals(farmType);
  }

  private boolean isTrustNumberRequired(NppSubmissionDataResource data) {
    String farmType = getFarmType(data);
    return FIELD_VALUE_FARM_TYPE_COMMUNAL_ORGANIZATION.equals(farmType)
        || FIELD_VALUE_FARM_TYPE_TRUST.equals(farmType);
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
    Pattern pattern = Pattern.compile("^" + TRUST_NUMBER_PREFIX + "(\\d{8})");
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

  private CrmNppTaskResource createValidNppTask(NppSubmissionDataResource data, Integer participantPin,
      CrmAccountResource crmAccount) throws ServiceException {

    String method = getMethod(data);
    String primaryFarmingActivity = data.getWhatIsYourMainFarmingActivity();
    if (primaryFarmingActivity != null && primaryFarmingActivity.equals("otherPleaseSpecify")) {
      primaryFarmingActivity = data.getSpecifyOther();
    }

    String subjectFormatStr = VALID_FORM_TASK_SUBJECT_FORMAT;
    if (data.getLateParticipant() != null && data.getLateParticipant() == true) {
      subjectFormatStr = VALID_FORM_LATE_TASK_SUBJECT_FORMAT;
    }

    String subject = StringUtils.formatWithNullAsEmptyString(subjectFormatStr,
        getProgramYear(data), FORM_SHORT_NAME, participantPin);
    String description = StringUtils.formatWithNullAsEmptyString(VALID_FORM_TASK_DESCRIPTION_FORMAT, primaryFarmingActivity);

    String queueId = getQueueIdForParticipantType(data);

    String accountId = null;
    if (crmAccount != null) {
      accountId = crmAccount.getAccountid();
    }

    CrmNppTaskResource task = new CrmNppTaskResource();
    task.setSubject(subject);
    task.setDescription(description);
    task.setCr4dd_method(method);
    task.setCr4dd_origin(data.getOrigin());
    task.setCr4dd_businessnumber(getBusinessTaxNumberForCrm(data));
    task.setCr4dd_sin(data.getSinNumber());
    task.setCr4dd_primaryfarmingactivity(primaryFarmingActivity);
    task.setCr4dd_participanttype(StringUtils.ifNull(getFarmType(data), data.getBusinessStructure()));
    task.setAccountIdParameter(accountId);

    return crmDao.createValidNppTask(task, queueId);
  }

  private CrmTaskResource createValidationErrorTask(CrmAccountResource crmAccount, NppSubmissionDataResource data,
      List<String> validationErrors) throws ServiceException {

    Integer participantPin = getParticipantPin(data);
    String method = getMethod(data);
    String environment = data.getEnvironment();

    String primaryFarmingActivity = data.getWhatIsYourMainFarmingActivity();
    if (primaryFarmingActivity.equals("otherPleaseSpecify")) {
      primaryFarmingActivity = data.getSpecifyOther();
    }

    String sinBnFieldName;
    String sinBnValue = null;
    String businessNumber = null;
    if (isIndividual(data)) {
      sinBnFieldName = FIELD_NAME_SIN_NUMBER;
      sinBnValue = data.getSinNumber();
    } else {
      sinBnFieldName = FIELD_NAME_BUSINESS_NUMBER;
      String businessTaxNumber = getBusinessTaxNumberForCrm(data);
      if (StringUtils.isNotBlank(businessTaxNumber)) {
        sinBnValue = businessTaxNumber;
        businessNumber = sinBnValue;
      }
    }

    StringBuilder errorsText = new StringBuilder();

    for (String error : validationErrors) {
      errorsText.append("- ");
      errorsText.append(error);
      errorsText.append("\n");
    }

    Integer programYear = getProgramYear(data);
    programYear= (programYear == null) ? DateUtils.getYearFromDate(new Date()) : programYear;
    
    String subject = String.format(INVALID_FORM_TASK_SUBJECT_FORMAT, programYear,
        FORM_SHORT_NAME, participantPin);

    String farmType = StringUtils.ifNull(getFarmType(data), data.getBusinessStructure());
    String description = StringUtils.formatWithNullAsEmptyString(INVALID_FORM_TASK_DESCRIPTION_PREFIX_FORMAT, formUserType,
        FORM_LONG_NAME, errorsText, environment, data.getFirstName(), data.getLastName(),
        data.getCorporationName(), data.getTelephone(), data.getEmail(), farmType, sinBnFieldName,
        sinBnValue);

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
    task.setCr4dd_primaryfarmingactivity(primaryFarmingActivity);
    task.setCr4dd_participanttype(farmType);
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
      String queueName = configUtil.getValue(ConfigurationKeys.CRM_QUEUES_NPP_VALIDATION);
      validationQueueId = queryQueueId(queueName);
    }
    return validationQueueId;
  }

  private String queryQueueId(String queueName) throws ServiceException {
    CrmQueueResource queue = crmDao.getQueueByName(queueName);
    return queue.getQueueId();
  }

  private Date getFiscalStartDate(Date date) {
    LocalDate ld = DateUtils.convertToLocalDate(date);
    return DateUtils.convertToDate(ld.minusYears(1).plusDays(1));
  }

  private Client getClientByParticipantPin(Integer participantPin) throws ServiceException {
    Client client = null;
    try {
      if (participantPin != null) {
        client = readDAO.readClient(participantPin);
      }

    } catch (Exception e) {
      throw new ServiceException(e);
    }
    return client;
  }

  private String getApplicationVersion() {
    return configUtil.getValue(ConfigurationKeys.APPLICATION_VERSION);
  }

  private String getQueueIdForParticipantType(NppSubmissionDataResource data) throws ServiceException {
    String queueId;
    if (isIndividual(data)) {
      queueId = getIndividualQueueId();
    } else {
      queueId = getCorporateQueueId();
    }
    return queueId;
  }

  private String getCorporateQueueId() throws ServiceException {
    if (corporateQueueId == null) {
      String queueName = configUtil.getValue(ConfigurationKeys.CRM_QUEUES_NPP_CORPORATE);
      corporateQueueId = queryQueueId(queueName);
    }
    return corporateQueueId;
  }

  private String getIndividualQueueId() throws ServiceException {
    if (individualQueueId == null) {
      String queueName = configUtil.getValue(ConfigurationKeys.CRM_QUEUES_NPP_INDIVIDUAL);
      individualQueueId = queryQueueId(queueName);
    }
    return individualQueueId;
  }

  private String getBusinessTaxNumberForCrm(NppSubmissionDataResource data) {
    String businessTaxNumberBn = null;
    if(isTrustNumberRequired(data)) {
      businessTaxNumberBn = data.getTrustBusinessNumber();
    } else if(isBusinessNumberRequired(data)) {
      businessTaxNumberBn = data.getBusinessTaxNumberBn() + BUSINESS_NUMBER_SUFFIX;
    }
    return businessTaxNumberBn;
  }

  private String getPrefixedTrustNumber(NppSubmissionDataResource data) {
    String result = null;
    if(StringUtils.isNotBlank(data.getTrustNumber())) {
      result = TRUST_NUMBER_PREFIX + data.getTrustNumber();
    }
    return result;
  }

  private Integer getParticipantPin(NppSubmissionDataResource data) {
    return data.getAgriStabilityAgriInvestPin();
  }

  private Integer getProgramYear(NppSubmissionDataResource data) {
    return DateUtils.getYearFromDate(data.getFiscalYearEnd());
  }

}
