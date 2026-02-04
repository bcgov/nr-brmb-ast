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
package ca.bc.gov.srm.farm.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ca.bc.gov.srm.farm.util.StringUtils;


/**
 * @author awilkinson
 */
public class CombinedFarmClient implements Serializable {

  private static final long serialVersionUID = 9015489177507415884L;

  private Integer scenarioId;
  
  private Integer scenarioNumber;
  
  private Integer scenarioRevisionCount;
  
  private Double totalBenefit;
  
  /**
   * participantPin (Personal Information Number) is the unique
   * AgriStability/AgriInvest pin for this producuer. Was previous CAIS PIN and
   * NISA PIN.
   */
  private Integer participantPin;

  /** firstName of the contact. */
  private String firstName;

  /** lastName of the contact. */
  private String lastName;

  /** corpName is the Contact Business Name, as provided by the participant. */
  private String corpName;

  /**
   * @return the participantPin
   */
  public Integer getParticipantPin() {
    return participantPin;
  }

  /**
   * @param participantPin the participantPin to set
   */
  public void setParticipantPin(Integer participantPin) {
    this.participantPin = participantPin;
  }

  /**
   * @return the firstName
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * @param firstName the firstName to set
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * @return the lastName
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * @param lastName the lastName to set
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * @return the corpName
   */
  public String getCorpName() {
    return corpName;
  }

  /**
   * @param corpName the corpName to set
   */
  public void setCorpName(String corpName) {
    this.corpName = corpName;
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
   * @return the scenarioNumber
   */
  public Integer getScenarioNumber() {
    return scenarioNumber;
  }

  /**
   * @param scenarioNumber the scenarioNumber to set
   */
  public void setScenarioNumber(Integer scenarioNumber) {
    this.scenarioNumber = scenarioNumber;
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
   * @return the scenarioRevisionCount
   */
  public Integer getScenarioRevisionCount() {
    return scenarioRevisionCount;
  }

  /**
   * @param scenarioRevisionCount the scenarioRevisionCount to set
   */
  public void setScenarioRevisionCount(Integer scenarioRevisionCount) {
    this.scenarioRevisionCount = scenarioRevisionCount;
  }

  /**
   * @return  Rather than having N JSPs format the name, we should just do it
   *          once here. The "Person" table also holds corporations.
   */
  @JsonIgnore
  public String getFullName() {
    String name = corpName;

    if (StringUtils.isBlank(name)) {
      name = firstName + " " + lastName;
    }

    return name;
  }

  /**
   * 
   * @return String
   * @see Object#toString()
   */
  @Override
  public String toString(){

    return "CombinedFarmClient"+"\n"+
    "\t scenario : "+scenarioId+"\n"+
    "\t scenarioNumber : "+scenarioNumber+"\n"+
    "\t participantPin : "+participantPin+"\n"+
    "\t firstName : "+firstName+"\n"+
    "\t lastName : "+lastName+"\n"+
    "\t corpName : "+corpName+"\n"+
    "\t totalBenefit : "+totalBenefit+"\n"+
    "\t scenarioRevisionCount : "+scenarioRevisionCount+"\n";
  }
  
}
