/**
 *
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;

/**
 * @author awilkinson
 * @created Jan 4, 2011
 */
public class IncomeExpensesAddNewAction extends IncomeExpensesViewAction {
  
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

    logger.debug("Adding New Income and Expenses...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    IncomeExpensesForm form = (IncomeExpensesForm) actionForm;
    Scenario scenario = getScenario(form);
    ActionMessages errors = new ActionMessages();
    
    if(isMissingStatementAReceivedDates(scenario)) {
      errors.add("", new ActionMessage(MessageConstants.ERRORS_PROVINCIAL_STATEMENT_A_RECEIVED_DATE_ADJUSTMENT_SCREENS));
    }
    
    setReadOnlyFlag(request, form, scenario);
    populateForm(form, scenario, false);

    if(errors.isEmpty()) {
      addNew(form, scenario);
    } else {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
    }

    return forward;
  }

  /**
   * Fill the form fields from the scenario
   * @param form The form object to fill.
   * @param scenario The scenario to fill the form from.
   */
  protected void addNew(IncomeExpensesForm form, Scenario scenario) {
    populateRequiredYears(form, scenario);
    
    DecimalFormat df = new DecimalFormat("00");
    
    List<Boolean> booleans = new ArrayList<>(2);
    booleans.add(Boolean.TRUE);
    booleans.add(Boolean.FALSE);
    
    boolean isNew = true;

    final int numberOfNewLines = 10;
    int itemNum = 0;
    int numAddedThisView = 0;
    for(Boolean isExpense : booleans) {
      for(Boolean isEligible : booleans) {
        while(numAddedThisView < numberOfNewLines) {
          String lineKey = IncomeExpenseFormData.getLineKey(
              df.format(itemNum),
              isExpense.booleanValue(),
              isEligible.booleanValue(),
              isNew);
          IncomeExpenseFormData fd = form.getIncomeExpense(lineKey);
          fd.setNew(isNew);
          fd.setExpense(isExpense);
          fd.setEligible(isEligible);
          itemNum++;
          numAddedThisView++;
        }
        numAddedThisView = 0;
      }
      
    }

    form.setAddedNew(true);
  }

}
