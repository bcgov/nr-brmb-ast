/**
 * Copyright (c) 2011,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.incomeexpenses;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.list.LineItemListView;
import ca.bc.gov.srm.farm.service.ListService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorAction;

/**
 * @author awilkinson
 * @created Mar 31, 2011
 */
public class IncomeExpensesListsAction extends CalculatorAction {

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

    logger.debug("Retrieving Income and Expense Line Item List...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    IncomeExpensesForm form = (IncomeExpensesForm) actionForm;
    Scenario scenario = getScenario(form);

    ServiceFactory.getListService().setLineItems(scenario);

    LineItemListView[] lineItems = (LineItemListView[]) CacheFactory
    .getUserCache().getItem(ListService.SERVER_LIST + ListService.LINE_ITEMS);
    
    LineItemListView[] escapedLineItems = new LineItemListView[lineItems.length];
    for(int ii = 0; ii < lineItems.length; ii++) {
      LineItemListView curLineItem = lineItems[ii];
      LineItemListView newLineItem = new LineItemListView();
      
      copyLineItem(curLineItem, newLineItem);

      String description = newLineItem.getDescription();
      String escapedDescription =
        org.apache.commons.lang.StringUtils.replace(description, "\"", "\\\"");
      newLineItem.setDescription(escapedDescription);
      escapedLineItems[ii] = newLineItem;
    }
    
    form.setLineItems(escapedLineItems);

    return forward;
  }

  /**
   * @param curLineItem curLineItem
   * @param newLineItem newLineItem
   */
  private void copyLineItem(LineItemListView curLineItem, LineItemListView newLineItem) {
    
    newLineItem.setLineItem(curLineItem.getLineItem());
    newLineItem.setDescription(curLineItem.getDescription());
    newLineItem.setEligible(curLineItem.getEligible());
    newLineItem.setIneligible(curLineItem.getIneligible());
  }

}
