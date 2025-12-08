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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.chefs.forms.CoverageFormConstants;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.service.ReportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * @author awilkinson
 * @created Feb 15, 2011
 */
public class BenefitDocumentViewAction extends CalculatorStatusViewAction {
  
  private Logger logger = LoggerFactory.getLogger(getClass());
  private String PDF_REPORT_FILENAME_FORMAT = "%s_%s_%s_Report.pdf";

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

    logger.debug("Viewing BenefitDocument ...");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    
    CalculatorStatusForm form = (CalculatorStatusForm) actionForm;
    Scenario scenario = getScenario(form);
    
    String fileName;
    if (scenario.isCoverageNotice()) {
      fileName = StringUtils.formatWithNullAsEmptyString(PDF_REPORT_FILENAME_FORMAT, form.getYear(), CoverageFormConstants.FORM_LONG_NAME,
          form.getPin());
    } else {
      fileName = StringUtils.formatWithNullAsEmptyString(PDF_REPORT_FILENAME_FORMAT, form.getYear(), "Calculation of Benefits Notice",
          form.getPin());
    }
    
    ReportService service = ServiceFactory.getReportService();
    
    Integer scenarioId = scenario.getScenarioId();
    service.writeCobToResponse(scenarioId, response, fileName);

    return forward;
  }

}
