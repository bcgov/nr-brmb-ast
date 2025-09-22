package ca.bc.gov.srm.farm.domain.reasonability.production;

import java.io.Serializable;

import ca.bc.gov.srm.farm.domain.codes.CropUnitCodes;
import ca.bc.gov.srm.farm.util.MathUtils;

public class FruitVegProductionResult implements Serializable,  Comparable<FruitVegProductionResult> {
  
  private static final long serialVersionUID = 1L;
  
  private String fruitVegTypeCode;
  private String fruitVegTypeCodeDescription;
  
  private String cropUnitCode = CropUnitCodes.POUNDS;
  private Double variance;
  private Double varianceLimit;
  private Double reportedProduction;
  private Double expectedQuantityProduced;
  private Double productiveCapacityAmount;
    
  private Boolean pass;
  
  public Double getReportedProductionPerUnit() {
    Double result = null;
    if(reportedProduction != null && productiveCapacityAmount != null && productiveCapacityAmount != 0) {
      result = MathUtils.roundProduction(reportedProduction / productiveCapacityAmount);
    }
    return result;
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

  public String getCropUnitCode() {
    return cropUnitCode;
  }

  public void setCropUnitCode(String cropUnitCode) {
    this.cropUnitCode = cropUnitCode;
  }

  public Double getVariance() {
    return variance;
  }

  public void setVariance(Double variance) {
    this.variance = variance;
  }

  public Double getReportedProduction() {
    return reportedProduction;
  }

  public void setReportedProduction(Double reportedProduction) {
    this.reportedProduction = reportedProduction;
  }

  public Double getExpectedQuantityProduced() {
    return expectedQuantityProduced;
  }

  public void setExpectedQuantityProduced(Double expectedQuantityProduced) {
    this.expectedQuantityProduced = expectedQuantityProduced;
  }

  public Double getProductiveCapacityAmount() {
    return productiveCapacityAmount;
  }

  public void setProductiveCapacityAmount(Double productiveCapacityAmount) {
    this.productiveCapacityAmount = productiveCapacityAmount;
  }

  public Boolean getPass() {
    return pass;
  }

  public void setPass(Boolean pass) {
    this.pass = pass;
  }

  public Double getVarianceLimit() {
    return varianceLimit;
  }

  public void setVarianceLimit(Double varianceLimit) {
    this.varianceLimit = varianceLimit;
  }

  @Override
  public int compareTo(FruitVegProductionResult o) {
    return this.getFruitVegTypeCode().compareTo(o.getFruitVegTypeCode());
  }
}