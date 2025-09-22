package ca.bc.gov.srm.farm.domain.reasonability.revenue;

import java.io.Serializable;

public class RevenueRiskIncomeTestResult implements Serializable, Comparable<RevenueRiskIncomeTestResult> {

  private static final long serialVersionUID = 1L;

  private Integer lineItemCode;

  private Double reportedRevenue;
  private Double expectedRevenue;
  private Double variance;

  private String description;

  private Boolean pass;

  private String commodityTypeCode;
  private String commodityTypeCodeDescription;
  
  public Double getReportedRevenue() {
    return reportedRevenue;
  }

  public void setReportedRevenue(Double reportedRevenue) {
    this.reportedRevenue = reportedRevenue;
  }

  public Double getExpectedRevenue() {
    return expectedRevenue;
  }

  public void setExpectedRevenue(Double estimatedIncome) {
    this.expectedRevenue = estimatedIncome;
  }

  public Double getVariance() {
    return variance;
  }

  public void setVariance(Double variance) {
    this.variance = variance;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean getPass() {
    return pass;
  }

  public void setPass(Boolean pass) {
    this.pass = pass;
  }

  public Integer getLineItemCode() {
    return lineItemCode;
  }

  public void setLineItemCode(Integer lineItemCode) {
    this.lineItemCode = lineItemCode;
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
    return "RevenueRiskIncomeTestResult [\n" + "\t lineItemCode=" + lineItemCode + "\n" + "\t reportedIncome="
        + reportedRevenue + "\n" + "\t estimatedIncome=" + expectedRevenue + "\n" + "\t variance=" + variance + "\n"
        + "\t pass=" + pass + "]";
  }

  @Override
  public int compareTo(RevenueRiskIncomeTestResult o) {
    return this.getLineItemCode().compareTo(o.getLineItemCode());
  }
}