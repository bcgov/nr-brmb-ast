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

public class CattleGrid extends ChefsResource {

  private LabelValue cattleType;
  private Integer salesCattle;
  private Integer deathsCattle;
  private Double endingFmvCattle;
  private Double purchasesCattle;
  private Double averageWeightSales;
  private Double averageWeightEnding;
  private Double endingInventoryCattle;
  private Double averageWeightPurchases;

  public LabelValue getCattleType() {
    return cattleType;
  }

  public void setCattleType(LabelValue cattleType) {
    this.cattleType = cattleType;
  }

  public Integer getSalesCattle() {
    return salesCattle;
  }

  public void setSalesCattle(Integer salesCattle) {
    this.salesCattle = salesCattle;
  }

  public Integer getDeathsCattle() {
    return deathsCattle;
  }

  public void setDeathsCattle(Integer deathsCattle) {
    this.deathsCattle = deathsCattle;
  }

  public Double getEndingFmvCattle() {
    return endingFmvCattle;
  }

  public void setEndingFmvCattle(Double endingFmvCattle) {
    this.endingFmvCattle = endingFmvCattle;
  }

  public Double getPurchasesCattle() {
    return purchasesCattle;
  }

  public void setPurchasesCattle(Double purchasesCattle) {
    this.purchasesCattle = purchasesCattle;
  }

  public Double getAverageWeightSales() {
    return averageWeightSales;
  }

  public void setAverageWeightSales(Double averageWeightSales) {
    this.averageWeightSales = averageWeightSales;
  }

  public Double getAverageWeightEnding() {
    return averageWeightEnding;
  }

  public void setAverageWeightEnding(Double averageWeightEnding) {
    this.averageWeightEnding = averageWeightEnding;
  }

  public Double getEndingInventoryCattle() {
    return endingInventoryCattle;
  }

  public void setEndingInventoryCattle(Double endingInventoryCattle) {
    this.endingInventoryCattle = endingInventoryCattle;
  }

  public Double getAverageWeightPurchases() {
    return averageWeightPurchases;
  }

  public void setAverageWeightPurchases(Double averageWeightPurchases) {
    this.averageWeightPurchases = averageWeightPurchases;
  }

  @Override
  public String toString() {
    return "SupplementalCattleGrid [cattleType=" + cattleType + ", salesCattle=" + salesCattle + ", deathsCattle=" + deathsCattle
        + ", endingFmvCattle=" + endingFmvCattle + ", purchasesCattle=" + purchasesCattle + ", averageWeightSales=" + averageWeightSales
        + ", averageWeightEnding=" + averageWeightEnding + ", endingInventoryCattle=" + endingInventoryCattle + ", averageWeightPurchases="
        + averageWeightPurchases + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(averageWeightEnding, averageWeightPurchases, averageWeightSales, cattleType, deathsCattle, endingFmvCattle,
        endingInventoryCattle, purchasesCattle, salesCattle);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CattleGrid other = (CattleGrid) obj;
    return Objects.equals(averageWeightEnding, other.averageWeightEnding) && Objects.equals(averageWeightPurchases, other.averageWeightPurchases)
        && Objects.equals(averageWeightSales, other.averageWeightSales) && Objects.equals(cattleType, other.cattleType)
        && Objects.equals(deathsCattle, other.deathsCattle) && Objects.equals(endingFmvCattle, other.endingFmvCattle)
        && Objects.equals(endingInventoryCattle, other.endingInventoryCattle) && Objects.equals(purchasesCattle, other.purchasesCattle)
        && Objects.equals(salesCattle, other.salesCattle);
  }

}
