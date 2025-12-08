package ca.bc.gov.srm.farm.crm.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class CrmAnnotationResource extends CrmResource {

	private String subject;
	private String notetext;
	private String filename;
	private Boolean isdocument;
	private String documentbody;

	@JsonIgnore
	protected String entityId;

	@JsonProperty("@odata.context")
	private String odataContext;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getNotetext() {
		return notetext;
	}

	public void setNotetext(String notetext) {
		this.notetext = notetext;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Boolean getIsdocument() {
		return isdocument;
	}

	public void setIsdocument(Boolean isdocument) {
		this.isdocument = isdocument;
	}

	public String getDocumentbody() {
		return documentbody;
	}

	public void setDocumentbody(String documentbody) {
		this.documentbody = documentbody;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

}
