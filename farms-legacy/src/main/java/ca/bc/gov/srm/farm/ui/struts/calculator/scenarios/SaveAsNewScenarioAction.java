/**
 *
 * Copyright (c) 2010,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.scenarios;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.service.AdjustmentService;
import ca.bc.gov.srm.farm.service.BenefitService;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;


/**
 * @author awilkinson
 * @created Dec 20, 2010
 */
public class SaveAsNewScenarioAction extends ScenariosViewAction {
  
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

    logger.debug("Saving as new Scenario...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    ScenariosForm form = (ScenariosForm) actionForm;
    Scenario scenario = getScenario(form);
    

    CalculatorService calculatorService = ServiceFactory.getCalculatorService();
    
    try {
      boolean basedOnCra = ScenarioTypeCodes.CRA.equals(scenario.getScenarioTypeCode());
      int programYear = form.getYear();

      Integer newScenarioNumber = calculatorService.saveScenarioAsNew(
          scenario.getScenarioId(),
          ScenarioTypeCodes.USER,
          ScenarioCategoryCodes.UNKNOWN,
          programYear,
          getUserId());

      form.setScenarioNumber(newScenarioNumber);

      scenario = refreshScenario(form);

      if(basedOnCra) {
        AdjustmentService adjService = ServiceFactory.getAdjustmentService();
        adjService.makeInventoryValuationAdjustments(scenario);
        // refresh the scenario again to get the adjustments
        scenario = refreshScenario(form);
      }
      
      BenefitService benefitService = ServiceFactory.getBenefitService();
      // ignore error messages returned
      benefitService.calculateBenefit(scenario, getUserId());

      populateForm(form, scenario);
      populateScenarioOptions(form, scenario);

    } catch(InvalidRevisionCountException irce) {
      handleInvalidRevisionCount(request);
      forward = mapping.findForward(ActionConstants.FAILURE);
    }
    
    setReadOnlyFlag(request, form, scenario);
    form.setAssignedToCurrentUser(isAssignedToCurrentUser(scenario));
    populateSelectBoxOptions(form, scenario, request);

    return forward;
  }

}
