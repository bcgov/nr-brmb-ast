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
package ca.bc.gov.srm.farm.domain.codes;

import ca.bc.gov.srm.farm.util.StringUtils;

/**
 * @author awilkinson
 */
public final class CodeTables {

  /**  */
  private CodeTables() {
  }

  /* Generic code tables */
  public static final String CROP_UNIT = "CROP_UNIT";
  public static final String FEDERAL_ACCOUNTING = "FEDERAL_ACCOUNTING";
  public static final String FEDERAL_STATUS = "FEDERAL_STATUS";
  public static final String FARM_TYPE = "FARM_TYPE";
  public static final String PARTICIPANT_CLASS = "PARTICIPANT_CLASS";
  public static final String PARTICIPANT_LANGUAGE = "PARTICIPANT_LANGUAGE";
  public static final String PARTICIPANT_PROFILE = "PARTICIPANT_PROFILE";
  public static final String MUNICIPALITY = "MUNICIPALITY";
  public static final String REGIONAL_OFFICE = "REGIONAL_OFFICE";
  public static final String STRUCTURE_GROUP = "STRUCTURE_GROUP";
  public static final String INVENTORY_ITEM = "INVENTORY_ITEM";
  public static final String TRIAGE_QUEUE = "TRIAGE_QUEUE";
  public static final String SECTOR = "SECTOR";
  public static final String SECTOR_DETAIL = "SECTOR_DETAIL";
  public static final String LINE_ITEMS = "LINE_ITEMS";

  /* Editable generic code tables */
  public static final String[] GENERIC_CODE_TABLES = {
    CROP_UNIT,
    FARM_TYPE,
    FEDERAL_ACCOUNTING,
    FEDERAL_STATUS,
    PARTICIPANT_CLASS,
    PARTICIPANT_LANGUAGE,
    PARTICIPANT_PROFILE,
    TRIAGE_QUEUE,
  };

  public static boolean canCreate(String codeTable) {
    return ! StringUtils.isOneOf(codeTable, CodeTables.TRIAGE_QUEUE);
  }
  
  public static boolean canDelete(String codeTable) {
    return ! StringUtils.isOneOf(codeTable, CodeTables.TRIAGE_QUEUE);
  }
}
