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
package ca.bc.gov.srm.farm.ui.struts.calculator.farmingoperation;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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
import ca.bc.gov.srm.farm.domain.ProductionInsurance;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.list.CodeListView;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorAction;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorForm;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.DateUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * 
 * @author awilkinson
 *
 */
public class FarmingOperationViewAction extends CalculatorAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Viewing Operation...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    FarmingOperationForm form = (FarmingOperationForm) actionForm;
    Scenario scenario = getScenario(form);
    
    setReadOnlyFlag(request, form, scenario);
    populateScheduleOptions(form, scenario);
    populateForm(form, scenario, request);

    return forward;
  }

  /**
   * Fill the form fields from the account
   * @param form The form object to fill.
   * @param scenario The scenario to fill the form from.
   * @param request 
   * @throws Exception On Exception
   */
  protected void populateForm(FarmingOperationForm form, Scenario scenario, HttpServletRequest request)
  throws Exception {

    form.setScenarioRevisionCount(scenario.getRevisionCount());

    String schedule = getOperationSchedule(form, scenario);
    
    FarmingOperation fo = scenario.getFarmingYear().getFarmingOperationBySchedule(schedule);
    
    if(fo != null) {

      // Integers
      form.setOperationNumber(StringUtils.toString(fo.getOperationNumber()));
      form.setPartnershipPin(StringUtils.toString(fo.getPartnershipPin()));
      form.setOperationRevisionCount(StringUtils.toString(fo.getRevisionCount()));
  
      // Doubles
      form.setBusinessUseHomeExpense(StringUtils.toString(fo.getBusinessUseHomeExpense()));
      form.setExpenses(StringUtils.toString(fo.getFarmingExpenses()));
      form.setGrossIncome(StringUtils.toString(fo.getGrossIncome()));
      form.setInventoryAdjustments(StringUtils.toString(fo.getInventoryAdjustments()));
      form.setNetFarmIncome(StringUtils.toString(fo.getNetFarmIncome()));
      form.setNetIncomeAfterAdj(StringUtils.toString(fo.getNetIncomeAfterAdj()));
      form.setNetIncomeBeforeAdj(StringUtils.toString(fo.getNetIncomeBeforeAdj()));
      form.setOtherDeductions(StringUtils.toString(fo.getOtherDeductions()));
  
      // Dates
      form.setFiscalYearStart(DateUtils.formatDateForDatePicker(fo.getFiscalYearStart()));
      form.setFiscalYearEnd(DateUtils.formatDateForDatePicker(fo.getFiscalYearEnd()));
  
      // Strings
      form.setAgristabAccountingCode(fo.getAccountingCode());
      form.setPartnershipName(fo.getPartnershipName());
      
      // Boolean Indicators
      form.setCropDisaster(fo.getIsCropDisaster().booleanValue());
      form.setCropShare(fo.getIsCropShare().booleanValue());
      form.setFeederMember(fo.getIsFeederMember().booleanValue());
      form.setLandlord(fo.getIsLandlord().booleanValue());
      form.setLivestockDisaster(fo.getIsLivestockDisaster().booleanValue());
      form.setLocallyGenerated(fo.getIsLocallyGenerated().booleanValue());
      
      // Tip Report details
      form.setTipReportDocId(fo.getTipReportDocId());
  
      // Special logic for the following fields
  
      if(fo.getPartnershipPercent() == null) {
        form.setPartnershipPercent(null);
      } else {
        Double partnershipPercent = fo.getPartnershipPercent() * 100;
        DecimalFormat twoDecimalPlacesFormat = new DecimalFormat("#0.00");
        form.setPartnershipPercent(StringUtils.formatDouble(partnershipPercent, twoDecimalPlacesFormat));
      }
      
      List<ProductionInsurance> pi = fo.getProductionInsurances();
      if(pi != null) {
        populateProductionInsurances(form, pi);
      }
  
      boolean pyvHasVerifiedScenario = getPyvHasVerifiedScenario(scenario);
      form.setPyvHasVerifiedScenario(pyvHasVerifiedScenario);
      
      boolean programYearVersionHasVerifiedRealBenefit = ScenarioUtils.programYearVersionHasVerifiedBenefit(scenario);
      if(programYearVersionHasVerifiedRealBenefit && form.isCanModifyScenario()) {
        ActionMessages messages = new ActionMessages();
        messages.add("", new ActionMessage(MessageConstants.ERRORS_FARMING_OPERATION_SAVE_VERSION_HAS_VERIFIED_BENEFIT));
        request.setAttribute("infoMessages", messages);
      }
    }
  }

  /**
   * @param scenario scenario
   * @return boolean
   * @throws Exception On Exception
   */
  protected boolean getPyvHasVerifiedScenario(Scenario scenario) throws Exception {
    CalculatorService calculatorService = ServiceFactory.getCalculatorService();
    boolean pyvHasVerifiedScenario = false;
    if(scenario.getFarmingYear() != null) {
      Integer programYearVersionId = scenario.getFarmingYear().getProgramYearVersionId();
      pyvHasVerifiedScenario = calculatorService.programYearVersionHasVerifiedScenario(programYearVersionId);
    }
    return pyvHasVerifiedScenario;
  }

  /**
   * @param form FarmingOperationForm
   * @param scenario scenario
   * @return String
   */
  protected String getOperationSchedule(FarmingOperationForm form, Scenario scenario) {
    String schedule = form.getSchedule();
    FarmingOperation fo = scenario.getFarmingYear().getFarmingOperationBySchedule(schedule);
    if(fo == null) {
      List<FarmingOperation> farmingOperations = scenario.getFarmingYear().getFarmingOperations();
      if(farmingOperations != null && !farmingOperations.isEmpty()) {
        FarmingOperation firstFo = farmingOperations.get(0);
        schedule = firstFo.getSchedule();
        form.setSchedule(schedule);
      }
    }
    return schedule;
  }

  /**
   * @param form FarmingOperationForm
   * @param pi List
   */
  private void populateProductionInsurances(FarmingOperationForm form, List<ProductionInsurance> pi) {

    form.setProductionInsuranceNumber1(null);
    form.setProductionInsuranceNumber2(null);
    form.setProductionInsuranceNumber3(null);
    form.setProductionInsuranceNumber4(null);

    for(int i = 0; i < pi.size(); i++) {

      final int first = 0;
      ProductionInsurance curPI = pi.get(i);
      if(i == first && curPI != null) {
        form.setProductionInsuranceNumber1(curPI.getProductionInsuranceNumber());
      }
      
      final int second = 1;
      if(i == second && curPI != null) {
        form.setProductionInsuranceNumber2(curPI.getProductionInsuranceNumber());
      }
      
      final int third = 2;
      if(i == third && curPI != null) {
        form.setProductionInsuranceNumber3(curPI.getProductionInsuranceNumber());
      }
      
      final int fourth = 3;
      if(i == fourth && curPI != null) {
        form.setProductionInsuranceNumber4(curPI.getProductionInsuranceNumber());
      }
    }
  }

  
  /**
   * @param form form
   * @param scenario scenario
   */
  protected void populateScheduleOptions(FarmingOperationForm form, Scenario scenario) {
    
    List<ListView> options = new ArrayList<>();

    if(scenario.getFarmingYear() != null && scenario.getFarmingYear().getFarmingOperations() != null) {
      for(FarmingOperation fo : scenario.getFarmingYear().getFarmingOperations()) {
        String schedule = fo.getSchedule();
        ListView item = new CodeListView(schedule, "Operation " + schedule);
        options.add(item);
      }
    }
    
    form.setScheduleOptions(options);
  }


  @Override
  protected boolean isReadOnly(
      HttpServletRequest request,
      CalculatorForm form,
      Scenario scenario) throws Exception {

    boolean programYearVersionHasVerifiedRealBenefit = ScenarioUtils.programYearVersionHasVerifiedBenefit(scenario);
    boolean result = !canModifyScenario(request, form, scenario)
        || programYearVersionHasVerifiedRealBenefit;
    
    return result;
  }

}
