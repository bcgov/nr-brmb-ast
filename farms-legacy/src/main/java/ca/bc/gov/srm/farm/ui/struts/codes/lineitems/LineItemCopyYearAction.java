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

import java.util.List;

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
public class LineItemCopyYearAction extends LineItemAction {

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
    logger.debug("<doExecute");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    LineItemsForm form = (LineItemsForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);

    if (errors != null && !errors.isEmpty()) {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
    } else {

      String user = CurrentUser.getUser().getUserId();
      CodesService codesService = ServiceFactory.getCodesService();
      
      Integer toYear = form.getYearFilter();
      boolean exists = checkYearLineItemsExist(codesService, toYear);
      if(exists) {
        ActionMessages errorMessages = new ActionMessages();
        errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_YEAR_LINE_ITEM_EXISTS, toYear.toString()));
        saveErrors(request, errorMessages);
        forward = mapping.findForward(ActionConstants.FAILURE);
      } else {
        codesService.copyYearLineItems(toYear, user);
      }

    }

    logger.debug(">doExecute");
    return forward;
  }

  /**
   * @param codesService codesService
   * @param year year
   * @return true if line items already exists in the database for this year
   * @throws Exception On Exception
   */
  private boolean checkYearLineItemsExist(CodesService codesService, Integer year)
      throws Exception {
    logger.debug("<doExecute");
    
    List<LineItemCode> lineItems = codesService.getLineItems(year);
    boolean exists = ! lineItems.isEmpty();
    
    logger.debug("exists = " + exists);

    logger.debug(">doExecute");
    return exists;
  }

}
