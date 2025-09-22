/**
 *
 * Copyright (c) 2010,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.codes.lineitems;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.domain.codes.LineItemCode;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;

/**
 * 
 * @author awilkinson
 *
 */
public class LineItemDeleteAction extends LineItemAction {

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

    logger.debug("Deleting Line Item...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    LineItemsForm form = (LineItemsForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);
    CodesService codesService = ServiceFactory.getCodesService();
    
    if (errors != null && !errors.isEmpty()) {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
      form.setFruitVegListViewItems(codesService.getFruitVegListItems());
    } else {      
      try {
        LineItemCode lineItem = getLineItemFromForm(form);
        boolean inUse = checkLineItemInUse(
            codesService, form.getLineItemYear(), lineItem.getLineItem());
        if(inUse) {
          ActionMessages errorMessages = new ActionMessages();
          errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_LINE_ITEM_IN_USE));
          saveErrors(request, errorMessages);
          forward = mapping.findForward(ActionConstants.FAILURE);
          form.setFruitVegListViewItems(codesService.getFruitVegListItems());
        } else {
          codesService.deleteLineItem(
              lineItem.getLineItemId(),
              form.getLineItemYear(),
              lineItem.getLineItem(),
              lineItem.getRevisionCount());
        }

      } catch(InvalidRevisionCountException irce) {
        ActionMessages errorMessages = new ActionMessages();
        errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_INVALID_REVISION_COUNT));
        saveErrors(request, errorMessages);
        forward = mapping.findForward(ActionConstants.FAILURE);
        form.setFruitVegListViewItems(codesService.getFruitVegListItems());
      }
    }
    
    return forward;
  }

  /**
   * @param codesService codesService
   * @param year year
   * @param lineItem lineItem
   * @return true if this line item is in use in a scenario
   * @throws Exception On Exception
   */
  private boolean checkLineItemInUse(CodesService codesService, Integer year, Integer lineItem)
      throws Exception {
    boolean inUse = codesService.isLineItemInUse(year, lineItem);
    return inUse;
  }

}
