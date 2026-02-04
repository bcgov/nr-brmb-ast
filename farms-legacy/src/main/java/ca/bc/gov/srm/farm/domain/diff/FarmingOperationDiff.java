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
package ca.bc.gov.srm.farm.domain.diff;

import java.io.Serializable;
import java.util.List;

/**
 * @author awilkinson
 * @created Mar 16, 2011
 */
public class FarmingOperationDiff implements Serializable {
  
  private static final long serialVersionUID = 5222287435764584379L;

  private Integer operationNumber;
  
  private String schedule; 
  
  private Boolean oldOpExists;

  private Boolean newOpExists;
  
  /** Indicates that the existing data has been updated in this system */
  private Boolean isLocallyUpdated;
  
  /** The list of differing fields. */
  private List<FieldDiff> fieldDiffs;
  
  /** The list of differing income/expenses. */
  private List<IncomeExpenseDiff> incomeExpenseDiffs;
  
  private List<CropDiff> cropDiffs;
  
  private List<LivestockDiff> livestockDiffs;
  
  private List<AccrualDiff> inputDiffs;
  
  private List<AccrualDiff> receivableDiffs;
  
  private List<AccrualDiff> payableDiffs;
  
  private List<ProductiveUnitDiff> productiveUnitDiffs;
  
  private Boolean isHasFieldDifferences;
  
  private Boolean isHasDifferences;

  public List<FieldDiff> getFieldDiffs() {
    return fieldDiffs;
  }

  public void setFieldDiffs(List<FieldDiff> fieldDiffs) {
    this.fieldDiffs = fieldDiffs;
  }

  public List<IncomeExpenseDiff> getIncomeExpenseDiffs() {
    return incomeExpenseDiffs;
  }

  public void setIncomeExpenseDiffs(List<IncomeExpenseDiff> incomeExpenseDiffs) {
    this.incomeExpenseDiffs = incomeExpenseDiffs;
  }

  public List<CropDiff> getCropDiffs() {
    return cropDiffs;
  }

  public void setCropDiffs(List<CropDiff> cropDiffs) {
    this.cropDiffs = cropDiffs;
  }

  public List<LivestockDiff> getLivestockDiffs() {
    return livestockDiffs;
  }

  public void setLivestockDiffs(List<LivestockDiff> livestockDiffs) {
    this.livestockDiffs = livestockDiffs;
  }

  public Boolean getIsLocallyUpdated() {
    return isLocallyUpdated;
  }

  public void setIsLocallyUpdated(Boolean isLocallyUpdated) {
    this.isLocallyUpdated = isLocallyUpdated;
  }

  public Integer getOperationNumber() {
    return operationNumber;
  }

  public void setOperationNumber(Integer operationNumber) {
    this.operationNumber = operationNumber;
  }

  public List<ProductiveUnitDiff> getProductiveUnitDiffs() {
    return productiveUnitDiffs;
  }

  public void setProductiveUnitDiffs(List<ProductiveUnitDiff> productiveUnitDiffs) {
    this.productiveUnitDiffs = productiveUnitDiffs;
  }

  public Boolean getNewOpExists() {
    return newOpExists;
  }

  public void setNewOpExists(Boolean newOpExists) {
    this.newOpExists = newOpExists;
  }

  public Boolean getOldOpExists() {
    return oldOpExists;
  }

  public void setOldOpExists(Boolean oldOpExists) {
    this.oldOpExists = oldOpExists;
  }

  public List<AccrualDiff> getInputDiffs() {
    return inputDiffs;
  }

  public void setInputDiffs(List<AccrualDiff> inputDiffs) {
    this.inputDiffs = inputDiffs;
  }

  public List<AccrualDiff> getPayableDiffs() {
    return payableDiffs;
  }

  public void setPayableDiffs(List<AccrualDiff> payableDiffs) {
    this.payableDiffs = payableDiffs;
  }

  public List<AccrualDiff> getReceivableDiffs() {
    return receivableDiffs;
  }

  public void setReceivableDiffs(List<AccrualDiff> receivableDiffs) {
    this.receivableDiffs = receivableDiffs;
  }

  public String getSchedule() {
    return schedule;
  }

  public void setSchedule(String schedule) {
    this.schedule = schedule;
  }

  public Boolean getIsHasFieldDifferences() {
    return isHasFieldDifferences;
  }

  public void setIsHasFieldDifferences(Boolean isHasFieldDifferences) {
    this.isHasFieldDifferences = isHasFieldDifferences;
  }

  public Boolean getIsHasDifferences() {
    return isHasDifferences;
  }

  public void setIsHasDifferences(Boolean isHasDifferences) {
    this.isHasDifferences = isHasDifferences;
  }

}
