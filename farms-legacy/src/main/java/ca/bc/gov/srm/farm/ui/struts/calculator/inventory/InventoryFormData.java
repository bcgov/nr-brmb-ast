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
package ca.bc.gov.srm.farm.ui.struts.calculator.inventory;

import java.io.Serializable;

import ca.bc.gov.srm.farm.domain.CropItem;
import ca.bc.gov.srm.farm.domain.ProducedItem;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
import ca.bc.gov.srm.farm.util.MathUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * @author awilkinson
 * @created Jan 27, 2011
 */
public class InventoryFormData implements Serializable {

  private static final long serialVersionUID = 1L;

  public static final String TYPE_NEW = "z_new";

  private String lineKey;
  
  private String lineCode;
  
  private String lineCodeDescription;

  private String itemType;

  private boolean isNew = false;
  
  private String searchInput;

  private Integer reportedId;
  private Integer adjustmentId;

  private String reportedQuantityProduced;
  private String adjQuantityProduced;
  private String totalQuantityProduced;

  private String reportedQuantityStart;
  private String adjQuantityStart;
  private String totalQuantityStart;
  
  private String reportedQuantityEnd;
  private String adjQuantityEnd;
  private String totalQuantityEnd;

  private String reportedPriceStart;
  private String adjPriceStart;
  private String totalPriceStart;

  private String reportedPriceEnd;
  private String adjPriceEnd;
  private String totalPriceEnd;

  private String reportedEndYearProducerPrice;
  private String adjEndYearProducerPrice;
  
  private String adjustedByUserId;
  
  private Double changeInValue;
  
  private Integer commodityXrefId;

  private Integer revisionCount;
  
  private boolean deleted;

  private boolean errorUnits;
  private boolean errorQuantityProduced;

  private boolean errorQuantityStart;
  private boolean errorQuantityEnd;
  
  private boolean errorPriceStart;
  private boolean errorPriceEnd;

  private String cropUnitCode;
  private String cropUnitCodeDescription;
  
  private Double endYearProducerPrice;
  
  private Double fmvStart;
  private Double fmvEnd;
  private Double fmvPreviousYearEnd;
  private String fmvVariance;
  private boolean priceStartOutsideFmvVariance;
  private boolean priceEndOutsideFmvVariance;
  private boolean reportedPriceStartOutsideFmvVariance;
  private boolean reportedPriceEndOutsideFmvVariance;
  
  private Boolean marketCommodity;
  
  private String onFarmAcres;
  private String unseedableAcres;


  /**
   * @return the adjEndYearProducerPrice
   */
  public String getAdjEndYearProducerPrice() {
    return adjEndYearProducerPrice;
  }

  /**
   * @param adjEndYearProducerPrice the adjEndYearProducerPrice to set the value to
   */
  public void setAdjEndYearProducerPrice(String adjEndYearProducerPrice) {
    this.adjEndYearProducerPrice = adjEndYearProducerPrice;
  }

  /**
   * @return the adjPriceEnd
   */
  public String getAdjPriceEnd() {
    return adjPriceEnd;
  }

  /**
   * @param adjPriceEnd the adjPriceEnd to set the value to
   */
  public void setAdjPriceEnd(String adjPriceEnd) {
    this.adjPriceEnd = adjPriceEnd;
  }

  /**
   * @return the adjPriceStart
   */
  public String getAdjPriceStart() {
    return adjPriceStart;
  }

  /**
   * @param adjPriceStart the adjPriceStart to set the value to
   */
  public void setAdjPriceStart(String adjPriceStart) {
    this.adjPriceStart = adjPriceStart;
  }

  /**
   * @return the adjQuantityEnd
   */
  public String getAdjQuantityEnd() {
    return adjQuantityEnd;
  }

  /**
   * @param adjQuantityEnd the adjQuantityEnd to set the value to
   */
  public void setAdjQuantityEnd(String adjQuantityEnd) {
    this.adjQuantityEnd = adjQuantityEnd;
  }

  /**
   * @return the adjQuantityStart
   */
  public String getAdjQuantityStart() {
    return adjQuantityStart;
  }

  /**
   * @param adjQuantityStart the adjQuantityStart to set the value to
   */
  public void setAdjQuantityStart(String adjQuantityStart) {
    this.adjQuantityStart = adjQuantityStart;
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
   * @return the adjustmentId
   */
  public Integer getAdjustmentId() {
    return adjustmentId;
  }

  /**
   * @param adjustmentId the adjustmentId to set the value to
   */
  public void setAdjustmentId(Integer adjustmentId) {
    this.adjustmentId = adjustmentId;
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
   * @return the itemType
   */
  public String getItemType() {
    return itemType;
  }

  /**
   * @param itemType the itemType to set the value to
   */
  public void setItemType(String itemType) {
    this.itemType = itemType;
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
   * @return the reportedEndYearProducerPrice
   */
  public String getReportedEndYearProducerPrice() {
    return reportedEndYearProducerPrice;
  }

  /**
   * @param reportedEndYearProducerPrice the reportedEndYearProducerPrice to set the value to
   */
  public void setReportedEndYearProducerPrice(String reportedEndYearProducerPrice) {
    this.reportedEndYearProducerPrice = reportedEndYearProducerPrice;
  }

  /**
   * @return the reportedId
   */
  public Integer getReportedId() {
    return reportedId;
  }

  /**
   * @param reportedId the reportedId to set the value to
   */
  public void setReportedId(Integer reportedId) {
    this.reportedId = reportedId;
  }

  /**
   * @return the reportedPriceEnd
   */
  public String getReportedPriceEnd() {
    return reportedPriceEnd;
  }

  /**
   * @param reportedPriceEnd the reportedPriceEnd to set the value to
   */
  public void setReportedPriceEnd(String reportedPriceEnd) {
    this.reportedPriceEnd = reportedPriceEnd;
  }

  /**
   * @return the reportedPriceStart
   */
  public String getReportedPriceStart() {
    return reportedPriceStart;
  }

  /**
   * @param reportedPriceStart the reportedPriceStart to set the value to
   */
  public void setReportedPriceStart(String reportedPriceStart) {
    this.reportedPriceStart = reportedPriceStart;
  }

  /**
   * @return the reportedQuantityEnd
   */
  public String getReportedQuantityEnd() {
    return reportedQuantityEnd;
  }

  /**
   * @param reportedQuantityEnd the reportedQuantityEnd to set the value to
   */
  public void setReportedQuantityEnd(String reportedQuantityEnd) {
    this.reportedQuantityEnd = reportedQuantityEnd;
  }

  /**
   * @return the reportedQuantityStart
   */
  public String getReportedQuantityStart() {
    return reportedQuantityStart;
  }

  /**
   * @param reportedQuantityStart the reportedQuantityStart to set the value to
   */
  public void setReportedQuantityStart(String reportedQuantityStart) {
    this.reportedQuantityStart = reportedQuantityStart;
  }

  /**
   * @return the adjQuantityProduced
   */
  public String getAdjQuantityProduced() {
    return adjQuantityProduced;
  }

  /**
   * @param adjQuantityProduced the adjQuantityProduced to set the value to
   */
  public void setAdjQuantityProduced(String adjQuantityProduced) {
    this.adjQuantityProduced = adjQuantityProduced;
  }

  /**
   * @return the reportedQuantityProduced
   */
  public String getReportedQuantityProduced() {
    return reportedQuantityProduced;
  }

  /**
   * @param reportedQuantityProduced the reportedQuantityProduced to set the value to
   */
  public void setReportedQuantityProduced(String reportedQuantityProduced) {
    this.reportedQuantityProduced = reportedQuantityProduced;
  }

  /**
   * @return the totalQuantityProduced
   */
  public String getTotalQuantityProduced() {
    return totalQuantityProduced;
  }

  /**
   * @param totalQuantityProduced the totalQuantityProduced to set the value to
   */
  public void setTotalQuantityProduced(String totalQuantityProduced) {
    this.totalQuantityProduced = totalQuantityProduced;
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
   * @return the totalPriceEnd
   */
  public String getTotalPriceEnd() {
    return totalPriceEnd;
  }

  /**
   * @param totalPriceEnd the totalPriceEnd to set the value to
   */
  public void setTotalPriceEnd(String totalPriceEnd) {
    this.totalPriceEnd = totalPriceEnd;
  }

  /**
   * @return the totalPriceStart
   */
  public String getTotalPriceStart() {
    return totalPriceStart;
  }

  /**
   * @param totalPriceStart the totalPriceStart to set the value to
   */
  public void setTotalPriceStart(String totalPriceStart) {
    this.totalPriceStart = totalPriceStart;
  }

  /**
   * @return the totalQuantityEnd
   */
  public String getTotalQuantityEnd() {
    return totalQuantityEnd;
  }

  /**
   * @param totalQuantityEnd the totalQuantityEnd to set the value to
   */
  public void setTotalQuantityEnd(String totalQuantityEnd) {
    this.totalQuantityEnd = totalQuantityEnd;
  }

  /**
   * @return the totalQuantityStart
   */
  public String getTotalQuantityStart() {
    return totalQuantityStart;
  }

  /**
   * @param totalQuantityStart the totalQuantityStart to set the value to
   */
  public void setTotalQuantityStart(String totalQuantityStart) {
    this.totalQuantityStart = totalQuantityStart;
  }

  /**
   * @return the cropUnitCode
   */
  public String getCropUnitCode() {
    return cropUnitCode;
  }

  /**
   * @param cropUnitCode the cropUnitCode to set the value to
   */
  public void setCropUnitCode(String cropUnitCode) {
    this.cropUnitCode = cropUnitCode;
  }

  /**
   * @return the cropUnitCodeDescription
   */
  public String getCropUnitCodeDescription() {
    return cropUnitCodeDescription;
  }

  /**
   * @param cropUnitCodeDescription the cropUnitCodeDescription to set the value to
   */
  public void setCropUnitCodeDescription(String cropUnitCodeDescription) {
    this.cropUnitCodeDescription = cropUnitCodeDescription;
  }

  /**
   * @return the errorPriceEnd
   */
  public boolean isErrorPriceEnd() {
    return errorPriceEnd;
  }

  /**
   * @param errorPriceEnd the errorPriceEnd to set the value to
   */
  public void setErrorPriceEnd(boolean errorPriceEnd) {
    this.errorPriceEnd = errorPriceEnd;
  }

  /**
   * @return the errorPriceStart
   */
  public boolean isErrorPriceStart() {
    return errorPriceStart;
  }

  /**
   * @param errorPriceStart the errorPriceStart to set the value to
   */
  public void setErrorPriceStart(boolean errorPriceStart) {
    this.errorPriceStart = errorPriceStart;
  }

  /**
   * @return the errorQuantityEnd
   */
  public boolean isErrorQuantityEnd() {
    return errorQuantityEnd;
  }

  /**
   * @param errorQuantityEnd the errorQuantityEnd to set the value to
   */
  public void setErrorQuantityEnd(boolean errorQuantityEnd) {
    this.errorQuantityEnd = errorQuantityEnd;
  }

  /**
   * @return the errorQuantityProduced
   */
  public boolean isErrorQuantityProduced() {
    return errorQuantityProduced;
  }

  /**
   * @param errorQuantityProduced the errorQuantityProduced to set the value to
   */
  public void setErrorQuantityProduced(boolean errorQuantityProduced) {
    this.errorQuantityProduced = errorQuantityProduced;
  }

  /**
   * @return the errorUnits
   */
  public boolean isErrorUnits() {
    return errorUnits;
  }

  /**
   * @param errorUnits the errorUnits to set the value to
   */
  public void setErrorUnits(boolean errorUnits) {
    this.errorUnits = errorUnits;
  }

  /**
   * @return the errorQuantityStart
   */
  public boolean isErrorQuantityStart() {
    return errorQuantityStart;
  }

  /**
   * @param errorQuantityStart the errorQuantityStart to set the value to
   */
  public void setErrorQuantityStart(boolean errorQuantityStart) {
    this.errorQuantityStart = errorQuantityStart;
  }

  /**
   * @return the fmvEnd
   */
  public Double getFmvEnd() {
    return fmvEnd;
  }

  /**
   * @param fmvEnd the fmvEnd to set the value to
   */
  public void setFmvEnd(Double fmvEnd) {
    this.fmvEnd = fmvEnd;
  }

  /**
   * @return the fmvStart
   */
  public Double getFmvStart() {
    return fmvStart;
  }

  /**
   * @param fmvStart the fmvStart to set the value to
   */
  public void setFmvStart(Double fmvStart) {
    this.fmvStart = fmvStart;
  }

  /**
   * @return the fmvPreviousYearEnd
   */
  public Double getFmvPreviousYearEnd() {
    return fmvPreviousYearEnd;
  }

  /**
   * @param fmvPreviousYearEnd the fmvPreviousYearEnd to set
   */
  public void setFmvPreviousYearEnd(Double fmvPreviousYearEnd) {
    this.fmvPreviousYearEnd = fmvPreviousYearEnd;
  }

  /**
   * @return the fmvVariance
   */
  public String getFmvVariance() {
    return fmvVariance;
  }

  /**
   * @param fmvVariance the fmvVariance to set the value to
   */
  public void setFmvVariance(String fmvVariance) {
    this.fmvVariance = fmvVariance;
  }

  /**
   * @return the priceEndOutsideFmvVariance
   */
  public boolean isPriceEndOutsideFmvVariance() {
    return priceEndOutsideFmvVariance;
  }

  /**
   * @param priceEndOutsideFmvVariance the priceEndOutsideFmvVariance to set the value to
   */
  public void setPriceEndOutsideFmvVariance(boolean priceEndOutsideFmvVariance) {
    this.priceEndOutsideFmvVariance = priceEndOutsideFmvVariance;
  }

  /**
   * @return the priceStartOutsideFmvVariance
   */
  public boolean isPriceStartOutsideFmvVariance() {
    return priceStartOutsideFmvVariance;
  }

  /**
   * @param priceStartOutsideFmvVariance the priceStartOutsideFmvVariance to set the value to
   */
  public void setPriceStartOutsideFmvVariance(boolean priceStartOutsideFmvVariance) {
    this.priceStartOutsideFmvVariance = priceStartOutsideFmvVariance;
  }

  /**
   * @return the reportedPriceStartOutsideFmvVariance
   */
  public boolean isReportedPriceStartOutsideFmvVariance() {
    return reportedPriceStartOutsideFmvVariance;
  }

  /**
   * @param reportedPriceStartOutsideFmvVariance the reportedPriceStartOutsideFmvVariance to set
   */
  public void setReportedPriceStartOutsideFmvVariance(boolean reportedPriceStartOutsideFmvVariance) {
    this.reportedPriceStartOutsideFmvVariance = reportedPriceStartOutsideFmvVariance;
  }

  /**
   * @return the reportedPriceEndOutsideFmvVariance
   */
  public boolean isReportedPriceEndOutsideFmvVariance() {
    return reportedPriceEndOutsideFmvVariance;
  }

  /**
   * @param reportedPriceEndOutsideFmvVariance the reportedPriceEndOutsideFmvVariance to set
   */
  public void setReportedPriceEndOutsideFmvVariance(boolean reportedPriceEndOutsideFmvVariance) {
    this.reportedPriceEndOutsideFmvVariance = reportedPriceEndOutsideFmvVariance;
  }

  /**
   * @return the marketCommodity
   */
  public Boolean getMarketCommodity() {
    return marketCommodity;
  }

  /**
   * @param marketCommodity the marketCommodity to set the value to
   */
  public void setMarketCommodity(Boolean marketCommodity) {
    this.marketCommodity = marketCommodity;
  }

  /**
   * Gets onFarmAcres
   *
   * @return the onFarmAcres
   */
  public String getOnFarmAcres() {
    return onFarmAcres;
  }

  /**
   * Sets onFarmAcres
   *
   * @param pOnFarmAcres the onFarmAcres to set
   */
  public void setOnFarmAcres(String pOnFarmAcres) {
    onFarmAcres = pOnFarmAcres;
  }

  /**
   * @return String
   */
  public String getUnseedableAcres() {
    return unseedableAcres;
  }

  /**
   * @param unseedableAcres unseedableAcres
   */
  public void setUnseedableAcres(String unseedableAcres) {
    this.unseedableAcres = unseedableAcres;
  }

  /**
   * Gets endYearProducerPrice
   *
   * @return the endYearProducerPrice
   */
  public Double getEndYearProducerPrice() {
    return endYearProducerPrice;
  }

  /**
   * Sets endYearProducerPrice
   *
   * @param endYearProducerPrice the endYearProducerPrice to set
   */
  public void setEndYearProducerPrice(Double endYearProducerPrice) {
    this.endYearProducerPrice = endYearProducerPrice;
  }

  /**
   * @param item ProducedItem
   * @return String
   */
  public static String getWholeFarmLineKey(ProducedItem item) {
    // prevent javascript error caused by code -1 - Unknown
    String inventoryItemCode = item.getInventoryItemCode().replace('-', 'm');
    String inventoryClassCode = item.getInventoryClassCode();
    
    String cropUnitCode = null;
    if(item.getInventoryClassCode().equals(InventoryClassCodes.CROP)) {
      CropItem cropItem = (CropItem) item;
      cropUnitCode = cropItem.getCropUnitCode();
    }
    
    String priceStart = null;
    if(item.getTotalPriceStart() != null) {
      Double ps = MathUtils.round(item.getTotalPriceStart(), 2);
      // prevent javascript error caused by decimal point
      priceStart = ps.toString().replace('.', 'd').replace('-', 'm');
    }
    
    String priceEnd = null;
    if(item.getTotalPriceEnd() != null) {
      Double ps = MathUtils.round(item.getTotalPriceEnd(), 2);
      priceEnd = ps.toString().replace('.', 'd').replace('-', 'm');
    }

    StringBuffer key = new StringBuffer();
    key.append(getItemType(inventoryClassCode));
    key.append("_");
    key.append(inventoryItemCode);
    key.append("_");
    key.append(cropUnitCode);
    key.append("_");
    key.append(priceStart);
    key.append("_");
    key.append(priceEnd);

    return key.toString();
  }
  
  /**
   * @param item ProducedItem
   * @return String
   */
  public static String getLineKey(ProducedItem item) {
    String inventoryItemCode = item.getInventoryItemCode();
    String inventoryClassCode = item.getInventoryClassCode();

    String cropUnitCode = null;
    if(item.getInventoryClassCode().equals(InventoryClassCodes.CROP)) {
      CropItem cropItem = (CropItem) item;
      cropUnitCode = cropItem.getCropUnitCode();
    }

    Integer reportedId = item.getReportedInventoryId();
    Integer adjustmentId = item.getAdjInventoryId();
    String id = "";
    if(reportedId != null) {
      id = "_" + reportedId.toString();
    } else if(adjustmentId != null) {
      id = "_" + adjustmentId.toString();
    }
    String lineKey =
      getItemType(inventoryClassCode) + "_" +
      // fix javascript error caused by code -1 - Unknown
      inventoryItemCode.replace('-', 'm')
      + "_" + cropUnitCode + id;
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
  public boolean isShowAdjToolTipQuantityProduced() {
    boolean result = false;
    if(isShowAdjToolTip() && ! StringUtils.equal(reportedQuantityProduced, totalQuantityProduced)) {
      result = true;
    }
    return result;
  }
  
  /**
   * @return boolean
   */
  public boolean isShowAdjToolTipQuantityStart() {
    boolean result = false;
    if(isShowAdjToolTip() && ! StringUtils.equal(reportedQuantityStart, totalQuantityStart)) {
      result = true;
    }
    return result;
  }
  
  /**
   * @return boolean
   */
  public boolean isShowAdjToolTipPriceStart() {
    boolean result = false;
    if(isShowAdjToolTip() && ! StringUtils.equal(reportedPriceStart, totalPriceStart)) {
      result = true;
    }
    return result;
  }
  
  /**
   * @return boolean
   */
  public boolean isShowAdjToolTipQuantityEnd() {
    boolean result = false;
    if(isShowAdjToolTip() && ! StringUtils.equal(reportedQuantityEnd, totalQuantityEnd)) {
      result = true;
    }
    return result;
  }
  
  /**
   * @return boolean
   */
  public boolean isShowAdjToolTipPriceEnd() {
    boolean result = false;
    if(isShowAdjToolTip() && ! StringUtils.equal(reportedPriceEnd, totalPriceEnd)) {
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
  public static String getCropType() {
    return getItemType(InventoryClassCodes.CROP);
  }
  
  /**
   * @return String
   */
  public static String getLivestockType() {
    return getItemType(InventoryClassCodes.LIVESTOCK);
  }

}
