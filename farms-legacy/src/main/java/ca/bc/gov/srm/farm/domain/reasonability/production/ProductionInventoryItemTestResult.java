package ca.bc.gov.srm.farm.domain.reasonability.production;

import java.io.Serializable;

import ca.bc.gov.srm.farm.util.MathUtils;

public class ProductionInventoryItemTestResult implements Serializable, Comparable<ProductionInventoryItemTestResult> {
    
  private static final long serialVersionUID = 1L;
  
  private String inventoryItemCode;
  private String inventoryItemCodeDescription;
  private String quantityProducedCropUnitCode;
  private String commodityTypeCode;
  
  private String fruitVegTypeCode;
  private String fruitVegTypeCodeDescription;

  private Double variance;
  private Double reportedProduction;
  private Double expectedProductionPerUnit;
  private Double expectedQuantityProduced;
  private Double productiveCapacityAmount;
    
  private Boolean pass;
  
  private Boolean missingExpectedProduction;
  
  public Double getReportedProductionPerUnit() {
    Double result = null;
    if(reportedProduction != null && productiveCapacityAmount != null
        && MathUtils.roundProduction(reportedProduction) != 0 && MathUtils.roundProduction(productiveCapacityAmount) != 0) {
      result = MathUtils.roundProduction(reportedProduction / productiveCapacityAmount);
    }
    return result;
  }
  
  public Double getVariance() {
    return variance;
  }
  
  public void setVariance(Double variance) {
    this.variance = variance;
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

  public Double getReportedProduction() {
    return reportedProduction;
  }

  public void setReportedProduction(Double reportedProduction) {
    this.reportedProduction = reportedProduction;
  }

  public Boolean getPass() {
    return pass;
  }

  public void setPass(Boolean pass) {
    this.pass = pass;
  }

  public Double getExpectedProductionPerUnit() {
    return expectedProductionPerUnit;
  }

  public void setExpectedProductionPerUnit(Double expectedProductionPerUnit) {
    this.expectedProductionPerUnit = expectedProductionPerUnit;
  }

  public Double getExpectedQuantityProduced() {
    return expectedQuantityProduced;
  }

  public void setExpectedQuantityProduced(Double expectedQuantityProduced) {
    this.expectedQuantityProduced = expectedQuantityProduced;
  }
  
  public String getCropUnitCode() {
    return quantityProducedCropUnitCode;
  }

  public void setCropUnitCode(String cropUnitCode) {
    this.quantityProducedCropUnitCode = cropUnitCode;
  }

  public String getCommodityTypeCode() {
    return commodityTypeCode;
  }

  public void setCommodityTypeCode(String commodityTypeCode) {
    this.commodityTypeCode = commodityTypeCode;
  }

  public Double getProductiveCapacityAmount() {
    return productiveCapacityAmount;
  }

  public void setProductiveCapacityAmount(Double productiveCapacityAmount) {
    this.productiveCapacityAmount = productiveCapacityAmount;
  }
  
  public String getFruitVegTypeCode() {
    return fruitVegTypeCode;
  }

  public void setFruitVegTypeCode(String fruitVegTypeCode) {
    this.fruitVegTypeCode = fruitVegTypeCode;
  }

  public String getFruitVegTypeCodeDescription() {
    return fruitVegTypeCodeDescription;
  }

  public void setFruitVegTypeCodeDescription(String fruitVegTypeCodeDescription) {
    this.fruitVegTypeCodeDescription = fruitVegTypeCodeDescription;
  }

  public Boolean getMissingExpectedProduction() {
    return missingExpectedProduction;
  }

  public void setMissingExpectedProduction(Boolean missingExpectedProduction) {
    this.missingExpectedProduction = missingExpectedProduction;
  }

  @Override
  public int compareTo(ProductionInventoryItemTestResult o) {
    int compare = String.valueOf(this.getFruitVegTypeCodeDescription()).compareTo(String.valueOf(o.getFruitVegTypeCodeDescription()));
    if(compare == 0) {
      compare = this.getInventoryItemCode().compareTo(o.getInventoryItemCode());
    }
    return compare;
  }
}
