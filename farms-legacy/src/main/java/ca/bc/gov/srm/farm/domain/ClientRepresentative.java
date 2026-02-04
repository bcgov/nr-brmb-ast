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


/**
 * ClientRepresentative is a type of user who can access AgriStability
 * information data on behalf of specified clients . An
 * ClientRepresentative can be associated with one or more clients. An
 * ClientRepresentative may have an associated Person. An
 * ClientRepresentative will be identified from federal tax return data
 * or could be supplied by BC staff.
 *
 * @author awilkinson
 * @created Nov 18, 2010
 */
public final class ClientRepresentative implements Serializable {

  private static final long serialVersionUID = -6937636430055618237L;

  /** back-reference to the object containing this */
  @JsonBackReference
  private ClientSubscription clientSubscription;

  /**
   * representativeId is a surrogate unique identifier for
   * AgristabilityRepresentatives.
   */
  private Integer representativeId;


  /**
   * userGuid describes each user or group in a directory using a unique
   * identifer called a GUID or Globally Unique Identifier. The value is a 128
   * bit number but is displayed and passed around the netowork as a 32
   * character hex string. There is no guarantee that a user ID is unique.
   */
  private String userGuid;

  /**
   * userid is the user ID associated with the GUID that a user would use to log
   * on with. For this purpose only the username is stored in the record.
   */
  private String userid;

  /**
   * revisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   */
  private Integer revisionCount;


  /**
   * representativeId is a surrogate unique identifier for
   * AgristabilityRepresentatives.
   *
   * @return  Integer
   */
  public Integer getRepresentativeId() {
    return representativeId;
  }

  /**
   * representativeId is a surrogate unique identifier for
   * AgristabilityRepresentatives.
   *
   * @param  newVal  The new value for this property
   */
  public void setRepresentativeId(final Integer newVal) {
    representativeId = newVal;
  }

  /**
   * UserGuid describes each user or group in a directory using a unique
   * identifer called a GUID or Globally Unique Identifier. The value is a 128
   * bit number but is displayed and passed around the netowork as a 32
   * character hex string. There is no guarantee that a user ID is unique.
   *
   * @return  String
   */
  public String getUserGuid() {
    return userGuid;
  }

  /**
   * UserGuid describes each user or group in a directory using a unique
   * identifer called a GUID or Globally Unique Identifier. The value is a 128
   * bit number but is displayed and passed around the netowork as a 32
   * character hex string. There is no guarantee that a user ID is unique.
   *
   * @param  newVal  The new value for this property
   */
  public void setUserGuid(final String newVal) {
    userGuid = newVal;
  }

  /**
   * Userid is the user ID associated with the GUID that a user would use to log
   * on with. For this purpose only the username is stored in the record.
   *
   * @return  String
   */
  public String getUserid() {
    return userid;
  }

  /**
   * Userid is the user ID associated with the GUID that a user would use to log
   * on with. For this purpose only the username is stored in the record.
   *
   * @param  newVal  The new value for this property
   */
  public void setUserid(final String newVal) {
    userid = newVal;
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
   * @return the clientSubscription
   */
  public ClientSubscription getClientSubscription() {
    return clientSubscription;
  }

  /**
   * @param clientSubscription the clientSubscription to set the value to
   */
  public void setClientSubscription(ClientSubscription clientSubscription) {
    this.clientSubscription = clientSubscription;
  }

  /**
   * 
   * @return String
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString(){

    Integer clientSubscriptionId = null;
    if(clientSubscription != null) {
      clientSubscriptionId = clientSubscription.getClientSubscriptionId();
    }

    return "ClientRepresentative"+"\n"+
    "\t clientSubscription : "+clientSubscriptionId+"\n"+
    "\t representativeId : "+representativeId+"\n"+
    "\t revisionCount : "+revisionCount+"\n"+
    "\t userGuid : "+userGuid+"\n"+
    "\t userid : "+userid;
  }
}
