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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.StructuralChangeCodes;
import ca.bc.gov.srm.farm.util.ScenarioUtils;


/**
 * This class depends on the AccrualCalculator and FarmSizeRatioCalculator
 * being run first.
 * 
 * Structural change is used to make reference years look like the current
 * program year so that they can be compared more easily.
 */
public abstract class StructuralChangeCalculator {
  
  protected Scenario scenario;
  
  protected StructuralChangeCalculator(Scenario scenario) {
    this.scenario = scenario;
  }

  /**
   * Structural Change only applies to reference years. 
   * It should only be calculated if the benefit has a structural 
   * change method.
   */
  public void calculateMarginStructuralChange() {
    calculateStructuralChange(CalculatorConfig.CALC_TYPE_MARGIN);
  }
  
  /**
   * Structural Change only applies to reference years. 
   * It should only be calculated if the benefit has a structural 
   * change method.
   */
  public void calculateExpenseStructuralChange() {
    calculateStructuralChange(CalculatorConfig.CALC_TYPE_EXPENSE);
  }
  
  /**
   * Structural Change only applies to reference years. 
   * It should only be calculated if the benefit has a structural 
   * change method.
   * @param calculationType Either MARGIN or EXPENSE.
   */
  public void calculateStructuralChange(String calculationType) {
    boolean scCalculated = false; // if set to false, previous value will be wiped out
    Benefit benefit = scenario.getBenefit();

    if(benefit != null) {
      String scmCode;
      if(calculationType.equals(CalculatorConfig.CALC_TYPE_MARGIN)) {
        scmCode = benefit.getStructuralChangeMethodCode();
      } else { // CalculatorConstants.CALC_TYPE_EXPENSE
        scmCode = benefit.getExpenseStructuralChangeMethodCode();
      }
      
      if(scmCode != null) {
        scCalculated = StructuralChangeCodes.RATIO.equals(scmCode) || StructuralChangeCodes.ADDITIVE.equals(scmCode);
        
        // Calculate ratio and additive every time for margin so we can display them and use them for reasonability tests.
        if(StructuralChangeCodes.RATIO.equals(scmCode) || calculationType.equals(CalculatorConfig.CALC_TYPE_MARGIN)) {
          calculateRatioStructuralChange(calculationType, scmCode);
        }
        if(StructuralChangeCodes.ADDITIVE.equals(scmCode) || calculationType.equals(CalculatorConfig.CALC_TYPE_MARGIN)) {
          calculateAdditiveStructuralChange(calculationType, scmCode);
        }
      }
    }
    
    if(!scCalculated) {
      //
      // Wipe out any previous results
      //
      // Reference scenarios only
      Map<Integer, MarginTotal> yearMargins = scenario.getReferenceYearMargins();
      for(Integer refYear : yearMargins.keySet()) {
        
        MarginTotal mt = yearMargins.get(refYear);
        
        if(calculationType.equals(CalculatorConfig.CALC_TYPE_MARGIN)) {
          mt.setStructuralChangeAdjs(new Double(0));
          mt.setIsStructuralChangeNotable(Boolean.FALSE);
        } else { // CalculatorConstants.CALC_TYPE_EXPENSE
          mt.setExpenseStructuralChangeAdjs(new Double(0));
        }
        
      }
    }
  }

  
  /**
   * Set structural change notable (material) to null for all years.
   */
  public void resetIsStructuralChangeNotable() {
    Map<Integer, MarginTotal> yearMargins = scenario.getYearMargins();
    
    for(Integer year : yearMargins.keySet()) {
      MarginTotal  mt = yearMargins.get(year);
      mt.setIsStructuralChangeNotable(null);
    }
  }
  
  
  /**
   * For reference years, for the structural change to be significant, it
   * must be more than $5,000 and greater than 10% of the average "unadjusted production
   * margin".
   * 
   * For version 2.1.2, users decided that only the "olympic average" structural change
   * should be considered and that it should be a single value for the overall calculation. 
   * It was too late to update the model to move this field up a level, so the "SC notable" 
   * indicator is being set for all reference years.
   */
  public void calculateIsStructuralChangeNotable() {
    Map<Integer, MarginTotal> yearMargins = scenario.getYearMargins();

    double oaStructuralChange = getOlympicAverageStructuralChangeAdjs(CalculatorConfig.STRUCTURAL_CHANGE_METHOD_SELECTED);
    double oaRatioStructuralChange = getOlympicAverageStructuralChangeAdjs(CalculatorConfig.STRUCTURAL_CHANGE_METHOD_RATIO);
    double oaAdditiveStructuralChange = getOlympicAverageStructuralChangeAdjs(CalculatorConfig.STRUCTURAL_CHANGE_METHOD_ADDITIVE);
    Boolean isNotable = Boolean.FALSE;
    Boolean isRatioNotable = Boolean.FALSE;
    Boolean isAdditiveNotable = Boolean.FALSE;
    
    // make sure SC difference is positive
    oaStructuralChange = Math.abs(oaStructuralChange);
    oaRatioStructuralChange = Math.abs(oaRatioStructuralChange);
    oaAdditiveStructuralChange = Math.abs(oaAdditiveStructuralChange);
    
    double oaMargin = getOlympicAverageProdMargin(CalculatorConfig.STRUCTURAL_CHANGE_METHOD_SELECTED);
    double oaRatioMargin = getOlympicAverageProdMargin(CalculatorConfig.STRUCTURAL_CHANGE_METHOD_RATIO);
    double oaAdditiveMargin = getOlympicAverageProdMargin(CalculatorConfig.STRUCTURAL_CHANGE_METHOD_ADDITIVE);
    
    //
    // If the average SC was by at least X amount, and if the percentage
    // change was more than Y percent, then the SC is notable (now labeled in the UI as "Material").
    // TODO Rename notable to material
    //
    if(oaStructuralChange > CalculatorConfig.STRUCTURAL_CHANGE_LEAST_AMOUNT) {
      double scRatio = 0;
      
      if(oaMargin != 0) {
        scRatio = Math.abs(oaStructuralChange/oaMargin);
      }
      
      if(scRatio > CalculatorConfig.STRUCTURAL_CHANGE_LEAST_PERCENT) {
        isNotable = Boolean.TRUE;
      }
    }

    // RATIO
    if(oaRatioStructuralChange > CalculatorConfig.STRUCTURAL_CHANGE_LEAST_AMOUNT) {
      double scRatio = 0;
      
      if(oaRatioMargin != 0) {
        scRatio = Math.abs(oaRatioStructuralChange/oaRatioMargin);
      }
      
      if(scRatio > CalculatorConfig.STRUCTURAL_CHANGE_LEAST_PERCENT) {
        isRatioNotable = Boolean.TRUE;
      }
    }
    
    // ADDITIVE
    if(oaAdditiveStructuralChange > CalculatorConfig.STRUCTURAL_CHANGE_LEAST_AMOUNT) {
      double scRatio = 0;
      
      if(oaAdditiveMargin != 0) {
        scRatio = Math.abs(oaAdditiveStructuralChange/oaAdditiveMargin);
      }
      
      if(scRatio > CalculatorConfig.STRUCTURAL_CHANGE_LEAST_PERCENT) {
        isAdditiveNotable = Boolean.TRUE;
      }
    }
    
    //
    // update all years with the same value
    //
    for(Integer curYear : yearMargins.keySet()) {
      
      MarginTotal mt = yearMargins.get(curYear);
      
      //
      // The user can override this, so only update it if it is null
      //
      if(mt.getIsStructuralChangeNotable() == null) {
        mt.setIsStructuralChangeNotable(isNotable);
      }
      mt.setIsRatioStructuralChangeNotable(isRatioNotable);
      mt.setIsAdditiveStructuralChangeNotable(isAdditiveNotable);
    }
  }
  

  /**
   * After a couple of years it was decided that there should only
   * be one "SC Notable" flag. However as of writing the flag is still
   * strored separately for each year.
   */
  public boolean getIsStructuralChangeNotable() {
    boolean scNotable = false;
    
    if(scenario.getFarmingYear().getMarginTotal() != null
        && scenario.getFarmingYear().getMarginTotal().getIsStructuralChangeNotable() != null) {
      scNotable = scenario.getFarmingYear().getMarginTotal().getIsStructuralChangeNotable().booleanValue();
    } 
    
    return scNotable;
  }


  /**
   * @return Map<String key, List<ProductiveUnitCapacity>>
   */
  public abstract Map<String, List<ProductiveUnitCapacity>> getProgramYearPucMap();

  
  /**
   * @return map<year, map<iic-sgc, List<ProductiveUnitCapacity>>>
   */
  public abstract Map<Integer, Map<String, List<ProductiveUnitCapacity>>> getRefYearPucMap();
  
  
  /**
   * For the additive method, you want to sum up the value of the 
   * productive capacity variance compared to the program year.
   * 
   * Note that items might be grown in the program year, but not
   * the reference year, and visa versa, so we really want to consider
   * the super set of items grown in either year.
   * @param calculationType Either MARGIN or EXPENSE.
   * @param scmCode structural change method code
   */
  private void calculateAdditiveStructuralChange(String calculationType, String scmCode) {
    Map<String, List<ProductiveUnitCapacity>> programYearPucMap = getProgramYearPucMap();
    Map<Integer, Map<String, List<ProductiveUnitCapacity>>> refYearsPucMap = getRefYearPucMap();

    // Reference scenarios only
    Map<Integer, MarginTotal> yearMargins = scenario.getReferenceYearMargins();
    
    for(Integer refYear : yearMargins.keySet()) {
      
      MarginTotal yearMargin = yearMargins.get(refYear);
      calculateAdditiveStructuralChange(yearMargin, refYear, programYearPucMap, refYearsPucMap, calculationType, scmCode);
    }
  }


  /**
   * @param yearMargin yearMargin
   * @param refYear refYear
   * @param programYearPucMap programYearPucMap
   * @param refYearsPucMap refYearsPucMap
   * @param calculationType Either MARGIN or EXPENSE.
   * @param scmCode structural change method code
   */
  private void calculateAdditiveStructuralChange(
      MarginTotal yearMargin,
      Integer refYear,
      Map<String, List<ProductiveUnitCapacity>> programYearPucMap,
      Map<Integer, Map<String, List<ProductiveUnitCapacity>>> refYearsPucMap,
      String calculationType,
      String scmCode) {
    ProductiveValueCalculator pvCalc = CalculatorFactory.getProductiveValueCalculator(scenario);

    Boolean bpuLeadInd = yearMargin.getBpuLeadInd();
    double total = 0;
    Map<String, List<ProductiveUnitCapacity>> refYearPucMap = refYearsPucMap.get(refYear);
    
    //
    // Loop through the reference year commodities.
    // Sort the keys for ease of debugging.
    //
    List<String> refYearPucKeys = new ArrayList<>();
    refYearPucKeys.addAll(refYearPucMap.keySet());
    Collections.sort(refYearPucKeys);
    for(String key : refYearPucKeys) {
      List<ProductiveUnitCapacity> refYrPucList = refYearPucMap.get(key);
      
      // get the difference in capacity
      List<ProductiveUnitCapacity> pyPucList = programYearPucMap.get(key);
      double capacityVariance = getCapacityVariance(refYrPucList, pyPucList);

      // get the reference year price
      Double basePrice = pvCalc.getBasePriceForYear(refYrPucList, refYear, bpuLeadInd, calculationType);
      
      if(basePrice != null) {
        // add the difference in value to the total
        double differenceValue = capacityVariance * basePrice.doubleValue();
        total += differenceValue;
      }
    }
    
    //
    // Consider any items grown in the program year that were not
    // grown in the reference year.
    // Sort the keys for ease of debugging.
    //
    List<String> programYearPucKeys = new ArrayList<>();
    programYearPucKeys.addAll(programYearPucMap.keySet());
    Collections.sort(programYearPucKeys);
    for(String key : programYearPucKeys) {
      
      if(!refYearPucMap.keySet().contains(key)) {
        List<ProductiveUnitCapacity> pyPucList = programYearPucMap.get(key);
        double capacityVariance = getCapacityVariance(null, pyPucList);
        Double basePrice = pvCalc.getBasePriceForYear(pyPucList, refYear, bpuLeadInd, calculationType);
        
        if(basePrice != null) {
          double differenceValue = capacityVariance * basePrice.doubleValue();
          total += differenceValue;
        }
      }
    }
    
    Double structuralChangeAdjs = Double.valueOf(total);
    if(CalculatorConfig.CALC_TYPE_MARGIN.equals(calculationType)) {
      yearMargin.setAdditiveStructuralChangeAdjs(structuralChangeAdjs);
      if(StructuralChangeCodes.ADDITIVE.equals(scmCode)) {
        yearMargin.setStructuralChangeAdjs(structuralChangeAdjs);
      }
    } else { // CalculatorConstants.CALC_TYPE_EXPENSE
      yearMargin.setExpenseStructuralChangeAdjs(structuralChangeAdjs);
    }
  }

  
  /**
   * Use the farm size ratio to determine the change in the production
   * margin. Note that this method takes into account all the accrual
   * adjustments, whereas the additive method only considers crop
   * and livestock adjustments.
   * @param calculationType Either MARGIN or EXPENSE.
   * @param scmCode structural change method code
   */
  private void calculateRatioStructuralChange(String calculationType, String scmCode) {
    Map<Integer, MarginTotal> yearMargins = scenario.getReferenceYearMargins();
    
    // Reference scenarios only
    for(Integer refYear : yearMargins.keySet()) {
      MarginTotal yearMargin = yearMargins.get(refYear);
      
      if(calculationType.equals(CalculatorConfig.CALC_TYPE_MARGIN)) {
        calculateRatioStructuralChange(yearMargin, scmCode);
      } else { // CalculatorConstants.CALC_TYPE_EXPENSE
        calculateExpenseRatioStructuralChange(yearMargin);
      }
    }
  }


  /**
   * @param yearMargin yearMargin
   * @param scmCode structural change method code
   */
  private void calculateRatioStructuralChange(MarginTotal yearMargin, String scmCode) {
    double prodMargin = yearMargin.getProductionMargAccrAdjs().doubleValue();
    double fsRatio = yearMargin.getFarmSizeRatio().doubleValue();
    double prodMarginWithSC = (prodMargin * fsRatio);
    double scAdjs = prodMarginWithSC - prodMargin;
    
    yearMargin.setRatioStructuralChangeAdjs(Double.valueOf(scAdjs));
    if(StructuralChangeCodes.RATIO.equals(scmCode)) {
      yearMargin.setStructuralChangeAdjs(Double.valueOf(scAdjs));
    }
  }
  
  
  /**
   * @param yearMargin yearMargin
   */
  protected void calculateExpenseRatioStructuralChange(MarginTotal yearMargin) {
    double expensesWithAccrualAdjs = yearMargin.getExpenseAccrualAdjs().doubleValue();
    double fsRatio = yearMargin.getExpenseFarmSizeRatio().doubleValue();
    double expensesWithSC = (expensesWithAccrualAdjs * fsRatio);
    double scAdjs = expensesWithSC - expensesWithAccrualAdjs;
    
    yearMargin.setExpenseStructuralChangeAdjs(new Double(scAdjs));
  }
	
	
	/**
	 * variance is the difference in capacity between the program year 
	 * and the reference year
	 * 
	 * @param refYearPucList refYearPucList
	 * @param progYearPucList progYearPucList
	 * 
	 * @return variance in whatever units the PUC is measured in 
	 * (usually acres or head)
	 */
	public double getCapacityVariance(
			List<ProductiveUnitCapacity> refYearPucList, 
			List<ProductiveUnitCapacity> progYearPucList) {
		double totalVariance = 0;
		
		if((progYearPucList == null) || (progYearPucList.size() == 0)) {
			//
			// If the crop isn't grown in the program year, then the variance will
			// be negative so that we can pretend the crop wasn't grown in the 
			// reference year either.
			//
			double refYrCap = sumCapacity(refYearPucList);
			totalVariance = 0 - refYrCap;
		} else if ((refYearPucList == null) || (refYearPucList.size() == 0)) {
			//
			// Crop wasn't grown in the reference year, so the variance is
			// the program year capacity
			//
			totalVariance = sumCapacity(progYearPucList);
		} else {
			//
			// The variance is the program year capacity - ref year capacity. Note that it
			// is important to take the partnership percent from the different PUCs because they
			// could belong to different operations.
			//
			double totalProgYearCap = sumCapacity(progYearPucList);
			double totalRefYearCap = sumCapacity(refYearPucList);
			
			totalVariance = totalProgYearCap - totalRefYearCap;
		}
		
		return totalVariance;
	}
	
	
	/**
	 * 
	 * @param pucs pucs
	 * @return total productive amount from all operations for a commodity
	 */
	public double sumCapacity(List<ProductiveUnitCapacity> pucs) {
		double total = 0;
		
		for(int ii = 0; ii < pucs.size(); ii++) {
			ProductiveUnitCapacity puc = pucs.get(ii);
			
			if(puc.getTotalProductiveCapacityAmount() != null) {
				double cap = puc.getTotalProductiveCapacityAmount().doubleValue();
				double pp = ScenarioUtils.getPartnershipPercent(puc.getFarmingOperation());
				
				total += (cap * pp);
			}
		}
		
		return total;
	}
	
	
	/**
	 * For each commodity return a list of PUCs. Use a list because the commodity
	 * might be grown by more than one operation for the farm. Each operation
	 * has its own partnership percentage...
	 * 
	 * @param fy fy
	 * 
	 * @return map<iic-sgc, List>
	 */
	protected Map<String, List<ProductiveUnitCapacity>> getPucMap(FarmingYear fy) {
		Map<String, List<ProductiveUnitCapacity>> pucMap = new HashMap<>();
		List<ProductiveUnitCapacity> progYearPucs = getPucList(fy);
		
		for(int ii = 0; ii < progYearPucs.size(); ii++) {
			ProductiveUnitCapacity puc = progYearPucs.get(ii);
			String key = getKey(puc);
			
			if(pucMap.containsKey(key)) {
				List<ProductiveUnitCapacity> pucList = pucMap.get(key);
				pucList.add(puc);
			} else {
				List<ProductiveUnitCapacity> pucList = new ArrayList<>();
				pucList.add(puc);
				pucMap.put(key, pucList);
			}
		}
		
		return pucMap;
	}
	
	
	/**
	 * Used in conjuction with getProgramYearPucMap
	 * 
	 * @param puc puc
	 * @return key
	 */
	public String getKey(ProductiveUnitCapacity puc) {
		String key = puc.getInventoryItemCode() + "-" + puc.getStructureGroupCode();
		return key;
	}
	
	
	/**
	 * @param fy fy
	 * @return List<ProductiveUnitCapacity>
	 */
	private List<ProductiveUnitCapacity> getPucList(FarmingYear fy) {
		List<ProductiveUnitCapacity> pucs = new ArrayList<>();
		
		for(FarmingOperation fo : fy.getFarmingOperations()) {
			
			if(fo.getProductiveUnitCapacitiesForStructureChange() != null) {
				pucs.addAll(fo.getProductiveUnitCapacitiesForStructureChange());
			}
		}
		
		return pucs;
	}

  /**
   * @return olympic average of structural change adjustments
   */
  private double getOlympicAverageStructuralChangeAdjs(String structuralChangeMethod) {
    ReferenceYearCalculator refYearCalc = CalculatorFactory.getReferenceYearCalculator(scenario);
    
    Map<Integer, MarginTotal> refYearMargins = refYearCalc.getReferenceYearMargins(scenario, true, true, structuralChangeMethod);
    
    double total = 0;
    int numRefYears = refYearMargins.size();
    
    for(Integer refYear : refYearMargins.keySet()) {
      MarginTotal refYearMargin = refYearMargins.get(refYear);
      
      // Sometimes a structural change is not calculated.
      if(refYearMargin.getStructuralChangeAdjs() != null) {
        if(CalculatorConfig.STRUCTURAL_CHANGE_METHOD_SELECTED.equals(structuralChangeMethod)) {
          total += refYearMargin.getStructuralChangeAdjs().doubleValue();
        } else if(CalculatorConfig.STRUCTURAL_CHANGE_METHOD_RATIO.equals(structuralChangeMethod)) {
          total += refYearMargin.getRatioStructuralChangeAdjs().doubleValue();
        } else if(CalculatorConfig.STRUCTURAL_CHANGE_METHOD_ADDITIVE.equals(structuralChangeMethod)) {
          total += refYearMargin.getAdditiveStructuralChangeAdjs().doubleValue();
        }
      }
    }
    
    double average = total/numRefYears;
    
    return average;
  }

  /**
   * 
   * @return olympic average of production margins
   */
  private double getOlympicAverageProdMargin(String structuralChangeMethod) {
    ReferenceYearCalculator refYearCalc = CalculatorFactory.getReferenceYearCalculator(scenario);
    Map<Integer, MarginTotal> refYearMargins = refYearCalc.getReferenceYearMargins(scenario, true, true, structuralChangeMethod);
    double total = 0;
    int numRefYears = refYearMargins.size();
    
    for(Integer refYear : refYearMargins.keySet()) {
      MarginTotal refYearMargin = refYearMargins.get(refYear);
      
      // FARM-776 important to use this margin
      total += refYearMargin.getProductionMargAccrAdjs().doubleValue();
    }
    
    double average = total/numRefYears;
    
    return average;
  }
  

  /**
   * @return boolean[]
   */
  public abstract boolean[] getBpuLeadIndArray();


  /**
   * @param bpuLeads boolean[]
   */
  public abstract void updateBpuLeadInds(boolean[] bpuLeads);
  
  
  /**
   * @param structuralChangeCode String
   * @param expenseStructuralChangeCode String
   */
  public void updateStructuralChangeCode(String structuralChangeCode, String expenseStructuralChangeCode) {
    
    Benefit benefit = scenario.getBenefit();
    benefit.setStructuralChangeMethodCode(structuralChangeCode);
    benefit.setExpenseStructuralChangeMethodCode(expenseStructuralChangeCode);
  }
}
