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

public class StatementACombined extends ChefsResource {

  private Integer pin;
  private String addRemove;

  public Integer getPin() {
    return pin;
  }

  public void setPin(Integer pin) {
    this.pin = pin;
  }

  public String getAddRemove() {
    return addRemove;
  }

  public void setAddRemove(String addRemove) {
    this.addRemove = addRemove;
  }

  @Override
  public int hashCode() {
    return Objects.hash(addRemove, pin);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    StatementACombined other = (StatementACombined) obj;
    return Objects.equals(addRemove, other.addRemove) && Objects.equals(pin, other.pin);
  }

}
