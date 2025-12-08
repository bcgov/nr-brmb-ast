/**
 * Copyright (c) 2021,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.preverification;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.domain.PreVerificationChecklist;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorAction;
import ca.bc.gov.srm.farm.util.JsonUtils;

/**
 * Action for screen 960
 */
public class PreVerificationViewAction extends CalculatorAction {

  private static ObjectMapper jsonObjectMapper = JsonUtils.getJsonObjectMapper();

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    PreVerificationForm form = (PreVerificationForm) actionForm;
    Scenario scenario = getScenario(form);
    
    setReadOnlyFlag(request, form, scenario);
    populateForm(form, scenario);
    
    return forward;
  }


  protected void populateForm(PreVerificationForm form,
      Scenario scenario)
  throws Exception {
    
    PreVerificationChecklist checklist = scenario.getPreVerificationChecklist();
    
    String resultsJson = jsonObjectMapper.writeValueAsString(checklist);
    form.setChecklistJson(resultsJson);
  }

}
