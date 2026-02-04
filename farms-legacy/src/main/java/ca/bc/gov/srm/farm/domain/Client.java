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
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Client is the taxable legal-entity (e.g. individuals, companies,
 * etc.) registered with the AgriStability program. This object refers to
 * identification and validation information, including program PIN.
 * Client data will originate from and be updated via federal data
 * imports - specifically tax return data. An Client will always be
 * associated with one or more ProgramYears. An Client may have
 * associated PersonS. Client may be associated with an
 * ClientRepresentative. Client information may be updated
 * by Provinical AgriStability Staff.
 *
 * @author awilkinson
 * @created Nov 12, 2010
 */
public class Client implements Serializable {
  
  private static final long serialVersionUID = -8549925701396677602L;

  /** back-reference to the object containing this */
  @JsonBackReference
  private Scenario scenario;

  /** The person to contact for questions to the tax return. */
  private Person contact;

  /** The farmer who will get the claim. */
  private Person owner;

  private List<ClientSubscription> clientSubscriptions;

  /**
   * clientId is a surrogate unique identifier for AGRI STABILITY
   * CLIENTs.
   */
  private Integer clientId;

  /**
   * federalIdentifier captures the participant Social Insurance Number, BN, or
   * Trust Number- A CTN/SBRN is 9 numbers followed by a SBRN extension of 6
   * numbers (total 15 numbers).- If a T is the first of the 9 characters this
   * identifies a trust or a commune.- The corporate tax number (CTN) is 8
   * numbers.- A Social Insurance Number for an individual is 9 numbers without
   * the extension.
   */
  private String federalIdentifier;
  
  private String sin;
  
  private String businessNumber;
  
  private String trustNumber;

  /**
   * participantPin (Personal Information Number) is the unique
   * AgriStability/AgriInvest pin for this producuer. Was previous CAIS PIN and
   * NISA PIN.
   */
  private Integer participantPin;

  /** isPublicOffice is the Public Office / AAFC Employee Indicator. */
  private Boolean isPublicOffice;

  /**
   * isLocallyUpdated identifies if the record was updated after being imported
   * into the system. If this value is 'Y', the client has updated the
   * AGRISTABILITY CLIENT information; as a result, the FARM system will not
   * allow the overwriting of that particular AGRISTABILITY CLIENT data by
   * subsequent imports.
   */
  private Boolean isLocallyUpdated;

  /**
   * participantClassCode is a unique code for the object participantClassCode
   * described as a numeric code used to uniquely identify the classification of
   * the farming operation. Examples of codes and descriptions are 1 = INDV, 2 =
   * CORP, or 3 = COOP.
   */
  private String participantClassCode;

  /** Description for participantClassCode. */
  private String participantClassCodeDescription;

  /**
   * participantLangCode is a unique code for the object ParticipantLanguageCode
   * described as a numeric code used to uniquely identify the preferred
   * language of the AgristabilityClient. Examples of codes and descriptions are
   * 1 - English, 2 - French.
   */
  private String participantLangCode;

  /** Description for participantLangCode. */
  private String participantLangCodeDescription;
  
  /**
   * identEffectiveDate is the date the address and name information was last updated.
   * This may apply to the participant information or the contact.
   */
  private Date identEffectiveDate;

  /**
   * revisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   */
  private Integer revisionCount;


  /**
   * @return the clientSubscriptions
   */
  public List<ClientSubscription> getClientSubscriptions() {
    return clientSubscriptions;
  }

  /**
   * @param clientSubscriptions the clientSubscriptions to set
   */
  public void setClientSubscriptions(List<ClientSubscription> clientSubscriptions) {
    if(clientSubscriptions != null) {
      for(ClientSubscription cur : clientSubscriptions) {
        cur.setClient(this);
      }
    }
    this.clientSubscriptions = clientSubscriptions;
  }

  /**
   * @return  the owner
   */
  public Person getOwner() {
    return owner;
  }

  /**
   * @param  owner  the owner to set
   */
  public void setOwner(Person owner) {
    if(owner != null) {
      owner.setClient(this);
    }
    this.owner = owner;
  }

  /**
   * @return  the contact
   */
  public Person getContact() {
    return contact;
  }

  /**
   * @param  contact  The new value for this property
   */
  public void setContact(Person contact) {
    if(contact != null) {
      contact.setClient(this);
    }
    this.contact = contact;
  }

  /**
   * clientId is a surrogate unique identifier for AGRISTABILITY CLIENTs.
   *
   * @return  Integer
   */
  public Integer getClientId() {
    return clientId;
  }

  /**
   * clientId is a surrogate unique identifier for AGRISTABILITY CLIENTs.
   *
   * @param  newVal  The new value for this property
   */
  public void setClientId(final Integer newVal) {
    clientId = newVal;
  }

  /**
   * FederalIdentifier captures the participant Social Insurance Number, BN, or
   * Trust Number- A CTN/SBRN is 9 numbers followed by a SBRN extension of 6
   * numbers (total 15 numbers).- If a T is the first of the 9 characters this
   * identifies a trust or a commune.- The corporate tax number (CTN) is 8
   * numbers.- A Social Insurance Number for an individual is 9 numbers without
   * the extension.
   *
   * @return  String
   */
  public String getFederalIdentifier() {
    return federalIdentifier;
  }

  /**
   * FederalIdentifier captures the participant Social Insurance Number, BN, or
   * Trust Number- A CTN/SBRN is 9 numbers followed by a SBRN extension of 6
   * numbers (total 15 numbers).- If a T is the first of the 9 characters this
   * identifies a trust or a commune.- The corporate tax number (CTN) is 8
   * numbers.- A Social Insurance Number for an individual is 9 numbers without
   * the extension.
   *
   * @param  newVal  The new value for this property
   */
  public void setFederalIdentifier(final String newVal) {
    federalIdentifier = newVal;
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
   * ParticipantPin (Personal Information Number) is the unique
   * AgriStability/AgriInvest pin for this producuer. Was previous CAIS PIN and
   * NISA PIN.
   *
   * @return  Integer
   */
  public Integer getParticipantPin() {
    return participantPin;
  }

  /**
   * ParticipantPin (Personal Information Number) is the unique
   * AgriStability/AgriInvest pin for this producuer. Was previous CAIS PIN and
   * NISA PIN.
   *
   * @param  newVal  The new value for this property
   */
  public void setParticipantPin(final Integer newVal) {
    participantPin = newVal;
  }

  /**
   * IsPublicOffice is the Public Office / AAFC Employee Indicator.
   *
   * @return  Boolean
   */
  public Boolean getIsPublicOffice() {
    return isPublicOffice;
  }

  /**
   * IsPublicOffice is the Public Office / AAFC Employee Indicator.
   *
   * @param  newVal  The new value for this property
   */
  public void setIsPublicOffice(final Boolean newVal) {
    isPublicOffice = newVal;
  }

  /**
   * ParticipantClassCode is a unique code for the object participantClassCode
   * described as a numeric code used to uniquely identify the classification of
   * the farming operation. Examples of codes and descriptions are 1 = INDV, 2 =
   * CORP, or 3 = COOP.
   *
   * @return  String
   */
  public String getParticipantClassCode() {
    return participantClassCode;
  }

  /**
   * ParticipantClassCode is a unique code for the object participantClassCode
   * described as a numeric code used to uniquely identify the classification of
   * the farming operation. Examples of codes and descriptions are 1 = INDV, 2 =
   * CORP, or 3 = COOP.
   *
   * @param  newVal  The new value for this property
   */
  public void setParticipantClassCode(final String newVal) {
    participantClassCode = newVal;
  }

  /**
   * Description for participantClassCode.
   *
   * @return  String
   */
  public String getParticipantClassCodeDescription() {
    return participantClassCodeDescription;
  }

  /**
   * Description for participantClassCode.
   *
   * @param  newVal  The new value for this property
   */
  public void setParticipantClassCodeDescription(final String newVal) {
    participantClassCodeDescription = newVal;
  }

  /**
   * ParticipantLangCode is a unique code for the object ParticipantLanguageCode
   * described as a numeric code used to uniquely identify the preferred
   * language of the AgristabilityClient. Examples of codes and descriptions are
   * 1 - English, 2 - French.
   *
   * @return  String
   */
  public String getParticipantLangCode() {
    return participantLangCode;
  }

  /**
   * ParticipantLangCode is a unique code for the object ParticipantLanguageCode
   * described as a numeric code used to uniquely identify the preferred
   * language of the AgristabilityClient. Examples of codes and descriptions are
   * 1 - English, 2 - French.
   *
   * @param  newVal  The new value for this property
   */
  public void setParticipantLangCode(final String newVal) {
    participantLangCode = newVal;
  }

  /**
   * Description for participantLangCode.
   *
   * @return  String
   */
  public String getParticipantLangCodeDescription() {
    return participantLangCodeDescription;
  }

  /**
   * Description for participantLangCode.
   *
   * @param  newVal  The new value for this property
   */
  public void setParticipantLangCodeDescription(final String newVal) {
    participantLangCodeDescription = newVal;
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
   * isLocallyUpdated identifies if the record was updated after being imported
   * into the system. If this value is 'Y', the client has updated the
   * AGRISTABILITY CLIENT information; as a result, the FARM system will not
   * allow the overwriting of that particular AGRISTABILITY CLIENT data by
   * subsequent imports.
   *
   * @return  the isLocallyUpdated
   */
  public Boolean getIsLocallyUpdated() {
    return isLocallyUpdated;
  }

  /**
   * isLocallyUpdated identifies if the record was updated after being imported
   * into the system. If this value is 'Y', the client has updated the
   * AGRISTABILITY CLIENT information; as a result, the FARM system will not
   * allow the overwriting of that particular AGRISTABILITY CLIENT data by
   * subsequent imports.
   *
   * @param  isLocallyUpdated  the isLocallyUpdated to set
   */
  public void setIsLocallyUpdated(final Boolean isLocallyUpdated) {
    this.isLocallyUpdated = isLocallyUpdated;
  }

  /**
   * identEffectiveDate is the date the address and name information was last updated.
   * This may apply to the participant information or the contact.
   * 
   * @return Date
   */
  public Date getIdentEffectiveDate() {
    return identEffectiveDate;
  }

  /**
   * identEffectiveDate is the date the address and name information was last updated.
   * This may apply to the participant information or the contact.
   * 
   * @param identEffectiveDate identEffectiveDate
   */
  public void setIdentEffectiveDate(Date identEffectiveDate) {
    this.identEffectiveDate = identEffectiveDate;
  }

  /**
   * @return the scenario
   */
  public Scenario getScenario() {
    return scenario;
  }

  /**
   * @param scenario the scenario to set the value to
   */
  public void setScenario(Scenario scenario) {
    this.scenario = scenario;
  }

  /**
   * 
   * @return String
   * @see Object#toString()
   */
  @Override
  public String toString(){

    Integer scenarioId = null;
    if(scenario != null) {
      scenarioId = scenario.getScenarioId();
    }

    Integer clientSubscriptionLenth = null;
    if(clientSubscriptions != null) {
      clientSubscriptionLenth = new Integer(clientSubscriptions.size());
    }

    return "AgristabilityClient"+"\n"+
    "\t participantPin : "+participantPin+"\n"+
    "\t scenario : "+scenarioId+"\n"+
    "\t clientId : "+clientId+"\n"+
    "\t federalIdentifier : "+federalIdentifier+"\n"+
    "\t isLocallyUpdated : "+isLocallyUpdated+"\n"+
    "\t isPublicOffice : "+isPublicOffice+"\n"+
    "\t participantClassCode : "+participantClassCode+"\n"+
    "\t participantClassCodeDescription : "+participantClassCodeDescription+"\n"+
    "\t participantLangCode : "+participantLangCode+"\n"+
    "\t participantLangCodeDescription : "+participantLangCodeDescription+"\n"+
    "\t revisionCount : "+revisionCount+"\n"+
    "\t owner : "+owner+"\n"+
    "\t contact : "+contact+"\n"+
    "\t clientSubscription : "+(clientSubscriptionLenth+"");
  }
}
