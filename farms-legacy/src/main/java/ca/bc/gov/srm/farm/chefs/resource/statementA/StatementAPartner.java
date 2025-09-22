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
package ca.bc.gov.srm.farm.chefs.resource.statementA;

import java.util.Objects;

import ca.bc.gov.srm.farm.chefs.resource.ChefsResource;

public class StatementAPartner extends ChefsResource {

  private Integer pin;
  private Double percent;

  public Integer getPin() {
    return pin;
  }

  public void setPin(Integer pin) {
    this.pin = pin;
  }

  public Double getPercent() {
    return percent;
  }

  public void setPercent(Double percent) {
    this.percent = percent;
  }

  @Override
  public int hashCode() {
    return Objects.hash(percent, pin);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    StatementAPartner other = (StatementAPartner) obj;
    return Objects.equals(percent, other.percent) && Objects.equals(pin, other.pin);
  }

}
