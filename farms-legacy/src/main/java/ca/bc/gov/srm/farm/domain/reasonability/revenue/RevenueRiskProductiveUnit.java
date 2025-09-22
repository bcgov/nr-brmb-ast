package ca.bc.gov.srm.farm.domain.reasonability.revenue;

import java.io.Serializable;

public class RevenueRiskProductiveUnit implements Serializable, Comparable<RevenueRiskProductiveUnit> {
  
  private static final long serialVersionUID = 1L;
  
  private String inventoryItemCode;
  private String inventoryItemCodeDescription;
  private String structureGroupCode;
  private String structureGroupCodeDescription;
  private Double productiveCapacityAmount;
  
  public String getCode() {
    String code;
    if(structureGroupCode != null) {
      code = structureGroupCode;
    } else {
      code = inventoryItemCode;
    }
    return code;
  }
  
  public String getDescription() {
    String description;
    if(structureGroupCodeDescription != null) {
      description = structureGroupCodeDescription;
    } else {
      description = inventoryItemCodeDescription;
    }
    return description;
  }
  
  public String getInventoryItemCode() {
    return inventoryItemCode;
  }
  
  public void setInventoryItemCode(String inventoryItemCode) {
    this.inventoryItemCode = inventoryItemCode;
  }
  
  public String getInventoryItemCodeDescription() {
    return inventoryItemCodeDescription;
  }
  
  public void setInventoryItemCodeDescription(String inventoryItemCodeDescription) {
    this.inventoryItemCodeDescription = inventoryItemCodeDescription;
  }
  
  public String getStructureGroupCode() {
    return structureGroupCode;
  }

  public void setStructureGroupCode(String structureGroupCode) {
    this.structureGroupCode = structureGroupCode;
  }

  public String getStructureGroupCodeDescription() {
    return structureGroupCodeDescription;
  }

  public void setStructureGroupCodeDescription(String structureGroupCodeDescription) {
    this.structureGroupCodeDescription = structureGroupCodeDescription;
  }

  public Double getProductiveCapacityAmount() {
    return productiveCapacityAmount;
  }

  public void setProductiveCapacityAmount(Double productiveCapacityAmount) {
    this.productiveCapacityAmount = productiveCapacityAmount;
  }

  @Override
  public int compareTo(RevenueRiskProductiveUnit o) {
    int result;
    if(this.getInventoryItemCode() != null && o.getInventoryItemCode() != null) {
      result = this.getInventoryItemCode().compareTo(o.getInventoryItemCode());
    } else {
      result = this.getStructureGroupCode().compareTo(o.getStructureGroupCode());
    }
    return result;
  }

  @Override
  public String toString() {
    return "RevenueRiskProductiveUnit [\n"
        + "inventoryItemCode=" + inventoryItemCode + "\n"
        + "\t inventoryItemCodeDescription=" + inventoryItemCodeDescription + "\n"
        + "\t structureGroupCode=" + structureGroupCode + "\n"
        + "\t structureGroupCodeDescription=" + structureGroupCodeDescription + "\n"
        + "\t productiveCapacityAmount=" + productiveCapacityAmount + "\n"
        + "]";
  }
}
