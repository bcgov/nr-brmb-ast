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
package ca.bc.gov.srm.farm.ui.struts.calculator.accruals;

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
import ca.bc.gov.srm.farm.calculator.BenefitNullFixer;
import ca.bc.gov.srm.farm.calculator.CalculatorFactory;
import ca.bc.gov.srm.farm.calculator.InventoryCalculator;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingOperationImportOption;
import ca.bc.gov.srm.farm.domain.InventoryItem;
import ca.bc.gov.srm.farm.domain.Margin;
import ca.bc.gov.srm.farm.domain.MarginTotal;
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
import ca.bc.gov.srm.farm.util.AccrualsAdjustmentUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * @author awilkinson
 * @created Feb 22, 2011
 */
public class AccrualsViewAction extends CalculatorAction {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Viewing Accruals...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    AccrualsForm form = (AccrualsForm) actionForm;
    Scenario scenario = getScenario(form);

    populateForm(form, scenario, true);
    setReadOnlyFlag(request, form, scenario);

    if( scenario.getScenarioStateCode().equals(ScenarioStateCodes.IN_PROGRESS) ) {
      ActionMessages warnings = getDuplicateWarnings(scenario);
      if(!warnings.isEmpty()) {
        saveMessages(request, warnings);
      }
    }

    return forward;
  }
  
  
  protected ActionMessages getDuplicateWarnings(Scenario scenario) {
    ActionMessages warnings = new ActionMessages();
    InventoryCalculator invCalc = CalculatorFactory.getInventoryCalculator();
    Set<String> duplicateCodes = new TreeSet<>();
    
    for(ReferenceScenario refScenario : scenario.getAllScenarios()) {
      for(FarmingOperation fo : refScenario.getFarmingYear().getFarmingOperations()) {
        for(InventoryItem item : fo.getAccrualItems()) {
          
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
      String errorKey = MessageConstants.WARNING_ACCRUAL_DUPLICATES;
      
      warnings.add("", new ActionMessage(errorKey, codeCsv));
    }
    
    return warnings;
  }

  /**
   * Fill the form fields from the scenario
   * @param form The form object to fill.
   * @param scenario The scenario to fill the form from.
   * @param setAdjustedValues if false, don not clear the form and set everything but adjusted values
   * @throws Exception On Exception
   */
  protected void populateForm(
      AccrualsForm form,
      Scenario scenario,
      boolean setAdjustedValues)
  throws Exception {
    
    if(setAdjustedValues) {
      form.clear();
    }
    
    if(StringUtils.isBlank(form.getFinancialViewRadio())) {
      form.setItemTypeRadio("input");
      form.setEligibilityRadio(Boolean.TRUE.toString());
      if(scenario.isUserScenario() || scenario.isBenefitTriageScenario()) {
        form.setFinancialViewRadio("adjusted");
      } else {
        form.setFinancialViewRadio("cra");
      }
    }
    
    if(setAdjustedValues) {
      form.setScenarioRevisionCount(scenario.getRevisionCount());
    }
    
    syncFarmViewCacheWithForm(form);
    
    BenefitNullFixer nullFixer = CalculatorFactory.getBenefitNullFixer(scenario);
    nullFixer.fixNulls(scenario);

    // calculate accruals for display of "Total Change..." 
    AccrualCalculator accrualCalc = CalculatorFactory.getAccrualCalculator(scenario);
    accrualCalc.calculateTotals();

    if(form.getFarmViewYear() == null) {
      form.setFarmViewYear(scenario.getYear().toString());
    }
    
    if(form.getFarmViewYear().equals(scenario.getYear().toString())) {
      form.setProgramYear(true);
    } else {
      form.setProgramYear(false);
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


  private void populateFormForSingleOperation(
      AccrualsForm form,
      Scenario scenario,
      boolean setAdjustedValues)
  throws Exception {
    
    String schedule = form.getFarmView();
    ReferenceScenario refScenario = getReferenceScenario(scenario, form.getFarmViewYear());
    if(refScenario != null) {
      FarmingOperation fo = refScenario.getFarmingYear().getFarmingOperationBySchedule(schedule);
      if(fo != null) {
        List<InventoryItem> items = fo.getAccrualItems();
        
        for(InventoryItem item : items ) {
          AccrualsAdjustmentUtils.populateFormFromInventoryItem(form, item, setAdjustedValues);
        }

        Map<String, Double> totalValues = new HashMap<>();
        Margin margin = fo.getMargin();
        Double inputTotal = margin.getAccrualAdjsPurchasedInputs();
        Double receivableTotal = margin.getAccrualAdjsReceivables();
        Double payableTotal = margin.getAccrualAdjsPayables();
        totalValues.put(AccrualFormData.getInputType(), inputTotal);
        totalValues.put(AccrualFormData.getReceivableType(), receivableTotal);
        totalValues.put(AccrualFormData.getPayableType(), payableTotal);
        form.setTotalValues(totalValues);
        form.setDeferredProgramPayments(margin.getDeferredProgramPayments());
      }
    }
  }


  /**
   * Fill the form fields from the reference scenario
   * @param form The form object to fill.
   * @param scenario The scenario to fill the form from.
   * @throws Exception On Exception
   */
  protected void populateFormForWholeFarm(
      AccrualsForm form,
      Scenario scenario)
  throws Exception {
    
    final double scale = 2d; // decimal places to round to
    final double scaleMultiplier = Math.pow(10d, scale);

    List<InventoryItem> items = getAccrualItemsForWholeFarm(scenario, form.getFarmViewYear(), null);
    List<InventoryItem> inputItems = new ArrayList<>();
    List<InventoryItem> receivableItems = new ArrayList<>();
    List<InventoryItem> payableItems = new ArrayList<>();
    
    Map<String, List<InventoryItem>> lineKeyItemListMap = new HashMap<>();
    
    for(InventoryItem item : items) {
      String lineKey =
        AccrualFormData.getAccrualKey(item.getInventoryItemCode(), item.getInventoryClassCode());
      
      List<InventoryItem> itemList = lineKeyItemListMap.get(lineKey);
      if(itemList == null) {
        itemList = new ArrayList<>(2); // most will be one or two
        lineKeyItemListMap.put(lineKey, itemList);
      }
      
      itemList.add(item);
    }
    

    for(String lineKey : lineKeyItemListMap.keySet()) {
      List<InventoryItem> itemList = lineKeyItemListMap.get(lineKey);
      
      InventoryItem firstItem = itemList.get(0);
      InventoryItem wholeFarmItem = firstItem.getClass().newInstance();
      wholeFarmItem.setInventoryItemCode(firstItem.getInventoryItemCode());
      wholeFarmItem.setInventoryItemCodeDescription(firstItem.getInventoryItemCodeDescription());
      wholeFarmItem.setInventoryClassCode(firstItem.getInventoryClassCode());
      wholeFarmItem.setInventoryClassCodeDescription(firstItem.getInventoryClassCodeDescription());
      wholeFarmItem.setInventoryGroupCode(firstItem.getInventoryGroupCode());
      wholeFarmItem.setInventoryClassCodeDescription(firstItem.getInventoryGroupCodeDescription());
      wholeFarmItem.setIsEligible(firstItem.getIsEligible());
      
      double reportedStartSum = 0d;
      double reportedEndSum = 0d;
      double adjStartSum = 0d;
      double adjEndSum = 0d;
      
      boolean reportedStartExists = false;
      boolean reportedEndExists = false;
      boolean adjStartExists = false;
      boolean adjEndExists = false;
      
      for(InventoryItem item : itemList) {
        FarmingOperation fo = item.getFarmingOperation();
        double partnershipPercent = ScenarioUtils.getPartnershipPercent(fo);
        
        if(item.getReportedStartOfYearAmount() != null) {
          reportedStartExists = true;
          reportedStartSum += item.getReportedStartOfYearAmount().doubleValue() * partnershipPercent;
        }
        if(item.getReportedEndOfYearAmount() != null) {
          reportedEndExists = true;
          reportedEndSum += item.getReportedEndOfYearAmount().doubleValue() * partnershipPercent;
        }
        if(item.getAdjStartOfYearAmount() != null) {
          adjStartExists = true;
          adjStartSum += item.getAdjStartOfYearAmount().doubleValue() * partnershipPercent;
        }
        if(item.getAdjEndOfYearAmount() != null) {
          adjEndExists = true;
          adjEndSum += item.getAdjEndOfYearAmount().doubleValue() * partnershipPercent;
        }
      }
      
      if(reportedStartExists) {
        // round to the specified number of decimal places
        double roundedStartAmount = Math.round(reportedStartSum * scaleMultiplier) / scaleMultiplier;
        wholeFarmItem.setReportedStartOfYearAmount(new Double(roundedStartAmount));
      }
      if(reportedEndExists) {
        // round to the specified number of decimal places
        double roundedEndAmount = Math.round(reportedEndSum * scaleMultiplier) / scaleMultiplier;
        wholeFarmItem.setReportedEndOfYearAmount(new Double(roundedEndAmount));
      }
      if(adjStartExists) {
        // round to the specified number of decimal places
        double roundedStartAmount = Math.round(adjStartSum * scaleMultiplier) / scaleMultiplier;
        wholeFarmItem.setAdjStartOfYearAmount(new Double(roundedStartAmount));
      }
      if(adjEndExists) {
        // round to the specified number of decimal places
        double roundedEndAmount = Math.round(adjEndSum * scaleMultiplier) / scaleMultiplier;
        wholeFarmItem.setAdjEndOfYearAmount(new Double(roundedEndAmount));
      }

      AccrualsAdjustmentUtils.populateFormFromInventoryItem(form, wholeFarmItem, true);

      if(wholeFarmItem.getInventoryClassCode().equals(InventoryClassCodes.INPUT)) {
        inputItems.add(wholeFarmItem);
      } else if(wholeFarmItem.getInventoryClassCode().equals(InventoryClassCodes.RECEIVABLE)) {
        receivableItems.add(wholeFarmItem);
      } else if(wholeFarmItem.getInventoryClassCode().equals(InventoryClassCodes.PAYABLE)) {
        payableItems.add(wholeFarmItem);
      }

    }

    ReferenceScenario refScenario = getReferenceScenario(scenario, form.getFarmViewYear());
    if(refScenario != null) {
      MarginTotal marginTotal = refScenario.getFarmingYear().getMarginTotal();
      Double inputTotal = marginTotal.getAccrualAdjsPurchasedInputs();
      Double receivableTotal = marginTotal.getAccrualAdjsReceivables();
      Double payableTotal = marginTotal.getAccrualAdjsPayables();
      Map<String, Double> totalValues = new HashMap<>();
      totalValues.put(AccrualFormData.getInputType(), inputTotal);
      totalValues.put(AccrualFormData.getReceivableType(), receivableTotal);
      totalValues.put(AccrualFormData.getPayableType(), payableTotal);
      form.setTotalValues(totalValues);
      form.setDeferredProgramPayments(marginTotal.getDeferredProgramPayments());
    }
  }
  
  
  /**
   * @param scenario Scenario
   * @param farmViewYear String
   * @return ReferenceScenario
   * @throws Exception On Exception
   */
  private ReferenceScenario getReferenceScenario(
      Scenario scenario,
      String farmViewYear)
  throws Exception {
    
    ReferenceScenario result = null;
    
    for(ReferenceScenario refScenario : scenario.getAllScenarios()) {
      String year = refScenario.getYear().toString();
      if(year.equals(farmViewYear)) {
        result = refScenario;
        break;
      }
    }
    
    return result;
  }
  
  
  /**
   * @param scenario Scenario
   * @param farmViewYear String
   * @param schedule String
   * @return List<InventoryItem>
   * @throws Exception On Exception
   */
  private List<InventoryItem> getAccrualItemsForWholeFarm(
      Scenario scenario,
      String farmViewYear,
      String schedule)
  throws Exception {
    
    int year = Integer.parseInt(farmViewYear);
    List<InventoryItem> accruals = new ArrayList<>();
    
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
          
          if(fo.getAccrualItems() != null) {
            accruals.addAll(fo.getAccrualItems());
          }
          
        }
      }
    }
    
    return accruals;
  }


  /**
   * @param form form
   * @param scenario Scenario
   */
  protected void populateFarmViewYearOptions(AccrualsForm form, Scenario scenario) {
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

      AccrualsForm accrualsForm = (AccrualsForm) form;
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

  protected boolean isMissingSupplementalDates(Scenario scenario, AccrualsForm form) {
    boolean checkSupplementalData =
        scenario.getYear().toString().equals(form.getFarmViewYear())
        && ScenarioUtils.isMissingSupplementalDates(scenario);
    return checkSupplementalData;
  }

  
  private void populateOperationsForImport(AccrualsForm form, Scenario scenario) throws ServiceException {

    CalculatorService calculatorService = ServiceFactory.getCalculatorService();
    List<FarmingOperationImportOption> operations = calculatorService.readOperationsForAccrualImport(
        scenario.getClient().getParticipantPin(),
        scenario.getYear(),
        scenario.getFarmingYear().getProgramYearVersionNumber());
    
    form.setOperationsForImport(operations);
  }
}
