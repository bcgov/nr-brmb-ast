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

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * ClientSubscription belongs to a Client and has one instances of ClientRepresentative.
 *
 * @author awilkinson
 * @created Nov 18, 2010
 */
public final class ClientSubscription implements Serializable {

  private static final long serialVersionUID = 1461459075545221869L;

  /** back-reference to the object containing this */
  @JsonBackReference
  private Client client;
  
  private ClientRepresentative representative;

  /**
   * clientSubscriptionId is a surrogate unique identifier for
   * ClientSubscriptions.
   */
  private Integer clientSubscriptionId;

  /**
   * subscriptionNumber is the unique value issued to a user of the application
   * to facilitate the user registration process.
   */
  private String subscriptionNumber;

  /** the date the subscription was created. */
  private Date generatedDate;

  /** the date the subscription was activated. */
  private Date activatedDate;

  /** the date the subscription can no longer be activated. */
  private Date activationExpiryDate;

  /** subscription Status. */
  private String subscriptionStatusCode;

  /** subscription status description. */
  private String subscriptionStatusCodeDescription;

  /** who generated the subscrition. */
  private String generatedByUserid;

  /** who activated the subscrition. */
  private String activatedByUserid;
  
  
  /** who generated the subscrition. */
  private String javascriptGeneratedByUserid;

  /** who activated the subscrition. */
  private String javascriptActivatedByUserid;

  /**
   * revisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   */
  private Integer revisionCount;


  /**
   * ClientSubscriptionId is a surrogate unique identifier for
   * ClientSubscriptions.
   *
   * @return  Integer
   */
  public Integer getClientSubscriptionId() {
    return clientSubscriptionId;
  }

  /**
   * ClientSubscriptionId is a surrogate unique identifier
   * forClientSubscriptions.
   *
   * @param  newVal  The new value for this property
   */
  public void setClientSubscriptionId(final Integer newVal) {
    clientSubscriptionId = newVal;
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
   * SubscriptionNumber is the unique value issued to a user of the application
   * to facilitate the user registration process.
   *
   * @return  String
   */
  public String getSubscriptionNumber() {
    return subscriptionNumber;
  }

  /**
   * SubscriptionNumber is the unique value issued to a user of the application
   * to facilitate the user registration process.
   *
   * @param  newVal  The new value for this property
   */
  public void setSubscriptionNumber(final String newVal) {
    subscriptionNumber = newVal;
  }

  /**
   * @return  the activatedDate
   */
  public Date getActivatedDate() {
    return activatedDate;
  }

  /**
   * @param  newVal  the activatedDate to set
   */
  public void setActivatedDate(final Date newVal) {
    this.activatedDate = newVal;
  }

  /**
   * @return  the activationExpiryDate
   */
  public Date getActivationExpiryDate() {
    return activationExpiryDate;
  }

  /**
   * @param  newVal  the activationExpiryDate to set
   */
  public void setActivationExpiryDate(final Date newVal) {
    this.activationExpiryDate = newVal;
  }

  /**
   * @return  the generatedDate
   */
  public Date getGeneratedDate() {
    return generatedDate;
  }

  /**
   * @param  newVal  the generatedDate to set
   */
  public void setGeneratedDate(final Date newVal) {
    this.generatedDate = newVal;
  }

  /**
   * @return  the subscriptionStatusCode
   */
  public String getSubscriptionStatusCode() {
    return subscriptionStatusCode;
  }

  /**
   * @param  newVal  the subscriptionStatusCode to set
   */
  public void setSubscriptionStatusCode(final String newVal) {
    this.subscriptionStatusCode = newVal;
  }

  /**
   * @return  the subscriptionStatusCodeDescription
   */
  public String getSubscriptionStatusCodeDescription() {
    return subscriptionStatusCodeDescription;
  }

  /**
   * @param  newVal  the subscriptionStatusCodeDescription to set
   */
  public void setSubscriptionStatusCodeDescription(final String newVal) {
    this.subscriptionStatusCodeDescription = newVal;
  }

  /**
   * @return  the generatedByUserid
   */
  public String getGeneratedByUserid() {
    return generatedByUserid;
  }

  /**
   * @param  newVal  the generatedByUserid to set
   */
  public void setGeneratedByUserid(final String newVal) {
    this.generatedByUserid = newVal;
  }

  /**
   * @return  the activatedByUserid
   */
  public String getActivatedByUserid() {
    return activatedByUserid;
  }

  /**
   * @param  newVal  the activatedByUserid to set
   */
  public void setActivatedByUserid(final String newVal) {
    this.activatedByUserid = newVal;
  }
  
  

  /**
   * @return the javascriptActivatedByUserid
   */
  public String getJavascriptActivatedByUserid() {
    return javascriptActivatedByUserid;
  }

  /**
   * @param javascriptActivatedByUserid the javascriptActivatedByUserid to set
   */
  public void setJavascriptActivatedByUserid(String javascriptActivatedByUserid) {
    this.javascriptActivatedByUserid = javascriptActivatedByUserid;
  }

  /**
   * @return the javascriptGeneratedByUserid
   */
  public String getJavascriptGeneratedByUserid() {
    return javascriptGeneratedByUserid;
  }

  /**
   * @param javascriptGeneratedByUserid the javascriptGeneratedByUserid to set
   */
  public void setJavascriptGeneratedByUserid(String javascriptGeneratedByUserid) {
    this.javascriptGeneratedByUserid = javascriptGeneratedByUserid;
  }

  /**
   * @return the representative
   */
  public ClientRepresentative getRepresentative() {
    return representative;
  }

  /**
   * @param representative the representative to set
   */
  public void setRepresentative(ClientRepresentative representative) {
    if(representative != null) {
      representative.setClientSubscription(this);
    }
    this.representative = representative;
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

    return "ClientSubscription"+"\n"+
    "\t client : "+clientId+"\n"+
    "\t clientSubscriptionId : "+clientSubscriptionId+"\n"+
    "\t subscriptionNumber : "+subscriptionNumber+"\n"+
    "\t generatedDate : "+generatedDate+"\n"+
    "\t activatedDate : "+activatedDate+"\n"+
    "\t activationExpiryDate : "+activationExpiryDate+"\n"+
    "\t subscriptionStatusCode : "+subscriptionStatusCode+"\n"+
    "\t subscriptionStatusCodeDescription : "+subscriptionStatusCodeDescription+"\n"+
    "\t generatedByUserid : "+generatedByUserid+"\n"+
    "\t activatedByUserid : "+activatedByUserid+"\n"+
    "\t revisionCount : "+revisionCount+"\n"+
    "\t representative : "+representative;
  }
}
