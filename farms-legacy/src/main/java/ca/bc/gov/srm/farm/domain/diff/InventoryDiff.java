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
 * A InventoryDiff represents a difference between the
 * crop/livestock items of two program year versions.
 * 
 * @author awilkinson
 * @created Mar 21, 2011
 */
public abstract class InventoryDiff implements Serializable {

  private static final long serialVersionUID = -8860579512309484666L;

  /** The inventory item code */
  private String code;

  /** The line item description */
  private String description;
  
  private Double quantityStart;
  
  private Double priceStart;
  
  private Double quantityEnd;
  
  private Double priceEnd;

  /** Added/Removed */
  private String action;

  /**
   * @return the action
   */
  public String getAction() {
    return action;
  }

  /**
   * @param action the action to set the value to
   */
  public void setAction(String action) {
    this.action = action;
  }

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
   * @return the priceEnd
   */
  public Double getPriceEnd() {
    return priceEnd;
  }

  /**
   * @param priceEnd the priceEnd to set the value to
   */
  public void setPriceEnd(Double priceEnd) {
    this.priceEnd = priceEnd;
  }

  /**
   * @return the priceStart
   */
  public Double getPriceStart() {
    return priceStart;
  }

  /**
   * @param priceStart the priceStart to set the value to
   */
  public void setPriceStart(Double priceStart) {
    this.priceStart = priceStart;
  }

  /**
   * @return the quantityEnd
   */
  public Double getQuantityEnd() {
    return quantityEnd;
  }

  /**
   * @param quantityEnd the quantityEnd to set the value to
   */
  public void setQuantityEnd(Double quantityEnd) {
    this.quantityEnd = quantityEnd;
  }

  /**
   * @return the quantityStart
   */
  public Double getQuantityStart() {
    return quantityStart;
  }

  /**
   * @param quantityStart the quantityStart to set the value to
   */
  public void setQuantityStart(Double quantityStart) {
    this.quantityStart = quantityStart;
  }

  
}
