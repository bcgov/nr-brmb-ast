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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.codes.MarketRatePremium;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;

/**
 *
 * @authro hwang
 */
public class MarketRatePremiumViewAction extends SecureAction {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Viewing Market Rate Premiums...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);

    MarketRatePremiumsForm form = (MarketRatePremiumsForm) actionForm;

    populateForm(form, true);

    return forward;
  }


  protected void populateForm(MarketRatePremiumsForm form, boolean setUserInputValues) throws Exception {

    CodesService codesService = ServiceFactory.getCodesService();
    MarketRatePremium mrp = codesService.getMarketRatePremium(form.getMarketRatePremiumId());

    if (mrp != null) {
      form.setMarketRatePremiumId(mrp.getMarketRatePremiumId());
      form.setRevisionCount(mrp.getRevisionCount());

      if (setUserInputValues) {
        form.setMinTotalPremiumAmount(mrp.getMinTotalPremiumAmount().intValue());
        form.setMaxTotalPremiumAmount(mrp.getMaxTotalPremiumAmount().intValue());
        form.setRiskChargeFlatAmount(mrp.getRiskChargeFlatAmount().intValue());
        form.setRiskChargePercentagePremium(mrp.getRiskChargePercentagePremium().intValue());
        form.setAdjustChargeFlatAmount(mrp.getAdjustChargeFlatAmount().intValue());
      }
    }
  }
}
