package ca.bc.gov.srm.farm.ui.struts.codes.tips.benchmarkreports;

import java.nio.file.Files;
import java.nio.file.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.service.TipReportService;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.util.IOUtils;

public class TipBenchmarkReportDownloadAction extends SecureAction {

  @Override
  public ActionForward doExecute(
    final ActionMapping mapping,
    final ActionForm actionForm, 
    final HttpServletRequest request,
    final HttpServletResponse response) throws Exception {

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    TipReportService tipReportService = ServiceFactory.getTipReportService();
    TipBencmarkReportsForm form = (TipBencmarkReportsForm) actionForm;
    
    if(TipReportService.REPORT_TYPE_BENCHMARK_EXTRACT.equals(form.getReportType())) {
      Path extractFile = tipReportService.getBenchmarkExtractZipFile();
      
      if(Files.exists(extractFile)) {
        IOUtils.writeFileToResponse(response, extractFile, IOUtils.CONTENT_TYPE_ZIP);
      }
    }

    return forward;
  }
}
