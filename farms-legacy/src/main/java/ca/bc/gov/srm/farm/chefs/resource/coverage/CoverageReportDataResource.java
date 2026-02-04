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
package ca.bc.gov.srm.farm.chefs.resource.coverage;

import java.util.Date;
import java.util.List;

import ca.bc.gov.srm.farm.chefs.resource.submission.ChefsSubmissionDataResource;

public class CoverageReportDataResource extends ChefsSubmissionDataResource {

  private String participantName;
  private String address;
  private String city;
  private String province;
  private String postalCode;
  private Date reportDate;

  private Integer agriStabilityAgriInvestPin;
  private Integer programYear;

  private Double referenceMargin;
  private Double paymentCapPercentageOfTotalMarginDecline;
  private Double standardPositiveMarginCompensationRate;
  private Double totalMarginDecline;

  private List<CoverageRefScenarioDataResource> refs;

  public String getParticipantName() {
    return participantName;
  }

  public void setParticipantName(String participantName) {
    this.participantName = participantName;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public Date getReportDate() {
    return reportDate;
  }

  public void setReportDate(Date reportDate) {
    this.reportDate = reportDate;
  }

  public Integer getAgriStabilityAgriInvestPin() {
    return agriStabilityAgriInvestPin;
  }

  public void setAgriStabilityAgriInvestPin(Integer agriStabilityAgriInvestPin) {
    this.agriStabilityAgriInvestPin = agriStabilityAgriInvestPin;
  }

  public Integer getProgramYear() {
    return programYear;
  }

  public void setProgramYear(Integer programYear) {
    this.programYear = programYear;
  }

  public Double getPaymentCapPercentageOfTotalMarginDecline() {
    return paymentCapPercentageOfTotalMarginDecline;
  }

  public void setPaymentCapPercentageOfTotalMarginDecline(Double paymentCapPercentageOfTotalMarginDecline) {
    this.paymentCapPercentageOfTotalMarginDecline = paymentCapPercentageOfTotalMarginDecline;
  }

  public Double getStandardPositiveMarginCompensationRate() {
    return standardPositiveMarginCompensationRate;
  }

  public void setStandardPositiveMarginCompensationRate(Double standardPositiveMarginCompensationRate) {
    this.standardPositiveMarginCompensationRate = standardPositiveMarginCompensationRate;
  }

  public Double getReferenceMargin() {
    return referenceMargin;
  }

  public void setReferenceMargin(Double referenceMargin) {
    this.referenceMargin = referenceMargin;
  }

  public Double getTotalMarginDecline() {
    return totalMarginDecline;
  }

  public void setTotalMarginDecline(Double totalMarginDecline) {
    this.totalMarginDecline = totalMarginDecline;
  }

  public List<CoverageRefScenarioDataResource> getRefs() {
    return refs;
  }

  public void setRefs(List<CoverageRefScenarioDataResource> refs) {
    this.refs = refs;
  }

  @Override
  public String toString() {
    return "CoverageReportDataResource [participantName=" + participantName + ", address=" + address + ", city=" + city + ", province=" + province
        + ", postalCode=" + postalCode + ", reportDate=" + reportDate + ", agriStabilityAgriInvestPin=" + agriStabilityAgriInvestPin
        + ", programYear=" + programYear + ", paymentCapPercentage=" + paymentCapPercentageOfTotalMarginDecline + ", positiveMarginCompRate=" + standardPositiveMarginCompensationRate
        + ", refs=" + refs + "]";
  }

}