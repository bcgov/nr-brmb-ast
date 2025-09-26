/**
 *
 * Copyright (c) 2009,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.calculator;

import static ca.bc.gov.srm.farm.util.MathUtils.*;

import java.util.Arrays;
import java.util.Map;

import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes;


/**
 * Used to calculate the benefit.
 */
public abstract class BenefitCalculator {
  
  protected Scenario scenario;

  protected BenefitCalculator(Scenario scenario) {
    this.scenario = scenario;
  }
  
  private static final int BENEFIT_INDEX_STANDARD = 0;
  private static final int BENEFIT_INDEX_ENHANCED = 1;
  private static final int BENEFIT_INDEX_LATE_ENROLMENT_PENALTY = 2;
  private static final int BENEFIT_INDEX_ENHANCED_LATE_ENROLMENT_PENALTY = 3;


  /**
   * Once the reference years are calculated, more validation
   * is supposed to happen. Rather than having the calculator
   * classes use struts error classes, I thought it was cleaner to
   * break the calculation into two parts, and have the service
   * do the validation.
   */
  public void calculateBenefitPhase1() {
    calculateReferenceData();
    postPhase1Processing();
  }
  
  
  /**
   * Calculate the benefit for the program year.
   */
  public void calculateBenefitPhase2() {
    ReferenceYearCalculator refYearCalc = CalculatorFactory.getReferenceYearCalculator(scenario);
    
    Map<Integer, MarginTotal> refYearMargins = refYearCalc.getReferenceYearMargins(scenario, true, false);
    resetUsedInCalc();
    updateScenarioUsedInCalc(refYearMargins);
      
    calculateBenefit(refYearMargins);
    postPhase2Processing();
  }
  
  
  private void calculateReferenceData() {
    IncomeExpenseCalculator ieCalc = CalculatorFactory.getIncomeExpenseCalculator(scenario);
    ProductionMarginCalculator pmCalc = CalculatorFactory.getProductionMarginCalculator(scenario);
    FarmSizeRatioCalculator fsrCalc = CalculatorFactory.getFarmSizeRatioCalculator(scenario);
    StructuralChangeCalculator scCalc = CalculatorFactory.getStructuralChangeCalculator(scenario);
    AccrualCalculator accCalc = CalculatorFactory.getAccrualCalculator(scenario);
    
    // order of calls is very important
    accCalc.calculateTotals();
    ieCalc.calculateIncomeExpense();
    pmCalc.calculateUnadjusted();
    pmCalc.calculateWithAccruals();
    fsrCalc.calculateRatio();
    calculateExpenseRatio(fsrCalc);
    scCalc.calculateMarginStructuralChange();
    calculateExpenseStructuralChange(scCalc);
    scCalc.calculateIsStructuralChangeNotable();
    pmCalc.calculateWithStructuralChange();
    calculateExpensesWithStructuralChange(ieCalc);
  }


  private void calculateExpenseRatio(FarmSizeRatioCalculator fsrCalc) {
    int programYear = scenario.getYear();
    if(programYear >= CalculatorConfig.GROWING_FORWARD_2013) {
      fsrCalc.calculateExpenseRatio();
    }
  }


  private void calculateExpenseStructuralChange(StructuralChangeCalculator scCalc) {
    int programYear = scenario.getYear();
    if(programYear >= CalculatorConfig.GROWING_FORWARD_2013) {
      scCalc.calculateExpenseStructuralChange();
    }
  }


  private void calculateExpensesWithStructuralChange(IncomeExpenseCalculator ieCalc) {
    int programYear = scenario.getYear();
    if(programYear >= CalculatorConfig.GROWING_FORWARD_2013) {
      ieCalc.calculateExpensesWithStructuralChange();
    }
  }


  private void calculateBenefit(Map<Integer, MarginTotal> refYearMargins) {
    Benefit benefit = getBenefit();
    int programYear = scenario.getYear();
    String scenarioCategoryCode = scenario.getScenarioCategoryCode();
    
    // order of calls is important
    calculateReferenceMargins(benefit, refYearMargins);
    calculateWholeFarmAllocation(benefit);
    calculateAllocatedReferenceMargin(benefit, refYearMargins, programYear);
    calculateProgramYearMargin(benefit);
    calculateMarginDecline(benefit, programYear);
    
    calculateTierValues(benefit, programYear);
    calculateSmcAdjustment();
    calculateNegativeMarginValues(benefit, refYearMargins, programYear);
    
    double benefitValues[] = calculateBenefitBeforeDeductions(benefit, programYear);
    benefitValues = deductProductionInsurance(benefit, benefitValues, programYear);
    benefitValues = applyInterimDeduction(benefit, benefitValues, scenarioCategoryCode, programYear);
    benefitValues = applyPaymentCap(benefit, benefitValues, programYear);
    benefitValues = applyLateEnrolmentPenalty(benefit, benefitValues, programYear);
    
    calculateTotalBenefit(benefitValues);
  }


  /**
   * Need to calculate the total separately for combined farms.
   * @param benefitValues benefitValues
   */
  protected abstract void calculateTotalBenefit(double benefitValues[]);
  
  
  /**
   * @param incomingBenefitValues incomingBenefitValues
   * @return benefitValues
   */
  protected double[] applyMaxBenefit(double[] incomingBenefitValues) {
    double[] benefitValues = Arrays.copyOf(incomingBenefitValues, incomingBenefitValues.length);
    int programYear = scenario.getYear();
    double agriStabilityMaxBenefit = CalculatorConfig.getAgriStabilityMaxBenefit(programYear);
    double bcEnhancedMaxBenefit = CalculatorConfig.getBCEnhancedMaxBenefit(programYear);
    
    for(int i = 0; i < benefitValues.length; i++) {
      double maxBenefit = agriStabilityMaxBenefit;
      
      if(i == BENEFIT_INDEX_ENHANCED || i == BENEFIT_INDEX_ENHANCED_LATE_ENROLMENT_PENALTY) {
        maxBenefit = bcEnhancedMaxBenefit;
      }
      
      if(benefitValues[i] > maxBenefit) {
        benefitValues[i] = maxBenefit;
      }
    }
    
    return benefitValues;
  }


  /**
   * @param incomingBenefitValues incomingBenefitValues
   * @return benefitValues
   */
  protected double[] roundBenefitValues(double[] incomingBenefitValues) {
    double[] benefitValues = Arrays.copyOf(incomingBenefitValues, incomingBenefitValues.length);
    
    for(int i = 0; i < benefitValues.length; i++) {
      benefitValues[i] = round(benefitValues[i], 0);
    }
    
    return benefitValues;
  }


  protected double[] calculateTotalBenefitValues(Benefit benefit, int programYear, double[] incomingBenefitValues) {
    double[] benefitValues = roundBenefitValues(incomingBenefitValues);
    
    if(CalculatorConfig.hasEnhancedBenefits(programYear)) {
      
      if(scenario.isLateParticipant() && programYear == CalculatorConfig.GROWING_FORWARD_2017) {
        benefitValues[BENEFIT_INDEX_STANDARD] = 0;
      }
      double enhancedAdditionalBenefit = benefitValues[BENEFIT_INDEX_ENHANCED] - benefitValues[BENEFIT_INDEX_STANDARD];
      
      benefit.setStandardBenefit(benefitValues[BENEFIT_INDEX_STANDARD]);
      benefit.setEnhancedTotalBenefit(benefitValues[BENEFIT_INDEX_ENHANCED]);
      benefit.setEnhancedAdditionalBenefit(enhancedAdditionalBenefit);
      benefit.setTotalBenefit(benefitValues[BENEFIT_INDEX_ENHANCED]);
      
    } else {
      benefit.setStandardBenefit(null);
      benefit.setEnhancedTotalBenefit(null);
      benefit.setEnhancedAdditionalBenefit(null);
      benefit.setTotalBenefit(benefitValues[BENEFIT_INDEX_STANDARD]);
    }
    
    return benefitValues;
  }


  /**
   * 
   * @param benefit benefit
   * @param incomingBenefitValues incomingBenefitValues
   * @param scenarioCategoryCode scenarioCategoryCode
   * @param programYear programYear
   * @return benefitValues
   */
  protected double[] applyInterimDeduction(Benefit benefit, double incomingBenefitValues[], String scenarioCategoryCode, int programYear) {
    double[] benefitValues = Arrays.copyOf(incomingBenefitValues, incomingBenefitValues.length);

    // wipe out any previous value
    benefit.setBenefitAfterInterimDeduction(null);
    benefit.setEnhancedBenefitAfterInterimDeduction(null);
    
    if (scenarioCategoryCode.equals(ScenarioCategoryCodes.INTERIM)) {
      double percent = benefit.getInterimBenefitPercent();
      
      benefitValues[BENEFIT_INDEX_STANDARD] *= percent;
      benefit.setBenefitAfterInterimDeduction(benefitValues[BENEFIT_INDEX_STANDARD]);
      
      if(CalculatorConfig.hasEnhancedBenefits(programYear)) {

        benefitValues[BENEFIT_INDEX_ENHANCED] *= percent;
        benefit.setEnhancedBenefitAfterInterimDeduction(benefitValues[BENEFIT_INDEX_ENHANCED]);
      }
    }
    
    return benefitValues;
  }
   
  
  private double[] applyPaymentCap(Benefit benefit, double incomingBenefitValues[], int programYear) {
    double[] benefitValues = Arrays.copyOf(incomingBenefitValues, incomingBenefitValues.length);
    
    // wipe out any previous value
    benefit.setPaymentCap(null);
    benefit.setBenefitAfterPaymentCap(null);
    benefit.setEnhancedBenefitAfterPaymentCap(null);
    
    if (CalculatorConfig.isPaymentCapEnabled(programYear)) {
      Double paymentCapPercentage = CalculatorConfig.getPaymentCapPercentageOfTotalMarginDecline(programYear);
      double paymentCap = round(benefit.getMarginDecline() * paymentCapPercentage, 0);
      
      if(benefitValues[BENEFIT_INDEX_STANDARD] > paymentCap) {
        benefitValues[BENEFIT_INDEX_STANDARD] = paymentCap;
      }
      if(benefitValues[BENEFIT_INDEX_ENHANCED] > paymentCap) {
        benefitValues[BENEFIT_INDEX_ENHANCED] = paymentCap;
      }
      
      benefit.setPaymentCap(paymentCap);
      benefit.setBenefitAfterPaymentCap(benefitValues[BENEFIT_INDEX_STANDARD]);
      benefit.setEnhancedBenefitAfterPaymentCap(benefitValues[BENEFIT_INDEX_ENHANCED]);
    }
    
    return benefitValues;
  }
  
  
  private double[] applyLateEnrolmentPenalty(Benefit benefit, double incomingBenefitValues[], int programYear) {
    double[] benefitValues = Arrays.copyOf(incomingBenefitValues, incomingBenefitValues.length);
    
    // wipe out any previous value
    benefit.setLateEnrolmentPenalty(null);
    benefit.setBenefitAfterLateEnrolmentPenalty(null);
    benefit.setEnhancedLateEnrolmentPenalty(null);
    benefit.setEnhancedBenefitAfterLateEnrolmentPenalty(null);
    
    if (scenario.isLateParticipant()) {
      double lateEnrolmentPenaltyPercent = CalculatorConfig.getLateParticipantPenaltyPercent(programYear);
      
      benefitValues[BENEFIT_INDEX_LATE_ENROLMENT_PENALTY] = round(benefitValues[BENEFIT_INDEX_STANDARD] * lateEnrolmentPenaltyPercent, 0);
      benefitValues[BENEFIT_INDEX_ENHANCED_LATE_ENROLMENT_PENALTY] = round(benefitValues[BENEFIT_INDEX_ENHANCED] * lateEnrolmentPenaltyPercent, 0);
      
      benefitValues[BENEFIT_INDEX_STANDARD] -= benefitValues[BENEFIT_INDEX_LATE_ENROLMENT_PENALTY];
      benefitValues[BENEFIT_INDEX_ENHANCED] -= benefitValues[BENEFIT_INDEX_ENHANCED_LATE_ENROLMENT_PENALTY];
      
      benefit.setLateEnrolmentPenalty(benefitValues[BENEFIT_INDEX_LATE_ENROLMENT_PENALTY]);
      benefit.setBenefitAfterLateEnrolmentPenalty(benefitValues[BENEFIT_INDEX_STANDARD]);
      benefit.setEnhancedLateEnrolmentPenalty(benefitValues[BENEFIT_INDEX_ENHANCED_LATE_ENROLMENT_PENALTY]);
      benefit.setEnhancedBenefitAfterLateEnrolmentPenalty(benefitValues[BENEFIT_INDEX_ENHANCED]);
    }
    
    return benefitValues;
  }


  /**
   * 
   * @param benefit benefit
   * @param programYear programYear
   * @param incomingBenefitValues incomingBenefitValues
   */
  protected double[] applyBenefitPercentage(Benefit benefit, int programYear, double incomingBenefitValues[]) {
    double benefitValues[] = Arrays.copyOf(incomingBenefitValues, incomingBenefitValues.length);
    
    // wipe out any previous value
    benefit.setBenefitAfterAppliedBenefitPercent(null);
    benefit.setLateEnrolmentPenaltyAfterAppliedBenefitPercent(null);
    benefit.setEnhancedBenefitAfterAppliedBenefitPercent(null);
    benefit.setEnhancedLateEnrolmentPenaltyAfterAppliedBenefitPercent(null);
    
    if (benefit.getAppliedBenefitPercent() != null) {
      double percent = benefit.getAppliedBenefitPercent();
      
      for(int i = 0; i < benefitValues.length; i++) {
        benefitValues[i] = round(benefitValues[i] * percent, 0);
      }
      
      benefit.setBenefitAfterAppliedBenefitPercent(benefitValues[BENEFIT_INDEX_STANDARD]);
      
      boolean hasEnhancedBenefits = CalculatorConfig.hasEnhancedBenefits(programYear);
      
      if(hasEnhancedBenefits) {
        benefit.setEnhancedBenefitAfterAppliedBenefitPercent(benefitValues[BENEFIT_INDEX_ENHANCED]);
      }
      
      if(scenario.isLateParticipant()) {
        benefit.setLateEnrolmentPenaltyAfterAppliedBenefitPercent(benefitValues[BENEFIT_INDEX_LATE_ENROLMENT_PENALTY]);
      }
      
      if(hasEnhancedBenefits && scenario.isLateParticipant()) {
        benefit.setEnhancedLateEnrolmentPenaltyAfterAppliedBenefitPercent(benefitValues[BENEFIT_INDEX_ENHANCED_LATE_ENROLMENT_PENALTY]);
      }
    }
    
    return benefitValues;
  }


  /**
   * 
   * @param benefit benefit
   * @param incomingBenefitValues incomingBenefitValues
   * @param programYear programYear
   * @return benefitValues
   */
  private double[] deductProductionInsurance(Benefit benefit, double incomingBenefitValues[], int programYear) {
    double[] benefitValues = Arrays.copyOf(incomingBenefitValues, incomingBenefitValues.length);
    
    // wipe out any previous value
    benefit.setBenefitAfterProdInsDeduction(null);
    benefit.setEnhancedBenefitAfterProdInsDeduction(null);
    
    if (benefit.getProdInsurDeemedBenefit() != null) {
      double deduction = calculateProductionInsuranceDeduction(benefit, programYear, false);
      
      benefitValues[BENEFIT_INDEX_STANDARD] -= deduction;
      benefit.setBenefitAfterProdInsDeduction(benefitValues[BENEFIT_INDEX_STANDARD]);
      
      if(CalculatorConfig.hasEnhancedBenefits(programYear)) {
        deduction = calculateProductionInsuranceDeduction(benefit, programYear, true);
        benefitValues[BENEFIT_INDEX_ENHANCED] -= deduction;
        benefit.setEnhancedBenefitAfterProdInsDeduction(benefitValues[BENEFIT_INDEX_ENHANCED]);
      }
    }
    
    return benefitValues;
  }


  public double calculateProductionInsuranceDeduction(Benefit benefit, int programYear, boolean forEnhancedBenefit) {
    double prodInsDeemedBenefit = benefit.getProdInsurDeemedBenefit();
    double deduction = applyProductionInsurancePaymentPercentage(prodInsDeemedBenefit, programYear, forEnhancedBenefit);
    
    Double nmb = benefit.getNegativeMarginBenefit();
    
    if(forEnhancedBenefit) {
      // for 2019 and beyond the EnhancedNegativeMarginBenefit is a bit bigger than the NegativeMarginBenefit
      nmb = benefit.getEnhancedNegativeMarginBenefit();
    }
    
    if(nmb != null) {
      // the production insurance deduction is supposed to max out at the negative margin benefit.
      if(deduction > nmb) {
        deduction = nmb;
      }
    }
    
    return deduction;
  }


  private double applyProductionInsurancePaymentPercentage(double prodInsDeemedBenefit, int programYear, boolean forEnhancedBenefit) {
    double deduction;
    if(programYear >= CalculatorConfig.GROWING_FORWARD_2024) {
      deduction = prodInsDeemedBenefit; // the prodInsuranceFactor is now applied in an earlier step
    } else {
      double prodInsuranceFactor;
      if(forEnhancedBenefit) {
        prodInsuranceFactor = CalculatorConfig.getEnhancedProductionInsuranceFactor(programYear);
      } else {
        prodInsuranceFactor = CalculatorConfig.getProductionInsuranceFactor(programYear);
      }
      deduction = prodInsDeemedBenefit * prodInsuranceFactor;
    }
    return deduction;
  }
  
  
  private double[] calculateBenefitBeforeDeductions(Benefit benefit, int programYear) {
    
    // Standard, BC Enhanced, Standard Late Enrolment Penalty, BC Enhanced Late Enrolment Penalty
    double totals[] = {0,0,0,0};
    
    if(benefit.getTier2Benefit() != null) {
      totals[BENEFIT_INDEX_STANDARD] += benefit.getTier2Benefit();
    }
    totals[BENEFIT_INDEX_STANDARD] -= benefit.getSupplyManagedCommoditiesAdj();
    totals[BENEFIT_INDEX_STANDARD] += benefit.getTier3Benefit();
    totals[BENEFIT_INDEX_STANDARD] += benefit.getNegativeMarginBenefit();
    
    benefit.setBenefitBeforeDeductions(totals[BENEFIT_INDEX_STANDARD]);
    
    if(CalculatorConfig.hasEnhancedBenefits(programYear)) {
        totals[BENEFIT_INDEX_ENHANCED] += benefit.getEnhancedPositiveMarginBenefit();
        totals[BENEFIT_INDEX_ENHANCED] += benefit.getEnhancedNegativeMarginBenefit();
        benefit.setEnhancedBenefitBeforeDeductions(totals[BENEFIT_INDEX_ENHANCED]);
    } else {
      benefit.setEnhancedBenefitBeforeDeductions(null);
    }
    
    return totals;
  }
  
  
  /**
   * 
   * @param benefit benefit
   * @param refYearMargins refYearMargins
   * @param programYear programYear
   */
  private void calculateNegativeMarginValues(Benefit benefit, Map<Integer, MarginTotal> refYearMargins, int programYear) {
    double arm = benefit.getAllocatedReferenceMargin();
    double pym = benefit.getProgramYearMargin();
    
    //
    // negative margin decline
    //
    double negMarginDecline = 0;
    
    if(pym > 0) {
      negMarginDecline = 0;
    } else if((pym < 0) && (arm > 0)) {
      negMarginDecline = Math.abs(pym);
    } else if ((pym < 0) && (arm < 0)) {
      if(Math.abs(pym) <= Math.abs(arm)) {
        negMarginDecline = 0;
      } else {
        negMarginDecline = Math.abs(pym) - Math.abs(arm);
      }
    }
    
    benefit.setNegativeMarginDecline(negMarginDecline);
    
    if(CalculatorConfig.hasEnhancedBenefits(programYear)) {
      benefit.setEnhancedNegativeMarginDecline(negMarginDecline);
    }
    
    //
    // negative margin benefit - if the ARM is greater than zero then
    // calculate the NMB, otherwise, if at least two ref years have a 
    // PMASC greater than zero then calculate the NMB.
    //
    boolean calculateBenefit = false;
    double negMarginBenefit = 0;
    
    if(arm > 0) {
      calculateBenefit = true;
    } else {
      final int minNumPosYears = 2;
      int numPosYears = 0;
      
      for(Integer refYear : refYearMargins.keySet()) {
        MarginTotal refYearMargin = refYearMargins.get(refYear);
        
        // important to use this margin
        double margin = refYearMargin.getProductionMargAftStrChangs();
        
        if(margin > 0) {
          numPosYears++;
        }
      }

      if(numPosYears >= minNumPosYears) {
        calculateBenefit = true;
      }
    }
    
    if(calculateBenefit) {
      double negativeMarginCompensationRate;
      
      negativeMarginCompensationRate = CalculatorConfig.getStandardNegativeMarginCompensationRate(programYear);
      negMarginBenefit = negMarginDecline * negativeMarginCompensationRate;
    }
    
    benefit.setNegativeMarginBenefit(negMarginBenefit);
    
    if(CalculatorConfig.hasEnhancedBenefits(programYear)) {
      double enhancedNegMarginBenefit = 0; 
      
      if(calculateBenefit) {
        double compensationRate = CalculatorConfig.getEnhancedNegativeMarginCompensationRate(programYear);
        enhancedNegMarginBenefit = negMarginDecline * compensationRate;
      
      }
      benefit.setEnhancedNegativeMarginBenefit(enhancedNegMarginBenefit);
    }
  }
  
  
  /**
   * Tier 2 stuff depends on tier 3 stuff so do them all at once.
   * 
   * @param benefit benefit
   */
  private void calculateTierValues(Benefit benefit, int programYear) {
    double arm = benefit.getAllocatedReferenceMargin();
    
    if(arm > 0) {
      //
      // Calculate the tier 2 & 3 stuff
      //
      double pym = benefit.getProgramYearMargin();
      
      double tier3Trigger = arm * CalculatorConfig.getPaymentTriggerFactor();
      benefit.setTier3Trigger(tier3Trigger);
      
      // tier 3 margin decline
      double tier3md = 0;
      
      if( (pym < tier3Trigger) && (pym > 0) ) {
        tier3md = tier3Trigger - pym;
      } else if ((pym < tier3Trigger) && (pym <= 0)) {
        tier3md = arm * CalculatorConfig.TIER_3_MARGIN_DECLINE_FACTOR;
      }
      benefit.setTier3MarginDecline(tier3md);
      
      // tier 3 benefit
      double tier3BenefitFactor = CalculatorConfig.getStandardPositiveMarginCompensationRate(programYear);
      double tier3Benefit = tier3md * tier3BenefitFactor;
      tier3Benefit = enforceMinumum(tier3Benefit, CalculatorConfig.MIN_TIER_3_BENEFIT);
      benefit.setTier3Benefit(tier3Benefit);
      

      // tier triggers
      if(programYear >= CalculatorConfig.GROWING_FORWARD_2013) {
        benefit.setTier2Trigger(null);
        benefit.setTier2MarginDecline(null);
        benefit.setTier2Benefit(null);
      } else {
        double tier2Trigger = arm * CalculatorConfig.TIER_2_TRIGGER_FACTOR;
        benefit.setTier2Trigger(tier2Trigger);
        
        // tier 2 margin decline
        double tier2md = 0;
        
        if( (pym < tier2Trigger) && (pym > tier3Trigger) ) {
          tier2md = tier2Trigger - pym;
        } else if (pym <= tier3Trigger) {
          tier2md = arm * CalculatorConfig.TIER_2_MARGIN_DECLINE_FACTOR;
        }
        benefit.setTier2MarginDecline(tier2md);
        
        // tier 2 benefit
        double tier2Benefit = tier2md * CalculatorConfig.TIER_2_BENEFIT_FACTOR;
        tier2Benefit = enforceMinumum(tier2Benefit, CalculatorConfig.MIN_TIER_2_BENEFIT);
        benefit.setTier2Benefit(tier2Benefit);
      }

    } else {
      //
      // Tier 2 & 3 stuff doesn't apply. Set their benefits
      // to zero because they are used elsewhere. The
      // Tier3MarginDecline is used in the calculateSmcAdjustment
      // method but there is a null check.
      //
      benefit.setTier2Benefit(0.0);
      benefit.setTier3Benefit(0.0);
      
      //
      // Previous versions of the code used to calculate negative values
      // for this stuff so null them out for now. These lines could be removed
      // in future releases.
      //
      benefit.setTier2Trigger(null);
      benefit.setTier3Trigger(null);
      benefit.setTier2MarginDecline(null);
      benefit.setTier3MarginDecline(null);
    }
    
    
    if(CalculatorConfig.hasEnhancedBenefits(programYear)) {
      
      double enhancedReferenceMarginForBenefitCalculation = benefit.getEnhancedReferenceMarginForBenefitCalculation();
      if(enhancedReferenceMarginForBenefitCalculation > 0) {
        double pym = benefit.getProgramYearMargin();
        
        double enhancedPositiveMarginTrigger = enhancedReferenceMarginForBenefitCalculation * CalculatorConfig.getPaymentTriggerFactor();
        
        // tier 3 margin decline (AKA Positive Margin Decline)
        double enhancedPositiveMarginDecline = 0;
        
        if( (pym < enhancedPositiveMarginTrigger) && (pym > 0) ) {
          enhancedPositiveMarginDecline = enhancedPositiveMarginTrigger - pym;
        } else if ((pym < enhancedPositiveMarginTrigger) && (pym <= 0)) {
          enhancedPositiveMarginDecline = enhancedReferenceMarginForBenefitCalculation * CalculatorConfig.TIER_3_MARGIN_DECLINE_FACTOR;
        }
        benefit.setEnhancedPositiveMarginDecline(enhancedPositiveMarginDecline);
        
        // tier 3 benefit
        double enhancedPositiveMarginCompensationRate = CalculatorConfig.getEnhancedPositiveMarginCompensationRate(programYear);
        double enhancedPositiveMarginBenefit = enhancedPositiveMarginDecline * enhancedPositiveMarginCompensationRate;
        enhancedPositiveMarginBenefit = enforceMinumum(enhancedPositiveMarginBenefit, CalculatorConfig.MIN_TIER_3_BENEFIT);
        benefit.setEnhancedPositiveMarginBenefit(enhancedPositiveMarginBenefit);
        
      } else {
        benefit.setEnhancedPositiveMarginDecline(0.0);
        benefit.setEnhancedPositiveMarginBenefit(0.0);
      }
      
    } else {
      benefit.setEnhancedPositiveMarginDecline(null);
      benefit.setEnhancedPositiveMarginBenefit(null);
    }
    
  }


  /**
   * Supply Managed Commodities Adjustment
   */
  private void calculateSmcAdjustment() {
    Benefit benefit = getBenefit();
    int programYear = scenario.getYear();
  
    double smcAdj = 0;
    
    // if tier 2 is removed then Supply Managed Commodities adjustment does not apply
    if(programYear < CalculatorConfig.GROWING_FORWARD_2013) {
      double arm = benefit.getAllocatedReferenceMargin();
      
      if(arm > 0) {
        //
        // Tier 2 & 3 stuff only calculated if arm > 0
        //
        double tier3MarginDecline = benefit.getTier3MarginDecline();
        
        if(tier3MarginDecline == 0) {
          SupplyManagedCommoditiesRatioCalculator smcrCalc = 
            CalculatorFactory.getSupplyManagedCommoditiesRatioCalculator(scenario);
          
          if(benefit.getTier2Benefit() == null) {
            smcAdj = 0;
          } else {
            double smcRatio = smcrCalc.calculateRatio();
            double tier2Benefit = benefit.getTier2Benefit();
            
            smcAdj = tier2Benefit * smcRatio;
          }
        }
      }
    }
    
    benefit.setSupplyManagedCommoditiesAdj(smcAdj);
  }
  
  
  /**
   * margin decline must be greater than or equal to zero.
   * 
   * @param benefit benefit
   * @param programYear programYear
   */
  private void calculateMarginDecline(Benefit benefit, int programYear) {
    double arm = benefit.getAllocatedReferenceMargin();
    double pym = benefit.getProgramYearMargin();
    double decline = 0;
    
    if(arm - pym > 0) {
      decline = arm - pym;
    }
    
    decline = enforceMinumum(decline, CalculatorConfig.MIN_MARGIN_DECLINE);
    benefit.setMarginDecline(decline);
    
    if(CalculatorConfig.hasEnhancedBenefits(programYear)) {
      double enhancedAllocatedReferenceMargin = benefit.getEnhancedReferenceMarginForBenefitCalculation();
      double enhancedMarginDecline = enhancedAllocatedReferenceMargin - pym;
        enhancedMarginDecline = enforceMinumum(enhancedMarginDecline, CalculatorConfig.MIN_MARGIN_DECLINE);
          benefit.setEnhancedMarginDecline(enhancedMarginDecline);
    }
  }
  
  
  /**
   * 
   * @param value value
   * @param min min
   * @return value or min
   */
  private double enforceMinumum(double value, double min) {
    double returnedValue = value;
    
    if(value < min) {
      returnedValue = min;
    }
    
    return returnedValue;
  }

  /**
   * program year margin is just another name for "production margin with 
   * accrual adjustments"
   */
  protected abstract void calculateProgramYearMargin(Benefit benefit);
  
  
  /**
   * For some reason this is always hard coded to 100% (i.e 1.000)
   * 
   * @param benefit benefit
   */
  private void calculateWholeFarmAllocation(Benefit benefit) {
    double wfa = CalculatorConfig.WHOLE_FARM_ALLOCATION;
    benefit.setWholeFarmAllocation(wfa);
  }
  
  
  private void calculateAllocatedReferenceMargin(Benefit benefit, Map<Integer, MarginTotal> refYearMargins, int programYear) {
    double wfa = benefit.getWholeFarmAllocation();
    double adjRefMargin = benefit.getAdjustedReferenceMargin();
    double arm = wfa * adjRefMargin;
    
    if(CalculatorConfig.hasEnhancedBenefits(programYear)) {
      double enhancedAllocatedReferenceMargin = arm;
      benefit.setEnhancedReferenceMarginForBenefitCalculation(enhancedAllocatedReferenceMargin);
    } else {
      benefit.setEnhancedReferenceMarginForBenefitCalculation(null);
    }
    
    // Reference Margin Limit was introduced in 2013
    if(programYear >= CalculatorConfig.GROWING_FORWARD_2013) {
      // As of 2020, reference margin limit is no longer used by the benefit calculation,
      // but we still need to calculate it because it is included in the Analytical Surveillance Strategy report.
      boolean useReferenceMarginLimit = CalculatorConfig.useReferenceMarginLimit(programYear); // 2013 to 2019
      double referenceMarginLimit = calculateReferenceMarginLimit(benefit, refYearMargins);
      double referenceMarginLimitForBenefitCalc = referenceMarginLimit;
      
      // For 2018 apply the cap (minimum 70% of reference margin). 
      if(programYear >= CalculatorConfig.GROWING_FORWARD_2018) {
        double referenceMarginLimitCap = arm * CalculatorConfig.REFERENCE_MARGIN_LIMIT_CAP_FACTOR;
        referenceMarginLimitCap = round(referenceMarginLimitCap, CalculatorConfig.DOLLAR_AMOUNT_DECIMAL_PLACES);
        if(referenceMarginLimitCap > referenceMarginLimitForBenefitCalc) {
          referenceMarginLimitForBenefitCalc = referenceMarginLimitCap;
        }
        benefit.setReferenceMarginLimitCap(referenceMarginLimitCap);
        benefit.setReferenceMarginLimitForBenefitCalc(referenceMarginLimitForBenefitCalc);
      }
      
      if(useReferenceMarginLimit) {
        if(arm > referenceMarginLimitForBenefitCalc) {
          arm = referenceMarginLimitForBenefitCalc ;
        }
      }
    }
    
    benefit.setAllocatedReferenceMargin(arm);
  }


  /**
   * Average of the total eligible expenses for the years used in the calculation.
   * @param benefit benefit
   * @param refYearMargins refYearMargins
   */
  private double calculateReferenceMarginLimit(Benefit benefit, Map<Integer, MarginTotal> refYearMargins) {
    double expenseTotalForAverage = 0;
    for(Integer refYear : refYearMargins.keySet()) {
      MarginTotal refYearMargin = refYearMargins.get(refYear);

      double refYearExpenses = refYearMargin.getExpensesAfterStructuralChange();

      expenseTotalForAverage += refYearExpenses;
    }
    
    int numRefYears = refYearMargins.size();
    double referenceMarginLimit = expenseTotalForAverage / numRefYears;
    referenceMarginLimit = round(referenceMarginLimit, CalculatorConfig.DOLLAR_AMOUNT_DECIMAL_PLACES);
    benefit.setReferenceMarginLimit(referenceMarginLimit);
    
    return referenceMarginLimit;
  }
  
  
  /**
   * @param benefit benefit
   * @param refYearMargins refYearMargins
   */
  private void calculateReferenceMargins(Benefit benefit, Map<Integer, MarginTotal> refYearMargins) {
    double unadjustedReferenceMarginTotal = 0;
    double adjustedReferenceMarginTotal = 0;
    int numRefYears = refYearMargins.size();
    
    for(Integer refYear : refYearMargins.keySet()) {
      MarginTotal refYearMargin = refYearMargins.get(refYear);
      
      unadjustedReferenceMarginTotal += refYearMargin.getUnadjustedProductionMargin();
      adjustedReferenceMarginTotal += refYearMargin.getProductionMargAftStrChangs();
    }
    
    double unadjustedReferenceMargin = unadjustedReferenceMarginTotal / numRefYears;
    benefit.setUnadjustedReferenceMargin(unadjustedReferenceMargin);
    
    double adjustedReferenceMargin = adjustedReferenceMarginTotal / numRefYears;
    benefit.setAdjustedReferenceMargin(adjustedReferenceMargin);
    
    
    ReferenceYearCalculator refYearCalc = CalculatorFactory.getReferenceYearCalculator(scenario);
    
    // Ratio Reference Margin
    {
      double ratioAdjustedReferenceMarginTotal = 0;
      Map<Integer, MarginTotal> ratioRefYearMargins = refYearCalc.getReferenceYearMargins(scenario, true, false,
          CalculatorConfig.STRUCTURAL_CHANGE_METHOD_RATIO);
      
      for(Integer refYear : ratioRefYearMargins.keySet()) {
        MarginTotal refYearMargin = ratioRefYearMargins.get(refYear);
        
        ratioAdjustedReferenceMarginTotal += refYearMargin.getRatioProductionMargAftStrChangs();
      }
      
      double ratioAdjustedReferenceMargin = ratioAdjustedReferenceMarginTotal / numRefYears;
      benefit.setRatioAdjustedReferenceMargin(ratioAdjustedReferenceMargin);
    }
    
    // Additive Reference Margin
    {
      double additiveAdjustedReferenceMarginTotal = 0;
      Map<Integer, MarginTotal> additiveRefYearMargins = refYearCalc.getReferenceYearMargins(scenario, true, false,
          CalculatorConfig.STRUCTURAL_CHANGE_METHOD_ADDITIVE);
      
      for(Integer refYear : additiveRefYearMargins.keySet()) {
        MarginTotal refYearMargin = additiveRefYearMargins.get(refYear);
        
        additiveAdjustedReferenceMarginTotal += refYearMargin.getAdditiveProductionMargAftStrChangs();
      }
      
      double additiveAdjustedReferenceMargin = additiveAdjustedReferenceMarginTotal / numRefYears;
      benefit.setAdditiveAdjustedReferenceMargin(additiveAdjustedReferenceMargin);
    }
  }


  protected abstract void resetUsedInCalc();


  protected abstract void updateScenarioUsedInCalc(Map<Integer, MarginTotal> refYearMargins);


  public abstract boolean[] getUsedInCalc();
  
  
  protected abstract void postPhase1Processing();
  
  
  protected abstract void postPhase2Processing();
  
  
  /**
   * Some fields can be overridden on the benefit screen.
   * This method is used by other screens to set those
   * fields to null. 
   */
  public abstract void resetOverridables();


  public abstract Benefit getBenefit();
  

}
