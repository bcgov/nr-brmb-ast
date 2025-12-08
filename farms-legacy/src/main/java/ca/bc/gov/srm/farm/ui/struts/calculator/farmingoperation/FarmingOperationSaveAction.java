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
import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.ProductionInsurance;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.service.BenefitService;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.DataParseUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * 
 * @author awilkinson
 */
public class FarmingOperationSaveAction extends FarmingOperationViewAction {

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

    logger.debug("Saving Farming Operation...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    FarmingOperationForm form = (FarmingOperationForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);

    Scenario scenario = getScenario(form);
    
    String schedule = form.getSchedule();

    checkReadOnly(request, form, scenario);
    
    boolean dataUpToDate =
      scenario.getRevisionCount().equals(form.getScenarioRevisionCount());
    if(dataUpToDate && form.isNew()) {
      boolean pyvHasVerifiedScenario = getPyvHasVerifiedScenario(scenario);
      if(pyvHasVerifiedScenario) {
        dataUpToDate = false;
      }
    }
    
    if(dataUpToDate) {

      if (errors == null || errors.isEmpty()) {
        errors = new ActionMessages();
        checkErrors(form, scenario, errors);
      }

      if (!errors.isEmpty()) {
        saveErrors(request, errors);
        forward = mapping.findForward(ActionConstants.FAILURE);
      } else {
        FarmingOperation fo;
        if(form.isNew()) {
          fo = new FarmingOperation();
          fo.setFarmingYear(scenario.getFarmingYear());
        } else {
          fo = scenario.getFarmingYear().getFarmingOperationBySchedule(schedule);
        }
        
        populateFarmingOperationFromForm(form, fo);
  
        CalculatorService calculatorService = ServiceFactory.getCalculatorService();
        
        try {
          if(form.isNew()) {
            fo.setSchedule(schedule);
            calculatorService.createFarmingOperation(fo, getUserId());
            form.setNew(false);
          } else {
            calculatorService.updateFarmingOperation(fo, getUserId());
          }

          scenario = refreshScenario(form);
          populateForm(form, scenario, request);

          BenefitService benefitService = ServiceFactory.getBenefitService();
          // ignore error messages returned
          benefitService.calculateBenefit(scenario, getUserId());

        } catch(InvalidRevisionCountException irce) {
          handleInvalidRevisionCount(request);
          forward = mapping.findForward(ActionConstants.FAILURE);
        }
      }
    } else {
      handleInvalidRevisionCount(request);
      forward = mapping.findForward(ActionConstants.FAILURE);
    }

    populateScheduleOptions(form, scenario);

    setReadOnlyFlag(request, form, scenario);

    return forward;
  }


  /**
   * @param form form
   * @param scenario scenario
   * @param errors errors
   */
  private void checkErrors(FarmingOperationForm form, Scenario scenario, ActionMessages errors) {
    
    if(scenario.isInCombinedFarm()) {
      String schedule = form.getSchedule();
      FarmingYear farmingYear = scenario.getFarmingYear();
      FarmingOperation fo = farmingYear.getFarmingOperationBySchedule(schedule);
  
      String oldAccountingCode = fo.getAccountingCode();
      String newAccountingCode = form.getAgristabAccountingCode();
      
      if(!StringUtils.isBlank(oldAccountingCode)
          && !newAccountingCode.equals(oldAccountingCode)) {
        
        String messageKey = MessageConstants.ERRORS_FARMING_OPERATION_COMBINED_FARM_ACCOUNTING_CODE_CHANGE;
        errors.add("", new ActionMessage(messageKey));
      }
    }
  }


  /**
   * @param form form
   * @param fo fo
   * @throws Exception On Exception
   */
  private void populateFarmingOperationFromForm(FarmingOperationForm form, FarmingOperation fo)
  throws Exception {

    // Integers
    fo.setPartnershipPin(DataParseUtils.parseIntegerObject(form.getPartnershipPin()));
    fo.setRevisionCount(DataParseUtils.parseIntegerObject(form.getOperationRevisionCount()));
    
    // Doubles
    fo.setBusinessUseHomeExpense(DataParseUtils.parseDoubleObject(form.getBusinessUseHomeExpense()));
    fo.setFarmingExpenses(DataParseUtils.parseDoubleObject(form.getExpenses()));
    fo.setGrossIncome(DataParseUtils.parseDoubleObject(form.getGrossIncome()));
    fo.setInventoryAdjustments(DataParseUtils.parseDoubleObject(form.getInventoryAdjustments()));
    fo.setNetFarmIncome(DataParseUtils.parseDoubleObject(form.getNetFarmIncome()));
    fo.setNetIncomeAfterAdj(DataParseUtils.parseDoubleObject(form.getNetIncomeAfterAdj()));
    fo.setNetIncomeBeforeAdj(DataParseUtils.parseDoubleObject(form.getNetIncomeBeforeAdj()));
    fo.setOtherDeductions(DataParseUtils.parseDoubleObject(form.getOtherDeductions()));
    
    // Dates
    fo.setFiscalYearStart(DataParseUtils.parseDate(form.getFiscalYearStart()));
    fo.setFiscalYearEnd(DataParseUtils.parseDate(form.getFiscalYearEnd()));
    
    // Strings
    fo.setAccountingCode(form.getAgristabAccountingCode());
    fo.setPartnershipName(form.getPartnershipName());
    
    // Boolean Indicators
    fo.setIsCropDisaster(Boolean.valueOf(form.isCropDisaster()));
    fo.setIsCropShare(Boolean.valueOf(form.isCropShare()));
    fo.setIsFeederMember(Boolean.valueOf(form.isFeederMember()));
    fo.setIsLandlord(Boolean.valueOf(form.isLandlord()));
    fo.setIsLivestockDisaster(Boolean.valueOf(form.isLivestockDisaster()));

    // Special logic for the following fields

    final int hundred = 100;

    double partnershipPercent = Double.parseDouble(form.getPartnershipPercent()) / hundred;
    fo.setPartnershipPercent(new Double(partnershipPercent));

    final int maxProdIns = 4;
    List<ProductionInsurance> productionInsurances = new ArrayList<>(maxProdIns);
    
    if(!StringUtils.isBlank(form.getProductionInsuranceNumber1())) {
      ProductionInsurance pi = new ProductionInsurance();
      pi.setProductionInsuranceNumber(form.getProductionInsuranceNumber1());
      productionInsurances.add(pi);
    }
    if(!StringUtils.isBlank(form.getProductionInsuranceNumber2())) {
      ProductionInsurance pi = new ProductionInsurance();
      pi.setProductionInsuranceNumber(form.getProductionInsuranceNumber2());
      productionInsurances.add(pi);
    }
    if(!StringUtils.isBlank(form.getProductionInsuranceNumber3())) {
      ProductionInsurance pi = new ProductionInsurance();
      pi.setProductionInsuranceNumber(form.getProductionInsuranceNumber3());
      productionInsurances.add(pi);
    }
    if(!StringUtils.isBlank(form.getProductionInsuranceNumber4())) {
      ProductionInsurance pi = new ProductionInsurance();
      pi.setProductionInsuranceNumber(form.getProductionInsuranceNumber4());
      productionInsurances.add(pi);
    }
    
    fo.setProductionInsurances(productionInsurances);

  }

}
