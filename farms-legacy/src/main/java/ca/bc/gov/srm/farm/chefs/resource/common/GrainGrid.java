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

public class GrainGrid extends CropGrid {

  private String isIrrigated;
  private String grade;
  private Double endingPricePerUnit;
  private Double endingInventory;
  private Double quantityPurchased;
  private Double quantityUsedForFeed;
  private Double quantityUsedForSeed;

  public String getIsIrrigated() {
    return isIrrigated;
  }

  public void setIsIrrigated(String isIrrigated) {
    this.isIrrigated = isIrrigated;
  }

  public String getGrade() {
    return grade;
  }

  public void setGrade(String grade) {
    this.grade = grade;
  }

  public Double getEndingPricePerUnit() {
    return endingPricePerUnit;
  }

  public void setEndingPricePerUnit(Double endingPricePerUnit) {
    this.endingPricePerUnit = endingPricePerUnit;
  }

  public Double getEndingInventory() {
    return endingInventory;
  }

  public void setEndingInventory(Double endingInventory) {
    this.endingInventory = endingInventory;
  }

  public Double getQuantityPurchased() {
    return quantityPurchased;
  }

  public void setQuantityPurchased(Double quantityPurchased) {
    this.quantityPurchased = quantityPurchased;
  }

  public Double getQuantityUsedForFeed() {
    return quantityUsedForFeed;
  }

  public void setQuantityUsedForFeed(Double quantityUsedForFeed) {
    this.quantityUsedForFeed = quantityUsedForFeed;
  }

  public Double getQuantityUsedForSeed() {
    return quantityUsedForSeed;
  }

  public void setQuantityUsedForSeed(Double quantityUsedForSeed) {
    this.quantityUsedForSeed = quantityUsedForSeed;
  }

  @Override
  public String toString() {
    return "SupplementalGrainGrid [commodity=" + commodity + ", units=" + units + ", acres=" + acres + ", isIrrigated=" + isIrrigated + ", grade="
        + grade + ", endingPricePerUnit=" + endingPricePerUnit + ", endingInventory=" + endingInventory + ", quantitySold=" + quantitySold
        + ", quantityProduced=" + quantityProduced + ", quantityPurchased=" + quantityPurchased + ", quantityUsedForFeed=" + quantityUsedForFeed
        + ", quantityUsedForSeed=" + quantityUsedForSeed + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(acres, commodity, endingInventory, endingPricePerUnit, grade, isIrrigated, quantityProduced, quantityPurchased, quantitySold,
        quantityUsedForFeed, quantityUsedForSeed, units);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    GrainGrid other = (GrainGrid) obj;
    return Objects.equals(acres, other.acres) && Objects.equals(commodity, other.commodity) && Objects.equals(endingInventory, other.endingInventory)
        && Objects.equals(endingPricePerUnit, other.endingPricePerUnit) && Objects.equals(grade, other.grade)
        && Objects.equals(isIrrigated, other.isIrrigated) && Objects.equals(quantityProduced, other.quantityProduced)
        && Objects.equals(quantityPurchased, other.quantityPurchased) && Objects.equals(quantitySold, other.quantitySold)
        && Objects.equals(quantityUsedForFeed, other.quantityUsedForFeed) && Objects.equals(quantityUsedForSeed, other.quantityUsedForSeed)
        && Objects.equals(units, other.units);
  }

}
