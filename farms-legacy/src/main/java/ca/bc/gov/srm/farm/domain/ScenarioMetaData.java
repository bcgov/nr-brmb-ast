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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class will replace PinMetadataRead which was
 * a Data Transfer Object (DTO) used to transfer data outside of the system to
 * the external calculator containing Metadata for all information stored in the
 * system regarding this Participant.
 * 
 * It includes fields used to display a list of scenarios.

 * It will also be used in the Benefit Calculator web application for the same purpose.
 *
 * @author awilkinson
 * @created Dec 20, 2010
 */
public class ScenarioMetaData implements Serializable {
  
  private static final long serialVersionUID = -4480540116607784125L;

  /** back-reference to the object containing this */
  @JsonBackReference
  private Scenario scenario;
  
  private Integer scenarioId;

  private String calcVersion;

  private Boolean defaultInd;

  private Integer programYear;

  private String scenarioStateCode;
  
  private String scenarioStateDescription;
  
  private String scenarioCategoryCode;
  
  private String scenarioCategoryDescription;

  private Integer programYearVersion;

  private Integer programYearVersionId;

  private Integer revisionCount;

  private Date scenarioCreatedDate;

  private String scenarioDescription;

  private String scenarioCreatedBy;

  private Integer scenarioNumber;

  private String scenarioTypeCode;
  
  private String scenarioTypeDescription;

  private Integer combinedFarmNumber;

  private Integer chefsFormSubmissionId;
  
  private String chefsFormSubmissionGuid;
  
  private String participantDataSrcCode;
  
  private String chefsViewSubmissionUrl;

  private String municipalityCode;


  /**
   * @return the calcVersion
   */
  public String getCalcVersion() {
    return calcVersion;
  }

  /**
   * @param calcVersion the calcVersion to set
   */
  public void setCalcVersion(String calcVersion) {
    this.calcVersion = calcVersion;
  }

  /**
   * @return the defaultInd
   */
  public Boolean getDefaultInd() {
    return defaultInd;
  }

  /**
   * @param defaultInd the defaultInd to set
   */
  public void setDefaultInd(Boolean defaultInd) {
    this.defaultInd = defaultInd;
  }

  /**
   * @return the programYear
   */
  public Integer getProgramYear() {
    return programYear;
  }

  /**
   * @param programYear the programYear to set
   */
  public void setProgramYear(Integer programYear) {
    this.programYear = programYear;
  }

  /**
   * @return the programYearVersion
   */
  public Integer getProgramYearVersion() {
    return programYearVersion;
  }

  /**
   * @param programYearVersion the programYearVersion to set
   */
  public void setProgramYearVersion(Integer programYearVersion) {
    this.programYearVersion = programYearVersion;
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
   * @return the scenarioCreatedBy
   */
  public String getScenarioCreatedBy() {
    return scenarioCreatedBy;
  }

  /**
   * @param scenarioCreatedBy the scenarioCreatedBy to set
   */
  public void setScenarioCreatedBy(String scenarioCreatedBy) {
    this.scenarioCreatedBy = scenarioCreatedBy;
  }

  /**
   * @return the scenarioCreatedDate
   */
  public Date getScenarioCreatedDate() {
    return scenarioCreatedDate;
  }

  /**
   * @param scenarioCreatedDate the scenarioCreatedDate to set
   */
  public void setScenarioCreatedDate(Date scenarioCreatedDate) {
    this.scenarioCreatedDate = scenarioCreatedDate;
  }

  /**
   * @return the scenarioDescription
   */
  public String getScenarioDescription() {
    return scenarioDescription;
  }

  /**
   * @param scenarioDescription the scenarioDescription to set
   */
  public void setScenarioDescription(String scenarioDescription) {
    this.scenarioDescription = scenarioDescription;
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
   * @return the scenarioStateDescription
   */
  public String getScenarioStateDescription() {
    return scenarioStateDescription;
  }

  /**
   * @param scenarioStateDescription the scenarioStateDescription to set
   */
  public void setScenarioStateDescription(String scenarioStateDescription) {
    this.scenarioStateDescription = scenarioStateDescription;
  }

  /**
   * @return the scenarioCategoryCode
   */
  public String getScenarioCategoryCode() {
    return scenarioCategoryCode;
  }

  /**
   * @param scenarioCategoryCode the scenarioCategoryCode to set
   */
  public void setScenarioCategoryCode(String scenarioCategoryCode) {
    this.scenarioCategoryCode = scenarioCategoryCode;
  }

  /**
   * @return the scenarioCategoryDescription
   */
  public String getScenarioCategoryDescription() {
    return scenarioCategoryDescription;
  }

  /**
   * @param scenarioCategoryDescription the scenarioCategoryDescription to set
   */
  public void setScenarioCategoryDescription(String scenarioCategoryDescription) {
    this.scenarioCategoryDescription = scenarioCategoryDescription;
  }
  
  /**
   * @return the scenarioTypeCode
   */
  public String getScenarioTypeCode() {
    return scenarioTypeCode;
  }

  /**
   * @param scenarioTypeCode the scenarioTypeCode to set
   */
  public void setScenarioTypeCode(String scenarioTypeCode) {
    this.scenarioTypeCode = scenarioTypeCode;
  }

  /**
   * @return the scenarioTypeDescription
   */
  public String getScenarioTypeDescription() {
    return scenarioTypeDescription;
  }

  /**
   * @param scenarioTypeDescription the scenarioTypeDescription to set
   */
  public void setScenarioTypeDescription(String scenarioTypeDescription) {
    this.scenarioTypeDescription = scenarioTypeDescription;
  }

  /**
   * @return the scenarioId
   */
  public Integer getScenarioId() {
    return scenarioId;
  }

  /**
   * @param scenarioId the scenarioId to set the value to
   */
  public void setScenarioId(Integer scenarioId) {
    this.scenarioId = scenarioId;
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
   * @return the combinedFarmNumber
   */
  public Integer getCombinedFarmNumber() {
    return combinedFarmNumber;
  }

  /**
   * @param combinedFarmNumber the combinedFarmNumber to set
   */
  public void setCombinedFarmNumber(Integer combinedFarmNumber) {
    this.combinedFarmNumber = combinedFarmNumber;
  }

  public Integer getChefsFormSubmissionId() {
    return chefsFormSubmissionId;
  }

  public void setChefsFormSubmissionId(Integer chefsFormSubmissionId) {
    this.chefsFormSubmissionId = chefsFormSubmissionId;
  }

  public String getParticipantDataSrcCode() {
    return participantDataSrcCode;
  }

  public void setParticipantDataSrcCode(String participantDataSrcCode) {
    this.participantDataSrcCode = participantDataSrcCode;
  }

  public String getChefsFormSubmissionGuid() {
    return chefsFormSubmissionGuid;
  }

  public void setChefsFormSubmissionGuid(String chefsFormSubmissionGuid) {
    this.chefsFormSubmissionGuid = chefsFormSubmissionGuid;
  }

  public String getChefsViewSubmissionUrl() {
    return chefsViewSubmissionUrl;
  }

  public void setChefsViewSubmissionUrl(String chefsViewSubmissionUrl) {
    this.chefsViewSubmissionUrl = chefsViewSubmissionUrl;
  }

  public String getMunicipalityCode() {
    return municipalityCode;
  }

  public void setMunicipalityCode(String municipalityCode) {
    this.municipalityCode = municipalityCode;
  }

  public Integer getProgramYearVersionId() {
    return programYearVersionId;
  }

  public void setProgramYearVersionId(Integer programYearVersionId) {
    this.programYearVersionId = programYearVersionId;
  }

  /**
   * @return the scenarioCreatedBy
   */
  @JsonIgnore
  public String getScenarioCreatedByDisplay() {
    final int idirLength = 5;
    String result;
    if(scenarioCreatedBy == null) {
      result = null;
    } else {
      if(scenarioCreatedBy.toUpperCase().startsWith("IDIR\\")
          && scenarioCreatedBy.length() > idirLength) {
        result = scenarioCreatedBy.substring(idirLength);
      } else {
        result = scenarioCreatedBy;
      }
    }
    return result;
  }
  
  public boolean stateIsOneOf(String... states) {
    List<String> stateList = Arrays.asList(states);
    boolean result = stateList.contains(getScenarioStateCode());
    return result;
  }
  
  public boolean typeIsOneOf(String... types) {
    List<String> typeList = Arrays.asList(types);
    boolean result = typeList.contains(getScenarioTypeCode());
    return result;
  }

  public boolean categoryIsOneOf(String... categories) {
    List<String> categoryList = Arrays.asList(categories);
    boolean result = categoryList.contains(getScenarioCategoryCode());
    return result;
  }
  
  /**
   * @return  String
   *
   * @see     java.lang.Object#toString()
   */
  @Override
  public String toString() {
    
    Integer parentScenarioId = null;
    if(scenario != null) {
      parentScenarioId = scenario.getScenarioId();
    }

    return this.getClass().getName() + "\n" +
    "\t parentScenarioId : " + parentScenarioId+ "\n" +
    "\t scenarioId : " + scenarioId+ "\n" +
    "\t calcVersion : " + calcVersion + "\n" + 
    "\t defaultInd : " + defaultInd + "\n" +
    "\t programYear : " + programYear + "\n" +
    "\t scenarioStateCode : " + scenarioStateCode + "\n" +
    "\t scenarioStateDescription : " + scenarioStateDescription + "\n" +
    "\t scenarioCategoryCode : " + scenarioCategoryCode + "\n" +
    "\t scenarioCategoryDescription : " + scenarioCategoryDescription + "\n" +
    "\t programYearVersion : " + programYearVersion + "\n" +
    "\t programYearVersionId : " + programYearVersionId + "\n" +
    "\t revisionCount : " + revisionCount + "\n" +
    "\t scenarioCreatedDate : " + scenarioCreatedDate + "\n" +
    "\t scenarioCreatedBy : " + scenarioCreatedBy + "\n" +
    "\t scenarioDescription : " + scenarioDescription + "\n" +
    "\t scenarioNumber : " + scenarioNumber + "\n" +
    "\t scenarioTypeCode : " + scenarioTypeCode + "\n" +
    "\t scenarioTypeDescription : " + scenarioTypeDescription +
    "\t combinedFarmNumber : " + combinedFarmNumber +
    "\t chefsFormSubmissionId : " + chefsFormSubmissionId +
    "\t chefsFormSubmissionGuid : " + chefsFormSubmissionGuid +
    "\t chefsSubmissionUrl: " + chefsViewSubmissionUrl +
    "\t participantDataSrcCode : " + participantDataSrcCode;
  }
}
