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
package ca.bc.gov.srm.farm.calculator.basic;

import ca.bc.gov.srm.farm.calculator.BenchmarkMarginCalculator;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.util.MathUtils;

/**
 * @author awilkinson
 */
public class BasicBenchmarkMarginCalculator extends BenchmarkMarginCalculator {

  @Override
  public double calculateBenchmarkMargin(Scenario scenario, String calculationType, boolean onlyYearsUsedInCalc, boolean applyLag) {
    double benchmarkMargin = calculateIndividualBenchmarkMargin(scenario, calculationType, onlyYearsUsedInCalc, applyLag);
    benchmarkMargin = MathUtils.roundCurrency(benchmarkMargin);
    return benchmarkMargin;
  }

}
