/**
 *
 * Copyright (c) 2010,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ca.bc.gov.srm.farm.domain.codes.CropUnitConversion;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
import ca.bc.gov.srm.farm.util.MathUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * Represents inventory items with INVENTORY CLASS CODE
 * 1 - Crops Inventory
 * 
 * This class represents the entries for both reported (federal data imports)
 * and adjustment values (fields are prefixed with "reported" and "adj"
 * respectively). Each adjustable value also has a getter for the total value
 * (sum of reported and adjustment) prefixed with "getTotal".  
 * 
 * @author awilkinson
 * @created Nov 12, 2010
 */
public class CropItem extends ProducedItem {

  private static final long serialVersionUID = 4559011557416659387L;

  /**
   * cropUnitCode is a unique code for the object UNIT CODE described as a
   * numeric code used to uniquely identify the unit of measure for the crop
   * inventory . Examples of codes and descriptions are 1-Pounds, 2-Tonnes,
   * 3-Dozen, 4-Bushels.
   */
  private String cropUnitCode;

  /** Description for cropUnitCode. */
  private String cropUnitCodeDescription;

  /**
   * quantityProduced is the quantity of a crop produced - section 8 column e.
   */
  private Double reportedQuantityProduced;
  private Double adjQuantityProduced;
  
  private Double onFarmAcres;
  
  private Double unseedableAcres;
  
  private CropUnitConversion cropUnitConversion;
  
  
  public CropItem() {
    setInventoryClassCode(InventoryClassCodes.CROP);
  }


  /**
   * CropUnitCode is a unique code for the object UNIT CODE described as a
   * numeric code used to uniquely identify the unit of measure for the crop
   * inventory . Examples of codes and descriptions are 1-Pounds, 2-Tonnes,
   * 3-Dozen, 4-Bushels.
   *
   * @return  String
   */
  public String getCropUnitCode() {
    return cropUnitCode;
  }

  /**
   * CropUnitCode is a unique code for the object UNIT CODE described as a
   * numeric code used to uniquely identify the unit of measure for the crop
   * inventory . Examples of codes and descriptions are 1-Pounds, 2-Tonnes,
   * 3-Dozen, 4-Bushels.
   *
   * @param  newVal  The new value for this property
   */
  public void setCropUnitCode(final String newVal) {
    cropUnitCode = newVal;
  }

  /**
   * Description for cropUnitCode.
   *
   * @return  String
   */
  public String getCropUnitCodeDescription() {
    return cropUnitCodeDescription;
  }

  /**
   * Description for cropUnitCode.
   *
   * @param  newVal  The new value for this property
   */
  public void setCropUnitCodeDescription(final String newVal) {
    cropUnitCodeDescription = newVal;
  }

  /**
   * QuantityProduced is the quantity of a crop produced - section 8 column e.
   *
   * @return the reportedQuantityProduced
   */
  public Double getReportedQuantityProduced() {
    return reportedQuantityProduced;
  }

  /**
   * QuantityProduced is the quantity of a crop produced - section 8 column e.
   *
   * @param reportedQuantityProduced the reportedQuantityProduced to set
   */
  public void setReportedQuantityProduced(Double reportedQuantityProduced) {
    this.reportedQuantityProduced = reportedQuantityProduced;
  }

  /**
   * QuantityProduced is the quantity of a crop produced - section 8 column e.
   *
   * @return  Double
   */
  public Double getAdjQuantityProduced() {
    return adjQuantityProduced;
  }

  /**
   * QuantityProduced is the quantity of a crop produced - section 8 column e.
   *
   * @param  newVal  The new value for this property
   */
  public void setAdjQuantityProduced(final Double newVal) {
    adjQuantityProduced = newVal;
  }

  /**
   * QuantityProduced is the quantity of a crop produced - section 8 column e.
   *
   * @return Double
   */
  @JsonIgnore
  public Double getTotalQuantityProduced() {
    return calculateTotal(reportedQuantityProduced, adjQuantityProduced);
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

  /**
   * @param o Object to compare
   * @return true if o is a CropItem and the inventory item code, crop unit code, and the reported values are equal
   */
  @Override
  public boolean equals(Object o) {
    boolean result = false;
    if(o != null && this.getClass().isInstance(o)) {
      CropItem item = (CropItem) o;
      result =
        super.equals(o)
        && StringUtils.equal(this.cropUnitCode, item.cropUnitCode)
        && MathUtils.equalToThreeDecimalPlaces(this.onFarmAcres, item.onFarmAcres)
        && MathUtils.equalToThreeDecimalPlaces(this.unseedableAcres, item.unseedableAcres)
        && MathUtils.equalToThreeDecimalPlaces(this.reportedQuantityProduced, item.reportedQuantityProduced);
    } else {
      result = false;
    }
    return result;
  }

  /**
   * @return hash code
   */
  @Override
  public int hashCode() {
    int hash = super.hashCode();
    int cu = 0;
    int ofa = 0;
    int ua = 0;
    int qp = 0;
    if(cropUnitCode != null) {
      cu = cropUnitCode.hashCode();
    }
    if(onFarmAcres != null) {
      ofa = onFarmAcres.hashCode();
    }
    if(unseedableAcres != null) {
      ua = unseedableAcres.hashCode();
    }
    if(reportedQuantityProduced != null) {
      qp = reportedQuantityProduced.hashCode();
    }

    final int twentyNine = 29;
    final int thirtyOne = 31;
    final int thirtySeven = 37;
    final int fortyOne = 41;
    hash = hash * twentyNine + cu;
    hash = hash * thirtyOne + ofa;
    hash = hash * thirtySeven + qp;
    hash = hash * fortyOne + ua;

    return hash;
  }

  public CropUnitConversion getCropUnitConversion() {
    return cropUnitConversion;
  }

  public void setCropUnitConversion(CropUnitConversion cropUnitConversion) {
    this.cropUnitConversion = cropUnitConversion;
  }
  
  /**
   * 
   * @return String
   * @see Object#toString()
   */
  @Override
  public String toString(){
    return "CropItem"+"\n"+
    "[ : "+super.toString()+"]\n"+
    "\t cropUnitCode : "+cropUnitCode+"\n"+
    "\t cropUnitCodeDescription : "+cropUnitCodeDescription+"\n"+
    "\t unseedableAcres : "+unseedableAcres+"\n"+
    "\t onFarmAcres : "+onFarmAcres+"\n"+
    "\t reportedQuantityProduced : "+reportedQuantityProduced+"\n"+
    "\t adjQuantityProduced : "+adjQuantityProduced;
  }  
}
