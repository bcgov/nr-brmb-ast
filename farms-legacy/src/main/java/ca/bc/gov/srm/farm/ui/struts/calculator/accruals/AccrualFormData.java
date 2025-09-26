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
package ca.bc.gov.srm.farm.ui.struts.calculator.accruals;

import java.io.Serializable;

import ca.bc.gov.srm.farm.domain.InventoryItem;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
import ca.bc.gov.srm.farm.util.StringUtils;


/**
 * @author awilkinson
 * @created Feb 22, 2011
 */
public class AccrualFormData implements Serializable {

  private static final long serialVersionUID = -2645279241365403101L;

  public static final String TYPE_NEW = "z_new";

  private String lineKey;
  
  private String lineCode;
  
  private String lineCodeDescription;

  private String itemType;

  private boolean isNew = false;
  
  private String searchInput;


  private Integer reportedId;
  private Integer adjustmentId;

  private String reportedStartOfYearAmount;
  private String adjStartOfYearAmount;

  private String reportedEndOfYearAmount;
  private String adjEndOfYearAmount;
  
  private String totalStartOfYearAmount;
  private String totalEndOfYearAmount;
  
  private String adjustedByUserId;
  
  private Double changeInValue;
  
  private Integer commodityXrefId;

  private Integer revisionCount;
  
  private boolean deleted;

  private boolean startError;
  private boolean endError;
  private boolean eligible;

  /**
   * @return the adjEndOfYearAmount
   */
  public String getAdjEndOfYearAmount() {
    return adjEndOfYearAmount;
  }

  /**
   * @param adjEndOfYearAmount the adjEndOfYearAmount to set the value to
   */
  public void setAdjEndOfYearAmount(String adjEndOfYearAmount) {
    this.adjEndOfYearAmount = adjEndOfYearAmount;
  }

  /**
   * @return the adjInventoryId
   */
  public Integer getAdjustmentId() {
    return adjustmentId;
  }

  /**
   * @param adjInventoryId the adjInventoryId to set the value to
   */
  public void setAdjustmentId(Integer adjInventoryId) {
    this.adjustmentId = adjInventoryId;
  }

  /**
   * @return the adjStartOfYearAmount
   */
  public String getAdjStartOfYearAmount() {
    return adjStartOfYearAmount;
  }

  /**
   * @param adjStartOfYearAmount the adjStartOfYearAmount to set the value to
   */
  public void setAdjStartOfYearAmount(String adjStartOfYearAmount) {
    this.adjStartOfYearAmount = adjStartOfYearAmount;
  }

  /**
   * @return the isNew
   */
  public boolean isNew() {
    return isNew;
  }

  /**
   * @param newValue the isNew to set the value to
   */
  public void setNew(boolean newValue) {
    this.isNew = newValue;
  }

  /**
   * @return the lineCode
   */
  public String getLineCode() {
    return lineCode;
  }

  /**
   * @param lineCode the lineCode to set the value to
   */
  public void setLineCode(String lineCode) {
    this.lineCode = lineCode;
  }

  /**
   * @return the lineCodeDescription
   */
  public String getLineCodeDescription() {
    return lineCodeDescription;
  }

  /**
   * @param lineCodeDescription the lineCodeDescription to set the value to
   */
  public void setLineCodeDescription(String lineCodeDescription) {
    this.lineCodeDescription = lineCodeDescription;
  }

  /**
   * @return the lineKey
   */
  public String getLineKey() {
    return lineKey;
  }

  /**
   * @param lineKey the lineKey to set the value to
   */
  public void setLineKey(String lineKey) {
    this.lineKey = lineKey;
  }

  /**
   * @return the reportedEndOfYearAmount
   */
  public String getReportedEndOfYearAmount() {
    return reportedEndOfYearAmount;
  }

  /**
   * @param reportedEndOfYearAmount the reportedEndOfYearAmount to set the value to
   */
  public void setReportedEndOfYearAmount(String reportedEndOfYearAmount) {
    this.reportedEndOfYearAmount = reportedEndOfYearAmount;
  }

  /**
   * @return the reportedInventoryId
   */
  public Integer getReportedId() {
    return reportedId;
  }

  /**
   * @param reportedInventoryId the reportedInventoryId to set the value to
   */
  public void setReportedId(Integer reportedInventoryId) {
    this.reportedId = reportedInventoryId;
  }

  /**
   * @return the reportedStartOfYearAmount
   */
  public String getReportedStartOfYearAmount() {
    return reportedStartOfYearAmount;
  }

  /**
   * @param reportedStartOfYearAmount the reportedStartOfYearAmount to set the value to
   */
  public void setReportedStartOfYearAmount(String reportedStartOfYearAmount) {
    this.reportedStartOfYearAmount = reportedStartOfYearAmount;
  }

  /**
   * @return the totalEndOfYearAmount
   */
  public String getTotalEndOfYearAmount() {
    return totalEndOfYearAmount;
  }

  /**
   * @param totalEndOfYearAmount the totalEndOfYearAmount to set the value to
   */
  public void setTotalEndOfYearAmount(String totalEndOfYearAmount) {
    this.totalEndOfYearAmount = totalEndOfYearAmount;
  }

  /**
   * @return the totalStartOfYearAmount
   */
  public String getTotalStartOfYearAmount() {
    return totalStartOfYearAmount;
  }

  /**
   * @param totalStartOfYearAmount the totalStartOfYearAmount to set the value to
   */
  public void setTotalStartOfYearAmount(String totalStartOfYearAmount) {
    this.totalStartOfYearAmount = totalStartOfYearAmount;
  }

  /**
   * @return the revisionCount
   */
  public Integer getRevisionCount() {
    return revisionCount;
  }

  /**
   * @param revisionCount the revisionCount to set the value to
   */
  public void setRevisionCount(Integer revisionCount) {
    this.revisionCount = revisionCount;
  }

  /**
   * @return the searchInput
   */
  public String getSearchInput() {
    return searchInput;
  }

  /**
   * @param searchInput the searchInput to set the value to
   */
  public void setSearchInput(String searchInput) {
    this.searchInput = searchInput;
  }

  /**
   * @return the type
   */
  public String getItemType() {
    return itemType;
  }

  /**
   * @param itemType the type to set the value to
   */
  public void setItemType(String itemType) {
    this.itemType = itemType;
  }

  /**
   * @return the adjustedByUserId
   */
  public String getAdjustedByUserId() {
    return adjustedByUserId;
  }

  /**
   * @param adjustedByUserId the adjustedByUserId to set the value to
   */
  public void setAdjustedByUserId(String adjustedByUserId) {
    this.adjustedByUserId = adjustedByUserId;
  }

  /**
   * @return the changeInValue
   */
  public Double getChangeInValue() {
    return changeInValue;
  }

  /**
   * @param changeInValue the changeInValue to set the value to
   */
  public void setChangeInValue(Double changeInValue) {
    this.changeInValue = changeInValue;
  }

  /**
   * @return the deleted
   */
  public boolean isDeleted() {
    return deleted;
  }

  /**
   * @param deleted the deleted to set the value to
   */
  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  /**
   * @return the endError
   */
  public boolean isEndError() {
    return endError;
  }

  /**
   * @param endError the endError to set the value to
   */
  public void setEndError(boolean endError) {
    this.endError = endError;
  }

  /**
   * @return the startError
   */
  public boolean isStartError() {
    return startError;
  }

  /**
   * @param startError the startError to set the value to
   */
  public void setStartError(boolean startError) {
    this.startError = startError;
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
   * @param inventoryItemCode String
   * @param inventoryClassCode String
   * @return String
   */
  public static String getAccrualKey(
      String inventoryItemCode,
      String inventoryClassCode) {
    String lineKey = getItemType(inventoryClassCode) + "_" + inventoryItemCode;
    return lineKey;
  }
  
  /**
   * @param item InventoryItem
   * @return String
   */
  public static String getLineKey(InventoryItem item) {
    String lineCode = item.getInventoryItemCode();
    String inventoryClassCode = item.getInventoryClassCode();
    Integer reportedId = item.getReportedInventoryId();
    Integer adjustmentId = item.getAdjInventoryId();
    String id = "";
    if(reportedId != null) {
      id = "_" + reportedId.toString();
    } else if(adjustmentId != null) {
      id = "_" + adjustmentId.toString();
    }
    String lineKey =
      getItemType(inventoryClassCode) +
      // prevent javascript error caused by code -1 - Unknown
      "_" + lineCode.replace('-', 'm') +
      id;
    return lineKey;
  }
  
  /**
   * @return boolean
   */
  private boolean isShowAdjToolTip() {
    boolean result = false;
    if(!deleted) {
      result = true;
    }
    return result;
  }
  
  /**
   * @return boolean
   */
  public boolean isShowAdjToolTipStart() {
    boolean result = false;
    if(isShowAdjToolTip() && ! StringUtils.equal(reportedStartOfYearAmount, totalStartOfYearAmount)) {
      result = true;
    }
    return result;
  }
  
  /**
   * @return boolean
   */
  public boolean isShowAdjToolTipEnd() {
    boolean result = false;
    if(isShowAdjToolTip() && ! StringUtils.equal(reportedEndOfYearAmount, totalEndOfYearAmount)) {
      result = true;
    }
    return result;
  }
  
  /**
   * @param inventoryClassCode String
   * @return String
   */
  public static String getItemType(String inventoryClassCode) {
    return "class" + inventoryClassCode;
  }

  /**
   * @return String
   */
  public static String getInputType() {
    return getItemType(InventoryClassCodes.INPUT);
  }
  
  /**
   * @return String
   */
  public static String getReceivableType() {
    return getItemType(InventoryClassCodes.RECEIVABLE);
  }
  
  /**
   * @return String
   */
  public static String getPayableType() {
    return getItemType(InventoryClassCodes.PAYABLE);
  }

}
