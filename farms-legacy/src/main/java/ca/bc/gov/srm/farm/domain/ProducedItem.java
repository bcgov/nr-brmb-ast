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

import com.fasterxml.jackson.annotation.JsonBackReference;

import ca.bc.gov.srm.farm.util.MathUtils;

/**
 * Represents inventory items with Fair Market Values.
 * 
 * This class represents the entries for both reported (federal data imports)
 * and adjustment values (fields are prefixed with "reported" and "adj"
 * respectively). Each adjustable value also has a getter for the total value
 * (sum of reported and adjustment) prefixed with "getTotal".  
 *
 * @author awilkinson
 * @created Nov 15, 2010
 */
public abstract class ProducedItem extends InventoryItem {

  private static final long serialVersionUID = -8714985369710886679L;

  /** Fair Market Value for the first month of the year. */
  private Double fmvStart;

  /** Fair Market Value for the last month of the year. */
  private Double fmvEnd;
  
  /** Fair Market Value for the last month of the previous year. */
  private Double fmvPreviousYearEnd;

  /** Standard deviation of the Fair Market Values of each month of the year. */
  private Double fmvVariance;
  
  /** Average of the Fair Market Values of each month of the year. */
  private Double fmvAverage;


  /**
   * @return the fmvEnd
   */
  public Double getFmvEnd() {
    return fmvEnd;
  }

  /**
   * @param fmvEnd the fmvEnd to set
   */
  public void setFmvEnd(Double fmvEnd) {
    this.fmvEnd = fmvEnd;
  }

  /**
   * @return the fmvStart
   */
  public Double getFmvStart() {
    return fmvStart;
  }

  /**
   * @param fmvStart the fmvStart to set
   */
  public void setFmvStart(Double fmvStart) {
    this.fmvStart = fmvStart;
  }

  /**
   * @return the fmvVariance
   */
  public Double getFmvVariance() {
    return fmvVariance;
  }

  /**
   * @param fmvVariance the fmvVariance to set
   */
  public void setFmvVariance(Double fmvVariance) {
    this.fmvVariance = fmvVariance;
  }

  /**
   * @return the fmvAverage
   */
  public Double getFmvAverage() {
    return fmvAverage;
  }

  /**
   * @param fmvAverage the fmvAverage to set the value to
   */
  public void setFmvAverage(Double fmvAverage) {
    this.fmvAverage = fmvAverage;
  }

  /**
   * @return the fmvPreviousYearEnd
   */
  public Double getFmvPreviousYearEnd() {
    return fmvPreviousYearEnd;
  }

  /**
   * @param fmvPreviousYearEnd the fmvPreviousYearEnd to set
   */
  public void setFmvPreviousYearEnd(Double fmvPreviousYearEnd) {
    this.fmvPreviousYearEnd = fmvPreviousYearEnd;
  }

  /**
   * @return the calculated change in value based on start and end quantity and price
   */
  @JsonBackReference
  public Double getTotalChangeInValue() {
    double result;
    double startValue = 0;
    double endValue = 0;

    if(getTotalQuantityStart() != null && getTotalPriceStart() != null) {
      startValue = getTotalQuantityStart().doubleValue() * getTotalPriceStart().doubleValue();
    }
    
    if(getTotalQuantityEnd() != null && getTotalPriceEnd() != null) {
      endValue = getTotalQuantityEnd().doubleValue() * getTotalPriceEnd().doubleValue();
    }
    
    result = endValue - startValue;
    
    return new Double(result);
  }
  
  
  /**
   * @param o Object to compare
   * @return true if o is a ProducedItem and the inventory item code and the reported values are equal
   */
  @Override
  public boolean equals(Object o) {
    boolean result = false;
    if(o != null && this.getClass().isInstance(o)) {
      ProducedItem item = (ProducedItem) o;
      result =
        this.getInventoryItemCode().equals(item.getInventoryItemCode())
        && MathUtils.equalToThreeDecimalPlaces(this.getReportedQuantityStart(), item.getReportedQuantityStart())
        && MathUtils.equalToTwoDecimalPlaces(this.getReportedPriceStart(), item.getReportedPriceStart())
        && MathUtils.equalToThreeDecimalPlaces(this.getReportedQuantityEnd(), item.getReportedQuantityEnd())
        && MathUtils.equalToTwoDecimalPlaces(this.getReportedPriceEnd(), item.getReportedPriceEnd());
    } else {
      result = false;
    }
    return result;
  }


  /**
   * @return hash code
   */
  @Override
  public int hashCode() {
    int hash = 1;
    int iic = 0;
    int qs = 0;
    int ps = 0;
    int qe = 0;
    int pe = 0;
    if(getInventoryItemCode() != null) {
      iic = getInventoryItemCode().hashCode();
    }
    if(getReportedQuantityStart() != null) {
      qs = getReportedQuantityStart().hashCode();
    }
    if(getReportedPriceStart() != null) {
      ps = getReportedPriceStart().hashCode();
    }
    if(getReportedQuantityEnd() != null) {
      qe = getReportedQuantityEnd().hashCode();
    }
    if(getReportedPriceEnd() != null) {
      pe = getReportedPriceEnd().hashCode();
    }

    final int seventeen = 17;
    final int thirtyOne = 31;
    final int thirteen = 13;
    final int nineteen = 19;
    final int twentyThree = 23;

    hash = hash * seventeen + iic;
    hash = hash * thirtyOne + qs;
    hash = hash * thirteen + ps;
    hash = hash * nineteen + qe;
    hash = hash * twentyThree + pe;
    return hash;
  }


  /**
   * @return String
   * @see Object#toString()
   */
  @Override
  public String toString(){
    return "ProducedItem"+"\n"+
    "[ : "+super.toString()+"]\n"+
    "\t fmvStart : "+fmvStart+"\n"+
    "\t fmvEnd : "+fmvEnd+"\n"+
    "\t fmvPreviousYearEnd : "+fmvPreviousYearEnd+"\n"+
    "\t fmvVariance : "+fmvVariance+"\n"+
    "\t fmvAverage : "+fmvAverage;
  }
  
}
