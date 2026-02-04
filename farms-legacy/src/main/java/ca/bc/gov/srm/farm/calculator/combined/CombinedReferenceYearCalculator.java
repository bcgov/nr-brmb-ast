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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.calculator.ReferenceYearCalculator;
import ca.bc.gov.srm.farm.domain.CombinedFarm;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.Scenario;

/**
 * @author awilkinson
 */
public class CombinedReferenceYearCalculator extends ReferenceYearCalculator {
  
  
  /**
   * @param scenario scenario
   * @return the three most recent years ReferenceScenarios
   */
  @Override
  protected Map<Integer, MarginTotal> getMostRecentYears(Scenario scenario) {
    final int numYearsNeeded = 3;
    Map<Integer, MarginTotal> yearMargins = new HashMap<>();
    int programYear = scenario.getYear();
    int minYear = programYear - numYearsNeeded;
    
    CombinedFarm combinedFarm = scenario.getCombinedFarm();
    Map<Integer, MarginTotal> combinedYearMargins = combinedFarm.getYearMargins();
    List<Integer> refYears = combinedFarm.getReferenceYears();
    
    for(Integer refYear : refYears) {
      MarginTotal refYearMargin = combinedYearMargins.get(refYear);
      if(refYear >= minYear) {
        yearMargins.put(refYear, refYearMargin);
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
      boolean useStructuralChangeEvenIfNotNotable, String structuralChangeMethod) {
    
    Map<Integer, MarginTotal> allYearMargins = scenario.getCombinedFarm().getYearMargins();
    List<Integer> refYears = scenario.getCombinedFarm().getReferenceYears();
    
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
    
    CombinedFarm combinedFarm = scenario.getCombinedFarm();
    Map<Integer, Boolean> deemedFarmingYearMap = combinedFarm.getDeemedFarmingYearMap();

    for(Integer refYear : combinedFarm.getReferenceYears()) {
      Boolean isDeemedFarmingYear = deemedFarmingYearMap.get(refYear);
      
      if(isDeemedFarmingYear) {
        numDeemedYears++;
      }
    }
    
    return (numDeemedYears == numYearsNeeded);
  }

}
