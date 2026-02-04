/**
 *
 * Copyright (c) 2024,
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ca.bc.gov.srm.farm.domain.codes.ParticipantDataSrcCodes;

/**
 * This class will replace PinMetadataRead which was
 * a Data Transfer Object (DTO) used to transfer data outside of the system to
 * the external calculator containing Metadata for all information stored in the
 * system regarding this Participant.
 * 
 * It includes fields used to display a list of scenarios.

 * It will also be used in the Benefit Calculator web application for the same purpose.
 *
 */
public class FarmingOperationImportOption implements Serializable {
  
  private static final long serialVersionUID = 7153926245324459812L;

  private Integer programYearVersion;
  private Integer scenarioNumber;
  private String scenarioClassCode;
  private String scenarioClassDescription;
  private String scenarioCategoryCode;
  private String scenarioCategoryDescription;
  private String scenarioStateCode;
  private String scenarioStateDescription;
  private Date scenarioCreatedDate;
  private String alignmentKey;
  private Integer partnershipPin;
  private String partnershipName;
  private Integer farminOperationId;
  private Boolean hasCraProductiveUnits;
  private Boolean hasLocalProductiveUnits;
  
  public Integer getProgramYearVersion() {
    return programYearVersion;
  }
  public void setProgramYearVersion(Integer programYearVersion) {
    this.programYearVersion = programYearVersion;
  }
  public Integer getScenarioNumber() {
    return scenarioNumber;
  }
  public void setScenarioNumber(Integer scenarioNumber) {
    this.scenarioNumber = scenarioNumber;
  }
  public String getScenarioClassCode() {
    return scenarioClassCode;
  }
  public void setScenarioClassCode(String scenarioClassCode) {
    this.scenarioClassCode = scenarioClassCode;
  }
  public String getScenarioClassDescription() {
    return scenarioClassDescription;
  }
  public void setScenarioClassDescription(String scenarioClassDescription) {
    this.scenarioClassDescription = scenarioClassDescription;
  }
  public String getScenarioCategoryCode() {
    return scenarioCategoryCode;
  }
  public void setScenarioCategoryCode(String scenarioCategoryCode) {
    this.scenarioCategoryCode = scenarioCategoryCode;
  }
  public String getScenarioCategoryDescription() {
    return scenarioCategoryDescription;
  }
  public void setScenarioCategoryDescription(String scenarioCategoryDescription) {
    this.scenarioCategoryDescription = scenarioCategoryDescription;
  }
  public String getScenarioStateCode() {
    return scenarioStateCode;
  }
  public void setScenarioStateCode(String scenarioStateCode) {
    this.scenarioStateCode = scenarioStateCode;
  }
  public String getScenarioStateDescription() {
    return scenarioStateDescription;
  }
  public void setScenarioStateDescription(String scenarioStateDescription) {
    this.scenarioStateDescription = scenarioStateDescription;
  }
  public Date getScenarioCreatedDate() {
    return scenarioCreatedDate;
  }
  public void setScenarioCreatedDate(Date scenarioCreatedDate) {
    this.scenarioCreatedDate = scenarioCreatedDate;
  }
  public String getAlignmentKey() {
    return alignmentKey;
  }
  public void setAlignmentKey(String alignmentKey) {
    this.alignmentKey = alignmentKey;
  }
  public Integer getPartnershipPin() {
    return partnershipPin;
  }
  public void setPartnershipPin(Integer partnershipPin) {
    this.partnershipPin = partnershipPin;
  }
  public String getPartnershipName() {
    return partnershipName;
  }
  public void setPartnershipName(String partnershipName) {
    this.partnershipName = partnershipName;
  }
  public Integer getFarminOperationId() {
    return farminOperationId;
  }
  public void setFarminOperationId(Integer farminOperationId) {
    this.farminOperationId = farminOperationId;
  }
  public Boolean getHasCraProductiveUnits() {
    return hasCraProductiveUnits;
  }
  public void setHasCraProductiveUnits(Boolean hasCraProductiveUnits) {
    this.hasCraProductiveUnits = hasCraProductiveUnits;
  }
  public Boolean getHasLocalProductiveUnits() {
    return hasLocalProductiveUnits;
  }
  public void setHasLocalProductiveUnits(Boolean hasLocalProductiveUnits) {
    this.hasLocalProductiveUnits = hasLocalProductiveUnits;
  }
  @Override
  public String toString() {
    return "FarmingOperationImportOption [programYearVersion=" + programYearVersion + ", scenarioNumber=" + scenarioNumber + ", scenarioClassCode="
        + scenarioClassCode + ", scenarioClassDescription=" + scenarioClassDescription + ", scenarioCategoryCode=" + scenarioCategoryCode
        + ", scenarioCategoryDescription=" + scenarioCategoryDescription + ", scenarioStateCode=" + scenarioStateCode + ", scenarioStateDescription="
        + scenarioStateDescription + ", scenarioCreatedDate=" + scenarioCreatedDate + ", alignmentKey=" + alignmentKey + ", partnershipPin="
        + partnershipPin + ", partnershipName=" + partnershipName + ", farminOperationId=" + farminOperationId + ", hasCraProductiveUnits="
        + hasCraProductiveUnits + ", hasLocalProductiveUnits=" + hasLocalProductiveUnits + "]";
  }

  public List<String> getParticipantDataSrcCodeList() {
    List<String> result = new ArrayList<>();
    
    if(hasCraProductiveUnits) {
      result.add(ParticipantDataSrcCodes.CRA);
    }
    if(hasLocalProductiveUnits) {
      result.add(ParticipantDataSrcCodes.LOCAL);
    }
    
    return result;
  }
  
  public String getParticipantDataSrcCodesSpaceDelimitedString() {
    return String.join(" ", getParticipantDataSrcCodeList());
  }
}
