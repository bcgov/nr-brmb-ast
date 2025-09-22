/**
 *
 * Copyright (c) 2009,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.cache;

/**
 * CacheKeys.
 */
public final class CacheKeys {

  /** Private Constructor. */
  private CacheKeys() {

  }

  public static final String CURRENT_BUSINESS_ACTION =
    "current.business.action";

  public static final String CURRENT_USER = "current.user";

  public static final String CURRENT_ENROLMENT_PINS = "current.enrolment.pins";

  public static final String CURRENT_ACCOUNT = "current.account";

  public static final String CURRENT_CLIENTS = "current.clients";

  public static final String CURRENT_IMPORT = "current.import";

  public static final String STAGING_RESULTS = "current.staging.results";

  public static final String IMPORT_RESULTS = "current.import.results";
  
  public static final String IMPORT_LOG = "current.import.log";
  
  public static final String IMPORT_VERSION = "current.import.version";

  public static final String CALCULATOR_INBOX_CONTEXT = "calculator.inbox.context";
  
  public static final String LINE_ITEMS_FILTER_CONTEXT = "line.items.filter.context";

  public static final String YEAR_CONFIGURATION_PARAMETER_FILTER_CONTEXT = "year.configuration.parameter.filter.context";
  
  public static final String BPU_FILTER_CONTEXT = "bpu.filter.context";
  
  public static final String ENROLMENTS_FILTER_CONTEXT = "enrolments.filter.context";
  
  public static final String CROP_UNIT_CONVERSION_FILTER_CONTEXT = "crop.unit.conversion.filter.context";
  
  public static final String TIP_REPORTS_FILTER_CONTEXT = "tip.reports.filter.context";

  public static final String FMV_FILTER_CONTEXT = "fmv.filter.context";
  
  public static final String INVENTORY_ITEM_CODE_FILTER_CONTEXT =
      "inventory.item.code.filter.context";
  
  public static final String EXPECTED_PRODUCTION_ITEM_CODE_FILTER_CONTEXT =
      "expected.production.item.filter.context";  
  
  public static final String INVENTORY_CROP_XREF_FILTER_CONTEXT =
      "inventory.crop.xref.filter.context";
  
  public static final String INVENTORY_LIVESTOCK_XREF_FILTER_CONTEXT =
      "inventory.livestock.xref.filter.context";
  
  public static final String INVENTORY_INPUT_XREF_FILTER_CONTEXT =
      "inventory.input.xref.filter.context";
  
  public static final String INVENTORY_RECEIVABLE_XREF_FILTER_CONTEXT =
      "inventory.receivable.xref.filter.context";
  
  public static final String INVENTORY_PAYABLE_XREF_FILTER_CONTEXT =
      "inventory.payable.xref.filter.context";

  public static final String STRUCTURE_GROUP_CODE_FILTER_CONTEXT =
      "structure.group.code.filter.context";

  /** Account PIN and year will be appended to this to form the cache key */
  public static final String SCENARIO_PREFIX = "scenario.";
  
  /** Account PIN and year will be appended to this to form the cache key */
  public static final String FARM_VIEW_PREFIX = "farm.view.";

  public static final String SECTOR_CODES_FILTER_CONTEXT = "sector.codes.filter.context";

  public static final String SECTOR_DETAIL_CODES_FILTER_CONTEXT = "sector.detail.codes.filter.context";

  public static final String CHEFS_SUBMISSION_FORM_TYPE_FILTER_CONTEXT = "chefs.submission.form.type.filter.context";
  
  /**
   * @param pin int
   * @param year int
   * @param scenarioNumber Integer
   * @return String
   */
  public static String getScenarioCacheKey(final int pin, final int year, Integer scenarioNumber) {
    return CacheKeys.SCENARIO_PREFIX + pin + "." + year + "." + scenarioNumber;
  }
  
  /**
   * @param pin int
   * @param year int
   * @param scenarioNumber Integer
   * @return String
   */
  public static String getFarmViewCacheKey(final int pin, final int year, Integer scenarioNumber) {
    return CacheKeys.FARM_VIEW_PREFIX + pin + "." + year + "." + scenarioNumber;
  }

}
