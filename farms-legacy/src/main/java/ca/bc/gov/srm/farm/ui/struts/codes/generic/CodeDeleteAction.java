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
package ca.bc.gov.srm.farm.ui.struts.codes.generic;

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
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;

/**
 * 
 * @author awilkinson
 *
 */
public class CodeDeleteAction extends SecureAction {

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

    logger.debug("Deleting Code...");

    CodesForm form = (CodesForm) actionForm;
    ActionForward forward = CodeActionUtils.getSuccessRedirect(mapping, form);
    
    ActionMessages errors = form.validate(mapping, request);

    if (errors != null && !errors.isEmpty()) {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
    } else {

      CodesService codesService = ServiceFactory.getCodesService();
      
      try {
        String codeTable = form.getCodeTable();
        boolean inUse = checkCodeInUse(codesService, codeTable, form.getCode());
        if(inUse) {
          ActionMessages errorMessages = new ActionMessages();
          errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_CODE_IN_USE));
          saveErrors(request, errorMessages);
          forward = mapping.findForward(ActionConstants.FAILURE);
        } else {
          codesService.deleteCode(codeTable, form.getCode(), form.getRevisionCount());
          ServiceFactory.getListService().refreshCodeTableList(codeTable);
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

  /**
   * @param codesService codesService
   * @param codeTable codeTable
   * @param code code
   * @return true if the code is in use
   * @throws Exception On Exception
   */
  private static boolean checkCodeInUse(CodesService codesService, String codeTable, String code)
      throws Exception {
    boolean inUse = codesService.isCodeInUse(codeTable, code);
    return inUse;
  }

}
