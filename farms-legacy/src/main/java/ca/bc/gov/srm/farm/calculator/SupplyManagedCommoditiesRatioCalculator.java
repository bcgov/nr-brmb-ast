/**
 *
 * Copyright (c) 2009,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.calculator;

import java.util.Map;

import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.Scenario;


/**
 * Supply Managed Commodities are things like milk, chickens, eggs.
 * Things that have a government imposed limit on how much a farm
 * is allowed to produce.
 */
public class SupplyManagedCommoditiesRatioCalculator {
  
  private Scenario scenario;
  
  public SupplyManagedCommoditiesRatioCalculator(Scenario scenario) {
    this.scenario = scenario;
  }

  public double calculateRatio() {
    ReferenceYearCalculator refYearCalc = CalculatorFactory.getReferenceYearCalculator(scenario);
    Map<Integer, MarginTotal> refYearMargins = refYearCalc.getReferenceYearMargins(scenario, false, false);

    double totalSmcIncome = 0;
    double totalIncome = 0;
    
    for(Integer refYear : refYearMargins.keySet()) {
      MarginTotal refYearMargin = refYearMargins.get(refYear);
      Double smci = refYearMargin.getSupplyManagedCommodityIncome();
      Double inc = refYearMargin.getTotalAllowableIncome();
      
      if(smci != null) {
        totalSmcIncome += smci.doubleValue();
      }
      
      if(inc != null) {
        totalIncome += inc.doubleValue();
      }
    }
    
    double ratio = 0;
    
    if(totalIncome != 0) {
      ratio = totalSmcIncome/totalIncome;
    }
    
    return ratio;
  }
}
