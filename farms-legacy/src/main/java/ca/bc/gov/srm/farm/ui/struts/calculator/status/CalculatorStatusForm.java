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
package ca.bc.gov.srm.farm.ui.struts.calculator.status;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.ui.struts.calculator.search.CalculatorSearchForm;

/**
 * @author awilkinson
 * @created Dec 21, 2010
 */
public class CalculatorStatusForm extends CalculatorSearchForm {

  private static final long serialVersionUID = -2649172921816403100L;
  
  private List<Integer> refYears;

  private Map<Integer, Boolean> taxYearDataGenerated;
  private Map<Integer, Boolean> newBaseDataArrived;
  
  private boolean hasProgramYearSupplemental;
  private boolean hasCraProgramYearSupplemental;
  private boolean previousYearVerified;
  private boolean previousYearCombinedWholeFarm;
  private boolean bpuSetComplete;
  private boolean fmvSetComplete;
  private boolean nonParticipant;
  private boolean cashMargins;
  private boolean lateParticipant;
  private boolean lateParticipantEnabled;
  
  private boolean assignedToCurrentUser;
  
  private String yearToCreate;
  private String numOperations;
  
  private String reportUrl;
  
  private String inProgressCombinedFarmNumber;
  
  private String localSupplementalReceivedDate;
  private String localStatementAReceivedDate;

  private Date cashMarginsOptInDate;

  /**
   * @param mapping mapping
   * @param request request
   */
  @Override
  public void reset(ActionMapping mapping, HttpServletRequest request) {
    super.reset(mapping, request);
  }

  /**
   * @return the refYearDataGenerated
   */
  public Map<Integer, Boolean> getTaxYearDataGenerated() {
    return taxYearDataGenerated;
  }

  /**
   * @param taxYearDataGenerated the refYearDataGenerated to set the value to
   */
  public void setTaxYearDataGenerated(Map<Integer, Boolean> taxYearDataGenerated) {
    this.taxYearDataGenerated = taxYearDataGenerated;
  }

  /**
   * @return the newBaseDataArrived
   */
  public Map<Integer, Boolean> getNewBaseDataArrived() {
    return newBaseDataArrived;
  }

  /**
   * @param newBaseDataArrived the newBaseDataArrived to set the value to
   */
  public void setNewBaseDataArrived(Map<Integer, Boolean> newBaseDataArrived) {
    this.newBaseDataArrived = newBaseDataArrived;
  }

  /**
   * @return the years
   */
  public List<Integer> getRefYears() {
    return refYears;
  }

  /**
   * @param years the years to set the value to
   */
  public void setRefYears(List<Integer> years) {
    this.refYears = years;
  }

  /**
   * @return the hasProgramYearSupplemental
   */
  public boolean isHasProgramYearSupplemental() {
    return hasProgramYearSupplemental;
  }

  /**
   * @param hasProgramYearSupplemental the hasProgramYearSupplemental to set the value to
   */
  public void setHasProgramYearSupplemental(boolean hasProgramYearSupplemental) {
    this.hasProgramYearSupplemental = hasProgramYearSupplemental;
  }

  /**
   * @return the hasCraProgramYearSupplemental
   */
  public boolean isHasCraProgramYearSupplemental() {
    return hasCraProgramYearSupplemental;
  }

  /**
   * @param hasCraProgramYearSupplemental the hasCraProgramYearSupplemental to set
   */
  public void setHasCraProgramYearSupplemental(boolean hasCraProgramYearSupplemental) {
    this.hasCraProgramYearSupplemental = hasCraProgramYearSupplemental;
  }

  /**
   * @return the previousYearVerified
   */
  public boolean isPreviousYearVerified() {
    return previousYearVerified;
  }

  /**
   * @param previousYearVerified the previousYearVerified to set the value to
   */
  public void setPreviousYearVerified(boolean previousYearVerified) {
    this.previousYearVerified = previousYearVerified;
  }

  /**
   * @return the previousYearCombinedWholeFarm
   */
  public boolean isPreviousYearCombinedWholeFarm() {
    return previousYearCombinedWholeFarm;
  }

  /**
   * @param previousYearCombinedWholeFarm the previousYearCombinedWholeFarm to set the value to
   */
  public void setPreviousYearCombinedWholeFarm(boolean previousYearCombinedWholeFarm) {
    this.previousYearCombinedWholeFarm = previousYearCombinedWholeFarm;
  }

  /**
   * @return the bpuSetComplete
   */
  public boolean isBpuSetComplete() {
    return bpuSetComplete;
  }

  /**
   * @param bpuSetComplete the bpuSetComplete to set the value to
   */
  public void setBpuSetComplete(boolean bpuSetComplete) {
    this.bpuSetComplete = bpuSetComplete;
  }

  /**
   * @return the fmvSetComplete
   */
  public boolean isFmvSetComplete() {
    return fmvSetComplete;
  }

  /**
   * @param fmvSetComplete the fmvSetComplete to set the value to
   */
  public void setFmvSetComplete(boolean fmvSetComplete) {
    this.fmvSetComplete = fmvSetComplete;
  }

  /**
   * @return the assignedToCurrentUser
   */
  public boolean isAssignedToCurrentUser() {
    return assignedToCurrentUser;
  }

  /**
   * @param assignedToCurrentUser the assignedToCurrentUser to set the value to
   */
  public void setAssignedToCurrentUser(boolean assignedToCurrentUser) {
    this.assignedToCurrentUser = assignedToCurrentUser;
  }

  /**
   * @return the missingYear
   */
  public String getYearToCreate() {
    return yearToCreate;
  }

  /**
   * @param missingYear the missingYear to set the value to
   */
  public void setYearToCreate(String missingYear) {
    this.yearToCreate = missingYear;
  }

  /**
   * @return the numOperations
   */
  public String getNumOperations() {
    return numOperations;
  }

  /**
   * @param numOperations the numOperations to set the value to
   */
  public void setNumOperations(String numOperations) {
    this.numOperations = numOperations;
  }

  /**
   * @return the reportUrl
   */
  public String getReportUrl() {
    return reportUrl;
  }

  /**
   * @param reportUrl the reportUrl to set the value to
   */
  public void setReportUrl(String reportUrl) {
    this.reportUrl = reportUrl;
  }

  /**
   * @return the inProgressCombinedFarmNumber
   */
  public String getInProgressCombinedFarmNumber() {
    return inProgressCombinedFarmNumber;
  }

  /**
   * @param inProgressCombinedFarmNumber the inProgressCombinedFarmNumber to set
   */
  public void setInProgressCombinedFarmNumber(String inProgressCombinedFarmNumber) {
    this.inProgressCombinedFarmNumber = inProgressCombinedFarmNumber;
  }

  /**
   * @return the nonParticipant
   */
  public boolean isNonParticipant() {
    return nonParticipant;
  }

  /**
   * @param nonParticipant the nonParticipant to set
   */
  public void setNonParticipant(boolean nonParticipant) {
    this.nonParticipant = nonParticipant;
  }
  
  public boolean isCashMargins() {
    return cashMargins;
  }

  public void setCashMargins(boolean cashMargins) {
    this.cashMargins = cashMargins;
  }

  public boolean isLateParticipant() {
    return lateParticipant;
  }

  public void setLateParticipant(boolean lateParticipant) {
    this.lateParticipant = lateParticipant;
  }

  public boolean isLateParticipantEnabled() {
    return lateParticipantEnabled;
  }

  public void setLateParticipantEnabled(boolean lateParticipantEnabled) {
    this.lateParticipantEnabled = lateParticipantEnabled;
  }

  public String getLocalSupplementalReceivedDate() {
    return localSupplementalReceivedDate;
  }

  public void setLocalSupplementalReceivedDate(String localSupplementalReceivedDate) {
    this.localSupplementalReceivedDate = localSupplementalReceivedDate;
  }

  public String getLocalStatementAReceivedDate() {
    return localStatementAReceivedDate;
  }

  public void setLocalStatementAReceivedDate(String localStatementAReceivedDate) {
    this.localStatementAReceivedDate = localStatementAReceivedDate;
  }

  public Date getCashMarginsOptInDate() {
    return cashMarginsOptInDate;
  }

  public void setCashMarginsOptInDate(Date cashMarginsOptInDate) {
    this.cashMarginsOptInDate = cashMarginsOptInDate;
  }
  
}
