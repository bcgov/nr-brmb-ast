/**
 *
 * Copyright (c) 2018,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.calculator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

/**
 * @author awilkinson
 */
public abstract class BenchmarkMarginCalculator {

  private Logger logger = LoggerFactory.getLogger(getClass());
  
  public abstract double calculateBenchmarkMargin(Scenario scenario, String calculationType, boolean onlyYearsUsedInCalc, boolean applyLag);

  public double calculateIndividualBenchmarkMargin(Scenario scenario, String calculationType, boolean onlyYearsUsedInCalc, boolean applyLag) {
    logger.debug("Calculating Individual Benchmark Margin...");
    
    ProductiveValueCalculator pvCalc = CalculatorFactory.getProductiveValueCalculator(scenario);
    
    double individualBenchmarkMargin = 0;
    
    List<Integer> yearsToUse = getBpuYears(scenario, onlyYearsUsedInCalc, applyLag);
    
    List<FarmingOperation> farmingOperations = scenario.getFarmingYear().getFarmingOperations();
    for(FarmingOperation fo : farmingOperations) {
      double partnershipPercent = ScenarioUtils.getPartnershipPercent(fo);
      
      if(fo.getProductiveUnitCapacitiesForStructureChange() != null) {
        for(ProductiveUnitCapacity puc : fo.getProductiveUnitCapacitiesForStructureChange()) {
          double bpuAverage = pvCalc.calculateBpuAverage(puc, calculationType, yearsToUse);
          
          double amount = puc.getTotalProductiveCapacityAmount();
          double value = amount * bpuAverage;
          value = value * partnershipPercent;
          individualBenchmarkMargin += value;
        }
      }
    }
    return individualBenchmarkMargin;
  }

  public List<Integer> getBpuYears(Scenario scenario, boolean onlyYearsUsedInCalc, boolean applyLag) {
    int programYear = scenario.getYear().intValue();
    
    List<Integer> yearsToUse = null; // Use a list, not a set, because there can be duplicates.
    if(onlyYearsUsedInCalc || applyLag) {
      yearsToUse = new ArrayList<>();
      for(ReferenceScenario refScenario : scenario.getReferenceScenarios()) {
        if(!onlyYearsUsedInCalc || refScenario.getUsedInCalc()) {
          int bpuYear = refScenario.getYear().intValue();
          boolean lag = ! refScenario.getFarmingYear().getMarginTotal().getBpuLeadInd();
          if(applyLag && lag) {
            bpuYear--;
          }
          yearsToUse.add(Integer.valueOf(bpuYear));
        }
      }
    }
    
    if(yearsToUse == null) {
      final int fiveYears = 5; // use a 5-year average
      yearsToUse = new ArrayList<>();
      int minYear = programYear - fiveYears;
      for(int curYear = minYear; curYear < programYear; curYear++) {
        yearsToUse.add(Integer.valueOf(curYear));
      }
    }
    return yearsToUse;
  }

}
