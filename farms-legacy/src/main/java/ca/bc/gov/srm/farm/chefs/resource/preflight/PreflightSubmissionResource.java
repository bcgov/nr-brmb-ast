package ca.bc.gov.srm.farm.chefs.resource.preflight;

import ca.bc.gov.srm.farm.chefs.resource.ChefsResource;

public class PreflightSubmissionResource extends ChefsResource {
  private String id;
  private String formVersionId;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFormVersionId() {
    return formVersionId;
  }

  public void setFormVersionId(String formVersionId) {
    this.formVersionId = formVersionId;
  }

  @Override
  public String toString() {
    return "PreflightSubmissionResource [id=" + id + ", formVersionId=" + formVersionId + "]";
  }

}
