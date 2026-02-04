package ca.bc.gov.srm.farm.ui.struts.codes.tips.benchmarks;

import java.util.List;

import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.domain.codes.TipBenchmarkInfo;

public class TipBenchmarksForm extends ValidatorForm {
  
  private static final long serialVersionUID = 1;
  
  private List<TipBenchmarkInfo> benchmarkInfos;
  
  private Integer year;
  
  private Integer latestBenchmarkYear;
  
  private boolean benchmarksMatchConfig;

  public List<TipBenchmarkInfo> getBenchmarkInfos() {
    return benchmarkInfos;
  }

  public void setBenchmarkInfos(List<TipBenchmarkInfo> benchmarkInfos) {
    this.benchmarkInfos = benchmarkInfos;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public boolean isBenchmarksMatchConfig() {
    return benchmarksMatchConfig;
  }

  public void setBenchmarksMatchConfig(boolean benchmarksMatchConfig) {
    this.benchmarksMatchConfig = benchmarksMatchConfig;
  }

  public Integer getLatestBenchmarkYear() {
    return latestBenchmarkYear;
  }

  public void setLatestBenchmarkYear(Integer latestBenchmarkYear) {
    this.latestBenchmarkYear = latestBenchmarkYear;
  }
  
}
