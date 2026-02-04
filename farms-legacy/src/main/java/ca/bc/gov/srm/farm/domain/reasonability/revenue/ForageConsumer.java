package ca.bc.gov.srm.farm.domain.reasonability.revenue;

import java.io.Serializable;

public class ForageConsumer implements Serializable, Comparable<ForageConsumer> {
  
  private static final long serialVersionUID = 1L;
  
  private String structureGroupCode;
  private String structureGroupCodeDescription;
  
  private Double productiveUnitCapacity;
  private Double quantityConsumedPerUnit;
  private Double quantityConsumed;

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

  public Double getProductiveUnitCapacity() {
    return productiveUnitCapacity;
  }

  public void setProductiveUnitCapacity(Double productiveUnitCapacity) {
    this.productiveUnitCapacity = productiveUnitCapacity;
  }

  public Double getQuantityConsumedPerUnit() {
    return quantityConsumedPerUnit;
  }

  public void setQuantityConsumedPerUnit(Double quantityConsumedPerUnit) {
    this.quantityConsumedPerUnit = quantityConsumedPerUnit;
  }

  public Double getQuantityConsumed() {
    return quantityConsumed;
  }

  public void setQuantityConsumed(Double quantityConsumed) {
    this.quantityConsumed = quantityConsumed;
  }

  @Override
  public String toString() {
    return "RevenueRiskInventoryItem [\n"
        + " structureGroupCode=" + structureGroupCode + "\n"
        + "\t structureGroupCodeDescription=" + structureGroupCodeDescription + "\n"
        + "\t productiveUnitCapacity=" + productiveUnitCapacity + "\n"
        + "\t quantityConsumedPerUnit=" + quantityConsumedPerUnit + "\n"
        + "\t quantityConsumed=" + quantityConsumed + "]";
  }
  
  @Override
  public int compareTo(ForageConsumer o) {
    return this.getStructureGroupCode().compareTo(o.getStructureGroupCode());
  }
}
