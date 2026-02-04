package ca.bc.gov.srm.farm.ui.struts.codes.tips.benchmarkreports;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.service.ReportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.service.TipReportService;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * Generate a report in the background.
 * The resulting output file may be retrieved later using ReportDownloadAction
 */
public class TipBackgroundReportGenerateAction extends SecureAction {

  @Override
  public ActionForward doExecute(
  	final ActionMapping mapping,
    final ActionForm actionForm, 
    final HttpServletRequest request,
    final HttpServletResponse response) throws Exception {

    TipBencmarkReportsForm form = (TipBencmarkReportsForm) actionForm;
    
    final int level3 = 3;
    final int level2 = 2;
    String farmType3Name = null;
    String farmType2Name = null;
    String farmType1Name = null;
    
    if(StringUtils.isNotBlank(form.getFarmTypeLevel())) {
      Integer farmTypeLevel = Integer.valueOf(form.getFarmTypeLevel());
      if(farmTypeLevel == level3) {
        farmType3Name = form.getFarmType();
      } else if(farmTypeLevel == level2) {
        farmType2Name = form.getFarmType();
      } else {
        farmType1Name = form.getFarmType();
      }
    }
    
    TipReportService tipReportService = ServiceFactory.getTipReportService();
    ReportService reportService = ServiceFactory.getReportService();
    
    if(TipReportService.REPORT_TYPE_BENCHMARK_EXTRACT.equals(form.getReportType())) {
      reportService.writeReportStatusFile(form.getReportType());
      
      Double incomeRangeLow = null;
      Double incomeRangeHigh = null;
      
      if(StringUtils.isNotBlank(form.getIncomeRangeLow())) {
        incomeRangeLow = Double.valueOf(form.getIncomeRangeLow());
      }
      if(StringUtils.isNotBlank(form.getIncomeRangeHigh())) {
        incomeRangeHigh = Double.valueOf(form.getIncomeRangeHigh());
      }
      
      tipReportService.generateBenchmarkExtract(
          form.getReportYear(),
          farmType3Name,
          farmType2Name,
          farmType1Name,
          incomeRangeLow,
          incomeRangeHigh);
    }

    return null;
  }
}
