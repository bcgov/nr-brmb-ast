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
package ca.bc.gov.srm.farm.ui.struts.calculator.inventory;

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

import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.InventoryItem;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.service.AdjustmentService;
import ca.bc.gov.srm.farm.service.BenefitService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.InventoryAdjustmentUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

/**
 * @author awilkinson
 * @created Jan 27, 2011
 */
public class InventorySaveAction extends InventoryViewAction {
  
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Saving Inventory...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    InventoryForm form = (InventoryForm) actionForm;
    Scenario scenario = getScenario(form);
    
    checkReadOnly(request, form, scenario);

    populateForm(form, scenario, false);

    if(scenario.getRevisionCount().equals(form.getScenarioRevisionCount())) {
      ActionMessages errors = new ActionMessages();
      ActionMessages messages = new ActionMessages();
      
      String year = form.getFarmViewYear();
      String schedule = form.getFarmView();
      Map<String, FarmingOperation> yearOpMap = getYearOpMap(schedule, scenario);
      FarmingOperation farmingOperation = yearOpMap.get(year);
  
      Map<String, List<InventoryItem>> adjustmentsMap = InventoryAdjustmentUtils.parseAdjustments(form, farmingOperation, errors);
      
      checkLocalSupplementalReceivedDate(scenario, errors, adjustmentsMap, form);

      saveErrors(request, errors);
  
      if(errors.isEmpty()) {
        
        InventoryAdjustmentUtils.addCopyForwardAdjustments(farmingOperation, adjustmentsMap, messages);
  
        try {
          AdjustmentService adjService = ServiceFactory.getAdjustmentService();
          
          adjService.adjustInventoriesAndAccruals(scenario, adjustmentsMap, getUserId());
    
          scenario = refreshScenario(form);
          
          BenefitService benefitService = ServiceFactory.getBenefitService();
          // ignore error messages returned
          benefitService.calculateBenefit(scenario, getUserId());

          scenario = refreshScenario(form);
          
          populateForm(form, scenario, true);
          form.setAddedNew(false);

          if(!messages.isEmpty()) {
            request.setAttribute("inventoryMessages", messages);
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
    
    ActionMessages warnings = getDuplicateWarnings(scenario);
    warnings.add(getMissingFmvWarnings(scenario));
    warnings.add(getNonMarketCommodityMismatchedPricesWarnings(scenario, form));
    if(!warnings.isEmpty()) {
      saveMessages(request, warnings);
    }

    setReadOnlyFlag(request, form, scenario);

    return forward;
  }

  
  private void checkLocalSupplementalReceivedDate(Scenario scenario, ActionMessages errors,
      Map<String, List<InventoryItem>> adjustmentsMap, InventoryForm form) {
    
    if(isMissingSupplementalDates(scenario, form)) {
      boolean hasSupplemental = hasSupplemental(adjustmentsMap);
      
      if(!hasSupplemental) {
        hasSupplemental = ScenarioUtils.hasProgramYearSupplemental(scenario);
      }
      
      if(hasSupplemental) {
        errors.add("", new ActionMessage(MessageConstants.ERRORS_PROVINCIAL_SUPPLEMENTAL_RECEIVED_DATE_ADJUSTMENT_SCREENS));
      }
    }
    
  }

  private boolean hasSupplemental(Map<String, List<InventoryItem>> adjustmentsMap) {
    for(String action : adjustmentsMap.keySet()) {
      if(action.equals(AdjustmentService.ACTION_ADD) || action.equals(AdjustmentService.ACTION_UPDATE)) {

        for(InventoryItem item : adjustmentsMap.get(action)) {
          if(ScenarioUtils.hasSupplemental(item, false)) {
            return true;
          }
        }

      }
    }
    return false;
  }

}
