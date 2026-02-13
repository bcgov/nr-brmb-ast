package ca.bc.gov.srm.farm.ui.struts.benefit.triage;

import java.util.List;

import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.domain.benefit.triage.BenefitTriageStatus;
import ca.bc.gov.srm.farm.list.ListView;

public class BenefitTriageStatusForm extends ValidatorForm {

  private static final long serialVersionUID = -1117656610503380741L;

  private Integer year;

  private List<BenefitTriageStatus> statusResults;
  private List<ListView> programYearSelectOptions;
  private List<ListView> scenarioStateSelectOptions;

  private String pinFilter;
  private String nameFilter;
  private String statusFilter;
  private String isPaymentFileFilter;

  public List<BenefitTriageStatus> getStatusResults() {
    return statusResults;
  }

  public void setStatusResults(List<BenefitTriageStatus> statusResults) {
    this.statusResults = statusResults;
  }

  public int getNumStatusResults() {
    return statusResults.size();
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public List<ListView> getProgramYearSelectOptions() {
    return programYearSelectOptions;
  }

  public void setProgramYearSelectOptions(List<ListView> programYearSelectOptions) {
    this.programYearSelectOptions = programYearSelectOptions;
  }

  public List<ListView> getScenarioStateSelectOptions() {
    return scenarioStateSelectOptions;
  }

  public void setScenarioStateSelectOptions(List<ListView> scenarioStateSelectOptions) {
    this.scenarioStateSelectOptions = scenarioStateSelectOptions;
  }

  public String getPinFilter() {
    return pinFilter;
  }

  public void setPinFilter(String pinFilter) {
    this.pinFilter = pinFilter;
  }

  public String getNameFilter() {
    return nameFilter;
  }

  public void setNameFilter(String nameFilter) {
    this.nameFilter = nameFilter;
  }

  public String getStatusFilter() {
    return statusFilter;
  }

  public void setStatusFilter(String statusFilter) {
    this.statusFilter = statusFilter;
  }

  public String getIsPaymentFileFilter() {
    return isPaymentFileFilter;
  }

  public void setIsPaymentFileFilter(String isPaymentFileFilter) {
    this.isPaymentFileFilter = isPaymentFileFilter;
  }

}
