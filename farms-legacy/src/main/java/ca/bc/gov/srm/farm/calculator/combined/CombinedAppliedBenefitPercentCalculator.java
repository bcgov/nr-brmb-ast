/**
 * Copyright (c) 2012,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.calculator.combined;

import java.util.List;

import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.calculator.CalculatorFactory;
import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.domain.CombinedFarm;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.util.MathUtils;

/**
 * This calculator applies only to combined farms so it does not
 * extend a base class like the other combined/basic calculators.
 * 
 * @author awilkinson
 */
public class CombinedAppliedBenefitPercentCalculator {

  private Scenario scenario;

  public CombinedAppliedBenefitPercentCalculator(Scenario scenario) {
    this.scenario = scenario;
  }


  public void reset() {
    CombinedFarm combinedFarm = scenario.getCombinedFarm();
    List<Scenario> scenarios = combinedFarm.getScenarios();

    for(Scenario curScenario : scenarios) {

      Benefit benefit = curScenario.getFarmingYear().getBenefit();
      benefit.setAppliedBenefitPercent(null);
    }
  }
  
  
  public void calculate() {
    CombinedFarm combinedFarm = scenario.getCombinedFarm();
    List<Scenario> scenarios = combinedFarm.getScenarios();
    
    boolean hasNullBenefitPercent = false;
    for(Scenario curScenario : scenarios) {
      
      Benefit benefit = curScenario.getFarmingYear().getBenefit();
      Double appliedBenefitPercent = benefit.getCombinedFarmPercent();
      if(appliedBenefitPercent == null) {
        hasNullBenefitPercent = true;
        break;
      }
    }
    
    if(hasNullBenefitPercent) {
      calculateAppliedBenefitPercent();
    }
  }


  private void calculateAppliedBenefitPercent() {
    
    CombinedBenchmarkMarginCalculator benchmarkMarginCalculator = CalculatorFactory.getCombinedBenchmarkMarginCalculator();

    CombinedFarm combinedFarm = scenario.getCombinedFarm();
    List<Scenario> scenarios = combinedFarm.getScenarios();
    int scenarioCount = scenarios.size();
    
    double[] pinMargins = benchmarkMarginCalculator.calculateIndividualBenchmarkMargins(scenario, CalculatorConfig.CALC_TYPE_MARGIN, false, true);

    // add up the benchmark margins to get the combined benchmark margin
    double combinedBenchmarkMargin = benchmarkMarginCalculator.calculateCombinedBenchmarkMargin(scenario, pinMargins);

    // calculate the ratio for each pin
    double[] pinRatios = new double[scenarioCount];
    double totalRatio = 0;
    final int decimalPlaces = 3;
    for(int ii = 0; ii < scenarioCount; ii++) {
      if(combinedBenchmarkMargin > 0) {
        double ratio = pinMargins[ii] / combinedBenchmarkMargin;
        ratio = MathUtils.round(ratio, decimalPlaces);
        pinRatios[ii] = ratio;
        totalRatio += ratio;
      } else {
        pinRatios[ii] = 0;
      }
    }
    
    // make sure the ratios add up to 1 (a whole)
    totalRatio = MathUtils.round(totalRatio, decimalPlaces);
    final double requiredTotalRatio = 1;
    if(totalRatio != requiredTotalRatio) {
      double adjustment = requiredTotalRatio - totalRatio;
      pinRatios[0] += adjustment;
      pinRatios[0] = MathUtils.round(pinRatios[0], decimalPlaces);
    }
    
    int index = 0;
    for(Scenario curScenario : scenarios) {

      Benefit benefit = curScenario.getFarmingYear().getBenefit();
      Double appliedBenefitPercent = new Double(pinRatios[index]);
      benefit.setAppliedBenefitPercent(appliedBenefitPercent);
      index++;
    }
  }

}
