/**
 * Copyright (c) 2006, 
 * Government of British Columbia, 
 * Canada
 *
 * All rights reserved. 
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.codes.bpu;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.domain.codes.BPU;
import ca.bc.gov.srm.farm.list.ListView;


/**
 * 
 */
public class BPUsForm extends ValidatorForm {

  private static final long serialVersionUID = 5403743286888777305L;

  private List<ListView> programYearSelectOptions;
  private Integer yearFilter;

  private String inventoryCodeFilter;
  private String inventoryDescFilter;
  private String municipalityFilter;
  private String marginExpenseFilter;

  private List<BPU> bpus;
  private int numBPUs;

  private String inventorySearchInput;
  
  // single BPU attributes
  private Integer bpuId;
  private String invSgCode;
  private String invSgCodeDescription;
  private String municipalityCode;
  private String municipalityCodeDescription;
  private int[] years = new int[BPU.NUMBER_OF_YEARS];
  private String[] averageMargins = new String[BPU.NUMBER_OF_YEARS];
  private String[] averageExpenses = new String[BPU.NUMBER_OF_YEARS];
  private String[] revisionCounts = new String[BPU.NUMBER_OF_YEARS];
  
  private boolean isNew = false;

  /**
   * @param mapping mapping
   * @param request request
   */
  @Override
  public void reset(ActionMapping mapping, HttpServletRequest request) {
    super.reset(mapping, request);
    setNew(false);
  }

	/**
	 * @return the bpus
	 */
	public List<BPU> getBpus() {
		return bpus;
	}

	/**
	 * @param bpus the bpus to set
	 */
	public void setBpus(List<BPU> bpus) {
		this.bpus = bpus;
	}
	

	/**
	 * @return the inventoryCodeFilter
	 */
	public String getInventoryCodeFilter() {
		return inventoryCodeFilter;
	}

	/**
	 * @param inventoryCodeFilter the inventoryCodeFilter to set
	 */
	public void setInventoryCodeFilter(String inventoryCodeFilter) {
		this.inventoryCodeFilter = inventoryCodeFilter;
	}

	/**
	 * @return the inventoryDescFilter
	 */
	public String getInventoryDescFilter() {
		return inventoryDescFilter;
	}

	/**
	 * @param inventoryDescFilter the inventoryDescFilter to set
	 */
	public void setInventoryDescFilter(String inventoryDescFilter) {
		this.inventoryDescFilter = inventoryDescFilter;
	}

	/**
	 * @return the inventorySearchInput
	 */
	public String getInventorySearchInput() {
		return inventorySearchInput;
	}

	/**
	 * @param inventorySearchInput the inventorySearchInput to set
	 */
	public void setInventorySearchInput(String inventorySearchInput) {
		this.inventorySearchInput = inventorySearchInput;
	}

	/**
	 * @return the invSgCode
	 */
	public String getInvSgCode() {
		return invSgCode;
	}

	/**
	 * @param invSgCode the invSgCode to set
	 */
	public void setInvSgCode(String invSgCode) {
		this.invSgCode = invSgCode;
	}

	/**
	 * @return the invSgCodeDescription
	 */
	public String getInvSgCodeDescription() {
		return invSgCodeDescription;
	}

	/**
	 * @param invSgCodeDescription the invSgCodeDescription to set
	 */
	public void setInvSgCodeDescription(String invSgCodeDescription) {
		this.invSgCodeDescription = invSgCodeDescription;
	}

	/**
	 * @return the isNew
	 */
	public boolean getNew() {
		return isNew;
	}

	/**
	 * @param value the isNew to set
	 */
	public void setNew(boolean value) {
		this.isNew = value;
	}

	/**
	 * @return the municipalityCode
	 */
	public String getMunicipalityCode() {
		return municipalityCode;
	}

	/**
	 * @param municipalityCode the municipalityCode to set
	 */
	public void setMunicipalityCode(String municipalityCode) {
		this.municipalityCode = municipalityCode;
	}

	/**
	 * @return the municipalityCodeDescription
	 */
	public String getMunicipalityCodeDescription() {
		return municipalityCodeDescription;
	}

	/**
	 * @param municipalityCodeDescription the municipalityCodeDescription to set
	 */
	public void setMunicipalityCodeDescription(String municipalityCodeDescription) {
		this.municipalityCodeDescription = municipalityCodeDescription;
	}

	/**
	 * @return the municipalityFilter
	 */
	public String getMunicipalityFilter() {
		return municipalityFilter;
	}

	/**
	 * @param municipalityFilter the municipalityFilter to set
	 */
	public void setMunicipalityFilter(String municipalityFilter) {
		this.municipalityFilter = municipalityFilter;
	}

	/**
	 * @return the marginExpenseFilter
	 */
	public String getMarginExpenseFilter() {
		return marginExpenseFilter;
	}

	/**
	 * @param marginExpenseFilter the marginExpenseFilter to set
	 */
	public void setMarginExpenseFilter(String marginExpenseFilter) {
		this.marginExpenseFilter = marginExpenseFilter;
	}
	
	/**
	 * @return the numBPUs
	 */
	public int getNumBPUs() {
		return numBPUs;
	}

	/**
	 * @param numBPUs the numBPUs to set
	 */
	public void setNumBPUs(int numBPUs) {
		this.numBPUs = numBPUs;
	}

	/**
	 * @return the programYearSelectOptions
	 */
	public List<ListView> getProgramYearSelectOptions() {
		return programYearSelectOptions;
	}

	/**
	 * @param programYearSelectOptions the programYearSelectOptions to set
	 */
	public void setProgramYearSelectOptions(List<ListView> programYearSelectOptions) {
		this.programYearSelectOptions = programYearSelectOptions;
	}

	/**
	 * @return the yearFilter
	 */
	public Integer getYearFilter() {
		return yearFilter;
	}

	/**
	 * @param yearFilter the yearFilter to set
	 */
	public void setYearFilter(Integer yearFilter) {
		this.yearFilter = yearFilter;
		
		int year = yearFilter.intValue() - BPU.NUMBER_OF_YEARS;
		for(int ii = 0; ii < years.length; ii++) {
			years[ii] = year;
			year++;
		}
	}

	/**
	 * @return the years
	 */
	public int[] getYears() {
		return years;
	}

	/**
	 * @param years the years to set
	 */
	public void setYears(int[] years) {
		this.years = years;
	}

	/**
	 * @return the bpuId
	 */
	public Integer getBpuId() {
		return bpuId;
	}

	/**
	 * @param bpuId the bpuId to set
	 */
	public void setBpuId(Integer bpuId) {
		this.bpuId = bpuId;
	}

	/**
	 * @return the averageMargins
	 */
	public String[] getAverageMargins() {
		return averageMargins;
	}

	/**
	 * @param averageMargins the averageMargins to set
	 */
	public void setAverageMargins(String[] averageMargins) {
		this.averageMargins = averageMargins;
	}
	
	/**
	 * @return the averageExpenses
	 */
	public String[] getAverageExpenses() {
		return averageExpenses;
	}

	/**
	 * @param averageExpenses the averageExpenses to set
	 */
	public void setAverageExpenses(String[] averageExpenses) {
		this.averageExpenses = averageExpenses;
	}


	/**
	 * @return the revisionCounts
	 */
	public String[] getRevisionCounts() {
		return revisionCounts;
	}

	/**
	 * @param revisionCounts the revisionCounts to set
	 */
	public void setRevisionCounts(String[] revisionCounts) {
		this.revisionCounts = revisionCounts;
	}
	
  /**
   * @return the growingForward2013
   */
  public boolean isGrowingForward2013() {
    return yearFilter.intValue() >= CalculatorConfig.GROWING_FORWARD_2013;
  }

}
