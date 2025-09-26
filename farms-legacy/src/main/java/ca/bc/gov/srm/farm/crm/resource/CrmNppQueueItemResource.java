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

import com.fasterxml.jackson.annotation.JsonProperty;

import ca.bc.gov.srm.farm.crm.CrmConstants;
import ca.bc.gov.srm.farm.crm.CrmTransferFormatUtil;

public class CrmNppQueueItemResource extends CrmQueueItemResource {

  @Override
  @JsonProperty("objectid_vsi_newparticipanttask@odata.bind")
  public String getObjectIdTaskDataBind() {
    return CrmTransferFormatUtil.formatNavigationPropertyValue(CrmConstants.NPP_TASK_ENDPOINT, activityIdParameter);
  }

}
