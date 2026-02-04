/**
 * Copyright (c) 2011,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.scenarios;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;

/**
 * @author awilkinson
 * @created Feb 1, 2011
 */
public class ScenariosCheckOutAction extends ScenariosViewAction {
  
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

    logger.debug("Assigning Program Year to user from Scenarios screen...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    ScenariosForm form = (ScenariosForm) actionForm;
    Scenario scenario = getScenario(form);

    scenario = checkOut(form, scenario, request);

    setReadOnlyFlag(request, form, scenario);
    form.setAssignedToCurrentUser(isAssignedToCurrentUser(scenario));
    populateScenarioOptions(form, scenario);
    populateForm(form, scenario);
    populateSelectBoxOptions(form, scenario, request);

    return forward;
  }
}
