package ca.bc.gov.srm.farm.ui.struts.codes.tips.lineitems;

import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.srm.farm.domain.codes.FarmSubtype;
import ca.bc.gov.srm.farm.domain.codes.TipLineItem;
import ca.bc.gov.srm.farm.list.FarmSubtypeListView;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;

public abstract class TipLineItemAction extends SecureAction {
  
  protected List<FarmSubtypeListView> getFarmSubtypeBListViews() throws Exception {
    List<FarmSubtype> farmSubtypes = ServiceFactory.getCodesService().getFarmSubtypesB();
    
    List<FarmSubtypeListView> farmSubtypeListViews = new ArrayList<>();
    for (FarmSubtype farmSubtype : farmSubtypes) {
      FarmSubtypeListView farmSubtypeListView = new FarmSubtypeListView();
      farmSubtypeListView.setName(farmSubtype.getName());
      farmSubtypeListViews.add(farmSubtypeListView);
    }
    
    return farmSubtypeListViews;
  }
  
  protected TipLineItemForm populateFormFromLineItem(TipLineItem lineItem, TipLineItemForm form) {
    form.setFarmSubtypeB(lineItem.getFarmSubtypeBName());
    form.setLineItem(lineItem.getLineItem().toString());
    form.setIsUsedInDirectExpense(lineItem.getIsUsedInDirectExpense());
    form.setIsUsedInLandAndBuildingExpense(lineItem.getIsUsedInLandAndBuildingExpense());
    form.setIsUsedInMachineryExpense(lineItem.getIsUsedInMachineryExpense());
    form.setIsUsedInOpCost(lineItem.getIsUsedInOpCost());
    form.setIsUsedInOverheadExpense(lineItem.getIsUsedInOverheadExpense());
    form.setIsProgramPaymentForTips(lineItem.getIsProgramPaymentForTips());
    form.setIsOther(lineItem.getIsOther());
    return form;
  }
  
  protected TipLineItem getLineItemFromForm(TipLineItemForm form) {
    TipLineItem lineItem = new TipLineItem();
    lineItem.setId(form.getId());
    lineItem.setFarmSubtypeBName(form.getFarmSubtypeB());
    lineItem.setLineItem(Integer.parseInt(form.getLineItem().toString()));
    lineItem.setIsUsedInDirectExpense(form.getIsUsedInDirectExpense());
    lineItem.setIsUsedInLandAndBuildingExpense(form.getIsUsedInLandAndBuildingExpense());
    lineItem.setIsUsedInMachineryExpense(form.getIsUsedInMachineryExpense());
    lineItem.setIsUsedInOpCost(form.getIsUsedInOpCost());
    lineItem.setIsUsedInOverheadExpense(form.getIsUsedInOverheadExpense());
    lineItem.setIsProgramPaymentForTips(form.getIsProgramPaymentForTips());
    lineItem.setIsOther(form.getIsOther());
    
    return lineItem;
  }
}