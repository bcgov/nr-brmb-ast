/**
 *
 * Copyright (c) 2022,
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.InvalidRevisionCountException;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;


/**
 * @author awilkinson
 * @created May 27, 2022
 */
public class CopyProgramYearVersionAction extends ScenariosViewAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Copying Program Year Version...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    ScenariosForm form = (ScenariosForm) actionForm;
    Scenario scenario = getScenario(form);
    
    CalculatorService calculatorService = ServiceFactory.getCalculatorService();
    
    try {
      Integer newProgramYearVersionNumber = calculatorService.copyProgramYearVersion(scenario, getUserId());
      
      ActionMessages messages = new ActionMessages();
      messages.add("", new ActionMessage(MessageConstants.INFO_NEW_PROGRAM_YEAR_VERSION_CREATED, newProgramYearVersionNumber));
      request.setAttribute("infoMessages", messages);

      scenario = refreshScenario(form);

      populateForm(form, scenario);
      populateScenarioOptions(form, scenario);

    } catch(InvalidRevisionCountException irce) {
      handleInvalidRevisionCount(request);
      forward = mapping.findForward(ActionConstants.FAILURE);
    }
    
    setReadOnlyFlag(request, form, scenario);
    form.setAssignedToCurrentUser(isAssignedToCurrentUser(scenario));
    populateSelectBoxOptions(form, scenario, request);

    return forward;
  }

}
