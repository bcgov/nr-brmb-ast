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
package ca.bc.gov.srm.farm.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ca.bc.gov.srm.farm.util.ScenarioUtils;

/**
 * @author awilkinson
 */
public class CombinedFarm implements Serializable {

  private static final long serialVersionUID = 5428617034927402788L;

  private List<Scenario> scenarios;
  
  private Benefit benefit;
  
  private Map<Integer, MarginTotal> yearMargins;
  
  private Map<Integer, Boolean> deemedFarmingYearMap;

  public List<Scenario> getScenarios() {
    return scenarios;
  }

  public void setScenarios(List<Scenario> scenarios) {
    this.scenarios = scenarios;
  }

  public Benefit getBenefit() {
    return benefit;
  }

  public void setBenefit(Benefit benefit) {
    this.benefit = benefit;
  }

  public Map<Integer, MarginTotal> getYearMargins() {
    return yearMargins;
  }

  public void setYearMargins(Map<Integer, MarginTotal> yearMargins) {
    this.yearMargins = yearMargins;
  }

  public Map<Integer, Boolean> getDeemedFarmingYearMap() {
    return deemedFarmingYearMap;
  }

  public void setDeemedFarmingYearMap(Map<Integer, Boolean> deemedFarmingYearMap) {
    this.deemedFarmingYearMap = deemedFarmingYearMap;
  }

  public List<ReferenceScenario> getReferenceScenariosByYear(Integer year) {
    List<ReferenceScenario> result = new ArrayList<>();
    for(Scenario curScenario : scenarios) {
      ReferenceScenario rs = curScenario.getReferenceScenarioByYear(year);
      if(rs != null) {
        result.add(rs);
      }
    }
    return result;
  }
  
  public List<ReferenceScenario> getAllReferenceScenarios() {
    List<ReferenceScenario> result = new ArrayList<>();
    for(Scenario curScenario : getScenarios()) {
      result.addAll(curScenario.getReferenceScenarios());
    }
    return result;
  }

  @JsonIgnore
  public List<Integer> getAllYears() {
    Scenario firstScenario = scenarios.get(0);
    Integer programYear = firstScenario.getYear();
    List<Integer> result = getReferenceYears();
    result.add(programYear);
    return result;
  }
  
  @JsonIgnore
  public List<Integer> getAllYearsIncludingMissing() {
    Scenario firstScenario = scenarios.get(0);
    Integer programYear = firstScenario.getYear();
    return ScenarioUtils.getAllYears(programYear);
  }
  
  @JsonIgnore
  public List<Integer> getReferenceYears() {
    Scenario firstScenario = scenarios.get(0);
    Integer programYear = firstScenario.getYear();
    List<Integer> allRefYears = ScenarioUtils.getReferenceYears(programYear);
    List<Integer> result = new ArrayList<>();
    for(Integer curYear : allRefYears) {
      boolean hasDataForYear = firstScenario.hasDataForYear(curYear);
      if(hasDataForYear) {
        result.add(curYear);
      }
    }
    return result;
  }
  
  @JsonIgnore
  public List<Integer> getReferenceYearsIncludingMissing() {
    Scenario firstScenario = scenarios.get(0);
    Integer programYear = firstScenario.getYear();
    List<Integer> result = ScenarioUtils.getReferenceYears(programYear);
    return result;
  }

  @JsonIgnore
  public Integer getProgramYear() {
    Scenario firstScenario = scenarios.get(0);
    Integer programYear = firstScenario.getYear();
    return programYear;
  }
}
