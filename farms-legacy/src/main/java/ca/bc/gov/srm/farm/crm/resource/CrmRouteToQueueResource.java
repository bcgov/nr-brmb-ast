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

public class CrmRouteToQueueResource extends CrmResource {

  @JsonProperty("Target")
  private CrmRouteToTargetResource target;

  @JsonProperty("QueueItem")
  private CrmRouteToQueueItemResource queueItem;
  
  public CrmRouteToQueueResource(CrmRouteToTargetResource target, CrmRouteToQueueItemResource queueItem) {
    this.target = target;
    this.queueItem = queueItem;
  }

  public CrmRouteToTargetResource getTarget() {
    return target;
  }

  public void setTarget(CrmRouteToTargetResource target) {
    this.target = target;
  }

  public CrmRouteToQueueItemResource getQueueItem() {
    return queueItem;
  }

  public void setQueueItem(CrmRouteToQueueItemResource queueItem) {
    this.queueItem = queueItem;
  }

}
