package ca.bc.gov.srm.farm.ui.struts.report;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.service.ReportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.ui.struts.message.MessageConstants;

/**
 * Generate a report in the background.
 * The resulting output file may be retrieved later using ReportDownloadAction
 */
public class BackgroundReportGenerateAction extends SecureAction {

  /**
   * doExecute.
   *
   * @param   mapping     mapping
   * @param   actionForm  actionForm
   * @param   request     request
   * @param   response    response
   *
   * @return  The return value
   *
   * @throws  Exception  On Exception
   */
  @Override
  public ActionForward doExecute(
  	final ActionMapping mapping,
    final ActionForm actionForm, 
    final HttpServletRequest request,
    final HttpServletResponse response) throws Exception {

    if (!isTokenValid(request, true)) {
      ActionMessages errors = new ActionMessages();
      String key = MessageConstants.ERRORS_DUPLICATE_SUBMIT;
      errors.add("", new ActionMessage(key));
      addErrors(request, errors);
      return mapping.findForward(ActionConstants.FAILURE);
    }

    ReportForm form = (ReportForm) actionForm;
    ReportService service = ServiceFactory.getReportService();
    String reportType = form.getReportType();
    
    if(reportType.equals(ReportService.REPORT_NATIONAL_SURVEILLANCE_STRATEGY)) {
      Integer year = Integer.valueOf(form.getYear());
      service.generateNationalSurveillanceStrategy(year);
    } else if(reportType.equals(ReportService.REPORT_ANALYTICAL_SURVEILLANCE_STRATEGY)) {
      Integer year = Integer.valueOf(form.getYear());
      service.generateAnalyticalSurveillanceStrategy(year);
    }

    return null;
  }
}
