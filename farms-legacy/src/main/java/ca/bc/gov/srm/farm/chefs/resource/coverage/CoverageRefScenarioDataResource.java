/**
 * Copyright (c) 2024,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.chefs.resource.coverage;

import java.io.Serializable;

public class CoverageRefScenarioDataResource implements Serializable {

  private static final long serialVersionUID = -5134483570909942550L;
  private Integer year;
  private Boolean structureChangeApplied;
  private Boolean usedInCalc;
  private Double totalEligibleIncome;
  private Double totalEligibleExpenses;
  private Double percentageChange;
  private Double productionMargAccrAdjs;
  private Double productionMargAftStrChanges;

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public Boolean getStructureChangeApplied() {
    return structureChangeApplied;
  }

  public void setStructureChangeApplied(Boolean structureChangeApplied) {
    this.structureChangeApplied = structureChangeApplied;
  }

  public Boolean getUsedInCalc() {
    return usedInCalc;
  }

  public void setUsedInCalc(Boolean usedInCalc) {
    this.usedInCalc = usedInCalc;
  }

  public Double getTotalEligibleIncome() {
    return totalEligibleIncome;
  }

  public void setTotalEligibleIncome(Double totalEligibleIncome) {
    this.totalEligibleIncome = totalEligibleIncome;
  }

  public Double getTotalEligibleExpenses() {
    return totalEligibleExpenses;
  }

  public void setTotalEligibleExpenses(Double totalEligibleExpenses) {
    this.totalEligibleExpenses = totalEligibleExpenses;
  }

  public Double getPercentageChange() {
    return percentageChange;
  }

  public void setPercentageChange(Double percentageChange) {
    this.percentageChange = percentageChange;
  }

  public Double getProductionMargAccrAdjs() {
    return productionMargAccrAdjs;
  }

  public void setProductionMargAccrAdjs(Double productionMargAccrAdjs) {
    this.productionMargAccrAdjs = productionMargAccrAdjs;
  }

  public Double getProductionMargAftStrChanges() {
    return productionMargAftStrChanges;
  }

  public void setProductionMargAftStrChanges(Double productionMargAftStrChanges) {
    this.productionMargAftStrChanges = productionMargAftStrChanges;
  }

  @Override
  public String toString() {
    return "CoverageRefScenarioDataResource [year=" + year + ", structureChangeApplied=" + structureChangeApplied + ", usedInCalc=" + usedInCalc
        + ", totalEligibleIncome=" + totalEligibleIncome + ", totalEligibleExpenses=" + totalEligibleExpenses + ", percentageChange="
        + percentageChange + ", productionMargAccrAdjs=" + productionMargAccrAdjs + ", productionMargAftStrChanges=" + productionMargAftStrChanges
        + "]";
  }

}