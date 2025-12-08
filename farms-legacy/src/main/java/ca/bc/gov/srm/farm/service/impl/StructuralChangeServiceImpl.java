/**
 *
 * Copyright (c) 2011,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.service.impl;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.calculator.CalculatorFactory;
import ca.bc.gov.srm.farm.calculator.ProductiveValueCalculator;
import ca.bc.gov.srm.farm.calculator.StructuralChangeCalculator;
import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.StructuralChangeCodes;
import ca.bc.gov.srm.farm.service.BaseService;
import ca.bc.gov.srm.farm.service.StructuralChangeService;
import ca.bc.gov.srm.farm.ui.domain.calculator.StructuralChangeRow;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

/**
 * StructuralChangeServiceImpl. The SC screen wants alot of data 
 * that is not stored in the database. 
 */
final class StructuralChangeServiceImpl extends BaseService implements StructuralChangeService {

	/**
	 *
	 * @param   programYearScenario  programYearScenario
	 *
	 * @return  List<StructuralChangeRow>
	 */
	@Override
  public List<StructuralChangeRow> getStructuralChanges(final Scenario programYearScenario) {
    StructuralChangeCalculator scCalc = CalculatorFactory.getStructuralChangeCalculator(programYearScenario);
		Collection<StructuralChangeRow> rows = getRows(programYearScenario, scCalc);
		List<StructuralChangeRow> sortedRows = new ArrayList<>(rows);

		Collections.sort(sortedRows, new RowComparator());
		
		return sortedRows;
	}
	
	
	/**
	 * @param programYearScenario programYearScenario
	 * @param scCalc scCalc
	 * @return list<StructuralChangeRow>
	 */
	private Collection<StructuralChangeRow> getRows(Scenario programYearScenario, StructuralChangeCalculator scCalc) {
		String marginScmCode = getMethodCode(programYearScenario, CalculatorConfig.CALC_TYPE_MARGIN);
		String expenseScmCode = getMethodCode(programYearScenario, CalculatorConfig.CALC_TYPE_EXPENSE);
		Map<String, List<ProductiveUnitCapacity>> programYearPucMap = scCalc.getProgramYearPucMap();
		Map<Integer, Map<String, List<ProductiveUnitCapacity>>> refYearsPucMap = scCalc.getRefYearPucMap();
		Map<Integer, Boolean> bpuLeadMap = createBpuLeadMap(programYearScenario);
		Integer programYear = programYearScenario.getYear();
		
		// get the super-set of all commodities
		Map<String, StructuralChangeRow> rowMap = createRowMap(programYearPucMap, refYearsPucMap, scCalc);
		
		// for each commodity
		for(String key : rowMap.keySet()) {
			StructuralChangeRow row = rowMap.get(key);
			List<ProductiveUnitCapacity> progYearPucList = programYearPucMap.get(key);
			
			// for each reference year
			for(Integer year : refYearsPucMap.keySet()) {	
				Map<String, List<ProductiveUnitCapacity>> refYearMap = refYearsPucMap.get(year);
				Boolean bpuLeadInd = bpuLeadMap.get(year);
				List<ProductiveUnitCapacity> refYearPucList = refYearMap.get(key);
				
				if(progYearPucList == null && refYearPucList == null) {
					//
					// The commodity wasn't grown in this year or the program year, however,
					// users still want to see a BPU. 
					//
					getRefYearBpu(refYearsPucMap, key, row, year, bpuLeadInd, programYearScenario);
				} else {
					getValuesForYear(year, programYear, row, marginScmCode, progYearPucList, refYearPucList, bpuLeadInd, scCalc, CalculatorConfig.CALC_TYPE_MARGIN, programYearScenario);

					if(programYear.intValue() >= CalculatorConfig.GROWING_FORWARD_2013) {
						getValuesForYear(year, programYear, row, expenseScmCode, progYearPucList, refYearPucList, bpuLeadInd, scCalc, CalculatorConfig.CALC_TYPE_EXPENSE, programYearScenario);
					}
				}
			}
		}
		
		return rowMap.values();
	}


  /**
	 * The base price for 2006 should be the same no matter if you got it from a 2006 PUC or a 2010 PUC,
	 * so just use the first non-null reference year PUC.
	 * 
	 * @param refYearsPucMap refYearsPucMap
	 * @param key key
	 * @param row row 
	 * @param year year
	 * @param bpuLeadInd bpuLeadInd
	 * @param programYearScenario programYearScenario
	 */
	private void getRefYearBpu(
			Map<Integer, Map<String, List<ProductiveUnitCapacity>>> refYearsPucMap, 
			String key, 
			StructuralChangeRow row, 
			Integer year, 
			Boolean bpuLeadInd,
			Scenario programYearScenario) {
		
		for(Integer refYear : refYearsPucMap.keySet()) {
			Map<String, List<ProductiveUnitCapacity>> refYearMap = refYearsPucMap.get(refYear);
			List<ProductiveUnitCapacity> pucList = refYearMap.get(key);
			
			if(pucList != null) {
				Double marginRefYearBasePrice = getBasePrice(pucList, null, year, bpuLeadInd, CalculatorConfig.CALC_TYPE_MARGIN, programYearScenario);
				Double expenseRefYearBasePrice = getBasePrice(pucList, null, year, bpuLeadInd, CalculatorConfig.CALC_TYPE_EXPENSE, programYearScenario);
				row.getBpuYearMap().put(year, marginRefYearBasePrice);
				row.getExpenseBpuYearMap().put(year, expenseRefYearBasePrice);
			}
		}
	}
	
	
	/**
	 * 
	 * @param year year
	 * @param programYear programYear
	 * @param row row
	 * @param scmCode scmCode
	 * @param progYearPucList progYearPucList
	 * @param refYearPucList refYearPucList
	 * @param bpuLeadInd bpuLeadInd
	 * @param scCalc scCalc
	 * @param programYearScenario programYearScenario
	 */
	private void getValuesForYear(
			Integer year, 
			Integer programYear,
			StructuralChangeRow row,
			String scmCode,
			List<ProductiveUnitCapacity> progYearPucList,
			List<ProductiveUnitCapacity> refYearPucList,
			Boolean bpuLeadInd,
			StructuralChangeCalculator scCalc,
			String structualChangeType,
			Scenario programYearScenario) {
		Double capacity = getCapacity(refYearPucList);
		row.getCapacityYearMap().put(year, capacity);
		
		Double refYearBasePrice = getBasePrice(refYearPucList, progYearPucList, year, bpuLeadInd, structualChangeType, programYearScenario);
		
    if(structualChangeType.equals(CalculatorConfig.CALC_TYPE_MARGIN)) {
      row.getBpuYearMap().put(year, refYearBasePrice);
    } else { // CalculatorConstants.CALC_TYPE_EXPENSE
      row.getExpenseBpuYearMap().put(year, refYearBasePrice);
    }
		
		if(StructuralChangeCodes.ADDITIVE.equals(scmCode)) {
			//
			// capacity variance (partnershipPercent already factored in)
			//
			double variance = scCalc.getCapacityVariance(refYearPucList, progYearPucList);
			row.getVarianceYearMap().put(year, new Double(variance));
			
			//
			// additive structural adjustment is just the reference year 
			// value of the capacity variance
			//
			if(refYearBasePrice != null) {
			  double adjValue = variance * refYearBasePrice.doubleValue();
			  if(structualChangeType.equals(CalculatorConfig.CALC_TYPE_MARGIN)) {
			    row.getAdjustmentsYearMap().put(year, new Double(adjValue));
	      } else { // CalculatorConstants.CALC_TYPE_EXPENSE
	        row.getExpenseAdjustmentsYearMap().put(year, new Double(adjValue));
	      }
			}
		} else if(StructuralChangeCodes.RATIO.equals(scmCode)) {
			//
			// if we couldn't get the base price then show empty cells 
			//
			if(refYearBasePrice != null) {
				double refYearPrice = refYearBasePrice.doubleValue();
				
				// reference year productive value
				if(capacity != null) {
					double rypv = capacity.doubleValue() * refYearPrice;
	        if(structualChangeType.equals(CalculatorConfig.CALC_TYPE_MARGIN)) {
	          row.getRefYearProdValueYearMap().put(year, new Double(rypv));
	        } else { // CalculatorConstants.CALC_TYPE_EXPENSE
	          row.getExpenseRefYearProdValueYearMap().put(year, new Double(rypv));
	        }
				}
				
				//
				// program year productive value = pretend that the farm grew
				// the amount that it grew in the program year in the reference
				// year (i.e: sell the PY amount for the reference year price)
			  // 
				if((progYearPucList != null) && (progYearPucList.size() > 0)) {
					double pyCapcity = scCalc.sumCapacity(progYearPucList);
					double pypv = pyCapcity * refYearPrice;

					if(structualChangeType.equals(CalculatorConfig.CALC_TYPE_MARGIN)) {
	          row.getProgYearProdValueYearMap().put(year, new Double(pypv));
	        } else { // CalculatorConstants.CALC_TYPE_EXPENSE
	          row.getExpenseProgYearProdValueYearMap().put(year, new Double(pypv));
	        }
				}
			}
		}
	}

	
	/**
	 * 
	 * @param programYearScenario programYearScenario
	 * @param structuralChangeType structuralChangeType
	 * @return sc method code
	 */
	private String getMethodCode(Scenario programYearScenario, String structuralChangeType) {
		String scmCode = StructuralChangeCodes.NONE;
		Benefit benefit = programYearScenario.getFarmingYear().getBenefit();
		
    // the user might be looking at a CRA scenario which doesn't have a benefit
		if(benefit != null) {
		  if(structuralChangeType.equals(CalculatorConfig.CALC_TYPE_MARGIN)) {
		    scmCode = benefit.getStructuralChangeMethodCode();
		  } else { // CalculatorConstants.CALC_TYPE_EXPENSE
		    scmCode = benefit.getExpenseStructuralChangeMethodCode();
		  }
		}
		
		return scmCode;
	}
	
	
	/**
	 * @param programYearScenario programYearScenario
	 * @return map<year, Boolean>
	 */
	private Map<Integer, Boolean> createBpuLeadMap(Scenario programYearScenario) {
		Map<Integer, Boolean> map = new HashMap<>();

		for(ReferenceScenario rs : programYearScenario.getReferenceScenarios()) {
			MarginTotal mt = rs.getFarmingYear().getMarginTotal();
			
			if(mt != null) {
				Boolean ind = mt.getBpuLeadInd();
				Integer refYear = rs.getYear();
				
				map.put(refYear, ind);
			}
		}
		
		return map;
	}

	
	/**
	 * A commodity might not be grown in the reference year, so use
	 * the BPUs attched to the program year PUC in that case. The base
	 * price for 2006 should be the same no matter if you got it from
	 * a 2006 PUC or a 2010 PUC.
	 * 
	 * @param refYearPucList refYearPucList
	 * @param progYearPucList progYearPucList
	 * @param year year
	 * @param bpuLeadInd bpuLeadInd
	 * @param valueType the value type to get. Either MARGIN or EXPENSE.
	 * @param programYearScenario programYearScenario
	 * @return base price
	 */
	private Double getBasePrice(
			List<ProductiveUnitCapacity> refYearPucList, 
			List<ProductiveUnitCapacity> progYearPucList,
			Integer year,
			Boolean bpuLeadInd,
			String valueType,
			Scenario programYearScenario) {
		Double basePrice = null;
		
		ProductiveValueCalculator pvCalc = CalculatorFactory.getProductiveValueCalculator(programYearScenario);
		
		if((refYearPucList != null) && (refYearPucList.size() > 0)) {
			basePrice = pvCalc.getBasePriceForYear(refYearPucList, year, bpuLeadInd, valueType);
		} else {
			basePrice = pvCalc.getBasePriceForYear(progYearPucList, year, bpuLeadInd, valueType);
		}
		
		return basePrice;
	}
	
	
	
	/**
	 * 
	 * @param pucList pucList
	 * @return capacity
	 */
	private Double getCapacity(List<ProductiveUnitCapacity> pucList) {
		Double totalCapacity = null;
		
		if(pucList != null) {
			for(int ii = 0; ii < pucList.size(); ii++) {
				ProductiveUnitCapacity puc = pucList.get(ii);
			  Double capacity = puc.getTotalProductiveCapacityAmount();
		
				if(capacity != null) {
          double partnershipPercent = ScenarioUtils.getPartnershipPercent(puc.getFarmingOperation());
					double cap = capacity.doubleValue() * partnershipPercent;
					
					if(totalCapacity == null) {
						totalCapacity = new Double(cap);
					} else {
						totalCapacity = new Double(cap + totalCapacity.doubleValue());
					}
				}
			}
		}
		
		return totalCapacity;
	}
	
	/**
	 * 
	 * @param programYearPucMap programYearPucMap
	 * @param refYearsPucMap refYearsPucMap
	 * @param scCalc scCalc
	 * @return map<iic-sgc, StructuralChangeRow>
	 */
	private Map<String, StructuralChangeRow> createRowMap(
			Map<String, List<ProductiveUnitCapacity>> programYearPucMap,
			Map<Integer, Map<String, List<ProductiveUnitCapacity>>> refYearsPucMap,
			StructuralChangeCalculator scCalc) {
		HashMap<String, StructuralChangeRow> map = new HashMap<>();
		
		List<List<ProductiveUnitCapacity>> allPucLists = new ArrayList<>();
		allPucLists.addAll(programYearPucMap.values());
		
		for(Integer year : refYearsPucMap.keySet()) {	
			Map<String, List<ProductiveUnitCapacity>> refYearMap = refYearsPucMap.get(year);
			allPucLists.addAll(refYearMap.values());
		}
		
		for(int ii = 0; ii < allPucLists.size(); ii++) {
			List<ProductiveUnitCapacity> pucList = allPucLists.get(ii);
			
			for(int jj = 0; jj < pucList.size(); jj++) {
				ProductiveUnitCapacity puc = pucList.get(jj);
				String key = scCalc.getKey(puc);
				
				if(!map.keySet().contains(key)) {
					StructuralChangeRow row = createRow(puc);
					
					map.put(key, row);
				}
			}
		}
		
		return map;
	}
	
	
	/**
	 * 
	 * @param puc puc
	 * @return StructuralChangeRow
	 */
	private StructuralChangeRow createRow(ProductiveUnitCapacity puc) {
		StructuralChangeRow row = new StructuralChangeRow();
		String code = puc.getCode();
		String desc = puc.getDescription();
		
		row.setCode(code);
		row.setDescription(desc);
		
		return row;
	}
	
	/**
	 * Sort the rows on the screen by code to be consistent 
	 * with other screens.
	 * 
	 * (iic 129 = Honey, gsc 129 = Leaf Cutter Bees)
	 */
	private class RowComparator implements Comparator<StructuralChangeRow> {
	  
	  RowComparator() {
	  }

    /**
     * @param   r1  row 1
     * @param   r2  row 2
     *
     * @return  int
     */
    @Override
    public int compare(StructuralChangeRow r1, StructuralChangeRow r2) {
    	StructuralChangeRow li1 = r1;
    	StructuralChangeRow li2 = r2;

      return li1.getCode().compareTo(li2.getCode());
    }
  }
}
