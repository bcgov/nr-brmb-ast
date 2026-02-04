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

import java.util.Date;

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
import ca.bc.gov.srm.farm.domain.codes.StructureGroupCode;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.DataParseUtils;
import ca.bc.gov.srm.farm.util.DateUtils;

/**
 *
 * @author hwang
 *
 */
public class StructureGroupCodeSaveAction extends StructureGroupCodeViewAction {

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
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Saving Code...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);

    StructureGroupCodesForm form = (StructureGroupCodesForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);
    populateForm(form, false);

    ActionMessages errorMessages = new ActionMessages();

    if (errors != null && !errors.isEmpty()) {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
    } else {

      String user = CurrentUser.getUser().getUserId();
      CodesService codesService = ServiceFactory.getCodesService();

      try {
        StructureGroupCode code = getStructureGroupCodeFromForm(form);

        if (form.isNew()) {
          boolean exists = checkCodeExists(codesService, code.getCode());
          if (exists) {
            errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_CODE_EXISTS));
            saveErrors(request, errorMessages);
            forward = mapping.findForward(ActionConstants.FAILURE);
          } else if (errorMessages.isEmpty()) {
            codesService.createStructureGroupCode(code, user);
            ServiceFactory.getListService().refreshCodeTableList(CodeTables.STRUCTURE_GROUP);
          }
        } else if (errorMessages.isEmpty()) {
          codesService.updateStructureGroupCode(code, user);
          ServiceFactory.getListService().refreshCodeTableList(CodeTables.STRUCTURE_GROUP);
        }
      } catch (InvalidRevisionCountException irce) {
        errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_INVALID_REVISION_COUNT));
        saveErrors(request, errorMessages);
        forward = mapping.findForward(ActionConstants.FAILURE);
      }
    }

    return forward;
  }

  /**
   * @param codesService codesService
   * @param code         code
   * @return true if the code already exists in the database
   * @throws Exception On Exception
   */
  private boolean checkCodeExists(CodesService codesService, String code) throws Exception {
    StructureGroupCode codeObj = codesService.getStructureGroupCode(code);
    boolean exists = codeObj != null;
    return exists;
  }

  /**
   * @param form form
   * @return StructureGroupCode
   * @throws Exception On Exception
   */
  private StructureGroupCode getStructureGroupCodeFromForm(StructureGroupCodesForm form) throws Exception {

    StructureGroupCode code = new StructureGroupCode();
    code.setCode(form.getCode());
    code.setDescription(form.getDescription());
    code.setRollupStructureGroupCode(form.getRollupStructureGroupCode());
    code.setRollupStructureGroupCodeDescription(form.getRollupStructureGroupCodeDescription());
    code.setEstablishedDate(new Date());
    code.setExpiryDate(DataParseUtils.parseDate(DateUtils.NEVER_EXPIRES_DATE_STRING));
    code.setRevisionCount(form.getRevisionCount());

    return code;
  }
}
