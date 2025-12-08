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
package ca.bc.gov.srm.farm.calculator.basic;

import ca.bc.gov.srm.farm.calculator.BenefitValidator;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;

/**
 * @author awilkinson
 */
public class BasicBenefitValidator extends BenefitValidator {
  
  @Override
  public boolean hasEnoughReferenceYears(Scenario programYearScenario) {
    boolean ok = scenarioHasEnoughReferenceYears(programYearScenario);
    return ok;
  }

  /**
   * @param programYearScenario programYearScenario
   * @return true if OK
   */
  @Override
  public boolean validateBpus(Scenario programYearScenario) {
    clearMissingBpuCodes();
    
    // check reference scenarios only
    for(ReferenceScenario rs : programYearScenario.getReferenceScenarios()) {
      
      validateBpus(rs.getFarmingYear());
    }
    
    boolean ok = isValid();
    return ok;
  }

}
