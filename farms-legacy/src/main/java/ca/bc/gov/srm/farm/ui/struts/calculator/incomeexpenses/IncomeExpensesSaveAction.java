/**
 *
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
import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.LineItem;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.exception.LineItemNotFoundException;
import ca.bc.gov.srm.farm.service.AdjustmentService;
import ca.bc.gov.srm.farm.service.BenefitService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.calculator.AdjustmentGridForm;
import ca.bc.gov.srm.farm.ui.struts.calculator.AdjustmentGridItemData;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.DataParseUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * @author awilkinson
 * @created Jan 4, 2011
 */
public class IncomeExpensesSaveAction extends IncomeExpensesViewAction {
  
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

    logger.debug("Saving Income Expenses...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    IncomeExpensesForm form = (IncomeExpensesForm) actionForm;
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
  
      Map<String, List<IncomeExpense>> adjustmentsMap = parseAdjustments(form, yearOpMap, minValue, maxValue, errors);
  
      checkLocalStatementAReceivedDate(scenario, errors);
      
      saveErrors(request, errors);
  
      if(errors.isEmpty()) {
  
        try {
          AdjustmentService adjService = ServiceFactory.getAdjustmentService();
          
          adjService.adjustIncomeExpenses(scenario, adjustmentsMap, getUserId());

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
        } catch(LineItemNotFoundException linfe) {
          ActionMessages errorMessages = new ActionMessages();
          errorMessages.add("", new ActionMessage(linfe.getMessage(), false));
          saveErrors(request, errorMessages);
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
  private void checkForDuplicates(IncomeExpensesForm form, ActionMessages errors) {

    final String incomeErrorKey = MessageConstants.ERRORS_DUPLICATE_INCOME_LINE_ITEM;
    final String expenseErrorKey = MessageConstants.ERRORS_DUPLICATE_EXPENSE_LINE_ITEM;
    final String eligible = "Eligible";
    final String ineligible = "Ineligible";

    Set<String> incomeEligItems = new HashSet<>();
    Set<String> incomeIneligItems = new HashSet<>();
    Set<String> expenseEligItems = new HashSet<>();
    Set<String> expenseIneligItems = new HashSet<>();

    for(AdjustmentGridItemData agid : form.getItems().values()) {
      IncomeExpenseFormData fd = (IncomeExpenseFormData) agid;
      String lineCode = fd.getLineCode();
      String eligibility;
      if(fd.getEligible().booleanValue()) {
        eligibility = eligible;
      } else {
        eligibility = ineligible;
      }
      if(fd.getExpense().booleanValue()) {
        if(fd.getEligible().booleanValue()) {
          if(expenseEligItems.contains(lineCode)) {
            errors.add("", new ActionMessage(expenseErrorKey, lineCode, eligibility));
          } else if( ! StringUtils.isBlank(lineCode) ) {
            expenseEligItems.add(lineCode);
          }
        } else {
          if(expenseIneligItems.contains(lineCode)) {
            errors.add("", new ActionMessage(expenseErrorKey, lineCode, eligibility));
          } else if( ! StringUtils.isBlank(lineCode) ) {
            expenseIneligItems.add(lineCode);
          }
        }
      } else {
        if(fd.getEligible().booleanValue()) {
          if(incomeEligItems.contains(lineCode)) {
            errors.add("", new ActionMessage(incomeErrorKey, lineCode, eligibility));
          } else if( ! StringUtils.isBlank(lineCode) ) {
            incomeEligItems.add(lineCode);
          }
        } else {
          if(incomeIneligItems.contains(lineCode)) {
            errors.add("", new ActionMessage(incomeErrorKey, lineCode, eligibility));
          } else if( ! StringUtils.isBlank(lineCode) ) {
            incomeIneligItems.add(lineCode);
          }
        }
      }
    }
  }
  

  /**
   * This method is an exact duplicate of that in ProductiveUnitsSaveAction.
   * Refactoring to share this method was going to be messy and not worth the reward.
   * If a change is made to this method. It likely should be made in both classes.
   * @param form AdjustmentGridForm
   * @param yearOpMap Map
   * @param minValue double
   * @param maxValue double
   * @param errors ActionMessages
   * @return Map<String action, List<IncomeExpense>>
   * @throws Exception On Exception
   */
  public Map<String, List<IncomeExpense>> parseAdjustments(
      AdjustmentGridForm form,
      Map<String, FarmingOperation> yearOpMap,
      double minValue,
      double maxValue,
      ActionMessages errors)
  throws Exception {

    Map<String, List<IncomeExpense>> adjustmentsMap = new HashMap<>();
    
    String errorKey = MessageConstants.ERRORS_ADJUSTED_PUC_VALUE;
    boolean blankLineCode = false;
    
    for(AdjustmentGridItemData fd : form.getItems().values()) {
      for(String year : fd.getAdjustedValues().keySet()) {
        FarmingOperation farmingOperation = yearOpMap.get(year);

        String totalString = fd.getAdjusted(year);
        boolean badValue = false;
        Double total = null;
        if(!StringUtils.isBlank(totalString)) {
          try {
            total = Double.valueOf(totalString);
            if(total.doubleValue() < minValue || total.doubleValue() > maxValue) {
               badValue = true;
            }
          } catch(NumberFormatException nfe) {
            badValue = true;
          }
          if(StringUtils.isBlank(fd.getLineCode())) {
            blankLineCode = true;
          }
        } else if(fd.getReportedId(year) != null
            || (fd.getAdjustmentId(year) != null && !fd.isAdjustmentDeleted(year))) {
          badValue = true;
        }
        
        if(badValue) {
          errors.add("", new ActionMessage(errorKey, year, fd.getLineCode()));
          fd.setError(year, Boolean.TRUE);
        } else if( (! StringUtils.isBlank(totalString) || fd.isAdjustmentDeleted(year))
            && ! AdjustmentGridItemData.TYPE_NEW.equals(fd.getType()) ) {
          
          Double reported = DataParseUtils.parseDoubleObject(fd.getCra(year));
          Double prevAdjustment = DataParseUtils.parseDoubleObject(fd.getAdjustment(year));
          Double adjustment = null;
          if( ! StringUtils.isBlank(totalString) ) {
            if(reported == null) {
              adjustment = total;
            } else {
              Objects.requireNonNull(total); // prevent "potential null-pointer access" compiler warning
              adjustment = new Double(total.doubleValue() - reported.doubleValue());
            }
            Objects.requireNonNull(adjustment); // prevent "potential null-pointer access" compiler warning
            if(adjustment.equals(prevAdjustment)) {
              adjustment = null;
            }
          }
          
          String action = null;
          if(fd.isNew()
              || (prevAdjustment == null && reported == null)
              || (prevAdjustment == null && reported != null && !reported.equals(total))) {
            action = AdjustmentService.ACTION_ADD;
          } else if(fd.isAdjustmentDeleted(year)
              && (StringUtils.isBlank(totalString)
                  || (reported != null && reported.equals(total)))) {
            action = AdjustmentService.ACTION_DELETE;
          } else if(prevAdjustment != null && adjustment != null) {
            action = AdjustmentService.ACTION_UPDATE;
          }
          
          if(action != null) {
            addAdjustment(adjustmentsMap, fd, year, adjustment, action, farmingOperation);
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
   * @param gridItemData AdjustmentGridItemData
   * @param year String
   * @param adjustmentValue Double
   * @param action String
   * @param farmingOperation FarmingOperation
   * @throws Exception On Exception
   */
  private void addAdjustment(
      Map<String, List<IncomeExpense>> adjustmentsMap,
      AdjustmentGridItemData gridItemData,
      String year,
      Double adjustmentValue,
      String action,
      FarmingOperation farmingOperation)
  throws Exception {
    IncomeExpenseFormData fd = (IncomeExpenseFormData) gridItemData;
    IncomeExpense ie = new IncomeExpense();
    ie.setFarmingOperation(farmingOperation);
    ie.setAdjAmount(adjustmentValue);

    LineItem lineItem = new LineItem();
    lineItem.setLineItem(DataParseUtils.parseIntegerObject(fd.getLineCode()));
    lineItem.setIsEligible(fd.getEligible());
    ie.setLineItem(lineItem);
    ie.setIsExpense(fd.getExpense());
    ie.setAdjIncomeExpenseId(fd.getAdjustmentId(year));
    ie.setReportedIncomeExpenseId(fd.getReportedId(year));
    ie.setRevisionCount(fd.getRevisionCount(year));

    List<IncomeExpense> adjustmentList = adjustmentsMap.get(action);
    if(adjustmentList == null) {
      adjustmentList = new ArrayList<>();
      adjustmentsMap.put(action, adjustmentList);
    }
    adjustmentList.add(ie);
  }
  
  private void checkLocalStatementAReceivedDate(Scenario scenario, ActionMessages errors) {

    boolean missingStatementAReceivedDates = isMissingStatementAReceivedDates(scenario);
    if(missingStatementAReceivedDates) {
      errors.add("", new ActionMessage(MessageConstants.ERRORS_PROVINCIAL_STATEMENT_A_RECEIVED_DATE_ADJUSTMENT_SCREENS));
    }

  }

}
