/**
 * Copyright (c) 2023,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.chefs.resource.interim;

import ca.bc.gov.srm.farm.chefs.resource.ChefsResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.LabelValue;

public class ProductionGrid extends ChefsResource {

  private LabelValue crop;

  private Double productiveArea;
  private Double unseedableAcres;

  private Double openingInventoryUnits;
  private Double estimatedEndingInventoryUnits;

  private Double estimatedTotalProduction;
  private Double estimatedProductionIntendedForSale;

  private String units;
  private LabelValue cropUnit;
  private String specifyCrop;

  public LabelValue getCrop() {
    return crop;
  }

  public void setCrop(LabelValue crop) {
    this.crop = crop;
  }

  public Double getProductiveArea() {
    return productiveArea;
  }

  public void setProductiveArea(Double productiveArea) {
    this.productiveArea = productiveArea;
  }

  public Double getUnseedableAcres() {
    return unseedableAcres;
  }

  public void setUnseedableAcres(Double unseedableAcres) {
    this.unseedableAcres = unseedableAcres;
  }

  public Double getEstimatedProductionIntendedForSale() {
    return estimatedProductionIntendedForSale;
  }

  public void setEstimatedProductionIntendedForSale(Double estimatedProductionIntendedForSale) {
    this.estimatedProductionIntendedForSale = estimatedProductionIntendedForSale;
  }

  public String getUnits() {
    return units;
  }

  public void setUnits(String units) {
    this.units = units;
  }

  public Double getEstimatedTotalProduction() {
    return estimatedTotalProduction;
  }

  public void setEstimatedTotalProduction(Double estimatedTotalProduction) {
    this.estimatedTotalProduction = estimatedTotalProduction;
  }

  public Double getOpeningInventoryUnits() {
    return openingInventoryUnits;
  }

  public void setOpeningInventoryUnits(Double openingInventoryUnits) {
    this.openingInventoryUnits = openingInventoryUnits;
  }

  public Double getEstimatedEndingInventoryUnits() {
    return estimatedEndingInventoryUnits;
  }

  public void setEstimatedEndingInventoryUnits(Double estimatedEndingInventoryUnits) {
    this.estimatedEndingInventoryUnits = estimatedEndingInventoryUnits;
  }

  public LabelValue getCropUnit() {
    return cropUnit;
  }

  public void setCropUnit(LabelValue cropUnit) {
    this.cropUnit = cropUnit;
  }

  public String getSpecifyCrop() {
    return specifyCrop;
  }

  public void setSpecifyCrop(String specifyCrop) {
    this.specifyCrop = specifyCrop;
  }

  @Override
  public String toString() {
    return "ProductionGrid [crop=" + crop + ", productiveArea=" + productiveArea + ", unseedableAcres=" + unseedableAcres
        + ", estimatedTotalProduction=" + estimatedTotalProduction + ", estimatedProductionIntendedForSale=" + estimatedProductionIntendedForSale
        + ", units=" + units + ", openingInventoryUnits=" + openingInventoryUnits + ", estimatedEndingInventoryUnits=" + estimatedEndingInventoryUnits
        + ", cropUnit=" + cropUnit + "]";
  }

}
