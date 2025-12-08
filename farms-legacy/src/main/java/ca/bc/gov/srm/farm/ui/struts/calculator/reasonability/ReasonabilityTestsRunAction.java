/**
 * Copyright (c) 2018,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.reasonability;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResults;
import ca.bc.gov.srm.farm.exception.ReasonabilityTestException;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ReasonabilityTestService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.service.impl.ReasonabilityTestServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;

/**
 * Run the tests and populate form with the results.
 * @author awilkinson
 * @created Dec 10, 2018
 */
public class ReasonabilityTestsRunAction extends ReasonabilityTestsAction {

  @Override
  public ActionForward doExecute(
  	final ActionMapping mapping,
    final ActionForm actionForm, 
    final HttpServletRequest request,
    final HttpServletResponse response) throws Exception {

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    ReasonabilityTestsForm form = (ReasonabilityTestsForm) actionForm;
    Scenario scenario = getScenario(form);
    
    checkReadOnly(request, form, scenario);

    try {
      ReasonabilityTestService testService = ReasonabilityTestServiceFactory.getInstance();
      CalculatorService calculatorService = ServiceFactory.getCalculatorService();
      
      ReasonabilityTestResults results = testService.test(scenario);
      calculatorService.updateReasonabilityTests(scenario, results, getUserId());
      scenario = refreshScenario(form);
    } catch(ReasonabilityTestException e) {
      ActionMessages errors = new ActionMessages();
      errors.add("", new ActionMessage(MessageConstants.ERRORS_REASONABILITY_BENEFIT_NOT_CALCULATED));
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
    }
    
    populateForm(form, scenario);
    
    setReadOnlyFlag(request, form, scenario);
    return forward;
  }

}
