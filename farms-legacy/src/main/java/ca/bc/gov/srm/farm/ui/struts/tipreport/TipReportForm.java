package ca.bc.gov.srm.farm.ui.struts.tipreport;

import java.util.List;

import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.domain.tips.TipFarmingOperation;

public class TipReportForm extends ValidatorForm {

  private static final long serialVersionUID = -1019093062992166707L;
  
  public static final String TIP_REPORT_STATUS_FILTER_GENERATED = "GENERATED";
  
  private boolean benchmarkDataGenerated;
  private boolean jobInProgress;
  
  private List<ListView> yearSelectOptions;
  private List<TipFarmingOperation> farmOps;
  
  private Integer year;
  private int numFarmOps;
  
  private String farmingOperationIds;
  
  private String participantPins;
  private Boolean isTipParticipant;
  
  private String reportStatusFilter;
  
  private String pinFilter;
  
  private String tipParticipantIndFilter;
  
  private String agriStabilityParticipantIndFilter;
  
  private boolean benchmarksMatchConfig;

  private Integer tipReportDocId;
  
  private Integer farmingOperationId;
  
  private Integer importVersionId;
  
  private String tipReportErrorText;
  
  public boolean isBenchmarkDataGenerated() {
    return benchmarkDataGenerated;
  }

  public void setBenchmarkDataGenerated(boolean benchmarkDataGenerated) {
    this.benchmarkDataGenerated = benchmarkDataGenerated;
  }

  public List<ListView> getYearSelectOptions() {
    return yearSelectOptions;
  }

  public void setYearSelectOptions(List<ListView> yearSelectOptions) {
    this.yearSelectOptions = yearSelectOptions;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public String getFarmingOperationIds() {
    return farmingOperationIds;
  }

  public void setFarmingOperationIds(String farmingOperationIds) {
    this.farmingOperationIds = farmingOperationIds;
  }

  public List<TipFarmingOperation> getFarmOps() {
    return farmOps;
  }

  public void setFarmOps(List<TipFarmingOperation> farmOps) {
    this.farmOps = farmOps;
  }

  public int getNumFarmOps() {
    return numFarmOps;
  }

  public void setNumFarmOps(int numFarmOps) {
    this.numFarmOps = numFarmOps;
  }

  public String getReportStatusFilter() {
    return reportStatusFilter;
  }

  public void setReportStatusFilter(String reportStatusFilter) {
    this.reportStatusFilter = reportStatusFilter;
  }

  public boolean isJobInProgress() {
    return jobInProgress;
  }

  public void setJobInProgress(boolean jobInProgress) {
    this.jobInProgress = jobInProgress;
  }

  public boolean isBenchmarksMatchConfig() {
    return benchmarksMatchConfig;
  }

  public void setBenchmarksMatchConfig(boolean benchmarksMatchConfig) {
    this.benchmarksMatchConfig = benchmarksMatchConfig;
  }

  public String getPinFilter() {
    return pinFilter;
  }

  public void setPinFilter(String pinFilter) {
    this.pinFilter = pinFilter;
  }

  public Integer getTipReportDocId() {
    return tipReportDocId;
  }

  public void setTipReportDocId(Integer tipReportDocId) {
    this.tipReportDocId = tipReportDocId;
  }

  public Integer getFarmingOperationId() {
    return farmingOperationId;
  }

  public void setFarmingOperationId(Integer farmingOperationId) {
    this.farmingOperationId = farmingOperationId;
  }

  public Integer getImportVersionId() {
    return importVersionId;
  }

  public void setImportVersionId(Integer importVersionId) {
    this.importVersionId = importVersionId;
  }

  public String getParticipantPins() {
    return participantPins;
  }

  public void setParticipantPins(String participantPins) {
    this.participantPins = participantPins;
  }

  public Boolean getIsTipParticipant() {
    return isTipParticipant;
  }

  public void setIsTipParticipant(Boolean isTipParticipant) {
    this.isTipParticipant = isTipParticipant;
  }

  public String getTipParticipantIndFilter() {
    return tipParticipantIndFilter;
  }

  public void setTipParticipantIndFilter(String tipParticipantIndFilter) {
    this.tipParticipantIndFilter = tipParticipantIndFilter;
  }

  public String getAgriStabilityParticipantIndFilter() {
    return agriStabilityParticipantIndFilter;
  }

  public void setAgriStabilityParticipantIndFilter(String agriStabilityFilter) {
    this.agriStabilityParticipantIndFilter = agriStabilityFilter;
  }

  public String getTipReportErrorText() {
    return tipReportErrorText;
  }

  public void setTipReportErrorText(String tipReportErrorText) {
    this.tipReportErrorText = tipReportErrorText;
  }
}
