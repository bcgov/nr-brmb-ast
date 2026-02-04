package ca.bc.gov.srm.farm.ui.struts.codes.tips.benchmarkreports;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.service.ReportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.service.TipReportService;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.util.DateUtils;
import ca.bc.gov.srm.farm.util.IOUtils;
import ca.bc.gov.srm.farm.util.JsonUtils;

public class TipBenchmarkReportStatusAction extends SecureAction {

  @Override
  public ActionForward doExecute(
    final ActionMapping mapping,
    final ActionForm actionForm, 
    final HttpServletRequest request,
    final HttpServletResponse response) throws Exception {

    TipBencmarkReportsForm form = (TipBencmarkReportsForm) actionForm;
    
    ReportService reportService = ServiceFactory.getReportService();
    TipReportService tipReportService = ServiceFactory.getTipReportService();

    reportService.readReportStatusFile(form);
    String reportType = form.getReportType();
    
    if(TipReportService.REPORT_TYPE_BENCHMARK_EXTRACT.equals(reportType)) {
      Path reportFilePath = tipReportService.getBenchmarkExtractZipFile();
  
      String fileDateString = null;
      String reportRequestDateString = form.getReportRequestDate();
      if(Files.exists(reportFilePath)) {
        Date lastModified = new Date(reportFilePath.toFile().lastModified());
        fileDateString = DateUtils.formatDateTime(lastModified);
      } else {
        reportService.readReportStatusFile(form);
      }
      
      ResponseObject responseObj = new ResponseObject();
      responseObj.setFileName(reportFilePath.getFileName().toString());
      responseObj.setReportFileDate(fileDateString);
      responseObj.setReportRequestDate(reportRequestDateString);
      responseObj.setRequestorAccountName(form.getRequestorAccountName());
      
      
      String responseJson = JsonUtils.getJsonObjectMapper().writeValueAsString(responseObj);
  
      IOUtils.writeJsonToResponse(response, responseJson);
    }
    
    return null;
  }
  
  class ResponseObject {
    String fileName;
    String reportFileDate;
    String reportRequestDate;
    String requestorAccountName;
    
    public String getFileName() {
      return fileName;
    }
    public void setFileName(String fileName) {
      this.fileName = fileName;
    }
    public String getReportFileDate() {
      return reportFileDate;
    }
    public void setReportFileDate(String reportFileDate) {
      this.reportFileDate = reportFileDate;
    }
    public String getReportRequestDate() {
      return reportRequestDate;
    }
    public void setReportRequestDate(String reportRequestDate) {
      this.reportRequestDate = reportRequestDate;
    }
    public String getRequestorAccountName() {
      return requestorAccountName;
    }
    public void setRequestorAccountName(String requestorAccountName) {
      this.requestorAccountName = requestorAccountName;
    }
  }
}
