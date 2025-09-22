package ca.bc.gov.srm.farm.ui.struts.report;

import java.io.File;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.service.ReportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.util.DateUtils;
import ca.bc.gov.srm.farm.util.IOUtils;


/**
 * AafmExtractAction - The extract returns a zip, so we can't use
 * Oracle reports. Generate the zip and return it as a stream to the browser.
 */
public class BackgroundReportFileViewAction extends SecureAction {
	private Logger log = LoggerFactory.getLogger(getClass());

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
    log.debug("<doExecute");

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    ReportForm form = (ReportForm) actionForm;
    ReportService service = ServiceFactory.getReportService();
    String reportType = form.getReportType();
    String reportFileName = service.getBackgroundReportFileName(reportType);

    File tempDir = IOUtils.getTempDir(); 

    File reportOutputFile = new File(tempDir, reportFileName);
    String fileDateString = null;
    if(reportOutputFile.exists()) {
      Date lastModified = new Date(reportOutputFile.lastModified());
      fileDateString = DateUtils.formatDateTime(lastModified);
    } else {
      service.readReportStatusFile(form);
    }
    
    form.setReportFileName(reportFileName);
    form.setReportFileDate(fileDateString);

    log.debug("report file last modified date: " + fileDateString);

    log.debug(">doExecute");
    return forward;
  }
  
  
}
