package ca.bc.gov.srm.farm.ui.struts.codes.tips.lineitems;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.domain.codes.FarmSubtype;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;

public class TipLineItemSaveAction extends TipLineItemAction {
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
  protected ActionForward doExecute(final ActionMapping mapping, final ActionForm actionForm,
      final HttpServletRequest request, final HttpServletResponse response) throws Exception {

    logger.debug("Saving Tip Line Item...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);

    TipLineItemForm form = (TipLineItemForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);

    if (errors != null && !errors.isEmpty()) {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
    } else {

      String user = CurrentUser.getUser().getUserId();
      CodesService codesService = ServiceFactory.getCodesService();
      ActionMessages errorMessages = new ActionMessages();

      try {
        Boolean errorFree = true;

        if (form.isNew()) {
          if (form.getLineItem() == null || form.getLineItem().isEmpty()) {
            errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_LINE_ITEM_BLANK));
            saveErrors(request, errorMessages);
            forward = mapping.findForward(ActionConstants.FAILURE);
            errorFree = false;
          } else {
            try {
              if (codesService.checkTipLineItemExists(Integer.parseInt(form.getLineItem()))) { 
                errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_TIP_LINE_ITEM_EXISTS));
                saveErrors(request, errorMessages); forward =
                mapping.findForward(ActionConstants.FAILURE); errorFree = false; 
              }
            } catch (NumberFormatException e) {
              errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_LINE_ITEM_BLANK));
              saveErrors(request, errorMessages);
              forward = mapping.findForward(ActionConstants.FAILURE);
              errorFree = false;
            }
          }
        }

        FarmSubtype farmSubtypeB = null;
        if (form.getFarmSubtypeB() != null && !form.getFarmSubtypeB().isEmpty()) {
          farmSubtypeB = codesService.getFarmSubtypeB(form.getFarmSubtypeB());
        }
        
        if (errorFree && form.isNew()) {
          logger.debug("Creating TIP Line Item.");
          codesService.createTipLineItem(farmSubtypeB, user, getLineItemFromForm(form));
        } else if (errorFree && !form.isNew()) {
          codesService.updateTipLineItem(farmSubtypeB, user, getLineItemFromForm(form));
        } else {
          form.setFarmSubtypeBListViewItems(getFarmSubtypeBListViews());
        }
      } catch (Exception e) {
        saveErrors(request, errorMessages);
        forward = mapping.findForward(ActionConstants.FAILURE);
      }
    }

    return forward;
  }

}
