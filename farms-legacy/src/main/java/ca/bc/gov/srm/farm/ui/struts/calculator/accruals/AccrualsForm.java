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
package ca.bc.gov.srm.farm.ui.struts.calculator.accruals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.domain.FarmingOperationImportOption;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.ui.struts.calculator.search.CalculatorSearchForm;

/**
 * @author awilkinson
 * @created Feb 22, 2011
 */
public class AccrualsForm extends CalculatorSearchForm {

  private static final long serialVersionUID = -2645502921365405790L;
  
  /** Map<lineKey, AccrualFormData>
   * key is a type prefix plus inventory item code or structure group code */
  private Map<String, AccrualFormData> items = new HashMap<>();
  
  private List<String> lineKeys;

  private String farmViewYear;

  private List<ListView> farmViewYearOptions;
  
  private ListView[] inventoryItems;
  
  private String[] itemTypes = {
      AccrualFormData.getInputType(),
      AccrualFormData.getReceivableType(),
      AccrualFormData.getPayableType()
  };

  private List<Boolean> eligibilityFilterValues;

  private String itemTypeRadio;
  private String eligibilityRadio;
  private String financialViewRadio;
  
  /** set to true when the Add New button is clicked */
  private boolean addedNew = false;
  
  /** Map<itemType, value> */
  private Map<String, Double> totalValues;
  
  private Double deferredProgramPayments;
  
  private boolean programYear;
  
  private List<FarmingOperationImportOption> operationsForImport;

  private Integer importScenarioNumber;
  
  private String importOperationSchedule;
  
  public AccrualsForm() {

    eligibilityFilterValues = new ArrayList<>();
    eligibilityFilterValues.add(Boolean.TRUE);
    eligibilityFilterValues.add(Boolean.FALSE);
  }

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
   * @return the eligibilityFilterValues
   */
  public List<Boolean> getEligibilityFilterValues() {
    return eligibilityFilterValues;
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
  public Map<String, AccrualFormData> getItems() {
    return items;
  }

  /**
   * @param items the items to set the value to
   */
  public void setItems(Map<String, AccrualFormData> items) {
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
   * @return the eligibilityRadio
   */
  public String getEligibilityRadio() {
    return eligibilityRadio;
  }


  /**
   * @param eligibilityRadio the eligibilityRadio to set
   */
  public void setEligibilityRadio(String eligibilityRadio) {
    this.eligibilityRadio = eligibilityRadio;
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

  /**
   * Gets programYear
   *
   * @return the programYear
   */
  public boolean isProgramYear() {
    return programYear;
  }

  /**
   * Sets programYear
   *
   * @param pProgramYear the programYear to set
   */
  public void setProgramYear(boolean pProgramYear) {
    programYear = pProgramYear;
  }

  /**
   * Gets deferredProgramPayments
   *
   * @return the deferredProgramPayments
   */
  public Double getDeferredProgramPayments() {
    return deferredProgramPayments;
  }

  /**
   * Sets deferredProgramPayments
   *
   * @param pDeferredProgramPayments the deferredProgramPayments to set
   */
  public void setDeferredProgramPayments(Double pDeferredProgramPayments) {
    deferredProgramPayments = pDeferredProgramPayments;
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
      List<AccrualFormData> itemList = new ArrayList<>();
      itemList.addAll(items.values());
      Collections.sort(itemList,
          new Comparator<AccrualFormData>() {
        @Override
        public int compare(AccrualFormData i1, AccrualFormData i2) {
          int result;

          if(i1.isNew() && i2.isNew()) {
            result = i1.getLineKey().compareTo(i2.getLineKey());
          } else if(i1.isNew() & !i2.isNew()) {
            result = 1;
          } else if(!i1.isNew() & i2.isNew()) {
            result = -1;
          } else {
            Integer lcInt1 = Integer.valueOf(i1.getLineCode());
            Integer lcInt2 = Integer.valueOf(i2.getLineCode());
            result = lcInt1.compareTo(lcInt2);
          }
          return result;
        }
      });
      
      lineKeys = new ArrayList<>();
      for(AccrualFormData item : itemList) {
        lineKeys.add(item.getLineKey());
      }
    }
    return lineKeys;
  }

  /**
   * @param lineKey String
   * @return AccrualFormData
   */
  public AccrualFormData getItem(String lineKey) {
    AccrualFormData fd = items.get(lineKey);
    if(fd == null) {
      fd = new AccrualFormData();
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
  public String getTypeInput() {
    return AccrualFormData.getItemType(InventoryClassCodes.INPUT);
  }
  
  /**
   * @return String
   */
  public String getTypeReceivable() {
    return AccrualFormData.getItemType(InventoryClassCodes.RECEIVABLE);
  }
  
  /**
   * @return String
   */
  public String getTypePayable() {
    return AccrualFormData.getItemType(InventoryClassCodes.PAYABLE);
  }

  public boolean isAccrualsAvailableForImport() {
    return ! operationsForImport.isEmpty();
  }

}
