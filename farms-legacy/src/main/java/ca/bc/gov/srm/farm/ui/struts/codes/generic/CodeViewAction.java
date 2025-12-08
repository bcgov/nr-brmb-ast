/**
 * Copyright (c) 2006, 
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

import ca.bc.gov.srm.farm.domain.codes.Code;
import ca.bc.gov.srm.farm.domain.codes.CodeTables;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;

/**
 *
 * @author awilkinson
 */
public class CodeViewAction extends SecureAction {
  
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

    logger.debug("Viewing Code...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    CodesForm form = (CodesForm) actionForm;
    
    populateForm(form);
    
    return forward;
  }

  /**
   * @param form form
   * @throws Exception On Exception
   */
  protected void populateForm(CodesForm form) throws Exception {
    String codeTable = form.getCodeTable();
    
    CodesService service = ServiceFactory.getCodesService();
    Code code = service.getCode(codeTable, form.getCode());
    if(code != null) {
      form.setCode(code.getCode());
      form.setDescription(code.getDescription());
      form.setRevisionCount(code.getRevisionCount());
      
      form.setCanCreate(CodeTables.canCreate(codeTable));
      form.setCanDelete(CodeTables.canDelete(codeTable));
    }
  }

}
