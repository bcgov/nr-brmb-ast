/**
 * Copyright (c) 2018
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.codes.crop.unit.conversion;

import java.io.Serializable;

/**
 * @author awilkinson
 */
public class CropUnitConversionFilterContext implements Serializable {

  private static final long serialVersionUID = -6382308024959666188L;

  private String inventoryCodeFilter;
  private String inventoryDescFilter;

  public String getInventoryCodeFilter() {
    return inventoryCodeFilter;
  }

  public void setInventoryCodeFilter(String inventoryCodeFilter) {
    this.inventoryCodeFilter = inventoryCodeFilter;
  }

  public String getInventoryDescFilter() {
    return inventoryDescFilter;
  }

  public void setInventoryDescFilter(String inventoryDescFilter) {
    this.inventoryDescFilter = inventoryDescFilter;
  }

}
