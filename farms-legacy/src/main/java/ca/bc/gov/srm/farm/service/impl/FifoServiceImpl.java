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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
import ca.bc.gov.srm.farm.crm.resource.CrmEnrolmentResource;
import ca.bc.gov.srm.farm.crm.resource.CrmProgramYearResource;
import ca.bc.gov.srm.farm.dao.FifoDAO;
import ca.bc.gov.srm.farm.dao.StagingDAO;
import ca.bc.gov.srm.farm.dao.VersionDAO;
import ca.bc.gov.srm.farm.domain.BasePricePerUnit;
import ca.bc.gov.srm.farm.domain.BasePricePerUnitYear;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.BPU;
import ca.bc.gov.srm.farm.domain.codes.BPUYear;
import ca.bc.gov.srm.farm.domain.codes.MunicipalityCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.StructuralChangeCodes;
import ca.bc.gov.srm.farm.domain.fifo.FifoCalculationItem;
import ca.bc.gov.srm.farm.domain.fifo.FifoItemResult;
import ca.bc.gov.srm.farm.domain.fifo.FifoResults;
import ca.bc.gov.srm.farm.domain.fifo.FifoStatus;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResults;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.message.MessageKeys;
import ca.bc.gov.srm.farm.security.BusinessAction;
import ca.bc.gov.srm.farm.service.AdjustmentService;
import ca.bc.gov.srm.farm.service.BaseService;
import ca.bc.gov.srm.farm.service.BenefitService;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ClientService;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.CrmTransferService;
import ca.bc.gov.srm.farm.service.FifoService;
import ca.bc.gov.srm.farm.service.ReasonabilityTestService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.srm.farm.util.PropertyLoader;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

public class FifoServiceImpl extends BaseService implements FifoService {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  private CrmTransferService crmTransferService;
  private CalculatorService calculatorService;
  private ConfigurationUtility configUtil;
  
  private Properties messageProperties;
  
  private ObjectMapper jsonObjectMapper = new ObjectMapper();
  
  
  public FifoServiceImpl() {
    crmTransferService = ServiceFactory.getCrmTransferService();
    configUtil = ConfigurationUtility.getInstance();
    calculatorService = ServiceFactory.getCalculatorService();
    messageProperties = PropertyLoader.loadProperties(MessageKeys.MESSAGES_FILE_PATH);
  }

  @Override
  public List<FifoStatus> getFifoStatusByYear(int year) throws ServiceException {
    logMethodStart(logger);

    Transaction transaction = openTransaction();
    FifoDAO fifoDao = new FifoDAO();

    List<FifoStatus> fifoStatusList = null;
    try {

      fifoStatusList = fifoDao.readFifoStatusByYear(transaction, year);

    } catch (Exception e) {
      throw new ServiceException(e);
    }

    logMethodEnd(logger);
    return fifoStatusList;
  }
  
  @Override
  public void processFifoCalculations(final Connection connection, final File csvFile, final Integer importVersionId,
      final String userId) throws ServiceException {
    logMethodStart(logger);
    
    CacheFactory.getRequestCache().addItem(CacheKeys.CURRENT_BUSINESS_ACTION, BusinessAction.system());

    VersionDAO vdao = null;
    StagingDAO sdao = null;
    FifoDAO fifoDao = new FifoDAO();
    
    FifoResults fifoResults = new FifoResults();
    List<FifoItemResult> fifoItemResults = new ArrayList<>();
    fifoResults.setFifoItemResults(fifoItemResults);

    try {
      vdao = new VersionDAO(connection);
      sdao = new StagingDAO(connection);
      connection.setAutoCommit(false);

      vdao.startImport(importVersionId, userId);
      connection.commit();
      sdao.status(importVersionId, "Started");
      
      List<FifoCalculationItem> fifoItems = fifoDao.readFifoCalculationItems(connection);
      
      calculateFifoBenefits(connection, fifoItems, fifoItemResults, sdao, importVersionId, userId);

      sdao.status(importVersionId, "FIFO calculations completed.");
      markJobComplete(connection, importVersionId, userId, vdao, fifoResults);

    } catch (Exception e) {
      logger.error("Unexpected error: ", e);
      String formattedException = formatException(e);
      fifoResults.setUnexpectedError(formattedException);
      String resultsJson = convertResultsToJson(fifoResults);
      try {
        if (vdao != null) {
          vdao.importFailed(importVersionId, resultsJson, userId);
          connection.commit();
        }
        if (sdao != null) {
          sdao.status(importVersionId, "Failed to complete FIFO calculations.");
        }
      } catch (SQLException | IOException ex) {
        throw new ServiceException(ex);
      }
    }
    
    logMethodEnd(logger);
  }

  private String convertResultsToJson(FifoResults fifoResults) throws ServiceException {
    String resultsJson;
    try {
      resultsJson = jsonObjectMapper.writeValueAsString(fifoResults);
    } catch (JsonProcessingException jsonEx) {
      throw new ServiceException(jsonEx);
    }
    return resultsJson;
  }


  @Override
  public void calculateFifoBenefits(Connection connection, List<FifoCalculationItem> fifoItems,
      List<FifoItemResult> results, StagingDAO sdao, Integer importVersionId, String userId)
      throws Exception {
    logMethodStart(logger);
    
    String verifierUserEmail = configUtil.getValue(ConfigurationKeys.FIFO_VERIFIER_USER_EMAIL);
    
    Map<Integer, List<BPU>> yearBpuListMap = new HashMap<>();
    
    int fifoItemCount = fifoItems.size();
    int itemsProcessed = 0;
    
    for(FifoCalculationItem item : fifoItems) {
      Integer participantPin = item.getParticipantPin();
      Integer programYear = item.getProgramYear();
      
      // The query does not return records with FAILED status
      // so we won't hit this condition but it is here just to be safe.
      if(FAILED.equals(item.getFifoScenarioStateCode())) {
        continue;
      }
      
      // If they are not enrolled for this program year then we don't
      // need to calculate the estimated benefit.
      boolean enrolled = checkEnrolled(participantPin, programYear);
      if( ! enrolled ) {
        continue;
      }
      
      Integer fifoScenarioNumber;
      boolean createdNewScenario = false;
      if(item.getFifoScenarioNumber() != null) {
        fifoScenarioNumber = item.getFifoScenarioNumber();
      } else {
        fifoScenarioNumber = calculatorService.saveScenarioAsNew(item.getCraScenarioId(),
            ScenarioTypeCodes.FIFO,
            FIFO,
            item.getCraScenarioNumber(),
            userId);
        
        createdNewScenario = true;
      }
      
      Scenario fifoScenario = getScenario(participantPin, programYear, fifoScenarioNumber, connection);

      if(createdNewScenario) {
        AdjustmentService adjustmentService = ServiceFactory.getAdjustmentService();
        adjustmentService.makeInventoryValuationAdjustments(fifoScenario, true, true);
        
        fifoScenario = getScenario(participantPin, programYear, fifoScenarioNumber, connection);
      }
      
      fixMissingBpus(fifoScenario, yearBpuListMap);
      fixBpuZeroes(fifoScenario);
      
      List<String> errorMessages = calculateBenefit(fifoScenario, userId);
      
      FifoItemResult result = new FifoItemResult();
      results.add(result);
      result.setParticipantPin(fifoScenario.getClient().getParticipantPin());
      result.setClientName(fifoScenario.getClient().getOwner().getFullName());
      result.setProgramYear(fifoScenario.getYear());
      result.setScenarioNumber(fifoScenario.getScenarioNumber());
      result.setErrorMessages(errorMessages);
      
      String currentScenarioStateCode = fifoScenario.getScenarioStateCode();
      
      String newScenarioStateCode = FAILED;
      boolean isPaymentFile = false;
      boolean zeroPass = false;
      Double totalBenefit = null;
      
      try {
        
        if(errorMessages.isEmpty()) {
          
          totalBenefit = fifoScenario.getBenefit().getTotalBenefit();
          isPaymentFile = totalBenefit > 1;
          result.setEstimatedBenefit(totalBenefit);
          result.setIsPaymentFile(isPaymentFile);
          
          runReasonabilityTests(userId, fifoScenario);
          fifoScenario = getScenario(participantPin, programYear, fifoScenarioNumber, connection);
          ReasonabilityTestResults testResults = fifoScenario.getReasonabilityTestResults();
          
          if(testResults != null) {
            newScenarioStateCode = COMPLETED;
            
            if( ! isPaymentFile ) {
              String structuralChangeCode = fifoScenario.getBenefit().getStructuralChangeMethodCode();
              boolean structureChangeEnabled = ! StructuralChangeCodes.NONE.equals(structuralChangeCode);
              
              zeroPass = structureChangeEnabled
                  && testResults.getMarginTest().getResult()
                  && testResults.getStructuralChangeTest().getResult();
            }
            
          }
        }
        
        String fifoResultType = null;
        if(zeroPass) {
          fifoResultType = FIFO_RESULT_TYPE_ZERO_PASS;
        }
  
        logger.debug("Updating scenario state");
        if( ! currentScenarioStateCode.equals(newScenarioStateCode) ) {
          calculatorService.updateScenario(fifoScenario, newScenarioStateCode, null,
              fifoScenario.getScenarioCategoryCode(), verifierUserEmail, null, null, null, fifoResultType, userId);
          fifoScenario = getScenario(participantPin, programYear, fifoScenarioNumber, connection);
        }
        
        if(zeroPass) {
          // Create a Verified Final scenario from the FIFO scenario
          Integer finalScenarioNumber = calculatorService.saveScenarioAsNew(item.getCraScenarioId(),
              ScenarioTypeCodes.USER,
              UNKNOWN,
              fifoScenarioNumber,
              userId);
          
          Scenario finalScenario = getScenario(participantPin, programYear, finalScenarioNumber, connection);
          
          // Update category to Final. Triggers an In Progress Final Benefit Update.
          calculatorService.updateScenario(finalScenario, finalScenario.getScenarioStateCode(), null,
              FINAL, verifierUserEmail, null, null, null, fifoResultType, userId);
          finalScenario = getScenario(participantPin, programYear, finalScenarioNumber, connection);
  
          // Expecting errorMessages to be empty because it was when calculating for the FIFO scenario
          errorMessages = calculateBenefit(finalScenario, userId);
          finalScenario = getScenario(participantPin, programYear, finalScenarioNumber, connection);
          
          runReasonabilityTests(userId, finalScenario);
          finalScenario = getScenario(participantPin, programYear, finalScenarioNumber, connection);
          
          // Update state to Verified. Triggers a Verified Final Benefit Update.
          calculatorService.updateScenario(finalScenario, VERIFIED, null,
              finalScenario.getScenarioCategoryCode(), verifierUserEmail, null, null, null, fifoResultType, userId);
          

        } else {
          crmTransferService.scheduleBenefitTransfer(fifoScenario, verifierUserEmail, userId);
        }
        
        result.setScenarioStateCodeDesc(fifoScenario.getScenarioStateCodeDescription());
        
      } catch(Exception e) {
        e.printStackTrace();
        logger.error("Unexpected error: ", e);
        result.setScenarioStateCodeDesc(FAILED_DESCRIPTION);
      }
      
      itemsProcessed++;
      if(sdao != null && importVersionId != null) {
        sdao.status(importVersionId, String.format("%d of %d calculations completed.", itemsProcessed, fifoItemCount));
      }
    }

    logMethodEnd(logger);
  }

  private Scenario getScenario(Integer participantPin, Integer programYear, Integer fifoScenarioNumber, Connection connection)
      throws ServiceException {
    ClientService clientService = ClientServiceFactory.getInstance(connection);
    return clientService.getClientInfoWithHistory(
        participantPin, programYear, fifoScenarioNumber, ClientService.COMP_FIRST_MODE);
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

  private List<String> calculateBenefit(Scenario scenario, final String user) throws Exception {
    List<String> errorMessages = new ArrayList<>();
    
    BenefitNullFixer nullFixer = CalculatorFactory.getBenefitNullFixer(scenario);
    nullFixer.fixNulls(scenario);
 
    ActionMessages messages = new ActionMessages();
    BenefitValidator validator = CalculatorFactory.getBenefitValidator(scenario);
    
    // TODO For apples accruals, if the Receivable is missing use last years value
    
    boolean missingBpus = !validator.validateBpus(scenario);
    if(missingBpus) {
      StructuralChangeCalculator scCalc = CalculatorFactory.getStructuralChangeCalculator(scenario);
      String structuralChangeCode = StructuralChangeCodes.NONE;
      String expenseStructuralChangeCode = StructuralChangeCodes.NONE;
      scCalc.updateStructuralChangeCode(structuralChangeCode, expenseStructuralChangeCode);
    }
    
    BenefitService benefitService = ServiceFactory.getBenefitService();
    messages = benefitService.calculateBenefit(scenario, user, true, true, true);
    
    if(!messages.isEmpty()) {
      
      for(@SuppressWarnings("unchecked") Iterator<ActionMessage> mi = messages.get(); mi.hasNext(); ) {
        ActionMessage msg = mi.next();
        errorMessages.add(messageProperties.getProperty(msg.getKey()));
      }
      
    }
    
    logMethodEnd(logger, errorMessages);
    return errorMessages;
  }


  private void fixBpuZeroes(Scenario scenario) {
    
    for(ReferenceScenario refScenario : scenario.getAllScenarios()) {
      for(FarmingOperation fo : refScenario.getFarmingYear().getFarmingOperations()) {
        for(ProductiveUnitCapacity puc : fo.getAllProductiveUnitCapacitiesForStructureChange()) {
          BasePricePerUnit bpu = puc.getBasePricePerUnit();
          if(bpu != null) {
            List<BasePricePerUnitYear> bpuYears = bpu.getBasePricePerUnitYears().stream().sorted(
                (o1, o2) -> o1.getYear().compareTo(o2.getYear())).collect(Collectors.toList());
            for (BasePricePerUnitYear bpuYear : bpuYears) {
              double margin = bpuYear.getMargin().doubleValue();
              double expense = bpuYear.getExpense().doubleValue();
              if(margin == 0.0d || expense == 0.0d) {
                BasePricePerUnitYear previousBpuYear = getPreviousBasePricePerUnitYear(bpu, bpuYear.getYear());
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


  private void fixMissingBpus(Scenario scenario, Map<Integer, List<BPU>> yearBpuListMap) throws ServiceException {
    
    CodesService codesService = ServiceFactory.getCodesService();
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
          
          if(bpu == null) {
            
            BPU previousYearBPU = previousYearBpus.stream()
            .filter(pyb -> codeMatches(puc, pyb)
                && (pyb.getMunicipalityCode().equals(municipalityCode)
                    || pyb.getMunicipalityCode().equals(MunicipalityCodes.ALL_MUNICIPALITIES)))
            .max((o1, o2) -> Integer.valueOf(o1.getMunicipalityCode()).compareTo(Integer.valueOf(o2.getMunicipalityCode())))
            .orElse(null);
            
            if(previousYearBPU != null) {
              bpu = new BasePricePerUnit();
              bpu.setMunicipalityCode(previousYearBPU.getMunicipalityCode());
              
              if("SG".equals(previousYearBPU.getInvSgType())) {
                bpu.setStructureGroupCode(previousYearBPU.getInvSgCode());
                bpu.setComment(previousYearBPU.getInvSgCodeDescription());
              } else { // "INV"
                bpu.setInventoryCode(previousYearBPU.getInvSgCode());
                bpu.setComment(previousYearBPU.getInvSgCodeDescription());
              }
              
              List<BPUYear> prevYearBpuYears = Arrays.asList(previousYearBPU.getYears()).stream().sorted(
                  (o1, o2) -> o2.getYear().compareTo(o1.getYear())).collect(Collectors.toList());
              List<BasePricePerUnitYear> basePricePerUnitYears = new ArrayList<>();
              
              boolean mostRecentYear = true;
              for(BPUYear prevYearBpuYear : prevYearBpuYears) {
                
                Integer curBpuYear = prevYearBpuYear.getYear();
                if(mostRecentYear) {
                  mostRecentYear = false;
                  BasePricePerUnitYear bpuYear = new BasePricePerUnitYear();
                  // Add year + 1 because we got this from the previous program year's BPUs
                  // so it doesn't the latest reference year
                  bpuYear.setYear(curBpuYear + 1);
                  bpuYear.setMargin(prevYearBpuYear.getAverageMargin());
                  bpuYear.setExpense(prevYearBpuYear.getAverageExpense());
                  basePricePerUnitYears.add(bpuYear);
                }
                
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

  private ReasonabilityTestResults runReasonabilityTests(String userId, Scenario scenario) {
    ReasonabilityTestResults testResults = null;
    try {
      ReasonabilityTestService testService = ReasonabilityTestServiceFactory.getInstance();
      
      testResults = testService.test(scenario);
      calculatorService.updateReasonabilityTests(scenario, testResults, userId);
      
    } catch(Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
    }
    return testResults;
  }

  private void markJobComplete(
      final Connection connection,
      final Integer importVersionId,
      final String user,
      final VersionDAO vdao,
      final FifoResults fifoResults) throws Exception {
    logMethodStart(logger);
    
    Boolean hasErrors = Boolean.FALSE;
    vdao.uploadedVersion(importVersionId, "", hasErrors, user);
    String resultsJson = convertResultsToJson(fifoResults);
    vdao.importCompleted(importVersionId, resultsJson, user);
    connection.commit();
    
    logMethodEnd(logger);
  }
  
  private String formatException(final Throwable t) {
    
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
