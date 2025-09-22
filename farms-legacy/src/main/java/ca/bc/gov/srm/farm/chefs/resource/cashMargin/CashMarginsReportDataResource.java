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
package ca.bc.gov.srm.farm.chefs.resource.cashMargin;

import java.text.SimpleDateFormat;
import java.util.Date;

import ca.bc.gov.srm.farm.chefs.resource.submission.ChefsSubmissionDataResource;

public class CashMarginsReportDataResource extends ChefsSubmissionDataResource {

  private String participantName;
  private Integer agriStabilityPin;
  private String phoneNumber;
  private String businessStructure;
  private String email;
  private String businessTaxNumberBn;
  private String sinNumber;
  private Date signatureDate;

  public String getParticipantName() {
    return participantName;
  }

  public void setParticipantName(String participantName) {
    this.participantName = participantName;
  }

  public Integer getAgriStabilityPin() {
    return agriStabilityPin;
  }

  public void setAgriStabilityPin(Integer agriStabilityPin) {
    this.agriStabilityPin = agriStabilityPin;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getBusinessStructure() {
    return businessStructure;
  }

  public void setBusinessStructure(String businessStructure) {
    this.businessStructure = businessStructure;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getBusinessTaxNumberBn() {
    return businessTaxNumberBn;
  }

  public void setBusinessTaxNumberBn(String businessTaxNumberBn) {
    this.businessTaxNumberBn = businessTaxNumberBn;
  }

  public String getSinNumber() {
    return sinNumber;
  }

  public void setSinNumber(String sinNumber) {
    this.sinNumber = sinNumber;
  }

  public String getSignatureDate() {
    if (signatureDate == null) {
      return null;
    }
    return new SimpleDateFormat("yyyy-MM-dd").format(signatureDate);
  }

  public void setSignatureDate(Date signatureDate) {
    this.signatureDate = signatureDate;
  }

  @Override
  public String toString() {
    return "CashMarginsReportDataResource [participantName=" + participantName + ", agriStabilityPin=" + agriStabilityPin + ", phoneNumber="
        + phoneNumber + ", businessStructure=" + businessStructure + ", email=" + email + ", businessTaxNumberBn=" + businessTaxNumberBn
        + ", sinNumber=" + sinNumber + "]";
  }

}