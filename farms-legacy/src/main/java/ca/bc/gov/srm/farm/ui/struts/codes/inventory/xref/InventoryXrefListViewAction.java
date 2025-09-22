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
package ca.bc.gov.srm.farm.ui.struts.codes.inventory.xref;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.domain.codes.InventoryXref;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;

/**
 * @author awilkinson
 */
public abstract class InventoryXrefListViewAction extends InventoryXrefAction {

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    InventoryXrefsForm form = (InventoryXrefsForm) actionForm;
    
    populateForm(form);
    
    return forward;
  }


  protected void populateForm(InventoryXrefsForm form) throws Exception {

    syncFilterContextWithForm(form);
    
    CodesService service = ServiceFactory.getCodesService();
    List<InventoryXref> codes = service.getInventoryXrefs(form.getInventoryClassCode());
    form.setCodes(codes);
    form.setNumCodes(codes.size());
  }

}
