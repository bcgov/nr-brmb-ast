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

import java.util.ArrayList;
import java.util.Collections;
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

import ca.bc.gov.srm.farm.calculator.AccrualCalculator;
import ca.bc.gov.srm.farm.calculator.CalculatorFactory;
import ca.bc.gov.srm.farm.calculator.InventoryCalculator;
import ca.bc.gov.srm.farm.domain.CropItem;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingOperationImportOption;
import ca.bc.gov.srm.farm.domain.InventoryItem;
import ca.bc.gov.srm.farm.domain.LivestockItem;
import ca.bc.gov.srm.farm.domain.ProducedItem;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.list.CodeListView;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorAction;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorForm;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.InventoryAdjustmentUtils;
import ca.bc.gov.srm.farm.util.MathUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * @author awilkinson
 * @created Jan 25, 2011
 */
public class InventoryViewAction extends CalculatorAction {

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

    logger.debug("Viewing Inventory...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    InventoryForm form = (InventoryForm) actionForm;
    Scenario scenario = getScenario(form);

    populateForm(form, scenario, true);
    setReadOnlyFlag(request, form, scenario);

    if( scenario.getScenarioStateCode().equals(ScenarioStateCodes.IN_PROGRESS) ) {
      ActionMessages warnings = getDuplicateWarnings(scenario);
      warnings.add(getMissingFmvWarnings(scenario));
      warnings.add(getNonMarketCommodityMismatchedPricesWarnings(scenario, form));
      if(!warnings.isEmpty()) {
        saveMessages(request, warnings);
      }
    }

    return forward;
  }
  
  
  /**
   * @param scenario scenario
   * @return ActionMessages
   */
  protected ActionMessages getDuplicateWarnings(Scenario scenario) {
    ActionMessages warnings = new ActionMessages();
    InventoryCalculator invCalc = CalculatorFactory.getInventoryCalculator();
    Set<String> duplicateCodes = new TreeSet<>();
    
    for(ReferenceScenario refScenario : scenario.getAllScenarios()) {
      for(FarmingOperation fo : refScenario.getFarmingYear().getFarmingOperations()) {
        for(InventoryItem item : fo.getProducedItems()) {
          
          List<InventoryItem> sameYearMatchingInventoryItems = invCalc.getSameYearMatchingInventoryItems(item);
          boolean hasDuplicates = sameYearMatchingInventoryItems.size() > 1;
          if(hasDuplicates) {
            duplicateCodes.add(item.getInventoryItemCode());
          }
        }
      }
    }
    
    if(!duplicateCodes.isEmpty()) {
      String codeCsv = StringUtils.toCsv(new ArrayList<>(duplicateCodes));
      String errorKey = MessageConstants.WARNING_INVENTORY_DUPLICATES;
      
      warnings.add("", new ActionMessage(errorKey, codeCsv));
    }
    
    return warnings;
  }


  /**
   * @param scenario scenario
   * @return ActionMessages
   */
  protected ActionMessages getMissingFmvWarnings(Scenario scenario) {
  	ActionMessages warnings = new ActionMessages();
  	Map<Integer, Set<String>> inventoryWithMissingFMVsMap = ScenarioUtils.getAllInventoryWithMissingFMVs(scenario);

  	for(Integer year : inventoryWithMissingFMVsMap.keySet() ) {
  	  Set<String> inventoryWithMissingFMVs = inventoryWithMissingFMVsMap.get(year);
      if(!inventoryWithMissingFMVs.isEmpty()) {
        String codeCsv = StringUtils.toCsv(new ArrayList<>(inventoryWithMissingFMVs));
        String errorKey = MessageConstants.WARNING_INVENTORY_FMV_MISSING;
        
        warnings.add("", new ActionMessage(errorKey, year.toString(), codeCsv));
      }
  	}
    
    return warnings;
  }
  
  
  /**
   * @param scenario scenario
   * @param form form
   * @return ActionMessages
   */
  protected ActionMessages getNonMarketCommodityMismatchedPricesWarnings(
      Scenario scenario, InventoryForm form) {
    Integer selectedYear = new Integer(form.getFarmViewYear());
    ReferenceScenario refScenario = scenario.getReferenceScenarioByYear(selectedYear);

    ActionMessages warnings = new ActionMessages();
    Set<String> nonMarketCommodities =
        ScenarioUtils.getNonMarketCommoditiesWithMismatchedPrices(refScenario);
    
    if(!nonMarketCommodities.isEmpty()) {
      String codeCsv = StringUtils.toCsv(new ArrayList<>(nonMarketCommodities));
      String errorKey = MessageConstants.WARNING_INVENTORY_NON_MARKET_COMMODITY_MISMATCHED_PRICES;
      
      warnings.add("", new ActionMessage(errorKey, codeCsv));
    }
    
    return warnings;
  }


  /**
   * Fill the form fields from the scenario
   * @param form The form object to fill.
   * @param scenario The scenario to fill the form from.
   * @param setAdjustedValues if false, do not clear the form and set everything but adjusted values
   * @throws Exception On Exception
   */
  protected void populateForm(
      InventoryForm form,
      Scenario scenario,
      boolean setAdjustedValues)
  throws Exception {
    
    if(setAdjustedValues) {
      form.clear();
    }
    
    if(StringUtils.isBlank(form.getFinancialViewRadio())) {
      form.setItemTypeRadio("crop");
      if(scenario.isUserScenario() || scenario.isFifoScenario()) {
        form.setFinancialViewRadio("adjusted");
      } else {
        form.setFinancialViewRadio("cra");
      }
    }
    
    if(setAdjustedValues) {
      form.setScenarioRevisionCount(scenario.getRevisionCount());
    }
    
    syncFarmViewCacheWithForm(form);

    if(form.getFarmViewYear() == null) {
      form.setFarmViewYear(scenario.getYear().toString());
    }
    
    if(form.getFarmView().equals(CalculatorForm.FARM_VIEW_WHOLE)) {
      populateFormForWholeFarm(form, scenario);
    } else {
      populateFormForSingleOperation(form, scenario, setAdjustedValues);
    }
    
    populateFarmViewOptions(form, scenario);
    populateFarmViewYearOptions(form, scenario);
    populateOperationsForImport(form, scenario);
  }


  /**
   * @param form form
   * @param scenario Scenario
   * @param setAdjustedValues boolean
   * @throws Exception On Exception
   */
  private void populateFormForSingleOperation(
      InventoryForm form,
      Scenario scenario,
      boolean setAdjustedValues)
  throws Exception {
    
    String schedule = form.getFarmView();
    List<ProducedItem> items = getInventoryItems(scenario, form.getFarmViewYear(), schedule);
    List<ProducedItem> cropItems = new ArrayList<>();
    List<ProducedItem> livestockItems = new ArrayList<>();
    
    for(ProducedItem item : items) {
      if(item.getInventoryClassCode().equals(InventoryClassCodes.CROP)) {
        cropItems.add(item);
      } else if(item.getInventoryClassCode().equals(InventoryClassCodes.LIVESTOCK)) {
        livestockItems.add(item);
      }
      InventoryAdjustmentUtils.populateFormFromInventoryItem(form, item, setAdjustedValues, false);
    }

    Map<String, Double> totalValues = new HashMap<>();
    AccrualCalculator accrualCalc = CalculatorFactory.getAccrualCalculator(scenario);
    Double cropTotal = accrualCalc.calculateTotal(cropItems);
    Double livestockTotal = accrualCalc.calculateTotal(livestockItems);
    totalValues.put(InventoryFormData.getCropType(), cropTotal);
    totalValues.put(InventoryFormData.getLivestockType(), livestockTotal);
    form.setTotalValues(totalValues);
  }


  /**
   * Fill the form fields from the reference scenario
   * @param form The form object to fill.
   * @param scenario The scenario to fill the form from.
   * @throws Exception On Exception
   */
  protected void populateFormForWholeFarm(
      InventoryForm form,
      Scenario scenario)
  throws Exception {
    
    // constant for rounding quantities
    final int three = 3;

    List<ProducedItem> items = getInventoryItems(scenario, form.getFarmViewYear(), null);
    
    Map<String, List<ProducedItem>> lineKeyItemListMap = new HashMap<>();
    
    for(ProducedItem item : items) {
      String lineKey = InventoryFormData.getWholeFarmLineKey(item);
      
      List<ProducedItem> itemList = lineKeyItemListMap.get(lineKey);
      if(itemList == null) {
        itemList = new ArrayList<>(2); // most will be one or two
        lineKeyItemListMap.put(lineKey, itemList);
      }
      
      itemList.add(item);
    }
    
    List<ProducedItem> cropItems = new ArrayList<>();
    List<ProducedItem> livestockItems = new ArrayList<>();
    
    for(String lineKey : lineKeyItemListMap.keySet()) {
      List<ProducedItem> itemList = lineKeyItemListMap.get(lineKey);
      
      ProducedItem firstItem = itemList.get(0);
      ProducedItem wholeFarmItem = firstItem.getClass().newInstance();
      
      CropItem wholeFarmCropItem = null;
      LivestockItem wholeFarmLivestockItem = null;
      
      double reportedQuantityProducedSum = 0d;
      double adjQuantityProducedSum = 0d;
      double reportedQuantityStartSum = 0d;
      double reportedQuantityEndSum = 0d;
      double adjQuantityStartSum = 0d;
      double adjQuantityEndSum = 0d;
      double onFarmAcresSum = 0d;
      double unseedableAcresSum = 0d;
      
      boolean reportedQuantityProducedExists = false;
      boolean adjQuantityProducedExists = false;
      boolean reportedQuantityStartExists = false;
      boolean reportedQuantityEndExists = false;
      boolean adjQuantityStartExists = false;
      boolean adjQuantityEndExists = false;
      boolean onFarmAcresExists = false;
      boolean unseedableAcresExists = false;
      
      boolean isFirstItem = true;
      for(ProducedItem item : itemList) {
        CropItem cropItem = null;
        LivestockItem livestockItem = null;
        if(item.getInventoryClassCode().equals(InventoryClassCodes.CROP)) {
          cropItem = (CropItem) item;
        } else if(item.getInventoryClassCode().equals(InventoryClassCodes.LIVESTOCK)) {
          livestockItem = (LivestockItem) item;
        }
        FarmingOperation fo = item.getFarmingOperation();

        double partnershipPercent = ScenarioUtils.getPartnershipPercent(fo);
        
        if(isFirstItem) {
          isFirstItem = false;
          wholeFarmItem = item.getClass().newInstance();
          if(cropItem != null) {
            wholeFarmCropItem = (CropItem) wholeFarmItem;
            wholeFarmCropItem.setCropUnitCode(cropItem.getCropUnitCode());
            wholeFarmCropItem.setCropUnitCodeDescription(cropItem.getCropUnitCodeDescription());
          } else if(livestockItem != null) {
            wholeFarmLivestockItem = (LivestockItem) wholeFarmItem;
            wholeFarmLivestockItem.setIsMarketCommodity(livestockItem.getIsMarketCommodity());
          }
          wholeFarmItem.setInventoryItemCode(item.getInventoryItemCode());
          wholeFarmItem.setInventoryItemCodeDescription(item.getInventoryItemCodeDescription());
          wholeFarmItem.setInventoryClassCode(item.getInventoryClassCode());
          wholeFarmItem.setInventoryClassCodeDescription(item.getInventoryClassCodeDescription());
          wholeFarmItem.setInventoryGroupCode(item.getInventoryGroupCode());
          wholeFarmItem.setInventoryClassCodeDescription(item.getInventoryGroupCodeDescription());
          wholeFarmItem.setIsEligible(Boolean.TRUE);
          wholeFarmItem.setFmvStart(item.getFmvStart());
          wholeFarmItem.setFmvEnd(item.getFmvEnd());
          wholeFarmItem.setFmvPreviousYearEnd(item.getFmvPreviousYearEnd());
          wholeFarmItem.setFmvVariance(item.getFmvVariance());
          wholeFarmItem.setFmvAverage(item.getFmvAverage());
        }
        
        if(cropItem != null) {
          if(cropItem.getReportedQuantityProduced() != null) {
            reportedQuantityProducedExists = true;
            reportedQuantityProducedSum +=
              cropItem.getReportedQuantityProduced().doubleValue() * partnershipPercent;
          }
          if(cropItem.getAdjQuantityProduced() != null) {
            adjQuantityProducedExists = true;
            adjQuantityProducedSum += cropItem.getAdjQuantityProduced().doubleValue() * partnershipPercent;
          }
          if(cropItem.getOnFarmAcres() != null) {
            onFarmAcresExists = true;
            onFarmAcresSum += cropItem.getOnFarmAcres().doubleValue() * partnershipPercent;
          }
          if(cropItem.getUnseedableAcres() != null) {
            unseedableAcresExists = true;
            unseedableAcresSum += cropItem.getUnseedableAcres().doubleValue() * partnershipPercent;
          }
        }
        
        if(item.getReportedQuantityStart() != null) {
          reportedQuantityStartExists = true;
          reportedQuantityStartSum += item.getReportedQuantityStart().doubleValue() * partnershipPercent;
        }
        if(item.getReportedQuantityEnd() != null) {
          reportedQuantityEndExists = true;
          reportedQuantityEndSum += item.getReportedQuantityEnd().doubleValue() * partnershipPercent;
        }
        if(item.getAdjQuantityStart() != null) {
          adjQuantityStartExists = true;
          adjQuantityStartSum += item.getAdjQuantityStart().doubleValue() * partnershipPercent;
        }
        if(item.getAdjQuantityEnd() != null) {
          adjQuantityEndExists = true;
          adjQuantityEndSum += item.getAdjQuantityEnd().doubleValue() * partnershipPercent;
        }
        
        // It does not make sense to sum prices. Just use the last one.
        if(item.getReportedPriceStart() != null || item.getAdjPriceStart() != null) {
          wholeFarmItem.setReportedPriceStart(item.getReportedPriceStart());
          wholeFarmItem.setAdjPriceStart(item.getAdjPriceStart());
        }
        if(item.getReportedPriceEnd() != null || item.getAdjPriceEnd() != null) {
          wholeFarmItem.setReportedPriceEnd(item.getReportedPriceEnd());
          wholeFarmItem.setAdjPriceEnd(item.getAdjPriceEnd());
        }

      }
      
      if(wholeFarmCropItem != null) {
        if(reportedQuantityProducedExists) {
          // round to the specified number of decimal places
          double roundedProduced =
            MathUtils.round(reportedQuantityProducedSum, three);
          wholeFarmCropItem.setReportedQuantityProduced(new Double(roundedProduced));
        }
        if(adjQuantityProducedExists) {
          double roundedProduced = MathUtils.round(adjQuantityProducedSum, three);
          wholeFarmCropItem.setAdjQuantityProduced(new Double(roundedProduced));
        }
        if(onFarmAcresExists) {
          double rounded = MathUtils.round(onFarmAcresSum, three);
          wholeFarmCropItem.setOnFarmAcres(new Double(rounded));
        }
        if(unseedableAcresExists) {
          double rounded = MathUtils.round(unseedableAcresSum, three);
          wholeFarmCropItem.setUnseedableAcres(new Double(rounded));
        }
      }
      
      if(reportedQuantityStartExists) {
        double roundedStart = MathUtils.round(reportedQuantityStartSum, three);
        wholeFarmItem.setReportedQuantityStart(new Double(roundedStart));
      }
      if(reportedQuantityEndExists) {
        double roundedEnd = MathUtils.round(reportedQuantityEndSum, three);
        wholeFarmItem.setReportedQuantityEnd(new Double(roundedEnd));
      }
      if(adjQuantityStartExists) {
        double roundedStart = MathUtils.round(adjQuantityStartSum, three);
        wholeFarmItem.setAdjQuantityStart(new Double(roundedStart));
      }
      if(adjQuantityEndExists) {
        double roundedEnd = MathUtils.round(adjQuantityEndSum, three);
        wholeFarmItem.setAdjQuantityEnd(new Double(roundedEnd));
      }

      InventoryAdjustmentUtils.populateFormFromInventoryItem(form, wholeFarmItem, true, true);

      if(wholeFarmItem.getInventoryClassCode().equals(InventoryClassCodes.CROP)) {
        cropItems.add(wholeFarmItem);
      } else if(wholeFarmItem.getInventoryClassCode().equals(InventoryClassCodes.LIVESTOCK)) {
        livestockItems.add(wholeFarmItem);
      }

    }

    Map<String, Double> totalValues = new HashMap<>();
    AccrualCalculator accrualCalc = CalculatorFactory.getAccrualCalculator(scenario);
    Double cropTotal = accrualCalc.calculateTotal(cropItems);
    Double livestockTotal = accrualCalc.calculateTotal(livestockItems);
    totalValues.put(InventoryFormData.getCropType(), cropTotal);
    totalValues.put(InventoryFormData.getLivestockType(), livestockTotal);
    form.setTotalValues(totalValues);
  }


  /**
   * @param scenario Scenario
   * @param schedule String
   * @param farmViewYear String
   * @return List<ProducedItem>
   * @throws Exception On Exception
   */
  private List<ProducedItem> getInventoryItems(
      Scenario scenario,
      String farmViewYear,
      String schedule)
  throws Exception {
    
    int year = Integer.parseInt(farmViewYear);
    List<ProducedItem> items = new ArrayList<>();
    
    List<ReferenceScenario> referenceScenarios;
    // If schedule is null then we are displaying Whole Farm View or Combined Farm View,
    // which will combined multiple operations.
    // If schedule is not null then just display for the one operation for this PIN.
    if(schedule == null) {
      referenceScenarios = scenario.getReferenceScenariosByYear(year);
    } else {
      referenceScenarios = new ArrayList<>();
      ReferenceScenario rs = scenario.getReferenceScenarioByYear(year);
      if(rs != null) {
        referenceScenarios.add(rs);
      }
    }
    
    for(ReferenceScenario refScenario : referenceScenarios) {
      List<FarmingOperation> farmingOperations = refScenario.getFarmingYear().getFarmingOperations();
      for(FarmingOperation fo : farmingOperations) {
        
        if(schedule == null || fo.getSchedule().equals(schedule)) {
          
          if(fo.getProducedItems() != null) {
            items.addAll(fo.getProducedItems());
          }
        
        }
      }
    }
    
    return items;
  }


  /**
   * @param form form
   * @param scenario Scenario
   */
  protected void populateFarmViewYearOptions(InventoryForm form, Scenario scenario) {
    final int size = 6;
    List<ListView> yearOptions = new ArrayList<>(size);
    
    for(ReferenceScenario refScenario : scenario.getAllScenarios()) {
      String year = refScenario.getYear().toString();
      ListView item = new CodeListView(year, year);
      yearOptions.add(item);
    }
    
    Collections.reverse(yearOptions);
    
    form.setFarmViewYearOptions(yearOptions);
  }
  

  /**
   * @param request request
   * @param form form
   * @param scenario Scenario
   * @return boolean
   * @throws Exception On Exception
   */
  @Override
  protected boolean isReadOnly(
      HttpServletRequest request,
      CalculatorForm form,
      Scenario scenario) throws Exception {

    boolean readOnly = super.isReadOnly(request, form, scenario);

    boolean wholeFarmView = form.isWholeFarmView();

    if(!readOnly && !wholeFarmView) {

      InventoryForm accrualsForm = (InventoryForm) form;
      String schedule = form.getFarmView();
      boolean operationExists = false;
      for(ReferenceScenario refScenario : scenario.getAllScenarios()) {
        String year = refScenario.getYear().toString();
        if(year.equals(accrualsForm.getFarmViewYear())) {
          FarmingOperation fo = refScenario.getFarmingYear().getFarmingOperationBySchedule(schedule);
          if(fo != null) {
            operationExists = true;
          }
        }
      }

      if(operationExists) {
        readOnly = false;
      } else {
        readOnly = true;
      }

    } else {
      readOnly = true;
    }
    
    return readOnly;
  }


  protected boolean isMissingSupplementalDates(Scenario scenario, InventoryForm form) {
    boolean missingSuppDates =
        scenario.getYear().toString().equals(form.getFarmViewYear())
        && ScenarioUtils.isMissingSupplementalDates(scenario);
    return missingSuppDates;
  }
  
  
  private void populateOperationsForImport(InventoryForm form, Scenario scenario) throws ServiceException {

    CalculatorService calculatorService = ServiceFactory.getCalculatorService();
    List<FarmingOperationImportOption> operations = calculatorService.readOperationsForInventoryImport(
        scenario.getClient().getParticipantPin(),
        scenario.getYear(),
        scenario.getFarmingYear().getProgramYearVersionNumber());
    
    form.setOperationsForImport(operations);
  }

}
