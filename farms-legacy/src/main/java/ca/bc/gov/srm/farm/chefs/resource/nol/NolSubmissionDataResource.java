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
package ca.bc.gov.srm.farm.chefs.resource.nol;

import java.util.List;

import ca.bc.gov.srm.farm.chefs.resource.submission.ChefsSubmissionDataResource;

/**
 * @author awilkinson
 */
public class NolSubmissionDataResource extends ChefsSubmissionDataResource {

  private String growerCorporationName;
  private String phoneNumber;
  private String participantType;
  private String email;
  private Integer programYear;
  private Integer agriStabilityPin;
  private String sinNumber;
  private String businessNumber;
  private String primaryFarmingActivity;
  private String primaryFarmingActivityOther;

  private String onBehalfOfParticipant;
  private String signatureFirstName;
  private String signatureLastName;
  private String signatureDate;
  private String howDoYouKnowTheParticipant;
  
  
  private String submitTo;
  private IncomeBelowThresholdCheckboxes incomeBelowThresholdCheckboxes;
  
  private String incomeDecreaseDetails;
  private String expenseIncreaseDetails;
  
  private Boolean submitAnAdditionalNolForm;
  private Integer productionInsurancePolicyNumber;
  private Integer productionInsurancePolicyNumber2;
  private Perils perils;
  private Perils perils2;
  private List<String> selectCrops;
  private List<String> cropPlantTypeDamaged2;
  private String dateOfEvent;
  private String dateOfEvent2;
  private String eventCauseOfLoss;
  private String eventCauseOfLoss2;
  private String locationOfTheLand;
  private String locationOfTheLand2;
  private String selectYourLocalOffice;
  private String observationOfDamageRemarks1;
  private String observationOfDamageRemarks2;
  private String selectYourPlan;
  private String extentOfDamage;
  private String extentOfDamage2;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
  
  public String getGrowerCorporationName() {
    return growerCorporationName;
  }

  public void setGrowerCorporationName(String growerCorporationName) {
    this.growerCorporationName = growerCorporationName;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getParticipantType() {
    return participantType;
  }

  public void setParticipantType(String participantType) {
    this.participantType = participantType;
  }

  public Integer getProgramYear() {
    return programYear;
  }

  public void setProgramYear(Integer programYear) {
    this.programYear = programYear;
  }

  public Integer getAgriStabilityPin() {
    return agriStabilityPin;
  }

  public void setAgriStabilityPin(Integer agriStabilityPin) {
    this.agriStabilityPin = agriStabilityPin;
  }

  public String getSinNumber() {
    return sinNumber;
  }

  public void setSinNumber(String sinNumber) {
    this.sinNumber = sinNumber;
  }

  public String getBusinessNumber() {
    return businessNumber != null ? businessNumber.replaceAll("\\s", "") : null;
  }

  public void setBusinessNumber(String businessNumber) {
    this.businessNumber = businessNumber;
  }

  public String getPrimaryFarmingActivity() {
    return primaryFarmingActivity;
  }

  public void setPrimaryFarmingActivity(String primaryFarmingActivity) {
    this.primaryFarmingActivity = primaryFarmingActivity;
  }

  public String getPrimaryFarmingActivityOther() {
    return primaryFarmingActivityOther;
  }

  public void setPrimaryFarmingActivityOther(String primaryFarmingActivityOther) {
    this.primaryFarmingActivityOther = primaryFarmingActivityOther;
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

  public String getSubmitTo() {
    return submitTo;
  }

  public void setSubmitTo(String submitTo) {
    this.submitTo = submitTo;
  }

  public String getIncomeDecreaseDetails() {
    return incomeDecreaseDetails;
  }

  public void setIncomeDecreaseDetails(String incomeDecreaseDetails) {
    this.incomeDecreaseDetails = incomeDecreaseDetails;
  }

  public String getExpenseIncreaseDetails() {
    return expenseIncreaseDetails;
  }

  public void setExpenseIncreaseDetails(String expenseIncreaseDetails) {
    this.expenseIncreaseDetails = expenseIncreaseDetails;
  }

  public IncomeBelowThresholdCheckboxes getIncomeBelowThresholdCheckboxes() {
    return incomeBelowThresholdCheckboxes;
  }

  public void setIncomeBelowThresholdCheckboxes(IncomeBelowThresholdCheckboxes incomeBelowThresholdCheckboxes) {
    this.incomeBelowThresholdCheckboxes = incomeBelowThresholdCheckboxes;
  }


  public Perils getPerils() {
    return perils;
  }

  public void setPerils(Perils perils) {
    this.perils = perils;
  }


  public List<String> getSelectCrops() {
    return selectCrops;
  }

  public void setSelectCrops(List<String> selectCrops) {
    this.selectCrops = selectCrops;
  }


  public Integer getProductionInsurancePolicyNumber() {
    return productionInsurancePolicyNumber;
  }

  public void setProductionInsurancePolicyNumber(Integer productionInsurancePolicyNumber) {
    this.productionInsurancePolicyNumber = productionInsurancePolicyNumber;
  }


  public class IncomeBelowThresholdCheckboxes {
    
    private Boolean yes;
    
    private Boolean no;

    public Boolean getYes() {
      return yes;
    }

    public void setYes(Boolean yes) {
      this.yes = yes;
    }

    public Boolean getNo() {
      return no;
    }

    public void setNo(Boolean no) {
      this.no = no;
    }
  }


  public Boolean getSubmitAnAdditionalNolForm() {
    return submitAnAdditionalNolForm;
  }

  public void setSubmitAnAdditionalNolForm(Boolean submitAnAdditionalNolForm) {
    this.submitAnAdditionalNolForm = submitAnAdditionalNolForm;
  }

  public String getDateOfEvent() {
    return dateOfEvent;
  }

  public void setDateOfEvent(String dateOfEvent) {
    this.dateOfEvent = dateOfEvent;
  }

  public String getLocationOfTheLand() {
    return locationOfTheLand;
  }

  public void setLocationOfTheLand(String locationOfTheLand) {
    this.locationOfTheLand = locationOfTheLand;
  }

  public String getSelectYourLocalOffice() {
    return selectYourLocalOffice;
  }

  public void setSelectYourLocalOffice(String selectYourLocalOffice) {
    this.selectYourLocalOffice = selectYourLocalOffice;
  }

  public String getObservationOfDamageRemarks1() {
    return observationOfDamageRemarks1;
  }

  public void setObservationOfDamageRemarks1(String observationOfDamageRemarks1) {
    this.observationOfDamageRemarks1 = observationOfDamageRemarks1;
  }

  public String getSelectYourPlan() {
    return selectYourPlan;
  }

  public void setSelectYourPlan(String selectYourPlan) {
    this.selectYourPlan = selectYourPlan;
  }

  public String getExtentOfDamage() {
    return extentOfDamage;
  }

  public void setExtentOfDamage(String extentOfDamage) {
    this.extentOfDamage = extentOfDamage;
  }

  public Integer getProductionInsurancePolicyNumber2() {
    return productionInsurancePolicyNumber2;
  }

  public void setProductionInsurancePolicyNumber2(Integer productionInsurancePolicyNumber2) {
    this.productionInsurancePolicyNumber2 = productionInsurancePolicyNumber2;
  }

  public Perils getPerils2() {
    return perils2;
  }

  public void setPerils2(Perils perils2) {
    this.perils2 = perils2;
  }

  public List<String> getCropPlantTypeDamaged2() {
    return cropPlantTypeDamaged2;
  }

  public void setCropPlantTypeDamaged2(List<String> cropPlantTypeDamaged2) {
    this.cropPlantTypeDamaged2 = cropPlantTypeDamaged2;
  }

  public String getDateOfEvent2() {
    return dateOfEvent2;
  }

  public void setDateOfEvent2(String dateOfEvent2) {
    this.dateOfEvent2 = dateOfEvent2;
  }

  public String getEventCauseOfLoss() {
    return eventCauseOfLoss;
  }

  public void setEventCauseOfLoss(String eventCauseOfLoss) {
    this.eventCauseOfLoss = eventCauseOfLoss;
  }

  public String getEventCauseOfLoss2() {
    return eventCauseOfLoss2;
  }

  public void setEventCauseOfLoss2(String eventCauseOfLoss2) {
    this.eventCauseOfLoss2 = eventCauseOfLoss2;
  }

  public String getLocationOfTheLand2() {
    return locationOfTheLand2;
  }

  public void setLocationOfTheLand2(String locationOfTheLand2) {
    this.locationOfTheLand2 = locationOfTheLand2;
  }

  public String getObservationOfDamageRemarks2() {
    return observationOfDamageRemarks2;
  }

  public void setObservationOfDamageRemarks2(String observationOfDamageRemarks2) {
    this.observationOfDamageRemarks2 = observationOfDamageRemarks2;
  }

  public String getExtentOfDamage2() {
    return extentOfDamage2;
  }

  public void setExtentOfDamage2(String extentOfDamage2) {
    this.extentOfDamage2 = extentOfDamage2;
  }
}
