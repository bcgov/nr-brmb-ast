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

/**
 * Represents inventory items with INVENTORY CLASS CODE
 * 2 - Livestock Inventory
 * 
 * @author awilkinson
 * @created Nov 12, 2010
 */
public class LivestockItem extends ProducedItem {
  
  private static final long serialVersionUID = -1968985751269426179L;

  private Boolean isMarketCommodity;
  
  
  public LivestockItem() {
    setInventoryClassCode(InventoryClassCodes.LIVESTOCK);
  }
  

  /**
   * @return the isMarketCommodity
   */
  public Boolean getIsMarketCommodity() {
    return isMarketCommodity;
  }

  /**
   * @param isMarketCommodity the isMarketCommodity to set the value to
   */
  public void setIsMarketCommodity(Boolean isMarketCommodity) {
    this.isMarketCommodity = isMarketCommodity;
  }


  /**
   * @return String
   * @see Object#toString()
   */
  @Override
  public String toString(){
    return "LivestockItem"+"\n"+
    "[ : "+super.toString()+"]\n";
  }

}
