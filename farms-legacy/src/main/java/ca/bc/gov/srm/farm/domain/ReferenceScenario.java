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
 * ReferenceScenario refers to a unique instance of the associated data.
 * ReferenceScenario will be enforced on Operation, Margin, Claim and all
 * Adjustments data according to specific business rules (i.e. tied to State
 * changes). Many instances of a ReferenceScenario may exist for the above
 * listed entities.
 * 
 * @author awilkinson
 * @created Nov 12, 2010
 *
 */
public class ReferenceScenario implements Serializable {
  
  private static final long serialVersionUID = 7445038808388123683L;

  /** back-reference to the object containing this
   * If this object is the main Scenario object
   * then this will be a reference to itself.
   * (set in the Scenario constructor).
   */
  @JsonBackReference
  private Scenario parentScenario;

  /** The year for this scenario */
  private Integer year;

  /**
   * scenarioId is a surrogate unique identifier for
   * AgristabilityScenario.
   */
  private Integer scenarioId;
  
  private FarmingYear farmingYear;

  private ImportVersion importVersion;

  /**
   * scenarioNumber is the user unique identifier for a ReferenceScenario. A
   * SCENARIO NAME must be unique across the same scenario.
   */
  private Integer scenarioNumber;

  /** The scenario type (USER, CRA, REFERENCE). */
  private String scenarioTypeCode;

  /** Description for senarioTypeCode. */
  private String scenarioTypeCodeDescription;

  /**
   * benefitsCalculatorVersion identifies the version from the benefits
   * calculator spreadsheet used to calculate the claim.
   */
  private String benefitsCalculatorVersion;

  /** scenarioCreatedBy is the user who created the AgristabilityScenario. */
  private String scenarioCreatedBy;

  /**
   * description identifies the user defined name of a save version (or
   * scenario).
   */
  private String description;

  /**
   * isDefault is used to determine which AgristabilityScenario is the currently
   * active record.
   */
  private Boolean isDefaultInd;

  /** scenarioDate is the date that the scenario was created. */
  private Date scenarioDate;

  /**
   * usedInCalc identifies if the AgristabilityScenario record was used in the
   * calculation of the claim.
   */
  private Boolean usedInCalc;
  
  private Boolean isDeemedFarmingYear;
  
  private String participantDataSrcCode;

  
  @JsonIgnore
  public boolean isProgramYear() {
    return parentScenario.getYear().equals(year);
  }

  /**
   * @return the importVersion
   */
  public ImportVersion getImportVersion() {
    return importVersion;
  }

  /**
   * @param importVersion the importVersion to set
   */
  public void setImportVersion(ImportVersion importVersion) {
    if(importVersion != null) {
      importVersion.setReferenceScenario(this);
    }
    this.importVersion = importVersion;
  }

  /**
   * @return the farmingYear
   */
  public FarmingYear getFarmingYear() {
    return farmingYear;
  }

  /**
   * @param farmingYear the farmingYear to set
   */
  public void setFarmingYear(FarmingYear farmingYear) {
    if(farmingYear != null) {
      farmingYear.setReferenceScenario(this);
    }
    this.farmingYear = farmingYear;
  }

  /**
   * scenarioId is a surrogate unique identifier for
   * AgristabilityScenario.
   *
   * @return  Integer
   */
  public Integer getScenarioId() {
    return scenarioId;
  }

  /**
   * scenarioId is a surrogate unique identifier for
   * AgristabilityScenario.
   *
   * @param  newVal  The new value for this property
   */
  public void setScenarioId(final Integer newVal) {
    scenarioId = newVal;
  }

  /**
   * ScenarioNumber is the user unique identifier for a AgristabilityScenario. A
   * SCENARIO NAME must be unique across the same scenario.
   *
   * @return  Integer
   */
  public Integer getScenarioNumber() {
    return scenarioNumber;
  }

  /**
   * ScenarioNumber is the user unique identifier for a AgristabilityScenario. A
   * SCENARIO NAME must be unique across the same scenario.
   *
   * @param  newVal  The new value for this property
   */
  public void setScenarioNumber(final Integer newVal) {
    scenarioNumber = newVal;
  }

  /**
   * BenefitsCalculatorVersion identifies the version from the benefits
   * calculator spreadsheet used to calculate the claim.
   *
   * @return  String
   */
  public String getBenefitsCalculatorVersion() {
    return benefitsCalculatorVersion;
  }

  /**
   * BenefitsCalculatorVersion identifies the version from the benefits
   * calculator spreadsheet used to calculate the claim.
   *
   * @param  newVal  The new value for this property
   */
  public void setBenefitsCalculatorVersion(final String newVal) {
    benefitsCalculatorVersion = newVal;
  }

  /**
   * ScenarioCreatedBy is the user who created the AgristabilityScenario.
   *
   * @return  String
   */
  public String getScenarioCreatedBy() {
    return scenarioCreatedBy;
  }

  /**
   * ScenarioCreatedBy is the user who created the AgristabilityScenario.
   *
   * @param  newVal  The new value for this property
   */
  public void setScenarioCreatedBy(final String newVal) {
    scenarioCreatedBy = newVal;
  }

  /**
   * Description identifies the user defined name of a save version (or
   * scenario).
   *
   * @return  String
   */
  public String getDescription() {
    return description;
  }

  /**
   * Description identifies the user defined name of a save version (or
   * scenario).
   *
   * @param  newVal  The new value for this property
   */
  public void setDescription(final String newVal) {
    description = newVal;
  }

  /**
   * IsDefault is used to determine which AgristabilityScenario is the currently
   * active record.
   *
   * @return  Boolean
   */
  public Boolean getIsDefaultInd() {
    return isDefaultInd;
  }

  /**
   * IsDefault is used to determine which AgristabilityScenario is the currently
   * active record.
   *
   * @param  isDefaultInd  The new value for this property
   */
  public void setIsDefaultInd(final Boolean isDefaultInd) {
    this.isDefaultInd = isDefaultInd;
  }

  /**
   * ScenarioDate is the date that the scenario was created.
   *
   * @return  Date
   */
  public Date getScenarioDate() {
    return scenarioDate;
  }

  /**
   * ScenarioDate is the date that the scenario was created.
   *
   * @param  newVal  The new value for this property
   */
  public void setScenarioDate(final Date newVal) {
    scenarioDate = newVal;
  }

  /**
   * @return  String
   */
  public String getScenarioTypeCode() {
    return scenarioTypeCode;
  }

  /**
   * @param  newVal  The new value for this property
   */
  public void setScenarioTypeCode(final String newVal) {
    this.scenarioTypeCode = newVal;
  }

  /**
   * @return  the senarioTypeCode
   */
  public String getScenarioTypeCodeDescription() {
    return scenarioTypeCodeDescription;
  }

  /**
   * @param  newVal  The new value for this property
   */
  public void setScenarioTypeCodeDescription(final String newVal) {
    this.scenarioTypeCodeDescription = newVal;
  }

  /**
   * @return the year
   */
  public Integer getYear() {
    return year;
  }

  /**
   * @param year the year to set
   */
  public void setYear(Integer year) {
    this.year = year;
  }

  /**
   * usedInCalc identifies if the Benefit record was used in the
   * calculation of the claim.
   *
   * @return  Boolean
   */
  public Boolean getUsedInCalc() {
    return usedInCalc;
  }

  /**
   * usedInCalc identifies if the Benefit record was used in the
   * calculation of the claim.
   *
   * @param  newVal  The new value for this property
   */
  public void setUsedInCalc(final Boolean newVal) {
    usedInCalc = newVal;
  }

  /**
   * @return the parentScenario
   */
  public Scenario getParentScenario() {
    return parentScenario;
  }

  /**
   * @param parentScenario the parentScenario to set the value to
   */
  public void setParentScenario(Scenario parentScenario) {
    this.parentScenario = parentScenario;
  }

  /**
	 * @return the isDeemedFarmingYear
	 */
	public Boolean getIsDeemedFarmingYear() {
		return isDeemedFarmingYear;
	}

	/**
	 * @param isDeemedFarmingYear the isDeemedFarmingYear to set
	 */
	public void setIsDeemedFarmingYear(Boolean isDeemedFarmingYear) {
		this.isDeemedFarmingYear = isDeemedFarmingYear;
	}

	public String getParticipantDataSrcCode() {
    return participantDataSrcCode;
  }

  public void setParticipantDataSrcCode(String participantDataSrcCode) {
    this.participantDataSrcCode = participantDataSrcCode;
  }

  /**
   * 
   * @return String
   * @see Object#toString()
   */
  @Override
  public String toString(){

    Integer parentScenarioId = null;
    if(parentScenario != null) {
      parentScenarioId = parentScenario.getScenarioId();
    }

    return "AgristabilityScenario"+"\n"+
    "\t parent scenario : "+parentScenarioId+"\n"+
    "\t usedInCalc : "+usedInCalc+"\n"+
    "\t scenarioId : "+scenarioId+"\n"+
    "\t year : "+year+"\n"+
    "\t scenarioTypeCode : "+scenarioTypeCode+"\n"+
    "\t scenarioTypeCodeDescription : "+scenarioTypeCodeDescription+"\n"+
    "\t scenarioNumber : "+scenarioNumber+"\n"+
    "\t benefitsCalculatorVersion : "+benefitsCalculatorVersion+"\n"+
    "\t scenarioCreatedBy : "+scenarioCreatedBy+"\n"+
    "\t description : "+description+"\n"+
    "\t isDefaultInd : "+isDefaultInd+"\n"+
    "\t scenarioDate : "+scenarioDate+"\n"+
    "\t importVersion : "+importVersion+"\n"+
    "\t participantDataSrcCode : "+participantDataSrcCode+"\n"+
    "\t farmingYear : "+farmingYear+"\n";
  }
}
