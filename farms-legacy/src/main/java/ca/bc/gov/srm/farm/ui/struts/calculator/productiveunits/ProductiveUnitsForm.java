/**
 * 
 * Copyright (c) 2010,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.productiveunits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.FarmingOperationImportOption;
import ca.bc.gov.srm.farm.domain.ScenarioMetaData;
import ca.bc.gov.srm.farm.domain.codes.ParticipantDataSrcCodes;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.ui.struts.calculator.search.CalculatorSearchForm;

/**
 * @author awilkinson
 * @created Dec 21, 2010
 */
public class ProductiveUnitsForm extends CalculatorSearchForm {

  private static final long serialVersionUID = -2649172921365403512L;

  public static final String FINANCIAL_VIEW_ADJUSTED = "adjusted";
  public static final String FINANCIAL_VIEW_ADJUSTMENTS = "adjustments";
  public static final String FINANCIAL_VIEW_CRA = "cra";

  public static final String UNITS_VIEW_ROLLUP = "rollup";
  public static final String UNITS_VIEW_ON_FARM = "onFarm";
  public static final String UNITS_VIEW_UNSEEDABLE = "unseedable";
  
  public static final String PRODUCTION_INSURANCE = "productionInsurance";

  private ListView[] inventoryItems;

  private ListView[] structureGroups;

  private List<ScenarioMetaData> scenarioMetaList;
  
  private List<FarmingOperationImportOption> operationsForImport;

  private String financialViewRadio;

  private String unitsViewRadio;

  private String viewDataSetRadio;

  private String dataSetUsedRadio;

  /** Map<String lineKey, ProductiveUnitFormLine>
   *  key is a type prefix plus inventory item code or structure group code 
   */
  private Map<String, ProductiveUnitFormLine> items = new HashMap<>();
  private Map<String, ProductiveUnitFormLine> rolledUpItems = new HashMap<>();
  
  private Map<String, FarmingOperation> yearOperationMap;
  
  private List<String> lineKeys;
  private List<String> rolledUpLineKeys;

  private Integer importScenarioNumber;
  
  private String importOperationSchedule;
  
  private String participantDataSrcCode;

  /** set to true when the Add New button is clicked */
  private boolean addedNew = false;

  private boolean isBaseData = false;

  /** Map<year, value> */
  private Map<String, List<String>> participantDataSrcCodesByYear = new HashMap<>();

  /**
   * @return the items
   */
  public Map<String, ProductiveUnitFormLine> getItems() {
    return items;
  }

  /**
   * @return the rolled up items
   */
  public Map<String, ProductiveUnitFormLine> getRolledUpItems() {
    return rolledUpItems;
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

  public List<String> getLineKeys() {
    return getLineKeys(false);
  }

  public List<String> getRolledUpLineKeys() {
    return getLineKeys(true);
  }

  public List<String> getLineKeysCra() {
    return this.isBaseData ? getLineKeysCra(false) : getLineKeys();
  }

  public List<String> getRolledUpLineKeysCra() {
    return this.isBaseData ? getLineKeysCra(true) : getRolledUpLineKeys();
  }

  public List<String> getLineKeysCra(boolean isRolledUp) {
    List<String> lineKeysCra = new ArrayList<>(getLineKeys(isRolledUp));

    // filter out those line keys that don't have CRA value
    Iterator<String> iterator = lineKeysCra.iterator();
    while (iterator.hasNext()) {
      String lineKey = iterator.next();
      boolean hasValue = false;
      for (String year : this.getRequiredYears()) {
        for (String curParticipantDataSrcCode : this.participantDataSrcCodesByYear.get(year)) {
          ProductiveUnitFormLine item = isRolledUp ? this.rolledUpItems.get(lineKey) : this.items.get(lineKey);
          ProductiveUnitFormRecord record = item.getRecord(curParticipantDataSrcCode);
          String craValue = record.getCra(year);
          hasValue |= (craValue != null && craValue.trim().length() > 0);
        }
      }
      if (!hasValue) {
        iterator.remove();
      }
    }

    return lineKeysCra;
  }

  /**
   * Line keys are sorted by the numeric line code
   * except for new items which are sorted after the others by line key. 
   * @return List<String>
   */
  public List<String> getLineKeys(boolean isRolledUp) {
    List<String> newLineKeys = isRolledUp ? rolledUpLineKeys : lineKeys;

    if(newLineKeys == null) {
      List<ProductiveUnitFormLine> itemList = new ArrayList<>();
      itemList.addAll(isRolledUp ? rolledUpItems.values() : items.values());
      Collections.sort(itemList,
          new Comparator<ProductiveUnitFormLine>() {
        @Override
        public int compare(ProductiveUnitFormLine o1, ProductiveUnitFormLine o2) {
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
      
      newLineKeys = new ArrayList<>();
      for(Iterator<ProductiveUnitFormLine> ii = itemList.iterator(); ii.hasNext(); ) {
        ProductiveUnitFormLine item = ii.next();
        newLineKeys.add(item.getLineKey());
      }

      if(isRolledUp) {
        rolledUpLineKeys = newLineKeys;
      } else {
        lineKeys = newLineKeys;
      }
    }
    return newLineKeys;
  }
  
  /**
   * Clears out the form data (except the farmView parameter).
   * Action calls this before populating/repopulating the form.
   */
  public void clear() {
    items = new HashMap<>();
    rolledUpItems = new HashMap<>();
  }


  public ProductiveUnitFormLine getItem(String lineKey) {
    ProductiveUnitFormLine result = items.get(lineKey);
    if(result == null) {
      result = new ProductiveUnitFormLine();
      items.put(lineKey, result);
    }
    result.setLineKey(lineKey);
    return result;
  }

  public ProductiveUnitFormLine getRolledUpItem(String rolledUpLineKey) {
    ProductiveUnitFormLine result = rolledUpItems.get(rolledUpLineKey);
    if(result == null) {
      result = new ProductiveUnitFormLine();
      rolledUpItems.put(rolledUpLineKey, result);
    }
    result.setLineKey(rolledUpLineKey);
    return result;
  }

  public ListView[] getInventoryItems() {
    return inventoryItems;
  }

  public void setInventoryItems(ListView[] inventoryItems) {
    this.inventoryItems = inventoryItems;
  }

  public ListView[] getStructureGroups() {
    return structureGroups;
  }

  public void setStructureGroups(ListView[] structureGroups) {
    this.structureGroups = structureGroups;
  }
  
  public List<ScenarioMetaData> getScenarioMetaList() {
    return scenarioMetaList;
  }

  public void setScenarioMetaList(List<ScenarioMetaData> scenarioMetaList) {
    this.scenarioMetaList = scenarioMetaList;
  }

  public List<FarmingOperationImportOption> getOperationsForImport() {
    return operationsForImport;
  }

  public void setOperationsForImport(List<FarmingOperationImportOption> operationsForImport) {
    this.operationsForImport = operationsForImport;
  }

  public String getFinancialViewRadio() {
    return financialViewRadio;
  }

  public void setFinancialViewRadio(String financialViewRadio) {
    this.financialViewRadio = financialViewRadio;
  }

  public String getUnitsViewRadio() {
    return unitsViewRadio;
  }

  public void setUnitsViewRadio(String unitsViewRadio) {
    this.unitsViewRadio = unitsViewRadio;
  }

  public boolean isAddedNew() {
    return addedNew;
  }

  public void setAddedNew(boolean addedNew) {
    this.addedNew = addedNew;
  }

  public String getPucTypeNew() {
    return ProductiveUnitFormLine.TYPE_NEW;
  }

  public String getPucTypeInventory() {
    return ProductiveUnitFormLine.TYPE_INVENTORY_ITEM;
  }

  public String getPucTypeStructureGroup() {
    return ProductiveUnitFormLine.TYPE_STRUCTURE_GROUP;
  }

  public String getFinancialViewAdjusted() {
    return FINANCIAL_VIEW_ADJUSTED;
  }

  public String getFinancialViewAdjustments() {
    return FINANCIAL_VIEW_ADJUSTMENTS;
  }

  public String getFinancialViewCra() {
    return FINANCIAL_VIEW_CRA;
  }

  public String getUnitsViewRollup() {
    return UNITS_VIEW_ROLLUP;
  }

  public String getUnitsViewOnFarm() {
    return UNITS_VIEW_ON_FARM;
  }

  public String getUnitsViewUnseedable() {
    return UNITS_VIEW_UNSEEDABLE;
  }

  public String getViewDataSetRadio() {
    return viewDataSetRadio;
  }

  public void setViewDataSetRadio(String viewDataSetRadio) {
    this.viewDataSetRadio = viewDataSetRadio;
  }

  public String getDataSetUsedRadio() {
    return dataSetUsedRadio;
  }

  public void setDataSetUsedRadio(String dataSetUsedRadio) {
    this.dataSetUsedRadio = dataSetUsedRadio;
  }

  public String getDataSetCra() {
    return ParticipantDataSrcCodes.CRA;
  }

  public String getDataSetProductioniInsurance() {
    return PRODUCTION_INSURANCE;
  }

  public String getDataSetLocalSupplemental() {
    return ParticipantDataSrcCodes.LOCAL;
  }

  public String getViewDataCra() {
    return ParticipantDataSrcCodes.CRA;
  }

  public String getViewDataProductioniInsurance() {
    return PRODUCTION_INSURANCE;
  }

  public String getViewDataLocalSupplemental() {
    return ParticipantDataSrcCodes.LOCAL;
  }

  public Integer getImportScenarioNumber() {
    return importScenarioNumber;
  }

  public void setImportScenarioNumber(Integer importScenarioNumber) {
    this.importScenarioNumber = importScenarioNumber;
  }

  public String getParticipantDataSrcCode() {
    return participantDataSrcCode;
  }

  public void setParticipantDataSrcCode(String participantDataSrcCode) {
    this.participantDataSrcCode = participantDataSrcCode;
  }
  
  public Map<String, List<String>> getParticipantDataSrcCodesByYear() {
    return participantDataSrcCodesByYear;
  }

  public void setParticipantDataSrcCodesByYear(Map<String, List<String>> participantDataSrcCodesByYear) {
    this.participantDataSrcCodesByYear = participantDataSrcCodesByYear;
  }

  public String getImportOperationSchedule() {
    return importOperationSchedule;
  }

  public void setImportOperationSchedule(String importOperationSchedule) {
    this.importOperationSchedule = importOperationSchedule;
  }

  public boolean isCraProductiveUnitsAvailableForImport() {
    return operationsForImport.stream()
        .anyMatch(o -> o.getHasCraProductiveUnits());
  }

  public boolean isLocalProductiveUnitsAvailableForImport() {
    return operationsForImport.stream()
        .anyMatch(o -> o.getHasLocalProductiveUnits());
  }

  public void setIsBaseData(boolean isBaseData) {
    this.isBaseData = isBaseData;
  }
}
