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

import ca.bc.gov.srm.farm.ui.struts.ActionConstants;

/**
 * @author awilkinson
 */
public class SectorDetailCodeNewAction extends SectorDetailCodeAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Viewing Create Inventory Item New Code...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    SectorDetailCodesForm form = (SectorDetailCodesForm) actionForm;
    
    populateForm(form, true);
    
    return forward;
  }

  @Override
  protected void populateForm(SectorDetailCodesForm form, boolean setUserInputValues) throws Exception {

    syncFilterContextWithForm(form);

    form.setNew(true);
  }

}
