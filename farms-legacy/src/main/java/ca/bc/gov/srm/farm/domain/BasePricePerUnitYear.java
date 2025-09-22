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
package ca.bc.gov.srm.farm.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * BasePricePerUnit (BPU) is the term used by the clients; however it is really
 * the expected profit margin on a commodity.
 *
 * @author   Vivid Solutions Inc.
 * @created  03-Jul-2009 2:06:48 PM
 */
public final class BasePricePerUnitYear implements Serializable {

  private static final long serialVersionUID = -7895606690518575133L;

  /** back-reference to the object containing this */
  @JsonBackReference
  private BasePricePerUnit basePricePerUnit;

  /** the expected profit. */
  private Double margin;
  
  /** the expected expense */
  private Double expense;

  private Integer year;

  /**
   * revisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   */
  private Integer revisionCount;

  /** Constructor. */
  public BasePricePerUnitYear() {

  }

  /**
   * Gets margin.
   *
   * @return  the margin
   */
  public Double getMargin() {
    return margin;
  }

  /**
   * Sets margin.
   *
   * @param  pMargin  the margin to set
   */
  public void setMargin(Double pMargin) {
    margin = pMargin;
  }

  /**
   * @return the expense
   */
  public Double getExpense() {
    return expense;
  }

  /**
   * @param pExpense the expense to set
   */
  public void setExpense(Double pExpense) {
    this.expense = pExpense;
  }

  /**
   * Gets year.
   *
   * @return  the year
   */
  public Integer getYear() {
    return year;
  }

  /**
   * Sets year.
   *
   * @param  pYear  the year to set
   */
  public void setYear(Integer pYear) {
    year = pYear;
  }

  /**
   * Gets revisionCount.
   *
   * @return  the revisionCount
   */
  public Integer getRevisionCount() {
    return revisionCount;
  }

  /**
   * Sets revisionCount.
   *
   * @param  pRevisionCount  the revisionCount to set
   */
  public void setRevisionCount(Integer pRevisionCount) {
    revisionCount = pRevisionCount;
  }

  /**
   * @return the basePricePerUnit
   */
  public BasePricePerUnit getBasePricePerUnit() {
    return basePricePerUnit;
  }

  /**
   * @param basePricePerUnit the basePricePerUnit to set the value to
   */
  public void setBasePricePerUnit(BasePricePerUnit basePricePerUnit) {
    this.basePricePerUnit = basePricePerUnit;
  }
  

  /**
   * @return  String
   *
   * @see     java.lang.Object#toString()
   */
  @Override
  public String toString() {

    Integer basePricePerUnitId = null;
    if(basePricePerUnit != null) {
      basePricePerUnitId = basePricePerUnit.getBasePricePerUnitId();
    }

    return "BasePricePerUnitYear" + "\n" +
    "\t basePricePerUnit : "+basePricePerUnitId+"\n"+
    "\t year : " + year + "\n" +
    "\t margin : " + margin + "\n" +
    "\t expense : " + expense + "\n" +
    "\trevisionCount : " + revisionCount;
  }
  
}
