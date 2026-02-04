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

import com.fasterxml.jackson.annotation.JsonProperty;

import ca.bc.gov.srm.farm.chefs.resource.ChefsResource;

/**
 * @author awilkinson
 */
public class SubmissionWrapperResource<T extends ChefsResource> extends ChefsResource {

  @JsonProperty("submission")
  private SubmissionParentResource<T> submissionMetaData;

  public SubmissionParentResource<T> getSubmissionMetaData() {
    return submissionMetaData;
  }

  public void setSubmissionMetaData(SubmissionParentResource<T> submissionMetaData) {
    this.submissionMetaData = submissionMetaData;
  }
  
}
