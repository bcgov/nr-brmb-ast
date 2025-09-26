/**
 *
 * Copyright (c) 2022,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.codes.sector.details;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.domain.codes.CodeTables;
import ca.bc.gov.srm.farm.domain.codes.SectorDetailCodes;
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
public class SectorDetailCodeDeleteAction extends SectorDetailCodeAction {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Deleting Sector Detail Code...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    SectorDetailCodesForm form = (SectorDetailCodesForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);
    populateForm(form, false);

    if (errors != null && !errors.isEmpty()) {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
    } else {

      CodesService codesService = ServiceFactory.getCodesService();
      
      try {
        boolean mixed = SectorDetailCodes.isMixed(form.getSectorDetailCode());
        if(mixed) {
          ActionMessages errorMessages = new ActionMessages();
          errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_SECTOR_DETAIL_DELETE_MIXED));
          saveErrors(request, errorMessages);
          forward = mapping.findForward(ActionConstants.FAILURE);
        } else if(isCodeInUse(codesService, form.getSectorDetailCode())) {
            ActionMessages errorMessages = new ActionMessages();
            errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_CODE_IN_USE));
            saveErrors(request, errorMessages);
            forward = mapping.findForward(ActionConstants.FAILURE);
        } else {
          codesService.deleteSectorDetailCode(form.getSectorDetailCode(), form.getRevisionCount());
          ServiceFactory.getListService().refreshCodeTableList(CodeTables.SECTOR_DETAIL);
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


  private boolean isCodeInUse(CodesService codesService, String code)
      throws Exception {
    boolean inUse = codesService.isSectorDetailCodeInUse(code);
    return inUse;
  }

}
