/**
 * Copyright (c) 2012,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.codes.tips;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.domain.codes.TipFarmTypeIncomeRange;
import ca.bc.gov.srm.farm.util.DataParseUtils;

/**
 * @author awilkinson
 */
public class TipIncomeRangeFormDataUtil {

  private TipIncomeRangeFormDataUtil() {
//    private constructor
  }

  public static List<TipFarmTypeIncomeRange> getIncomeRangeList(String incomeRangeJson, ObjectMapper jsonObjectMapper) throws Exception {
    TipFarmTypeIncomeRangeFormData incomeData = jsonObjectMapper.readValue(incomeRangeJson, TipFarmTypeIncomeRangeFormData.class);
    List<TipFarmTypeIncomeRange> ranges = new ArrayList<>();
    
    for (TipFarmTypeIncomeRangeFormItem item : incomeData.getIncomeRange()) {
      String rangeHigh = item.getRangeHigh();
      if (rangeHigh.isEmpty()) {
        rangeHigh = TipFarmTypeIncomeRange.MAX_RANGE_HIGH_VAL;
      }
      
      TipFarmTypeIncomeRange range = new TipFarmTypeIncomeRange();
      range.setRangeHigh(Double.parseDouble(rangeHigh));
      range.setRangeLow(Double.parseDouble(item.getRangeLow()));
      range.setMinimumGroupCount(DataParseUtils.parseInt(item.getMinimumGroupCount()));
      ranges.add(range);
    }
    
    return ranges;
  }

  public static List<TipFarmTypeIncomeRangeFormItem> getFormIncomeRangeItems(List<TipFarmTypeIncomeRange> range) {
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
