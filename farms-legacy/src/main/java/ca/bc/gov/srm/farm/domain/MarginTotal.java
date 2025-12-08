/**
 *
 * Copyright (c) 2010,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A rollup of all the Margins for a scenario.
 *
 * @author awilkinson
 * @created Nov 18, 2010
 */
public final class MarginTotal implements Serializable {
  
  private static final long serialVersionUID = -1452633024208704851L;

  /** back-reference to the object containing this */
  @JsonBackReference
  private FarmingYear farmingYear;

  /**
   * marginTotalId is a surrogate unique identifier for
   * MarginTotal.
   */
  private Integer marginTotalId;

  /**
   * accrualAdjsCropInventory IS the Calculated Accrual Adjustments for crop
   * inventory for a ProgramYear.
   */
  private Double accrualAdjsCropInventory;

  /**
   * accrualAdjsLvstckInventory is the Calculated Accrual Adjusments for
   * livestock for a ProgramYear.
   */
  private Double accrualAdjsLvstckInventory;

  /**
   * accrualAdjsPayables are Calculated Accrual Adjusments for payables for a
   * ProgramYear.
   */
  private Double accrualAdjsPayables;

  /**
   * accrualAdjsPurchasedInputs is the Calculated Accrual Adjusments for
   * purchases for a ProgramYear.
   */
  private Double accrualAdjsPurchasedInputs;

  /**
   * accrualAdjsReceivables is the Calculated Accrual Adjusments for receivables
   * for a ProgramYear.
   */
  private Double accrualAdjsReceivables;
  
  /**
   * ratioStructuralChangeAdjs is the Calculated Structural Change Adjusments for a
   * ProgramYear using the Ratio method.
   */
  private Double ratioStructuralChangeAdjs;
  
  /**
   * additiveStructuralChangeAdjs is the Calculated Structural Change Adjusments for a
   * ProgramYear using the Additive method.
   */
  private Double additiveStructuralChangeAdjs;

  /**
   * structuralChangeAdjs is the Calculated Structural Change Adjusments for a
   * ProgramYear.
   */
  private Double structuralChangeAdjs;

  /** totalAllowableExpenses are the Calculated expenses for a program year. */
  private Double totalAllowableExpenses;

  /** totalAllowableIncome is the Calculated income for a ProgramYear. */
  private Double totalAllowableIncome;

  /**
   * unadjustedProductionMargin is the Calculated unadjusted production for a
   * ProgramYear.
   */
  private Double unadjustedProductionMargin;

  /**
   * productionMargAccrAdjs identifies the production margin associated with
   * accrual adjustments.
   */
  private Double productionMargAccrAdjs;

  /**
   * ratioProductionMargAftStrChangs is the production margin after structure
   * changes, using the Ratio method.
   */
  private Double ratioProductionMargAftStrChangs;
  
  /**
   * additiveProductionMargAftStrChangs is the production margin after structure
   * changes, using the Additive method.
   */
  private Double additiveProductionMargAftStrChangs;
  
  /**
   * productionMargAftStrChangs is the production margin after structure
   * changes.
   */
  private Double productionMargAftStrChangs;



  /**
   * revisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   */
  private Integer revisionCount;
  
  private Double fiscalYearProRateAdj;
  
  private Double farmSizeRatio;
  private Double expenseFarmSizeRatio;
  private Boolean isStructuralChangeNotable;
  private Boolean isRatioStructuralChangeNotable;
  private Boolean isAdditiveStructuralChangeNotable;
  private Boolean bpuLeadInd;
  
  private Double supplyManagedCommodityIncome;
  private Double unadjustedAllowableIncome;
  private Double yardageIncome;
  private Double programPaymentIncome;
  private Double totalUnallowableIncome;
  
  private Double unadjustedAllowableExpenses;
  private Double yardageExpenses;
  private Double contractWorkExpenses;
  private Double manualExpenses;
  private Double totalUnallowableExpenses;
  
  private Double deferredProgramPayments;
  
  /** Expenses with accrual adjustments */
  private Double expenseAccrualAdjs;
  /** Structural Change adjustment to expenses */
  private Double expenseStructuralChangeAdjs;
  /** Expenses After Structural Change */
  private Double expensesAfterStructuralChange;
  
  private Double prodInsurDeemedSubtotal;
  private Double prodInsurDeemedTotal;
  
  
  /**
   * For now this is just used for debugging the benefit.
   * I doubt it will become a database field.
   */
  private Double productiveValue;
  private Double expenseProductiveValue;


  /**
   * marginTotalId is a surrogate unique identifier for
   * MarginTotal.
   *
   * @return  Integer
   */
  public Integer getMarginTotalId() {
    return marginTotalId;
  }

  /**
   * @param  newVal  The new value for this property
   */
  public void setMarginTotalId(final Integer newVal) {
    marginTotalId = newVal;
  }

  /**
   * AccrualAdjsCropInventory IS the Calculated Accrual Adjustments for crop
   * inventory for a ProgramYear.
   *
   * @return  Double
   */
  public Double getAccrualAdjsCropInventory() {
    return accrualAdjsCropInventory;
  }

  /**
   * AccrualAdjsCropInventory IS the Calculated Accrual Adjustments for crop
   * inventory for a ProgramYear.
   *
   * @param  newVal  The new value for this property
   */
  public void setAccrualAdjsCropInventory(final Double newVal) {
    accrualAdjsCropInventory = newVal;
  }

  /**
   * AccrualAdjsLvstckInventory is the Calculated Accrual Adjusments for
   * livestock for a ProgramYear.
   *
   * @return  Double
   */
  public Double getAccrualAdjsLvstckInventory() {
    return accrualAdjsLvstckInventory;
  }

  /**
   * AccrualAdjsLvstckInventory is the Calculated Accrual Adjusments for
   * livestock for a ProgramYear.
   *
   * @param  newVal  The new value for this property
   */
  public void setAccrualAdjsLvstckInventory(final Double newVal) {
    accrualAdjsLvstckInventory = newVal;
  }

  /**
   * AccrualAdjsPayables are Calculated Accrual Adjusments for payables for a
   * ProgramYear.
   *
   * @return  Double
   */
  public Double getAccrualAdjsPayables() {
    return accrualAdjsPayables;
  }

  /**
   * AccrualAdjsPayables are Calculated Accrual Adjusments for payables for a
   * ProgramYear.
   *
   * @param  newVal  The new value for this property
   */
  public void setAccrualAdjsPayables(final Double newVal) {
    accrualAdjsPayables = newVal;
  }

  /**
   * AccrualAdjsPurchasedInputs is the Calculated Accrual Adjusments for
   * purchases for a ProgramYear.
   *
   * @return  Double
   */
  public Double getAccrualAdjsPurchasedInputs() {
    return accrualAdjsPurchasedInputs;
  }

  /**
   * AccrualAdjsPurchasedInputs is the Calculated Accrual Adjusments for
   * purchases for a ProgramYear.
   *
   * @param  newVal  The new value for this property
   */
  public void setAccrualAdjsPurchasedInputs(final Double newVal) {
    accrualAdjsPurchasedInputs = newVal;
  }

  /**
   * AccrualAdjsReceivables is the Calculated Accrual Adjusments for receivables
   * for a ProgramYear.
   *
   * @return  Double
   */
  public Double getAccrualAdjsReceivables() {
    return accrualAdjsReceivables;
  }

  /**
   * AccrualAdjsReceivables is the Calculated Accrual Adjusments for receivables
   * for a ProgramYear.
   *
   * @param  newVal  The new value for this property
   */
  public void setAccrualAdjsReceivables(final Double newVal) {
    accrualAdjsReceivables = newVal;
  }

  public Double getRatioStructuralChangeAdjs() {
    return ratioStructuralChangeAdjs;
  }

  public void setRatioStructuralChangeAdjs(Double ratioStructuralChangeAdjs) {
    this.ratioStructuralChangeAdjs = ratioStructuralChangeAdjs;
  }

  public Double getAdditiveStructuralChangeAdjs() {
    return additiveStructuralChangeAdjs;
  }

  public void setAdditiveStructuralChangeAdjs(Double additiveStructuralChangeAdjs) {
    this.additiveStructuralChangeAdjs = additiveStructuralChangeAdjs;
  }

  /**
   * StructuralChangeAdjs is the Calculated Structural Change Adjusments for a
   * ProgramYear.
   *
   * @return  Double
   */
  public Double getStructuralChangeAdjs() {
    return structuralChangeAdjs;
  }

  /**
   * StructuralChangeAdjs is the Calculated Structural Change Adjusments for a
   * ProgramYear.
   *
   * @param  newVal  The new value for this property
   */
  public void setStructuralChangeAdjs(final Double newVal) {
    structuralChangeAdjs = newVal;
  }

  /**
   * TotalAllowableExpenses are the Calculated expenses for a program year.
   *
   * @return  Double
   */
  public Double getTotalAllowableExpenses() {
    return totalAllowableExpenses;
  }

  /**
   * TotalAllowableExpenses are the Calculated expenses for a program year.
   *
   * @param  newVal  The new value for this property
   */
  public void setTotalAllowableExpenses(final Double newVal) {
    totalAllowableExpenses = newVal;
  }

  /**
   * TotalAllowableIncome is the Calculated income for a ProgramYear.
   *
   * @return  Double
   */
  public Double getTotalAllowableIncome() {
    return totalAllowableIncome;
  }

  /**
   * TotalAllowableIncome is the Calculated income for a ProgramYear.
   *
   * @param  newVal  The new value for this property
   */
  public void setTotalAllowableIncome(final Double newVal) {
    totalAllowableIncome = newVal;
  }

  /**
   * UnadjustedProductionMargin is the Calculated unadjusted production for a
   * ProgramYear.
   *
   * @return  Double
   */
  public Double getUnadjustedProductionMargin() {
    return unadjustedProductionMargin;
  }

  /**
   * UnadjustedProductionMargin is the Calculated unadjusted production for a
   * ProgramYear.
   *
   * @param  newVal  The new value for this property
   */
  public void setUnadjustedProductionMargin(final Double newVal) {
    unadjustedProductionMargin = newVal;
  }

  /**
   * ProductionMargAccrAdjs identifies the production margin associated with
   * accrual adjustments.
   *
   * @return  Double
   */
  public Double getProductionMargAccrAdjs() {
    return productionMargAccrAdjs;
  }

  /**
   * ProductionMargAccrAdjs identifies the production margin associated with
   * accrual adjustments.
   *
   * @param  newVal  The new value for this property
   */
  public void setProductionMargAccrAdjs(final Double newVal) {
    productionMargAccrAdjs = newVal;
  }

  public Double getRatioProductionMargAftStrChangs() {
    return ratioProductionMargAftStrChangs;
  }

  public void setRatioProductionMargAftStrChangs(Double ratioProductionMargAftStrChangs) {
    this.ratioProductionMargAftStrChangs = ratioProductionMargAftStrChangs;
  }

  public Double getAdditiveProductionMargAftStrChangs() {
    return additiveProductionMargAftStrChangs;
  }

  public void setAdditiveProductionMargAftStrChangs(Double additiveProductionMargAftStrChangs) {
    this.additiveProductionMargAftStrChangs = additiveProductionMargAftStrChangs;
  }

  /**
   * ProductionMargAftStrChangs is the production margin after structure
   * changes.
   *
   * @return  Double
   */
  public Double getProductionMargAftStrChangs() {
    return productionMargAftStrChangs;
  }

  /**
   * ProductionMargAftStrChangs is the production margin after structure
   * changes.
   *
   * @param  newVal  The new value for this property
   */
  public void setProductionMargAftStrChangs(final Double newVal) {
    productionMargAftStrChangs = newVal;
  }

  /**
   * RevisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   *
   * @return  Integer
   */
  public Integer getRevisionCount() {
    return revisionCount;
  }

  /**
   * RevisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   *
   * @param  newVal  The new value for this property
   */
  public void setRevisionCount(final Integer newVal) {
    revisionCount = newVal;
  }

  /**
   * Gets fiscalYearProRateAdj
   *
   * @return the fiscalYearProRateAdj
   */
  public Double getFiscalYearProRateAdj() {
    return fiscalYearProRateAdj;
  }

  /**
   * Sets fiscalYearProRateAdj
   *
   * @param pFiscalYearProRateAdj the fiscalYearProRateAdj to set
   */
  public void setFiscalYearProRateAdj(Double pFiscalYearProRateAdj) {
    fiscalYearProRateAdj = pFiscalYearProRateAdj;
  }


  /**
   * @return the farmingYear
   */
  public FarmingYear getFarmingYear() {
    return farmingYear;
  }

  /**
   * @param farmingYear the farmingYear to set the value to
   */
  public void setFarmingYear(FarmingYear farmingYear) {
    this.farmingYear = farmingYear;
  }
  
  

  /**
	 * @return the farmSizeRatio
	 */
	public Double getFarmSizeRatio() {
		return farmSizeRatio;
	}

	/**
	 * @param farmSizeRatio the farmSizeRatio to set
	 */
	public void setFarmSizeRatio(Double farmSizeRatio) {
		this.farmSizeRatio = farmSizeRatio;
	}

	/**
   * @return the expenseFarmSizeRatio
   */
  public Double getExpenseFarmSizeRatio() {
    return expenseFarmSizeRatio;
  }

  /**
   * @param expenseFarmSizeRatio the expenseFarmSizeRatio to set
   */
  public void setExpenseFarmSizeRatio(Double expenseFarmSizeRatio) {
    this.expenseFarmSizeRatio = expenseFarmSizeRatio;
  }

	/**
	 * @return the isStructuralChangeNotable
	 */
	public Boolean getIsStructuralChangeNotable() {
		return isStructuralChangeNotable;
	}

	/**
	 * @param isStructuralChangeNotable the isStructuralChangeNotable to set
	 */
	public void setIsStructuralChangeNotable(Boolean isStructuralChangeNotable) {
		this.isStructuralChangeNotable = isStructuralChangeNotable;
	}
	
	public Boolean getIsRatioStructuralChangeNotable() {
		return isRatioStructuralChangeNotable;
	}

	public void setIsRatioStructuralChangeNotable(Boolean isRatioStructuralChangeNotable) {
		this.isRatioStructuralChangeNotable = isRatioStructuralChangeNotable;
	}

	public Boolean getIsAdditiveStructuralChangeNotable() {
		return isAdditiveStructuralChangeNotable;
	}

	public void setIsAdditiveStructuralChangeNotable(Boolean isAdditiveStructuralChangeNotable) {
		this.isAdditiveStructuralChangeNotable = isAdditiveStructuralChangeNotable;
	}

	/**
	 * @return the contractWorkExpenses
	 */
	public Double getContractWorkExpenses() {
		return contractWorkExpenses;
	}

	/**
	 * @param contractWorkExpenses the contractWorkExpenses to set
	 */
	public void setContractWorkExpenses(Double contractWorkExpenses) {
		this.contractWorkExpenses = contractWorkExpenses;
	}

	/**
   * @return the manualExpenses
   */
  public Double getManualExpenses() {
    return manualExpenses;
  }

  /**
   * @param manualExpenses the manualExpenses to set
   */
  public void setManualExpenses(Double manualExpenses) {
    this.manualExpenses = manualExpenses;
  }

	/**
	 * @return the programPaymentIncome
	 */
	public Double getProgramPaymentIncome() {
		return programPaymentIncome;
	}

	/**
	 * @param programPaymentIncome the programPaymentIncome to set
	 */
	public void setProgramPaymentIncome(Double programPaymentIncome) {
		this.programPaymentIncome = programPaymentIncome;
	}

	/**
	 * @return the supplyManagedCommodityIncome
	 */
	public Double getSupplyManagedCommodityIncome() {
		return supplyManagedCommodityIncome;
	}

	/**
	 * @param supplyManagedCommodityIncome the supplyManagedCommodityIncome to set
	 */
	public void setSupplyManagedCommodityIncome(Double supplyManagedCommodityIncome) {
		this.supplyManagedCommodityIncome = supplyManagedCommodityIncome;
	}

	/**
	 * @return the totalUnallowableExpenses
	 */
	public Double getTotalUnallowableExpenses() {
		return totalUnallowableExpenses;
	}

	/**
	 * @param totalUnallowableExpenses the totalUnallowableExpenses to set
	 */
	public void setTotalUnallowableExpenses(Double totalUnallowableExpenses) {
		this.totalUnallowableExpenses = totalUnallowableExpenses;
	}

	/**
	 * @return the totalUnallowableIncome
	 */
	public Double getTotalUnallowableIncome() {
		return totalUnallowableIncome;
	}

	/**
	 * @param totalUnallowableIncome the totalUnallowableIncome to set
	 */
	public void setTotalUnallowableIncome(Double totalUnallowableIncome) {
		this.totalUnallowableIncome = totalUnallowableIncome;
	}

	/**
	 * @return the unadjustedAllowableExpenses
	 */
	public Double getUnadjustedAllowableExpenses() {
		return unadjustedAllowableExpenses;
	}

	/**
	 * @param unadjustedAllowableExpenses the unadjustedAllowableExpenses to set
	 */
	public void setUnadjustedAllowableExpenses(Double unadjustedAllowableExpenses) {
		this.unadjustedAllowableExpenses = unadjustedAllowableExpenses;
	}

	/**
	 * @return the unadjustedAllowableIncome
	 */
	public Double getUnadjustedAllowableIncome() {
		return unadjustedAllowableIncome;
	}

	/**
	 * @param unadjustedAllowableIncome the unadjustedAllowableIncome to set
	 */
	public void setUnadjustedAllowableIncome(Double unadjustedAllowableIncome) {
		this.unadjustedAllowableIncome = unadjustedAllowableIncome;
	}

	/**
	 * @return the yardageExpenses
	 */
	public Double getYardageExpenses() {
		return yardageExpenses;
	}

	/**
	 * @param yardageExpenses the yardageExpenses to set
	 */
	public void setYardageExpenses(Double yardageExpenses) {
		this.yardageExpenses = yardageExpenses;
	}

	/**
	 * @return the yardageIncome
	 */
	public Double getYardageIncome() {
		return yardageIncome;
	}

	/**
	 * @param yardageIncome the yardageIncome to set
	 */
	public void setYardageIncome(Double yardageIncome) {
		this.yardageIncome = yardageIncome;
	}

	/**
	 * @return the productiveValue
	 */
	public Double getProductiveValue() {
		return productiveValue;
	}

	/**
	 * @param productiveValue the productiveValue to set
	 */
	public void setProductiveValue(Double productiveValue) {
		this.productiveValue = productiveValue;
	}
	
	/**
   * @return the expenseProductiveValue
   */
  public Double getExpenseProductiveValue() {
    return expenseProductiveValue;
  }

  /**
   * @param expenseProductiveValue the expenseProductiveValue to set
   */
  public void setExpenseProductiveValue(Double expenseProductiveValue) {
    this.expenseProductiveValue = expenseProductiveValue;
  }

  /**
   * Gets bpuLeadInd
   *
   * @return the bpuLeadInd
   */
  public Boolean getBpuLeadInd() {
    return bpuLeadInd;
  }

  /**
   * Sets bpuLeadInd
   *
   * @param value the value to set
   */
  public void setBpuLeadInd(Boolean value) {
    bpuLeadInd = value;
  }

	/**
   * Gets deferredProgramPayments
   *
   * @return the deferredProgramPayments
   */
  public Double getDeferredProgramPayments() {
    return deferredProgramPayments;
  }

  /**
   * Sets deferredProgramPayments
   *
   * @param pDeferredProgramPayments the deferredProgramPayments to set
   */
  public void setDeferredProgramPayments(Double pDeferredProgramPayments) {
    deferredProgramPayments = pDeferredProgramPayments;
  }

  /**
   * @return the expenseAccrualAdjs
   */
  public Double getExpenseAccrualAdjs() {
    return expenseAccrualAdjs;
  }

  /**
   * @param expenseAccrualAdjs the expenseAccrualAdjs to set
   */
  public void setExpenseAccrualAdjs(Double expenseAccrualAdjs) {
    this.expenseAccrualAdjs = expenseAccrualAdjs;
  }

  /**
   * @return the expenseStructuralChangeAdjs
   */
  public Double getExpenseStructuralChangeAdjs() {
    return expenseStructuralChangeAdjs;
  }

  /**
   * @param expenseStructuralChangeAdjs the expenseStructuralChangeAdjs to set
   */
  public void setExpenseStructuralChangeAdjs(Double expenseStructuralChangeAdjs) {
    this.expenseStructuralChangeAdjs = expenseStructuralChangeAdjs;
  }

  /**
   * @return the expensesAfterStructuralChange
   */
  public Double getExpensesAfterStructuralChange() {
    return expensesAfterStructuralChange;
  }

  /**
   * @param expensesAfterStructuralChange the expensesAfterStructuralChange to set
   */
  public void setExpensesAfterStructuralChange(Double expensesAfterStructuralChange) {
    this.expensesAfterStructuralChange = expensesAfterStructuralChange;
  }

  public Double getProdInsurDeemedSubtotal() {
    return prodInsurDeemedSubtotal;
  }

  public void setProdInsurDeemedSubtotal(Double prodInsurDeemedSubtotal) {
    this.prodInsurDeemedSubtotal = prodInsurDeemedSubtotal;
  }

  public Double getProdInsurDeemedTotal() {
    return prodInsurDeemedTotal;
  }

  public void setProdInsurDeemedTotal(Double prodInsurDeemedTotal) {
    this.prodInsurDeemedTotal = prodInsurDeemedTotal;
  }

  /**
   * @return  the total accruals
   */
  @JsonIgnore
  public double getTotalAccrualAdjs() {
    double total = 0;

    if (accrualAdjsCropInventory != null) {
      total += accrualAdjsCropInventory.doubleValue();
    }

    if (accrualAdjsLvstckInventory != null) {
      total += accrualAdjsLvstckInventory.doubleValue();
    }

    if (accrualAdjsPayables != null) {
      total += accrualAdjsPayables.doubleValue();
    }

    if (accrualAdjsPurchasedInputs != null) {
      total += accrualAdjsPurchasedInputs.doubleValue();
    }

    if (accrualAdjsReceivables != null) {
      total += accrualAdjsReceivables.doubleValue();
    }

    return total;
  }

  @JsonIgnore
  public Double getPercentageOfStructuralChange() {
    final double oneHundredPercent = 100;
    Double percentage = 0.0;
    
    if(productionMargAccrAdjs != null
        && productionMargAccrAdjs != 0
        && productionMargAftStrChangs != null) {
      
      percentage = (productionMargAftStrChangs - productionMargAccrAdjs) / Math.abs(productionMargAccrAdjs) * oneHundredPercent;
    }

    return percentage;
  }
 
  /**
   * 
   * @return String
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString(){
    
    Integer farmingYearId = null;
    if(farmingYear != null) {
      farmingYearId = farmingYear.getProgramYearId();
    }

    return "MarginTotal"+"\n"+
    "\t farmingYear : "+farmingYearId+"\n"+
    "\t marginTotalId : "+marginTotalId+"\n"+
    "\t accrualAdjsCropInventory : "+accrualAdjsCropInventory+"\n"+
    "\t accrualAdjsLvstckInventory : "+accrualAdjsLvstckInventory+"\n"+
    "\t accrualAdjsPayables : "+accrualAdjsPayables+"\n"+
    "\t accrualAdjsPurchasedInputs : "+accrualAdjsPurchasedInputs+"\n"+
    "\t accrualAdjsReceivables : "+accrualAdjsReceivables+"\n"+
    "\t ratioStructuralChangeAdjs : "+ratioStructuralChangeAdjs+"\n"+
    "\t additiveStructuralChangeAdjs : "+additiveStructuralChangeAdjs+"\n"+
    "\t structuralChangeAdjs : "+structuralChangeAdjs+"\n"+
    "\t totalAllowableExpenses : "+totalAllowableExpenses+"\n"+
    "\t totalAllowableIncome : "+totalAllowableIncome+"\n"+
    "\t unadjustedProductionMargin : "+unadjustedProductionMargin+"\n"+
    "\t productionMargAccrAdjs : "+productionMargAccrAdjs+"\n"+
    "\t ratioProductionMargAftStrChangs : "+ratioProductionMargAftStrChangs+"\n"+
    "\t additiveProductionMargAftStrChangs : "+additiveProductionMargAftStrChangs+"\n"+
    "\t productionMargAftStrChangs : "+productionMargAftStrChangs+"\n"+
    "\t bpuLeadInd : "+bpuLeadInd+"\n"+
    "\t contractWorkExpenses : "+contractWorkExpenses+"\n"+
    "\t manualExpenses : "+manualExpenses+"\n"+
    "\t farmSizeRatio : "+farmSizeRatio+"\n"+
    "\t expenseFarmSizeRatio : "+expenseFarmSizeRatio+"\n"+
    "\t fiscalYearProRateAdj : "+fiscalYearProRateAdj+"\n"+
    "\t isStructuralChangeNotable : "+isStructuralChangeNotable+"\n"+
    "\t isRatioStructuralChangeNotable : "+isRatioStructuralChangeNotable+"\n"+
    "\t isAdditiveStructuralChangeNotable : "+isStructuralChangeNotable+"\n"+
    "\t productiveValue : "+productiveValue+"\n"+
    "\t expenseProductiveValue : "+expenseProductiveValue+"\n"+
    "\t programPaymentIncome : "+programPaymentIncome+"\n"+
    "\t supplyManagedCommodityIncome : "+supplyManagedCommodityIncome+"\n"+
    "\t totalUnallowableExpenses : "+totalUnallowableExpenses+"\n"+
    "\t totalUnallowableIncome : "+totalUnallowableIncome+"\n"+
    "\t unadjustedAllowableExpenses : "+unadjustedAllowableExpenses+"\n"+
    "\t unadjustedAllowableIncome : "+unadjustedAllowableIncome+"\n"+
    "\t yardageExpenses : "+yardageExpenses+"\n"+
    "\t yardageIncome : "+yardageIncome+"\n"+
    "\t deferredProgramPayments : "+deferredProgramPayments+"\n"+
    "\t expenseAccrualAdjs : "+expenseAccrualAdjs+"\n"+
    "\t expenseStructuralChangeAdjs : "+expenseStructuralChangeAdjs+"\n"+
    "\t expensesAfterStructuralChange : "+expensesAfterStructuralChange+"\n"+
    "\t prodInsurDeemedSubtotal : "+prodInsurDeemedSubtotal+"\n"+
    "\t prodInsurDeemedTotal : "+prodInsurDeemedTotal+"\n"+
    "\t revisionCount : "+revisionCount;
  }
}
