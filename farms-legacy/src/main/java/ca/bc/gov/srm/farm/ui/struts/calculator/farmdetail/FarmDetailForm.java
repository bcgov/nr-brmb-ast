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
package ca.bc.gov.srm.farm.ui.struts.calculator.farmdetail;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.ui.struts.calculator.search.CalculatorSearchForm;

/**
 * 
 * @author awilkinson
 *
 */
public class FarmDetailForm extends CalculatorSearchForm {

  private static final long serialVersionUID = -2645502781365403100L;

  /**
   * commonShareTotal is the Outstanding Common SharesIndividual: zero; Entity:
   * >= zero.a.
   */
  private String commonShareTotal;

  /** farmYears is the number of years the farm has been in operation. */
  private String farmYears;

  /**
   * isAccrualWorksheet denotes if the "Accrual Reference Margin Worksheet" box
   * is checked.
   */
  private boolean accrualWorksheet;

  /**
   * isCompletedProdCycle denotes if the "Have you completed a production cycle
   * on at least one of the commodities you produced?" box is checked.
   */
  private boolean completedProdCycle;

  /**
   * isCwbWorksheet denotes if the "CWB Adjustment Worksheet" box is checked.
   */
  private boolean cwbWorksheet;

  /**
   * isPerishableCommodities denotes if the Perishable Commodities Worksheet box
   * is checked.
   */
  private boolean perishableCommodities;

  /** isReceipts denotes if receipts are available and included with report. */
  private boolean receipts;

  /**
   * isAccrualCashConversion denotes if the Accrual to Cash / Cash to Accrual
   * Conversions box is checked.
   */
  private boolean accrualCashConversion;

  /** isCombinedFarm as indicated by the participant. */
  private boolean combinedFarm;

  /**
   * isCoopMember is "Y" if carried on farming business as a member of a co-
   * operative; otherwise "N".
   */
  private boolean coopMember;

  /**
   * isCorporateShareholder is "Y" if carried on farming business as a
   * shareholder of a corporation; otherwise "N".
   */
  private boolean corporateShareholder;

  /**
   * isDisaster denotes if the "were you unable to complete a production cycle
   * due to disaster circumstances?" box is checked.
   */
  private boolean disaster;

  /**
   * isPartnershipMember is "Y" if carried on farming business partner of a
   * partnership; otherwise "N".
   */
  private boolean partnershipMember;

  /**
   * isSoleProprietor is "Y" if carried on farming business as a sole
   * proprietor; otherwise "N".
   */
  private boolean soleProprietor;

  /**
   * otherText is any additional justification or supporting details provided by
   * participant or administration.
   */
  private String otherText;

  /**
   * provinceOfResidence denotes the province for the operation making the tax
   * submission.
   */
  private String provinceOfResidence;

  /**
   * isLastYearFarming indicates if the current ProgramYear was the last year of
   * farming for the client.
   */
  private boolean lastYearFarming;

  /**
   * isCanSendCobToRep Indicates that a copy of the Calculation of Benefits
   * (COB) statement should be sent to the contact person.a.
   */
  private boolean canSendCobToRep;

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
  private String agristabFedStsCode;

  /** municipalityCode denotes the municipality of the FARMSTEAD. */
  private String municipalityCode;
  
  private String municipalityCodeDescription;

  /**
   * participantProfileCode is a unique code for the object
   * participantProfileCode described as a numeric code used to uniquely
   * identify which programs the participant is applying for. Examples of codes
   * and descriptions are 1 - Agri- Stability Only, 2 - Agri-Invest Only, 3 -
   * Agri-Stability and Agri-Invest. Default = 3.
   */
  private String participantProfileCode;

  /**
   * revisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   */
  private String programYearVersionRevisionCount;
  
  
  private String localSupplementalReceivedDate;
  
  private String localStatementAReceivedDate;
  
  private boolean municipalityLocked;
  
  private boolean cashMargins;
  private String cashMarginsOptInDate;

  /**
   * @param mapping mapping
   * @param request request
   */
  @Override
  public void reset(ActionMapping mapping, HttpServletRequest request) {
    super.reset(mapping, request);
    setAccrualCashConversion(false);
    setCanSendCobToRep(false);
    setCombinedFarm(false);
    setCompletedProdCycle(false);
    setCoopMember(false);
    setCorporateShareholder(false);
    setCwbWorksheet(false);
    setDisaster(false);
    setLastYearFarming(false);
    setPartnershipMember(false);
    setReceipts(false);
    setSoleProprietor(false);
  }


  /**
   * @return the farmYears
   */
  public String getFarmYears() {
    return farmYears;
  }


  /**
   * @param farmYears the farmYears to set
   */
  public void setFarmYears(String farmYears) {
    this.farmYears = farmYears;
  }

  /**
   * @return the isAccrualCashConversion
   */
  public boolean isAccrualCashConversion() {
    return accrualCashConversion;
  }

  /**
   * @param isAccrualCashConversion the isAccrualCashConversion to set
   */
  public void setAccrualCashConversion(boolean isAccrualCashConversion) {
    this.accrualCashConversion = isAccrualCashConversion;
  }

  /**
   * @return the isCanSendCobToRep
   */
  public boolean isCanSendCobToRep() {
    return canSendCobToRep;
  }

  /**
   * @param isCanSendCobToRep the isCanSendCobToRep to set
   */
  public void setCanSendCobToRep(boolean isCanSendCobToRep) {
    this.canSendCobToRep = isCanSendCobToRep;
  }

  /**
   * @return the isCombinedFarm
   */
  public boolean isCombinedFarm() {
    return combinedFarm;
  }

  /**
   * @param isCombinedFarm the isCombinedFarm to set
   */
  public void setCombinedFarm(boolean isCombinedFarm) {
    this.combinedFarm = isCombinedFarm;
  }

  /**
   * @return the isCompletedProdCycle
   */
  public boolean isCompletedProdCycle() {
    return completedProdCycle;
  }

  /**
   * @param isCompletedProdCycle the isCompletedProdCycle to set
   */
  public void setCompletedProdCycle(boolean isCompletedProdCycle) {
    this.completedProdCycle = isCompletedProdCycle;
  }

  /**
   * @return the isCoopMember
   */
  public boolean isCoopMember() {
    return coopMember;
  }

  /**
   * @param isCoopMember the isCoopMember to set
   */
  public void setCoopMember(boolean isCoopMember) {
    this.coopMember = isCoopMember;
  }

  /**
   * @return the isCorporateShareholder
   */
  public boolean isCorporateShareholder() {
    return corporateShareholder;
  }

  /**
   * @param isCorporateShareholder the isCorporateShareholder to set
   */
  public void setCorporateShareholder(boolean isCorporateShareholder) {
    this.corporateShareholder = isCorporateShareholder;
  }

  /**
   * @return the isCwbWorksheet
   */
  public boolean isCwbWorksheet() {
    return cwbWorksheet;
  }

  /**
   * @param isCwbWorksheet the isCwbWorksheet to set
   */
  public void setCwbWorksheet(boolean isCwbWorksheet) {
    this.cwbWorksheet = isCwbWorksheet;
  }

  /**
   * @return the isDisaster
   */
  public boolean isDisaster() {
    return disaster;
  }

  /**
   * @param isDisaster the isDisaster to set
   */
  public void setDisaster(boolean isDisaster) {
    this.disaster = isDisaster;
  }

  /**
   * @return the isLastYearFarming
   */
  public boolean isLastYearFarming() {
    return lastYearFarming;
  }

  /**
   * @param isLastYearFarming the isLastYearFarming to set
   */
  public void setLastYearFarming(boolean isLastYearFarming) {
    this.lastYearFarming = isLastYearFarming;
  }

  /**
   * @return the isPartnershipMember
   */
  public boolean isPartnershipMember() {
    return partnershipMember;
  }

  /**
   * @param isPartnershipMember the isPartnershipMember to set
   */
  public void setPartnershipMember(boolean isPartnershipMember) {
    this.partnershipMember = isPartnershipMember;
  }

  /**
   * @return the isReceipts
   */
  public boolean isReceipts() {
    return receipts;
  }

  /**
   * @param isReceipts the isReceipts to set
   */
  public void setReceipts(boolean isReceipts) {
    this.receipts = isReceipts;
  }

  /**
   * @return the isSoleProprietor
   */
  public boolean isSoleProprietor() {
    return soleProprietor;
  }

  /**
   * @param isSoleProprietor the isSoleProprietor to set
   */
  public void setSoleProprietor(boolean isSoleProprietor) {
    this.soleProprietor = isSoleProprietor;
  }

  /**
   * @return the municipalityCode
   */
  public String getMunicipalityCode() {
    return municipalityCode;
  }

  /**
   * @param municipalityCode the municipalityCode to set
   */
  public void setMunicipalityCode(String municipalityCode) {
    this.municipalityCode = municipalityCode;
  }

  /**
   * @return the otherText
   */
  public String getOtherText() {
    return otherText;
  }

  /**
   * @param otherText the otherText to set
   */
  public void setOtherText(String otherText) {
    this.otherText = otherText;
  }

  /**
   * @return the provinceOfMainFarmstead
   */
  public String getProvinceOfMainFarmstead() {
    return provinceOfMainFarmstead;
  }

  /**
   * @param provinceOfMainFarmstead the provinceOfMainFarmstead to set
   */
  public void setProvinceOfMainFarmstead(String provinceOfMainFarmstead) {
    this.provinceOfMainFarmstead = provinceOfMainFarmstead;
  }

  /**
   * @return the provinceOfResidence
   */
  public String getProvinceOfResidence() {
    return provinceOfResidence;
  }

  /**
   * @param provinceOfResidence the provinceOfResidence to set
   */
  public void setProvinceOfResidence(String provinceOfResidence) {
    this.provinceOfResidence = provinceOfResidence;
  }

  /**
   * @return the programYearVersionRevisionCount
   */
  public String getProgramYearVersionRevisionCount() {
    return programYearVersionRevisionCount;
  }


  /**
   * @param programYearVersionRevisionCount the programYearVersionRevisionCount to set
   */
  public void setProgramYearVersionRevisionCount(String programYearVersionRevisionCount) {
    this.programYearVersionRevisionCount = programYearVersionRevisionCount;
  }


  /**
   * @return the commonShareTotal
   */
  public String getCommonShareTotal() {
    return commonShareTotal;
  }


  /**
   * @param commonShareTotal the commonShareTotal to set
   */
  public void setCommonShareTotal(String commonShareTotal) {
    this.commonShareTotal = commonShareTotal;
  }


  /**
   * @return the isAccrualWorksheet
   */
  public boolean isAccrualWorksheet() {
    return accrualWorksheet;
  }

  /**
   * @param isAccrualWorksheet the isAccrualWorksheet to set
   */
  public void setAccrualWorksheet(boolean isAccrualWorksheet) {
    this.accrualWorksheet = isAccrualWorksheet;
  }

  /**
   * @return the participantProfileCode
   */
  public String getParticipantProfileCode() {
    return participantProfileCode;
  }

  /**
   * @param participantProfileCode the participantProfileCode to set
   */
  public void setParticipantProfileCode(String participantProfileCode) {
    this.participantProfileCode = participantProfileCode;
  }

  /**
   * @return the perishableCommodities
   */
  public boolean isPerishableCommodities() {
    return perishableCommodities;
  }

  /**
   * @param perishableCommodities the perishableCommodities to set
   */
  public void setPerishableCommodities(boolean perishableCommodities) {
    this.perishableCommodities = perishableCommodities;
  }


  /**
   * @return the agristabFedStsCode
   */
  public String getAgristabFedStsCode() {
    return agristabFedStsCode;
  }


  /**
   * @param agristabFedStsCode the agristabFedStsCode to set
   */
  public void setAgristabFedStsCode(String agristabFedStsCode) {
    this.agristabFedStsCode = agristabFedStsCode;
  }

  public String getLocalSupplementalReceivedDate() {
    return localSupplementalReceivedDate;
  }

  public void setLocalSupplementalReceivedDate(String localSupplementalReceivedDate) {
    this.localSupplementalReceivedDate = localSupplementalReceivedDate;
  }

  public boolean isMunicipalityLocked() {
    return municipalityLocked;
  }

  public void setMunicipalityLocked(boolean municipalityLocked) {
    this.municipalityLocked = municipalityLocked;
  }

  public String getMunicipalityCodeDescription() {
    return municipalityCodeDescription;
  }

  public void setMunicipalityCodeDescription(String municipalityCodeDescription) {
    this.municipalityCodeDescription = municipalityCodeDescription;
  }

  public String getLocalStatementAReceivedDate() {
    return localStatementAReceivedDate;
  }

  public void setLocalStatementAReceivedDate(String localStatementAReceivedDate) {
    this.localStatementAReceivedDate = localStatementAReceivedDate;
  }

  public boolean isCashMargins() {
    return cashMargins;
  }

  public void setCashMargins(boolean cashMargins) {
    this.cashMargins = cashMargins;
  }


  public String getCashMarginsOptInDate() {
    return cashMarginsOptInDate;
  }


  public void setCashMarginsOptInDate(String cashMarginsOptInDate) {
    this.cashMarginsOptInDate = cashMarginsOptInDate;
  }

}
