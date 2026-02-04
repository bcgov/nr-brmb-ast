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

import java.io.Serializable;

/**
 * @author awilkinson
 */
public class PersonFormData implements Serializable {

  private static final long serialVersionUID = -2645502921365403101L;

  /** addressLine1 is the first line of the Contact Mailing Address. */
  private String addressLine1;

  /** addressLine2 is the second line of the Contact Mailing Address. */
  private String addressLine2;

  /** city is the Contact Mailing Address City. */
  private String city;

  /** corpName is the Contact Business Name, as provided by the participant. */
  private String corpName;

  /** daytimePhone is the day time telephone number of the participant. */
  private String daytimePhone;

  /** eveningPhone is the evening telephone number of the participant. */
  private String eveningPhone;

  /** faxNumber is the faxNumber of the participant. */
  private String faxNumber;
  
  private String cellNumber;

  /** firstName of the contact. */
  private String firstName;

  /** lastName of the contact. */
  private String lastName;

  /** postalCode for the Contact Mailing Address. */
  private String postalCode;

  /** provinceState for the contact mailing address. */
  private String provinceState;

  /** country for the contact mailing address. */
  private String country;
  
  private String emailAddress;
  
  private String revisionCount;

  /**
   * @return the addressLine1
   */
  public String getAddressLine1() {
    return addressLine1;
  }

  /**
   * @param addressLine1 the addressLine1 to set
   */
  public void setAddressLine1(String addressLine1) {
    this.addressLine1 = addressLine1;
  }

  /**
   * @return the addressLine2
   */
  public String getAddressLine2() {
    return addressLine2;
  }

  /**
   * @param addressLine2 the addressLine2 to set
   */
  public void setAddressLine2(String addressLine2) {
    this.addressLine2 = addressLine2;
  }

  /**
   * @return the city
   */
  public String getCity() {
    return city;
  }

  /**
   * @param city the city to set
   */
  public void setCity(String city) {
    this.city = city;
  }

  /**
   * @return the corpName
   */
  public String getCorpName() {
    return corpName;
  }

  /**
   * @param corpName the corpName to set
   */
  public void setCorpName(String corpName) {
    this.corpName = corpName;
  }

  /**
   * @return the country
   */
  public String getCountry() {
    return country;
  }

  /**
   * @param country the country to set
   */
  public void setCountry(String country) {
    this.country = country;
  }

  /**
   * @return the daytimePhone
   */
  public String getDaytimePhone() {
    return daytimePhone;
  }

  /**
   * @param daytimePhone the daytimePhone to set
   */
  public void setDaytimePhone(String daytimePhone) {
    this.daytimePhone = daytimePhone;
  }

  /**
   * @return the eveningPhone
   */
  public String getEveningPhone() {
    return eveningPhone;
  }

  /**
   * @param eveningPhone the eveningPhone to set
   */
  public void setEveningPhone(String eveningPhone) {
    this.eveningPhone = eveningPhone;
  }

  /**
   * @return the faxNumber
   */
  public String getFaxNumber() {
    return faxNumber;
  }

  /**
   * @param faxNumber the faxNumber to set
   */
  public void setFaxNumber(String faxNumber) {
    this.faxNumber = faxNumber;
  }

  public String getCellNumber() {
    return cellNumber;
  }

  public void setCellNumber(String cellNumber) {
    this.cellNumber = cellNumber;
  }

  /**
   * @return the firstName
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * @param firstName the firstName to set
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * @return the lastName
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * @param lastName the lastName to set
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * @return the postalCode
   */
  public String getPostalCode() {
    return postalCode;
  }

  /**
   * @param postalCode the postalCode to set
   */
  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  /**
   * @return the provinceState
   */
  public String getProvinceState() {
    return provinceState;
  }

  /**
   * @param provinceState the provinceState to set
   */
  public void setProvinceState(String provinceState) {
    this.provinceState = provinceState;
  }

  /**
   * @return the revisionCount
   */
  public String getRevisionCount() {
    return revisionCount;
  }

  /**
   * @param revisionCount the revisionCount to set
   */
  public void setRevisionCount(String revisionCount) {
    this.revisionCount = revisionCount;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }
  
}
