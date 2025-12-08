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
package ca.bc.gov.srm.farm.domain.codes;


/**
 * @author awilkinson
 */
public class InventoryXref {

  private Integer commodityXrefId;
  private String inventoryItemCode;
  private String inventoryItemCodeDescription;
  private String inventoryClassCode;
  private String inventoryClassCodeDescription;
  private String inventoryGroupCode;
  private String inventoryGroupCodeDescription;
  private Boolean isMarketCommodity;
  private Integer revisionCount;


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
   * @return the isMarketCommodity
   */
  public Boolean getIsMarketCommodity() {
    return isMarketCommodity;
  }

  /**
   * @param isMarketCommodity the isMarketCommodity to set
   */
  public void setIsMarketCommodity(Boolean isMarketCommodity) {
    this.isMarketCommodity = isMarketCommodity;
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
   * @return the revisionCount
   */
  public Integer getRevisionCount() {
    return revisionCount;
  }

  /**
   * @param revisionCount the revisionCount to set
   */
  public void setRevisionCount(Integer revisionCount) {
    this.revisionCount = revisionCount;
  }

}
