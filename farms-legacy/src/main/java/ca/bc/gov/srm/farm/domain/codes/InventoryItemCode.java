/**
 * Copyright (c) 2012, 
 * Government of British Columbia, 
 * Canada
 *
 * All rights reserved. 
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain.codes;

import java.util.HashMap;
import java.util.Map;


/**
 * @author awilkinson
 */
public class InventoryItemCode extends AbstractCode {

  private String code;
  private String rollupInventoryItemCode;
  private String rollupInventoryItemCodeDescription;
  
  private Map<Integer, InventoryItemDetail> details = new HashMap<>();

  /**
   * @return the code
   */
  public String getCode() {
    return code;
  }

  /**
   * @param code the code to set
   */
  public void setCode(String code) {
    this.code = code;
  }

  /**
   * @return the rollupInventoryItemCode
   */
  public String getRollupInventoryItemCode() {
    return rollupInventoryItemCode;
  }

  /**
   * @param rollupInventoryItemCode the rollupInventoryItemCode to set
   */
  public void setRollupInventoryItemCode(String rollupInventoryItemCode) {
    this.rollupInventoryItemCode = rollupInventoryItemCode;
  }

  /**
   * @return the rollupInventoryItemCodeDescription
   */
  public String getRollupInventoryItemCodeDescription() {
    return rollupInventoryItemCodeDescription;
  }

  /**
   * @param rollupInventoryItemCodeDescription the rollupInventoryItemCodeDescription to set
   */
  public void setRollupInventoryItemCodeDescription(String rollupInventoryItemCodeDescription) {
    this.rollupInventoryItemCodeDescription = rollupInventoryItemCodeDescription;
  }

  /**
   * @return the details
   */
  public Map<Integer, InventoryItemDetail> getDetails() {
    return details;
  }

  /**
   * @param details the details to set
   */
  public void setDetails(Map<Integer, InventoryItemDetail> details) {
    this.details = details;
  }

}
