package ca.bc.gov.srm.farm.ui.struts.codes.reasonability.expectedproductions;

import java.io.Serializable;

public class ExpectedProductionsFilterContext implements Serializable {

  private static final long serialVersionUID = -1738649411855464977L;
  
  private String inventoryCodeFilter;
  private String inventoryDescFilter;
  private String cropUnitFilter;
  
  public String getInventoryCodeFilter() {
    return inventoryCodeFilter;
  }
  
  public void setInventoryCodeFilter(String inventoryCodeFilter) {
    this.inventoryCodeFilter = inventoryCodeFilter;
  }
  
  public String getInventoryDescFilter() {
    return inventoryDescFilter;
  }
  
  public void setInventoryDescFilter(String inventoryDescFilter) {
    this.inventoryDescFilter = inventoryDescFilter;
  }  
  
  public String getCropUnitFilter() {
    return cropUnitFilter;
  }
  
  public void setCropUnitFilter(String cropUnitFilter) {
    this.cropUnitFilter = cropUnitFilter;
  }
  
}
