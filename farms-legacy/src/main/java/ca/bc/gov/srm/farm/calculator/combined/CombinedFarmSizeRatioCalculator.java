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
import java.util.Map;

import ca.bc.gov.srm.farm.calculator.CalculatorFactory;
import ca.bc.gov.srm.farm.calculator.FarmSizeRatioCalculator;
import ca.bc.gov.srm.farm.calculator.StructuralChangeCalculator;
import ca.bc.gov.srm.farm.domain.CombinedFarm;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.Scenario;

/**
 * @author awilkinson
 */
public class CombinedFarmSizeRatioCalculator extends FarmSizeRatioCalculator {
  
  public CombinedFarmSizeRatioCalculator(Scenario scenario) {
    super(scenario);
  }
  
  /**
   * @param calculationType Either MARGIN or EXPENSE.
   */
  @Override
  protected void calculateRatio(String calculationType) {
    StructuralChangeCalculator scCalc = CalculatorFactory.getStructuralChangeCalculator(scenario);
    Map<String, List<ProductiveUnitCapacity>> pyPucMap = scCalc.getProgramYearPucMap();
    Map<Integer, Map<String, List<ProductiveUnitCapacity>>> refYearsPucMap = scCalc.getRefYearPucMap();
    
    CombinedFarm combinedFarm = scenario.getCombinedFarm();
    Map<Integer, MarginTotal> yearMargins = combinedFarm.getYearMargins();
    List<Integer> refYears = combinedFarm.getReferenceYears();

    // calculate for reference years
    for(Integer refYear : refYears) {

      MarginTotal yearMargin = yearMargins.get(refYear);
      calculateRatio(yearMargin, refYear, pyPucMap, refYearsPucMap, scCalc, calculationType);
    }
  }

}
