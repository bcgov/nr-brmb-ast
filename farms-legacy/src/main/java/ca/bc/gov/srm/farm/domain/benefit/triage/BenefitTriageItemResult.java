/**
 * Copyright (c) 2024,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain.benefit.triage;

import java.util.ArrayList;
import java.util.List;

public class BenefitTriageItemResult {

  private Integer participantPin;
  private Integer programYear;
  private String clientName;
  private String scenarioStateCodeDesc;
  private Double estimatedBenefit;
  private Boolean isPaymentFile;
  private Integer scenarioNumber;
  private List<String> errorMessages;
  private List<String> failMessages;
  
  // Only used by unit tests
  private boolean zeroPass;
  private boolean paymentPass;

  public Integer getParticipantPin() {
    return participantPin;
  }

  public void setParticipantPin(Integer participantPin) {
    this.participantPin = participantPin;
  }

  public Integer getProgramYear() {
    return programYear;
  }

  public void setProgramYear(Integer programYear) {
    this.programYear = programYear;
  }

  public String getClientName() {
    return clientName;
  }

  public void setClientName(String clientName) {
    this.clientName = clientName;
  }

  public String getScenarioStateCodeDesc() {
    return scenarioStateCodeDesc;
  }

  public void setScenarioStateCodeDesc(String scenarioStateCodeDesc) {
    this.scenarioStateCodeDesc = scenarioStateCodeDesc;
  }

  public Double getEstimatedBenefit() {
    return estimatedBenefit;
  }

  public void setEstimatedBenefit(Double estimatedBenefit) {
    this.estimatedBenefit = estimatedBenefit;
  }

  public Boolean getIsPaymentFile() {
    return isPaymentFile;
  }

  public void setIsPaymentFile(Boolean isPaymentFile) {
    this.isPaymentFile = isPaymentFile;
  }

  public Integer getScenarioNumber() {
    return scenarioNumber;
  }

  public void setScenarioNumber(Integer scenarioNumber) {
    this.scenarioNumber = scenarioNumber;
  }

  public List<String> getErrorMessages() {
    return errorMessages;
  }

  public void setErrorMessages(List<String> errorMessages) {
    this.errorMessages = errorMessages;
  }

  /**
   * Only used by unit tests
   */
  public boolean isZeroPass() {
    return zeroPass;
  }

  public void setZeroPass(boolean zeroPass) {
    this.zeroPass = zeroPass;
  }

  public boolean isPaymentPass() {
    return paymentPass;
  }

  public void setPaymentPass(boolean paymentPass) {
    this.paymentPass = paymentPass;
  }

  public List<String> getFailMessages() {
    if(failMessages == null) {
      failMessages = new ArrayList<>();
    }
    return failMessages;
  }

  public void setFailMessages(List<String> failMessages) {
    this.failMessages = failMessages;
  }

  @Override
  public String toString() {
    return "BenefitTriageItemResult [participantPin=" + participantPin + ", programYear=" + programYear + ", clientName=" + clientName
        + ", scenarioStateCodeDesc=" + scenarioStateCodeDesc + ", estimatedBenefit=" + estimatedBenefit + ", isPaymentFile=" + isPaymentFile
        + ", scenarioNumber=" + scenarioNumber + ", errorMessages=" + errorMessages + ", zeroPass=" + zeroPass + "]";
  }

}