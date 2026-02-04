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
package ca.bc.gov.srm.farm.ui.struts.codes.fmv;

import java.io.Serializable;

/**
 * @author awilkinson
 */
public class FMVFilterContext implements Serializable {

  private static final long serialVersionUID = -6382308024959666188L;

  private Integer yearFilter;
  private String inventoryCodeFilter;
  private String inventoryDescFilter;
  private String municipalityFilter;
  private String cropUnitFilter;

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
   * Gets cropUnitFilter
   *
   * @return the cropUnitFilter
   */
  public String getCropUnitFilter() {
    return cropUnitFilter;
  }

  /**
   * Sets cropUnitFilter
   *
   * @param pCropUnitFilter the cropUnitFilter to set
   */
  public void setCropUnitFilter(String pCropUnitFilter) {
    cropUnitFilter = pCropUnitFilter;
  }

}
