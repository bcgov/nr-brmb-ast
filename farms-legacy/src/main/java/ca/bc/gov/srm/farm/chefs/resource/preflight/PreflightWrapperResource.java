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
package ca.bc.gov.srm.farm.chefs.resource.preflight;

import com.fasterxml.jackson.annotation.JsonProperty;

import ca.bc.gov.srm.farm.chefs.resource.ChefsResource;

/**
 * @author awilkinson
 */
public class PreflightWrapperResource<T extends ChefsResource> extends ChefsResource {

  @JsonProperty("submission")
  private PreflightSubmissionResource submission;
  
  @JsonProperty("version")
  private PreflightVersionResource version;
  
  @JsonProperty("form")
  private PreflightFormResource form;

  public PreflightSubmissionResource getSubmission() {
    return submission;
  }

  public void setSubmission(PreflightSubmissionResource submission) {
    this.submission = submission;
  }

  public PreflightVersionResource getVersion() {
    return version;
  }

  public void setVersion(PreflightVersionResource version) {
    this.version = version;
  }

  public PreflightFormResource getForm() {
    return form;
  }

  public void setForm(PreflightFormResource form) {
    this.form = form;
  }

}
