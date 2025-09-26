/**
 * Copyright (c) 2018,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain.reasonability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonBackReference;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.CommodityTypeCodes;

/**
 * @author awilkinson
 */
public class BenefitRiskAssessmentTestResult extends ReasonabilityTestResult {

  private static final long serialVersionUID = 1L;

  public static final String NAME = "BENEFIT RISK";
  
  private List<BenefitRiskProductiveUnit> benefitRiskProductiveUnits;
  
  private Map<Integer, BenefitRiskCombinedFarmResult> combinedFarmResults;
  
  private Double programYearMargin;
  private Double totalBenefit;
  private Double benefitBenchmarkBeforeCombinedFarmPercent;
  private Double combinedFarmPercent;
  private Double benefitBenchmark;
  private Double benchmarkMargin;
  private Double benefitRiskDeductible;
  private Double benefitRiskPayoutLevel;
  private Double benefitBenchmarkLessDeductible;
  private Double benefitBenchmarkLessProgramYearMargin;
  private Double variance;
  private Double varianceLimit;

  /** back-reference to the object containing this */
  @JsonBackReference
  private ReasonabilityTestResults reasonabilityTestResults;

  public List<BenefitRiskProductiveUnit> getBenefitRiskProductiveUnits() {
    if(benefitRiskProductiveUnits == null) {
      benefitRiskProductiveUnits = new ArrayList<>();
    }
    return benefitRiskProductiveUnits;
  }

  public void setBenefitRiskProductiveUnits(List<BenefitRiskProductiveUnit> benefitRiskProductiveUnits) {
    this.benefitRiskProductiveUnits = benefitRiskProductiveUnits;
  }

  public Map<Integer, BenefitRiskCombinedFarmResult> getCombinedFarmResults() {
    if(combinedFarmResults == null) {
      combinedFarmResults = new HashMap<>();
    }
    return combinedFarmResults;
  }

  public void setCombinedFarmResults(Map<Integer, BenefitRiskCombinedFarmResult> combinedFarmResults) {
    this.combinedFarmResults = combinedFarmResults;
  }

  public Double getProgramYearMargin() {
    return programYearMargin;
  }

  public void setProgramYearMargin(Double programYearMargin) {
    this.programYearMargin = programYearMargin;
  }

  public Double getTotalBenefit() {
    return totalBenefit;
  }

  public void setTotalBenefit(Double totalBenefit) {
    this.totalBenefit = totalBenefit;
  }

  public Double getBenefitBenchmark() {
    return benefitBenchmark;
  }

  public void setBenefitBenchmark(Double benefitBenchmark) {
    this.benefitBenchmark = benefitBenchmark;
  }

  public Double getVariance() {
    return variance;
  }

  public void setVariance(Double variance) {
    this.variance = variance;
  }
  
  public Double getVarianceLimit() {
    return varianceLimit;
  }

  public void setVarianceLimit(Double varianceLimit) {
    this.varianceLimit = varianceLimit;
  }

  public Double getBenchmarkMargin() {
    return benchmarkMargin;
  }

  public void setBenchmarkMargin(Double benchmarkMargin) {
    this.benchmarkMargin = benchmarkMargin;
  }

  public Double getBenefitBenchmarkLessDeductible() {
    return benefitBenchmarkLessDeductible;
  }

  public void setBenefitBenchmarkLessDeductible(Double benefitBenchmarkLessDeductible) {
    this.benefitBenchmarkLessDeductible = benefitBenchmarkLessDeductible;
  }

  public Double getBenefitBenchmarkLessProgramYearMargin() {
    return benefitBenchmarkLessProgramYearMargin;
  }

  public void setBenefitBenchmarkLessProgramYearMargin(Double benefitBenchmarkLessProgramYearMargin) {
    this.benefitBenchmarkLessProgramYearMargin = benefitBenchmarkLessProgramYearMargin;
  }

  public Double getBenefitRiskDeductible() {
    return benefitRiskDeductible;
  }

  public void setBenefitRiskDeductible(Double benefitRiskDeductible) {
    this.benefitRiskDeductible = benefitRiskDeductible;
  }

  public Double getBenefitRiskPayoutLevel() {
    return benefitRiskPayoutLevel;
  }

  public void setBenefitRiskPayoutLevel(Double benefitRiskPayoutLevel) {
    this.benefitRiskPayoutLevel = benefitRiskPayoutLevel;
  }

  public Double getBenefitBenchmarkBeforeCombinedFarmPercent() {
    return benefitBenchmarkBeforeCombinedFarmPercent;
  }

  public void setBenefitBenchmarkBeforeCombinedFarmPercent(Double benefitBenchmarkBeforeCombinedFarmPercent) {
    this.benefitBenchmarkBeforeCombinedFarmPercent = benefitBenchmarkBeforeCombinedFarmPercent;
  }

  public ReasonabilityTestResults getReasonabilityTestResults() {
    return reasonabilityTestResults;
  }

  public void setReasonabilityTestResults(ReasonabilityTestResults reasonabilityTestResults) {
    this.reasonabilityTestResults = reasonabilityTestResults;
  }

  public Double getCombinedFarmPercent() {
    return combinedFarmPercent;
  }

  public void setCombinedFarmPercent(Double combinedFarmPercent) {
    this.combinedFarmPercent = combinedFarmPercent;
  }
  
  public boolean isForageProductiveUnitsConsumed() {
    boolean result = false;
    
    if(!result) {
      for(BenefitRiskProductiveUnit item : getBenefitRiskProductiveUnits()) {
        if(CommodityTypeCodes.FORAGE.equals(item.getCommodityTypeCode())
            && item.getConsumedProductiveCapacityAmount() != null
            && item.getConsumedProductiveCapacityAmount() > 0) {
          result = true;
          break;
        }
      }
    }
    
    return result;
  }
  
  /**
   *  This setter is here to make sure this is treated
   *  as a property and serialized to JSON
   */
  @SuppressWarnings("unused")
  public void setForageInventoryConsumed(boolean hasForage) {
    // do nothing
  }

  @Override
  public String toString() {
    return "BenefitRiskAssessmentTestResult [\n"
        + "\t benefitRiskProductiveUnits=" + benefitRiskProductiveUnits + "\n"
        + "\t programYearMargin=" + programYearMargin + "\n"
        + "\t totalBenefit=" + totalBenefit + "\n"
        + "\t benefitBenchmark=" + benefitBenchmark + "\n"
        + "\t benefitRiskDeductible=" + benefitRiskDeductible + "\n"
        + "\t benefitRiskPayoutLevel=" + benefitRiskPayoutLevel + "\n"
        + "\t benefitBenchmarkLessDeductible=" + benefitBenchmarkLessDeductible + "\n"
        + "\t benefitBenchmarkLessProgramYearMargin=" + benefitBenchmarkLessProgramYearMargin + "\n"
        + "\t benefitBenchmarkBeforeCombinedFarmPercent=" + benefitBenchmarkBeforeCombinedFarmPercent + "\n"
        + "\t combinedFarmPercent=" + combinedFarmPercent + "\n"
        + "\t variance=" + variance + "\n"
        + "\t varianceLimit=" + varianceLimit + "]";
  }
  
  public void copy(BenefitRiskAssessmentTestResult o) {
    super.copy(o);
    
    getBenefitRiskProductiveUnits().clear();
    getBenefitRiskProductiveUnits().addAll(o.getBenefitRiskProductiveUnits());
    
    getCombinedFarmResults().clear();
    getCombinedFarmResults().putAll(o.getCombinedFarmResults());
    
    programYearMargin = o.programYearMargin;
    totalBenefit = o.totalBenefit;
    benefitBenchmark = o.benefitBenchmark;
    benchmarkMargin = o.benchmarkMargin;
    benefitRiskDeductible = o.benefitRiskDeductible;
    benefitRiskPayoutLevel = o.benefitRiskPayoutLevel;
    benefitBenchmarkLessDeductible = o.benefitBenchmarkLessDeductible;
    benefitBenchmarkLessProgramYearMargin = o.benefitBenchmarkLessProgramYearMargin;
    benefitBenchmarkBeforeCombinedFarmPercent = o.benefitBenchmarkBeforeCombinedFarmPercent;
    variance = o.variance;
    varianceLimit = o.varianceLimit;
    
    Scenario scenario = reasonabilityTestResults.getScenario();
    if(scenario.isInCombinedFarm()) {
      Integer participantPin = scenario.getClient().getParticipantPin();
      BenefitRiskCombinedFarmResult combinedFarmResult = getCombinedFarmResults().get(participantPin);
      
      totalBenefit = combinedFarmResult.getTotalBenefit();
      benefitBenchmark = combinedFarmResult.getBenefitBenchmark();
      combinedFarmPercent = combinedFarmResult.getCombinedFarmPercent();
    }
  }

  @Override
  public String getName() {
    return NAME;
  }
}
