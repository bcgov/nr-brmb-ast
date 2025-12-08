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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.domain.DeductionLineItem;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorAction;

/**
 * @author awilkinson
 * @created Jan 4, 2011
 */
public class DeductionLineItemsViewAction extends CalculatorAction {

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

    logger.debug("Viewing Deduction Line Items...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    IncomeExpensesForm form = (IncomeExpensesForm) actionForm;
    Scenario scenario = getScenario(form);

    populateForm(form, scenario);

    return forward;
  }

  /**
   * Fill the form fields
   * @param form The form object to fill.
   * @param scenario scenario
   * @throws Exception On Exception
   */
  private void populateForm(IncomeExpensesForm form, Scenario scenario)
  throws Exception {

    CalculatorService calculatorService = ServiceFactory.getCalculatorService();
    
    List<DeductionLineItem> deductionLineItems =
        calculatorService.getDeductionLineItems(
            scenario.getYear(), form.getDeductionLineItemType());
    
    form.setDeductionLineItems(deductionLineItems);
    
    populateRequiredYears(form, scenario);
  }

}
