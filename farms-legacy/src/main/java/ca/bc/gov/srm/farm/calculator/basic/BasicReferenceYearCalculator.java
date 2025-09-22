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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.calculator.ReferenceYearCalculator;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

/**
 * @author awilkinson
 */
public class BasicReferenceYearCalculator extends ReferenceYearCalculator {
  
  
  /**
   * @param scenario scenario
   * @return the three most recent years ReferenceScenarios
   */
  @Override
  protected Map<Integer, MarginTotal> getMostRecentYears(Scenario scenario) {
    final int numYearsNeeded = 3;
    Map<Integer, MarginTotal> yearMargins = new HashMap<>();
    int minYear = scenario.getYear().intValue() - numYearsNeeded;
    
    for(ReferenceScenario rs : scenario.getReferenceScenarios()) {
      Integer refYear = rs.getYear();
      
      if(refYear.intValue() >= minYear) {
        yearMargins.put(refYear, rs.getFarmingYear().getMarginTotal());
      }
    }
    
    return yearMargins;
  }
    
  
  /**
   * Throw out the highest and lowset
   * 
   * @return Map<year, MarginTotal>
   */
  @Override
  protected Map<Integer, MarginTotal> getOlympicAverageYears(
      Scenario scenario,
      boolean useStructuralChanges,
      boolean useStructuralChangeEvenIfNotNotable,
      String structuralChangeMethod) {
    
    Map<Integer, MarginTotal> allYearMargins = scenario.getYearMargins();
    List<Integer> refYears = ScenarioUtils.getReferenceYears(scenario.getYear());
    
    return getOlympicAverageYears(scenario, useStructuralChanges, useStructuralChangeEvenIfNotNotable, allYearMargins, refYears, structuralChangeMethod);
  }


  /**
   * @param scenario scenario
   * @return true is it has five deemed farming years
   */
  @Override
  protected boolean hasFiveYearsOfData(Scenario scenario) {
    final int numYearsNeeded = 5;
    int numDeemedYears = 0;
    
    for(ReferenceScenario rs : scenario.getReferenceScenarios()) {
      boolean isDeemedFarmingYear = rs.getIsDeemedFarmingYear().booleanValue();
      
      if(isDeemedFarmingYear) {
        numDeemedYears++;
      }
    }
    
    return (numDeemedYears == numYearsNeeded);
  }

}
