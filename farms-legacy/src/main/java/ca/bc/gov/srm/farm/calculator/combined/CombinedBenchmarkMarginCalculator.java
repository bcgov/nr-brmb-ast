/**
 * Copyright (c) 2018,
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

import ca.bc.gov.srm.farm.calculator.BenchmarkMarginCalculator;
import ca.bc.gov.srm.farm.domain.CombinedFarm;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.util.MathUtils;

/**
 * @author awilkinson
 */
public class CombinedBenchmarkMarginCalculator extends BenchmarkMarginCalculator {

  @Override
  public double calculateBenchmarkMargin(Scenario scenario, String calculationType, boolean onlyYearsUsedInCalc, boolean applyLag) {
    double[] pinMargins = calculateIndividualBenchmarkMargins(scenario, calculationType, onlyYearsUsedInCalc, applyLag);
    double benchmarkMargin = calculateCombinedBenchmarkMargin(scenario, pinMargins);
    benchmarkMargin = MathUtils.roundCurrency(benchmarkMargin);
    return benchmarkMargin;
  }


  public double calculateCombinedBenchmarkMargin(Scenario programYearScenario, double[] pinMargins) {
    double combinedBenchmarkMargin = 0;
    int scenarioCount = programYearScenario.getCombinedFarm().getScenarios().size();
    for(int ii = 0; ii < scenarioCount; ii++) {
      combinedBenchmarkMargin += pinMargins[ii];
    }
    return combinedBenchmarkMargin;
  }


  public double[] calculateIndividualBenchmarkMargins(Scenario programYearScenario, String calculationType, boolean onlyYearsUsedInCalc, boolean applyLag) {
    CombinedFarm combinedFarm = programYearScenario.getCombinedFarm();
    List<Scenario> scenarios = combinedFarm.getScenarios();
    int scenarioCount = scenarios.size();
    double[] scMargins = new double[scenarioCount];
    
    int index = 0;
    for(Scenario curScenario : scenarios) {
      double individualBenchmarkMargin = calculateIndividualBenchmarkMargin(curScenario, calculationType, onlyYearsUsedInCalc, applyLag);
      scMargins[index] = individualBenchmarkMargin;
      index++;
    }
    return scMargins;
  }

}
