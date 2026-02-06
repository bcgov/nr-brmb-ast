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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ca.bc.gov.srm.farm.domain.BasePricePerUnitYear;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;

/**
 * 
 */
public abstract class BenefitValidator {
  
  public static final Collection<String> BPU_CODES_ALLOWING_ZEROES = Arrays.asList(new String[]{"17","18","19"});
  
  private Set<String> refYearMissingBpuInventoryCodes = new HashSet<>();
  private Set<String> refYearMissingBpuStructureGroupCodes = new HashSet<>();
  private Set<String> programYearMissingBpuInventoryCodes = new HashSet<>();
  private Set<String> programYearMissingBpuStructureGroupCodes = new HashSet<>();
  
  
  public List<String> getRefYearMissingBpuInventoryCodeList() {
    return toList(refYearMissingBpuInventoryCodes);
  }

  public List<String> getRefYearMissingBpuStructureGroupCodeList() {
    return toList(refYearMissingBpuStructureGroupCodes);
  }
  
  protected Set<String> getRefYearMissingBpuInventoryCodes() {
    return refYearMissingBpuInventoryCodes;
  }

  protected Set<String> getRefYearMissingBpuStructureGroupCodes() {
    return refYearMissingBpuStructureGroupCodes;
  }
  
  
  public List<String> getProgramYearMissingBpuInventoryCodeList() {
    return toList(programYearMissingBpuInventoryCodes);
  }

  public List<String> getProgramYearMissingBpuStructureGroupCodeList() {
    return toList(programYearMissingBpuStructureGroupCodes);
  }

  public Set<String> getProgramYearMissingBpuInventoryCodes() {
    return programYearMissingBpuInventoryCodes;
  }

  public Set<String> getProgramYearMissingBpuStructureGroupCodes() {
    return programYearMissingBpuStructureGroupCodes;
  }

  protected void clearMissingBpuCodes() {
    Set<String> missingRefYearBpuInventoryCodes = getRefYearMissingBpuInventoryCodes();
    Set<String> missingRefYearBpuStructureGroupCodes = getRefYearMissingBpuStructureGroupCodes();
    Set<String> missingProgramYearBpuInventoryCodes = getProgramYearMissingBpuInventoryCodes();
    Set<String> missingProgramYearBpuStructureGroupCodes = getProgramYearMissingBpuStructureGroupCodes();
    missingRefYearBpuInventoryCodes.clear();
    missingRefYearBpuStructureGroupCodes.clear();
    missingProgramYearBpuInventoryCodes.clear();
    missingProgramYearBpuStructureGroupCodes.clear();
  }


  private List<String> toList(Set<String> set) {
    ArrayList<String> list = new ArrayList<>();
    
    list.addAll(set);
    Collections.sort(list);
    
    return list;
  }

  public boolean isValid() {
    return getRefYearMissingBpuInventoryCodes().isEmpty()
        && getRefYearMissingBpuStructureGroupCodes().isEmpty()
        && getProgramYearMissingBpuInventoryCodes().isEmpty()
        && getProgramYearMissingBpuStructureGroupCodes().isEmpty();
  }

  /**
   * 
   * @param programYearScenario programYearScenario
   * @return true if OK
   */
  public abstract boolean validateBpus(Scenario programYearScenario);
  
  /**
   * @param scenario scenario
   * @return true if OK
   */
  public boolean validateReferenceYears(Scenario scenario) {
    
    boolean ok = true;
    for(ReferenceScenario refScenario : scenario.getAllScenarios()) {
      // For combined farms, margin totals are calculated for the whole farm
      // and copied to each scenario so we just need to check the current one.
      MarginTotal refYearMargin = refScenario.getFarmingYear().getMarginTotal();

      double income = refYearMargin.getTotalAllowableIncome();
      double expenses = refYearMargin.getTotalAllowableExpenses();
      
      if((income == 0) && (expenses == 0)) {
        ok = false;
        break;
      }
    }
    
    return ok;
  }
  
  
  /**
   * 
   * @param programYearScenario programYearScenario
   * @return true if OK
   */
  public abstract boolean hasEnoughReferenceYears(Scenario programYearScenario);
  
  
  /**
   * 
   * @param programYearScenario programYearScenario
   * @return true if OK
   */
  public boolean scenarioHasEnoughReferenceYears(Scenario programYearScenario) {
    int numRefYears = programYearScenario.getReferenceScenarios().size();
    boolean ok = (CalculatorConfig.MIN_NUM_REF_YEARS <= numRefYears);
    return ok;
  }
  
  
  /**
   * 
   * @param fy fy
   */
  public void validateBpus(FarmingYear fy) {

    if(fy.getFarmingOperations() != null) {

      for(FarmingOperation fo : fy.getFarmingOperations()) {
        
        //
        // Crops have PUCs, some livestock don't
        //
        if(fo.getProductiveUnitCapacitiesForStructureChange() != null) {
          validateBpus(fo);
        }
      }
    }
  }
  
  
  private void validateBpus(FarmingOperation fo) {
    ReferenceScenario refScenario = fo.getFarmingYear().getReferenceScenario();
    int programYear = refScenario.getParentScenario().getYear().intValue();
    int pucYear = refScenario.getYear().intValue();
    
    MarginTotal marginTotal = refScenario.getFarmingYear().getMarginTotal();
    boolean lead = true;
    if(marginTotal != null && marginTotal.getBpuLeadInd() != null) {
      lead = marginTotal.getBpuLeadInd();
    }
    
    for(ProductiveUnitCapacity puc : fo.getProductiveUnitCapacitiesForStructureChange()) {
      boolean ok = false;
      
      //
      // The capacity could be zero, if the item was entered in error,
      // and then the user entered a negative adjustment to "zero out"
      // the item in error. In this case don't expect a BPU because
      // the inventory code was likely wrong.
      //
      if(puc.getTotalProductiveCapacityAmount() == 0) {
        ok = true;
      } else {
        if(puc.getBasePricePerUnit() != null) {
          
          // For some inventory codes, a value of zero is allowed
          String inventoryCode = puc.getBasePricePerUnit().getInventoryCode();
          boolean skipValidation = inventoryCode != null && BPU_CODES_ALLOWING_ZEROES.contains(inventoryCode);

          List<BasePricePerUnitYear> bpuYears = puc.getBasePricePerUnit().getBasePricePerUnitYears();
          
          if(skipValidation) {
            ok = true;
          } else if(pucYear == programYear) {
            ok = validateBpuForProgramYear(programYear, bpuYears, refScenario.getParentScenario());
          } else {
            ok = validateBpuForReferenceYear(programYear, pucYear, bpuYears, lead);
          }
          
        }
      }
      
      if(!ok) {
        if(puc.getInventoryItemCode() == null) {
          if(pucYear == programYear) {
            programYearMissingBpuStructureGroupCodes.add(puc.getStructureGroupCode());
          } else {
            refYearMissingBpuStructureGroupCodes.add(puc.getStructureGroupCode());
          }
        } else {
          if(pucYear == programYear) {
            programYearMissingBpuInventoryCodes.add(puc.getInventoryItemCode());
          } else {
            refYearMissingBpuInventoryCodes.add(puc.getInventoryItemCode());
          }
        }
      }
    }
  }


  /**
   * @return true if the BPU has valid margin and expense values for the five years prior to the program year
   */
  private boolean validateBpuForProgramYear(int programYear, List<BasePricePerUnitYear> bpuYears, Scenario scenario) {
    
    BenchmarkMarginCalculator benchmarkMarginCalculator = CalculatorFactory.getBenchmarkMarginCalculator(scenario);
    List<Integer> bpuYearIntegers = benchmarkMarginCalculator.getBpuYears(scenario, false, true);
    
    boolean ok = true;
    yearIntegers: for(Integer year : bpuYearIntegers) {
      for(BasePricePerUnitYear bpuYear : bpuYears) {
        if(year.equals(bpuYear.getYear())) {
          if(!validateBpuYear(programYear, bpuYear)) {
            ok = false;
            break yearIntegers;
          }
        }
      }
    }
    
    return ok;
  }


  /**
   * @param programYear programYear
   * @param pucYear pucYear
   * @param bpuYears bpuYears
   * @param lead lead
   * @return true if the BPU has valid margin and expense values for the reference year (or the previous year if lead = false)
   */
  private boolean validateBpuForReferenceYear(int programYear, int pucYear, List<BasePricePerUnitYear> bpuYears, boolean lead) {
    boolean ok = false;
    int yearToLookFor = pucYear;
    if(!lead) {
      yearToLookFor--;
    }
    
    for(BasePricePerUnitYear bpuYear : bpuYears) {
      if(yearToLookFor == bpuYear.getYear().intValue()) {
        
        ok = validateBpuYear(programYear, bpuYear);
        break;
      }
    }
    return ok;
  }


  /**
   * @param programYear programYear
   * @param bpuYear bpuYear
   * @return true if 
   */
  private boolean validateBpuYear(int programYear, BasePricePerUnitYear bpuYear) {
    
    boolean marginOk = bpuYear.getMargin() != 0;
    boolean expenseOk = programYear < CalculatorConfig.GROWING_FORWARD_2013
        || (bpuYear.getExpense() != null && bpuYear.getExpense() != 0);
    return marginOk && expenseOk;
  }

}
