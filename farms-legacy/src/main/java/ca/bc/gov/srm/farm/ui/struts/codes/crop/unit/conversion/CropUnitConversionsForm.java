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
package ca.bc.gov.srm.farm.ui.struts.codes.crop.unit.conversion;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.domain.codes.CropUnitConversion;

/**
 * @author awilkinson
 */
public class CropUnitConversionsForm extends ValidatorForm {

  private static final long serialVersionUID = 5403743286959977305L;

  /* context filters */
  private String inventoryCodeFilter;
  private String inventoryDescFilter;

  /* conversions list */
  private List<CropUnitConversion> cropUnitConversions;
  private int numCropUnitConversions;


  /* conversion view/edit */
  private String inventoryItemCode;
  private String inventoryItemCodeDescription;
  private String defaultCropUnitCode;
  private String defaultCropUnitCodeDescription;
  
  private String cropUnitConversionJson;
  
  private String inventorySearchInput;
  
  private String cropUnitSelectOptionsJson;

  private boolean isNew = false;
  private boolean updateFilterContext = false;

  @Override
  public void reset(ActionMapping mapping, HttpServletRequest request) {
    super.reset(mapping, request);
    setNew(false);
  }

  public String getInventoryCodeFilter() {
    return inventoryCodeFilter;
  }

  public void setInventoryCodeFilter(String pInventoryCodeFilter) {
    inventoryCodeFilter = pInventoryCodeFilter;
  }

  public String getInventoryDescFilter() {
    return inventoryDescFilter;
  }

  public void setInventoryDescFilter(String pInventoryDescFilter) {
    inventoryDescFilter = pInventoryDescFilter;
  }

  public List<CropUnitConversion> getCropUnitConversions() {
    return cropUnitConversions;
  }

  public void setCropUnitConversions(List<CropUnitConversion> cropUnitConversions) {
    this.cropUnitConversions = cropUnitConversions;
  }

  public int getNumCropUnitConversions() {
    return numCropUnitConversions;
  }

  public void setNumCropUnitConversions(int numCropUnitConversions) {
    this.numCropUnitConversions = numCropUnitConversions;
  }

  public String getInventoryItemCode() {
    return inventoryItemCode;
  }

  public void setInventoryItemCode(String pInventoryItemCode) {
    inventoryItemCode = pInventoryItemCode;
  }

  public String getInventoryItemCodeDescription() {
    return inventoryItemCodeDescription;
  }

  public void setInventoryItemCodeDescription(String pInventoryItemCodeDescription) {
    inventoryItemCodeDescription = pInventoryItemCodeDescription;
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

  public boolean isNew() {
    return isNew;
  }

  public void setNew(boolean pIsNew) {
    isNew = pIsNew;
  }

  public boolean isUpdateFilterContext() {
    return updateFilterContext;
  }

  public void setUpdateFilterContext(boolean updateFilterContext) {
    this.updateFilterContext = updateFilterContext;
  }

  public String getInventorySearchInput() {
    return inventorySearchInput;
  }

  public void setInventorySearchInput(String inventorySearchInput) {
    this.inventorySearchInput = inventorySearchInput;
  }

  public String getCropUnitConversionJson() {
    return cropUnitConversionJson;
  }

  public void setCropUnitConversionJson(String cropUnitConversionJson) {
    this.cropUnitConversionJson = cropUnitConversionJson;
  }

  public String getCropUnitSelectOptionsJson() {
    return cropUnitSelectOptionsJson;
  }

  public void setCropUnitSelectOptionsJson(String cropUnitSelectOptionsJson) {
    this.cropUnitSelectOptionsJson = cropUnitSelectOptionsJson;
  }

}
