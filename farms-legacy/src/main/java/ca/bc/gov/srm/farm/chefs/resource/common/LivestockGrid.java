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

public class LivestockGrid extends ChefsResource {

  private LabelValue commodity;
  private Double endingFmv;
  private Double quantitySold;
  private Double endingInventory;

  public LabelValue getCommodity() {
    return commodity;
  }

  public void setCommodity(LabelValue commodity) {
    this.commodity = commodity;
  }

  public Double getEndingFmv() {
    return endingFmv;
  }

  public void setEndingFmv(Double endingFmv) {
    this.endingFmv = endingFmv;
  }

  public Double getQuantitySold() {
    return quantitySold;
  }

  public void setQuantitySold(Double quantitySold) {
    this.quantitySold = quantitySold;
  }

  public Double getEndingInventory() {
    return endingInventory;
  }

  public void setEndingInventory(Double endingInventory) {
    this.endingInventory = endingInventory;
  }

  @Override
  public String toString() {
    return "SupplementalLivestockGrid [commodity=" + commodity + ", endingFmv=" + endingFmv + ", quantitySold=" + quantitySold + ", endingInventory="
        + endingInventory + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(commodity, endingFmv, endingInventory, quantitySold);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    LivestockGrid other = (LivestockGrid) obj;
    return Objects.equals(commodity, other.commodity) && Objects.equals(endingFmv, other.endingFmv)
        && Objects.equals(endingInventory, other.endingInventory) && Objects.equals(quantitySold, other.quantitySold);
  }

}
