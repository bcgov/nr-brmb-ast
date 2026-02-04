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

import ca.bc.gov.srm.farm.calculator.IncomeExpenseCalculator;
import ca.bc.gov.srm.farm.domain.CombinedFarm;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;

/**
 * @author awilkinson
 */
public class CombinedIncomeExpenseCalculator extends IncomeExpenseCalculator {

  public CombinedIncomeExpenseCalculator(Scenario scenario) {
    super(scenario);
  }

  /**
   * Calculates the income and expenses for the program year and all reference years
   * and puts the results into the Margin and MarginTotal objects.
   */
  @Override
  public void calculateIncomeExpense() {
    
    CombinedFarm combinedFarm = scenario.getCombinedFarm();

    List<Integer> refYears = combinedFarm.getAllYears();
    for(Integer curYear : refYears) {
      boolean isProgramYear = curYear.equals(scenario.getYear());

      MarginTotal refYearMargin = combinedFarm.getYearMargins().get(curYear);
      List<ReferenceScenario> refScenarios = combinedFarm.getReferenceScenariosByYear(curYear);
      
      calculateIncomeExpense(refYearMargin, refScenarios, isProgramYear);
        
        // TODO Uncomment. FARM-262 - This change was slated for release 2.21.0 but was deferred.
//      List<ReferenceScenario> yearRefScenarios = scenario.getReferenceScenariosByYear(curYear);
//      for(ReferenceScenario rs : yearRefScenarios) {
//        CombinedBenefitCalculator combinedBenefitCalculator = CalculatorFactory.getCombinedBenefitCalculator(scenario);
//        combinedBenefitCalculator.copyMarginTotal(rs.getFarmingYear().getMarginTotal(), refYearMargin);
//      }
    }
  }

}
