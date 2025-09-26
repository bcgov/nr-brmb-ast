/**
 *
 * Copyright (c) 2010,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import ca.bc.gov.srm.farm.domain.codes.CommodityTypeCodes;
import ca.bc.gov.srm.farm.util.MathUtils;
import ca.bc.gov.srm.farm.util.StringUtils;


/**
 * ProductiveUnitCapacity is a measure of the productive capacity for a given
 * AGRASTABILITY COMMODITY. A ProductiveUnitCapacity associates with a specific
 * AGRASTABILITY COMMODITY. A ProductiveUnitCapacity may have more than one
 * ProductiveUnitCapacity ADJUSTMENT. A ProductiveUnitCapacity is received via
 * federal imports and through provincial data imports.
 *
 * This class represents the entries for both reported (federal data imports)
 * and adjustment values (fields are prefixed with "reported" and "adj"
 * respectively). Each adjustable value also has a getter for the total value
 * (sum of reported and adjustment) prefixed with "getTotal".  
 *
 * @author awilkinson
 * @created Nov 16, 2010
 *
 */
public class ProductiveUnitCapacity implements Serializable {
  
  private static final long serialVersionUID = 7188440835793880760L;

  /** back-reference to the object containing this */
  @JsonBackReference
  private FarmingOperation farmingOperation;

  /** BENCHMARK PER UNIT is the cost for growing a certain amount of
   *  a certain crop on a model farm.  The price can have regional
   *  differences, for example growing 1 acre of field tomatoes on
   *  a farm in the far north might have extra heating and transportation
   *  costs compared to a lower-mainland tomato farm. */
  private BasePricePerUnit basePricePerUnit;

  /**
   * reportedPoductiveUnitCapacityId is a surrogate unique identifier for
   * the PRODUCTIVE UNIT CAPACITY reported by the farmer.
   */
  private Integer reportedProductiveUnitCapacityId;

  /** adjPoductiveUnitCapacityId is a surrogate unique identifier for
   * the PRODUCTIVE UNIT CAPACITY adjustment.
   */
  private Integer adjProductiveUnitCapacityId;

  /**
   * productiveCapacityAmount is the quantity entered in section 9 for this
   * inventory/bpu code.
   */
  private Double reportedAmount;
  private Double adjAmount;
  
  /**
   * On Farm Acres and Unseedable Acres are pulled from matching inventory records.
   * Since there may be multiple inventory records, these are totals.
   */
  private Double onFarmAcres;
  private Double unseedableAcres;

  /** structureGroupCode identifies the type of structure group. */
  private String structureGroupCode;

  /** Description for structureGroupCode. */
  private String structureGroupCodeDescription;
  
  private String rollupInventoryItemCode;
  private String rollupInventoryItemCodeDescription;
  
  private String rollupStructureGroupCode;
  private String rollupStructureGroupCodeDescription;


  /**
   * inventoryItemCode is a unique code for the object inventoryItemCode
   * described as a numeric code used to uniquely identify an inventory item.
   * Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3
   * - Barley (seed), 4 - Beans (Dry Edible).
   */
  private String inventoryItemCode;

  /** Description for inventoryItemCode. */
  private String inventoryItemCodeDescription;
  
  private String fruitVegTypeCode;
  private String fruitVegTypeCodeDescription;
  
  private String commodityTypeCode;
  private String commodityTypeCodeDescription;
  
  /** A unique code that identifies the type of multistage commodity this item is,
   * for commodities that have separate INVENTORY ITEM CODEs at different stages of their development/growth. */
  private String multiStageCommodityCode;
  private String multiStageCommodityCodeDescription;
  
  private String expectedProductionCropUnitCode;
  private BigDecimal expectedProductionPerProductiveUnit;
  
  private String participantDataSrcCode;

  /**
   * revisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   */
  private Integer revisionCount;
  
  /** The user who made the adjustment */
  private String adjustedByUserId;

  /**
   * reportedPoductiveUnitCapacityId is a surrogate unique identifier for
   * the PRODUCTIVE UNIT CAPACITY reported by the farmer.
   *
   * @return  Integer
   */
  public Integer getReportedProductiveUnitCapacityId() {
    return reportedProductiveUnitCapacityId;
  }

  /**
   * reportedPoductiveUnitCapacityId is a surrogate unique identifier for
   * the PRODUCTIVE UNIT CAPACITY reported by the farmer.
   *
   * @param  newVal  The new value for this property
   */
  public void setReportedProductiveUnitCapacityId(final Integer newVal) {
    reportedProductiveUnitCapacityId = newVal;
  }
  
  /**
   * adjPoductiveUnitCapacityId is a surrogate unique identifier for
   * the PRODUCTIVE UNIT CAPACITY adjustment.
   *
   * @return  Integer
   */
  public Integer getAdjProductiveUnitCapacityId() {
    return adjProductiveUnitCapacityId;
  }
  
  /**
   * adjPoductiveUnitCapacityId is a surrogate unique identifier for
   * the PRODUCTIVE UNIT CAPACITY adjustment.
   *
   * @param  newVal  The new value for this property
   */
  public void setAdjProductiveUnitCapacityId(final Integer newVal) {
    adjProductiveUnitCapacityId = newVal;
  }

  /**
   * productiveCapacityAmount is the quantity entered in section 9 for this
   * inventory/bpu code.
   * This reflects what was reported by the farmer.
   *
   * @return  Double
   */
  public Double getReportedAmount() {
    return reportedAmount;
  }

  /**
   * productiveCapacityAmount is the quantity entered in section 9 for this
   * inventory/bpu code.
   * This reflects what was reported by the farmer.
   *
   * @param  newVal  The new value for this property
   */
  public void setReportedAmount(final Double newVal) {
    reportedAmount = newVal;
  }

  /**
   * productiveCapacityAmount is the quantity entered in section 9 for this
   * inventory/bpu code.
   * This reflects adjustments made.
   * 
   * @return the adjProductiveCapacityAmount
   */
  public Double getAdjAmount() {
    return adjAmount;
  }

  /**
   * productiveCapacityAmount is the quantity entered in section 9 for this
   * inventory/bpu code.
   * This reflects adjustments made.
   * 
   * @param adjProductiveCapacityAmount the adjProductiveCapacityAmount to set
   */
  public void setAdjAmount(Double adjProductiveCapacityAmount) {
    this.adjAmount = adjProductiveCapacityAmount;
  }

  /**
   * productiveCapacityAmount is the quantity entered in section 9 for this
   * inventory/bpu code.
   * This reflects the sum of the reported amount and adjustments made.
   * 
   * @return the adjProductiveCapacityAmount
   */
  @JsonIgnore
  public Double getTotalProductiveCapacityAmount() {
    Double result;
    
    if(reportedAmount != null && adjAmount != null) {
      result = new Double(reportedAmount.doubleValue() + adjAmount.doubleValue());
    } else if(reportedAmount != null) {
      result = reportedAmount;
    } else if(adjAmount != null) {
      result = adjAmount;
    } else {
      throw new IllegalStateException("reportedAmount and adjAmount cannot both be null");
    }
    
    return result;
  }

  /**
   * StructureGroupCode identifies the type of structure group.
   *
   * @return  String
   */
  public String getStructureGroupCode() {
    return structureGroupCode;
  }

  /**
   * StructureGroupCode identifies the type of structure group.
   *
   * @param  newVal  The new value for this property
   */
  public void setStructureGroupCode(final String newVal) {
    structureGroupCode = newVal;
  }

  /**
   * Description for structureGroupCode.
   *
   * @return  String
   */
  public String getStructureGroupCodeDescription() {
    return structureGroupCodeDescription;
  }

  /**
   * Description for structureGroupCode.
   *
   * @param  newVal  The new value for this property
   */
  public void setStructureGroupCodeDescription(final String newVal) {
    structureGroupCodeDescription = newVal;
  }

  /**
   * RevisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   *
   * @return  Integer
   */
  public Integer getRevisionCount() {
    return revisionCount;
  }

  /**
   * RevisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   *
   * @param  newVal  The new value for this property
   */
  public void setRevisionCount(final Integer newVal) {
    revisionCount = newVal;
  }

  /**
   * Gets inventoryItemCode.
   *
   * @return  the inventoryItemCode
   */
  public String getInventoryItemCode() {
    return inventoryItemCode;
  }

  /**
   * Sets inventoryItemCode.
   *
   * @param  pInventoryItemCode  the inventoryItemCode to set
   */
  public void setInventoryItemCode(final String pInventoryItemCode) {
    inventoryItemCode = pInventoryItemCode;
  }

  /**
   * Gets inventoryItemCodeDescription.
   *
   * @return  the inventoryItemCodeDescription
   */
  public String getInventoryItemCodeDescription() {
    return inventoryItemCodeDescription;
  }

  /**
   * Sets inventoryItemCodeDescription.
   *
   * @param  pInventoryItemCodeDescription  the inventoryItemCodeDescription to
   *                                        set
   */
  public void setInventoryItemCodeDescription(
    final String pInventoryItemCodeDescription) {
    inventoryItemCodeDescription = pInventoryItemCodeDescription;
  }

  /**
   * @return the basePricePerUnit
   */
  public BasePricePerUnit getBasePricePerUnit() {
    return basePricePerUnit;
  }

  /**
   * @param basePricePerUnit the basePricePerUnit to set
   */
  public void setBasePricePerUnit(BasePricePerUnit basePricePerUnit) {
    this.basePricePerUnit = basePricePerUnit;
  }

  /**
   * @return the adjustedByUserId
   */
  public String getAdjustedByUserId() {
    return adjustedByUserId;
  }

  /**
   * @param adjustedByUserId the adjustedByUserId to set the value to
   */
  public void setAdjustedByUserId(String adjustedByUserId) {
    this.adjustedByUserId = adjustedByUserId;
  }

  /**
   * @return the onFarmAcres
   */
  public Double getOnFarmAcres() {
    return onFarmAcres;
  }

  /**
   * @param onFarmAcres the onFarmAcres to set
   */
  public void setOnFarmAcres(Double onFarmAcres) {
    this.onFarmAcres = onFarmAcres;
  }

  /**
   * @return the rnseedableAcres
   */
  public Double getUnseedableAcres() {
    return unseedableAcres;
  }

  /**
   * @param unseedableAcres the unseedableAcres to set
   */
  public void setUnseedableAcres(Double unseedableAcres) {
    this.unseedableAcres = unseedableAcres;
  }

  /**
   * @return the farmingOperation
   */
  public FarmingOperation getFarmingOperation() {
    return farmingOperation;
  }

  /**
   * @param farmingOperation the farmingOperation to set the value to
   */
  public void setFarmingOperation(FarmingOperation farmingOperation) {
    this.farmingOperation = farmingOperation;
  }

  public String getCommodityTypeCode() {
    if(commodityTypeCode == null && structureGroupCode != null) {
      commodityTypeCode = CommodityTypeCodes.getCommodityTypeForStructureGroup(structureGroupCode);
    }
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

  public String getExpectedProductionCropUnitCode() {
    return expectedProductionCropUnitCode;
  }

  public void setExpectedProductionCropUnitCode(String expectedProductionCropUnitCode) {
    this.expectedProductionCropUnitCode = expectedProductionCropUnitCode;
  }

  public BigDecimal getExpectedProductionPerProductiveUnit() {
    return expectedProductionPerProductiveUnit;
  }

  public void setExpectedProductionPerProductiveUnit(BigDecimal expectedProductionPerProductiveUnit) {
    this.expectedProductionPerProductiveUnit = expectedProductionPerProductiveUnit;
  }

  public String getMultiStageCommodityCode() {
    return multiStageCommodityCode;
  }

  public void setMultiStageCommodityCode(String multiStageCommodityCode) {
    this.multiStageCommodityCode = multiStageCommodityCode;
  }

  public String getMultiStageCommodityCodeDescription() {
    return multiStageCommodityCodeDescription;
  }

  public void setMultiStageCommodityCodeDescription(String multiStageCommodityCodeDescription) {
    this.multiStageCommodityCodeDescription = multiStageCommodityCodeDescription;
  }

  /**
   * @param puc ProductiveUnitCapacity to compare
   * @return true if the IncomeExpense matches the line item and the expense indicator
   */
  public boolean matches(ProductiveUnitCapacity puc) {
    return StringUtils.equal(this.structureGroupCode, puc.structureGroupCode)
      && StringUtils.equal(this.inventoryItemCode, puc.inventoryItemCode);
  }


  /**
   * @param o Object to compare
   * @return true if the InventoryItem matches the inventory item code and the reported values
   */
  @Override
  public boolean equals(Object o) {
    boolean result = false;
    if(o != null && this.getClass().isInstance(o)) {
      ProductiveUnitCapacity puc = (ProductiveUnitCapacity) o;
      result =
        StringUtils.equal(this.structureGroupCode, puc.structureGroupCode)
        && StringUtils.equal(this.inventoryItemCode, puc.inventoryItemCode)
        && MathUtils.equalToThreeDecimalPlaces(this.reportedAmount, puc.reportedAmount);
    } else {
      result = false;
    }
    return result;
  }


  /**
   * @return hash code
   */
  @Override
  public int hashCode() {
    int hash = 1;
    int iic = 0;
    int sgc = 0;
    int ra = 0;
    if(inventoryItemCode != null) {
      iic = inventoryItemCode.hashCode();
    }
    if(structureGroupCode != null) {
      sgc = structureGroupCode.hashCode();
    }
    if(reportedAmount != null) {
      ra = reportedAmount.hashCode();
    }

    final int seventeen = 17;
    final int thirtyOne = 31;
    final int nineteen = 19;

    hash = hash * seventeen + iic;
    hash = hash * thirtyOne + sgc;
    hash = hash * nineteen + ra;
    return hash;
  }
  
  
  /**
   * @return code of the thing being measured
   */
  @JsonIgnore
  public String getCode() {
    String code;
    if(structureGroupCode != null) {
      code = structureGroupCode;
    } else {
      code = inventoryItemCode;
    }
    return code;
  }
  
  
  /**
   * @return description of the thing being measured
   */
  @JsonIgnore
  public String getDescription() {
    String code;
    if(structureGroupCode != null) {
      code = structureGroupCodeDescription;
    } else {
      code = inventoryItemCodeDescription;
    }
    return code;
  }
  
  
  /**
   * @return code of the thing being measured
   */
  @JsonIgnore
  public String getRollupCode() {
    String code;
    if(rollupStructureGroupCode != null) {
      code = rollupStructureGroupCode;
    } else {
      code = rollupInventoryItemCode;
    }
    return code;
  }
  
  
  /**
   * @return description of the thing being measured
   */
  @JsonIgnore
  public String getRollupDescription() {
    String code;
    if(rollupStructureGroupCode != null) {
      code = rollupStructureGroupCodeDescription;
    } else {
      code = rollupInventoryItemCodeDescription;
    }
    return code;
  }

  
  @JsonIgnore
  public boolean isGrain() {
    return CommodityTypeCodes.GRAIN.equals(commodityTypeCode);
  }

  @JsonIgnore
  public boolean isForage() {
    return CommodityTypeCodes.FORAGE.equals(commodityTypeCode);
  }

  @JsonIgnore
  public boolean isForageSeed() {
    return CommodityTypeCodes.FORAGE_SEED.equals(commodityTypeCode);
  }


  public String getParticipantDataSrcCode() {
    return participantDataSrcCode;
  }

  public void setParticipantDataSrcCode(String participantDataSrcCode) {
    this.participantDataSrcCode = participantDataSrcCode;
  }

  public String getRollupInventoryItemCode() {
    return rollupInventoryItemCode;
  }

  public void setRollupInventoryItemCode(String rollupInventoryItemCode) {
      this.rollupInventoryItemCode = rollupInventoryItemCode;
  }

  public String getRollupInventoryItemCodeDescription() {
    return rollupInventoryItemCodeDescription;
  }

  public void setRollupInventoryItemCodeDescription(String rollupInventoryItemCodeDescription) {
      this.rollupInventoryItemCodeDescription = rollupInventoryItemCodeDescription;
  }

  public String getRollupStructureGroupCode() {
    return rollupStructureGroupCode;
  }

  public void setRollupStructureGroupCode(String rollupStructureGroupCode) {
    this.rollupStructureGroupCode = rollupStructureGroupCode;
  }

  public String getRollupStructureGroupCodeDescription() {
    return rollupStructureGroupCodeDescription;
  }

  public void setRollupStructureGroupCodeDescription(String rollupStructureGroupCodeDescription) {
      this.rollupStructureGroupCodeDescription = rollupStructureGroupCodeDescription;
  }

  /**
   * @return String
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString(){
    
    Integer farmingOperationId = null;
    if(farmingOperation != null) {
      farmingOperationId = farmingOperation.getFarmingOperationId();
    }

    return "ProductiveUnitCapacity"+"\n"+
    "\t farmingOperation : "+farmingOperationId+"\n"+
    "\t reportedProductiveUnitCapacityId : "+reportedProductiveUnitCapacityId+"\n"+
    "\t adjProductiveUnitCapacityId : "+adjProductiveUnitCapacityId+"\n"+
    "\t reportedAmount : "+reportedAmount+"\n"+
    "\t adjAmount : "+adjAmount+"\n"+
    "\t participantDataSrcCode : "+participantDataSrcCode+"\n"+
    "\t structureGroupCode : "+structureGroupCode+"\n"+
    "\t structureGroupCodeDescription : "+structureGroupCodeDescription+"\n"+
    "\t inventoryItemCode : "+inventoryItemCode+"\n"+
    "\t inventoryItemCodeDescription : "+inventoryItemCodeDescription+"\n"+
    "\t commodityTypeCode : "+commodityTypeCode+"\n"+
    "\t onFarmAcres : "+onFarmAcres+"\n"+
    "\t unseedableAcres : "+unseedableAcres+"\n"+
    "\t adjustedByUserId : "+adjustedByUserId+"\n"+
    "\t revisionCount : "+revisionCount+"\n"+
    "\t basePricePerUnit : "+basePricePerUnit+"\n"+
    "\t expectedProductionCropUnitCode : "+expectedProductionCropUnitCode+"\n"+
    "\t expectedProductionPerProductiveUnit : "+expectedProductionPerProductiveUnit+"\n"+
    "\t fruitVegTypeCode : "+fruitVegTypeCode+"\n"+
    "\t fruitVegTypeCodeDescription : "+fruitVegTypeCodeDescription+"\n"+
    "\t rollupInventoryItemCode : "+rollupInventoryItemCode+"\n"+
    "\t rollupInventoryItemCodeDescription : "+rollupInventoryItemCodeDescription+"\n"+
    "\t rollupStructureGroupCode : "+rollupStructureGroupCode+"\n"+
    "\t rollupStructureGroupCodeDescription : "+rollupStructureGroupCodeDescription+"\n"+
    "\t multiStageCommodityCode : "+multiStageCommodityCode+"\n"+
    "\t multiStageCommodityCodeDescription : "+multiStageCommodityCodeDescription+"\n";
  }

  public static ProductiveUnitCapacity rollup(List<ProductiveUnitCapacity> list) {
    ProductiveUnitCapacity result = new ProductiveUnitCapacity();

    result.setFarmingOperation(
      list.stream()
        .map(ProductiveUnitCapacity::getFarmingOperation)
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null)
    );

    result.setReportedAmount(
      list.stream()
        .map(ProductiveUnitCapacity::getReportedAmount)
        .reduce(null, (subTotal, num) -> {
          if (subTotal == null) return num;
          if (num == null) return subTotal;
          return subTotal + num;
        })
    );

    result.setAdjAmount(
      list.stream()
        .map(ProductiveUnitCapacity::getAdjAmount)
        .reduce(null, (subTotal, num) -> {
          if (subTotal == null) return num;
          if (num == null) return subTotal;
          return subTotal + num;
        })
    );

    result.setOnFarmAcres(
      list.stream()
        .map(ProductiveUnitCapacity::getOnFarmAcres)
        .reduce(null, (subTotal, num) -> {
          if (subTotal == null) return num;
          if (num == null) return subTotal;
          return subTotal + num;
        })
    );

    result.setUnseedableAcres(
      list.stream()
        .map(ProductiveUnitCapacity::getUnseedableAcres)
        .reduce(null, (subTotal, num) -> {
          if (subTotal == null) return num;
          if (num == null) return subTotal;
          return subTotal + num;
        })
    );

    result.setStructureGroupCode(
      list.stream()
        .map(ProductiveUnitCapacity::getRollupStructureGroupCode)
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null)
    );

    result.setStructureGroupCodeDescription(
      list.stream()
        .map(ProductiveUnitCapacity::getRollupStructureGroupCodeDescription)
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null)
    );

    result.setRollupInventoryItemCode(
      list.stream()
        .map(ProductiveUnitCapacity::getRollupInventoryItemCode)
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null)
    );

    result.setRollupInventoryItemCodeDescription(
      list.stream()
        .map(ProductiveUnitCapacity::getRollupInventoryItemCodeDescription)
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null)
    );

    result.setRollupStructureGroupCode(
      list.stream()
        .map(ProductiveUnitCapacity::getRollupStructureGroupCode)
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null)
    );

    result.setRollupStructureGroupCodeDescription(
      list.stream()
        .map(ProductiveUnitCapacity::getRollupStructureGroupCodeDescription)
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null)
    );

    result.setInventoryItemCode(
      list.stream()
        .map(ProductiveUnitCapacity::getRollupInventoryItemCode)
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null)
    );

    result.setInventoryItemCodeDescription(
      list.stream()
        .map(ProductiveUnitCapacity::getRollupInventoryItemCodeDescription)
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null)
    );

    result.setMultiStageCommodityCode(
      list.stream()
        .map(ProductiveUnitCapacity::getMultiStageCommodityCode)
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null)
    );

    result.setMultiStageCommodityCodeDescription(
      list.stream()
        .map(ProductiveUnitCapacity::getMultiStageCommodityCodeDescription)
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null)
    );

    result.setExpectedProductionCropUnitCode(
      list.stream()
        .map(ProductiveUnitCapacity::getExpectedProductionCropUnitCode)
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null)
    );

    result.setParticipantDataSrcCode(
      list.stream()
        .map(ProductiveUnitCapacity::getParticipantDataSrcCode)
        .filter(Objects::nonNull)
        .findFirst()
        .orElse(null)
    );

    return result;
  }
}
