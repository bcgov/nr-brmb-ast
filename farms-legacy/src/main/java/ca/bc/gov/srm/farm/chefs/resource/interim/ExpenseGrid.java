/**
 * Copyright (c) 2023,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.chefs.resource.interim;

import ca.bc.gov.srm.farm.chefs.resource.ChefsResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.LabelValue;

public class ExpenseGrid extends ChefsResource {

  private LabelValue expenseCategories;
  private String specify;
  private Double estimatedTotalExpenseAmount;

  public LabelValue getExpenseCategories() {
    return expenseCategories;
  }

  public void setExpenseCategories(LabelValue expenseCategories) {
    this.expenseCategories = expenseCategories;
  }

  public Double getEstimatedTotalExpenseAmount() {
    return estimatedTotalExpenseAmount;
  }

  public void setEstimatedTotalExpenseAmount(Double estimatedTotalExpenseAmount) {
    this.estimatedTotalExpenseAmount = estimatedTotalExpenseAmount;
  }

  public String getSpecify() {
    return specify;
  }

  public void setSpecify(String specify) {
    this.specify = specify;
  }


  @Override
  public String toString() {
    return "ExpenseGrid [expenseCategories=" + expenseCategories + ", estimatedTotalExpenseAmount=" + estimatedTotalExpenseAmount + "]";
  }

}
