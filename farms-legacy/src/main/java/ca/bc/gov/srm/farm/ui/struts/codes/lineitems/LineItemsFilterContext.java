/**
 * Copyright (c) 2011,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.codes.lineitems;

import java.io.Serializable;

/**
 * @author awilkinson
 */
public class LineItemsFilterContext implements Serializable {

  private static final long serialVersionUID = 5165325176701961235L;

  private Integer yearFilter;
  
  private String codeFilter;
  private String descriptionFilter;
  private String sectorFilter;

  private String isEligibleFilter;
  private String isEligibleRefYearsFilter;
  private String isYardageFilter;
  private String isProgramPaymentFilter;
  private String isContractWorkFilter;
  private String isSupplyManagedCommodityFilter;
  private String isExcludeFromRevenueCalculationFilter;
  private String isIndustryAverageExpenseFilter;
  
  private String fruitVegCodeFilter;  

  /**
   * Gets yearFilter
   *
   * @return the yearFilter
   */
  public Integer getYearFilter() {
    return yearFilter;
  }

  /**
   * Sets yearFilter
   *
   * @param pYearFilter the yearFilter to set
   */
  public void setYearFilter(Integer pYearFilter) {
    yearFilter = pYearFilter;
  }

  /**
   * Gets isEligibleFilter
   *
   * @return the isEligibleFilter
   */
  public String getIsEligibleFilter() {
    return isEligibleFilter;
  }

  /**
   * Sets isEligibleFilter
   *
   * @param pIsEligibleFilter the isEligibleFilter to set
   */
  public void setIsEligibleFilter(String pIsEligibleFilter) {
    isEligibleFilter = pIsEligibleFilter;
  }
  
  /**
   * Gets isEligibleRefYearsFilter
   *
   * @return the isEligibleRefYearsFilter
   */
  public String getIsEligibleRefYearsFilter() {
    return isEligibleRefYearsFilter;
  }

  /**
   * Sets isEligibleRefYearsFilter
   *
   * @param pIsEligibleRefYearsFilter the isEligibleRefYearsFilter to set
   */
  public void setIsEligibleRefYearsFilter(String pIsEligibleRefYearsFilter) {
    isEligibleRefYearsFilter = pIsEligibleRefYearsFilter;
  }

  /**
   * Gets isYardageFilter
   *
   * @return the isYardageFilter
   */
  public String getIsYardageFilter() {
    return isYardageFilter;
  }

  /**
   * Sets isYardageFilter
   *
   * @param pIsYardageFilter the isYardageFilter to set
   */
  public void setIsYardageFilter(String pIsYardageFilter) {
    isYardageFilter = pIsYardageFilter;
  }

  /**
   * Gets isProgramPaymentFilter
   *
   * @return the isProgramPaymentFilter
   */
  public String getIsProgramPaymentFilter() {
    return isProgramPaymentFilter;
  }

  /**
   * Sets isProgramPaymentFilter
   *
   * @param pIsProgramPaymentFilter the isProgramPaymentFilter to set
   */
  public void setIsProgramPaymentFilter(String pIsProgramPaymentFilter) {
    isProgramPaymentFilter = pIsProgramPaymentFilter;
  }

  /**
   * Gets isContractWorkFilter
   *
   * @return the isContractWorkFilter
   */
  public String getIsContractWorkFilter() {
    return isContractWorkFilter;
  }

  /**
   * Sets isContractWorkFilter
   *
   * @param pIsContractWorkFilter the isContractWorkFilter to set
   */
  public void setIsContractWorkFilter(String pIsContractWorkFilter) {
    isContractWorkFilter = pIsContractWorkFilter;
  }

  /**
   * Gets isSupplyManagedCommodityFilter
   *
   * @return the isSupplyManagedCommodityFilter
   */
  public String getIsSupplyManagedCommodityFilter() {
    return isSupplyManagedCommodityFilter;
  }

  /**
   * Sets isSupplyManagedCommodityFilter
   *
   * @param pIsSupplyManagedCommodityFilter the isSupplyManagedCommodityFilter to set
   */
  public void setIsSupplyManagedCommodityFilter(String pIsSupplyManagedCommodityFilter) {
    isSupplyManagedCommodityFilter = pIsSupplyManagedCommodityFilter;
  }
  
  /**
   * Gets isExcludeFromRevenueCalculationFilter
   *
   * @return the isExcludeFromRevenueCalculationFilter
   */
  public String getIsExcludeFromRevenueCalculationFilter() {
    return isExcludeFromRevenueCalculationFilter;
  }

  /**
   * Sets isExcludeFromRevenueCalculationFilter
   *
   * @param pIsExcludeFromRevenueCalculationFilter the isExcludeFromRevenueCalculationFilter to set
   */
  public void setIsExcludeFromRevenueCalculationFilter(String pIsExcludeFromRevenueCalculationFilter) {
	  isExcludeFromRevenueCalculationFilter = pIsExcludeFromRevenueCalculationFilter;
  }

  public String getIsIndustryAverageExpenseFilter() {
    return isIndustryAverageExpenseFilter;
  }

  public void setIsIndustryAverageExpenseFilter(String isIndustryAverageExpenseFilter) {
    this.isIndustryAverageExpenseFilter = isIndustryAverageExpenseFilter;
  }

  /**
   * Gets codeFilter
   *
   * @return the codeFilter
   */
  public String getCodeFilter() {
    return codeFilter;
  }

  /**
   * Sets codeFilter
   *
   * @param pCodeFilter the codeFilter to set
   */
  public void setCodeFilter(String pCodeFilter) {
    codeFilter = pCodeFilter;
  }

  /**
   * Gets descriptionFilter
   *
   * @return the descriptionFilter
   */
  public String getDescriptionFilter() {
    return descriptionFilter;
  }

  /**
   * Sets descriptionFilter
   *
   * @param pDescriptionFilter the descriptionFilter to set
   */
  public void setDescriptionFilter(String pDescriptionFilter) {
    descriptionFilter = pDescriptionFilter;
  }

  /**
   * Gets sectorFilter
   *
   * @return the sectorFilter
   */
  public String getSectorFilter() {
    return sectorFilter;
  }

  /**
   * Sets sectorFilter
   *
   * @param pSectorFilter the sectorFilter to set
   */
  public void setSectorFilter(String pSectorFilter) {
    sectorFilter = pSectorFilter;
  }

  public String getFruitVegCodeFilter() {
    return fruitVegCodeFilter;
  }

  public void setFruitVegCodeFilter(String fruitVegCodeFilter) {
    this.fruitVegCodeFilter = fruitVegCodeFilter;
  }

}
