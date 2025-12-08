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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.calculator.AccrualCalculator;
import ca.bc.gov.srm.farm.domain.CombinedFarm;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;

/**
 * @author awilkinson
 */
public class CombinedAccrualCalculator extends AccrualCalculator {

  private Logger logger = LoggerFactory.getLogger(getClass());
  
  public CombinedAccrualCalculator(Scenario scenario) {
    super(scenario);
  }
  
  @Override
  public void calculateTotals() {
    logger.debug("Calculating accrual totals...");
    CombinedFarm combinedFarm = scenario.getCombinedFarm();
    List<Integer> allYears = combinedFarm.getAllYears();
    
    // calculate for all years
    for(Integer curYear : allYears) {
      
      MarginTotal yearMargin = combinedFarm.getYearMargins().get(curYear);
      List<ReferenceScenario> refScenarios = combinedFarm.getReferenceScenariosByYear(curYear);
      
      calculateTotals(yearMargin, refScenarios);
    }
  }

}
