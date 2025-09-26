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
package ca.bc.gov.srm.farm.ui.struts.codes.bpu;

import java.io.Serializable;

/**
 * 
 */
public class BPUFilterContext implements Serializable {

  private static final long serialVersionUID = -6036352249755941339L;

  private Integer yearFilter;
  private String inventoryCodeFilter;
  private String inventoryDescFilter;
  private String municipalityFilter;
  private String marginExpenseFilter;

  /**
   * Gets yearFilter
   *
   * @return the yearFilter
   */
  public Integer getYearFilter() {
    return yearFilter;
  }

  /**
   * Sets yearFilter
   *
   * @param pYearFilter the yearFilter to set
   */
  public void setYearFilter(Integer pYearFilter) {
    yearFilter = pYearFilter;
  }

  /**
   * Gets inventoryCodeFilter
   *
   * @return the inventoryCodeFilter
   */
  public String getInventoryCodeFilter() {
    return inventoryCodeFilter;
  }

  /**
   * Sets inventoryCodeFilter
   *
   * @param pInventoryCodeFilter the inventoryCodeFilter to set
   */
  public void setInventoryCodeFilter(String pInventoryCodeFilter) {
    inventoryCodeFilter = pInventoryCodeFilter;
  }

  /**
   * Gets inventoryDescFilter
   *
   * @return the inventoryDescFilter
   */
  public String getInventoryDescFilter() {
    return inventoryDescFilter;
  }

  /**
   * Sets inventoryDescFilter
   *
   * @param pInventoryDescFilter the inventoryDescFilter to set
   */
  public void setInventoryDescFilter(String pInventoryDescFilter) {
    inventoryDescFilter = pInventoryDescFilter;
  }

  /**
   * Gets municipalityFilter
   *
   * @return the municipalityFilter
   */
  public String getMunicipalityFilter() {
    return municipalityFilter;
  }

  /**
   * Sets municipalityFilter
   *
   * @param pMunicipalityFilter the municipalityFilter to set
   */
  public void setMunicipalityFilter(String pMunicipalityFilter) {
    municipalityFilter = pMunicipalityFilter;
  }
  
  /**
   * Gets marginExpenseFilter
   *
   * @return the marginExpenseFilter
   */
  public String getMarginExpenseFilter() {
    return marginExpenseFilter;
  }

  /**
   * Sets marginExpenseFilter
   *
   * @param pMarginExpenseFilter the marginExpenseFilter to set
   */
  public void setMarginExpenseFilter(String pMarginExpenseFilter) {
    marginExpenseFilter = pMarginExpenseFilter;
  }
}
