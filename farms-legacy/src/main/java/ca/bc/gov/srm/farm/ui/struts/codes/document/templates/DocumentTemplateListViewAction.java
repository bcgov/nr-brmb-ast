package ca.bc.gov.srm.farm.ui.struts.codes.document.templates;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.domain.codes.DocumentTemplate;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;

public class DocumentTemplateListViewAction extends DocumentTemplateAction {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Viewing Document Templates...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    DocumentTemplateForm form = (DocumentTemplateForm) actionForm;
    
    populateForm(form);
    
    return forward;
  }

  @Override
  protected void populateForm(DocumentTemplateForm form) throws Exception {
    CodesService service = ServiceFactory.getCodesService();
    List<DocumentTemplate> documentTemplates = service.getDocumentTemplates();
    form.setDocumentTemplates(documentTemplates);
    form.setNumDocumentTemplates(documentTemplates.size());
  }
}
