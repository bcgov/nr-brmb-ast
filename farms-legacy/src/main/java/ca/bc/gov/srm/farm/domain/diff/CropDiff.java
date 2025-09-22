/**
 * Copyright (c) 2011,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain.diff;

/**
 * A CropDiff represents a difference between the
 * crop items of two program year versions.
 * 
 * @author awilkinson
 * @created Mar 22, 2011
 */
public class CropDiff extends InventoryDiff {
  
  private static final long serialVersionUID = -7740368615923009207L;

  private String cropUnitCode;
  
  private String cropUnitCodeDescription;

  private Double quantityProduced;
  
  private Double onFarmAcres;
  
  private Double unseedableAcres;

  /**
   * @return the cropUnitCode
   */
  public String getCropUnitCode() {
    return cropUnitCode;
  }

  /**
   * @param cropUnitCode the cropUnitCode to set the value to
   */
  public void setCropUnitCode(String cropUnitCode) {
    this.cropUnitCode = cropUnitCode;
  }

  /**
   * @return the cropUnitCodeDescription
   */
  public String getCropUnitCodeDescription() {
    return cropUnitCodeDescription;
  }

  /**
   * @param cropUnitCodeDescription the cropUnitCodeDescription to set the value to
   */
  public void setCropUnitCodeDescription(String cropUnitCodeDescription) {
    this.cropUnitCodeDescription = cropUnitCodeDescription;
  }

  /**
   * @return the quantityProduced
   */
  public Double getQuantityProduced() {
    return quantityProduced;
  }

  /**
   * @param quantityProduced the quantityProduced to set the value to
   */
  public void setQuantityProduced(Double quantityProduced) {
    this.quantityProduced = quantityProduced;
  }

  /**
   * Gets onFarmAcres
   *
   * @return the onFarmAcres
   */
  public Double getOnFarmAcres() {
    return onFarmAcres;
  }

  /**
   * Sets onFarmAcres
   *
   * @param pOnFarmAcres the onFarmAcres to set
   */
  public void setOnFarmAcres(Double pOnFarmAcres) {
    onFarmAcres = pOnFarmAcres;
  }

  public Double getUnseedableAcres() {
    return unseedableAcres;
  }

  public void setUnseedableAcres(Double unseedableAcres) {
    this.unseedableAcres = unseedableAcres;
  }

}
