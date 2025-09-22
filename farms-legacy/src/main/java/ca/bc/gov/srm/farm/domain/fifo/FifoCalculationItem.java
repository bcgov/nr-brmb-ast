/**
 * Copyright (c) 2024,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain.fifo;

public class FifoCalculationItem {

  private Integer participantPin;
  private Integer programYear;
  private Integer craProgramYearVersionId;
  private Integer craScenarioId;
  private Integer craScenarioNumber;
  private Integer fifoScenarioId;
  private Integer fifoScenarioNumber;
  private Integer fifoProgramYearVersionId;
  private String fifoScenarioStateCode;

  public Integer getParticipantPin() {
    return participantPin;
  }

  public void setParticipantPin(Integer participantPin) {
    this.participantPin = participantPin;
  }

  public Integer getProgramYear() {
    return programYear;
  }

  public void setProgramYear(Integer programYear) {
    this.programYear = programYear;
  }

  public Integer getCraProgramYearVersionId() {
    return craProgramYearVersionId;
  }

  public void setCraProgramYearVersionId(Integer craProgramYearVersionId) {
    this.craProgramYearVersionId = craProgramYearVersionId;
  }

  public Integer getCraScenarioId() {
    return craScenarioId;
  }

  public void setCraScenarioId(Integer craScenarioId) {
    this.craScenarioId = craScenarioId;
  }

  public Integer getCraScenarioNumber() {
    return craScenarioNumber;
  }

  public void setCraScenarioNumber(Integer craScenarioNumber) {
    this.craScenarioNumber = craScenarioNumber;
  }

  public Integer getFifoScenarioId() {
    return fifoScenarioId;
  }

  public void setFifoScenarioId(Integer fifoScenarioId) {
    this.fifoScenarioId = fifoScenarioId;
  }

  public Integer getFifoScenarioNumber() {
    return fifoScenarioNumber;
  }

  public void setFifoScenarioNumber(Integer fifoScenarioNumber) {
    this.fifoScenarioNumber = fifoScenarioNumber;
  }

  public Integer getFifoProgramYearVersionId() {
    return fifoProgramYearVersionId;
  }

  public void setFifoProgramYearVersionId(Integer fifoProgramYearVersionId) {
    this.fifoProgramYearVersionId = fifoProgramYearVersionId;
  }

  public String getFifoScenarioStateCode() {
    return fifoScenarioStateCode;
  }

  public void setFifoScenarioStateCode(String fifoScenarioStateCode) {
    this.fifoScenarioStateCode = fifoScenarioStateCode;
  }

  @Override
  public String toString() {
    return "FifoCalculationItem [participantPin=" + participantPin + ", programYear=" + programYear + ", craProgramYearVersionId="
        + craProgramYearVersionId + ", craScenarioId=" + craScenarioId + ", craScenarioNumber=" + craScenarioNumber + ", fifoScenarioId="
        + fifoScenarioId + ", fifoScenarioNumber=" + fifoScenarioNumber + ", fifoProgramYearVersionId=" + fifoProgramYearVersionId
        + ", fifoScenarioStateCode=" + fifoScenarioStateCode + "]";
  }

}