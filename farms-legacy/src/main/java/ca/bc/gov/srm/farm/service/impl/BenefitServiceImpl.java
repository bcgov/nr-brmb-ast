/**
 *
 * Copyright (c) 2009,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.calculator.BenefitCalculator;
import ca.bc.gov.srm.farm.calculator.BenefitNullFixer;
import ca.bc.gov.srm.farm.calculator.BenefitValidator;
import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.calculator.CalculatorFactory;
import ca.bc.gov.srm.farm.calculator.NegativeMarginCalculator;
import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationUtility;
import ca.bc.gov.srm.farm.dao.CalculatorDAO;
import ca.bc.gov.srm.farm.dao.WriteDAO;
import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.Margin;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.StructuralChangeCodes;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.BaseService;
import ca.bc.gov.srm.farm.service.BenefitService;
import ca.bc.gov.srm.farm.transaction.Transaction;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.ScenarioUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * BenefitServiceImpl
 */
final class BenefitServiceImpl extends BaseService implements BenefitService {
  private Logger logger = LoggerFactory.getLogger(BenefitServiceImpl.class);
  
  
  /**
   * validate, calculate, save
   * 
   * @param scenario scenario
   * @param userId userId
   * @throws Exception on error
   * 
   * @return ActionMessages messages
   */
  @Override
  public ActionMessages calculateBenefit(final Scenario scenario, final String userId) 
  throws Exception {
    return calculateBenefit(scenario, userId, true, true, true);
  }
  
  
  /**
   * Try to get all the errors that might cause the benefit to not
   * be calculated.
   * 
   * @param scenario scenario
   * @throws Exception on error
   * 
   * @return ActionMessages messages
   */
  @Override
  public ActionMessages getAllValidationErrors(final Scenario scenario, final String userId) 
  throws Exception {
    return calculateBenefit(scenario, userId, false, true, false);
  }
  
  
  
  /**
   * validate, calculate, save
   * 
   * @param scenario scenario
   * @param userId userId
   * @param saveBenefit true if you want the calculations saved
   * @param validate true if you want to validate the scenario before calculation
   * @param recalculateOverridables true if you want to recalculate fields
   *                                that can be overridden on the benefit screen
   * @throws Exception on error
   * 
   * @return ActionMessages messages
   */
  @Override
  public ActionMessages calculateBenefit(
      final Scenario scenario,
      final String userId,
      final boolean saveBenefit,
      final boolean validate,
      final boolean recalculateOverridables) 
  throws Exception {
    //
    // Create missing objects so that even if validation fails
    // we don't have to put null checks everywhere.
    //
    BenefitNullFixer nullFixer = CalculatorFactory.getBenefitNullFixer(scenario);
    nullFixer.fixNulls(scenario);
    BenefitCalculator calculator = CalculatorFactory.getBenefitCalculator(scenario);
    
    //
    // As per FARM-776, users wanted the StructuralChangeNotable flag
    // to always be recalculated except from the benefit screen.
    // A similar rule applies to applied benefit percent for combined farms.
    //
    if(recalculateOverridables) {
      calculator.resetOverridables();
    }
    
    ActionMessages errors;
    if(validate) {
      errors = validatePreCalculation(scenario);
    } else {
      errors = new ActionMessages();
    }
    
    if(errors.isEmpty()) {
      
      calculator.calculateBenefitPhase1();
      
      if(validate) {
        errors = validatePhase1Calculation(scenario);
      }
      
      if(errors.isEmpty()) {
        
        // Run before Negative Margin to determine if there is a Megative Margin Benefit
        calculator.calculateBenefitPhase2();
        
        if (scenario.isNegativeMarginCalculationEnabled()) {
          NegativeMarginCalculator negativeMarginCalculator = CalculatorFactory.getNegativeMarginCalculator(scenario);
          negativeMarginCalculator.calculateDeemedPiTotals(userId);
          
          errors = validateNegativeMargin(negativeMarginCalculator);
          if(errors.isEmpty()) {
            // Run again so the Negative Margin (PI) deduction can be applied 
            calculator.calculateBenefitPhase2();
          }
        }
        
        if(saveBenefit) {
          save(scenario, userId);
        }
      }
    }

    return errors;
  }


  /**
   * Validate the scenario to make sure it is safe
   * to calculate the benefit.
   */
  private ActionMessages validatePreCalculation(final Scenario scenario) {
    ActionMessages errors = new ActionMessages();
    BenefitValidator validator = CalculatorFactory.getBenefitValidator(scenario);
    
    Benefit benefit = scenario.getFarmingYear().getBenefit();
    String scMethod = benefit.getStructuralChangeMethodCode();
    boolean validateBpus = !StructuralChangeCodes.NONE.equals(scMethod);
    
    if(validateBpus) {
      validateBPUs(scenario, errors);
    }
    
    if(scenario.isInCombinedFarm()) {
      validateCombinedFarm(scenario, errors);
    }
    
    if(!validator.hasEnoughReferenceYears(scenario)) {
      errors.add("", new ActionMessage(MessageConstants.ERRORS_850_NOT_ENOUGH_REF_YEARS));
    }

    return errors;
  }


  private void validateBPUs(final Scenario scenario, ActionMessages errors) {
    BenefitValidator validator = CalculatorFactory.getBenefitValidator(scenario);
    
    if(!validator.validateBpus(scenario)) {
      
      if(validator.getRefYearMissingBpuInventoryCodeList().size() > 0) {
        String key = MessageConstants.ERRORS_850_STRUCTURAL_CHANGE_MISSING_BPU_INV_CODES;
        List<String> codeList = validator.getRefYearMissingBpuInventoryCodeList();
        
        //
        // Looks like there's no way to show a dynamic number
        // of parameters using the message resourses, so turn it
        // into a single CSV string ourselves.
        //
        String codeCsv = StringUtils.toCsv(codeList);
        errors.add("", new ActionMessage(key, codeCsv));
      }
      
      if(validator.getRefYearMissingBpuStructureGroupCodeList().size() > 0) {
        String key = MessageConstants.ERRORS_850_STRUCTURAL_CHANGE_MISSING_BPU_SG_CODES;
        List<String> codeList = validator.getRefYearMissingBpuStructureGroupCodeList();
        String codeCsv = StringUtils.toCsv(codeList);
 
        errors.add("", new ActionMessage(key, codeCsv));
      }
      
      if(validator.getProgramYearMissingBpuInventoryCodeList().size() > 0) {
        String key = MessageConstants.ERRORS_850_COMBINED_FARM_MISSING_BPU_INV_CODES;
        List<String> codeList = validator.getProgramYearMissingBpuInventoryCodeList();
        String codeCsv = StringUtils.toCsv(codeList);
        
        errors.add("", new ActionMessage(key, codeCsv));
      }
      
      if(validator.getProgramYearMissingBpuStructureGroupCodeList().size() > 0) {
        String key = MessageConstants.ERRORS_850_COMBINED_FARM_MISSING_BPU_SG_CODES;
        List<String> codeList = validator.getProgramYearMissingBpuStructureGroupCodeList();
        String codeCsv = StringUtils.toCsv(codeList);
        
        errors.add("", new ActionMessage(key, codeCsv));
      }
      
    }
  }


  private void validateCombinedFarm(Scenario scenario, ActionMessages errors) {

    boolean foundInterim = false;
    boolean foundNonInterim = false;
    int cashMarginsOptInCount = 0;
    int cashMarginsNotOptInCount = 0;
    if(scenario.isInCombinedFarm()) {
      for (Scenario curScenario : scenario.getCombinedFarm().getScenarios()) {
        if(curScenario.isInterim()) {
          foundInterim = true;
        } else {
          foundNonInterim = true;
        }
        if(curScenario.getFarmingYear().getIsCashMargins()) {
          cashMarginsOptInCount++;
        } else {
          cashMarginsNotOptInCount++;
        }
      }
    }
    
    if(cashMarginsOptInCount > 0 && cashMarginsNotOptInCount > 0) {
      errors.add("", new ActionMessage(MessageConstants.ERRORS_COMBINED_SIMPLIFIED_MARGINS_MISMATCH));
    }
    
    if(foundInterim && foundNonInterim) {
      errors.add("", new ActionMessage(MessageConstants.ERRORS_850_COMBINED_FARM_INTERIM_AND_NON_INTERIM));
    }
  }
  
  
  /**
   * At this point we are supposed to see if the three reference
   * years have non-zero income and expenses.
   * 
   * @param   scenario  scenario
   *
   * @return  ActionMessages
   */
  private ActionMessages validatePhase1Calculation(final Scenario scenario) {
    ActionMessages errors = new ActionMessages();
    BenefitValidator validator = CalculatorFactory.getBenefitValidator(scenario);
    
    boolean ok = validator.validateReferenceYears(scenario);
    
    if(!ok) {
      String key = MessageConstants.ERRORS_850_REF_YEAR_NO_INCOME_EXPENSES;
      errors.add("", new ActionMessage(key));
    }
    
    return errors;
  }
  
  
  /**
   * 
   * @param scenario scenario
   * @param userId userId
   * @throws Exception on Exception
   */
  private void save(final Scenario scenario, final String userId) throws Exception {
    Transaction transaction = null;
    
    try {
      transaction = openTransaction();
      WriteDAO dao = new WriteDAO();
      CalculatorDAO calculatorDAO = new CalculatorDAO();

      String appVerKey = ConfigurationKeys.APPLICATION_VERSION;
      String appVersion = ConfigurationUtility.getInstance().getValue(appVerKey);
      
      transaction.begin();
      
      List<Scenario> scenarios;
      if(scenario.isInCombinedFarm()) {
        scenarios = scenario.getCombinedFarm().getScenarios();
      } else {
        scenarios = new ArrayList<>();
        scenarios.add(scenario);
      }
      
      for(Scenario curScenario : scenarios) {
        Integer pyScenarioId = curScenario.getScenarioId();
        
        //
        // save program year stuff
        //
        Benefit benefit = curScenario.getFarmingYear().getBenefit();
        dao.writeBenefit(transaction, benefit, pyScenarioId, userId);
        
        dao.writeCalculatorVersion(transaction, appVersion, pyScenarioId, userId);
        
        MarginTotal mt = curScenario.getFarmingYear().getMarginTotal();
        dao.writeMarginTotal(transaction, mt, pyScenarioId, userId);
        
        for(FarmingOperation fo : curScenario.getFarmingYear().getFarmingOperations()) {
          Integer opId = fo.getFarmingOperationId();
          Margin margin = fo.getMargin();
          
          dao.writeMargin(transaction, margin, pyScenarioId, opId, userId);
          
          Map<String, String> parameters = CalculatorConfig.getParameters(curScenario.getYear());
          calculatorDAO.upsertScenarioParameters(transaction, curScenario.getScenarioId(), parameters, userId);
        }
        
        //
        // save reference scenario stuff (no benefit)
        //
        for(ReferenceScenario rs : curScenario.getReferenceScenarios()) {
          
          Integer scenarioId = rs.getScenarioId();  
          mt = rs.getFarmingYear().getMarginTotal();
          dao.writeMarginTotal(transaction, mt, scenarioId, userId);
          
          dao.writeReferenceScenario(transaction, rs, pyScenarioId, userId);
          
          for(FarmingOperation fo : rs.getFarmingYear().getFarmingOperations()) {
            Integer opId = fo.getFarmingOperationId();
            Margin margin = fo.getMargin();
            
            dao.writeMargin(transaction, margin, scenarioId, opId, userId);
          }
        }
        
      }
      
      transaction.commit();
    } catch (InvalidRevisionCountException e) {
      logger.warn("Optimistic locking exception: ", e);
      if (transaction != null) {
        transaction.rollback();
      }
      throw e;
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      logger.error(ScenarioUtils.getScenarioInfoForLog(scenario));
      
      if (transaction != null) {
        transaction.rollback();
      }
      if(e instanceof ServiceException) {
        throw e;
      }
      throw new ServiceException(e);
    } finally {
      closeTransaction(transaction);
    }
  }

  
  private ActionMessages validateNegativeMargin(NegativeMarginCalculator negativeMarginCalculator) {
    ActionMessages errors = new ActionMessages();
    List<String> failedInventoryCodeList = negativeMarginCalculator.getNegativeMarginFailedInventoryCodeList();
    
    if(failedInventoryCodeList.size() > 0) {
      String key = MessageConstants.ERRORS_850_NEGATIVE_MARGIN_FAILED_INVENT0RY_CODES;
      String codeCsv = StringUtils.toCsv(failedInventoryCodeList);

      errors.add("", new ActionMessage(key, codeCsv));
    }
    
    return errors;
  }
  
}
