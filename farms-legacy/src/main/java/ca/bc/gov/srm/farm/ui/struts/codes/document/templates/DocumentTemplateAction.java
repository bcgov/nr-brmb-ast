package ca.bc.gov.srm.farm.ui.struts.codes.document.templates;

import ca.bc.gov.srm.farm.domain.codes.DocumentTemplate;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;

public abstract class DocumentTemplateAction extends SecureAction {
  
  protected void populateForm(DocumentTemplateForm form) throws Exception {
    CodesService service = ServiceFactory.getCodesService();
    DocumentTemplate documentTemplate = service.getDocumentTemplate(form.getTemplateName());
    
    if(documentTemplate != null) {
     form.setTemplateName(documentTemplate.getTemplateName());
     form.setTemplateContent(documentTemplate.getTemplateContent());
    }      
  }
  
  protected DocumentTemplate getDocumentTemplateFromForm(DocumentTemplateForm form) {
    DocumentTemplate documentTemplate = new DocumentTemplate();
    documentTemplate.setTemplateName(form.getTemplateName());
    documentTemplate.setTemplateContent(form.getTemplateContent());
    return documentTemplate;
  }  
}