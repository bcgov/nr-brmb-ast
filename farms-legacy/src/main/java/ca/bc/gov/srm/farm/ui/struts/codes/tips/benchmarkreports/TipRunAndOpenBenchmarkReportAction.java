package ca.bc.gov.srm.farm.ui.struts.codes.tips.benchmarkreports;

import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;

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
import ca.bc.gov.srm.farm.util.StringUtils;

public class TipRunAndOpenBenchmarkReportAction extends SecureAction {
  
  private static DecimalFormat incomeFormat = new DecimalFormat("#");

  @Override
  public ActionForward doExecute(
    final ActionMapping mapping,
    final ActionForm actionForm, 
    final HttpServletRequest request,
    final HttpServletResponse response) throws Exception {

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    TipBencmarkReportsForm form = (TipBencmarkReportsForm) actionForm;
    
    final int level3 = 3;
    final int level2 = 2;
    Integer reportYear = form.getReportYear();
    Integer farmTypeLevel = null;
    String farmType = form.getFarmType();
    
    String farmType3Name = null;
    String farmType2Name = null;
    String farmType1Name = null;
    
    if(StringUtils.isNotBlank(form.getFarmTypeLevel())) {
      farmTypeLevel = Integer.valueOf(form.getFarmTypeLevel());
      if(farmTypeLevel == level3) {
        farmType3Name = farmType;
      } else if(farmTypeLevel == level2) {
        farmType2Name = farmType;
      } else {
        farmType1Name = farmType;
      }
    }
    
    Double incomeRangeLow = null;
    Double incomeRangeHigh = null;
    if(StringUtils.isNotBlank(form.getIncomeRangeLow())) {
      incomeRangeLow = Double.valueOf(form.getIncomeRangeLow());
    }
    if(StringUtils.isNotBlank(form.getIncomeRangeHigh())) {
      incomeRangeHigh = Double.valueOf(form.getIncomeRangeHigh());
    }
    
    TipReportService tipReportService = ServiceFactory.getTipReportService();
    if(TipReportService.REPORT_TYPE_BENCHMARK_SUMMARY.equals(form.getReportType())) {
      
      String reportContent = tipReportService.getBenchmarkSummaryReport(
          reportYear,
          farmType3Name,
          farmType2Name,
          farmType1Name,
          incomeRangeLow);
      
      String fileName =
          "benchmarkSummary" + "_" + reportYear
          + "_farmType" + farmTypeLevel
          + "_" + farmType
          + "_" + incomeFormat.format(incomeRangeLow)
          + ".csv";
      IOUtils.writeStringToResponse(response, IOUtils.CONTENT_TYPE_CSV, reportContent, fileName);
      
    } else if(TipReportService.REPORT_TYPE_BENCHMARK_EXTRACT.equals(form.getReportType())) {
      
      tipReportService.generateBenchmarkExtract(
          reportYear,
          farmType3Name,
          farmType2Name,
          farmType1Name,
          incomeRangeLow,
          incomeRangeHigh);
      
      Path benchmarkExtractZipFile = tipReportService.getBenchmarkExtractZipFile();
      
      String fileNamePrefix = "benchmarkExtract";
      String fileExtension = ".zip";
      String fileName = generateFileName(reportYear, farmTypeLevel, farmType, incomeRangeLow, incomeRangeHigh, fileNamePrefix, fileExtension);
      IOUtils.writeFileToResponse(response, benchmarkExtractZipFile, IOUtils.CONTENT_TYPE_ZIP, fileName);
      Files.delete(benchmarkExtractZipFile);

    } else if(TipReportService.REPORT_TYPE_GROUPING_CONFIG.equals(form.getReportType())) {
      
      Path reportFile = tipReportService.generateGroupingConfigReport();
      String fileName = "tipGroupingConfiguration.csv";
      IOUtils.writeFileToResponse(response, reportFile, IOUtils.CONTENT_TYPE_CSV, fileName);
      Files.delete(reportFile);
    }

    return forward;
  }

  private String generateFileName(Integer reportYear, Integer farmTypeLevel, String farmType, Double incomeRangeLow, Double incomeRangeHigh,
      String fileNamePrefix, String fileExtension) {
    String fileNameSuffix = "_" + reportYear;
    if(farmTypeLevel != null && StringUtils.isNotBlank(farmType)) {
      fileNameSuffix += "_farmType" + farmTypeLevel + "_" + farmType;
      
      fileNameSuffix += "_";
      if(incomeRangeLow != null) {
        fileNameSuffix += incomeFormat.format(incomeRangeLow);
      } else {
        fileNameSuffix += "0";
      }
      if(incomeRangeHigh != null) {
        fileNameSuffix += "-" + incomeFormat.format(incomeRangeHigh);
      } else {
        fileNameSuffix += "-infinity";
      }
    }
    String fileName = fileNamePrefix + fileNameSuffix + fileExtension;
    return fileName;
  }
}
