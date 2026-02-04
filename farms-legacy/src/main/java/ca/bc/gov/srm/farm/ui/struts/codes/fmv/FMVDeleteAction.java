/**
 *
 * Copyright (c) 2011,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.codes.fmv;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

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
public class FMVDeleteAction extends FMVAction {

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

    logger.debug("Deleting FMV...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    FMVsForm form = (FMVsForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);

    if (errors != null && !errors.isEmpty()) {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
    } else {

      CodesService codesService = ServiceFactory.getCodesService();
      
      try {
        boolean inUse =
          codesService.isFMVInUse(
            form.getFmvYear(),
            form.getInventoryItemCode(),
            form.getMunicipalityCode(),
            form.getCropUnitCode());
        if(inUse) {
          ActionMessages errorMessages = new ActionMessages();
          errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_FMV_IN_USE));
          saveErrors(request, errorMessages);
          forward = mapping.findForward(ActionConstants.FAILURE);
        } else {
          codesService.deleteFMV(
              form.getFmvYear(),
              form.getInventoryItemCode(),
              form.getMunicipalityCode(),
              form.getCropUnitCode());
        }

      } catch(InvalidRevisionCountException irce) {
        ActionMessages errorMessages = new ActionMessages();
        errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_INVALID_REVISION_COUNT));
        saveErrors(request, errorMessages);
        forward = mapping.findForward(ActionConstants.FAILURE);
      }
    }

    return forward;
  }

}
