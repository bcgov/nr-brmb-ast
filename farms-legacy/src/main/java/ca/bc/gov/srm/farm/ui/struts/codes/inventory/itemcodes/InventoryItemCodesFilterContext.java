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
package ca.bc.gov.srm.farm.ui.struts.codes.inventory.itemcodes;

import java.io.Serializable;

/**
 * @author awilkinson
 */
public class InventoryItemCodesFilterContext implements Serializable {
  
  private static final long serialVersionUID = 4943761191130147084L;

  private boolean setFilterContext;

  private String codeFilter;
  private String descriptionFilter;


  /**
   * @return the setFilterContext
   */
  public boolean isSetFilterContext() {
    return setFilterContext;
  }

  /**
   * @param setFilterContext the setFilterContext to set
   */
  public void setSetFilterContext(boolean setFilterContext) {
    this.setFilterContext = setFilterContext;
  }

  /**
   * @return the codeFilter
   */
  public String getCodeFilter() {
    return codeFilter;
  }

  /**
   * @param codeFilter the codeFilter to set
   */
  public void setCodeFilter(String codeFilter) {
    this.codeFilter = codeFilter;
  }

  /**
   * @return the descriptionFilter
   */
  public String getDescriptionFilter() {
    return descriptionFilter;
  }

  /**
   * @param descriptionFilter the descriptionFilter to set
   */
  public void setDescriptionFilter(String descriptionFilter) {
    this.descriptionFilter = descriptionFilter;
  }

}
