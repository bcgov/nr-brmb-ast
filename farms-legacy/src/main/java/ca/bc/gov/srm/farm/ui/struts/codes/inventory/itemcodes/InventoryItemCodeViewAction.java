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
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.domain.codes.InventoryItemCode;
import ca.bc.gov.srm.farm.domain.codes.InventoryItemDetail;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 *
 * @author awilkinson
 */
public class InventoryItemCodeViewAction extends InventoryItemCodeAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Viewing Inventory Item Code...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    InventoryItemCodesForm form = (InventoryItemCodesForm) actionForm;
    
    populateForm(form, true);
    
    return forward;
  }


  protected void populateForm(InventoryItemCodesForm form, boolean setUserInputValues) throws Exception {

    syncFilterContextWithForm(form);
    
    CodesService codesService = ServiceFactory.getCodesService();
    List<Integer> programYears = codesService.getProgramYears();
    InventoryItemCode code = codesService.getInventoryItemCode(form.getCode());
    
    form.setFruitVegListViewItems(codesService.getFruitVegListItems());
    form.setCommodityTypesListViewItems(codesService.getCommodityTypeListItems());

    if(code != null) {
      form.setCode(code.getCode());
      form.setRevisionCount(code.getRevisionCount());
      
      if(setUserInputValues) {
        form.setDescription(code.getDescription());
        form.setRollupInventoryItemCode(code.getRollupInventoryItemCode());
        form.setRollupInventoryItemCodeDescription(code.getRollupInventoryItemCodeDescription());  
      }
      
      if(code.getRollupInventoryItemCode() != null && code.getRollupInventoryItemCodeDescription() != null) {
        form.setInventorySearchInput(code.getRollupInventoryItemCode() + " - " + code.getRollupInventoryItemCodeDescription());
      }
      
      Map<Integer, InventoryItemDetail> details = code.getDetails();
      
      List<String> yearKeys = new ArrayList<>();
      
      for(Integer year : programYears) {
        InventoryDetailFormData fd = form.getDetail(year.toString());
        InventoryItemDetail inventoryItemDetail = details.get(year);
        
        if(inventoryItemDetail != null) {
          fd.setInventoryItemDetailId(StringUtils.toString(inventoryItemDetail.getInventoryItemDetailId()));
          fd.setRevisionCount(StringUtils.toString(inventoryItemDetail.getRevisionCount()));
          fd.setEligibleOriginal(inventoryItemDetail.getIsEligible().booleanValue());
          fd.setFruitVegCodeNameOriginal(inventoryItemDetail.getFruitVegCodeName());
          fd.setProgramYear(year);
          fd.setOriginalLineItem(inventoryItemDetail.getLineItem());
          fd.setInsurableValueOriginal(inventoryItemDetail.getInsurableValue());
          fd.setPremiumRateOriginal(inventoryItemDetail.getPremiumRate());
          fd.setCommodityTypeCodeNameOriginal(inventoryItemDetail.getCommodityTypeCodeName());
          
          if(setUserInputValues) {
            fd.setEligible(inventoryItemDetail.getIsEligible().booleanValue());
            fd.setFruitVegCodeName(inventoryItemDetail.getFruitVegCodeName());
            fd.setLineItem(inventoryItemDetail.getLineItem());
            fd.setInsurableValue(inventoryItemDetail.getInsurableValue());
            fd.setPremiumRate(inventoryItemDetail.getPremiumRate());
            fd.setCommodityTypeCodeName(inventoryItemDetail.getCommodityTypeCodeName());
          }
          
          yearKeys.add(year.toString());
        }
      }
      
      form.setYears(yearKeys);
    }
  }

}
