/**
 * Copyright (c) 2020,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain.reasonability;

import java.io.Serializable;

/**
 * @author awilkinson
 */
public class BenefitRiskCombinedFarmResult implements Serializable {

  private static final long serialVersionUID = 1L;

  private Double totalBenefit;
  private Double totalProducerIncome;
  private Double benefitBenchmark;
  private Double combinedFarmPercent;

  public Double getTotalBenefit() {
    return totalBenefit;
  }

  public void setTotalBenefit(Double totalBenefit) {
    this.totalBenefit = totalBenefit;
  }

  public Double getTotalProducerIncome() {
    return totalProducerIncome;
  }

  public void setTotalProducerIncome(Double totalProducerIncome) {
    this.totalProducerIncome = totalProducerIncome;
  }

  public Double getBenefitBenchmark() {
    return benefitBenchmark;
  }

  public void setBenefitBenchmark(Double benefitBenchmark) {
    this.benefitBenchmark = benefitBenchmark;
  }

  public Double getCombinedFarmPercent() {
    return combinedFarmPercent;
  }

  public void setCombinedFarmPercent(Double combinedFarmPercent) {
    this.combinedFarmPercent = combinedFarmPercent;
  }

  @Override
  public String toString() {
    return "BenefitRiskCombinedFarmResult [\n"
        + "\t totalBenefit=" + totalBenefit + "\n"
        + "\t totalProducerIncome=" + totalProducerIncome + "\n"
        + "\t benefitBenchmark=" + benefitBenchmark + "\n"
        + "\t combinedFarmPercent=" + combinedFarmPercent + "]";
  }

}
