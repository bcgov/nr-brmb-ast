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
package ca.bc.gov.srm.farm.ui.domain;

import java.util.Date;

import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * @author awilkinson
 * @created Dec 15, 2010
 */
public class CalculatorInboxItem {

  private static final long serialVersionUID = -2645502921365403844L;
  
  private Integer scenarioId;
  
  private Integer pin;
  
  /** The name of the owner. Either corporate name or last, first name */
  private String name;
  
  private String scenarioStateCode;

  private String scenarioStateCodeDescription;

  /** The date the scenario was updated */
  private Date lastChangedDate;
  
  private Double totalBenefit;
  
  private String assignedToUserId;

  private String assignedToUserGuid;
  
  /** The date that the CRA data was imported */
  private Date receivedDate;

  /**
   * @return the assignedToUserId
   */
  public String getAssignedToUserId() {
    return assignedToUserId;
  }

  /**
   * @param assignedToUserId the assignedToUserId to set
   */
  public void setAssignedToUserId(String assignedToUserId) {
    this.assignedToUserId = assignedToUserId;
  }

  /**
   * @return the lastChangedDate
   */
  public Date getLastChangedDate() {
    return lastChangedDate;
  }

  /**
   * @param lastChangedDate the lastChangedDate to set
   */
  public void setLastChangedDate(Date lastChangedDate) {
    this.lastChangedDate = lastChangedDate;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the pin
   */
  public Integer getPin() {
    return pin;
  }

  /**
   * @param pin the pin to set
   */
  public void setPin(Integer pin) {
    this.pin = pin;
  }

  /**
   * @return the receivedDate
   */
  public Date getReceivedDate() {
    return receivedDate;
  }

  /**
   * @param receivedDate the receivedDate to set
   */
  public void setReceivedDate(Date receivedDate) {
    this.receivedDate = receivedDate;
  }

  /**
   * @return the scenarioId
   */
  public Integer getScenarioId() {
    return scenarioId;
  }

  /**
   * @param scenarioId the scenarioId to set
   */
  public void setScenarioId(Integer scenarioId) {
    this.scenarioId = scenarioId;
  }

  /**
   * @return the scenarioStateCode
   */
  public String getScenarioStateCode() {
    return scenarioStateCode;
  }

  /**
   * @param scenarioStateCode the scenarioStateCode to set
   */
  public void setScenarioStateCode(String scenarioStateCode) {
    this.scenarioStateCode = scenarioStateCode;
  }

  /**
   * @return the scenarioStateCodeDescription
   */
  public String getScenarioStateCodeDescription() {
    return scenarioStateCodeDescription;
  }

  /**
   * @param scenarioStateCodeDescription the scenarioStateCodeDescription to set
   */
  public void setScenarioStateCodeDescription(String scenarioStateCodeDescription) {
    this.scenarioStateCodeDescription = scenarioStateCodeDescription;
  }

  /**
   * @return the totalBenefit
   */
  public Double getTotalBenefit() {
    return totalBenefit;
  }

  /**
   * @param totalBenefit the totalBenefit to set
   */
  public void setTotalBenefit(Double totalBenefit) {
    this.totalBenefit = totalBenefit;
  }

  /**
   * @return the assignedToUserGuid
   */
  public String getAssignedToUserGuid() {
    return assignedToUserGuid;
  }

  /**
   * @param assignedToUserGuid the assignedToUserGuid to set
   */
  public void setAssignedToUserGuid(String assignedToUserGuid) {
    this.assignedToUserGuid = assignedToUserGuid;
  }
  
  /**
   * @return String
   */
  public String getAssignedToUserIdEscapedForString() {
    return StringUtils.formatUserIdForDisplay(assignedToUserId);
  }

}
