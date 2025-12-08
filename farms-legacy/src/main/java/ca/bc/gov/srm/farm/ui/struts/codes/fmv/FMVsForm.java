/**
 * Copyright (c) 2006, 
 * Government of British Columbia, 
 * Canada
 *
 * All rights reserved. 
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.codes.fmv;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.domain.codes.FMV;
import ca.bc.gov.srm.farm.list.ListView;

/**
 * @author awilkinson
 */
public class FMVsForm extends ValidatorForm {

  private static final long serialVersionUID = 5403743286959977305L;

  private List<ListView> programYearSelectOptions;
  private Integer yearFilter;

  private Integer fmvYear;

  private String inventoryCodeFilter;
  private String inventoryDescFilter;
  private String municipalityFilter;
  private String cropUnitFilter;

  private List<FMV> fmvs;
  private int numFMVs;

  private String inventoryItemCode;
  private String inventoryItemCodeDescription;
  private String municipalityCode;
  private String municipalityCodeDescription;
  private String cropUnitCode;
  private String cropUnitCodeDescription;
  private String cropUnitCodeSelect;
  private String defaultCropUnitCode;
  private String defaultCropUnitCodeDescription;

  private String inventorySearchInput;
  
  private final int[] periods = {1,2,3,4,5,6,7,8,9,10,11,12};
  private String[] fairMarketValueIds = new String[FMV.NUMBER_OF_PERIODS];
  private String[] prices = new String[FMV.NUMBER_OF_PERIODS];
  private String[] percentVariances = new String[FMV.NUMBER_OF_PERIODS];
  private String[] revisionCounts = new String[FMV.NUMBER_OF_PERIODS];

  private String inventoryType;
  
  private boolean isNew = false;

  /**
   * @param mapping mapping
   * @param request request
   */
  @Override
  public void reset(ActionMapping mapping, HttpServletRequest request) {
    super.reset(mapping, request);
    setNew(false);
  }

  /**
   * Gets programYearSelectOptions
   *
   * @return the programYearSelectOptions
   */
  public List<ListView> getProgramYearSelectOptions() {
    return programYearSelectOptions;
  }

  /**
   * Sets programYearSelectOptions
   *
   * @param pProgramYearSelectOptions the programYearSelectOptions to set
   */
  public void setProgramYearSelectOptions(List<ListView> pProgramYearSelectOptions) {
    programYearSelectOptions = pProgramYearSelectOptions;
  }

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
   * Gets fmvYear
   *
   * @return the fmvYear
   */
  public Integer getFmvYear() {
    return fmvYear;
  }

  /**
   * Sets fmvYear
   *
   * @param pFmvYear the fmvYear to set
   */
  public void setFmvYear(Integer pFmvYear) {
    fmvYear = pFmvYear;
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

  /**
   * Gets fmvs
   *
   * @return the fmvs
   */
  public List<FMV> getFmvs() {
    return fmvs;
  }

  /**
   * Sets fmvs
   *
   * @param pFmvs the fmvs to set
   */
  public void setFmvs(List<FMV> pFmvs) {
    fmvs = pFmvs;
  }

  /**
   * Gets numFMVs
   *
   * @return the numFMVs
   */
  public int getNumFMVs() {
    return numFMVs;
  }

  /**
   * Sets numFMVs
   *
   * @param pNumFMVs the numFMVs to set
   */
  public void setNumFMVs(int pNumFMVs) {
    numFMVs = pNumFMVs;
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

  public String getCropUnitCodeSelect() {
    return cropUnitCodeSelect;
  }

  public void setCropUnitCodeSelect(String cropUnitCodeSelect) {
    this.cropUnitCodeSelect = cropUnitCodeSelect;
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
   * Gets fairMarketValueIds
   *
   * @return the fairMarketValueIds
   */
  public String[] getFairMarketValueIds() {
    return fairMarketValueIds;
  }

  /**
   * Gets prices
   *
   * @return the prices
   */
  public String[] getPrices() {
    return prices;
  }

  /**
   * Gets percentVariances
   *
   * @return the percentVariances
   */
  public String[] getPercentVariances() {
    return percentVariances;
  }

  /**
   * Gets revisionCounts
   *
   * @return the revisionCounts
   */
  public String[] getRevisionCounts() {
    return revisionCounts;
  }

  /**
   * Gets inventoryType
   *
   * @return the inventoryType
   */
  public String getInventoryType() {
    return inventoryType;
  }

  /**
   * Sets inventoryType
   *
   * @param pInventoryType the inventoryType to set
   */
  public void setInventoryType(String pInventoryType) {
    inventoryType = pInventoryType;
  }

  /**
   * Gets isNew
   *
   * @return the isNew
   */
  public boolean isNew() {
    return isNew;
  }

  /**
   * Sets isNew
   *
   * @param pIsNew the isNew to set
   */
  public void setNew(boolean pIsNew) {
    isNew = pIsNew;
  }

  /**
   * Gets inventorySearchInput
   *
   * @return the inventorySearchInput
   */
  public String getInventorySearchInput() {
    return inventorySearchInput;
  }

  /**
   * Sets inventorySearchInput
   *
   * @param pInventorySearchInput the inventorySearchInput to set
   */
  public void setInventorySearchInput(String pInventorySearchInput) {
    inventorySearchInput = pInventorySearchInput;
  }

  /**
   * @param index index
   * @return the fairMarketValueId
   */
  public String getFairMarketValueId(int index) {
    return fairMarketValueIds[index];
  }

  /**
   * @param index index
   * @param pFairMarketValueId the fairMarketValueId to set
   */
  public void setFairMarketValueId(int index, String pFairMarketValueId) {
    fairMarketValueIds[index] = pFairMarketValueId;
  }

  /**
   * @param index index
   * @return the price
   */
  public String getPrice(int index) {
    return prices[index];
  }

  /**
   * @param index index
   * @param pPrice the price to set
   */
  public void setPrice(int index, String pPrice) {
    prices[index] = pPrice;
  }

  /**
   * @param index index
   * @return the percentVariance
   */
  public String getPercentVariance(int index) {
    return percentVariances[index];
  }

  /**
   * @param index index
   * @param pPercentVariance the percentVariance to set
   */
  public void setPercentVariance(int index, String pPercentVariance) {
    percentVariances[index] = pPercentVariance;
  }

  /**
   * @param index index
   * @return the revisionCount
   */
  public String getRevisionCount(int index) {
    return revisionCounts[index];
  }

  /**
   * @param index index
   * @param pRevisionCount the revisionCount to set
   */
  public void setRevisionCount(int index, String pRevisionCount) {
    revisionCounts[index] = pRevisionCount;
  }

  /**
   * Gets periods
   *
   * @return the periods
   */
  public int[] getPeriods() {
    return periods;
  }

}
