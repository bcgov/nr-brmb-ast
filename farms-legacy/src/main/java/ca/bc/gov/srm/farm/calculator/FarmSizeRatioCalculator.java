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

import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.Scenario;


/**
 *
 */
public abstract class FarmSizeRatioCalculator {
  
  protected Scenario scenario;

  protected FarmSizeRatioCalculator(Scenario scenario) {
    this.scenario = scenario;
  }
  
	/**
	 * number(8,3) means the biggest number we can handle 
	 * is 99999.999 or 9,999,999%
	 */
  protected static final double MAX_FSR_RATIO = 99999.999;
  
  /**
   * Calculates the farm size ratio for the reference years.
   * 
   * Basically pretend that the farm grew what it was capable
   * of producing in the program year in the reference year.
   * If the farm grew 100 acres of wheat in 2009, what income
   * would that have produced in 2006. The ratio is then the
   * 2006 pretend productive value / actual 2006 productive value.
   */
  public void calculateRatio() {
    calculateRatio(CalculatorConfig.CALC_TYPE_MARGIN);
  }
  
  /**
   * Calculates the expense farm size ratio for the reference years.
   * 
   * Basically pretend that the farm had the same list of expenses
   * in the reference year as in the program year.
   * If the farm bought 100 pounds of seed in 2009, what expenses
   * would that have generated in 2006. The ratio is then the
   * 2006 pretend expense productive value / actual 2006 expense
   * productive value.
   */
  public void calculateExpenseRatio() {
    calculateRatio(CalculatorConfig.CALC_TYPE_EXPENSE);
  }

  /**
   * @param calculationType Either MARGIN or EXPENSE.
   */
  protected abstract void calculateRatio(String calculationType);


	/**
   * @param yearMargin yearMargin
	 * @param refYear refYear
	 * @param pyPucMap pyPucMap
	 * @param refYearsPucMap refYearsPucMap
	 * @param scCalc scCalc
	 * @param calculationType Either MARGIN or EXPENSE.
   */
  protected void calculateRatio(
      MarginTotal yearMargin,
      Integer refYear,
      Map<String, List<ProductiveUnitCapacity>> pyPucMap,
      Map<Integer, Map<String, List<ProductiveUnitCapacity>>> refYearsPucMap,
      StructuralChangeCalculator scCalc,
      String calculationType) {
    
    ProductiveValueCalculator pvCalc = CalculatorFactory.getProductiveValueCalculator(scenario);
    
    Boolean bpuLead = yearMargin.getBpuLeadInd();
    
    double refYearProdValue = 0;
    double pyProdValue = 0;
    double ratio = 0;
    
    Map<String, List<ProductiveUnitCapacity>> refYearPucMap = refYearsPucMap.get(refYear);
    
    //
    // loop through the reference year commodities
    //
    for(String key : refYearPucMap.keySet()) {
      List<ProductiveUnitCapacity> refYearPucList = refYearPucMap.get(key);
      Double refYearBasePrice = pvCalc.getBasePriceForYear(refYearPucList, refYear, bpuLead, calculationType);
      double refYearCapacity = scCalc.sumCapacity(refYearPucList);
      
      if(refYearBasePrice != null) {
        double price = refYearBasePrice.doubleValue();
        
        refYearProdValue += (refYearCapacity * price);
        
        List<ProductiveUnitCapacity> pyPucList = pyPucMap.get(key);
        if(pyPucList != null && pyPucList.size() > 0) {
          double pyCapacity = scCalc.sumCapacity(pyPucList);
          pyProdValue += (pyCapacity * price);
        } 
      }
    }
    
    //
    // Also consider any items grown in the program year that were not
    // grown in the reference year.
    //
    for(String pyPucKey : pyPucMap.keySet()) {
      
      if(!refYearPucMap.keySet().contains(pyPucKey)) {
        List<ProductiveUnitCapacity> pyPucList = pyPucMap.get(pyPucKey);
        Double basePrice = pvCalc.getBasePriceForYear(pyPucList, refYear, bpuLead, calculationType);
        
        if(basePrice != null) {
          double pyCapacity = scCalc.sumCapacity(pyPucList);
          pyProdValue += (pyCapacity * basePrice.doubleValue());
        }
      }
    }

    if(refYearProdValue != 0) {
      ratio = pyProdValue/refYearProdValue;
    }
    
    //
    // Guard against the ratio being too big to fit into the database field.
    // Note that we also have to look out for large negative numbers.
    //
    if((ratio > 0) && (ratio > MAX_FSR_RATIO)) {
      ratio = MAX_FSR_RATIO;
    } else if ((ratio < 0) && (ratio < (0-MAX_FSR_RATIO))) {
      ratio = 0 - MAX_FSR_RATIO;
    }
    
    if(CalculatorConfig.CALC_TYPE_MARGIN.equals(calculationType)) {
      yearMargin.setFarmSizeRatio(new Double(ratio));
      yearMargin.setProductiveValue(new Double(refYearProdValue));
    } else { // CalculatorConstants.CALC_TYPE_EXPENSE
      yearMargin.setExpenseFarmSizeRatio(new Double(ratio));
      yearMargin.setExpenseProductiveValue(new Double(refYearProdValue));
    }
  }
}
