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

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.calculator.BenefitCalculator;
import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.calculator.CalculatorFactory;
import ca.bc.gov.srm.farm.calculator.ReferenceYearCalculator;
import ca.bc.gov.srm.farm.calculator.StructuralChangeCalculator;
import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.StructuralChangeCodes;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.service.BenefitService;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.service.StructuralChangeService;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.domain.calculator.StructuralChangeRow;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorAction;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

/**
 * Action to view screen 930.
 */
public class StructuralChangeViewAction extends CalculatorAction {

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

    logger.debug("start");

    ActionMessages errors = null;
    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    StructuralChangeForm form = (StructuralChangeForm) actionForm;

    form.setRefresh(true);
    Scenario scenario = getScenario(form);
    setReadOnlyFlag(request, form, scenario);
    
    if(!form.isReadOnly()) {
    	// validate, calculate, save
      String userId = CurrentUser.getUser().getUserId();
      BenefitService service = ServiceFactory.getBenefitService();
      CalculatorService calculatorService = ServiceFactory.getCalculatorService();
      try {
          errors = service.calculateBenefit(scenario, userId);
          if(errors.isEmpty()) {
            calculatorService.updateScenarioRevisionCount(scenario, getUserId());
          }
      } catch(InvalidRevisionCountException irce) {
          handleInvalidRevisionCount(request);
          forward = mapping.findForward(ActionConstants.FAILURE);
      }
    }
    
    form.setScenarioRevisionCount(scenario.getRevisionCount());
    initForm(form, scenario);
    
    if (errors != null && !errors.isEmpty()) {
      saveErrors(request, errors);
    }
    
    ActionMessages warnings = getMismatchingScWarnings(form);
    if(!warnings.isEmpty()) {
      saveMessages(request, warnings);
    }
    
    return forward;
  }
  
  public void initForm(StructuralChangeForm form, Scenario scenario) throws Exception {
    StructuralChangeService service = ServiceFactory.getStructuralChangeService();
    StructuralChangeCalculator scCalc = CalculatorFactory.getStructuralChangeCalculator(scenario);
    BenefitCalculator benCalc = CalculatorFactory.getBenefitCalculator(scenario);
    
    List<StructuralChangeRow> rows = service.getStructuralChanges(scenario);
    form.setRows(rows);
    
    boolean[] bpuLeads = scCalc.getBpuLeadIndArray();
    
    for(int index = 0; index < bpuLeads.length; index++) {
      
      form.setIsLead(index, bpuLeads[index]);
    }
    
    //
    // Get the method code.
    //
    Benefit benefit = benCalc.getBenefit(); 
    String scmCode = StructuralChangeCodes.NONE;
    String expScmCode = StructuralChangeCodes.NONE;
    
    // This screen used to allow looking at CRA scenarios which don't have a benefit.
    if(benefit != null) {
      scmCode = benefit.getStructuralChangeMethodCode();
      expScmCode = benefit.getExpenseStructuralChangeMethodCode();
    }
    form.setStructuralChangeMethod(scmCode);
    form.setExpenseStructuralChangeMethod(expScmCode);
    
    if(form.isGrowingForward2024()) {
      populateUsedInCalc(form, scenario);
    }
  }

  
  public void populateUsedInCalc(StructuralChangeForm form, Scenario scenario) throws Exception {
    
    int referenceYearsCount = scenario.getReferenceScenarios().size();
    boolean[] usedInRatioCalc = new boolean[referenceYearsCount];
    boolean[] usedInAdditiveCalc = new boolean[referenceYearsCount];
    
    boolean benefitSuccessfullyCalculated = scenario.isBenefitSuccessfullyCalculated();
    
    if(benefitSuccessfullyCalculated) {
    
      ReferenceYearCalculator refYearCalc = CalculatorFactory.getReferenceYearCalculator(scenario);
      Map<Integer, MarginTotal> ratioRefYearMargins = refYearCalc.getReferenceYearMargins(
          scenario, true, false, CalculatorConfig.STRUCTURAL_CHANGE_METHOD_RATIO);
      Map<Integer, MarginTotal> additiveRefYearMargins = refYearCalc.getReferenceYearMargins(
          scenario, true, false, CalculatorConfig.STRUCTURAL_CHANGE_METHOD_ADDITIVE);
      
      List<Integer> refYears = ScenarioUtils.getReferenceYears(scenario.getYear());
      int index = 0;
      for(Integer curYear : refYears) {
        
        usedInRatioCalc[index] = ratioRefYearMargins.get(curYear) != null;
        usedInAdditiveCalc[index] = additiveRefYearMargins.get(curYear) != null;
        index++;
      }
      
    }
    
    form.setUsedInRatioCalc(usedInRatioCalc);
    form.setUsedInAdditiveCalc(usedInAdditiveCalc);
  }
  
  /**
   * @param form 
   * @return ActionMessages
   */
  protected ActionMessages getMismatchingScWarnings(StructuralChangeForm form) {
  	ActionMessages warnings = new ActionMessages();

  	if(!form.isGrowingForward2024()) {
      warnings.add("", new ActionMessage(MessageConstants.WARNING_MISMATCHING_STRUCTURAL_CHANGES));
  	}
    
    return warnings;
  }
}
