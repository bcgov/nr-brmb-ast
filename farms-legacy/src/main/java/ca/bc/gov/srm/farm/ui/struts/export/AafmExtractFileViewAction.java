package ca.bc.gov.srm.farm.ui.struts.export;

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
import ca.bc.gov.srm.farm.service.impl.ExportServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.util.DateUtils;
import ca.bc.gov.srm.farm.util.IOUtils;


/**
 * AafmExtractAction - The extract returns a zip, so we can't use
 * Oracle reports. Generate the zip and return it as a stream to the browser.
 */
public class AafmExtractFileViewAction extends SecureAction {
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

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    ExportForm form = (ExportForm) actionForm;
    ReportService service = ServiceFactory.getReportService();
    
    String exportType = form.getExportType();

    final String exportFileName = (ReportService.REPORT_AAFMA.equals(exportType)) ? ExportServiceFactory.ZIP_FILE_NAME_AAFMA : ExportServiceFactory.ZIP_FILE_NAME_AAFM;
    File tempDir = IOUtils.getTempDir(); 
    File zip = new File(tempDir, exportFileName);
    String fileDateString = null;

    if(zip.exists()) {
      Date lastModified = new Date(zip.lastModified());
      fileDateString = DateUtils.formatDateTime(lastModified);
    } else {
      service.readReportStatusFile(form);
    }

    form.setExportFileName(exportFileName);
    form.setExportFileDate(fileDateString);
    log.debug("zip file last modified date: " + fileDateString);
    
    return forward;
  }
  
  
}
