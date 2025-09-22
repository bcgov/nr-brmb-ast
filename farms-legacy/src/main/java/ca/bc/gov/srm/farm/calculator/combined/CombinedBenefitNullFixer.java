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

import ca.bc.gov.srm.farm.calculator.BenefitNullFixer;
import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.domain.CombinedFarm;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes;
import ca.bc.gov.srm.farm.domain.codes.StructuralChangeCodes;

/**
 * @author awilkinson
 */
public class CombinedBenefitNullFixer extends BenefitNullFixer {
  

  /**
   * @param programYearScenario Scenario
   */
  @Override
  public void fixNulls(Scenario programYearScenario) {
    
    CombinedFarm combinedFarm = programYearScenario.getCombinedFarm();
    
    fixNullCombinedMarginTotals(combinedFarm);
    
    fixNullBenefits(combinedFarm);
    
    fixNullStructuralChangeCode(combinedFarm);
    
    fixNullInterimBenefitPercent(combinedFarm);
    
    fixFarmingYearNulls(combinedFarm);
    
    fixNullBpuLeadInds(combinedFarm);
    
  }


  /**
   * @param combinedFarm CombinedFarm
   */
  private void fixNullCombinedMarginTotals(
      CombinedFarm combinedFarm) {
    Map<Integer, MarginTotal> yearMargins = combinedFarm.getYearMargins();

    List<Integer> years = combinedFarm.getAllYears();
    for(Integer curYear : years) {
      MarginTotal yearMargin = yearMargins.get(curYear);
      if(yearMargin == null) {
        yearMargin = new MarginTotal();
      }
      yearMargins.put(curYear, yearMargin);
    }
  }


  /**
   * @param combinedFarm CombinedFarm
   */
  private void fixNullBenefits(CombinedFarm combinedFarm) {
    List<Scenario> scenarios = combinedFarm.getScenarios();
    Benefit combBenefit = combinedFarm.getBenefit();
    if(combBenefit == null) {
      combBenefit = new Benefit();
      combinedFarm.setBenefit(combBenefit);
    }
    for(Scenario curScenario : scenarios) {
      Benefit curBenefit = curScenario.getFarmingYear().getBenefit();
      if(curBenefit == null) {
        curBenefit = new Benefit();
        curScenario.getFarmingYear().setBenefit(curBenefit);
      }
    }
  }


  /**
   * Make sure structural change code is set.
   * @param combinedFarm CombinedFarm
   */
  private void fixNullStructuralChangeCode(CombinedFarm combinedFarm) {
    List<Scenario> scenarios = combinedFarm.getScenarios();
    Benefit combinedBenefit = combinedFarm.getBenefit();

    // Supposed to use RATIO SC method by default, except for Cash Margins
    // This may never be called because nulls are usually fixed when a
    // scenario is created and it's never a combined farm when it's created.
    final String defaultCode;
    Boolean isCashMargins = false;
    for(Scenario curScenario : scenarios) {
      isCashMargins = curScenario.getFarmingYear().getIsCashMargins();
      if(isCashMargins) {
        break;
      }
    }
    
    if(isCashMargins) {
      defaultCode = StructuralChangeCodes.ADDITIVE;
    } else {
      defaultCode = StructuralChangeCodes.RATIO;
    }

    String structuralChangeCode = combinedBenefit.getStructuralChangeMethodCode();
    if(structuralChangeCode == null) {
      structuralChangeCode = defaultCode;
      combinedBenefit.setStructuralChangeMethodCode(structuralChangeCode);
    }
    
    String expenseStructuralChangeCode = combinedBenefit.getExpenseStructuralChangeMethodCode();
    if(expenseStructuralChangeCode == null) {
      expenseStructuralChangeCode = defaultCode;
      combinedBenefit.setExpenseStructuralChangeMethodCode(expenseStructuralChangeCode);
    }
    
    for(Scenario curScenario : scenarios) {
      Benefit curBenefit = curScenario.getFarmingYear().getBenefit();
      curBenefit.setStructuralChangeMethodCode(structuralChangeCode);
      curBenefit.setExpenseStructuralChangeMethodCode(expenseStructuralChangeCode);
    }
  }
  
  
  /**
   * Make sure interim benefit percent is set.
   * @param combinedFarm CombinedFarm
   */
  private void fixNullInterimBenefitPercent(CombinedFarm combinedFarm) {
    List<Scenario> scenarios = combinedFarm.getScenarios();
    Benefit combinedBenefit = combinedFarm.getBenefit();
    Scenario firstScenario = scenarios.get(0);
    String scenarioCategoryCode = firstScenario.getScenarioCategoryCode();
    boolean isInterim = scenarioCategoryCode.equals(ScenarioCategoryCodes.INTERIM);
    
    Double interimBenefitPercent = combinedBenefit.getInterimBenefitPercent();
    if(isInterim && interimBenefitPercent == null) {
      interimBenefitPercent = Double.valueOf(CalculatorConfig.INTERIM_BENEFIT_FACTOR);
    } else if(!isInterim) {
      interimBenefitPercent = null;
    }
    
    combinedBenefit.setInterimBenefitPercent(interimBenefitPercent);
    for(Scenario curScenario : scenarios) {
      Benefit curBenefit = curScenario.getFarmingYear().getBenefit();
      curBenefit.setInterimBenefitPercent(interimBenefitPercent);
    }
  }


  /**
   * @param combinedFarm CombinedFarm
   */
  private void fixFarmingYearNulls(CombinedFarm combinedFarm) {
    List<Scenario> scenarios = combinedFarm.getScenarios();

    for(Scenario curScenario : scenarios) {

      for(ReferenceScenario rs : curScenario.getAllScenarios()) {
        fixNulls(rs.getFarmingYear());
      }
    }
  }


  /**
   * @param combinedFarm CombinedFarm
   */
  private void fixNullBpuLeadInds(CombinedFarm combinedFarm) {
    List<Scenario> scenarios = combinedFarm.getScenarios();
    Map<Integer, MarginTotal> yearMargins = combinedFarm.getYearMargins();

    for(Scenario curScenario : scenarios) {
      calculateBpuLeadInd(curScenario);
    }
    
    List<Integer> years = combinedFarm.getAllYears();
    for(Integer refYear : years) {
      MarginTotal combinedMargin = yearMargins.get(refYear);
      if(combinedMargin != null && combinedMargin.getBpuLeadInd() == null) {
        
        for(Scenario curScenario : scenarios) {
          ReferenceScenario rs = curScenario.getReferenceScenarioByYear(refYear);
          
          if(rs != null) {
            MarginTotal refMargin = rs.getFarmingYear().getMarginTotal();
            if(refMargin.getBpuLeadInd() != null) {
              combinedMargin.setBpuLeadInd(refMargin.getBpuLeadInd());
              break;
            }
          }
        }
      }
    }
  }

}
