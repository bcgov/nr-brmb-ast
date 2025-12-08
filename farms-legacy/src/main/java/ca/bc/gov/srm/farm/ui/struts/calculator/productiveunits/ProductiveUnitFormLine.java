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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author awilkinson
 * @created Dec 22, 2010
 */
public class ProductiveUnitFormLine implements Serializable {

  private static final long serialVersionUID = 1L;
  
  /** Just in case having a number a the start of the line key causes problems */
  public static final String PREFIX = "p";

  public static final String TYPE_INVENTORY_ITEM = "inv";
  public static final String TYPE_STRUCTURE_GROUP = "str";

  public static final String TYPE_NEW = "z_new";

  private String lineKey;
  
  private String lineCode;
  
  private String lineCodeDescription;

  private String type;

  private boolean isNew = false;
  
  private String searchInput;
  
  private String multiStageCommodityCode;
  
  private Map<String, Boolean> errors = new HashMap<>();
  
  private Map<String, String> onFarmAcresValues = new HashMap<>();
  
  private Map<String, String> unseedableAcresValues = new HashMap<>();
  
  /** Map<participantDataSrcCode, pucFormData> */
  private Map<String, ProductiveUnitFormRecord> records = new HashMap<>();

  
  public static String getLineKey(String structureGroupCode, String inventoryItemCode) {
    String lineKey;
    if(structureGroupCode != null) {
      // prevent javascript error caused by code -1 - Unknown
      lineKey = PREFIX + structureGroupCode.replace('-', 'm') + TYPE_STRUCTURE_GROUP;
    } else {
      lineKey = PREFIX + inventoryItemCode.replace('-', 'm') + TYPE_INVENTORY_ITEM;
    }
    
    return lineKey;
  }

  /** @return Map<participantDataSrcCode, puc record> */
  public Map<String, ProductiveUnitFormRecord> getRecords() {
    return records;
  }
  
  public void setRecords(Map<String, ProductiveUnitFormRecord> records) {
    this.records = records;
  }

  /**
   * @return the lineKey
   */
  public String getLineKey() {
    return lineKey;
  }

  /**
   * @param lineKey the lineKey to set the value to
   */
  public void setLineKey(String lineKey) {
    this.lineKey = lineKey;
  }

  /**
   * @return the lineCode
   */
  public String getLineCode() {
    return lineCode;
  }

  /**
   * @param lineCode the lineCode to set the value to
   */
  public void setLineCode(String lineCode) {
    this.lineCode = lineCode;
  }

  /**
   * @return the lineCodeDescription
   */
  public String getLineCodeDescription() {
    return lineCodeDescription;
  }

  /**
   * @param lineCodeDescription the lineCodeDescription to set the value to
   */
  public void setLineCodeDescription(String lineCodeDescription) {
    this.lineCodeDescription = lineCodeDescription;
  }

  /**
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * @param type the type to set the value to
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * @return the isNew
   */
  public boolean isNew() {
    return isNew;
  }

  /**
   * @param newVal the isNew to set the value to
   */
  public void setNew(boolean newVal) {
    this.isNew = newVal;
  }

  /**
   * @return the searchInput
   */
  public String getSearchInput() {
    return searchInput;
  }

  /**
   * @param searchInput the searchInput to set the value to
   */
  public void setSearchInput(String searchInput) {
    this.searchInput = searchInput;
  }

  /**
   * @param participantDataSrcCode String
   * @return ProductiveUnitFormLine
   */
  public ProductiveUnitFormRecord getRecord(String participantDataSrcCode) {
    ProductiveUnitFormRecord record = records.get(participantDataSrcCode);
    if(record == null) {
      record = new ProductiveUnitFormRecord();
      record.setParticipantDataSrcCode(participantDataSrcCode);
      records.put(participantDataSrcCode, record);
    }
    return record;
  }
  
  /**
   * @param participantDataSrcCode String
   * @param record ProductiveUnitFormLine
   */
  public void setAdjusted(String participantDataSrcCode, ProductiveUnitFormRecord record) {
    records.put(participantDataSrcCode, record);
  }
  
  /**
   * @return the errors
   */
  public Map<String, Boolean> getErrors() {
    return errors;
  }
  
  /**
   * @param year String
   * @param value Boolean
   */
  public void setError(String year, Boolean value) {
    errors.put(year, value);
  }

  public String getMultiStageCommodityCode() {
    return multiStageCommodityCode;
  }

  public void setMultiStageCommodityCode(String multiStageCommodityCode) {
    this.multiStageCommodityCode = multiStageCommodityCode;
  }
  
  /**
   * @return the onFarmAcresValues
   */
  public Map<String, String> getOnFarmAcresValues() {
    return onFarmAcresValues;
  }

  /**
   * @return the unseedableAcresValues
   */
  public Map<String, String> getUnseedableAcresValues() {
    return unseedableAcresValues;
  }
  
  /**
   * @param year String
   * @param value String
   */
  public void setOnFarmAcres(String year, String value) {
    onFarmAcresValues.put(year, value);
  }
  
  /**
   * @param year String
   * @param value String
   */
  public void setUnseedableAcres(String year, String value) {
    unseedableAcresValues.put(year, value);
  }

}
