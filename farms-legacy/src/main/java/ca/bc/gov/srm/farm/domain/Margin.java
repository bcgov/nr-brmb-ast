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
 * Margin refer to the difference between revenues and expenses.
 * Margin is used for calculating claims - note that claim values
 * will not be calculated within the system, rather they will imported via the
 * federal data imports. There may be many Margin associated with a
 * client. There are several types of Margin including.
 *
 * <p>1) ProgramYear margin - calculated for any given year. 2) Current
 * ProgramYear margin - the margin for the current year. 3) Adjusted ProgramYear
 * margin - includes adjustments to financial or commodity data (structural
 * changes) to account for changes that have occurred in the producer operation
 * since the original documentation was filed. 4) Reference margin - the Olympic
 * average of adjusted margins for the past five years. 5) Contribution
 * reference margin - the estimated margin for the current ProgramYear (used for
 * fee calculations).</p>
 *
 * @author awilkinson
 * @created Nov 18, 2010
 */
public final class Margin implements Serializable {
  
  private static final long serialVersionUID = 9120431159351627113L;

  /** back-reference to the object containing this */
  @JsonBackReference
  private FarmingOperation farmingOperation;

  /**
   * marginId is a surrogate unique identifier for Margin.
   */
  private Integer marginId;

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
  private Double expenseAccrualAdjs;
  private Double prodInsurDeemedSubtotal;
  private Double prodInsurDeemedTotal;


  /**
   * revisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   */
  private Integer revisionCount;


  /**
   * @return  Integer
   */
  public Integer getMarginId() {
    return marginId;
  }

  /**
   * @param  newVal  The new value for this property
   */
  public void setMarginId(final Integer newVal) {
    marginId = newVal;
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
   * @return the farmingOperation
   */
  public FarmingOperation getFarmingOperation() {
    return farmingOperation;
  }

  /**
   * @param farmingOperation the farmingOperation to set the value to
   */
  public void setFarmingOperation(FarmingOperation farmingOperation) {
    this.farmingOperation = farmingOperation;
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

	/**
   * 
   * @return String
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString(){
    
    Integer farmingOperationId = null;
    if(farmingOperation != null) {
      farmingOperationId = farmingOperation.getFarmingOperationId();
    }

    return "Margin"+"\n"+
    "\t farmingOperation : "+farmingOperationId+"\n"+
    "\t marginId : "+marginId+"\n"+
    "\t accrualAdjsCropInventory : "+accrualAdjsCropInventory+"\n"+
    "\t accrualAdjsLvstckInventory : "+accrualAdjsLvstckInventory+"\n"+
    "\t accrualAdjsPayables : "+accrualAdjsPayables+"\n"+
    "\t accrualAdjsPurchasedInputs : "+accrualAdjsPurchasedInputs+"\n"+
    "\t accrualAdjsReceivables : "+accrualAdjsReceivables+"\n"+
    "\t totalAllowableExpenses : "+totalAllowableExpenses+"\n"+
    "\t totalAllowableIncome : "+totalAllowableIncome+"\n"+
    "\t unadjustedProductionMargin : "+unadjustedProductionMargin+"\n"+
    "\t productionMargAccrAdjs : "+productionMargAccrAdjs+"\n"+
    "\t contractWorkExpenses : "+contractWorkExpenses+"\n"+
    "\t manualExpenses : "+manualExpenses+"\n"+
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
    "\t prodInsurDeemedSubtotal : "+prodInsurDeemedSubtotal+"\n"+
    "\t prodInsurDeemedTotal : "+prodInsurDeemedTotal+"\n"+
    "\t revisionCount : "+revisionCount;
  }
  
}
