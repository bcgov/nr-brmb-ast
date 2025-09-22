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

/**
 * WholeFarmParticipant describes the combined PINS used by the FarmingOperation
 * for a given ProgramYear VERSION.
 *
 * @author   Vivid Solutions Inc.
 * @created  03-Jul-2009 2:07:03 PM
 */
public final class WholeFarmParticipant implements Serializable {
  
  private static final long serialVersionUID = -2234788350637054713L;

  /** back-reference to the object containing this */
  private FarmingYear farmingYear;

  /** wholeFarmParticipantId is a surrogate unique identifier for WHOLE FARM. */
  private Integer wholeFarmParticipantId;

  /**
   * wholeFarmCombinedPin of Participant that has been combined with the
   * participant on this form for whole farms calculation, see Section 2 -
   * Participant Profile.
   */
  private Integer wholeFarmCombinedPin;

  /**
   * isWholeFarmCombPinAdd of Participant that has been combined with the
   * participant on this form for whole farms calculation, see Section 2 -
   * Participant Profile.
   */
  private Boolean isWholeFarmCombPinAdd;

  /**
   * isWholeFarmCombPinRemove should be removed from the PINs combined with this
   * participant for whole farms.
   */
  private Boolean isWholeFarmCombPinRemove;

  /**
   * revisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   */
  private Integer revisionCount;

  /** Constructor. */
  public WholeFarmParticipant() {

  }

  /**
   * WholeFarmParticipantId is a surrogate unique identifier for WHOLE FARM.
   *
   * @return  Integer
   */
  public Integer getWholeFarmParticipantId() {
    return wholeFarmParticipantId;
  }

  /**
   * WholeFarmParticipantId is a surrogate unique identifier for WHOLE FARM.
   *
   * @param  newVal  The new value for this property
   */
  public void setWholeFarmParticipantId(final Integer newVal) {
    wholeFarmParticipantId = newVal;
  }

  /**
   * WholeFarmCombinedPin of Participant that has been combined with the
   * participant on this form for whole farms calculation, see Section 2 -
   * Participant Profile.
   *
   * @return  Integer
   */
  public Integer getWholeFarmCombinedPin() {
    return wholeFarmCombinedPin;
  }

  /**
   * WholeFarmCombinedPin of Participant that has been combined with the
   * participant on this form for whole farms calculation, see Section 2 -
   * Participant Profile.
   *
   * @param  newVal  The new value for this property
   */
  public void setWholeFarmCombinedPin(final Integer newVal) {
    wholeFarmCombinedPin = newVal;
  }

  /**
   * IsWholeFarmCombPinAdd of Participant that has been combined with the
   * participant on this form for whole farms calculation, see Section 2 -
   * Participant Profile.
   *
   * @return  Boolean
   */
  public Boolean isWholeFarmCombPinAdd() {
    return isWholeFarmCombPinAdd;
  }

  /**
   * IsWholeFarmCombPinAdd of Participant that has been combined with the
   * participant on this form for whole farms calculation, see Section 2 -
   * Participant Profile.
   *
   * @param  newVal  The new value for this property
   */
  public void setIsWholeFarmCombPinAdd(final Boolean newVal) {
    isWholeFarmCombPinAdd = newVal;
  }

  /**
   * IsWholeFarmCombPinRemove should be removed from the PINs combined with this
   * participant for whole farms.
   *
   * @return  Boolean
   */
  public Boolean isWholeFarmCombPinRemove() {
    return isWholeFarmCombPinRemove;
  }

  /**
   * IsWholeFarmCombPinRemove should be removed from the PINs combined with this
   * participant for whole farms.
   *
   * @param  newVal  The new value for this property
   */
  public void setIsWholeFarmCombPinRemove(final Boolean newVal) {
    isWholeFarmCombPinRemove = newVal;
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
   * @return the farmingYear
   */
  public FarmingYear getFarmingYear() {
    return farmingYear;
  }

  /**
   * @param farmingYear the farmingYear to set the value to
   */
  public void setFarmingYear(FarmingYear farmingYear) {
    this.farmingYear = farmingYear;
  }

  /**
   * 
   * @return String
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString(){
    
    Integer farmingYearId = null;
    if(farmingYear != null) {
      farmingYearId = farmingYear.getProgramYearId();
    }
    
    return "WholeFarmParticipant "+"\n"+
    "\t farmingYear : "+farmingYearId+"\n"+
    "\t isWholeFarmCombPinAdd : "+isWholeFarmCombPinAdd+"\n"+
    "\t isWholeFarmCombPinRemove : "+isWholeFarmCombPinRemove+"\n"+
    "\t revisionCount : "+revisionCount+"\n"+
    "\t wholeFarmCombinedPin : "+wholeFarmCombinedPin+"\n"+
    "\t wholeFarmParticipantId : "+wholeFarmParticipantId;
  }
}
