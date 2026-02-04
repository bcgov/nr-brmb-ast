package ca.bc.gov.srm.farm.domain.codes;

public class FruitVegTypeCode extends AbstractCode {
  
  private String code;
  private String description;
  private Double varianceLimit;
  
  public String getName() {
    return code;
  }
  
  public void setName(String name) {
    this.code = name;
  }
  
  @Override
  public String getDescription() {
    return description;
  }
  
  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  public Double getVarianceLimit() {
    return varianceLimit;
  }

  public void setVarianceLimit(Double varianceLimit) {
    this.varianceLimit = varianceLimit;
  }
}
