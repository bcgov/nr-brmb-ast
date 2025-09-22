package ca.bc.gov.srm.farm.ui.struts.codes.reasonability.expectedproductions;

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
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;

public class ExpectedProductionDeleteAction extends ExpectedProductionAction {
  private Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * @param mapping mapping
   * @param actionForm actionForm
   * @param request request
   * @param response response
   * @return The ActionForward
   * @throws Exception On Exception
   */
  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Deleting Expected Production Item...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    ExpectedProductionsForm form = (ExpectedProductionsForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);
    
    syncFilterContextWithForm(form);
    
    if (errors != null && !errors.isEmpty()) {
        saveErrors(request, errors);
        forward = mapping.findForward(ActionConstants.FAILURE);
      } else {
        CodesService codesService = ServiceFactory.getCodesService();
        ActionMessages errorMessages = new ActionMessages();
        
        try {
          codesService.deleteExpectedProductionItem(getExpectedProductionItemFromForm(form));
        } catch(Exception e) {
          saveErrors(request, errorMessages);
          forward = mapping.findForward(ActionConstants.FAILURE);
        }
      }
    
    return forward;
  }
}
