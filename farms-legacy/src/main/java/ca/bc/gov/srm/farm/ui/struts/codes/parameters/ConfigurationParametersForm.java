package ca.bc.gov.srm.farm.ui.struts.codes.parameters;

import java.util.List;

import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.domain.codes.ConfigurationParameter;
import ca.bc.gov.srm.farm.list.CodeListView;

public class ConfigurationParametersForm extends ValidatorForm {
  private static final long serialVersionUID = 1L;
  
  private List<ConfigurationParameter> configurationParameters;
  private int numConfigurationParameters;
  private List<CodeListView> paramTypeCodesListItems;
  
  private Integer id;
  private String name;
  private String value;
  private String type;
  private String typeDescription;
  private boolean sensitiveDataInd;
  
  private boolean isNew = false;

  public List<ConfigurationParameter> getConfigurationParameters() {
    return configurationParameters;
  }

  public void setConfigurationParameters(List<ConfigurationParameter> configurationParameters) {
    this.configurationParameters = configurationParameters;
  }

  public int getNumConfigurationParameters() {
    return numConfigurationParameters;
  }

  public void setNumConfigurationParameters(int numConfigurationParameters) {
    this.numConfigurationParameters = numConfigurationParameters;
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
  
  public boolean getSensitiveDataInd() {
    return sensitiveDataInd;
  }

  public void setSensitiveDataInd(boolean sensitiveDataInd) {
    this.sensitiveDataInd = sensitiveDataInd;
  }

  public boolean isTipsParameter() {
    return ConfigurationKeys.isTipsConfigurationParameter(name);
  }
}