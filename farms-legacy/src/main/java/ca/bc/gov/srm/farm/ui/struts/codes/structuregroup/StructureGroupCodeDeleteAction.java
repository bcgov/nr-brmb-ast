/**
 *
 * Copyright (c) 2012,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.codes.structuregroup;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.codes.CodeTables;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;

/**
 *
 * @author hwang
 *
 */
public class StructureGroupCodeDeleteAction extends StructureGroupCodeViewAction {

  private Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * @param mapping mapping
   * @param actionForm actionForm
   * @param request request
   * @param response response
   * @return The ActionForward
   * @throws Exception On Exception
   */
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Deleting Code...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);

    StructureGroupCodesForm form = (StructureGroupCodesForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);
    populateForm(form, false);

    if (errors != null && !errors.isEmpty()) {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
    } else {

      CodesService codesService = ServiceFactory.getCodesService();

      try {
        boolean inUse = checkCodeInUse(codesService, form.getCode());
        if (inUse) {
          ActionMessages errorMessages = new ActionMessages();
          errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_CODE_IN_USE));
          saveErrors(request, errorMessages);
          forward = mapping.findForward(ActionConstants.FAILURE);
        } else {
          codesService.deleteStructureGroupCode(form.getCode(), form.getRevisionCount());
          ServiceFactory.getListService().refreshCodeTableList(CodeTables.STRUCTURE_GROUP);
        }
      } catch (InvalidRevisionCountException irce) {
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
   * @param code code
   * @return true if the code is in use
   * @throws Exception on Exception
   */
  private boolean checkCodeInUse(CodesService codesService, String code)
      throws Exception {
    boolean inUse = codesService.isStructureGroupCodeInUse(code);
    return inUse;
  }
}
