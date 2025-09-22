/**
 * Copyright (c) 2016, 
 * Government of British Columbia, 
 * Canada
 *
 * All rights reserved. 
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain.codes;

import java.io.Serializable;


/**
 * @author awilkinson
 */
public class InventoryItemDetail implements Serializable {

  private static final long serialVersionUID = -2645279241365403001L;

  private Integer inventoryItemDetailId;
  private String inventoryItemCode;
  private Double insurableValue;
  private Double premiumRate;
  private Integer programYear;
  private Boolean isEligible;
  private Integer revisionCount;
  private String fruitVegCodeName;
  private String commodityTypeCodeName;
  private String lineItem;
  

  /**
   * @return the inventoryItemDetailId
   */
  public Integer getInventoryItemDetailId() {
    return inventoryItemDetailId;
  }

  /**
   * @param inventoryItemDetailId the inventoryItemDetailId to set
   */
  public void setInventoryItemDetailId(Integer inventoryItemDetailId) {
    this.inventoryItemDetailId = inventoryItemDetailId;
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
   * @return the insurableValue
   */
  public Double getInsurableValue() {
    return insurableValue;
  }

  /**
   * @param insurableValue the insurableValue to set
   */
  public void setInsurableValue(Double insurableValue) {
    this.insurableValue = insurableValue;
  }

  /**
   * @return the premiumRate
   */
  public Double getPremiumRate() {
    return premiumRate;
  }

  /**
   * @param premiumRate the premiumRate to set
   */
  public void setPremiumRate(Double premiumRate) {
    this.premiumRate = premiumRate;
  }

  /**
   * @return the programYear
   */
  public Integer getProgramYear() {
    return programYear;
  }

  /**
   * @param programYear the programYear to set
   */
  public void setProgramYear(Integer programYear) {
    this.programYear = programYear;
  }

  /**
   * @return the isEligible
   */
  public Boolean getIsEligible() {
    return isEligible;
  }

  /**
   * @param isEligible the isEligible to set
   */
  public void setIsEligible(Boolean isEligible) {
    this.isEligible = isEligible;
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
  
  @Override
  public String toString() {
    return "InventoryItemDetail \n"
        + "\t inventoryItemDetailId=" + inventoryItemDetailId + "\n"
        + "\t inventoryItemCode=" + inventoryItemCode + "\n"
        + "\t programYear=" + programYear + "\n"
        + "\t isEligible=" + isEligible + "\n"
        + "\t revisionCount=" + revisionCount + "\n"
        + "\t commodityTypeCodeName=" + commodityTypeCodeName + "\n";
  }

  public String getFruitVegCodeName() {
    return fruitVegCodeName;
  }

  public void setFruitVegCodeName(String fruitVegCodeName) {
    this.fruitVegCodeName = fruitVegCodeName;
  }

  public String getLineItem() {
    return lineItem;
  }

  public void setLineItem(String lineItem) {
    this.lineItem = lineItem;
  }

  public String getCommodityTypeCodeName() {
    return commodityTypeCodeName;
  }

  public void setCommodityTypeCodeName(String commodityTypeCodeName) {
    this.commodityTypeCodeName = commodityTypeCodeName;
  }

}
