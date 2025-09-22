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
package ca.bc.gov.srm.farm.ui.struts.calculator.benefit;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.calculator.BenefitCalculator;
import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.calculator.CalculatorFactory;
import ca.bc.gov.srm.farm.calculator.StructuralChangeCalculator;
import ca.bc.gov.srm.farm.domain.Benefit;
import ca.bc.gov.srm.farm.domain.CombinedFarm;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.ReferenceScenario;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.service.BenefitService;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorAction;
import ca.bc.gov.srm.farm.util.MathUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * Action to view screen 850.
 */
public class BenefitViewAction extends CalculatorAction {

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

    logger.debug("Viewing Benefit");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    ActionMessages errors = null;
    BenefitForm form = (BenefitForm) actionForm;
    
    form.setRefresh(true);
    Scenario scenario = getScenario(form);
    
    setReadOnlyFlag(request, form, scenario);
    
    BenefitService benefitService = ServiceFactory.getBenefitService();
    CalculatorService calculatorService = ServiceFactory.getCalculatorService();
    
    if(!form.isReadOnly()) {
    	// validate, calculate, save
      String userId = CurrentUser.getUser().getUserId();
      
      try {
          errors = benefitService.calculateBenefit(scenario, userId);
          if(errors.isEmpty()) {
            calculatorService.updateScenarioRevisionCount(scenario, getUserId());
          }
      } catch(InvalidRevisionCountException irce) {
          handleInvalidRevisionCount(request);
          forward = mapping.findForward(ActionConstants.FAILURE);
      }
    } else if(scenario.isFifoScenario() && scenario.stateIsOneOf(ScenarioStateCodes.FAILED)) {
      errors = benefitService.calculateBenefit(scenario, getUserId(), false, true, true);
    }
    
    boolean initEditableFields = true;
    initForm(form, scenario, initEditableFields);
    
    if (errors != null && !errors.isEmpty()) {
      saveErrors(request, errors);
    }
  
    return forward;
  }


  /**
   * @param form form
   * @param scenario programYearScenario
   * @param initEditableFields initEditableFields
   * @throws Exception On Exception
   */
  protected void initForm(BenefitForm form, Scenario scenario, boolean initEditableFields) throws Exception {
    form.setScenarioRevisionCount(scenario.getRevisionCount());
    form.setLineNumber(0);
    populateRequiredYears(form, scenario);
    syncFarmViewCacheWithForm(form);
    
    BenefitCalculator benefitCalc = CalculatorFactory.getBenefitCalculator(scenario);
    Benefit benefit = benefitCalc.getBenefit();
    boolean[] usedInCalc = benefitCalc.getUsedInCalc();
    form.setUsedInCalc(usedInCalc);
    
    if(initEditableFields) {
      // SC Material Override
      StructuralChangeCalculator scCalc = CalculatorFactory.getStructuralChangeCalculator(scenario);
      boolean scNotable = scCalc.getIsStructuralChangeNotable();
      if(scNotable) {
        form.setScMaterialOverride(BenefitForm.YES);
      } else {
        form.setScMaterialOverride(BenefitForm.NO);
      }

      // production insurance benefit
      Double val = benefit.getProdInsurDeemedBenefit();
      if(val != null) {
        DecimalFormat df = new DecimalFormat("#0.00");
        String insuranceString = StringUtils.formatDouble(val, df);
        form.setInsuranceBenefit(insuranceString);
      }
      
      Double interimBenefitPercent = benefit.getInterimBenefitPercent();
      if(interimBenefitPercent != null) {
        // stored as fraction, turn into 0 to 100
        double fraction = interimBenefitPercent;
        final int oneHundred = 100;
        double percent = fraction * oneHundred;
        initInterimBenefitPercent(form, percent);
      }
      
    }
    
    boolean isWholeFarm = CalculatorAction.FARM_VIEW_WHOLE_FARM_CODE.equals(form.getFarmView());
    boolean isCombinedFarm = scenario.isInCombinedFarm();
    boolean showCombinedFarmView = isWholeFarm && isCombinedFarm;

    if(showCombinedFarmView) {
      initCombinedFarmForm(form, scenario, initEditableFields);
    } else {
      initBasicFarmForm(form, scenario, initEditableFields);
    }

    populateFarmViewOptions(form, scenario);
    populateParameters(form, scenario);
  }


  /**
   * @param form form
   * @param interimBenefitPercent
   */
  protected void initInterimBenefitPercent(BenefitForm form, double interimBenefitPercent) {
    DecimalFormat df = new DecimalFormat("#0");
    String percentString = StringUtils.formatDouble(interimBenefitPercent, df);
    form.setInterimBenefitPercent(percentString);
  }


  /**
   * @param form form
   * @param scenario scenario
   * @param initEditableFields initEditableFields
   */
  private void initBasicFarmForm(BenefitForm form, Scenario scenario, boolean initEditableFields) {

    Benefit benefit = scenario.getFarmingYear().getBenefit();
    
    form.setBenefit(benefit);
    
    // totals
    String farmView = form.getFarmView();
    Object[] totals = BenefitUtils.getTotalsForFarmView(scenario, farmView);
    form.setTotals(totals);
    
    if(initEditableFields) {
      // deemed farm year
      List<Integer> refYears = ScenarioUtils.getReferenceYears(scenario.getYear());
    	int index = 0;
  
  		for(Integer refYear : refYears) {
  			ReferenceScenario rs = scenario.getReferenceScenarioByYear(refYear);
  			
  			boolean deemed;
  			if(rs != null) {
    			deemed = rs.getIsDeemedFarmingYear();
        } else {
          deemed = false;
        }
  			form.setIsDeemedFarmYear(index, deemed);
  			index++;
  		}
  
  	  Double val = benefit.getAppliedBenefitPercent();
  	  if(val != null) {
  	  	// stored as fraction, turn into 0 to 100
  	  	double fraction = val;
  	  	final int oneHundred = 100;
        double percent = fraction * oneHundred;
  	  	form.setAppliedBenefitPercent(Double.toString(percent));
  	  }
    }
  }
  
  
  /**
   * @param form form
   * @param scenario scenario
   * @param initEditableFields initEditableFields
   */
  private void initCombinedFarmForm(BenefitForm form, Scenario scenario, boolean initEditableFields) {

    CombinedFarm combinedFarm = scenario.getCombinedFarm();
    Benefit benefit = combinedFarm.getBenefit();
    Map<Integer, MarginTotal> yearMargins = combinedFarm.getYearMargins();
    Map<Integer, Boolean> deemedFarmingYearMap = combinedFarm.getDeemedFarmingYearMap();
    List<Integer> allYears = combinedFarm.getAllYearsIncludingMissing();
    
    form.setBenefit(benefit);
    
    // totals
    Object[] totals = new Object[allYears.size()];
    form.setTotals(totals);

    int index = 0;
    for(Integer curYear : allYears) {
      MarginTotal yearMargin = yearMargins.get(curYear);
      
      totals[index] = yearMargin;
      index++;
    }


    if(initEditableFields) {
      // deemed farm year
      index = 0;
  
      for(Integer refYear : allYears) {
        
        Boolean isDeemedFarmingYear = deemedFarmingYearMap.get(refYear);
        boolean b = isDeemedFarmingYear != null && isDeemedFarmingYear;
        form.setIsDeemedFarmYear(index, b);
        index++;
      }
      
      for(Scenario curScenario : combinedFarm.getScenarios()) {
        Benefit scenarioBenefit = curScenario.getFarmingYear().getBenefit();
        String pinString = curScenario.getClient().getParticipantPin().toString();
        Double benefitPercent = scenarioBenefit.getCombinedFarmPercent();
        if(benefitPercent != null) {
          // stored as fraction, turn into 0 to 100
          double fraction = benefitPercent;
          final int oneHundred = 100;
          final int decimalPlaces = 1;
          double percent = fraction * oneHundred;
          percent = MathUtils.round(percent, decimalPlaces);
          form.setAppliedBenefitPercentMap(pinString, Double.toString(percent));
        }
      }
    }
    
  }


  private void populateParameters(BenefitForm form, Scenario scenario) {
    Integer year = scenario.getYear();
    boolean paymentCapEnabled = CalculatorConfig.isPaymentCapEnabled(year);
    Double paymentCapPercentage = null;
    Integer programYear = scenario.getYear();
    
    if(paymentCapEnabled) {
      paymentCapPercentage = CalculatorConfig.getPaymentCapPercentageOfTotalMarginDecline(year);
    }
    
    Double productionInsuranceFactor = CalculatorConfig.getProductionInsuranceFactor(programYear);
    Double paymentTriggerFactor = CalculatorConfig.getPaymentTriggerFactor();
    Double standardPositiveMarginCompensationRate = CalculatorConfig.getStandardPositiveMarginCompensationRate(year);
    Double standardNegativeMarginCompensationRate = CalculatorConfig.getStandardNegativeMarginCompensationRate(year);
    
    Double enhancedPositiveMarginCompensationRate = null;
    Double enhancedNegativeMarginCompensationRate = null;
    
    if(year >= CalculatorConfig.GROWING_FORWARD_2017) {
      enhancedPositiveMarginCompensationRate = CalculatorConfig.getEnhancedPositiveMarginCompensationRate(year);
      enhancedNegativeMarginCompensationRate = CalculatorConfig.getEnhancedNegativeMarginCompensationRate(year);
    }
    
    form.setPaymentCapEnabled(paymentCapEnabled);
    form.setPaymentCapPercentageOfMarginDecline(paymentCapPercentage);
    form.setProductionInsuranceFactor(productionInsuranceFactor);
    form.setPaymentTriggerFactor(paymentTriggerFactor);
    form.setStandardPositiveMarginCompensationRate(standardPositiveMarginCompensationRate);
    form.setStandardNegativeMarginCompensationRate(standardNegativeMarginCompensationRate);
    form.setEnhancedPositiveMarginCompensationRate(enhancedPositiveMarginCompensationRate);
    form.setEnhancedNegativeMarginCompensationRate(enhancedNegativeMarginCompensationRate);
  }
  
}
