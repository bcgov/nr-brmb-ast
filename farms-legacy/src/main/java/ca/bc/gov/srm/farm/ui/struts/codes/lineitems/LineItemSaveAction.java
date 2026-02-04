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

import java.util.concurrent.TimeUnit;

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
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;

/**
 * 
 * @author awilkinson
 *
 */
public class LineItemSaveAction extends LineItemAction {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {
    
    logger.debug("Saving Line Item...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    LineItemsForm form = (LineItemsForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);
    String user = CurrentUser.getUser().getUserId();
    CodesService codesService = ServiceFactory.getCodesService();
    
    if (errors != null && !errors.isEmpty()) {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
      form.setFruitVegListViewItems(codesService.getFruitVegListItems());
      form.setCommodityTypesListViewItems(codesService.getCommodityTypeListItems());
    } else {      
      try {
        LineItemCode lineItem = getLineItemFromForm(form);
        if(form.isNew()) {
          boolean exists = checkLineItemExistsNotExpired(
              codesService, form.getLineItemYear(), lineItem.getLineItem());
          if(exists) {
            ActionMessages errorMessages = new ActionMessages();
            errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_LINE_ITEM_EXISTS));
            saveErrors(request, errorMessages);
            forward = mapping.findForward(ActionConstants.FAILURE);
            form.setFruitVegListViewItems(codesService.getFruitVegListItems());
            form.setCommodityTypesListViewItems(codesService.getCommodityTypeListItems());
          } else {
            codesService.createLineItem(lineItem, user);
          }
        } else {
          codesService.updateLineItem(lineItem, user);
        }
  
      } catch(InvalidRevisionCountException irce) {
        ActionMessages errorMessages = new ActionMessages();
        errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_INVALID_REVISION_COUNT));
        saveErrors(request, errorMessages);
        forward = mapping.findForward(ActionConstants.FAILURE);
        form.setFruitVegListViewItems(codesService.getFruitVegListItems());
        form.setCommodityTypesListViewItems(codesService.getCommodityTypeListItems());
      }
    }
    
    // For some unknown reason, when a line item is created the list of line items
    // displayed does not get updated unless we do this this 1 second sleep.
    TimeUnit.SECONDS.sleep(1);

    return forward;
  }

  /**
   * @return true if the line item already exists in the database and is not expired
   */
  private boolean checkLineItemExistsNotExpired(CodesService codesService, Integer year, Integer lineItem)
      throws Exception {
    LineItemCode lineItemCode = codesService.getLineItem(year, lineItem);
    boolean existsNotExpired = lineItemCode != null;
    return existsNotExpired;
  }

}
