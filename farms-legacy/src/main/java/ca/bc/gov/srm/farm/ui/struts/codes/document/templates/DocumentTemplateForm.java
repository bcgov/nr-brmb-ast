package ca.bc.gov.srm.farm.ui.struts.codes.document.templates;

import java.util.List;

import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.domain.codes.DocumentTemplate;

public class DocumentTemplateForm extends ValidatorForm {
  private static final long serialVersionUID = 1L;
  
  private List<DocumentTemplate> documentTemplate;
  private int numDocumentTemplates;
  
  private String templateName;
  private String templateContent;

  public List<DocumentTemplate> getDocumentTemplates() {
    return documentTemplate;
  }

  public void setDocumentTemplates(List<DocumentTemplate> documentTemplate) {
    this.documentTemplate = documentTemplate;
  }

  public int getNumDocumentTemplates() {
    return numDocumentTemplates;
  }

  public void setNumDocumentTemplates(int numDocumentTemplates) {
    this.numDocumentTemplates = numDocumentTemplates;
  }

  public String getTemplateName() {
    return templateName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  public String getTemplateContent() {
    return templateContent;
  }

  public void setTemplateContent(String templateContent) {
    this.templateContent = templateContent;
  }
  
}