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
package ca.bc.gov.srm.farm.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * Person contains address and contact information for a Client or
 * ClientRepresentative.
 *
 * @author   Vivid Solutions Inc.
 * @version  1.0
 * @created  03-Jul-2009 2:06:54 PM
 */
public final class Person implements Serializable {

  private static final long serialVersionUID = -4898244330552906433L;

  /** back-reference to the object containing this */
  @JsonBackReference
  private Client client;

  /** personId is a surrogate unique identifier for Persons. */
  private Integer personId;

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

  /**
   * revisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   */
  private Integer revisionCount;


  /** Constructor. */
  public Person() {

  }


  /**
   * PersonId is a surrogate unique identifier for Persons.
   *
   * @return  Integer
   */
  public Integer getPersonId() {
    return personId;
  }

  /**
   * PersonId is a surrogate unique identifier for Persons.
   *
   * @param  newVal  The new value for this property
   */
  public void setPersonId(final Integer newVal) {
    personId = newVal;
  }

  /**
   * AddressLine1 is the first line of the Contact Mailing Address.
   *
   * @return  String
   */
  public String getAddressLine1() {
    return addressLine1;
  }

  /**
   * AddressLine1 is the first line of the Contact Mailing Address.
   *
   * @param  newVal  The new value for this property
   */
  public void setAddressLine1(final String newVal) {
    addressLine1 = newVal;
  }

  /**
   * AddressLine2 is the second line of the Contact Mailing Address.
   *
   * @return  String
   */
  public String getAddressLine2() {
    return addressLine2;
  }

  /**
   * AddressLine2 is the second line of the Contact Mailing Address.
   *
   * @param  newVal  The new value for this property
   */
  public void setAddressLine2(final String newVal) {
    addressLine2 = newVal;
  }

  /**
   * City is the Contact Mailing Address City.
   *
   * @return  String
   */
  public String getCity() {
    return city;
  }

  /**
   * City is the Contact Mailing Address City.
   *
   * @param  newVal  The new value for this property
   */
  public void setCity(final String newVal) {
    city = newVal;
  }

  /**
   * CorpName is the Contact Business Name, as provided by the participant.
   *
   * @return  String
   */
  public String getCorpName() {
    return corpName;
  }

  /**
   * CorpName is the Contact Business Name, as provided by the participant.
   *
   * @param  newVal  The new value for this property
   */
  public void setCorpName(final String newVal) {
    corpName = newVal;
  }

  /**
   * DaytimePhone is the day time telephone number of the participant.
   *
   * @return  String
   */
  public String getDaytimePhone() {
    return daytimePhone;
  }

  /**
   * DaytimePhone is the day time telephone number of the participant.
   *
   * @param  newVal  The new value for this property
   */
  public void setDaytimePhone(final String newVal) {
    daytimePhone = newVal;
  }

  /**
   * EveningPhone is the evening telephone number of the participant.
   *
   * @return  String
   */
  public String getEveningPhone() {
    return eveningPhone;
  }

  /**
   * EveningPhone is the evening telephone number of the participant.
   *
   * @param  newVal  The new value for this property
   */
  public void setEveningPhone(final String newVal) {
    eveningPhone = newVal;
  }

  /**
   * FaxNumber is the faxNumber of the participant.
   *
   * @return  String
   */
  public String getFaxNumber() {
    return faxNumber;
  }

  /**
   * FaxNumber is the faxNumber of the participant.
   *
   * @param  newVal  The new value for this property
   */
  public void setFaxNumber(final String newVal) {
    faxNumber = newVal;
  }

  public String getCellNumber() {
    return cellNumber;
  }


  public void setCellNumber(String cellNumber) {
    this.cellNumber = cellNumber;
  }

  /**
   * FirstName of the contact.
   *
   * @return  String
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * FirstName of the contact.
   *
   * @param  newVal  The new value for this property
   */
  public void setFirstName(final String newVal) {
    firstName = newVal;
  }

  /**
   * LastName of the contact.
   *
   * @return  String
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * LastName of the contact.
   *
   * @param  newVal  The new value for this property
   */
  public void setLastName(final String newVal) {
    lastName = newVal;
  }

  /**
   * PostalCode for the Contact Mailing Address.
   *
   * @return  String
   */
  public String getPostalCode() {
    return postalCode;
  }

  /**
   * PostalCode for the Contact Mailing Address.
   *
   * @param  newVal  The new value for this property
   */
  public void setPostalCode(final String newVal) {
    postalCode = newVal;
  }

  /**
   * ProvinceState for the contact mailing address.
   *
   * @return  String
   */
  public String getProvinceState() {
    return provinceState;
  }

  /**
   * ProvinceState for the contact mailing address.
   *
   * @param  newVal  The new value for this property
   */
  public void setProvinceState(final String newVal) {
    provinceState = newVal;
  }


  /**
   * RevisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   *
   * @return  Integer
   */
  public Integer getRevisionCount() {
    return revisionCount;
  }

  /**
   * RevisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   *
   * @param  newVal  The new value for this property
   */
  public void setRevisionCount(final Integer newVal) {
    revisionCount = newVal;
  }


  /**
   * @return  the country
   */
  public String getCountry() {
    return country;
  }


  /**
   * @param  country  the country to set
   */
  public void setCountry(String country) {
    this.country = country;
  }


  /**
   * @return the client
   */
  public Client getClient() {
    return client;
  }


  /**
   * @param client the client to set the value to
   */
  public void setClient(Client client) {
    this.client = client;
  }


  /**
   * @return  Rather than having N JSPs format the name, we should just do it
   *          once here. The "Person" table also holds corporations.
   */
  @JsonIgnore
  public String getFullName() {
    String name = corpName;

    if (StringUtils.isBlank(name)) {
      name = firstName + " " + lastName;
    }

    return name;
  }
  
  /**
   * Get daytimePhone formatted for display.
   * @return daytimePhone formatted for display
   */
  @JsonIgnore
  public String getDaytimePhoneDisplay() {
    return StringUtils.formatPhoneNumber(daytimePhone);
  }
  
  /**
   * Get eveningPhone formatted for display.
   * @return eveningPhone formatted for display
   */
  @JsonIgnore
  public String getEveningPhoneDisplay() {
    return StringUtils.formatPhoneNumber(eveningPhone);
  }
  
  /**
   * Get faxNumber formatted for display.
   * @return faxNumber formatted for display
   */
  @JsonIgnore
  public String getFaxNumberDisplay() {
    return StringUtils.formatPhoneNumber(faxNumber);
  }
  
  /**
   * Get cellNumber formatted for display.
   * @return cellNumber formatted for display
   */
  @JsonIgnore
  public String getCellNumberDisplay() {
    return StringUtils.formatPhoneNumber(cellNumber);
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  /**
   * 
   * @return String
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString(){

    Integer clientId = null;
    if(client != null) {
      clientId = client.getClientId();
    }

    return "Person"+"\n"+
    "\t client : "+clientId+"\n"+
    "\t personId : "+personId+"\n"+
    "\t addressLine1 : "+addressLine1+"\n"+
    "\t addressLine2 : "+addressLine2+"\n"+
    "\t city : "+city+"\n"+
    "\t corpName : "+corpName+"\n"+
    "\t daytimePhone : "+daytimePhone+"\n"+
    "\t eveningPhone : "+eveningPhone+"\n"+
    "\t faxNumber : "+faxNumber+"\n"+
    "\t cellNumber : "+cellNumber+"\n"+
    "\t firstName : "+firstName+"\n"+
    "\t lastName : "+lastName+"\n"+
    "\t postalCode : "+postalCode+"\n"+
    "\t provinceState : "+provinceState+"\n"+
    "\t country : "+country+"\n"+
    "\t emailAddress : "+emailAddress+"\n"+
    "\t revisionCount : "+revisionCount;
  }
}
