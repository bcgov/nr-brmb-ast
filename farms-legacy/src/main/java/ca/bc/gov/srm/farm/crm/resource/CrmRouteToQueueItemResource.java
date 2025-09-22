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

public class CrmRouteToQueueItemResource extends CrmResource {

  @JsonProperty("@odata.type")
  private final String odataType = "Microsoft.Dynamics.CRM.queueitem";

  @JsonProperty("queueitemid")
  private String queueitemid;
  
  public CrmRouteToQueueItemResource(String queueitemid) {
    this.queueitemid = queueitemid;
  }

  public String getQueueitemid() {
    return queueitemid;
  }

  public void setQueueitemid(String queueitemid) {
    this.queueitemid = queueitemid;
  }

  public String getOdataType() {
    return odataType;
  }
}
