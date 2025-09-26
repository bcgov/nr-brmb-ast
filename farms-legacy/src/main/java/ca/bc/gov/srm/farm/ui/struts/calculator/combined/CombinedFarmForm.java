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
package ca.bc.gov.srm.farm.ui.struts.calculator.combined;

import java.util.HashMap;
import java.util.Map;

import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorForm;

/**
 * @author awilkinson
 */
public class CombinedFarmForm extends CalculatorForm {

  private static final long serialVersionUID = -7607163032134144021L;

  private String action;

  private String pinToAdd;
  
  private String scenarioIdToRemove;
  
  private Map<String, String> combinedScenarioNumbers = new HashMap<>();

  private Map<String, String> combinedScenarioIds = new HashMap<>();


  /**
   * @return the pinToAdd
   */
  public String getPinToAdd() {
    return pinToAdd;
  }

  /**
   * @param pinToAdd the pinToAdd to set
   */
  public void setPinToAdd(String pinToAdd) {
    this.pinToAdd = pinToAdd;
  }

  /**
   * @return the scenarioIdToRemove
   */
  public String getScenarioIdToRemove() {
    return scenarioIdToRemove;
  }

  /**
   * @param scenarioIdToRemove the scenarioIdToRemove to set
   */
  public void setScenarioIdToRemove(String scenarioIdToRemove) {
    this.scenarioIdToRemove = scenarioIdToRemove;
  }

  /**
   * @return the scenarioNumbers
   */
  public Map<String, String> getCombinedScenarioNumbers() {
    return combinedScenarioNumbers;
  }

  /**
   * @param pin pin as string
   * @return scenario number as string
   */
  public String getCombinedScenarioNumber(String pin) {
    String scNum = combinedScenarioNumbers.get(pin);
    return scNum;
  }
  
  /**
   * @param pin pin as string
   * @param scenarioNumber scenario number as string
   */
  public void setCombinedScenarioNumber(String pin, String scenarioNumber) {
    combinedScenarioNumbers.put(pin, scenarioNumber);
  }

  /**
   * @return the combinedScenarioIds
   */
  public Map<String, String> getCombinedScenarioIds() {
    return combinedScenarioIds;
  }

  /**
   * @param pin pin as string
   * @return scenario ID as string
   */
  public String getCombinedScenarioId(String pin) {
    String scNum = combinedScenarioIds.get(pin);
    return scNum;
  }
  
  /**
   * @param pin pin as string
   * @param scenarioId scenario ID as string
   */
  public void setCombinedScenarioId(String pin, String scenarioId) {
    combinedScenarioIds.put(pin, scenarioId);
  }

  /**
   * @return the action
   */
  public String getAction() {
    return action;
  }

  /**
   * @param action the action to set
   */
  public void setAction(String action) {
    this.action = action;
  }
}
