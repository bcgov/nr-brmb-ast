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

public class InputGrid extends ChefsResource {

  private LabelValue input;
  private String specify;
  private Double amountRemainingAtYearEnd;

  public LabelValue getInput() {
    return input;
  }

  public void setInput(LabelValue input) {
    this.input = input;
  }

  public String getSpecify() {
    return specify;
  }

  public void setSpecify(String specify) {
    this.specify = specify;
  }

  public Double getAmountRemainingAtYearEnd() {
    return amountRemainingAtYearEnd;
  }

  public void setAmountRemainingAtYearEnd(Double amountRemainingAtYearEnd) {
    this.amountRemainingAtYearEnd = amountRemainingAtYearEnd;
  }

  @Override
  public int hashCode() {
    return Objects.hash(amountRemainingAtYearEnd, input, specify);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    InputGrid other = (InputGrid) obj;
    return Objects.equals(amountRemainingAtYearEnd, other.amountRemainingAtYearEnd) && Objects.equals(input, other.input)
        && Objects.equals(specify, other.specify);
  }

  @Override
  public String toString() {
    return "SupplementalInputGrid [input=" + input + ", specify=" + specify + ", amountRemainingAtYearEnd=" + amountRemainingAtYearEnd + "]";
  }

}
