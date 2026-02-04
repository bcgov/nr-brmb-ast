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
package ca.bc.gov.srm.farm.ui.struts.codes.inventory.itemcodes;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;

/**
 * @author awilkinson
 */
public class InventoryItemCodeNewAction extends InventoryItemCodeAction {
  
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

    logger.debug("Viewing Create Inventory Item New Code...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    InventoryItemCodesForm form = (InventoryItemCodesForm) actionForm;
    
    populateForm(form);
    
    return forward;
  }

  /**
   * @param form form
   * @throws Exception On Exception
   */
  protected void populateForm(InventoryItemCodesForm form) throws Exception {

    syncFilterContextWithForm(form);

    CodesService codesService = ServiceFactory.getCodesService();
    List<Integer> programYears = codesService.getProgramYears();
    
    form.setFruitVegListViewItems(codesService.getFruitVegListItems());
    form.setCommodityTypesListViewItems(codesService.getCommodityTypeListItems());
    
    List<String> yearKeys = new ArrayList<>();
    
    for(Integer year : programYears) {
      InventoryDetailFormData fd = form.getDetail(year.toString());
      fd.setEligible(true);
      
      yearKeys.add(year.toString());
    }
    
    form.setYears(yearKeys);

    form.setNew(true);
  }

}
