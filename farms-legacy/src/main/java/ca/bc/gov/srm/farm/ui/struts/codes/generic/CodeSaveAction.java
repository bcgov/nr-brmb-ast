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

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.domain.codes.Code;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.DataParseUtils;
import ca.bc.gov.srm.farm.util.DateUtils;

/**
 * 
 * @author awilkinson
 *
 */
public class CodeSaveAction extends SecureAction {

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

    CodesForm form = (CodesForm) actionForm;
    ActionForward forward = CodeActionUtils.getSuccessRedirect(mapping, form);
    
    ActionMessages errors = form.validate(mapping, request);

    if (errors != null && !errors.isEmpty()) {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
    } else {

      String user = CurrentUser.getUser().getUserId();
      CodesService codesService = ServiceFactory.getCodesService();
      
      try {
        String codeTable = form.getCodeTable();
        Code code = getCodeFromForm(form);
        if(form.isNew()) {
          boolean exists = checkCodeExists(codesService, codeTable, code.getCode());
          if(exists) {
            ActionMessages errorMessages = new ActionMessages();
            errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_CODE_EXISTS));
            saveErrors(request, errorMessages);
            forward = mapping.findForward(ActionConstants.FAILURE);
          } else {
            codesService.createCode(codeTable, code, user);
            ServiceFactory.getListService().refreshCodeTableList(codeTable);
          }
        } else {
          codesService.updateCode(codeTable, code, user);
          ServiceFactory.getListService().refreshCodeTableList(codeTable);
        }
  
      } catch(InvalidRevisionCountException irce) {
        ActionMessages errorMessages = new ActionMessages();
        errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_INVALID_REVISION_COUNT));
        saveErrors(request, errorMessages);
        forward = mapping.findForward(ActionConstants.FAILURE);
      }
    }

    logger.debug(">doExecute");
    return forward;
  }

  /**
   * @param codesService codesService
   * @param codeTable codeTable
   * @param code code
   * @return true if the code already exists in the database
   * @throws Exception On Exception
   */
  private boolean checkCodeExists(CodesService codesService, String codeTable, String code)
      throws Exception {
    logger.debug("<checkCodeExists");

    Code codeObj = codesService.getCode(codeTable, code);
    boolean exists = codeObj != null;

    logger.debug(">checkCodeExists");
    return exists;
  }

  /**
   * @param form form
   * @return Code
   * @throws Exception On Exception
   */
  private Code getCodeFromForm(CodesForm form)
  throws Exception {
    logger.debug("<getCodeFromForm");

    Code code = new Code();
    code.setCode(form.getCode());
    code.setDescription(form.getDescription());
    code.setEstablishedDate(new Date());
    code.setExpiryDate(DataParseUtils.parseDate(DateUtils.NEVER_EXPIRES_DATE_STRING));
    code.setRevisionCount(form.getRevisionCount());
    
    logger.debug(">getCodeFromForm");
    return code;
  }

}
