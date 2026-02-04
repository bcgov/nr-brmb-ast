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
package ca.bc.gov.srm.farm.crm.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CrmEnrolmentResource extends CrmResource {

  private String vsi_participantprogramyearid;
  private String _vsi_participantid_value;

  @JsonProperty("vsi_programyearid")
  private CrmProgramYearResource vsiProgramYear;

  @JsonProperty("vsi_enrolmentstatus")
  private Integer enrolmentStatusCode;

  public String getVsi_participantprogramyearid() {
    return vsi_participantprogramyearid;
  }

  public void setVsi_participantprogramyearid(String vsi_participantprogramyearid) {
    this.vsi_participantprogramyearid = vsi_participantprogramyearid;
  }

  public String get_vsi_participantid_value() {
    return _vsi_participantid_value;
  }

  public void set_vsi_participantid_value(String _vsi_participantid_value) {
    this._vsi_participantid_value = _vsi_participantid_value;
  }

  public CrmProgramYearResource getVsiProgramYear() {
    return vsiProgramYear;
  }

  public void setVsiProgramYear(CrmProgramYearResource vsiProgramYear) {
    this.vsiProgramYear = vsiProgramYear;
  }

  public Integer getEnrolmentStatusCode() {
    return enrolmentStatusCode;
  }

  public void setEnrolmentStatusCode(Integer enrolmentStatusCode) {
    this.enrolmentStatusCode = enrolmentStatusCode;
  }

  @Override
  public String toString() {
    return "CrmParticipantProgramYearResource [vsi_participantprogramyearid=" + vsi_participantprogramyearid + ", vsiProgramYear=" + vsiProgramYear
        + "]";
  }

}
