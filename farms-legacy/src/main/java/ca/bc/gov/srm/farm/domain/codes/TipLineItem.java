package ca.bc.gov.srm.farm.domain.codes;

public class TipLineItem extends AbstractCode {
  private Integer id;
  private Integer lineItem;
  private Integer farmSubtypeBLookupId;
  
  private Boolean isUsedInOpCost;
  private Boolean isUsedInDirectExpense;
  private Boolean isUsedInMachineryExpense;
  private Boolean isUsedInLandAndBuildingExpense;
  private Boolean isUsedInOverheadExpense;
  private Boolean isProgramPaymentForTips;
  private Boolean isOther;
  
  private String farmSubtypeBName;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getLineItem() {
    return lineItem;
  }

  public void setLineItem(Integer lineItem) {
    this.lineItem = lineItem;
  }

  public Integer getFarmSubtypeBLookupId() {
    return farmSubtypeBLookupId;
  }

  public void setFarmSubtypeBLookupId(Integer farmSubtypeBLookupId) {
    this.farmSubtypeBLookupId = farmSubtypeBLookupId;
  }

  public Boolean getIsUsedInOpCost() {
    return isUsedInOpCost;
  }

  public void setIsUsedInOpCost(Boolean isUsedInOpCost) {
    this.isUsedInOpCost = isUsedInOpCost;
  }

  public Boolean getIsUsedInDirectExpense() {
    return isUsedInDirectExpense;
  }

  public void setIsUsedInDirectExpense(Boolean isUsedInDirectExpense) {
    this.isUsedInDirectExpense = isUsedInDirectExpense;
  }

  public Boolean getIsUsedInMachineryExpense() {
    return isUsedInMachineryExpense;
  }

  public void setIsUsedInMachineryExpense(Boolean isUsedInMachineryExpense) {
    this.isUsedInMachineryExpense = isUsedInMachineryExpense;
  }

  public Boolean getIsUsedInLandAndBuildingExpense() {
    return isUsedInLandAndBuildingExpense;
  }

  public void setIsUsedInLandAndBuildingExpense(Boolean isUsedInLandAndBuildingExpense) {
    this.isUsedInLandAndBuildingExpense = isUsedInLandAndBuildingExpense;
  }

  public Boolean getIsUsedInOverheadExpense() {
    return isUsedInOverheadExpense;
  }

  public void setIsUsedInOverheadExpense(Boolean isUsedInOverheadExpense) {
    this.isUsedInOverheadExpense = isUsedInOverheadExpense;
  }

  public Boolean getIsProgramPaymentForTips() {
    return isProgramPaymentForTips;
  }

  public void setIsProgramPaymentForTips(Boolean isProgramPaymentForTips) {
    this.isProgramPaymentForTips = isProgramPaymentForTips;
  }

  public Boolean getIsOther() {
    return isOther;
  }

  public void setIsOther(Boolean isOther) {
    this.isOther = isOther;
  }

  public String getFarmSubtypeBName() {
    return farmSubtypeBName;
  }

  public void setFarmSubtypeBName(String farmSubtypeBName) {
    this.farmSubtypeBName = farmSubtypeBName;
  }
}
