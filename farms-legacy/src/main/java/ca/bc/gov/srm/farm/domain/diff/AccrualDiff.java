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
 * @author awilkinson
 * @created Mar 21, 2011
 */
public class AccrualDiff extends InventoryDiff {
  
  private static final long serialVersionUID = 6649493257407216143L;

  /** The inventory item code */
  private String code;

  /** The line item description */
  private String description;

  private Double startValue;
  
  private Double endValue;

  /** Added/Removed */
  private String action;

  /**
   * @return the code
   */
  @Override
  public String getCode() {
    return code;
  }

  /**
   * @param code the code to set the value to
   */
  @Override
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * @return the description
   */
  @Override
  public String getDescription() {
    return description;
  }

  /**
   * @param description the description to set the value to
   */
  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @return the endValue
   */
  public Double getEndValue() {
    return endValue;
  }

  /**
   * @param endValue the endValue to set the value to
   */
  public void setEndValue(Double endValue) {
    this.endValue = endValue;
  }

  /**
   * @return the startValue
   */
  public Double getStartValue() {
    return startValue;
  }

  /**
   * @param startValue the startValue to set the value to
   */
  public void setStartValue(Double startValue) {
    this.startValue = startValue;
  }

  /**
   * @return the action
   */
  @Override
  public String getAction() {
    return action;
  }

  /**
   * @param action the action to set the value to
   */
  @Override
  public void setAction(String action) {
    this.action = action;
  }

  
}
