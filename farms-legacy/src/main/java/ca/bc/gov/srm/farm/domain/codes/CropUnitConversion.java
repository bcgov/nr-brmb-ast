/**
 * Copyright (c) 2018,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain.codes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author awilkinson
 */
public class CropUnitConversion implements Serializable {

  private static final long serialVersionUID = 1L;
  
  private String inventoryItemCode;
  private String inventoryItemCodeDescription;
  private String defaultCropUnitCode;
  private String defaultCropUnitCodeDescription;
  private Integer revisionCount;
  
  private List<CropUnitConversionItem> conversionItems;

  public String getInventoryItemCode() {
    return inventoryItemCode;
  }

  public void setInventoryItemCode(String inventoryItemCode) {
    this.inventoryItemCode = inventoryItemCode;
  }

  public String getInventoryItemCodeDescription() {
    return inventoryItemCodeDescription;
  }

  public void setInventoryItemCodeDescription(String inventoryItemCodeDescription) {
    this.inventoryItemCodeDescription = inventoryItemCodeDescription;
  }

  public String getDefaultCropUnitCode() {
    return defaultCropUnitCode;
  }

  public void setDefaultCropUnitCode(String defaultCropUnitCode) {
    this.defaultCropUnitCode = defaultCropUnitCode;
  }

  public String getDefaultCropUnitCodeDescription() {
    return defaultCropUnitCodeDescription;
  }

  public void setDefaultCropUnitCodeDescription(String defaultCropUnitCodeDescription) {
    this.defaultCropUnitCodeDescription = defaultCropUnitCodeDescription;
  }

  public List<CropUnitConversionItem> getConversionItems() {
    if(conversionItems == null) {
      conversionItems = new ArrayList<>();
    }
    return conversionItems;
  }

  public void setConversionItems(List<CropUnitConversionItem> conversionItems) {
    this.conversionItems = conversionItems;
  }

  public Integer getRevisionCount() {
    return revisionCount;
  }

  public void setRevisionCount(Integer revisionCount) {
    this.revisionCount = revisionCount;
  }

  @Override
  public String toString() {
    return "CropUnitConversion \n["
        + "\t inventoryItemCode=" + inventoryItemCode
        + "\t inventoryItemCodeDescription=" + inventoryItemCodeDescription
        + "\t defaultCropUnitCode=" + defaultCropUnitCode
        + "\t defaultCropUnitCodeDescription=" + defaultCropUnitCodeDescription
        + "\t revisionCount=" + revisionCount
        + "\t conversionItems=" + conversionItems + "]";
  }
  
}
