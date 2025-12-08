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
package ca.bc.gov.srm.farm.ui.struts.calculator.sc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.calculator.BenefitNullFixer;
import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.calculator.CalculatorFactory;
import ca.bc.gov.srm.farm.calculator.StructuralChangeCalculator;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.service.BenefitService;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;

/**
 * Action to save from screen 930.
 */
public class StructuralChangeSaveAction extends StructuralChangeViewAction {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("start");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    StructuralChangeForm form = (StructuralChangeForm) actionForm;

    Scenario scenario = getScenario(form);
    setReadOnlyFlag(request, form, scenario);
    
    if(scenario.getRevisionCount().equals(form.getScenarioRevisionCount())) {
      // note that there's no form validation needed
      updateScenario(form, scenario);
      
      String userId = CurrentUser.getUser().getUserId();
      
      // calculate the whole benefit
      BenefitService service = ServiceFactory.getBenefitService();
      
      try {
          ActionMessages errors = service.calculateBenefit(scenario, userId);
          
          if (!errors.isEmpty()) {
            saveErrors(request, errors);
            forward = mapping.findForward(ActionConstants.FAILURE);
          } else {
            CalculatorService calculatorService = ServiceFactory.getCalculatorService();
            calculatorService.flagReasonabilityTestsStale(scenario, userId);
            
            addScenarioLog(form, scenario, "Update Structural Change");

            // update revision count
            scenario = refreshScenario(form);
            form.setScenarioRevisionCount(scenario.getRevisionCount());
          }
      } catch(InvalidRevisionCountException irce) {
          handleInvalidRevisionCount(request);
          forward = mapping.findForward(ActionConstants.FAILURE);
      }
    } else {
      handleInvalidRevisionCount(request);
      forward = mapping.findForward(ActionConstants.FAILURE);
    }
    
    // get what we need to populate the JSP
    initForm(form, scenario);
    
    ActionMessages warnings = getMismatchingScWarnings(form);
    if(!warnings.isEmpty()) {
      saveMessages(request, warnings);
    }
    
    return forward;
  }
  
  
  private void updateScenario(StructuralChangeForm form, Scenario scenario) {

    BenefitNullFixer nullFixer = CalculatorFactory.getBenefitNullFixer(scenario);
    nullFixer.fixNulls(scenario);

    StructuralChangeCalculator scCalc = CalculatorFactory.getStructuralChangeCalculator(scenario);
    
  	boolean[] leadBpuInd = form.getIsLead();
  	scCalc.updateBpuLeadInds(leadBpuInd);
		
		// set the SC method
		String scmCode = form.getStructuralChangeMethod();
		String escmCode;
		
		// For 2024 forward we only have one field for SC code 
		if(form.getYear() >= CalculatorConfig.GROWING_FORWARD_2024) {
		  escmCode = scmCode;
		} else {
      escmCode = form.getExpenseStructuralChangeMethod();
		}
		
		scCalc.updateStructuralChangeCode(scmCode, escmCode);
		logger.debug("scmCode: " + scmCode + ", escmCode: " + escmCode);
  }
  
}
