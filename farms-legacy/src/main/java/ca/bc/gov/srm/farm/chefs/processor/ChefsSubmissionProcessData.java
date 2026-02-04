package ca.bc.gov.srm.farm.chefs.processor;

import ca.bc.gov.srm.farm.crm.resource.CrmTaskResource;
import ca.bc.gov.srm.farm.domain.chefs.ChefsSubmission;

public class ChefsSubmissionProcessData {
  
  private ChefsSubmission chefsSubmission;
  
  private Boolean process;
  
  private CrmTaskResource validationErrorTask;

  public ChefsSubmission getChefsSubmission() {
    return chefsSubmission;
  }

  public void setChefsSubmission(ChefsSubmission chefsSubmission) {
    this.chefsSubmission = chefsSubmission;
  }

  public Boolean getProcess() {
    return process;
  }

  public void setProcess(Boolean process) {
    this.process = process;
  }

  public CrmTaskResource getValidationErrorTask() {
    return validationErrorTask;
  }

  public void setValidationErrorTask(CrmTaskResource validationErrorTask) {
    this.validationErrorTask = validationErrorTask;
  }
}
