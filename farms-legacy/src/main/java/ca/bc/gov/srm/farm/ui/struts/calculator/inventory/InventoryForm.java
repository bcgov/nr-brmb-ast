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
package ca.bc.gov.srm.farm.ui.struts.calculator.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.domain.FarmingOperationImportOption;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.ui.struts.calculator.search.CalculatorSearchForm;

/**
 * @author awilkinson
 * @created Jan 25, 2011
 */
public class InventoryForm extends CalculatorSearchForm {

  private static final long serialVersionUID = -2645502968765405790L;
  
  /** Map<String lineKey, InventoryFormData>
   * key is a type prefix plus inventory item code or structure group code */
  private Map<String, InventoryFormData> items = new HashMap<>();

  private List<String> lineKeys;

  private String farmViewYear;

  /** List<ListView> */
  private List<ListView> farmViewYearOptions;
  
  private ListView[] inventoryItems;
  
  private String[] itemTypes = {
      InventoryFormData.getCropType(),
      InventoryFormData.getLivestockType()
  };

  private String itemTypeRadio;
  private String financialViewRadio;

  private Map<String, Double> totalValues;
  
  /** set to true when the Add New button is clicked */
  private boolean addedNew = false;
  
  private List<FarmingOperationImportOption> operationsForImport;

  private Integer importScenarioNumber;
  
  private String importOperationSchedule;


  /**
   * @return the farmViewYear
   */
  public String getFarmViewYear() {
    return farmViewYear;
  }

  /**
   * @param farmViewYear the farmViewYear to set the value to
   */
  public void setFarmViewYear(String farmViewYear) {
    this.farmViewYear = farmViewYear;
  }

  /**
   * @return the years
   */
  public List<ListView> getFarmViewYearOptions() {
    return farmViewYearOptions;
  }

  /**
   * @param years the years to set the value to
   */
  public void setFarmViewYearOptions(List<ListView> years) {
    this.farmViewYearOptions = years;
  }

  /**
   * @return the inventoryItems
   */
  public ListView[] getInventoryItems() {
    return inventoryItems;
  }

  /**
   * @param inventoryItems the inventoryItems to set the value to
   */
  public void setInventoryItems(ListView[] inventoryItems) {
    this.inventoryItems = inventoryItems;
  }

  /**
   * @return the itemTypes
   */
  public String[] getItemTypes() {
    return itemTypes;
  }

  /**
   * @param itemTypes the itemTypes to set the value to
   */
  public void setItemTypes(String[] itemTypes) {
    this.itemTypes = itemTypes;
  }

  /**
   * @return the totalValues
   */
  public Map<String, Double> getTotalValues() {
    return totalValues;
  }

  /**
   * @param totalValues the totalValues to set the value to
   */
  public void setTotalValues(Map<String, Double> totalValues) {
    this.totalValues = totalValues;
  }

  /**
   * @return the items
   */
  public Map<String, InventoryFormData> getItems() {
    return items;
  }

  /**
   * @param items the items to set the value to
   */
  public void setItems(Map<String, InventoryFormData> items) {
    this.items = items;
  }

  /**
   * @return the financialViewRadio
   */
  public String getFinancialViewRadio() {
    return financialViewRadio;
  }

  /**
   * @param financialViewRadio the financialViewRadio to set the value to
   */
  public void setFinancialViewRadio(String financialViewRadio) {
    this.financialViewRadio = financialViewRadio;
  }

  /**
   * @return the itemTypeRadio
   */
  public String getItemTypeRadio() {
    return itemTypeRadio;
  }

  /**
   * @param itemTypeRadio the itemTypeRadio to set the value to
   */
  public void setItemTypeRadio(String itemTypeRadio) {
    this.itemTypeRadio = itemTypeRadio;
  }

  /**
   * @return the addedNew
   */
  public boolean isAddedNew() {
    return addedNew;
  }

  /**
   * @param addedNew the addedNew to set the value to
   */
  public void setAddedNew(boolean addedNew) {
    this.addedNew = addedNew;
  }

  public List<FarmingOperationImportOption> getOperationsForImport() {
    return operationsForImport;
  }

  public void setOperationsForImport(List<FarmingOperationImportOption> operationsForImport) {
    this.operationsForImport = operationsForImport;
  }

  public Integer getImportScenarioNumber() {
    return importScenarioNumber;
  }

  public void setImportScenarioNumber(Integer importScenarioNumber) {
    this.importScenarioNumber = importScenarioNumber;
  }

  public String getImportOperationSchedule() {
    return importOperationSchedule;
  }

  public void setImportOperationSchedule(String importOperationSchedule) {
    this.importOperationSchedule = importOperationSchedule;
  }

  /**
   * Line keys are sorted by the numeric line code
   * except for new items which are sorted after the others by line key. 
   * @return List<String>
   */
  public List<String> getLineKeys() {
    if(lineKeys == null) {
      List<InventoryFormData> itemList = new ArrayList<>();
      itemList.addAll(items.values());
      Collections.sort(itemList,
          new Comparator<InventoryFormData>() {
        @SuppressWarnings("null")
        @Override
        public int compare(InventoryFormData o1, InventoryFormData o2) {
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
            if(result == 0) {
              String unit1 = o1.getCropUnitCodeDescription();
              String unit2 = o2.getCropUnitCodeDescription();
              if(unit1 == null && unit2 == null) {
                result = 0;
              } else if(unit1 == null && unit2 != null) {
                result = -1;
              } else if(unit1 != null && unit2 == null) {
                result = 1;
              } else {
                result = unit1.compareTo(unit2);
              }
            }
          }
          return result;
        }
      });
      
      lineKeys = new ArrayList<>();
      for(Iterator<InventoryFormData> ii = itemList.iterator(); ii.hasNext(); ) {
        InventoryFormData item = ii.next();
        lineKeys.add(item.getLineKey());
      }
    }
    return lineKeys;
  }

  /**
   * @param lineKey String
   * @return InventoryFormData
   */
  public InventoryFormData getItem(String lineKey) {
    InventoryFormData fd = items.get(lineKey);
    if(fd == null) {
      fd = new InventoryFormData();
      items.put(lineKey, fd);
    }
    fd.setLineKey(lineKey);
    return fd;
  }
  
  /**
   * Clears out form data
   * Action calls this before populating/repopulating the form.
   */
  public void clear() {
    items = new HashMap<>();
  }

  /**
   * @return String
   */
  public String getTypeCrop() {
    return InventoryFormData.getItemType(InventoryClassCodes.CROP);
  }
  
  /**
   * @return String
   */
  public String getTypeLivestock() {
    return InventoryFormData.getItemType(InventoryClassCodes.LIVESTOCK);
  }

  public boolean isInventoryAvailableForImport() {
    return ! operationsForImport.isEmpty();
  }

}
