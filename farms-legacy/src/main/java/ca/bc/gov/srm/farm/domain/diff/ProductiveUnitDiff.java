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

import java.io.Serializable;

/**
 * @author awilkinson
 * @created Mar 21, 2011
 */
public class ProductiveUnitDiff implements Serializable {

  private static final long serialVersionUID = 2795427669096894013L;

  /** The structure group code or inventory item code */
  private String code;

  /** The line item description */
  private String description;
  
  private Double oldValue;
  
  private Double newValue;

  /**
   * @return the code
   */
  public String getCode() {
    return code;
  }

  /**
   * @param code the code to set the value to
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description the description to set the value to
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @return the newValue
   */
  public Double getNewValue() {
    return newValue;
  }

  /**
   * @param newValue the newValue to set the value to
   */
  public void setNewValue(Double newValue) {
    this.newValue = newValue;
  }

  /**
   * @return the oldValue
   */
  public Double getOldValue() {
    return oldValue;
  }

  /**
   * @param oldValue the oldValue to set the value to
   */
  public void setOldValue(Double oldValue) {
    this.oldValue = oldValue;
  }

  
}
