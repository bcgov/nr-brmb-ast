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
package ca.bc.gov.srm.farm.ui.struts.calculator;

import static ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes.*;
import static ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes.*;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.ScenarioMetaData;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.list.CodeListView;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.security.SecurityActionConstants;
import ca.bc.gov.srm.farm.service.AdjustmentService;
import ca.bc.gov.srm.farm.service.BenefitService;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ListServiceConstants;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.domain.CalculatorInboxContext;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.ProgramYearUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

/**
 * 
 * @author awilkinson
 *
 */
public abstract class CalculatorAction extends SecureAction {

  public static final String FARM_VIEW_WHOLE_FARM_CODE = "WHOLE";
  public static final String FARM_VIEW_WHOLE_FARM_DISPLAY = "Whole Farm View";
  public static final String FARM_VIEW_COMBINED_FARM_DISPLAY = "Combined Farm View";
  
  private Logger logger = LoggerFactory.getLogger(getClass());
  private CalculatorService calculatorService = ServiceFactory.getCalculatorService();

  public static final NumberFormat currencyOutputFormat = NumberFormat.getInstance();
  static {
    currencyOutputFormat.setMinimumFractionDigits(2);
    currencyOutputFormat.setMaximumFractionDigits(2);
    currencyOutputFormat.setGroupingUsed(false);
  }  

  /**
   * @param   mapping     mapping
   * @param   actionForm  actionForm
   * @param   request     request
   * @param   response    response
   * @return  The return value
   * @throws  Exception  On Exception
   */
  @Override
  public ActionForward execute(final ActionMapping mapping,
    final ActionForm actionForm, final HttpServletRequest request,
    final HttpServletResponse response) throws Exception {


    try {
      ActionForward forward = super.execute(mapping, actionForm, request, response);
  
      return forward;
    } catch(Exception e) {
      CalculatorForm form = (CalculatorForm) actionForm;
      StringBuffer sb = new StringBuffer();
      sb.append("Error occurred performing action for PIN: ");
      sb.append(form.getPin());
      sb.append(" Program Year: ");
      sb.append(form.getYear());
      sb.append(" Scenario Number: ");
      sb.append(form.getScenarioNumber());
      logger.error(sb.toString());
      throw e;
    }
  }
  
  
  /**
   * @param form form
   * @return Scenario
   * @throws Exception On Exception
   */
  protected Scenario getScenario(final CalculatorForm form)
  throws Exception {
    logger.debug("getScenario() - pin=" + form.getPin());
    
    if(form.getYear() == 0) {
      CalculatorInboxContext inboxContext =
        (CalculatorInboxContext) CacheFactory.getUserCache().getItem(CacheKeys.CALCULATOR_INBOX_CONTEXT);

      int year;
      if(inboxContext != null && inboxContext.getInboxYear() != 0) {
        year = inboxContext.getInboxYear();
      } else {
        year = ProgramYearUtils.getCurrentProgramYear().intValue();
      }
  
      form.setYear(year);
    }
    
    Scenario scenario = null;
    
    if(!form.isRefresh()) {
      scenario = form.getScenario();
    }
    
    if(scenario == null) {
      scenario = refreshScenario(form);
      if(form.isRefresh()) {
        form.setRefresh(false);
      }
    } else {
      Integer revisionCount = calculatorService.getScenarioRevisionCount(scenario.getScenarioId());
      boolean cacheOutOfDate = false;
      if(scenario.getRevisionCount() != null) {
        cacheOutOfDate = ! scenario.getRevisionCount().equals(revisionCount);
      }
      if(cacheOutOfDate) {
        scenario = refreshScenario(form);
        
      }
    }
    
    return scenario;
  }

  
  /**
   * @param form CalculatorForm
   * @return Scenario
   * @throws ServiceException On Exception
   */
  protected Scenario refreshScenario(final CalculatorForm form)
  throws ServiceException {
    Scenario scenario;

    logger.debug("getting scenario object for pin=" + form.getPin() + ", year=" + form.getYear() + ", scenarioNumber=" + form.getScenarioNumber());
    scenario = calculatorService.loadScenario(form.getPin(), form.getYear(), form.getScenarioNumber());

    if (scenario == null) {
      logger.error("scenario read returned null");
      throw new ServiceException("scenario read returned null");
    }

    boolean isUserScenario = ScenarioTypeCodes.USER.equals(scenario.getScenarioTypeCode());

    boolean refScenariosCreated = false;
    if (isUserScenario && isAssignedToCurrentUser(scenario) && scenario.isInProgress()) {

      List<ReferenceScenario> referenceScenarios;
      if (scenario.isInCombinedFarm()) {
        referenceScenarios = scenario.getCombinedFarm().getAllReferenceScenarios();
      } else {
        referenceScenarios = scenario.getReferenceScenarios();
      }

      for (ReferenceScenario refScenario : referenceScenarios) {
        if (!refScenario.getScenarioTypeCode().equals(ScenarioTypeCodes.REF)) {
          calculatorService.createReferenceScenario(scenario, refScenario.getScenarioId(), scenario.getScenarioCategoryCode(), getUserId());
          refScenariosCreated = true;
        }
      }
    }

    if (refScenariosCreated) {
      scenario = calculatorService.loadScenario(form.getPin(), form.getYear(), form.getScenarioNumber());
    }

    logger.debug("adding scenario object to cache");
    CacheFactory.getUserCache().addItem(CacheKeys.getScenarioCacheKey(form.getPin(), form.getYear(), scenario.getScenarioNumber()), scenario);

    if (form.getScenarioNumber() == null) {
      form.setScenarioNumber(scenario.getScenarioNumber());
    }

    return scenario;
  }

  /**
   * @param request request
   */
  protected void handleInvalidRevisionCount(final HttpServletRequest request) {
    ActionMessages errorMessages = new ActionMessages();
    errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_SCENARIO_INVALID_REVISION_COUNT));
    saveErrors(request, errorMessages);
  }

  /**
   * @param form form
   * @param scenario scenario
   */
  protected void populateFarmViewOptions(
      CalculatorForm form,
      Scenario scenario) {
    populateFarmViewOptions(form, scenario, true);
  }

  /**
   * @param form form
   * @param scenario scenario
   * @param includeWholeFarmDisplay includeWholeFarmDisplay
   */
  protected void populateFarmViewOptions(
      CalculatorForm form,
      Scenario scenario,
      boolean includeWholeFarmDisplay) {
    List<ListView> options = new ArrayList<>();

    if(includeWholeFarmDisplay) {
      boolean isCombinedFarm = scenario.isInCombinedFarm();
      
      String wholeFarmDisplay;
      if(isCombinedFarm) {
        wholeFarmDisplay = FARM_VIEW_COMBINED_FARM_DISPLAY;
      } else {
        wholeFarmDisplay = FARM_VIEW_WHOLE_FARM_DISPLAY;
      }
      ListView item = new CodeListView(FARM_VIEW_WHOLE_FARM_CODE, wholeFarmDisplay);
      options.add(item);
    }
    
    Set<String> schedules = getFarmingOperationSchedules(scenario);

    for(String schedule : schedules) {
      ListView item = new CodeListView(schedule, "Operation " + schedule);
      options.add(item);
    }
    
    form.setFarmViewOptions(options);
  }


  /**
   * @param scenario scenario
   * @return Set<String schedule>
   */
  protected Set<String> getFarmingOperationSchedules(Scenario scenario) {
    Set<String> schedules = new TreeSet<>();
    for(ReferenceScenario refScenario : scenario.getAllScenarios()) {
      if(refScenario.getFarmingYear() != null
          && refScenario.getFarmingYear().getFarmingOperations() != null) {
        for(FarmingOperation fo : refScenario.getFarmingYear().getFarmingOperations()) {
          String schedule = fo.getSchedule();
          schedules.add(schedule);
        }
      }
    }
    return schedules;
  }

  /**
   * @param schedule String
   * @param scenario Scenario
   * @return Map<String year, FarmingOperation>
   * @throws Exception On Exception
   */
  protected Map<String, FarmingOperation> getYearOpMap(String schedule, Scenario scenario)
  throws Exception {

    Map<String, FarmingOperation> yearOpMap = new HashMap<>();
    
    for(ReferenceScenario refScenario : scenario.getAllScenarios()) {
      FarmingOperation fo = refScenario.getFarmingYear().getFarmingOperationBySchedule(schedule);
      if(fo != null) {
        yearOpMap.put(fo.getFarmingYear().getReferenceScenario().getYear().toString(), fo);
      }
    }


    return yearOpMap;
  }
  
  
  /**
   * @param form form
   * @param scenario scenario
   */
  protected void populateRequiredYears(CalculatorForm form, Scenario scenario) {
    Integer programYear = scenario.getYear();
    List<Integer> requiredYearInts = getRequiredYears(scenario);
    List<String> requiredYearStrings = new ArrayList<>(requiredYearInts.size());
    List<String> referenceYearStrings = new ArrayList<>(requiredYearInts.size() - 1);
    for(Integer year : requiredYearInts) {
      requiredYearStrings.add(year.toString());
      if(!year.equals(programYear)) {
        referenceYearStrings.add(year.toString());
      }
    }
    form.setRequiredYears(requiredYearStrings);
    form.setReferenceYears(referenceYearStrings);
  }


  protected List<Integer> getRequiredYears(Scenario scenario) {
    return ScenarioUtils.getAllYears(scenario.getYear());
  }

  protected List<Integer> getReferenceYears(Scenario scenario) {
    return ScenarioUtils.getReferenceYears(scenario.getYear());
  }
  
  protected void setReadOnlyFlag(
      HttpServletRequest request,
      CalculatorForm form,
      Scenario scenario) throws Exception {
    form.setReadOnly(isReadOnly(request, form, scenario));
    form.setCanModifyScenario(canModifyScenario(request, form, scenario));
  }

  protected boolean isReadOnly(
      HttpServletRequest request,
      CalculatorForm form,
      Scenario scenario) throws Exception {

    boolean result = !canModifyScenario(request, form, scenario);
    
    return result;
  }


  protected boolean canModifyScenario(
      HttpServletRequest request,
      @SuppressWarnings("unused") CalculatorForm form,
      Scenario scenario) throws Exception {
    boolean result;

    boolean authorizedAction = canPerformAction(request, SecurityActionConstants.EDIT_SCENARIO);
    boolean userScenario = ScenarioTypeCodes.USER.equals(scenario.getScenarioTypeCode());
    boolean assignedTo = isAssignedToCurrentUser(scenario);
    boolean inProgress = IN_PROGRESS.equals(scenario.getScenarioStateCode());
    boolean categoryUnknown = UNKNOWN.equals(scenario.getScenarioCategoryCode());

    if(authorizedAction && userScenario && assignedTo && inProgress && !categoryUnknown) {
      result = true;
    } else {
      result = false;
    }
    return result;
  }
  
  
  /**
   * Call during a save action.
   * If a user somehow manages to save on a readonly scenario,
   * then throw a SecurityException
   * @param request request
   * @param form form
   * @param scenario scenario
   * @throws Exception On Exception
   */
  protected void checkReadOnly(
      HttpServletRequest request,
      CalculatorForm form,
      Scenario scenario) throws Exception {

    if (isReadOnly(request, form, scenario)) {
      logger.error("Edit functions are not permitted in read only mode.");
      throw new SecurityException(
        "Edit functions are not permitted in read only mode.");
    }

  }

  /**
   * @param scenario scenario
   * @return boolean
   */
  protected boolean isAssignedToCurrentUser(Scenario scenario) {
    
    return calculatorService.isAssignedToCurrentUser(scenario);
  }

  /**
   * @param form
   *          form
   * @param scenario
   *          Scenario
   */
  protected void populateYearScenarioMap(CalculatorForm form, Scenario scenario) {
    form.setYearScenarioMap(getYearScenarioMap(scenario));
  }

  /**
   * @param scenario scenario
   * @return Map<ReferenceScenario>
   */
  protected Map<Integer, ReferenceScenario> getYearScenarioMap(Scenario scenario) {
    Map<Integer, ReferenceScenario> yearScenarioMap = new HashMap<>();
    yearScenarioMap.put(scenario.getYear(), scenario);

    for (ReferenceScenario refScenario : scenario.getReferenceScenarios()) {
      yearScenarioMap.put(refScenario.getYear(), refScenario);
    }
    return yearScenarioMap;
  }


  /**
   * @param form form
   * @param scenario scenario
   * @param request request
   * @return Scenario
   * @throws Exception On Exception
   */
  protected Scenario checkOut(CalculatorForm form, Scenario scenario, HttpServletRequest request)
  throws Exception {
    Scenario resultScenario = null;

    if(scenario.getRevisionCount().equals(form.getScenarioRevisionCount())) {

      boolean createUserScenario = !ScenarioUtils.hasUserScenario(scenario);
      boolean createFromCra = false;
      try {
        if(createUserScenario) {
          ScenarioMetaData latest = ScenarioUtils.getLatestBaseScenario(scenario, scenario.getYear());
          Integer scenarioId = latest.getScenarioId();
          int programYear = form.getYear();
          logger.debug("Creating new user scenario...");
          Integer newScenarioNumber = calculatorService.saveScenarioAsNew(scenarioId,
              ScenarioTypeCodes.USER, UNKNOWN, programYear, getUserId());
          form.setScenarioNumber(newScenarioNumber);
          if(latest.getScenarioTypeCode().equals(ScenarioTypeCodes.CRA)) {
            createFromCra = true;
          }
          resultScenario = refreshScenario(form);
        } else {
          resultScenario = scenario;
        }
        
        calculatorService.assignToUser(
            resultScenario,
            CurrentUser.getUser().getGuid(),
            getUserId());
        
        if(createFromCra) {
          AdjustmentService adjService = ServiceFactory.getAdjustmentService();
          adjService.makeInventoryValuationAdjustments(resultScenario);
        }

        resultScenario = refreshScenario(form);

        if(createUserScenario) {
          BenefitService benefitService = ServiceFactory.getBenefitService();
          // ignore error messages returned
          benefitService.calculateBenefit(resultScenario, getUserId());
          resultScenario = refreshScenario(form);
        }
      } catch(InvalidRevisionCountException irce) {
        handleInvalidRevisionCount(request);
      }
    } else {
      handleInvalidRevisionCount(request);
      resultScenario = scenario;
    }

    return resultScenario;
  }

  
  /**
   * @param form form
   */
  protected void syncFarmViewCacheWithForm(CalculatorForm form) {
  	String defaultFarmView = CalculatorAction.FARM_VIEW_WHOLE_FARM_CODE;
    String farmViewCacheKey =
      CacheKeys.getFarmViewCacheKey(form.getPin(), form.getYear(), form.getScenarioNumber());
    if(form.getFarmView() != null) {
      CacheFactory.getUserCache().addItem(farmViewCacheKey, form.getFarmView());
    } else {
      String farmView = (String) CacheFactory.getUserCache().getItem(farmViewCacheKey);
      if(farmView == null) {
        farmView = defaultFarmView;
        CacheFactory.getUserCache().addItem(farmViewCacheKey, farmView);
      }
      form.setFarmView(farmView);
    }
  }

  /**
   * @param form form
   */
  protected void syncInboxContextWithForm(CalculatorForm form) {
    CalculatorInboxContext inboxContext =
      (CalculatorInboxContext) CacheFactory.getUserCache().getItem(CacheKeys.CALCULATOR_INBOX_CONTEXT);

    if(inboxContext == null) {
      // if inbox context does not exist, create it and set the defaults
      inboxContext = new CalculatorInboxContext();
      CacheFactory.getUserCache().addItem(CacheKeys.CALCULATOR_INBOX_CONTEXT, inboxContext);
    }
    
    if(form.getInboxYear() == 0) {
      // if year is not set assume no inbox form fields are set and set them from the inbox context
      
      if(inboxContext.getInboxYear() == 0) {
        // if inbox context fields are not set, then set the defaults
        inboxContext.setInboxYear(ProgramYearUtils.getCurrentProgramYear().intValue());
        inboxContext.setInboxSearchType(ListServiceConstants.CALCULATOR_INBOX_SEARCH_TYPE_USER);
        inboxContext.setInProgressCB(true);
        inboxContext.setVerifiedCB(false);
        inboxContext.setClosedCB(false);
      }
      form.setInboxYear(inboxContext.getInboxYear());
      form.setInboxSearchType(inboxContext.getInboxSearchType());
      form.setInProgressCB(inboxContext.isInProgressCB());
      form.setVerifiedCB(inboxContext.isVerifiedCB());
      form.setClosedCB(inboxContext.isClosedCB());
    } else {
      // if year is set assume inbox form fields are set and update the inbox context from the form
      inboxContext.setInboxYear(form.getInboxYear());
      inboxContext.setInboxSearchType(form.getInboxSearchType());
      inboxContext.setInProgressCB(form.isInProgressCB());
      inboxContext.setVerifiedCB(form.isVerifiedCB());
      inboxContext.setClosedCB(form.isClosedCB());
    }
  }
  
  
  /**
	 * @param form form
	 * @param scenario scenario
	 * @param message message
	 * @throws ServiceException ServiceException
	 */
	protected void addScenarioLog(CalculatorForm form,
	    Scenario scenario,
	    String message) throws ServiceException {

	  addScenarioLog(form, scenario, message, true);
	}
	
	
	/**
	 * @param form form
	 * @param scenario scenario
	 * @param message message
	 * @param updateRevisionCount updateRevisionCount
	 * @throws ServiceException ServiceException
	 */
	protected void addScenarioLog(CalculatorForm form,
	    Scenario scenario,
	    String message,
	    boolean updateRevisionCount) throws ServiceException {

	  calculatorService.addScenarioLog(scenario, message, getUserId());
	  if(updateRevisionCount) {
  	  calculatorService.updateScenarioRevisionCount(scenario, getUserId());
  	  form.setScenarioRevisionCount(scenario.getRevisionCount());
	  }
	}

	
  /**
   * format the input string to ensure it has only two decimal places
   * @param currencyString currencyString
   * 
   */
  protected String formatCurrency(String currencyString) {
    String output = new String("");

    if (currencyString != null && !currencyString.equals("")) {
      BigDecimal currency = new BigDecimal(currencyString);
      output = currencyOutputFormat.format(currency);
    }
    return output;
  }

	
	
}
