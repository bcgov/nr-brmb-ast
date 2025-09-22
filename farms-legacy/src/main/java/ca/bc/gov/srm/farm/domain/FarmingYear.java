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
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * FarmingYear contains the Program Year Version details,
 * Farming Operations, Margin Totals, and Benefit data
 * for the combination of this program year version
 * and the Scenario it is contained in. 
 * 
 * @author awilkinson
 * @created Nov 12, 2010
 */
public class FarmingYear implements Serializable {
  
  private static final long serialVersionUID = -4972763318355335210L;

  /** back-reference to the object containing this */
  @JsonBackReference
  private ReferenceScenario referenceScenario;

  private List<FarmingOperation> farmingOperations;

  private MarginTotal marginTotal;

  private Benefit benefit;
  
  private List<WholeFarmParticipant> wholeFarmParticipants;

  /**
   * programYearVersionId is a surrogate unique identifier for ProgramYear
   * VERSIONS.
   */
  private Integer programYearVersionId;

  /**
   * programYearVersionNumber identifies the unique version of the ProgramYear
   * VERSION for the given ProgramYear.
   */
  private Integer programYearVersionNumber;

  /**
   * formVersionNumber distinguishes between different versions of the
   * AgriStability application from the producer. Both the producer and the
   * administration can initiate adjustments that create a new form version in a
   * specific program year.
   */
  private Integer formVersionNumber;

  /**
   * commonShareTotal is the Outstanding Common SharesIndividual: zero; Entity:
   * >= zero.a.
   */
  private Integer commonShareTotal;

  /** farmYears is the number of years the farm has been in operation. */
  private Integer farmYears;

  /**
   * isAccrualWorksheet denotes if the "Accrual Reference Margin Worksheet" box
   * is checked.
   */
  private Boolean isAccrualWorksheet;

  /**
   * isCompletedProdCycle denotes if the "Have you completed a production cycle
   * on at least one of the commodities you produced?" box is checked.
   */
  private Boolean isCompletedProdCycle;

  /**
   * isCwbWorksheet denotes if the "CWB Adjustment Worksheet" box is checked.
   */
  private Boolean isCwbWorksheet;

  /**
   * isPerishableCommodities denotes if the Perishable Commodities Worksheet box
   * is checked.
   */
  private Boolean isPerishableCommodities;

  /** isReceipts denotes if receipts are available and included with report. */
  private Boolean isReceipts;

  /**
   * isAccrualCashConversion denotes if the Accrual to Cash / Cash to Accrual
   * Conversions box is checked.
   */
  private Boolean isAccrualCashConversion;

  /** isCombinedFarm as indicated by the participant. */
  private Boolean isCombinedFarm;

  /**
   * isCoopMember is "Y" if carried on farming business as a member of a co-
   * operative; otherwise "N".
   */
  private Boolean isCoopMember;

  /**
   * isCorporateShareholder is "Y" if carried on farming business as a
   * shareholder of a corporation; otherwise "N".
   */
  private Boolean isCorporateShareholder;

  /**
   * isDisaster denotes if the "were you unable to complete a production cycle
   * due to isDisaster circumstances?" box is checked.
   */
  private Boolean isDisaster;

  /**
   * isPartnershipMember is "Y" if carried on farming business partner of a
   * partnership; otherwise "N".
   */
  private Boolean isPartnershipMember;

  /**
   * isSoleProprietor is "Y" if carried on farming business as a sole
   * proprietor; otherwise "N".
   */
  private Boolean isSoleProprietor;

  /**
   * isLastYearFarming indicates if the current ProgramYear was the last year of
   * farming for the client.
   */
  private Boolean isLastYearFarming;

  /**
   * isCanSendCobToRep Indicates that a copy of the Calculation of Benefits
   * (COB) statement should be sent to the contact person.a.
   */
  private Boolean isCanSendCobToRep;

  /**
   * otherText is any additional justification or supporting details provided by
   * participant or administration.
   */
  private String otherText;

  /**
   * postMarkDate is the Date the Form was Postmarked. Will be received date if
   * received before filing deadline.
   */
  private Date postMarkDate;

  /**
   * provinceOfResidence denotes the province for the operation making the tax
   * submission.
   */
  private String provinceOfResidence;

  /** craStatementAReceivedDate is the date the form was received by RCT.
   *  This is for this specific program year version.
   */
  private Date craStatementAReceivedDate;

  /**
   * descriptionOfChange summarizes the modifications made to the current
   * ImportVersion since the last ImportVersion. This field will be
   * automatically populated by the import process based on a series of checks
   * against the system.
   */
  private String descriptionOfChange;

  /**
   * provinceOfMainFarmstead is the main farmstead's Province in the legal land
   * description.
   */
  private String provinceOfMainFarmstead;

  /**
   * agristabFedStsCode identifies the federal status code of the application.
   * Possible Values: 1 - Waiting for Data, 2 - In Progress, 3 - Complete -
   * Ineligible, 4 - Complete - Zero Payment, 5 - Complete - Payment.
   */
  private Integer agristabFedStsCode;

  /** Description for agristabFedStsCode. */
  private String agristabFedStsCodeDescription;


  /** municipalityCode denotes the municipality of the FARMSTEAD. */
  private String municipalityCode;

  /** Description for municipalityCode. */
  private String municipalityCodeDescription;

  /**
   * participantProfileCode is a unique code for the object
   * participantProfileCode described as a numeric code used to uniquely
   * identify which programs the participant is applying for. Examples of codes
   * and descriptions are 1 - Agri- Stability Only, 2 - Agri-Invest Only, 3 -
   * Agri-Stability and Agri-Invest. Default = 3.
   */
  private String participantProfileCode;

  /** Description for participantProfileCode. */
  private String participantProfileCodeDescription;

  /** isLocallyUpdated identifies if the record was updated by the client. */
  private Boolean isLocallyUpdated;

  /**
   * revisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   */
  private Integer revisionCount;

  private Integer programYearId;
  
  private Boolean isNonParticipant;
  
  private Boolean isLateParticipant;

  private Boolean isCashMargins;
  
  private Date cashMarginsOptInDate;

  /**
   * @return  size() from farmingOperations
   */
  @JsonIgnore
  public int getFarmingOperationCount() {
    int count = 0;
    if(farmingOperations != null) {
      count = farmingOperations.size();
    }
    return count;
  }

  /**
   * @return the benefit
   */
  public Benefit getBenefit() {
    return benefit;
  }

  /**
   * @param benefit the benefit to set
   */
  public void setBenefit(Benefit benefit) {
    if(benefit != null) {
      benefit.setFarmingYear(this);
    }
    this.benefit = benefit;
  }

  /**
   * @return the benefitCalcMarginTotal
   */
  public MarginTotal getMarginTotal() {
    return marginTotal;
  }

  /**
   * @param marginTotal the benefitCalcMarginTotal to set
   */
  public void setMarginTotal(MarginTotal marginTotal) {
    if(marginTotal != null) {
      marginTotal.setFarmingYear(this);
    }
    this.marginTotal = marginTotal;
  }

  /**
   * @return the farmingOperations
   */
  public List<FarmingOperation> getFarmingOperations() {
    return farmingOperations;
  }

  /**
   * @param farmingOperations the farmingOperations to set
   */
  public void setFarmingOperations(List<FarmingOperation> farmingOperations) {
    if(farmingOperations != null) {
      for(FarmingOperation cur : farmingOperations) {
        cur.setFarmingYear(this);
      }
    }
    this.farmingOperations = farmingOperations;
  }

  /**
   * programYearVersionId is a surrogate unique identifier for ProgramYear
   * VERSIONS.
   *
   * @return  Integer
   */
  public Integer getProgramYearVersionId() {
    return programYearVersionId;
  }

  /**
   * programYearVersionId is a surrogate unique identifier for ProgramYear
   * VERSIONS.
   *
   * @param  programYearVersionId  The new value for this property
   */
  public void setProgramYearVersionId(final Integer programYearVersionId) {
    this.programYearVersionId = programYearVersionId;
  }

  /**
   * ProgramYearVersionNumber identifies the unique version of the ProgramYear
   * VERSION for the given ProgramYear.
   *
   * @return  Integer
   */
  public Integer getProgramYearVersionNumber() {
    return programYearVersionNumber;
  }

  /**
   * ProgramYearVersionNumber identifies the unique version of the ProgramYear
   * VERSION for the given ProgramYear.
   *
   * @param  programYearVersionNumber  The new value for this property
   */
  public void setProgramYearVersionNumber(final Integer programYearVersionNumber) {
    this.programYearVersionNumber = programYearVersionNumber;
  }

  /**
   * FormVersionNumber distinguishes between different versions of the
   * AgriStability application from the producer. Both the producer and the
   * administration can initiate adjustments that create a new form version in a
   * specific program year.
   *
   * @return  Integer
   */
  public Integer getFormVersionNumber() {
    return formVersionNumber;
  }

  /**
   * FormVersionNumber distinguishes between different versions of the
   * AgriStability application from the producer. Both the producer and the
   * administration can initiate adjustments that create a new form version in a
   * specific program year.
   *
   * @param  formVersionNumber  The new value for this property
   */
  public void setFormVersionNumber(final Integer formVersionNumber) {
    this.formVersionNumber = formVersionNumber;
  }

  /**
   * CommonShareTotal is the Outstanding Common SharesIndividual: zero; Entity:
   * >= zero.a.
   *
   * @return  Integer
   */
  public Integer getCommonShareTotal() {
    return commonShareTotal;
  }

  /**
   * CommonShareTotal is the Outstanding Common SharesIndividual: zero; Entity:
   * >= zero.a.
   *
   * @param  commonShareTotal  The new value for this property
   */
  public void setCommonShareTotal(final Integer commonShareTotal) {
    this.commonShareTotal = commonShareTotal;
  }

  /**
   * FarmYears is the number of years the farm has been in operation.
   *
   * @return  Integer
   */
  public Integer getFarmYears() {
    return farmYears;
  }

  /**
   * FarmYears is the number of years the farm has been in operation.
   *
   * @param  farmYears  The new value for this property
   */
  public void setFarmYears(final Integer farmYears) {
    this.farmYears = farmYears;
  }

  /**
   * isAccrualWorksheet denotes if the "Accrual Reference Margin Worksheet" box
   * is checked.
   *
   * @return  Boolean
   */
  public Boolean getIsAccrualWorksheet() {
    return isAccrualWorksheet;
  }

  /**
   * isAccrualWorksheet denotes if the "Accrual Reference Margin Worksheet" box
   * is checked.
   *
   * @param  isAccrualWorksheet  The new value for this property
   */
  public void setIsAccrualWorksheet(final Boolean isAccrualWorksheet) {
    this.isAccrualWorksheet = isAccrualWorksheet;
  }

  /**
   * isCompletedProdCycle denotes if the "Have you completed a production cycle
   * on at least one of the commodities you produced?" box is checked.
   *
   * @return  Boolean
   */
  public Boolean getIsCompletedProdCycle() {
    return isCompletedProdCycle;
  }

  /**
   * isCompletedProdCycle denotes if the "Have you completed a production cycle
   * on at least one of the commodities you produced?" box is checked.
   *
   * @param  isCompletedProdCycle  The new value for this property
   */
  public void setIsCompletedProdCycle(final Boolean isCompletedProdCycle) {
    this.isCompletedProdCycle = isCompletedProdCycle;
  }

  /**
   * isCwbWorksheet denotes if the "CWB Adjustment Worksheet" box is checked.
   *
   * @return  Boolean
   */
  public Boolean getIsCwbWorksheet() {
    return isCwbWorksheet;
  }

  /**
   * isCwbWorksheet denotes if the "CWB Adjustment Worksheet" box is checked.
   *
   * @param  isCwbWorksheet  The new value for this property
   */
  public void setIsCwbWorksheet(final Boolean isCwbWorksheet) {
    this.isCwbWorksheet = isCwbWorksheet;
  }

  /**
   * isPerishableCommodities denotes if the Perishable Commodities Worksheet box
   * is checked.
   *
   * @return  Boolean
   */
  public Boolean getIsPerishableCommodities() {
    return isPerishableCommodities;
  }

  /**
   * isPerishableCommodities denotes if the Perishable Commodities Worksheet box
   * is checked.
   *
   * @param  isPerishableCommodities  The new value for this property
   */
  public void setIsPerishableCommodities(final Boolean isPerishableCommodities) {
    this.isPerishableCommodities = isPerishableCommodities;
  }

  /**
   * @return  Boolean
   */
  public Boolean getIsReceipts() {
    return isReceipts;
  }

  /**
   * @param  isReceipts  The new value for this property
   */
  public void setIsReceipts(final Boolean isReceipts) {
    this.isReceipts = isReceipts;
  }

  /**
   * isAccrualCashConversion denotes if the Accrual to Cash / Cash to Accrual
   * Conversions box is checked.
   *
   * @return  Boolean
   */
  public Boolean getIsAccrualCashConversion() {
    return isAccrualCashConversion;
  }

  /**
   * isAccrualCashConversion denotes if the Accrual to Cash / Cash to Accrual
   * Conversions box is checked.
   *
   * @param  isAccrualCashConversion  The new value for this property
   */
  public void setIsAccrualCashConversion(final Boolean isAccrualCashConversion) {
    this.isAccrualCashConversion = isAccrualCashConversion;
  }

  /**
   * isCombinedFarm as indicated by the participant.
   *
   * @return  Boolean
   */
  public Boolean getIsCombinedFarm() {
    return isCombinedFarm;
  }

  /**
   * isCombinedFarm as indicated by the participant.
   *
   * @param  isCombinedFarm  The new value for this property
   */
  public void setIsCombinedFarm(final Boolean isCombinedFarm) {
    this.isCombinedFarm = isCombinedFarm;
  }

  /**
   * isCoopMember is "Y" if carried on farming business as a member of a co-
   * operative; otherwise "N".
   *
   * @return  Boolean
   */
  public Boolean getIsCoopMember() {
    return isCoopMember;
  }

  /**
   * isCoopMember is "Y" if carried on farming business as a member of a co-
   * operative; otherwise "N".
   *
   * @param  isCoopMember  The new value for this property
   */
  public void setIsCoopMember(final Boolean isCoopMember) {
    this.isCoopMember = isCoopMember;
  }

  /**
   * isCorporateShareholder is "Y" if carried on farming business as a
   * shareholder of a corporation; otherwise "N".
   *
   * @return  Boolean
   */
  public Boolean getIsCorporateShareholder() {
    return isCorporateShareholder;
  }

  /**
   * isCorporateShareholder is "Y" if carried on farming business as a
   * shareholder of a corporation; otherwise "N".
   *
   * @param  isCorporateShareholder  The new value for this property
   */
  public void setIsCorporateShareholder(final Boolean isCorporateShareholder) {
    this.isCorporateShareholder = isCorporateShareholder;
  }

  /**
   * isDisaster denotes if the "were you unable to complete a production cycle
   * due to isDisaster circumstances?" box is checked.
   *
   * @return  Boolean
   */
  public Boolean getIsDisaster() {
    return isDisaster;
  }

  /**
   * isDisaster denotes if the "were you unable to complete a production cycle
   * due to isDisaster circumstances?" box is checked.
   *
   * @param  isDisaster  The new value for this property
   */
  public void setIsDisaster(final Boolean isDisaster) {
    this.isDisaster = isDisaster;
  }

  /**
   * isPartnershipMember is "Y" if carried on farming business partner of a
   * partnership; otherwise "N".
   *
   * @return  Boolean
   */
  public Boolean getIsPartnershipMember() {
    return isPartnershipMember;
  }

  /**
   * isPartnershipMember is "Y" if carried on farming business partner of a
   * partnership; otherwise "N".
   *
   * @param  isPartnershipMember  The new value for this property
   */
  public void setIsPartnershipMember(final Boolean isPartnershipMember) {
    this.isPartnershipMember = isPartnershipMember;
  }

  /**
   * isSoleProprietor is "Y" if carried on farming business as a sole
   * proprietor; otherwise "N".
   *
   * @return  Boolean
   */
  public Boolean getIsSoleProprietor() {
    return isSoleProprietor;
  }

  /**
   * isSoleProprietor is "Y" if carried on farming business as a sole
   * proprietor; otherwise "N".
   *
   * @param  isSoleProprietor  The new value for this property
   */
  public void setIsSoleProprietor(final Boolean isSoleProprietor) {
    this.isSoleProprietor = isSoleProprietor;
  }

  /**
   * OtherText is any additional justification or supporting details provided by
   * participant or administration.
   *
   * @return  String
   */
  public String getOtherText() {
    return otherText;
  }

  /**
   * OtherText is any additional justification or supporting details provided by
   * participant or administration.
   *
   * @param  otherText  The new value for this property
   */
  public void setOtherText(final String otherText) {
    this.otherText = otherText;
  }

  /**
   * PostMarkDate is the Date the Form was Postmarked. Will be received date if
   * received before filing deadline.
   *
   * @return  Date
   */
  public Date getPostMarkDate() {
    return postMarkDate;
  }

  /**
   * PostMarkDate is the Date the Form was Postmarked. Will be received date if
   * received before filing deadline.
   *
   * @param  postMarkDate  The new value for this property
   */
  public void setPostMarkDate(final Date postMarkDate) {
    this.postMarkDate = postMarkDate;
  }

  /**
   * ProvinceOfResidence denotes the province for the operation making the tax
   * submission.
   *
   * @return  String
   */
  public String getProvinceOfResidence() {
    return provinceOfResidence;
  }

  /**
   * ProvinceOfResidence denotes the province for the operation making the tax
   * submission.
   *
   * @param  provinceOfResidence  The new value for this property
   */
  public void setProvinceOfResidence(final String provinceOfResidence) {
    this.provinceOfResidence = provinceOfResidence;
  }

  /**
   * ReceivedDate is the date the form was received by RCT.
   *
   * @return  Date
   */
  public Date getCraStatementAReceivedDate() {
    return craStatementAReceivedDate;
  }

  /**
   * ReceivedDate is the date the form was received by RCT.
   *
   * @param  craStatementAReceivedDate  The new value for this property
   */
  public void setCraStatementAReceivedDate(final Date craStatementAReceivedDate) {
    this.craStatementAReceivedDate = craStatementAReceivedDate;
  }

  /**
   * IsLastYearFarming indicates if the current ProgramYear was the last year of
   * farming for the client.
   *
   * @return  Boolean
   */
  public Boolean getIsLastYearFarming() {
    return isLastYearFarming;
  }

  /**
   * IsLastYearFarming indicates if the current ProgramYear was the last year of
   * farming for the client.
   *
   * @param  isLastYearFarming  The new value for this property
   */
  public void setIsLastYearFarming(final Boolean isLastYearFarming) {
    this.isLastYearFarming = isLastYearFarming;
  }

  /**
   * DescriptionOfChange summarizes the modifications made to the current
   * ImportVersion since the last ImportVersion. This field will be
   * automatically populated by the import process based on a series of checks
   * against the system.
   *
   * @return  String
   */
  public String getDescriptionOfChange() {
    return descriptionOfChange;
  }

  /**
   * DescriptionOfChange summarizes the modifications made to the current
   * ImportVersion since the last ImportVersion. This field will be
   * automatically populated by the import process based on a series of checks
   * against the system.
   *
   * @param  descriptionOfChange  The new value for this property
   */
  public void setDescriptionOfChange(final String descriptionOfChange) {
    this.descriptionOfChange = descriptionOfChange;
  }

  /**
   * IsCanSendCobToRep Indicates that a copy of the Calculation of Benefits
   * (COB) statement should be sent to the contact person.a.
   *
   * @return  Boolean
   */
  public Boolean getIsCanSendCobToRep() {
    return isCanSendCobToRep;
  }

  /**
   * IsCanSendCobToRep Indicates that a copy of the Calculation of Benefits
   * (COB) statement should be sent to the contact person.a.
   *
   * @param  isCanSendCobToRep  The new value for this property
   */
  public void setIsCanSendCobToRep(final Boolean isCanSendCobToRep) {
    this.isCanSendCobToRep = isCanSendCobToRep;
  }

  /**
   * ProvinceOfMainFarmstead is the main farmstead's Province in the legal land
   * description.
   *
   * @return  String
   */
  public String getProvinceOfMainFarmstead() {
    return provinceOfMainFarmstead;
  }

  /**
   * ProvinceOfMainFarmstead is the main farmstead's Province in the legal land
   * description.
   *
   * @param  provinceOfMainFarmstead  The new value for this property
   */
  public void setProvinceOfMainFarmstead(final String provinceOfMainFarmstead) {
    this.provinceOfMainFarmstead = provinceOfMainFarmstead;
  }

  /**
   * AgristabFedStsCode identifies the federal status code of the application.
   * Possible Values: 1 - Waiting for Data, 2 - In Progress, 3 - Complete -
   * Ineligible, 4 - Complete - Zero Payment, 5 - Complete - Payment.
   *
   * @return  Integer
   */
  public Integer getAgristabFedStsCode() {
    return agristabFedStsCode;
  }

  /**
   * AgristabFedStsCode identifies the federal status code of the application.
   * Possible Values: 1 - Waiting for Data, 2 - In Progress, 3 - Complete -
   * Ineligible, 4 - Complete - Zero Payment, 5 - Complete - Payment.
   *
   * @param  agristabFedStsCode  The new value for this property
   */
  public void setAgristabFedStsCode(final Integer agristabFedStsCode) {
    this.agristabFedStsCode = agristabFedStsCode;
  }

  /**
   * Description for agristabFedStsCode.
   *
   * @return  String
   */
  public String getAgristabFedStsCodeDescription() {
    return agristabFedStsCodeDescription;
  }

  /**
   * Description for agristabFedStsCode.
   *
   * @param  agristabFedStsCodeDescription  The new value for this property
   */
  public void setAgristabFedStsCodeDescription(final String agristabFedStsCodeDescription) {
    this.agristabFedStsCodeDescription = agristabFedStsCodeDescription;
  }


  /**
   * MunicipalityCode denotes the municipality of the FARMSTEAD.
   *
   * @return  String
   */
  public String getMunicipalityCode() {
    return municipalityCode;
  }

  /**
   * MunicipalityCode denotes the municipality of the FARMSTEAD.
   *
   * @param  municipalityCode  The new value for this property
   */
  public void setMunicipalityCode(final String municipalityCode) {
    this.municipalityCode = municipalityCode;
  }

  /**
   * Description for municipalityCode.
   *
   * @return  String
   */
  public String getMunicipalityCodeDescription() {
    return municipalityCodeDescription;
  }

  /**
   * Description for municipalityCode.
   *
   * @param  municipalityCodeDescription  The new value for this property
   */
  public void setMunicipalityCodeDescription(final String municipalityCodeDescription) {
    this.municipalityCodeDescription = municipalityCodeDescription;
  }

  /**
   * ParticipantProfileCode is a unique code for the object
   * participantProfileCode described as a numeric code used to uniquely
   * identify which programs the participant is applying for. Examples of codes
   * and descriptions are 1 - Agri- Stability Only, 2 - Agri-Invest Only, 3 -
   * Agri-Stability and Agri-Invest. Default = 3.
   *
   * @return  String
   */
  public String getParticipantProfileCode() {
    return participantProfileCode;
  }

  /**
   * ParticipantProfileCode is a unique code for the object
   * participantProfileCode described as a numeric code used to uniquely
   * identify which programs the participant is applying for. Examples of codes
   * and descriptions are 1 - Agri- Stability Only, 2 - Agri-Invest Only, 3 -
   * Agri-Stability and Agri-Invest. Default = 3.
   *
   * @param  participantProfileCode  The new value for this property
   */
  public void setParticipantProfileCode(final String participantProfileCode) {
    this.participantProfileCode = participantProfileCode;
  }

  /**
   * Description for participantProfileCode.
   *
   * @return  String
   */
  public String getParticipantProfileCodeDescription() {
    return participantProfileCodeDescription;
  }

  /**
   * Description for participantProfileCode.
   *
   * @param  participantProfileCodeDescription  The new value for this property
   */
  public void setParticipantProfileCodeDescription(final String participantProfileCodeDescription) {
    this.participantProfileCodeDescription = participantProfileCodeDescription;
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
   * Note that the FARM_PROGRAM_YEAR_VERSIONS.REVISION_COUNT is being stored
   * in this field. 
   *
   * @param  revisionCount  The new value for this property
   */
  public void setRevisionCount(final Integer revisionCount) {
    this.revisionCount = revisionCount;
  }

  /**
   * @return  Boolean
   */
  public Boolean getIsLocallyUpdated() {
    return isLocallyUpdated;
  }

  /**
   * @param  isLocallyUpdated  The new value for this property
   */
  public void setIsLocallyUpdated(final Boolean isLocallyUpdated) {
    this.isLocallyUpdated = isLocallyUpdated;
  }

  /**
   * @return the wholeFarmParticipant
   */
  public List<WholeFarmParticipant> getWholeFarmParticipants() {
    return wholeFarmParticipants;
  }

  /**
   * @param wholeFarmParticipants the wholeFarmParticipants to set
   */
  public void setWholeFarmParticipants(List<WholeFarmParticipant> wholeFarmParticipants) {
    if(wholeFarmParticipants != null) {
      for(WholeFarmParticipant cur : wholeFarmParticipants) {
        cur.setFarmingYear(this);
      }
    }
    this.wholeFarmParticipants = wholeFarmParticipants;
  }

  /**
   * @return the referenceScenario
   */
  public ReferenceScenario getReferenceScenario() {
    return referenceScenario;
  }

  /**
   * @param referenceScenario the referenceScenario to set the value to
   */
  public void setReferenceScenario(ReferenceScenario referenceScenario) {
    this.referenceScenario = referenceScenario;
  }

  /**
   * Gets programYearId
   *
   * @return the programYearId
   */
  public Integer getProgramYearId() {
    return programYearId;
  }

  /**
   * Sets programYearId
   *
   * @param pProgramYearId the programYearId to set
   */
  public void setProgramYearId(Integer pProgramYearId) {
    programYearId = pProgramYearId;
  }

  /**
   * IsNonParticipant Indicates that the client is not participating
   * in the Agristability Program for this program year.
   *
   * @return  Boolean
   */
  public Boolean getIsNonParticipant() {
    return isNonParticipant;
  }

  /**
   * IsNonParticipant Indicates that the client is not participating
   * in the Agristability Program for this program year.
   *
   * @param  isNonParticipant  The new value for this property
   */
  public void setIsNonParticipant(final Boolean isNonParticipant) {
    this.isNonParticipant = isNonParticipant;
  }

  public Boolean getIsLateParticipant() {
    return isLateParticipant;
  }

  public void setIsLateParticipant(Boolean isLateParticipant) {
    this.isLateParticipant = isLateParticipant;
  }
  
  public Boolean getIsCashMargins() {
    return isCashMargins;
  }

  public void setIsCashMargins(Boolean isCashMargins) {
    this.isCashMargins = isCashMargins;
  }

  public Date getCashMarginsOptInDate() {
    return cashMarginsOptInDate;
  }

  public void setCashMarginsOptInDate(Date cashMarginsOptInDate) {
    this.cashMarginsOptInDate = cashMarginsOptInDate;
  }

  /**
   * @param operationNumber Integer
   * @return FarmingOperation
   */
  public FarmingOperation getFarmingOperationByNumber(Integer operationNumber) {
    FarmingOperation operation = null;
    if(farmingOperations != null) {
      
      for(FarmingOperation curOperation : farmingOperations) {
        if(curOperation.getOperationNumber().equals(operationNumber)) {
          operation = curOperation;
          break;
        }
      }
    }
    return operation;
  }

  /**
   * @param schedule Integer
   * @return FarmingOperation
   */
  public FarmingOperation getFarmingOperationBySchedule(String schedule) {
    FarmingOperation operation = null;
    if(schedule != null && farmingOperations != null) {
      
      for(FarmingOperation curOperation : farmingOperations) {
        if(curOperation.getSchedule().equals(schedule)) {
          operation = curOperation;
          break;
        }
      }
    }
    return operation;
  }

  /**
   * 
   * @return String
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString(){
    
    Integer farmingOperationsCount = null;
    if(farmingOperations != null) {
      farmingOperationsCount = new Integer(farmingOperations.size());
    }
    
    Integer referenceScenarioId = null;
    if(referenceScenario != null) {
      referenceScenarioId = referenceScenario.getScenarioId();
    }

    return "FarmingOperation"+"\n"+
    "\t referenceScenario : "+referenceScenarioId+"\n"+
    "\t programYearVersionId : "+programYearVersionId+"\n"+
    "\t programYearVersionNumber : "+programYearVersionNumber+"\n"+
    "\t formVersionNumber : "+formVersionNumber+"\n"+
    "\t commonShareTotal : "+commonShareTotal+"\n"+
    "\t farmYears : "+farmYears+"\n"+
    "\t isAccrualWorksheet : "+isAccrualWorksheet+"\n"+
    "\t isCanSendCobToRep : "+isCanSendCobToRep+"\n"+
    "\t isCompletedProdCycle : "+isCompletedProdCycle+"\n"+
    "\t isCwbWorksheet : "+isCwbWorksheet+"\n"+
    "\t isPerishableCommodities : "+isPerishableCommodities+"\n"+
    "\t isReceipts : "+isReceipts+"\n"+
    "\t isAccrualCashConversion : "+isAccrualCashConversion+"\n"+
    "\t isCombinedFarm : "+isCombinedFarm+"\n"+
    "\t isCoopMember : "+isCoopMember+"\n"+
    "\t isCorporateShareholder : "+isCorporateShareholder+"\n"+
    "\t isDisaster : "+isDisaster+"\n"+
    "\t isLastYearFarming : "+isLastYearFarming+"\n"+
    "\t isPartnershipMember : "+isPartnershipMember+"\n"+
    "\t isSoleProprietor : "+isSoleProprietor+"\n"+
    "\t otherText : "+otherText+"\n"+
    "\t postMarkDate : "+postMarkDate+"\n"+
    "\t craStatementAReceivedDate : "+craStatementAReceivedDate+"\n"+
    "\t provinceOfResidence : "+provinceOfResidence+"\n"+
    "\t provinceOfMainFarmstead : "+provinceOfMainFarmstead+"\n"+
    "\t descriptionOfChange : "+descriptionOfChange+"\n"+
    "\t agristabFedStsCode : "+agristabFedStsCode+"\n"+
    "\t agristabFedStsCodeDescription : "+agristabFedStsCodeDescription+"\n"+
    "\t municipalityCode : "+municipalityCode+"\n"+
    "\t municipalityCodeDescription : "+municipalityCodeDescription+"\n"+
    "\t participantProfileCode : "+participantProfileCode+"\n"+
    "\t participantProfileCodeDescription : "+participantProfileCodeDescription+"\n"+
    "\t isLocallyUpdated : "+isLocallyUpdated+"\n"+
    "\t revisionCount : "+revisionCount+"\n"+
    "\t farmingOperations : "+farmingOperationsCount+"\n"+
    "\t benefitCalcMarginTotal : "+marginTotal+"\n"+
    "\t isNonParticipant : "+isNonParticipant+"\n"+
    "\t isLateParticipant : "+isLateParticipant+"\n"+
    "\t isCashMargins : "+isCashMargins+"\n"+
    "\t farmingOperations : "+farmingOperationsCount+"\n"+
    "\t benefit : "+benefit+"\n";
  }

}