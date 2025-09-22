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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.calculator.StructuralChangeCalculator;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;

/**
 * @author awilkinson
 */
public class BasicStructuralChangeCalculator extends StructuralChangeCalculator {
  
  public BasicStructuralChangeCalculator(Scenario scenario) {
    super(scenario);
  }


  /**
   * @return Map<String key, List<ProductiveUnitCapacity>>
   */
  @Override
  public Map<String, List<ProductiveUnitCapacity>> getProgramYearPucMap() {
    Map<String, List<ProductiveUnitCapacity>> pucMap = getPucMap(scenario.getFarmingYear());
    
    return pucMap;
  }

  
  /**
   * @return map<year, map<iic-sgc, List<ProductiveUnitCapacity>>>
   */
  @Override
  public Map<Integer, Map<String, List<ProductiveUnitCapacity>>> getRefYearPucMap() {
    Map<Integer, Map<String, List<ProductiveUnitCapacity>>> refYearsPucMap = new HashMap<>();
    
    for(ReferenceScenario rs : scenario.getReferenceScenarios()) {
      Map<String, List<ProductiveUnitCapacity>> refYearPucMap = getPucMap(rs.getFarmingYear());
      Integer refYear = rs.getYear();
      
      refYearsPucMap.put(refYear, refYearPucMap);
    }
    
    return refYearsPucMap;
  }


  /**
   * @return boolean[]
   */
  @Override
  public boolean[] getBpuLeadIndArray() {
    int index = 0;
    
    List<ReferenceScenario> refScenarios = scenario.getReferenceScenarios();
    int arraySize = refScenarios.size();
    boolean[] bpuLeads = new boolean[arraySize];
    
    for(ReferenceScenario rs : refScenarios) {
      MarginTotal mt = rs.getFarmingYear().getMarginTotal();
      boolean b = true;
      
      if(mt != null && mt.getBpuLeadInd() != null) {
        b = mt.getBpuLeadInd().booleanValue();
      }
      bpuLeads[index] = b;
       
      index++;
    }
    
    return bpuLeads;
  }


  /**
   * @param bpuLeads boolean[]
   */
  @Override
  public void updateBpuLeadInds(boolean[] bpuLeads) {
    int index = 0;
    
    List<ReferenceScenario> refScenarios = scenario.getReferenceScenarios();
    
    for(ReferenceScenario rs : refScenarios) {
      MarginTotal mt = rs.getFarmingYear().getMarginTotal();
      
      Boolean b = Boolean.valueOf(bpuLeads[index]);
      mt.setBpuLeadInd(b);
      
       
      index++;
    }
    
  }

}
