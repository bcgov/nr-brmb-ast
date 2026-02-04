/**
 * Copyright (c) 2023,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.crm.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import ca.bc.gov.srm.farm.crm.CrmConstants;
import ca.bc.gov.srm.farm.crm.CrmTransferFormatUtil;

/**
 * @author awilkinson
 */
public class CrmQueueItemResource extends CrmResource {

  @JsonProperty("queueitemid")
  private String queueItemId;
  
  @JsonIgnore
  protected String activityIdParameter;
  
  @JsonIgnore
  private String queueIdParameter;

  private Integer objecttypecode;

  @JsonProperty("objectid_task@odata.bind")
  public String getObjectIdTaskDataBind() {
    return CrmTransferFormatUtil.formatNavigationPropertyValue(CrmConstants.TASK_ENDPOINT, activityIdParameter);
  }

  @JsonInclude(Include.NON_NULL)
  @JsonProperty("queueid@odata.bind")
  public String getQueueIdDataBind() {
    return CrmTransferFormatUtil.formatNavigationPropertyValue(CrmConstants.QUEUE_ENDPOINT, queueIdParameter);
  }

  public String getQueueItemId() {
    return queueItemId;
  }

  public void setQueueItemId(String queueItemId) {
    this.queueItemId = queueItemId;
  }

  public Integer getObjecttypecode() {
    return objecttypecode;
  }

  public void setObjecttypecode(Integer objecttypecode) {
    this.objecttypecode = objecttypecode;
  }

  public void setActivityIdParameter(String activityIdParameter) {
    this.activityIdParameter = activityIdParameter;
  }

  public void setQueueIdParameter(String queueId) {
    this.queueIdParameter = queueId;
  }

}
