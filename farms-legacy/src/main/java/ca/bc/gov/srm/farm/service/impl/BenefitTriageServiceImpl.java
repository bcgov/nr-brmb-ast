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
package ca.bc.gov.srm.farm.service.impl;

import static ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes.*;
import static ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes.*;
import static ca.bc.gov.srm.farm.log.LoggingUtils.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.calculator.BenefitNullFixer;
import ca.bc.gov.srm.farm.calculator.BenefitValidator;
import ca.bc.gov.srm.farm.calculator.CalculatorFactory;
import ca.bc.gov.srm.farm.calculator.StructuralChangeCalculator;
import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationUtility;
import ca.bc.gov.srm.farm.crm.CrmConstants;
import ca.bc.gov.srm.farm.crm.CrmRestApiDao;
import ca.bc.gov.srm.farm.crm.resource.CrmAccountResource;
import ca.bc.gov.srm.farm.crm.resource.CrmCoreConfigurationResource;
import ca.bc.gov.srm.farm.crm.resource.CrmEnrolmentResource;
import ca.bc.gov.srm.farm.crm.resource.CrmProgramYearResource;
import ca.bc.gov.srm.farm.dao.BenefitTriageDAO;
import ca.bc.gov.srm.farm.dao.ImportDAO;
import ca.bc.gov.srm.farm.dao.StagingDAO;
import ca.bc.gov.srm.farm.dao.VersionDAO;
import ca.bc.gov.srm.farm.domain.BasePricePerUnit;
import ca.bc.gov.srm.farm.domain.BasePricePerUnitYear;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.ImportVersion;
import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.ScenarioMetaData;
import ca.bc.gov.srm.farm.domain.benefit.triage.BenefitTriageCalculationItem;
import ca.bc.gov.srm.farm.domain.benefit.triage.BenefitTriageItemResult;
import ca.bc.gov.srm.farm.domain.benefit.triage.BenefitTriageResults;
import ca.bc.gov.srm.farm.domain.benefit.triage.BenefitTriageStatus;
import ca.bc.gov.srm.farm.domain.codes.BPU;
import ca.bc.gov.srm.farm.domain.codes.BPUYear;
import ca.bc.gov.srm.farm.domain.codes.ImportClassCodes;
import ca.bc.gov.srm.farm.domain.codes.ImportStateCodes;
import ca.bc.gov.srm.farm.domain.codes.MunicipalityCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.StructuralChangeCodes;
import ca.bc.gov.srm.farm.domain.reasonability.MarginTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResults;
import ca.bc.gov.srm.farm.exception.DataAccessException;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.message.MessageKeys;
import ca.bc.gov.srm.farm.security.BusinessAction;
import ca.bc.gov.srm.farm.service.AdjustmentService;
import ca.bc.gov.srm.farm.service.BaseService;
import ca.bc.gov.srm.farm.service.BenefitService;
import ca.bc.gov.srm.farm.service.BenefitTriageService;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ClientService;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.CrmTransferService;
import ca.bc.gov.srm.farm.service.ReasonabilityTestService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.srm.farm.util.DateUtils;
import ca.bc.gov.srm.farm.util.PropertyLoader;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

public class BenefitTriageServiceImpl extends BaseService implements BenefitTriageService {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  private static final String MESSAGE_FAIL_STRUCTURE_CHANGE_NOT_ENABLED =
      "Fail: Structure Change is not enabled because BPUs are missing.";
  
  private static final String MESSAGE_FAIL_REFERENCE_MARGIN_FAILED_AT_LOW_END =
      "Fail: The Reference Margin Test failed at the low end.";
  
  private static final String MESSAGE_FAIL_STRUCTURAL_CHANGE_ADD_DIV_FAILED =
      "Fail: Structural Change Additive Division Test failed.";
  
  private static final String MESSAGE_FAIL_LESS_THAN_5_YEARS_OF_DATA =
      "Fail: Less than 5 reference years of data.";
  
  private static final String MESSAGE_FAIL_FISCAL_YEAR_END_DATE_CHANGED =
      "Fail: Fiscal Year End date changed.";
  
  private static final String MESSAGE_FAIL_COMBINED_FARM =
      "Fail: Last year this producer was part of a Combined Farm.";

  private CrmTransferService crmTransferService;
  private AdjustmentService adjustmentService;
  private CalculatorService calculatorService;
  private BenefitService benefitService;
  private CodesService codesService;
  private ReasonabilityTestService testService;
  private ConfigurationUtility configUtil;
  
  private Properties messageProperties;
  
  private ObjectMapper jsonObjectMapper = new ObjectMapper();
  
  
  public BenefitTriageServiceImpl() {
    crmTransferService = ServiceFactory.getCrmTransferService();
    configUtil = ConfigurationUtility.getInstance();
    adjustmentService = ServiceFactory.getAdjustmentService();
    benefitService = ServiceFactory.getBenefitService();
    calculatorService = ServiceFactory.getCalculatorService();
    codesService = ServiceFactory.getCodesService();
    testService = ReasonabilityTestServiceFactory.getInstance();
    messageProperties = PropertyLoader.loadProperties(MessageKeys.MESSAGES_FILE_PATH);
  }

  @Override
  public List<BenefitTriageStatus> getBenefitTriageStatusByYear(int year) throws ServiceException {
    logMethodStart(logger);

    Transaction transaction = openTransaction();
    BenefitTriageDAO benefitTriageDao = new BenefitTriageDAO();

    List<BenefitTriageStatus> triageStatusList = null;
    try {

      triageStatusList = benefitTriageDao.readTriageStatusByYear(transaction, year);

    } catch (Exception e) {
      throw new ServiceException(e);
    }

    logMethodEnd(logger);
    return triageStatusList;
  }
  
  
  @Override
  public Integer queueBenefitTriage(String triageJobDescription, Connection connection, String userId) throws ServiceException {
    
    try {
      ImportDAO dao = new ImportDAO();
      
      String importClassCode = ImportClassCodes.TRIAGE;
      String importStateCode = ImportStateCodes.SCHEDULED_FOR_STAGING;
      
      ImportVersion importVersion = dao.createEmptyTransferRecord(
          userId, connection, importClassCode, importStateCode, triageJobDescription);
      connection.commit();
      
      return importVersion.getImportVersionId();
      
    } catch (DataAccessException e) {
      logger.error("DataAccessException: ", e);
      throw new ServiceException(e);
    } catch (SQLException e) {
      logger.error("SQLException: ", e);
      throw new ServiceException(e);
    }

  }
  
  
  @Override
  public BenefitTriageResults processBenefitTriage(Connection connection, Integer importVersionId, String userId) throws ServiceException {
    
    BenefitTriageDAO benefitTriageDao = new BenefitTriageDAO();
    List<BenefitTriageCalculationItem> triageItems = benefitTriageDao.readTriageCalculationItems(connection);
    
    return processBenefitTriageItems(connection, importVersionId, triageItems, userId);
  }
  
  
  @Override
  public BenefitTriageResults processBenefitTriageItems(Connection connection, Integer importVersionId,
      List<BenefitTriageCalculationItem> triageItems, String userId) throws ServiceException {
    logMethodStart(logger);
    
    CacheFactory.getRequestCache().addItem(CacheKeys.CURRENT_BUSINESS_ACTION, BusinessAction.system());
    
    BenefitTriageResults triageResults = new BenefitTriageResults();
    List<BenefitTriageItemResult> triageItemResults = triageResults.getTriageItemResults();
    
    VersionDAO vdao = null;
    StagingDAO sdao = null;

    try {
      vdao = new VersionDAO(connection);
      sdao = new StagingDAO(connection);
      connection.setAutoCommit(false);

      vdao.startImport(importVersionId, userId);
      connection.commit();
      sdao.status(importVersionId, "Started");
      
      calculateTriageBenefits(connection, triageItems, triageItemResults, sdao, importVersionId, userId);

      sdao.status(importVersionId, "Benefit Triage calculations completed.");
      markJobComplete(connection, importVersionId, userId, vdao, triageResults);

    } catch (SQLException | ServiceException e) {
      logger.error("Unexpected error: ", e);
      String formattedException = formatException(e);
      triageResults.setUnexpectedError(formattedException);
      String resultsJson = convertResultsToJson(triageResults);
      try {
        vdao.importFailed(importVersionId, resultsJson, userId);
        connection.commit();
        sdao.status(importVersionId, "Failed to complete Benefit Triage calculations.");
        connection.commit();
      } catch (SQLException | IOException ex) {
        throw new ServiceException(ex);
      }
    }
    
    logMethodEnd(logger);
    return triageResults;
  }


  private String convertResultsToJson(BenefitTriageResults triageResults) throws ServiceException {
    String resultsJson;
    try {
      resultsJson = jsonObjectMapper.writeValueAsString(triageResults);
    } catch (JsonProcessingException jsonEx) {
      logger.error("Unexpected error processing Benefit Triage", jsonEx);
      throw new ServiceException(jsonEx);
    }
    return resultsJson;
  }


  @Override
  public void calculateTriageBenefits(Connection connection, List<BenefitTriageCalculationItem> triageItems,
      List<BenefitTriageItemResult> results, StagingDAO sdao, Integer importVersionId, String userId)
          throws ServiceException {
    logMethodStart(logger);
    
    String verifierUserEmail = configUtil.getValue(ConfigurationKeys.BENEFIT_TRIAGE_VERIFIER_USER_EMAIL);
    
    CrmRestApiDao crmDao = new CrmRestApiDao();
    CrmCoreConfigurationResource coreConfiguration = crmDao.getCoreConfiguration();
    BigDecimal paymentThreshold = coreConfiguration.getVsi_triagepaymentthreshold();
    
    Map<Integer, List<BPU>> yearBpuListMap = new HashMap<>();
    
    int triageItemCount = triageItems.size();
    int itemsProcessed = 0;
    
    for(BenefitTriageCalculationItem item : triageItems) {
      Integer participantPin = item.getParticipantPin();
      Integer programYear = item.getProgramYear();
      Integer baseScenarioId = item.getCraScenarioId(); // Either CRA or CHEF scenario
      Integer baseScenarioNumber = item.getCraScenarioNumber();
      
      calculateTriageBenefit(participantPin, programYear, baseScenarioId, baseScenarioNumber, results, verifierUserEmail,
          yearBpuListMap, paymentThreshold, connection, userId);
      
      itemsProcessed++;
      if(sdao != null && importVersionId != null) {
        try {
          sdao.status(importVersionId, String.format("%d of %d calculations completed.", itemsProcessed, triageItemCount));
        } catch (SQLException e) {
          logger.error(String.format("Unexpected error processing Benefit Triage importVersionId: %d", importVersionId), e);
          throw new ServiceException(e);
        }
      }
    }

    logMethodEnd(logger);
  }


  private BenefitTriageItemResult calculateTriageBenefit(Integer participantPin, Integer programYear, Integer baseScenarioId,
      Integer baseScenarioNumber, List<BenefitTriageItemResult> results, String verifierUserEmail,
      Map<Integer, List<BPU>> yearBpuListMap, BigDecimal paymentThreshold, Connection connection, String userId)
      throws ServiceException {
    
    List<String> errorMessages = new ArrayList<>();
    
    BenefitTriageItemResult result = new BenefitTriageItemResult();
    result.setParticipantPin(participantPin);
    result.setProgramYear(programYear);
    result.setErrorMessages(errorMessages);
    
    // If they are not enrolled for this program year then we don't
    // need to calculate the estimated benefit. They might enroll later,
    // so keep checking.
    boolean enrolled = checkEnrolled(participantPin, programYear);
    
    if( ! enrolled ) {
        return result;
    }
    
    results.add(result);
    
    try {
      
      Integer triageScenarioNumber = calculatorService.saveScenarioAsNew(baseScenarioId,
          ScenarioTypeCodes.TRIAGE,
          TRIAGE,
          baseScenarioNumber,
          userId);
      Scenario triageScenario = getScenario(participantPin, programYear, triageScenarioNumber, connection);
      result.setClientName(triageScenario.getClient().getOwner().getFullName());
      result.setScenarioNumber(triageScenario.getScenarioNumber());
      
      calculateBenefit(triageScenario, yearBpuListMap, errorMessages, connection, userId);
      triageScenario = reloadScenario(triageScenario, connection);
      
      String newScenarioStateCode = FAILED;
      boolean isPaymentFile = false;
      boolean zeroPass = false;
      boolean paymentPass = false;
      Double totalBenefit = null;
      
      if(errorMessages.isEmpty()) {
        
        totalBenefit = triageScenario.getBenefit().getTotalBenefit();
        isPaymentFile = totalBenefit > 0;
        result.setEstimatedBenefit(totalBenefit);
        result.setIsPaymentFile(isPaymentFile);
        
        ReasonabilityTestResults testResults = triageScenario.getReasonabilityTestResults();
        
        if(testResults != null) {
          newScenarioStateCode = COMPLETED;
          
          String structuralChangeCode = triageScenario.getBenefit().getStructuralChangeMethodCode();
          boolean structureChangeEnabled = ! StructuralChangeCodes.NONE.equals(structuralChangeCode);
          Boolean referenceMarginTestPassed = testResults.getMarginTest().getWithinLimitOfReferenceMargin();
          boolean varianceOverTheUpperLimitOfReferenceMargin = checkMarginVarianceOverTheUpperLimit(triageScenario);
          boolean referenceMarginTestPassedOrFailedAtTheHighEnd = referenceMarginTestPassed || varianceOverTheUpperLimitOfReferenceMargin;
          boolean structuralChangeAdditiveDivisionTestPassed = testResults.getStructuralChangeTest().getWithinAdditiveDivisionLimit();
          boolean hasFiveYearsOfData = checkHasFiveYearsOfReferenceData(triageScenario);
          boolean fiscalEndDatesConsistent = checkFiscalEndDatesConsistent(triageScenario);
          boolean notCombinedFarm = checkNotCombinedFarm(triageScenario);
          
          if(isPaymentFile) {
            
            boolean accountingMethodConsistent = true; // TODO check accounting method
            boolean municipalityConsistent = true;     // TODO check municipality
            boolean paymentWithinThreshold = BigDecimal.valueOf(totalBenefit).compareTo(paymentThreshold) <= 0;
            
            paymentPass = structureChangeEnabled
                && referenceMarginTestPassedOrFailedAtTheHighEnd
                && structuralChangeAdditiveDivisionTestPassed
                && hasFiveYearsOfData
                && fiscalEndDatesConsistent
                && notCombinedFarm
                && accountingMethodConsistent
                && municipalityConsistent
                && paymentWithinThreshold;
            
            addMessages(
                result,
                structureChangeEnabled,
                referenceMarginTestPassedOrFailedAtTheHighEnd,
                structuralChangeAdditiveDivisionTestPassed,
                hasFiveYearsOfData,
                fiscalEndDatesConsistent,
                notCombinedFarm);
            
          } else {
            
            zeroPass = structureChangeEnabled
                && referenceMarginTestPassedOrFailedAtTheHighEnd
                && structuralChangeAdditiveDivisionTestPassed
                && hasFiveYearsOfData
                && fiscalEndDatesConsistent
                && notCombinedFarm;
            
            addMessages(
                result,
                structureChangeEnabled,
                referenceMarginTestPassedOrFailedAtTheHighEnd,
                structuralChangeAdditiveDivisionTestPassed,
                hasFiveYearsOfData,
                fiscalEndDatesConsistent,
                notCombinedFarm);
          }
          
        }
      }
      
      result.setZeroPass(zeroPass);       // These are only used by the unit tests
      result.setPaymentPass(paymentPass); 
      
      String triageResultType = null;
      if(zeroPass) {
        triageResultType = TRIAGE_RESULT_TYPE_ZERO_PASS;
      }
 
      logger.debug("Updating scenario state");
      calculatorService.updateScenario(triageScenario, newScenarioStateCode, null,
          triageScenario.getScenarioCategoryCode(), verifierUserEmail, null, null, null, triageResultType, userId);
      triageScenario = reloadScenario(triageScenario, connection);
      
      if(zeroPass || paymentPass) {
        // Create a Verified Final scenario from the TRIAGE scenario
        Integer finalScenarioNumber = calculatorService.saveScenarioAsNew(baseScenarioId,
            ScenarioTypeCodes.USER,
            UNKNOWN,
            triageScenarioNumber,
            userId);
        
        Scenario finalScenario = getScenario(participantPin, programYear, finalScenarioNumber, connection);
        
        // Update category to Final. Triggers an In Progress Final Benefit Update.
        calculatorService.updateScenario(finalScenario, finalScenario.getScenarioStateCode(), null,
            FINAL, verifierUserEmail, null, null, null, triageResultType, userId);
        finalScenario = reloadScenario(finalScenario, connection);
 
        // Expecting errorMessages to be empty because it was when calculating for the TRIAGE scenario
        calculateBenefit(finalScenario, yearBpuListMap, errorMessages, connection, userId);
        finalScenario = reloadScenario(finalScenario, connection);
        
        // Update state to Verified. Triggers a Verified Final Benefit Update.
        calculatorService.updateScenario(finalScenario, VERIFIED, null,
            finalScenario.getScenarioCategoryCode(), verifierUserEmail, null, null, null, triageResultType, userId);

      } else {
        crmTransferService.scheduleBenefitTransfer(triageScenario, verifierUserEmail, userId);
      }
      
      result.setScenarioStateCodeDesc(triageScenario.getScenarioStateCodeDescription());
      
    } catch(Exception e) {
      e.printStackTrace();
      logger.error(String.format("Unexpected error processing %d PIN %d: ", programYear, participantPin), e);
      result.setScenarioStateCodeDesc(FAILED_DESCRIPTION);
    }
    
    return result;
  }

  private boolean checkMarginVarianceOverTheUpperLimit(Scenario triageScenario) {
    
    MarginTestResult marginTestResults = triageScenario.getReasonabilityTestResults().getMarginTest();
    Double variance = marginTestResults.getAdjustedReferenceMarginVariance();
    double referenceMarginVarianceLimit = marginTestResults.getAdjustedReferenceMarginVarianceLimit();
    
    boolean overTheUpperLimitOfReferenceMargin =
        variance != null
        && variance >= referenceMarginVarianceLimit;
    
    return overTheUpperLimitOfReferenceMargin;
  }

  private boolean checkHasFiveYearsOfReferenceData(Scenario scenario) {
    final int numYearsNeeded = 5;
    int referenceScenarioCount = scenario.getReferenceScenarios().size();
    
    boolean hasFiveYearsOfData = true;
    
    if(referenceScenarioCount != numYearsNeeded) {
      hasFiveYearsOfData = false;
    } else {
      
      for (ReferenceScenario refScenario : scenario.getAllScenarios()) {
        
        Map<Integer, IncomeExpense> incomes = ScenarioUtils.getConsolidatedIncomeExpense(scenario, true, null, refScenario.getYear());
        Map<Integer, IncomeExpense> expenses = ScenarioUtils.getConsolidatedIncomeExpense(scenario, false, null, refScenario.getYear());
        boolean hasIncomes = incomes.values().stream().anyMatch(i -> i.getTotalAmount() != 0);
        boolean hasExpenses = expenses.values().stream().anyMatch(i -> i.getTotalAmount() != 0);
        
        if(!hasIncomes && !hasExpenses) {
          hasFiveYearsOfData = false;
          break;
        }
      }
      
    }
    
    return hasFiveYearsOfData;
  }

  private boolean checkFiscalEndDatesConsistent(Scenario scenario) {
    
    boolean datesConsistent = true;
    
    Integer programYear = scenario.getYear();
    ReferenceScenario lastYearReferenceScenario = scenario.getReferenceScenarioByYear(programYear - 1);
    FarmingYear lastYearFarmingYear = lastYearReferenceScenario.getFarmingYear();
    List<FarmingOperation> farmingOperations = scenario.getFarmingYear().getFarmingOperations();
    
    for (FarmingOperation farmingOperation : farmingOperations) {
      Integer operationNumber = farmingOperation.getOperationNumber();
      Date programYearFiscalYearEnd = farmingOperation.getFiscalYearEnd();
      
      FarmingOperation lastYearFarmingOperation = lastYearFarmingYear.getFarmingOperationByNumber(operationNumber);
      
      if(lastYearFarmingOperation == null) {
        datesConsistent = false;
        break;
      }
      
      Date lastYearFiscalYearEnd = lastYearFarmingOperation.getFiscalYearEnd();
      
      Date programYearFiscalEndMinusOneYear = DateUtils.subtractYears(programYearFiscalYearEnd, 1);
      
      boolean fiscalYearEndChanged = ! programYearFiscalEndMinusOneYear.equals(lastYearFiscalYearEnd);
      
      if(fiscalYearEndChanged) {
        datesConsistent = false;
        break;
      }
      
    }
    
    return datesConsistent;
  }

  private boolean checkNotCombinedFarm(Scenario triageScenario) {
    
    int programYear = triageScenario.getYear();
    int lastYear = programYear - 1;
    List<ScenarioMetaData> scenarioMetaDataList = triageScenario.getScenarioMetaDataList();
    List<ScenarioMetaData> lastYearCombinedFarmScenarios =
        ScenarioUtils.findCombinedFarmScenarios(scenarioMetaDataList, lastYear);
    
    return lastYearCombinedFarmScenarios.isEmpty();
  }

  private void addMessages(BenefitTriageItemResult result,
      boolean structureChangeEnabled,
      boolean referenceMarginTestPassedOrFailedAtTheHighEnd,
      boolean structuralChangeAdditiveDivisionTestPassed,
      boolean hasFiveYearsOfData,
      boolean fiscalEndDatesConsistent,
      boolean notCombinedFarm) {
    
    List<String> failMessages = result.getFailMessages();
    
    if( ! structureChangeEnabled ) {
      failMessages.add(MESSAGE_FAIL_STRUCTURE_CHANGE_NOT_ENABLED);
    }
    if( ! referenceMarginTestPassedOrFailedAtTheHighEnd ) {
      failMessages.add(MESSAGE_FAIL_REFERENCE_MARGIN_FAILED_AT_LOW_END);
    }
    if( ! structuralChangeAdditiveDivisionTestPassed ) {
      failMessages.add(MESSAGE_FAIL_STRUCTURAL_CHANGE_ADD_DIV_FAILED);
    }
    if( ! hasFiveYearsOfData ) {
      failMessages.add(MESSAGE_FAIL_LESS_THAN_5_YEARS_OF_DATA);
    }
    if( ! fiscalEndDatesConsistent ) {
      failMessages.add(MESSAGE_FAIL_FISCAL_YEAR_END_DATE_CHANGED);
    }
    if( ! notCombinedFarm ) {
      failMessages.add(MESSAGE_FAIL_COMBINED_FARM);
    }
  }

  private Scenario reloadScenario(Scenario scenario, Connection connection)
      throws ServiceException {
    return getScenario(
        scenario.getClient().getParticipantPin(), scenario.getYear(), scenario.getScenarioNumber(), connection);
  }

  private Scenario getScenario(Integer participantPin, Integer programYear, Integer triageScenarioNumber, Connection connection)
      throws ServiceException {
    ClientService clientService = ClientServiceFactory.getInstance(connection);
    return clientService.getClientInfoWithHistory(
        participantPin, programYear, triageScenarioNumber, ClientService.COMP_FIRST_MODE);
  }

  private boolean checkEnrolled(Integer participantPin, Integer programYear) throws ServiceException {
    boolean enrolled = false;

    CrmRestApiDao crmDao = new CrmRestApiDao();
    CrmProgramYearResource crmProgramYear = crmDao.getProgramYear(programYear);
    CrmAccountResource crmAccount = crmDao.getAccountByPin(participantPin);
    
    if(crmProgramYear != null && crmAccount != null) {
      String accountId = crmAccount.getAccountid();
      String vsi_programyearid = crmProgramYear.getVsi_programyearid();
    
      CrmEnrolmentResource crmEnrolment = crmDao.getEnrolment(vsi_programyearid, accountId);
      if(crmEnrolment != null) {
        Integer enrolmentStatusCode = crmEnrolment.getEnrolmentStatusCode();
        if(enrolmentStatusCode != null) {
          enrolled = enrolmentStatusCode.intValue() == CrmConstants.ENROLMENT_STATUS_CODE_ENROLLED
              || enrolmentStatusCode.intValue() == CrmConstants.ENROLMENT_STATUS_CODE_LATE_ENROLLED;
        }
      }
    }
    
    return enrolled;
  }

  private void calculateBenefit(
      Scenario scenarioParam,
      Map<Integer, List<BPU>> yearBpuListMap,
      List<String> errorMessages,
      Connection connection,
      String userId) throws Exception {
    
    Scenario scenario = scenarioParam;
    
    adjustmentService.makeInventoryValuationAdjustments(scenario, true);
    scenario = reloadScenario(scenario, connection);
    
    fixMissingBpus(scenario, yearBpuListMap);
    fixBpuZeroes(scenario);
    
    BenefitNullFixer nullFixer = CalculatorFactory.getBenefitNullFixer(scenario);
    nullFixer.fixNulls(scenario);
 
    ActionMessages messages = new ActionMessages();
    BenefitValidator validator = CalculatorFactory.getBenefitValidator(scenario);
    
    
    boolean missingBpus = !validator.validateBpus(scenario);
    if(missingBpus) {
      StructuralChangeCalculator scCalc = CalculatorFactory.getStructuralChangeCalculator(scenario);
      String structuralChangeCode = StructuralChangeCodes.NONE;
      String expenseStructuralChangeCode = StructuralChangeCodes.NONE;
      scCalc.updateStructuralChangeCode(structuralChangeCode, expenseStructuralChangeCode);
    }
    
    messages = benefitService.calculateBenefit(scenario, userId, true, true, true);
    
    if(!messages.isEmpty()) {
      
      for(@SuppressWarnings("unchecked") Iterator<ActionMessage> mi = messages.get(); mi.hasNext(); ) {
        ActionMessage msg = mi.next();
        errorMessages.add(messageProperties.getProperty(msg.getKey()));
      }
      
    }
    
    if(errorMessages.isEmpty()) {
      runReasonabilityTests(scenario, userId);
    }
    
    logMethodEnd(logger);
  }


  /**
   * For BPUs that have a 0.0 (zero) for the margin or expense value,
   * set those values to the values of the year minus one from within
   * the same BPU set (for example, the 2024 BPU set).
   */
  private void fixBpuZeroes(Scenario scenario) {
    
    for(ReferenceScenario refScenario : scenario.getAllScenarios()) {
      for(FarmingOperation fo : refScenario.getFarmingYear().getFarmingOperations()) {
        for(ProductiveUnitCapacity puc : fo.getAllProductiveUnitCapacitiesForStructureChange()) {
          BasePricePerUnit bpu = puc.getBasePricePerUnit();
          if(bpu != null) {
            String inventoryCode = bpu.getInventoryCode();
            boolean skip = inventoryCode != null && BenefitValidator.BPU_CODES_ALLOWING_ZEROES.contains(inventoryCode);
            
            if( ! skip ) {
              List<BasePricePerUnitYear> bpuYears = bpu.getBasePricePerUnitYears().stream().sorted(
                  (o1, o2) -> o1.getYear().compareTo(o2.getYear())).collect(Collectors.toList());
              for (BasePricePerUnitYear bpuYear : bpuYears) {
                double margin = bpuYear.getMargin().doubleValue();
                double expense = bpuYear.getExpense().doubleValue();
                if(margin == 0.0d || expense == 0.0d) {
                  Integer year = bpuYear.getYear();
                  BasePricePerUnitYear previousBpuYear = getPreviousBasePricePerUnitYear(bpu, year);
                  if(previousBpuYear != null) {
                    double previousYearMargin = previousBpuYear.getMargin().doubleValue();
                    double previousYearExpense = previousBpuYear.getExpense().doubleValue();
                    if(margin == 0.0d && previousYearMargin != 0.0d) {
                      bpuYear.setMargin(previousYearMargin);
                    }
                    if(expense == 0.0d && previousYearExpense != 0.0d) {
                      bpuYear.setExpense(previousYearExpense);
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }


  private void fixMissingBpus(Scenario scenario, Map<Integer, List<BPU>> yearBpuListMap) throws ServiceException {
    
    Integer programYear = scenario.getYear();
    String municipalityCode = scenario.getFarmingYear().getMunicipalityCode();
    int previousYear = programYear - 1;
    
    List<BPU> previousYearBpus = yearBpuListMap.get(previousYear);
    if(previousYearBpus == null) {
      previousYearBpus = codesService.getBPUs(previousYear);
      yearBpuListMap.put(previousYear, previousYearBpus);
    }
    
    for(ReferenceScenario refScenario : scenario.getAllScenarios()) {
      for(FarmingOperation fo : refScenario.getFarmingYear().getFarmingOperations()) {
        for(ProductiveUnitCapacity puc : fo.getAllProductiveUnitCapacitiesForStructureChange()) {
          BasePricePerUnit bpu = puc.getBasePricePerUnit();
          
          // If there is no BPU for the program year then use the BPU from the previous year.
          // The previous year's BPU won't have a value for PY - 1 (PY = Program Year)
          // so, add that and copy forward the Margin and Expense values.
          if(bpu == null) {
            
            BPU previousYearBPU = previousYearBpus.stream()
            
                .filter(pyb -> codeMatches(puc, pyb)
                    && (pyb.getMunicipalityCode().equals(municipalityCode)
                        || pyb.getMunicipalityCode().equals(MunicipalityCodes.ALL_MUNICIPALITIES)))
                // The 'All Municipalities' code is zero and the rest are positive numbers,
                // so the max code will be the matching municipality, if it exists, otherwise use 'All Municipalities'.
                .max((o1, o2) -> Integer.valueOf(o1.getMunicipalityCode()).compareTo(Integer.valueOf(o2.getMunicipalityCode())))
                .orElse(null);
            
            if(previousYearBPU != null) {
              bpu = new BasePricePerUnit();
              bpu.setMunicipalityCode(previousYearBPU.getMunicipalityCode());
              
              if(BPU.BPU_CODE_TYPE_STRUCTURE_GROUP.equals(previousYearBPU.getInvSgType())) {
                bpu.setStructureGroupCode(previousYearBPU.getInvSgCode());
                bpu.setComment(previousYearBPU.getInvSgCodeDescription());
              } else { // BPU.BPU_CODE_TYPE_INVENTORY
                bpu.setInventoryCode(previousYearBPU.getInvSgCode());
                bpu.setComment(previousYearBPU.getInvSgCodeDescription());
              }
              
              // Sort the BPUYear objects by year in descending order (latest first). 
              List<BPUYear> prevYearBpuYears = Arrays.asList(previousYearBPU.getYears()).stream().sorted(
                  (o1, o2) -> o2.getYear().compareTo(o1.getYear())).collect(Collectors.toList());
              List<BasePricePerUnitYear> basePricePerUnitYears = new ArrayList<>();
              
              if( ! prevYearBpuYears.isEmpty() ) {
                // BPUs were sorted above with the most recent first so
                // get the first element in the list.
                BPUYear prevYearBpuYear = prevYearBpuYears.get(0);
                BasePricePerUnitYear bpuYear = new BasePricePerUnitYear();
                
                // Since this is the previous year's BPU, the latest is PY - 2 (PY = Program Year).
                // Copy the values and add 1 to the year so that we have values for PY - 1.
                bpuYear.setYear(prevYearBpuYear.getYear()  + 1);
                bpuYear.setMargin(prevYearBpuYear.getAverageMargin());
                bpuYear.setExpense(prevYearBpuYear.getAverageExpense());
                basePricePerUnitYears.add(bpuYear);
              }
              
              for(BPUYear prevYearBpuYear : prevYearBpuYears) {
                
                BasePricePerUnitYear bpuYear = new BasePricePerUnitYear();
                bpuYear.setYear(prevYearBpuYear.getYear());
                bpuYear.setMargin(prevYearBpuYear.getAverageMargin());
                bpuYear.setExpense(prevYearBpuYear.getAverageExpense());
                basePricePerUnitYears.add(bpuYear);
              }
              bpu.setBasePricePerUnitYears(basePricePerUnitYears);
              puc.setBasePricePerUnit(bpu);
            }
          }
        }
      }
    }
  }


  private boolean codeMatches(ProductiveUnitCapacity puc, BPU bpu) {
    return bpu.getInvSgCode().equals(puc.getStructureGroupCode())
        || bpu.getInvSgCode().equals(puc.getInventoryItemCode());
  }

  /**
   * Get the BPU margin and expense values for the year minus 1 from the same BPU set.
   */
  private BasePricePerUnitYear getPreviousBasePricePerUnitYear(BasePricePerUnit bpu, Integer year) {
    BasePricePerUnitYear result = null;
    for(BasePricePerUnitYear bpuYear : bpu.getBasePricePerUnitYears()) {
      int previousYear = year - 1;
      if(bpuYear.getYear().equals(previousYear)) {
        result = bpuYear;
        break;
      }
    }
    return result;
  }

  private ReasonabilityTestResults runReasonabilityTests(Scenario scenario, String userId) {
    Integer participantPin = scenario.getClient().getParticipantPin();
    Integer year = scenario.getYear();
    
    ReasonabilityTestResults testResults = null;
    try {
      testResults = testService.test(scenario);
      calculatorService.updateReasonabilityTests(scenario, testResults, userId);
      
    } catch(Exception e) {
      e.printStackTrace();
      logger.error(String.format("Unexpected error running reasonability tests for %d PIN %d: ", year, participantPin), e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
    }
    return testResults;
  }

  private void markJobComplete(
      Connection connection,
      Integer importVersionId,
      String user,
      VersionDAO vdao,
      BenefitTriageResults triageResults) throws ServiceException {
    logMethodStart(logger);
    
    Boolean hasErrors = Boolean.FALSE;
    try {
      vdao.uploadedVersion(importVersionId, "", hasErrors, user);
      String resultsJson = convertResultsToJson(triageResults);
      vdao.importCompleted(importVersionId, resultsJson, user);
      connection.commit();
    } catch (SQLException | IOException e) {
      logger.error(String.format("Unexpected error processing Benefit Triage importVersionId: %d", importVersionId), e);
      throw new ServiceException(e);
    }
    
    logMethodEnd(logger);
  }
  
  private String formatException(Throwable t) {
    
    StringWriter stringWriter = new StringWriter();
    stringWriter.append("Unexpected Exception: ");
    stringWriter.append(t.getMessage());
    stringWriter.append("\n");
    
    PrintWriter printWriter = new PrintWriter(stringWriter);
    t.printStackTrace(printWriter);
    printWriter.flush();

    String errorMsg = stringWriter.toString();

    return errorMsg;
  }

}
