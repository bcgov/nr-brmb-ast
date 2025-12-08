/**
 * Copyright (c) 2022,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.codes.tips;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.domain.codes.TipFarmTypeIncomeRange;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.util.JsonUtils;

public class TipFarmTypeDefaultIncomeRangeSaveAction extends SecureAction {
  private Logger logger = LoggerFactory.getLogger(getClass());
  protected static ObjectMapper jsonObjectMapper = JsonUtils.getJsonObjectMapper();

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping, 
      final ActionForm actionForm,
      final HttpServletRequest request, 
      final HttpServletResponse response) throws Exception {

    logger.debug("Saving Farm Type Default Income Range...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);

    TipFarmTypeDefaultIncomeRangeForm form = (TipFarmTypeDefaultIncomeRangeForm) actionForm;
    ActionMessages errors = form.validate(mapping, request);

    if (errors != null && !errors.isEmpty()) {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
    } else {

      String user = CurrentUser.getUser().getUserId();
      CodesService codesService = ServiceFactory.getCodesService();
      ActionMessages errorMessages = new ActionMessages();

      try {
        List<TipFarmTypeIncomeRange> range = TipIncomeRangeFormDataUtil.getIncomeRangeList(form.getIncomeRangeJson(), jsonObjectMapper);
        if (!range.isEmpty()) {
          codesService.deleteFarmTypeDefaultIncomeRange();
          codesService.createFarmTypeDefaultIncomeRange(range, user);
        }
      } catch (Exception e) {
        saveErrors(request, errorMessages);
        forward = mapping.findForward(ActionConstants.FAILURE);
      }
    }

    return forward;
  }
}
