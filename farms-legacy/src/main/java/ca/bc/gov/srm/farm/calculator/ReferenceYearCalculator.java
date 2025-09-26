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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.Scenario;


/**
 * 
 */
public abstract class ReferenceYearCalculator {

  /**
   * @param scenario scenario
   * @param useStructuralChanges useStructuralChanges
   * @param useStructuralChangeEvenIfNotNotable useStructuralChangeEvenIfNotNotable
   * @return Map<year, MarginTotal>
   */
  public Map<Integer, MarginTotal> getReferenceYearMargins(
      Scenario scenario, 
      boolean useStructuralChanges,
      boolean useStructuralChangeEvenIfNotNotable) {
    
    return getReferenceYearMargins(
        scenario, 
        useStructuralChanges,
        useStructuralChangeEvenIfNotNotable,
        CalculatorConfig.STRUCTURAL_CHANGE_METHOD_SELECTED);
  }
  
  /**
   * @param scenario scenario
   * @param useStructuralChanges useStructuralChanges
   * @param useStructuralChangeEvenIfNotNotable useStructuralChangeEvenIfNotNotable
   * @return Map<year, MarginTotal>
   */
  public Map<Integer, MarginTotal> getReferenceYearMargins(
      Scenario scenario, 
      boolean useStructuralChanges,
      boolean useStructuralChangeEvenIfNotNotable,
      String structuralChangeMethod) {
    
    Map<Integer, MarginTotal> yearMargins;
    
    if(hasFiveYearsOfData(scenario)) {
      yearMargins = getOlympicAverageYears(scenario, useStructuralChanges, useStructuralChangeEvenIfNotNotable, structuralChangeMethod);
    } else {
      yearMargins = getMostRecentYears(scenario);
    }
    
    return yearMargins;
  }


  /**
   * @param scenario scenario
   * @return true is it has five deemed farming years
   */
  protected abstract boolean hasFiveYearsOfData(Scenario scenario);
  
  /**
   * 
   * @param scenario scenario
   * @return the three most recent years ReferenceScenarios
   */
  protected abstract Map<Integer, MarginTotal> getMostRecentYears(Scenario scenario);
  
  /**
   * Throw out the highest and lowset
   * 
   * @return Map<year, MarginTotal>
   */
  protected abstract Map<Integer, MarginTotal> getOlympicAverageYears(
      Scenario scenario,
      boolean useStructuralChanges,
      boolean useStructuralChangeEvenIfNotNotable, String structuralChangeMethod);


  protected Map<Integer, MarginTotal> getOlympicAverageYears(Scenario scenario, boolean useStructuralChanges,
      boolean useStructuralChangeEvenIfNotNotable, Map<Integer, MarginTotal> allYearMargins, List<Integer> refYears, String structuralChangeMethod) {
    Map<Integer, MarginTotal> yearMargins = new HashMap<>();
    double minMargin = Double.MAX_VALUE;
    double maxMargin = 0 - Double.MAX_VALUE;  // Double.MIN_VALUE isn't negative
    Integer minMarginYear = null;
    Integer maxMarginYear = null;
    
    for(Integer refYear : refYears) {
      MarginTotal refYearMargin = allYearMargins.get(refYear);
      double margin = refYearMargin.getProductionMargAccrAdjs().doubleValue();
      
      if(CalculatorConfig.STRUCTURAL_CHANGE_METHOD_SELECTED.equals(structuralChangeMethod)) {
        if(useStructuralChanges) {
          if(useStructuralChangeEvenIfNotNotable) {
            margin = refYearMargin.getProductionMargAccrAdjs().doubleValue() + refYearMargin.getStructuralChangeAdjs().doubleValue();
          } else {
            margin = refYearMargin.getProductionMargAftStrChangs().doubleValue();
          }
        }
      } else if(CalculatorConfig.STRUCTURAL_CHANGE_METHOD_RATIO.equals(structuralChangeMethod)) {
        if(useStructuralChanges) {
          if(useStructuralChangeEvenIfNotNotable) {
            margin = refYearMargin.getProductionMargAccrAdjs().doubleValue() + refYearMargin.getRatioStructuralChangeAdjs().doubleValue();
          } else {
            margin = refYearMargin.getRatioProductionMargAftStrChangs().doubleValue();
          }
        }
      } else if(CalculatorConfig.STRUCTURAL_CHANGE_METHOD_ADDITIVE.equals(structuralChangeMethod)) {
        if(useStructuralChanges) {
          if(useStructuralChangeEvenIfNotNotable) {
            margin = refYearMargin.getProductionMargAccrAdjs().doubleValue() + refYearMargin.getAdditiveStructuralChangeAdjs().doubleValue();
          } else {
            margin = refYearMargin.getAdditiveProductionMargAftStrChangs().doubleValue();
          }
        }
      }
      
      if(margin < minMargin) {
        minMargin = margin;
        minMarginYear = refYear;
      }
      
      if(margin > maxMargin) {
        maxMargin = margin;
        maxMarginYear = refYear;
      }
    }
    
    if(minMarginYear == maxMarginYear) {
      // In this case, all margins are the same, so we need to set the max to the second year.
      maxMarginYear = scenario.getReferenceScenarios().get(1).getYear();
    }
    
    for(Integer refYear : refYears) {
      
      if(!refYear.equals(minMarginYear) && !refYear.equals(maxMarginYear)) {
        MarginTotal refYearMargin = allYearMargins.get(refYear);
        yearMargins.put(refYear, refYearMargin);
      }
    }
    
    return yearMargins;
  }

}
