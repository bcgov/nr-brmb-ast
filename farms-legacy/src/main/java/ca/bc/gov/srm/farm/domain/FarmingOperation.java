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

import static java.util.stream.Collectors.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import ca.bc.gov.srm.farm.domain.codes.ParticipantDataSrcCodes;

/**
 * FarmingOperation refers to revenue and expense information for an
 * AgristabilityClient- i.e. corresponding to tax return statements. An
 * AgristabilityClient can have multiple instances of a FarmingOperation in a
 * given Program Year. FarmingOperation data will originate from federal data
 * imports. Updates to FarmingOperation data may be received for past years.
 * FarmingOperation data will only exist from 2003 forward (i.e. the origin of
 * the CAIS/AgriStability program).
 *
 * @author awilkinson
 * @created Nov 12, 2010
 */
public class FarmingOperation implements Serializable {
  
  private static final long serialVersionUID = -3263871849042732671L;

  /** back-reference to the object containing this */
  @JsonBackReference
  private FarmingYear farmingYear;

  @JsonManagedReference
  private List<ProductiveUnitCapacity> craProductiveUnitCapacities;
  @JsonManagedReference
  private List<ProductiveUnitCapacity> localProductiveUnitCapacities;
  
  @JsonManagedReference
  private List<ProductiveUnitCapacity> rolledUpCraProductiveUnitCapacities;
  @JsonManagedReference
  private List<ProductiveUnitCapacity> rolledUpLocalProductiveUnitCapacities;
  
  private boolean productiveUnitsRollupEnabled;

  private List<IncomeExpense> incomeExpenses;

  private List<CropItem> cropItems;

  private List<LivestockItem> livestockItems;

  private List<InputItem> inputItems;

  private List<ReceivableItem> receivableItems;

  private List<PayableItem> payableItems;
  
  private List<ProductionInsurance> productionInsurances;
  
  private List<FarmingOperationPartner> farmingOperationPartners;

  private Margin margin;
  
  @JsonIgnore
  private FarmingOperation previousYearFarmingOperation;

  /**
   * farmingOperationId is a surrogate unique identifier for FarmingOperations.
   */
  private Integer farmingOperationId;
  
  private String schedule;

  /**
   * businessUseHomeExpense is the allowable portion of Business-use-of-home
   * expenses (9934).
   */
  private Double businessUseHomeExpense;

  /** farmingExpenses are the Total Expenses for the FarmingOperation (9968). */
  private Double farmingExpenses;

  /**
   * fiscalYearEnd is the Operation at the end of the Fiscal Year (yyyymmdd -
   * may be blank).
   */
  private Date fiscalYearEnd;

  /**
   * fiscalYearStart is the Operation at the start of the Fiscal year (yyyymmdd
   * - may be blank).
   */
  private Date fiscalYearStart;

  /**
   * grossIncome is the income before taxes or deductions for the
   * FarmingOperation. Uses form field number (9959) .
   */
  private Double grossIncome;

  /**
   * inventoryAdjustments are the changes to reported inventory quantities. Curr
   * Year (9941& 9942).
   */
  private Double inventoryAdjustments;

  /**
   * isCropShare denotes if this participant participated in a crop share as a
   * tenant.
   */
  private Boolean isCropShare;

  /**
   * isFeederMember denotes if this participant was a member of a feeder
   * association.
   */
  private Boolean isFeederMember;

  /**
   * isLandlord denotes if this Participant carried on a farming business as a
   * isLandlord.
   */
  private Boolean isLandlord;

  /** netFarmIncome denotes the Net Farming Income after adjustments (9946). */
  private Double netFarmIncome;

  /**
   * netIncomeAfterAdj is the Net Income after adjustments amount (line 9944).
   */
  private Double netIncomeAfterAdj;

  /** netIncomeBeforeAdj is the Net Income before adjustment (9969). */
  private Double netIncomeBeforeAdj;

  /** otherDeductions to the net income amount (line 9940). */
  private Double otherDeductions;

  /**
   * partnershipName is the name of the partnership for the FarmingOperation.
   */
  private String partnershipName;

  /**
   * partnershipPercent is the Percentage of the Partnership (100% will be
   * stored as 1.0).
   */
  private Double partnershipPercent;

  /**
   * partnershipPin uniquely identifies the partnership. If both the partners in
   * an operation file applications, the same partnershipPin will show up under
   * both pins. Partnership pins represent the same operation if/when they are
   * used in different stab years.
   */
  private Integer partnershipPin;
  
  /**
   * operationNumber identifies each operation a participant reports for a given
   * stab year. Operations may have different operationNumber in different stab
   * years.
   */
  private Integer operationNumber;

  /**
   * isCropDisaster identifies if the productive capacity decreased due to
   * disaster circumstances.
   */
  private Boolean isCropDisaster;

  /**
   * isLivestockDisaster identifies if productive capacity decreased due to
   * disaster circumstances.
   */
  private Boolean isLivestockDisaster;

  /**
   * accountingCode is a unique code for the object
   * accountingCode described as a numeric code used to uniquely
   * identify the method of Accounting to RCT and NISA. Examples of codes and
   * descriptions are 1- Accrual, 2-Cash.
   */
  private String accountingCode;

  /** Description for accountingCode. */
  private String accountingCodeDescription;

  /** isLocallyUpdated identifies if the record was updated by the client. */
  private Boolean isLocallyUpdated;

  /** isLocallyGenerated identifies if the record was created by the client. */
  private Boolean isLocallyGenerated;

  /**
   * revisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   */
  private Integer revisionCount;
  
  private Integer tipReportDocId;
    
  private Date tipReportGeneratedDate;

  private Double appleFmvPrice;
  
  private Double cherryFmvPrice;
  
  private Double chickenBroilerFMVPrice;
  
  private Double turkeyBroilerFMVPrice;
  
  private Double chickenEggsHatchFMVPrice;
  
  private Double chickenEggsConsumpFMVPrice;

  /**
   * 
   * @return A List of InventoryItem containing all crops, livestock, and payable inventory items.
   */
  @JsonIgnore
  public List<InventoryItem> getInventoryItems() {
    List<InventoryItem> allInventoryItems = new ArrayList<>();

    if(cropItems != null) {
      allInventoryItems.addAll(cropItems);
    }

    if(livestockItems != null) {
      allInventoryItems.addAll(livestockItems);
    }

    if(inputItems != null) {
      allInventoryItems.addAll(inputItems);
    }
    
    if(receivableItems != null) {
      allInventoryItems.addAll(receivableItems);
    }
    
    if(payableItems != null) {
      allInventoryItems.addAll(payableItems);
    }
    
    return allInventoryItems;
  }
  
  /**
   * 
   * @return List<ProducedItem> containing all crops and livestock.
   */
  @JsonIgnore
  public List<ProducedItem> getProducedItems() {
    List<ProducedItem> producedItems = new ArrayList<>();
    
    if(cropItems != null) {
      producedItems.addAll(cropItems);
    }
    
    if(livestockItems != null) {
      producedItems.addAll(livestockItems);
    }
    
    return producedItems;
  }
  
  /**
   * 
   * @return List<InventoryItem> containing all inputs, receivables and payables.
   */
  @JsonIgnore
  public List<InventoryItem> getAccrualItems() {
    List<InventoryItem> accrualItems = new ArrayList<>();
    
    if(inputItems != null) {
      accrualItems.addAll(inputItems);
    }
    
    if(receivableItems != null) {
      accrualItems.addAll(receivableItems);
    }
    
    if(payableItems != null) {
      accrualItems.addAll(payableItems);
    }
    
    return accrualItems;
  }

  public List<CropItem> getCropItems() {
    if(cropItems == null) {
      cropItems = new ArrayList<>();
    }
    return cropItems;
  }

  public void setCropItems(List<CropItem> crops) {
    if(crops != null) {
      for(InventoryItem cur : crops) {
        cur.setFarmingOperation(this);
      }
    }
    this.cropItems = crops;
  }

  public List<IncomeExpense> getIncomeExpenses() {
    // ClientServiceFactory expects this to be null initially.
    // Don't set this if it is null like other get methods. 
    return incomeExpenses;
  }

  public void setIncomeExpenses(List<IncomeExpense> incomeExpenses) {
    if(incomeExpenses != null) {
      for(IncomeExpense cur : incomeExpenses) {
        cur.setFarmingOperation(this);
      }
    }
    this.incomeExpenses = incomeExpenses;
  }

  public List<LivestockItem> getLivestockItems() {
    if(livestockItems == null) {
      livestockItems = new ArrayList<>();
    }
    return livestockItems;
  }

  public void setLivestockItems(List<LivestockItem> livestock) {
    if(livestock != null) {
      for(InventoryItem cur : livestock) {
        cur.setFarmingOperation(this);
      }
    }
    this.livestockItems = livestock;
  }

  public List<PayableItem> getPayableItems() {
    if(payableItems == null) {
      payableItems = new ArrayList<>();
    }
    return payableItems;
  }

  public void setPayableItems(List<PayableItem> payables) {
    if(payables != null) {
      for(InventoryItem cur : payables) {
        cur.setFarmingOperation(this);
      }
    }
    this.payableItems = payables;
  }

  @JsonIgnore
  public List<ProductiveUnitCapacity> getProductiveUnitCapacities() {
    List<ProductiveUnitCapacity> result;
    
    String participantDataSrcCode = null;
    
    ReferenceScenario refScenario = getFarmingYear().getReferenceScenario();
    if(refScenario != null) {
      participantDataSrcCode = refScenario.getParticipantDataSrcCode();
    }
    
    if (ParticipantDataSrcCodes.CRA.equals(participantDataSrcCode)) {
      result = getCraProductiveUnitCapacities();
    } else if (ParticipantDataSrcCodes.LOCAL.equals(participantDataSrcCode)) {
      result = getLocalProductiveUnitCapacities();
    } else {
      throw new IllegalStateException("participantDataSrcCode in ReferenceScenario cannot be null");
    }
    return result;
  }
  
  public void setProductiveUnitCapacities(List<ProductiveUnitCapacity> productiveUnitCapacities) {
    craProductiveUnitCapacities = new ArrayList<>();
    localProductiveUnitCapacities = new ArrayList<>();
    for (ProductiveUnitCapacity cur : productiveUnitCapacities) {
      cur.setFarmingOperation(this);
      if (ParticipantDataSrcCodes.CRA.equals(cur.getParticipantDataSrcCode())) {
        craProductiveUnitCapacities.add(cur);
      } else if (ParticipantDataSrcCodes.LOCAL.equals(cur.getParticipantDataSrcCode())) {
        localProductiveUnitCapacities.add(cur);
      }
    }
    
    ReferenceScenario referenceScenario = farmingYear.getReferenceScenario();
    Scenario scenario = referenceScenario.getParentScenario();
    productiveUnitsRollupEnabled = scenario.isProductiveUnitsRollupEnabled();

    if(productiveUnitsRollupEnabled) {
    
      rolledUpCraProductiveUnitCapacities = craProductiveUnitCapacities.stream()
        .collect(groupingBy(ProductiveUnitCapacity::getRollupCode))
        .entrySet()
        .stream()
        .map(e -> ProductiveUnitCapacity.rollup(e.getValue()))
        .filter(e -> e.getTotalProductiveCapacityAmount() != 0)
        .collect(Collectors.toList());
  
      rolledUpLocalProductiveUnitCapacities = localProductiveUnitCapacities.stream()
        .collect(groupingBy(ProductiveUnitCapacity::getRollupCode))
        .entrySet()
        .stream()
        .map(e -> ProductiveUnitCapacity.rollup(e.getValue()))
        .filter(e -> e.getTotalProductiveCapacityAmount() != 0)
        .collect(Collectors.toList());
      
    } else {
      rolledUpCraProductiveUnitCapacities = Collections.emptyList();
      rolledUpLocalProductiveUnitCapacities = Collections.emptyList();
    }
  }
  
  @JsonIgnore
  public List<ProductiveUnitCapacity> getAllProductiveUnitCapacities() {
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    productiveUnitCapacities.addAll(getCraProductiveUnitCapacities());
    productiveUnitCapacities.addAll(getLocalProductiveUnitCapacities());
    return productiveUnitCapacities;
  }

  public List<ProductiveUnitCapacity> getCraProductiveUnitCapacities() {
    if (craProductiveUnitCapacities == null) {
      craProductiveUnitCapacities = new ArrayList<>();
    }
    return craProductiveUnitCapacities;
  }

  public List<ProductiveUnitCapacity> getLocalProductiveUnitCapacities() {
    if (localProductiveUnitCapacities == null) {
      localProductiveUnitCapacities = new ArrayList<>();
    }
    return localProductiveUnitCapacities;
  }
  
  @JsonIgnore
  public List<ProductiveUnitCapacity> getProductiveUnitCapacities(String participantDataSrcCode) {
    return getAllProductiveUnitCapacities().stream()
    .filter(p -> p.getParticipantDataSrcCode().equals(participantDataSrcCode))
    .collect(Collectors.toList());
  }

  @JsonIgnore
  public List<ProductiveUnitCapacity> getRolledUpCraProductiveUnitCapacities() {
    if (rolledUpCraProductiveUnitCapacities == null) {
      rolledUpCraProductiveUnitCapacities = new ArrayList<>();
    }
    return rolledUpCraProductiveUnitCapacities;
  }

  public void setRolledUpCraProductiveUnitCapacities(List<ProductiveUnitCapacity> rolledUpCraProductiveUnitCapacities) {
    if (rolledUpCraProductiveUnitCapacities != null) {
      this.productiveUnitsRollupEnabled = true;
      
      for (ProductiveUnitCapacity cur : rolledUpCraProductiveUnitCapacities) {
        cur.setFarmingOperation(this);
      }
    }
    this.rolledUpCraProductiveUnitCapacities = rolledUpCraProductiveUnitCapacities;
  }

  @JsonIgnore
  public List<ProductiveUnitCapacity> getRolledUpLocalProductiveUnitCapacities() {
    if (rolledUpLocalProductiveUnitCapacities == null) {
      rolledUpLocalProductiveUnitCapacities = new ArrayList<>();
    }
    return rolledUpLocalProductiveUnitCapacities;
  }

  public void setRolledUpLocalProductiveUnitCapacities(List<ProductiveUnitCapacity> rolledUpLocalProductiveUnitCapacities) {
    if (rolledUpLocalProductiveUnitCapacities != null) {
      this.productiveUnitsRollupEnabled = true;
      
      for (ProductiveUnitCapacity cur : rolledUpLocalProductiveUnitCapacities) {
        cur.setFarmingOperation(this);
      }
    }
    this.rolledUpLocalProductiveUnitCapacities = rolledUpLocalProductiveUnitCapacities;
  }
  
  @JsonIgnore
  public List<ProductiveUnitCapacity> getAllProductiveUnitCapacitiesForStructureChange() {
    if(productiveUnitsRollupEnabled) {
      return getAllRolledUpProductiveUnitCapacities();
    } else {
      return getAllProductiveUnitCapacities();
    }
  }
  
  @JsonIgnore
  public List<ProductiveUnitCapacity> getAllRolledUpProductiveUnitCapacities() {
    List<ProductiveUnitCapacity> productiveUnitCapacities = new ArrayList<>();
    productiveUnitCapacities.addAll(getRolledUpCraProductiveUnitCapacities());
    productiveUnitCapacities.addAll(getRolledUpLocalProductiveUnitCapacities());
    return productiveUnitCapacities;
  }
  
  @JsonIgnore
  public List<ProductiveUnitCapacity> getProductiveUnitCapacitiesForStructureChange() {
    if(productiveUnitsRollupEnabled) {
      return getRolledUpProductiveUnitCapacities();
    } else {
      return getProductiveUnitCapacities();
    }
  }

  @JsonIgnore
  public List<ProductiveUnitCapacity> getRolledUpProductiveUnitCapacities() {
    List<ProductiveUnitCapacity> result;
    
    String participantDataSrcCode = null;
    
    ReferenceScenario refScenario = getFarmingYear().getReferenceScenario();
    if(refScenario != null) {
      participantDataSrcCode = refScenario.getParticipantDataSrcCode();
    }
    
    if (ParticipantDataSrcCodes.CRA.equals(participantDataSrcCode)) {
      result = getRolledUpCraProductiveUnitCapacities();
    } else if (ParticipantDataSrcCodes.LOCAL.equals(participantDataSrcCode)) {
      result = getRolledUpLocalProductiveUnitCapacities();
    } else {
      throw new IllegalStateException("participantDataSrcCode cannot be null");
    }
    return result;
  }

  public Margin getMargin() {
    return margin;
  }

  public void setMargin(Margin margin) {
    if(margin != null) {
      margin.setFarmingOperation(this);
    }
    this.margin = margin;
  }

  public List<ReceivableItem> getReceivableItems() {
    if(receivableItems == null) {
      receivableItems = new ArrayList<>();
    }
    return receivableItems;
  }

  public void setReceivableItems(List<ReceivableItem> receivableItems) {
    if(receivableItems != null) {
      for(InventoryItem cur : receivableItems) {
        cur.setFarmingOperation(this);
      }
    }
    this.receivableItems = receivableItems;
  }

  public List<InputItem> getInputItems() {
    if(inputItems == null) {
      inputItems = new ArrayList<>();
    }
    return inputItems;
  }

  public void setInputItems(List<InputItem> inputItems) {
    if(inputItems != null) {
      for(InventoryItem cur : inputItems) {
        cur.setFarmingOperation(this);
      }
    }
    this.inputItems = inputItems;
  }

  /**
   * FarmingOperationId is a surrogate unique identifier for FarmingOperations.
   *
   * @return  Integer
   */
  public Integer getFarmingOperationId() {
    return farmingOperationId;
  }

  /**
   * FarmingOperationId is a surrogate unique identifier for FarmingOperations.
   *
   * @param  newVal  The new value for this property
   */
  public void setFarmingOperationId(final Integer newVal) {
    farmingOperationId = newVal;
  }

  /**
   * BusinessUseHomeExpense is the allowable portion of Business-use-of-home
   * expenses (9934).
   *
   * @return  Double
   */
  public Double getBusinessUseHomeExpense() {
    return businessUseHomeExpense;
  }

  /**
   * BusinessUseHomeExpense is the allowable portion of Business-use-of-home
   * expenses (9934).
   *
   * @param  newVal  The new value for this property
   */
  public void setBusinessUseHomeExpense(final Double newVal) {
    businessUseHomeExpense = newVal;
  }

  /**
   * Expenses are the Total Expenses for the FarmingOperation (9968).
   *
   * @return  Double
   */
  public Double getFarmingExpenses() {
    return farmingExpenses;
  }

  /**
   * Expenses are the Total Expenses for the FarmingOperation (9968).
   *
   * @param  newVal  The new value for this property
   */
  public void setFarmingExpenses(final Double newVal) {
    farmingExpenses = newVal;
  }

  /**
   * FiscalYearEnd is the Operation at the end of the Fiscal Year (yyyymmdd -
   * may be blank).
   *
   * @return  Date
   */
  public Date getFiscalYearEnd() {
    return fiscalYearEnd;
  }

  /**
   * FiscalYearEnd is the Operation at the end of the Fiscal Year (yyyymmdd -
   * may be blank).
   *
   * @param  newVal  The new value for this property
   */
  public void setFiscalYearEnd(final Date newVal) {
    fiscalYearEnd = newVal;
  }

  /**
   * FiscalYearStart is the Operation at the start of the Fiscal year (yyyymmdd
   * - may be blank).
   *
   * @return  Date
   */
  public Date getFiscalYearStart() {
    return fiscalYearStart;
  }

  /**
   * FiscalYearStart is the Operation at the start of the Fiscal year (yyyymmdd
   * - may be blank).
   *
   * @param  newVal  The new value for this property
   */
  public void setFiscalYearStart(final Date newVal) {
    fiscalYearStart = newVal;
  }

  /**
   * GrossIncome is the income before taxes or deductions for the
   * FarmingOperation. Uses form field number (9959) .
   *
   * @return  Double
   */
  public Double getGrossIncome() {
    return grossIncome;
  }

  /**
   * GrossIncome is the income before taxes or deductions for the
   * FarmingOperation. Uses form field number (9959) .
   *
   * @param  newVal  The new value for this property
   */
  public void setGrossIncome(final Double newVal) {
    grossIncome = newVal;
  }

  /**
   * InventoryAdjustments are the changes to reported inventory quantities. Curr
   * Year (9941& 9942).
   *
   * @return  Double
   */
  public Double getInventoryAdjustments() {
    return inventoryAdjustments;
  }

  /**
   * InventoryAdjustments are the changes to reported inventory quantities. Curr
   * Year (9941& 9942).
   *
   * @param  newVal  The new value for this property
   */
  public void setInventoryAdjustments(final Double newVal) {
    inventoryAdjustments = newVal;
  }

  /**
   * IsCropShare denotes if this participant participated in a crop share as a
   * tenant.
   *
   * @return  Boolean
   */
  public Boolean getIsCropShare() {
    return isCropShare;
  }

  /**
   * IsCropShare denotes if this participant participated in a crop share as a
   * tenant.
   *
   * @param  newVal  The new value for this property
   */
  public void setIsCropShare(final Boolean newVal) {
    isCropShare = newVal;
  }

  /**
   * IsFeederMember denotes if this participant was a member of a feeder
   * association.
   *
   * @return  Boolean
   */
  public Boolean getIsFeederMember() {
    return isFeederMember;
  }

  /**
   * IsFeederMember denotes if this participant was a member of a feeder
   * association.
   *
   * @param  newVal  The new value for this property
   */
  public void setIsFeederMember(final Boolean newVal) {
    isFeederMember = newVal;
  }

  /**
   * IsLandlord denotes if this Participant carried on a farming business as a
   * isLandlord.
   *
   * @return  Boolean
   */
  public Boolean getIsLandlord() {
    return isLandlord;
  }

  /**
   * IsLandlord denotes if this Participant carried on a farming business as a
   * isLandlord.
   *
   * @param  newVal  The new value for this property
   */
  public void setIsLandlord(final Boolean newVal) {
    isLandlord = newVal;
  }

  /**
   * NetFarmIncome denotes the Net Farming Income after adjustments (9946).
   *
   * @return  Double
   */
  public Double getNetFarmIncome() {
    return netFarmIncome;
  }

  /**
   * NetFarmIncome denotes the Net Farming Income after adjustments (9946).
   *
   * @param  newVal  The new value for this property
   */
  public void setNetFarmIncome(final Double newVal) {
    netFarmIncome = newVal;
  }

  /**
   * NetIncomeAfterAdj is the Net Income after adjustments amount (line 9944).
   *
   * @return  Double
   */
  public Double getNetIncomeAfterAdj() {
    return netIncomeAfterAdj;
  }

  /**
   * NetIncomeAfterAdj is the Net Income after adjustments amount (line 9944).
   *
   * @param  newVal  The new value for this property
   */
  public void setNetIncomeAfterAdj(final Double newVal) {
    netIncomeAfterAdj = newVal;
  }

  /**
   * NetIncomeBeforeAdj is the Net Income before adjustment (9969).
   *
   * @return  Double
   */
  public Double getNetIncomeBeforeAdj() {
    return netIncomeBeforeAdj;
  }

  /**
   * NetIncomeBeforeAdj is the Net Income before adjustment (9969).
   *
   * @param  newVal  The new value for this property
   */
  public void setNetIncomeBeforeAdj(final Double newVal) {
    netIncomeBeforeAdj = newVal;
  }

  /**
   * OtherDeductions to the net income amount (line 9940).
   *
   * @return  Double
   */
  public Double getOtherDeductions() {
    return otherDeductions;
  }

  /**
   * OtherDeductions to the net income amount (line 9940).
   *
   * @param  newVal  The new value for this property
   */
  public void setOtherDeductions(final Double newVal) {
    otherDeductions = newVal;
  }

  /**
   * PartnershipName is the name of the partnership for the FarmingOperation.
   *
   * @return  String
   */
  public String getPartnershipName() {
    return partnershipName;
  }

  /**
   * PartnershipName is the name of the partnership for the FarmingOperation.
   *
   * @param  newVal  The new value for this property
   */
  public void setPartnershipName(final String newVal) {
    partnershipName = newVal;
  }

  /**
   * PartnershipPercent is the Percentage of the Partnership (100% will be
   * stored as 1.0).
   *
   * @return  Double
   */
  public Double getPartnershipPercent() {
    return partnershipPercent;
  }

  /**
   * PartnershipPercent is the Percentage of the Partnership (100% will be
   * stored as 1.0).
   *
   * @param  newVal  The new value for this property
   */
  public void setPartnershipPercent(final Double newVal) {
    partnershipPercent = newVal;
  }

  /**
   * PartnershipPin uniquely identifies the partnership. If both the partners in
   * an operation file applications, the same partnershipPin will show up under
   * both pins. Partnership pins represent the same operation if/when they are
   * used in different stab years.
   *
   * @return  Integer
   */
  public Integer getPartnershipPin() {
    return partnershipPin;
  }

  /**
   * PartnershipPin uniquely identifies the partnership. If both the partners in
   * an operation file applications, the same partnershipPin will show up under
   * both pins. Partnership pins represent the same operation if/when they are
   * used in different stab years.
   *
   * @param  newVal  The new value for this property
   */
  public void setPartnershipPin(final Integer newVal) {
    partnershipPin = newVal;
  }

  /**
   * operationNumber identifies each operation a participant reports for a given
   * stab year. Operations may have different operationNumber in different stab
   * years.
   *
   * @return  Integer
   */
  public Integer getOperationNumber() {
    return operationNumber;
  }

  /**
   * operationNumber identifies each operation a participant reports for a given
   * stab year. Operations may have different operationNumber in different stab
   * years.
   *
   * @param  newVal  The new value for this property
   */
  public void setOperationNumber(final Integer newVal) {
    operationNumber = newVal;
  }

  /**
   * IsCropDisaster identifies if the productive capacity decreased due to
   * disaster circumstances.
   *
   * @return  Boolean
   */
  public Boolean getIsCropDisaster() {
    return isCropDisaster;
  }

  /**
   * IsCropDisaster identifies if the productive capacity decreased due to
   * disaster circumstances.
   *
   * @param  newVal  The new value for this property
   */
  public void setIsCropDisaster(final Boolean newVal) {
    isCropDisaster = newVal;
  }

  /**
   * IsLivestockDisaster identifies if productive capacity decreased due to
   * disaster circumstances.
   *
   * @return  Boolean
   */
  public Boolean getIsLivestockDisaster() {
    return isLivestockDisaster;
  }

  /**
   * IsLivestockDisaster identifies if productive capacity decreased due to
   * disaster circumstances.
   *
   * @param  newVal  The new value for this property
   */
  public void setIsLivestockDisaster(final Boolean newVal) {
    isLivestockDisaster = newVal;
  }

  /**
   * accountingCode is a unique code for the object
   * accountingCode described as a numeric code used to uniquely
   * identify the method of Accounting to RCT and NISA. Examples of codes and
   * descriptions are 1- Accrual, 2-Cash.
   *
   * @return  String
   */
  public String getAccountingCode() {
    return accountingCode;
  }

  /**
   * accountingCode is a unique code for the object
   * accountingCode described as a numeric code used to uniquely
   * identify the method of Accounting to RCT and NISA. Examples of codes and
   * descriptions are 1- Accrual, 2-Cash.
   *
   * @param  newVal  The new value for this property
   */
  public void setAccountingCode(final String newVal) {
    accountingCode = newVal;
  }

  /**
   * Description for accountingCode.
   *
   * @return  String
   */
  public String getAccountingCodeDescription() {
    return accountingCodeDescription;
  }

  /**
   * Description for accountingCode.
   *
   * @param  newVal  The new value for this property
   */
  public void setAccountingCodeDescription(final String newVal) {
    accountingCodeDescription = newVal;
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
   * @return  Boolean
   */
  public Boolean getIsLocallyUpdated() {
    return isLocallyUpdated;
  }

  /**
   * @param  newVal  The new value for this property
   */
  public void setIsLocallyUpdated(final Boolean newVal) {
    isLocallyUpdated = newVal;
  }

  /**
   * Gets isLocallyGenerated
   *
   * @return the isLocallyGenerated
   */
  public Boolean getIsLocallyGenerated() {
    return isLocallyGenerated;
  }

  /**
   * Sets isLocallyGenerated
   *
   * @param newVal the isLocallyGenerated to set
   */
  public void setIsLocallyGenerated(Boolean newVal) {
    this.isLocallyGenerated = newVal;
  }

  /**
   * @return the farmingOperationPartners
   */
  public List<FarmingOperationPartner> getFarmingOperationPartners() {
    return farmingOperationPartners;
  }

  /**
   * @param farmingOperationPartners the farmingOperationPartners to set
   */
  public void setFarmingOperationPartners(List<FarmingOperationPartner> farmingOperationPartners) {
    if(farmingOperationPartners != null) {
      for(FarmingOperationPartner cur : farmingOperationPartners) {
        cur.setFarmingOperation(this);
      }
    }
    this.farmingOperationPartners = farmingOperationPartners;
  }

  /**
   * @return the productionInsurances
   */
  public List<ProductionInsurance> getProductionInsurances() {
    return productionInsurances;
  }

  /**
   * @param productionInsurances the productionInsurances to set
   */
  public void setProductionInsurances(List<ProductionInsurance> productionInsurances) {
    if(productionInsurances != null) {
      for(ProductionInsurance cur : productionInsurances) {
        cur.setFarmingOperation(this);
      }
    }
    this.productionInsurances = productionInsurances;
  }

  /**
   * @return the previousYearFarmingOperation
   */
  public FarmingOperation getPreviousYearFarmingOperation() {
    return previousYearFarmingOperation;
  }

  /**
   * @param previousYearFarmingOperation the previousYearFarmingOperation to set the value to
   */
  public void setPreviousYearFarmingOperation(FarmingOperation previousYearFarmingOperation) {
    this.previousYearFarmingOperation = previousYearFarmingOperation;
  }

  /**
   * @return the farmingYear
   */
  public FarmingYear getFarmingYear() {
    return farmingYear;
  }

  /**
   * @param farmingYear the farmingYear to set the value to
   */
  public void setFarmingYear(FarmingYear farmingYear) {
    this.farmingYear = farmingYear;
  }

  public Double getAppleFmvPrice() {
    return appleFmvPrice;
  }

  public void setAppleFmvPrice(Double appleFmvPrice) {
    this.appleFmvPrice = appleFmvPrice;
  }
  
  public Double getCherryFmvPrice() {
    return cherryFmvPrice;
  }

  public void setCherryFmvPrice(Double cherryFmvPrice) {
    this.cherryFmvPrice = cherryFmvPrice;
  }

  public Double getChickenBroilerFMVPrice() {
    return chickenBroilerFMVPrice;
  }

  public void setChickenBroilerFMVPrice(Double chickenBroilerFMVPrice) {
    this.chickenBroilerFMVPrice = chickenBroilerFMVPrice;
  }

  public Double getTurkeyBroilerFMVPrice() {
    return turkeyBroilerFMVPrice;
  }

  public void setTurkeyBroilerFMVPrice(Double turkeyBroilerFMVPrice) {
    this.turkeyBroilerFMVPrice = turkeyBroilerFMVPrice;
  }

  public Double getChickenEggsHatchFMVPrice() {
    return chickenEggsHatchFMVPrice;
  }

  public void setChickenEggsHatchFMVPrice(Double chickenEggsHatchFMVPrice) {
    this.chickenEggsHatchFMVPrice = chickenEggsHatchFMVPrice;
  }

  public Double getChickenEggsConsumpFMVPrice() {
    return chickenEggsConsumpFMVPrice;
  }

  public void setChickenEggsConsumpFMVPrice(Double chickenEggsConsumpFMVPrice) {
    this.chickenEggsConsumpFMVPrice = chickenEggsConsumpFMVPrice;
  }

  /**
   * 
   * @return String
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString(){

    Integer craProductiveUnitCapacitiesCount = null;
    if(craProductiveUnitCapacities != null) {
      craProductiveUnitCapacitiesCount = new Integer(craProductiveUnitCapacities.size());
    }
    
    Integer localProductiveUnitCapacitiesCount = null;
    if(localProductiveUnitCapacities != null) {
      localProductiveUnitCapacitiesCount = new Integer(localProductiveUnitCapacities.size());
    }

    Integer incomeExpensesCount = null;
    if(incomeExpenses != null) {
      incomeExpensesCount = new Integer(incomeExpenses.size());
    }
   
    Integer cropItemsCount = null;
    if(cropItems != null) {
      cropItemsCount = new Integer(cropItems.size());
    }
    
    Integer livestockItemsCount = null;
    if(livestockItems != null) {
      livestockItemsCount = new Integer(livestockItems.size());
    }

    Integer inputItemsCount = null;
    if(inputItems != null) {
      inputItemsCount = new Integer(inputItems.size());
    }

    Integer receivableItemsCount = null;
    if(receivableItems != null) {
      receivableItemsCount = new Integer(receivableItems.size());
    }

    Integer payableItemsCount = null;
    if(payableItems != null) {
      payableItemsCount = new Integer(payableItems.size());
    }

    Integer farmingOperationPartnersCount = null;
    if(farmingOperationPartners != null) {
      farmingOperationPartnersCount = new Integer(farmingOperationPartners.size());
    }
    
    Integer productionInsurancesCount = null;
    if(productionInsurances != null) {
      productionInsurancesCount = new Integer(productionInsurances.size());
    }
    
    Integer farmingYearId = null;
    if(farmingYear != null) {
      farmingYearId = farmingYear.getProgramYearId();
    }
    
    String prevYearFarmingOperationOutput = "null";
    if(previousYearFarmingOperation != null) {
      prevYearFarmingOperationOutput = "not null";
    }
    

    return "FarmingOperation"+"\n"+
    "\t farmingYear : "+farmingYearId+"\n"+
    "\t farmingOperationId : "+farmingOperationId+"\n"+
    "\t schedule : "+schedule+"\n"+
    "\t previousYearFarmingOperation : "+prevYearFarmingOperationOutput+"\n"+
    "\t businessUseHomeExpense : "+businessUseHomeExpense+"\n"+
    "\t farmingExpenses : "+farmingExpenses+"\n"+
    "\t fiscalYearEnd : "+fiscalYearEnd+"\n"+
    "\t fiscalYearStart : "+fiscalYearStart+"\n"+
    "\t grossIncome : "+grossIncome+"\n"+
    "\t inventoryAdjustments : "+inventoryAdjustments+"\n"+
    "\t isCropDisaster : "+isCropDisaster+"\n"+
    "\t isCropShare : "+isCropShare+"\n"+
    "\t isFeederMember : "+isFeederMember+"\n"+
    "\t isLandlord : "+isLandlord+"\n"+
    "\t isLivestockDisaster : "+isLivestockDisaster+"\n"+
    "\t isLocallyUpdated : "+isLocallyUpdated+"\n"+
    "\t netFarmIncome : "+netFarmIncome+"\n"+
    "\t netIncomeAfterAdj : "+netIncomeAfterAdj+"\n"+
    "\t netIncomeBeforeAdj : "+netIncomeBeforeAdj+"\n"+
    "\t otherDeductions : "+otherDeductions+"\n"+
    "\t partnershipName : "+partnershipName+"\n"+
    "\t partnershipPercent : "+partnershipPercent+"\n"+
    "\t partnershipPin : "+partnershipPin+"\n"+
    "\t operationNumber : "+operationNumber+"\n"+
    "\t accountingCode : "+accountingCode+"\n"+
    "\t accountingCodeDescription : "+accountingCodeDescription+"\n"+
    "\t revisionCount : "+revisionCount+"\n"+
    "\t craProductiveUnitCapacities : "+craProductiveUnitCapacitiesCount+"\n"+
    "\t localProductiveUnitCapacities : "+localProductiveUnitCapacitiesCount+"\n"+
    "\t incomeExpenses : "+incomeExpensesCount+"\n"+
    "\t cropItems : "+cropItemsCount+"\n"+
    "\t livestockItems : "+livestockItemsCount+"\n"+
    "\t inputItems : "+inputItemsCount+"\n"+
    "\t receivableItems : "+receivableItemsCount+"\n"+
    "\t payableItems : "+payableItemsCount+"\n"+
    "\t farmingOperationPartners : "+farmingOperationPartnersCount+"\n"+
    "\t productionInsurances : "+productionInsurancesCount+"\n"+
    "\t margin : "+margin+"\n"+
    "\t cherryFmvPrice : "+cherryFmvPrice+"\n"+
    "\t appleFmvPrice : "+appleFmvPrice+"\n";
  }

  /**
   * Gets schedule
   *
   * @return the schedule
   */
  public String getSchedule() {
    return schedule;
  }

  /**
   * Sets schedule
   *
   * @param pSchedule the schedule to set
   */
  public void setSchedule(String pSchedule) {
    schedule = pSchedule;
  }
  
  @JsonIgnore
  public Boolean getIsTipReportGenerated() {
    if (getTipReportDocId() == null) {
      return false;
    } 
    return true;
  }

  public Date getTipReportGeneratedDate() {
    return tipReportGeneratedDate;
  }

  public void setTipReportGeneratedDate(Date tipReportGeneratedDate) {
    this.tipReportGeneratedDate = tipReportGeneratedDate;
  }

  public Integer getTipReportDocId() {
    return tipReportDocId;
  }

  public void setTipReportDocId(Integer tipReportDocId) {
    this.tipReportDocId = tipReportDocId;
  }

}
