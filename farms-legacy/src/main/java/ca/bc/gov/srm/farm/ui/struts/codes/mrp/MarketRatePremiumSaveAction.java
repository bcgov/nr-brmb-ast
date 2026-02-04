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
package ca.bc.gov.srm.farm.ui.struts.codes.mrp;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.codes.MarketRatePremium;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;

/**
 * @author hwang
 */
public class MarketRatePremiumSaveAction extends MarketRatePremiumViewAction {

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

    logger.debug("Saving MRP...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);

    MarketRatePremiumsForm form = (MarketRatePremiumsForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);
    populateForm(form, false);

    ActionMessages errorMessages = new ActionMessages();

    if (errors != null && !errors.isEmpty()) {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
    } else {

      String user = CurrentUser.getUser().getUserId();
      CodesService codesService = ServiceFactory.getCodesService();

      try {
        MarketRatePremium mrp = getMarketRatePremiumFromForm(form);

        if (form.isNew()) {
          codesService.createMarketRatePremium(mrp, user);
        } else {
          codesService.updateMarketRatePremium(mrp, user);
        }
      } catch (InvalidRevisionCountException irce) {
        errorMessages.add("", new ActionMessage(MessageConstants.ERRORS_INVALID_REVISION_COUNT));
        saveErrors(request, errorMessages);
        forward = mapping.findForward(ActionConstants.FAILURE);
      }
    }

    return forward;
  }

  /**
   * @param form form
   * @return MarketRatePremium
   * @throws Exception On Exception
   */
  private MarketRatePremium getMarketRatePremiumFromForm(MarketRatePremiumsForm form) throws Exception {

    MarketRatePremium mrp = new MarketRatePremium();
    mrp.setMarketRatePremiumId(form.getMarketRatePremiumId());
    mrp.setMinTotalPremiumAmount(BigDecimal.valueOf(form.getMinTotalPremiumAmount()));
    mrp.setMaxTotalPremiumAmount(BigDecimal.valueOf(form.getMaxTotalPremiumAmount()));
    mrp.setRiskChargeFlatAmount(BigDecimal.valueOf(form.getRiskChargeFlatAmount()));
    mrp.setRiskChargePercentagePremium(BigDecimal.valueOf(form.getRiskChargePercentagePremium()));
    mrp.setAdjustChargeFlatAmount(BigDecimal.valueOf(form.getAdjustChargeFlatAmount()));
    mrp.setRevisionCount(form.getRevisionCount());

    return mrp;
  }
}
