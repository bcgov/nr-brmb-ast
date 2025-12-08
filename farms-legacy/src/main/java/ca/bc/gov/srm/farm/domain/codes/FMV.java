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
package ca.bc.gov.srm.farm.domain.codes;


/**
 * @author awilkinson
 */
public class FMV {

  public static final int NUMBER_OF_PERIODS = 12;

  private Integer programYear;
  private String inventoryItemCode;
  private String inventoryItemCodeDescription;
  private String municipalityCode;
  private String municipalityCodeDescription;
  private String cropUnitCode;
  private String cropUnitCodeDescription;
  private String defaultCropUnitCode;
  private String defaultCropUnitCodeDescription;
  private FMVPeriod[] periods;

  /**
   * Gets programYear
   *
   * @return the programYear
   */
  public Integer getProgramYear() {
    return programYear;
  }

  /**
   * Sets programYear
   *
   * @param pProgramYear the programYear to set
   */
  public void setProgramYear(Integer pProgramYear) {
    programYear = pProgramYear;
  }

  /**
   * Gets inventoryItemCode
   *
   * @return the inventoryItemCode
   */
  public String getInventoryItemCode() {
    return inventoryItemCode;
  }

  /**
   * Sets inventoryItemCode
   *
   * @param pInventoryItemCode the inventoryItemCode to set
   */
  public void setInventoryItemCode(String pInventoryItemCode) {
    inventoryItemCode = pInventoryItemCode;
  }

  /**
   * Gets inventoryItemCodeDescription
   *
   * @return the inventoryItemCodeDescription
   */
  public String getInventoryItemCodeDescription() {
    return inventoryItemCodeDescription;
  }

  /**
   * Sets inventoryItemCodeDescription
   *
   * @param pInventoryItemCodeDescription the inventoryItemCodeDescription to set
   */
  public void setInventoryItemCodeDescription(String pInventoryItemCodeDescription) {
    inventoryItemCodeDescription = pInventoryItemCodeDescription;
  }

  /**
   * Gets municipalityCode
   *
   * @return the municipalityCode
   */
  public String getMunicipalityCode() {
    return municipalityCode;
  }

  /**
   * Sets municipalityCode
   *
   * @param pMunicipalityCode the municipalityCode to set
   */
  public void setMunicipalityCode(String pMunicipalityCode) {
    municipalityCode = pMunicipalityCode;
  }

  /**
   * Gets municipalityCodeDescription
   *
   * @return the municipalityCodeDescription
   */
  public String getMunicipalityCodeDescription() {
    return municipalityCodeDescription;
  }

  /**
   * Sets municipalityCodeDescription
   *
   * @param pMunicipalityCodeDescription the municipalityCodeDescription to set
   */
  public void setMunicipalityCodeDescription(String pMunicipalityCodeDescription) {
    municipalityCodeDescription = pMunicipalityCodeDescription;
  }

  /**
   * Gets cropUnitCode
   *
   * @return the cropUnitCode
   */
  public String getCropUnitCode() {
    return cropUnitCode;
  }

  /**
   * Sets cropUnitCode
   *
   * @param pCropUnitCode the cropUnitCode to set
   */
  public void setCropUnitCode(String pCropUnitCode) {
    cropUnitCode = pCropUnitCode;
  }

  /**
   * Gets cropUnitCodeDescription
   *
   * @return the cropUnitCodeDescription
   */
  public String getCropUnitCodeDescription() {
    return cropUnitCodeDescription;
  }

  /**
   * Sets cropUnitCodeDescription
   *
   * @param pCropUnitCodeDescription the cropUnitCodeDescription to set
   */
  public void setCropUnitCodeDescription(String pCropUnitCodeDescription) {
    cropUnitCodeDescription = pCropUnitCodeDescription;
  }

  public String getDefaultCropUnitCode() {
    return defaultCropUnitCode;
  }

  public void setDefaultCropUnitCode(String defaultCropUnitCode) {
    this.defaultCropUnitCode = defaultCropUnitCode;
  }

  public String getDefaultCropUnitCodeDescription() {
    return defaultCropUnitCodeDescription;
  }

  public void setDefaultCropUnitCodeDescription(String defaultCropUnitCodeDescription) {
    this.defaultCropUnitCodeDescription = defaultCropUnitCodeDescription;
  }

  /**
   * Gets periods
   *
   * @return the periods
   */
  public FMVPeriod[] getPeriods() {
    if(periods == null) {
      periods = new FMVPeriod[NUMBER_OF_PERIODS];
    }
    return periods;
  }

  /**
   * Sets periods
   *
   * @param pPeriods the periods to set
   */
  public void setPeriods(FMVPeriod[] pPeriods) {
    periods = pPeriods;
  }

  /**
   * 
   * @return String
   * @see Object#toString()
   */
  @Override
  public String toString(){
    
    int nonNullPeriods = 0;
    for(int ii = 0; ii < periods.length; ii++) {
      if(periods[ii] != null) {
        nonNullPeriods++;
      }
    }
    
    return "FMVPeriod"+"\n"+
    "\t programYear : "+programYear+"\n"+
    "\t inventoryItemCode : "+inventoryItemCode+"\n"+
    "\t inventoryItemCodeDescription : "+inventoryItemCodeDescription+"\n"+
    "\t municipalityCode : "+municipalityCode+"\n"+
    "\t municipalityCodeDescription : "+municipalityCodeDescription+"\n"+
    "\t cropUnitCode : "+cropUnitCode+"\n"+
    "\t cropUnitCodeDescription : "+cropUnitCodeDescription+"\n"+
    "\t defaultCropUnitCode : "+defaultCropUnitCode+"\n"+
    "\t defaultCropUnitCodeDescription : "+defaultCropUnitCodeDescription+"\n"+
    "\t non-null periods : " + nonNullPeriods;
  }

}
