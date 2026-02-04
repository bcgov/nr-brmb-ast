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

public class CrmProgramYearResource extends CrmResource {

  @JsonProperty("vsi_year")
  private Integer vsiYear;
  private String vsi_programyearid;

  public Integer getVsiYear() {
    return vsiYear;
  }

  public void setVsiYear(Integer vsiYear) {
    this.vsiYear = vsiYear;
  }

  public String getVsi_programyearid() {
    return vsi_programyearid;
  }

  public void setVsi_programyearid(String vsi_programyearid) {
    this.vsi_programyearid = vsi_programyearid;
  }

  @Override
  public String toString() {
    return "CrmProgramYearResource [vsiYear=" + vsiYear + ", vsi_programyearid=" + vsi_programyearid + "]";
  }

}
