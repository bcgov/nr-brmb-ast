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
package ca.bc.gov.srm.farm.util;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import ca.bc.gov.srm.farm.domain.CombinedFarm;
import ca.bc.gov.srm.farm.domain.Scenario;

/**
 * @author awilkinson
 */
public final class CombinedFarmUtils {

  /** */
  private CombinedFarmUtils() {
  }
  
  
  /**
   * @param scenario Scenario
   * @return boolean
   */
  public static boolean municipalitiesMatch(Scenario scenario) {
    CombinedFarm combinedFarm = scenario.getCombinedFarm();
    Set<String> municipalityCodes = new TreeSet<>();

    List<Scenario> scenarios = combinedFarm.getScenarios();
    for(Scenario curScenario : scenarios ) {
      String municipalityCode = curScenario.getFarmingYear().getMunicipalityCode();
      municipalityCodes.add(municipalityCode);
    }

    int municipalityCount = municipalityCodes.size();
    boolean result = municipalityCount == 1;

    return result;
  }

}
