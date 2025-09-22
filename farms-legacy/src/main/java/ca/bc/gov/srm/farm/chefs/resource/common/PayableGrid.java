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

import java.util.Objects;

import ca.bc.gov.srm.farm.chefs.resource.ChefsResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.LabelValue;

public class PayableGrid extends ChefsResource {

  private LabelValue sourceOfExpense;
  private String specify;
  private Double programExpensesNotPaidByYearEnd;

  public LabelValue getSourceOfExpense() {
    return sourceOfExpense;
  }

  public void setSourceOfExpense(LabelValue sourceOfExpense) {
    this.sourceOfExpense = sourceOfExpense;
  }

  public String getSpecify() {
    return specify;
  }

  public void setSpecify(String specify) {
    this.specify = specify;
  }

  public Double getProgramExpensesNotPaidByYearEnd() {
    return programExpensesNotPaidByYearEnd;
  }

  public void setProgramExpensesNotPaidByYearEnd(Double programExpensesNotPaidByYearEnd) {
    this.programExpensesNotPaidByYearEnd = programExpensesNotPaidByYearEnd;
  }

  @Override
  public String toString() {
    return "SupplementalExpensesGrid [sourceOfExpense=" + sourceOfExpense + ", specify=" + specify + ", programExpensesNotPaidByYearEnd="
        + programExpensesNotPaidByYearEnd + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(programExpensesNotPaidByYearEnd, sourceOfExpense, specify);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    PayableGrid other = (PayableGrid) obj;
    return Objects.equals(programExpensesNotPaidByYearEnd, other.programExpensesNotPaidByYearEnd)
        && Objects.equals(sourceOfExpense, other.sourceOfExpense) && Objects.equals(specify, other.specify);
  }

}
