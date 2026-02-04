
package ca.bc.gov.srm.farm.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import ca.bc.gov.srm.farm.domain.codes.CommodityTypeCodes;

/**
 * LineItem describes valid incomes and expensed used by Agristability.
 *
 * @author   Vivid Solutions Inc.
 * @version  1.0
 * @created  03-Jul-2009 2:06:53 PM
 */
public final class LineItem implements Serializable {
  
  private static final long serialVersionUID = -6347536175330838615L;

  /** back-reference to the object containing this */
  @JsonBackReference
  private IncomeExpense incomeExpense;

  /** lineItemId is a surrogate unique identifier for lineItemIds. */
  private Integer lineItemId;

  /** programYear is the applicable programYear for this record. */
  private Integer programYear;

  /** lineItem is income or expense item for Agristability. */
  private Integer lineItem;

  /** description describes the LineItem. */
  private String description;

  /**
   * province identifies the province the code is eligible or not eligible in.
   */
  private String province;

  /**
   * isEligible identifies if the LineItem is eligible for the specified
   * province in the program year.
   */
  private Boolean isEligible;
  
  /**
   * isEligibleRefYears identifies if the LineItem is eligible in reference
   * years for the specified program year. 
   */
  private Boolean isEligibleRefYears;
  
  /** is the line item for yardage work */
  private Boolean isYardage;
  
  /** is the line item for a program payment */
  private Boolean isProgramPayment;
  
  /** is the line item for contract work */
  private Boolean isContractWork;
  
  /** is the line item for manual expense */
  private Boolean isManualExpense;
  
  /** is the line item for supply managed items */
  private Boolean isSupplyManagedCommodity;
  
  private Boolean isIndustryAverageExpense;
  
  /** should be excluded from revenue calculations */
  private Boolean isExcludeFromRevenueCalculation;
  
  private String commodityTypeCode;
  private String commodityTypeCodeDescription;
  
  /** A unique code that identifies the type of fruit or vegetable this item is */
  private String fruitVegTypeCode;
  private String fruitVegTypeCodeDescription;
  
  /**
   * revisionCount is a counter identifying the number of times this record as
   * been modified. Used in the web page access to determine if the record as
   * been modified since the data was first retrieved.
   */
  private Integer revisionCount;

  /** Constructor. */
  public LineItem() {
  }


  /**
   * LineItemId is a surrogate unique identifier for lineItemIds.
   *
   * @return  Integer
   */
  public Integer getLineItemId() {
    return lineItemId;
  }

  /**
   * LineItemId is a surrogate unique identifier for lineItemIds.
   *
   * @param  newVal  The new value for this property
   */
  public void setLineItemId(final Integer newVal) {
    lineItemId = newVal;
  }

  /**
   * ProgramYear is the applicable programYear for this record.
   *
   * @return  Integer
   */
  public Integer getProgramYear() {
    return programYear;
  }

  /**
   * ProgramYear is the applicable programYear for this record.
   *
   * @param  newVal  The new value for this property
   */
  public void setProgramYear(final Integer newVal) {
    programYear = newVal;
  }

  /**
   * LineItem is income or expense item for Agristability.
   *
   * @return  Integer
   */
  public Integer getLineItem() {
    return lineItem;
  }

  /**
   * LineItem is income or expense item for Agristability.
   *
   * @param  newVal  The new value for this property
   */
  public void setLineItem(final Integer newVal) {
    lineItem = newVal;
  }

  /**
   * Description describes the LineItem.
   *
   * @return  String
   */
  public String getDescription() {
    return description;
  }

  /**
   * Description describes the LineItem.
   *
   * @param  newVal  The new value for this property
   */
  public void setDescription(final String newVal) {
    description = newVal;
  }

  /**
   * Province identifies the province the code is eligible or not eligible in.
   *
   * @return  String
   */
  public String getProvince() {
    return province;
  }

  /**
   * Province identifies the province the code is eligible or not eligible in.
   *
   * @param  newVal  The new value for this property
   */
  public void setProvince(final String newVal) {
    province = newVal;
  }

  /**
   * getIsEligible identifies if the LineItem is eligible for the specified
   * province in the program year.
   *
   * @return  Boolean
   */
  public Boolean getIsEligible() {
    return isEligible;
  }

  /**
   * IsEligible identifies if the LineItem is eligible for the specified
   * province in the program year.
   *
   * @param  newVal  The new value for this property
   */
  public void setIsEligible(final Boolean newVal) {
    isEligible = newVal;
  }
  
  /**
   * @return  the isEligibleRefYears
   */
  public Boolean getIsEligibleRefYears() {
    return isEligibleRefYears;
  }

  /**
   * @param  newVal  the isEligibleRefYears to set
   */
  public void setIsEligibleRefYears(final Boolean newVal) {
    isEligibleRefYears = newVal;
  }

  /**
   * @return the isProgramPayment
   */
  public Boolean getIsProgramPayment() {
    return isProgramPayment;
  }


  /**
   * @param isProgramPayment the isProgramPayment to set
   */
  public void setIsProgramPayment(Boolean isProgramPayment) {
    this.isProgramPayment = isProgramPayment;
  }


  /**
   * @return the isYardage
   */
  public Boolean getIsYardage() {
    return isYardage;
  }


  /**
   * @param isYardage the isYardage to set
   */
  public void setIsYardage(Boolean isYardage) {
    this.isYardage = isYardage;
  }

  

  /**
   * @return the isContractWork
   */
  public Boolean getIsContractWork() {
    return isContractWork;
  }


  /**
   * @param isContractWork the isContractWork to set
   */
  public void setIsContractWork(Boolean isContractWork) {
    this.isContractWork = isContractWork;
  }


  /**
   * @return the isManualExpense
   */
  public Boolean getIsManualExpense() {
    return isManualExpense;
  }


  /**
   * @param isManualExpense the isManualExpense to set
   */
  public void setIsManualExpense(Boolean isManualExpense) {
    this.isManualExpense = isManualExpense;
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

  public IncomeExpense getIncomeExpense() {
    return incomeExpense;
  }

  public void setIncomeExpense(IncomeExpense incomeExpense) {
    this.incomeExpense = incomeExpense;
  }
  
  public Boolean getIsSupplyManagedCommodity() {
    return isSupplyManagedCommodity;
  }

  public void setIsSupplyManagedCommodity(Boolean isSupplyManagedCommodity) {
    this.isSupplyManagedCommodity = isSupplyManagedCommodity;
  }

  public Boolean getIsExcludeFromRevenueCalculation() {
    return isExcludeFromRevenueCalculation;
  }

  public void setIsExcludeFromRevenueCalculation(Boolean isExcludeFromRevenueCalculation) {
    this.isExcludeFromRevenueCalculation = isExcludeFromRevenueCalculation;
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

  public Boolean getIsIndustryAverageExpense() {
    return isIndustryAverageExpense;
  }

  public void setIsIndustryAverageExpense(Boolean isIndustryAverageExpense) {
    this.isIndustryAverageExpense = isIndustryAverageExpense;
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


  /**
   * @param theProgramYear
   * @return whether or not this line item is eligible in the given program year
   */
  public boolean checkEligible(int theProgramYear) {
    
    boolean isProgramYear = this.programYear.intValue() == theProgramYear;
    boolean eligibleThisYear = this.isEligible.booleanValue();
    boolean eligibleRefYears = this.isEligibleRefYears.booleanValue();
    boolean eligible = eligibleThisYear || (!isProgramYear && eligibleRefYears);
    
    return eligible;
  }


  /**
   * 
   * @return String
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString(){
    
    Integer incomeExpenseId = null;
    if(incomeExpense != null) {
      incomeExpenseId = incomeExpense.getReportedIncomeExpenseId();
    }

    return "LineItem"+"\n"+
    "\t incomeExpense : "+incomeExpenseId+"\n"+
    "\t lineItemId : "+lineItemId+"\n"+
    "\t programYear : "+programYear+"\n"+
    "\t lineItem : "+lineItem+"\n"+
    "\t description : "+description+"\n"+
    "\t province : "+province+"\n"+
    "\t isEligible : "+isEligible+"\n"+
    "\t isEligibleRefYears : "+isEligibleRefYears+"\n"+
    "\t isYardage : "+isYardage+"\n"+
    "\t isProgramPayment : "+isProgramPayment+"\n"+
    "\t isContractWork : "+isContractWork+"\n"+
    "\t isManualExpense : "+isManualExpense+"\n"+
    "\t isSupplyManagedCommodity : "+isSupplyManagedCommodity+"\n"+
    "\t isIndustryAverageExpense : "+isIndustryAverageExpense+"\n"+
    "\t isExcludeFromRevenueCalculation : "+isExcludeFromRevenueCalculation+"\n"+
    "\t fruitVegTypeCode : "+fruitVegTypeCode+"\n"+
    "\t fruitVegTypeCodeDescription : "+fruitVegTypeCodeDescription+"\n"+
    "\t commodityTypeCode : "+commodityTypeCode+"\n"+
    "\t commodityTypeCodeDescription : "+commodityTypeCodeDescription+"\n"+
    "\t revisionCount : "+revisionCount;
  }
}
