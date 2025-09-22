
package ca.bc.gov.srm.farm.domain.staging;

/**
 * Z50ParticipntBnftCalc identifies benefit calculations used to determine the
 * participants CAIS Benefits, including adjusted and unadjusted Margins, and
 * calculation type indicators. This data can only be provided for years FIPD
 * has processed. This file is created by FIPD. This is a staging object used to
 * load temporary data set before being merged into the operational data
 *
 * @author   Vivid Solutions Inc.
 * @version  1.0
 * @created  03-Jul-2009 2:07:18 PM
 */
public final class Z50ParticipntBnftCalc {

  /**
   * participantPin is the unique AgriStability/AgriInvest pin for this
   * prodcuer. Was previous CAIS Pin and NISA Pin.
   */
  private java.lang.Integer participantPin;

  /** programYear is the stabilization year for this record. */
  private java.lang.Integer programYear;

  /**
   * benefitCalcKey is the primary key for the file. Provides each row with a
   * unique identifier over the whole file.
   */
  private java.lang.Integer benefitCalcKey;

  /**
   * agristabilityStatus is the status of this participant's data for the
   * current year.
   */
  private java.lang.Integer agristabilityStatus;

  /** unadjustedReferenceMargin is initial calculated margin. */
  private java.lang.Double unadjustedReferenceMargin;

  /**
   * adjustedReferenceMargin is the final reference margin used, after
   * adjustments for Structure change, Combining, cash vs accrual changes, and
   * other adjustments.
   */
  private java.lang.Double adjustedReferenceMargin;

  /** programMargin is the ProgramYear margin. */
  private java.lang.Double programMargin;

  /**
   * isWholeFarm indicates this participants data was combined with another
   * participants data. Allowable values are Y - Yes, N - No.
   */
  private java.lang.Boolean isWholeFarm;

  /**
   * isStructureChange indicates if this partcipants data was modified because
   * of a structure change situation. Allowable values are Y - Yes, N - No.
   */
  private java.lang.Boolean isStructureChange;

  /**
   * structureChangeAdjAmount is the amount that affects the production margin
   * of a farm (or combined whole farm) due to a structure change.
   */
  private java.lang.Double structureChangeAdjAmount;

  /**
   * revisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   */
  private java.lang.Integer revisionCount;

  /** Constructor. */
  public Z50ParticipntBnftCalc() {

  }

  /**
   * ParticipantPin is the unique AgriStability/AgriInvest pin for this
   * prodcuer. Was previous CAIS Pin and NISA Pin.
   *
   * @return  java.lang.Integer
   */
  public java.lang.Integer getParticipantPin() {
    return participantPin;
  }

  /**
   * ParticipantPin is the unique AgriStability/AgriInvest pin for this
   * prodcuer. Was previous CAIS Pin and NISA Pin.
   *
   * @param  newVal  The new value for this property
   */
  public void setParticipantPin(final java.lang.Integer newVal) {
    participantPin = newVal;
  }

  /**
   * ProgramYear is the stabilization year for this record.
   *
   * @return  java.lang.Integer
   */
  public java.lang.Integer getProgramYear() {
    return programYear;
  }

  /**
   * ProgramYear is the stabilization year for this record.
   *
   * @param  newVal  The new value for this property
   */
  public void setProgramYear(final java.lang.Integer newVal) {
    programYear = newVal;
  }

  /**
   * BenefitCalcKey is the primary key for the file. Provides each row with a
   * unique identifier over the whole file.
   *
   * @return  java.lang.Integer
   */
  public java.lang.Integer getBenefitCalcKey() {
    return benefitCalcKey;
  }

  /**
   * BenefitCalcKey is the primary key for the file. Provides each row with a
   * unique identifier over the whole file.
   *
   * @param  newVal  The new value for this property
   */
  public void setBenefitCalcKey(final java.lang.Integer newVal) {
    benefitCalcKey = newVal;
  }

  /**
   * AgristabilityStatus is the status of this participant's data for the
   * current year.
   *
   * @return  java.lang.Integer
   */
  public java.lang.Integer getAgristabilityStatus() {
    return agristabilityStatus;
  }

  /**
   * AgristabilityStatus is the status of this participant's data for the
   * current year.
   *
   * @param  newVal  The new value for this property
   */
  public void setAgristabilityStatus(final java.lang.Integer newVal) {
    agristabilityStatus = newVal;
  }

  /**
   * UnadjustedReferenceMargin is initial calculated margin.
   *
   * @return  java.lang.Double
   */
  public java.lang.Double getUnadjustedReferenceMargin() {
    return unadjustedReferenceMargin;
  }

  /**
   * UnadjustedReferenceMargin is initial calculated margin.
   *
   * @param  newVal  The new value for this property
   */
  public void setUnadjustedReferenceMargin(final java.lang.Double newVal) {
    unadjustedReferenceMargin = newVal;
  }

  /**
   * AdjustedReferenceMargin is the final reference margin used, after
   * adjustments for Structure change, Combining, cash vs accrual changes, and
   * other adjustments.
   *
   * @return  java.lang.Double
   */
  public java.lang.Double getAdjustedReferenceMargin() {
    return adjustedReferenceMargin;
  }

  /**
   * AdjustedReferenceMargin is the final reference margin used, after
   * adjustments for Structure change, Combining, cash vs accrual changes, and
   * other adjustments.
   *
   * @param  newVal  The new value for this property
   */
  public void setAdjustedReferenceMargin(final java.lang.Double newVal) {
    adjustedReferenceMargin = newVal;
  }

  /**
   * ProgramMargin is the ProgramYear margin.
   *
   * @return  java.lang.Double
   */
  public java.lang.Double getProgramMargin() {
    return programMargin;
  }

  /**
   * ProgramMargin is the ProgramYear margin.
   *
   * @param  newVal  The new value for this property
   */
  public void setProgramMargin(final java.lang.Double newVal) {
    programMargin = newVal;
  }

  /**
   * IsWholeFarm indicates this participants data was combined with another
   * participants data. Allowable values are Y - Yes, N - No.
   *
   * @return  java.lang.Boolean
   */
  public java.lang.Boolean isWholeFarm() {
    return isWholeFarm;
  }

  /**
   * IsWholeFarm indicates this participants data was combined with another
   * participants data. Allowable values are Y - Yes, N - No.
   *
   * @param  newVal  The new value for this property
   */
  public void setIsWholeFarm(final java.lang.Boolean newVal) {
    isWholeFarm = newVal;
  }

  /**
   * IsStructureChange indicates if this partcipants data was modified because
   * of a structure change situation. Allowable values are Y - Yes, N - No.
   *
   * @return  java.lang.Boolean
   */
  public java.lang.Boolean isStructureChange() {
    return isStructureChange;
  }

  /**
   * IsStructureChange indicates if this partcipants data was modified because
   * of a structure change situation. Allowable values are Y - Yes, N - No.
   *
   * @param  newVal  The new value for this property
   */
  public void setIsStructureChange(final java.lang.Boolean newVal) {
    isStructureChange = newVal;
  }

  /**
   * StructureChangeAdjAmount is the amount that affects the production margin
   * of a farm (or combined whole farm) due to a structure change.
   *
   * @return  java.lang.Double
   */
  public java.lang.Double getStructureChangeAdjAmount() {
    return structureChangeAdjAmount;
  }

  /**
   * StructureChangeAdjAmount is the amount that affects the production margin
   * of a farm (or combined whole farm) due to a structure change.
   *
   * @param  newVal  The new value for this property
   */
  public void setStructureChangeAdjAmount(final java.lang.Double newVal) {
    structureChangeAdjAmount = newVal;
  }

  /**
   * RevisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   *
   * @return  java.lang.Integer
   */
  public java.lang.Integer getRevisionCount() {
    return revisionCount;
  }

  /**
   * RevisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   *
   * @param  newVal  The new value for this property
   */
  public void setRevisionCount(final java.lang.Integer newVal) {
    revisionCount = newVal;
  }

}
