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

import static ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes.*;
import static ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes.*;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.domain.codes.TriageQueueCodes;
import ca.bc.gov.srm.farm.domain.enrolment.EnwEnrolment;
import ca.bc.gov.srm.farm.domain.reasonability.ReasonabilityTestResults;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.security.SecurityActionConstants;
import ca.bc.gov.srm.farm.service.BenefitService;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ReasonabilityTestService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.service.impl.ReasonabilityTestServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.ScenarioUtils;
import ca.bc.gov.srm.farm.util.StringUtils;


/**
 * @author awilkinson
 * @created Dec 20, 2010
 */
public class ScenariosSaveAction extends ScenariosViewAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Saving Scenario...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    ScenariosForm form = (ScenariosForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);

    Scenario scenario = getScenario(form);
    
    checkReadOnly(request, form, scenario);
    
    if(scenario.getRevisionCount().equals(form.getScenarioRevisionCount())) {
      boolean populateEditableFields = false;
      String oldStateCode = scenario.getScenarioStateCode();
      String newStateCode = form.getScenarioStateCode();
      String stateChangeReason = form.getStateChangeReason();
      String oldCategoryCode = scenario.getScenarioCategoryCode();
      Integer verifiedUserId = form.getVerifierUserId();
      
      // Only allowed to change Scenario Category if the state is In Progress
      // and the scenario has not previously been Verified, Pre-Verified or Enrolment Notice Complete.
      String newCategoryCode = scenario.getScenarioCategoryCode();
      if(oldStateCode.equals(IN_PROGRESS) && scenario.categoryIsOneOf(UNKNOWN)) {
        newCategoryCode = form.getScenarioCategoryCode();
      }
      boolean categoryChanged = !newCategoryCode.equals(oldCategoryCode);
      boolean wasRealBenefit = ScenarioUtils.categoryIsRealBenefit(oldCategoryCode);
      boolean isRealBenefit = ScenarioUtils.categoryIsRealBenefit(newCategoryCode);
      boolean hasInProgressRealBenefit = ScenarioUtils.programYearHasInProgressRealBenefit(scenario);
      boolean canVerifyScenario = canPerformAction(request, SecurityActionConstants.VERIFY_SCENARIO);
      boolean isCompletedCoverageNotice = COMPLETED.equals(newStateCode) && COVERAGE_NOTICE.equals(newCategoryCode);
      
      // If reopening, then the category will not be set in the form data, so set it.
      if(oldStateCode.equals(CLOSED) && newStateCode.equals(IN_PROGRESS)) {
        form.setScenarioCategoryCode(oldCategoryCode);
      }

      if(categoryChanged && !wasRealBenefit && isRealBenefit && hasInProgressRealBenefit) {
        errors.add("", new ActionMessage(MessageConstants.ERRORS_MULTIPLE_IN_PROGRESS_REAL_BENEFITS));
        
      } else if(newStateCode.equals(VERIFIED) && !canVerifyScenario) {
        errors.add("", new ActionMessage(MessageConstants.ERRORS_DO_NOT_HAVE_PERMISSION_TO_VERIFY));
        
      } else if(StringUtils.isOneOf(newStateCode, VERIFIED, PRE_VERIFIED, ENROLMENT_NOTICE_COMPLETE) || isCompletedCoverageNotice) {
        String newState;
        switch(newStateCode) {
        case VERIFIED:
          newState = "Verified";
          break;
        case PRE_VERIFIED:
          newState = "Pre-Verified";
          break;
        case ENROLMENT_NOTICE_COMPLETE:
          newState = "Enrolment Notice Complete";
          break;
        default:
          newState = newStateCode;
          break;
        }
        boolean benefitNotCalculated = false;
        
        if(categoryChanged && newCategoryCode.equals(INTERIM)) {
          benefitNotCalculated = true;
          
        } else if(!scenario.isBenefitSuccessfullyCalculated()) {
          benefitNotCalculated = true;

        } else if(scenario.isInCombinedFarm()) {
        
          List<Scenario> scenarios = scenario.getCombinedFarm().getScenarios();
          for(Scenario curScenario : scenarios) {
            Benefit curBenefit = curScenario.getFarmingYear().getBenefit();
            if(curBenefit == null || curBenefit.getTotalBenefit() == null) {
              benefitNotCalculated = true;
              break;
            }
          }
          
        }
        
        if(benefitNotCalculated) {
          errors.add("", new ActionMessage(MessageConstants.ERRORS_STATE_CHANGE_BENEFIT_NOT_CALCULATED, newState));
          
        } else if(ENROLMENT_NOTICE_COMPLETE.equals(newStateCode)) {
          EnwEnrolment enw = scenario.getEnwEnrolment();
          
          boolean enrolmentFeeNotCalculated = enw == null || enw.getEnrolmentFee() == null;
          boolean calcTypeIsBenefitAndBenefitNotCalculated = benefitNotCalculated && enw != null
              && EnwEnrolment.CALCULATION_TYPE_BENEFIT_MARGINS.equals(enw.getEnrolmentCalculationTypeCode());
          
          if(enrolmentFeeNotCalculated || calcTypeIsBenefitAndBenefitNotCalculated) {
            errors.add("", new ActionMessage(MessageConstants.ERRORS_STATE_CHANGE_ENROLMENT_NOT_CALCULATED, newState));
          }
        }
        
        if(CalculatorConfig.reasonabilityTestsRequired(scenario.getYear(), newCategoryCode)) {
          ReasonabilityTestResults reasonabilityTestResults = scenario.getReasonabilityTestResults();
          if(reasonabilityTestResults == null || ! reasonabilityTestResults.getIsFresh()) {
            errors.add("", new ActionMessage(MessageConstants.ERRORS_STATE_REASONABILITY_TESTS_NOT_RUN, newState));
          }
        }

        if ((verifiedUserId == null || verifiedUserId == 0) &&
            newStateCode.equals(VERIFIED) && StringUtils.isOneOf(newCategoryCode, INTERIM, FINAL, PRODUCER_ADJUSTMENT)) {
          errors.add("", new ActionMessage(MessageConstants.ERROR_VERIFIED_BY_REQUIRED));
        }
        
        populateForm(form, scenario, populateEditableFields);
      }
      
      if(scenario.getScenarioStateCode().equals(newStateCode)
          && ! StringUtils.isBlank(stateChangeReason)) {
        errors.add("", new ActionMessage(MessageConstants.ERRORS_REASON_WITHOUT_STATE_CHANGE));
        populateForm(form, scenario, populateEditableFields);
      }
      
  
      if (errors != null && !errors.isEmpty()) {
        saveErrors(request, errors);
        forward = mapping.findForward(ActionConstants.FAILURE);
      } else {
        CalculatorService calculatorService = ServiceFactory.getCalculatorService();
        
        try {
          scenario.setDescription(form.getScenarioDescription());
          scenario.setIsDefaultInd(Boolean.valueOf(form.isDefaultInd()));
          
          if(verifiedUserId == null || verifiedUserId == 0) {
            scenario.setVerifierUserId(null);
          } else {
            scenario.setVerifierUserId(verifiedUserId);
          }
          
          List<ActionMessage> updateErrors = calculatorService.updateScenario(
              scenario,
              newStateCode,
              stateChangeReason,
              newCategoryCode,
              CurrentUser.getUser().getEmailAddress(),
              null,
              null,
              null,
              null,
              getUserId());
          
          scenario = refreshScenario(form);
          
          if(updateErrors.isEmpty()) {
            if(newStateCode.equals(ScenarioStateCodes.PRE_VERIFIED)
                && scenario.getPreVerificationChecklist().getTriageQueue().equals(TriageQueueCodes.ZERO_PAYMENT_PASS)
                && ! ScenarioUtils.programYearHasVerifiedFinal(scenario)) {
              forward = createVerifiedFinal(mapping, request, form);
              scenario = getScenario(form); // createVerifiedFinal loads the new scenario, so get it
            }
            populateForm(form, scenario, populateEditableFields);
          } else {
            errors = new ActionMessages();
            for(ActionMessage updateError : updateErrors) {
              errors.add("", updateError);
            }
            saveErrors(request, errors);
            forward = mapping.findForward(ActionConstants.FAILURE);
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

    form.setAssignedToCurrentUser(isAssignedToCurrentUser(scenario));
    setReadOnlyFlag(request, form, scenario);
    populateScenarioOptions(form, scenario);
    populateSelectBoxOptions(form, scenario, request);

    return forward;
  }

  private ActionForward createVerifiedFinal(
      ActionMapping mapping,
      HttpServletRequest request,
      ScenariosForm form) throws Exception {

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    CalculatorService calculatorService = ServiceFactory.getCalculatorService();
    String user = getUserId();
    Scenario scenario = getScenario(form);
    Integer programYear = scenario.getYear();
    
    Integer newScenarioNumber = calculatorService.saveScenarioAsNew(scenario.getScenarioId(),
        ScenarioTypeCodes.USER, FINAL, programYear, user);

    form.setScenarioNumber(newScenarioNumber);

    scenario = refreshScenario(form);
    
    BenefitService benefitService = ServiceFactory.getBenefitService();
    // ignore error messages returned
    benefitService.calculateBenefit(scenario, user);
    
    ReasonabilityTestService testService = ReasonabilityTestServiceFactory.getInstance();
    ReasonabilityTestResults results = testService.test(scenario);
    calculatorService.updateReasonabilityTests(scenario, results, user);
    scenario = refreshScenario(form);
    
    scenario.setIsDefaultInd(true);
    List<ActionMessage> updateErrors = calculatorService.updateScenario(
        scenario,
        VERIFIED,
        null,
        scenario.getScenarioCategoryCode(),
        CurrentUser.getUser().getEmailAddress(),
        null,
        null,
        null,
        null,
        getUserId());
    
    scenario = refreshScenario(form);
    populateForm(form, scenario, true);
    
    if( ! updateErrors.isEmpty() ) {
      ActionMessages errors = new ActionMessages();
      for(ActionMessage updateError : updateErrors) {
        errors.add("", updateError);
      }
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
    }
    
    return forward;
  }

}
