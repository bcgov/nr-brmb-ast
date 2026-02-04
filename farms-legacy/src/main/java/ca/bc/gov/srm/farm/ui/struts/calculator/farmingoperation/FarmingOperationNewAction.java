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
package ca.bc.gov.srm.farm.ui.struts.calculator.farmingoperation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;

/**
 * 
 * @author awilkinson
 *
 */
public class FarmingOperationNewAction extends FarmingOperationViewAction {
  
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

    logger.debug("Viewing Create New Operation...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    FarmingOperationForm form = (FarmingOperationForm) actionForm;
    Scenario scenario = getScenario(form);
    
    setReadOnlyFlag(request, form, scenario);

    boolean pyvHasVerifiedScenario = getPyvHasVerifiedScenario(scenario);
    
    if(!pyvHasVerifiedScenario) {
      CalculatorService calculatorService = ServiceFactory.getCalculatorService();
      String schedule =
        calculatorService.getNewOperationSchedule(scenario.getClient().getClientId());
      
      final String defaultPartnershipPin = "0";
      final String defaultPartnershipPercent = "100";

      form.setSchedule(schedule);
      form.setPartnershipPin(defaultPartnershipPin);
      form.setPartnershipPercent(defaultPartnershipPercent);
      form.setScenarioRevisionCount(scenario.getRevisionCount());
      form.setNew(true);
    }

    return forward;
  }

}
