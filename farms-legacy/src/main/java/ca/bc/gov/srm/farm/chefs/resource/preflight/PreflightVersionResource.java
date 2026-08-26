package ca.bc.gov.srm.farm.chefs.resource.preflight;

import ca.bc.gov.srm.farm.chefs.resource.ChefsResource;

public class PreflightVersionResource extends ChefsResource {

  private String id;
  private String formId;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFormId() {
    return formId;
  }

  public void setFormId(String formId) {
    this.formId = formId;
  }

  @Override
  public String toString() {
    return "PreflightVersionResource [id=" + id + ", formId=" + formId + "]";
  }

}
