/**
 * Copyright (c) 2025,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.negativemargin;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.Margin;
import ca.bc.gov.srm.farm.domain.NegativeMargin;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.service.NegativeMarginService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorAction;

/**
 * @author hwang
 */
public class NegativeMarginsViewAction extends CalculatorAction {

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

    logger.debug("Viewing Negative Margins...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);

    NegativeMarginsForm form = (NegativeMarginsForm) actionForm;

    Scenario scenario = getScenario(form);

    setReadOnlyFlag(request, form, scenario);
    
    populateForm(form, scenario, true);

    return forward;
  }

  /**
   * Fill the form fields from the scenario
   * @param form The form object to fill.
   * @param scenario The scenario to fill the form from.
   * @throws Exception On Exception
   */
  protected void populateForm(
      NegativeMarginsForm form,
      Scenario scenario,
      boolean setEditableValues)
  throws Exception {

    populateFarmViewOptions(form, scenario, false);

    if (form.getFarmView() == null && form.getFarmViewOptions() != null && !form.getFarmViewOptions().isEmpty()) {
      String defaultFarmView = form.getFarmViewOptions().iterator().next().getValue();
      form.setFarmView(defaultFarmView);
    }

    populateNegativeMargins(form, scenario, setEditableValues);
  }

  /**
   * Fill the negative margins from the scenario
   * @param form form
   * @param setEditableValues 
   * @return scenario
   */
  private void populateNegativeMargins(
      NegativeMarginsForm form,
      Scenario scenario,
      boolean setEditableValues)
  throws Exception {

    String schedule = form.getFarmView();
    Integer scenarioId = scenario.getScenarioId();
    
    BigDecimal paymentPercentage =
        CalculatorConfig.getNegativeMarginPaymentPercentage(scenario.getYear());
    form.setPayablePercent(paymentPercentage);

    FarmingOperation farmingOperation = scenario.getFarmingYear().getFarmingOperationBySchedule(schedule);

    if (farmingOperation != null && setEditableValues) {
      Integer farmingOperationId = farmingOperation.getFarmingOperationId();
      
      Double partnershipPercent = farmingOperation.getPartnershipPercent();
      form.setPartnershipPercent(BigDecimal.valueOf(partnershipPercent));
      
      String user = CurrentUser.getUser().getUserId();
      NegativeMarginService nmService = ServiceFactory.getNegativeMarginService();

      nmService.calculateNegativeMargins(farmingOperationId, scenarioId, user);
      List<NegativeMargin> negativeMargins = nmService.getNegativeMargins(farmingOperationId, scenarioId);
      form.getInventoryItemCodes().clear();

      for (NegativeMargin nm : negativeMargins) {
        NegativeMarginFormLine line = form.getNegativeMargin(nm.getInventoryItemCode());
        line.setNegativeMarginId(nm.getNegativeMarginId());
        line.setFarmingOperationId(nm.getFarmingOperationId());
        line.setInventoryItemCode(nm.getInventoryItemCode());
        line.setRevisionCount(nm.getRevisionCount());
        line.setInventory(nm.getInventory());
        line.setDeductiblePercentage(nm.getDeductiblePercentage() == null ? null : nm.getDeductiblePercentage().toString());
        line.setInsurableValue(nm.getInsurableValue() == null ? null : nm.getInsurableValue().toString());
        line.setInsurableValuePurchased(nm.getInsurableValuePurchased() == null ? null : nm.getInsurableValuePurchased().toString());
        line.setReported(nm.getReported() == null ? null : nm.getReported().toString());
        line.setGuaranteedProdValue(nm.getGuaranteedProdValue() == null ? null : nm.getGuaranteedProdValue().toString());
        line.setPremiumsPaid(nm.getPremiumsPaid() == null ? null : nm.getPremiumsPaid().toString());
        line.setClaimsReceived(nm.getClaimsReceived() == null ? null : nm.getClaimsReceived().toString());
        line.setDeemedReceived(nm.getDeemedReceived() == null ? null : nm.getDeemedReceived().toString());
        line.setDeemedPiValue(nm.getDeemedPiValue() == null ? null : nm.getDeemedPiValue().toString());
        line.setPremiumRate(nm.getPremiumRate() == null ? null : nm.getPremiumRate().toString());
        line.setClaimsCalculation(nm.getClaimsCalculation() == null ? null : nm.getClaimsCalculation().toString());
        line.setPremium(nm.getPremium() == null ? null : nm.getPremium().toString());
        line.setMrp(nm.getMrp() == null ? null : nm.getMrp().toString());
        line.setDeemedPremium(nm.getDeemedPremium() == null ? null : nm.getDeemedPremium().toString());
      }
      
      Margin margin = farmingOperation.getMargin();
      if(margin != null) {
        form.setSubtotal(BigDecimal.valueOf(margin.getProdInsurDeemedSubtotal()));
        form.setTotalPayable(BigDecimal.valueOf(margin.getProdInsurDeemedTotal()));
      }
    }
    
    form.getInventoryItemCodes().addAll(form.getNegativeMargins().keySet());
    Collections.sort(form.getInventoryItemCodes());
  }
}
