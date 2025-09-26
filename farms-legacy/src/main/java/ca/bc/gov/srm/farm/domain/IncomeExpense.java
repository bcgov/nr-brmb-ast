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

import ca.bc.gov.srm.farm.util.MathUtils;


/**
 * IncomeExpense data refers to data submitted on a client's tax return
 * (statement). IncomeExpense data will originate from the federal
 * imports. There may be many IncomeExpense records for a given
 * FarmingOperation- i.e. multiple income and isExpense line items on a tax
 * return. IncomeExpense items may have many instances of an INCOME
 * EXPENSE ADJUSTMENT.
 *
 * This class represents the entries for both reported (federal data imports)
 * and adjustment values (fields are prefixed with "reported" and "adj"
 * respectively). Each adjustable value also has a getter for the total value
 * (sum of reported and adjustment) prefixed with "getTotal".  
 *
 * @author awilkinson
 * @created Nov 12, 2010
 */
public class IncomeExpense implements Serializable {
  
  private static final long serialVersionUID = 611203306265719047L;

  /** back-reference to the object containing this */
  @JsonBackReference
  private FarmingOperation farmingOperation;

  /**
   * reportedIncomeExpenseId is a surrogate unique identifier for
   * IncomeExpenses.
   */
  private Integer reportedIncomeExpenseId;
  private Integer adjIncomeExpenseId;

  /** amount is the Income/Expense Amount (not adjusted for prshp pct). */
  private Double reportedAmount;
  private Double adjAmount;

  /** isExpense is the indator for the Income or Expense ('Y' or 'N'). */
  private Boolean isExpense;

  /** DOCUMENT ME! */
  private LineItem lineItem;
  
  /** The user who made the adjustment */
  private String adjustedByUserId;

  /**
   * revisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   */
  private Integer revisionCount;


  /**
   * reportedIncomeExpenseId is a surrogate unique identifier for
   * the reported (original) isExpense.
   *
   * @return  Integer
   */
  public Integer getReportedIncomeExpenseId() {
    return reportedIncomeExpenseId;
  }

  /**
   * reportedIncomeExpenseId is a surrogate unique identifier for
   * the reported (original) isExpense.
   *
   * @param  newVal  The new value for this property
   */
  public void setReportedIncomeExpenseId(final Integer newVal) {
    reportedIncomeExpenseId = newVal;
  }

  /**
   * adjIncomeExpenseId is a surrogate unique identifier for
   * the isExpense adjustment.
   *
   * @return the adjIncomeExpenseId
   */
  public Integer getAdjIncomeExpenseId() {
    return adjIncomeExpenseId;
  }

  /**
   * adjIncomeExpenseId is a surrogate unique identifier for
   * the isExpense adjustment.
   *
   * @param adjIncomeExpenseId the adjIncomeExpenseId to set
   */
  public void setAdjIncomeExpenseId(Integer adjIncomeExpenseId) {
    this.adjIncomeExpenseId = adjIncomeExpenseId;
  }

  /**
   * reportedAmount is the Income/Expense Amount (not adjusted for prshp pct)
   * reported by the farmer.
   *
   * @return  Double
   */
  public Double getReportedAmount() {
    return reportedAmount;
  }

  /**
   * reportedAmount is the Income/Expense Amount (not adjusted for prshp pct)
   * reported by the farmer.
   *
   * @param  newVal  The new value for this property
   */
  public void setReportedAmount(final Double newVal) {
    reportedAmount = newVal;
  }

  /**
   * adjAmount is the Income/Expense Amount (not adjusted for prshp pct)
   * adjustment.
   *
   * @return the adjAmount
   */
  public Double getAdjAmount() {
    return adjAmount;
  }

  /**
   * adjAmount is the Income/Expense Amount (not adjusted for prshp pct)
   * adjustment.
   *
   * @param adjAmount the adjAmount to set
   */
  public void setAdjAmount(Double adjAmount) {
    this.adjAmount = adjAmount;
  }

  /**
   * totalAmount is the Income/Expense Amount (not adjusted for prshp pct)
   * calculated as the sum of reported amount and adjusted amount.
   *
   * @return the totalAmount
   */
  @JsonIgnore
  public Double getTotalAmount() {
    Double result;
    
    if(reportedAmount != null && adjAmount != null) {
      result = new Double(reportedAmount.doubleValue() + adjAmount.doubleValue());
    } else if(reportedAmount != null) {
      result = reportedAmount;
    } else if(adjAmount != null) {
      result = adjAmount;
    } else {
      throw new IllegalStateException("reportedAmount and adjAmount cannot both be null");
    }
    
    return result;
  }

  /**
   * isExpense is the indator for the Income or Expense ('Y' or 'N').
   *
   * @return  Boolean
   */
  public Boolean getIsExpense() {
    return isExpense;
  }

  /**
   * isExpense is the indator for the Income or Expense ('Y' or 'N').
   *
   * @param  newVal  The new value for this property
   */
  public void setIsExpense(final Boolean newVal) {
    isExpense = newVal;
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
   * @return  the lineItem
   */
  public LineItem getLineItem() {
    return lineItem;
  }

  /**
   * @param  lineItem  the lineItem to set
   */
  public void setLineItem(final LineItem lineItem) {
    if(lineItem != null) {
      lineItem.setIncomeExpense(this);
    }
    this.lineItem = lineItem;
  }

  /**
   * @return the adjustedByUserId
   */
  public String getAdjustedByUserId() {
    return adjustedByUserId;
  }

  /**
   * @param adjustedByUserId the adjustedByUserId to set the value to
   */
  public void setAdjustedByUserId(String adjustedByUserId) {
    this.adjustedByUserId = adjustedByUserId;
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
   * @param ie IncomeExpense to compare
   * @return true if the IncomeExpense matches the line item and the expense indicator
   */
  public boolean matches(IncomeExpense ie) {
    return this.lineItem.getLineItem().equals(ie.lineItem.getLineItem())
      && this.isExpense.equals(ie.isExpense);
  }
  
  /**
   * @param ie IncomeExpense to compare
   * @return true if the IncomeExpense matches the line item and the expense indicator
   */
  public boolean equals(IncomeExpense ie) {
    return this.lineItem.getLineItem().equals(ie.lineItem.getLineItem())
      && this.isExpense.equals(ie.isExpense)
      && MathUtils.equalToTwoDecimalPlaces(this.reportedAmount, ie.reportedAmount);
  }

  /**
   * @return hash code
   */
  @Override
  public int hashCode() {
    int hash = super.hashCode();
    int li = 0;
    int ie = 0;
    int ra = 0;
    if(lineItem != null && lineItem.getLineItem() != null) {
      li = lineItem.getLineItem().intValue();
    }
    if(isExpense != null) {
      ie = isExpense.hashCode();
    }
    if(reportedAmount != null) {
      ra = reportedAmount.hashCode();
    }

    final int twentyNine = 29;
    final int seventeen = 17;
    final int twentyThree = 23;

    hash = hash * twentyNine + li;
    hash = hash * seventeen + ie;
    hash = hash * twentyThree + ra;

    return hash;
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

    return "IncomeExpense"+"\n"+
    "\t farmingOperation : "+farmingOperationId+"\n"+
    "\t reportedIncomeExpenseId : "+reportedIncomeExpenseId+"\n"+
    "\t adjIncomeExpenseId : "+adjIncomeExpenseId+"\n"+
    "\t reportedAmount : "+reportedAmount+"\n"+
    "\t adjAmount : "+adjAmount+"\n"+
    "\t isExpense : "+isExpense+"\n"+
    "\t revisionCount : "+revisionCount+"\n"+
    "\t lineItem : "+lineItem;
  }
}
