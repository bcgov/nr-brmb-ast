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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.domain.codes.InventoryXref;

/**
 * @author awilkinson
 */
public class InventoryXrefsForm extends ValidatorForm {

  private static final long serialVersionUID = -7692378219699575848L;

  private List<InventoryXref> codes;
  private int numCodes;

  private Integer commodityXrefId;
  private String inventoryItemCode;
  private String inventoryItemCodeDescription;
  private String inventoryClassCode;
  private String inventoryClassCodeDescription;
  private String inventoryGroupCode;
  private String inventoryGroupCodeDescription;
  private boolean isMarketCommodity;

  private String inventorySearchInput;

  private Integer revisionCount;

  private boolean isNew = false;

  private boolean isSetFilterContext;
  private String codeFilter;
  private String descriptionFilter;
  private String classFilter;
  private String groupFilter;
  private String isMarketCommodityFilter;

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
  public List<InventoryXref> getCodes() {
    return codes;
  }

  /**
   * Sets codes
   *
   * @param pCodes the codes to set
   */
  public void setCodes(List<InventoryXref> pCodes) {
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
   * @return the commodityXrefId
   */
  public Integer getCommodityXrefId() {
    return commodityXrefId;
  }

  /**
   * @param commodityXrefId the commodityXrefId to set
   */
  public void setCommodityXrefId(Integer commodityXrefId) {
    this.commodityXrefId = commodityXrefId;
  }

  /**
   * @return the inventoryItemCode
   */
  public String getInventoryItemCode() {
    return inventoryItemCode;
  }

  /**
   * @param inventoryItemCode the inventoryItemCode to set
   */
  public void setInventoryItemCode(String inventoryItemCode) {
    this.inventoryItemCode = inventoryItemCode;
  }

  /**
   * @return the inventoryItemCodeDescription
   */
  public String getInventoryItemCodeDescription() {
    return inventoryItemCodeDescription;
  }

  /**
   * @param inventoryItemCodeDescription the inventoryItemCodeDescription to set
   */
  public void setInventoryItemCodeDescription(String inventoryItemCodeDescription) {
    this.inventoryItemCodeDescription = inventoryItemCodeDescription;
  }

  /**
   * @return the inventoryClassCode
   */
  public String getInventoryClassCode() {
    return inventoryClassCode;
  }

  /**
   * @param inventoryClassCode the inventoryClassCode to set
   */
  public void setInventoryClassCode(String inventoryClassCode) {
    this.inventoryClassCode = inventoryClassCode;
  }

  /**
   * @return the inventoryClassCodeDescription
   */
  public String getInventoryClassCodeDescription() {
    return inventoryClassCodeDescription;
  }

  /**
   * @param inventoryClassCodeDescription the inventoryClassCodeDescription to set
   */
  public void setInventoryClassCodeDescription(
      String inventoryClassCodeDescription) {
    this.inventoryClassCodeDescription = inventoryClassCodeDescription;
  }

  /**
   * @return the inventoryGroupCode
   */
  public String getInventoryGroupCode() {
    return inventoryGroupCode;
  }

  /**
   * @param inventoryGroupCode the inventoryGroupCode to set
   */
  public void setInventoryGroupCode(String inventoryGroupCode) {
    this.inventoryGroupCode = inventoryGroupCode;
  }

  /**
   * @return the inventoryGroupCodeDescription
   */
  public String getInventoryGroupCodeDescription() {
    return inventoryGroupCodeDescription;
  }

  /**
   * @param inventoryGroupCodeDescription the inventoryGroupCodeDescription to set
   */
  public void setInventoryGroupCodeDescription(
      String inventoryGroupCodeDescription) {
    this.inventoryGroupCodeDescription = inventoryGroupCodeDescription;
  }

  /**
   * @return the isMarketCommodity
   */
  public boolean isMarketCommodity() {
    return isMarketCommodity;
  }

  /**
   * @param pIsMarketCommodity the isMarketCommodity to set
   */
  public void setMarketCommodity(boolean pIsMarketCommodity) {
    this.isMarketCommodity = pIsMarketCommodity;
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
   * @return the inventorySearchInput
   */
  public String getInventorySearchInput() {
    return inventorySearchInput;
  }

  /**
   * @param inventorySearchInput the inventorySearchInput to set
   */
  public void setInventorySearchInput(String inventorySearchInput) {
    this.inventorySearchInput = inventorySearchInput;
  }

}
