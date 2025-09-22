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
package ca.bc.gov.srm.farm.ui.struts.calculator.scenarios;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.ui.struts.calculator.search.CalculatorSearchForm;

/**
 * @author awilkinson
 * @created Dec 20, 2010
 */
public class ScenariosForm extends CalculatorSearchForm {

  private static final long serialVersionUID = -2649172921365403100L;
  
  public static final String PURPOSE_ENROLMENT = "ENROLMENT";
  
  // The purpose of loading this scenario.
  // We need a different message depending on the purpose.
  private String purpose;

  private String scenarioStateCode;
  
  private String scenarioCategoryCode;
  
  private boolean defaultInd;
  
  private String stateChangeReason;

  private String scenarioDescription;
  
  private String oldScenarioStateCode;
  
  private boolean assignedToCurrentUser;
  
  private Map<Integer, ReferenceScenario> scenarioByIdMap;

  private List<ListView> scenarioStateSelectOptions;

  private List<ListView> scenarioCategorySelectOptions;

  private List<ListView> scenarioNumberOptions;
  
  private String inProgressCombinedFarmNumber;
  private String verifiedCombinedFarmNumber;
  private boolean combinedFarmChanged;
  
  private List<ListView> verifierOptions;
  private Integer verifierUserId;


  /**
   * @param mapping mapping
   * @param request request
   */
  @Override
  public void reset(ActionMapping mapping, HttpServletRequest request) {
    super.reset(mapping, request);
    setDefaultInd(false);
    setCombinedFarmChanged(false);
  }

  /**
   * @return the purpose
   */
  public String getPurpose() {
    return purpose;
  }

  /**
   * @param purpose the purpose to set
   */
  public void setPurpose(String purpose) {
    this.purpose = purpose;
  }

  /**
   * @return the defaultInd
   */
  public boolean isDefaultInd() {
    return defaultInd;
  }

  /**
   * @param defaultInd the defaultInd to set
   */
  public void setDefaultInd(boolean defaultInd) {
    this.defaultInd = defaultInd;
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
   * @return the stateChangeReason
   */
  public String getStateChangeReason() {
    return stateChangeReason;
  }

  /**
   * @param stateChangeReason the stateChangeReason to set
   */
  public void setStateChangeReason(String stateChangeReason) {
    this.stateChangeReason = stateChangeReason;
  }

  /**
   * @return the oldScenarioStateCode
   */
  public String getOldScenarioStateCode() {
    return oldScenarioStateCode;
  }

  /**
   * @param oldScenarioStateCode the oldScenarioStateCode to set the value to
   */
  public void setOldScenarioStateCode(String oldScenarioStateCode) {
    this.oldScenarioStateCode = oldScenarioStateCode;
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
   * @return the scenarioNumberOptions
   */
  public List<ListView> getScenarioNumberOptions() {
    return scenarioNumberOptions;
  }

  /**
   * @param scenarioNumberOptions the scenarioNumberOptions to set
   */
  public void setScenarioNumberOptions(List<ListView> scenarioNumberOptions) {
    this.scenarioNumberOptions = scenarioNumberOptions;
  }

  /**
   * @return the refScenarioIdMap
   */
  public Map<Integer, ReferenceScenario> getScenarioByIdMap() {
    return scenarioByIdMap;
  }

  /**
   * @param refScenarioIdMap the refScenarioIdMap to set the value to
   */
  public void setScenarioByIdMap(Map<Integer, ReferenceScenario> refScenarioIdMap) {
    this.scenarioByIdMap = refScenarioIdMap;
  }

  /**
   * Gets scenarioStateSelectOptions
   *
   * @return the scenarioStateSelectOptions
   */
  public List<ListView> getScenarioStateSelectOptions() {
    return scenarioStateSelectOptions;
  }

  /**
   * Sets scenarioStateSelectOptions
   *
   * @param pScenarioStateSelectOptions the scenarioStateSelectOptions to set
   */
  public void setScenarioStateSelectOptions(List<ListView> pScenarioStateSelectOptions) {
    scenarioStateSelectOptions = pScenarioStateSelectOptions;
  }
  
  /**
   * Gets scenarioCategorySelectOptions
   *
   * @return the scenarioCategorySelectOptions
   */
  public List<ListView> getScenarioCategorySelectOptions() {
    return scenarioCategorySelectOptions;
  }

  /**
   * Sets scenarioCategorySelectOptions
   *
   * @param pScenarioCategorySelectOptions the scenarioCategorySelectOptions to set
   */
  public void setScenarioCategorySelectOptions(List<ListView> pScenarioCategorySelectOptions) {
    scenarioCategorySelectOptions = pScenarioCategorySelectOptions;
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
   * @return the verifiedCombinedFarmNumber
   */
  public String getVerifiedCombinedFarmNumber() {
    return verifiedCombinedFarmNumber;
  }

  /**
   * @param verifiedCombinedFarmNumber the verifiedCombinedFarmNumber to set
   */
  public void setVerifiedCombinedFarmNumber(String verifiedCombinedFarmNumber) {
    this.verifiedCombinedFarmNumber = verifiedCombinedFarmNumber;
  }

  /**
   * @return the combinedFarmChanged
   */
  public boolean isCombinedFarmChanged() {
    return combinedFarmChanged;
  }

  /**
   * @param combinedFarmChanged the combinedFarmChanged to set
   */
  public void setCombinedFarmChanged(boolean combinedFarmChanged) {
    this.combinedFarmChanged = combinedFarmChanged;
  }

  public List<ListView> getVerifierOptions() {
    return verifierOptions;
  }

  public void setVerifierOptions(List<ListView> verifierOptions) {
    this.verifierOptions = verifierOptions;
  }

  public Integer getVerifierUserId() {
    return verifierUserId;
  }

  public void setVerifierUserId(Integer verifierUserId) {
    this.verifierUserId = verifierUserId;
  }

}
