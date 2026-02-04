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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.LineItem;
import ca.bc.gov.srm.farm.domain.Margin;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.util.MathUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

/**
 * 
 */
public abstract class IncomeExpenseCalculator {
  
  protected Scenario scenario;

  protected IncomeExpenseCalculator(Scenario scenario) {
    this.scenario = scenario;
  }

  public static final String MARGIN_TOTAL_5_YEAR_AVERAGE_KEY = "5yearAverage";

  /**
   * Calculates the income and expenses for the program year and all reference years
   * and puts the results into the Margin and MarginTotal objects.
   */
  public abstract void calculateIncomeExpense();

  
  /**
   * Calculates the income and expenses for all farming operations and put results
   * into the Margin objects and the MarginTotal passed in.
   * 
   * @param yearMargin yearMargin
   * @param refScenarios refScenarios
   * @param isProgramYear isProgramYear
   */
  protected void calculateIncomeExpense(
      MarginTotal yearMargin,
      List<ReferenceScenario> refScenarios,
      boolean isProgramYear) {

    final int numTotals = 13;
    double[] totals = new double[numTotals];
    int ii = 0;

    for(ReferenceScenario rs : refScenarios) {
      
      for(FarmingOperation fo : rs.getFarmingYear().getFarmingOperations()) {
        Margin m = fo.getMargin();
        calculateIncomeExpense(fo, isProgramYear);
        
        double partnershipPercent = ScenarioUtils.getPartnershipPercent(fo);
        ii = 0;
        totals[ii++] += m.getTotalAllowableIncome().doubleValue() * partnershipPercent;
        totals[ii++] += m.getTotalAllowableExpenses().doubleValue() * partnershipPercent;
        totals[ii++] += m.getUnadjustedAllowableIncome().doubleValue() * partnershipPercent;
        totals[ii++] += m.getYardageIncome().doubleValue() * partnershipPercent;
        totals[ii++] += m.getProgramPaymentIncome().doubleValue() * partnershipPercent;
        totals[ii++] += m.getSupplyManagedCommodityIncome().doubleValue() * partnershipPercent;
        totals[ii++] += m.getTotalUnallowableIncome().doubleValue() * partnershipPercent;
        totals[ii++] += m.getUnadjustedAllowableExpenses().doubleValue() * partnershipPercent;
        totals[ii++] += m.getYardageExpenses().doubleValue() * partnershipPercent;
        totals[ii++] += m.getContractWorkExpenses().doubleValue() * partnershipPercent;
        totals[ii++] += m.getManualExpenses().doubleValue() * partnershipPercent;
        totals[ii++] += m.getTotalUnallowableExpenses().doubleValue() * partnershipPercent;
        totals[ii++] += m.getExpenseAccrualAdjs().doubleValue() * partnershipPercent;
      }
    }
    
    ii = 0;
    yearMargin.setTotalAllowableIncome(new Double(totals[ii++]));
    yearMargin.setTotalAllowableExpenses(new Double(totals[ii++]));
    yearMargin.setUnadjustedAllowableIncome(new Double(totals[ii++]));
    yearMargin.setYardageIncome(new Double(totals[ii++]));
    yearMargin.setProgramPaymentIncome(new Double(totals[ii++]));
    yearMargin.setSupplyManagedCommodityIncome(new Double(totals[ii++]));
    yearMargin.setTotalUnallowableIncome(new Double(totals[ii++]));
    yearMargin.setUnadjustedAllowableExpenses(new Double(totals[ii++]));
    yearMargin.setYardageExpenses(new Double(totals[ii++]));
    yearMargin.setContractWorkExpenses(new Double(totals[ii++]));
    yearMargin.setManualExpenses(new Double(totals[ii++]));
    yearMargin.setTotalUnallowableExpenses(new Double(totals[ii++]));
    yearMargin.setExpenseAccrualAdjs(new Double(totals[ii++]));
  }


  /**
   * Calculates the allowable income and expense for a farming operation.
   * 
   * @param fo FarmingOperation
   * @param isProgramYear isProgramYear
   */
  protected void calculateIncomeExpense(FarmingOperation fo, boolean isProgramYear) {
		double totalExpense = 0;
		double yardageExpense = 0;
		double workExpense = 0;
		double manualExpense = 0;
		double totalIncome = 0;
		double yardageIncome = 0;
		double paymentsIncome = 0;
		double supplyManagedIncome = 0;
		double unallowableIncome = 0;
		double unallowableExpense = 0;
		
    if(fo.getIncomeExpenses() != null) {
  		
  		for(IncomeExpense ie : fo.getIncomeExpenses()) {
  			LineItem li = ie.getLineItem();
  			
  			double currentAmount = ie.getTotalAmount().doubleValue();
        boolean isExpense = ie.getIsExpense().booleanValue();
        boolean isEligibleThisYear = li.getIsEligible().booleanValue();
        boolean isEligibleRefYears = li.getIsEligibleRefYears().booleanValue();
        boolean isAllowed = isEligibleThisYear || (!isProgramYear && isEligibleRefYears);
        boolean isYardage = li.getIsYardage().booleanValue(); 
        boolean isContractWork = li.getIsContractWork().booleanValue();
        boolean isManualExpense = li.getIsManualExpense().booleanValue();
        boolean isPayment = li.getIsProgramPayment().booleanValue();
        boolean isSupply = li.getIsSupplyManagedCommodity().booleanValue();
        
        if(isContractWork && !isAllowed && !isExpense){
        	//
        	// Contract work is confusing. It is an unallowed income, and part of
        	// it gets subtracted from the grower's total allowed expenses.
        	//
        	workExpense += currentAmount * CalculatorConfig.CONTRACT_WORK_FACTOR;
        } else if(isYardage && isExpense && isAllowed){
        	yardageExpense += currentAmount * CalculatorConfig.YARDAGE_FACTOR;
        } else if(isYardage && !isExpense && isAllowed){
        	yardageIncome += currentAmount * CalculatorConfig.YARDAGE_FACTOR;
        } else if(isManualExpense && isExpense && isAllowed){
          manualExpense += currentAmount;
        } else if(isPayment && !isExpense && isAllowed && !isProgramYear && !isEligibleRefYears){
          // The logic for isEligibleRefYears is a bit funny. Initially we thought that a value of Y (true)
          // would just override the isEligibleThisYear flag. But the client really wants it to override the
          // isPayment flag as well. Note that the value for isEligibleRefYears comes from the program year line item.
        	paymentsIncome += currentAmount;
        } else if(isSupply && !isExpense){
        	supplyManagedIncome += currentAmount;
        }
        
        if(isExpense){
        	if(isAllowed) {
        	  totalExpense += currentAmount;
        	} else {
        		unallowableExpense += currentAmount;
        	}
        } else {
        	if(isAllowed) {
        	  totalIncome += currentAmount;
        	} else {
        		unallowableIncome += currentAmount;
        	}
        }
  		}
    }

		// income
		double allowableIncome = totalIncome - yardageIncome - paymentsIncome;
		fo.getMargin().setUnadjustedAllowableIncome(new Double(totalIncome));
		fo.getMargin().setYardageIncome(new Double(yardageIncome));
		fo.getMargin().setProgramPaymentIncome(new Double(paymentsIncome));
		fo.getMargin().setSupplyManagedCommodityIncome(new Double(supplyManagedIncome));
		fo.getMargin().setTotalAllowableIncome(new Double(allowableIncome));
		fo.getMargin().setTotalUnallowableIncome(new Double(unallowableIncome));
		
		// expenses
		double allowableExpense = totalExpense - yardageExpense - workExpense - manualExpense;
		fo.getMargin().setUnadjustedAllowableExpenses(new Double(totalExpense));
		fo.getMargin().setYardageExpenses(new Double(yardageExpense));
		fo.getMargin().setContractWorkExpenses(new Double(workExpense));
		fo.getMargin().setManualExpenses(new Double(manualExpense));
		fo.getMargin().setTotalAllowableExpenses(new Double(allowableExpense));
		fo.getMargin().setTotalUnallowableExpenses(new Double(unallowableExpense));
		
		double accrualAdjsPurchasedInputs = fo.getMargin().getAccrualAdjsPurchasedInputs().doubleValue();
		double accrualAdjsPayables = fo.getMargin().getAccrualAdjsPayables().doubleValue();
		double expensesWithAccrualAdjustments = allowableExpense - accrualAdjsPurchasedInputs - accrualAdjsPayables;
		fo.getMargin().setExpenseAccrualAdjs(new Double(expensesWithAccrualAdjustments));
	}
  
  /**
   * Calculates the "expenses after structural change" for the 
   * reference years and puts the results into the MarginTotal objects.
   * Structural change is only applied to reference years to try
   * to make them look smiliar to the program year.
   * 
   * Calculated for 2013 forward only.
   */
  public void calculateExpensesWithStructuralChange() {
    Map<Integer, MarginTotal> refYearMargins = scenario.getReferenceYearMargins();

    // reference years only
    for(Integer curYear : refYearMargins.keySet()) {
      
      MarginTotal yearMargin = refYearMargins.get(curYear);
      calculateWithStructuralChange(yearMargin);
    }
  }
  
  
  /**
   * @param yearMargin yearMargin
   */
  protected void calculateWithStructuralChange(MarginTotal yearMargin) {
    double expenses = yearMargin.getExpenseAccrualAdjs().doubleValue();
    
    if( yearMargin.getExpenseStructuralChangeAdjs() != null &&
        yearMargin.getIsStructuralChangeNotable() != null &&
        yearMargin.getIsStructuralChangeNotable().booleanValue()) {
      //
      // Any value we get from the MarginTotal object has already
      // had the partnership percentage applied to it, so we don't
      // need to do that here.
      //
      expenses += yearMargin.getExpenseStructuralChangeAdjs().doubleValue();
    }
    
    yearMargin.setExpensesAfterStructuralChange(new Double(expenses));
  }
  
  
  public Map<String, MarginTotal> getSingleOperationMarginTotalMap(String schedule) {
    
    // Map<String year, MarginTotal>
    Map<String, MarginTotal> marginTotalMap = new HashMap<>();
    
    final int numTotals = 11;
    double[] totals = new double[numTotals];
    int ii = 0;

    for(ReferenceScenario refScenario : scenario.getAllScenarios()) {
      boolean curProgramYear = refScenario == refScenario.getParentScenario();
      FarmingOperation fo = refScenario.getFarmingYear().getFarmingOperationBySchedule(schedule);

      if(fo != null && fo.getMargin() != null) {
        Margin m = fo.getMargin();
        
        MarginTotal yearTotal = new MarginTotal();
        yearTotal.setSupplyManagedCommodityIncome(m.getSupplyManagedCommodityIncome());
        yearTotal.setUnadjustedAllowableIncome(m.getUnadjustedAllowableIncome());
        yearTotal.setUnadjustedAllowableExpenses(m.getUnadjustedAllowableExpenses());
        yearTotal.setYardageIncome(m.getYardageIncome());
        yearTotal.setYardageExpenses(m.getYardageExpenses());
        yearTotal.setProgramPaymentIncome(m.getProgramPaymentIncome());
        yearTotal.setContractWorkExpenses(m.getContractWorkExpenses());
        yearTotal.setManualExpenses(m.getManualExpenses());
        yearTotal.setTotalAllowableIncome(m.getTotalAllowableIncome());
        yearTotal.setTotalAllowableExpenses(m.getTotalAllowableExpenses());
        yearTotal.setTotalUnallowableIncome(m.getTotalUnallowableIncome());
        yearTotal.setTotalUnallowableExpenses(m.getTotalUnallowableExpenses());

        marginTotalMap.put(fo.getFarmingYear().getReferenceScenario().getYear().toString(), yearTotal);
        
        // do not include current program year in the 5 year average
        if(!curProgramYear) {
          ii = 0;
          totals[ii++] += MathUtils.getPrimitiveValue(m.getSupplyManagedCommodityIncome());
          totals[ii++] += MathUtils.getPrimitiveValue(m.getUnadjustedAllowableIncome());
          totals[ii++] += MathUtils.getPrimitiveValue(m.getUnadjustedAllowableExpenses());
          totals[ii++] += MathUtils.getPrimitiveValue(m.getYardageIncome());
          totals[ii++] += MathUtils.getPrimitiveValue(m.getYardageExpenses());
          totals[ii++] += MathUtils.getPrimitiveValue(m.getProgramPaymentIncome());
          totals[ii++] += MathUtils.getPrimitiveValue(m.getContractWorkExpenses());
          totals[ii++] += MathUtils.getPrimitiveValue(m.getTotalAllowableIncome());
          totals[ii++] += MathUtils.getPrimitiveValue(m.getTotalAllowableExpenses());
          totals[ii++] += MathUtils.getPrimitiveValue(m.getTotalUnallowableIncome());
          totals[ii++] += MathUtils.getPrimitiveValue(m.getTotalUnallowableExpenses());
        }
      }
    }

    
    final int fiveYears = 5;
    MarginTotal fiveYearAverage = new MarginTotal();

    ii = 0;
    fiveYearAverage.setSupplyManagedCommodityIncome(new Double(totals[ii++] / fiveYears));
    fiveYearAverage.setUnadjustedAllowableIncome(new Double(totals[ii++] / fiveYears));
    fiveYearAverage.setUnadjustedAllowableExpenses(new Double(totals[ii++] / fiveYears));
    fiveYearAverage.setYardageIncome(new Double(totals[ii++] / fiveYears));
    fiveYearAverage.setYardageExpenses(new Double(totals[ii++] / fiveYears));
    fiveYearAverage.setProgramPaymentIncome(new Double(totals[ii++] / fiveYears));
    fiveYearAverage.setContractWorkExpenses(new Double(totals[ii++] / fiveYears));
    fiveYearAverage.setTotalAllowableIncome(new Double(totals[ii++] / fiveYears));
    fiveYearAverage.setTotalAllowableExpenses(new Double(totals[ii++] / fiveYears));
    fiveYearAverage.setTotalUnallowableIncome(new Double(totals[ii++] / fiveYears));
    fiveYearAverage.setTotalUnallowableExpenses(new Double(totals[ii++] / fiveYears));
    
    marginTotalMap.put(MARGIN_TOTAL_5_YEAR_AVERAGE_KEY, fiveYearAverage);

    return marginTotalMap;
  }



  public Map<String, MarginTotal> getWholeFarmMarginTotalMap() {
    
    // Map<String year, MarginTotal>
    Map<String, MarginTotal> marginTotalMap = new HashMap<>();
    
    MarginTotal programYearMarginTotal = scenario.getFarmingYear().getMarginTotal();
    marginTotalMap.put(scenario.getYear().toString(), programYearMarginTotal);
    
    final int numTotals = 12;
    double[] totals = new double[numTotals];
    int ii = 0;
    
    for(ReferenceScenario rs: scenario.getReferenceScenarios()) {
      
      MarginTotal m = rs.getFarmingYear().getMarginTotal();
      marginTotalMap.put(rs.getYear().toString(), m);
      
      ii = 0;
      totals[ii++] += MathUtils.getPrimitiveValue(m.getSupplyManagedCommodityIncome());
      totals[ii++] += MathUtils.getPrimitiveValue(m.getUnadjustedAllowableIncome());
      totals[ii++] += MathUtils.getPrimitiveValue(m.getUnadjustedAllowableExpenses());
      totals[ii++] += MathUtils.getPrimitiveValue(m.getYardageIncome());
      totals[ii++] += MathUtils.getPrimitiveValue(m.getYardageExpenses());
      totals[ii++] += MathUtils.getPrimitiveValue(m.getProgramPaymentIncome());
      totals[ii++] += MathUtils.getPrimitiveValue(m.getContractWorkExpenses());
      totals[ii++] += MathUtils.getPrimitiveValue(m.getManualExpenses());
      totals[ii++] += MathUtils.getPrimitiveValue(m.getTotalAllowableIncome());
      totals[ii++] += MathUtils.getPrimitiveValue(m.getTotalAllowableExpenses());
      totals[ii++] += MathUtils.getPrimitiveValue(m.getTotalUnallowableIncome());
      totals[ii++] += MathUtils.getPrimitiveValue(m.getTotalUnallowableExpenses());
    }
    
    final int fiveYears = 5;
    MarginTotal fiveYearAverage = new MarginTotal();

    ii = 0;
    fiveYearAverage.setSupplyManagedCommodityIncome(new Double(totals[ii++] / fiveYears));
    fiveYearAverage.setUnadjustedAllowableIncome(new Double(totals[ii++] / fiveYears));
    fiveYearAverage.setUnadjustedAllowableExpenses(new Double(totals[ii++] / fiveYears));
    fiveYearAverage.setYardageIncome(new Double(totals[ii++] / fiveYears));
    fiveYearAverage.setYardageExpenses(new Double(totals[ii++] / fiveYears));
    fiveYearAverage.setProgramPaymentIncome(new Double(totals[ii++] / fiveYears));
    fiveYearAverage.setContractWorkExpenses(new Double(totals[ii++] / fiveYears));
    fiveYearAverage.setManualExpenses(new Double(totals[ii++] / fiveYears));
    fiveYearAverage.setTotalAllowableIncome(new Double(totals[ii++] / fiveYears));
    fiveYearAverage.setTotalAllowableExpenses(new Double(totals[ii++] / fiveYears));
    fiveYearAverage.setTotalUnallowableIncome(new Double(totals[ii++] / fiveYears));
    fiveYearAverage.setTotalUnallowableExpenses(new Double(totals[ii++] / fiveYears));
    
    marginTotalMap.put(MARGIN_TOTAL_5_YEAR_AVERAGE_KEY, fiveYearAverage);

    return marginTotalMap;
  }


}
