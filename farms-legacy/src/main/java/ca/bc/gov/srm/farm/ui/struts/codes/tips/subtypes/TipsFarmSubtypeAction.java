package ca.bc.gov.srm.farm.ui.struts.codes.tips.subtypes;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.domain.codes.FarmSubtype;
import ca.bc.gov.srm.farm.domain.codes.FarmType3;
import ca.bc.gov.srm.farm.domain.codes.TipFarmTypeIncomeRange;
import ca.bc.gov.srm.farm.list.FarmSubtypeListView;
import ca.bc.gov.srm.farm.list.FarmType3ListView;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.ui.struts.codes.tips.TipFarmTypeIncomeRangeFormItem;
import ca.bc.gov.srm.farm.util.JsonUtils;

public abstract class TipsFarmSubtypeAction extends SecureAction {
  protected static ObjectMapper jsonObjectMapper = JsonUtils.getJsonObjectMapper();

  /**
   * @param form form
   * @return InventoryItemCode
   * @throws Exception On Exception
   */
  protected FarmSubtype getFarmSubtypeCodeFromForm(TipsFarmSubtypeForm form)
  throws Exception {

    FarmSubtype code = new FarmSubtype();
    code.setName(form.getName());
    code.setParentName(form.getParentName());
    code.setId(form.getId());
    code.setParentId(ServiceFactory.getCodesService().getFarmTypeCode(form.getParentName()).getFarmTypeId());
    code.setRevisionCount(form.getRevisionCount());
    
    return code;
  }
  
  protected FarmSubtype getFarmSubtypeBCodeFromForm(TipsFarmSubtypeForm form)
  throws Exception {
    FarmSubtype code = new FarmSubtype();
    code.setName(form.getName());
    code.setParentName(form.getParentName());
    code.setId(form.getId());
    code.setParentId(ServiceFactory.getCodesService().getFarmSubtypeA(form.getParentName()).getId());
    code.setRevisionCount(form.getRevisionCount());
    
    return code;
  }  
  
  protected List<FarmType3ListView> getListItemViews() throws Exception {
    List<FarmType3> farmTypes = ServiceFactory.getCodesService().getFarmType3s();
    
    List<FarmType3ListView> farmTypeListViews = new ArrayList<>();
    for (FarmType3 farmType : farmTypes) {
      FarmType3ListView farmTypeListView = new FarmType3ListView();
      farmTypeListView.setName(farmType.getFarmTypeName());
      farmTypeListViews.add(farmTypeListView);
    }
    
    return farmTypeListViews;
  }
  
  protected List<FarmSubtypeListView> getListItemAViews() throws Exception {
    List<FarmSubtype> farmSubtypes = ServiceFactory.getCodesService().getFarmType2Codes();
    
    List<FarmSubtypeListView> farmSubtypeListViews = new ArrayList<>();
    for (FarmSubtype farmSubtype : farmSubtypes) {
      FarmSubtypeListView farmSubtypeListView = new FarmSubtypeListView();
      farmSubtypeListView.setName(farmSubtype.getName());
      farmSubtypeListViews.add(farmSubtypeListView);
    }
    
    return farmSubtypeListViews;
  }
  
  protected List<TipFarmTypeIncomeRangeFormItem> getFormIncomeRangeItems(List<TipFarmTypeIncomeRange> range) {
    List<TipFarmTypeIncomeRangeFormItem> formItems = new ArrayList<>();
    
    for (TipFarmTypeIncomeRange rangeItem : range) {
      TipFarmTypeIncomeRangeFormItem item = new TipFarmTypeIncomeRangeFormItem();
      item.setRangeHigh(rangeItem.getRangeHigh().toString());
      item.setRangeLow(rangeItem.getRangeLow().toString());
      item.setMinimumGroupCount(rangeItem.getMinimumGroupCount().toString());
      formItems.add(item);
    }
    
    return formItems;
  }
}