/**
 *
 * Copyright (c) 2010,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import ca.bc.gov.srm.farm.domain.codes.CommodityTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;


/**
 * InventoryItem information contains production details for
 * agricultural commodities, including Production Unit (i.e. quantity and values
 * of livestock and crops) as well as accrual data.
 * InventoryItem data originates from the federal data imports
 * (collected via "harmonized forms"). InventoryItem is
 * associated with each FarmingOperation however ReportedIncomeExpense data must
 * also exist for each FarmingOperation (see Requirements).
 * InventoryItem data may have associated Form Data Adjustments.
 * 
 * This class represents the entries for both reported (federal data imports)
 * and adjustment values (fields are prefixed with "reported" and "adj"
 * respectively). Each adjustable value also has a getter for the total value
 * (sum of reported and adjustment) prefixed with "getTotal".  
 *
 * @author awilkinson
 * @created Nov 12, 2010
 */
public abstract class InventoryItem implements Serializable {
  
  private static final long serialVersionUID = -133905398649631852L;
  
  public static final double MINIMUM_QUANTITY = 0.001;

  /** back-reference to the object containing this */
  @JsonBackReference
  private FarmingOperation farmingOperation;

  /**
   * InventoryId is a surrogate unique identifier for
   * InventoryItem.
   */
  private Integer reportedInventoryId;
  private Integer adjInventoryId;

  /** quantityStart is the start of year quantity of inventory. */
  private Double reportedQuantityStart;
  private Double adjQuantityStart;

  /** quantityEnd is the end of year quantity of inventory. */
  private Double reportedQuantityEnd;
  private Double adjQuantityEnd;

  /**
   * startOfYearAmount is the end of year dollar amount for Purchased Inputs,
   * Deferred Income/receivables or Accounts Payable.
   */
  private Double reportedStartOfYearAmount;
  private Double adjStartOfYearAmount;

  /**
   * endOfYearAmount is the end of year dollar amount for Purchased Inputs,
   * Deferred Income/receivables or Accounts Payable.
   */
  private Double reportedEndOfYearAmount;
  private Double adjEndOfYearAmount;

  /**
   * priceStart is the opening price used when calculating the benefit for the
   * reference year.
   */
  private Double reportedPriceStart;
  private Double adjPriceStart;

  /**
   * priceEnd is the End of Year (P2) inventory price reported by the
   * participant.
   */
  private Double reportedPriceEnd;
  private Double adjPriceEnd;

  /**
   * endYearProducerPrice is the price supplied by the participant for the end
   * of the year.
   */
  private Double reportedEndYearProducerPrice;
  private Double adjEndYearProducerPrice;

  /**
   * adjIsAcceptProducerPrice indicates if the P2 Producer price was used, even if
   * it was outside FMV bands for the reference year.
   */
  private Boolean reportedIsAcceptProducerPrice;
  private Boolean adjIsAcceptProducerPrice;

  /**
   * aarmReferenceP1Price identifies the start of year prices for 2007 payment
   * processing. When processing 2007 payments, the start of year prices for
   * each reference year could be manipulated for AARM purposes, to adjust the
   * calculated margin for that year. This would affect processing of the
   * ProgramYear (2007), but not affect processing of the year being adjusted.
   * This field will only be populated if the start of year price has been
   * over-ridden. Overides the start year price when using this year as a
   * reference margin.
   */
  private Double reportedAarmReferenceP1Price;
  private Double adjAarmReferenceP1Price;

  /**
   * aarmReferenceP2Price identifies the start of year prices for 2007 payment
   * processing. When processing 2007 payments, the start of year prices for
   * each reference year could be manipulated for AARM purposes, to adjust the
   * calculated margin for that year. This would affect processing of the
   * ProgramYear (2007), but not affect processing of the year being adjusted.
   * This field will only be populated if the start of year price has been
   * over-ridden. Overides the end year price when using this year as a
   * reference margin.
   */
  private Double reportedAarmReferenceP2Price;
  private Double adjAarmReferenceP2Price;

  /**
   * revisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   */
  private Integer revisionCount;
  
  /** The user who made the adjustment */
  private String adjustedByUserId;

  private Integer commodityXrefId;

  /**
   * inventoryClassCode is a unique code for the object inventoryClassCode
   * described as a numeric code used to uniquely identify an inventory type.
   * Examples of codes and descriptions are 1 - Crops Inventory, 2 - Livestock
   * Inventory, 3 - Purchased Inputs, 4 - Deferred Income and Receivables, 5 -
   * Accounts Payables.
   */
  private String inventoryClassCode;

  /** Description for inventoryClassCode. */
  private String inventoryClassCodeDescription;

  /**
   * inventoryGroupCode is a unique code for the object inventoryGroupCode
   * described as a numeric code used to uniquely identify an inventory item
   * group. Examples of codes and descriptions are 1 - Apples, 10 - Dry Beans,
   * 11 - Edible Horticulture, 12 - Fadabeans, 13 - Field Peas.
   */
  private String inventoryGroupCode;

  /** Description for inventoryGroupCode. */
  private String inventoryGroupCodeDescription;

  /**
   * inventoryItemCode is a unique code for the object inventoryItemCode
   * described as a numeric code used to uniquely identify an inventory item.
   * Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3
   * - Barley (seed), 4 - Beans (Dry Edible).
   */
  private String inventoryItemCode;

  /** Description for inventoryItemCode. */
  private String inventoryItemCodeDescription;
  
  private String rollupInventoryItemCode;
  private String rollupInventoryItemCodeDescription;
  
  /** A unique code that identifies the type of fruit or vegetable this item is */
  private String fruitVegTypeCode;
  private String fruitVegTypeCodeDescription;
  
  /** A unique code that identifies the type of commodity this item is */
  private String commodityTypeCode;
  private String commodityTypeCodeDescription;
  
  /** A unique code that identifies the type of multistage commodity this item is,
   * for commodities that have separate INVENTORY ITEM CODEs at different stages of their development/growth. */
  private String multiStageCommodityCode;
  private String multiStageCommodityCodeDescription;

  private Boolean isEligible;
  
  /** The variance limit on the total revenue of a FRUIT VEG TYPE CODE
   *  when comparing it to the expected total revenue. */
  private Double revenueVarianceLimit;

  private Integer lineItem;
  
  private String lineItemDescription;

  /**
   * reportedInventoryId is a surrogate unique identifier for 
   * a reported inventory entry.
   *
   * @return  Integer
   */
  public Integer getReportedInventoryId() {
    return reportedInventoryId;
  }

  /**
   * reportedInventoryId is a surrogate unique identifier for 
   * a reported inventory entry.
   *
   * @param  newVal  The new value for this property
   */
  public void setReportedInventoryId(final Integer newVal) {
    reportedInventoryId = newVal;
  }

  /**
   * adjInventoryId is a surrogate unique identifier for 
   * a adjustment inventory entry.
   *
   * @return the adjInventoryId
   */
  public Integer getAdjInventoryId() {
    return adjInventoryId;
  }

  /**
   * adjInventoryId is a surrogate unique identifier for 
   * a adjustment inventory entry.
   *
   * @param adjInventoryId the adjInventoryId to set
   */
  public void setAdjInventoryId(Integer adjInventoryId) {
    this.adjInventoryId = adjInventoryId;
  }

  /**
   * QuantityEnd is the end of year quantity of inventory.
   * This is the value reported by the farmer.
   *
   * @return  Double
   */
  public Double getReportedQuantityEnd() {
    return reportedQuantityEnd;
  }

  /**
   * QuantityEnd is the end of year quantity of inventory.
   * This is the value reported by the farmer.
   *
   * @param  newVal  The new value for this property
   */
  public void setReportedQuantityEnd(final Double newVal) {
    reportedQuantityEnd = newVal;
  }

  /**
   * QuantityEnd is the end of year quantity of inventory.
   * This is the adjustment value.
   *
   * @return  Double
   */
  public Double getAdjQuantityEnd() {
    return adjQuantityEnd;
  }

  /**
   * QuantityEnd is the end of year quantity of inventory.
   * This is the adjustment value.
   *
   * @param adjQuantityEnd the adjQuantityEnd to set
   */
  public void setAdjQuantityEnd(Double adjQuantityEnd) {
    this.adjQuantityEnd = adjQuantityEnd;
  }

  /**
   * QuantityEnd is the end of year quantity of inventory.
   * This is the total value.
   *
   * @return  Double
   */
  @JsonIgnore
  public Double getTotalQuantityEnd() {
    return calculateTotal(reportedQuantityEnd, adjQuantityEnd);
  }

  /**
   * QuantityStart is the start of year quantity of inventory.
   *
   * @return the reportedQuantityStart
   */
  public Double getReportedQuantityStart() {
    return reportedQuantityStart;
  }

  /**
   * QuantityStart is the start of year quantity of inventory.
   *
   * @param reportedQuantityStart the reportedQuantityStart to set
   */
  public void setReportedQuantityStart(Double reportedQuantityStart) {
    this.reportedQuantityStart = reportedQuantityStart;
  }

  /**
   * QuantityStart is the start of year quantity of inventory.
   *
   * @return  Double
   */
  public Double getAdjQuantityStart() {
    return adjQuantityStart;
  }

  /**
   * QuantityStart is the start of year quantity of inventory.
   *
   * @param  newVal  The new value for this property
   */
  public void setAdjQuantityStart(final Double newVal) {
    adjQuantityStart = newVal;
  }

  /**
   * QuantityStart is the start of year quantity of inventory.
   * 
   * @return Double
   */
  @JsonIgnore
  public Double getTotalQuantityStart() {
    return calculateTotal(reportedQuantityStart, adjQuantityStart);
  }

  /**
   * IsAcceptProducerPrice indicates if the P2 Producer price was used, even if
   * it was outside FMV bands for the reference year.
   *
   * @return  Boolean
   */
  public Boolean getReportedIsAcceptProducerPrice() {
    return reportedIsAcceptProducerPrice;
  }
  
  /**
   * IsAcceptProducerPrice indicates if the P2 Producer price was used, even if
   * it was outside FMV bands for the reference year.
   *
   * @param reportedIsAcceptProducerPrice the reportedIsAcceptProducerPrice to set
   */
  public void setReportedIsAcceptProducerPrice(Boolean reportedIsAcceptProducerPrice) {
    this.reportedIsAcceptProducerPrice = reportedIsAcceptProducerPrice;
  }

  /**
   * IsAcceptProducerPrice indicates if the P2 Producer price was used, even if
   * it was outside FMV bands for the reference year.
   *
   * @return  Boolean
   */
  public Boolean getAdjIsAcceptProducerPrice() {
    return adjIsAcceptProducerPrice;
  }

  /**
   * IsAcceptProducerPrice indicates if the P2 Producer price was used, even if
   * it was outside FMV bands for the reference year.
   *
   * @param  newVal  The new value for this property
   */
  public void setAdjIsAcceptProducerPrice(final Boolean newVal) {
    adjIsAcceptProducerPrice = newVal;
  }

  /**
   * AarmReferenceP1Price identifies the start of year prices for 2007 payment
   * processing. When processing 2007 payments, the start of year prices for
   * each reference year could be manipulated for AARM purposes, to adjust the
   * calculated margin for that year. This would affect processing of the
   * ProgramYear (2007), but not affect processing of the year being adjusted.
   * This field will only be populated if the start of year price has been
   * over-ridden. Overides the start year price when using this year as a
   * reference margin.
   * 
   * @return the reportedAarmReferenceP1Price
   */
  public Double getReportedAarmReferenceP1Price() {
    return reportedAarmReferenceP1Price;
  }

  /**
   * AarmReferenceP1Price identifies the start of year prices for 2007 payment
   * processing. When processing 2007 payments, the start of year prices for
   * each reference year could be manipulated for AARM purposes, to adjust the
   * calculated margin for that year. This would affect processing of the
   * ProgramYear (2007), but not affect processing of the year being adjusted.
   * This field will only be populated if the start of year price has been
   * over-ridden. Overides the start year price when using this year as a
   * reference margin.
   * 
   * @param reportedAarmReferenceP1Price the reportedAarmReferenceP1Price to set
   */
  public void setReportedAarmReferenceP1Price(Double reportedAarmReferenceP1Price) {
    this.reportedAarmReferenceP1Price = reportedAarmReferenceP1Price;
  }

  /**
   * AarmReferenceP1Price identifies the start of year prices for 2007 payment
   * processing. When processing 2007 payments, the start of year prices for
   * each reference year could be manipulated for AARM purposes, to adjust the
   * calculated margin for that year. This would affect processing of the
   * ProgramYear (2007), but not affect processing of the year being adjusted.
   * This field will only be populated if the start of year price has been
   * over-ridden. Overides the start year price when using this year as a
   * reference margin.
   *
   * @return  Double
   */
  public Double getAdjAarmReferenceP1Price() {
    return adjAarmReferenceP1Price;
  }

  /**
   * AarmReferenceP1Price identifies the start of year prices for 2007 payment
   * processing. When processing 2007 payments, the start of year prices for
   * each reference year could be manipulated for AARM purposes, to adjust the
   * calculated margin for that year. This would affect processing of the
   * ProgramYear (2007), but not affect processing of the year being adjusted.
   * This field will only be populated if the start of year price has been
   * over-ridden. Overides the start year price when using this year as a
   * reference margin.
   *
   * @param  newVal  The new value for this property
   */
  public void setAdjAarmReferenceP1Price(final Double newVal) {
    adjAarmReferenceP1Price = newVal;
  }

  /**
   * AarmReferenceP2Price identifies the start of year prices for 2007 payment
   * processing. When processing 2007 payments, the start of year prices for
   * each reference year could be manipulated for AARM purposes, to adjust the
   * calculated margin for that year. This would affect processing of the
   * ProgramYear (2007), but not affect processing of the year being adjusted.
   * This field will only be populated if the start of year price has been
   * over-ridden. Overides the end year price when using this year as a
   * reference margin.
   *
   * @return  Double
   */
  public Double getAdjAarmReferenceP2Price() {
    return adjAarmReferenceP2Price;
  }

  /**
   * AarmReferenceP2Price identifies the start of year prices for 2007 payment
   * processing. When processing 2007 payments, the start of year prices for
   * each reference year could be manipulated for AARM purposes, to adjust the
   * calculated margin for that year. This would affect processing of the
   * ProgramYear (2007), but not affect processing of the year being adjusted.
   * This field will only be populated if the start of year price has been
   * over-ridden. Overides the end year price when using this year as a
   * reference margin.
   *
   * @param  newVal  The new value for this property
   */
  public void setAdjAarmReferenceP2Price(final Double newVal) {
    adjAarmReferenceP2Price = newVal;
  }

  /**
   * AarmReferenceP2Price identifies the start of year prices for 2007 payment
   * processing. When processing 2007 payments, the start of year prices for
   * each reference year could be manipulated for AARM purposes, to adjust the
   * calculated margin for that year. This would affect processing of the
   * ProgramYear (2007), but not affect processing of the year being adjusted.
   * This field will only be populated if the start of year price has been
   * over-ridden. Overides the end year price when using this year as a
   * reference margin.
   *
   * @return the reportedAarmReferenceP2Price
   */
  public Double getReportedAarmReferenceP2Price() {
    return reportedAarmReferenceP2Price;
  }

  /**
   * AarmReferenceP2Price identifies the start of year prices for 2007 payment
   * processing. When processing 2007 payments, the start of year prices for
   * each reference year could be manipulated for AARM purposes, to adjust the
   * calculated margin for that year. This would affect processing of the
   * ProgramYear (2007), but not affect processing of the year being adjusted.
   * This field will only be populated if the start of year price has been
   * over-ridden. Overides the end year price when using this year as a
   * reference margin.
   *
   * @param reportedAarmReferenceP2Price the reportedAarmReferenceP2Price to set
   */
  public void setReportedAarmReferenceP2Price(Double reportedAarmReferenceP2Price) {
    this.reportedAarmReferenceP2Price = reportedAarmReferenceP2Price;
  }

  /**
   * RevisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   *
   * @return  Integer
   */
  public Integer getRevisionCount() {
    return revisionCount;
  }

  /**
   * RevisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   *
   * @param  newVal  The new value for this property
   */
  public void setRevisionCount(final Integer newVal) {
    revisionCount = newVal;
  }

  /**
   * @return  Integer
   */
  public Integer getCommodityXrefId() {
    return commodityXrefId;
  }

  /**
   * @param  newVal  The new value for this property
   */
  public void setCommodityXrefId(final Integer newVal) {
    commodityXrefId = newVal;
  }

  /**
   * InventoryClassCode is a unique code for the object inventoryClassCode
   * described as a numeric code used to uniquely identify an inventory type.
   * Examples of codes and descriptions are 1 - Crops Inventory, 2 - Livestock
   * Inventory, 3 - Purchased Inputs, 4 - Deferred Income and Receivables, 5 -
   * Accounts Payables.
   *
   * @return  String
   */
  public String getInventoryClassCode() {
    return inventoryClassCode;
  }

  /**
   * InventoryClassCode is a unique code for the object inventoryClassCode
   * described as a numeric code used to uniquely identify an inventory type.
   * Examples of codes and descriptions are 1 - Crops Inventory, 2 - Livestock
   * Inventory, 3 - Purchased Inputs, 4 - Deferred Income and Receivables, 5 -
   * Accounts Payables.
   *
   * @param  newVal  The new value for this property
   */
  public void setInventoryClassCode(final String newVal) {
    inventoryClassCode = newVal;
  }

  /**
   * Description for inventoryClassCode.
   *
   * @return  String
   */
  public String getInventoryClassCodeDescription() {
    return inventoryClassCodeDescription;
  }

  /**
   * Description for inventoryClassCode.
   *
   * @param  newVal  The new value for this property
   */
  public void setInventoryClassCodeDescription(final String newVal) {
    inventoryClassCodeDescription = newVal;
  }

  /**
   * InventoryGroupCode is a unique code for the object inventoryGroupCode
   * described as a numeric code used to uniquely identify an inventory item
   * group. Examples of codes and descriptions are 1 - Apples, 10 - Dry Beans,
   * 11 - Edible Horticulture, 12 - Fadabeans, 13 - Field Peas.
   *
   * @return  String
   */
  public String getInventoryGroupCode() {
    return inventoryGroupCode;
  }

  /**
   * InventoryGroupCode is a unique code for the object inventoryGroupCode
   * described as a numeric code used to uniquely identify an inventory item
   * group. Examples of codes and descriptions are 1 - Apples, 10 - Dry Beans,
   * 11 - Edible Horticulture, 12 - Fadabeans, 13 - Field Peas.
   *
   * @param  newVal  The new value for this property
   */
  public void setInventoryGroupCode(final String newVal) {
    inventoryGroupCode = newVal;
  }

  /**
   * Description for inventoryGroupCode.
   *
   * @return  String
   */
  public String getInventoryGroupCodeDescription() {
    return inventoryGroupCodeDescription;
  }

  /**
   * Description for inventoryGroupCode.
   *
   * @param  newVal  The new value for this property
   */
  public void setInventoryGroupCodeDescription(final String newVal) {
    inventoryGroupCodeDescription = newVal;
  }

  /**
   * InventoryItemCode is a unique code for the object inventoryItemCode
   * described as a numeric code used to uniquely identify an inventory item.
   * Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3
   * - Barley (seed), 4 - Beans (Dry Edible).
   *
   * @return  String
   */
  public String getInventoryItemCode() {
    return inventoryItemCode;
  }

  /**
   * InventoryItemCode is a unique code for the object inventoryItemCode
   * described as a numeric code used to uniquely identify an inventory item.
   * Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3
   * - Barley (seed), 4 - Beans (Dry Edible).
   *
   * @param  newVal  The new value for this property
   */
  public void setInventoryItemCode(final String newVal) {
    inventoryItemCode = newVal;
  }

  /**
   * Description for inventoryItemCode.
   *
   * @return  String
   */
  public String getInventoryItemCodeDescription() {
    return inventoryItemCodeDescription;
  }

  /**
   * Description for inventoryItemCode.
   *
   * @param  newVal  The new value for this property
   */
  public void setInventoryItemCodeDescription(final String newVal) {
    inventoryItemCodeDescription = newVal;
  }


  /**
   * Gets startOfYearAmount
   *
   * @return the startOfYearAmount
   */
  public Double getReportedStartOfYearAmount() {
    return reportedStartOfYearAmount;
  }

  /**
   * Sets startOfYearAmount
   *
   * @param pStartOfYearAmount the startOfYearAmount to set
   */
  public void setReportedStartOfYearAmount(Double pStartOfYearAmount) {
    reportedStartOfYearAmount = pStartOfYearAmount;
  }

  /**
   * @return the adjStartOfYearAmount
   */
  public Double getAdjStartOfYearAmount() {
    return adjStartOfYearAmount;
  }

  /**
   * @param adjStartOfYearAmount the adjStartOfYearAmount to set
   */
  public void setAdjStartOfYearAmount(Double adjStartOfYearAmount) {
    this.adjStartOfYearAmount = adjStartOfYearAmount;
  }
  
  /**
   * @return Double
   */
  @JsonIgnore
  public Double getTotalStartOfYearAmount() {
    return calculateTotal(reportedStartOfYearAmount, adjStartOfYearAmount);
  }

  /**
   * EndOfYearAmount is the end of year dollar amount for Purchased Inputs,
   * Deferred Income/receivables or Accounts Payable.
   *
   * @return  Double
   */
  public Double getReportedEndOfYearAmount() {
    return reportedEndOfYearAmount;
  }

  /**
   * EndOfYearAmount is the end of year dollar amount for Purchased Inputs,
   * Deferred Income/receivables or Accounts Payable.
   *
   * @param  newVal  The new value for this property
   */
  public void setReportedEndOfYearAmount(final Double newVal) {
    reportedEndOfYearAmount = newVal;
  }

  /**
   * EndOfYearAmount is the end of year dollar amount for Purchased Inputs,
   * Deferred Income/receivables or Accounts Payable.
   *
   * @return the adjEndOfYearAmount
   */
  public Double getAdjEndOfYearAmount() {
    return adjEndOfYearAmount;
  }

  /**
   * EndOfYearAmount is the end of year dollar amount for Purchased Inputs,
   * Deferred Income/receivables or Accounts Payable.
   *
   * @param adjEndOfYearAmount the adjEndOfYearAmount to set
   */
  public void setAdjEndOfYearAmount(Double adjEndOfYearAmount) {
    this.adjEndOfYearAmount = adjEndOfYearAmount;
  }

  /**
   * EndOfYearAmount is the end of year dollar amount for Purchased Inputs,
   * Deferred Income/receivables or Accounts Payable.
   * 
   * @return Double
   */
  @JsonIgnore
  public Double getTotalEndOfYearAmount() {
    return calculateTotal(reportedEndOfYearAmount, adjEndOfYearAmount);
  }

  /**
   * PriceEnd is the End of Year (P2) inventory price reported by the
   * participant.
   *
   * @return the reportedPriceEnd
   */
  public Double getReportedPriceEnd() {
    return reportedPriceEnd;
  }

  /**
   * PriceEnd is the End of Year (P2) inventory price reported by the
   * participant.
   *
   * @param reportedPriceEnd the reportedPriceEnd to set
   */
  public void setReportedPriceEnd(Double reportedPriceEnd) {
    this.reportedPriceEnd = reportedPriceEnd;
  }

  /**
   * PriceStart is the opening price used when calculating the benefit for the
   * reference year.
   *
   * @return the reportedPriceStart
   */
  public Double getReportedPriceStart() {
    return reportedPriceStart;
  }

  /**
   * PriceStart is the opening price used when calculating the benefit for the
   * reference year.
   *
   * @param reportedPriceStart the reportedPriceStart to set
   */
  public void setReportedPriceStart(Double reportedPriceStart) {
    this.reportedPriceStart = reportedPriceStart;
  }

  /**
   * PriceStart is the opening price used when calculating the benefit for the
   * reference year.
   *
   * @return  Double
   */
  public Double getAdjPriceStart() {
    return adjPriceStart;
  }

  /**
   * PriceStart is the opening price used when calculating the benefit for the
   * reference year.
   *
   * @param  newVal  The new value for this property
   */
  public void setAdjPriceStart(final Double newVal) {
    adjPriceStart = newVal;
  }

  /**
   * PriceStart is the opening price used when calculating the benefit for the
   * reference year.
   * 
   * @return Double
   */
  @JsonIgnore
  public Double getTotalPriceStart() {
    return calculateTotal(reportedPriceStart, adjPriceStart);
  }

  /**
   * PriceEnd is the End of Year (P2) inventory price reported by the
   * participant.
   *
   * @return  Double
   */
  public Double getAdjPriceEnd() {
    return adjPriceEnd;
  }

  /**
   * PriceEnd is the End of Year (P2) inventory price reported by the
   * participant.
   *
   * @param  newVal  The new value for this property
   */
  public void setAdjPriceEnd(final Double newVal) {
    adjPriceEnd = newVal;
  }

  /**
   * PriceEnd is the End of Year (P2) inventory price reported by the
   * participant.
   * 
   * @return Double
   */
  @JsonIgnore
  public Double getTotalPriceEnd() {
    return calculateTotal(reportedPriceEnd, adjPriceEnd);
  }

  /**
   * EndYearProducerPrice is the price supplied by the participant for the end
   * of the year.
   *
   * @return the reportedEndYearProducerPrice
   */
  public Double getReportedEndYearProducerPrice() {
    return reportedEndYearProducerPrice;
  }

  /**
   * EndYearProducerPrice is the price supplied by the participant for the end
   * of the year.
   *
   * @param reportedEndYearProducerPrice the reportedEndYearProducerPrice to set
   */
  public void setReportedEndYearProducerPrice(Double reportedEndYearProducerPrice) {
    this.reportedEndYearProducerPrice = reportedEndYearProducerPrice;
  }

  /**
   * EndYearProducerPrice is the price supplied by the participant for the end
   * of the year.
   *
   * @return  Double
   */
  public Double getAdjEndYearProducerPrice() {
    return adjEndYearProducerPrice;
  }

  /**
   * EndYearProducerPrice is the price supplied by the participant for the end
   * of the year.
   *
   * @param  newVal  The new value for this property
   */
  public void setAdjEndYearProducerPrice(final Double newVal) {
    adjEndYearProducerPrice = newVal;
  }

  /**
   * @return the farmingOperation
   */
  public FarmingOperation getFarmingOperation() {
    return farmingOperation;
  }

  /**
   * @param farmingOperation the farmingOperation to set the value to
   */
  public void setFarmingOperation(FarmingOperation farmingOperation) {
    this.farmingOperation = farmingOperation;
  }

  /**
   * EndYearProducerPrice is the price supplied by the participant for the end
   * of the year.
   * 
   * @return Double
   */
  @JsonIgnore
  public Double getTotalEndYearProducerPrice() {
    return calculateTotal(reportedEndYearProducerPrice, adjEndYearProducerPrice);
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

  public String getCommodityTypeCode() {
    return commodityTypeCode;
  }

  public void setCommodityTypeCode(String commodityTypeCode) {
    this.commodityTypeCode = commodityTypeCode;
  }

  public String getCommodityTypeCodeDescription() {
    return commodityTypeCodeDescription;
  }

  public void setCommodityTypeCodeDescription(String commodityTypeCodeDescription) {
    this.commodityTypeCodeDescription = commodityTypeCodeDescription;
  }

  @JsonIgnore
  public boolean isGrain() {
    return CommodityTypeCodes.GRAIN.equals(commodityTypeCode);
  }

  @JsonIgnore
  public boolean isForage() {
    return CommodityTypeCodes.FORAGE.equals(commodityTypeCode);
  }

  @JsonIgnore
  public boolean isForageSeed() {
    return CommodityTypeCodes.FORAGE_SEED.equals(commodityTypeCode);
  }

  public String getFruitVegTypeCode() {
    return fruitVegTypeCode;
  }

  public void setFruitVegTypeCode(String fruitVegTypeCode) {
    this.fruitVegTypeCode = fruitVegTypeCode;
  }

  public String getFruitVegTypeCodeDescription() {
    return fruitVegTypeCodeDescription;
  }

  public void setFruitVegTypeCodeDescription(String fruitVegTypeCodeDescription) {
    this.fruitVegTypeCodeDescription = fruitVegTypeCodeDescription;
  }

  public String getMultiStageCommodityCode() {
    return multiStageCommodityCode;
  }

  public void setMultiStageCommodityCode(String multiStageCommodityCode) {
    this.multiStageCommodityCode = multiStageCommodityCode;
  }

  public String getMultiStageCommodityCodeDescription() {
    return multiStageCommodityCodeDescription;
  }

  public void setMultiStageCommodityCodeDescription(String multiStageCommodityCodeDescription) {
    this.multiStageCommodityCodeDescription = multiStageCommodityCodeDescription;
  }

  /**
   * 
   * @param reported the reported value
   * @param adj the adjustment value
   * @return the sume of the reported and adjustment values
   */
  protected Double calculateTotal(Double reported, Double adj) {
    Double result;
    
    if(reported != null && adj != null) {
      result = new Double(reported.doubleValue() + adj.doubleValue());
    } else if(reported != null) {
      result = reported;
    } else if(adj != null) {
      result = adj;
    } else {
      result = null;
    }
    
    return result;
  }
  
  @JsonIgnore
  public boolean isAccrual() {
    return InventoryClassCodes.isAccrual(inventoryClassCode);
  }


  public Double getRevenueVarianceLimit() {
    return revenueVarianceLimit;
  }

  public void setRevenueVarianceLimit(Double pricePerPoundVarianceLimit) {
    this.revenueVarianceLimit = pricePerPoundVarianceLimit;
  }

  public Integer getLineItem() {
    return lineItem;
  }

  public void setLineItem(Integer lineItem) {
    this.lineItem = lineItem;
  }

  public String getLineItemDescription() {
    return lineItemDescription;
  }

  public void setLineItemDescription(String lineItemDescription) {
    this.lineItemDescription = lineItemDescription;
  }

  public String getRollupInventoryItemCode() {
    return rollupInventoryItemCode;
  }

  public void setRollupInventoryItemCode(String rollupInventoryItemCode) {
    this.rollupInventoryItemCode = rollupInventoryItemCode;
  }

  public String getRollupInventoryItemCodeDescription() {
    return rollupInventoryItemCodeDescription;
  }

  public void setRollupInventoryItemCodeDescription(String rollupInventoryItemCodeDescription) {
    this.rollupInventoryItemCodeDescription = rollupInventoryItemCodeDescription;
  }

  /**
   * 
   * @return String
   * @see Object#toString()
   */
  @Override
  public String toString(){
    
    Integer farmingOperationId = null;
    if(farmingOperation != null) {
      farmingOperationId = farmingOperation.getFarmingOperationId();
    }

    return "InventoryItem"+"\n"+
    "\t farmingOperation : "+farmingOperationId+"\n"+
    "\t reportedInventoryId : "+reportedInventoryId+"\n"+
    "\t adjInventoryId : "+adjInventoryId+"\n"+
    "\t reportedQuantityStart : "+reportedQuantityStart+"\n"+
    "\t adjQuantityStart : "+adjQuantityStart+"\n"+
    "\t reportedQuantityEnd : "+reportedQuantityEnd+"\n"+
    "\t adjQuantityEnd : "+adjQuantityEnd+"\n"+
    "\t reportedEndOfYearAmount : "+reportedEndOfYearAmount+"\n"+
    "\t adjEndOfYearAmount : "+adjEndOfYearAmount+"\n"+
    "\t reportedStartOfYearAmount : "+reportedStartOfYearAmount+"\n"+
    "\t adjStartOfYearAmount : "+adjStartOfYearAmount+"\n"+
    "\t reportedPriceStart : "+reportedPriceStart+"\n"+
    "\t adjPriceStart : "+adjPriceStart+"\n"+
    "\t reportedPriceEnd : "+reportedPriceEnd+"\n"+
    "\t adjPriceEnd : "+adjPriceEnd+"\n"+
    "\t adjustedByUserId : "+adjustedByUserId+"\n"+
    "\t reportedEndYearProducerPrice : "+reportedEndYearProducerPrice+"\n"+
    "\t adjEndYearProducerPrice : "+adjEndYearProducerPrice+"\n"+
    "\t reportedIsAcceptProducerPrice : "+reportedIsAcceptProducerPrice+"\n"+
    "\t adjIsAcceptProducerPrice : "+adjIsAcceptProducerPrice+"\n"+
    "\t reportedAarmReferenceP1Price : "+reportedAarmReferenceP1Price+"\n"+
    "\t adjAarmReferenceP1Price : "+adjAarmReferenceP1Price+"\n"+
    "\t reportedAarmReferenceP2Price : "+reportedAarmReferenceP2Price+"\n"+
    "\t adjAarmReferenceP2Price : "+adjAarmReferenceP2Price+"\n"+
    "\t revisionCount : "+revisionCount+"\n"+
    "\t commodityXrefId : "+commodityXrefId+"\n"+
    "\t inventoryClassCode : "+inventoryClassCode+"\n"+
    "\t inventoryClassCodeDescription : "+inventoryClassCodeDescription+"\n"+
    "\t inventoryGroupCode : "+inventoryGroupCode+"\n"+
    "\t inventoryGroupCodeDescription : "+inventoryGroupCodeDescription+"\n"+
    "\t inventoryItemCode : " +inventoryItemCode+"\n"+
    "\t inventoryItemCodeDescription : "+inventoryItemCodeDescription+"\n"+
    "\t rollupInventoryItemCode : "+rollupInventoryItemCode+"\n"+
    "\t rollupInventoryItemCodeDescription : "+rollupInventoryItemCodeDescription+"\n"+
    "\t isEligible : "+isEligible+"\n"+
    "\t commodityTypeCode: "+commodityTypeCode+"\n"+
    "\t lineItem : "+lineItem+"\n"+
    "\t lineItemDescription : "+lineItemDescription+"\n"+
    "\t fruitVegTypeCode : "+fruitVegTypeCode+"\n"+
    "\t fruitVegTypeCodeDescription : "+fruitVegTypeCodeDescription+"\n"+
    "\t multiStageCommodityCode : "+multiStageCommodityCode+"\n"+
    "\t multiStageCommodityCodeDescription : "+multiStageCommodityCodeDescription;
  }
  
}
