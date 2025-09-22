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

import java.io.Serializable;

/**
 * @author awilkinson
 */
public class InventoryDetailFormData implements Serializable {

  private static final long serialVersionUID = 1L;

  private String inventoryItemDetailId;
  private Double insurableValue;
  private Double insurableValueOriginal;
  private Double premiumRate;
  private Double premiumRateOriginal;
  private Integer programYear;
  private boolean eligible;
  private boolean eligibleOriginal;
  private String revisionCount;
  
  private String fruitVegCodeName;
  private String fruitVegCodeNameOriginal;
  private String originalLineItem;
  private String lineItem;
  private String commodityTypeCodeName;
  private String commodityTypeCodeNameOriginal;

  /**
   * @return the inventoryItemDetailId
   */
  public String getInventoryItemDetailId() {
    return inventoryItemDetailId;
  }

  /**
   * @param inventoryItemDetailId the inventoryItemDetailId to set
   */
  public void setInventoryItemDetailId(String inventoryItemDetailId) {
    this.inventoryItemDetailId = inventoryItemDetailId;
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
    this.insurableValue = (insurableValue == null || insurableValue > 0) ? insurableValue : null;
  }

  /**
   * @return the insurableValueOriginal
   */
  public Double getInsurableValueOriginal() {
    return insurableValueOriginal;
  }

  /**
   * @param insurableValueOriginal the insurableValueOriginal to set
   */
  public void setInsurableValueOriginal(Double insurableValueOriginal) {
    this.insurableValueOriginal = (insurableValueOriginal == null || insurableValueOriginal > 0) ? insurableValueOriginal : null;
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
    this.premiumRate = (premiumRate == null || premiumRate > 0) ? premiumRate : null;
  }

  /**
   * @return the premiumRateOriginal
   */
  public Double getPremiumRateOriginal() {
    return premiumRateOriginal;
  }

  /**
   * @param premiumRateOriginal the premiumRateOriginal to set
   */
  public void setPremiumRateOriginal(Double premiumRateOriginal) {
    this.premiumRateOriginal = (premiumRateOriginal == null || premiumRateOriginal > 0) ? premiumRateOriginal : null;
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
   * @return the eligible
   */
  public boolean isEligible() {
    return eligible;
  }

  /**
   * @param eligible the eligible to set
   */
  public void setEligible(boolean eligible) {
    this.eligible = eligible;
  }

  /**
   * @return the eligibleOriginal
   */
  public boolean isEligibleOriginal() {
    return eligibleOriginal;
  }

  /**
   * @param eligibleOriginal the eligibleOriginal to set
   */
  public void setEligibleOriginal(boolean eligibleOriginal) {
    this.eligibleOriginal = eligibleOriginal;
  }

  /**
   * @return the revisionCount
   */
  public String getRevisionCount() {
    return revisionCount;
  }

  /**
   * @param revisionCount the revisionCount to set
   */
  public void setRevisionCount(String revisionCount) {
    this.revisionCount = revisionCount;
  }

  @Override
  public String toString() {
    return "InventoryItemDetail \n"
        + "\t inventoryItemDetailId=" + inventoryItemDetailId + "\n"
        + "\t programYear=" + programYear + "\n"
        + "\t eligible=" + eligible + "\n"
        + "\t eligibleOriginal=" + eligibleOriginal + "\n"
        + "\t revisionCount=" + revisionCount + "\n"
        + "\t commodityTypeCodeName=" + commodityTypeCodeName + "\n";
  }

  public String getFruitVegCodeName() {
    return fruitVegCodeName;
  }

  public void setFruitVegCodeName(String fruitVegCodeName) {
    this.fruitVegCodeName = fruitVegCodeName;
  }

  public String getFruitVegCodeNameOriginal() {
    return fruitVegCodeNameOriginal;
  }

  public void setFruitVegCodeNameOriginal(String fruitVegCodeNameOriginal) {
    this.fruitVegCodeNameOriginal = fruitVegCodeNameOriginal;
  }

  public String getLineItem() {
    return lineItem;
  }

  public void setLineItem(String lineItem) {
    this.lineItem = lineItem;
  }

  public String getOriginalLineItem() {
    return originalLineItem;
  }

  public void setOriginalLineItem(String originalLineItem) {
    this.originalLineItem = originalLineItem;
  }

  public String getCommodityTypeCodeName() {
    return commodityTypeCodeName;
  }

  public void setCommodityTypeCodeName(String commodityTypeCodeName) {
    this.commodityTypeCodeName = commodityTypeCodeName;
  }

  public String getCommodityTypeCodeNameOriginal() {
    return commodityTypeCodeNameOriginal;
  }

  public void setCommodityTypeCodeNameOriginal(String commodityTypeCodeNameOriginal) {
    this.commodityTypeCodeNameOriginal = commodityTypeCodeNameOriginal;
  }
  
}
