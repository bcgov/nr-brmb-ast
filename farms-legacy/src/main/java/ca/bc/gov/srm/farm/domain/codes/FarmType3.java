package ca.bc.gov.srm.farm.domain.codes;

public class FarmType3 extends AbstractCode {
  private String farmTypeName;
  private Integer farmTypeId;
  
  public FarmType3(String farmTypeName) {
    this.farmTypeName = farmTypeName;
  }

  public String getFarmTypeName() {
    return this.farmTypeName;
  }
  
  public void setFarmTypeName(String farmTypeName) {
    this.farmTypeName = farmTypeName;
  }

  public Integer getFarmTypeId() {
	return farmTypeId;
  }

  public void setFarmTypeId(int farmTypeId) {
	this.farmTypeId = farmTypeId;
  }

}