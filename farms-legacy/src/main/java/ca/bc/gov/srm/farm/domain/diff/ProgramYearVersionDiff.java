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
public class ProgramYearVersionDiff implements Serializable {
  
  private static final long serialVersionUID = 6582396518103844030L;

  /** The list of differing fields. */
  private List<FieldDiff> fieldDiffs;
  
  /** Indicates that the existing data has been updated in this system */
  private Boolean isLocallyUpdated;

  /** hasLocallyGeneratedOperations identifies if the existing data
   *  has operations that were created by the client. */
  private Boolean hasLocallyGeneratedOperations;
  
  private Boolean isNewCRAMissingOperations;
  
  private Boolean isHasPyvDetailDifferences;
  
  private Boolean isHasDifferences;
  
  /** The list of farming operation comparisons. */
  private List<FarmingOperationDiff> farmingOperationDiffs;

  /**
   * @return the farmingOperationDiffs
   */
  public List<FarmingOperationDiff> getFarmingOperationDiffs() {
    return farmingOperationDiffs;
  }

  /**
   * @param farmingOperationDiffs the farmingOperationDiffs to set the value to
   */
  public void setFarmingOperationDiffs(List<FarmingOperationDiff> farmingOperationDiffs) {
    this.farmingOperationDiffs = farmingOperationDiffs;
  }

  /**
   * @return the fieldDiffs
   */
  public List<FieldDiff> getFieldDiffs() {
    return fieldDiffs;
  }

  /**
   * @param fieldDiffs the fieldDiffs to set the value to
   */
  public void setFieldDiffs(List<FieldDiff> fieldDiffs) {
    this.fieldDiffs = fieldDiffs;
  }

  /**
   * @return the isLocallyUpdated
   */
  public Boolean getIsLocallyUpdated() {
    return isLocallyUpdated;
  }

  /**
   * @param isLocallyUpdated the isLocallyUpdated to set the value to
   */
  public void setIsLocallyUpdated(Boolean isLocallyUpdated) {
    this.isLocallyUpdated = isLocallyUpdated;
  }

  /**
   * Gets hasLocallyGeneratedOperations
   *
   * @return the hasLocallyGeneratedOperations
   */
  public Boolean getHasLocallyGeneratedOperations() {
    return hasLocallyGeneratedOperations;
  }

  /**
   * Sets hasLocallyGeneratedOperations
   *
   * @param hasLocallyGeneratedOperations the hasLocallyGeneratedOperations to set
   */
  public void setHasLocallyGeneratedOperations(Boolean hasLocallyGeneratedOperations) {
    this.hasLocallyGeneratedOperations = hasLocallyGeneratedOperations;
  }

  /**
   * Gets isNewCRAMissingOperations
   *
   * @return the isNewCRAMissingOperations
   */
  public Boolean getIsNewCRAMissingOperations() {
    return isNewCRAMissingOperations;
  }

  /**
   * Sets isNewCRAMissingOperations
   *
   * @param isNewCRAMissingOperations the isNewCRAMissingOperations to set
   */
  public void setIsNewCRAMissingOperations(Boolean isNewCRAMissingOperations) {
    this.isNewCRAMissingOperations = isNewCRAMissingOperations;
  }

  public Boolean getIsHasPyvDetailDifferences() {
    return isHasPyvDetailDifferences;
  }

  public void setIsHasPyvDetailDifferences(Boolean isHasPyvDetailDifferences) {
    this.isHasPyvDetailDifferences = isHasPyvDetailDifferences;
  }

  public Boolean getIsHasDifferences() {
    return isHasDifferences;
  }

  public void setIsHasDifferences(Boolean isHasDifferences) {
    this.isHasDifferences = isHasDifferences;
  }

}
