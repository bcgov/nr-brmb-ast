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
package ca.bc.gov.srm.farm.calculator.combined;

import java.util.List;

import ca.bc.gov.srm.farm.calculator.BenefitValidator;
import ca.bc.gov.srm.farm.domain.CombinedFarm;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;

/**
 * @author awilkinson
 */
public class CombinedBenefitValidator extends BenefitValidator {
  
  @Override
  public boolean hasEnoughReferenceYears(Scenario programYearScenario) {
    CombinedFarm combinedFarm = programYearScenario.getCombinedFarm();
    List<Scenario> scenarios = combinedFarm.getScenarios();
    boolean ok = true;
    
    for(Scenario curScenario : scenarios) {
      ok = scenarioHasEnoughReferenceYears(curScenario);
      if(!ok) {
        break;
      }
    }
    return ok;
  }


  @Override
  public boolean validateBpus(Scenario programYearScenario) {
    clearMissingBpuCodes();
    
    CombinedFarm combinedFarm = programYearScenario.getCombinedFarm();
    List<Scenario> scenarios = combinedFarm.getScenarios();
    
    for(Scenario curScenario : scenarios) {
      
      // For combined farm we need to check the program year as well as the reference years
      // because BPUs are used in the calculation of applied benefit percent (combined farm %) 
      for(ReferenceScenario rs : curScenario.getAllScenarios()) {
        validateBpus(rs.getFarmingYear());
      }
    }
    
    boolean ok = isValid();
    return ok;
  }

}
