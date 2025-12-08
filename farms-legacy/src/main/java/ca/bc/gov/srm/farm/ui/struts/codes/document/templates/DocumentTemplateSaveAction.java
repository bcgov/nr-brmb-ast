package ca.bc.gov.srm.farm.ui.struts.codes.document.templates;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;

public class DocumentTemplateSaveAction extends DocumentTemplateAction {
  private Logger logger = LoggerFactory.getLogger(getClass());


  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Saving Document Template...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    DocumentTemplateForm form = (DocumentTemplateForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);
    
    if (errors != null && !errors.isEmpty()) {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
    } else {

      String user = CurrentUser.getUser().getUserId();
      CodesService codesService = ServiceFactory.getCodesService();
                  
      codesService.updateDocumentTemplate(getDocumentTemplateFromForm(form), user);
    }    
        
    return forward;
  }
}
