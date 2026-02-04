/**
 * Copyright (c) 2022,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.newparticipant;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ca.bc.gov.srm.farm.ui.struts.calculator.participant.PersonFormData;


/**
 * @author awilkinson
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class NewParticipantFormData {

  private PersonFormData owner = new PersonFormData();
  private PersonFormData contact = new PersonFormData();
  
  private List<OperationFormData> operations;
  
  private String participantPin;
  
  private String sin;
  private String businessNumber;
  private String trustNumber;
  
  private String municipalityCode;
  
  private String programYear;

  public PersonFormData getOwner() {
    return owner;
  }

  public void setOwner(PersonFormData owner) {
    this.owner = owner;
  }

  public PersonFormData getContact() {
    return contact;
  }

  public void setContact(PersonFormData contact) {
    this.contact = contact;
  }

  public String getSin() {
    return sin;
  }

  public void setSin(String sin) {
    this.sin = sin;
  }

  public String getBusinessNumber() {
    return businessNumber;
  }

  public void setBusinessNumber(String businessNumber) {
    this.businessNumber = businessNumber;
  }

  public String getTrustNumber() {
    return trustNumber;
  }

  public void setTrustNumber(String trustNumber) {
    this.trustNumber = trustNumber;
  }

  public List<OperationFormData> getOperations() {
    return operations;
  }

  public void setOperations(List<OperationFormData> operations) {
    this.operations = operations;
  }

  public String getParticipantPin() {
    return participantPin;
  }

  public void setParticipantPin(String participantPin) {
    this.participantPin = participantPin;
  }

  public String getMunicipalityCode() {
    return municipalityCode;
  }

  public void setMunicipalityCode(String municipalityCode) {
    this.municipalityCode = municipalityCode;
  }

  public String getProgramYear() {
    return programYear;
  }

  public void setProgramYear(String programYear) {
    this.programYear = programYear;
  }

}
