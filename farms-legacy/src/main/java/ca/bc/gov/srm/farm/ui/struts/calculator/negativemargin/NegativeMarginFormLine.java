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
package ca.bc.gov.srm.farm.ui.struts.calculator.negativemargin;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.NegativeMarginUtils;

public class NegativeMarginFormLine implements Serializable {

  private static final long serialVersionUID = 1L;
  
  private Integer negativeMarginId;
  private Integer farmingOperationId;
  private String inventoryItemCode;
  private Integer revisionCount;
  private String inventory;
  private String deductiblePercentage;
  private String insurableValue;
  private String insurableValuePurchased;
  private String reported;
  private String guaranteedProdValue;
  private String premiumsPaid;
  private String claimsReceived;
  private String deemedReceived;
  private String deemedPiValue;
  private String premiumRate;
  private String claimsCalculation;
  private String premium;
  private String mrp;
  private String deemedPremium;

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

  public String getDeductiblePercentage() {
    return deductiblePercentage;
  }

  public void setDeductiblePercentage(String deductiblePercentage) {
    this.deductiblePercentage = deductiblePercentage;
  }

  public String getInsurableValue() {
    return insurableValue;
  }

  public void setInsurableValue(String insurableValue) {
    this.insurableValue = insurableValue;
  }

  public String getInsurableValuePurchased() {
    return insurableValuePurchased;
  }

  public void setInsurableValuePurchased(String insurableValuePurchased) {
    this.insurableValuePurchased = insurableValuePurchased;
  }

  public String getReported() {
    return reported;
  }

  public void setReported(String reported) {
    this.reported = reported;
  }

  public String getGuaranteedProdValue() {
    return guaranteedProdValue;
  }

  public void setGuaranteedProdValue(String guaranteedProdValue) {
    this.guaranteedProdValue = guaranteedProdValue;
  }

  public String getPremiumsPaid() {
    return premiumsPaid;
  }

  public void setPremiumsPaid(String premiumsPaid) {
    this.premiumsPaid = premiumsPaid;
  }

  public String getClaimsReceived() {
    return claimsReceived;
  }

  public void setClaimsReceived(String claimsReceived) {
    this.claimsReceived = claimsReceived;
  }

  public String getDeemedReceived() {
    return deemedReceived;
  }

  public void setDeemedReceived(String deemedReceived) {
    this.deemedReceived = deemedReceived;
  }

  public String getDeemedPiValue() {
    return deemedPiValue;
  }

  public void setDeemedPiValue(String deemedPiValue) {
    this.deemedPiValue = deemedPiValue;
  }

  public String getPremiumRate() {
    return premiumRate;
  }

  public void setPremiumRate(String premiumRate) {
    this.premiumRate = premiumRate;
  }

  public String getClaimsCalculation() {
    return claimsCalculation;
  }

  public void setClaimsCalculation(String claimsCalculation) {
    this.claimsCalculation = claimsCalculation;
  }

  public String getPremium() {
    return premium;
  }

  public void setPremium(String premium) {
    this.premium = premium;
  }

  public String getMrp() {
    return mrp;
  }

  public void setMrp(String mrp) {
    this.mrp = mrp;
  }

  public String getDeemedPremium() {
    return deemedPremium;
  }

  public void setDeemedPremium(String deemedPremium) {
    this.deemedPremium = deemedPremium;
  }

  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
    ActionErrors errors = new ActionErrors();

    final BigDecimal maxValue = new BigDecimal("99999999999.99");
    final BigDecimal minValue = new BigDecimal("0");

    String errorDeductiblePercentage = MessageConstants.ERRORS_NEGATIVE_MARGIN_DEDUCTIBLE_PERCENTAGE;
    String errorInsurableValuePurchased = MessageConstants.ERRORS_NEGATIVE_MARGIN_INSURABLE_VALUE_PURCHASED;
    String errorGuaranteedProdValue = MessageConstants.ERRORS_NEGATIVE_MARGIN_GUARANTEED_PROD_VALUE;
    String errorPremiumsPaid = MessageConstants.ERRORS_NEGATIVE_MARGIN_PREMIUMS_PAID;
    String errorClaimsReceived = MessageConstants.ERRORS_NEGATIVE_MARGIN_CLAIMS_RECEIVED;

    boolean badDeductiblePercentage = false;
    boolean badInsurableValuePurchased = false;
    boolean badGuaranteedProdValue = false;
    boolean badPremiumsPaid = false;
    boolean badClaimsReceived = false;

    if (!StringUtils.isBlank(deductiblePercentage)) {
      try {
        BigDecimal value = new BigDecimal(deductiblePercentage);
        if (NegativeMarginUtils.outOfRange(value, new BigDecimal("0"), new BigDecimal("100"))) {
          badDeductiblePercentage = true;
        }
      } catch (NumberFormatException e) {
        badDeductiblePercentage = true;
      }
    }

    if (!StringUtils.isBlank(insurableValuePurchased)) {
      try {
        BigDecimal value = new BigDecimal(insurableValuePurchased);
        if (NegativeMarginUtils.outOfRange(value, minValue, maxValue)) {
          badInsurableValuePurchased = true;
        }
      } catch (NumberFormatException e) {
        badInsurableValuePurchased = true;
      }
    }

    if (!StringUtils.isBlank(guaranteedProdValue)) {
      try {
        BigDecimal value = new BigDecimal(guaranteedProdValue);
        if (NegativeMarginUtils.outOfRange(value, minValue, maxValue)) {
          badGuaranteedProdValue = true;
        }
      } catch (NumberFormatException e) {
        badGuaranteedProdValue = true;
      }
    }

    if (!StringUtils.isBlank(premiumsPaid)) {
      try {
        BigDecimal value = new BigDecimal(premiumsPaid);
        if (NegativeMarginUtils.outOfRange(value, minValue, maxValue)) {
          badPremiumsPaid = true;
        }
      } catch (NumberFormatException e) {
        badPremiumsPaid = true;
      }
    }

    if (!StringUtils.isBlank(claimsReceived)) {
      try {
        BigDecimal value = new BigDecimal(claimsReceived);
        if (NegativeMarginUtils.outOfRange(value, minValue, maxValue)) {
          badClaimsReceived = true;
        }
      } catch (NumberFormatException e) {
        badClaimsReceived = true;
      }
    }

    if (badDeductiblePercentage
        || badInsurableValuePurchased
        || badGuaranteedProdValue
        || badPremiumsPaid
        || badClaimsReceived) {

      if (badDeductiblePercentage) {
        errors.add("", new ActionError(errorDeductiblePercentage));
      }
      if (badInsurableValuePurchased) {
        errors.add("", new ActionError(errorInsurableValuePurchased));
      }
      if (badGuaranteedProdValue) {
        errors.add("", new ActionError(errorGuaranteedProdValue));
      }
      if (badPremiumsPaid) {
        errors.add("", new ActionError(errorPremiumsPaid));
      }
      if (badClaimsReceived) {
        errors.add("", new ActionError(errorClaimsReceived));
      }
    }

    return errors;
  }
}
