package ca.bc.gov.srm.farm.cdogs.resource;

import ca.bc.gov.srm.farm.chefs.resource.submission.ChefsSubmissionDataResource;
import ca.bc.gov.srm.farm.crm.resource.CrmResource;

public class CdogsTemplateDataResource extends CrmResource {

  private ChefsSubmissionDataResource data;

  private CdogsOptionsResource options;

  private String formatters;

  public CdogsTemplateDataResource() {
  }

  public CdogsTemplateDataResource(ChefsSubmissionDataResource data, String fileName) {
    this.data = data;
    this.options = new CdogsOptionsResource(fileName);
  }

  public ChefsSubmissionDataResource getData() {
    return data;
  }

  public void setData(ChefsSubmissionDataResource data) {
    this.data = data;
  }

  public CdogsOptionsResource getOptions() {
    return options;
  }

  public void setOptions(CdogsOptionsResource options) {
    this.options = options;
  }

  public String getFormatters() {
    return formatters;
  }

  public void setFormatters(String formatters) {
    this.formatters = formatters;
  }

  @Override
  public String toString() {
    return "CdogsTemplateResource [data=" + data.toString() + ", options=" + options.toString() + "]";
  }

}
