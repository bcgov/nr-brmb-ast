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
package ca.bc.gov.srm.farm.ui.struts.calculator.productiveunits;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author awilkinson
 * @created Dec 22, 2010
 */
public class ProductiveUnitFormRecord implements Serializable {

  private static final long serialVersionUID = 1L;
  
  private String participantDataSrcCode;
  
  /** Map<year, value> */
  private Map<String, String> adjustedValues = new HashMap<>();

  private Map<String, String> adjustmentValues = new HashMap<>();

  private Map<String, String> craValues = new HashMap<>();
  
  private Map<String, String> adjustmentUsers = new HashMap<>();
  
  /** Map<year, value> */
  private Map<String, Integer> adjustmentIds = new HashMap<>();
  
  private Map<String, Integer> reportedIds = new HashMap<>();
  
  private Map<String, Integer> revisionCounts = new HashMap<>();
  
  private Map<String, Boolean> deletedAdjustments = new HashMap<>();

  
  public Map<String, String> getAdjustedValues() {
    return adjustedValues;
  }

  /**
   * @return the adjustmentValues
   */
  public Map<String, String> getAdjustmentValues() {
    return adjustmentValues;
  }

  /**
   * @return the craValues
   */
  public Map<String, String> getCraValues() {
    return craValues;
  }

  /**
   * @param year String
   * @return String
   */
  public String getAdjusted(String year) {
    return adjustedValues.get(year);
  }
  
  /**
   * @param year String
   * @param value String
   */
  public void setAdjusted(String year, String value) {
    adjustedValues.put(year, value);
  }
  
  /**
   * @param year String
   * @return String
   */
  public String getAdjustment(String year) {
    return adjustmentValues.get(year);
  }
  
  /**
   * @param year String
   * @param value String
   */
  public void setAdjustment(String year, String value) {
    adjustmentValues.put(year, value);
  }
  
  /**
   * @param year String
   * @return String
   */
  public String getCra(String year) {
    return craValues.get(year);
  }
  
  /**
   * @param year String
   * @param value String
   */
  public void setCra(String year, String value) {
    craValues.put(year, value);
  }

  /**
   * @return the adjustmentUsers
   */
  public Map<String, String> getAdjustmentUsers() {
    return adjustmentUsers;
  }
  
  /**
   * @param year String
   * @return boolean
   */
  public boolean isAdjustmentDeleted(String year) {
    Boolean deleted = getDeletedAdjustment(year);
    boolean result;
    if(deleted == null) {
      result = false;
    } else {
      result = deleted.booleanValue();
    }
    return result;
  }
  
  /**
   * @return the errors
   */
  public Map<String, Boolean> getDeletedAdjustments() {
    return deletedAdjustments;
  }
  
  /**
   * @param year String
   * @return Boolean
   */
  public Boolean getDeletedAdjustment(String year) {
    return deletedAdjustments.get(year);
  }
  
  /**
   * @param year String
   * @param value Boolean
   */
  public void setDeletedAdjustment(String year, Boolean value) {
    deletedAdjustments.put(year, value);
  }


  /**
   * @return the adjustmentIds
   */
  public Map<String, Integer> getAdjustmentIds() {
    return adjustmentIds;
  }

  /**
   * @param year String
   * @return String
   */
  public Integer getAdjustmentId(String year) {
    return adjustmentIds.get(year);
  }
  
  /**
   * @param year String
   * @param value Integer
   */
  public void setAdjustmentId(String year, Integer value) {
    adjustmentIds.put(year, value);
  }

  /**
   * @return the reportedIds
   */
  public Map<String, Integer> getReportedIds() {
    return reportedIds;
  }
  
  /**
   * @param year String
   * @return String
   */
  public Integer getReportedId(String year) {
    return reportedIds.get(year);
  }
  
  /**
   * @param year String
   * @param value Integer
   */
  public void setReportedId(String year, Integer value) {
    reportedIds.put(year, value);
  }

  /**
   * @return the revisionCounts
   */
  public Map<String, Integer> getRevisionCounts() {
    return revisionCounts;
  }
  
  /**
   * @param year String
   * @return String
   */
  public Integer getRevisionCount(String year) {
    return revisionCounts.get(year);
  }
  
  /**
   * @param year String
   * @param value Integer
   */
  public void setRevisionCount(String year, Integer value) {
    revisionCounts.put(year, value);
  }

  /**
   * @param year String
   * @param userId String
   */
  public void setAdjustmentUser(String year, String userId) {
    // escape slashes because this string is only used in a javascript string for display
    adjustmentUsers.put(year, userId);
  }

  public String getParticipantDataSrcCode() {
    return participantDataSrcCode;
  }

  public void setParticipantDataSrcCode(String participantDataSrcCode) {
    this.participantDataSrcCode = participantDataSrcCode;
  }

}
