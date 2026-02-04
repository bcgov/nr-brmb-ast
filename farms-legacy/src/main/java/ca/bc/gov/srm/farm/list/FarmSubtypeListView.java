package ca.bc.gov.srm.farm.list;

public class FarmSubtypeListView extends BaseListView {
  private String name;

  @Override
  public String getLabel() {
    return this.name;
  }

  @Override
  public String getValue() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
}