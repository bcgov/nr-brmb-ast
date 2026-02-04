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

public class CrmRouteToTargetResource extends CrmResource {

  @JsonProperty("@odata.type")
  private final String odataType = "Microsoft.Dynamics.CRM.queue";

  @JsonProperty("queueid")
  private String queueId;

  public CrmRouteToTargetResource(String queueId) {
    this.queueId = queueId;
  }

  public String getQueueId() {
    return queueId;
  }

  public void setQueueId(String queueId) {
    this.queueId = queueId;
  }

  public String getOdataType() {
    return odataType;
  }

}
