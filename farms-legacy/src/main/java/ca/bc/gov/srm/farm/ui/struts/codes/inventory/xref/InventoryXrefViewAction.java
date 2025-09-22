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
 *
 * @author awilkinson
 */
public class InventoryXrefViewAction extends InventoryXrefAction {

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

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    InventoryXrefsForm form = (InventoryXrefsForm) actionForm;
    
    populateForm(form);
    
    return forward;
  }

  /**
   * @param form form
   * @throws Exception On Exception
   */
  protected void populateForm(InventoryXrefsForm form) throws Exception {
    
    CodesService codesService = ServiceFactory.getCodesService();
    InventoryXref xref = codesService.getInventoryXref(form.getCommodityXrefId());
    if(xref != null) {
      form.setCommodityXrefId(xref.getCommodityXrefId());
      form.setInventoryClassCode(xref.getInventoryClassCode());
      form.setInventoryClassCodeDescription(xref.getInventoryClassCodeDescription());
      form.setInventoryGroupCode(xref.getInventoryGroupCode());
      form.setInventoryGroupCodeDescription(xref.getInventoryGroupCodeDescription());
      form.setInventoryItemCode(xref.getInventoryItemCode());
      form.setInventoryItemCodeDescription(xref.getInventoryItemCodeDescription());
      form.setMarketCommodity(xref.getIsMarketCommodity().booleanValue());
      form.setRevisionCount(xref.getRevisionCount());
    }

    syncFilterContextWithForm(form);
  }

}
