/**
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
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.domain.codes.CodeTables;
import ca.bc.gov.srm.farm.domain.codes.SectorDetailCode;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;

public class SectorDetailCodeSaveAction extends SectorDetailCodeAction {
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Saving Sector Detail Code...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    SectorDetailCodesForm form = (SectorDetailCodesForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);
    
    if (errors != null && !errors.isEmpty()) {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
    } else {

      String user = CurrentUser.getUser().getUserId();
      CodesService codesService = ServiceFactory.getCodesService();

      SectorDetailCode sectorDetailCode = getSectorDetailCodeFromForm(form);
      if(form.isNew()) {
        codesService.createSectorDetailCode(sectorDetailCode, user);
        ServiceFactory.getListService().refreshCodeTableList(CodeTables.SECTOR_DETAIL);
      } else {
        codesService.updateSectorDetailCode(sectorDetailCode, user);
      }
    }    
    
    return forward;
  }
}
