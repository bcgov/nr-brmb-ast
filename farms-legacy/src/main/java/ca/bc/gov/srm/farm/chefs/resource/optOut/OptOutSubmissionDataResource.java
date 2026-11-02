/**
 * Copyright (c) 2026,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.chefs.resource.optOut;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import ca.bc.gov.srm.farm.chefs.resource.submission.ChefsSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.LabelValue;

/** Data keys from the supplied Opt Out CHEFS schema. */
public class OptOutSubmissionDataResource extends ChefsSubmissionDataResource {
  private String participantName;
  private BigDecimal agriStabilityAgriInvestPin;
  private LabelValue farmType;
  private String provinceTerritoryOfMainFarmstead;
  private String businessTaxNumberBn;
  private String trustBusinessNumber;
  private String trustNumber;
  private String sinNumber;
  private String bandNumber;
  private String reasonForCancelling;
  private JsonNode simplefile;
  private String pleaseDescribeTheSpecificReasonForCancelling;
  private String onBehalfOfParticipant;
  private String signature1;
  private String signatureFirstName;
  private String signatureLastName;
  private String signatureDate1;
  private String howDoYouKnowTheParticipant;
  private String iWouldLikeToOptOutOfAgriStabilityForFollowingProgramYear;
  private String signature;
  private String signatureFullName;
  private String signatureDate;

  public String getParticipantName() {
    return participantName;
  }

  public void setParticipantName(String value) {
    participantName = value;
  }

  public BigDecimal getAgriStabilityAgriInvestPin() {
    return agriStabilityAgriInvestPin;
  }

  public void setAgriStabilityAgriInvestPin(BigDecimal value) {
    agriStabilityAgriInvestPin = value;
  }

  public LabelValue getFarmType() {
    return farmType;
  }

  public void setFarmType(LabelValue value) {
    farmType = value;
  }

  public String getProvinceTerritoryOfMainFarmstead() {
    return provinceTerritoryOfMainFarmstead;
  }

  public void setProvinceTerritoryOfMainFarmstead(String value) {
    provinceTerritoryOfMainFarmstead = value;
  }

  public String getBusinessTaxNumberBn() {
    return businessTaxNumberBn;
  }

  public void setBusinessTaxNumberBn(String value) {
    businessTaxNumberBn = value;
  }

  public String getTrustBusinessNumber() {
    return trustBusinessNumber;
  }

  public void setTrustBusinessNumber(String value) {
    trustBusinessNumber = value;
  }

  public String getTrustNumber() {
    return trustNumber;
  }

  public void setTrustNumber(String value) {
    trustNumber = value;
  }

  public String getSinNumber() {
    return sinNumber;
  }

  public void setSinNumber(String value) {
    sinNumber = value;
  }

  public String getBandNumber() {
    return bandNumber;
  }

  public void setBandNumber(String value) {
    bandNumber = value;
  }

  public String getReasonForCancelling() {
    return reasonForCancelling;
  }

  public void setReasonForCancelling(String value) {
    reasonForCancelling = value;
  }

  public JsonNode getSimplefile() {
    return simplefile;
  }

  public void setSimplefile(JsonNode value) {
    simplefile = value;
  }

  public String getPleaseDescribeTheSpecificReasonForCancelling() {
    return pleaseDescribeTheSpecificReasonForCancelling;
  }

  public void setPleaseDescribeTheSpecificReasonForCancelling(String value) {
    pleaseDescribeTheSpecificReasonForCancelling = value;
  }

  public String getOnBehalfOfParticipant() {
    return onBehalfOfParticipant;
  }

  public void setOnBehalfOfParticipant(String value) {
    onBehalfOfParticipant = value;
  }

  public String getSignature1() {
    return signature1;
  }

  public void setSignature1(String value) {
    signature1 = value;
  }

  public String getSignatureFirstName() {
    return signatureFirstName;
  }

  public void setSignatureFirstName(String value) {
    signatureFirstName = value;
  }

  public String getSignatureLastName() {
    return signatureLastName;
  }

  public void setSignatureLastName(String value) {
    signatureLastName = value;
  }

  public String getSignatureDate1() {
    return signatureDate1;
  }

  public void setSignatureDate1(String value) {
    signatureDate1 = value;
  }

  public String getHowDoYouKnowTheParticipant() {
    return howDoYouKnowTheParticipant;
  }

  public void setHowDoYouKnowTheParticipant(String value) {
    howDoYouKnowTheParticipant = value;
  }

  @JsonProperty("iWouldLikeToOptOutOfAgriStabilityForFollowingProgramYear")
  public String getIWouldLikeToOptOutOfAgriStabilityForFollowingProgramYear() {
    return iWouldLikeToOptOutOfAgriStabilityForFollowingProgramYear;
  }

  @JsonProperty("iWouldLikeToOptOutOfAgriStabilityForFollowingProgramYear")
  public void setIWouldLikeToOptOutOfAgriStabilityForFollowingProgramYear(String value) {
    iWouldLikeToOptOutOfAgriStabilityForFollowingProgramYear = value;
  }

  public String getSignature() {
    return signature;
  }

  public void setSignature(String value) {
    signature = value;
  }

  public String getSignatureFullName() {
    return signatureFullName;
  }

  public void setSignatureFullName(String value) {
    signatureFullName = value;
  }

  public String getSignatureDate() {
    return signatureDate;
  }

  public void setSignatureDate(String value) {
    signatureDate = value;
  }

  @Override
  @JsonIgnore
  public Integer getParticipantPin() {
    if (agriStabilityAgriInvestPin == null) return null;
    try {
      return agriStabilityAgriInvestPin.intValueExact();
    } catch (ArithmeticException e) {
      return null;
    }
  }

  @Override
  @JsonIgnore
  public Integer getYear() {
    String year = iWouldLikeToOptOutOfAgriStabilityForFollowingProgramYear;
    return year != null && year.matches("[0-9]{4}") ? Integer.valueOf(year) : null;
  }
}
