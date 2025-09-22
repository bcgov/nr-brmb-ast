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
package ca.bc.gov.srm.farm.ui.struts.calculator.combined;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.domain.CombinedFarmClient;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.BenefitService;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.DataParseUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * @author awilkinson
 * @created Mar 2, 2011
 */
public class CombinedFarmSaveAction extends CombinedFarmViewAction {

  private Logger logger = LoggerFactory.getLogger(getClass());
  
  private static final String ACTION_ADD = "ADD";
  private static final String ACTION_REMOVE = "REMOVE";
  private static final String ACTION_UPDATE = "UPDATE";

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Saving Combined PINs...");

    String forwardAction = ActionConstants.SUCCESS;
    
    CombinedFarmForm form = (CombinedFarmForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);
    Scenario scenario = getScenario(form);
    
    checkReadOnly(request, form, scenario);
    
    if (errors != null && !errors.isEmpty()) {
      saveErrors(request, errors);
      forwardAction = ActionConstants.FAILURE;
    } else {
      
      if(scenario.getRevisionCount().equals(form.getScenarioRevisionCount())) {
        try {
          if(ACTION_ADD.equals(form.getAction())) {
            forwardAction = handleAdd(form, request, scenario);
          } else if(ACTION_REMOVE.equals(form.getAction())) {
            forwardAction = handleRemove(form, scenario);
          } else if(ACTION_UPDATE.equals(form.getAction())) {
            forwardAction = handleUpdate(form, scenario);
          }
          scenario = refreshScenario(form);

          BenefitService benefitService = ServiceFactory.getBenefitService();
          // ignore error messages returned
          benefitService.calculateBenefit(scenario, getUserId());

        } catch(InvalidRevisionCountException irce) {
          handleInvalidRevisionCount(request);
          forwardAction = ActionConstants.FAILURE;
        }
      } else {
        handleInvalidRevisionCount(request);
        forwardAction = ActionConstants.FAILURE;
      }
    }
    
    populateForm(form, request, scenario);

    setReadOnlyFlag(request, form, scenario);

    return mapping.findForward(forwardAction);
  }


  private String handleAdd(CombinedFarmForm form, HttpServletRequest request, Scenario scenario)
      throws Exception {

    String forwardAction = ActionConstants.SUCCESS;
    
    Integer pinToAdd = DataParseUtils.parseIntegerObject(form.getPinToAdd());
    
    ActionMessages errorMessages = checkAddErrors(scenario, pinToAdd);
    
    if(!errorMessages.isEmpty()) {
      saveErrors(request, errorMessages);
      forwardAction = ActionConstants.FAILURE;
      
    } else {
      Map<Integer, Integer> pinScNumUpdateMap = parsePinScNumUpdateMap(form, scenario);
      List<Integer> updatedPins = determineUpdatedPins(scenario, pinToAdd);
      CalculatorService service = ServiceFactory.getCalculatorService();
      service.addToCombinedFarm(
          scenario,
          pinToAdd,
          pinScNumUpdateMap,
          updatedPins,
          getUserId());
      
    }
    form.setPinToAdd(null);

    return forwardAction;
  }


  private ActionMessages checkAddErrors(
      Scenario scenario,
      Integer pinToAdd) throws ServiceException {
    
    ActionMessages errorMessages = new ActionMessages();
    CalculatorService service = ServiceFactory.getCalculatorService();
    
    Integer curPin = scenario.getClient().getParticipantPin();
    Integer curScenarioId = scenario.getScenarioId();
    Integer programYear = scenario.getYear();
    String municipalityCode = scenario.getFarmingYear().getMunicipalityCode();
    String scenarioCategoryCode = scenario.getScenarioCategoryCode();
    
    String errorKey = null;
    
    if(pinToAdd == null) {
      errorKey = MessageConstants.ERRORS_COMBINED_FARM_PIN_REQUIRED;
      errorMessages.add("", new ActionMessage(errorKey));
    }
    
    if(errorKey == null) {
      if(curPin.equals(pinToAdd)) {
        errorKey = MessageConstants.ERRORS_COMBINED_FARM_SAME_PIN_AS_CURRENT;
      }
    }
    
    if(errorKey == null) {
      boolean pinExists = service.pinExists(pinToAdd);
      if(!pinExists) {
        errorKey = MessageConstants.ERRORS_COMBINED_FARM_PIN_NOT_FOUND;
      }
    }
    
    if(errorKey == null) {
      boolean ipScenarioExists = service.matchingScenarioExists(pinToAdd, programYear, municipalityCode, scenarioCategoryCode);
      if(!ipScenarioExists) {
        errorKey = MessageConstants.ERRORS_COMBINED_FARM_MATCHING_SCENARIO;
      }
    }
    
    if(errorKey == null) {
      boolean checkedOut = service.pinCheckedOutByUser(pinToAdd, CurrentUser.getUser().getGuid());
      if(!checkedOut) {
        errorKey = MessageConstants.ERRORS_COMBINED_FARM_PIN_NOT_CHECKED_OUT;
      }
    }
    
    if(errorKey == null) {
      Integer combinedFarmNumber = service.getInProgressCombinedFarmNumber(pinToAdd, programYear);
      if(combinedFarmNumber != null) {
        errorKey = MessageConstants.ERRORS_COMBINED_FARM_ALREADY_COMBINED;
      }
    }
    
    if(errorKey == null) {
      boolean accountingCodeError =
          service.combinedFarmHasAccountingCodeError(pinToAdd, programYear, curScenarioId);
      if(accountingCodeError) {
        errorKey = MessageConstants.ERRORS_COMBINED_FARM_ACCOUNTING_CODE;
      }
    }
    
    if(errorKey == null) {
      boolean accountingCodeError =
          service.combinedFarmHasReferenceYearMismatchError(pinToAdd, programYear, curScenarioId);
      if(accountingCodeError) {
        errorKey = MessageConstants.ERRORS_COMBINED_FARM_REFERENCE_YEAR_SET_MISMATCH;
      }
    }
    
    if(errorKey != null) {
      Object[] messageParameters = new Object[]{StringUtils.toString(pinToAdd)};
      errorMessages.add("", new ActionMessage(errorKey, messageParameters));
    }
    
    return errorMessages;
  }
  
  
  private String handleRemove(CombinedFarmForm form, Scenario scenario)
      throws Exception {
    
    String forwardAction = ActionConstants.SUCCESS;
    CalculatorService service = ServiceFactory.getCalculatorService();
    
    Integer scenarioIdToRemove = DataParseUtils.parseIntegerObject(form.getScenarioIdToRemove());
    
    Map<Integer, Integer> pinScNumUpdateMap = parsePinScNumUpdateMap(form, scenario);
    List<Integer> updatedPins = determineUpdatedPins(scenario, null);
    service.removeFromCombinedFarm(
        scenario,
        scenarioIdToRemove,
        pinScNumUpdateMap,
        updatedPins,
        getUserId());

    return forwardAction;
  }
  
  
  private String handleUpdate(CombinedFarmForm form, Scenario scenario)
      throws Exception {
    
    String forwardAction = ActionConstants.SUCCESS;
    CalculatorService service = ServiceFactory.getCalculatorService();
    
    Map<Integer, Integer> pinScNumUpdateMap = parsePinScNumUpdateMap(form, scenario);
    List<Integer> updatedPins = determineUpdatedPins(scenario, null);

    if(!pinScNumUpdateMap.isEmpty()) {
      service.updateCombinedFarmScenarioNumbers(
          scenario,
          pinScNumUpdateMap,
          updatedPins,
          getUserId());
    }
    
    return forwardAction;
  }


  /**
   * @param form form
   * @param scenario scenario
   * @return Map
   * @throws Exception Exception
   */
  private Map<Integer, Integer> parsePinScNumUpdateMap(CombinedFarmForm form, Scenario scenario)
      throws Exception {
    Map<Integer, Integer> pinScNumUpdateMap = new HashMap<>();
    
    Integer scenarioIdToRemove = DataParseUtils.parseIntegerObject(form.getScenarioIdToRemove());
    
    Map<String, String> formScNums = form.getCombinedScenarioNumbers();
    for(String pinStr : formScNums.keySet()) {
      String scNumStr = formScNums.get(pinStr);
      Integer curPin = DataParseUtils.parseIntegerObject(pinStr);
      Integer curScNum = DataParseUtils.parseIntegerObject(scNumStr);
      pinScNumUpdateMap.put(curPin, curScNum);
    }
    
    if(scenario.getCombinedFarmClients() != null) {
      for(CombinedFarmClient client : scenario.getCombinedFarmClients()) {
        Integer curPin = client.getParticipantPin();
        Integer curScId = client.getScenarioId();
        Integer oldScNum = client.getScenarioNumber();
        Integer newScNum = pinScNumUpdateMap.get(curPin);
        
        // if the scenario has not changed or it is being removed do not update it
        if(oldScNum.equals(newScNum) || curScId.equals(scenarioIdToRemove)) {
          pinScNumUpdateMap.remove(curPin);
        }
      }
    }
    
    return pinScNumUpdateMap;
  }


  /**
   * @param scenario scenario
   * @param pinToAdd pinToAdd
   * @return List
   */
  private List<Integer> determineUpdatedPins(
      Scenario scenario,
      Integer pinToAdd) {
    
    List<Integer> updatedPins = new ArrayList<>();
    
    updatedPins.add(scenario.getClient().getParticipantPin());
    if(pinToAdd != null) {
      updatedPins.add(pinToAdd);
    }

    List<CombinedFarmClient> clients = scenario.getCombinedFarmClients();
    if(clients != null) {
      for(CombinedFarmClient client : clients) {
        Integer curPin = client.getParticipantPin();
        
        if(!updatedPins.contains(curPin)) {
          updatedPins.add(curPin);
        }
      }
    }
    
    return updatedPins;
  }

}
