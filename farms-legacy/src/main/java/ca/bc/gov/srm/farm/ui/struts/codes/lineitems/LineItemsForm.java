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
package ca.bc.gov.srm.farm.ui.struts.codes.lineitems;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import ca.bc.gov.srm.farm.calculator.CalculatorConfig;
import ca.bc.gov.srm.farm.domain.codes.LineItemCode;
import ca.bc.gov.srm.farm.list.CodeListView;
import ca.bc.gov.srm.farm.list.ListView;

/**
 * @author awilkinson
 */
public class LineItemsForm extends ValidatorForm {

  private static final long serialVersionUID = 503691354208515742L;

  private ListView[] sectors;
  private List<ListView> programYearSelectOptions;
  private Integer yearFilter;

  private Integer lineItemYear;
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

  private List<LineItemCode> lineItems;
  private int numLineItems;

  private Integer lineItemId;
  private String lineItem;
  private String description;
  private boolean isEligible;
  private boolean isEligibleRefYears;
  private boolean isYardage;
  private boolean isProgramPayment;
  private boolean isContractWork;
  private boolean isSupplyManagedCommodity;
  private boolean isExcludeFromRevenueCalculation;
  private boolean isIndustryAverageExpense;
  private Integer revisionCount;
  
  private String sectorDetailLineItemId;
  private String sectorCode;
  private String sectorCodeDescription;
  private String sectorDetailCode;
  private String sectorDetailCodeDescription;
  
  private List<CodeListView> fruitVegListViewItems;
  private String fruitVegTypeCode;
  private String fruitVegTypeCodeDescription;
  
  private List<CodeListView> commodityTypesListViewItems;
  private String commodityTypeCode;
  private String commodityTypeCodeDescription;
  
  private boolean isNew = false;
  
  private String filterSelection = "R";

  /**
   * @param mapping mapping
   * @param request request
   */
  @Override
  public void reset(ActionMapping mapping, HttpServletRequest request) {
    super.reset(mapping, request);
    setNew(false);
    setEligible(false);
    setEligibleRefYears(false);
    setYardage(false);
    setProgramPayment(false);
    setContractWork(false);
    setSupplyManagedCommodity(false);
    setExcludeFromRevenueCalculation(false);
    setIndustryAverageExpense(false);
  }

  /**
   * Gets lineItems
   *
   * @return the lineItems
   */
  public List<LineItemCode> getLineItems() {
    return lineItems;
  }

  /**
   * Sets lineItems
   *
   * @param pLineItems the lineItems to set
   */
  public void setLineItems(List<LineItemCode> pLineItems) {
    lineItems = pLineItems;
  }

  /**
   * Gets numLineItems
   *
   * @return the numLineItems
   */
  public int getNumLineItems() {
    return numLineItems;
  }

  /**
   * Sets numLineItems
   *
   * @param pNumLineItems the numLineItems to set
   */
  public void setNumLineItems(int pNumLineItems) {
    numLineItems = pNumLineItems;
  }

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
   * Gets lineItem
   *
   * @return the lineItem
   */
  public String getLineItem() {
    return lineItem;
  }

  /**
   * Sets lineItem
   *
   * @param pLineItem the lineItem to set
   */
  public void setLineItem(String pLineItem) {
    lineItem = pLineItem;
  }

  /**
   * Gets description
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets description
   *
   * @param pDescription the description to set
   */
  public void setDescription(String pDescription) {
    description = pDescription;
  }

  /**
   * Gets revisionCount
   *
   * @return the revisionCount
   */
  public Integer getRevisionCount() {
    return revisionCount;
  }

  /**
   * Sets revisionCount
   *
   * @param pRevisionCount the revisionCount to set
   */
  public void setRevisionCount(Integer pRevisionCount) {
    revisionCount = pRevisionCount;
  }

  /**
   * Gets sectorDetailLineItemId
   *
   * @return the sectorDetailLineItemId
   */
  public String getSectorDetailLineItemId() {
    return sectorDetailLineItemId;
  }

  /**
   * Sets sectorDetailLineItemId
   *
   * @param pSectorDetailLineItemId the sectorDetailLineItemId to set
   */
  public void setSectorDetailLineItemId(String pSectorDetailLineItemId) {
    sectorDetailLineItemId = pSectorDetailLineItemId;
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

  /**
   * Gets isNew
   *
   * @return the isNew
   */
  public boolean isNew() {
    return isNew;
  }

  /**
   * Sets isNew
   *
   * @param pIsNew the isNew to set
   */
  public void setNew(boolean pIsNew) {
    isNew = pIsNew;
  }

  /**
   * Gets lineItemYear
   *
   * @return the lineItemYear
   */
  public Integer getLineItemYear() {
    return lineItemYear;
  }

  /**
   * Sets lineItemYear
   *
   * @param pLineItemYear the lineItemYear to set
   */
  public void setLineItemYear(Integer pLineItemYear) {
    lineItemYear = pLineItemYear;
  }

  /**
   * Gets isEligible
   *
   * @return the isEligible
   */
  public boolean isEligible() {
    return isEligible;
  }

  /**
   * Sets isEligible
   *
   * @param pIsEligible the isEligible to set
   */
  public void setEligible(boolean pIsEligible) {
    isEligible = pIsEligible;
  }

  /**
   * Gets isEligibleRefYears
   *
   * @return the isEligibleRefYears
   */
  public boolean isEligibleRefYears() {
    return isEligibleRefYears;
  }

  /**
   * Sets isEligibleRefYears
   *
   * @param pIsEligibleRefYears the isEligibleRefYears to set
   */
  public void setEligibleRefYears(boolean pIsEligibleRefYears) {
    isEligibleRefYears = pIsEligibleRefYears;
  }
  
  /**
   * Gets isYardage
   *
   * @return the isYardage
   */
  public boolean isYardage() {
    return isYardage;
  }

  /**
   * Sets isYardage
   *
   * @param pIsYardage the isYardage to set
   */
  public void setYardage(boolean pIsYardage) {
    isYardage = pIsYardage;
  }

  /**
   * Gets isProgramPayment
   *
   * @return the isProgramPayment
   */
  public boolean isProgramPayment() {
    return isProgramPayment;
  }

  /**
   * Sets isProgramPayment
   *
   * @param pIsProgramPayment the isProgramPayment to set
   */
  public void setProgramPayment(boolean pIsProgramPayment) {
    isProgramPayment = pIsProgramPayment;
  }

  /**
   * Gets isContractWork
   *
   * @return the isContractWork
   */
  public boolean isContractWork() {
    return isContractWork;
  }

  /**
   * Sets isContractWork
   *
   * @param pIsContractWork the isContractWork to set
   */
  public void setContractWork(boolean pIsContractWork) {
    isContractWork = pIsContractWork;
  }

  /**
   * Gets isSupplyManagedCommodity
   *
   * @return the isSupplyManagedCommodity
   */
  public boolean isSupplyManagedCommodity() {
    return isSupplyManagedCommodity;
  }

  /**
   * Sets isSupplyManagedCommodity
   *
   * @param pIsSupplyManagedCommodity the isSupplyManagedCommodity to set
   */
  public void setSupplyManagedCommodity(boolean pIsSupplyManagedCommodity) {
    isSupplyManagedCommodity = pIsSupplyManagedCommodity;
  }

  /**
   * @return the isExcludeFromRevenueCalculation
   */
  public boolean isExcludeFromRevenueCalculation() {
    return isExcludeFromRevenueCalculation;
  }

  /**
   * @param isExcludeFromRevenueCalculation the isExcludeFromRevenueCalculation to set
   */
  public void setExcludeFromRevenueCalculation(boolean isExcludeFromRevenueCalculation) {
    this.isExcludeFromRevenueCalculation = isExcludeFromRevenueCalculation;
  }

  /**
   * Gets programYearSelectOptions
   *
   * @return the programYearSelectOptions
   */
  public List<ListView> getProgramYearSelectOptions() {
    return programYearSelectOptions;
  }

  /**
   * Sets programYearSelectOptions
   *
   * @param pProgramYearSelectOptions the programYearSelectOptions to set
   */
  public void setProgramYearSelectOptions(List<ListView> pProgramYearSelectOptions) {
    programYearSelectOptions = pProgramYearSelectOptions;
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

  public boolean isIndustryAverageExpense() {
    return isIndustryAverageExpense;
  }

  public void setIndustryAverageExpense(boolean isIndustryAverageExpense) {
    this.isIndustryAverageExpense = isIndustryAverageExpense;
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

  /**
   * Gets sectors
   *
   * @return the sectors
   */
  public ListView[] getSectors() {
    return sectors;
  }

  /**
   * Sets sectors
   *
   * @param pSectors the sectors to set
   */
  public void setSectors(ListView[] pSectors) {
    sectors = pSectors;
  }
  
  /**
   * @return the growingForward2013
   */
  public boolean isGrowingForward2013() {
    Integer year = yearFilter;
    if(year == null) {
      year = lineItemYear;
    }
    return year.intValue() >= CalculatorConfig.GROWING_FORWARD_2013;
  }

  public List<CodeListView> getFruitVegListViewItems() {
    return fruitVegListViewItems;
  }

  public void setFruitVegListViewItems(List<CodeListView> fruitVegListViewItems) {
    this.fruitVegListViewItems = fruitVegListViewItems;
  }

  public String getFruitVegTypeCode() {
    return fruitVegTypeCode;
  }

  public void setFruitVegTypeCode(String fruitVegTypeCode) {
    this.fruitVegTypeCode = fruitVegTypeCode;
  }

  public String getFilterSelection() {
    return filterSelection;
  }

  public void setFilterSelection(String filterSelection) {
    this.filterSelection = filterSelection;
  }

  public String getFruitVegCodeFilter() {
    return fruitVegCodeFilter;
  }

  public void setFruitVegCodeFilter(String fruitVegCodeFilter) {
    this.fruitVegCodeFilter = fruitVegCodeFilter;
  }

  public List<CodeListView> getCommodityTypesListViewItems() {
    return commodityTypesListViewItems;
  }

  public void setCommodityTypesListViewItems(List<CodeListView> commodityTypesListViewItems) {
    this.commodityTypesListViewItems = commodityTypesListViewItems;
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

  public String getFruitVegTypeCodeDescription() {
    return fruitVegTypeCodeDescription;
  }

  public void setFruitVegTypeCodeDescription(String fruitVegTypeCodeDescription) {
    this.fruitVegTypeCodeDescription = fruitVegTypeCodeDescription;
  }

}
