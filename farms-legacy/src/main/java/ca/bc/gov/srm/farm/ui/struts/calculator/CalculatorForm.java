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
package ca.bc.gov.srm.farm.ui.struts.calculator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

/**
 * 
 * @author awilkinson
 */
public class CalculatorForm extends ValidatorForm {

  private static final long serialVersionUID = -2627402921365403105L;

  public static final String FARM_VIEW_WHOLE = "WHOLE";

  private int pin;
  private int year;
  private Integer scenarioNumber;
  
  private Integer scenarioRevisionCount;
  
  private boolean refresh = false;

  private boolean readOnly = true;
  
  private boolean canModifyScenario = false;

  private String farmView;
  
  private List<String> requiredYears;
  
  private List<String> referenceYears;

  private List<ListView> farmViewOptions;
  
  private Map<Integer, ReferenceScenario> yearScenarioMap;
  
  private String user;

  private String userGuid;

  /** Inbox fields */
  private int inboxYear;
  private String inboxSearchType;
  private boolean inProgressCB;
  private boolean verifiedCB;
  private boolean onHoldCB;
  private boolean closedCB;

  /**
   * @param mapping mapping
   * @param request request
   */
  @Override
  public void reset(ActionMapping mapping, HttpServletRequest request) {
    super.reset(mapping, request);
    setRefresh(false);
    setReadOnly(true);
  }

  /**
   * @return the pin
   */
  public int getPin() {
    return pin;
  }

  /**
   * @param pin the pin to set
   */
  public void setPin(int pin) {
    this.pin = pin;
  }

  /**
   * @return int
   */
  public int getYear() {
    return year;
  }

  /**
   * @param year int
   */
  public void setYear(int year) {
    this.year = year;
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
    if(scenarioNumber != null && scenarioNumber.intValue() == 0) {
      this.scenarioNumber = null;
    } else {
      this.scenarioNumber = scenarioNumber;
    }
  }

  /**
   * @return the refresh
   */
  public boolean isRefresh() {
    return refresh;
  }

  /**
   * @param refresh the refresh to set
   */
  public void setRefresh(boolean refresh) {
    this.refresh = refresh;
  }


  /**
   * @return the readOnly
   */
  public boolean isReadOnly() {
    return readOnly;
  }

  /**
   * @param readOnly the readOnly to set the value to
   */
  public void setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
  }

  /**
   * @return the canModifyScenario
   */
  public boolean isCanModifyScenario() {
    return canModifyScenario;
  }

  /**
   * @param canModifyScenario the canModifyScenario to set
   */
  public void setCanModifyScenario(boolean canModifyScenario) {
    this.canModifyScenario = canModifyScenario;
  }

  /**
   * @return the refScenarioMap
   */
  public Map<Integer, ReferenceScenario> getYearScenarioMap() {
    return yearScenarioMap;
  }

  /**
   * @param refScenarioMap the refScenarioMap to set the value to
   */
  public void setYearScenarioMap(Map<Integer, ReferenceScenario> refScenarioMap) {
    this.yearScenarioMap = refScenarioMap;
  }

  /**
   * @return the farmView
   */
  public String getFarmView() {
    return farmView;
  }


  /**
   * @param farmView the farmView to set
   */
  public void setFarmView(String farmView) {
    this.farmView = farmView;
  }

  /**
   * @return the scenarioRevisionCount
   */
  public Integer getScenarioRevisionCount() {
    return scenarioRevisionCount;
  }

  /**
   * @param scenarioRevisionCount the scenarioRevisionCount to set the value to
   */
  public void setScenarioRevisionCount(Integer scenarioRevisionCount) {
    this.scenarioRevisionCount = scenarioRevisionCount;
  }

  /**
   * @return the farmViewOptions
   */
  public List<ListView> getFarmViewOptions() {
    return farmViewOptions;
  }
  
  /**
   * @return the requiredYears
   */
  public List<String> getRequiredYears() {
    return requiredYears;
  }

  /**
   * @param requiredYears the requiredYears to set the value to
   */
  public void setRequiredYears(List<String> requiredYears) {
    this.requiredYears = requiredYears;
  }

  /**
   * @return the referenceYears
   */
  public List<String> getReferenceYears() {
    return referenceYears;
  }

  /**
   * @param referenceYears the referenceYears to set
   */
  public void setReferenceYears(List<String> referenceYears) {
    this.referenceYears = referenceYears;
  }

  /**
   * @return boolean
   */
  public boolean isWholeFarmView() {
    return CalculatorAction.FARM_VIEW_WHOLE_FARM_CODE.equals(getFarmView());
  }

  /**
   * @param farmViewOptions the farmViewOptions to set the value to
   */
  public void setFarmViewOptions(List<ListView> farmViewOptions) {
    this.farmViewOptions = farmViewOptions;
  }

  /**
   * @return the user
   */
  public String getUser() {
    return user;
  }

  /**
   * @param user the user to set
   */
  public void setUser(String user) {
    this.user = user;
  }

  /**
   * @return the userGuid
   */
  public String getUserGuid() {
    return userGuid;
  }

  /**
   * @param userGuid the userGuid to set
   */
  public void setUserGuid(String userGuid) {
    this.userGuid = userGuid;
  }

  /**
   * @return the inboxSearchType
   */
  public String getInboxSearchType() {
    return inboxSearchType;
  }

  /**
   * @param inboxSearchType the inboxSearchType to set the value to
   */
  public void setInboxSearchType(String inboxSearchType) {
    this.inboxSearchType = inboxSearchType;
  }

  /**
   * @return the verifiedCB
   */
  public boolean isVerifiedCB() {
    return verifiedCB;
  }

  /**
   * @param verifiedCB the verifiedCB to set the value to
   */
  public void setVerifiedCB(boolean verifiedCB) {
    this.verifiedCB = verifiedCB;
  }

  /**
   * @return the closedCB
   */
  public boolean isClosedCB() {
    return closedCB;
  }

  /**
   * @param closedCB the closedCB to set the value to
   */
  public void setClosedCB(boolean closedCB) {
    this.closedCB = closedCB;
  }

  /**
   * @return the inProgressCB
   */
  public boolean isInProgressCB() {
    return inProgressCB;
  }

  /**
   * @param inProgressCB the inProgressCB to set the value to
   */
  public void setInProgressCB(boolean inProgressCB) {
    this.inProgressCB = inProgressCB;
  }

  /**
   * @return the onHoldCB
   */
  public boolean isOnHoldCB() {
    return onHoldCB;
  }

  /**
   * @param onHoldCB the onHoldCB to set the value to
   */
  public void setOnHoldCB(boolean onHoldCB) {
    this.onHoldCB = onHoldCB;
  }

  /**
   * @return the inboxYear
   */
  public int getInboxYear() {
    return inboxYear;
  }

  /**
   * @param inboxYear the inboxYear to set the value to
   */
  public void setInboxYear(int inboxYear) {
    this.inboxYear = inboxYear;
  }

  /**
   * @return Map
   */
  public Map<String, String> getScenarioParams() {
    Map<String, String> params = new HashMap<>();
    params.put("pin", Integer.toString(pin));
    params.put("year", Integer.toString(year));
    params.put("scenarioNumber", String.valueOf(scenarioNumber));
    return params;
  }
  
  /**
   * @return String
   */
  public String getScenarioParamsString() {
    StringBuffer sb = new StringBuffer();
    sb.append("pin=");
    sb.append(Integer.toString(pin));
    sb.append("&year=");
    sb.append(Integer.toString(year));
    sb.append("&scenarioNumber=");
    sb.append(String.valueOf(scenarioNumber));
    return sb.toString();
  }
  
  /**
   * @return true if program year is 2013 or later
   */
  public boolean isGrowingForward2013() {
    return year >= CalculatorConfig.GROWING_FORWARD_2013;
  }
  
  /**
   * @return true if program year has Enhanced Benefits
   */
  public boolean getHasEnhancedBenefits() {
    return ScenarioUtils.hasEnhancedBenefits(getScenario());
  }
  
  /**
   * @return true if program year is 2017 or later
   */
  public boolean isDisplayLateParticipant() {
    return year >= CalculatorConfig.GROWING_FORWARD_2017;
  }
  
  /**
   * @return true if program year is 2017
   */
  public boolean isGrowingForward2017() {
    return year >= CalculatorConfig.GROWING_FORWARD_2017;
  }
  
  /**
   * @return true if program year is 2018
   */
  public boolean isGrowingForward2018() {
    return year >= CalculatorConfig.GROWING_FORWARD_2018;
  }
  
  /**
   * @return true if program year is 2019
   */
  public boolean isGrowingForward2019() {
    return year == CalculatorConfig.GROWING_FORWARD_2019;
  }
  
  /**
   * @return true if program year is 2020
   */
  public boolean isGrowingForward2020() {
    return year == CalculatorConfig.GROWING_FORWARD_2020;
  }
  
  /**
   * @return true if program year is 2021 to 2022
   */
  public boolean isGrowingForward2021() {
    return year >= CalculatorConfig.GROWING_FORWARD_2021;
  }
  
  /**
   * @return true if program year is 2023 or later
   */
  public boolean isGrowingForward2023() {
    return year >= CalculatorConfig.GROWING_FORWARD_2023;
  }
  
  /**
   * @return true if program year is 2024 or later
   */
  public boolean isGrowingForward2024() {
    return year >= CalculatorConfig.GROWING_FORWARD_2024;
  }
  
  public Scenario getScenario() {
    Scenario scenario = (Scenario) CacheFactory.getUserCache()
        .getItem(CacheKeys.getScenarioCacheKey(getPin(), getYear(), getScenarioNumber()));
    return scenario;
  }
}
