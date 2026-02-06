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
package ca.bc.gov.srm.farm.ui.struts.calculator.productiveunits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.service.AdjustmentService;
import ca.bc.gov.srm.farm.service.BenefitService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.DataParseUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * @author awilkinson
 * @created Dec 21, 2010
 */
public class ProductiveUnitsSaveAction extends ProductiveUnitsViewAction {
  
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

    logger.debug("Saving Productive Units...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    ProductiveUnitsForm form = (ProductiveUnitsForm) actionForm;
    Scenario scenario = getScenario(form);
    
    checkReadOnly(request, form, scenario);

    populateForm(form, scenario, false);

    final double maxValue = 99999999999d;
    final double minValue = -maxValue;

    if(scenario.getRevisionCount().equals(form.getScenarioRevisionCount())) {
      ActionMessages errors = new ActionMessages();
      checkForDuplicates(form, errors);
      
      String schedule = form.getFarmView();
      Map<String, FarmingOperation> yearOpMap = getYearOpMap(schedule, scenario);
  
      Map<String, List<ProductiveUnitCapacity>> adjustmentsMap = parseAdjustments(form, yearOpMap, minValue, maxValue, errors);
      
      checkLocalSupplementalReceivedDate(scenario, errors, adjustmentsMap);

      saveErrors(request, errors);
      
      if(errors.isEmpty()) {

        try {
          AdjustmentService adjService = ServiceFactory.getAdjustmentService();

          adjService.adjustProductiveUnitCapacities(scenario, adjustmentsMap, getUserId());

          scenario = refreshScenario(form);
          
          BenefitService benefitService = ServiceFactory.getBenefitService();
          // ignore error messages returned
          benefitService.calculateBenefit(scenario, getUserId());

          scenario = refreshScenario(form);
  
          populateForm(form, scenario, true);
          form.setAddedNew(false);
          
        } catch(InvalidRevisionCountException irce) {
          handleInvalidRevisionCount(request);
          forward = mapping.findForward(ActionConstants.FAILURE);
        }
      }
    } else {
      handleInvalidRevisionCount(request);
      forward = mapping.findForward(ActionConstants.FAILURE);
    }

    setReadOnlyFlag(request, form, scenario);

    return forward;
  }


  /**
   * @param form form
   * @param errors ActionMessages
   */
  private void checkForDuplicates(ProductiveUnitsForm form, ActionMessages errors) {

    String sgErrorKey = MessageConstants.ERRORS_DUPLICATE_STRUCTURE_GROUP_CODE;
    String invErrorKey = MessageConstants.ERRORS_DUPLICATE_INVENTORY_ITEM_CODE;
    Set<String> structureGroupCodes = new HashSet<>();
    Set<String> inventoryItemCodes = new HashSet<>();

    for (ProductiveUnitFormLine item : form.getItems().values()) {
      String lineCode = item.getLineCode();
      if (item.getType().equals(ProductiveUnitFormLine.TYPE_STRUCTURE_GROUP)) {
        if (structureGroupCodes.contains(lineCode)) {
          errors.add("", new ActionMessage(sgErrorKey, lineCode));
        } else {
          structureGroupCodes.add(lineCode);
        }
      } else if (item.getType().equals(ProductiveUnitFormLine.TYPE_INVENTORY_ITEM)) {
        if (inventoryItemCodes.contains(lineCode)) {
          errors.add("", new ActionMessage(invErrorKey, lineCode));
        } else {
          inventoryItemCodes.add(lineCode);
        }
      }
    }
  }


  /**
   * This method is an exact duplicate of that in IncomeExpensesSaveAction.
   * Refactoring to share this method was going to be messy and not worth the reward.
   * If a change is made to this method. It likely should be made in both classes.
   * @param form ProductiveUnitsForm
   * @param yearOpMap Map
   * @param minValue double
   * @param maxValue double
   * @param errors ActionMessages
   * @return Map<String action, List<ProductiveUnitCapacity>>
   * @throws Exception On Exception
   */
  public Map<String, List<ProductiveUnitCapacity>> parseAdjustments(
      ProductiveUnitsForm form,
      Map<String, FarmingOperation> yearOpMap,
      double minValue,
      double maxValue,
      ActionMessages errors)
  throws Exception {

    Map<String, List<ProductiveUnitCapacity>> adjustmentsMap = new HashMap<>();
    
    String errorKey = MessageConstants.ERRORS_ADJUSTED_PUC_VALUE;
    boolean blankLineCode = false;
    
    for(ProductiveUnitFormLine item : form.getItems().values()) {
      for(ProductiveUnitFormRecord record : item.getRecords().values()) {
        for(String year : record.getAdjustedValues().keySet()) {
          FarmingOperation farmingOperation = yearOpMap.get(year);
  
          String totalString = record.getAdjusted(year);
          boolean badValue = false;
          Double total = null;
          if(!StringUtils.isBlank(totalString)) {
            try {
              total = Double.valueOf(totalString);
              if(total < minValue || total > maxValue) {
                 badValue = true;
              }
            } catch(NumberFormatException nfe) {
              badValue = true;
            }
            if(StringUtils.isBlank(item.getLineCode())) {
              blankLineCode = true;
            }
          } else if(record.getReportedId(year) != null
              || (record.getAdjustmentId(year) != null && !record.isAdjustmentDeleted(year))) {
            badValue = true;
          }
          
          if(badValue) {
            errors.add("", new ActionMessage(errorKey, year, item.getLineCode()));
            item.setError(year, Boolean.TRUE);
          } else if( (! StringUtils.isBlank(totalString) || record.isAdjustmentDeleted(year))
              && ! ProductiveUnitFormLine.TYPE_NEW.equals(item.getType()) ) {
  
            Double reported = DataParseUtils.parseDoubleObject(record.getCra(year));
            Double prevAdjustment = DataParseUtils.parseDoubleObject(record.getAdjustment(year));
            Double adjustment = null;
            if( ! StringUtils.isBlank(totalString) ) {
              Objects.requireNonNull(total); // Prevent "may be null" Java warning
              if(reported == null) {
                adjustment = total;
              } else {
                adjustment = total - reported;
              }
              if(adjustment.equals(prevAdjustment)) {
                adjustment = null;
              }
            }
            
            String action = null;
            if(item.isNew()
                || (prevAdjustment == null && reported == null)
                || (prevAdjustment == null && reported != null && !reported.equals(total))) {
              action = AdjustmentService.ACTION_ADD;
            } else if(record.isAdjustmentDeleted(year)
                && (StringUtils.isBlank(totalString)
                    || (reported != null && reported.equals(total)))) {
              action = AdjustmentService.ACTION_DELETE;
            } else if(prevAdjustment != null && adjustment != null) {
              action = AdjustmentService.ACTION_UPDATE;
            }
            
            if(action != null) {
              addAdjustment(adjustmentsMap, item, record, year, adjustment, action, farmingOperation);
            }
          }
        }
      }
    }
    
    if(blankLineCode) {
      errors.add("", new ActionMessage(MessageConstants.ERRORS_LINE_CODE_BLANK));
    }

    return adjustmentsMap;
  }


  /**
   * @param adjustmentsMap Map
   * @param item ProductiveUnitFormLine
   * @param record 
   * @param year String
   * @param adjustmentValue Double
   * @param action String
   * @param farmingOperation FarmingOperation
   */
  private void addAdjustment(
      Map<String, List<ProductiveUnitCapacity>> adjustmentsMap,
      ProductiveUnitFormLine item,
      ProductiveUnitFormRecord record,
      String year,
      Double adjustmentValue,
      String action,
      FarmingOperation farmingOperation) {
    ProductiveUnitCapacity puc = new ProductiveUnitCapacity();
    puc.setFarmingOperation(farmingOperation);
    puc.setAdjAmount(adjustmentValue);

    // ignore ProductiveUnitFormLine.TYPE_NEW since the user did not enter data for those rows
    if(item.getType().equals(ProductiveUnitFormLine.TYPE_STRUCTURE_GROUP)) {
      puc.setStructureGroupCode(item.getLineCode());
    } else if(item.getType().equals(ProductiveUnitFormLine.TYPE_INVENTORY_ITEM)) {
      puc.setInventoryItemCode(item.getLineCode());
    }
    puc.setAdjProductiveUnitCapacityId(record.getAdjustmentId(year));
    puc.setReportedProductiveUnitCapacityId(record.getReportedId(year));
    puc.setRevisionCount(record.getRevisionCount(year));
    puc.setParticipantDataSrcCode(record.getParticipantDataSrcCode());

    List<ProductiveUnitCapacity> adjustmentList = adjustmentsMap.get(action);
    if(adjustmentList == null) {
      adjustmentList = new ArrayList<>();
      adjustmentsMap.put(action, adjustmentList);
    }
    adjustmentList.add(puc);
  }


  private void checkLocalSupplementalReceivedDate(Scenario scenario, ActionMessages errors,
      Map<String, List<ProductiveUnitCapacity>> adjustmentsMap) {
    
    boolean missingSupplementalDates = ScenarioUtils.isMissingSupplementalDates(scenario);
    
    if(missingSupplementalDates) {
      boolean hasSupplemental = false;
      for(String action : adjustmentsMap.keySet()) {
        if(action.equals(AdjustmentService.ACTION_ADD) || action.equals(AdjustmentService.ACTION_UPDATE)) {
          
          for(ProductiveUnitCapacity puc : adjustmentsMap.get(action)) {
            if(puc.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear().equals(scenario.getYear())) {
              hasSupplemental = puc.getTotalProductiveCapacityAmount() != null;
              if(hasSupplemental) {
                break;
              }
            }
          }
          
          if(hasSupplemental) {
            break;
          }
        }
      }
      
      if(!hasSupplemental) {
        hasSupplemental = ScenarioUtils.hasProgramYearSupplemental(scenario);
      }
      
      if(hasSupplemental) {
        errors.add("", new ActionMessage(MessageConstants.ERRORS_CHANGES_NOT_SAVED));
        errors.add("", new ActionMessage(MessageConstants.ERRORS_PROVINCIAL_SUPPLEMENTAL_RECEIVED_DATE_ADJUSTMENT_SCREENS));
      }
    }
    
  }

}
