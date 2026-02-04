/**
 * Copyright (c) 2021,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.enrolment;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;

import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.enrolment.Enrolment;
import ca.bc.gov.srm.farm.util.MathUtils;

/**
 * @author awilkinson
 */
public class LateParticipantEnrolmentCalculator {

  protected LateParticipantEnrolmentCalculator() {
  }
  
  /**
   * For Late Participants, a Verified scenario is used to calculate the enrolment
   * for the same enrolment year as the scenario's program year. Rather than calculating 
   * the Contribution Margin using enrolment year minus 2 to enrolment year minus 6 margins,
   * the Reference Margin of the scenario is used and the reference year margins "used"
   * (isMarginYearMinus2Used, etc.) are the same as the years used in the benefit calculation
   * (ReferenceScenario.usedInCalc).
   */
  public Enrolment calculateEnrolment(Scenario scenario) {
    int scenarioProgramYear = scenario.getYear();
    int enrolmentYear = scenarioProgramYear;
    
    Benefit benefit = scenario.getFarmingYear().getBenefit();
    double percentOfMargin;
    if(scenario.isInCombinedFarm()) {
      percentOfMargin = benefit.getCombinedFarmPercent();
    } else {
      percentOfMargin = 1.0; // if it's not a combined farm they get 100% of the benefit
    }
    
    double referenceMargin;
    if(CalculatorConfig.hasEnhancedBenefits(scenarioProgramYear)) {
      referenceMargin = benefit.getEnhancedReferenceMarginForBenefitCalculation();
    } else {
      referenceMargin = benefit.getAllocatedReferenceMargin();
    }
    referenceMargin *= percentOfMargin;  // apply combined farm percent if applicable.
    double enrolmentFee = referenceMargin * CalculatorConfig.getEnrolmentFeeFactor(enrolmentYear);
    enrolmentFee = MathUtils.round(enrolmentFee, 2);
    
    if (enrolmentFee < CalculatorConfig.MIN_ENROLMENT_FEE) {
      enrolmentFee = CalculatorConfig.MIN_ENROLMENT_FEE;
    }
    
    Double m2 = null;
    Double m3 = null;
    Double m4 = null;
    Double m5 = null;
    Double m6 = null;
    
    boolean m2Used = false;
    boolean m3Used = false;
    boolean m4Used = false;
    boolean m5Used = false;
    boolean m6Used = false;
    
    // Since the Late Participant uses the reference years of the enrolment year's scenario
    // we need to use year minus 1 to year minus 5 instead of minus 2 to minus 6.
    // The business area specifically requested this so that the enrolment data would be populated.
    // They have been informed that for Late Participants the field labels will not be accurate.
    int curYear = scenarioProgramYear - 1;
    final int yearMinus2 = curYear--;
    final int yearMinus3 = curYear--;
    final int yearMinus4 = curYear--;
    final int yearMinus5 = curYear--;
    final int yearMinus6 = curYear--;
    
    for(ReferenceScenario rs : scenario.getReferenceScenarios()) {
      if(rs != null && rs.getFarmingYear() != null) {
        int rsYear = rs.getYear();
        MarginTotal marginTotal = rs.getFarmingYear().getMarginTotal();
  
        Double curMargin = marginTotal.getProductionMargAftStrChangs();
        
        DecimalFormat df = new DecimalFormat("###.##");
        df.setRoundingMode(RoundingMode.UP);
        curMargin = Double.valueOf(df.format(curMargin));
  
        if(curMargin != null) {
          curMargin *= percentOfMargin;  // apply combined farm percent if applicable.
          
          if(rsYear == yearMinus2) {
            m2 = curMargin;
            m2Used = rs.getUsedInCalc();
          } else if(rsYear == yearMinus3) {
            m3 = curMargin;
            m3Used = rs.getUsedInCalc();
          } else if(rsYear == yearMinus4) {
            m4 = curMargin;
            m4Used = rs.getUsedInCalc();
          } else if(rsYear == yearMinus5) {
            m5 = curMargin;
            m5Used = rs.getUsedInCalc();
          } else if(rsYear == yearMinus6) {
            m6 = curMargin;
            m6Used = rs.getUsedInCalc();
          }
        }
      }
    }
    
    Enrolment enrolment = new Enrolment();
    enrolment.setClientId(scenario.getClient().getClientId());
    enrolment.setPin(scenario.getClient().getParticipantPin());
    enrolment.setEnrolmentYear(enrolmentYear);
    enrolment.setEnrolmentFee(enrolmentFee);
    enrolment.setFailedToGenerate(false);
    enrolment.setIsGeneratedFromCra(false);
    enrolment.setIsGeneratedFromEnw(scenario.isEnrolmentNoticeWorkflow());
    enrolment.setContributionMarginAverage(referenceMargin);
    enrolment.setMarginYearMinus2(m2);
    enrolment.setMarginYearMinus3(m3);
    enrolment.setMarginYearMinus4(m4);
    enrolment.setMarginYearMinus5(m5);
    enrolment.setMarginYearMinus6(m6);
    enrolment.setIsMarginYearMinus2Used(m2Used);
    enrolment.setIsMarginYearMinus3Used(m3Used);
    enrolment.setIsMarginYearMinus4Used(m4Used);
    enrolment.setIsMarginYearMinus5Used(m5Used);
    enrolment.setIsMarginYearMinus6Used(m6Used);
    enrolment.setIsCreateTaskInBarn(false);
    enrolment.setCombinedFarmPercent(benefit.getCombinedFarmPercent());
    enrolment.setMarginScenarioId(scenario.getScenarioId());
    enrolment.setGeneratedDate(new Date());
    return enrolment;
  }

}
