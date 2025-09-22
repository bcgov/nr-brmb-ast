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

public class ReceivablesGrid extends ChefsResource {

  private LabelValue incomeSource;
  private String specify;
  private Double incomeReceivedAfterYearEnd;

  public LabelValue getIncomeSource() {
    return incomeSource;
  }

  public void setIncomeSource(LabelValue incomeSource) {
    this.incomeSource = incomeSource;
  }

  public String getSpecify() {
    return specify;
  }

  public void setSpecify(String specify) {
    this.specify = specify;
  }

  public Double getIncomeReceivedAfterYearEnd() {
    return incomeReceivedAfterYearEnd;
  }

  public void setIncomeReceivedAfterYearEnd(Double incomeReceivedAfterYearEnd) {
    this.incomeReceivedAfterYearEnd = incomeReceivedAfterYearEnd;
  }

}
