package ca.bc.gov.srm.farm.domain.codes;

public class TipFarmTypeIncomeRange extends AbstractCode{
  public static final String MAX_RANGE_HIGH_VAL = "9999999999";
  
  private Boolean isDefault;
  private Boolean isInherited;
  private String inheritedFrom;
  private Integer inheritedFromId;
  
  private Double rangeLow;
  private Double rangeHigh;
  private Integer minimumGroupCount;
  private Integer id;
  
  public Boolean getIsDefault() {
    return isDefault;
  }
  
  public void setIsDefault(Boolean isDefault) {
    this.isDefault = isDefault;
  }
  
  public Double getRangeLow() {
    return rangeLow;
  }
  
  public void setRangeLow(Double rangeLow) {
    this.rangeLow = rangeLow;
  }
  
  public Double getRangeHigh() {
    return rangeHigh;
  }
  
  public void setRangeHigh(Double rangeHigh) {
    this.rangeHigh = rangeHigh;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Boolean getIsInherited() {
    return isInherited;
  }

  public void setIsInherited(Boolean isInherited) {
    this.isInherited = isInherited;
  }

  public String getInheritedFrom() {
    return inheritedFrom;
  }

  public void setInheritedFrom(String inheritedFrom) {
    this.inheritedFrom = inheritedFrom;
  }

  public Integer getInheritedFromId() {
    return inheritedFromId;
  }

  public void setInheritedFromId(Integer inheritedFromId) {
    this.inheritedFromId = inheritedFromId;
  }

  public Integer getMinimumGroupCount() {
    return minimumGroupCount;
  }

  public void setMinimumGroupCount(Integer minimumGroupCount) {
    this.minimumGroupCount = minimumGroupCount;
  }
}
