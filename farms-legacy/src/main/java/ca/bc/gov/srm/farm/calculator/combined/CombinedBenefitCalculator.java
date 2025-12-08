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
import java.util.Map;

import ca.bc.gov.srm.farm.calculator.BenefitCalculator;
import ca.bc.gov.srm.farm.calculator.CalculatorFactory;
import ca.bc.gov.srm.farm.calculator.StructuralChangeCalculator;
import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.domain.CombinedFarm;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;


/**
 * @author awilkinson
 */
public class CombinedBenefitCalculator extends BenefitCalculator {

  public CombinedBenefitCalculator(Scenario scenario) {
    super(scenario);
  }

  /**
   * Need to calculate the total separately for combined farms.
   */
  @Override
  protected void calculateTotalBenefit(double incomingBenefitValues[]) {
    CombinedFarm combinedFarm = scenario.getCombinedFarm();
    List<Scenario> scenarios = combinedFarm.getScenarios();
    int programYear = scenario.getYear();

    Benefit combinedBenefit = combinedFarm.getBenefit();
    
    double combinedBenefitValues[] = applyMaxBenefit(incomingBenefitValues);
    
    combinedBenefitValues = calculateTotalBenefitValues(combinedBenefit, programYear, combinedBenefitValues);
    
    CombinedAppliedBenefitPercentCalculator pctCalc = CalculatorFactory.getCombinedAppliedBenefitPercentCalculator(scenario);
    pctCalc.calculate();
    
    for(Scenario curScenario : scenarios) {
      Benefit curBenefit = curScenario.getFarmingYear().getBenefit();
      
      double[] individualBenefits = applyBenefitPercentage(curBenefit, programYear, combinedBenefitValues);
      calculateTotalBenefitValues(curBenefit, programYear, individualBenefits);
    }
  }
  
  
  /**
   * program year margin is just another name for "production margin with 
   * accrual adjustments"
   */
  @Override
  protected void calculateProgramYearMargin(Benefit benefit) {
    CombinedFarm combinedFarm = scenario.getCombinedFarm();
    Integer programYear = scenario.getYear();
    MarginTotal mt = combinedFarm.getYearMargins().get(programYear);
    
    Double margin = mt.getProductionMargAccrAdjs();
    benefit.setProgramYearMargin(margin);
  }


  @Override
  protected void resetUsedInCalc() {
    CombinedFarm combinedFarm = scenario.getCombinedFarm();
    
    for(Scenario curScenario : combinedFarm.getScenarios()) {
      
      for(ReferenceScenario rs : curScenario.getReferenceScenarios()) {
        
        rs.setUsedInCalc(Boolean.FALSE);
      }
    }
  }


  @Override
  protected void updateScenarioUsedInCalc(Map<Integer, MarginTotal> refYearMargins) {
    CombinedFarm combinedFarm = scenario.getCombinedFarm();
    
    for(Scenario curScenario : combinedFarm.getScenarios()) {
      
      for(Integer refYear : refYearMargins.keySet()) {
        ReferenceScenario rs = curScenario.getReferenceScenarioByYear(refYear);
        if (rs != null) {
          rs.setUsedInCalc(Boolean.TRUE);
        }
      }
    }
  }


  @Override
  public boolean[] getUsedInCalc() {
    CombinedFarm combinedFarm = scenario.getCombinedFarm();
    List<Integer> refYears = combinedFarm.getReferenceYearsIncludingMissing();
    boolean[] usedInCalc = new boolean[refYears.size()];
    int index = 0;
    
    for(Integer refYear : refYears) {
      
      List<ReferenceScenario> refScenarios = combinedFarm.getReferenceScenariosByYear(refYear);
      for(ReferenceScenario rs : refScenarios) {
        
        if(rs.getUsedInCalc()) {
          usedInCalc[index] = true;
          break;
        }
      }
      index++;
    }

    return usedInCalc;
  }
  
  
  /**
   * Processing specific to Combined Farms performed at the end of phase 1 of the calculation.
   */
  @Override
  protected void postPhase1Processing() {
    copyBenefitToScenarios();
  }
  
  
  /**
   * Processing specific to Combined Farms performed at the end of phase 2 of the calculation.
   */
  @Override
  protected void postPhase2Processing() {
    copyBenefitToScenarios();
  }


  /**
   * Copy combined farm data into the scenarios.
   */
  private void copyBenefitToScenarios() {
    CombinedFarm combinedFarm = scenario.getCombinedFarm();
    Benefit combinedBenefit = combinedFarm.getBenefit();
    Map<Integer, MarginTotal> yearMargins = combinedFarm.getYearMargins();
    Map<Integer, Boolean> deemedFarmingYearMap = combinedFarm.getDeemedFarmingYearMap();
    
    for(Scenario curScenario : combinedFarm.getScenarios()) {
      
      // copy benefit
      Benefit curBenefit = curScenario.getFarmingYear().getBenefit();
      copyBenefit(curBenefit, combinedBenefit);
      
      for(ReferenceScenario rs : curScenario.getAllScenarios()) {
        Integer curYear = rs.getYear();
        
        // copy margin totals
        MarginTotal rsMargin = rs.getFarmingYear().getMarginTotal();
        MarginTotal combinedMargin = yearMargins.get(curYear);
        copyMarginTotal(rsMargin, combinedMargin);
        
        // copy deemed farming year
        Boolean isDeemedFarmingYear = deemedFarmingYearMap.get(curYear);
        rs.setIsDeemedFarmingYear(isDeemedFarmingYear);
      }
    }
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
    
    CombinedAppliedBenefitPercentCalculator pctCalc =
        CalculatorFactory.getCombinedAppliedBenefitPercentCalculator(scenario);
    pctCalc.reset();
  }


  @Override
  public Benefit getBenefit() {
    return scenario.getCombinedFarm().getBenefit();
  }


  /**
   * Copies all fields except for:
   *     farmingYear
   *     claimId
   *     revisionCount
   *     appliedBenefitPercent
   *     benefitAfterAppliedBenefitPercent
   *     enhancedBenefitAfterAppliedBenefitPercent
   *     enhancedLateEnrolmentPenaltyAfterAppliedBenefitPercent
   *     enhancedAdditionalBenefit
   *     enhancedTotalBenefit
   *     lateEnrolmentPenaltyAfterAppliedBenefitPercent
   *     totalBenefit
   * 
   * These fields are not copied because they may be different for each Scenario/Benefit.
   * 
   * @param to Benefit
   * @param from Benefit
   */
  public void copyBenefit(Benefit to, Benefit from) {
    to.setStructuralChangeMethodCode(from.getStructuralChangeMethodCode());
    to.setStructuralChangeMethodCodeDescription(from.getStructuralChangeMethodCodeDescription());
    to.setAdministrativeCostShare(from.getAdministrativeCostShare());
    to.setLateApplicationPenalty(from.getLateApplicationPenalty());
    to.setMaximumContribution(from.getMaximumContribution());
    to.setOutstandingFees(from.getOutstandingFees());
    to.setProdInsurDeemedBenefit(from.getProdInsurDeemedBenefit());
    to.setProdInsurDeemedBenefitManuallyCalculated(from.getProdInsurDeemedBenefitManuallyCalculated());
    to.setProgramYearMargin(from.getProgramYearMargin());
    to.setProgramYearPaymentsReceived(from.getProgramYearPaymentsReceived());
    to.setAllocatedReferenceMargin(from.getAllocatedReferenceMargin());
    to.setRepaymentOfCashAdvances(from.getRepaymentOfCashAdvances());
    to.setTotalPayment(from.getTotalPayment());
    to.setSupplyManagedCommoditiesAdj(from.getSupplyManagedCommoditiesAdj());
    to.setProducerShare(from.getProducerShare());
    to.setFederalContributions(from.getFederalContributions());
    to.setProvincialContributions(from.getProvincialContributions());
    to.setInterimContributions(from.getInterimContributions());
    to.setWholeFarmAllocation(from.getWholeFarmAllocation());
    to.setTier2MarginDecline(from.getTier2MarginDecline());
    to.setTier3MarginDecline(from.getTier3MarginDecline());
    to.setTier2Benefit(from.getTier2Benefit());
    to.setTier3Benefit(from.getTier3Benefit());
    to.setNegativeMarginBenefit(from.getNegativeMarginBenefit());
    to.setNegativeMarginDecline(from.getNegativeMarginDecline());
    to.setRatioAdjustedReferenceMargin(from.getRatioAdjustedReferenceMargin());
    to.setAdditiveAdjustedReferenceMargin(from.getAdditiveAdjustedReferenceMargin());
    to.setAdjustedReferenceMargin(from.getAdjustedReferenceMargin());
    to.setUnadjustedReferenceMargin(from.getUnadjustedReferenceMargin());
    to.setReferenceMarginLimit(from.getReferenceMarginLimit());
    to.setReferenceMarginLimitCap(from.getReferenceMarginLimitCap());
    to.setReferenceMarginLimitForBenefitCalc(from.getReferenceMarginLimitForBenefitCalc());
    to.setExpenseStructuralChangeMethodCode(from.getExpenseStructuralChangeMethodCode());
    to.setExpenseStructuralChangeMethodCodeDescription(from.getExpenseStructuralChangeMethodCodeDescription());
    to.setMarginDecline(from.getMarginDecline());
    to.setTier2Trigger(from.getTier2Trigger());
    to.setTier3Trigger(from.getTier3Trigger());
    to.setBenefitBeforeDeductions(from.getBenefitBeforeDeductions());
    to.setBenefitAfterProdInsDeduction(from.getBenefitAfterProdInsDeduction());
    to.setInterimBenefitPercent(from.getInterimBenefitPercent());
    to.setBenefitAfterInterimDeduction(from.getBenefitAfterInterimDeduction());
    to.setLateEnrolmentPenalty(from.getLateEnrolmentPenalty());
    to.setBenefitAfterLateEnrolmentPenalty(from.getBenefitAfterLateEnrolmentPenalty());
    to.setEnhancedMarginDecline(from.getEnhancedMarginDecline());
    to.setEnhancedReferenceMarginForBenefitCalculation(from.getEnhancedReferenceMarginForBenefitCalculation());
    to.setEnhancedPositiveMarginDecline(from.getEnhancedPositiveMarginDecline());
    to.setEnhancedPositiveMarginBenefit(from.getEnhancedPositiveMarginBenefit());
    to.setEnhancedNegativeMarginDecline(from.getEnhancedNegativeMarginDecline());
    to.setEnhancedNegativeMarginBenefit(from.getEnhancedNegativeMarginBenefit());
    to.setEnhancedBenefitBeforeDeductions(from.getEnhancedBenefitBeforeDeductions());
    to.setEnhancedBenefitAfterProdInsDeduction(from.getEnhancedBenefitAfterProdInsDeduction());
    to.setEnhancedBenefitAfterInterimDeduction(from.getEnhancedBenefitAfterInterimDeduction());
    to.setEnhancedLateEnrolmentPenalty(from.getEnhancedLateEnrolmentPenalty());
    to.setEnhancedBenefitAfterLateEnrolmentPenalty(from.getEnhancedBenefitAfterLateEnrolmentPenalty());
    to.setPaymentCap(from.getPaymentCap());
    to.setBenefitAfterPaymentCap(from.getBenefitAfterPaymentCap());
    to.setEnhancedBenefitAfterPaymentCap(from.getEnhancedBenefitAfterPaymentCap());
    // see method comment for fields that are not copied
  }


  /**
   * copies all fields except for:
   *     farmingYear
   *     claimId
   *     revisionCount
   * 
   * @param to MarginTotal
   * @param from MarginTotal
   */
  public void copyMarginTotal(MarginTotal to, MarginTotal from) {
    to.setAccrualAdjsCropInventory(from.getAccrualAdjsCropInventory());
    to.setAccrualAdjsLvstckInventory(from.getAccrualAdjsLvstckInventory());
    to.setAccrualAdjsPayables(from.getAccrualAdjsPayables());
    to.setAccrualAdjsPurchasedInputs(from.getAccrualAdjsPurchasedInputs());
    to.setAccrualAdjsReceivables(from.getAccrualAdjsReceivables());
    to.setRatioStructuralChangeAdjs(from.getRatioStructuralChangeAdjs());
    to.setAdditiveStructuralChangeAdjs(from.getAdditiveStructuralChangeAdjs());
    to.setStructuralChangeAdjs(from.getStructuralChangeAdjs());
    to.setTotalAllowableExpenses(from.getTotalAllowableExpenses());
    to.setTotalAllowableIncome(from.getTotalAllowableIncome());
    to.setUnadjustedProductionMargin(from.getUnadjustedProductionMargin());
    to.setProductionMargAccrAdjs(from.getProductionMargAccrAdjs());
    to.setRatioProductionMargAftStrChangs(from.getRatioProductionMargAftStrChangs());
    to.setAdditiveProductionMargAftStrChangs(from.getAdditiveProductionMargAftStrChangs());
    to.setProductionMargAftStrChangs(from.getProductionMargAftStrChangs());
    to.setFiscalYearProRateAdj(from.getFiscalYearProRateAdj());
    to.setFarmSizeRatio(from.getFarmSizeRatio());
    to.setIsRatioStructuralChangeNotable(from.getIsRatioStructuralChangeNotable());
    to.setIsAdditiveStructuralChangeNotable(from.getIsAdditiveStructuralChangeNotable());
    to.setIsStructuralChangeNotable(from.getIsStructuralChangeNotable());
    to.setBpuLeadInd(from.getBpuLeadInd());
    to.setSupplyManagedCommodityIncome(from.getSupplyManagedCommodityIncome());
    to.setUnadjustedAllowableIncome(from.getUnadjustedAllowableIncome());
    to.setYardageIncome(from.getYardageIncome());
    to.setProgramPaymentIncome(from.getProgramPaymentIncome());
    to.setTotalUnallowableIncome(from.getTotalUnallowableIncome());
    to.setUnadjustedAllowableExpenses(from.getUnadjustedAllowableExpenses());
    to.setYardageExpenses(from.getYardageExpenses());
    to.setContractWorkExpenses(from.getContractWorkExpenses());
    to.setManualExpenses(from.getManualExpenses());
    to.setTotalUnallowableExpenses(from.getTotalUnallowableExpenses());
    to.setDeferredProgramPayments(from.getDeferredProgramPayments());
    to.setProductiveValue(from.getProductiveValue());
    to.setExpenseFarmSizeRatio(from.getExpenseFarmSizeRatio());
    to.setExpenseProductiveValue(from.getExpenseProductiveValue());
    to.setExpenseAccrualAdjs(from.getExpenseAccrualAdjs());
    to.setExpenseStructuralChangeAdjs(from.getExpenseStructuralChangeAdjs());
    to.setExpensesAfterStructuralChange(from.getExpensesAfterStructuralChange());
    to.setProdInsurDeemedSubtotal(from.getProdInsurDeemedSubtotal());
    to.setProdInsurDeemedTotal(from.getProdInsurDeemedTotal());
  }

}
