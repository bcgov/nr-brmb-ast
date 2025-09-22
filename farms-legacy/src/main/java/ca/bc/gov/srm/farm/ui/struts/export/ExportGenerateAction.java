package ca.bc.gov.srm.farm.ui.struts.export;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.list.CodeListView;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.service.ReportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.service.impl.ExportServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.util.IOUtils;
import ca.bc.gov.srm.farm.util.ProgramYearUtils;


/**
 * ExportGenerateAction - Action to generate reports. Note that at MOE the
 * safest thing is to just create the URL for the export, and have a browser
 * call that URL. Use Oracle repports to generate the CSV.
 */
public class ExportGenerateAction extends SecureAction {

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
    setYearSelectOptions(form);
    
    ReportService service = ServiceFactory.getReportService();
    ActionMessages errors = service.validateForm(mapping, form, request);
    String exportType = form.getExportType();

    if (errors.isEmpty()) {

      if(exportType.equals(ReportService.REPORT_600)) {
        String url = "detailedScenarioExtract.do?year="+form.getYear();
        form.setExportUrl(url);
        return forward;
      }
      
      if(exportType.equals(ReportService.REPORT_STA)) {
        String url = "statementAExtract.do?year="+form.getYear();
        form.setExportUrl(url);
        return forward;
      }
      
      if(exportType.equals(ReportService.REPORT_AAFM) || exportType.equals(ReportService.REPORT_AAFMA)) {
        service.writeReportStatusFile(exportType);
        File tempDir =  IOUtils.getTempDir(); 
        File zipFile = new File(tempDir,  (ReportService.REPORT_AAFMA.equals(exportType)) ? ExportServiceFactory.ZIP_FILE_NAME_AAFMA : ExportServiceFactory.ZIP_FILE_NAME_AAFM);
        if(zipFile.exists()) {
          zipFile.delete();
        }
      }

    	//
    	// Normally exports are just Oracle reports as CSVs.
    	// The AAFM extract however is a zip file of several CSVs which
    	// couldn't be created using Oracle reports.
    	//
      //String url = "aafmExtract.do?year="+form.getYear()+"&exportType="+exportType;
      String url = "aafmExtract.do?exportType="+exportType+"&year="+form.getYear();
      form.setExportUrl(url);
    } else {
      saveErrors(request, errors);
      forward = mapping.findForward(ActionConstants.FAILURE);
    }

    return forward;
  }

  private void setYearSelectOptions(ExportForm form) {
    List<ListView> yearSelectOptions = new ArrayList<>();
    Integer latestAdminYear = ProgramYearUtils.getLatestAdminYear();
    final Integer earliestYear = 2023;
    
    for (Integer year = latestAdminYear; year >= earliestYear; year--) {
      ListView lv = new CodeListView(year.toString(), year.toString());
      yearSelectOptions.add(lv);
    }
   
    form.setYearSelectOptions(yearSelectOptions);
  }
}
