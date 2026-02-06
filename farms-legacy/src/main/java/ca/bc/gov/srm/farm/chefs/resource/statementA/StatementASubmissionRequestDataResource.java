/**
 * Copyright (c) 2026,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.chefs.resource.statementA;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ca.bc.gov.srm.farm.chefs.resource.ChefsResource;
import ca.bc.gov.srm.farm.chefs.resource.submission.SubmissionResource;

public class StatementASubmissionRequestDataResource<T> extends ChefsResource {

    @JsonIgnore
    private String submissionGuid;
    private Boolean draft;
    private String createdBy;
    private String createdAt;
    private String updatedBy;
    private String updatedAt;

    private SubmissionResource<T> submission;

    public String getSubmissionGuid() {
        return submissionGuid;
    }

    public void setSubmissionGuid(String submissionGuid) {
        this.submissionGuid = submissionGuid;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public SubmissionResource<T> getSubmission() {
        return submission;
    }

    public void setSubmission(SubmissionResource<T> submission) {
        this.submission = submission;
    }

}
