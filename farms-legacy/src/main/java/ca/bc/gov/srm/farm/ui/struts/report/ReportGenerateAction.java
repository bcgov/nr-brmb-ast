package ca.bc.gov.srm.farm.ui.struts.report;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.service.ReportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.util.IOUtils;


/**
 * ReportCriteriaAction - Action to generate reports. Note that at MOE the
 * safest thing is to just create the URL for the report, and have a browser
 * call that URL.
 */
public class ReportGenerateAction extends SecureAction {

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

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    ReportForm form = (ReportForm) actionForm;
    ReportService service = ServiceFactory.getReportService();
    ActionMessages errors = service.validateForm(mapping, form, request);
    
    String reportType = form.getReportType();
    if(reportType.equals(ReportService.REPORT_NATIONAL_SURVEILLANCE_STRATEGY)
        || reportType.equals(ReportService.REPORT_ANALYTICAL_SURVEILLANCE_STRATEGY)) {

      String reportFileName = service.getBackgroundReportFileName(reportType);
      File tempDir =  IOUtils.getTempDir(); 
      File zipFile = new File(tempDir, reportFileName);
      if(zipFile.exists()) {
        zipFile.delete();
      }
    }

    if (errors.isEmpty()) {
      String url = service.getReportUrl(form);
      form.setReportUrl(url);
    } else {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
    }

    return forward;
  }
}
