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

import ca.bc.gov.srm.farm.calculator.ProductionMarginCalculator;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

/**
 * @author awilkinson
 */
public class BasicProductionMarginCalculator extends ProductionMarginCalculator {
  
  public BasicProductionMarginCalculator(Scenario scenario) {
    super(scenario);
  }

  /**
   * Calculates the unadjusted production margin for the program year and all 
   * reference years and puts the results into the Margin and MarginTotal 
   * objects.
   */
  @Override
  public void calculateUnadjusted() {
    FarmingYear fy = scenario.getFarmingYear();
    boolean isProgramYear = true;
    boolean isDeemedFarmingYear = true; // always true for program year
    Integer year = scenario.getYear();
    Boolean bpuLead = Boolean.FALSE;
    
    // calculate for program year
    calculateUnadjusted(fy, isProgramYear, isDeemedFarmingYear, year, bpuLead);
    
    // calculate for reference years
    isProgramYear = false;
    
    for(ReferenceScenario rs : scenario.getReferenceScenarios()) {
      
      fy = rs.getFarmingYear();
      year = rs.getYear();
      isDeemedFarmingYear = rs.getIsDeemedFarmingYear().booleanValue();
      bpuLead = fy.getMarginTotal().getBpuLeadInd();
      
      calculateUnadjusted(fy, isProgramYear, isDeemedFarmingYear, year, bpuLead);
    }
  }
  
  
  /**
   * Calculates the "production margin with accrual adjustments" for the 
   * program year and all reference years and puts the results into the 
   * Margin and MarginTotal objects.
   */
  @Override
  public void calculateWithAccruals() {
    // calculate for program year
    calculateWithAccruals(scenario.getFarmingYear());
    
    // calculate for reference years
    for(ReferenceScenario rs : scenario.getReferenceScenarios()) {
      
      calculateWithAccruals(rs.getFarmingYear());
    }
  }
  
  
  @Override
  public void calculateWithStructuralChange() {
    for(ReferenceScenario rs : scenario.getReferenceScenarios()) {
      
      MarginTotal yearMargin = rs.getFarmingYear().getMarginTotal();
      calculateWithStructuralChange(yearMargin);
    }
  }

  
  
  /**
   *
   * @param fy fy
   * @param isProgramYear isProgramYear
   * @param isDeemedFarmingYear isDeemedFarmingYear
   * @param year year
   * @param bpuLeadInd bpuLeadInd
   */
  protected void calculateUnadjusted(
      FarmingYear fy, 
      boolean isProgramYear,
      boolean isDeemedFarmingYear,
      Integer year,
      Boolean bpuLeadInd) {
    
    double total = 0;
    
    for(FarmingOperation fo : fy.getFarmingOperations()) {
      
      calculateUnadjusted(fo, isProgramYear, isDeemedFarmingYear, year, bpuLeadInd);
      
      double partnershipPercent = ScenarioUtils.getPartnershipPercent(fo);
      double margin = fo.getMargin().getUnadjustedProductionMargin().doubleValue();
      double partnershipMargin = margin * partnershipPercent;
      
      total += partnershipMargin;
    }
    
    fy.getMarginTotal().setUnadjustedProductionMargin(new Double(total));
  }
  
  
  /**
   * 
   * @param fy fy
   */
  private void calculateWithAccruals(FarmingYear fy) {
    double total = 0;
    
    for(FarmingOperation fo : fy.getFarmingOperations()) {
      
      calculateWithAccruals(fo);
      
      double partnershipPercent = ScenarioUtils.getPartnershipPercent(fo);
      double margin = fo.getMargin().getProductionMargAccrAdjs().doubleValue();
      double partnershipMargin = margin * partnershipPercent;
      
      total += partnershipMargin;
    }
    
    fy.getMarginTotal().setProductionMargAccrAdjs(new Double(total));
  }

}
