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

import ca.bc.gov.srm.farm.chefs.resource.submission.LabelValue;

public class NurseryGrid extends CropGrid {

  private Integer squareMeters;
  private Double endingInventory;

  public NurseryGrid() {
    super();
  }

  public NurseryGrid(String label, String value, Integer squareMeters, Double quantityProduced, Double endingInventory) {
    super();
    this.commodity = new LabelValue(label, value);
    this.squareMeters = squareMeters;
    this.quantityProduced = quantityProduced;
    this.endingInventory = endingInventory;
  }

  public Integer getSquareMeters() {
    return squareMeters;
  }

  public void setSquareMeters(Integer squareMeters) {
    this.squareMeters = squareMeters;
  }

  public Double getEndingInventory() {
    return endingInventory;
  }

  public void setEndingInventory(Double endingInventory) {
    this.endingInventory = endingInventory;
  }

  @Override
  public String toString() {
    return "SupplementalNurseryGrid [commodity=" + commodity + ", units=" + units + ", squareMeters=" + squareMeters + ", quantityProduced="
        + quantityProduced + ", quantitySold=" + quantitySold + ", endingInventory=" + endingInventory + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(quantitySold, commodity, endingInventory, quantityProduced, squareMeters, units);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    NurseryGrid other = (NurseryGrid) obj;
    return Objects.equals(quantitySold, other.quantitySold) && Objects.equals(commodity, other.commodity)
        && Objects.equals(endingInventory, other.endingInventory) && Objects.equals(quantityProduced, other.quantityProduced)
        && Objects.equals(squareMeters, other.squareMeters) && Objects.equals(units, other.units);
  }

}
