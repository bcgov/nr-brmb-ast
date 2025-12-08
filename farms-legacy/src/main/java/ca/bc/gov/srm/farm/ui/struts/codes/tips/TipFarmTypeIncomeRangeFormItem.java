package ca.bc.gov.srm.farm.ui.struts.codes.tips;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class TipFarmTypeIncomeRangeFormItem {
  private String isDefault;
  private String rangeLow;
  private String rangeHigh;
  private String minimumGroupCount;
  private String id;
  
  public String getIsDefault() {
    return isDefault;
  }
  
  public void setIsDefault(String isDefault) {
    this.isDefault = isDefault;
  }
  
  public String getRangeLow() {
    return rangeLow;
  }
  
  public void setRangeLow(String rangeLow) {
    this.rangeLow = rangeLow;
  }
  
  public String getRangeHigh() {
    return rangeHigh;
  }
  
  public void setRangeHigh(String rangeHigh) {
    this.rangeHigh = rangeHigh;
  }
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }

  public String getMinimumGroupCount() {
    return minimumGroupCount;
  }

  public void setMinimumGroupCount(String minimumGroupCount) {
    this.minimumGroupCount = minimumGroupCount;
  }
}
