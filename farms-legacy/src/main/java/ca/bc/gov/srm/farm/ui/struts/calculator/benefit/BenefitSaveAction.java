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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
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
import ca.bc.gov.srm.farm.domain.codes.ScenarioCategoryCodes;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.service.BenefitService;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.CombinedFarmUtils;
import ca.bc.gov.srm.farm.util.DataParseUtils;
import ca.bc.gov.srm.farm.util.MathUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;

/**
 * Action to view screen 850.
 */
public class BenefitSaveAction extends BenefitViewAction {

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

    logger.debug("Saving Benefit");
    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    BenefitForm form = (BenefitForm) actionForm;
    Scenario scenario = getScenario(form);
    String userId = CurrentUser.getUser().getUserId();
    
    if(!scenario.getRevisionCount().equals(form.getScenarioRevisionCount())) {
    	handleInvalidRevisionCount(request);
      forward = mapping.findForward(ActionConstants.FAILURE);
    } else {
      // validate what the user entered
      ActionMessages errors = form.validate(mapping, request);
      
      if (errors.isEmpty()) {
        validateAppliedBenefitPercent(form, scenario, errors);
      }
      
      if (errors.isEmpty()) {
      	// update the scenario with what the user entered
        boolean scOverridden = updateScenario(form, scenario);
        
        // validate, calculate, save
        BenefitService service = ServiceFactory.getBenefitService();
        
        try {
          errors = service.calculateBenefit(scenario, userId, true, true, false);
          
          ActionMessages warnings = new ActionMessages();
          
          // show warning about SC material flag being changed
          if(scOverridden) {
          	String msgKey = MessageConstants.WARNING_SC_NOTABLE_CHANGED;
            warnings.add("", new ActionMessage(msgKey));
          }
          
          // show warnings about FMVs
          Map<Integer, Set<String>> missingFMVsMap = ScenarioUtils.getAllInventoryWithMissingFMVs(scenario);
  
          if(!missingFMVsMap.isEmpty()) {
            for(Integer year : missingFMVsMap.keySet() ) {
              Set<String> missingFMVs = missingFMVsMap.get(year);
              String codeCsv = ca.bc.gov.srm.farm.util.StringUtils.toCsv(new ArrayList<>(missingFMVs));
              String msgKey = MessageConstants.WARNING_BENEFIT_INVENTORY_FMV_MISSING;
              warnings.add("", new ActionMessage(msgKey, year.toString(), codeCsv));
            }
          }
          
          if(scenario.isInCombinedFarm()) {
            boolean municipalitiesMatch = CombinedFarmUtils.municipalitiesMatch(scenario);
            if (!municipalitiesMatch) {
              String msgKey = MessageConstants.WARNING_COMBINED_FARM_MUNICIPALITY_MISMATCH;
              warnings.add("", new ActionMessage(msgKey));
            }
          }
          
          if (!warnings.isEmpty()) {
          	saveMessages(request, warnings);
          }
        } catch(InvalidRevisionCountException irce) {
            handleInvalidRevisionCount(request);
            forward = mapping.findForward(ActionConstants.FAILURE);
        }
      }
      
      if (errors.isEmpty()) {
        CalculatorService calculatorService = ServiceFactory.getCalculatorService();
        calculatorService.flagReasonabilityTestsStale(scenario, userId);
        
      	addScenarioLog(form, scenario, "Update Benefits and Margins");
      	scenario = refreshScenario(form);
      } else {
      	saveErrors(request, errors);
        forward = mapping.findForward(ActionConstants.FAILURE);
      }
    }

    setReadOnlyFlag(request, form, scenario);
    
    boolean initEditableFields = false;
    initForm(form, scenario, initEditableFields);

    return forward;
  }



  /**
   * @param form form
   * @param scenario scenario
   * @param errors errors
   */
  private void validateAppliedBenefitPercent(BenefitForm form, Scenario scenario, ActionMessages errors) {

    if(scenario.isInCombinedFarm()) {
      CombinedFarm combinedFarm = scenario.getCombinedFarm();
      double totalPercent = 0;
      
      for(Scenario curScenario :combinedFarm.getScenarios() ) {
        String pinString = curScenario.getClient().getParticipantPin().toString();
        
        String percentString = StringUtils.trim(form.getAppliedBenefitPercentMap(pinString));
        if(StringUtils.isBlank(percentString)) {
          String msgKey = MessageConstants.ERRORS_COMBINED_FARM_PERCENT_REQUIRED;
          errors.add("", new ActionMessage(msgKey));
          break;
        }
        
        try {
          double percent = Double.parseDouble(percentString);
          totalPercent += percent;
        } catch(NumberFormatException e) {
          String msgKey = MessageConstants.ERRORS_COMBINED_FARM_PERCENT_NUMBER;
          errors.add("", new ActionMessage(msgKey));
          break;
        }
      }
      
      if(errors.isEmpty()) {
        totalPercent = MathUtils.round(totalPercent, 1);
        
        final int oneHundred = 100;
        if(totalPercent != oneHundred) {
          String msgKey = MessageConstants.ERRORS_COMBINED_FARM_PERCENT_100;
          errors.add("", new ActionMessage(msgKey));
        }
      }
    }
  }



  /**
   * Update the scenario based on what the user entered.
   * 
   * @param form form
   * @param programYearScenario programYearScenario
   * @return true if the "SC Material" flag was overriden by the user
   */
  private boolean updateScenario(BenefitForm form, Scenario programYearScenario) {

    boolean scOverridden;
    if(programYearScenario.isInCombinedFarm()) {
      scOverridden = updateCombinedScenario(form, programYearScenario);
    } else {
      scOverridden = updateBasicScenario(form, programYearScenario);
    }
    
    Integer programYear = programYearScenario.getYear();
    if( ! CalculatorConfig.isNegativeMarginCalculationEnabled(programYear)
        || programYearScenario.isInterim()) {
    
      BenefitCalculator benefitCalc = CalculatorFactory.getBenefitCalculator(programYearScenario);
      String insuranceString = StringUtils.trim(form.getInsuranceBenefit());
      Benefit benefit = benefitCalc.getBenefit();
      
      if(StringUtils.isBlank(insuranceString)) {
        // make sure to clear any previous value
        benefit.setProdInsurDeemedBenefit(null);
      } else {
        benefit.setProdInsurDeemedBenefit(new Double(insuranceString));
      }
    }

    return scOverridden;
  }


  /**
   * Update the scenario based on what the user entered.
   * 
   * @param form form
   * @param programYearScenario programYearScenario
   * @return true if the "SC Material" flag was overriden by the user
   */
  private boolean updateBasicScenario(BenefitForm form,
      Scenario programYearScenario) {
    boolean scOverridden = false;
    StructuralChangeCalculator scCalc = CalculatorFactory.getStructuralChangeCalculator(programYearScenario);
  	boolean prevIsSCNotable = scCalc.getIsStructuralChangeNotable();
  	List<Integer> allYears = ScenarioUtils.getAllYears(programYearScenario.getYear());
  	Integer programYear = programYearScenario.getYear();
  	int index = 0;
  	boolean[] deemedInd = form.getIsDeemedFarmYear();
  	
		for(Integer curYear : allYears) {
      ReferenceScenario rs = programYearScenario.getReferenceScenarioByYear(curYear);
      
			if (rs != null) {
			  MarginTotal mt = rs.getFarmingYear().getMarginTotal();
			  
			  boolean isRefYear = !curYear.equals(programYear);
        if(isRefYear) {
          if (deemedInd[index]) {
            rs.setIsDeemedFarmingYear(Boolean.TRUE);
          } else {
            rs.setIsDeemedFarmingYear(Boolean.FALSE);
          }
			  }
			  
        if (BenefitForm.YES.equals(form.getScMaterialOverride())) {
          mt.setIsStructuralChangeNotable(Boolean.TRUE);
          scOverridden = !prevIsSCNotable;
        } else {
          mt.setIsStructuralChangeNotable(Boolean.FALSE);
          scOverridden = prevIsSCNotable;
        }
      }
      logger.debug("year: " + curYear + "," + deemedInd[index]);

			index++;
		}
		
	  Benefit benefit = programYearScenario.getFarmingYear().getBenefit();
	  
	  String appliedBenefitPercentString = StringUtils.trim(form.getAppliedBenefitPercent());
	  if(StringUtils.isBlank(appliedBenefitPercentString)) {
	  	benefit.setAppliedBenefitPercent(null);
	  } else {
	  	double percent = Double.parseDouble(appliedBenefitPercentString);
	  	final int hundred = 100;
      Double fraction = new Double(percent/hundred);
	  	benefit.setAppliedBenefitPercent(fraction);
	  }
	  
	  Double interimBenefitPercent = parseInterimBenefitPercent(form, programYearScenario);
	  benefit.setInterimBenefitPercent(interimBenefitPercent);
	  
	  return scOverridden;
  }
  
  
  /**
   * Update the combined farm based on what the user entered.
   * 
   * @param form form
   * @param programYearScenario programYearScenario
   * @return true if the "SC Material" flag was overriden by the user
   */
  private boolean updateCombinedScenario(BenefitForm form,
      Scenario programYearScenario) {
    CombinedFarm combinedFarm = programYearScenario.getCombinedFarm();
    Benefit combinedBenefit = combinedFarm.getBenefit();
    Map<Integer, MarginTotal> yearMargins = combinedFarm.getYearMargins();
    Map<Integer, Boolean> deemedFarmingYearMap = combinedFarm.getDeemedFarmingYearMap();
    final int hundred = 100;
    
    boolean scOverridden = false;
    StructuralChangeCalculator scCalc = CalculatorFactory.getStructuralChangeCalculator(programYearScenario);
    boolean prevIsSCNotable = scCalc.getIsStructuralChangeNotable();
    Integer programYear = programYearScenario.getYear();
    int index = 0;
    boolean[] deemedInd = form.getIsDeemedFarmYear();
    
    for(Integer curYear : combinedFarm.getAllYearsIncludingMissing()) {
      MarginTotal mt = yearMargins.get(curYear);
      
      if(mt != null) {
        
        boolean isRefYear = !curYear.equals(programYear);
        if(isRefYear) {
          Boolean isDeemedFarmingYear;
          if(deemedInd[index]) {
            isDeemedFarmingYear = Boolean.TRUE;
          } else {
            isDeemedFarmingYear = Boolean.FALSE;
          }
          deemedFarmingYearMap.put(curYear, isDeemedFarmingYear);
        }
        
        if(BenefitForm.YES.equals(form.getScMaterialOverride())) {
          mt.setIsStructuralChangeNotable(Boolean.TRUE);
          scOverridden = !prevIsSCNotable;
        } else {
          mt.setIsStructuralChangeNotable(Boolean.FALSE);
          scOverridden = prevIsSCNotable;
        }
      }
      
      logger.debug("year: " + curYear + "," + deemedInd[index]);
      
      index++;
    }
    
    Double interimBenefitPercent = parseInterimBenefitPercent(form, programYearScenario);
    
    for(Scenario curScenario : combinedFarm.getScenarios()) {
      Benefit benefit = curScenario.getFarmingYear().getBenefit();
      String pinString = curScenario.getClient().getParticipantPin().toString();
      
      String percentString = StringUtils.trim(form.getAppliedBenefitPercentMap(pinString));
      if(StringUtils.isBlank(percentString)) {
        benefit.setAppliedBenefitPercent(null);
      } else {
        double percent = Double.parseDouble(percentString);
        percent = MathUtils.round(percent, 1);
        Double fraction = new Double(percent/hundred);
        benefit.setAppliedBenefitPercent(fraction);
      }
      
      // Need to set this in the scenario benefit as well as the combined benefit
      // because both are used.
      benefit.setInterimBenefitPercent(interimBenefitPercent);
    }
    
    combinedBenefit.setInterimBenefitPercent(interimBenefitPercent);
    
    return scOverridden;
  }


  /**
   * @param form form
   * @param scenario scenario
   * @return interimBenefitPercent
   */
  private Double parseInterimBenefitPercent(BenefitForm form, Scenario scenario) {
    Double interimBenefitPercent = null;
    if(scenario.getScenarioCategoryCode().equals(ScenarioCategoryCodes.INTERIM)) {
      String interimBenefitPercentString = form.getInterimBenefitPercent();
      try {
        double percent = DataParseUtils.parseDouble(interimBenefitPercentString);
        percent = MathUtils.round(percent, 0);
        final int hundred = 100;
        interimBenefitPercent = Double.valueOf(percent/hundred);

        // reset the rounded percent in the form
        initInterimBenefitPercent(form, percent);
      } catch (ParseException e) {
        logger.debug("ParseException parsing interimBenefitPercent. Value=" + interimBenefitPercentString);
      }
    }
    return interimBenefitPercent;
  }

}
