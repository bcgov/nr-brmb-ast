package ca.bc.gov.srm.farm.chefs.resource.adjustment;

import com.fasterxml.jackson.annotation.JsonProperty;

import ca.bc.gov.srm.farm.chefs.resource.ChefsResource;


public class IdentifyInformation extends ChefsResource {

	@JsonProperty("aResponseToARequestFromTheAdministration")
	private Boolean requestFromAdmin;

	@JsonProperty("anAdjustmentToInformationPreviouslySubmittedOnProgramForms")
	private Boolean previousSubmission;

	@JsonProperty("additionalInformationSubmittedAtTheSameTimeAsYourProgramForms")
	private Boolean additionalInformation;

	public Boolean getRequestFromAdmin() {
		return requestFromAdmin;
	}

	public void setRequestFromAdmin(Boolean requestFromAdmin) {
		this.requestFromAdmin = requestFromAdmin;
	}

	public Boolean getPreviousSubmission() {
		return previousSubmission;
	}

	public void setPreviousSubmission(Boolean previousSubmission) {
		this.previousSubmission = previousSubmission;
	}

	public Boolean getAdditionalInformation() {
		return additionalInformation;
	}

	public void setAdditionalInformation(Boolean additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

}
