package ca.bc.gov.srm.farm.domain.reasonability.revenue;

import java.io.Serializable;

public class PoultryEggsRevenueRiskSubTestResult implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  private Boolean hasPoultryEggs;
  private Boolean subTestPass;
  
  private Double consumptionLayers; // Sum of all the PUs
  private Double consumptionAverageEggsPerLayer;
  private Double consumptionEggsTotal;
  private Double consumptionEggsDozen;
  private Double consumptionEggsDozenPrice;
  private Double consumptionExpectedRevenue;
  private Double consumptionReportedRevenue;
  private Double consumptionVariance;
  private Double consumptionVarianceLimit;
  private Boolean consumptionPass;
  
  private Double hatchingLayers; // Sum of all the PUs
  private Double hatchingAverageEggsPerLayer;
  private Double hatchingEggsTotal;
  private Double hatchingEggsDozen;
  private Double hatchingEggsDozenPrice;
  private Double hatchingExpectedRevenue;
  private Double hatchingReportedRevenue;
  private Double hatchingVariance;
  private Double hatchingVarianceLimit;
  private Boolean hatchingPass;

  public Boolean getHasPoultryEggs() {
    return hasPoultryEggs;
  }

  public void setHasPoultryEggs(Boolean hasPoultryEggs) {
    this.hasPoultryEggs = hasPoultryEggs;
  }

  public Boolean getSubTestPass() {
    return subTestPass;
  }

  public void setSubTestPass(Boolean subTestPass) {
    this.subTestPass = subTestPass;
  }

  public Double getConsumptionLayers() {
    return consumptionLayers;
  }

  public void setConsumptionLayers(Double consumptionLayers) {
    this.consumptionLayers = consumptionLayers;
  }

  public Double getConsumptionAverageEggsPerLayer() {
    return consumptionAverageEggsPerLayer;
  }

  public void setConsumptionAverageEggsPerLayer(Double consumptionAverageEggsPerLayer) {
    this.consumptionAverageEggsPerLayer = consumptionAverageEggsPerLayer;
  }

  public Double getConsumptionEggsTotal() {
    return consumptionEggsTotal;
  }

  public void setConsumptionEggsTotal(Double consumptionEggsTotal) {
    this.consumptionEggsTotal = consumptionEggsTotal;
  }

  public Double getConsumptionEggsDozen() {
    return consumptionEggsDozen;
  }

  public void setConsumptionEggsDozen(Double consumptionEggsDozen) {
    this.consumptionEggsDozen = consumptionEggsDozen;
  }

  public Double getConsumptionEggsDozenPrice() {
    return consumptionEggsDozenPrice;
  }

  public void setConsumptionEggsDozenPrice(Double consumptionEggsDozenPrice) {
    this.consumptionEggsDozenPrice = consumptionEggsDozenPrice;
  }

  public Double getConsumptionExpectedRevenue() {
    return consumptionExpectedRevenue;
  }

  public void setConsumptionExpectedRevenue(Double consumptionExpectedRevenue) {
    this.consumptionExpectedRevenue = consumptionExpectedRevenue;
  }

  public Double getConsumptionReportedRevenue() {
    return consumptionReportedRevenue;
  }

  public void setConsumptionReportedRevenue(Double consumptionReportedRevenue) {
    this.consumptionReportedRevenue = consumptionReportedRevenue;
  }

  public Double getConsumptionVariance() {
    return consumptionVariance;
  }

  public void setConsumptionVariance(Double consumptionVariance) {
    this.consumptionVariance = consumptionVariance;
  }

  public Double getConsumptionVarianceLimit() {
    return consumptionVarianceLimit;
  }

  public void setConsumptionVarianceLimit(Double consumptionVarianceLimit) {
    this.consumptionVarianceLimit = consumptionVarianceLimit;
  }

  public Boolean getConsumptionPass() {
    return consumptionPass;
  }

  public void setConsumptionPass(Boolean consumptionPass) {
    this.consumptionPass = consumptionPass;
  }

  public Double getHatchingLayers() {
    return hatchingLayers;
  }

  public void setHatchingLayers(Double hatchingLayers) {
    this.hatchingLayers = hatchingLayers;
  }

  public Double getHatchingAverageEggsPerLayer() {
    return hatchingAverageEggsPerLayer;
  }

  public void setHatchingAverageEggsPerLayer(Double hatchingAverageEggsPerLayer) {
    this.hatchingAverageEggsPerLayer = hatchingAverageEggsPerLayer;
  }

  public Double getHatchingEggsTotal() {
    return hatchingEggsTotal;
  }

  public void setHatchingEggsTotal(Double hatchingEggsTotal) {
    this.hatchingEggsTotal = hatchingEggsTotal;
  }

  public Double getHatchingEggsDozen() {
    return hatchingEggsDozen;
  }

  public void setHatchingEggsDozen(Double hatchingEggsDozen) {
    this.hatchingEggsDozen = hatchingEggsDozen;
  }

  public Double getHatchingEggsDozenPrice() {
    return hatchingEggsDozenPrice;
  }

  public void setHatchingEggsDozenPrice(Double hatchingEggsDozenPrice) {
    this.hatchingEggsDozenPrice = hatchingEggsDozenPrice;
  }

  public Double getHatchingExpectedRevenue() {
    return hatchingExpectedRevenue;
  }

  public void setHatchingExpectedRevenue(Double hatchingExpectedRevenue) {
    this.hatchingExpectedRevenue = hatchingExpectedRevenue;
  }

  public Double getHatchingReportedRevenue() {
    return hatchingReportedRevenue;
  }

  public void setHatchingReportedRevenue(Double hatchingReportedRevenue) {
    this.hatchingReportedRevenue = hatchingReportedRevenue;
  }

  public Double getHatchingVariance() {
    return hatchingVariance;
  }

  public void setHatchingVariance(Double hatchingVariance) {
    this.hatchingVariance = hatchingVariance;
  }

  public Double getHatchingVarianceLimit() {
    return hatchingVarianceLimit;
  }

  public void setHatchingVarianceLimit(Double hatchingVarianceLimit) {
    this.hatchingVarianceLimit = hatchingVarianceLimit;
  }

  public Boolean getHatchingPass() {
    return hatchingPass;
  }

  public void setHatchingPass(Boolean hatchingPass) {
    this.hatchingPass = hatchingPass;
  }

  public void copy(PoultryEggsRevenueRiskSubTestResult o) {

    hasPoultryEggs = o.hasPoultryEggs;
    subTestPass = o.subTestPass;
    
    consumptionLayers = o.consumptionLayers;
    consumptionAverageEggsPerLayer = o.consumptionAverageEggsPerLayer;
    consumptionEggsTotal = o.consumptionEggsTotal;
    consumptionEggsDozen = o.consumptionEggsDozen;
    consumptionEggsDozenPrice = o.consumptionEggsDozenPrice;
    consumptionExpectedRevenue = o.consumptionExpectedRevenue;
    consumptionReportedRevenue = o.consumptionReportedRevenue;
    consumptionVariance = o.consumptionVariance;
    consumptionVarianceLimit = o.consumptionVarianceLimit;
    consumptionPass = o.consumptionPass;
    
    hatchingLayers = o.hatchingLayers;
    hatchingAverageEggsPerLayer = o.hatchingAverageEggsPerLayer;
    hatchingEggsTotal = o.hatchingEggsTotal;
    hatchingEggsDozen = o.hatchingEggsDozen;
    hatchingEggsDozenPrice = o.hatchingEggsDozenPrice;
    hatchingExpectedRevenue = o.hatchingExpectedRevenue;
    hatchingReportedRevenue = o.hatchingReportedRevenue;
    hatchingVariance = o.hatchingVariance;
    hatchingVarianceLimit = o.hatchingVarianceLimit;
    hatchingPass = o.hatchingPass;
  }

}
