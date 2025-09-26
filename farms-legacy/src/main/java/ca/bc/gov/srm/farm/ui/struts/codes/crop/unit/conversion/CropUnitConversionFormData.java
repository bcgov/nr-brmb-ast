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
package ca.bc.gov.srm.farm.ui.struts.codes.crop.unit.conversion;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author awilkinson
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class CropUnitConversionFormData {

  private String inventoryItemCode;
  private String inventoryItemCodeDescription;
  private String defaultCropUnitCode;
  private String defaultCropUnitCodeDescription;
  private String revisionCount;
  
  private List<CropUnitConversionItemFormData> conversionItems;

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

  public List<CropUnitConversionItemFormData> getConversionItems() {
    if(conversionItems == null) {
      conversionItems = new ArrayList<>();
    }
    return conversionItems;
  }

  public void setConversionItems(List<CropUnitConversionItemFormData> conversionItems) {
    this.conversionItems = conversionItems;
  }

  public String getRevisionCount() {
    return revisionCount;
  }

  public void setRevisionCount(String revisionCount) {
    this.revisionCount = revisionCount;
  }
  
}
