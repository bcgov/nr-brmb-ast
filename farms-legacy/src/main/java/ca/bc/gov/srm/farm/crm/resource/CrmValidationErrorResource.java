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

/**
 * @author awilkinson
 */
public class CrmValidationErrorResource extends CrmTaskResource {
  
  private String cr4dd_origin;
  private String cr4dd_method;
  private String cr4dd_primaryfarmingactivity;
  private String cr4dd_participanttype;
  private String cr4dd_sinbn;
  private String cr4dd_businessnumber;
  private String cr4dd_chefsurl;
  
  // relationship name: interactionforemail_vsi_ValidationErrorTasks 
  private String regardingobjectid;
  
  
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

	public String getCr4dd_sinbn() {
		return cr4dd_sinbn;
	}

	public void setCr4dd_sinbn(String cr4dd_sinbn) {
		this.cr4dd_sinbn = cr4dd_sinbn;
	}

	public String getCr4dd_businessnumber() {
		return cr4dd_businessnumber;
	}

	public void setCr4dd_businessnumber(String cr4dd_businessnumber) {
		this.cr4dd_businessnumber = cr4dd_businessnumber;
	}

	public String getCr4dd_chefsurl() {
		return cr4dd_chefsurl;
	}

	public void setCr4dd_chefsurl(String cr4dd_chefsurl) {
		this.cr4dd_chefsurl = cr4dd_chefsurl;
	}

	public String getRegardingobjectid() {
		return regardingobjectid;
	}

	public void setRegardingobjectid(String regardingobjectid) {
		this.regardingobjectid = regardingobjectid;
	}

}
