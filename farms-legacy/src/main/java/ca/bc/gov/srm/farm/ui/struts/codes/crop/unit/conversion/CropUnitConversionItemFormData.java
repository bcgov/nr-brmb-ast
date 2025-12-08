/**
 * Copyright (c) 201*,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.codes.crop.unit.conversion;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author awilkinson
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class CropUnitConversionItemFormData {

  private String targetCropUnitCode;
  private String targetCropUnitCodeDescription;
  private String conversionFactor;

  public String getTargetCropUnitCode() {
    return targetCropUnitCode;
  }

  public void setTargetCropUnitCode(String targetCropUnitCode) {
    this.targetCropUnitCode = targetCropUnitCode;
  }

  public String getTargetCropUnitCodeDescription() {
    return targetCropUnitCodeDescription;
  }

  public void setTargetCropUnitCodeDescription(String targetCropUnitCodeDescription) {
    this.targetCropUnitCodeDescription = targetCropUnitCodeDescription;
  }

  public String getConversionFactor() {
    return conversionFactor;
  }

  public void setConversionFactor(String conversionFactor) {
    this.conversionFactor = conversionFactor;
  }
  
  
}
