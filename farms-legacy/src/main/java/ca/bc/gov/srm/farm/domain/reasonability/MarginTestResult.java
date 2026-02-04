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



/**
 * @author awilkinson
 */
public class MarginTestResult extends ReasonabilityTestResult {

  private static final long serialVersionUID = 1L;
  
  private Double programYearMargin;
  
  private Double adjustedReferenceMargin;
  private Double adjustedReferenceMarginVariance;
  private Double adjustedReferenceMarginVarianceLimit;
  private Boolean withinLimitOfReferenceMargin;
  
  private Boolean withinLimitOfIndustryAverage;
  private Double industryVarianceLimit;
  private Double industryAverage;
  private Double industryVariance;

  public Boolean getWithinLimitOfReferenceMargin() {
    return withinLimitOfReferenceMargin;
  }

  public void setWithinLimitOfReferenceMargin(Boolean withinLimitOfReferenceMargin) {
    this.withinLimitOfReferenceMargin = withinLimitOfReferenceMargin;
  }

  public Boolean getWithinLimitOfIndustryAverage() {
    return withinLimitOfIndustryAverage;
  }

  public void setWithinLimitOfIndustryAverage(Boolean withinLimitOfIndustryAverage) {
    this.withinLimitOfIndustryAverage = withinLimitOfIndustryAverage;
  }

  public Double getAdjustedReferenceMarginVarianceLimit() {
    return adjustedReferenceMarginVarianceLimit;
  }

  public void setAdjustedReferenceMarginVarianceLimit(Double adjustedReferenceMarginVarianceLimit) {
    this.adjustedReferenceMarginVarianceLimit = adjustedReferenceMarginVarianceLimit;
  }

  public Double getIndustryVarianceLimit() {
    return industryVarianceLimit;
  }

  public void setIndustryVarianceLimit(Double industryVarianceLimit) {
    this.industryVarianceLimit = industryVarianceLimit;
  }

  public Double getIndustryAverage() {
    return industryAverage;
  }

  public void setIndustryAverage(Double industryAverage) {
    this.industryAverage = industryAverage;
  }

  public Double getIndustryVariance() {
    return industryVariance;
  }

  public void setIndustryVariance(Double industryVariance) {
    this.industryVariance = industryVariance;
  }

  public Double getProgramYearMargin() {
    return programYearMargin;
  }

  public void setProgramYearMargin(Double programYearMargin) {
    this.programYearMargin = programYearMargin;
  }

  public Double getAdjustedReferenceMargin() {
    return adjustedReferenceMargin;
  }

  public void setAdjustedReferenceMargin(Double adjustedReferenceMargin) {
    this.adjustedReferenceMargin = adjustedReferenceMargin;
  }

  public Double getAdjustedReferenceMarginVariance() {
    return adjustedReferenceMarginVariance;
  }

  public void setAdjustedReferenceMarginVariance(Double adjustedReferenceMarginVariance) {
    this.adjustedReferenceMarginVariance = adjustedReferenceMarginVariance;
  }

  @Override
  public String toString() {
    return "MarginTestResult [\n"
        + "\t programYearMargin=" + programYearMargin + "\n"
        + "\t adjustedReferenceMargin=" + adjustedReferenceMargin + "\n"
        + "\t adjustedReferenceMarginVariance=" + adjustedReferenceMarginVariance + "\n"
        + "\t referenceMarginVarianceLimit=" + adjustedReferenceMarginVarianceLimit + "\n"
        + "\t withinLimitOfReferenceMargin=" + withinLimitOfReferenceMargin + "\n"
        + "\t withinLimitOfIndustryAverage=" + withinLimitOfIndustryAverage + "\n"
        + "\t industryVarianceLimit=" + industryVarianceLimit + "\n"
        + "\t industryAverage=" + industryAverage + "\n"
        + "\t industryVariance=" + industryVariance + "\n"
        + "]";
  }

  public void copy(MarginTestResult o) {
    super.copy(o);
    programYearMargin = o.programYearMargin;
    adjustedReferenceMargin = o.adjustedReferenceMargin;
    adjustedReferenceMarginVariance = o.adjustedReferenceMarginVariance;
    adjustedReferenceMarginVarianceLimit = o.adjustedReferenceMarginVarianceLimit;
    withinLimitOfReferenceMargin = o.withinLimitOfReferenceMargin;
    withinLimitOfIndustryAverage = o.withinLimitOfIndustryAverage;
    industryVarianceLimit = o.industryVarianceLimit;
    industryAverage = o.industryAverage;
    industryVariance = o.industryVariance;
  }

  @Override
  public String getName() {
    return null;
  }
}
