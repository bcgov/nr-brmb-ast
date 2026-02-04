/**
 * 
 * Copyright (c) 2011,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.ui.struts.calculator.search.CalculatorSearchForm;

/**
 * @author awilkinson
 * @created Jan 21, 2011
 */
public abstract class AdjustmentGridForm extends CalculatorSearchForm {
  
  private static final long serialVersionUID = -7164979821037011575L;

  /** Map<String lineKey, AdjustmentGridItemData>
   * key is a type prefix plus inventory item code or structure group code 
   * value could be a subclass of AdjustmentGridItemData */
  private Map<String, AdjustmentGridItemData> items = new HashMap<>();
  
  private Map<String, FarmingOperation> yearOperationMap;
  
  private List<String> lineKeys;

  /**
   * @param lineKey String
   * @return AdjustmentGridItemData
   */
  public AdjustmentGridItemData getItem(String lineKey) {
    Object obj = items.get(lineKey);
    AdjustmentGridItemData result;
    if(obj == null) {
      result = getNewAdjustmentGridItemData();
      items.put(lineKey, result);
    } else {
      result = (AdjustmentGridItemData) obj;
    }
    result.setLineKey(lineKey);
    return result;
  }

  /**
   * @return new instance of AdjustmentGridItemData
   */
  protected abstract AdjustmentGridItemData getNewAdjustmentGridItemData();

  /**
   * @return the items
   */
  public Map<String, AdjustmentGridItemData> getItems() {
    return items;
  }

  /**
   * @return the yearOperationMap
   */
  public Map<String, FarmingOperation> getYearOperationMap() {
    return yearOperationMap;
  }

  /**
   * @param yearOperationMap the yearOperationMap to set the value to
   */
  public void setYearOperationMap(Map<String, FarmingOperation> yearOperationMap) {
    this.yearOperationMap = yearOperationMap;
  }

  /**
   * Line keys are sorted by the numeric line code
   * except for new items which are sorted after the others by line key. 
   * @return List<String>
   */
  public List<String> getLineKeys() {
    if(lineKeys == null) {
      List<AdjustmentGridItemData> itemList = new ArrayList<>();
      itemList.addAll(items.values());
      Collections.sort(itemList,
          new Comparator<AdjustmentGridItemData>() {
        @Override
        public int compare(AdjustmentGridItemData o1, AdjustmentGridItemData o2) {
          int result;

          if(o1.isNew() && o2.isNew()) {
            result = o1.getLineKey().compareTo(o2.getLineKey());
          } else if(o1.isNew() & !o2.isNew()) {
            result = 1;
          } else if(!o1.isNew() & o2.isNew()) {
            result = -1;
          } else {
            Integer lcInt1 = Integer.valueOf(o1.getLineCode());
            Integer lcInt2 = Integer.valueOf(o2.getLineCode());
            result = lcInt1.compareTo(lcInt2);
          }
          return result;
        }
      });
      
      lineKeys = new ArrayList<>();
      for(Iterator<AdjustmentGridItemData> ii = itemList.iterator(); ii.hasNext(); ) {
        AdjustmentGridItemData item = ii.next();
        lineKeys.add(item.getLineKey());
      }
    }
    return lineKeys;
  }
  
  /**
   * Clears out the form data (except the farmView parameter).
   * Action calls this before populating/repopulating the form.
   */
  public void clear() {
    items = new HashMap<>();
  }

}
