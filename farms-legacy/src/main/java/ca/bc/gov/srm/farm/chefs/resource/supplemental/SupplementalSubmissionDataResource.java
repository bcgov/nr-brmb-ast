package ca.bc.gov.srm.farm.chefs.resource.supplemental;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ca.bc.gov.srm.farm.chefs.resource.submission.LabelValue;

public class SupplementalSubmissionDataResource extends SupplementalBaseDataResource {

  private Integer agriStabilityAgriInvestPin;
  private String participantName;
  private String telephone;
  private String businessStructure;
  private String email;
  private String businessTaxNumber;
  private String sinNumber;

  private String onBehalfOfParticipant;
  private String signatureFirstName;
  private String signatureLastName;
  private String signatureDate;
  private String howDoYouKnowTheParticipant;

  private String hasDecreasedDueToDisaster;
  private LabelValue programYear;
  private SupplementalAccruals checkAllThatApply;
  private LabelValue typeOfAnimalCustomFed;

  @JsonIgnore
  private String pattern = "yyyy-MM-dd";
  @JsonIgnore
  private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

  public String getParticipantName() {
    return participantName;
  }

  public void setParticipantName(String participantName) {
    this.participantName = participantName;
  }

  public Integer getAgriStabilityAgriInvestPin() {
    return agriStabilityAgriInvestPin;
  }

  public void setAgriStabilityAgriInvestPin(Integer agriStabilityAgriInvestPin) {
    this.agriStabilityAgriInvestPin = agriStabilityAgriInvestPin;
  }

  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
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

  public String getBusinessTaxNumber() {
    return businessTaxNumber;
  }

  public void setBusinessTaxNumber(String businessTaxNumber) {
    this.businessTaxNumber = businessTaxNumber;
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

  public String getHasDecreasedDueToDisaster() {
    return hasDecreasedDueToDisaster;
  }

  public void setHasDecreasedDueToDisaster(String hasDecreasedDueToDisaster) {
    this.hasDecreasedDueToDisaster = hasDecreasedDueToDisaster;
  }

  public LabelValue getProgramYear() {
    return programYear;
  }

  public void setProgramYear(LabelValue programYear) {
    this.programYear = programYear;
  }

  public SupplementalAccruals getCheckAllThatApply() {
    return checkAllThatApply;
  }

  public void setCheckAllThatApply(SupplementalAccruals checkAllThatApply) {
    this.checkAllThatApply = checkAllThatApply;
  }

  public LabelValue getTypeOfAnimalCustomFed() {
    return typeOfAnimalCustomFed;
  }

  public void setTypeOfAnimalCustomFed(LabelValue typeOfAnimalCustomFed) {
    this.typeOfAnimalCustomFed = typeOfAnimalCustomFed;
  }

}
