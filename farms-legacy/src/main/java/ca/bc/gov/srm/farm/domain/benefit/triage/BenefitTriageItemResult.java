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

import org.apache.commons.lang.StringEscapeUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    if(errorMessages == null) {
      errorMessages = new ArrayList<>();
    }
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

  /**
   * The error and fail messages as one HTML fragment, ready to drop into the
   * JavaScript literal the results grid is built from.
   *
   * These messages are whatever the database or the calculator threw, so they
   * routinely carry newlines, quotes and backslashes. Written out raw those
   * terminate the JavaScript string early and the whole inline script fails to
   * parse, which leaves the grid empty, so escape for JavaScript here after
   * escaping the message text itself for HTML.
   *
   * @return the messages, one per line, or an empty string if there are none
   */
  @JsonIgnore
  public String getDisplayMessages() {
    StringBuilder text = new StringBuilder();

    for (String message : getErrorMessages()) {
      text.append(StringEscapeUtils.escapeHtml(message)).append("<br/>");
    }
    for (String message : getFailMessages()) {
      text.append(StringEscapeUtils.escapeHtml(message)).append("<br/>");
    }

    return StringEscapeUtils.escapeJavaScript(text.toString());
  }

  /**
   * The client name, escaped the same way as {@link #getDisplayMessages()}.
   *
   * @return the client name, or an empty string if it is not set
   */
  @JsonIgnore
  public String getDisplayClientName() {
    if (clientName == null) {
      return "";
    }
    return StringEscapeUtils.escapeJavaScript(StringEscapeUtils.escapeHtml(clientName));
  }

  @Override
  public String toString() {
    return "BenefitTriageItemResult [participantPin=" + participantPin + ", programYear=" + programYear + ", clientName=" + clientName
        + ", scenarioStateCodeDesc=" + scenarioStateCodeDesc + ", estimatedBenefit=" + estimatedBenefit + ", isPaymentFile=" + isPaymentFile
        + ", scenarioNumber=" + scenarioNumber + ", errorMessages=" + errorMessages + ", zeroPass=" + zeroPass + "]";
  }

}