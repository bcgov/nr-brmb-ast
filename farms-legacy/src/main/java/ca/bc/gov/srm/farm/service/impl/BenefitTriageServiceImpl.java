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
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

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
import ca.bc.gov.srm.farm.crm.resource.CrmQueueResource;
import ca.bc.gov.srm.farm.crm.resource.CrmTaskResource;
import ca.bc.gov.srm.farm.crm.resource.CrmValidationErrorResource;
import ca.bc.gov.srm.farm.dao.BenefitTriageDAO;
import ca.bc.gov.srm.farm.dao.CalculatorDAO;
import ca.bc.gov.srm.farm.dao.ImportDAO;
import ca.bc.gov.srm.farm.dao.ReadDAO;
import ca.bc.gov.srm.farm.dao.StagingDAO;
import ca.bc.gov.srm.farm.dao.VersionDAO;
import ca.bc.gov.srm.farm.domain.BasePricePerUnit;
import ca.bc.gov.srm.farm.domain.BasePricePerUnitYear;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.ImportVersion;
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
import ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes;
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
import ca.bc.gov.srm.farm.util.MathUtils;
import ca.bc.gov.srm.farm.util.PropertyLoader;
import ca.bc.gov.srm.farm.util.ScenarioUtils;
import ca.bc.gov.srm.farm.util.SleepUtils;
import ca.bc.gov.srm.farm.util.StringUtils;
import ca.bc.gov.srm.farm.util.StrutsUtils;

public class BenefitTriageServiceImpl extends BaseService implements BenefitTriageService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private CrmTransferService crmTransferService;
  private AdjustmentService adjustmentService;
  private CalculatorService calculatorService;
  private BenefitService benefitService;
  private CodesService codesService;
  private ReasonabilityTestService testService;
  private ConfigurationUtility configUtil;
  private CrmRestApiDao crmDao;
  
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
    crmDao = new CrmRestApiDao();
  }

  @Override
  public List<BenefitTriageStatus> getBenefitTriageStatusByYear(int year) throws ServiceException {
    logMethodStart(logger);

    BenefitTriageDAO benefitTriageDao = new BenefitTriageDAO();

    List<BenefitTriageStatus> triageStatusList = null;
    try (Transaction transaction = openTransaction()) {

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
      String formattedException = formatExceptionForFailedImport(e);
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
      
      Scenario triageScenario = createTriageScenario(participantPin, programYear, baseScenarioId, baseScenarioNumber, connection, userId);
      Integer triageScenarioNumber = triageScenario.getScenarioNumber();
      
      result.setClientName(triageScenario.getClient().getOwner().getFullName());
      result.setScenarioNumber(triageScenario.getScenarioNumber());
      
      calculateBenefit(triageScenario, yearBpuListMap, errorMessages, connection, userId);
      triageScenario = reloadScenario(triageScenario, connection);
      
      String newScenarioStateCode = FAILED;
      boolean isPaymentFile = false;
      boolean zeroPass = false;
      boolean paymentPass = false;
      Double triagePaymentAmount = null;
      
      if(errorMessages.isEmpty()) {
        
        triagePaymentAmount = triageScenario.getBenefit().getTotalBenefit();
        isPaymentFile = triagePaymentAmount > 0;
        result.setEstimatedBenefit(triagePaymentAmount);
        result.setIsPaymentFile(isPaymentFile);
        
        ReasonabilityTestResults testResults = triageScenario.getReasonabilityTestResults();
        
        if(testResults != null) {
          newScenarioStateCode = COMPLETED;
          
          String structuralChangeCode = triageScenario.getBenefit().getStructuralChangeMethodCode();
          boolean structureChangeEnabled = ! StructuralChangeCodes.NONE.equals(structuralChangeCode);
          boolean structuralChangeAdditiveDivisionTestPassed = testResults.getStructuralChangeTest().getWithinAdditiveDivisionLimit();
          boolean hasFiveReferenceYears = checkHasFiveReferenceYears(triageScenario);
          boolean hasIncomeForAllYears = ScenarioUtils.checkHasIncomeForAllYears(triageScenario);
          boolean hasExpensesForAllYears = ScenarioUtils.checkHasExpensesForAllYears(triageScenario);
          boolean fiscalEndDatesConsistent = checkFiscalEndDatesConsistent(triageScenario);
          boolean notCombinedFarm = checkNotCombinedFarm(triageScenario);
          
          if(isPaymentFile) {
            
            boolean accountingMethodConsistent = checkAccountingMethodConsistent(triageScenario);
            boolean municipalityConsistent = checkMunicipalityConsistent(triageScenario);
            boolean benefitRiskTestPassed = testResults.getBenefitRisk().getResult();
            Double benefitRiskTestVariance = testResults.getBenefitRisk().getVariance();
            boolean benefitLowerThanEstimated = benefitRiskTestVariance != null && benefitRiskTestVariance < 0;
            boolean benefitRiskTestPassedOrBenefitLowerThanEstimated = benefitRiskTestPassed || benefitLowerThanEstimated;
            boolean paymentWithinThreshold = BigDecimal.valueOf(triagePaymentAmount).compareTo(paymentThreshold) <= 0;
            
            paymentPass = structureChangeEnabled
                && structuralChangeAdditiveDivisionTestPassed
                && hasFiveReferenceYears
                && hasIncomeForAllYears
                && hasExpensesForAllYears
                && fiscalEndDatesConsistent
                && notCombinedFarm
                && accountingMethodConsistent
                && municipalityConsistent
                && benefitRiskTestPassedOrBenefitLowerThanEstimated
                && paymentWithinThreshold;
            
            addPaymentPassMessages(
                result,
                structureChangeEnabled,
                structuralChangeAdditiveDivisionTestPassed,
                hasFiveReferenceYears,
                hasIncomeForAllYears,
                hasExpensesForAllYears,
                fiscalEndDatesConsistent,
                notCombinedFarm,
                accountingMethodConsistent,
                municipalityConsistent,
                benefitRiskTestPassedOrBenefitLowerThanEstimated,
                paymentWithinThreshold);
            
          } else {
            
            Boolean referenceMarginTestPassed = testResults.getMarginTest().getWithinLimitOfReferenceMargin();
            boolean varianceOverTheUpperLimitOfReferenceMargin = checkMarginVarianceOverTheUpperLimit(triageScenario);
            boolean referenceMarginTestPassedOrFailedAtTheHighEnd = referenceMarginTestPassed || varianceOverTheUpperLimitOfReferenceMargin;
            
            zeroPass = structureChangeEnabled
                && referenceMarginTestPassedOrFailedAtTheHighEnd
                && structuralChangeAdditiveDivisionTestPassed
                && hasFiveReferenceYears
                && hasIncomeForAllYears
                && hasExpensesForAllYears
                && fiscalEndDatesConsistent
                && notCombinedFarm;
            
            addZeroPassMessages(
                result,
                structureChangeEnabled,
                referenceMarginTestPassedOrFailedAtTheHighEnd,
                structuralChangeAdditiveDivisionTestPassed,
                hasFiveReferenceYears,
                hasIncomeForAllYears,
                hasExpensesForAllYears,
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
      } else if(paymentPass) {
        triageResultType = TRIAGE_RESULT_TYPE_PAYMENT_PASS;
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
        SleepUtils.waitASecond(); // to ensure XSTATE (benefit updatates) are processed in the correct order 
        
        Double finalPaymentAmount = triageScenario.getBenefit().getTotalBenefit();
        boolean paymentConsistent = MathUtils.equalToTwoDecimalPlaces(triagePaymentAmount, finalPaymentAmount);
        
        if(errorMessages.isEmpty() && paymentConsistent) {
          // Update state to Verified. Triggers a Verified Final Benefit Update.
          calculatorService.updateScenario(finalScenario, VERIFIED, null,
              finalScenario.getScenarioCategoryCode(), verifierUserEmail, null, null, null, triageResultType, userId);
        } else {
          createCalculationInconsistencyTask(errorMessages, paymentConsistent, triagePaymentAmount, finalPaymentAmount);
        }
        
      } else {
        crmTransferService.scheduleBenefitTransfer(triageScenario, verifierUserEmail, userId);
      }
      
      result.setScenarioStateCodeDesc(triageScenario.getScenarioStateCodeDescription());
      
    } catch(Exception e) {
      e.printStackTrace();
      logger.error(String.format("Unexpected error processing %d PIN %d: ", programYear, participantPin), e);
      result.setScenarioStateCodeDesc(FAILED_DESCRIPTION);
      String errorMessage = formatExceptionForErrorMessage(e);
      result.getErrorMessages().add(errorMessage);
    }
    
    return result;
  }


  /**
   * If there is an error while processing the TRIAGE scenario and it is left In Progress,
   * the system will try again the next time triage is run. In that case, use the existing In Progress scenario.
   * The query should never return PINs that have Completed or Failed triage scenarios, so checking the scenario
   * state is just a precaution.
   */
  private Scenario createTriageScenario(Integer participantPin, Integer programYear, Integer baseScenarioId, Integer baseScenarioNumber,
      Connection connection, String userId) throws SQLException, ServiceException {
    
    ReadDAO readDAO = new ReadDAO(connection);
    CalculatorDAO calculatorDao = new CalculatorDAO();
    
    List<ScenarioMetaData> scenarioMetadata = readDAO.readProgramYearMetadata(participantPin, programYear);
    ScenarioMetaData triageScenarioMetadata = ScenarioUtils.findScenarioByCategory(scenarioMetadata, programYear, TRIAGE, ScenarioTypeCodes.TRIAGE);
    
    if(triageScenarioMetadata != null && triageScenarioMetadata.stateIsOneOf(ScenarioStateCodes.IN_PROGRESS)) {
      Integer triageScenarioNumber = triageScenarioMetadata.getScenarioNumber();
      logger.debug("Found existing In Progress TRIAGE scenario number: " + triageScenarioNumber + ". Deleting.");
      Integer triageScenarioId = triageScenarioMetadata.getScenarioId();
      calculatorDao.deleteUserScenario(connection, triageScenarioId);
      connection.commit();
    }
    
    Integer triageScenarioNumber = calculatorService.saveScenarioAsNew(baseScenarioId,
        ScenarioTypeCodes.TRIAGE,
        TRIAGE,
        baseScenarioNumber,
        userId);
    
    Scenario triageScenario = getScenario(participantPin, programYear, triageScenarioNumber, connection);
    return triageScenario;
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
  
  
  private boolean checkHasFiveReferenceYears(Scenario scenario) {
    final int numYearsNeeded = 5;
    int referenceScenarioCount = scenario.getReferenceScenarios().size();
    
    boolean hasFiveYearsOfData = referenceScenarioCount == numYearsNeeded;
    
    return hasFiveYearsOfData;
  }


  private boolean checkFiscalEndDatesConsistent(Scenario scenario) {
    
    Integer programYear = scenario.getYear();
    ReferenceScenario lastYearReferenceScenario = scenario.getReferenceScenarioByYear(programYear - 1);
    
    if(lastYearReferenceScenario == null || lastYearReferenceScenario.getFarmingYear() == null
      || lastYearReferenceScenario.getFarmingYear().getFarmingOperations() == null) {
      return false;
    }
    
    FarmingYear lastYearFarmingYear = lastYearReferenceScenario.getFarmingYear();
    List<FarmingOperation> farmingOperations = scenario.getFarmingYear().getFarmingOperations();
    
    for (FarmingOperation farmingOperation : farmingOperations) {
      Integer operationNumber = farmingOperation.getOperationNumber();
      Date programYearFiscalYearEnd = farmingOperation.getFiscalYearEnd();
      
      FarmingOperation lastYearFarmingOperation = lastYearFarmingYear.getFarmingOperationByNumber(operationNumber);
      
      if(lastYearFarmingOperation == null || programYearFiscalYearEnd == null) {
        return false;
      }
      
      Date lastYearFiscalYearEnd = lastYearFarmingOperation.getFiscalYearEnd();
      
      Date programYearFiscalEndMinusOneYear = DateUtils.subtractYears(programYearFiscalYearEnd, 1);
      
      boolean fiscalYearEndChanged = ! programYearFiscalEndMinusOneYear.equals(lastYearFiscalYearEnd);
      
      if(fiscalYearEndChanged) {
        return false;
      }
      
    }
    
    return true;
  }

  private boolean checkNotCombinedFarm(Scenario triageScenario) {
    
    int programYear = triageScenario.getYear();
    int lastYear = programYear - 1;
    List<ScenarioMetaData> scenarioMetaDataList = triageScenario.getScenarioMetaDataList();
    List<ScenarioMetaData> lastYearCombinedFarmScenarios =
        ScenarioUtils.findCombinedFarmScenarios(scenarioMetaDataList, lastYear);
    
    return lastYearCombinedFarmScenarios.isEmpty();
  }


  private boolean checkAccountingMethodConsistent(Scenario scenario) {
    
    boolean accountingMethodsConsistent = true;
    
    Integer programYear = scenario.getYear();
    ReferenceScenario lastYearReferenceScenario = scenario.getReferenceScenarioByYear(programYear - 1);

    if(lastYearReferenceScenario == null || lastYearReferenceScenario.getFarmingYear() == null
      || lastYearReferenceScenario.getFarmingYear().getFarmingOperations() == null) {
      return false;
    }

    FarmingYear lastYearFarmingYear = lastYearReferenceScenario.getFarmingYear();
    List<FarmingOperation> farmingOperations = scenario.getFarmingYear().getFarmingOperations();
    
    for (FarmingOperation farmingOperation : farmingOperations) {
      Integer operationNumber = farmingOperation.getOperationNumber();
      String programYearAccountingCode = farmingOperation.getAccountingCode();
      
      FarmingOperation lastYearFarmingOperation = lastYearFarmingYear.getFarmingOperationByNumber(operationNumber);
      
      if(lastYearFarmingOperation == null) {
        accountingMethodsConsistent = false;
        break;
      }
      
      String lastYearAccountingCode = lastYearFarmingOperation.getAccountingCode();
      
      boolean changed = ! StringUtils.equal(programYearAccountingCode, lastYearAccountingCode);
      
      if(changed) {
        accountingMethodsConsistent = false;
        break;
      }
      
    }
    
    return accountingMethodsConsistent;
  }
  
  
  private boolean checkMunicipalityConsistent(Scenario scenario) {
    
    Integer programYear = scenario.getYear();
    ReferenceScenario lastYearReferenceScenario = scenario.getReferenceScenarioByYear(programYear - 1);

    if(lastYearReferenceScenario == null || lastYearReferenceScenario.getFarmingYear() == null) {
      return false;
    }

    FarmingYear lastYearFarmingYear = lastYearReferenceScenario.getFarmingYear();
    
    String programYearMunicipalityCode = scenario.getFarmingYear().getMunicipalityCode();
    String lastYearMunicipalityCode = lastYearFarmingYear.getMunicipalityCode();
    
    boolean municipalityConsistent = programYearMunicipalityCode.equals(lastYearMunicipalityCode);
    
    return municipalityConsistent;
  }


  private void addZeroPassMessages(BenefitTriageItemResult result,
      boolean structureChangeEnabled,
      boolean referenceMarginTestPassedOrFailedAtTheHighEnd,
      boolean structuralChangeAdditiveDivisionTestPassed,
      boolean hasFiveReferenceYears,
      boolean hasIncomeForAllYears,
      boolean hasExpensesForAllYears,
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
    if( ! hasFiveReferenceYears ) {
      failMessages.add(MESSAGE_FAIL_LESS_THAN_5_YEARS_OF_DATA);
    }
    if( ! hasIncomeForAllYears ) {
      failMessages.add(MESSAGE_FAIL_MISSING_INCOME);
    }
    if( ! hasExpensesForAllYears ) {
      failMessages.add(MESSAGE_FAIL_MISSING_EXPENSES);
    }
    if( ! fiscalEndDatesConsistent ) {
      failMessages.add(MESSAGE_FAIL_FISCAL_YEAR_END_DATE_CHANGED);
    }
    if( ! notCombinedFarm ) {
      failMessages.add(MESSAGE_FAIL_COMBINED_FARM);
    }
  }
  
  
  private void addPaymentPassMessages(BenefitTriageItemResult result,
      boolean structureChangeEnabled,
      boolean structuralChangeAdditiveDivisionTestPassed,
      boolean hasFiveReferenceYears,
      boolean hasIncomeForAllYears,
      boolean hasExpensesForAllYears,
      boolean fiscalEndDatesConsistent,
      boolean notCombinedFarm,
      boolean accountingMethodConsistent,
      boolean municipalityConsistent,
      boolean benefitRiskTestPassedOrBenefitLowerThanEstimated,
      boolean paymentWithinThreshold) {
    
    List<String> failMessages = result.getFailMessages();
    
    if( ! structureChangeEnabled ) {
      failMessages.add(MESSAGE_FAIL_STRUCTURE_CHANGE_NOT_ENABLED);
    }
    if( ! structuralChangeAdditiveDivisionTestPassed ) {
      failMessages.add(MESSAGE_FAIL_STRUCTURAL_CHANGE_ADD_DIV_FAILED);
    }
    if( ! hasFiveReferenceYears ) {
      failMessages.add(MESSAGE_FAIL_LESS_THAN_5_YEARS_OF_DATA);
    }
    if( ! hasIncomeForAllYears ) {
      failMessages.add(MESSAGE_FAIL_MISSING_INCOME);
    }
    if( ! hasExpensesForAllYears ) {
      failMessages.add(MESSAGE_FAIL_MISSING_EXPENSES);
    }
    if( ! fiscalEndDatesConsistent ) {
      failMessages.add(MESSAGE_FAIL_FISCAL_YEAR_END_DATE_CHANGED);
    }
    if( ! notCombinedFarm ) {
      failMessages.add(MESSAGE_FAIL_COMBINED_FARM);
    }
    if( ! accountingMethodConsistent ) {
      failMessages.add(MESSAGE_FAIL_ACCOUNTING_METHOD_CHANGED);
    }
    if( ! municipalityConsistent ) {
      failMessages.add(MESSAGE_FAIL_MUNICIPALITY_CHANGED);
    }
    if( ! benefitRiskTestPassedOrBenefitLowerThanEstimated ) {
      failMessages.add(MESSAGE_FAIL_BENEFIT_RISK_FAILED_AT_HIGH_END);
    }
    if( ! paymentWithinThreshold ) {
      failMessages.add(MESSAGE_FAIL_PAYMENT_TOO_LARGE);
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
    
    List<String> calculationErrors = StrutsUtils.convertActionMessagesToStringList(messages, messageProperties);
    
    errorMessages.addAll(calculationErrors);
    
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
  
  private String formatExceptionForFailedImport(Throwable t) {
    
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


  private String formatExceptionForErrorMessage(Exception e) {
    Throwable t = (e.getCause() != null) ? e.getCause() : e;

    StackTraceElement[] stack = t.getStackTrace();
    StringBuilder msg = new StringBuilder();
    msg.append(t.toString());

    final int maxNumberOfLines = 5;
    int lines = Math.min(maxNumberOfLines, stack.length);
    for (int i = 0; i < lines; i++) {
        msg.append("\tat ")
           .append(stack[i]);
    }

    String failMessage = msg.toString();
    return failMessage;
  }

  private CrmTaskResource createValidationErrorTask(String subject, String description) throws ServiceException {

    CrmValidationErrorResource task = new CrmValidationErrorResource();
    task.setSubject(subject);
    task.setDescription(description);

    String queueId = getValidationErrorQueueId();
    CrmValidationErrorResource newTask = crmDao.createValidationErrorTask(task, queueId);
    
    return newTask;
  }

  private String getValidationErrorQueueId() throws ServiceException {
    // TODO Use a parameter (config key) specific to Benefit Triage or consolidate existing parameters into one for validation errors
    String queueName = configUtil.getValue(ConfigurationKeys.CRM_QUEUES_NPP_CORPORATE);
    String queueId = queryQueueId(queueName);
    return queueId;
  }

  private String queryQueueId(String queueName) throws ServiceException {
    CrmQueueResource queue = crmDao.getQueueByName(queueName);
    return queue.getQueueId();
  }


  private void createCalculationInconsistencyTask(List<String> errorMessages, boolean paymentConsistent, Double triagePaymentAmount,
      Double finalPaymentAmount) throws ServiceException {
    String taskSubject = "Benefit Triage Calculation Issue";
    StringBuilder taskDescription = new StringBuilder();
    
    if( ! paymentConsistent ) {
      String formattedTriagePaymentAmount = StringUtils.formatCurrency(triagePaymentAmount);
      String formattedFinalPaymentAmount = StringUtils.formatCurrency(finalPaymentAmount);
      
      taskDescription.append("Final payment amount does not match triage scenario amount.\n");
      taskDescription.append("Triage Amount: ").append(formattedTriagePaymentAmount).append("\n");
      taskDescription.append("Final Amount: ").append(formattedFinalPaymentAmount).append("\n");
    }
    
    if( ! errorMessages.isEmpty() ) {
      taskDescription.append("Benefit Calculation errors encountered calculating Final benefit:\n");
      for (String message : errorMessages) {
        taskDescription.append("- ").append(message).append("\n");
      }
    }
    
    taskDescription.append(" These issues did not occur when calculating the triage scenario benefit amount.");
    taskDescription.append(" That indicates a bug. Please notify the development team.");
    
    createValidationErrorTask(taskSubject, taskDescription.toString());
  }

}
