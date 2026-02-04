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
package ca.bc.gov.srm.farm.ui.struts.codes.lineitems;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.domain.codes.LineItemCode;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.util.DataParseUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 *
 * @author awilkinson
 */
public class LineItemViewAction extends LineItemAction {
  
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

    logger.debug("Viewing Line Item...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    LineItemsForm form = (LineItemsForm) actionForm;
    
    populateForm(form);
    
    return forward;
  }

  /**
   * @param form form
   * @throws Exception On Exception
   */
  protected void populateForm(LineItemsForm form) throws Exception {

    syncFilterContextWithForm(form);

    CodesService service = ServiceFactory.getCodesService();
    Integer selectedYear = form.getYearFilter();
    Integer lineItemInt = DataParseUtils.parseIntegerObject(form.getLineItem());
    LineItemCode lineItem = service.getLineItem(selectedYear, lineItemInt);
    
    form.setFruitVegListViewItems(service.getFruitVegListItems());
    form.setCommodityTypesListViewItems(service.getCommodityTypeListItems());
    if(lineItem != null) {
      form.setLineItemId(lineItem.getLineItemId());
      form.setLineItemYear(lineItem.getProgramYear());
      form.setLineItem(lineItem.getLineItem().toString());
      form.setDescription(lineItem.getDescription());
      form.setEligible(lineItem.getIsEligible().booleanValue());
      form.setEligibleRefYears(lineItem.getIsEligibleRefYears().booleanValue());
      form.setYardage(lineItem.getIsYardage().booleanValue());
      form.setContractWork(lineItem.getIsContractWork().booleanValue());
      form.setProgramPayment(lineItem.getIsProgramPayment().booleanValue());
      form.setSupplyManagedCommodity(lineItem.getIsSupplyManagedCommodity().booleanValue());
      form.setExcludeFromRevenueCalculation(lineItem.getIsExcludeFromRevenueCalculation().booleanValue());
      form.setIndustryAverageExpense(lineItem.getIsIndustryAverageExpense().booleanValue());
      form.setSectorDetailLineItemId(StringUtils.toString(lineItem.getSectorDetailLineItemId()));
      form.setSectorCode(lineItem.getSectorCode());
      form.setSectorCodeDescription(lineItem.getSectorCodeDescription());
      form.setSectorDetailCode(lineItem.getSectorDetailCode());
      form.setSectorDetailCodeDescription(lineItem.getSectorDetailCodeDescription());
      form.setRevisionCount(lineItem.getRevisionCount());
      form.setFruitVegTypeCode(lineItem.getFruitVegCodeName());
      form.setFruitVegTypeCodeDescription(lineItem.getFruitVegCodeDescription());
      form.setCommodityTypeCode(lineItem.getCommodityTypeCode());
      form.setCommodityTypeCodeDescription(lineItem.getCommodityTypeCodeDescription());
    }
  }

}
