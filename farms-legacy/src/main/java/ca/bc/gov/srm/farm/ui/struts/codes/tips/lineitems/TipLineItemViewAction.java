package ca.bc.gov.srm.farm.ui.struts.codes.tips.lineitems;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.domain.codes.TipLineItem;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;

public class TipLineItemViewAction extends TipLineItemAction {
  private Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * @param mapping    mapping
   * @param actionForm actionForm
   * @param request    request
   * @param response   response
   * @return The ActionForward
   * @throws Exception On Exception
   */
  @Override
  protected ActionForward doExecute
  (final ActionMapping mapping, 
      final ActionForm actionForm,
      final HttpServletRequest request, 
      final HttpServletResponse response) throws Exception {

    logger.debug("Viewing Tip Line Item...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);

    TipLineItemForm form = (TipLineItemForm) actionForm;

    populateForm(form);

    return forward;
  }

  /**
   * @param TipLineItemForm form
   * @throws Exception On Exception
   */
  protected void populateForm(TipLineItemForm form) throws Exception {
     CodesService service = ServiceFactory.getCodesService(); 
     TipLineItem lineItem = service.getTipLineItem(form.getId());
     populateFormFromLineItem(lineItem, form);
     
     form.setFarmSubtypeBListViewItems(getFarmSubtypeBListViews());
  }
}
