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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.domain.codes.InventoryItemCode;
import ca.bc.gov.srm.farm.list.CodeListView;

/**
 * @author awilkinson
 */
public class InventoryItemCodesForm extends ValidatorForm {

  private static final long serialVersionUID = -9188974583155950778L;

  private List<InventoryItemCode> codes;
  private int numCodes;

  private String code;
  private String description;
  private String rollupInventoryItemCode;
  private String rollupInventoryItemCodeDescription;
  private String inventorySearchInput;
  private Integer revisionCount;

  /** Map<programYear, InventoryDetailFormData> */
  private Map<String, InventoryDetailFormData> details = new HashMap<>();
  private List<CodeListView> fruitVegListViewItems;
  private List<CodeListView> commodityTypesListViewItems;

  private List<String> years;

  private boolean isNew = false;

  private boolean isSetFilterContext;
  private String codeFilter;
  private String descriptionFilter;

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
   * Gets codes
   *
   * @return the codes
   */
  public List<InventoryItemCode> getCodes() {
    return codes;
  }

  /**
   * Sets codes
   *
   * @param pCodes the codes to set
   */
  public void setCodes(List<InventoryItemCode> pCodes) {
    codes = pCodes;
  }

  /**
   * Gets numCodes
   *
   * @return the numCodes
   */
  public int getNumCodes() {
    return numCodes;
  }

  /**
   * Sets numCodes
   *
   * @param pNumCodes the numCodes to set
   */
  public void setNumCodes(int pNumCodes) {
    numCodes = pNumCodes;
  }

  /**
   * Gets code
   *
   * @return the code
   */
  public String getCode() {
    return code;
  }

  /**
   * Sets code
   *
   * @param pCode the code to set
   */
  public void setCode(String pCode) {
    code = pCode;
  }

  /**
   * Gets description
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets description
   *
   * @param pDescription the description to set
   */
  public void setDescription(String pDescription) {
    description = pDescription;
  }

  /**
   * Gets rollupInventoryItemCode
   *
   * @return the rollupInventoryItemCode
   */
  public String getRollupInventoryItemCode() {
    return rollupInventoryItemCode;
  }

  /**
   * Sets rollupInventoryItemCode
   *
   * @param pRollupInventoryItemCode the rollupInventoryItemCode to set
   */
  public void setRollupInventoryItemCode(String pRollupInventoryItemCode) {
    rollupInventoryItemCode = pRollupInventoryItemCode;
  }

  /**
   * Gets rollupInventoryItemCodeDescription
   *
   * @return the rollupInventoryItemCodeDescription
   */
  public String getRollupInventoryItemCodeDescription() {
    return rollupInventoryItemCodeDescription;
  }

  /**
   * Sets rollupInventoryItemCodeDescription
   *
   * @param pRollupInventoryItemCodeDescription the rollupInventoryItemCodeDescription to set
   */
  public void setRollupInventoryItemCodeDescription(String pRollupInventoryItemCodeDescription) {
    rollupInventoryItemCodeDescription = pRollupInventoryItemCodeDescription;
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
   * Gets revisionCount
   *
   * @return the revisionCount
   */
  public Integer getRevisionCount() {
    return revisionCount;
  }

  /**
   * Sets revisionCount
   *
   * @param pRevisionCount the revisionCount to set
   */
  public void setRevisionCount(Integer pRevisionCount) {
    revisionCount = pRevisionCount;
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
   * @return the isSetFilterContext
   */
  public boolean isSetFilterContext() {
    return isSetFilterContext;
  }

  /**
   * @param pIsNew the isSetFilterContext to set
   */
  public void setSetFilterContext(boolean pIsNew) {
    this.isSetFilterContext = pIsNew;
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
   * @return the items
   */
  public Map<String, InventoryDetailFormData> getDetails() {
    return details;
  }

  /**
   * @param details the items to set
   */
  public void setDetails(Map<String, InventoryDetailFormData> details) {
    this.details = details;
  }

  /**
   * @return the years
   */
  public List<String> getYears() {
    return years;
  }

  /**
   * @param years the years to set
   */
  public void setYears(List<String> years) {
    this.years = years;
  }

  /**
   * @param year String
   * @return AccrualFormData
   */
  public InventoryDetailFormData getDetail(String year) {
    InventoryDetailFormData fd = details.get(year);
    if(fd == null) {
      fd = new InventoryDetailFormData();
      details.put(year, fd);
    }
    fd.setProgramYear(Integer.valueOf(year));
    return fd;
  }
  
  public List<CodeListView> getFruitVegListViewItems() {
    return fruitVegListViewItems;
  }

  public void setFruitVegListViewItems(List<CodeListView> fruitVegListViewItems) {
    this.fruitVegListViewItems = fruitVegListViewItems;
  }

  public List<CodeListView> getCommodityTypesListViewItems() {
    return commodityTypesListViewItems;
  }

  public void setCommodityTypesListViewItems(List<CodeListView> commodityTypesListViewItems) {
    this.commodityTypesListViewItems = commodityTypesListViewItems;
  }

}
