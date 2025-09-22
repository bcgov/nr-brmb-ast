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
package ca.bc.gov.srm.farm.ui.struts.codes.inventory.xref;

import java.io.Serializable;

/**
 * @author awilkinson
 */
public class InventoryXrefsFilterContext implements Serializable {
  
  private static final long serialVersionUID = 8291432505964132268L;

  private boolean setFilterContext;

  private String codeFilter;
  private String descriptionFilter;
  private String classFilter;
  private String groupFilter;
  private String isMarketCommodityFilter;


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

  /**
   * @return the groupFilter
   */
  public String getGroupFilter() {
    return groupFilter;
  }

  /**
   * @param groupFilter the groupFilter to set
   */
  public void setGroupFilter(String groupFilter) {
    this.groupFilter = groupFilter;
  }

  /**
   * @return the isMarketCommodityFilter
   */
  public String getIsMarketCommodityFilter() {
    return isMarketCommodityFilter;
  }

  /**
   * @param isMarketCommodityFilter the isMarketCommodityFilter to set
   */
  public void setIsMarketCommodityFilter(String isMarketCommodityFilter) {
    this.isMarketCommodityFilter = isMarketCommodityFilter;
  }

  /**
   * @return the classFilter
   */
  public String getClassFilter() {
    return classFilter;
  }

  /**
   * @param classFilter the classFilter to set
   */
  public void setClassFilter(String classFilter) {
    this.classFilter = classFilter;
  }

}
