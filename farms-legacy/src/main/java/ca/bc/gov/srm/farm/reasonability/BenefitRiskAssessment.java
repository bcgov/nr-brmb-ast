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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.calculator.BenchmarkMarginCalculator;
import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.calculator.CalculatorFactory;
import ca.bc.gov.srm.farm.calculator.ProductiveValueCalculator;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.reasonability.BenefitRiskAssessmentTestResult;
import ca.bc.gov.srm.farm.domain.reasonability.BenefitRiskCombinedFarmResult;
import ca.bc.gov.srm.farm.domain.reasonability.BenefitRiskProductiveUnit;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResults;
import ca.bc.gov.srm.farm.exception.ReasonabilityTestException;
import ca.bc.gov.srm.farm.log.LoggingUtils;
import ca.bc.gov.srm.farm.ui.struts.calculator.benefit.BenefitUtils;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.MathUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * @author awilkinson
 */
public class BenefitRiskAssessment extends ReasonabilityTest {
  
  private static Logger logger = LoggerFactory.getLogger(BenefitRiskAssessment.class);

  public static final String INFO_PASS_BENCHMARK_ZERO_OR_NEGATIVE = 
      "Variance not calculated. Pass because the Benefit Benchmark is less than or equal to zero.";
  public static final String INFO_PASS_UNDER_MINIMUM_BENEFIT = 
      "Variance not calculated. Pass because the benefit is less than the minimum "
      + StringUtils.formatCurrency(CalculatorConfig.MIN_BENEFIT);
  
  private double benefitRiskVarianceLimit;
  private double benefitRiskDeductible;
  private double benefitRiskPayoutLevel;

  public BenefitRiskAssessment() {
    ReasonabilityConfiguration config = ReasonabilityConfiguration.getInstance();
    benefitRiskVarianceLimit = config.getBenefitRiskVariance();
    benefitRiskDeductible = config.getBenefitRiskDeductiblePercent();
    benefitRiskPayoutLevel = config.getBenefitRiskPayoutLevelPercent();
  }
  
  @Override
  public void test(Scenario scenario) throws ReasonabilityTestException {
    LoggingUtils.logMethodStart(logger);
    
    BenefitRiskAssessmentTestResult result = new BenefitRiskAssessmentTestResult();
    ReasonabilityTestResults reasonabilityTestResults = scenario.getReasonabilityTestResults();
    reasonabilityTestResults.setBenefitRisk(result);
    result.setBenefitRiskDeductible(benefitRiskDeductible);
    result.setBenefitRiskPayoutLevel(benefitRiskPayoutLevel);
    
    if (missingRequiredData(scenario)) {
      throw new ReasonabilityTestException(MessageConstants.ERRORS_REASONABILITY_BENEFIT_NOT_CALCULATED);
    }
      
    double programYearMargin = scenario.getFarmingYear().getBenefit().getProgramYearMargin();
    double totalBenefit = BenefitUtils.getAgristabilityTotalBenefit(scenario);
    double industryAverageMargin = 0;
    
    ProductiveValueCalculator pvCalc = CalculatorFactory.getProductiveValueCalculator(scenario);
    BenchmarkMarginCalculator benchmarkMarginCalculator = CalculatorFactory.getBenchmarkMarginCalculator(scenario);
    List<Integer> bpuYears = benchmarkMarginCalculator.getBpuYears(scenario, false, true);
    
    List<BenefitRiskProductiveUnit> productiveUnits = result.getBenefitRiskProductiveUnits();
    
    Map<String, ProductiveUnitCapacity> pucMap = ScenarioUtils.getConsolidatedProductiveUnitsMap(scenario, null, true);
    
    for(ProductiveUnitCapacity puc : pucMap.values()) {
      
      double bpuCalculated = pvCalc.calculateBpuAverage(puc, CalculatorConfig.CALC_TYPE_MARGIN, bpuYears);
      double productiveCapacity = puc.getTotalProductiveCapacityAmount();
      
      BenefitRiskProductiveUnit brpu = new BenefitRiskProductiveUnit();
      brpu.setInventoryItemCode(puc.getInventoryItemCode());
      brpu.setInventoryItemCodeDescription(puc.getInventoryItemCodeDescription());
      brpu.setStructureGroupCode(puc.getStructureGroupCode());
      brpu.setStructureGroupCodeDescription(puc.getStructureGroupCodeDescription());
      brpu.setReportedProductiveCapacityAmount(productiveCapacity);
      brpu.setNetProductiveCapacityAmount(productiveCapacity);
      brpu.setBpuCalculated(bpuCalculated);
      
      productiveUnits.add(brpu);
    }
    
    subtractQuantityUsedForCattleFeed(productiveUnits, reasonabilityTestResults.getForageConsumerCapacity());
    
    for(BenefitRiskProductiveUnit brpu : productiveUnits) {
      double bpuCalculated = brpu.getBpuCalculated();
      double productiveCapacity = brpu.getNetProductiveCapacityAmount();
      double margin = MathUtils.roundCurrency(productiveCapacity * bpuCalculated);
      industryAverageMargin += margin;
      brpu.setEstimatedMargin(margin);
      
      // Round it after the margin has been calculated for a more accurate margin calculation.
      // This makes a huge difference if the margin is millions of dollars.
      brpu.setBpuCalculated(MathUtils.roundCurrency(bpuCalculated));
    }
    
    Collections.sort(productiveUnits);
    
    industryAverageMargin = MathUtils.roundCurrency(industryAverageMargin);
    
    double benefitBenchmarkLessDeductible = MathUtils.roundCurrency(industryAverageMargin * (1 - benefitRiskDeductible));
    double benefitBenchmarkLessProgramYearMargin = MathUtils.roundCurrency(benefitBenchmarkLessDeductible - programYearMargin);
    double benefitBenchmarkBeforeCombinedFarmPercent = MathUtils.roundCurrency(benefitBenchmarkLessProgramYearMargin * benefitRiskPayoutLevel);
    double benefitBenchmark;

    if(scenario.isInCombinedFarm()) {
      double combinedFarmPercent = scenario.getFarmingYear().getBenefit().getCombinedFarmPercent();
      benefitBenchmark = MathUtils.roundCurrency(benefitBenchmarkBeforeCombinedFarmPercent * combinedFarmPercent);
      result.setCombinedFarmPercent(combinedFarmPercent);
      
      for(Scenario curScenario : scenario.getCombinedFarm().getScenarios()) {
        
        BenefitRiskCombinedFarmResult combinedFarmResult = new BenefitRiskCombinedFarmResult();
        result.getCombinedFarmResults().put(curScenario.getClient().getParticipantPin(), combinedFarmResult);
        
        double curCombinedFarmPercent = curScenario.getFarmingYear().getBenefit().getCombinedFarmPercent();
        double curBenefitBenchmark = MathUtils.roundCurrency(benefitBenchmarkBeforeCombinedFarmPercent * curCombinedFarmPercent);
        double curTotalBenefit = BenefitUtils.getAgristabilityTotalBenefit(curScenario);
        double curTotalProducerIncome = MathUtils.roundCurrency(programYearMargin + curTotalBenefit);
        
        combinedFarmResult.setTotalBenefit(curTotalBenefit);
        combinedFarmResult.setTotalProducerIncome(curTotalProducerIncome);
        combinedFarmResult.setBenefitBenchmark(curBenefitBenchmark);
        combinedFarmResult.setCombinedFarmPercent(curCombinedFarmPercent);
      }
    } else {
      benefitBenchmark = benefitBenchmarkBeforeCombinedFarmPercent;
    }
    
    boolean resultStatus;
    if(benefitBenchmark <= 0d) {
      resultStatus = true;
      result.addInfoMessage(INFO_PASS_BENCHMARK_ZERO_OR_NEGATIVE);
    } else if(totalBenefit < CalculatorConfig.MIN_BENEFIT) {
      resultStatus = true;
      result.addInfoMessage(INFO_PASS_UNDER_MINIMUM_BENEFIT);
    } else {
      // Other tests use the absolute value of the variance,
      // but for this one a negative variance is always a pass.
      double variance = MathUtils.round( (totalBenefit / benefitBenchmark) - 1, PERCENT_DECIMAL_PLACES );
      result.setVariance(variance);
      resultStatus = Math.abs(variance) < benefitRiskVarianceLimit;
    }
    
    result.setBenchmarkMargin(industryAverageMargin);
    result.setBenefitBenchmarkLessDeductible(benefitBenchmarkLessDeductible);
    result.setBenefitBenchmarkLessProgramYearMargin(benefitBenchmarkLessProgramYearMargin);
    result.setBenefitBenchmarkBeforeCombinedFarmPercent(benefitBenchmarkBeforeCombinedFarmPercent);
    result.setBenefitBenchmark(benefitBenchmark);
    result.setProgramYearMargin(programYearMargin);
    result.setTotalBenefit(totalBenefit);
    result.setVarianceLimit(benefitRiskVarianceLimit);
    result.setResult(resultStatus);
    
    LoggingUtils.logMethodStart(logger);
  }
  
  private double subtractQuantityUsedForCattleFeed(List<BenefitRiskProductiveUnit> productiveUnits, double amountFedToCattle) {
    
    Map<String, BenefitRiskProductiveUnit> pucMap = new HashMap<>();
    for (BenefitRiskProductiveUnit puc : productiveUnits) {
      pucMap.put(puc.getCode(), puc);
    }

    double amountFedRemaining = amountFedToCattle;
    for (String inventoryItemCode : ReasonabilityTestUtils.FED_OUT_INVENTORY_CODE_ORDER) {
      BenefitRiskProductiveUnit puc = pucMap.get(inventoryItemCode);
      if(puc == null) {
        continue;
      }
      if(amountFedRemaining == 0.0) {
        puc.setConsumedProductiveCapacityAmount(0.0);
        continue;
      }
      
      if(puc.getNetProductiveCapacityAmount() != null) {
        Double amountConsumed = null;
        if(puc.getNetProductiveCapacityAmount() < amountFedRemaining) {
          amountConsumed = puc.getNetProductiveCapacityAmount();
          amountFedRemaining -= amountConsumed;
          puc.setNetProductiveCapacityAmount(0.0);
        } else {
          amountConsumed = amountFedRemaining;
          double productiveCapacityAmount = puc.getNetProductiveCapacityAmount() - amountConsumed;
          puc.setNetProductiveCapacityAmount(productiveCapacityAmount);
          amountFedRemaining = 0.0;
        }
        puc.setConsumedProductiveCapacityAmount(amountConsumed);
      }
    }
    
    return amountFedRemaining;
  }

  public static boolean missingRequiredData(Scenario scenario) {
    boolean missingData = scenario.getFarmingYear() == null
        || scenario.getFarmingYear().getBenefit() == null
        || scenario.getFarmingYear().getBenefit().getTotalBenefit() == null
        || scenario.getFarmingYear().getBenefit().getProgramYearMargin() == null
        || (CalculatorConfig.hasEnhancedBenefits(scenario.getYear())
            && scenario.getFarmingYear().getBenefit().getStandardBenefit() == null);
    
    if(!missingData) {
      if(scenario.isInCombinedFarm()) {
        for(Scenario curScenario : scenario.getCombinedFarm().getScenarios()) {
          if(curScenario == null
              || curScenario.getFarmingYear() == null
              || curScenario.getFarmingYear().getBenefit() == null
              || curScenario.getFarmingYear().getBenefit().getCombinedFarmPercent() == null) {
            missingData = true;
            break;
          }
        }
      }
    }
    
    return missingData;
  }

}
