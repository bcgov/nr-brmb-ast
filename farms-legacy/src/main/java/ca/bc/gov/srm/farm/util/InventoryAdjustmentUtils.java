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
import ca.bc.gov.srm.farm.calculator.FmvCalculator;
import ca.bc.gov.srm.farm.calculator.InventoryCalculator;
import ca.bc.gov.srm.farm.domain.CropItem;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.InventoryItem;
import ca.bc.gov.srm.farm.domain.LivestockItem;
import ca.bc.gov.srm.farm.domain.ProducedItem;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
import ca.bc.gov.srm.farm.service.AdjustmentService;
import ca.bc.gov.srm.farm.ui.struts.calculator.inventory.InventoryForm;
import ca.bc.gov.srm.farm.ui.struts.calculator.inventory.InventoryFormData;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;

/**
 * @author awilkinson
 * @created Mar 18, 2011
 */
public final class InventoryAdjustmentUtils {

  /** */
  private InventoryAdjustmentUtils() {
  }


  /**
   * @param form The form object to fill.
   * @param item ProducedItem
   * @param setAdjustedValues if false, don not clear the form and set everything but adjusted values
   * @param wholeFarmView wholeFarmView
   * @throws Exception On Exception
   */
  public static void populateFormFromInventoryItem(
      InventoryForm form,
      ProducedItem item,
      boolean setAdjustedValues, boolean wholeFarmView)
  throws Exception {
    
    String lineCode = item.getInventoryItemCode();
    String lineKey;
    if(wholeFarmView) {
      lineKey = InventoryFormData.getWholeFarmLineKey(item);
    } else {
      lineKey = InventoryFormData.getLineKey(item);
    }
    InventoryFormData fd = form.getItem(lineKey);
    fd.setLineCode(lineCode);
    fd.setItemType(InventoryFormData.getItemType(item.getInventoryClassCode()));
    fd.setLineCodeDescription(item.getInventoryItemCodeDescription());

    if(setAdjustedValues) {
      fd.setTotalQuantityStart(StringUtils.formatThreeDecimalPlaces(item.getTotalQuantityStart()));
      fd.setTotalQuantityEnd(StringUtils.formatThreeDecimalPlaces(item.getTotalQuantityEnd()));
      fd.setTotalPriceStart(StringUtils.formatTwoDecimalPlaces(item.getTotalPriceStart()));
      fd.setTotalPriceEnd(StringUtils.formatTwoDecimalPlaces(item.getTotalPriceEnd()));
    }

    fd.setAdjustmentId(item.getAdjInventoryId());
    fd.setReportedId(item.getReportedInventoryId());
    fd.setAdjustedByUserId(StringUtils.formatUserIdForDisplay(item.getAdjustedByUserId()));
    fd.setRevisionCount(item.getRevisionCount());

    fd.setAdjQuantityStart(StringUtils.formatThreeDecimalPlaces(item.getAdjQuantityStart()));
    fd.setAdjQuantityEnd(StringUtils.formatThreeDecimalPlaces(item.getAdjQuantityEnd()));
    fd.setReportedQuantityStart(StringUtils.formatThreeDecimalPlaces(item.getReportedQuantityStart()));
    fd.setReportedQuantityEnd(StringUtils.formatThreeDecimalPlaces(item.getReportedQuantityEnd()));
    
    fd.setAdjPriceStart(StringUtils.formatTwoDecimalPlaces(item.getAdjPriceStart()));
    fd.setAdjPriceEnd(StringUtils.formatTwoDecimalPlaces(item.getAdjPriceEnd()));
    fd.setReportedPriceStart(StringUtils.formatTwoDecimalPlaces(item.getReportedPriceStart()));
    fd.setReportedPriceEnd(StringUtils.formatTwoDecimalPlaces(item.getReportedPriceEnd()));
    
    InventoryCalculator inventoryCalc = CalculatorFactory.getInventoryCalculator();
    Double changeInValue = new Double(inventoryCalc.calculateChangeInValue(item));
    fd.setChangeInValue(changeInValue);

    fd.setCommodityXrefId(item.getCommodityXrefId());
    
    fd.setEndYearProducerPrice(item.getTotalEndYearProducerPrice());
    
    fd.setFmvStart(item.getFmvStart());
    fd.setFmvEnd(item.getFmvEnd());
    fd.setFmvPreviousYearEnd(item.getFmvPreviousYearEnd());
    final int hundred = 100;
    if(item.getFmvVariance() == null) {
      fd.setFmvVariance(null);
    } else {
      double variancePercent = item.getFmvVariance().doubleValue() / hundred;
      fd.setFmvVariance(StringUtils.formatPercent(new Double(variancePercent)));
    }

    FmvCalculator fmvCalc = CalculatorFactory.getFmvCalculator();
    fd.setPriceStartOutsideFmvVariance(fmvCalc.isPriceStartOutOfVariance(item));
    fd.setPriceEndOutsideFmvVariance(fmvCalc.isPriceEndOutOfVariance(item));
    fd.setReportedPriceStartOutsideFmvVariance(fmvCalc.isReportedPriceStartOutOfVariance(item));
    fd.setReportedPriceEndOutsideFmvVariance(fmvCalc.isReportedPriceEndOutOfVariance(item));

    if(item.getInventoryClassCode().equals(InventoryClassCodes.CROP)) {
      CropItem cropItem = (CropItem) item;
      fd.setCropUnitCode(cropItem.getCropUnitCode());
      fd.setCropUnitCodeDescription(cropItem.getCropUnitCodeDescription());
      fd.setOnFarmAcres(StringUtils.formatThreeDecimalPlaces(cropItem.getOnFarmAcres()));
      fd.setUnseedableAcres(StringUtils.formatThreeDecimalPlaces(cropItem.getUnseedableAcres()));
      fd.setAdjQuantityProduced(StringUtils.formatThreeDecimalPlaces(cropItem.getAdjQuantityProduced()));
      fd.setReportedQuantityProduced(
          StringUtils.formatThreeDecimalPlaces(cropItem.getReportedQuantityProduced()));
      if(setAdjustedValues) {
        fd.setTotalQuantityProduced(
            StringUtils.formatThreeDecimalPlaces(cropItem.getTotalQuantityProduced()));
      }
    }

    if(item.getInventoryClassCode().equals(InventoryClassCodes.LIVESTOCK)) {
      LivestockItem livestockItem = (LivestockItem) item;
      fd.setMarketCommodity(livestockItem.getIsMarketCommodity());
    }
  }


  /**
   * @param form InventoryForm
   * @param farmingOperation farmingOperation
   * @param errors ActionMessages
   * @return Map<String action, List<InventoryItem>>
   * @throws Exception On Exception
   */
  public static Map<String, List<InventoryItem>> parseAdjustments(
      InventoryForm form,
      FarmingOperation farmingOperation,
      ActionMessages errors)
  throws Exception {

    final double maxValue = 99999999999d;
    final double minValue = -maxValue;

    // Map<String action, List<InventoryItem>>
    Map<String, List<InventoryItem>> adjustmentsMap = new HashMap<>();
    
    String errorLineCodeBlank = MessageConstants.ERRORS_LINE_CODE_BLANK;
    String errorKeyUnits = MessageConstants.ERRORS_UNITS_BLANK;
    String errorKeyQuantityProduced = MessageConstants.ERRORS_ADJUSTED_QUANTITY_PRODUCED;
    String errorKeyQuantityStart = MessageConstants.ERRORS_ADJUSTED_QUANTITY_START;
    String errorKeyQuantityEnd = MessageConstants.ERRORS_ADJUSTED_QUANTITY_END;
    String errorKeyPriceStart = MessageConstants.ERRORS_ADJUSTED_PRICE_START;
    String errorKeyPriceEnd = MessageConstants.ERRORS_ADJUSTED_PRICE_END;
    boolean blankLineCodeError = false;
    
    for(Iterator itemIt = form.getItems().values().iterator(); itemIt.hasNext(); ) {
      InventoryFormData fd = (InventoryFormData) itemIt.next();
      boolean isNew = fd.isNew();

      String totalQuantityProducedString = fd.getTotalQuantityProduced();
      String totalQuantityStartString = fd.getTotalQuantityStart();
      String totalQuantityEndString = fd.getTotalQuantityEnd();
      String totalPriceStartString = fd.getTotalPriceStart();
      String totalPriceEndString = fd.getTotalPriceEnd();

      boolean badUnitsValue = false;
      boolean badQuantityProducedValue = false;
      boolean badQuantityStartValue = false;
      boolean badQuantityEndValue = false;
      boolean badPriceStartValue = false;
      boolean badPriceEndValue = false;

      Double totalQuantityProduced = null;
      Double totalQuantityStart = null;
      Double totalQuantityEnd = null;
      Double totalPriceStart = null;
      Double totalPriceEnd = null;

      boolean hasLineCode = ! StringUtils.isBlank(fd.getLineCode());
      boolean hasUnits = ! StringUtils.isBlank(fd.getCropUnitCode());
      boolean missingUnits = !hasUnits && fd.getItemType().equals(InventoryFormData.getCropType());
      boolean hasValues =
        (!StringUtils.isBlank(totalQuantityProducedString)
          || !StringUtils.isBlank(totalQuantityStartString)
          || !StringUtils.isBlank(totalQuantityEndString)
          || !StringUtils.isBlank(totalPriceStartString)
          || !StringUtils.isBlank(totalPriceEndString));

      /* Start parse totals */
      try {
        totalQuantityProduced = AdjustmentUtils.parseDouble(totalQuantityProducedString);
        if(AdjustmentUtils.outOfRange(totalQuantityProduced, minValue, maxValue)) {
          badQuantityProducedValue = true;
        }
      } catch(ParseException pe) {
        badQuantityProducedValue = true;
      }

      try {
        totalQuantityStart = AdjustmentUtils.parseDouble(totalQuantityStartString);
        if(AdjustmentUtils.outOfRange(totalQuantityStart, minValue, maxValue)) {
          badQuantityStartValue = true;
        }
      } catch(ParseException pe) {
        badQuantityStartValue = true;
      }

      try {
        totalQuantityEnd = AdjustmentUtils.parseDouble(totalQuantityEndString);
        if(AdjustmentUtils.outOfRange(totalQuantityEnd, minValue, maxValue)) {
          badQuantityEndValue = true;
        }
      } catch(ParseException pe) {
        badQuantityEndValue = true;
      }
      
      try {
        totalPriceStart = AdjustmentUtils.parseDouble(totalPriceStartString);
        if(AdjustmentUtils.outOfRange(totalPriceStart, minValue, maxValue)) {
          badPriceStartValue = true;
        }
      } catch(ParseException pe) {
        badPriceStartValue = true;
      }
      
      try {
        totalPriceEnd = AdjustmentUtils.parseDouble(totalPriceEndString);
        if(AdjustmentUtils.outOfRange(totalPriceEnd, minValue, maxValue)) {
          badPriceEndValue = true;
        }
      } catch(ParseException pe) {
        badPriceEndValue = true;
      }
      /* End parse totals */

      if((hasValues || hasLineCode) && fd.isNew() && missingUnits) {
        badUnitsValue = true;
      }
      
      if((hasValues || hasUnits) && !hasLineCode) {
        blankLineCodeError = true;
      }
      
      if(badUnitsValue
          || badQuantityProducedValue
          || badQuantityStartValue
          || badQuantityEndValue
          || badPriceStartValue
          || badPriceEndValue) {

        if(badUnitsValue) {
          errors.add("", new ActionMessage(errorKeyUnits, fd.getLineCode()));
          fd.setErrorUnits(true);
        }
        if(badQuantityProducedValue) {
          errors.add("", new ActionMessage(errorKeyQuantityProduced, fd.getLineCode()));
          fd.setErrorQuantityProduced(true);
        }
        if(badQuantityStartValue) {
          errors.add("", new ActionMessage(errorKeyQuantityStart, fd.getLineCode()));
          fd.setErrorQuantityStart(true);
        }
        if(badQuantityEndValue) {
          errors.add("", new ActionMessage(errorKeyQuantityEnd, fd.getLineCode()));
          fd.setErrorQuantityEnd(true);
        }
        if(badPriceStartValue) {
          errors.add("", new ActionMessage(errorKeyPriceStart, fd.getLineCode()));
          fd.setErrorPriceStart(true);
        }
        if(badPriceEndValue) {
          errors.add("", new ActionMessage(errorKeyPriceEnd, fd.getLineCode()));
          fd.setErrorPriceEnd(true);
        }

      } else if( ! (isNew && !hasLineCode) ) {

        Double reportedQuantityProduced = AdjustmentUtils.parseDouble(fd.getReportedQuantityProduced());
        Double prevAdjQuantityProduced = AdjustmentUtils.parseDouble(fd.getAdjQuantityProduced());
        Double adjustmentQuantityProduced =
          AdjustmentUtils.calculateQuantityAdjustment(totalQuantityProduced, reportedQuantityProduced);
        
        Double reportedQuantityStart = AdjustmentUtils.parseDouble(fd.getReportedQuantityStart());
        Double prevAdjQuantityStart = AdjustmentUtils.parseDouble(fd.getAdjQuantityStart());
        Double adjustmentQuantityStart =
          AdjustmentUtils.calculateQuantityAdjustment(totalQuantityStart, reportedQuantityStart);
        
        Double reportedQuantityEnd = AdjustmentUtils.parseDouble(fd.getReportedQuantityEnd());
        Double prevAdjQuantityEnd = AdjustmentUtils.parseDouble(fd.getAdjQuantityEnd());
        Double adjustmentQuantityEnd =
          AdjustmentUtils.calculateQuantityAdjustment(totalQuantityEnd, reportedQuantityEnd);
        
        Double reportedPriceStart = AdjustmentUtils.parseDouble(fd.getReportedPriceStart());
        Double prevAdjPriceStart = AdjustmentUtils.parseDouble(fd.getAdjPriceStart());
        Double adjustmentPriceStart =
          AdjustmentUtils.calculatePriceAdjustment(totalPriceStart, reportedPriceStart);
        
        Double reportedPriceEnd = AdjustmentUtils.parseDouble(fd.getReportedPriceEnd());
        Double prevAdjPriceEnd = AdjustmentUtils.parseDouble(fd.getAdjPriceEnd());
        Double adjustmentPriceEnd = AdjustmentUtils.calculatePriceAdjustment(totalPriceEnd, reportedPriceEnd);
        
        boolean hasPrevAdjustment = fd.getAdjustmentId() != null;
        boolean adjustmentsEqualPrevAdj =
          MathUtils.equalToThreeDecimalPlaces(prevAdjQuantityProduced, adjustmentQuantityProduced) &&
          MathUtils.equalToThreeDecimalPlaces(prevAdjQuantityStart, adjustmentQuantityStart) &&
          MathUtils.equalToThreeDecimalPlaces(prevAdjQuantityEnd, adjustmentQuantityEnd) &&
          MathUtils.equalToTwoDecimalPlaces(prevAdjPriceStart, adjustmentPriceStart) &&
          MathUtils.equalToTwoDecimalPlaces(prevAdjPriceEnd, adjustmentPriceEnd);
        boolean hasAdjustmentValues =
          adjustmentQuantityProduced != null ||
          adjustmentQuantityStart != null ||
          adjustmentQuantityEnd != null ||
          adjustmentPriceStart != null ||
          adjustmentPriceEnd != null;

        String action = null;
        if(hasAdjustmentValues && (fd.isNew() || !hasPrevAdjustment)) {
          action = AdjustmentService.ACTION_ADD;
        } else if(hasPrevAdjustment && !hasAdjustmentValues) {
          action = AdjustmentService.ACTION_DELETE;
        } else if(hasPrevAdjustment && !adjustmentsEqualPrevAdj) {
          action = AdjustmentService.ACTION_UPDATE;
        }
        
        if(action != null) {
          InventoryItem item = null;
          if(fd.getItemType().equals(InventoryFormData.getCropType())) {
            CropItem cropItem = new CropItem();
            item = cropItem;
            item.setInventoryClassCode(InventoryClassCodes.CROP);
            cropItem.setCropUnitCode(fd.getCropUnitCode());
            cropItem.setReportedQuantityProduced(reportedQuantityProduced);
            cropItem.setAdjQuantityProduced(adjustmentQuantityProduced);
          } else if(fd.getItemType().equals(InventoryFormData.getLivestockType())) {
            LivestockItem livestockItem = new LivestockItem();
            item = livestockItem;
            item.setInventoryClassCode(InventoryClassCodes.LIVESTOCK);
            livestockItem.setIsMarketCommodity(fd.getMarketCommodity());
          }
          
          item.setFarmingOperation(farmingOperation);
          
          item.setReportedQuantityStart(reportedQuantityStart);
          item.setReportedPriceStart(reportedPriceStart);
          item.setReportedQuantityEnd(reportedQuantityEnd);
          item.setReportedPriceEnd(reportedPriceEnd);
          
          item.setAdjQuantityStart(adjustmentQuantityStart);
          item.setAdjPriceStart(adjustmentPriceStart);
          item.setAdjQuantityEnd(adjustmentQuantityEnd);
          item.setAdjPriceEnd(adjustmentPriceEnd);
          
          item.setInventoryItemCode(fd.getLineCode());
          item.setAdjInventoryId(fd.getAdjustmentId());
          item.setReportedInventoryId(fd.getReportedId());
          item.setRevisionCount(fd.getRevisionCount());
          item.setCommodityXrefId(fd.getCommodityXrefId());
          item.setIsEligible(Boolean.TRUE);
          
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
      errors.add("", new ActionMessage(errorLineCodeBlank));
    }

    return adjustmentsMap;
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
    if(farmingOperation.getProducedItems() != null) {
      curYearInvList.addAll(farmingOperation.getProducedItems());
      
      if(curYearDeleteList != null) {
        for(Iterator<InventoryItem> pi = curYearInvList.iterator(); pi.hasNext(); ) {
          ProducedItem item = (ProducedItem) pi.next();
          for(Iterator<InventoryItem> di = curYearDeleteList.iterator(); di.hasNext(); ) {
            ProducedItem deletedItem = (ProducedItem) di.next();
            if(deletedItem.getAdjInventoryId().equals(item.getAdjInventoryId())
                && item.getReportedInventoryId() == null) {
              pi.remove();
              break;
            }
          }
        }
      }
      
      if(curYearAddList != null) {
        for(Iterator<InventoryItem> ai = curYearAddList.iterator(); ai.hasNext(); ) {
          ProducedItem addedItem = (ProducedItem) ai.next();
          if(addedItem.getAdjInventoryId() == null && addedItem.getReportedInventoryId() == null) {
            curYearInvList.add(addedItem);
          }
        }
      }
    }
    
    
    // build list of adjustments to copy forward from
    List<ProducedItem> copyFromAdjList = new ArrayList<>();
    Set multipleThisYearCodes = new TreeSet();
    
    if(curYearAddList != null) {
      for(InventoryItem inventoryItem : curYearAddList) {
        ProducedItem item = (ProducedItem) inventoryItem;
        // make sure we do not copy forward if there are multiple for this year
        if(invCalc.findMatchingItems(item, curYearInvList).size() > 1) {
          multipleThisYearCodes.add(item.getInventoryItemCode());
        } else {
          copyFromAdjList.add(item);
        }
      }
    }
    if(curYearUpdateList != null) {
      for(Iterator ci = curYearUpdateList.iterator(); ci.hasNext(); ) {
        ProducedItem item = (ProducedItem) ci.next();
        // make sure we do not copy forward if there are multiple for this year
        if(invCalc.findMatchingItems(item, curYearInvList).size() > 1) {
          multipleThisYearCodes.add(item.getInventoryItemCode());
        } else {
          copyFromAdjList.add(item);
        }
      }
    }
    if(curYearDeleteList != null) {
      for(Iterator ci = curYearDeleteList.iterator(); ci.hasNext(); ) {
        ProducedItem item = (ProducedItem) ci.next();
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
      for(Iterator ci = multipleThisYearCodes.iterator(); ci.hasNext(); ) {
        String itemCode = (String) ci.next();
        Object[] msgValues = {itemCode, curYear.toString(), nextYear.toString()};
        messages.add("", new ActionMessage(
            MessageConstants.INV_COPY_FORWARD_MULTIPLE_RECORDS_THIS_YEAR, msgValues));
      }
    }

    List nextYearAddList = new ArrayList();
    List nextYearUpdateList = new ArrayList();

    for(Iterator ci = copyFromAdjList.iterator(); ci.hasNext(); ) {
      ProducedItem curYearItem = (ProducedItem) ci.next();
      List nextYearItems = invCalc.getNextYearInventoryItems(curYearItem);

      if(nextYearItems != null && nextYearItems.size() > 1) {
        // skip items where there are multiple matches
        // because we cannot decide which one to use
        Object[] msgValues = {curYearItem.getInventoryItemCode(), nextYear.toString()};
        messages.add("", new ActionMessage(
            MessageConstants.INV_COPY_FORWARD_MULTIPLE_RECORDS_NEXT_YEAR, msgValues));

      } else if(nextYearItems == null || nextYearItems.isEmpty()) {
        // found no match so create a new record
        copyForwardNoMatch(messages, invCalc, nextYearAddList, curYearItem,
            nextYear);

      } else {
        // found exactly one match so update it if necessary
        ProducedItem nextYearItem = (ProducedItem) nextYearItems.get(0);
        
        copyForwardOneMatch(messages, invCalc, nextYearAddList,
            nextYearUpdateList, curYearItem, nextYear, nextYearItem);
      }
      
    }
    
    // if there are any "copy forward" adjustments, update the list
    
    if(!nextYearAddList.isEmpty()) {
      List allAdjAddList = new ArrayList();
      if(curYearAddList != null) {
        allAdjAddList.addAll(curYearAddList);
      }
      allAdjAddList.addAll(nextYearAddList);
      curYearAdjMap.put(AdjustmentService.ACTION_ADD, allAdjAddList);
    }
    
    if(!nextYearUpdateList.isEmpty()) {
      List allAdjUpdateList = new ArrayList();
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
  private static void copyForwardNoMatch(ActionMessages messages,
      InventoryCalculator invCalc, List nextYearAddList,
      ProducedItem curYearItem, Integer nextYear) {
    ProducedItem nextYearItem = null;
    Double curQuantityEnd = curYearItem.getTotalQuantityEnd();
    
    FarmingOperation nextYearOperation = invCalc.getNextYearFarmingOperation(curYearItem);
    
    // skip if there is no quantity
    // or if a corresponding operation does not exist in the next year 
    if(curQuantityEnd != null
        && curQuantityEnd.doubleValue() != 0
        && nextYearOperation != null) {
      
      if(curYearItem.getInventoryClassCode().equals(InventoryClassCodes.CROP)) {
        CropItem curYearCropItem = (CropItem) curYearItem;
        CropItem nextYearCropItem = new CropItem();
        nextYearCropItem.setCropUnitCode(curYearCropItem.getCropUnitCode());
        nextYearItem = nextYearCropItem;
        nextYearItem.setInventoryClassCode(InventoryClassCodes.CROP);
      } else if(curYearItem.getInventoryClassCode().equals(InventoryClassCodes.LIVESTOCK)) {
        nextYearItem = new LivestockItem();
        nextYearItem.setInventoryClassCode(InventoryClassCodes.LIVESTOCK);
      }
      nextYearItem.setInventoryItemCode(curYearItem.getInventoryItemCode());
      nextYearItem.setFarmingOperation(nextYearOperation);
      nextYearItem.setAdjQuantityStart(curYearItem.getTotalQuantityEnd());
      nextYearItem.setIsEligible(curYearItem.getIsEligible());
      nextYearAddList.add(nextYearItem);
      
      if(invCalc.isMarketCommodity(curYearItem)) {
        nextYearItem.setAdjPriceStart(curYearItem.getTotalPriceEnd());
        Object[] msgValues = {curYearItem.getInventoryItemCode(), nextYear.toString(),
            formatQuantityStartForMessage(nextYearItem),
            formatPriceStartForMessage(nextYearItem)};
        messages.add("", new ActionMessage(
            MessageConstants.INV_COPY_FORWARD_MARKET_COMMODITY_NO_RECORD, msgValues));
      } else {
        Object[] msgValues = {curYearItem.getInventoryItemCode(), nextYear.toString(),
            formatQuantityStartForMessage(nextYearItem)};
        messages.add("", new ActionMessage(
            MessageConstants.INV_COPY_FORWARD_NON_MARKET_COMMODITY_NO_RECORD, msgValues));
      }
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
  private static void copyForwardOneMatch(ActionMessages messages,
      InventoryCalculator invCalc, List nextYearAddList,
      List<InventoryItem> nextYearUpdateList, ProducedItem curYearItem, Integer nextYear,
      ProducedItem nextYearItem) {
    Double curQuantityEnd = curYearItem.getTotalQuantityEnd();
    Double curPriceEnd = curYearItem.getTotalPriceEnd();
    Double nextQuantityStart = nextYearItem.getTotalQuantityStart();
    Double nextPriceStart = nextYearItem.getTotalPriceStart();
    boolean valuesAreEqual;
    if(invCalc.isMarketCommodity(curYearItem)) {
      valuesAreEqual = MathUtils.equalToThreeDecimalPlaces(curQuantityEnd, nextQuantityStart)
          && MathUtils.equalToTwoDecimalPlaces(curPriceEnd, nextPriceStart);
    } else {
      // do not check price for non-market commodity because we do not copy it forward
      valuesAreEqual = MathUtils.equalToThreeDecimalPlaces(curQuantityEnd, nextQuantityStart);
    }

    // skip if the current year end values are already
    // the same as the next year start values
    if(!valuesAreEqual) {

      Double reportedQuantityStart = nextYearItem.getReportedQuantityStart();
      Double newQuantityStartAdj =
          AdjustmentUtils.calculateQuantityAdjustment(curQuantityEnd, reportedQuantityStart);
      nextYearItem.setAdjQuantityStart(newQuantityStartAdj);
      
      // do not copy price forward for non-market commodity
      if(invCalc.isMarketCommodity(curYearItem) ) {
        Double reportedPriceStart = nextYearItem.getReportedPriceStart();
        Double newPriceStartAdj =
            AdjustmentUtils.calculatePriceAdjustment(curPriceEnd, reportedPriceStart);
        nextYearItem.setAdjPriceStart(newPriceStartAdj);
      }

      if(nextYearItem.getAdjInventoryId() == null) {
        nextYearAddList.add(nextYearItem);
        
        if(invCalc.isMarketCommodity(curYearItem) ) {
          Object[] msgValues = {curYearItem.getInventoryItemCode(), nextYear.toString(),
              formatQuantityStartForMessage(nextYearItem),
              formatPriceStartForMessage(nextYearItem)};
          messages.add("", new ActionMessage(MessageConstants.INV_COPY_FORWARD_MARKET_COMMODITY, msgValues));
        } else {
          Object[] msgValues = {curYearItem.getInventoryItemCode(), nextYear.toString(),
              formatQuantityStartForMessage(nextYearItem)};
          messages.add("", new ActionMessage(
              MessageConstants.INV_COPY_FORWARD_NON_MARKET_COMMODITY, msgValues));
        }
      } else {
        nextYearUpdateList.add(nextYearItem);
        
        if(invCalc.isMarketCommodity(curYearItem) ) {
          Object[] msgValues = {curYearItem.getInventoryItemCode(), nextYear.toString(),
              formatQuantityStartForMessage(nextYearItem),
              formatPriceStartForMessage(nextYearItem)};
          messages.add("", new ActionMessage(
              MessageConstants.INV_COPY_FORWARD_MARKET_COMMODITY, msgValues));
        } else {
          Object[] msgValues = {curYearItem.getInventoryItemCode(), nextYear.toString(),
              formatQuantityStartForMessage(nextYearItem)};
          messages.add("", new ActionMessage(
              MessageConstants.INV_COPY_FORWARD_NON_MARKET_COMMODITY, msgValues));
        }
      }
      
    }
  }


  /**
   * @param nextYearItem nextYearItem
   * @return String
   */
  private static String formatQuantityStartForMessage(ProducedItem nextYearItem) {
    Double quantity = nextYearItem.getTotalQuantityStart();
    String result = StringUtils.formatThreeDecimalPlaces(quantity);
    if(result.equals("")) {
      result = "blank";
    }
    return result;
  }
  
  
  /**
   * @param nextYearItem nextYearItem
   * @return String
   */
  private static String formatPriceStartForMessage(ProducedItem nextYearItem) {
    Double price = nextYearItem.getTotalPriceStart();
    String result = StringUtils.formatCurrency(price);
    if(result.equals("")) {
      result = "blank";
    }
    return result;
  }

}
