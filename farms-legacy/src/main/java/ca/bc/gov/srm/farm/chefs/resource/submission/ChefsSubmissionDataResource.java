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
package ca.bc.gov.srm.farm.chefs.resource.submission;

import java.util.Date;

import ca.bc.gov.srm.farm.chefs.resource.ChefsResource;

/**
 * @author awilkinson
 */
public class ChefsSubmissionDataResource extends ChefsResource {

  private String origin;
  private String externalMethod;
  private String internalMethod;
  private String environment;
  private String submissionGuid;

  private Integer version;
  
  private Boolean submit;
  private Boolean lateEntry;
  
  private String createdBy;
  private Date createdAt;
  private String updatedBy;
  private Date updatedAt;
  private String confirmationId;
  private String formVersionId;
  
  private Integer parsedProgramYear;
  private Integer parsedParticipantPin;
  
  
  public String getEnvironment() {
    return environment;
  }

  public void setEnvironment(String environment) {
    this.environment = environment;
  }

  public String getExternalMethod() {
    return externalMethod;
  }

  public void setExternalMethod(String externalMethod) {
    this.externalMethod = externalMethod;
  }

  public String getInternalMethod() {
    return internalMethod;
  }

  public void setInternalMethod(String internalMethod) {
    this.internalMethod = internalMethod;
  }

  public String getOrigin() {
    return origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

	public String getSubmissionGuid() {
		return submissionGuid;
	}

	public void setSubmissionGuid(String submissionGuid) {
		this.submissionGuid = submissionGuid;
	}

  public Boolean getSubmit() {
    return submit;
  }

  public void setSubmit(Boolean submit) {
    this.submit = submit;
  }

  public Boolean getLateEntry() {
    return lateEntry;
  }

  public void setLateEntry(Boolean lateEntry) {
    this.lateEntry = lateEntry;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  public String getConfirmationId() {
    return confirmationId;
  }

  public void setConfirmationId(String confirmationId) {
    this.confirmationId = confirmationId;
  }

  public String getFormVersionId() {
    return formVersionId;
  }

  public void setFormVersionId(String formVersionId) {
    this.formVersionId = formVersionId;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public Integer getParsedProgramYear() {
    return parsedProgramYear;
  }

  public void setParsedProgramYear(Integer parsedProgramYear) {
    this.parsedProgramYear = parsedProgramYear;
  }

  public Integer getParsedParticipantPin() {
    return parsedParticipantPin;
  }

  public void setParsedParticipantPin(Integer parsedParticipantPin) {
    this.parsedParticipantPin = parsedParticipantPin;
  }

}
