/**
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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.codes.StructureGroupCode;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;

/**
 * @author hwang
 */
public class StructureGroupCodeListViewAction extends StructureGroupCodeAction {

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

    logger.debug("Viewing Structure Group Codes...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);

    StructureGroupCodesForm form = (StructureGroupCodesForm) actionForm;

    populateForm(form);

    return forward;
  }

  /**
   * @param form form
   * @throws Exception On Exception
   */
  protected void populateForm(StructureGroupCodesForm form) throws Exception {

    syncFilterContextWithForm(form);

    CodesService service = ServiceFactory.getCodesService();
    List<StructureGroupCode> codes = service.getStructureGroupCodes();
    form.setCodes(codes);
    form.setNumCodes(codes.size());
  }

}
