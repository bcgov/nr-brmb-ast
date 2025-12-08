
package ca.bc.gov.srm.farm.domain.staging;

/**
 * Z23LivestockProdCpct identifies the Livestock production capacity information
 * provided by the participant in section 9 of the supplemental page of the
 * AgriStability application. This file will have 0 to many rows per participant
 * and farming operation.This file is created by FIPD. This is a staging object
 * used to load temporary data set before being merged into the operational data
 *
 * @author   Vivid Solutions Inc.
 * @version  1.0
 * @created  03-Jul-2009 2:07:14 PM
 */
public final class Z23LivestockProdCpct {

  /**
   * participantPin is the unique AgriStability/AgriInvest pin for this
   * prodcuer. Was previous CAIS Pin and NISA Pin.
   */
  private java.lang.Integer participantPin;

  /** programYear is the stabilization year for this record. */
  private java.lang.Integer programYear;

  /**
   * operationNumber identifies each operation a participant reports for a given
   * stab year. Operations may have different statement numbers in different
   * program years.
   */
  private java.lang.Integer operationNumber;

  /**
   * productiveCapacityKey is the primary key for the file. Provides each row
   * with a unique identifier over the whole file.
   */
  private java.lang.Integer productiveCapacityKey;

  /**
   * inventoryCode is a numeric code used to uniquely identify an inventory
   * item.
   */
  private java.lang.Integer inventoryCode;

  /**
   * productiveCapacityAmount is the quantity entered in section 9 for this
   * inventory/bpu code.
   */
  private java.lang.Double productiveCapacityAmount;

  /**
   * revisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   */
  private java.lang.Integer revisionCount;

  /** Constructor. */
  public Z23LivestockProdCpct() {

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
   * OperationNumber identifies each operation a participant reports for a given
   * stab year. Operations may have different statement numbers in different
   * program years.
   *
   * @return  java.lang.Integer
   */
  public java.lang.Integer getOperationNumber() {
    return operationNumber;
  }

  /**
   * OperationNumber identifies each operation a participant reports for a given
   * stab year. Operations may have different statement numbers in different
   * program years.
   *
   * @param  newVal  The new value for this property
   */
  public void setOperationNumber(final java.lang.Integer newVal) {
    operationNumber = newVal;
  }

  /**
   * ProductiveCapacityKey is the primary key for the file. Provides each row
   * with a unique identifier over the whole file.
   *
   * @return  java.lang.Integer
   */
  public java.lang.Integer getProductiveCapacityKey() {
    return productiveCapacityKey;
  }

  /**
   * ProductiveCapacityKey is the primary key for the file. Provides each row
   * with a unique identifier over the whole file.
   *
   * @param  newVal  The new value for this property
   */
  public void setProductiveCapacityKey(final java.lang.Integer newVal) {
    productiveCapacityKey = newVal;
  }

  /**
   * InventoryCode is a numeric code used to uniquely identify an inventory
   * item.
   *
   * @return  java.lang.Integer
   */
  public java.lang.Integer getInventoryCode() {
    return inventoryCode;
  }

  /**
   * InventoryCode is a numeric code used to uniquely identify an inventory
   * item.
   *
   * @param  newVal  The new value for this property
   */
  public void setInventoryCode(final java.lang.Integer newVal) {
    inventoryCode = newVal;
  }

  /**
   * ProductiveCapacityAmount is the quantity entered in section 9 for this
   * inventory/bpu code.
   *
   * @return  java.lang.Double
   */
  public java.lang.Double getProductiveCapacityAmount() {
    return productiveCapacityAmount;
  }

  /**
   * ProductiveCapacityAmount is the quantity entered in section 9 for this
   * inventory/bpu code.
   *
   * @param  newVal  The new value for this property
   */
  public void setProductiveCapacityAmount(final java.lang.Double newVal) {
    productiveCapacityAmount = newVal;
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
