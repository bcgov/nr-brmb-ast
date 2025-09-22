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

import com.fasterxml.jackson.annotation.JsonProperty;

import ca.bc.gov.srm.farm.chefs.resource.ChefsResource;

/**
 * @author awilkinson
 */
public class SubmissionParentResource<T> extends ChefsResource {

  @JsonProperty("id")
  private String submissionGuid;
  
  private Boolean deleted;
  private Boolean draft;
  private String createdBy;
  private Date createdAt;

  private String updatedBy;
  private Date updatedAt;
  private String formVersionId;
  private String confirmationId;
  
  private SubmissionResource<T> submission;

  public String getSubmissionGuid() {
    return submissionGuid;
  }

  public void setSubmissionGuid(String submissionGuid) {
    this.submissionGuid = submissionGuid;
  }

  public Boolean getDeleted() {
    return deleted;
  }

  public void setDeleted(Boolean deleted) {
    this.deleted = deleted;
  }

  public Boolean getDraft() {
    return draft;
  }

  public void setDraft(Boolean draft) {
    this.draft = draft;
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

  public String getFormVersionId() {
    return formVersionId;
  }

  public void setFormVersionId(String formVersionId) {
    this.formVersionId = formVersionId;
  }

  public String getConfirmationId() {
    return confirmationId;
  }

  public void setConfirmationId(String confirmationId) {
    this.confirmationId = confirmationId;
  }

  public SubmissionResource<T> getSubmission() {
    return submission;
  }

  public void setSubmission(SubmissionResource<T> submission) {
    this.submission = submission;
  }

}
