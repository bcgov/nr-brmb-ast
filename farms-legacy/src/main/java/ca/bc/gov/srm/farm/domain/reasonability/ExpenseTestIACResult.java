/**
 * Copyright (c) 2018,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain.reasonability;

/**
 * Results for Expense Test - Industry Average Comparison
 * 
 * @author awilkinson
 */
public class ExpenseTestIACResult extends ReasonabilityTestResult {
  
  private static final long serialVersionUID = 1L;
  
  private Double expenseAccrualAdjs;
  private Double industryVarianceLimit;
  private Double industryAverage;
  private Double industryVariance;

  public Double getExpenseAccrualAdjs() {
    return expenseAccrualAdjs;
  }

  public void setExpenseAccrualAdjs(Double expenseAccrualAdjs) {
    this.expenseAccrualAdjs = expenseAccrualAdjs;
  }

  public Double getIndustryVarianceLimit() {
    return industryVarianceLimit;
  }

  public void setIndustryVarianceLimit(Double industryVarianceLimit) {
    this.industryVarianceLimit = industryVarianceLimit;
  }

  public Double getIndustryAverage() {
    return industryAverage;
  }

  public void setIndustryAverage(Double industryAverage) {
    this.industryAverage = industryAverage;
  }

  public Double getIndustryVariance() {
    return industryVariance;
  }

  public void setIndustryVariance(Double industryVariance) {
    this.industryVariance = industryVariance;
  }

  @Override
  public String toString() {
    return "ExpenseTestIACResult [\n"
        + "\t expenseAccrualAdjs=" + expenseAccrualAdjs + "\n"
        + "\t industryVarianceLimit=" + industryVarianceLimit + "\n"
        + "\t industryAverage=" + industryAverage + "\n"
        + "\t industryVariance=" + industryVariance + "\n"
        + "]";
  }

  public void copy(ExpenseTestIACResult o) {
    super.copy(o);
    expenseAccrualAdjs = o.expenseAccrualAdjs;
    industryVarianceLimit = o.industryVarianceLimit;
    industryAverage = o.industryAverage;
    industryVariance = o.industryVariance;
  }

  @Override
  public String getName() {
    return null;
  }
}
