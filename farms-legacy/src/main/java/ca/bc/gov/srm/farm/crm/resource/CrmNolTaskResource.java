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
public class CrmNolTaskResource extends CrmTaskResource {

	private String cr4dd_incomedecreasedescription;
	private String cr4dd_expenseincreasedescription;
	private String cr4dd_origin;
	private String cr4dd_method;
	private String cr4dd_primaryfarmingactivity;
	private String cr4dd_participanttype;
	private String cr4dd_sin;
	private String cr4dd_businessnumber;
	private Boolean cr4dd_IncomeDecrease;

	// relationship name: interactionforemail_vsi_ValidationErrorTasks
	private String regardingobjectid;
	
	@JsonIgnore
	private String vsi_benefit;

	@JsonInclude(Include.NON_NULL)
	@JsonProperty("vsi_benefit_vsi_NOLTask@odata.bind")
	public String getVsiBenefitTaskDataBind() {
	  if (vsi_benefit == null) {
	    return null;
	  }
	  return CrmTransferFormatUtil.formatNavigationPropertyValue(CrmConstants.BENEFIT_ENDPOINT, vsi_benefit);
	}

	public String getCr4dd_origin() {
		return cr4dd_origin;
	}

	public void setCr4dd_origin(String cr4dd_origin) {
		this.cr4dd_origin = cr4dd_origin;
	}

	public String getCr4dd_method() {
		return cr4dd_method;
	}

	public void setCr4dd_method(String cr4dd_method) {
		this.cr4dd_method = cr4dd_method;
	}

	public String getCr4dd_primaryfarmingactivity() {
		return cr4dd_primaryfarmingactivity;
	}

	public void setCr4dd_primaryfarmingactivity(String cr4dd_primaryfarmingactivity) {
		this.cr4dd_primaryfarmingactivity = cr4dd_primaryfarmingactivity;
	}

	public String getCr4dd_participanttype() {
		return cr4dd_participanttype;
	}

	public void setCr4dd_participanttype(String cr4dd_participanttype) {
		this.cr4dd_participanttype = cr4dd_participanttype;
	}

	public String getCr4dd_sin() {
		return cr4dd_sin;
	}

	public void setCr4dd_sin(String cr4dd_sin) {
		this.cr4dd_sin = cr4dd_sin;
	}

	public String getCr4dd_businessnumber() {
		return cr4dd_businessnumber;
	}

	public void setCr4dd_businessnumber(String cr4dd_businessnumber) {
		this.cr4dd_businessnumber = cr4dd_businessnumber;
	}

	public String getRegardingobjectid() {
		return regardingobjectid;
	}

	public void setRegardingobjectid(String regardingobjectid) {
		this.regardingobjectid = regardingobjectid;
	}

	public String getCr4dd_incomedecreasedescription() {
		return cr4dd_incomedecreasedescription;
	}

	public void setCr4dd_incomedecreasedescription(String cr4dd_incomedecreasedescription) {
		this.cr4dd_incomedecreasedescription = cr4dd_incomedecreasedescription;
	}

	public String getCr4dd_expenseincreasedescription() {
		return cr4dd_expenseincreasedescription;
	}

	public void setCr4dd_expenseincreasedescription(String cr4dd_expenseincreasedescription) {
		this.cr4dd_expenseincreasedescription = cr4dd_expenseincreasedescription;
	}

	public Boolean getCr4dd_IncomeDecrease() {
		return cr4dd_IncomeDecrease;
	}

	public void setCr4dd_IncomeDecrease(Boolean cr4dd_IncomeDecrease) {
		this.cr4dd_IncomeDecrease = cr4dd_IncomeDecrease;
	}
	
	public String getVsi_benefit() {
	  return vsi_benefit;
	}

	public void setVsi_benefit(String vsi_benefit) {
	  this.vsi_benefit = vsi_benefit;
	}

  @Override
  public String toString() {
    return "CrmNolTaskResource [cr4dd_incomedecreasedescription=" + cr4dd_incomedecreasedescription + ", cr4dd_expenseincreasedescription="
        + cr4dd_expenseincreasedescription + ", cr4dd_origin=" + cr4dd_origin + ", cr4dd_method=" + cr4dd_method + ", cr4dd_primaryfarmingactivity="
        + cr4dd_primaryfarmingactivity + ", cr4dd_participanttype=" + cr4dd_participanttype + ", cr4dd_sin=" + cr4dd_sin + ", cr4dd_businessnumber="
        + cr4dd_businessnumber + ", vsi_benefit=" + vsi_benefit + ", cr4dd_IncomeDecrease=" + cr4dd_IncomeDecrease + ", regardingobjectid="
        + regardingobjectid + "]";
  }

}
