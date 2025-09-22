package ca.bc.gov.srm.farm.ui.struts.codes.year.parameters;

import java.util.List;

import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.domain.codes.YearConfigurationParameter;
import ca.bc.gov.srm.farm.list.CodeListView;
import ca.bc.gov.srm.farm.list.ListView;

public class YearConfigurationParametersForm extends ValidatorForm {
  private static final long serialVersionUID = 1L;
  
  private List<YearConfigurationParameter> yearConfigurationParameters;
  private int numYearConfigurationParameters;
  private List<CodeListView> paramTypeCodesListItems;
  
  private List<ListView> programYearSelectOptions;
  private Integer yearFilter;
  
  private Integer id;
  private Integer programYear;
  private String name;
  private String value;
  private String type;
  private String typeDescription;
  
  private boolean isNew = false;

  public List<YearConfigurationParameter> getYearConfigurationParameters() {
    return yearConfigurationParameters;
  }

  public void setYearConfigurationParameters(List<YearConfigurationParameter> yearConfigurationParameters) {
    this.yearConfigurationParameters = yearConfigurationParameters;
  }

  public int getNumYearConfigurationParameters() {
    return numYearConfigurationParameters;
  }

  public void setNumYearConfigurationParameters(int numYearConfigurationParameters) {
    this.numYearConfigurationParameters = numYearConfigurationParameters;
  }

  public boolean isNew() {
    return isNew;
  }

  public void setNew(boolean isNew) {
    this.isNew = isNew;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getTypeDescription() {
    return typeDescription;
  }

  public void setTypeDescription(String typeDescription) {
    this.typeDescription = typeDescription;
  }

  public List<CodeListView> getParamTypeCodesListItems() {
    return paramTypeCodesListItems;
  }

  public void setParamTypeCodesListItems(List<CodeListView> paramTypeCodesListItems) {
    this.paramTypeCodesListItems = paramTypeCodesListItems;
  }
  
  public boolean isTipsParameter() {
    return ConfigurationKeys.isTipsConfigurationParameter(name);
  }

  public Integer getProgramYear() {
    return programYear;
  }

  public void setProgramYear(Integer programYear) {
    this.programYear = programYear;
  }

  public List<ListView> getProgramYearSelectOptions() {
    return programYearSelectOptions;
  }

  public void setProgramYearSelectOptions(List<ListView> programYearSelectOptions) {
    this.programYearSelectOptions = programYearSelectOptions;
  }

  public Integer getYearFilter() {
    return yearFilter;
  }

  public void setYearFilter(Integer yearFilter) {
    this.yearFilter = yearFilter;
  }
}