/**
 *
 * Copyright (c) 2024,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.accruals;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.InventoryItem;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.AdjustmentService;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.AccrualItemUtils;

public class AccrualsImportAction extends AccrualsViewAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Importing Accrual Items...");

    String userId = CurrentUser.getUser().getUserId();
    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);

    AccrualsForm form = (AccrualsForm) actionForm;
    Scenario scenario = getScenario(form);
    
    ActionMessages errors = new ActionMessages();
    ActionMessages messages = new ActionMessages();

    Integer sourceScenarioNumber = form.getImportScenarioNumber();
    String sourceOperationSchedule = form.getImportOperationSchedule();
    
    if (sourceScenarioNumber != null && sourceOperationSchedule != null) {

      Scenario sourceScenario = getSourceScenario(form.getPin(), form.getYear(), sourceScenarioNumber);
      
      FarmingOperation sourceOperation = sourceScenario.getFarmingYear().getFarmingOperationBySchedule(sourceOperationSchedule);
      
      List<InventoryItem> sourceItems = getNonZeroItems(sourceOperation.getAccrualItems());
      
      Set<String> sourceInventoryCodes = getInventoryCodeSet(sourceItems);
      logger.debug("Accrual Codes to be imported: " + sourceInventoryCodes);

      FarmingOperation targetOperation = scenario.getFarmingYear().getFarmingOperationBySchedule(sourceOperationSchedule);
      List<InventoryItem> targetItems = getNonZeroItems(targetOperation.getAccrualItems());
      
      Set<String> targetInventoryCodes = getInventoryCodeSet(targetItems);
      
      Set<String> intersectionInventoryCodes = new HashSet<>(sourceInventoryCodes);
      intersectionInventoryCodes.retainAll(targetInventoryCodes);
      
      if( ! intersectionInventoryCodes.isEmpty() ) {
        for (Iterator<InventoryItem> itemIter = sourceItems.iterator(); itemIter.hasNext(); ) {
          InventoryItem item = itemIter.next();
          String inventoryCode = item.getInventoryItemCode();
          if(intersectionInventoryCodes.contains(inventoryCode)) {
            itemIter.remove();
          }
        }
        
        Set<String> inventoryCodesImported = getInventoryCodeSet(sourceItems);
        
        addMessageWithCodeListIfNotEmpty(messages, inventoryCodesImported, MessageConstants.INFO_ACCRUAL_IMPORT_ITEMS_IMPORTED);
        addMessageWithCodeListIfNotEmpty(messages, intersectionInventoryCodes, MessageConstants.INFO_ACCRUAL_IMPORT_ITEMS_ALREADY_EXIST);
      }

      if(!messages.isEmpty()) {
        request.setAttribute("accrualMessages", messages);
      }
      
      if(sourceItems.isEmpty()) {
        errors.add("", new ActionMessage(MessageConstants.ERRORS_ACCRUAL_NO_ITEMS_COULD_BE_IMPORTED));
      }
      
      saveErrors(request, errors);

      if( errors.isEmpty() ) {
        List<InventoryItem> sourceInventoryItems = new ArrayList<>(sourceItems);
        
        AdjustmentService adjService = ServiceFactory.getAdjustmentService();
        adjService.copyInventoriesAndAccruals(scenario, targetOperation, sourceInventoryItems, userId);
      }

    } else {
      logger.error("Missing importScenarioNumber, unable to import accruals");
      throw new ServiceException("Missing importScenarioNumber, unable to import accruals");
    }
    
    scenario = refreshScenario(form);

    populateForm(form, scenario, true);

    setReadOnlyFlag(request, form, scenario);

    return forward;
  }

  private void addMessageWithCodeListIfNotEmpty(ActionMessages messages, Set<String> codeSet, String messageKey) {
    if( ! codeSet.isEmpty() ) {
      Object[] codesMsgValues = {getSortedCodeListString(codeSet)};
      messages.add("", new ActionMessage(messageKey, codesMsgValues));
    }
  }

  private String getSortedCodeListString(Set<String> codeSet) {
    List<String> codeList = new ArrayList<>(codeSet);
    codeList.sort(new Comparator<String>() {

      @Override
      public int compare(String c1, String c2) {
        return Integer.valueOf(c1).compareTo(Integer.valueOf(c2));
      }
    });
    return String.join(", ", codeList);
  }

  private Set<String> getInventoryCodeSet(List<InventoryItem> items) {
    return items.stream()
        .map(i -> i.getInventoryItemCode())
        .collect(Collectors.toSet());
  }

  private List<InventoryItem> getNonZeroItems(List<InventoryItem> accrualItems) {
    List<InventoryItem> nonZeroItems = accrualItems;
    nonZeroItems = nonZeroItems.stream()
        .filter(i -> AccrualItemUtils.hasNonZeroAmounts(i))
        .collect(Collectors.toList());
    return nonZeroItems;
  }

  private Scenario getSourceScenario(int participantPin, int programYear, int scenarioNumber) throws ServiceException {
    Scenario scenario;

    CalculatorService calculatorService = ServiceFactory.getCalculatorService();
    scenario = calculatorService.loadScenario(participantPin, programYear, scenarioNumber);

    if (scenario == null) {
      logger.error("scenario read returned null");
      throw new ServiceException("scenario read returned null");
    }

    return scenario;
  }

}
