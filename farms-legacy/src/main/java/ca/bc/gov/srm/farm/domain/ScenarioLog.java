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
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * 
 */
public final class ScenarioLog implements Serializable {
  
  private static final long serialVersionUID = 4722869832337960441L;

  /** back-reference to the object containing this */
  @JsonBackReference
  private Scenario scenario;

  private String logMessage;
  private String userId;
  private Date logDate;
  
	/**
	 * @return the commentDate
	 */
	public Date getLogDate() {
		return logDate;
	}
	
	/**
	 * @param logDate the logDate to set
	 */
	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}
	
	/**
	 * @return the logMessage
	 */
	public String getLogMessage() {
		return logMessage;
	}
	
	/**
	 * @param logMessage the logMessage to set
	 */
	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}
	
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

  /**
   * @return the scenario
   */
  public Scenario getScenario() {
    return scenario;
  }

  /**
   * @param scenario the scenario to set the value to
   */
  public void setScenario(Scenario scenario) {
    this.scenario = scenario;
  }

  /**
   * @return The assignedToUserId with IDIR\ stripped off the start.
   */
  @JsonIgnore
  public String getUserIdDisplay() {
    return StringUtils.formatUserIdForDisplay(userId);
  }
}
