package ca.bc.gov.srm.farm.ui.struts.codes.reasonability;

import java.util.List;

import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.domain.codes.FruitVegTypeCode;

public class FruitVegCodeForm extends ValidatorForm {

  private static final long serialVersionUID = 7130300594771565980L;
  
  private int numFruitVegCodes;
  
  private String name;
  private String description;
  private String varianceLimit;
  private Integer revisionCount;
  
  private boolean isNew = false;
  
  private List<FruitVegTypeCode> fruitVegCodes;
  
  public int getNumFruitVegCodes() {
    return numFruitVegCodes;
  }
  
  public void setNumFruitVegCodes(int numFruitVegCodes) {
    this.numFruitVegCodes = numFruitVegCodes;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getDescription() {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  public String getVarianceLimit() {
    return varianceLimit;
  }
  
  public void setVarianceLimit(String variance) {
    this.varianceLimit = variance;
  }

  public List<FruitVegTypeCode> getFruitVegCodes() {
    return fruitVegCodes;
  }

  public void setFruitVegCodes(List<FruitVegTypeCode> fruitVegCodes) {
    this.fruitVegCodes = fruitVegCodes;
  }

  public Integer getRevisionCount() {
    return revisionCount;
  }

  public void setRevisionCount(Integer revisionCount) {
    this.revisionCount = revisionCount;
  }

  public boolean isNew() {
    return isNew;
  }

  public void setNew(boolean isNew) {
    this.isNew = isNew;
  }
  
}
