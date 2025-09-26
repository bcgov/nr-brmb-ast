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

import ca.bc.gov.srm.farm.calculator.BenefitNullFixer;
import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes;
import ca.bc.gov.srm.farm.domain.codes.StructuralChangeCodes;

/**
 * @author awilkinson
 */
public class BasicBenefitNullFixer extends BenefitNullFixer {
  

  /**
   * @param programYearScenario Scenario
   */
  @Override
  public void fixNulls(Scenario programYearScenario) {
    FarmingYear programYear = programYearScenario.getFarmingYear();
    Benefit benefit = programYear.getBenefit();
    String scenarioCategoryCode = programYearScenario.getScenarioCategoryCode();
    boolean isInterim = scenarioCategoryCode.equals(ScenarioCategoryCodes.INTERIM);
    
    if(benefit == null){
      benefit = new Benefit();
      
      // supposed to use RATIO SC method by default, except for Cash Margins
      String defaultCode;
      if(programYearScenario.getFarmingYear().getIsCashMargins()) {
        defaultCode = StructuralChangeCodes.ADDITIVE;
      } else {
        defaultCode = StructuralChangeCodes.RATIO;
      }
      benefit.setStructuralChangeMethodCode(defaultCode);
      benefit.setExpenseStructuralChangeMethodCode(defaultCode);
      
      programYear.setBenefit(benefit);
    }
    
    Double interimBenefitPercent = benefit.getInterimBenefitPercent();
    if(isInterim && interimBenefitPercent == null) {
      interimBenefitPercent = Double.valueOf(CalculatorConfig.INTERIM_BENEFIT_FACTOR);
    } else if(!isInterim) {
      interimBenefitPercent = null;
    }
    benefit.setInterimBenefitPercent(interimBenefitPercent);
    
    fixNulls(programYearScenario.getFarmingYear());
    
    for(ReferenceScenario rs : programYearScenario.getReferenceScenarios()) {
      
      fixNulls(rs.getFarmingYear());
    }
    
    calculateBpuLeadInd(programYearScenario);
  }

}
