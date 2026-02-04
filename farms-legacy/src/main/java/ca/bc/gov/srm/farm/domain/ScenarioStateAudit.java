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

/**
 * ScenarioStateAudit tracks changes to the state of the scenario.
 *
 * @author   Vivid Solutions Inc.
 * @version  1.0
 * @created  03-Jul-2009 2:06:47 PM
 */
public final class ScenarioStateAudit implements Serializable {
  
  private static final long serialVersionUID = 1L;

  /** back-reference to the object containing this */
  @JsonBackReference
  private Scenario scenario;

  /**
   * scenarioStateAudtId is a surrogate unique identifier for
   * ScenarioStateAudits.
   */
  private Integer scenarioStateAudtId;

  /**
   * scenarioStateCode is a unique code for the object scenarioStateCode
   * described as a character code used to uniquely identify the state of the .
   */
  private String scenarioStateCode;

  /**
   * scenarioStateCodeDesc is a textual description of the ScenarioStateCode.
   */
  private String scenarioStateCodeDesc;

  /**
   * stateChangeReason describes the reason for changing the ScenarioStateCode
   * of the .
   */
  private String stateChangeReason;
  
  /** The user who set this scenario state. */
  private String stateChangedByUserId;
  
  /** The date and time that this scenario state was set. */
  private Date stateChangeTimestamp;

  /**
   * revisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   */
  private Integer revisionCount;


  /** Constructor. */
  public ScenarioStateAudit() {

  }

  /**
   * ScenarioStateAudtId is a surrogate unique identifier for
   * ScenarioStateAudits.
   *
   * @return  Integer
   */
  public Integer getScenarioStateAudtId() {
    return scenarioStateAudtId;
  }

  /**
   * ScenarioStateAudtId is a surrogate unique identifier for
   * ScenarioStateAudits.
   *
   * @param  newVal  The new value for this property
   */
  public void setScenarioStateAudtId(final Integer newVal) {
    scenarioStateAudtId = newVal;
  }

  /**
   * ScenarioStateCode is a unique code for the object scenarioStateCode
   * described as a character code used to uniquely identify the state of the .
   *
   * @return  String
   */
  public String getScenarioStateCode() {
    return scenarioStateCode;
  }

  /**
   * ScenarioStateCode is a unique code for the object scenarioStateCode
   * described as a character code used to uniquely identify the state of the
   * scenario.
   *
   * @param  newVal  The new value for this property
   */
  public void setScenarioStateCode(final String newVal) {
    scenarioStateCode = newVal;
  }

  /**
   * ScenarioStateCodeDesc is a textual description of the ScenarioStateCode.
   *
   * @return  String
   */
  public String getScenarioStateCodeDesc() {
    return scenarioStateCodeDesc;
  }

  /**
   * ScenarioStateCodeDesc is a textual description of the ScenarioStateCode.
   *
   * @param  newVal  The new value for this property
   */
  public void setScenarioStateCodeDesc(final String newVal) {
    scenarioStateCodeDesc = newVal;
  }

  /**
   * StateChangeReason describes the reason for changing the ScenarioStateCode
   * of the scenario.
   *
   * @return  String
   */
  public String getStateChangeReason() {
    return stateChangeReason;
  }

  /**
   * StateChangeReason describes the reason for changing the ScenarioStateCode
   * of the scenario.
   *
   * @param  newVal  The new value for this property
   */
  public void setStateChangeReason(final String newVal) {
    stateChangeReason = newVal;
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
   * @return the stateChangedByUserId
   */
  public String getStateChangedByUserId() {
    return stateChangedByUserId;
  }

  /**
   * @param stateChangedByUserId the stateChangedByUserId to set the value to
   */
  public void setStateChangedByUserId(String stateChangedByUserId) {
    this.stateChangedByUserId = stateChangedByUserId;
  }

  /**
   * @return the stateChangeTimestamp
   */
  public Date getStateChangeTimestamp() {
    return stateChangeTimestamp;
  }

  /**
   * @param stateChangeTimestamp the stateChangeTimestamp to set the value to
   */
  public void setStateChangeTimestamp(Date stateChangeTimestamp) {
    this.stateChangeTimestamp = stateChangeTimestamp;
  }

  /**
   * @return the stateChangedByUserId
   */
  @JsonIgnore
  public String getStateChangedByUserIdDisplay() {
    final int idirLength = 5;
    String result;
    if(stateChangedByUserId == null) {
      result = null;
    } else {
      if(stateChangedByUserId.toUpperCase().startsWith("IDIR\\")
          && stateChangedByUserId.length() > idirLength) {
        result = stateChangedByUserId.substring(idirLength);
      } else {
        result = stateChangedByUserId;
      }
    }
    return result;
  }

  /**
   * 
   * @return String
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString(){
    
    Integer scenarioId = null;
    if(scenario != null) {
      scenarioId = scenario.getScenarioId();
    }

    return "ScenarioStateAudit"+"\n"+
    "\t scenario : " + scenarioId+ "\n" +
    "\t scenarioStateAudtId : "+scenarioStateAudtId+"\n"+
    "\t scenarioStateCode : "+scenarioStateCode+"\n"+
    "\t scenarioStateCodeDesc : "+scenarioStateCodeDesc+"\n"+
    "\t stateChangeReason : "+stateChangeReason+"\n"+
    "\t stateChangedByUserId : "+stateChangedByUserId+"\n"+
    "\t stateChangeTimestamp : "+stateChangeTimestamp+"\n"+
    "\t revisionCount : "+revisionCount;
  }
}
