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
package ca.bc.gov.srm.farm.chefs.resource.supplemental;

import ca.bc.gov.srm.farm.chefs.resource.ChefsResource;

public class SupplementalAccruals extends ChefsResource {

  private Boolean deferredIncomeAndReceivables;
  private Boolean purchasedInputs;
  private Boolean unpaidExpenses;
  private Boolean none;

  public Boolean getDeferredIncomeAndReceivables() {
    return deferredIncomeAndReceivables;
  }

  public void setDeferredIncomeAndReceivables(Boolean deferredIncomeAndReceivables) {
    this.deferredIncomeAndReceivables = deferredIncomeAndReceivables;
  }

  public Boolean getNone() {
    return none;
  }

  public void setNone(Boolean none) {
    this.none = none;
  }

  public Boolean getPurchasedInputs() {
    return purchasedInputs;
  }

  public void setPurchasedInputs(Boolean purchasedInputs) {
    this.purchasedInputs = purchasedInputs;
  }

  public Boolean getUnpaidExpenses() {
    return unpaidExpenses;
  }

  public void setUnpaidExpenses(Boolean unpaidExpenses) {
    this.unpaidExpenses = unpaidExpenses;
  }

}
