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
package ca.bc.gov.srm.farm.ui.struts.calculator.productiveunits;

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
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.AdjustmentService;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.ObjectUtils;

public class ProductiveUnitsImportAction extends ProductiveUnitsViewAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());


  @Override
  protected ActionForward doExecute(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Importing Productive Units...");

    String userId = CurrentUser.getUser().getUserId();
    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);

    ProductiveUnitsForm form = (ProductiveUnitsForm) actionForm;
    Scenario scenario = getScenario(form);
    
    ActionMessages errors = new ActionMessages();
    ActionMessages messages = new ActionMessages();

    String participantDataSrcCode = form.getViewDataSetRadio();

    Integer sourceScenarioNumber = form.getImportScenarioNumber();
    String sourceOperationSchedule = form.getImportOperationSchedule();
    
    if (sourceScenarioNumber != null && sourceOperationSchedule != null) {

      Scenario sourceScenario = getSourceScenario(form.getPin(), form.getYear(), sourceScenarioNumber);
      
      FarmingOperation sourceOperation = sourceScenario.getFarmingYear().getFarmingOperationBySchedule(sourceOperationSchedule);
      
      List<ProductiveUnitCapacity> sourcePucs = getNonZeroPucs(sourceOperation.getProductiveUnitCapacities(participantDataSrcCode));
      
      Set<String> sourcePucCodes = getPucCodeSet(sourcePucs);
      logger.debug("Productive Unit Codes to be imported: " + sourcePucCodes);

      FarmingOperation targetOperation = scenario.getFarmingYear().getFarmingOperationBySchedule(sourceOperationSchedule);
      List<ProductiveUnitCapacity> targetPucs = getNonZeroPucs(targetOperation.getProductiveUnitCapacities(participantDataSrcCode));
      
      Set<String> targetPucCodes = getPucCodeSet(targetPucs);
      
      Set<String> intersectionPucCodes = new HashSet<>(sourcePucCodes);
      intersectionPucCodes.retainAll(targetPucCodes);
      
      if( ! intersectionPucCodes.isEmpty() ) {
        for (Iterator<ProductiveUnitCapacity> pucIter = sourcePucs.iterator(); pucIter.hasNext(); ) {
          ProductiveUnitCapacity puc = pucIter.next();
          String pucCode = ObjectUtils.ifNull(puc.getStructureGroupCode(), puc.getInventoryItemCode());
          if(intersectionPucCodes.contains(pucCode)) {
            pucIter.remove();
          }
        }
        
        Set<String> pucCodesImported = getPucCodeSet(sourcePucs);
        
        addMessageWithCodeListIfNotEmpty(messages, pucCodesImported, MessageConstants.INFO_PUC_IMPORT_PUCS_IMPORTED);
        addMessageWithCodeListIfNotEmpty(messages, intersectionPucCodes, MessageConstants.INFO_PUC_IMPORT_PUCS_ALREADY_EXIST);
      }

      if(!messages.isEmpty()) {
        request.setAttribute("productiveUnitMessages", messages);
      }
      
      if(sourcePucs.isEmpty()) {
        errors.add("", new ActionMessage(MessageConstants.ERRORS_NO_PUCS_COULD_BE_IMPORTED));
      }
      
      saveErrors(request, errors);

      if( errors.isEmpty() ) {
        AdjustmentService adjService = ServiceFactory.getAdjustmentService();
        adjService.copyProductiveUnitCapacities(scenario, targetOperation, sourcePucs, participantDataSrcCode, userId);
      }

    } else {
      logger.error("Missing importScenarioNumber, unable to import productive units");
      throw new ServiceException("Missing importScenarioNumber, unable to import productive units");
    }
    
    scenario = refreshScenario(form);

    populateForm(form, scenario, true);

    setReadOnlyFlag(request, form, scenario);

    return forward;
  }

  private void addMessageWithCodeListIfNotEmpty(ActionMessages messages, Set<String> pucCodesImported, String messageKey) {
    if( ! pucCodesImported.isEmpty() ) {
      Object[] pucCodesImportedMsgValues = {getSortedCodeListString(pucCodesImported)};
      messages.add("", new ActionMessage(messageKey, pucCodesImportedMsgValues));
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

  private Set<String> getPucCodeSet(List<ProductiveUnitCapacity> pucs) {
    return pucs.stream()
        .map(p -> ObjectUtils.ifNull(p.getStructureGroupCode(), p.getInventoryItemCode()))
        .collect(Collectors.toSet());
  }

  private List<ProductiveUnitCapacity> getNonZeroPucs(List<ProductiveUnitCapacity> productiveUnitCapacities) {
    List<ProductiveUnitCapacity> nonZeroPucs = productiveUnitCapacities;
    nonZeroPucs = nonZeroPucs.stream()
        .filter(p -> p.getTotalProductiveCapacityAmount() != 0)
        .collect(Collectors.toList());
    return nonZeroPucs;
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
