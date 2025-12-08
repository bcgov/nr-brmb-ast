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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.ScenarioMetaData;
import ca.bc.gov.srm.farm.domain.ScenarioStateAudit;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.list.CodeListView;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.list.UserListView;
import ca.bc.gov.srm.farm.security.SecurityActionConstants;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ListService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorAction;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorForm;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.ScenarioUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * @author awilkinson
 * @created Dec 20, 2010
 */
public class ScenariosViewAction extends CalculatorAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Viewing Scenarios...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    ScenariosForm form = (ScenariosForm) actionForm;
    Scenario scenario = getScenario(form);

    boolean scNumRequested =
      form.getScenarioNumber() != null && form.getScenarioNumber().intValue() != 0;
    if(scenario == null || scenario.getScenarioId() == null
        || (scNumRequested && !form.getScenarioNumber().equals(scenario.getScenarioNumber()))) {
    	//
    	// If the user entered a bad PIN or year on the URL then
    	// give them an error page rather than an exception. For some 
    	// reason the "read" service never returns a null scenario.
    	//
    	ActionMessages errors = new ActionMessages();
    	String pin = String.valueOf(form.getPin());
    	String year = String.valueOf(form.getYear());
    	String[] msgParameters;

      String msgKey;
      if(ScenariosForm.PURPOSE_ENROLMENT.equals(form.getPurpose())) {
        msgKey = MessageConstants.ERRORS_830_INVALID_SCENARIO_ENROLMENT;
        String enrolmentYear = String.valueOf(form.getYear() + 2);
        msgParameters = new String[]{year, pin, enrolmentYear};
      } else if(scNumRequested) {
        msgKey = MessageConstants.ERRORS_830_INVALID_SCENARIO_WITH_NUMBER;
        String scNumStr = form.getScenarioNumber().toString();
        msgParameters = new String[]{pin, year, scNumStr};
      } else {
        msgKey = MessageConstants.ERRORS_830_INVALID_SCENARIO;
        msgParameters = new String[]{pin, year};
      }

    	errors.add("", new ActionMessage(msgKey, msgParameters));
    	saveErrors(request, errors);
    	
    	forward = mapping.findForward(ActionConstants.INVALID);
    } else {
    	//
    	// Good PIN and year
    	//
	    setReadOnlyFlag(request, form, scenario);
	    form.setAssignedToCurrentUser(isAssignedToCurrentUser(scenario));
	    populateScenarioOptions(form, scenario);
	    populateForm(form, scenario);
      populateSelectBoxOptions(form, scenario, request);
    }

    return forward;
  }

  /**
   * Fill the form fields from the account
   * @param form The form object to fill.
   * @param scenario The scenario to fill the form from.
   * @throws Exception On Exception
   */
  protected void populateForm(
      ScenariosForm form,
      Scenario scenario)
  throws Exception {

    boolean populateEditableFields = true;
    populateForm(form, scenario, populateEditableFields);
  }


  protected void populateForm(
      ScenariosForm form,
      Scenario scenario,
      boolean populateEditableFields)
  throws Exception {
    
    CalculatorService calcService = ServiceFactory.getCalculatorService();

    Integer participantPin = scenario.getClient().getParticipantPin();
    Integer year = scenario.getYear();
    Integer inProgressCombinedFarmNumber = calcService.getInProgressCombinedFarmNumber(participantPin, year);
    form.setInProgressCombinedFarmNumber(StringUtils.toString(inProgressCombinedFarmNumber));
    
    Integer verifiedCombinedFarmNumber = calcService.getVerifiedCombinedFarmNumber(participantPin, year);
    form.setVerifiedCombinedFarmNumber(StringUtils.toString(verifiedCombinedFarmNumber));
    
    // check that the combined farm matches previous the combined farm
    // of the most recent verified scenario so we can warn the user of the change
    // on set to Verified
    if(scenario.getScenarioStateCode().equals(IN_PROGRESS)
        && verifiedCombinedFarmNumber != null) {
      boolean combinedFarmMatchesVerified =
          calcService.combinedFarmMatchesVerified(scenario.getScenarioId(), verifiedCombinedFarmNumber);
      boolean combinedFarmChanged = !combinedFarmMatchesVerified;
      form.setCombinedFarmChanged(combinedFarmChanged);
    } else {
      form.setCombinedFarmChanged(false);
    }

    form.setOldScenarioStateCode(scenario.getScenarioStateCode());
    form.setScenarioRevisionCount(scenario.getRevisionCount());
    
    populateRefScenarioIdMap(form, scenario);
    
    if(populateEditableFields) {
      form.setScenarioStateCode(scenario.getScenarioStateCode());
      form.setScenarioCategoryCode(scenario.getScenarioCategoryCode());
      form.setDefaultInd(scenario.getIsDefaultInd().booleanValue());
      form.setScenarioDescription(scenario.getDescription());
      form.setVerifierUserId(scenario.getVerifierUserId());
      
      List<ScenarioStateAudit> scenarioStateAudits = scenario.getScenarioStateAudits();
      if(scenarioStateAudits == null || scenarioStateAudits.isEmpty()) {
        form.setStateChangeReason(null);
      } else {
        ScenarioStateAudit mostRecent = scenarioStateAudits.get(0);
        form.setStateChangeReason(mostRecent.getStateChangeReason());
      }
    }
  }
  
  
  protected void populateSelectBoxOptions(ScenariosForm form, Scenario scenario, HttpServletRequest request) throws Exception {
    if(!form.isReadOnly()) {
      populateScenarioStateOptions(form, scenario, request);
      populateScenarioCategoryOptions(form, scenario);
      populateVerifierOptions(form);
    }
  }


  protected void populateScenarioOptions(ScenariosForm form, Scenario scenario) {
    List<ScenarioMetaData> scenarios = scenario.getScenarioMetaDataList();
    List<ListView> options = new ArrayList<>(scenarios.size());
    for(ScenarioMetaData curScenario : scenarios) {
      if(curScenario.getProgramYear().equals(scenario.getYear())) {
        String curScenarioNumber = curScenario.getScenarioNumber().toString();
        ListView item = new CodeListView(curScenarioNumber, "Scenario " + curScenarioNumber);
        options.add(item);
      }
    }
    
    form.setScenarioNumberOptions(options);
  }
  
  
  protected void populateScenarioStateOptions(
      ScenariosForm form,
      Scenario scenario,
      HttpServletRequest request)
  throws Exception {
    ListView[] statesArray = ServiceFactory.getListService().getListArray(ListService.SCENARIO_STATE);
    List<ListView> states = new ArrayList<>();
    
    boolean canVerifyScenario = canPerformAction(request, SecurityActionConstants.VERIFY_SCENARIO);

    for(ListView stateItem : statesArray) {
      String stateCode = stateItem.getValue();
      
      if(stateCode.equals(IN_PROGRESS)) {
         states.add(stateItem);

      } else if (stateCode.equals(CLOSED)){
        boolean hasBeenVerified = scenario.isHasBeenVerified();
        boolean canReopenVerified = canPerformAction(request, SecurityActionConstants.REOPEN_VERIFIED_SCENARIO);
        // If the user can reopen a Verified scenario they can also close a scenario that has been verified.
        if(!hasBeenVerified || canReopenVerified) {
          states.add(stateItem);
        }
        
      } else if (stateCode.equals(VERIFIED)){
        if (scenario.categoryIsOneOf(ADMINISTRATIVE_ADJUSTMENT, PRODUCER_ADJUSTMENT, INTERIM, FINAL)
            && canVerifyScenario) {
          states.add(stateItem);
        }
        
      } else if (stateCode.equals(PRE_VERIFIED)){
        if (scenario.categoryIsOneOf(PRE_VERIFICATION)) {
          states.add(stateItem);
        }
      } else if (stateCode.equals(ENROLMENT_NOTICE_COMPLETE)){
        if (scenario.categoryIsOneOf(ENROLMENT_NOTICE_WORKFLOW)) {
          states.add(stateItem);
        }
      } else if (stateCode.equals(COMPLETED)){
        if (scenario.categoryIsOneOf(NOL, COVERAGE_NOTICE)) {
          states.add(stateItem);
        }
      }
    }
    form.setScenarioStateSelectOptions(states);
  }
  
  
  protected void populateScenarioCategoryOptions(
      ScenariosForm form,
      Scenario scenario)
  throws Exception {
    ListView[] categoriesArray = ServiceFactory.getListService().getListArray(ListService.SCENARIO_CATEGORY);
    List<ListView> categorySelectOptions = new ArrayList<>();

    // check for existence of a scenario in current program year with state Verified and category Final
    boolean verifiedFinalExists = ScenarioUtils.programYearHasVerifiedFinal(scenario);
    
    // business logic for which categories to display in drop down options
    Set<String> allowedCategories = new HashSet<>();
    
    // always allowed
    allowedCategories.add(COMPARISON_SCENARIO);
    allowedCategories.add(ENROLMENT_NOTICE_WORKFLOW);
    
    // always add the selected category to the list
    allowedCategories.add(scenario.getScenarioCategoryCode());
    
    if(verifiedFinalExists) {
      allowedCategories.add(ADMINISTRATIVE_ADJUSTMENT);
      allowedCategories.add(PRODUCER_ADJUSTMENT);
    } else {
      allowedCategories.add(FINAL);
      allowedCategories.add(INTERIM);
    }

    for(ListView categoryItem : categoriesArray) {
      String curCategoryCode = categoryItem.getValue();
      
      if(allowedCategories.contains(curCategoryCode)) {
        categorySelectOptions.add(categoryItem);
      }
    }
    form.setScenarioCategorySelectOptions(categorySelectOptions);
  }


  protected void populateRefScenarioIdMap(ScenariosForm form, Scenario scenario) {
    Map<Integer, ReferenceScenario> scenarioByIdMap = new HashMap<>();
    scenarioByIdMap.put(scenario.getScenarioId(), scenario);
    for(ReferenceScenario rs : scenario.getReferenceScenarios()) {
      scenarioByIdMap.put(rs.getScenarioId(), rs);
    }
    form.setScenarioByIdMap(scenarioByIdMap);
  }
  
  protected void populateVerifierOptions(ScenariosForm form) throws ServiceException {
    
    ListService listService = ServiceFactory.getListService();

    UserListView[] userListViews = null;
    try {
      userListViews = (UserListView[]) listService.getListWithExpireDate(ListService.VERIFIERS, CurrentUser.getUser().getUserId());
    } catch (ServiceException e) {
      logger.error(e.getMessage());
      throw new ServiceException(e);  
    }
    
    List<ListView> verifiers = new ArrayList<>();
    for (UserListView ulv : userListViews) {
      verifiers.add(ulv);
    }

    form.setVerifierOptions(verifiers);
  }
  

  @Override
  protected boolean isReadOnly(
      HttpServletRequest request,
      CalculatorForm form,
      Scenario scenario) throws Exception {

    boolean result;

    boolean inProgress = IN_PROGRESS.equals(scenario.getScenarioStateCode());
    boolean verified = VERIFIED.equals(scenario.getScenarioStateCode());
    boolean closed = CLOSED.equals(scenario.getScenarioStateCode());
    boolean completed = COMPLETED.equals(scenario.getScenarioStateCode());
    boolean enComplete = ENROLMENT_NOTICE_COMPLETE.equals(scenario.getScenarioStateCode());

    boolean authorizedAction = canPerformAction(request, SecurityActionConstants.EDIT_SCENARIO);
    boolean canReopenVerified = canPerformAction(request, SecurityActionConstants.REOPEN_VERIFIED_SCENARIO);
    boolean userScenario = scenario.getScenarioTypeCode().equals(ScenarioTypeCodes.USER);
    boolean assignedTo = isAssignedToCurrentUser(scenario);
    boolean editableStatus =
        inProgress
        || (verified && canReopenVerified)
        || (enComplete && canReopenVerified)
        || (closed && canReopenVerified)
        || (completed && canReopenVerified);

    if(authorizedAction && userScenario && assignedTo && editableStatus) {
      result = false;
    } else {
      result = true;
    }
    
    return result;
  }

}
