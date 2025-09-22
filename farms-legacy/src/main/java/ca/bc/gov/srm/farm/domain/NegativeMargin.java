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
package ca.bc.gov.srm.farm.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class NegativeMargin implements Serializable {

  private static final long serialVersionUID = 1L;
  
  private Integer negativeMarginId;
  private Integer farmingOperationId;
  private String inventoryItemCode;
  private Integer scenarioId;
  private Integer revisionCount;
  private String inventory;
  private BigDecimal deductiblePercentage;
  private BigDecimal insurableValue;
  private BigDecimal insurableValuePurchased;
  private BigDecimal reported;
  private BigDecimal guaranteedProdValue;
  private BigDecimal premiumsPaid;
  private BigDecimal claimsReceived;
  private BigDecimal deemedReceived;
  private BigDecimal deemedPiValue;
  private BigDecimal premiumRate;
  private BigDecimal claimsCalculation;
  private BigDecimal premium;
  private BigDecimal mrp;
  private BigDecimal deemedPremium;

  public Integer getNegativeMarginId() {
    return negativeMarginId;
  }

  public void setNegativeMarginId(Integer negativeMarginId) {
    this.negativeMarginId = negativeMarginId;
  }

  public Integer getFarmingOperationId() {
    return farmingOperationId;
  }

  public void setFarmingOperationId(Integer farmingOperationId) {
    this.farmingOperationId = farmingOperationId;
  }

  public String getInventoryItemCode() {
    return inventoryItemCode;
  }

  public void setInventoryItemCode(String inventoryItemCode) {
    this.inventoryItemCode = inventoryItemCode;
  }

  public Integer getRevisionCount() {
    return revisionCount;
  }

  public void setRevisionCount(Integer revisionCount) {
    this.revisionCount = revisionCount;
  }

  public String getInventory() {
    return inventory;
  }

  public void setInventory(String inventory) {
    this.inventory = inventory;
  }

  public BigDecimal getDeductiblePercentage() {
    return deductiblePercentage;
  }

  public void setDeductiblePercentage(BigDecimal deductiblePercentage) {
    this.deductiblePercentage = deductiblePercentage;
  }

  public BigDecimal getInsurableValue() {
    return insurableValue;
  }

  public void setInsurableValue(BigDecimal insurableValue) {
    this.insurableValue = insurableValue;
  }

  public BigDecimal getInsurableValuePurchased() {
    return insurableValuePurchased;
  }

  public void setInsurableValuePurchased(BigDecimal insurableValuePurchased) {
    this.insurableValuePurchased = insurableValuePurchased;
  }

  public BigDecimal getReported() {
    return reported;
  }

  public void setReported(BigDecimal reported) {
    this.reported = reported;
  }

  public BigDecimal getGuaranteedProdValue() {
    return guaranteedProdValue;
  }

  public void setGuaranteedProdValue(BigDecimal guaranteedProdValue) {
    this.guaranteedProdValue = guaranteedProdValue;
  }

  public BigDecimal getPremiumsPaid() {
    return premiumsPaid;
  }

  public void setPremiumsPaid(BigDecimal premiumsPaid) {
    this.premiumsPaid = premiumsPaid;
  }

  public BigDecimal getClaimsReceived() {
    return claimsReceived;
  }

  public void setClaimsReceived(BigDecimal claimsReceived) {
    this.claimsReceived = claimsReceived;
  }

  public BigDecimal getDeemedReceived() {
    return deemedReceived;
  }

  public void setDeemedReceived(BigDecimal deemedReceived) {
    this.deemedReceived = deemedReceived;
  }

  public BigDecimal getDeemedPiValue() {
    return deemedPiValue;
  }

  public void setDeemedPiValue(BigDecimal deemedPiValue) {
    this.deemedPiValue = deemedPiValue;
  }

  public BigDecimal getPremiumRate() {
    return premiumRate;
  }

  public void setPremiumRate(BigDecimal premiumRate) {
    this.premiumRate = premiumRate;
  }

  public BigDecimal getClaimsCalculation() {
    return claimsCalculation;
  }

  public void setClaimsCalculation(BigDecimal claimsCalculation) {
    this.claimsCalculation = claimsCalculation;
  }

  public BigDecimal getPremium() {
    return premium;
  }

  public void setPremium(BigDecimal premium) {
    this.premium = premium;
  }

  public BigDecimal getMrp() {
    return mrp;
  }

  public void setMrp(BigDecimal mrp) {
    this.mrp = mrp;
  }

  public BigDecimal getDeemedPremium() {
    return deemedPremium;
  }

  public void setDeemedPremium(BigDecimal deemedPremium) {
    this.deemedPremium = deemedPremium;
  }

  public Integer getScenarioId() {
    return scenarioId;
  }

  public void setScenarioId(Integer scenarioId) {
    this.scenarioId = scenarioId;
  }
}
