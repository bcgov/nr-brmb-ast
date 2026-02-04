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
package ca.bc.gov.srm.farm.ui.struts.calculator.status;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.domain.codes.ScenarioStateCodes;
import ca.bc.gov.srm.farm.domain.codes.ScenarioTypeCodes;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.security.SecurityActionConstants;
import ca.bc.gov.srm.farm.service.CdogsService;
import ca.bc.gov.srm.farm.service.ReportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.cache.CurrentUser;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.calculator.CalculatorForm;

/**
 * @author awilkinson
 * @created Feb 15, 2011
 */
public class BenefitDocumentRegenerateAction extends CalculatorStatusViewAction {
  
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

    logger.debug("Regenerating Cob...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    CalculatorStatusForm form = (CalculatorStatusForm) actionForm;
    Scenario scenario = getScenario(form);
    
    if (scenario.isCoverageNotice()) {
      regenerateCoverageNotice(request, form, scenario);
    } else {
      regenerateCob(request, form, scenario);
    }
    populateForm(form, scenario);

    return forward;
  }

  private void regenerateCoverageNotice(final HttpServletRequest request, CalculatorStatusForm form, Scenario scenario)
      throws Exception, ServiceException {
    
    CdogsService cdogsService = ServiceFactory.getCdogsService();
    String userId = CurrentUser.getUser().getUserId();
    cdogsService.createCdogsCoverageNoticeReport(scenario, userId);
    
    scenario.setBenefitDocCreatedDate(new java.util.Date());
    form.setReportUrl("viewBenefitDocument.do");
    
    addScenarioLog(form, scenario, "Reprint Coverage Notice");

    setReadOnlyFlag(request, form, scenario);
  }
  
  private void regenerateCob(final HttpServletRequest request, CalculatorStatusForm form, Scenario scenario)
      throws Exception, ServiceException {
    
    ReportService service = ServiceFactory.getReportService();
    String userId = CurrentUser.getUser().getUserId();
    
    //
    // save the report to a blob in the database
    //
    service.updateCob(scenario.getScenarioId(), scenario.getYear(), userId);
    
    //
    // return to the same screen, put the URL to view the COB in the 
    // "report URL" so that when the screen loads it pops up another
    // browser that reads the blob into the response.
    //
    scenario.setBenefitDocCreatedDate(new java.util.Date());
    form.setReportUrl("viewBenefitDocument.do");
    
    addScenarioLog(form, scenario, "Reprint COB");

    setReadOnlyFlag(request, form, scenario);
  }

  /**
   * @param request request
   * @param form form
   * @param scenario Scenario
   * @throws Exception On Exception
   */
  @Override
  protected void setReadOnlyFlag(
      HttpServletRequest request,
      CalculatorForm form,
      Scenario scenario) throws Exception {
    form.setReadOnly(super.isReadOnly(request, form, scenario));
    form.setCanModifyScenario(canModifyScenario(request, form, scenario));
  }

  /**
   * @param request request
   * @param form form
   * @param scenario Scenario
   * @return boolean
   * @throws Exception On Exception
   */
  @Override
  protected boolean isReadOnly(
      HttpServletRequest request,
      CalculatorForm form,
      Scenario scenario) throws Exception {

    boolean result;

    boolean authorizedAction = canPerformAction(request, SecurityActionConstants.REGENERATE_COB);
    boolean userScenario = scenario.getScenarioTypeCode().equals(ScenarioTypeCodes.USER);
    boolean assignedTo = isAssignedToCurrentUser(scenario);
    boolean verified = ScenarioStateCodes.VERIFIED.equals(scenario.getScenarioStateCode());
    boolean amended = ScenarioStateCodes.AMENDED.equals(scenario.getScenarioStateCode());
    boolean allowedStatus = verified || amended;

    if(authorizedAction && userScenario && assignedTo && allowedStatus) {
      result = false;
    } else {
      result = true;
    }
    
    return result;
  }

}
