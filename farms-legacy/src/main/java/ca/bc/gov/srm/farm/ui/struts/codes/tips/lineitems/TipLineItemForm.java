package ca.bc.gov.srm.farm.ui.struts.codes.tips.lineitems;

import ca.bc.gov.srm.farm.domain.codes.TipLineItem;
import ca.bc.gov.srm.farm.list.FarmSubtypeListView;

import java.util.List;

import org.apache.struts.validator.ValidatorForm;

public class TipLineItemForm extends ValidatorForm {
  
  private static final long serialVersionUID = -7524699746117510833L;
  
  private boolean isNew = false;
  
  private Integer numLineItems;
  private List<TipLineItem> lineItems;
  private List<FarmSubtypeListView> farmSubtypeBListViewItems;
  
  private Integer id;
  private String lineItem;
  private String farmSubtypeB;
  private Boolean isUsedInOpCost;
  private Boolean isUsedInDirectExpense;
  private Boolean isUsedInMachineryExpense;
  private Boolean isUsedInLandAndBuildingExpense;
  private Boolean isUsedInOverheadExpense;
  private Boolean isProgramPaymentForTips;
  private Boolean isOther;
  
  private String lineItemFilter;
  private String descFilter;
  private String farmSubtypeBFilter;
  
  public boolean isNew() {
    return isNew;
  }

  public void setNew(boolean isNew) {
    this.isNew = isNew;
  }

  public List<FarmSubtypeListView> getFarmSubtypeBListViewItems() {
    return farmSubtypeBListViewItems;
  }

  public void setFarmSubtypeBListViewItems(List<FarmSubtypeListView> farmSubtypeBListViewItems) {
    this.farmSubtypeBListViewItems = farmSubtypeBListViewItems;
  }

  public Integer getNumLineItems() {
    return numLineItems;
  }
  
  public void setNumLineItems(Integer numLineItems) {
    this.numLineItems = numLineItems;
  }
  
  public List<TipLineItem> getLineItems() {
    return lineItems;
  }
  
  public void setLineItems(List<TipLineItem> lineItems) {
    this.lineItems = lineItems;
  }

  public String getLineItem() {
    return lineItem;
  }

  public void setLineItem(String lineItem) {
    this.lineItem = lineItem;
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

  public String getFarmSubtypeB() {
    return farmSubtypeB;
  }

  public void setFarmSubtypeB(String farmSubtypeB) {
    this.farmSubtypeB = farmSubtypeB;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getLineItemFilter() {
    return lineItemFilter;
  }

  public void setLineItemFilter(String lineItemFilter) {
    this.lineItemFilter = lineItemFilter;
  }

  public String getDescFilter() {
    return descFilter;
  }

  public void setDescFilter(String descFilter) {
    this.descFilter = descFilter;
  }

  public String getFarmSubtypeBFilter() {
    return farmSubtypeBFilter;
  }

  public void setFarmSubtypeBFilter(String farmSubtypeBFilter) {
    this.farmSubtypeBFilter = farmSubtypeBFilter;
  }
}
