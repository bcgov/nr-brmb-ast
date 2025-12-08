/**
 * Copyright (c) 2011,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.chefs;

import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmission;

public class ChefsSubmissionResultForm extends ValidatorForm {

  private static final long serialVersionUID = -3000388317586141433L;

  private String submissionGuid;
  private String submissionUrl;
  private String validationTaskUrl;
  private String mainTaskUrl;
  private String formType;
  private String userFormType;
  private String resourceJson;
  private String submissionStatusCode;

  private ChefsSubmission submission = new ChefsSubmission();

  public String getSubmissionGuid() {
    return submissionGuid;
  }

  public void setSubmissionGuid(String submissionGuid) {
    this.submissionGuid = submissionGuid;
  }

  public ChefsSubmission getSubmission() {
    return submission;
  }

  public void setSubmission(ChefsSubmission submission) {
    this.submission = submission;
  }

  public String getFormType() {
    return formType;
  }

  public void setFormType(String formType) {
    this.formType = formType;
  }

  public String getUserFormType() {
    return userFormType;
  }

  public void setUserFormType(String userFormType) {
    this.userFormType = userFormType;
  }

  public String getSubmissionUrl() {
    return submissionUrl;
  }

  public void setSubmissionUrl(String submissionUrl) {
    this.submissionUrl = submissionUrl;
  }

  public String getResourceJson() {
    return resourceJson;
  }

  public void setResourceJson(String resourceJson) {
    this.resourceJson = resourceJson;
  }

  public String getSubmissionStatusCode() {
    return submissionStatusCode;
  }

  public void setSubmissionStatusCode(String submissionStatusCode) {
    this.submissionStatusCode = submissionStatusCode;
  }

  public String getValidationTaskUrl() {
    return validationTaskUrl;
  }

  public void setValidationTaskUrl(String validationTaskUrl) {
    this.validationTaskUrl = validationTaskUrl;
  }

  public String getMainTaskUrl() {
    return mainTaskUrl;
  }

  public void setMainTaskUrl(String mainTaskUrl) {
    this.mainTaskUrl = mainTaskUrl;
  }

}