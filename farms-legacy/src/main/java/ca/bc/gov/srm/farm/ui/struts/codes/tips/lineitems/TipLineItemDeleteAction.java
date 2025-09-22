package ca.bc.gov.srm.farm.ui.struts.codes.tips.lineitems;

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

public class TipLineItemDeleteAction extends TipLineItemAction {
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

    logger.debug("Deleting Tip Line Item...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);

    TipLineItemForm form = (TipLineItemForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);     
    
    if (errors != null && !errors.isEmpty()) {
        saveErrors(request, errors);
        forward = mapping.findForward(ActionConstants.FAILURE);
      } else {

        CodesService codesService = ServiceFactory.getCodesService();
        ActionMessages errorMessages = new ActionMessages();       
        
        try {
          codesService.deleteTipLineItem(form.getId());
        } catch(Exception e) {
          saveErrors(request, errorMessages);
          forward = mapping.findForward(ActionConstants.FAILURE);
          form.setFarmSubtypeBListViewItems(getFarmSubtypeBListViews());
        }
      }
    
    return forward;
  }
}
