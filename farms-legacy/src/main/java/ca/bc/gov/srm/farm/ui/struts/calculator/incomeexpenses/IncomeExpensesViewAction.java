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
package ca.bc.gov.srm.farm.ui.struts.calculator.incomeexpenses;

import java.text.DecimalFormat;
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

import ca.bc.gov.srm.farm.calculator.AccrualCalculator;
import ca.bc.gov.srm.farm.calculator.BenefitNullFixer;
import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.calculator.CalculatorFactory;
import ca.bc.gov.srm.farm.calculator.IncomeExpenseCalculator;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorAction;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorForm;
import ca.bc.gov.srm.farm.util.ScenarioUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * @author awilkinson
 * @created Jan 4, 2011
 */
public class IncomeExpensesViewAction extends CalculatorAction {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Viewing Income and Expenses...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    IncomeExpensesForm form = (IncomeExpensesForm) actionForm;
    Scenario scenario = getScenario(form);

    populateForm(form, scenario, true);
    setReadOnlyFlag(request, form, scenario);

    return forward;
  }

  /**
   * Fill the form fields from the scenario
   * @param form The form object to fill.
   * @param scenario The scenario to fill the form from.
   * @param setAdjustedValues if false, do not clear the form and set everything but adjusted values
   * @throws Exception On Exception
   */
  protected void populateForm(IncomeExpensesForm form, Scenario scenario, boolean setAdjustedValues)
  throws Exception {
    
    if(setAdjustedValues) {
      form.clear();
    }
    
    if(StringUtils.isBlank(form.getFinancialViewRadio())) {
      form.setIncomeExpenseRadio("income");
      form.setEligibilityRadio("eligible");
      if(scenario.isUserScenario() || scenario.isBenefitTriageScenario()) {
        form.setFinancialViewRadio("adjusted");
      } else {
        form.setFinancialViewRadio("cra");
      }
    }
    
    populateRequiredYears(form, scenario);
    
    if(setAdjustedValues) {
      form.setScenarioRevisionCount(scenario.getRevisionCount());
    }
    
    syncFarmViewCacheWithForm(form);
    
    if(form.getFarmView().equals(CalculatorForm.FARM_VIEW_WHOLE)) {
      populateFormForWholeFarm(form, scenario);
    } else {
      String schedule = form.getFarmView();
      form.setYearOperationMap(getYearOpMap(schedule, scenario));
      populateFormForSingleOperation(form, scenario, setAdjustedValues);
    }

    populateFarmViewOptions(form, scenario);

    boolean missingStatementAReceivedDates = ScenarioUtils.isMissingStatementAReceivedDates(scenario);
    form.setMissingStatementAReceivedDates(missingStatementAReceivedDates);
  }

  /**
   * @param form form
   * @param scenario Scenario
   * @param setAdjustedValues boolean
   * @throws Exception On Exception
   */
  private void populateFormForSingleOperation(
      IncomeExpensesForm form,
      Scenario scenario,
      boolean setAdjustedValues)
  throws Exception {
    
    String schedule = form.getFarmView();
    int programYear = scenario.getYear().intValue();
    List<IncomeExpense> items = getIncomeExpenses(scenario, schedule);
    
    for(IncomeExpense item : items) {
      String year = item.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear().toString();
      populateFormFromIncomeExpense(form, year, programYear, item, setAdjustedValues);
    }

    BenefitNullFixer nullFixer = CalculatorFactory.getBenefitNullFixer(scenario);
    nullFixer.fixNulls(scenario);

    // Need to calculate accrualAdjsPurchasedInputs for IncomeExpenseCalculator
    AccrualCalculator accrualCalc = CalculatorFactory.getAccrualCalculator(scenario);
    accrualCalc.calculateTotals();

    IncomeExpenseCalculator icCalc = CalculatorFactory.getIncomeExpenseCalculator(scenario);
    
    if(CalculatorConfig.incomeExpensesSummaryDataNotStored(programYear)) {
      icCalc.calculateIncomeExpense();
    }
    
    Map<String, MarginTotal> marginTotalMap = icCalc.getSingleOperationMarginTotalMap(schedule);
    form.setMarginTotalMap(marginTotalMap);
  }


  /**
   * Fill the form fields from the reference scenario
   * @param form The form object to fill.
   * @param scenario The scenario to fill the form from.
   * @throws Exception On Exception
   */
  protected void populateFormForWholeFarm(
      IncomeExpensesForm form,
      Scenario scenario)
  throws Exception {
    final double scale = 2d; // decimal places to round to
    final double scaleMultiplier = Math.pow(10d, scale);
    
    int programYear = scenario.getYear().intValue();
    List<IncomeExpense> items = getIncomeExpenses(scenario, null);
    
    // Map<year, Map<lineKey, List<IncomeExpense>>> 
    Map<String, Map<String, List<IncomeExpense>>> yearLineKeyItemListMap = new HashMap<>(scenario.getAllScenarios().size());
    
    for(IncomeExpense item : items) {

      String lineKey = IncomeExpenseFormData.getLineKey(
          item.getLineItem().getLineItem().toString(),
          item.getIsExpense().booleanValue(),
          item.getLineItem().checkEligible(programYear));
      String year = item.getFarmingOperation().getFarmingYear().getReferenceScenario().getYear().toString();
      
      Map<String, List<IncomeExpense>> lineKeyItemListMap = yearLineKeyItemListMap.get(year);
      if(lineKeyItemListMap == null) {
        lineKeyItemListMap = new HashMap<>();
        yearLineKeyItemListMap.put(year, lineKeyItemListMap);
      }
      
      List<IncomeExpense> itemList = lineKeyItemListMap.get(lineKey);
      if(itemList == null) {
        itemList = new ArrayList<>();
        lineKeyItemListMap.put(lineKey, itemList);
      }
      
      itemList.add(item);
    }
    

    // cycle through the years in chronological order
    // so that the most recent line item description gets set
    for(String year : form.getRequiredYears()) {
      Map<String, List<IncomeExpense>> lineKeyItemListMap = yearLineKeyItemListMap.get(year);
      
      if(lineKeyItemListMap != null) {
        for(String lineKey : lineKeyItemListMap.keySet()) {
          List<IncomeExpense> itemList = lineKeyItemListMap.get(lineKey);
          
          IncomeExpense wholeFarmItem = new IncomeExpense();
          
          double reportedAmountSum = 0d;
          double adjAmountSum = 0d;
          
          boolean reportedExists = false;
          boolean adjExists = false;
          
          boolean firstItem = true;
          for(IncomeExpense item : itemList) {
            FarmingOperation fo = item.getFarmingOperation();
            double partnershipPercent = ScenarioUtils.getPartnershipPercent(fo);
            
            if(firstItem) {
              firstItem = false;
              wholeFarmItem.setLineItem(item.getLineItem());
              wholeFarmItem.setIsExpense(item.getIsExpense());
            }
            
            if(item.getReportedAmount() != null) {
              reportedExists = true;
              reportedAmountSum += item.getReportedAmount().doubleValue() * partnershipPercent;
            }
            if(item.getAdjAmount() != null) {
              adjExists = true;
              adjAmountSum += item.getAdjAmount().doubleValue() * partnershipPercent;
            }
          }
          
          if(reportedExists) {
            // round to the specified number of decimal places
            double roundedAmount = Math.round(reportedAmountSum * scaleMultiplier) / scaleMultiplier;
            wholeFarmItem.setReportedAmount(new Double(roundedAmount));
          }
          if(adjExists) {
            // round to the specified number of decimal places
            double roundedAmount = Math.round(adjAmountSum * scaleMultiplier) / scaleMultiplier;
            wholeFarmItem.setAdjAmount(new Double(roundedAmount));
          }

          populateFormFromIncomeExpense(form, year, programYear, wholeFarmItem, true);
        }
      }

    }

    BenefitNullFixer nullFixer = CalculatorFactory.getBenefitNullFixer(scenario);
    nullFixer.fixNulls(scenario);

    // Need to calculate accrualAdjsPurchasedInputs for IncomeExpenseCalculator
    AccrualCalculator accrualCalc = CalculatorFactory.getAccrualCalculator(scenario);
    accrualCalc.calculateTotals();

    IncomeExpenseCalculator icCalc = CalculatorFactory.getIncomeExpenseCalculator(scenario);
    
    if(CalculatorConfig.incomeExpensesSummaryDataNotStored(programYear)) {
      icCalc.calculateIncomeExpense();
    }
    
    Map<String, MarginTotal> marginTotalMap = icCalc.getWholeFarmMarginTotalMap();
    form.setMarginTotalMap(marginTotalMap);
  }


  /**
   * @param scenario Scenario
   * @param schedule String
   * @return List<IncomeExpense>
   * @throws Exception On Exception
   */
  private List<IncomeExpense> getIncomeExpenses(
      Scenario scenario,
      String schedule)
  throws Exception {
    
    List<IncomeExpense> incomeExpenses = new ArrayList<>();
    
    // cycle through the years in chronological order
    // so that the most recent line item description gets set
    List<Integer> allYears = ScenarioUtils.getAllYears(scenario.getYear());
    for(int year : allYears) {
      
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
        if(refScenario.getFarmingYear() != null && refScenario.getFarmingYear().getFarmingOperations() != null) {
          List<FarmingOperation> farmingOperations = refScenario.getFarmingYear().getFarmingOperations();
          for(FarmingOperation fo : farmingOperations) {
            
            if(schedule == null || fo.getSchedule().equals(schedule)) {
              
              if(fo.getIncomeExpenses() != null) {
                for(IncomeExpense ie : fo.getIncomeExpenses()) {
                  incomeExpenses.add(ie);
                }
              }
            
            }
          }
        }
      }
    }
    
    return incomeExpenses;
  }

  /**
   * Fill the form fields from the reference scenario
   * @param form The form object to fill.
   * @param year String
   * @param programYear programYear
   * @param item IncomeExpense
   * @param setAdjustedValues if false, don not clear the form and set everything but adjusted values
   * @throws Exception On Exception
   */
  protected void populateFormFromIncomeExpense(
      IncomeExpensesForm form,
      String year,
      int programYear,
      IncomeExpense item,
      boolean setAdjustedValues)
  throws Exception {
    DecimalFormat df = new DecimalFormat("#0.00");
    
    IncomeExpenseFormData fd = null;
    String lineCode = item.getLineItem().getLineItem().toString();
    boolean eligible = item.getLineItem().checkEligible(programYear);
    String lineKey = IncomeExpenseFormData.getLineKey(
        lineCode,
        item.getIsExpense().booleanValue(),
        eligible);
    
    fd = form.getIncomeExpense(lineKey);
    fd.setLineCode(lineCode);
    fd.setLineCodeDescription(item.getLineItem().getDescription());
    if(setAdjustedValues) {
      fd.setAdjusted(year, StringUtils.formatDouble(item.getTotalAmount(), df));
    }
    fd.setExpense(item.getIsExpense());
    fd.setEligible(Boolean.valueOf(eligible));

    fd.setAdjustmentId(year, item.getAdjIncomeExpenseId());
    fd.setAdjustment(year, StringUtils.formatDouble(item.getAdjAmount(), df));
    fd.setAdjustmentUser(year, StringUtils.formatUserIdForDisplay(item.getAdjustedByUserId()));
    fd.setRevisionCount(year, item.getRevisionCount());

    fd.setReportedId(year, item.getReportedIncomeExpenseId());
    fd.setCra(year, StringUtils.formatDouble(item.getReportedAmount(), df));
    
    if( ! item.getLineItem().getProgramYear().equals(new Integer(form.getYear())) ) {
      fd.addToAdjustedFiveYearTotal(item.getTotalAmount());
      fd.addToAdjustmentFiveYearTotal(item.getAdjAmount());
      fd.addToCraFiveYearTotal(item.getReportedAmount());
    }
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
      readOnly = false;
    } else {
      readOnly = true;
    }
    
    return readOnly;
  }
  
  protected boolean isMissingStatementAReceivedDates(Scenario scenario) {

    return ScenarioUtils.isMissingStatementAReceivedDates(scenario);
  }

}
