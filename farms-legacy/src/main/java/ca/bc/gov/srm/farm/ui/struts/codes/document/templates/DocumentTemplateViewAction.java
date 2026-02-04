package ca.bc.gov.srm.farm.ui.struts.codes.document.templates;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.ui.struts.ActionConstants;

public class DocumentTemplateViewAction extends DocumentTemplateAction {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Viewing Document Template...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    DocumentTemplateForm form = (DocumentTemplateForm) actionForm;
    
    populateForm(form);
    
    return forward;
  }
}
