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
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.service.BenefitService;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;

/**
 * 
 * @author awilkinson
 */
public class FarmingOperationDeleteAction extends FarmingOperationViewAction {

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

    logger.debug("Deleting Farming Operation...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    FarmingOperationForm form = (FarmingOperationForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);

    Scenario scenario = getScenario(form);
    
    String schedule = form.getSchedule();

    checkReadOnly(request, form, scenario);
    
    if(scenario.getRevisionCount().equals(form.getScenarioRevisionCount())) {
      if (errors != null && !errors.isEmpty()) {
        saveErrors(request, errors);
        forward = mapping.findForward(ActionConstants.FAILURE);
      } else {

        try {
          boolean pyvHasVerifiedScenario = getPyvHasVerifiedScenario(scenario);
          FarmingOperation fo = scenario.getFarmingYear().getFarmingOperationBySchedule(schedule);
          if(!pyvHasVerifiedScenario && fo.getIsLocallyGenerated().booleanValue()) {
            CalculatorService calculatorService = ServiceFactory.getCalculatorService();
            calculatorService.deleteFarmingOperation(fo, getUserId());
            scenario = refreshScenario(form);
            populateForm(form, scenario, request);

            BenefitService benefitService = ServiceFactory.getBenefitService();
            // ignore error messages returned
            benefitService.calculateBenefit(scenario, getUserId());
          }

        } catch(InvalidRevisionCountException irce) {
          handleInvalidRevisionCount(request);
          forward = mapping.findForward(ActionConstants.FAILURE);
        }
      }
    } else {
      handleInvalidRevisionCount(request);
      forward = mapping.findForward(ActionConstants.FAILURE);
    }

    populateScheduleOptions(form, scenario);

    setReadOnlyFlag(request, form, scenario);

    return forward;
  }

}
