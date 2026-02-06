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
package ca.bc.gov.srm.farm.domain.enrolment;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import ca.bc.gov.srm.farm.domain.Scenario;

/**
 * @author awilkinson
 */
public class EnwEnrolment implements Serializable {

  private static final long serialVersionUID = 1L;
  
  public static final String CALCULATION_TYPE_BENEFIT_MARGINS = "BENEFIT";
  public static final String CALCULATION_TYPE_PROXY_MARGINS = "PROXY";
  public static final String CALCULATION_TYPE_MANUAL_MARGINS = "MANUAL";
  
  /** back-reference to the object containing this */
  @JsonBackReference
  private Scenario scenario;
   
  private Integer enwEnrolmentId;
  
  private Integer enrolmentYear;
  
  private Double enrolmentFee;
  private Double contributionMargin;
  
  private Double marginYearMinus2;
  private Double marginYearMinus3;
  private Double marginYearMinus4;
  private Double marginYearMinus5;
  private Double marginYearMinus6;

  private Boolean marginYearMinus2Used;
  private Boolean marginYearMinus3Used;
  private Boolean marginYearMinus4Used;
  private Boolean marginYearMinus5Used;
  private Boolean marginYearMinus6Used;
  
  private Double benefitEnrolmentFee;
  private Double benefitContributionMargin;
  
  private Double proxyEnrolmentFee;
  private Double proxyContributionMargin;
  
  private Double manualEnrolmentFee;
  private Double manualContributionMargin;
  
  private Double benefitMarginYearMinus2;
  private Double benefitMarginYearMinus3;
  private Double benefitMarginYearMinus4;
  private Double benefitMarginYearMinus5;
  private Double benefitMarginYearMinus6;

  private Boolean benefitMarginYearMinus2Used;
  private Boolean benefitMarginYearMinus3Used;
  private Boolean benefitMarginYearMinus4Used;
  private Boolean benefitMarginYearMinus5Used;
  private Boolean benefitMarginYearMinus6Used;
  
  private Double proxyMarginYearMinus2;
  private Double proxyMarginYearMinus3;
  private Double proxyMarginYearMinus4;
  
  private Double manualMarginYearMinus2;
  private Double manualMarginYearMinus3;
  private Double manualMarginYearMinus4;
  
  private String enrolmentCalculationTypeCode;
  
  private Double combinedFarmPercent;
  
  private Boolean benefitCalculated;
  
  private Boolean proxyMarginsCalculated;
  
  private Boolean manualMarginsCalculated;
  
  private Boolean hasProductiveUnits;
  
  private Boolean hasBpus;
  
  private Integer revisionCount;

  //---------------------------------------------------------
  // Fields below are not persisted
  //---------------------------------------------------------
  
  private Integer programYear;
  
  private Double enrolmentFeeCalculationFactor;
  
  private Double enrolmentFeeMinimum;
  
  private List<Integer> benefitMarginYears;
  
  private Double productiveValueYearMinus2;
  private Double productiveValueYearMinus3;
  private Double productiveValueYearMinus4;
  
  private List<Integer> proxyMarginYears;
  
  private List<Double> proxyMargins;
  
  private List<EnwProductiveUnit> enwProductiveUnits;
  
  private Boolean inCombinedFarm;
  
  public Integer getEnwEnrolmentId() {
    return enwEnrolmentId;
  }

  public void setEnwEnrolmentId(Integer enwEnrolmentId) {
    this.enwEnrolmentId = enwEnrolmentId;
  }

  public Integer getProgramYear() {
    return programYear;
  }

  public void setProgramYear(Integer programYear) {
    this.programYear = programYear;
  }

  public Double getContributionMargin() {
    return contributionMargin;
  }

  public void setContributionMargin(Double contributionMargin) {
    this.contributionMargin = contributionMargin;
  }

  public List<Integer> getBenefitMarginYears() {
    return benefitMarginYears;
  }

  public void setBenefitMarginYears(List<Integer> benefitMarginYears) {
    this.benefitMarginYears = benefitMarginYears;
  }

  public List<Integer> getProxyMarginYears() {
    return proxyMarginYears;
  }

  public void setProxyMarginYears(List<Integer> proxyMarginYears) {
    this.proxyMarginYears = proxyMarginYears;
  }

  public List<Double> getProxyMargins() {
    return proxyMargins;
  }

  public void setProxyMargins(List<Double> proxyMargins) {
    this.proxyMargins = proxyMargins;
  }

  public Scenario getScenario() {
    return scenario;
  }

  public void setScenario(Scenario scenario) {
    this.scenario = scenario;
  }

  public List<EnwProductiveUnit> getEnwProductiveUnits() {
    return enwProductiveUnits;
  }

  public void setEnwProductiveUnits(List<EnwProductiveUnit> enwProductiveUnits) {
    this.enwProductiveUnits = enwProductiveUnits;
  }

  public Integer getEnrolmentYear() {
    return enrolmentYear;
  }

  public void setEnrolmentYear(Integer enrolmentYear) {
    this.enrolmentYear = enrolmentYear;
  }

  public Double getEnrolmentFee() {
    return enrolmentFee;
  }

  public void setEnrolmentFee(Double enrolmentFee) {
    this.enrolmentFee = enrolmentFee;
  }

  public Double getBenefitEnrolmentFee() {
    return benefitEnrolmentFee;
  }

  public void setBenefitEnrolmentFee(Double benefitEnrolmentFee) {
    this.benefitEnrolmentFee = benefitEnrolmentFee;
  }

  public Double getManualEnrolmentFee() {
    return manualEnrolmentFee;
  }

  public void setManualEnrolmentFee(Double manualEnrolmentFee) {
    this.manualEnrolmentFee = manualEnrolmentFee;
  }

  public Double getBenefitContributionMargin() {
    return benefitContributionMargin;
  }

  public void setBenefitContributionMargin(Double benefitContributionMargin) {
    this.benefitContributionMargin = benefitContributionMargin;
  }

  public Double getProxyContributionMargin() {
    return proxyContributionMargin;
  }

  public void setProxyContributionMargin(Double proxyContributionMargin) {
    this.proxyContributionMargin = proxyContributionMargin;
  }

  public Double getBenefitMarginYearMinus2() {
    return benefitMarginYearMinus2;
  }

  public void setBenefitMarginYearMinus2(Double benefitMarginYearMinus2) {
    this.benefitMarginYearMinus2 = benefitMarginYearMinus2;
  }

  public Double getBenefitMarginYearMinus3() {
    return benefitMarginYearMinus3;
  }

  public void setBenefitMarginYearMinus3(Double benefitMarginYearMinus3) {
    this.benefitMarginYearMinus3 = benefitMarginYearMinus3;
  }

  public Double getBenefitMarginYearMinus4() {
    return benefitMarginYearMinus4;
  }

  public void setBenefitMarginYearMinus4(Double benefitMarginYearMinus4) {
    this.benefitMarginYearMinus4 = benefitMarginYearMinus4;
  }

  public Double getBenefitMarginYearMinus5() {
    return benefitMarginYearMinus5;
  }

  public void setBenefitMarginYearMinus5(Double benefitMarginYearMinus5) {
    this.benefitMarginYearMinus5 = benefitMarginYearMinus5;
  }

  public Double getBenefitMarginYearMinus6() {
    return benefitMarginYearMinus6;
  }

  public void setBenefitMarginYearMinus6(Double benefitMarginYearMinus6) {
    this.benefitMarginYearMinus6 = benefitMarginYearMinus6;
  }

  public Double getProxyMarginYearMinus2() {
    return proxyMarginYearMinus2;
  }

  public void setProxyMarginYearMinus2(Double proxyMarginYearMinus2) {
    this.proxyMarginYearMinus2 = proxyMarginYearMinus2;
  }

  public Double getProxyMarginYearMinus3() {
    return proxyMarginYearMinus3;
  }

  public void setProxyMarginYearMinus3(Double proxyMarginYearMinus3) {
    this.proxyMarginYearMinus3 = proxyMarginYearMinus3;
  }

  public Double getProxyMarginYearMinus4() {
    return proxyMarginYearMinus4;
  }

  public void setProxyMarginYearMinus4(Double proxyMarginYearMinus4) {
    this.proxyMarginYearMinus4 = proxyMarginYearMinus4;
  }

  public Double getCombinedFarmPercent() {
    return combinedFarmPercent;
  }

  public void setCombinedFarmPercent(Double combinedFarmPercent) {
    this.combinedFarmPercent = combinedFarmPercent;
  }

  public Double getProxyEnrolmentFee() {
    return proxyEnrolmentFee;
  }

  public void setProxyEnrolmentFee(Double proxyEnrolmentFee) {
    this.proxyEnrolmentFee = proxyEnrolmentFee;
  }

  public Double getManualContributionMargin() {
    return manualContributionMargin;
  }

  public void setManualContributionMargin(Double manualContributionMargin) {
    this.manualContributionMargin = manualContributionMargin;
  }

  public Double getManualMarginYearMinus2() {
    return manualMarginYearMinus2;
  }

  public void setManualMarginYearMinus2(Double manualMarginYearMinus2) {
    this.manualMarginYearMinus2 = manualMarginYearMinus2;
  }

  public Double getManualMarginYearMinus3() {
    return manualMarginYearMinus3;
  }

  public void setManualMarginYearMinus3(Double manualMarginYearMinus3) {
    this.manualMarginYearMinus3 = manualMarginYearMinus3;
  }

  public Double getManualMarginYearMinus4() {
    return manualMarginYearMinus4;
  }

  public void setManualMarginYearMinus4(Double manualMarginYearMinus4) {
    this.manualMarginYearMinus4 = manualMarginYearMinus4;
  }

  public String getEnrolmentCalculationTypeCode() {
    return enrolmentCalculationTypeCode;
  }

  public void setEnrolmentCalculationTypeCode(String enrolmentCalculationTypeCode) {
    this.enrolmentCalculationTypeCode = enrolmentCalculationTypeCode;
  }

  public Boolean getBenefitCalculated() {
    return benefitCalculated;
  }

  public void setBenefitCalculated(Boolean benefitCalculated) {
    this.benefitCalculated = benefitCalculated;
  }

  public Boolean getProxyMarginsCalculated() {
    return proxyMarginsCalculated;
  }

  public void setProxyMarginsCalculated(Boolean proxyMarginsCalculated) {
    this.proxyMarginsCalculated = proxyMarginsCalculated;
  }

  public Boolean getManualMarginsCalculated() {
    return manualMarginsCalculated;
  }

  public void setManualMarginsCalculated(Boolean manualMarginsCalculated) {
    this.manualMarginsCalculated = manualMarginsCalculated;
  }

  public Boolean getHasProductiveUnits() {
    return hasProductiveUnits;
  }

  public void setHasProductiveUnits(Boolean hasProductiveUnits) {
    this.hasProductiveUnits = hasProductiveUnits;
  }

  public Boolean getHasBpus() {
    return hasBpus;
  }

  public void setHasBpus(Boolean hasBpus) {
    this.hasBpus = hasBpus;
  }

  public Boolean getBenefitMarginYearMinus2Used() {
    return benefitMarginYearMinus2Used;
  }

  public void setBenefitMarginYearMinus2Used(Boolean benefitMarginYearMinus2Used) {
    this.benefitMarginYearMinus2Used = benefitMarginYearMinus2Used;
  }

  public Boolean getBenefitMarginYearMinus3Used() {
    return benefitMarginYearMinus3Used;
  }

  public void setBenefitMarginYearMinus3Used(Boolean benefitMarginYearMinus3Used) {
    this.benefitMarginYearMinus3Used = benefitMarginYearMinus3Used;
  }

  public Boolean getBenefitMarginYearMinus4Used() {
    return benefitMarginYearMinus4Used;
  }

  public void setBenefitMarginYearMinus4Used(Boolean benefitMarginYearMinus4Used) {
    this.benefitMarginYearMinus4Used = benefitMarginYearMinus4Used;
  }

  public Boolean getBenefitMarginYearMinus5Used() {
    return benefitMarginYearMinus5Used;
  }

  public void setBenefitMarginYearMinus5Used(Boolean benefitMarginYearMinus5Used) {
    this.benefitMarginYearMinus5Used = benefitMarginYearMinus5Used;
  }

  public Boolean getBenefitMarginYearMinus6Used() {
    return benefitMarginYearMinus6Used;
  }

  public void setBenefitMarginYearMinus6Used(Boolean benefitMarginYearMinus6Used) {
    this.benefitMarginYearMinus6Used = benefitMarginYearMinus6Used;
  }

  public Boolean getMarginYearMinus2Used() {
    return marginYearMinus2Used;
  }

  public void setMarginYearMinus2Used(Boolean marginYearMinus2Used) {
    this.marginYearMinus2Used = marginYearMinus2Used;
  }

  public Boolean getMarginYearMinus3Used() {
    return marginYearMinus3Used;
  }

  public void setMarginYearMinus3Used(Boolean marginYearMinus3Used) {
    this.marginYearMinus3Used = marginYearMinus3Used;
  }

  public Boolean getMarginYearMinus4Used() {
    return marginYearMinus4Used;
  }

  public void setMarginYearMinus4Used(Boolean marginYearMinus4Used) {
    this.marginYearMinus4Used = marginYearMinus4Used;
  }

  public Boolean getMarginYearMinus5Used() {
    return marginYearMinus5Used;
  }

  public void setMarginYearMinus5Used(Boolean marginYearMinus5Used) {
    this.marginYearMinus5Used = marginYearMinus5Used;
  }

  public Boolean getMarginYearMinus6Used() {
    return marginYearMinus6Used;
  }

  public void setMarginYearMinus6Used(Boolean marginYearMinus6Used) {
    this.marginYearMinus6Used = marginYearMinus6Used;
  }

  public Double getMarginYearMinus2() {
    return marginYearMinus2;
  }

  public void setMarginYearMinus2(Double marginYearMinus2) {
    this.marginYearMinus2 = marginYearMinus2;
  }

  public Double getMarginYearMinus3() {
    return marginYearMinus3;
  }

  public void setMarginYearMinus3(Double marginYearMinus3) {
    this.marginYearMinus3 = marginYearMinus3;
  }

  public Double getMarginYearMinus4() {
    return marginYearMinus4;
  }

  public void setMarginYearMinus4(Double marginYearMinus4) {
    this.marginYearMinus4 = marginYearMinus4;
  }

  public Double getMarginYearMinus5() {
    return marginYearMinus5;
  }

  public void setMarginYearMinus5(Double marginYearMinus5) {
    this.marginYearMinus5 = marginYearMinus5;
  }

  public Double getMarginYearMinus6() {
    return marginYearMinus6;
  }

  public void setMarginYearMinus6(Double marginYearMinus6) {
    this.marginYearMinus6 = marginYearMinus6;
  }

  public Integer getRevisionCount() {
    return revisionCount;
  }

  public void setRevisionCount(Integer revisionCount) {
    this.revisionCount = revisionCount;
  }

  public Double getEnrolmentFeeCalculationFactor() {
    return enrolmentFeeCalculationFactor;
  }

  public void setEnrolmentFeeCalculationFactor(Double enrolmentFeeCalculationFactor) {
    this.enrolmentFeeCalculationFactor = enrolmentFeeCalculationFactor;
  }

  public Double getEnrolmentFeeMinimum() {
    return enrolmentFeeMinimum;
  }

  public void setEnrolmentFeeMinimum(Double enrolmentFeeMinimum) {
    this.enrolmentFeeMinimum = enrolmentFeeMinimum;
  }

  public Boolean getInCombinedFarm() {
    return inCombinedFarm;
  }

  public void setInCombinedFarm(Boolean inCombinedFarm) {
    this.inCombinedFarm = inCombinedFarm;
  }

  public Double getProductiveValueYearMinus2() {
    return productiveValueYearMinus2;
  }

  public void setProductiveValueYearMinus2(Double productiveValueYearMinus2) {
    this.productiveValueYearMinus2 = productiveValueYearMinus2;
  }

  public Double getProductiveValueYearMinus3() {
    return productiveValueYearMinus3;
  }

  public void setProductiveValueYearMinus3(Double productiveValueYearMinus3) {
    this.productiveValueYearMinus3 = productiveValueYearMinus3;
  }

  public Double getProductiveValueYearMinus4() {
    return productiveValueYearMinus4;
  }

  public void setProductiveValueYearMinus4(Double productiveValueYearMinus4) {
    this.productiveValueYearMinus4 = productiveValueYearMinus4;
  }
  
  @JsonIgnore
  public Boolean getCanCalculateProxyMargins() {
    Boolean canCalculateProxyMargins = null;
    if(getHasBpus() != null && getHasProductiveUnits() != null) {
      canCalculateProxyMargins = getHasBpus() && getHasProductiveUnits();
    }
    return canCalculateProxyMargins;
  }

  @Override
  public String toString() {
    return "EnwEnrolment [enwEnrolmentId=" + enwEnrolmentId + ", enrolmentYear=" + enrolmentYear + ", enrolmentFee=" + enrolmentFee
        + ", contributionMargin=" + contributionMargin + ", marginYearMinus2=" + marginYearMinus2 + ", marginYearMinus3=" + marginYearMinus3
        + ", marginYearMinus4=" + marginYearMinus4 + ", marginYearMinus5=" + marginYearMinus5 + ", marginYearMinus6=" + marginYearMinus6
        + ", marginYearMinus2Used=" + marginYearMinus2Used + ", marginYearMinus3Used=" + marginYearMinus3Used + ", marginYearMinus4Used="
        + marginYearMinus4Used + ", marginYearMinus5Used=" + marginYearMinus5Used + ", marginYearMinus6Used=" + marginYearMinus6Used
        + ", benefitEnrolmentFee=" + benefitEnrolmentFee + ", benefitContributionMargin=" + benefitContributionMargin + ", proxyEnrolmentFee="
        + proxyEnrolmentFee + ", proxyContributionMargin=" + proxyContributionMargin + ", manualEnrolmentFee=" + manualEnrolmentFee
        + ", manualContributionMargin=" + manualContributionMargin + ", benefitMarginYearMinus2=" + benefitMarginYearMinus2
        + ", benefitMarginYearMinus3=" + benefitMarginYearMinus3 + ", benefitMarginYearMinus4=" + benefitMarginYearMinus4
        + ", benefitMarginYearMinus5=" + benefitMarginYearMinus5 + ", benefitMarginYearMinus6=" + benefitMarginYearMinus6
        + ", benefitMarginYearMinus2Used=" + benefitMarginYearMinus2Used + ", benefitMarginYearMinus3Used=" + benefitMarginYearMinus3Used
        + ", benefitMarginYearMinus4Used=" + benefitMarginYearMinus4Used + ", benefitMarginYearMinus5Used=" + benefitMarginYearMinus5Used
        + ", benefitMarginYearMinus6Used=" + benefitMarginYearMinus6Used + ", proxyMarginYearMinus2=" + proxyMarginYearMinus2
        + ", proxyMarginYearMinus3=" + proxyMarginYearMinus3 + ", proxyMarginYearMinus4=" + proxyMarginYearMinus4 + ", manualMarginYearMinus2="
        + manualMarginYearMinus2 + ", manualMarginYearMinus3=" + manualMarginYearMinus3 + ", manualMarginYearMinus4=" + manualMarginYearMinus4
        + ", enrolmentCalculationTypeCode=" + enrolmentCalculationTypeCode + ", combinedFarmPercent=" + combinedFarmPercent + ", benefitCalculated="
        + benefitCalculated + ", proxyMarginsCalculated=" + proxyMarginsCalculated + ", manualMarginsCalculated=" + manualMarginsCalculated
        + ", hasProductiveUnits=" + hasProductiveUnits + ", hasBpus=" + hasBpus + ", revisionCount=" + revisionCount + ", programYear=" + programYear
        + ", enrolmentFeeCalculationFactor=" + enrolmentFeeCalculationFactor + ", enrolmentFeeMinimum=" + enrolmentFeeMinimum
        + ", benefitMarginYears=" + benefitMarginYears + ", getCanCalculateProxyMargins=" + getCanCalculateProxyMargins() + ", productiveValueYearMinus2="
        + productiveValueYearMinus2 + ", productiveValueYearMinus3=" + productiveValueYearMinus3 + ", productiveValueYearMinus4="
        + productiveValueYearMinus4 + ", proxyMarginYears=" + proxyMarginYears + ", proxyMargins=" + proxyMargins + ", enwProductiveUnits="
        + enwProductiveUnits + ", inCombinedFarm=" + inCombinedFarm + "]";
  }
}
