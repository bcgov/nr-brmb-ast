package ca.bc.gov.srm.farm.domain.reasonability.revenue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HogsRevenueRiskSubTestResult implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  private Boolean hasHogs;
  private Boolean hogsPass;
  private Boolean farrowToFinishOperation;
  private Boolean feederOperation;
  private Double reportedExpenses;
  private Double sowsBreeding;
  private Double birthsPerCycle;
  private Double birthCyclesPerYear;
  private Double totalBirthsPerCycle;
  private Double totalBirthsAllCycles;
  private Double deathRate;
  private Double deaths;
  private Double feederProductiveUnits;
  private Double boarPurchaseCount;
  private Double boarPurchasePrice;
  private Double boarPurchaseExpense;
  private Double sowPurchaseCount;
  private Double sowPurchasePrice;
  private Double sowPurchaseExpense;
  private Double weanlingPurchaseCount;
  private Double weanlingPurchasePrice;
  private Double weanlingPurchaseExpense;
  private Double totalPurchaseCount;
  private Double totalQuantityStart;
  private Double totalQuantityEnd;
  private Double expectedSold;
  private Double heaviestHogFmvPrice;
  private Double reportedRevenue;
  private Double expectedRevenue;
  private Double revenueVariance;
  private Double varianceLimit;
  private List<RevenueRiskInventoryItem> inventory;

  public Double getTotalPurchaseCount() {
    return totalPurchaseCount;
  }

  public void setTotalPurchaseCount(Double totalPurchaseCount) {
    this.totalPurchaseCount = totalPurchaseCount;
  }

  public Double getSowsBreeding() {
    return sowsBreeding;
  }

  public void setSowsBreeding(Double sowsBreeding) {
    this.sowsBreeding = sowsBreeding;
  }

  public Double getBirthsPerCycle() {
    return birthsPerCycle;
  }

  public void setBirthsPerCycle(Double birthsPerCycle) {
    this.birthsPerCycle = birthsPerCycle;
  }

  public Double getTotalBirthsAllCycles() {
    return totalBirthsAllCycles;
  }

  public Double getTotalBirthsPerCycle() {
    return totalBirthsPerCycle;
  }

  public void setTotalBirthsPerCycle(Double totalBirthsPerCycle) {
    this.totalBirthsPerCycle = totalBirthsPerCycle;
  }

  public void setTotalBirthsAllCycles(Double birthsAllCycles) {
    this.totalBirthsAllCycles = birthsAllCycles;
  }

  public Double getFeederProductiveUnits() {
    return feederProductiveUnits;
  }

  public void setFeederProductiveUnits(Double feederProductiveUnits) {
    this.feederProductiveUnits = feederProductiveUnits;
  }

  public Double getBoarPurchaseCount() {
    return boarPurchaseCount;
  }

  public void setBoarPurchaseCount(Double boarPurchaseCount) {
    this.boarPurchaseCount = boarPurchaseCount;
  }

  public Double getSowPurchaseCount() {
    return sowPurchaseCount;
  }

  public void setSowPurchaseCount(Double sowPurchaseCount) {
    this.sowPurchaseCount = sowPurchaseCount;
  }

  public Double getHeaviestHogFmvPrice() {
    return heaviestHogFmvPrice;
  }

  public void setHeaviestHogFmvPrice(Double heaviestHogFmvPrice) {
    this.heaviestHogFmvPrice = heaviestHogFmvPrice;
  }

  public Double getBoarPurchaseExpense() {
    return boarPurchaseExpense;
  }

  public void setBoarPurchaseExpense(Double boarPurchaseExpense) {
    this.boarPurchaseExpense = boarPurchaseExpense;
  }

  public Double getSowPurchaseExpense() {
    return sowPurchaseExpense;
  }

  public void setSowPurchaseExpense(Double sowPurchaseExpense) {
    this.sowPurchaseExpense = sowPurchaseExpense;
  }

  public Boolean getHasHogs() {
    return hasHogs;
  }

  public void setHasHogs(Boolean hasHogs) {
    this.hasHogs = hasHogs;
  }

  public Boolean getFarrowToFinishOperation() {
    return farrowToFinishOperation;
  }

  public void setFarrowToFinishOperation(Boolean farrowToFinishOperation) {
    this.farrowToFinishOperation = farrowToFinishOperation;
  }

  public Boolean getFeederOperation() {
    return feederOperation;
  }

  public void setFeederOperation(Boolean feederOperation) {
    this.feederOperation = feederOperation;
  }

  public Double getBoarPurchasePrice() {
    return boarPurchasePrice;
  }

  public void setBoarPurchasePrice(Double boarPurchasePrice) {
    this.boarPurchasePrice = boarPurchasePrice;
  }

  public Double getSowPurchasePrice() {
    return sowPurchasePrice;
  }

  public void setSowPurchasePrice(Double sowPurchasePrice) {
    this.sowPurchasePrice = sowPurchasePrice;
  }

  public Double getWeanlingPurchaseCount() {
    return weanlingPurchaseCount;
  }

  public void setWeanlingPurchaseCount(Double weanlingPurchaseCount) {
    this.weanlingPurchaseCount = weanlingPurchaseCount;
  }

  public Double getWeanlingPurchasePrice() {
    return weanlingPurchasePrice;
  }

  public void setWeanlingPurchasePrice(Double weanlingPurchasePrice) {
    this.weanlingPurchasePrice = weanlingPurchasePrice;
  }

  public Double getWeanlingPurchaseExpense() {
    return weanlingPurchaseExpense;
  }

  public void setWeanlingPurchaseExpense(Double weanlingPurchaseExpense) {
    this.weanlingPurchaseExpense = weanlingPurchaseExpense;
  }

  public Boolean getHogsPass() {
    return hogsPass;
  }

  public void setHogsPass(Boolean hogsPass) {
    this.hogsPass = hogsPass;
  }

  public Double getReportedExpenses() {
    return reportedExpenses;
  }

  public void setReportedExpenses(Double reportedExpenses) {
    this.reportedExpenses = reportedExpenses;
  }

  public Double getBirthCyclesPerYear() {
    return birthCyclesPerYear;
  }

  public void setBirthCyclesPerYear(Double birthCyclesPerYear) {
    this.birthCyclesPerYear = birthCyclesPerYear;
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

  public Double getExpectedSold() {
    return expectedSold;
  }

  public void setExpectedSold(Double expectedSold) {
    this.expectedSold = expectedSold;
  }

  public Double getVarianceLimit() {
    return varianceLimit;
  }

  public void setVarianceLimit(Double varianceLimit) {
    this.varianceLimit = varianceLimit;
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

  public Double getRevenueVariance() {
    return revenueVariance;
  }

  public void setRevenueVariance(Double revenueVariance) {
    this.revenueVariance = revenueVariance;
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

  public void copy(HogsRevenueRiskSubTestResult o) {

    hasHogs = o.hasHogs;
    hogsPass = o.hogsPass;
    farrowToFinishOperation = o.farrowToFinishOperation;
    feederOperation = o.feederOperation;
    reportedExpenses = o.reportedExpenses;
    totalQuantityStart = o.totalQuantityStart;
    totalQuantityEnd = o.totalQuantityEnd;
    sowsBreeding = o.sowsBreeding;
    birthsPerCycle = o.birthsPerCycle;
    birthCyclesPerYear = o.birthCyclesPerYear;
    totalBirthsPerCycle = o.totalBirthsPerCycle;
    totalBirthsAllCycles = o.totalBirthsAllCycles;
    deathRate = o.deathRate;
    feederProductiveUnits = o.feederProductiveUnits;
    boarPurchaseCount = o.boarPurchaseCount;
    boarPurchasePrice = o.boarPurchasePrice;
    boarPurchaseExpense = o.boarPurchaseExpense;
    sowPurchaseCount = o.sowPurchaseCount;
    sowPurchasePrice = o.sowPurchasePrice;
    sowPurchaseExpense = o.sowPurchaseExpense;
    totalPurchaseCount = o.totalPurchaseCount;
    expectedSold = o.expectedSold;
    heaviestHogFmvPrice = o.heaviestHogFmvPrice;
    weanlingPurchaseCount = o.weanlingPurchaseCount;
    weanlingPurchasePrice = o.weanlingPurchasePrice;
    weanlingPurchaseExpense = o.weanlingPurchaseExpense;
    deaths = o.deaths;
    reportedRevenue = o.reportedRevenue;
    expectedRevenue = o.expectedRevenue;
    revenueVariance = o.revenueVariance;
    varianceLimit = o.varianceLimit;
    
    getInventory().clear();
    getInventory().addAll(o.getInventory());
  }

}
