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
import java.util.ArrayList;
import java.util.List;

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

import ca.bc.gov.srm.farm.domain.NegativeMargin;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.service.BenefitService;
import ca.bc.gov.srm.farm.service.NegativeMarginService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;

/**
 * @author hwang
 */
public class NegativeMarginsSaveAction extends NegativeMarginsViewAction {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Saving Negative Margins...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);

    NegativeMarginsForm form = (NegativeMarginsForm) actionForm;
    Scenario scenario = getScenario(form);
    Integer scenarioId = scenario.getScenarioId();
    
    checkReadOnly(request, form, scenario);
    
    ActionMessages errors = form.validate(mapping, request);

    ActionMessages errorMessages = new ActionMessages();

    if (errors != null && !errors.isEmpty()) {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
      populateForm(form, scenario, false);
    } else {

      String user = CurrentUser.getUser().getUserId();
      NegativeMarginService nmService = ServiceFactory.getNegativeMarginService();

      List<NegativeMargin> negativeMargins = new ArrayList<>();
      for (NegativeMarginFormLine line : form.getNegativeMargins().values()) {
        negativeMargins.add(getNegativeMarginFromForm(line, scenarioId));
      }

      try {
        nmService.updateNegativeMargins(negativeMargins, scenario, user);
        
        BenefitService benefitService = ServiceFactory.getBenefitService();
        // ignore error messages returned
        benefitService.calculateBenefit(scenario, getUserId());
        
      } catch (InvalidRevisionCountException irce) {
        errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_INVALID_REVISION_COUNT));
        saveErrors(request, errorMessages);
        forward = mapping.findForward(ActionConstants.FAILURE);
      }

      scenario = refreshScenario(form);
      populateForm(form, scenario, true);
    }

    setReadOnlyFlag(request, form, scenario);
    
    return forward;
  }

  /**
   * @param form form
   * @param scenarioId 
   * @return NegativeMargin
   * @throws Exception On Exception
   */
  private NegativeMargin getNegativeMarginFromForm(final NegativeMarginFormLine form, Integer scenarioId) throws Exception {
    NegativeMargin negativeMargin = new NegativeMargin();
    negativeMargin.setNegativeMarginId(form.getNegativeMarginId());
    negativeMargin.setFarmingOperationId(form.getFarmingOperationId());
    negativeMargin.setInventoryItemCode(form.getInventoryItemCode());
    negativeMargin.setScenarioId(scenarioId);
    negativeMargin.setRevisionCount(form.getRevisionCount());
    negativeMargin.setInventory(form.getInventory());
    negativeMargin.setDeductiblePercentage(StringUtils.isBlank(form.getDeductiblePercentage()) ? null : new BigDecimal(form.getDeductiblePercentage()));
    negativeMargin.setInsurableValue(StringUtils.isBlank(form.getInsurableValue()) ? null : new BigDecimal(form.getInsurableValue()));
    negativeMargin.setInsurableValuePurchased(StringUtils.isBlank(form.getInsurableValuePurchased()) ? null : new BigDecimal(form.getInsurableValuePurchased()));
    negativeMargin.setReported(StringUtils.isBlank(form.getReported()) ? null : new BigDecimal(form.getReported()));
    negativeMargin.setGuaranteedProdValue(StringUtils.isBlank(form.getGuaranteedProdValue()) ? null : new BigDecimal(form.getGuaranteedProdValue()));
    negativeMargin.setPremiumsPaid(StringUtils.isBlank(form.getPremiumsPaid()) ? null : new BigDecimal(form.getPremiumsPaid()));
    negativeMargin.setClaimsReceived(StringUtils.isBlank(form.getClaimsReceived()) ? null : new BigDecimal(form.getClaimsReceived()));
    negativeMargin.setDeemedReceived(StringUtils.isBlank(form.getDeemedReceived()) ? null : new BigDecimal(form.getDeemedReceived()));
    negativeMargin.setDeemedPiValue(StringUtils.isBlank(form.getDeemedPiValue()) ? null : new BigDecimal(form.getDeemedPiValue()));
    negativeMargin.setPremiumRate(StringUtils.isBlank(form.getPremiumRate()) ? null : new BigDecimal(form.getPremiumRate()));
    negativeMargin.setClaimsCalculation(StringUtils.isBlank(form.getClaimsCalculation()) ? null : new BigDecimal(form.getClaimsCalculation()));
    negativeMargin.setPremium(StringUtils.isBlank(form.getPremium()) ? null : new BigDecimal(form.getPremium()));
    negativeMargin.setMrp(StringUtils.isBlank(form.getMrp()) ? null : new BigDecimal(form.getMrp()));
    negativeMargin.setDeemedPremium(StringUtils.isBlank(form.getDeemedPremium()) ? null : new BigDecimal(form.getDeemedPremium()));
    return negativeMargin;
  }
}
