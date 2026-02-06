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

public class CropGrid extends ChefsResource {

  protected LabelValue commodity;
  protected LabelValue units;
  protected Double acres;
  protected Double quantityProduced;
  protected Double quantitySold;

  public CropGrid() {
    super();
  }

  public CropGrid(String label, String value, Double acres, Double quantityProduced, Double quantitySold) {
    super();
    this.commodity = new LabelValue(label, value);
    this.acres = acres;
    this.quantityProduced = quantityProduced;
    this.quantitySold = quantitySold;
  }

  public LabelValue getCommodity() {
    return commodity;
  }

  public void setCommodity(LabelValue commodity) {
    this.commodity = commodity;
  }

  public LabelValue getUnits() {
    return units;
  }

  public void setUnits(LabelValue units) {
    this.units = units;
  }

  public Double getAcres() {
    return acres;
  }

  public void setAcres(Double acres) {
    this.acres = acres;
  }

  public Double getQuantityProduced() {
    return quantityProduced;
  }

  public void setQuantityProduced(Double quantityProduced) {
    this.quantityProduced = quantityProduced;
  }

  public Double getQuantitySold() {
    return quantitySold;
  }

  public void setQuantitySold(Double quantitySold) {
    this.quantitySold = quantitySold;
  }

  @Override
  public int hashCode() {
    return Objects.hash(acres, commodity, quantityProduced, quantitySold, units);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CropGrid other = (CropGrid) obj;
    return Objects.equals(acres, other.acres) && Objects.equals(commodity, other.commodity)
        && Objects.equals(quantityProduced, other.quantityProduced) && Objects.equals(quantitySold, other.quantitySold)
        && Objects.equals(units, other.units);
  }

  @Override
  public String toString() {
    return "SupplementalCropGrid [commodity=" + commodity + ", units=" + units + ", acres=" + acres + ", quantityProduced=" + quantityProduced
        + ", quantitySold=" + quantitySold + "]";
  }

}
