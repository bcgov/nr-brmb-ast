package ca.bc.gov.srm.farm.ui.struts.codes.tips.benchmarkreports;

import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.ui.struts.report.BackgroundReportForm;

public class TipBencmarkReportsForm extends ValidatorForm implements BackgroundReportForm {
	
  private static final long serialVersionUID = -5209522543699372420L;
  
  private String reportType;
  private Integer reportYear;
  private String farmTypeLevel;
  private String farmType;
  private String incomeRangeLow;
  private String incomeRangeHigh;
  
  private String benchmarkGroupsJson;
  
  private String requestorAccountName;
  private String reportRequestDate;

  @Override
  public String getReportType() {
    return reportType;
  }

  public void setReportType(String reportName) {
    this.reportType = reportName;
  }

  public Integer getReportYear() {
    return reportYear;
  }

  public void setReportYear(Integer reportYear) {
    this.reportYear = reportYear;
  }

  public String getFarmTypeLevel() {
    return farmTypeLevel;
  }

  public void setFarmTypeLevel(String farmTypeLevel) {
    this.farmTypeLevel = farmTypeLevel;
  }

  public String getFarmType() {
    return farmType;
  }

  public void setFarmType(String farmType) {
    this.farmType = farmType;
  }

  public String getIncomeRangeLow() {
    return incomeRangeLow;
  }

  public void setIncomeRangeLow(String incomeRangeLow) {
    this.incomeRangeLow = incomeRangeLow;
  }

  public String getIncomeRangeHigh() {
    return incomeRangeHigh;
  }

  public void setIncomeRangeHigh(String incomeRangeHigh) {
    this.incomeRangeHigh = incomeRangeHigh;
  }

  public String getBenchmarkGroupsJson() {
    return benchmarkGroupsJson;
  }

  public void setBenchmarkGroupsJson(String benchmarkGroupsJson) {
    this.benchmarkGroupsJson = benchmarkGroupsJson;
  }

  @Override
  public String getRequestorAccountName() {
    return requestorAccountName;
  }

  @Override
  public void setRequestorAccountName(String requestorAccountName) {
    this.requestorAccountName = requestorAccountName;
  }

  @Override
  public String getReportRequestDate() {
    return reportRequestDate;
  }

  @Override
  public void setReportRequestDate(String reportRequestDate) {
    this.reportRequestDate = reportRequestDate;
  }
	
}