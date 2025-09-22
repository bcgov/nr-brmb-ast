/**
 * Copyright (c) 2024,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.chefs.resource.common;

import ca.bc.gov.srm.farm.chefs.resource.ChefsResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.LabelValue;

public class IncomeExpenseGrid extends ChefsResource {

  private LabelValue category;
  private Double amount;
  private String specify;

  public LabelValue getCategory() {
    return category;
  }

  public void setCategory(LabelValue incomeCategory) {
    this.category = incomeCategory;
  }

  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public String getSpecify() {
    return specify;
  }

  public void setSpecify(String specify) {
    this.specify = specify;
  }

}
