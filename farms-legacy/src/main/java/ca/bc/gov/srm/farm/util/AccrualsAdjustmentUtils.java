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
package ca.bc.gov.srm.farm.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.calculator.CalculatorFactory;
import ca.bc.gov.srm.farm.calculator.InventoryCalculator;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.InputItem;
import ca.bc.gov.srm.farm.domain.InventoryItem;
import ca.bc.gov.srm.farm.domain.PayableItem;
import ca.bc.gov.srm.farm.domain.ReceivableItem;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
import ca.bc.gov.srm.farm.service.AdjustmentService;
import ca.bc.gov.srm.farm.ui.struts.calculator.accruals.AccrualFormData;
import ca.bc.gov.srm.farm.ui.struts.calculator.accruals.AccrualsForm;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;

/**
 * @author awilkinson
 * @created Apr 5, 2011
 */
public final class AccrualsAdjustmentUtils {

  /** */
  private AccrualsAdjustmentUtils() {
  }


  /**
   * @param form The form object to fill.
   * @param item InventoryItem
   * @param setAdjustedValues if false, don not clear the form and set everything but adjusted values
   * @throws Exception On Exception
   */
  public static void populateFormFromInventoryItem(
      AccrualsForm form,
      InventoryItem item,
      boolean setAdjustedValues)
  throws Exception {
    
    String lineCode = item.getInventoryItemCode();
    String lineKey = AccrualFormData.getLineKey(item);
    AccrualFormData fd = form.getItem(lineKey);
    fd.setLineCode(lineCode);
    fd.setItemType(AccrualFormData.getItemType(item.getInventoryClassCode()));
    fd.setLineCodeDescription(item.getInventoryItemCodeDescription());

    if(setAdjustedValues) {
      fd.setTotalStartOfYearAmount(StringUtils.formatTwoDecimalPlaces(item.getTotalStartOfYearAmount()));
      fd.setTotalEndOfYearAmount(StringUtils.formatTwoDecimalPlaces(item.getTotalEndOfYearAmount()));
    }

    fd.setAdjustmentId(item.getAdjInventoryId());
    fd.setAdjStartOfYearAmount(StringUtils.formatTwoDecimalPlaces(item.getAdjStartOfYearAmount()));
    fd.setAdjEndOfYearAmount(StringUtils.formatTwoDecimalPlaces(item.getAdjEndOfYearAmount()));
    fd.setAdjustedByUserId(StringUtils.formatUserIdForDisplay(item.getAdjustedByUserId()));
    fd.setEligible(item.getIsEligible().booleanValue());
    fd.setRevisionCount(item.getRevisionCount());

    fd.setReportedId(item.getReportedInventoryId());
    fd.setReportedStartOfYearAmount(
        StringUtils.formatTwoDecimalPlaces(item.getReportedStartOfYearAmount()));
    fd.setReportedEndOfYearAmount(StringUtils.formatTwoDecimalPlaces(item.getReportedEndOfYearAmount()));
    
    InventoryCalculator inventoryCalc = CalculatorFactory.getInventoryCalculator();
    Double changeInValue = new Double(inventoryCalc.calculateChangeInValue(item));
    fd.setChangeInValue(changeInValue);
    
    fd.setCommodityXrefId(item.getCommodityXrefId());
  }


  /**
   * @param form AccrualsForm
   * @param farmingOperation farmingOperation
   * @param errors ActionMessages
   * @return Map<String action, List<InventoryItem>>
   * @throws Exception On Exception
   */
  public static Map<String, List<InventoryItem>> parseAdjustments(
      AccrualsForm form,
      FarmingOperation farmingOperation,
      ActionMessages errors)
  throws Exception {

    final double maxValue = 99999999999d;
    final double minValue = -maxValue;

    Map<String, List<InventoryItem>> adjustmentsMap = new HashMap<>();
    
    String startErrorKey = MessageConstants.ERRORS_ADJUSTED_START_VALUE;
    String endErrorKey = MessageConstants.ERRORS_ADJUSTED_END_VALUE;
    boolean blankLineCodeError = false;
    
    for(AccrualFormData fd : form.getItems().values()) {
      boolean isNew = fd.isNew();
      String totalStartString = fd.getTotalStartOfYearAmount();
      String totalEndString = fd.getTotalEndOfYearAmount();
      boolean badStartValue = false;
      boolean badEndValue = false;
      Double totalStart = null;
      Double totalEnd = null;
      boolean hasLineCode = ! StringUtils.isBlank(fd.getLineCode());
      boolean hasValues =
        (!AdjustmentUtils.isBlankValue(totalStartString) 
            || !AdjustmentUtils.isBlankValue(totalEndString));
      
      try {
        totalStart = AdjustmentUtils.parseDouble(totalStartString);
        if(AdjustmentUtils.outOfRange(totalStart, minValue, maxValue)) {
          badStartValue = true;
        }
      } catch(ParseException pe) {
        badStartValue = true;
      }
      
      try {
        totalEnd = AdjustmentUtils.parseDouble(totalEndString);
        if(AdjustmentUtils.outOfRange(totalEnd, minValue, maxValue)) {
          badEndValue = true;
        }
      } catch(ParseException pe) {
        badEndValue = true;
      }
      
      if(hasValues && !hasLineCode) {
        blankLineCodeError = true;
      }
    
      if(badStartValue || badEndValue) {

        if(badStartValue) {
          errors.add("", new ActionMessage(startErrorKey, fd.getLineCode()));
          fd.setStartError(true);
        }
        if(badEndValue) {
          errors.add("", new ActionMessage(endErrorKey, fd.getLineCode()));
          fd.setEndError(true);
        }

      } else if( ! (isNew && !hasLineCode) ) {

        Double reportedStart = AdjustmentUtils.parseDouble(fd.getReportedStartOfYearAmount());
        Double prevAdjStart = AdjustmentUtils.parseDouble(fd.getAdjStartOfYearAmount());
        Double adjustmentStart = AdjustmentUtils.calculateAccrualAdjustment(totalStart, reportedStart);
        
        Double reportedEnd = AdjustmentUtils.parseDouble(fd.getReportedEndOfYearAmount());
        Double prevAdjEnd = AdjustmentUtils.parseDouble(fd.getAdjEndOfYearAmount());
        Double adjustmentEnd = AdjustmentUtils.calculateAccrualAdjustment(totalEnd, reportedEnd);
        
        boolean hasPrevAdjustment = fd.getAdjustmentId() != null;
        boolean adjustmentsEqualPrevAdj =
          MathUtils.equalToTwoDecimalPlaces(prevAdjStart, adjustmentStart) &&
          MathUtils.equalToTwoDecimalPlaces(prevAdjEnd, adjustmentEnd);
        boolean hasAdjustmentValues =
          adjustmentStart != null ||
          adjustmentEnd != null;

        String action = null;
        if(hasAdjustmentValues && (fd.isNew() || !hasPrevAdjustment)) {
          action = AdjustmentService.ACTION_ADD;
        } else if(hasPrevAdjustment && !hasAdjustmentValues) {
          action = AdjustmentService.ACTION_DELETE;
        } else if(hasPrevAdjustment && !adjustmentsEqualPrevAdj) {
          action = AdjustmentService.ACTION_UPDATE;
        }
        
        if(action != null) {
          InventoryItem item = 
              buildAdjustment(fd, reportedStart, reportedEnd, adjustmentStart,
                  adjustmentEnd,farmingOperation);
          
          List<InventoryItem> adjustmentList = adjustmentsMap.get(action);
          if(adjustmentList == null) {
            adjustmentList = new ArrayList<>();
            adjustmentsMap.put(action, adjustmentList);
          }
          adjustmentList.add(item);
        }
      }
    }
    
    if(blankLineCodeError) {
      errors.add("", new ActionMessage(MessageConstants.ERRORS_LINE_CODE_BLANK));
    }

    return adjustmentsMap;
  }


  /**
   * @param fd AccrualFormData
   * @param reportedStartOfYearAmount reportedStartOfYearAmount
   * @param reportedEndOfYearAmount reportedEndOfYearAmount
   * @param adjStartOfYearAmount Double
   * @param adjEndOfYearAmount Double
   * @param farmingOperation FarmingOperation
   * @return InventoryItem
   */
  @SuppressWarnings("null")
  private static InventoryItem buildAdjustment(
      AccrualFormData fd,
      Double reportedStartOfYearAmount,
      Double reportedEndOfYearAmount,
      Double adjStartOfYearAmount,
      Double adjEndOfYearAmount,
      FarmingOperation farmingOperation) {
    InventoryItem item = null;
    if(fd.getItemType().equals(AccrualFormData.getInputType())) {
      item = new InputItem();
      item.setInventoryClassCode(InventoryClassCodes.INPUT);
    } else if(fd.getItemType().equals(AccrualFormData.getReceivableType())) {
      item = new ReceivableItem();
      item.setInventoryClassCode(InventoryClassCodes.RECEIVABLE);
    } else if(fd.getItemType().equals(AccrualFormData.getPayableType())) {
      item = new PayableItem();
      item.setInventoryClassCode(InventoryClassCodes.PAYABLE);
    }
    
    item.setFarmingOperation(farmingOperation);

    item.setReportedStartOfYearAmount(reportedStartOfYearAmount);
    item.setReportedEndOfYearAmount(reportedEndOfYearAmount);
    
    item.setAdjStartOfYearAmount(adjStartOfYearAmount);
    item.setAdjEndOfYearAmount(adjEndOfYearAmount);
    
    item.setInventoryItemCode(fd.getLineCode());
    item.setAdjInventoryId(fd.getAdjustmentId());
    item.setReportedInventoryId(fd.getReportedId());
    item.setRevisionCount(fd.getRevisionCount());
    item.setCommodityXrefId(fd.getCommodityXrefId());
    item.setIsEligible(Boolean.valueOf(fd.isEligible()));
    
    return item;
  }


  /**
   * Assumes that all adjustments contained in the curYearAdjMap
   * are for the same year and operation.
   * @param farmingOperation farmingOperation
   * @param curYearAdjMap Map<String action, List<InventoryItem>>
   * @param messages messages
   */
  public static void addCopyForwardAdjustments(FarmingOperation farmingOperation,
      Map<String, List<InventoryItem>> curYearAdjMap, ActionMessages messages) {
    InventoryCalculator invCalc = CalculatorFactory.getInventoryCalculator();
    ReferenceScenario refScenario = farmingOperation.getFarmingYear().getReferenceScenario();
    Integer programYear = refScenario.getParentScenario().getYear();
    Integer curYear = refScenario.getYear();
    Integer nextYear = new Integer(curYear.intValue() + 1);
    boolean isProgramYear = curYear.equals(programYear);

    List<InventoryItem> curYearAddList = curYearAdjMap.get(AdjustmentService.ACTION_ADD);
    List<InventoryItem> curYearUpdateList = curYearAdjMap.get(AdjustmentService.ACTION_UPDATE);
    List<InventoryItem> curYearDeleteList = curYearAdjMap.get(AdjustmentService.ACTION_DELETE);

    // build a list of items for this operation based on the latest adjustments
    // in order to check for multiples
    List<InventoryItem> curYearInvList = new ArrayList<>();
    if(farmingOperation.getAccrualItems() != null) {
      curYearInvList.addAll(farmingOperation.getAccrualItems());
      
      if(curYearDeleteList != null) {
        for(Iterator<InventoryItem> pi = curYearInvList.iterator(); pi.hasNext(); ) {
          InventoryItem item = pi.next();
          for(InventoryItem deletedItem : curYearDeleteList) {
            if(deletedItem.getAdjInventoryId().equals(item.getAdjInventoryId())
                && item.getReportedInventoryId() == null) {
              pi.remove();
              break;
            }
          }
        }
      }
      
      if(curYearAddList != null) {
        for(InventoryItem addedItem : curYearAddList) {
          if(addedItem.getAdjInventoryId() == null && addedItem.getReportedInventoryId() == null) {
            curYearInvList.add(addedItem);
          }
        }
      }
    }
    
    
    // build list of adjustments to copy forward from
    List<InventoryItem> copyFromAdjList = new ArrayList<>();
    Set<String> multipleThisYearCodes = new TreeSet<>();
    
    if(curYearAddList != null) {
      for(InventoryItem item : curYearAddList) {
        // make sure we do not copy forward if there are multiple for this year
        if(invCalc.findMatchingItems(item, curYearInvList).size() > 1) {
          multipleThisYearCodes.add(item.getInventoryItemCode());
        } else {
          copyFromAdjList.add(item);
        }
      }
    }
    if(curYearUpdateList != null) {
      for(InventoryItem item : curYearUpdateList) {
        // make sure we do not copy forward if there are multiple for this year
        if(invCalc.findMatchingItems(item, curYearInvList).size() > 1) {
          multipleThisYearCodes.add(item.getInventoryItemCode());
        } else {
          copyFromAdjList.add(item);
        }
      }
    }
    if(curYearDeleteList != null) {
      for(InventoryItem item : curYearDeleteList) {
        // only do a copy forward if we are reverting to the CRA values 
        if(item.getReportedInventoryId() != null) {
          // make sure we do not copy forward if there are multiple for this year
          if(invCalc.findMatchingItems(item, curYearInvList).size() > 1) {
            multipleThisYearCodes.add(item.getInventoryItemCode());
          } else {
            copyFromAdjList.add(item);
          }
        }
      }
    }
    
    if(!isProgramYear) {
      for(String itemCode : multipleThisYearCodes) {
        Object[] msgValues = {itemCode, curYear.toString(), nextYear.toString()};
        messages.add("", new ActionMessage(
            MessageConstants.ACCRUAL_COPY_FORWARD_MULTIPLE_RECORDS_THIS_YEAR, msgValues));
      }
    }

    List<InventoryItem> nextYearAddList = new ArrayList<>();
    List<InventoryItem> nextYearUpdateList = new ArrayList<>();

    for(InventoryItem curYearItem : copyFromAdjList) {
      List<InventoryItem> nextYearItems = invCalc.getNextYearInventoryItems(curYearItem);

      if(nextYearItems != null && nextYearItems.size() > 1) {
        // skip items where there are multiple matches
        // because we cannot decide which one to use
        Object[] msgValues = {curYearItem.getInventoryItemCode(), nextYear.toString()};
        messages.add("", new ActionMessage(
            MessageConstants.ACCRUAL_COPY_FORWARD_MULTIPLE_RECORDS_NEXT_YEAR, msgValues));

      } else if(nextYearItems == null || nextYearItems.isEmpty()) {
        // found no match so create a new record
        copyForwardNoMatch(messages, invCalc, nextYearAddList, curYearItem,
            nextYear);

      } else {
        // found exactly one match so update it if necessary
        InventoryItem nextYearItem = nextYearItems.get(0);
        
        copyForwardOneMatch(messages, invCalc, nextYearAddList,
            nextYearUpdateList, curYearItem, nextYear, nextYearItem);
      }
      
    }
    
    // if there are any "copy forward" adjustments, update the list
    
    if(!nextYearAddList.isEmpty()) {
      List<InventoryItem> allAdjAddList = new ArrayList<>();
      if(curYearAddList != null) {
        allAdjAddList.addAll(curYearAddList);
      }
      allAdjAddList.addAll(nextYearAddList);
      curYearAdjMap.put(AdjustmentService.ACTION_ADD, allAdjAddList);
    }
    
    if(!nextYearUpdateList.isEmpty()) {
      List<InventoryItem> allAdjUpdateList = new ArrayList<>();
      if(curYearUpdateList != null) {
        allAdjUpdateList.addAll(curYearUpdateList);
      }
      allAdjUpdateList.addAll(nextYearUpdateList);
      curYearAdjMap.put(AdjustmentService.ACTION_UPDATE, allAdjUpdateList);
    }

  }


  /**
   * @param messages messages
   * @param invCalc invCalc
   * @param nextYearAddList nextYearAddList
   * @param curYearItem curYearItem
   * @param nextYear nextYear
   */
  @SuppressWarnings("null")
  private static void copyForwardNoMatch(ActionMessages messages,
      InventoryCalculator invCalc, List<InventoryItem> nextYearAddList,
      InventoryItem curYearItem, Integer nextYear) {
    InventoryItem nextYearItem = null;
    Double curEndAmount = curYearItem.getTotalEndOfYearAmount();
    
    FarmingOperation nextYearOperation = invCalc.getNextYearFarmingOperation(curYearItem);
    
    // skip if there is no end amount
    // or if a corresponding operation does not exist in the next year 
    if(curEndAmount != null
        && curEndAmount.doubleValue() != 0
        && nextYearOperation != null) {
      
      if(curYearItem.getInventoryClassCode().equals(InventoryClassCodes.INPUT)) {
        nextYearItem = new InputItem();
        nextYearItem.setInventoryClassCode(InventoryClassCodes.INPUT);
      } else if(curYearItem.getInventoryClassCode().equals(InventoryClassCodes.RECEIVABLE)) {
        nextYearItem = new ReceivableItem();
        nextYearItem.setInventoryClassCode(InventoryClassCodes.RECEIVABLE);
      } else if(curYearItem.getInventoryClassCode().equals(InventoryClassCodes.PAYABLE)) {
        nextYearItem = new PayableItem();
        nextYearItem.setInventoryClassCode(InventoryClassCodes.PAYABLE);
      }
      nextYearItem.setInventoryItemCode(curYearItem.getInventoryItemCode());
      nextYearItem.setFarmingOperation(nextYearOperation);
      nextYearItem.setAdjStartOfYearAmount(curYearItem.getTotalEndOfYearAmount());
      nextYearItem.setIsEligible(curYearItem.getIsEligible());
      nextYearAddList.add(nextYearItem);
      
      Object[] msgValues = {curYearItem.getInventoryItemCode(), nextYear.toString(),
          formatStartAmountForMessage(nextYearItem)};
      messages.add("", new ActionMessage(MessageConstants.ACCRUAL_COPY_FORWARD_NO_RECORD, msgValues));
    }
  }


  /**
   * @param messages messages
   * @param invCalc invCalc
   * @param nextYearAddList nextYearAddList
   * @param nextYearUpdateList nextYearUpdateList
   * @param curYearItem curYearItem
   * @param nextYear nextYear
   * @param nextYearItem nextYearItem
   */
  private static void copyForwardOneMatch(
      ActionMessages messages,
      InventoryCalculator invCalc,
      List<InventoryItem> nextYearAddList,
      List<InventoryItem> nextYearUpdateList,
      InventoryItem curYearItem,
      Integer nextYear,
      InventoryItem nextYearItem) {
    Double curEndAmount = curYearItem.getTotalEndOfYearAmount();
    Double nextStartAmount = nextYearItem.getTotalStartOfYearAmount();
    boolean valuesAreEqual = MathUtils.equalToTwoDecimalPlaces(curEndAmount, nextStartAmount);

    // skip if the current year end value is already
    // the same as the next year start value
    if(!valuesAreEqual) {

      Double reportedStartAmount = nextYearItem.getReportedStartOfYearAmount();
      Double newStartAmountAdj =
          AdjustmentUtils.calculateAccrualAdjustment(curEndAmount, reportedStartAmount);
      nextYearItem.setAdjStartOfYearAmount(newStartAmountAdj);

      if(nextYearItem.getAdjInventoryId() == null) {
        nextYearAddList.add(nextYearItem);
      } else {
        nextYearUpdateList.add(nextYearItem);
      }
      
      Object[] msgValues = {curYearItem.getInventoryItemCode(), nextYear.toString(),
          formatStartAmountForMessage(nextYearItem)};
      messages.add("", new ActionMessage(MessageConstants.ACCRUAL_COPY_FORWARD, msgValues));
      
    }
  }


  /**
   * @param nextYearItem nextYearItem
   * @return String
   */
  private static String formatStartAmountForMessage(InventoryItem nextYearItem) {
    Double amount = nextYearItem.getTotalStartOfYearAmount();
    String result = StringUtils.formatCurrency(amount);
    if(result.equals("")) {
      result = "blank";
    }
    return result;
  }

}
