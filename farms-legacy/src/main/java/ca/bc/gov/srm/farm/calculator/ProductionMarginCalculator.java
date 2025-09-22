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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.Margin;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.Scenario;


/**
 * Assumes that the total income and expenses for the farming operations
 * have already been calculated.
 */
public abstract class ProductionMarginCalculator {

  private Logger logger = LoggerFactory.getLogger(ProductionMarginCalculator.class);
  
  private ProductiveValueCalculator pvCalc;
  
  protected Scenario scenario;

  protected ProductionMarginCalculator(Scenario scenario) {
    this.scenario = scenario;
    this.pvCalc = CalculatorFactory.getProductiveValueCalculator(scenario);
  }
	

  /**
   * Calculates the unadjusted production margin for the program year and all 
   * reference years and puts the results into the Margin and MarginTotal 
   * objects.
   */
  public abstract void calculateUnadjusted();
  

  /**
   * Calculates the "production margin with accrual adjustments" for the 
   * program year and all reference years and puts the results into the 
   * Margin and MarginTotal objects.
   */
  public abstract void calculateWithAccruals();
  

  /**
   * Calculates the "production margin after structural change" for the 
   * reference years and puts the results into the MarginTotal objects.
   * Structural change is only applied to reference years to try
   * to make them look smiliar to the program year.
   */
  public abstract void calculateWithStructuralChange();
  
  
  /**
   * @param yearMargin yearMargin
   */
  protected void calculateWithStructuralChange(MarginTotal yearMargin) {
    double margin = yearMargin.getProductionMargAccrAdjs().doubleValue();

    // ratio & margin calculated every time for display and reasonability test calculations
    double ratioMargin = margin;
    double additiveMargin = margin;
    
    // calculation for selected structural change method if any
    if( yearMargin.getStructuralChangeAdjs() != null &&
        yearMargin.getIsStructuralChangeNotable() != null &&
        yearMargin.getIsStructuralChangeNotable().booleanValue()) {
      //
      // Any value we get from the MarginTotal object has already
      // had the partnership percentage applied to it, so we don't
      // need to do that here.
      //
      margin += yearMargin.getStructuralChangeAdjs().doubleValue();
    }

    // RATIO
    if(yearMargin.getIsRatioStructuralChangeNotable() != null
        && yearMargin.getIsRatioStructuralChangeNotable().booleanValue()) {
      ratioMargin += yearMargin.getRatioStructuralChangeAdjs().doubleValue();
    }
    
    // ADDITIVE
    if(yearMargin.getIsAdditiveStructuralChangeNotable() != null
        && yearMargin.getIsAdditiveStructuralChangeNotable().booleanValue()) {
      additiveMargin += yearMargin.getAdditiveStructuralChangeAdjs().doubleValue();
    }
    
    yearMargin.setProductionMargAftStrChangs(Double.valueOf(margin));
    yearMargin.setRatioProductionMargAftStrChangs(Double.valueOf(ratioMargin));
    yearMargin.setAdditiveProductionMargAftStrChangs(Double.valueOf(additiveMargin));
  }


  /**
   * Calculates the unadjusted production margin for a farming operation.
   * 
   * @param fo FarmingOperation
   * @param isProgramYear isProgramYear
   * @param isDeemedFarmingYear isDeemedFarmingYear
   * @param year year
   * @param bpuLeadInd bpuLeadInd
   */
	protected void calculateUnadjusted(
			FarmingOperation fo, 
			boolean isProgramYear,
			boolean isDeemedFarmingYear,
			Integer year,
			Boolean bpuLeadInd) {
		
		double income = fo.getMargin().getTotalAllowableIncome().doubleValue();
		double expense = fo.getMargin().getTotalAllowableExpenses().doubleValue();
		double margin = income - expense;
		
		if(!isProgramYear && !isDeemedFarmingYear) {
			//
			// We are supposed to sum up the productive_capacity_amount * BPU
			//
			logger.debug("unadjusted production margin being calculated based on BPUs");

			margin = pvCalc.calculateProductiveValue(fo, year, bpuLeadInd, CalculatorConfig.CALC_TYPE_MARGIN);
		}
		
		fo.getMargin().setUnadjustedProductionMargin(new Double(margin));
	}
	
	
	/**
	 * Just add the accrual total to the unadjusted production margin.
	 * 
	 * @param fo fo
	 */
	protected void calculateWithAccruals(FarmingOperation fo) {
		Margin m = fo.getMargin();
		double unadjMargin = m.getUnadjustedProductionMargin().doubleValue();
		double accruals = m.getTotalAccrualAdjs();
		double marginWithAccruals = unadjMargin + accruals;
		
		m.setProductionMargAccrAdjs(new Double(marginWithAccruals));
	}
	
}
