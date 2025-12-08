/**
 *
 * Copyright (c) 2018, 
 * Government of British Columbia, 
 * Canada
 *
 * All rights reserved. 
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.reasonability;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.calculator.BenchmarkMarginCalculator;
import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.calculator.CalculatorFactory;
import ca.bc.gov.srm.farm.calculator.InventoryCalculator;
import ca.bc.gov.srm.farm.domain.CropItem;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.InventoryItem;
import ca.bc.gov.srm.farm.domain.LineItem;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.FruitVegTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
import ca.bc.gov.srm.farm.domain.codes.LineItemCodes;
import ca.bc.gov.srm.farm.domain.reasonability.ExpenseTestIACResult;
import ca.bc.gov.srm.farm.exception.ReasonabilityTestException;
import ca.bc.gov.srm.farm.log.LoggingUtils;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.MathUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

/**
 * Expense Test - Industry Average Comparison
 * 
 * @author awilkinson
 */
public class ExpenseTestIAC extends ReasonabilityTest {
  
  private static Logger logger = LoggerFactory.getLogger(ExpenseTestIAC.class);

  private double industryVarianceLimit;
  
  private InventoryCalculator inventoryCalculator = CalculatorFactory.getInventoryCalculator();

  public ExpenseTestIAC() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance();
    
    industryVarianceLimit = config.getExpenseIACIndustryVariance();
  }
  
  @Override
  public void test(Scenario scenario) throws ReasonabilityTestException {
    LoggingUtils.logMethodStart(logger);
    
    ExpenseTestIACResult result = new ExpenseTestIACResult();
    scenario.getReasonabilityTestResults().setExpenseTestIAC(result);

    if (missingRequiredData(scenario)) {
      throw new ReasonabilityTestException(MessageConstants.ERRORS_REASONABILITY_BENEFIT_NOT_CALCULATED);
    }
    
    boolean appleProducer = isAppleProducer(scenario);
    double excludedExpenses = calculateExcludedExpenses(scenario, appleProducer);
    double excludedAccruals = calculateExcludedAccruals(scenario, appleProducer);
    
    double expenseAccrualAdjs = scenario.getFarmingYear().getMarginTotal().getExpenseAccrualAdjs();
    expenseAccrualAdjs -= excludedExpenses;
    expenseAccrualAdjs += excludedAccruals;
    result.setExpenseAccrualAdjs(expenseAccrualAdjs);
    
    BenchmarkMarginCalculator benchmarkMarginCalculator = CalculatorFactory.getBenchmarkMarginCalculator(scenario);
    double industryAverage = benchmarkMarginCalculator.calculateBenchmarkMargin(scenario, CalculatorConfig.CALC_TYPE_EXPENSE, false, true);
    
    boolean withinLimitOfIndustryAverage;
    
    if(industryAverage == 0.0d) {
      result.setIndustryVariance(null);
      withinLimitOfIndustryAverage = false;
    } else {
      double industryVariance = MathUtils.round( (expenseAccrualAdjs - industryAverage) / industryAverage, PERCENT_DECIMAL_PLACES);
      result.setIndustryVariance(industryVariance);
      
      withinLimitOfIndustryAverage = Math.abs(industryVariance) < industryVarianceLimit;
    }
    
    result.setIndustryAverage(industryAverage);
    result.setIndustryVarianceLimit(industryVarianceLimit);
    
    result.setResult(withinLimitOfIndustryAverage);
    
    LoggingUtils.logMethodStart(logger);
  }

  private double calculateExcludedExpenses(Scenario scenario, boolean appleProducer) {
    double excludedExpenses = 0;
    
    if(appleProducer) {
      for (ReferenceScenario programYearScenario : scenario.getReferenceScenariosByYear(scenario.getYear())) {
        for(FarmingOperation fo : programYearScenario.getFarmingYear().getFarmingOperations()) {
          double partnershipPercent = ScenarioUtils.getPartnershipPercent(fo);
          
          if(fo.getIncomeExpenses() != null) {
            for(IncomeExpense curIncomeExpense : fo.getIncomeExpenses()) {
              LineItem lineItem = curIncomeExpense.getLineItem();
              if(curIncomeExpense.getIsExpense() && lineItem.getIsEligible()) {
                
                if(isExcludedForAppleProducers(lineItem.getLineItem())) {
                  excludedExpenses += curIncomeExpense.getTotalAmount() * partnershipPercent;
                }
                
              }
            }
          }
        }
      }
    }
    
    return excludedExpenses;
  }
  
  private double calculateExcludedAccruals(Scenario scenario, boolean appleProducer) {
    double excludedExpenses = 0;
    
    if(appleProducer) {
      for (ReferenceScenario programYearScenario : scenario.getReferenceScenariosByYear(scenario.getYear())) {
        for(FarmingOperation fo : programYearScenario.getFarmingYear().getFarmingOperations()) {
          double partnershipPercent = ScenarioUtils.getPartnershipPercent(fo);
          
          for(InventoryItem curAccrualItem : fo.getAccrualItems()) {
            Integer lineItem = Integer.valueOf(curAccrualItem.getInventoryItemCode());
            
            if(InventoryClassCodes.INPUT.equals(curAccrualItem.getInventoryClassCode())
                || InventoryClassCodes.PAYABLE.equals(curAccrualItem.getInventoryClassCode())) {
              if(isExcludedForAppleProducers(lineItem)) {
                excludedExpenses += inventoryCalculator.calculateChangeInValue(curAccrualItem) * partnershipPercent;
              }
            }
            
          }
        }
      }
    }
    
    return excludedExpenses;
  }

  private boolean isExcludedForAppleProducers(Integer lineItem) {
    List<Integer> excludedExpenseCodes = Arrays.asList(new Integer[] {
        LineItemCodes.CONTAINERS_AND_TWINE,
        LineItemCodes.COMMISSIONS_AND_LEVIES
        });
    return excludedExpenseCodes.contains(lineItem);
  }

  private boolean isAppleProducer(Scenario scenario) {
    
    for (Scenario programYearScenario : scenario.getProgramYearScenarios()) {
      for(FarmingOperation fo : programYearScenario.getFarmingYear().getFarmingOperations()) {
        
        if(fo.getProductiveUnitCapacities() != null) {
          for(ProductiveUnitCapacity curPuc : fo.getProductiveUnitCapacities()) {
            if(FruitVegTypeCodes.APPLE.equals(curPuc.getFruitVegTypeCode())
                && curPuc.getTotalProductiveCapacityAmount() != 0) {
              return true;
            }
          }
        }
      }
    }
    
    for (Scenario programYearScenario : scenario.getProgramYearScenarios()) {
      for(FarmingOperation fo : programYearScenario.getFarmingYear().getFarmingOperations()) {
        
        if(fo.getCropItems() != null) {
          for(CropItem curItem : fo.getCropItems()) {
            if(hasNonZeroAppleQuantities(curItem)) {
              return true;
            }
          }
        }
      }
    }
    
    for (Scenario programYearScenario : scenario.getProgramYearScenarios()) {
      for(FarmingOperation fo : programYearScenario.getFarmingYear().getFarmingOperations()) {
        
        if(fo.getIncomeExpenses() != null) {
          for(IncomeExpense curItem : fo.getIncomeExpenses()) {
            if(hasNonZeroAppleIncome(curItem)) {
              return true;
            }
          }
        }
      }
    }
    
    return false;
  }

  private boolean hasNonZeroAppleQuantities(CropItem curItem) {
    return FruitVegTypeCodes.APPLE.equals(curItem.getFruitVegTypeCode())
        &&
        (
            (curItem.getTotalQuantityProduced() != null && curItem.getTotalQuantityProduced() != 0)
            || (curItem.getTotalQuantityStart() != null && curItem.getTotalQuantityStart() != 0)
            || (curItem.getTotalQuantityEnd() != null && curItem.getTotalQuantityEnd() != 0)
        );
  }

  private boolean hasNonZeroAppleIncome(IncomeExpense curItem) {
    return FruitVegTypeCodes.APPLE.equals(curItem.getLineItem().getFruitVegTypeCode())
        && !curItem.getIsExpense()
        && curItem.getTotalAmount() != 0;
  }

  public static boolean missingRequiredData(Scenario scenario) {
    return scenario.getFarmingYear() == null
        || scenario.getFarmingYear().getMarginTotal() == null
        || scenario.getFarmingYear().getMarginTotal().getExpenseAccrualAdjs() == null;
  }

}
