/**
 * Copyright (c) 2018,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain.reasonability;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * @author awilkinson
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BenefitRiskProductiveUnit implements Comparable<BenefitRiskProductiveUnit>, Serializable {

  private static final long serialVersionUID = 1L;
  
  private String inventoryItemCode;
  private String inventoryItemCodeDescription;
  private String structureGroupCode;
  private String structureGroupCodeDescription;
  private String commodityTypeCode;
  private String commodityTypeCodeDescription;
  private Double reportedProductiveCapacityAmount;
  private Double netProductiveCapacityAmount;
  private Double consumedProductiveCapacityAmount;
  private Double bpuCalculated;
  private Double estimatedIncome;
  
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

  public String getCommodityTypeCode() {
    return commodityTypeCode;
  }

  public void setCommodityTypeCode(String commodityTypeCode) {
    this.commodityTypeCode = commodityTypeCode;
  }

  public String getCommodityTypeCodeDescription() {
    return commodityTypeCodeDescription;
  }

  public void setCommodityTypeCodeDescription(String commodityCodeDescription) {
    this.commodityTypeCodeDescription = commodityCodeDescription;
  }

  public Double getNetProductiveCapacityAmount() {
    return netProductiveCapacityAmount;
  }

  public void setNetProductiveCapacityAmount(Double productiveCapacityAmount) {
    this.netProductiveCapacityAmount = productiveCapacityAmount;
  }

  public Double getBpuCalculated() {
    return bpuCalculated;
  }

  public void setBpuCalculated(Double bpuCalculated) {
    this.bpuCalculated = bpuCalculated;
  }

  public Double getEstimatedIncome() {
    return estimatedIncome;
  }

  public void setEstimatedMargin(Double estimatedIncome) {
    this.estimatedIncome = estimatedIncome;
  }

  public Double getConsumedProductiveCapacityAmount() {
    return consumedProductiveCapacityAmount;
  }

  public void setConsumedProductiveCapacityAmount(Double consumedProductiveCapacityAmount) {
    this.consumedProductiveCapacityAmount = consumedProductiveCapacityAmount;
  }

  public Double getReportedProductiveCapacityAmount() {
    return reportedProductiveCapacityAmount;
  }

  public void setReportedProductiveCapacityAmount(Double reportedProductiveCapacityAmount) {
    this.reportedProductiveCapacityAmount = reportedProductiveCapacityAmount;
  }

  @Override
  public String toString() {
    return "ReasonabilityProductiveUnit [\n"
        + "\t inventoryItemCode=" + inventoryItemCode + "\n"
        + "\t inventoryItemCodeDescription=" + inventoryItemCodeDescription + "\n"
        + "\t structureGroupCode=" + structureGroupCode + "\n"
        + "\t structureGroupCodeDescription=" + structureGroupCodeDescription + "\n"
        + "\t netProductiveCapacityAmount=" + netProductiveCapacityAmount + "\n"
        + "\t bpuCalculated=" + bpuCalculated + "\n"
        + "\t estimatedIncome=" + estimatedIncome + "\n"
        + "]";
  }
  
  @Override
  public int compareTo(BenefitRiskProductiveUnit o) {
    return this.getCode().compareTo(o.getCode());
  }
}
