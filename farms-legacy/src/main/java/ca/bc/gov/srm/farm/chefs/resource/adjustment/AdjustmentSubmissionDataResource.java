/**
 * Copyright (c) 2023,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.chefs.resource.adjustment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ca.bc.gov.srm.farm.chefs.resource.submission.ChefsSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.LabelValue;

public class AdjustmentSubmissionDataResource extends ChefsSubmissionDataResource {

  private String participantName;
  private Integer agriStabilityPin;
  private String businessStructure;
  private String businessTaxNumberBn;
  private String sinNumber;

  private String onBehalfOfParticipant;
  private String signatureFirstName;
  private String signatureLastName;
  private String signatureDate;
  private String howDoYouKnowTheParticipant;

  private String otherDetails;

  private List<LabelValue> simpleselectadvanced = new ArrayList<>();
  
  private List<LabelValue> reasonForForm;

  private List<AdjustmentGrid> adjustmentGrid = new ArrayList<>();
  private Map<String, Boolean> yearsToAdjust;

  public String getOtherDetails() {
    return otherDetails;
  }

  public void setOtherDetails(String otherDetails) {
    this.otherDetails = otherDetails;
  }

  public List<AdjustmentGrid> getAdjustmentGrid() {
    return adjustmentGrid;
  }

  public void setAdjustmentGrid(List<AdjustmentGrid> adjustmentGrid) {
    this.adjustmentGrid = adjustmentGrid;
  }

  public Map<String, Boolean> getYearsToAdjust() {
    return yearsToAdjust;
  }

  public void setYearsToAdjust(Map<String, Boolean> yearsToAdjust) {
    this.yearsToAdjust = yearsToAdjust;
  }

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

  public String getBusinessStructure() {
    return businessStructure;
  }

  public void setBusinessStructure(String businessStructure) {
    this.businessStructure = businessStructure;
  }

  public String getBusinessTaxNumberBn() {
    return businessTaxNumberBn != null ? businessTaxNumberBn.replaceAll("\\s", "") : null;
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

  public String getOnBehalfOfParticipant() {
    return onBehalfOfParticipant;
  }

  public void setOnBehalfOfParticipant(String onBehalfOfParticipant) {
    this.onBehalfOfParticipant = onBehalfOfParticipant;
  }

  public String getSignatureFirstName() {
    return signatureFirstName;
  }

  public void setSignatureFirstName(String signatureFirstName) {
    this.signatureFirstName = signatureFirstName;
  }

  public String getSignatureLastName() {
    return signatureLastName;
  }

  public void setSignatureLastName(String signatureLastName) {
    this.signatureLastName = signatureLastName;
  }

  public String getSignatureDate() {
    return signatureDate;
  }

  public void setSignatureDate(String signatureDate) {
    this.signatureDate = signatureDate;
  }

  public String getHowDoYouKnowTheParticipant() {
    return howDoYouKnowTheParticipant;
  }

  public void setHowDoYouKnowTheParticipant(String howDoYouKnowTheParticipant) {
    this.howDoYouKnowTheParticipant = howDoYouKnowTheParticipant;
  }

  public List<LabelValue> getSimpleselectadvanced() {
    return simpleselectadvanced;
  }

  public void setSimpleselectadvanced(List<LabelValue> simpleselectadvanced) {
    this.simpleselectadvanced = simpleselectadvanced;
  }

  public List<LabelValue> getReasonForForm() {
    return reasonForForm;
  }

  public void setReasonForForm(List<LabelValue> reasonForForm) {
    this.reasonForForm = reasonForForm;
  }

  public List<Integer> getSelectedYears() {
    
    if (this.yearsToAdjust == null ) {
      return new ArrayList<>();
    }
    return this.yearsToAdjust.entrySet().stream().filter(map -> map.getValue()).map(e -> Integer.parseInt(e.getKey()))
        .sorted().collect(Collectors.toList());
  }

  public Integer getProgramYear() {
    if (getSelectedYears().size() > 0) {
      return getSelectedYears().get(getSelectedYears().size() - 1);
    }
    return null;
    
  }

}
