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
package ca.bc.gov.srm.farm.domain;

import java.io.Serializable;

/**
 * @author awilkinson
 */
public class DeductionLineItem implements Serializable {

  private static final long serialVersionUID = -5205370713535684053L;

  /** lineItem is income or expense item for Agristability. */
  private Integer lineItem;
  
  /** description describes the LineItem. */
  private String description;
  
  /** isDeductionProgramYearMinus5 indicates if this a deduction line item
   *  of a particular type for the program year minus 5 */
  private Boolean isDeductionProgramYearMinus5;
  
  /** isDeductionProgramYearMinus4 indicates if this a deduction line item
   *  of a particular type for the program year minus 4 */
  private Boolean isDeductionProgramYearMinus4;
  
  /** isDeductionProgramYearMinus3 indicates if this a deduction line item
   *  of a particular type for the program year minus 3 */
  private Boolean isDeductionProgramYearMinus3;
  
  /** isDeductionProgramYearMinus2 indicates if this a deduction line item
   *  of a particular type for the program year minus 2 */
  private Boolean isDeductionProgramYearMinus2;
  
  /** isDeductionProgramYearMinus1 indicates if this a deduction line item
   *  of a particular type for the program year minus 1 */
  private Boolean isDeductionProgramYearMinus1;
  
  /** isDeductionProgramYear indicates if this a deduction line item
   *  of a particular type for the program year */
  private Boolean isDeductionProgramYear;

  /**
   * @return the lineItem
   */
  public Integer getLineItem() {
    return lineItem;
  }

  /**
   * @param lineItem the lineItem to set
   */
  public void setLineItem(Integer lineItem) {
    this.lineItem = lineItem;
  }

  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @return the isDeductionProgramYearMinus5
   */
  public Boolean getIsDeductionProgramYearMinus5() {
    return isDeductionProgramYearMinus5;
  }

  /**
   * @param isDeductionProgramYearMinus5 the isDeductionProgramYearMinus5 to set
   */
  public void setIsDeductionProgramYearMinus5(Boolean isDeductionProgramYearMinus5) {
    this.isDeductionProgramYearMinus5 = isDeductionProgramYearMinus5;
  }

  /**
   * @return the isDeductionProgramYearMinus4
   */
  public Boolean getIsDeductionProgramYearMinus4() {
    return isDeductionProgramYearMinus4;
  }

  /**
   * @param isDeductionProgramYearMinus4 the isDeductionProgramYearMinus4 to set
   */
  public void setIsDeductionProgramYearMinus4(Boolean isDeductionProgramYearMinus4) {
    this.isDeductionProgramYearMinus4 = isDeductionProgramYearMinus4;
  }

  /**
   * @return the isDeductionProgramYearMinus3
   */
  public Boolean getIsDeductionProgramYearMinus3() {
    return isDeductionProgramYearMinus3;
  }

  /**
   * @param isDeductionProgramYearMinus3 the isDeductionProgramYearMinus3 to set
   */
  public void setIsDeductionProgramYearMinus3(Boolean isDeductionProgramYearMinus3) {
    this.isDeductionProgramYearMinus3 = isDeductionProgramYearMinus3;
  }

  /**
   * @return the isDeductionProgramYearMinus2
   */
  public Boolean getIsDeductionProgramYearMinus2() {
    return isDeductionProgramYearMinus2;
  }

  /**
   * @param isDeductionProgramYearMinus2 the isDeductionProgramYearMinus2 to set
   */
  public void setIsDeductionProgramYearMinus2(Boolean isDeductionProgramYearMinus2) {
    this.isDeductionProgramYearMinus2 = isDeductionProgramYearMinus2;
  }

  /**
   * @return the isDeductionProgramYearMinus1
   */
  public Boolean getIsDeductionProgramYearMinus1() {
    return isDeductionProgramYearMinus1;
  }

  /**
   * @param isDeductionProgramYearMinus1 the isDeductionProgramYearMinus1 to set
   */
  public void setIsDeductionProgramYearMinus1(Boolean isDeductionProgramYearMinus1) {
    this.isDeductionProgramYearMinus1 = isDeductionProgramYearMinus1;
  }

  /**
   * @return the isDeductionProgramYear
   */
  public Boolean getIsDeductionProgramYear() {
    return isDeductionProgramYear;
  }

  /**
   * @param isDeductionProgramYear the isDeductionProgramYear to set
   */
  public void setIsDeductionProgramYear(Boolean isDeductionProgramYear) {
    this.isDeductionProgramYear = isDeductionProgramYear;
  }
}
