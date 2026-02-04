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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.calculator.StructuralChangeCalculator;
import ca.bc.gov.srm.farm.domain.CombinedFarm;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;

/**
 * @author awilkinson
 */
public class CombinedStructuralChangeCalculator extends StructuralChangeCalculator {
  
  public CombinedStructuralChangeCalculator(Scenario scenario) {
    super(scenario);
  }

  @Override
  public Map<String, List<ProductiveUnitCapacity>> getProgramYearPucMap() {
    CombinedFarm combinedFarm = scenario.getCombinedFarm();
    Map<String, List<ProductiveUnitCapacity>> pucMap;
    
    List<Scenario> scenarios = combinedFarm.getScenarios();
    List<Map<String, List<ProductiveUnitCapacity>>> pucMaps = new ArrayList<>();
    for(Scenario curScenario : scenarios) {
      Map<String, List<ProductiveUnitCapacity>> curPucMap = getPucMap(curScenario.getFarmingYear());
      pucMaps.add(curPucMap);
    }
    
    pucMap = mergePucMaps(pucMaps);
    
    return pucMap;
  }

  
  @Override
  public Map<Integer, Map<String, List<ProductiveUnitCapacity>>> getRefYearPucMap() {
    CombinedFarm combinedFarm = scenario.getCombinedFarm();
    Map<Integer, Map<String, List<ProductiveUnitCapacity>>> refYearsPucMap = new HashMap<>();
    
    List<Integer> years = combinedFarm.getReferenceYears();
    
    for(Integer refYear : years) {
      List<Map<String, List<ProductiveUnitCapacity>>> refYearPucMaps = new ArrayList<>();

      List<ReferenceScenario> refScenarios = combinedFarm.getReferenceScenariosByYear(refYear);
      for(ReferenceScenario rs : refScenarios) {
        
        Map<String, List<ProductiveUnitCapacity>> curPucMap = getPucMap(rs.getFarmingYear());
        refYearPucMaps.add(curPucMap);
      }
      
      Map<String, List<ProductiveUnitCapacity>> refYearPucMap = mergePucMaps(refYearPucMaps);
      refYearsPucMap.put(refYear, refYearPucMap);
    }
    
    return refYearsPucMap;
  }


  /**
   * @param pucMaps List<Map<String key, List<ProductiveUnitCapacity>>>
   * @return Map<String key, List<ProductiveUnitCapacity>>
   */
  private Map<String, List<ProductiveUnitCapacity>> mergePucMaps(List<Map<String, List<ProductiveUnitCapacity>>> pucMaps) {
    Map<String, List<ProductiveUnitCapacity>> result = new HashMap<>();
    
    for(Map<String, List<ProductiveUnitCapacity>> pucMap : pucMaps) {
      
      for(String key : pucMap.keySet()) {
        List<ProductiveUnitCapacity> curPucList = pucMap.get(key);
        
        List<ProductiveUnitCapacity> resultPucList = result.get(key);
        if(resultPucList == null) {
          resultPucList = new ArrayList<>();
          result.put(key, resultPucList);
        }
        resultPucList.addAll(curPucList);
      }
    }
    
    return result;
  }


  @Override
  public boolean[] getBpuLeadIndArray() {
    int index = 0;
    
    CombinedFarm combinedFarm = scenario.getCombinedFarm();
    List<Integer> refYears = combinedFarm.getReferenceYearsIncludingMissing();
    Map<Integer, MarginTotal> yearMargins = combinedFarm.getYearMargins();
    
    int arraySize = refYears.size();
    boolean[] bpuLeads = new boolean[arraySize];
    
    for(Integer refYear : refYears) {
      MarginTotal mt = yearMargins.get(refYear);
      boolean b = true;
      
      if(mt != null && mt.getBpuLeadInd() != null) {
        b = mt.getBpuLeadInd();
      }
      
      bpuLeads[index] = b;
       
      index++;
    }
    
    return bpuLeads;
  }
  
  
  @Override
  public void updateBpuLeadInds(boolean[] bpuLeads) {
    int index = 0;
    
    CombinedFarm combinedFarm = scenario.getCombinedFarm();
    List<Integer> refYears = combinedFarm.getReferenceYearsIncludingMissing();
    Map<Integer, MarginTotal> yearMargins = combinedFarm.getYearMargins();
    List<Scenario> scenarios = combinedFarm.getScenarios();
    
    for(Integer refYear : refYears) {
      MarginTotal mt = yearMargins.get(refYear);
      
      if(mt != null) {
        boolean lead = bpuLeads[index];
        mt.setBpuLeadInd(lead);
        
        for(Scenario curScenario : scenarios) {
          ReferenceScenario refScenario = curScenario.getReferenceScenarioByYear(refYear);
          refScenario.getFarmingYear().getMarginTotal().setBpuLeadInd(lead);
        }
      }
      
      index++;
    }
  }

}
