/**
 * Copyright (c) 2026,
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

/** A status-only PATCH, without resending enrolment fees or participant details. */
public class CrmEnrolmentStatusUpdateResource extends CrmResource {
  @JsonProperty("vsi_enrolmentstatus")
  private Integer enrolmentStatusCode;

  public Integer getEnrolmentStatusCode() {
    return enrolmentStatusCode;
  }

  public void setEnrolmentStatusCode(Integer enrolmentStatusCode) {
    this.enrolmentStatusCode = enrolmentStatusCode;
  }
}
