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
package ca.bc.gov.srm.farm.domain;

import java.io.Serializable;

/**
 * @author awilkinson
 */
public class PreVerificationChecklist implements Serializable {

  private static final long serialVersionUID = 1;
  
  private Double paymentAmountRequiringASpecialist;
  private Double incomeAmountRequiringASpecialist;
  
  private Boolean hasBenefitPayment;
  private Boolean paymentOverSpecialistThreshold;
  private Boolean referenceMarginTestFailed;
  private Boolean structureChangeTestFailed;
  private Boolean inCombinedFarm;
  private Boolean allowableIncomeOverSpecialistThreshold;
  private String farmTypeDetailCode;
  private String farmTypeDetailCodeDescription;
  private String triageQueue;
  private String triageQueueDescription;

  public Double getPaymentAmountRequiringASpecialist() {
    return paymentAmountRequiringASpecialist;
  }

  public void setPaymentAmountRequiringASpecialist(Double paymentAmountRequiringASpecialist) {
    this.paymentAmountRequiringASpecialist = paymentAmountRequiringASpecialist;
  }

  public Double getIncomeAmountRequiringASpecialist() {
    return incomeAmountRequiringASpecialist;
  }

  public void setIncomeAmountRequiringASpecialist(Double incomeAmountRequiringASpecialist) {
    this.incomeAmountRequiringASpecialist = incomeAmountRequiringASpecialist;
  }

  public Boolean getHasBenefitPayment() {
    return hasBenefitPayment;
  }

  public void setHasBenefitPayment(Boolean hasBenefitPayment) {
    this.hasBenefitPayment = hasBenefitPayment;
  }

  public Boolean getPaymentOverSpecialistThreshold() {
    return paymentOverSpecialistThreshold;
  }

  public void setPaymentOverSpecialistThreshold(Boolean paymentOverSpecialistThreshold) {
    this.paymentOverSpecialistThreshold = paymentOverSpecialistThreshold;
  }

  public Boolean getReferenceMarginTestFailed() {
    return referenceMarginTestFailed;
  }

  public void setReferenceMarginTest(Boolean referenceMarginTestFailed) {
    this.referenceMarginTestFailed = referenceMarginTestFailed;
  }

  public Boolean getStructureChangeTestFailed() {
    return structureChangeTestFailed;
  }

  public void setStructureChangeTestFailed(Boolean structureChangeTestFailed) {
    this.structureChangeTestFailed = structureChangeTestFailed;
  }

  public Boolean getInCombinedFarm() {
    return inCombinedFarm;
  }

  public void setInCombinedFarm(Boolean inCombinedFarm) {
    this.inCombinedFarm = inCombinedFarm;
  }

  public Boolean getAllowableIncomeOverSpecialistThreshold() {
    return allowableIncomeOverSpecialistThreshold;
  }

  public void setAllowableIncomeOverSpecialistThreshold(Boolean allowableIncomeOverSpecialistThreshold) {
    this.allowableIncomeOverSpecialistThreshold = allowableIncomeOverSpecialistThreshold;
  }

  public String getFarmTypeDetailCode() {
    return farmTypeDetailCode;
  }

  public void setFarmTypeDetailCode(String farmTypeDetailCode) {
    this.farmTypeDetailCode = farmTypeDetailCode;
  }

  public String getFarmTypeDetailCodeDescription() {
    return farmTypeDetailCodeDescription;
  }

  public void setFarmTypeDetailCodeDescription(String farmTypeDetailCodeDescription) {
    this.farmTypeDetailCodeDescription = farmTypeDetailCodeDescription;
  }

  public String getTriageQueue() {
    return triageQueue;
  }

  public void setTriageQueue(String triageQueue) {
    this.triageQueue = triageQueue;
  }

  public String getTriageQueueDescription() {
    return triageQueueDescription;
  }

  public void setTriageQueueDescription(String triageQueueDescription) {
    this.triageQueueDescription = triageQueueDescription;
  }

  @Override
  public String toString() {
    return "PreVerificationChecklist ["
        + "\t paymentAmountRequiringASpecialist=" + paymentAmountRequiringASpecialist + "\n"
        + "\t incomeAmountRequiringASpecialist=" + incomeAmountRequiringASpecialist + "\n"
        + "\t hasBenefitPayment=" + hasBenefitPayment + "\n"
        + "\t paymentOverSpecialistThreshold=" + paymentOverSpecialistThreshold + "\n"
        + "\t referenceMarginTestFailed=" + referenceMarginTestFailed + "\n"
        + "\t structureChangeTestFailed=" + structureChangeTestFailed + "\n"
        + "\t inCombinedFarm=" + inCombinedFarm + "\n"
        + "\t allowableIncomeOverSpecialistThreshold=" + allowableIncomeOverSpecialistThreshold + "\n"
        + "\t farmTypeDetailCode=" + farmTypeDetailCode + "\n"
        + "\t sectorDetailCodeDescription=" + farmTypeDetailCodeDescription + "\n"
        + "\t triageQueue=" + triageQueue + "\n"
        + "\t triageQueueDescription=" + triageQueueDescription + "]";
  }
  
}
