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

import static ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes.*;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.BasePricePerUnitYear;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.Scenario;

/**
 * 
 */
public class ProductiveValueCalculator {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
  protected Scenario scenario;

  public ProductiveValueCalculator(Scenario scenario) {
    this.scenario = scenario;
  }


  /**
	 * @param fo fo
	 * @param year year
	 * @param bpuLeadInd bpuLeadInd
	 * @param valueType the value type to get. Either MARGIN or EXPENSE.
	 * @return the income the farm is capable of generating
	 */
	public double calculateProductiveValue(
			FarmingOperation fo, 
			Integer year,
			Boolean bpuLeadInd,
			String valueType) {
		double total = 0;
		
		//
		// Crops always have PUCs, but some types of livestock don't have a PUC.
		//
		if(fo.getProductiveUnitCapacitiesForStructureChange() == null) {
			logger.warn("null ProductiveUnitCapacities, unable to calculate productive value");
		} else {
			//
			// By the time this code gets called, it should have already been
			// validated that every PUC has a BPU.
			//
			for(ProductiveUnitCapacity puc : fo.getProductiveUnitCapacitiesForStructureChange()) {
				
				double productiveValue = calculateProductiveValue(puc, year, bpuLeadInd, valueType);
				total += productiveValue;
			}
		}
		
		return total;
	}


	/**
	 * @param puc puc
	 * @param year year
	 * @param bpuLeadInd bpuLeadInd
	 * @param valueType the value type to get. Either MARGIN or EXPENSE.
	 * @return value
	 */
	public double calculateProductiveValue(
			ProductiveUnitCapacity puc, 
			Integer year,
			Boolean bpuLeadInd,
			String valueType) {
		double capacity = puc.getTotalProductiveCapacityAmount().doubleValue();
		Double basePrice;
		
		if(scenario.categoryIsOneOf(COVERAGE_NOTICE)) {
		  basePrice = calculateBpuAverage(puc, CalculatorConfig.CALC_TYPE_MARGIN, null);
		} else {
		  basePrice = getBasePriceForYear(puc, year, bpuLeadInd, valueType);
		}
		
		double productiveValue = 0;
		
		if(basePrice != null) {
			productiveValue = capacity * basePrice.doubleValue();
		}
		
		return productiveValue;
	}


  /**
	 * Convience method
	 * 
	 * @param pucs pucs
	 * @param yearToLookFor yearToLookFor
	 * @param bpuLeadInd bpuLeadInd
	 * @param valueType the value type to get. Either MARGIN or EXPENSE.
	 * @return bpu
	 */
	public Double getBasePriceForYear(
			List<ProductiveUnitCapacity> pucs,
			Integer yearToLookFor,
			Boolean bpuLeadInd,
			String valueType) {
		Double bpu = null;
		
		if(pucs != null && (pucs.size() > 0)) {
			ProductiveUnitCapacity firstPuc = pucs.get(0);
			bpu = getBasePriceForYear(firstPuc, yearToLookFor, bpuLeadInd, valueType);
		}
		
		return bpu;
	}
	
	
	/**
	 * The BPUs only contain data for the reference years, for example,
	 * Look up BPUs for 2009, inv code 4854, and you'll get data
	 * for six years 2008 to 2003. 
	 * 
	 * Note that the bpuLeadInd needs to be passed as a parameter because
	 * sometimes we are using the PUC from the program year, and asking
	 * for the price from a reference year, in which case we want the
	 * reference year's BPU lead/lag indicator.
	 * @param puc puc
	 * @param yearToLookFor yearToLookFor
	 * @param bpuLeadInd bpuLeadInd
	 * @param valueType the value type to get. Either MARGIN or EXPENSE.
	 * @return base price for that year
	 */
	public Double getBasePriceForYear(
			ProductiveUnitCapacity puc,
			Integer yearToLookFor,
			Boolean bpuLeadInd,
			String valueType) {
		
		Double basePrice = null;
		
		if(puc.getBasePricePerUnit() != null) {
			List<BasePricePerUnitYear> bpus = puc.getBasePricePerUnit().getBasePricePerUnitYears();
			int lookForInt = yearToLookFor.intValue();
			boolean lead = true;
			
			if(bpuLeadInd != null) {
				lead = bpuLeadInd.booleanValue();
			}

			if(!lead) {
				lookForInt--;
			}
			
			for(BasePricePerUnitYear bpuYear : bpus) {
				Integer year = bpuYear.getYear();
	
				if(lookForInt == year.intValue()) {
				  if(CalculatorConfig.CALC_TYPE_MARGIN.equals(valueType)) {
				    basePrice = bpuYear.getMargin();
				  } else { // CalculatorConstants.CALC_TYPE_EXPENSE
				    basePrice = bpuYear.getExpense();
				  }
					break;
				}
			}
		}
		
		return basePrice;
	}


  public double calculateBpuAverage(ProductiveUnitCapacity puc, String calculationType, List<Integer> years) {
    List<Integer> yearsToUse = years;
    
    double total = 0;
    
    if(puc.getBasePricePerUnit() != null) {
      // BPUs are required to have a set of six years but we only
      // want to calculate an average on the last five.
      List<BasePricePerUnitYear> bpuYears = puc.getBasePricePerUnit().getBasePricePerUnitYears();
      
      if(years == null || years.isEmpty()) {
        yearsToUse = bpuYears.stream().map(y -> y.getYear()).collect(Collectors.toList());            
      }
      
      for(int yearToUse : yearsToUse) {
        for(BasePricePerUnitYear bpuy : bpuYears) {
          int curYear = bpuy.getYear();
          if(curYear == yearToUse) {
            if(CalculatorConfig.CALC_TYPE_MARGIN.equals(calculationType)) {
              total += bpuy.getMargin();
            } else if(CalculatorConfig.CALC_TYPE_EXPENSE.equals(calculationType) && bpuy.getExpense() != null) {
              total += bpuy.getExpense();
            }
          }
        }
      }
      
    }
    
    int numYearsUsed = yearsToUse.size();
    double bpuAverage = total / numYearsUsed;
    if(bpuAverage < 0) {
      bpuAverage = 0;
    }

    return bpuAverage;
  }
}
