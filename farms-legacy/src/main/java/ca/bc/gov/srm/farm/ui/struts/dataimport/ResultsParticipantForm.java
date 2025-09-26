package ca.bc.gov.srm.farm.ui.struts.dataimport;

import org.apache.struts.validator.ValidatorForm;

/**
 * ResultsParticipantForm used by screen 240.
 */
public class ResultsParticipantForm extends ValidatorForm {

	private static final long serialVersionUID = -1645506621365403099L;

	private String importVersionId;
	private String pin;
	private String participantHtml;

	/**
	 * @return the importVersionId
	 */
	public String getImportVersionId() {
		return importVersionId;
	}

	/**
	 * @param importVersionId
	 *          the importVersionId to set
	 */
	public void setImportVersionId(String importVersionId) {
		this.importVersionId = importVersionId;
	}

	/**
	 * @return the participantHtml
	 */
	public String getParticipantHtml() {
		return participantHtml;
	}

	/**
	 * @param participantHtml
	 *          the participantHtml to set
	 */
	public void setParticipantHtml(String participantHtml) {
		this.participantHtml = participantHtml;
	}

	/**
	 * @return the pin
	 */
	public String getPin() {
		return pin;
	}

	/**
	 * @param pin
	 *          the pin to set
	 */
	public void setPin(String pin) {
		this.pin = pin;
	}

}
