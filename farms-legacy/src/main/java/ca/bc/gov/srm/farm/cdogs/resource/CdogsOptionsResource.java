package ca.bc.gov.srm.farm.cdogs.resource;

import ca.bc.gov.srm.farm.crm.resource.CrmResource;

public class CdogsOptionsResource extends CrmResource {

	private String convertTo = "pdf";

	private String reportName;

	private Boolean overwrite = true;

	public String getConvertTo() {
		return convertTo;
	}

	public void setConvertTo(String convertTo) {
		this.convertTo = convertTo;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public Boolean getOverwrite() {
		return overwrite;
	}

	public void setOverwrite(Boolean overwrite) {
		this.overwrite = overwrite;
	}
	
	public CdogsOptionsResource(String reportName) {
		this.reportName = reportName;
	}

}
