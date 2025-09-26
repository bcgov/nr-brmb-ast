/**
 * Copyright (c) 2021,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain.tips;

import java.io.Serializable;

/**
 * @author awilkinson
 */
public class TipBenchmarkGroup implements Serializable {

  private static final long serialVersionUID = 1L;
  
  private Integer programYear;
  private Integer farmTypeLevel;
  private String farmType;
  private Double incomeRangeLow;
  private Double incomeRangeHigh;

  public Integer getProgramYear() {
    return programYear;
  }

  public void setProgramYear(Integer programYear) {
    this.programYear = programYear;
  }

  public Integer getFarmTypeLevel() {
    return farmTypeLevel;
  }

  public void setFarmTypeLevel(Integer farmTypeLevel) {
    this.farmTypeLevel = farmTypeLevel;
  }

  public String getFarmType() {
    return farmType;
  }

  public void setFarmType(String farmType) {
    this.farmType = farmType;
  }

  public Double getIncomeRangeLow() {
    return incomeRangeLow;
  }

  public void setIncomeRangeLow(Double incomeRangeLow) {
    this.incomeRangeLow = incomeRangeLow;
  }

  public Double getIncomeRangeHigh() {
    return incomeRangeHigh;
  }

  public void setIncomeRangeHigh(Double incomeRangeHigh) {
    this.incomeRangeHigh = incomeRangeHigh;
  }

  @Override
  public String toString() {
    return "TipBenchmarkGroup [incomeRangeLow="
        + incomeRangeLow + ", incomeRangeHigh=" + incomeRangeHigh + "]";
  }

}
