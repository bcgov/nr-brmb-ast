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
package ca.bc.gov.srm.farm.ui.struts.codes.bpu;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;

/**
 *
 */
public class BPUDeleteAction extends BPUAction {

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

    logger.debug("Deleting BPU...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    BPUsForm form = (BPUsForm) actionForm;
    CodesService codesService = ServiceFactory.getCodesService();
    boolean inUse = codesService.isBPUInUse(form.getBpuId());
    
    if(inUse) {
      ActionMessages errorMessages = new ActionMessages();
      errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_BPU_IN_USE));
      saveErrors(request, errorMessages);
      forward = mapping.findForward(ActionConstants.FAILURE);
      
      // repopulate form otherwise some fields are missing
      BpuUtils.populateFormForOneBPU(form, false);
    } else {
      codesService.deleteBPU(form.getBpuId());
    }

    return forward;
  }

}
