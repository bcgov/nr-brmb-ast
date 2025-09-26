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
package ca.bc.gov.srm.farm.ui.struts.calculator.farmdetail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.FarmingYear;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorAction;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;
import ca.bc.gov.srm.farm.util.DateUtils;
import ca.bc.gov.srm.farm.util.ScenarioUtils;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * 
 * @author awilkinson
 *
 */
public class FarmDetailViewAction extends CalculatorAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Viewing Farm Detail...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    FarmDetailForm form = (FarmDetailForm) actionForm;
    Scenario scenario = getScenario(form);
    
    setReadOnlyFlag(request, form, scenario);
    populateForm(form, scenario, request);
    
    return forward;
  }

  /**
   * Fill the form fields from the account
   * @param form The form object to fill.
   * @param scenario The scenario to fill the form from.
   */
  protected void populateForm(FarmDetailForm form, Scenario scenario, HttpServletRequest request) {

    form.setScenarioRevisionCount(scenario.getRevisionCount());

    FarmingYear fy = scenario.getFarmingYear();

    // Integers
    form.setAgristabFedStsCode(StringUtils.toString(fy.getAgristabFedStsCode()));
    form.setCommonShareTotal(StringUtils.toString(fy.getCommonShareTotal()));
    form.setProgramYearVersionRevisionCount(StringUtils.toString(fy.getRevisionCount()));
    form.setFarmYears(StringUtils.toString(fy.getFarmYears()));

    // Boolean Indicators
    form.setAccrualWorksheet(fy.getIsAccrualWorksheet().booleanValue());
    form.setAccrualCashConversion(fy.getIsAccrualCashConversion().booleanValue());
    form.setCanSendCobToRep(fy.getIsCanSendCobToRep().booleanValue());
    form.setCombinedFarm(fy.getIsCombinedFarm().booleanValue());
    form.setCompletedProdCycle(fy.getIsCompletedProdCycle().booleanValue());
    form.setCoopMember(fy.getIsCoopMember().booleanValue());
    form.setCorporateShareholder(fy.getIsCorporateShareholder().booleanValue());
    form.setCwbWorksheet(fy.getIsCwbWorksheet().booleanValue());
    form.setDisaster(fy.getIsDisaster().booleanValue());
    form.setLastYearFarming(fy.getIsLastYearFarming().booleanValue());
    form.setPartnershipMember(fy.getIsPartnershipMember().booleanValue());
    form.setPerishableCommodities(fy.getIsPerishableCommodities().booleanValue());
    form.setReceipts(fy.getIsReceipts().booleanValue());
    form.setSoleProprietor(fy.getIsSoleProprietor().booleanValue());
    

    // Strings
    form.setMunicipalityCode(fy.getMunicipalityCode());
    form.setMunicipalityCodeDescription(fy.getMunicipalityCodeDescription());
    form.setOtherText(fy.getOtherText());
    form.setParticipantProfileCode(fy.getParticipantProfileCode());
    form.setProvinceOfMainFarmstead(fy.getProvinceOfMainFarmstead());
    form.setProvinceOfResidence(fy.getProvinceOfResidence());
    
    // Dates
    form.setLocalStatementAReceivedDate(DateUtils.formatDateForDatePicker(scenario.getLocalStatementAReceivedDate()));
    form.setLocalSupplementalReceivedDate(DateUtils.formatDateForDatePicker(scenario.getLocalSupplementalReceivedDate()));

    // Cash Margins
    if (fy.getCashMarginsOptInDate() != null) {
      form.setCashMarginsOptInDate(DateUtils.formatDateForDatePicker(fy.getCashMarginsOptInDate()));
    }
    form.setCashMargins(fy.getIsCashMargins());

    boolean programYearVersionHasVerifiedBenefit = ScenarioUtils.programYearVersionHasVerifiedBenefit(scenario);
    boolean municipalityLocked = programYearVersionHasVerifiedBenefit || scenario.isInCombinedFarm();
    form.setMunicipalityLocked(municipalityLocked);
    
    if(form.isCanModifyScenario() && municipalityLocked) {
      ActionMessages messages = new ActionMessages();
      
      if(programYearVersionHasVerifiedBenefit) {
        messages.add("", new ActionMessage(MessageConstants.INFO_FARM_DETAIL_SAVE_VERSION_HAS_VERIFIED_BENEFIT));
      } else if(scenario.isInCombinedFarm()) {
        messages.add("", new ActionMessage(MessageConstants.ERRORS_FARM_DETAIL_COMBINED_FARM_MUNICIPALITY_CHANGE));
      }
      
      request.setAttribute("infoMessages", messages);
    }
  }
  
}
