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
package ca.bc.gov.srm.farm.domain.chefs;

import java.util.Date;

/**
 * @author awilkinson
 */
public class ChefsSubmission {

  private Integer submissionId;
  private String submissionGuid;
  private String validationTaskGuid;
  private String mainTaskGuid;
  private String formTypeCode;
  private String formTypeDescription;
  private String submissionStatusCode;
  private Integer revisionCount;
  private String bceidFormInd;
  private String userFormTypeCode;
  private Date createdDate;
  private Date updatedDate;

  public Integer getSubmissionId() {
    return submissionId;
  }

  public void setSubmissionId(Integer submissionId) {
    this.submissionId = submissionId;
  }

  public String getSubmissionGuid() {
    return submissionGuid;
  }

  public void setSubmissionGuid(String submissionGuid) {
    this.submissionGuid = submissionGuid;
  }

  public String getFormTypeCode() {
    return formTypeCode;
  }

  public void setFormTypeCode(String formTypeCode) {
    this.formTypeCode = formTypeCode;
  }

  public String getFormTypeDescription() {
    return formTypeDescription;
  }

  public void setFormTypeDescription(String formTypeDescription) {
    this.formTypeDescription = formTypeDescription;
  }

  public String getSubmissionStatusCode() {
    return submissionStatusCode;
  }

  public void setSubmissionStatusCode(String submissionStatusCode) {
    this.submissionStatusCode = submissionStatusCode;
  }

  public Integer getRevisionCount() {
    return revisionCount;
  }

  public void setRevisionCount(Integer revisionCount) {
    this.revisionCount = revisionCount;
  }

  public String getValidationTaskGuid() {
    return validationTaskGuid;
  }

  public void setValidationTaskGuid(String validationTaskGuid) {
    this.validationTaskGuid = validationTaskGuid;
  }

  public String getMainTaskGuid() {
    return mainTaskGuid;
  }

  public void setMainTaskGuid(String mainTaskGuid) {
    this.mainTaskGuid = mainTaskGuid;
  }

  public String getBceidFormInd() {
    return bceidFormInd;
  }

  public void setBceidFormInd(String bceidFormInd) {
    this.bceidFormInd = bceidFormInd;
  }


  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date created) {
    this.createdDate = created;
  }

  public Date getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(Date updated) {
    this.updatedDate = updated;
  }

  public String getUserFormTypeCode() {
    return userFormTypeCode;
  }

  public void setUserFormTypeCode(String userFormTypeCode) {
    this.userFormTypeCode = userFormTypeCode;
  }

  @Override
  public String toString() {
    return "ChefsSubmission [submissionId=" + submissionId + ", submissionGuid=" + submissionGuid + ", validationTaskGuid=" + validationTaskGuid
        + ", mainTaskGuid=" + mainTaskGuid + ", formTypeCode=" + formTypeCode + ", formTypeDescription=" + formTypeDescription
        + ", submissionStatusCode=" + submissionStatusCode + ", revisionCount=" + revisionCount + ", bceidFormInd=" + bceidFormInd
        + ", userFormTypeCode=" + userFormTypeCode + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + "]";
  }

  
}