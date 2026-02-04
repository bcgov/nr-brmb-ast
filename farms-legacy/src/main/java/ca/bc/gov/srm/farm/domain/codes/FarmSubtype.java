package ca.bc.gov.srm.farm.domain.codes;

public class FarmSubtype extends AbstractCode {
  private String name;
  private String parentName;
  private String secondaryParentName;
  
  private Integer id;
  private Integer parentId;
  private Integer secondaryParentId;
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }

  public String getParentName() {
    return parentName;
  }

  public void setParentName(String parentName) {
    this.parentName = parentName;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getSecondaryParentName() {
    return secondaryParentName;
  }

  public void setSecondaryParentName(String secondaryParentName) {
    this.secondaryParentName = secondaryParentName;
  }

  public Integer getParentId() {
    return parentId;
  }

  public void setParentId(Integer parentId) {
    this.parentId = parentId;
  }

  public Integer getSecondaryParentId() {
    return secondaryParentId;
  }

  public void setSecondaryParentId(Integer secondaryParentId) {
    this.secondaryParentId = secondaryParentId;
  }
  
}