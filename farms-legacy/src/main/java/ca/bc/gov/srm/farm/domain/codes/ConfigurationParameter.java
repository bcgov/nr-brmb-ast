package ca.bc.gov.srm.farm.domain.codes;

import ca.bc.gov.srm.farm.util.FarmSecurityUtils;

public class ConfigurationParameter extends AbstractCode {
  private Integer id;
  private String name;
  private String value;
  private String type;
  private String typeDescription;
  private Boolean sensitiveDataInd;
  
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

  public Boolean getSensitiveDataInd() {
    return sensitiveDataInd;
  }

  public void setSensitiveDataInd(Boolean sensitiveDataInd) {
    this.sensitiveDataInd = sensitiveDataInd;
  }

  public String getShortValue() {
    
    String result = getValue();
    if(result.length() > 30) {
      result = result.substring(0, 27) + "...";
    }
    
    if(sensitiveDataInd != null && sensitiveDataInd) {
      result = FarmSecurityUtils.maskSecret(result);
    }
    
    return result;
  }

}
