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
import java.math.BigDecimal;

/**
 * @author awilkinson
 */
public class CropUnitConversionItem implements Serializable {

  private static final long serialVersionUID = 1L;
  
  private String targetCropUnitCode;
  private String targetCropUnitCodeDescription;
  private BigDecimal conversionFactor;

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

  public BigDecimal getConversionFactor() {
    return conversionFactor;
  }

  public void setConversionFactor(BigDecimal conversionFactor) {
    this.conversionFactor = conversionFactor;
  }

  @Override
  public String toString() {
    return "CropUnitConversionItem ["
        + "\t targetCropUnitCode=" + targetCropUnitCode
        + "\t targetCropUnitCodeDescription=" + targetCropUnitCodeDescription
        + "\t conversionFactor=" + conversionFactor + "]";
  }
  
  
}
