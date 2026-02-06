/**
 *
 * Copyright (c) 2021,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.enw.enrolment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.enrolment.EnwEnrolment;
import ca.bc.gov.srm.farm.enrolment.EnrolmentCalculatorFactory;
import ca.bc.gov.srm.farm.enrolment.EnwEnrolmentCalculator;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.service.BenefitService;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorAction;
import ca.bc.gov.srm.farm.util.JsonUtils;

/**
 * Action to view screen 860.
 */
public class EnwEnrolmentViewAction extends CalculatorAction {

  private Logger logger = LoggerFactory.getLogger(getClass());
  
  protected static ObjectMapper jsonObjectMapper = JsonUtils.getJsonObjectMapper();

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Viewing ENW Enrolment");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    ActionMessages errors = new ActionMessages();
    EnwEnrolmentForm form = (EnwEnrolmentForm) actionForm;
    
    form.setRefresh(true);
    Scenario scenario = getScenario(form);
    
    setReadOnlyFlag(request, form, scenario);
    
    BenefitService benefitService = ServiceFactory.getBenefitService();
    if(!form.isReadOnly()) {
    	// validate, calculate, save
      String userId = CurrentUser.getUser().getUserId();
      CalculatorService calculatorService = ServiceFactory.getCalculatorService();
      
      try {
        errors = benefitService.calculateBenefit(scenario, userId);
        if(errors.isEmpty()) {
          calculatorService.updateScenarioRevisionCount(scenario, getUserId());
        } else {
          saveErrors(request, errors);
        }
      } catch(InvalidRevisionCountException irce) {
          handleInvalidRevisionCount(request);
          forward = mapping.findForward(ActionConstants.FAILURE);
      }
    }
    
    EnwEnrolmentCalculator enwEnrolmentCalculator = EnrolmentCalculatorFactory.getEnwEnrolmentCalculator();
    
    if(!form.isReadOnly() && scenario.isInProgress()) {
      CalculatorService calculatorService = ServiceFactory.getCalculatorService();
      
      boolean benefitCalculated = errors.isEmpty();
      calculatorService.calculateEnwEnrolment(scenario, benefitCalculated, getUserId());

      scenario = refreshScenario(form);
      errors = benefitService.calculateBenefit(scenario, getUserId(), false, true, false);
      
      enwEnrolmentCalculator.initNonEditableFields(scenario);
      
    } else {
      enwEnrolmentCalculator.initNonEditableFields(scenario);
    }
    
    initForm(form, scenario);
  
    return forward;
  }


  protected void initForm(EnwEnrolmentForm form, Scenario scenario) throws Exception {
    EnwEnrolment enw = scenario.getEnwEnrolment();
    
    form.setScenarioRevisionCount(scenario.getRevisionCount());
    
    String enrolmentJson = jsonObjectMapper.writeValueAsString(enw);
    form.setEnrolmentJson(enrolmentJson);
  }
  
}
