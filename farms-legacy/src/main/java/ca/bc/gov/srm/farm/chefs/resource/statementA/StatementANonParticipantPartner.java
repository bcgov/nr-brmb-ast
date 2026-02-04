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

public class StatementANonParticipantPartner extends ChefsResource {

  private String name;
  private Double percent;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Double getPercent() {
    return percent;
  }

  public void setPercent(Double percent) {
    this.percent = percent;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, percent);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    StatementANonParticipantPartner other = (StatementANonParticipantPartner) obj;
    return Objects.equals(name, other.name) && Objects.equals(percent, other.percent);
  }

}
