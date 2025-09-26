package ca.bc.gov.srm.farm.domain.reasonability.revenue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NurseryRevenueRiskSubTestResult implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  private Boolean subTestPass;
  private Double varianceLimit;
  private Double deathRate;
  private Double deaths;
  private Double totalQuantityStart;
  private Double totalQuantityEnd;
  private Double reportedRevenue;
  private Double expectedRevenue;
  private Double reportedExpenses;
  private Double variance;
  private List<RevenueRiskProductiveUnit> productiveUnits;
  private List<RevenueRiskInventoryItem> inventory;
  private List<RevenueRiskIncomeTestResult> incomes;

  public Boolean getSubTestPass() {
    return subTestPass;
  }

  public void setSubTestPass(Boolean subTestPass) {
    this.subTestPass = subTestPass;
  }

  public Double getVarianceLimit() {
    return varianceLimit;
  }

  public void setVarianceLimit(Double varianceLimit) {
    this.varianceLimit = varianceLimit;
  }

  public Double getDeathRate() {
    return deathRate;
  }

  public void setDeathRate(Double deathRate) {
    this.deathRate = deathRate;
  }

  public Double getDeaths() {
    return deaths;
  }

  public void setDeaths(Double deaths) {
    this.deaths = deaths;
  }

  public Double getReportedExpenses() {
    return reportedExpenses;
  }

  public void setReportedExpenses(Double reportedExpenses) {
    this.reportedExpenses = reportedExpenses;
  }

  public Double getReportedRevenue() {
    return reportedRevenue;
  }

  public void setReportedRevenue(Double reportedRevenue) {
    this.reportedRevenue = reportedRevenue;
  }

  public Double getExpectedRevenue() {
    return expectedRevenue;
  }

  public void setExpectedRevenue(Double expectedRevenue) {
    this.expectedRevenue = expectedRevenue;
  }

  public Double getVariance() {
    return variance;
  }

  public void setVariance(Double variance) {
    this.variance = variance;
  }

  public List<RevenueRiskProductiveUnit> getProductiveUnits() {
    if(productiveUnits == null) {
      productiveUnits = new ArrayList<>();
    }
    return productiveUnits;
  }

  public void setProductiveUnits(List<RevenueRiskProductiveUnit> productiveUnits) {
    this.productiveUnits = productiveUnits;
  }

  public List<RevenueRiskInventoryItem> getInventory() {
    if(inventory == null) {
      inventory = new ArrayList<>();
    }
    return inventory;
  }

  public void setInventory(List<RevenueRiskInventoryItem> inventory) {
    this.inventory = inventory;
  }

  public List<RevenueRiskIncomeTestResult> getIncomes() {
    if(incomes == null) {
      incomes = new ArrayList<>();
    }
    return incomes;
  }

  public void setIncomes(List<RevenueRiskIncomeTestResult> incomes) {
    this.incomes = incomes;
  }

  public Double getTotalQuantityStart() {
    return totalQuantityStart;
  }

  public void setTotalQuantityStart(Double totalQuantityStart) {
    this.totalQuantityStart = totalQuantityStart;
  }

  public Double getTotalQuantityEnd() {
    return totalQuantityEnd;
  }

  public void setTotalQuantityEnd(Double totalQuantityEnd) {
    this.totalQuantityEnd = totalQuantityEnd;
  }
  
  public Boolean getHasNursery() {
    return !getIncomes().isEmpty()
        || !getInventory().isEmpty()
        || !getProductiveUnits().isEmpty();
    
  }
  
  /**
   *  This setter is here to make sure this is treated
   *  as a property and serialized to JSON
   */
  @SuppressWarnings("unused")
  public void setHasNursery(Boolean hasNursery) {
    // do nothing
  }

  public void copy(NurseryRevenueRiskSubTestResult o) {
    
    subTestPass = o.subTestPass;
    varianceLimit = o.varianceLimit;
    deathRate = o.deathRate;
    deaths = o.deaths;
    totalQuantityStart = o.totalQuantityStart;
    totalQuantityEnd = o.totalQuantityEnd;
    reportedRevenue = o.reportedRevenue;
    expectedRevenue = o.expectedRevenue;
    reportedExpenses = o.reportedExpenses;
    variance = o.variance;
    
    getProductiveUnits().clear();
    getProductiveUnits().addAll(o.getProductiveUnits());
    getInventory().clear();
    getInventory().addAll(o.getInventory());
    getIncomes().clear();
    getIncomes().addAll(o.getIncomes());
  }

}
