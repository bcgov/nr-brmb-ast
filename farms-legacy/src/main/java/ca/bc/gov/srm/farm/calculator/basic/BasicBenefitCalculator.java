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

import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.calculator.BenefitCalculator;
import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.calculator.CalculatorFactory;
import ca.bc.gov.srm.farm.calculator.StructuralChangeCalculator;
import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.util.ScenarioUtils;


/**
 * @author awilkinson
 */
public class BasicBenefitCalculator extends BenefitCalculator {

  public BasicBenefitCalculator(Scenario scenario) {
    super(scenario);
  }


  /**
   * Need to calculate the total separately for combined farms.
   */
  @Override
  protected void calculateTotalBenefit(double incomingBenefitValues[]) {
    
    Benefit benefit = scenario.getFarmingYear().getBenefit();
    int programYear = scenario.getYear().intValue();
    
    if(programYear >= CalculatorConfig.GROWING_FORWARD_2013) {
      benefit.setAppliedBenefitPercent(null); // in case this scenario was removed from a Combined Farm
    }

    double benefitValues[] = applyBenefitPercentage(benefit, programYear, incomingBenefitValues);
    
    benefitValues = applyMaxBenefit(benefitValues);
    
    benefitValues = calculateTotalBenefitValues(benefit, programYear, benefitValues);
  }
  
  
  /**
   * program year margin is just another name for "production margin with 
   * accrual adjustments"
   */
  @Override
  protected void calculateProgramYearMargin(Benefit benefit) {
    MarginTotal mt = scenario.getFarmingYear().getMarginTotal();
    Double margin = mt.getProductionMargAccrAdjs();
    benefit.setProgramYearMargin(margin);
  }


  @Override
  protected void resetUsedInCalc() {
    
    for(ReferenceScenario rs : scenario.getReferenceScenarios()) {
      
      rs.setUsedInCalc(Boolean.FALSE);
    }
  }


  @Override
  protected void updateScenarioUsedInCalc(Map<Integer, MarginTotal> refYearMargins) {
    for(Integer refYear : refYearMargins.keySet()) {
      ReferenceScenario rs = scenario.getReferenceScenarioByYear(refYear);
      rs.setUsedInCalc(Boolean.TRUE);
    }
  }


  @Override
  public boolean[] getUsedInCalc() {
    List<Integer> refYears = ScenarioUtils.getReferenceYears(scenario.getYear());
    boolean[] usedInCalc = new boolean[refYears.size()];
    int index = 0;
    for(Integer curYear : refYears) {
      ReferenceScenario rs = scenario.getReferenceScenarioByYear(curYear);
      
      if(rs != null) {
        usedInCalc[index] = rs.getUsedInCalc().booleanValue();
      } else {
        usedInCalc[index] = false;
      }
      index++;
    }
    return usedInCalc;
  }
  
  
  @Override
  protected void postPhase1Processing() {
    // no processing for basic calculation
  }
  
  
  @Override
  protected void postPhase2Processing() {
    // no processing for basic calculation
  }
  
  
  /**
   * Some fields can be overridden on the benefit screen.
   * This method is used by other screens to set those
   * fields to null. 
   */
  @Override
  public void resetOverridables() {
    StructuralChangeCalculator scCalc =
        CalculatorFactory.getStructuralChangeCalculator(scenario);
    scCalc.resetIsStructuralChangeNotable();
  }


  @Override
  public Benefit getBenefit() {
    return scenario.getFarmingYear().getBenefit();
  }

}
