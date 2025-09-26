
package ca.bc.gov.srm.farm.domain.staging;

/**
 * Z51ParticipantContrib identifies contributions by the participant for the
 * program year, broken out by type. This data can only be provided for years
 * FIPD has processed. This file is created by FIPD. This is a staging object
 * used to load temporary data set before being merged into the operational data
 *
 * @author   Vivid Solutions Inc.
 * @version  1.0
 * @created  03-Jul-2009 2:07:19 PM
 */
public final class Z51ParticipantContrib {

  /**
   * participantPin is the unique AgriStability/AgriInvest pin for this
   * prodcuer. Was previous CAIS Pin and NISA Pin.
   */
  private java.lang.Integer participantPin;

  /** programYear is the stabilization year for this record. */
  private java.lang.Integer programYear;

  /**
   * contributionKey is the primary key for the file. Provides each row with a
   * unique identifier over the whole file.
   */
  private java.lang.Integer contributionKey;

  /**
   * provincialContributions is the amount of provincialContributions this
   * participant has received.
   */
  private java.lang.Double provincialContributions;

  /**
   * federalContributions is the amount of federalContributions this participant
   * has received.
   */
  private java.lang.Double federalContributions;

  /**
   * interimContributions is the amount of interimContributions for this
   * participant, if a final calculation is not yet been made. If a final
   * calculation has been made, this amount will be 0.
   */
  private java.lang.Double interimContributions;

  /**
   * producerShare is the amount of AgriStability withdrawals that are
   * producer's share.
   */
  private java.lang.Double producerShare;

  /**
   * revisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   */
  private java.lang.Integer revisionCount;

  /** Constructor. */
  public Z51ParticipantContrib() {

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
   * ContributionKey is the primary key for the file. Provides each row with a
   * unique identifier over the whole file.
   *
   * @return  java.lang.Integer
   */
  public java.lang.Integer getContributionKey() {
    return contributionKey;
  }

  /**
   * ContributionKey is the primary key for the file. Provides each row with a
   * unique identifier over the whole file.
   *
   * @param  newVal  The new value for this property
   */
  public void setContributionKey(final java.lang.Integer newVal) {
    contributionKey = newVal;
  }

  /**
   * ProvincialContributions is the amount of provincialContributions this
   * participant has received.
   *
   * @return  java.lang.Double
   */
  public java.lang.Double getProvincialContributions() {
    return provincialContributions;
  }

  /**
   * ProvincialContributions is the amount of provincialContributions this
   * participant has received.
   *
   * @param  newVal  The new value for this property
   */
  public void setProvincialContributions(final java.lang.Double newVal) {
    provincialContributions = newVal;
  }

  /**
   * FederalContributions is the amount of federalContributions this participant
   * has received.
   *
   * @return  java.lang.Double
   */
  public java.lang.Double getFederalContributions() {
    return federalContributions;
  }

  /**
   * FederalContributions is the amount of federalContributions this participant
   * has received.
   *
   * @param  newVal  The new value for this property
   */
  public void setFederalContributions(final java.lang.Double newVal) {
    federalContributions = newVal;
  }

  /**
   * InterimContributions is the amount of interimContributions for this
   * participant, if a final calculation is not yet been made. If a final
   * calculation has been made, this amount will be 0.
   *
   * @return  java.lang.Double
   */
  public java.lang.Double getInterimContributions() {
    return interimContributions;
  }

  /**
   * InterimContributions is the amount of interimContributions for this
   * participant, if a final calculation is not yet been made. If a final
   * calculation has been made, this amount will be 0.
   *
   * @param  newVal  The new value for this property
   */
  public void setInterimContributions(final java.lang.Double newVal) {
    interimContributions = newVal;
  }

  /**
   * ProducerShare is the amount of AgriStability withdrawals that are
   * producer's share.
   *
   * @return  java.lang.Double
   */
  public java.lang.Double getProducerShare() {
    return producerShare;
  }

  /**
   * ProducerShare is the amount of AgriStability withdrawals that are
   * producer's share.
   *
   * @param  newVal  The new value for this property
   */
  public void setProducerShare(final java.lang.Double newVal) {
    producerShare = newVal;
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
