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
package ca.bc.gov.srm.farm.calculator.basic;

import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.srm.farm.calculator.IncomeExpenseCalculator;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;

/**
 * @author awilkinson
 */
public class BasicIncomeExpenseCalculator extends IncomeExpenseCalculator {

  public BasicIncomeExpenseCalculator(Scenario scenario) {
    super(scenario);
  }

  /**
   * Calculates the income and expenses for the program year and all reference years
   * and puts the results into the Margin and MarginTotal objects.
   */
  @Override
  public void calculateIncomeExpense() {

    MarginTotal pyMargin = scenario.getFarmingYear().getMarginTotal();
    List<ReferenceScenario> refScenarios = new ArrayList<>();
    refScenarios.add(scenario);
    
    boolean isProgramYear = true;
    calculateIncomeExpense(pyMargin, refScenarios, isProgramYear);
    
    isProgramYear = false;
    for(ReferenceScenario rs : scenario.getReferenceScenarios()) {

      MarginTotal yearMargin = rs.getFarmingYear().getMarginTotal();
      refScenarios = new ArrayList<>();
      refScenarios.add(rs);
      
      calculateIncomeExpense(yearMargin, refScenarios, isProgramYear);
    }
  }

}
