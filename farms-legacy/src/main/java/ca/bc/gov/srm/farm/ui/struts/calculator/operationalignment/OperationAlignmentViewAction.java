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
package ca.bc.gov.srm.farm.ui.struts.calculator.operationalignment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.LineItem;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorAction;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.ScenarioUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * @author awilkinson
 * @created Feb 16, 2011
 */
public class OperationAlignmentViewAction extends CalculatorAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Viewing Operation Alignment...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    OperationAlignmentForm form = (OperationAlignmentForm) actionForm;
    Scenario scenario = getScenario(form);
    
    setReadOnlyFlag(request, form, scenario);
    populateForm(form, scenario, true, request);

    return forward;
  }

  /**
   * Fill the form fields from the account
   * @param form The form object to fill.
   * @param scenario The scenario to fill the form from.
   * @param setOpNumbers boolean
   * @param request 
   */
  protected void populateForm(OperationAlignmentForm form, Scenario scenario, boolean setOpNumbers, HttpServletRequest request) {

    form.setScenarioRevisionCount(scenario.getRevisionCount());

    populateYearScenarioMap(form, scenario);
    Map<Integer, ReferenceScenario> yearScenarioMap = form.getYearScenarioMap();
    
    // List<Integer>
    List<Integer> requiredYears = getRequiredYears(scenario);

    populateRequiredYears(form, scenario);
    
    String maxSchedule = "A";
    List<OperationDetailFormData> operationDetails = new ArrayList<>();
    for(Integer year : requiredYears) {
      // get OpYearFormData or create it if it doesn't exist
      OpYearFormData opYearFormData = form.getOpYearFormData(year.toString());
      Map<String, String> scheduleOpNumMap = opYearFormData.getScheduleOpNumMap();
      ReferenceScenario rs = yearScenarioMap.get(year);
      if(rs != null && rs.getFarmingYear() != null && rs.getFarmingYear().getFarmingOperations() != null) {
        for(FarmingOperation fo : rs.getFarmingYear().getFarmingOperations()) {
          Integer operationNumber = fo.getOperationNumber();
          String schedule = fo.getSchedule();
          if(setOpNumbers) {
            scheduleOpNumMap.put(schedule, operationNumber.toString());
          }
          operationDetails.add(getOperationDetailFormData(fo));
          if(schedule.compareTo(maxSchedule) > 0) {
            maxSchedule = schedule;
          }
        }
      }
    }
    form.setOperationDetails(operationDetails);

    setSchedules(form, maxSchedule);

    boolean programYearVersionHasVerifiedBenefit = ScenarioUtils.programYearVersionHasVerifiedBenefit(scenario);

    if(programYearVersionHasVerifiedBenefit && form.isCanModifyScenario()) {
      ActionMessages messages = new ActionMessages();
      messages.add("", new ActionMessage(MessageConstants.ERRORS_OPERATION_ALIGNMENT_SAVE_VERSION_HAS_VERIFIED_BENEFIT));
      request.setAttribute("infoMessages", messages);
    }
  }


  /**
   * @param form form
   * @param maxSchedule String
   */
  private void setSchedules(OperationAlignmentForm form, String maxSchedule) {
    final String[] scheduleArray = {
        "A", "B", "C", "D", "E", "F", "G", "H", "I",
        "J", "K", "L", "M", "N", "O", "P", "Q", "R",
        "S", "T", "U", "V", "W", "X", "Y", "Z"
    };

    List<String> schedules = Arrays.asList(scheduleArray);
    int lastScheduleIndex = schedules.indexOf(maxSchedule);
    if(lastScheduleIndex >= 0) {
      schedules = schedules.subList(0, lastScheduleIndex + 1);
    }
    form.setSchedules(schedules);
  }


  /**
   * @param fo FarmingOperation
   * @return OperationDetailFormData
   */
  private OperationDetailFormData getOperationDetailFormData(FarmingOperation fo) {
    OperationDetailFormData detail = new OperationDetailFormData();
    
    ReferenceScenario refScenario = fo.getFarmingYear().getReferenceScenario();
    Integer year = refScenario.getYear();

    detail.setYear(year.toString());
    detail.setOperationNumber(fo.getOperationNumber().toString());
    detail.setPartnershipName(fo.getPartnershipName());
    detail.setPartnershipPin(fo.getPartnershipPin().toString());
    
    if(fo.getPartnershipPercent() == null) {
      detail.setPartnershipPercent(null);
    } else {
      final int hundred = 100;
      Double partnershipPercent = new Double(fo.getPartnershipPercent().doubleValue() * hundred);
      DecimalFormat df = new DecimalFormat("#0.00");
      detail.setPartnershipPercent(StringUtils.formatDouble(partnershipPercent, df));
    }
    
    List<IncomeExpense> incomes = new ArrayList<>();
    if( fo.getIncomeExpenses() != null) {
      for(IncomeExpense ie : fo.getIncomeExpenses()) {
        if( ! ie.getIsExpense().booleanValue() ) {
          incomes.add(ie);
        }
      }
    }
    
    Collections.sort(incomes, new Comparator<IncomeExpense>() {
      // sort in descending order (largest first)
      @Override
      public int compare(IncomeExpense ie1, IncomeExpense ie2) {
        return ie2.getTotalAmount().compareTo(ie1.getTotalAmount());
      }
      
    });
    
    final int numTopIncomes = 5;
    String[] topIncome =  new String[numTopIncomes];

    int count = 0;
    for(IncomeExpense income : incomes) {
      LineItem lineItem = income.getLineItem();
      topIncome[count] = lineItem.getLineItem() + " - " + lineItem.getDescription();

      count++;
      if(count >= numTopIncomes) {
        break;
      }
    }
    detail.setTopIncome(topIncome);

    return detail;
  }

}
