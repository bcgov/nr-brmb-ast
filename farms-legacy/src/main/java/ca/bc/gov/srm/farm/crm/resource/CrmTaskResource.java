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
import com.fasterxml.jackson.annotation.JsonProperty;

import ca.bc.gov.srm.farm.crm.CrmConstants;
import ca.bc.gov.srm.farm.crm.CrmTransferFormatUtil;

import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author awilkinson
 */
public class CrmTaskResource extends CrmResource {
  
  private String subject;
  private String description;
  
  @JsonProperty("statecode")
  private Integer stateCode;
  
  @JsonProperty("statuscode")
  private Integer statusCode;
  
  /**
   *  This is the Task's Primary Key
   */
  @JsonProperty("activityid")
  private String activityId;
  
  @JsonProperty("_regardingobjectid_value")
  private String accountId;
  
  @JsonIgnore
  private String accountIdParameter;

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setAccountIdParameter(String accountId) {
    this.accountIdParameter = accountId;
  }

  @JsonInclude(Include.NON_NULL)
  @JsonProperty("regardingobjectid_account@odata.bind")
  public String getObjectIdTaskDataBind() {
    return CrmTransferFormatUtil.formatNavigationPropertyValue(CrmConstants.ACCOUNT_ENDPOINT, accountIdParameter);
  }

  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

  public String getActivityId() {
    return activityId;
  }

  public void setActivityId(String activityId) {
    this.activityId = activityId;
  }

  public Integer getStateCode() {
    return stateCode;
  }

  public void setStateCode(Integer stateCode) {
    this.stateCode = stateCode;
  }

  public Integer getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(Integer statusCode) {
    this.statusCode = statusCode;
  }

	@Override
	public String toString() {
		return "CrmTaskResource [subject=" + subject + ", description=" + description + ", stateCode=" + stateCode
		    + ", statusCode=" + statusCode + ", activityId=" + activityId + ", accountId=" + accountId
		    + ", accountIdParameter=" + accountIdParameter + "]";
	}

  
}
