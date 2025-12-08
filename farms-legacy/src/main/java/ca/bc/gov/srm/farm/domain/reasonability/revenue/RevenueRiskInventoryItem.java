package ca.bc.gov.srm.farm.domain.reasonability.revenue;

import java.io.Serializable;

public class RevenueRiskInventoryItem implements Serializable, Comparable<RevenueRiskInventoryItem> {
  
  private static final long serialVersionUID = 1L;
  
  private Integer lineItemCode;
  private String lineItemDescription;
  private String inventoryItemCode;
  private String inventoryItemCodeDescription;
  private String cropUnitCode;
  private String cropUnitCodeDescription;
  private String fruitVegTypeCode;
  private String fruitVegTypeCodeDescription;
  private String commodityTypeCode;
  private String commodityTypeCodeDescription;
  
  private Double quantityProduced;
  private Double quantityStart;
  private Double quantityEnd;
  private Double quantityConsumed;
  private Double reportedPrice;
  private Double expectedRevenue;
  private Double quantitySold;
  private Double fmvPrice;
  private Double varianceLimit;

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
  
  public Double getQuantityProduced() {
    return quantityProduced;
  }
  
  public void setQuantityProduced(Double quantityProduced) {
    this.quantityProduced = quantityProduced;
  }
  
  public Double getQuantityStart() {
    return quantityStart;
  }
  
  public void setQuantityStart(Double quantityStart) {
    this.quantityStart = quantityStart;
  }
  
  public Double getQuantityEnd() {
    return quantityEnd;
  }
  
  public void setQuantityEnd(Double quantityEnd) {
    this.quantityEnd = quantityEnd;
  }

  public String getCropUnitCode() {
    return cropUnitCode;
  }

  public void setCropUnitCode(String cropUnitCode) {
    this.cropUnitCode = cropUnitCode;
  }

  public String getCropUnitCodeDescription() {
    return cropUnitCodeDescription;
  }

  public void setCropUnitCodeDescription(String cropUnitCodeDescription) {
    this.cropUnitCodeDescription = cropUnitCodeDescription;
  }

  public Integer getLineItemCode() {
    return lineItemCode;
  }

  public void setLineItemCode(Integer lineItemId) {
    this.lineItemCode = lineItemId;
  }

  public String getLineItemDescription() {
    return lineItemDescription;
  }

  public void setLineItemDescription(String lineItemDescription) {
    this.lineItemDescription = lineItemDescription;
  }

  public Double getExpectedRevenue() {
    return expectedRevenue;
  }

  public void setExpectedRevenue(Double expectedRevenue) {
    this.expectedRevenue = expectedRevenue;
  }

  public Double getQuantitySold() {
    return quantitySold;
  }

  public void setQuantitySold(Double quantitySold) {
    this.quantitySold = quantitySold;
  }

  public Double getReportedPrice() {
    return reportedPrice;
  }

  public void setReportedPrice(Double reportedPrice) {
    this.reportedPrice = reportedPrice;
  }

  public Double getFmvPrice() {
    return fmvPrice;
  }

  public void setFmvPrice(Double fmvPrice) {
    this.fmvPrice = fmvPrice;
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

  public Double getVarianceLimit() {
    return varianceLimit;
  }

  public void setVarianceLimit(Double varianceLimit) {
    this.varianceLimit = varianceLimit;
  }

  public Double getQuantityConsumed() {
    return quantityConsumed;
  }

  public void setQuantityConsumed(Double quantityConsumed) {
    this.quantityConsumed = quantityConsumed;
  }

  public String getCommodityTypeCode() {
    return commodityTypeCode;
  }

  public void setCommodityTypeCode(String commodityTypeCode) {
    this.commodityTypeCode = commodityTypeCode;
  }

  public String getCommodityTypeCodeDescription() {
    return commodityTypeCodeDescription;
  }

  public void setCommodityTypeCodeDescription(String commodityTypeCodeDescription) {
    this.commodityTypeCodeDescription = commodityTypeCodeDescription;
  }

  @Override
  public String toString() {
    return "RevenueRiskInventoryItem [\n"
        + " lineItemCode=" + lineItemCode + "\n"
        + "\t lineItemDescription=" + lineItemDescription + "\n"
        + "\t inventoryItemCode=" + inventoryItemCode + "\n"
        + "\t inventoryItemCodeDescription=" + inventoryItemCodeDescription + "\n"
        + "\t cropUnitCode=" + cropUnitCode + "\n"
        + "\t cropUnitCodeDescription=" + cropUnitCodeDescription + "\n"
        + "\t commodityTypeCode=" + commodityTypeCode + "\n"
        + "\t quantityProduced=" + quantityProduced + "\n"
        + "\t quantityStart=" + quantityStart + "\n"
        + "\t quantityEnd=" + quantityEnd + "\n"
        + "\t quantityConsumed=" + quantityConsumed + "\n"
        + "\t reportedPrice=" + reportedPrice + "\n"
        + "\t expectedRevenue=" + expectedRevenue + "\n"
        + "\t fmvPrice=" + fmvPrice + "\n"
        + "\t fruitVegTypeCode=" + fruitVegTypeCode + "\n"
        + "\t fruitVegTypeCodeDescription=" + fruitVegTypeCodeDescription + "\n"
        + "\t varianceLimit=" + varianceLimit + "\n"
        + "\t quantitySold=" + quantitySold + "]";
  }
  
  @Override
  public int compareTo(RevenueRiskInventoryItem o) {
    return this.getInventoryItemCode().compareTo(o.getInventoryItemCode());
  }
}
