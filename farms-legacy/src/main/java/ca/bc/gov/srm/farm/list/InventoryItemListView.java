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
package ca.bc.gov.srm.farm.list;


/**
 * @author awilkinson
 * @created Mar 1, 2011
 */
public class InventoryItemListView extends BaseListView {

  private Integer commodityXrefId;

  private String inventoryClassCode;

  private String inventoryClassCodeDescription;

  private String inventoryItemCode;

  private String inventoryItemCodeDescription;
  
  private Boolean isMarketCommodity;
  
  private Boolean isEligible;
  
  private String defaultCropUnitCode;
  
  private Integer lineItem;
  
  private String commodityTypeCode;
  
  private String multiStageCommodityCode;

  
  public InventoryItemListView() {
    super();
  }
  
  @Override
  public String getLabel() {
    return getInventoryItemCodeDescription();
  }
  
  /**
   * @return inventoryItemCode
   */
  @Override
  public String getValue() {
    return getInventoryItemCode();
  }

  /**
   * @return the commodityXrefId
   */
  public Integer getCommodityXrefId() {
    return commodityXrefId;
  }

  /**
   * @param commodityXrefId the commodityXrefId to set the value to
   */
  public void setCommodityXrefId(Integer commodityXrefId) {
    this.commodityXrefId = commodityXrefId;
  }

  /**
   * @return the inventoryClassCode
   */
  public String getInventoryClassCode() {
    return inventoryClassCode;
  }

  /**
   * @param inventoryClassCode the inventoryClassCode to set the value to
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
   * @param inventoryClassCodeDescription the inventoryClassCodeDescription to set the value to
   */
  public void setInventoryClassCodeDescription(String inventoryClassCodeDescription) {
    this.inventoryClassCodeDescription = inventoryClassCodeDescription;
  }

  /**
   * @return the inventoryItemCode
   */
  public String getInventoryItemCode() {
    return inventoryItemCode;
  }

  /**
   * @param inventoryItemCode the inventoryItemCode to set the value to
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
   * @param inventoryItemCodeDescription the inventoryItemCodeDescription to set the value to
   */
  public void setInventoryItemCodeDescription(String inventoryItemCodeDescription) {
    this.inventoryItemCodeDescription = inventoryItemCodeDescription;
  }

  /**
   * @return the isMarketCommodity
   */
  public Boolean getIsMarketCommodity() {
    return isMarketCommodity;
  }

  public void setIsMarketCommodity(Boolean isMarketCommodity) {
    this.isMarketCommodity = isMarketCommodity;
  }

  public Boolean getIsEligible() {
    return isEligible;
  }

  public void setIsEligible(Boolean isEligible) {
    this.isEligible = isEligible;
  }

  public String getDefaultCropUnitCode() {
    return defaultCropUnitCode;
  }

  public void setDefaultCropUnitCode(String defaultCropUnitCode) {
    this.defaultCropUnitCode = defaultCropUnitCode;
  }

  public Integer getLineItem() {
    return lineItem;
  }

  public void setLineItem(Integer lineItem) {
    this.lineItem = lineItem;
  }

  public String getCommodityTypeCode() {
    return commodityTypeCode;
  }

  public void setCommodityTypeCode(String commodityTypeCode) {
    this.commodityTypeCode = commodityTypeCode;
  }

  public String getMultiStageCommodityCode() {
    return multiStageCommodityCode;
  }

  public void setMultiStageCommodityCode(String multiStageCommodityCode) {
    this.multiStageCommodityCode = multiStageCommodityCode;
  }
  
}
