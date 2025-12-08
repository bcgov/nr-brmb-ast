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

import java.util.List;

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
 * @author hwang
 */
public class MarketRatePremiumListViewAction extends SecureAction {

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

    logger.debug("Viewing Market Rate Premiums...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);

    MarketRatePremiumsForm form = (MarketRatePremiumsForm) actionForm;

    populateForm(form);

    return forward;
  }

  /**
   * @param form form
   * @throws Exception On Exception
   */
  protected void populateForm(MarketRatePremiumsForm form) throws Exception {
    CodesService service = ServiceFactory.getCodesService();
    List<MarketRatePremium> marketRatePremiums = service.getMarketRatePremiums();
    form.setMarketRatePremiums(marketRatePremiums);
    form.setNumMarketRatePremiums(marketRatePremiums.size());
  }

}
