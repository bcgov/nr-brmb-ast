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
package ca.bc.gov.srm.farm.domain.benefit.triage;

public class BenefitTriageCalculationItem {

  private Integer participantPin;
  private Integer programYear;
  private Integer craProgramYearVersionId;
  private Integer craScenarioId;
  private Integer craScenarioNumber;

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

  @Override
  public String toString() {
    return "BenefitTriageCalculationItem [participantPin=" + participantPin + ", programYear=" + programYear + ", craProgramYearVersionId="
        + craProgramYearVersionId + ", craScenarioId=" + craScenarioId + ", craScenarioNumber=" + craScenarioNumber + "]";
  }

}