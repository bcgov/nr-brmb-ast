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
package ca.bc.gov.srm.farm.ui.struts.calculator.status;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;

/**
 * @author awilkinson
 * @created Feb 14, 2011
 */
public class CreateMissingYearAction extends CalculatorStatusViewAction {
  
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

    logger.debug("Creating missing year...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    CalculatorStatusForm form = (CalculatorStatusForm) actionForm;
    Scenario scenario = getScenario(form);

    checkReadOnly(request, form, scenario);
    
    // do not check revision count if we are creating the current program year
    if( scenario.getYear().toString().equals(form.getYearToCreate())
        ||scenario.getRevisionCount().equals(form.getScenarioRevisionCount())) {
      CalculatorService calculatorService = ServiceFactory.getCalculatorService();
  
      final int minOperations = 1;
      final int maxOperations = 100;
  
      ActionMessages errors = new ActionMessages();
      
      Integer yearToCreate = new Integer(form.getYearToCreate());
      Integer numOperations = new Integer(form.getNumOperations());
      
      if(numOperations.intValue() < minOperations || numOperations.intValue() > maxOperations) {
        errors.add("", new ActionMessage(
            MessageConstants.ERRORS_NUMBER_OF_OPERATIONS,
            Integer.toString(minOperations), Integer.toString(maxOperations)));
      }
  
      saveErrors(request, errors);
  
      if(errors.isEmpty()) {
        calculatorService.createYear(
            scenario,
            new Integer(form.getPin()),
            yearToCreate,
            numOperations,
            ScenarioTypeCodes.GEN,
            ScenarioCategoryCodes.LOCAL_DATA_ENTRY,
            getUserId());

        scenario = refreshScenario(form);
      }
    } else {
      handleInvalidRevisionCount(request);
      forward = mapping.findForward(ActionConstants.FAILURE);
    }

    setReadOnlyFlag(request, form, scenario);
    populateForm(form, scenario);


    return forward;
  }

}
