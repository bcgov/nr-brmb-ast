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
package ca.bc.gov.srm.farm.domain.fifo;

public class FifoStatus {

  private Integer participantPin;
  private String clientName;
  private String scenarioStateCodeDesc;
  private Double estimatedBenefit;
  private String isPaymentFile;
  private Integer scenarioNumber;

  public Integer getParticipantPin() {
    return participantPin;
  }

  public void setParticipantPin(Integer participantPin) {
    this.participantPin = participantPin;
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

  public String getIsPaymentFile() {
    return isPaymentFile;
  }

  public void setIsPaymentFile(String isPaymentFile) {
    this.isPaymentFile = isPaymentFile;
  }

  public Integer getScenarioNumber() {
    return scenarioNumber;
  }

  public void setScenarioNumber(Integer scenarioNumber) {
    this.scenarioNumber = scenarioNumber;
  }

  @Override
  public String toString() {
    return "FifoStatus [participantPin=" + participantPin + ", clientName=" + clientName + ", scenarioStateCodeDesc=" + scenarioStateCodeDesc
        + ", estimatedBenefit=" + estimatedBenefit + ", isPaymentFile=" + isPaymentFile + ", scenarioNumber=" + scenarioNumber + "]";
  }

}