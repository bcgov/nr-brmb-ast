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
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.fasterxml.jackson.annotation.JsonBackReference;

import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * FarmingOperationPartner lists all the farming partners the producer has
 * entered in section 6 of the Harmonized form. This should not include the
 * participant. Even if the participant is in a partnerhip, there is no
 * requirement to submit a list of participants, so this file may not have any
 * data for that statement. This file will be sent to the provinces by FIPD.
 *
 * @author   Vivid Solutions Inc.
 * @version  1.0
 * @created  03-Jul-2009 2:06:52 PM
 */
public final class FarmingOperationPartner implements Serializable {
  
  private static final long serialVersionUID = 3169612213008460610L;

  /** back-reference to the object containing this */
  @JsonBackReference
  private FarmingOperation farmingOperation;

  /**
   * farmingOperationPartnerId is a surrogate unique identifier for
   * FarmingOperation PARTNERs.
   */
  private Integer farmingOperationPartnerId;

  /** partnerPercent is the Partner's Percentage Share. */
  private BigDecimal partnerPercent;

  /**
   * participantPin uniquely identifies the participant who is the partner.
   */
  private Integer participantPin;

  /** partnerSin or CTN or BN - see participant SIN/CTN/BN for valid formats. */
  private String partnerSin;

  /** firstName of the contact. */
  private String firstName;

  /** lastName of the contact. */
  private String lastName;

  /** corpName is the Contact Business Name, as provided by the participant. */
  private String corpName;

  /**
   * revisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   */
  private Integer revisionCount;

  /** Constructor. */
  public FarmingOperationPartner() {

  }

  /**
   * farmingOperationPartnerId is a surrogate unique identifier for
   * FarmingOperation PARTNERs.
   *
   * @return  Integer
   */
  public Integer getFarmingOperationPartnerId() {
    return farmingOperationPartnerId;
  }

  /**
   * farmingOperationPartnerId is a surrogate unique identifier for
   * FarmingOperation PARTNERs.
   *
   * @param  newVal  The new value for this property
   */
  public void setFarmingOperationPartnerId(final Integer newVal) {
    farmingOperationPartnerId = newVal;
  }

  /**
   * PartnerPercent is the Partner's Percentage Share.
   *
   * @return  Double
   */
  public BigDecimal getPartnerPercent() {
    return partnerPercent;
  }

  /**
   * PartnerPercent is the Partner's Percentage Share.
   *
   * @param  newVal  The new value for this property
   */
  public void setPartnerPercent(final BigDecimal newVal) {
    partnerPercent = newVal;
  }

  /**
   * PartnershipPin uniquely identifies the partnership. If both the partners in
   * an operation file applications, the same partnershipPin will show up under
   * both pins. partnershipPins represent the same operation if/when they are
   * used in different ProgramYearS.
   *
   * @return  Integer
   */
  public Integer getParticipantPin() {
    return participantPin;
  }

  /**
   * PartnershipPin uniquely identifies the partnership. If both the partners in
   * an operation file applications, the same partnershipPin will show up under
   * both pins. partnershipPins represent the same operation if/when they are
   * used in different ProgramYearS.
   *
   * @param  newVal  The new value for this property
   */
  public void setParticipantPin(final Integer newVal) {
    participantPin = newVal;
  }

  /**
   * PartnerSin or CTN or BN - see participant SIN/CTN/BN for valid formats.
   *
   * @return  String
   */
  public String getPartnerSin() {
    return partnerSin;
  }

  /**
   * PartnerSin or CTN or BN - see participant SIN/CTN/BN for valid formats.
   *
   * @param  newVal  The new value for this property
   */
  public void setPartnerSin(final String newVal) {
    partnerSin = newVal;
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
   * @return the farmingOperation
   */
  public FarmingOperation getFarmingOperation() {
    return farmingOperation;
  }

  /**
   * @param farmingOperation the farmingOperation to set the value to
   */
  public void setFarmingOperation(FarmingOperation farmingOperation) {
    this.farmingOperation = farmingOperation;
  }
  
  public String getDisplayName() {
    String result;
    if(StringUtils.isNotBlank(corpName)) {
      result = corpName;
    } else {
      result = lastName + ", " + firstName;
    }
    return result;
  }
  
  public BigDecimal getPartnerPercent100() {
    return partnerPercent.multiply(BigDecimal.TEN.pow(2)).setScale(2, RoundingMode.HALF_UP);
  }

  /**
   * 
   * @return String
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString(){

    Integer farmingOperationId = null;
    if(farmingOperation != null) {
      farmingOperationId = farmingOperation.getFarmingOperationId();
    }

    return "FarmingOperationPartner"+"\n"+
    "\t farmingOperation : "+farmingOperationId+"\n"+
    "\t farmingOperationPartnerId : "+farmingOperationPartnerId+"\n"+
    "\t partnerPercent : "+partnerPercent+"\n"+
    "\t participantPin : "+participantPin+"\n"+
    "\t partnerSin : "+partnerSin+"\n"+
    "\t firstName : "+firstName+"\n"+
    "\t lastName : "+lastName+"\n"+
    "\t corpName : "+corpName+"\n"+
    "\t revisionCount : "+revisionCount;
  }
}
