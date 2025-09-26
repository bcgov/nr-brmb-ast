/**
 * Copyright (c) 2025,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain.codes;

import java.math.BigDecimal;


/**
 * @author hwang
 */
public class MarketRatePremium {

  private Long marketRatePremiumId;
  private BigDecimal minTotalPremiumAmount;
  private BigDecimal maxTotalPremiumAmount;
  private BigDecimal riskChargeFlatAmount;
  private BigDecimal riskChargePercentagePremium;
  private BigDecimal adjustChargeFlatAmount;
  private Integer revisionCount;

  /**
   * @return the marketRatePremiumId
   */
  public Long getMarketRatePremiumId() {
    return marketRatePremiumId;
  }

  /**
   * @param marketRatePremiumId the marketRatePremiumId to set
   */
  public void setMarketRatePremiumId(final Long marketRatePremiumId) {
    this.marketRatePremiumId = marketRatePremiumId;
  }

  /**
   * @return the minTotalPremiumAmount
   */
  public BigDecimal getMinTotalPremiumAmount() {
    return minTotalPremiumAmount;
  }

  /**
   * @param minTotalPremiumAmount the minTotalPremiumAmount to set
   */
  public void setMinTotalPremiumAmount(final BigDecimal minTotalPremiumAmount) {
    this.minTotalPremiumAmount = minTotalPremiumAmount;
  }

  /**
   * @return the maxTotalPremiumAmount
   */
  public BigDecimal getMaxTotalPremiumAmount() {
    return maxTotalPremiumAmount;
  }

  /**
   * @param maxTotalPremiumAmount the maxTotalPremiumAmount to set
   */
  public void setMaxTotalPremiumAmount(final BigDecimal maxTotalPremiumAmount) {
    this.maxTotalPremiumAmount = maxTotalPremiumAmount;
  }

  /**
   * @return the riskChargeFlatAmount
   */
  public BigDecimal getRiskChargeFlatAmount() {
    return riskChargeFlatAmount;
  }

  /**
   * @param riskChargeFlatAmount the riskChargeFlatAmount to set
   */
  public void setRiskChargeFlatAmount(final BigDecimal riskChargeFlatAmount) {
    this.riskChargeFlatAmount = riskChargeFlatAmount;
  }

  /**
   * @return the riskChargePercentagePremium
   */
  public BigDecimal getRiskChargePercentagePremium() {
    return riskChargePercentagePremium;
  }

  /**
   * @param riskChargePercentagePremium the riskChargePercentagePremium to set
   */
  public void setRiskChargePercentagePremium(final BigDecimal riskChargePercentagePremium) {
    this.riskChargePercentagePremium = riskChargePercentagePremium;
  }

  /**
   * @return the adjustChargeFlatAmount
   */
  public BigDecimal getAdjustChargeFlatAmount() {
    return adjustChargeFlatAmount;
  }

  /**
   * @param adjustChargeFlatAmount the adjustChargeFlatAmount to set
   */
  public void setAdjustChargeFlatAmount(final BigDecimal adjustChargeFlatAmount) {
    this.adjustChargeFlatAmount = adjustChargeFlatAmount;
  }

  /**
   * @return the revisionCount
   */
  public Integer getRevisionCount() {
    return revisionCount;
  }

  /**
   * @param revisionCount the revisionCount to set
   */
  public void setRevisionCount(final Integer revisionCount) {
    this.revisionCount = revisionCount;
  }
}
