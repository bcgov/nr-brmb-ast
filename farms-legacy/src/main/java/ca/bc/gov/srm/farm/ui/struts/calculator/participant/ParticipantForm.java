/**
 *
 * Copyright (c) 2010,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.participant;

import ca.bc.gov.srm.farm.ui.struts.calculator.search.CalculatorSearchForm;


/**
 * @author awilkinson
 */
public class ParticipantForm extends CalculatorSearchForm {

  private static final long serialVersionUID = -2645502921365403100L;
  
  private PersonFormData owner = new PersonFormData();
  private PersonFormData contact = new PersonFormData();
  
  private Integer newPin;
  private String sin;
  private String businessNumber;
  private String trustNumber;
  
  private String participantClassCode;
  private String clientRevisionCount;

  /**
   * @return the contact
   */
  public PersonFormData getContact() {
    return contact;
  }

  /**
   * @param contact the contact to set
   */
  public void setContact(PersonFormData contact) {
    this.contact = contact;
  }

  public Integer getNewPin() {
    return newPin;
  }

  public void setNewPin(Integer newPin) {
    this.newPin = newPin;
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

  /**
   * @return the owner
   */
  public PersonFormData getOwner() {
    return owner;
  }

  /**
   * @param owner the owner to set
   */
  public void setOwner(PersonFormData owner) {
    this.owner = owner;
  }

  /**
   * @return the participantClassCode
   */
  public String getParticipantClassCode() {
    return participantClassCode;
  }

  /**
   * @param value the participantClassCode to set
   */
  public void setParticipantClassCode(String value) {
    this.participantClassCode = value;
  }

  /**
   * @return the clientRevisionCount
   */
  public String getClientRevisionCount() {
    return clientRevisionCount;
  }

  /**
   * @param clientRevisionCount the clientRevisionCount to set
   */
  public void setClientRevisionCount(String clientRevisionCount) {
    this.clientRevisionCount = clientRevisionCount;
  }

}
