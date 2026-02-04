/**
 * Copyright (c) 2006, 
 * Government of British Columbia, 
 * Canada
 *
 * All rights reserved. 
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain.codes;


/**
 * @author awilkinson
 */
public class LineItemCode extends AbstractCode {

  private Integer lineItemId;
  private Integer programYear;
  private Integer lineItem;
  private String province;
  private Boolean isEligible;
  private Boolean isEligibleRefYears;
  private Boolean isYardage;
  private Boolean isProgramPayment;
  private Boolean isContractWork;
  private Boolean isSupplyManagedCommodity;
  private Boolean isExcludeFromRevenueCalculation;
  private Boolean isIndustryAverageExpense;
  
  private Integer sectorDetailLineItemId;
  private String sectorCode;
  private String sectorCodeDescription;
  private String sectorDetailCode;
  private String sectorDetailCodeDescription;
  
  private String fruitVegCodeName;
  private String fruitVegCodeDescription;
  
  private String commodityTypeCode;
  private String commodityTypeCodeDescription;

  /**
   * Gets lineItemId
   *
   * @return the lineItemId
   */
  public Integer getLineItemId() {
    return lineItemId;
  }

  /**
   * Sets lineItemId
   *
   * @param pLineItemId the lineItemId to set
   */
  public void setLineItemId(Integer pLineItemId) {
    lineItemId = pLineItemId;
  }

  /**
   * Gets programYear
   *
   * @return the programYear
   */
  public Integer getProgramYear() {
    return programYear;
  }

  /**
   * Sets programYear
   *
   * @param pProgramYear the programYear to set
   */
  public void setProgramYear(Integer pProgramYear) {
    programYear = pProgramYear;
  }

  /**
   * Gets lineItem
   *
   * @return the lineItem
   */
  public Integer getLineItem() {
    return lineItem;
  }

  /**
   * Sets lineItem
   *
   * @param pLineItem the lineItem to set
   */
  public void setLineItem(Integer pLineItem) {
    lineItem = pLineItem;
  }

  /**
   * Gets province
   *
   * @return the province
   */
  public String getProvince() {
    return province;
  }

  /**
   * Sets province
   *
   * @param pProvince the province to set
   */
  public void setProvince(String pProvince) {
    province = pProvince;
  }

  /**
   * Gets isEligible
   *
   * @return the isEligible
   */
  public Boolean getIsEligible() {
    return isEligible;
  }

  /**
   * Sets isEligible
   *
   * @param pIsEligible the isEligible to set
   */
  public void setIsEligible(Boolean pIsEligible) {
    isEligible = pIsEligible;
  }
  
  /**
   * Gets isEligibleRefYears
   *
   * @return the isEligibleRefYears
   */
  public Boolean getIsEligibleRefYears() {
    return isEligibleRefYears;
  }

  /**
   * Sets isEligibleRefYears
   *
   * @param pIsEligibleRefYears the isEligibleRefYears to set
   */
  public void setIsEligibleRefYears(Boolean pIsEligibleRefYears) {
    isEligibleRefYears = pIsEligibleRefYears;
  }

  /**
   * Gets isYardage
   *
   * @return the isYardage
   */
  public Boolean getIsYardage() {
    return isYardage;
  }

  /**
   * Sets isYardage
   *
   * @param pIsYardage the isYardage to set
   */
  public void setIsYardage(Boolean pIsYardage) {
    isYardage = pIsYardage;
  }

  /**
   * Gets isProgramPayment
   *
   * @return the isProgramPayment
   */
  public Boolean getIsProgramPayment() {
    return isProgramPayment;
  }

  /**
   * Sets isProgramPayment
   *
   * @param pIsProgramPayment the isProgramPayment to set
   */
  public void setIsProgramPayment(Boolean pIsProgramPayment) {
    isProgramPayment = pIsProgramPayment;
  }

  /**
   * Gets isContractWork
   *
   * @return the isContractWork
   */
  public Boolean getIsContractWork() {
    return isContractWork;
  }

  /**
   * Sets isContractWork
   *
   * @param pIsContractWork the isContractWork to set
   */
  public void setIsContractWork(Boolean pIsContractWork) {
    isContractWork = pIsContractWork;
  }

  /**
   * Gets isSupplyManagedCommodity
   *
   * @return the isSupplyManagedCommodity
   */
  public Boolean getIsSupplyManagedCommodity() {
    return isSupplyManagedCommodity;
  }

  /**
   * Sets isSupplyManagedCommodity
   *
   * @param pIsSupplyManagedCommodity the isSupplyManagedCommodity to set
   */
  public void setIsSupplyManagedCommodity(Boolean pIsSupplyManagedCommodity) {
    isSupplyManagedCommodity = pIsSupplyManagedCommodity;
  }

  /**
   * @return the isExcludeFromRevenueCalculation
   */
  public Boolean getIsExcludeFromRevenueCalculation() {
    return isExcludeFromRevenueCalculation;
  }

  /**
   * @param isExcludeFromRevenueCalculation the isExcludeFromRevenueCalculation to set
   */
  public void setIsExcludeFromRevenueCalculation(Boolean isExcludeFromRevenueCalculation) {
    this.isExcludeFromRevenueCalculation = isExcludeFromRevenueCalculation;
  }

  /**
   * @return the sectorDetailLineItemId
   */
  public Integer getSectorDetailLineItemId() {
    return sectorDetailLineItemId;
  }

  /**
   * @param sectorDetailLineItemId the sectorDetailLineItemId to set
   */
  public void setSectorDetailLineItemId(Integer sectorDetailLineItemId) {
    this.sectorDetailLineItemId = sectorDetailLineItemId;
  }

  /**
   * Gets sectorCode
   *
   * @return the sectorCode
   */
  public String getSectorCode() {
    return sectorCode;
  }

  /**
   * Sets sectorCode
   *
   * @param pSectorCode the sectorCode to set
   */
  public void setSectorCode(String pSectorCode) {
    sectorCode = pSectorCode;
  }

  /**
   * Gets sectorCodeDescription
   *
   * @return the sectorCodeDescription
   */
  public String getSectorCodeDescription() {
    return sectorCodeDescription;
  }

  /**
   * Sets sectorCodeDescription
   *
   * @param pSectorCodeDescription the sectorCodeDescription to set
   */
  public void setSectorCodeDescription(String pSectorCodeDescription) {
    sectorCodeDescription = pSectorCodeDescription;
  }
  
  /**
   * Gets sectorDetailCode
   *
   * @return the sectorDetailCode
   */
  public String getSectorDetailCode() {
    return sectorDetailCode;
  }

  /**
   * Sets sectorDetailCode
   *
   * @param pSectorDetailCode the sectorDetailCode to set
   */
  public void setSectorDetailCode(String pSectorDetailCode) {
    sectorDetailCode = pSectorDetailCode;
  }

  /**
   * Gets sectorDetailCodeDescription
   *
   * @return the sectorDetailCodeDescription
   */
  public String getSectorDetailCodeDescription() {
    return sectorDetailCodeDescription;
  }

  /**
   * Sets sectorDetailCodeDescription
   *
   * @param pSectorDetailCodeDescription the sectorDetailCodeDescription to set
   */
  public void setSectorDetailCodeDescription(String pSectorDetailCodeDescription) {
    sectorDetailCodeDescription = pSectorDetailCodeDescription;
  }

  public Boolean getIsIndustryAverageExpense() {
    return isIndustryAverageExpense;
  }

  public void setIsIndustryAverageExpense(Boolean isIndustryAverageExpense) {
    this.isIndustryAverageExpense = isIndustryAverageExpense;
  }

  public String getFruitVegCodeName() {
    return fruitVegCodeName;
  }

  public void setFruitVegCodeName(String fruitVegCodeName) {
    this.fruitVegCodeName = fruitVegCodeName;
  }

  public String getFruitVegCodeDescription() {
    return fruitVegCodeDescription;
  }

  public void setFruitVegCodeDescription(String fruitVegCodeDescription) {
    this.fruitVegCodeDescription = fruitVegCodeDescription;
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

}
