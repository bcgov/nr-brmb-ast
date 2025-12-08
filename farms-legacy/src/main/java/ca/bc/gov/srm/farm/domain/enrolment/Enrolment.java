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
package ca.bc.gov.srm.farm.domain.enrolment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.service.ListServiceConstants;

/**
 * This class represents a client's enrolment status in the
 * Agristability Program for a particular year.
 * 
 * @author awilkinson
 * @created Dec 3, 2010
 */
public class Enrolment implements Serializable {
  
  private static final long serialVersionUID = 5925929752184006372L;

  public static final String REASON_INSUFF_REF_MARGIN_DATA =
    "Insufficient reference margin data for this participant.";
  
  public static final String REASON_ZERO_MARGINS =
    "Must have non-zero margins for the three most recent years.";
  
  public static final String REASON_FOUND_TOO_MANY_MARGINS =
      "Found too many margins.";
  
  public static final String REASON_OVERSIZE_MARGIN =
      "A margin in excess of 100 million dollars was detected.";
  
  public static final String REASON_BPU_SET_INCOMPLETE =
      "The BPU Set is incomplete.";
  
  public static final String BPU_INVENTORY_CODES =
      " Inventory Codes: ";
  
  public static final String BPU_STRUCTURE_GROUP_CODES =
      " Structure Group Codes: ";
  
  public static final String WARNING_MISSING_PRODUCTIVE_CAPACITY =
      "WARNING: one or more years has no productive capacity data.";
  
  public static final String ENROLMENT_SCENARIO_STATE_EN_COMPLETE =
      "EN_COMP";
  
  public static final String ENROLMENT_SCENARIO_STATE_EN_IN_PROGRESS =
      "EN_IP";
  
  /** back-reference to the object containing this */
  @JsonBackReference
  private Scenario scenario;
  
  private Integer clientId;
  
  private Integer pin;

  private Integer enrolmentId;
  
  private Integer enrolmentYear;
  
  private Double enrolmentFee;
  
  private Date generatedDate;
  
  private Date whenUpdated;
  
  private Integer revisionCount;
  
  private String failedReason;
  
  private String scenarioState;
  
  private String producerName;
  
  private Boolean failedToGenerate;
  
  private Double contributionMarginAverage;
  private Double marginYearMinus2;
  private Double marginYearMinus3;
  private Double marginYearMinus4;
  private Double marginYearMinus5;
  private Double marginYearMinus6;

  private Boolean isMarginYearMinus2Used;
  private Boolean isMarginYearMinus3Used;
  private Boolean isMarginYearMinus4Used;
  private Boolean isMarginYearMinus5Used;
  private Boolean isMarginYearMinus6Used;
  
  /** The scenario ID from which the program year margins
   *  were retrieved and the fee was calculated */
  private Integer marginScenarioId;

  private Boolean isGeneratedFromCra;
  private Boolean isGeneratedFromEnw;
  private Boolean isCreateTaskInBarn;
  private Boolean isLateParticipant;
  private Boolean isInCombinedFarm;

  private Double combinedFarmPercent;

  private String sectorCodeDescription;
  private String sectorDetailCodeDescription;
  
  private List<EnrolmentPartner> enrolmentPartners = new ArrayList<>();
  private List<EnrolmentCombinedFarmOwner> combinedFarmOwners = new ArrayList<>();
  
  /**
   * @return the enrolmentFee
   */
  public Double getEnrolmentFee() {
    return enrolmentFee;
  }

  /**
   * @param enrolmentFee the enrolmentFee to set
   */
  public void setEnrolmentFee(Double enrolmentFee) {
    this.enrolmentFee = enrolmentFee;
  }

  /**
   * @return the enrolmentId
   */
  public Integer getEnrolmentId() {
    return enrolmentId;
  }

  /**
   * @param enrolmentId the enrolmentId to set
   */
  public void setEnrolmentId(Integer enrolmentId) {
    this.enrolmentId = enrolmentId;
  }

  /**
   * @return the enrolmentYear
   */
  public Integer getEnrolmentYear() {
    return enrolmentYear;
  }

  /**
   * @param enrolmentYear the enrolmentYear to set
   */
  public void setEnrolmentYear(Integer enrolmentYear) {
    this.enrolmentYear = enrolmentYear;
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
   * @return the revisionCount
   */
  public Integer getRevisionCount() {
    return revisionCount;
  }

  /**
   * @param revisionCount the revisionCount to set
   */
  public void setRevisionCount(Integer revisionCount) {
    this.revisionCount = revisionCount;
  }

  /**
   * Gets failedToGenerate
   *
   * @return the failedToGenerate
   */
  public Boolean getFailedToGenerate() {
    return failedToGenerate;
  }

  /**
   * Sets failedToGenerate
   *
   * @param failedToGenerate the failedToGenerate to set
   */
  public void setFailedToGenerate(Boolean failedToGenerate) {
    this.failedToGenerate = failedToGenerate;
  }

  /**
   * @return the clientId
   */
  public Integer getClientId() {
    return clientId;
  }

  /**
   * @param clientId the clientId to set
   */
  public void setClientId(Integer clientId) {
    this.clientId = clientId;
  }

  /**
   * Gets generatedDate
   *
   * @return the generatedDate
   */
  public Date getGeneratedDate() {
    return generatedDate;
  }

  /**
   * Sets generatedDate
   *
   * @param pGeneratedDate the generatedDate to set
   */
  public void setGeneratedDate(Date pGeneratedDate) {
    generatedDate = pGeneratedDate;
  }


  /**
   * Gets marginScenarioId
   *
   * @return the marginScenarioId
   */
  public Integer getMarginScenarioId() {
    return marginScenarioId;
  }

  /**
   * Sets marginScenarioId
   *
   * @param marginScenarioId the marginScenarioId to set
   */
  public void setMarginScenarioId(Integer marginScenarioId) {
    this.marginScenarioId = marginScenarioId;
  }

  /**
   * Gets failedReason
   *
   * @return the failedReason
   */
  public String getFailedReason() {
    return failedReason;
  }

  /**
   * Sets failedReason
   *
   * @param failedReason the failedReason to set
   */
  public void setFailedReason(String failedReason) {
    this.failedReason = failedReason;
  }

  /**
   * Gets scenarioState
   *
   * @return the scenarioState
   */
  public String getScenarioState() {
    return scenarioState;
  }

  /**
   * Sets scenarioState
   *
   * @param scenarioState the scenarioState to set
   */
  public void setScenarioState(String scenarioState) {
    this.scenarioState = scenarioState;
  }
  
  /**
   * Gets producerName
   *
   * @return the producerName
   */
  public String getProducerName() {
    return producerName;
  }

  /**
   * Sets producerName
   *
   * @param producerName the producerName to set
   */
  public void setProducerName(String producerName) {
    this.producerName = producerName;
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
   * Gets contributionMarginAverage
   *
   * @return the contributionMarginAverage
   */
  public Double getContributionMarginAverage() {
    return contributionMarginAverage;
  }

  /**
   * Sets contributionMarginAverage
   *
   * @param contributionMarginAverage the contributionMarginAverage to set
   */
  public void setContributionMarginAverage(Double contributionMarginAverage) {
    this.contributionMarginAverage = contributionMarginAverage;
  }

  /**
   * Gets marginYearMinus2
   *
   * @return the marginYearMinus2
   */
  public Double getMarginYearMinus2() {
    return marginYearMinus2;
  }

  /**
   * Sets marginYearMinus2
   *
   * @param marginYearMinus2 the marginYearMinus2 to set
   */
  public void setMarginYearMinus2(Double marginYearMinus2) {
    this.marginYearMinus2 = marginYearMinus2;
  }

  /**
   * Gets marginYearMinus3
   *
   * @return the marginYearMinus3
   */
  public Double getMarginYearMinus3() {
    return marginYearMinus3;
  }

  /**
   * Sets marginYearMinus3
   *
   * @param marginYearMinus3 the marginYearMinus3 to set
   */
  public void setMarginYearMinus3(Double marginYearMinus3) {
    this.marginYearMinus3 = marginYearMinus3;
  }

  /**
   * Gets marginYearMinus4
   *
   * @return the marginYearMinus4
   */
  public Double getMarginYearMinus4() {
    return marginYearMinus4;
  }

  /**
   * Sets marginYearMinus4
   *
   * @param marginYearMinus4 the marginYearMinus4 to set
   */
  public void setMarginYearMinus4(Double marginYearMinus4) {
    this.marginYearMinus4 = marginYearMinus4;
  }

  /**
   * Gets marginYearMinus5
   *
   * @return the marginYearMinus5
   */
  public Double getMarginYearMinus5() {
    return marginYearMinus5;
  }

  /**
   * Sets marginYearMinus5
   *
   * @param marginYearMinus5 the marginYearMinus5 to set
   */
  public void setMarginYearMinus5(Double marginYearMinus5) {
    this.marginYearMinus5 = marginYearMinus5;
  }

  /**
   * Gets marginYearMinus6
   *
   * @return the marginYearMinus6
   */
  public Double getMarginYearMinus6() {
    return marginYearMinus6;
  }

  /**
   * Sets marginYearMinus6
   *
   * @param marginYearMinus6 the marginYearMinus6 to set
   */
  public void setMarginYearMinus6(Double marginYearMinus6) {
    this.marginYearMinus6 = marginYearMinus6;
  }

  /**
   * Gets isMarginYearMinus2Used
   *
   * @return the isMarginYearMinus2Used
   */
  public Boolean getIsMarginYearMinus2Used() {
    return isMarginYearMinus2Used;
  }

  /**
   * Sets isMarginYearMinus2Used
   *
   * @param isMarginYearMinus2Used the isMarginYearMinus2Used to set
   */
  public void setIsMarginYearMinus2Used(Boolean isMarginYearMinus2Used) {
    this.isMarginYearMinus2Used = isMarginYearMinus2Used;
  }

  /**
   * Gets isMarginYearMinus3Used
   *
   * @return the isMarginYearMinus3Used
   */
  public Boolean getIsMarginYearMinus3Used() {
    return isMarginYearMinus3Used;
  }

  /**
   * Sets isMarginYearMinus3Used
   *
   * @param isMarginYearMinus3Used the isMarginYearMinus3Used to set
   */
  public void setIsMarginYearMinus3Used(Boolean isMarginYearMinus3Used) {
    this.isMarginYearMinus3Used = isMarginYearMinus3Used;
  }

  /**
   * Gets isMarginYearMinus4Used
   *
   * @return the isMarginYearMinus4Used
   */
  public Boolean getIsMarginYearMinus4Used() {
    return isMarginYearMinus4Used;
  }

  /**
   * Sets isMarginYearMinus4Used
   *
   * @param isMarginYearMinus4Used the isMarginYearMinus4Used to set
   */
  public void setIsMarginYearMinus4Used(Boolean isMarginYearMinus4Used) {
    this.isMarginYearMinus4Used = isMarginYearMinus4Used;
  }

  /**
   * Gets isMarginYearMinus5Used
   *
   * @return the isMarginYearMinus5Used
   */
  public Boolean getIsMarginYearMinus5Used() {
    return isMarginYearMinus5Used;
  }

  /**
   * Sets isMarginYearMinus5Used
   *
   * @param isMarginYearMinus5Used the isMarginYearMinus5Used to set
   */
  public void setIsMarginYearMinus5Used(Boolean isMarginYearMinus5Used) {
    this.isMarginYearMinus5Used = isMarginYearMinus5Used;
  }

  /**
   * Gets isMarginYearMinus6Used
   *
   * @return the isMarginYearMinus6Used
   */
  public Boolean getIsMarginYearMinus6Used() {
    return isMarginYearMinus6Used;
  }

  /**
   * Sets isMarginYearMinus6Used
   *
   * @param isMarginYearMinus6Used the isMarginYearMinus6Used to set
   */
  public void setIsMarginYearMinus6Used(Boolean isMarginYearMinus6Used) {
    this.isMarginYearMinus6Used = isMarginYearMinus6Used;
  }

  /**
   * @return the enrolment status
   */
  @JsonIgnore
  public String getEnrolmentStatus() {
    String status;
    
    if(failedToGenerate.booleanValue()) {
      status = ListServiceConstants.ENROLMENT_STATUS_FAILED_TO_GENERATE;
    } else if(generatedDate != null) {
      status = ListServiceConstants.ENROLMENT_STATUS_GENERATED;
    } else {
      status = ListServiceConstants.ENROLMENT_STATUS_UNGENERATED;
    }
    
    return status;
  }

  /**
   * Gets isGeneratedFromCra
   *
   * @return the isGeneratedFromCra
   */
  public Boolean getIsGeneratedFromCra() {
    return isGeneratedFromCra;
  }

  /**
   * Sets isGeneratedFromCra
   *
   * @param isGeneratedFromCra the isGeneratedFromCra to set
   */
  public void setIsGeneratedFromCra(Boolean isGeneratedFromCra) {
    this.isGeneratedFromCra = isGeneratedFromCra;
  }

  public Boolean getIsGeneratedFromEnw() {
    return isGeneratedFromEnw;
  }

  public void setIsGeneratedFromEnw(Boolean isGeneratedFromEnw) {
    this.isGeneratedFromEnw = isGeneratedFromEnw;
  }

  /**
   * @return the isCreateTaskInBarn
   */
  public Boolean getIsCreateTaskInBarn() {
    return isCreateTaskInBarn;
  }

  /**
   * @param isCreateTaskInBarn the isCreateTaskInBarn to set
   */
  public void setIsCreateTaskInBarn(Boolean isCreateTaskInBarn) {
    this.isCreateTaskInBarn = isCreateTaskInBarn;
  }

  /**
   * @return the whenUpdated
   */
  public Date getWhenUpdated() {
    return whenUpdated;
  }

  /**
   * @param whenUpdated the whenUpdated to set
   */
  public void setWhenUpdated(Date whenUpdated) {
    this.whenUpdated = whenUpdated;
  }

  /**
   * @return the sectorCodeDescription
   */
  public String getSectorCodeDescription() {
    return sectorCodeDescription;
  }

  /**
   * @param sectorCodeDescription the sectorCodeDescription to set
   */
  public void setSectorCodeDescription(String sectorCodeDescription) {
    this.sectorCodeDescription = sectorCodeDescription;
  }

  /**
   * @return the sectorDetailCodeDescription
   */
  public String getSectorDetailCodeDescription() {
    return sectorDetailCodeDescription;
  }

  /**
   * @param sectorDetailCodeDescription the sectorDetailCodeDescription to set
   */
  public void setSectorDetailCodeDescription(String sectorDetailCodeDescription) {
    this.sectorDetailCodeDescription = sectorDetailCodeDescription;
  }

  public Boolean getIsLateParticipant() {
    return isLateParticipant;
  }

  public void setIsLateParticipant(Boolean isLateParticipant) {
    this.isLateParticipant = isLateParticipant;
  }

  public List<EnrolmentPartner> getEnrolmentPartners() {
    if(enrolmentPartners == null) {
      enrolmentPartners = new ArrayList<>();
    }
    return enrolmentPartners;
  }

  public void setEnrolmentPartners(List<EnrolmentPartner> enrolmentPartners) {
    this.enrolmentPartners = enrolmentPartners;
  }
  
  @JsonIgnore
  public boolean isPartnership() {
    return ! getEnrolmentPartners().isEmpty();
  }

  public Boolean getIsInCombinedFarm() {
    return isInCombinedFarm;
  }

  public void setIsInCombinedFarm(Boolean isInCombinedFarm) {
    this.isInCombinedFarm = isInCombinedFarm;
  }

  public List<EnrolmentCombinedFarmOwner> getCombinedFarmOwners() {
    if(combinedFarmOwners == null) {
      combinedFarmOwners = new ArrayList<>();
    }
    return combinedFarmOwners;
  }

  public void setCombinedFarmOwners(List<EnrolmentCombinedFarmOwner> combinedFarmOwners) {
    this.combinedFarmOwners = combinedFarmOwners;
  }

  public Double getCombinedFarmPercent() {
    return combinedFarmPercent;
  }

  public void setCombinedFarmPercent(Double combinedFarmPercent) {
    this.combinedFarmPercent = combinedFarmPercent;
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

    return "Enrolment"+"\n"+
    "\t scenario : "+scenarioId+"\n"+
    "\t clientId : "+clientId+"\n"+
    "\t pin : "+pin+"\n"+
    "\t enrolmentId : "+enrolmentId+"\n"+
    "\t enrolmentYear : "+enrolmentYear+"\n"+
    "\t enrolmentFee : "+enrolmentFee+"\n"+
    "\t generatedDate : "+generatedDate+"\n"+
    "\t whenUpdated : "+whenUpdated+"\n"+
    "\t revisionCount : "+revisionCount+"\n"+
    "\t failedReason : "+failedReason+"\n"+
    "\t failedToGenerate : "+failedToGenerate+"\n"+
    "\t scenarioState : "+scenarioState+"\n"+
    "\t producerName : "+producerName+"\n"+
    "\t contributionMarginAverage : "+contributionMarginAverage+"\n"+
    "\t marginYearMinus2 : "+marginYearMinus2+"\n"+
    "\t marginYearMinus3 : "+marginYearMinus3+"\n"+
    "\t marginYearMinus4 : "+marginYearMinus4+"\n"+
    "\t marginYearMinus5 : "+marginYearMinus5+"\n"+
    "\t marginYearMinus6 : "+marginYearMinus6+"\n"+
    "\t isMarginYearMinus2Used : "+isMarginYearMinus2Used+"\n"+
    "\t isMarginYearMinus3Used : "+isMarginYearMinus3Used+"\n"+
    "\t isMarginYearMinus4Used : "+isMarginYearMinus4Used+"\n"+
    "\t isMarginYearMinus5Used : "+isMarginYearMinus5Used+"\n"+
    "\t isMarginYearMinus6Used : "+isMarginYearMinus6Used+"\n"+
    "\t marginScenarioId : "+marginScenarioId+"\n"+
    "\t isGeneratedFromCra : "+isGeneratedFromCra+"\n"+
    "\t isGeneratedFromEnw : "+isGeneratedFromEnw+"\n"+
    "\t isCreateTaskInBarn : "+isCreateTaskInBarn+"\n"+
    "\t isLateParticipant : "+isLateParticipant+"\n"+
    "\t sectorCodeDescription : "+sectorCodeDescription+"\n"+
    "\t sectorDetailCodeDescription : "+sectorDetailCodeDescription+"\n"+
    "\t isInCombinedFarm : "+isInCombinedFarm+"\n"+
    "\t combinedFarmPercent : "+combinedFarmPercent+"\n"+
    "\t enrolmentPartners : "+enrolmentPartners+"\n"+
    "\t combinedFarmOwners : "+combinedFarmOwners+"\n";
  }

}
