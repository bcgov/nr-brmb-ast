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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.security.SecurityActionConstants;
import ca.bc.gov.srm.farm.service.BenefitService;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.CrmTransferService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorForm;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

/**
 * @author awilkinson
 * @created Feb 14, 2011
 */
public class SaveLateParticipantIndAction extends CalculatorStatusViewAction {
  
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

    logger.debug("Updating Late Participant Ind...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    CalculatorStatusForm form = (CalculatorStatusForm) actionForm;
    Scenario scenario = getScenario(form);

    checkReadOnly(request, form, scenario);
    
    // do not check revision count if we are creating the current program year
    if(scenario.getRevisionCount().equals(form.getScenarioRevisionCount())) {
      CalculatorService calculatorService = ServiceFactory.getCalculatorService();
  
      Boolean lateParticipantInd = Boolean.valueOf(form.isLateParticipant());
  
      calculatorService.saveLateParticipantInd(
          scenario,
          form.getScenarioRevisionCount(),
          lateParticipantInd,
          getUserId());

      scenario = refreshScenario(form);
      
      BenefitService benefitService = ServiceFactory.getBenefitService();
      // ignore error messages returned
      benefitService.calculateBenefit(scenario, getUserId());

      scenario = refreshScenario(form);

      // This is an extra check on the server side.
      // The screen will also prevent the user from toggling
      // the non-participant checkbox if this scenario is not a real benefit.
      boolean isRealBenefit = ScenarioUtils.categoryIsRealBenefit(scenario.getScenarioCategoryCode());
      if(isRealBenefit) {
        CrmTransferService transferService = ServiceFactory.getCrmTransferService();
        String userEmail = CurrentUser.getUser().getEmailAddress();
        transferService.scheduleBenefitTransfer(scenario, userEmail, getUserId());
      }

    } else {
      handleInvalidRevisionCount(request);
      forward = mapping.findForward(ActionConstants.FAILURE);
    }

    setReadOnlyFlag(request, form, scenario);
    populateForm(form, scenario);


    return forward;
  }

  @Override
  protected boolean canModifyScenario(
      HttpServletRequest request,
      CalculatorForm form,
      Scenario scenario) throws Exception {
    boolean result;

    boolean authorizedAction = canPerformAction(request, SecurityActionConstants.EDIT_SCENARIO);

    if(authorizedAction) {
      result = true;
    } else {
      result = false;
    }
    return result;
  }

}
