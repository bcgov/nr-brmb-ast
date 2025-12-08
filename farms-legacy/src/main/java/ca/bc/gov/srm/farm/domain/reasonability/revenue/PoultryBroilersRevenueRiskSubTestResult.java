package ca.bc.gov.srm.farm.domain.reasonability.revenue;

import java.io.Serializable;

public class PoultryBroilersRevenueRiskSubTestResult implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  private Boolean hasPoultryBroilers;
  private Boolean subTestPass;
  
  private Boolean hasChickens;
  private Double chickenKgProduced;
  private Double chickenAverageWeightKg;
  private Double chickenExpectedSoldCount;
  private Double chickenPricePerBird;
  private Double chickenExpectedRevenue;
  private Double chickenReportedRevenue;
  private Double chickenVariance;
  private Double chickenVarianceLimit;
  private Boolean chickenPass;
  
  private Boolean hasTurkeys;
  private Double turkeyKgProduced;
  private Double turkeyAverageWeightKg;
  private Double turkeyExpectedSoldCount;
  private Double turkeyPricePerBird;
  private Double turkeyExpectedRevenue;
  private Double turkeyReportedRevenue;
  private Double turkeyVariance;
  private Double turkeyVarianceLimit;
  private Boolean turkeyPass;

  public Boolean getHasChickens() {
    return hasChickens;
  }

  public void setHasChickens(Boolean hasChickens) {
    this.hasChickens = hasChickens;
  }

  public Double getChickenAverageWeightKg() {
    return chickenAverageWeightKg;
  }

  public void setChickenAverageWeightKg(Double chickenAverageWeightKg) {
    this.chickenAverageWeightKg = chickenAverageWeightKg;
  }

  public Boolean getSubTestPass() {
    return subTestPass;
  }

  public void setSubTestPass(Boolean subTestPass) {
    this.subTestPass = subTestPass;
  }

  public Boolean getHasPoultryBroilers() {
    return hasPoultryBroilers;
  }

  public void setHasPoultryBroilers(Boolean hasPoultryBroilers) {
    this.hasPoultryBroilers = hasPoultryBroilers;
  }

  public Double getChickenKgProduced() {
    return chickenKgProduced;
  }

  public void setChickenKgProduced(Double chickenKgProduced) {
    this.chickenKgProduced = chickenKgProduced;
  }

  public Double getChickenExpectedSoldCount() {
    return chickenExpectedSoldCount;
  }

  public void setChickenExpectedSoldCount(Double chickenExpectedSoldCount) {
    this.chickenExpectedSoldCount = chickenExpectedSoldCount;
  }

  public Double getChickenPricePerBird() {
    return chickenPricePerBird;
  }

  public void setChickenPricePerBird(Double chickenPricePerBird) {
    this.chickenPricePerBird = chickenPricePerBird;
  }

  public Double getChickenExpectedRevenue() {
    return chickenExpectedRevenue;
  }

  public void setChickenExpectedRevenue(Double chickenExpectedRevenue) {
    this.chickenExpectedRevenue = chickenExpectedRevenue;
  }

  public Double getChickenReportedRevenue() {
    return chickenReportedRevenue;
  }

  public void setChickenReportedRevenue(Double chickenReportedRevenue) {
    this.chickenReportedRevenue = chickenReportedRevenue;
  }

  public Double getChickenVariance() {
    return chickenVariance;
  }

  public void setChickenVariance(Double chickenVariance) {
    this.chickenVariance = chickenVariance;
  }

  public Double getChickenVarianceLimit() {
    return chickenVarianceLimit;
  }

  public void setChickenVarianceLimit(Double chickenVarianceLimit) {
    this.chickenVarianceLimit = chickenVarianceLimit;
  }

  public Boolean getChickenPass() {
    return chickenPass;
  }

  public void setChickenPass(Boolean chickenPass) {
    this.chickenPass = chickenPass;
  }

  public Boolean getHasTurkeys() {
    return hasTurkeys;
  }

  public void setHasTurkeys(Boolean hasTurkeys) {
    this.hasTurkeys = hasTurkeys;
  }

  public Double getTurkeyKgProduced() {
    return turkeyKgProduced;
  }

  public void setTurkeyKgProduced(Double turkeyKgProduced) {
    this.turkeyKgProduced = turkeyKgProduced;
  }

  public Double getTurkeyAverageWeightKg() {
    return turkeyAverageWeightKg;
  }

  public void setTurkeyAverageWeightKg(Double turkeyAverageWeightKg) {
    this.turkeyAverageWeightKg = turkeyAverageWeightKg;
  }

  public Double getTurkeyExpectedSoldCount() {
    return turkeyExpectedSoldCount;
  }

  public void setTurkeyExpectedSoldCount(Double turkeyExpectedSoldCount) {
    this.turkeyExpectedSoldCount = turkeyExpectedSoldCount;
  }

  public Double getTurkeyPricePerBird() {
    return turkeyPricePerBird;
  }

  public void setTurkeyPricePerBird(Double turkeyPricePerBird) {
    this.turkeyPricePerBird = turkeyPricePerBird;
  }

  public Double getTurkeyExpectedRevenue() {
    return turkeyExpectedRevenue;
  }

  public void setTurkeyExpectedRevenue(Double turkeyExpectedRevenue) {
    this.turkeyExpectedRevenue = turkeyExpectedRevenue;
  }

  public Double getTurkeyReportedRevenue() {
    return turkeyReportedRevenue;
  }

  public void setTurkeyReportedRevenue(Double turkeyReportedRevenue) {
    this.turkeyReportedRevenue = turkeyReportedRevenue;
  }

  public Double getTurkeyVariance() {
    return turkeyVariance;
  }

  public void setTurkeyVariance(Double turkeyVariance) {
    this.turkeyVariance = turkeyVariance;
  }

  public Double getTurkeyVarianceLimit() {
    return turkeyVarianceLimit;
  }

  public void setTurkeyVarianceLimit(Double turkeyVarianceLimit) {
    this.turkeyVarianceLimit = turkeyVarianceLimit;
  }

  public Boolean getTurkeyPass() {
    return turkeyPass;
  }

  public void setTurkeyPass(Boolean turkeyPass) {
    this.turkeyPass = turkeyPass;
  }

  public void copy(PoultryBroilersRevenueRiskSubTestResult o) {

    hasPoultryBroilers = o.hasPoultryBroilers;
    subTestPass = o.subTestPass;
    
    hasChickens = o.hasChickens;
    chickenKgProduced = o.chickenKgProduced;
    chickenAverageWeightKg = o.chickenAverageWeightKg;
    chickenExpectedSoldCount = o.chickenExpectedSoldCount;
    chickenPricePerBird = o.chickenPricePerBird;
    chickenExpectedRevenue = o.chickenExpectedRevenue;
    chickenReportedRevenue = o.chickenReportedRevenue;
    chickenVariance = o.chickenVariance;
    chickenVarianceLimit = o.chickenVarianceLimit;
    chickenPass = o.chickenPass;

    hasTurkeys = o.hasTurkeys;
    turkeyKgProduced = o.turkeyKgProduced;
    turkeyAverageWeightKg = o.turkeyAverageWeightKg;
    turkeyExpectedSoldCount = o.turkeyExpectedSoldCount;
    turkeyPricePerBird = o.turkeyPricePerBird;
    turkeyExpectedRevenue = o.turkeyExpectedRevenue;
    turkeyReportedRevenue = o.turkeyReportedRevenue;
    turkeyVariance = o.turkeyVariance;
    turkeyVarianceLimit = o.turkeyVarianceLimit;
    turkeyPass = o.turkeyPass;
  }

}
