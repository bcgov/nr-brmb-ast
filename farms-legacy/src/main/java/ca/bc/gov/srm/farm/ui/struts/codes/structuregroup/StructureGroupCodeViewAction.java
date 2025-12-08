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
 *
 * @author hwang
 */
public class StructureGroupCodeViewAction extends StructureGroupCodeAction {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Viewing Structure Group Code...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);

    StructureGroupCodesForm form = (StructureGroupCodesForm) actionForm;

    populateForm(form, true);

    return forward;
  }

  protected void populateForm(StructureGroupCodesForm form, boolean setUserInputValues) throws Exception {

    syncFilterContextWithForm(form);

    CodesService codesService = ServiceFactory.getCodesService();
    StructureGroupCode code = codesService.getStructureGroupCode(form.getCode());

    if (code != null) {
      form.setCode(code.getCode());
      form.setRevisionCount(code.getRevisionCount());

      if (setUserInputValues) {
        form.setDescription(code.getDescription());
        form.setRollupStructureGroupCode(code.getRollupStructureGroupCode());
        form.setRollupStructureGroupCodeDescription(code.getRollupStructureGroupCodeDescription());
      }

      if (code.getRollupStructureGroupCode() != null && code.getRollupStructureGroupCodeDescription() != null) {
        form.setStructureGroupSearchInput(code.getRollupStructureGroupCode() + " - " + code.getRollupStructureGroupCodeDescription());
      }
    }
  }
}
