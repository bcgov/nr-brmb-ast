/**
 *
 * Copyright (c) 2011,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.incomeexpenses;

import ca.bc.gov.srm.farm.ui.struts.calculator.AdjustmentGridItemData;

/**
 * @author awilkinson
 * @created Jan 4, 2011
 */
public class IncomeExpenseFormData extends AdjustmentGridItemData {

  private static final long serialVersionUID = 1L;

  public static final String TYPE_INCOME = "a_inc";
  public static final String TYPE_EXPENSE = "b_exp";

  public static final String ELIGIBILITY_ELIGIBLE = "_elig";
  public static final String ELIGIBILITY_INELIGIBLE = "_inelig";
  
  private Boolean expense;
  
  private Boolean eligible;
  
  private double adjustedFiveYearTotal = 0d;
  private double adjustmentFiveYearTotal = 0d;
  private double craFiveYearTotal = 0d;
  
  private final double fiveYearsDouble = 5d;
  
  /**
   * @param value Double
   */
  public void addToAdjustedFiveYearTotal(Double value) {
    if(value != null) {
      adjustedFiveYearTotal += value.doubleValue();
    }
  }
  
  /**
   * @param value Double
   */
  public void addToAdjustmentFiveYearTotal(Double value) {
    if(value != null) {
      adjustmentFiveYearTotal += value.doubleValue();
    }
  }
  
  /**
   * @param value Double
   */
  public void addToCraFiveYearTotal(Double value) {
    if(value != null) {
      craFiveYearTotal += value.doubleValue();
    }
  }
  
  /**
   * @return Double
   */
  public Double getAdjustedFiveYearAverage() {
    return calcFiveYearAverage(adjustedFiveYearTotal);
  }
  
  /**
   * @return Double
   */
  public Double getAdjustmentFiveYearAverage() {
    return calcFiveYearAverage(adjustmentFiveYearTotal);
  }
  
  /**
   * @return Double
   */
  public Double getCraFiveYearAverage() {
    return calcFiveYearAverage(craFiveYearTotal);
  }
  
  /**
   * @param fiveYearTotal double
   * @return Double
   */
  private Double calcFiveYearAverage(double fiveYearTotal) {
    final double scale = 2d;
    final double scaleMultiplier = Math.pow(10d, scale);

    double fiveYearAverage = fiveYearTotal / fiveYearsDouble;
    double roundedAverage = Math.round(fiveYearAverage * scaleMultiplier) / scaleMultiplier;
    return new Double(roundedAverage);
  }

  /**
   * @return the eligible
   */
  public Boolean getEligible() {
    return eligible;
  }

  /**
   * @param eligible the eligible to set the value to
   */
  public void setEligible(Boolean eligible) {
    this.eligible = eligible;
  }

  /**
   * @return the expense
   */
  public Boolean getExpense() {
    return expense;
  }

  /**
   * @param expense the expense to set the value to
   */
  public void setExpense(Boolean expense) {
    this.expense = expense;
  }

  /**
   * @param lineCode String
   * @param isExpense boolean
   * @param isEligible boolean
   * @return String
   */
  public static String getLineKey(String lineCode, boolean isExpense, boolean isEligible) {
    return getLineKey(lineCode, isExpense, isEligible, false);
  }

  /**
   * @param lineCode String
   * @param isExpense boolean
   * @param isEligible boolean
   * @param isNew boolean
   * @return String
   */
  public static String getLineKey(String lineCode, boolean isExpense, boolean isEligible, boolean isNew) {
    // prevent javascript error caused by code -1 - Unknown
    String fixedLineCode = lineCode.replace('-', 'm');

    String eligibility;
    if(isEligible) {
      eligibility = ELIGIBILITY_ELIGIBLE;
    } else {
      eligibility = ELIGIBILITY_INELIGIBLE;
    }

    String ieType;
    if(isNew) {
      ieType = TYPE_NEW;
    } else if(isExpense) {
      ieType = TYPE_EXPENSE;
    } else {
      ieType = TYPE_INCOME;
    }

    String lineKey = ieType + eligibility + fixedLineCode;
    return lineKey;
  }

}
