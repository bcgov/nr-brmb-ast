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

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.struts.action.ActionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JacksonException;

import ca.bc.gov.srm.farm.calculator.BenefitNullFixer;
import ca.bc.gov.srm.farm.calculator.CalculatorFactory;
import ca.bc.gov.srm.farm.chefs.ChefsConstants;
import ca.bc.gov.srm.farm.chefs.database.ChefsFormTypeCodes;
import ca.bc.gov.srm.farm.chefs.resource.npp.NppCommodityGrid;
import ca.bc.gov.srm.farm.chefs.resource.npp.NppCropGrid;
import ca.bc.gov.srm.farm.chefs.resource.npp.NppSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.npp.PartnershipInformation;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionParentResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionResource;
import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.crm.CrmConstants;
import ca.bc.gov.srm.farm.crm.resource.CrmAccountResource;
import ca.bc.gov.srm.farm.crm.resource.CrmEnrolmentResource;
import ca.bc.gov.srm.farm.crm.resource.CrmNppTaskResource;
import ca.bc.gov.srm.farm.crm.resource.CrmProgramYearResource;
import ca.bc.gov.srm.farm.crm.resource.CrmQueueResource;
import ca.bc.gov.srm.farm.crm.resource.CrmTaskResource;
import ca.bc.gov.srm.farm.crm.resource.CrmValidationErrorResource;
import ca.bc.gov.srm.farm.domain.Client;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingOperationPartner;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.NewParticipant;
import ca.bc.gov.srm.farm.domain.Person;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.ScenarioMetaData;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmission;
import ca.bc.gov.srm.farm.domain.codes.ParticipantClassCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.domain.enrolment.EnwEnrolment;
import ca.bc.gov.srm.farm.enrolment.EnrolmentCalculatorFactory;
import ca.bc.gov.srm.farm.enrolment.EnwEnrolmentCalculator;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.exception.TooManyRequestsException;
import ca.bc.gov.srm.farm.service.BenefitService;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ChefsSubmissionProcessorService;
import ca.bc.gov.srm.farm.service.CrmTransferService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.DateUtils;
import ca.bc.gov.srm.farm.util.MathUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;
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

  private static final String DUPLICATE_PRODUCTIVE_UNIT_CODE = "The following productive unit codes have duplicates: %s";
  
  private static final String MISSING_PRODUCTIVE_UNITS = "No productive units entered";

  private static final String ENW_MESSAGE_FOUND_EXISTING_ENROLMENT_NOTICE_WORKFLOW_SCENARIO =
      "Found unexpected existing Enrolment Notice Workflow scenario. Please review.";
  
  private static final String ENW_MESSAGE_NOT_CALCULATED_DUE_TO_STATUS_FORMAT =
      "Enrolment not calculated: %s";
  
  private static final String ERROR_ENW_CALCULATION_FAILED = "Enrolment Fee calculation failed.";
  private static final String ERROR_ENW_MISSING_BPU = "Missing BPUs for program year Productive Units.";
  private static final String ERROR_ENW_NO_PRODUCTIVE_UNITS = "Program year has no Productive Units.";
  private static final String ERROR_ENW_COMBINED_FARM_PERCENT = "Benefit calculation failed so Combined Farm % has not been calculated.";
  
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


    try {
      submissionRec = chefsSubmissionProcessData.getChefsSubmission();
      if(submissionRec == null) {
        submissionRec = newSubmissionRecord(submissionGuid);

        if(ChefsConstants.USER_TYPE_BASIC_BCEID.equals(getFormUserType())) {
          newTask = createValidationErrorTaskForNppBceid(data, submissionGuid);
          return newTask;
        }
      }

      Client client = null;
      CrmAccountResource crmAccount = null;
      if (participantPin != null) {
        client = getClientByParticipantPin(participantPin);
        crmAccount = getCrmAccountByParticipantPin(participantPin);
      } else {
        newTask = createValidationErrorTaskForNppNoPin(data, submissionGuid);
        return newTask;
      }

      List<String> validationErrors = validate(data, client);
      boolean hasErrors = !validationErrors.isEmpty();

      if (hasErrors) {
        CrmTaskResource existingValidationErrorTask = getValidationErrorTask(submissionGuid);
        if (existingValidationErrorTask == null) {
          newTask = createValidationErrorTask(crmAccount, data, validationErrors, submissionGuid);
        } else {
          logger.debug("Validation error task already exists: "  + existingValidationErrorTask.toString());
          if (existingValidationErrorTask.getStateCode() == CrmConstants.TASK_STATE_CODE_COMPLETED) {
            newTask = createValidationErrorTask(crmAccount, data, validationErrors, submissionGuid);
          } else {
            newTask = existingValidationErrorTask;
          }
        }

      } else {
      
        Objects.requireNonNull(participantPin); // prevent "potential null-pointer access" compiler warning

        submissionRec = createOrUpdateSubmission(submissionRec);
        Integer submissionId = submissionRec.getSubmissionId();
        
        CalculatorService calculatorService = ServiceFactory.getCalculatorService();
        ChefsSubmissionProcessorService chefsSubmissionProcessorService = ServiceFactory.getChefsSubmissionProcessorService();

        boolean createdParticipant = false;
        NewParticipant participant = buildParticipantFromFormData(data);
        if (client == null) {
          calculatorService.createNewParticipant(participant, ScenarioTypeCodes.CHEF, ScenarioCategoryCodes.CHEF_NPP,
              verifierUserEmail, submissionId, user);
          client = participant.getClient();
          createdParticipant = true;
        }
        
        List<ScenarioMetaData> programYearMetadata = readDAO.readProgramYearMetadata(participantPin, programYear);
        ScenarioMetaData chefNppScenarioMetaData = ScenarioUtils.findScenarioByCategory(
            programYearMetadata, programYear, ScenarioCategoryCodes.CHEF_NPP, submissionGuid);
        
        if(chefNppScenarioMetaData == null) {
          chefsSubmissionProcessorService.createNppSupplementalData(data, client.getClientId(), programYear,
              getApplicationVersion(), createdParticipant, submissionId, participant.getFarmingOperations(), user);
          
          programYearMetadata = readDAO.readProgramYearMetadata(participantPin, programYear);
          chefNppScenarioMetaData = ScenarioUtils.findLatestScenarioByChefSubmissionGuid(
              programYearMetadata, programYear, ScenarioCategoryCodes.CHEF_NPP, submissionGuid);
        }
        
        CrmTransferService crmTransferService = ServiceFactory.getCrmTransferService();
        crmAccount = crmTransferService.accountUpdate(connection, crmAccount, client, submissionGuid, formUserType, ChefsFormTypeCodes.NPP);

        Integer latestChefNppScenarioNumber = chefNppScenarioMetaData.getScenarioNumber();
        Scenario chefNppScenario = calculatorService.loadScenario(participantPin, programYear, latestChefNppScenarioNumber);

        if (data.getLateParticipant() != null && data.getLateParticipant() == true) {
          calculatorService.saveLateParticipantInd(chefNppScenario, chefNppScenario.getRevisionCount(), data.getLateParticipant(), user);
          
          // getEnrolmentStatusCode waits until the Enrolment has been created in CRM
          @SuppressWarnings("unused")
          Integer enrolmentStatusCode = getEnrolmentStatusCode(crmAccount, programYear);
        }

        chefNppScenario = calculatorService.loadScenario(participantPin, programYear, latestChefNppScenarioNumber);
        String chefsFormNotes = null;
        String benefitTriageResultType = null;
        crmTransferService.immediateBenefitTransfer(chefNppScenario, verifierUserEmail, user,
            chefsFormNotes, formUserType, formTypeCode, benefitTriageResultType, connection);
        
        // Check enrolment existence/status before creating an ENW scenario
        Integer enrolmentStatusCode = getEnrolmentStatusCode(crmAccount, programYear);
        boolean success;
        String successMessage = null;
        
        if(enrolmentStatusCode == null
            || enrolmentStatusCode == CrmConstants.ENROLMENT_STATUS_CODE_INITIALIZED
            || enrolmentStatusCode == CrmConstants.ENROLMENT_STATUS_CODE_TO_BE_REVIEWED) {
          
          // In case this process previously failed part way through, check if we already created an ENW scenario
          Scenario enwScenario = findOrCreateEnwScenario(participantPin, programYear, programYearMetadata, chefNppScenario,
              submissionGuid, submissionId);
          
          if(enwScenario.stateIsOneOf(ScenarioStateCodes.IN_PROGRESS)) {
            
            List<String> enwErrors = calculateEnrolment(enwScenario);
            
            if(enwErrors.isEmpty()) {
              success = true;
            } else {
              success = false;
              logger.info("NPP Enrolment calculation failed. Errors: " + enwErrors);
              
              newTask = createValidationErrorTask(crmAccount, data, enwErrors, submissionGuid);
            }
            
          } else {
            
            success = true;
            
            if(enwScenario.stateIsOneOf(ScenarioStateCodes.COMPLETED)) {
              successMessage = ENW_MESSAGE_FOUND_EXISTING_ENROLMENT_NOTICE_WORKFLOW_SCENARIO;
            }
            
          }
        } else {
          
          success = true;
          
          if(enrolmentStatusCode == CrmConstants.ENROLMENT_STATUS_CODE_INELIGIBLE) {
            successMessage = String.format(ENW_MESSAGE_NOT_CALCULATED_DUE_TO_STATUS_FORMAT,
                CrmConstants.getEnrolmentStatusDescription(enrolmentStatusCode));
          }
        }

        if(success) {
          logger.debug("Creating valid NPP task...");
          newTask = createValidNppTask(data, participantPin, crmAccount, successMessage, submissionGuid);
        }

      }

    } catch (SQLException e) {
      logger.error("Unexpected error: ", e);
      throw new ServiceException(e);
    }

    logMethodEnd(logger, newTask);
    return newTask;
  }

  private void waitForDynamicsFlows(int waitSeconds) {
    try {
      TimeUnit.SECONDS.sleep(waitSeconds);
    } catch (InterruptedException e) {
      logger.error("InterruptedException: ", e);
    }
  }

  private List<String> calculateEnrolment(Scenario enwScenarioParameter)
      throws ServiceException {
    
    Scenario enwScenario = enwScenarioParameter;
    BenefitService benefitService = ServiceFactory.getBenefitService();
    CalculatorService calculatorService = ServiceFactory.getCalculatorService();
    
    setBpuLead(enwScenario);
    
    ActionMessages errors = benefitService.calculateBenefit(enwScenario, user);
    boolean benefitCalculated = errors.isEmpty();
    
    EnwEnrolmentCalculator enwEnrolmentCalculator = EnrolmentCalculatorFactory.getEnwEnrolmentCalculator();
    enwEnrolmentCalculator.initNonEditableFields(enwScenario);
    EnwEnrolment enw = enwScenario.getEnwEnrolment();
    enw.setEnrolmentCalculationTypeCode(EnwEnrolment.CALCULATION_TYPE_PROXY_MARGINS);
    
    calculatorService.calculateEnwEnrolment(enwScenario, benefitCalculated, user);
    
    boolean enrolmentCalculated = enw.getProxyMarginsCalculated();
    List<String> enwErrors = new ArrayList<>();
    
    if(!enrolmentCalculated) {
      enwErrors.add(ERROR_ENW_CALCULATION_FAILED);
      if( ! enw.getHasBpus() ) {
        enwErrors.add(ERROR_ENW_MISSING_BPU);
      }
      if( ! enw.getHasProductiveUnits()  ) {
        enwErrors.add(ERROR_ENW_NO_PRODUCTIVE_UNITS);
      }
      if(enw.getInCombinedFarm() && ! enw.getBenefitCalculated()) {
        enwErrors.add(ERROR_ENW_COMBINED_FARM_PERCENT);
      }
      
    } else {
      
      Integer participantPin = enwScenario.getClient().getParticipantPin();
      Integer programYear = enwScenario.getYear();
      Integer scenarioNumber = enwScenario.getScenarioNumber();
      enwScenario = calculatorService.loadScenario(participantPin, programYear, scenarioNumber);
      
      calculatorService.updateScenario(enwScenario, ScenarioStateCodes.ENROLMENT_NOTICE_COMPLETE, null,
          ScenarioCategoryCodes.ENROLMENT_NOTICE_WORKFLOW, verifierUserEmail, null, formUserType, formTypeCode, null, user);
    }
    
    return enwErrors;
  }

  private void setBpuLead(Scenario enwScenario) {
    
    BenefitNullFixer nullFixer = CalculatorFactory.getBenefitNullFixer(enwScenario);
    nullFixer.fixNulls(enwScenario);
    
    FarmingOperation farmingOperation = enwScenario.getFarmingYear().getFarmingOperations().get(0);
    Date fiscalYearEndDate = farmingOperation.getFiscalYearEnd();
    String fiscalYearEndDateString = DateUtils.formatDate(fiscalYearEndDate);
    final String calendarYearEnd = "12-31";
    if( ! fiscalYearEndDateString.endsWith(calendarYearEnd) ) {
      List<ReferenceScenario> refScenarios = enwScenario.getReferenceScenarios();
      
      for(ReferenceScenario rs : refScenarios) {
        MarginTotal mt = rs.getFarmingYear().getMarginTotal();
        
        mt.setBpuLeadInd(Boolean.FALSE);
      }
    }
  }

  private Scenario findOrCreateEnwScenario(Integer participantPin, Integer programYear,
      List<ScenarioMetaData> programYearMetadata, Scenario chefNppScenario, String submissionGuid, Integer submissionId) throws ServiceException {
    
    CalculatorService calculatorService = ServiceFactory.getCalculatorService();
    
    ScenarioMetaData enwScenarioMetaData = ScenarioUtils.findLatestScenarioByChefSubmissionGuid(
        programYearMetadata, programYear, ScenarioCategoryCodes.ENROLMENT_NOTICE_WORKFLOW, submissionGuid);

    Integer enwScenarioNumber;
    if(enwScenarioMetaData == null || enwScenarioMetaData.stateIsOneOf(ScenarioStateCodes.CLOSED)) {
      enwScenarioNumber = calculatorService.saveScenarioAsNew(chefNppScenario.getScenarioId(),
          ScenarioTypeCodes.USER,
          ScenarioCategoryCodes.ENROLMENT_NOTICE_WORKFLOW,
          programYear,
          user);
    } else {
      enwScenarioNumber = enwScenarioMetaData.getScenarioNumber();
    }
    
    Scenario enwScenario = calculatorService.loadScenario(participantPin, programYear, enwScenarioNumber);
    
    if(enwScenario.getChefsSubmissionId() == null) {
      calculatorService.updateScenarioChefsSubmissionId(enwScenario.getScenarioId(), submissionId, user);
      enwScenario = calculatorService.loadScenario(participantPin, programYear, enwScenarioNumber);
    }
    
    return enwScenario;
  }

  private Integer getEnrolmentStatusCode(CrmAccountResource crmAccount, Integer programYear) throws ServiceException {
    Integer enrolmentStatusCode = null;

    CrmProgramYearResource crmProgramYear = crmDao.getProgramYear(programYear);
    
    if(crmProgramYear != null && crmAccount != null) {
      String accountId = crmAccount.getAccountid();
      String vsi_programyearid = crmProgramYear.getVsi_programyearid();
    
      CrmEnrolmentResource crmEnrolment = crmDao.getEnrolment(vsi_programyearid, accountId);
      
      final int maxWaitSeconds = 300;
      final int eachWaitSeconds = 2;
      int secondsWaited = 0;
      while(crmEnrolment == null && secondsWaited < maxWaitSeconds) {
        
        logger.debug("crmEnrolment not found. Waiting before retrieving again...");
        
        waitForDynamicsFlows(eachWaitSeconds);
        crmEnrolment = crmDao.getEnrolment(vsi_programyearid, accountId);
        secondsWaited += eachWaitSeconds;
      }
      
      if(crmEnrolment != null) {
        enrolmentStatusCode = crmEnrolment.getEnrolmentStatusCode();
      }
    }
    
    return enrolmentStatusCode;
  }

  private CrmTaskResource createValidationErrorTaskForNppNoPin(NppSubmissionDataResource data, String submissionGuid) throws ServiceException {

    String method = getMethod(data);
    final String subject = "NPP no PIN";
    final String description = "NPP client without a PIN";

    CrmValidationErrorResource task = new CrmValidationErrorResource();
    task.setSubject(subject);
    task.setDescription(description);
    task.setCr4dd_method(method);
    task.setCr4dd_origin(data.getOrigin());

    StringBuilder chefsSubmissionUrl = new StringBuilder();
    chefsSubmissionUrl.append(configUtil.getValue(ConfigurationKeys.CHEFS_UI_URL));
    chefsSubmissionUrl.append(URL_VIEW_CHEFS_SUBMISSION_FORM);
    chefsSubmissionUrl.append(data.getSubmissionGuid());
    task.setCr4dd_chefsurl(chefsSubmissionUrl.toString());

    String queueId = getQueueIdForParticipantType(data);
    CrmValidationErrorResource newTask = crmDao.createValidationErrorTask(task, queueId);
    
    setSubmissionInvalid(submissionGuid, newTask);
    
    return newTask;
  }

  private CrmTaskResource createValidationErrorTaskForNppBceid(NppSubmissionDataResource data, String submissionGuid) throws ServiceException {

    Integer participantPin = data.getParsedParticipantPin();
    String method = getMethod(data);

    String subject = "BCeID NPP submission";

    String description = "This form was submitted through BCeID. Please review and verify the information.";

    String accountId = null;
    if (participantPin != null) {
      CrmAccountResource crmAccount = getCrmAccountByParticipantPin(participantPin);
      if (crmAccount != null) {
        accountId = crmAccount.getAccountid();
      }
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

    String queueId = getQueueIdForParticipantType(data);
    CrmValidationErrorResource newTask = crmDao.createValidationErrorTask(task, queueId);
    
    setSubmissionInvalid(submissionGuid, newTask);
    
    return newTask;
  }

  private NewParticipant buildParticipantFromFormData(NppSubmissionDataResource data) {
    NewParticipant participant = new NewParticipant();
    Client client = new Client();
    participant.setClient(client);

    client.setParticipantPin(getParticipantPin(data));
    client.setSin(data.getSinNumber());
    if (StringUtils.isNotBlank(data.getBusinessTaxNumberBn())) {
      client.setBusinessNumber(data.getBusinessTaxNumberBn() + BUSINESS_NUMBER_SUFFIX);
    }
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
    fo.setSchedule("A");

    FarmingYear fy = new FarmingYear();

    fo.setFarmingYear(fy);
    fo.setFiscalYearStart(getFiscalStartDate(data.getFiscalYearEnd()));
    fo.setFiscalYearEnd(data.getFiscalYearEnd());
    fo.setPartnershipPin(0);
    fo.setPartnershipName(null);
    fo.setIsCropDisaster(false);
    fo.setIsCropShare(false);
    fo.setIsFeederMember(false);
    fo.setIsLandlord(false);
    fo.setIsLivestockDisaster(false);
    fo.setBusinessUseHomeExpense(0.0);
    fo.setFarmingExpenses(0.0);
    fo.setGrossIncome(0.0);
    fo.setInventoryAdjustments(0.0);
    fo.setNetFarmIncome(0.0);
    fo.setNetIncomeAfterAdj(0.0);
    fo.setNetIncomeBeforeAdj(0.0);
    fo.setOtherDeductions(0.0);
    
    Double partnershipPercent = null;
    BigDecimal totalPartnershipPercent = BigDecimal.ZERO;

    List<FarmingOperationPartner> fops = new ArrayList<>();
    for (PartnershipInformation p : data.getPartnershipInformation()) {
      FarmingOperationPartner fop = buildFarmingOperationPartner(fo, p);
      fops.add(fop);
      
      // If the participant entered themselves as a partner, then use that percentage
      if(fop.getPartnerPercent() != null) {
        if(data.getParsedParticipantPin().equals(fop.getParticipantPin())) {
          partnershipPercent = fop.getPartnerPercent().doubleValue();
        }
      
        totalPartnershipPercent = totalPartnershipPercent.add(fop.getPartnerPercent());
      }
    }
    fo.setFarmingOperationPartners(fops);
    
    // If the participant didn't enter themselves as a partner, then if the total of the other partners
    // is less than 100%, use the remainder as the participant's partnership percent.
    if(partnershipPercent == null) {
      if(totalPartnershipPercent.compareTo(BigDecimal.ONE) < 0) {
        partnershipPercent = BigDecimal.ONE.subtract(totalPartnershipPercent).doubleValue();
      } else {
        partnershipPercent = BigDecimal.ONE.doubleValue();
      }
    }
    fo.setPartnershipPercent(partnershipPercent);

    return fo;
  }

  private FarmingOperationPartner buildFarmingOperationPartner(FarmingOperation fo, PartnershipInformation p) {
    FarmingOperationPartner fop = new FarmingOperationPartner();
    final long oneHundred = 100;

    BigDecimal partnerPercent = null;
    if(p.getPartnershipPercentage() != null) {
      partnerPercent = BigDecimal.valueOf(p.getPartnershipPercentage());
      partnerPercent = partnerPercent.divide(BigDecimal.valueOf(oneHundred));
    }
    
    fop.setFarmingOperation(fo);
    fop.setPartnerPercent(partnerPercent);
    fop.setParticipantPin(p.getPartnershipPin() == null ? null : Integer.valueOf(p.getPartnershipPin()));
    fop.setFirstName(p.getPartnershipFirstName());
    fop.setLastName(p.getPartnershipLastName());
    fop.setCorpName(p.getPartnershipCorporationName());

    return fop;
  }

  private List<String> validate(NppSubmissionDataResource data, Client client) {
    logMethodStart(logger);

    List<String> validationErrors = new ArrayList<>();

    validateRequiredField(validationErrors, FIELD_NAME_AGRISTABILITY_PIN, getParticipantPin(data));
    validateRequiredField(validationErrors, FIELD_NAME_FISCAL_YEAR_END, getProgramYear(data));
    
    validateRequiredField(validationErrors, FIELD_NAME_FARM_TYPE, getFarmType(data));

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
    
    validateProductiveUnits(data, validationErrors);

    logMethodEnd(logger);
    return validationErrors;
  }

  private void validateProductiveUnits(NppSubmissionDataResource data, List<String> validationErrors) {
    
    List<String> berryCodes = extractCodesFromNppCommodityGrid(data.getBerryGrid());
    List<String> treeFruitCodes = extractCodesFromNppCommodityGrid(data.getTreeFruitGrid());
    List<String> vegetableCodes = extractCodesFromNppCommodityGrid(data.getVegetableGrid());
    List<String> grainCodes = extractCodesFromNppCommodityGrid(data.getGrainGrid());
    List<String> neCattleCodes = extractCodesFromNppCommodityGrid(data.getNeCattleGrid());
    List<String> opdCodes = extractCodesFromNppCommodityGrid(data.getOpdGrid());
    
    List<String> forageBasketCodes = extractCodesFromNppCropGrid(data.getForageBasketGrid());
    List<String> forageSeedCodes = extractCodesFromNppCropGrid(data.getForageSeedGrid());
    
    List<String> nurseryCodes = data.getNurseryGrid() == null ? new ArrayList<>() :
      data.getNurseryGrid().stream()
        .filter(p -> p.getCommodity() != null
        && StringUtils.isNotBlank(p.getCommodity().getValue())
        && (p.getSquareMeters() != null
            || p.getNumberOfPlants() != null))
        .map(p -> p.getCommodity().getValue())
        .collect(Collectors.toList());
    
    List<String> allCodes = new ArrayList<>();
    allCodes.addAll(berryCodes);
    allCodes.addAll(treeFruitCodes);
    allCodes.addAll(vegetableCodes);
    allCodes.addAll(grainCodes);
    allCodes.addAll(forageBasketCodes);
    allCodes.addAll(forageSeedCodes);
    allCodes.addAll(nurseryCodes);
    allCodes.addAll(neCattleCodes);
    allCodes.addAll(opdCodes);
    
    List<String> duplicateCodes =
        allCodes.stream()
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
        .entrySet().stream()
        .filter(e -> e.getValue() > 1)                 // keep only those occurring > 1
        .map(Map.Entry::getKey)
        .collect(Collectors.toList());
    
    boolean hasSingleFieldLivestockPucs = 
        MathUtils.isNonZero(data.getLayersEggsForHatching_108())
        || MathUtils.isNonZero(data.getLayersEggsForConsumption_109())
        || MathUtils.isNonZero(data.getBroilersChickens_143())
        || MathUtils.isNonZero(data.getBroilersTurkeys_144())
        || MathUtils.isNonZero(data.getProductiveCapacityLC123())
        || MathUtils.isNonZero(data.getFeederHogsFedOver50Lbs_124())
        || MathUtils.isNonZero(data.getFeederHogsFedUpTo50Lbs_125());
    
    if( allCodes.isEmpty() && !hasSingleFieldLivestockPucs ) {
      validationErrors.add(MISSING_PRODUCTIVE_UNITS);
    }

    if( ! duplicateCodes.isEmpty() ) {
      String duplicateCodesString = StringUtils.toCsv(duplicateCodes);
      validationErrors.add(String.format(DUPLICATE_PRODUCTIVE_UNIT_CODE, duplicateCodesString));
    }
  }

  private List<String> extractCodesFromNppCommodityGrid(List<NppCommodityGrid> grid) {
    @SuppressWarnings("unchecked")
    List<String> emptyList = Collections.EMPTY_LIST;
    return grid == null ? emptyList :
      grid.stream()
        .filter(p -> p.getCommodity() != null
            && StringUtils.isNotBlank(p.getCommodity().getValue())
            && (p.getAcres() != null
                || (p.getSquareMeters() != null && p.getSquareMeters() > 0)
                || (p.getNumberOfAnimals() != null && p.getNumberOfAnimals() > 0)
                )
            )
        .map(p -> p.getCommodity().getValue())
        .collect(Collectors.toList());
  }
  
  private List<String> extractCodesFromNppCropGrid(List<NppCropGrid> grid) {
    @SuppressWarnings("unchecked")
    List<String> emptyList = Collections.EMPTY_LIST;
    return grid == null ? emptyList :
      grid.stream()
      .filter(p -> StringUtils.isNotBlank(p.getCrop())
          && ((p.getAcres() != null && p.getAcres() > 0)
              || (p.getSquareMeters() != null && p.getSquareMeters() > 0)
              )
      )
      .map(p -> p.getCrop())
      .collect(Collectors.toList());
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
    return FIELD_VALUE_FARM_TYPE_STATUS_INDIAN.equals(farmType)
        || FIELD_VALUE_FARM_TYPE_INDIVIDUAL.equals(farmType);
  }

  private boolean isBusinessNumberRequired(NppSubmissionDataResource data) {
    String farmType = getFarmType(data);
    return FIELD_VALUE_FARM_TYPE_CORPORATION.equals(farmType)
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
      CrmAccountResource crmAccount, String optionalMessage, String submissionGuid) throws ServiceException {

    String method = getMethod(data);
    String primaryFarmingActivity = data.getWhatIsYourMainFarmingActivity();
    if (primaryFarmingActivity != null && primaryFarmingActivity.equals("otherPleaseSpecify")) {
      primaryFarmingActivity = data.getSpecifyOther();
    }

    String subjectFormatStr = VALID_FORM_TASK_SUBJECT_FORMAT;
    if (data.getLateParticipant() != null && data.getLateParticipant() == true) {
      subjectFormatStr = VALID_FORM_LATE_TASK_SUBJECT_FORMAT;
    }
    
    String messageText = optionalMessage == null ? "" : optionalMessage + "\n\n";

    String subject = StringUtils.formatWithNullAsEmptyString(subjectFormatStr,
        getProgramYear(data), FORM_SHORT_NAME, participantPin);
    String description = messageText +
        StringUtils.formatWithNullAsEmptyString(VALID_FORM_TASK_DESCRIPTION_FORMAT, primaryFarmingActivity);

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
    task.setCr4dd_participanttype(getFarmType(data));
    task.setAccountIdParameter(accountId);

    CrmNppTaskResource newTask = crmDao.createValidNppTask(task, queueId);
    
    setSubmissionProcessed(submissionGuid, newTask);
    
    return newTask;
  }

  private CrmTaskResource createValidationErrorTask(CrmAccountResource crmAccount, NppSubmissionDataResource data,
      List<String> validationErrors, String submissionGuid) throws ServiceException {
    logger.info("Creating Validation Error Task...");

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

    String farmType = getFarmType(data);
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
    CrmValidationErrorResource newTask = crmDao.createValidationErrorTask(task, queueId);
    
    setSubmissionInvalid(submissionGuid, newTask);
    
    return newTask;
  }

  private void validateBusinessNumber(String chefsValue, List<String> validationErrors) {
    
    if(chefsValue == null) {
      return;
    }

    String chefsBn = chefsValue.trim().replaceAll("\\s", "");

    Pattern pattern = Pattern.compile("^(\\d{9})");
    Matcher matcher = pattern.matcher(chefsBn);

    if (!matcher.find()) {
      validationErrors
          .add(FIELD_NAME_BUSINESS_NUMBER + " in BCFARMS does not start with a 9 digit number. Unable to validate.");
    }
  }

  private void validateTrustNumber(String chefsValue, List<String> validationErrors) {
    
    if(chefsValue == null) {
      return;
    }

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
