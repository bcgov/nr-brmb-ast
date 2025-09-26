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
package ca.bc.gov.srm.farm.ui.struts.codes.mrp;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.domain.codes.MarketRatePremium;

/**
 * @author hwang
 */
public class MarketRatePremiumsForm extends ValidatorForm {

  private static final long serialVersionUID = 1L;
  
  private List<MarketRatePremium> marketRatePremiums;
  private int numMarketRatePremiums;

  private Long marketRatePremiumId;
  private Integer minTotalPremiumAmount;
  private Integer maxTotalPremiumAmount;
  private Integer riskChargeFlatAmount;
  private Integer riskChargePercentagePremium;
  private Integer adjustChargeFlatAmount;
  private Integer revisionCount;

  private boolean isNew = false;

  /**
   * @param mapping mapping
   * @param request request
   */
  @Override
  public void reset(ActionMapping mapping, HttpServletRequest request) {
    super.reset(mapping, request);
    setNew(false);
  }

  /**
   * Get marketRatePremiums
   *
   * @return the marketRatePremiums
   */
  public List<MarketRatePremium> getMarketRatePremiums() {
    return marketRatePremiums;
  }

  /**
   * Set marketRatePremiums
   *
   * @param pMarketRatePremiums the marketRatePremiums to set
   */
  public void setMarketRatePremiums(final List<MarketRatePremium> pMarketRatePremiums) {
    this.marketRatePremiums = pMarketRatePremiums;
  }

  /**
   * Get numMarketRatePremiums
   *
   * @return the numMarketRatePremiums
   */
  public int getNumMarketRatePremiums() {
    return numMarketRatePremiums;
  }

  /**
   * Set numMarketRatePremiums
   *
   * @param pNumMarketRatePremiums the numMarketRatePremiums to set
   */
  public void setNumMarketRatePremiums(int pNumMarketRatePremiums) {
    this.numMarketRatePremiums = pNumMarketRatePremiums;
  }

  /**
   * Gets marketRatePremiumId
   *
   * @return the marketRatePremiumId
   */
  public Long getMarketRatePremiumId() {
    return marketRatePremiumId;
  }

  /**
   * Sets marketRatePremiumId
   *
   * @param pMarketRatePremiumId the marketRatePremiumId to set
   */
  public void setMarketRatePremiumId(Long pMarketRatePremiumId) {
    this.marketRatePremiumId = pMarketRatePremiumId;
  }

  /**
   * Gets minTotalPremiumAmount
   *
   * @return the minTotalPremiumAmount
   */
  public Integer getMinTotalPremiumAmount() {
    return minTotalPremiumAmount;
  }

  /**
   * Sets minTotalPremiumAmount
   *
   * @param pMinTotalPremiumAmount the minTotalPremiumAmount to set
   */
  public void setMinTotalPremiumAmount(Integer pMinTotalPremiumAmount) {
    this.minTotalPremiumAmount = pMinTotalPremiumAmount;
  }

  /**
   * Gets maxTotalPremiumAmount
   *
   * @return the maxTotalPremiumAmount
   */
  public Integer getMaxTotalPremiumAmount() {
    return maxTotalPremiumAmount;
  }

  /**
   * Sets maxTotalPremiumAmount
   *
   * @param pMaxTotalPremiumAmount the maxTotalPremiumAmount to set
   */
  public void setMaxTotalPremiumAmount(Integer pMaxTotalPremiumAmount) {
    this.maxTotalPremiumAmount = pMaxTotalPremiumAmount;
  }

  /**
   * Gets riskChargeFlatAmount
   *
   * @return the riskChargeFlatAmount
   */
  public Integer getRiskChargeFlatAmount() {
    return riskChargeFlatAmount;
  }

  /**
   * Sets riskChargeFlatAmount
   *
   * @param pRiskChargeFlatAmount the riskChargeFlatAmount to set
   */
  public void setRiskChargeFlatAmount(Integer pRiskChargeFlatAmount) {
    this.riskChargeFlatAmount = pRiskChargeFlatAmount;
  }

  /**
   * Gets riskChargePercentagePremium
   *
   * @return the riskChargePercentagePremium
   */
  public Integer getRiskChargePercentagePremium() {
    return riskChargePercentagePremium;
  }

  /**
   * Sets riskChargePercentagePremium
   *
   * @param pRiskChargePercentagePremium the riskChargePercentagePremium to set
   */
  public void setRiskChargePercentagePremium(Integer pRiskChargePercentagePremium) {
    this.riskChargePercentagePremium = pRiskChargePercentagePremium;
  }

  /**
   * Gets adjustChargeFlatAmount
   *
   * @return the adjustChargeFlatAmount
   */
  public Integer getAdjustChargeFlatAmount() {
    return adjustChargeFlatAmount;
  }

  /**
   * Sets adjustChargeFlatAmount
   *
   * @param pAdjustChargeFlatAmount the adjustChargeFlatAmount to set
   */
  public void setAdjustChargeFlatAmount(Integer pAdjustChargeFlatAmount) {
    this.adjustChargeFlatAmount = pAdjustChargeFlatAmount;
  }

  /**
   * Gets revisionCount
   *
   * @return the revisionCount
   */
  public Integer getRevisionCount() {
    return revisionCount;
  }

  /**
   * Sets revisionCount
   *
   * @param pRevisionCount the revisionCount to set
   */
  public void setRevisionCount(Integer pRevisionCount) {
    this.revisionCount = pRevisionCount;
  }

  /**
   * Gets isNew
   *
   * @return the isNew
   */
  public boolean isNew() {
    return isNew;
  }

  /**
   * Sets isNew
   *
   * @param pIsNew the isNew to set
   */
  public void setNew(boolean pIsNew) {
    this.isNew = pIsNew;
  }
}
