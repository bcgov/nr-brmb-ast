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

import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
import ca.bc.gov.srm.farm.util.MathUtils;

/**
 * Represents inventory items with INVENTORY CLASS CODE
 * 4 - Deferred Income and Receivables
 * 
 * @author awilkinson
 * @created Nov 15, 2010
 */
public class ReceivableItem extends InventoryItem {
  
  private static final long serialVersionUID = 3377267187555327805L;
  
  
  public ReceivableItem() {
    setInventoryClassCode(InventoryClassCodes.RECEIVABLE);
  }


  /**
   * @param o Object to compare
   * @return true if the InventoryItem matches the inventory item code and the reported values
   */
  @Override
  public boolean equals(Object o) {
    boolean result = false;
    if(o != null && this.getClass().isInstance(o)) {
      ReceivableItem item = (ReceivableItem) o;
      result =
        this.getInventoryItemCode().equals(item.getInventoryItemCode())
        && this.getInventoryClassCode().equals(item.getInventoryClassCode())
        && MathUtils.equalToTwoDecimalPlaces(this.getReportedStartOfYearAmount(), item.getReportedStartOfYearAmount())
        && MathUtils.equalToTwoDecimalPlaces(this.getReportedEndOfYearAmount(), item.getReportedEndOfYearAmount());
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
    int hash = 1;
    int iic = 0;
    int icc = 0;
    int sa = 0;
    int ea = 0;
    if(getInventoryItemCode() != null) {
      iic = getInventoryItemCode().hashCode();
    }
    if(getInventoryClassCode() != null) {
      icc = getInventoryClassCode().hashCode();
    }
    if(getReportedStartOfYearAmount() != null) {
      sa = getReportedStartOfYearAmount().hashCode();
    }
    if(getReportedEndOfYearAmount() != null) {
      ea = getReportedEndOfYearAmount().hashCode();
    }

    final int seventeen = 17;
    final int thirtyOne = 31;
    final int thirteen = 13;
    final int nineteen = 19;

    hash = hash * seventeen + iic;
    hash = hash * thirtyOne + icc;
    hash = hash * thirteen + sa;
    hash = hash * nineteen + ea;
    return hash;
  }

  /**
   * @return String
   * @see Object#toString()
   */
  @Override
  public String toString(){
    return "ReceivableItem"+"\n"+
    "[ : "+super.toString()+"]\n";
  }

}
