package ca.bc.gov.srm.farm.domain.reasonability.revenue;

import java.io.Serializable;

import ca.bc.gov.srm.farm.util.MathUtils;

public class RevenueRiskFruitVegItemTestResult implements Serializable, Comparable<RevenueRiskFruitVegItemTestResult> {
  
  private static final long serialVersionUID = 1L;
  
  private Double reportedRevenue;
  private Double quantityProduced;
  private Double expectedPrice;
  private Double expectedRevenue;
  
  private Double variance;
  private Double varianceLimit;
  private Boolean pass;
  
  private String fruitVegTypeCode;
  private String fruitVegTypeDesc;
  private String cropUnitCode;
  
  private Boolean isApple;
  
  public Double getReportedPrice() {
    Double result = null;
    if(reportedRevenue != null && quantityProduced != null && quantityProduced != 0) {
      result = MathUtils.roundCurrency(reportedRevenue / quantityProduced);
    }
    return result;
  }
  
  public Double getReportedRevenue() {
    return reportedRevenue;
  }
  
  public void setReportedRevenue(Double reportedRevenue) {
    this.reportedRevenue = reportedRevenue;
  }
  
  public Double getQuantityProduced() {
    return quantityProduced;
  }
  
  public void setQuantityProduced(Double quantityProduced) {
    this.quantityProduced = quantityProduced;
  }
  
  public Double getExpectedPrice() {
    return expectedPrice;
  }
  
  public void setExpectedPrice(Double expectedPrice) {
    this.expectedPrice = expectedPrice;
  }
  
  public Double getExpectedRevenue() {
    return expectedRevenue;
  }
  
  public void setExpectedRevenue(Double expectedRevenue) {
    this.expectedRevenue = expectedRevenue;
  }
  
  public Double getVariance() {
    return variance;
  }
  
  public void setVariance(Double variance) {
    this.variance = variance;
  }
  
  public Double getVarianceLimit() {
    return varianceLimit;
  }
  
  public void setVarianceLimit(Double varianceLimit) {
    this.varianceLimit = varianceLimit;
  }
  
  public Boolean getPass() {
    return pass;
  }
  
  public void setPass(Boolean pass) {
    this.pass = pass;
  }
  
  public String getFruitVegTypeCode() {
    return fruitVegTypeCode;
  }
  
  public void setFruitVegTypeCode(String fruitVegTypeCode) {
    this.fruitVegTypeCode = fruitVegTypeCode;
  }
  
  public String getFruitVegTypeDesc() {
    return fruitVegTypeDesc;
  }

  public void setFruitVegTypeDesc(String fruitVegTypeDesc) {
    this.fruitVegTypeDesc = fruitVegTypeDesc;
  }

  public String getCropUnitCode() {
    return cropUnitCode;
  }
  
  public void setCropUnitCode(String cropUnitCode) {
    this.cropUnitCode = cropUnitCode;
  }

  @Override
  public String toString() {
    return "RevenueRiskFruitVegItemTestResult [\n"
        + "reportedRevenue=" + reportedRevenue + "\n"
        + "\t quantityProduced=" + quantityProduced + "\n"
        + "\t expectedPrice=" + expectedPrice + "\n"
        + "\t expectedRevenue=" + expectedRevenue + "\n"
        + "\t variance=" + variance + "\n"
        + "\t varianceLimit=" + varianceLimit + "\n"
        + "\t pass=" + pass + "\n"
        + "\t fruitVegTypeCode=" + fruitVegTypeCode + "\n"
        + "\t fruitVegTypeDesc=" + fruitVegTypeDesc + "\n"
        + "\t cropUnitCode=" + cropUnitCode + "]";
  }
  
  @Override
  public int compareTo(RevenueRiskFruitVegItemTestResult o) {
    return this.getFruitVegTypeCode().compareTo(o.getFruitVegTypeCode());
  }

  public Boolean getIsApple() {
    return isApple;
  }

  public void setIsApple(Boolean isApple) {
    this.isApple = isApple;
  }
}
