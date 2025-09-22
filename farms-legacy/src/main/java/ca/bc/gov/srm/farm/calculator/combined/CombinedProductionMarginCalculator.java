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

import ca.bc.gov.srm.farm.calculator.ProductionMarginCalculator;
import ca.bc.gov.srm.farm.domain.CombinedFarm;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

/**
 * @author awilkinson
 */
public class CombinedProductionMarginCalculator extends ProductionMarginCalculator {
  
  public CombinedProductionMarginCalculator(Scenario scenario) {
    super(scenario);
  }

  /**
   * Calculates the unadjusted production margin for the program year and all 
   * reference years and puts the results into the Margin and MarginTotal 
   * objects.
   */
  @Override
  public void calculateUnadjusted() {
    boolean isProgramYear = true;
    boolean isDeemedFarmingYear = true; // always true for program year
    Integer programYear = scenario.getYear();
    
    CombinedFarm combinedFarm = scenario.getCombinedFarm();

    // calculate for program year
    calculateUnadjusted(combinedFarm, isProgramYear, programYear, isDeemedFarmingYear);
    
    // calculate for reference years
    isProgramYear = false;
    
    List<Integer> refYears = combinedFarm.getReferenceYears();
    for(Integer curYear : refYears) {
      Boolean deemed = combinedFarm.getDeemedFarmingYearMap().get(curYear);
      isDeemedFarmingYear = deemed.booleanValue();
      
      calculateUnadjusted(combinedFarm, isProgramYear, curYear, isDeemedFarmingYear);
    }
  }
  
  
  /**
   * @param combinedFarm combinedFarm
   * @param isProgramYear isProgramYear
   * @param year year
   * @param isDeemedFarmingYear isDeemedFarmingYear
   */
  protected void calculateUnadjusted(
      CombinedFarm combinedFarm, 
      boolean isProgramYear,
      Integer year,
      boolean isDeemedFarmingYear) {
    
    MarginTotal yearMargin = combinedFarm.getYearMargins().get(year);
    double total = 0;
    
    List<ReferenceScenario> refScenarios = combinedFarm.getReferenceScenariosByYear(year);
    for(ReferenceScenario rs : refScenarios) {
      
      Boolean bpuLead;
      if(isProgramYear) {
        bpuLead = Boolean.FALSE;    // always false for program year
      } else {
        bpuLead = yearMargin.getBpuLeadInd();
      }
      
      for(FarmingOperation fo : rs.getFarmingYear().getFarmingOperations()) {
        
        calculateUnadjusted(fo, isProgramYear, isDeemedFarmingYear, year, bpuLead);
        
        double partnershipPercent = ScenarioUtils.getPartnershipPercent(fo);
        double margin = fo.getMargin().getUnadjustedProductionMargin().doubleValue();
        double partnershipMargin = margin * partnershipPercent;
        
        total += partnershipMargin;
      }
    }
    
    yearMargin.setUnadjustedProductionMargin(new Double(total));
  }
  
  
  /**
   * Calculates the "production margin with accrual adjustments" for the 
   * program year and all reference years and puts the results into the 
   * Margin and MarginTotal objects.
   */
  @Override
  public void calculateWithAccruals() {
    CombinedFarm combinedFarm = scenario.getCombinedFarm();
    
    // calculate for program year
    calculateWithAccruals(combinedFarm, scenario.getYear());
    
    // calculate for reference years
    for(ReferenceScenario rs : scenario.getReferenceScenarios()) {
      Integer year = rs.getYear();
      
      calculateWithAccruals(combinedFarm, year);
    }
  }
  
  
  /**
   * Calculates the "production margin after structural change" for the 
   * reference years and puts the results into the MarginTotal objects.
   * Structural change is only applied to reference years to try
   * to make them look smiliar to the program year.
   */
  @Override
  public void calculateWithStructuralChange() {
    CombinedFarm combinedFarm = scenario.getCombinedFarm();

    // reference years only
    List<Integer> refYears = combinedFarm.getReferenceYears();
    for(Integer curYear : refYears) {
      
      MarginTotal yearMargin = combinedFarm.getYearMargins().get(curYear);
      calculateWithStructuralChange(yearMargin);
    }
  }
  
  
  /**
   * 
   * @param combinedFarm combinedFarm
   * @param year year
   */
  private void calculateWithAccruals(
      CombinedFarm combinedFarm,
      Integer year) {

    MarginTotal yearMargin = combinedFarm.getYearMargins().get(year);
    double total = 0;
    
    List<ReferenceScenario> refScenarios = combinedFarm.getReferenceScenariosByYear(year);
    for(ReferenceScenario rs : refScenarios) {
      
      for(FarmingOperation fo : rs.getFarmingYear().getFarmingOperations()) {
        
        calculateWithAccruals(fo);
        
        double partnershipPercent = ScenarioUtils.getPartnershipPercent(fo);
        double margin = fo.getMargin().getProductionMargAccrAdjs().doubleValue();
        double partnershipMargin = margin * partnershipPercent;
        
        total += partnershipMargin;
      }
    }
    
    yearMargin.setProductionMargAccrAdjs(new Double(total));
  }

}
