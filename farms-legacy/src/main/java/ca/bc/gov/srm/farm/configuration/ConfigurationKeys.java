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
package ca.bc.gov.srm.farm.configuration;

import java.util.Arrays;
import java.util.List;

/**
 * ConfigurationKeys.
 */
public final class ConfigurationKeys {

  /** Creates a new ConfigurationKeys object. */
  private ConfigurationKeys() {

  }

  public static final String APPLICATION_ENVIRONMENT =
    "application.environment";

  public static final String APPLICATION_VERSION =
    "application.version";

  public static final String APPLICATION_CODE = "application.code";

  public static final String IMPORT_VIRUS_SCAN_DIR =
    "app.config/import/virus.scan.dir";

  public static final String IMPORT_STAGING_START_TIME =
    "app.config/import/staging.window.start";

  public static final String IMPORT_STAGING_END_TIME =
    "app.config/import/staging.window.end";

  public static final String IMPORT_START_TIME =
    "app.config/import/import.window.start";

  public static final String IMPORT_END_TIME =
    "app.config/import/import.window.end";

  public static final String BUSINESS_START_TIME =
    "app.config/import/business.window.start";

  public static final String BUSINESS_END_TIME =
    "app.config/import/business.window.end";

  public static final String IMPORT_TIMER_FREQ =
    "app.config/import/timer.frequency";

  public static final String REPORTS_SERVER =
    "app.config/reports/oracle.reports.server";

  public static final String DIRECT_REPORTS_SERVER =
    "app.config/reports/java.reports.server";

  public static final String BCEID_WEB_SERVICE_URL =
    "app.config/bceid/web.service.url";

  public static final String BCEID_USER_ID = "app.config/bceid/user.id";

  public static final String BCEID_USER_PASSWORD =
    "app.config/bceid/user.password";

  public static final String BCEID_ONLINE_SERVICE_ID =
    "app.config/bceid/online.service.id";

  public static final String BCEID_EXPIRY_PERIOD =
    "app.config/bceid/expiry.period";

  public static final String HELP_URL =
    "app.config/help/url";

  public static final String HELP_FILE_PREFIX =
    "app.config/help/file.prefix";

  public static final String HELP_FILE_SUFFIX =
    "app.config/help/file.suffix";

  public static final String FIRST_BC_ADMIN_YEAR =
    "app.config/webapp/first.bc.admin.year";

  public static final String SIGN_OUT_URL =
    "app.config/webapp/sign.out.url";

  public static final String CRM_DYNAMICS_URL =
      "app.config/crm/dynamics.url";

  public static final String CRM_API_VERSION =
      "app.config/crm/api.version";

  public static final String CRM_AUTHORITY_URL =
      "app.config/crm/authority.url";

  public static final String CRM_CLIENT_ID =
      "app.config/crm/client.id";

  public static final String CRM_CLIENT_SECRET =
      "app.config/crm/client.secret";
  
  public static final String ENROLMENT_FEE_MODIFIED_BY_USER_OVERRIDE =
      "app.config/crm/enrolment.fee.modified.by.user.override";

  public static final String ENROLMENT_START_YEAR =
      "app.config/enrolment/start.year";

  public static final String ENROLMENT_YEARS_AHEAD =
      "app.config/enrolment/years.ahead";
  
  public static final String COMBINED_FARM_LOAD_SELF_SCENARIO_FROM_DATABASE =
      "combined.farm.load.self.scenario.from.database";

  public static final String CHEFS_URL =
      "app.config/chefs/api.url";

  public static final String CHEFS_API_VERSION =
      "app.config/chefs/api.version";

  public static final String CHEFS_AGENT_ENABLED =
      "app.config/chefs/agent.enabled";
  
  
  public static final String FIFO_AGENT_ENABLED =
      "app.config/fifo/agent.enabled";
  
  public static final String REPORTS_JASPER_URL =
      "app.config/reports/jasper.rest.url";
  public static final String REPORTS_JASPER_USERNAME =
      "app.config/reports/jasper.user.name";
  public static final String REPORTS_JASPER_PASSWORD =
      "app.config/reports/jasper.user.password";
  public static final String REPORTS_JASPER_REPORTS_PATH =
      "app.config/reports/jasper.reports.path";
  

  /*************************************************************************************
   ****  Global parameters (not configured by year - FARM_CONFIGURATION_PARAMETERS) ****
   *************************************************************************************/
  public static final String REASONABILITY_BENEFIT_RISK_VARIANCE_PREF =
      "Reasonability - Benefit Risk - Benefit Variance Limit";

  public static final String REASONABILITY_BENEFIT_RISK_DEDUCTIBLE_PREF =
      "Reasonability - Benefit Risk - Deductible %";
  
  public static final String REASONABILITY_BENEFIT_RISK_PAYOUT_LEVEL_PREF =
      "Reasonability - Benefit Risk - Payout Level %";
  
  public static final String REASONABILITY_MARGIN_TEST_REFERENCE_VARIANCE_PREF =
      "Reasonability - Margin - Reference Margin Variance Limit";

  public static final String REASONABILITY_MARGIN_TEST_INDUSTRY_VARIANCE_PREF =
      "Reasonability - Margin - Industry Average Variance Limit";

  public static final String REASONABILITY_STRUCTURAL_CHANGE_TEST_RATIO_ADDITIVE_DIFF_VARIANCE_PREF =
      "Reasonability - Structural Change - Ratio/Additive Difference Variance Limit";

  public static final String REASONABILITY_STRUCTURAL_CHANGE_TEST_ADDITIVE_DIVISION_VARIANCE_PREF =
      "Reasonability - Structural Change - Additive Division Variance Limit";

  public static final String REASONABILITY_STRUCTURAL_CHANGE_TEST_FARM_SIZE_RATIO_PREF =
      "Reasonability - Structural Change - Farm Size Ratio Limit";

  public static final String REASONABILITY_EXPENSE_TEST_IAC_INDUSTRY_VARIANCE_PREF =
      "Reasonability - Expense - Industry Average Comparison - Industry Average Variance Limit";

  public static final String REASONABILITY_PRODUCTION_FORAGE_TEST_VARIANCE_PREF =
      "Reasonability - Production - Forage - Variance Limit";

  public static final String REASONABILITY_PRODUCTION_FRUIT_VEG_TEST_VARIANCE_PREF =
      "Reasonability - Production - Fruit Veg - Variance Limit";

  public static final String REASONABILITY_PRODUCTION_GRAINS_TEST_VARIANCE_PREF =
      "Reasonability - Production - Grains - Variance Limit";

  public static final String REASONABILITY_EXPENSE_TEST_GC_VARIANCE_PREF =
      "Reasonability - Expense - Reference Year Comparison - General Crops - Variance Limit";

  public static final String REASONABILITY_REVENUE_RISK_TEST_FORAGE_GRAIN_VARIANCE_PREF =
      "Reasonability - Revenue Risk - Forage, Forage Seed, Grain - Variance Limit";

  public static final String REASONABILITY_REVENUE_RISK_TEST_FED_PER_PRODUCTIVE_UNIT_CATTLE =
      "Reasonability - Revenue Risk - Forage, Forage Seed, Grain - Fed Out Tonnes - 104 - Cattle";

  public static final String REASONABILITY_REVENUE_RISK_TEST_FED_PER_PRODUCTIVE_UNIT_FEEDER_CATTLE =
      "Reasonability - Revenue Risk - Forage, Forage Seed, Grain - Fed Out Tonnes - 105 - Feeder Cattle (Under 900 lbs)";

  public static final String REASONABILITY_REVENUE_RISK_TEST_FED_PER_PRODUCTIVE_UNIT_FINISHED_CATTLE =
      "Reasonability - Revenue Risk - Forage, Forage Seed, Grain - Fed Out Tonnes - 106 - Finished Cattle (Over 900 lbs)";

  public static final String REASONABILITY_REVENUE_RISK_TEST_FED_PER_PRODUCTIVE_UNIT_CATTLE_BRED_HEIFERS =
      "Reasonability - Revenue Risk - Forage, Forage Seed, Grain - Fed Out Tonnes - 171 - Cattle, Bred Heifers";

  public static final String REASONABILITY_REVENUE_RISK_TEST_CATTLE_VARIANCE_PREF =
      "Reasonability - Revenue Risk - Cattle - Variance Limit";
  
  public static final String REASONABILITY_REVENUE_RISK_TEST_CATTLE_DEATH_RATE_PREF =
      "Reasonability - Revenue Risk - Cattle - Death Rate";
  
  public static final String REASONABILITY_REVENUE_RISK_TEST_NURSERY_VARIANCE_PREF =
      "Reasonability - Revenue Risk - Nursery - Variance Limit";
  
  public static final String REASONABILITY_REVENUE_RISK_TEST_HOGS_FARROW_TO_FINISH_VARIANCE_PREF =
      "Reasonability - Revenue Risk - Hogs - Farrow to Finish - Variance Limit";
  
  public static final String REASONABILITY_REVENUE_RISK_TEST_HOGS_FARROW_TO_FINISH_DEATH_RATE_PREF =
      "Reasonability - Revenue Risk - Hogs - Farrow to Finish - Death Rate";
  
  public static final String REASONABILITY_REVENUE_RISK_TEST_HOGS_FARROW_TO_FINISH_BIRTHS_PER_CYCLE_PREF =
      "Reasonability - Revenue Risk - Hogs - Farrow to Finish - Births Per Cycle";
  
  public static final String REASONABILITY_REVENUE_RISK_TEST_HOGS_FARROW_TO_FINISH_BIRTH_CYCLES_PER_YEAR_PREF =
      "Reasonability - Revenue Risk - Hogs - Farrow to Finish - Birth Cycles per Year";
  
  public static final String REASONABILITY_REVENUE_RISK_TEST_HOGS_FEEDER_VARIANCE_PREF =
      "Reasonability - Revenue Risk - Hogs - Feeder - Variance Limit";
  
  public static final String REASONABILITY_REVENUE_RISK_TEST_POULTRY_BROILERS_CHICKENS_VARIANCE_PREF =
      "Reasonability - Revenue Risk - Poultry - Broilers  - Chickens - Variance Limit";
  
  public static final String REASONABILITY_REVENUE_RISK_TEST_POULTRY_BROILERS_CHICKENS_AVERAGE_WEIGHT_PREF =
      "Reasonability - Revenue Risk - Poultry - Broilers - Chickens - Average Weight (kg)";
  
  public static final String REASONABILITY_REVENUE_RISK_TEST_POULTRY_BROILERS_TURKEYS_VARIANCE_PREF =
      "Reasonability - Revenue Risk - Poultry - Broilers - Turkeys - Variance Limit";
  
  public static final String REASONABILITY_REVENUE_RISK_TEST_POULTRY_BROILERS_TURKEYS_AVERAGE_WEIGHT_PREF =
      "Reasonability - Revenue Risk - Poultry - Broilers - Turkeys - Average Weight (kg)";
  
  public static final String REASONABILITY_REVENUE_RISK_TEST_POULTRY_EGGS_CONSUMPTION_VARIANCE_PREF =
      "Reasonability - Revenue Risk - Poultry - Eggs - Consumption - Variance Limit";
  
  public static final String REASONABILITY_REVENUE_RISK_TEST_EGGS_CONSUMPTION_AVERAGE_EGGS_PER_LAYER_PREF =
      "Reasonability - Revenue Risk - Poultry - Eggs - Consumption - Average Eggs Per Layer";
  
  public static final String REASONABILITY_REVENUE_RISK_TEST_POULTRY_EGGS_HATCHING_VARIANCE_PREF =
      "Reasonability - Revenue Risk - Poultry - Eggs - Hatching - Variance Limit";
  
  public static final String REASONABILITY_REVENUE_RISK_TEST_EGGS_HATCHING_AVERAGE_EGGS_PER_LAYER_PREF =
      "Reasonability - Revenue Risk - Poultry - Eggs - Hatching - Average Eggs Per Layer";
  
  /**
   * Used to save the parameters to the database along with the reasonability test results.
   */
  public static final List<String> REASONABILITY_TEST_PARAMETERS =
      Arrays.asList(
          REASONABILITY_BENEFIT_RISK_VARIANCE_PREF,
          REASONABILITY_BENEFIT_RISK_DEDUCTIBLE_PREF,
          REASONABILITY_BENEFIT_RISK_PAYOUT_LEVEL_PREF,
          REASONABILITY_MARGIN_TEST_REFERENCE_VARIANCE_PREF,
          REASONABILITY_MARGIN_TEST_INDUSTRY_VARIANCE_PREF,
          REASONABILITY_STRUCTURAL_CHANGE_TEST_RATIO_ADDITIVE_DIFF_VARIANCE_PREF,
          REASONABILITY_STRUCTURAL_CHANGE_TEST_ADDITIVE_DIVISION_VARIANCE_PREF,
          REASONABILITY_STRUCTURAL_CHANGE_TEST_FARM_SIZE_RATIO_PREF,
          REASONABILITY_EXPENSE_TEST_IAC_INDUSTRY_VARIANCE_PREF,
          REASONABILITY_PRODUCTION_FORAGE_TEST_VARIANCE_PREF,
          REASONABILITY_PRODUCTION_FRUIT_VEG_TEST_VARIANCE_PREF,
          REASONABILITY_PRODUCTION_GRAINS_TEST_VARIANCE_PREF,
          REASONABILITY_EXPENSE_TEST_GC_VARIANCE_PREF,
          REASONABILITY_REVENUE_RISK_TEST_FORAGE_GRAIN_VARIANCE_PREF,
          REASONABILITY_REVENUE_RISK_TEST_FED_PER_PRODUCTIVE_UNIT_CATTLE,
          REASONABILITY_REVENUE_RISK_TEST_FED_PER_PRODUCTIVE_UNIT_FEEDER_CATTLE,
          REASONABILITY_REVENUE_RISK_TEST_FED_PER_PRODUCTIVE_UNIT_FINISHED_CATTLE,
          REASONABILITY_REVENUE_RISK_TEST_FED_PER_PRODUCTIVE_UNIT_CATTLE_BRED_HEIFERS,
          REASONABILITY_REVENUE_RISK_TEST_CATTLE_VARIANCE_PREF,
          REASONABILITY_REVENUE_RISK_TEST_CATTLE_DEATH_RATE_PREF,
          REASONABILITY_REVENUE_RISK_TEST_NURSERY_VARIANCE_PREF,
          REASONABILITY_REVENUE_RISK_TEST_HOGS_FARROW_TO_FINISH_VARIANCE_PREF,
          REASONABILITY_REVENUE_RISK_TEST_HOGS_FARROW_TO_FINISH_DEATH_RATE_PREF,
          REASONABILITY_REVENUE_RISK_TEST_HOGS_FARROW_TO_FINISH_BIRTHS_PER_CYCLE_PREF,
          REASONABILITY_REVENUE_RISK_TEST_HOGS_FARROW_TO_FINISH_BIRTH_CYCLES_PER_YEAR_PREF,
          REASONABILITY_REVENUE_RISK_TEST_HOGS_FEEDER_VARIANCE_PREF,
          REASONABILITY_REVENUE_RISK_TEST_POULTRY_BROILERS_CHICKENS_VARIANCE_PREF,
          REASONABILITY_REVENUE_RISK_TEST_POULTRY_BROILERS_CHICKENS_AVERAGE_WEIGHT_PREF,
          REASONABILITY_REVENUE_RISK_TEST_POULTRY_BROILERS_TURKEYS_VARIANCE_PREF,
          REASONABILITY_REVENUE_RISK_TEST_POULTRY_BROILERS_TURKEYS_AVERAGE_WEIGHT_PREF,
          REASONABILITY_REVENUE_RISK_TEST_POULTRY_EGGS_CONSUMPTION_VARIANCE_PREF,
          REASONABILITY_REVENUE_RISK_TEST_EGGS_CONSUMPTION_AVERAGE_EGGS_PER_LAYER_PREF,
          REASONABILITY_REVENUE_RISK_TEST_POULTRY_EGGS_HATCHING_VARIANCE_PREF,
          REASONABILITY_REVENUE_RISK_TEST_EGGS_HATCHING_AVERAGE_EGGS_PER_LAYER_PREF
      );
  
  public static final String PRE_VERIFICATION_PAYMENT_AMOUNT_REQUIRING_A_SPECIALIST =
      "Pre-Verification - Payment Amount Requiring a Verification Specialist";
  
  public static final String PRE_VERIFICATION_INCOME_AMOUNT_REQUIRING_A_SPECIALIST =
      "Pre-Verification - Income Amount Requiring a Verification Specialist";
  
  public static final String PRE_VERIFICATION_FARM_TYPE_DETAILED_CODES_FOR_ABBOTSFORD =
      "Pre-Verification - Farm Type Detailed Codes - Abbotsford";
  
  public static final String PRE_VERIFICATION_FARM_TYPE_DETAILED_CODES_FOR_KELOWNA =
      "Pre-Verification - Farm Type Detailed Codes - Kelowna";
  
  public static final String PRE_VERIFICATION_FARM_TYPE_DETAILED_CODES_REQUIRING_SPECIALIST =
      "Pre-Verification - Farm Type Detailed Codes - Specialist";
  
  
  /************** CRM **********************/
  public static final String CRM_APP_ID = "CRM - App ID";

  public static final String CRM_QUEUES_NOL_CORPORATE =
      "CRM - Queues - NOL Corp";
  
  public static final String CRM_QUEUES_NOL_INDIVIDUAL =
      "CRM - Queues - NOL Individual";
  
  public static final String CRM_QUEUES_NOL_VALIDATION =
      "CRM - Queues - NOL Validation";
  
  public static final String CRM_QUEUES_CASH_MARGINS_VALIDATION = "CRM - Queues - Cash Margins Validation";

  public static final String CRM_QUEUES_INTERIM_VALIDATION = "CRM - Queues - Interim Validation";

  public static final String CRM_QUEUES_ADJUSTMENT_VALIDATION = "CRM - Queues - Adjustment Validation";

  public static final String CRM_QUEUES_SUPPLEMENTAL_VALIDATION = "CRM - Queues - Supplemental Validation";

  public static final String CRM_QUEUES_STATEMENT_A_VALIDATION = "CRM - Queues - Statement A Validation";

  public static final String CRM_QUEUES_COVERAGE_CORPORATE = "CRM - Queues - Coverage Corp";

  public static final String CRM_QUEUES_COVERAGE_INDIVIDUAL = "CRM - Queues - Coverage Individual";

  public static final String CRM_QUEUES_COVERAGE_VALIDATION = "CRM - Queues - Coverage Validation";
  
  public static final String CRM_QUEUES_NPP_CORPORATE = "CRM - Queues - NPP Corp";

  public static final String CRM_QUEUES_NPP_INDIVIDUAL = "CRM - Queues - NPP Individual";

  public static final String CRM_QUEUES_NPP_VALIDATION = "CRM - Queues - NPP Validation";

  public static final String CRM_QUEUES_FIFO_ZERO_CORPORATE = "CRM - Queues - FIFO Zero Corp";
  
  public static final String CRM_QUEUES_FIFO_ZERO_INDIVIDUAL = "CRM - Queues - FIFO Zero Individual";
  
  public static final String CRM_QUEUES_FIFO_PAYMENT_INDIVIDUAL = "CRM - Queues - FIFO Payment Individual";
  
  public static final String CRM_QUEUES_FIFO_PAYMENT_CORPORATE = "CRM - Queues - FIFO Payment Corp";
  
  /************** CHEFS ********************/
  public static final String CHEFS_UI_URL = "CHEFS - UI URL";

  public static final String CHEFS_VERIFIER_USER_EMAIL =
      "CHEFS - Verifier User Email";

  public static final String CHEFS_NOL_FORM_IDIR_GUID =
      "CHEFS - NOL - IDIR Form GUID";
  
  public static final String CHEFS_NOL_FORM_IDIR_API_KEY =
      "CHEFS - NOL - IDIR Form API Key";
  
  public static final String CHEFS_NOL_FORM_BASIC_BCEID_GUID =
      "CHEFS - NOL - Basic BCeID Form GUID";
  
  public static final String CHEFS_NOL_FORM_BASIC_BCEID_API_KEY =
      "CHEFS - NOL - Basic BCeID Form API Key";
  
  public static final String CHEFS_INTERIM_IDIR_FORM_GUID =
    "CHEFS - Interim - IDIR Form GUID";
    
  public static final String CHEFS_INTERIM_IDIR_FORM_API_KEY =
    "CHEFS - Interim - IDIR Form API Key";
  
  public static final String CHEFS_INTERIM_BASIC_BCEID_FORM_GUID =
      "CHEFS - Interim - Basic BCeID Form GUID";
      
  public static final String CHEFS_INTERIM_BASIC_BCEID_FORM_API_KEY =
      "CHEFS - Interim - Basic BCeID Form API Key";
    
  public static final String CHEFS_NPP_FORM_IDIR_GUID = "CHEFS - NPP - IDIR Form GUID";

  public static final String CHEFS_NPP_FORM_IDIR_API_KEY = "CHEFS - NPP - IDIR Form API Key";
  
  public static final String CHEFS_NPP_FORM_BASIC_BCEID_GUID = "CHEFS - NPP - Basic BCeID Form GUID";

  public static final String CHEFS_NPP_FORM_BASIC_BCEID_API_KEY = "CHEFS - NPP - Basic BCeID Form API Key";
  
  public static final String CHEFS_ADJUSTMENT_IDIR_FORM_GUID = "CHEFS - Adjustment - IDIR Form GUID";

  public static final String CHEFS_ADJUSTMENT_IDIR_FORM_API_KEY = "CHEFS - Adjustment - IDIR Form API Key";

  public static final String CHEFS_ADJUSTMENT_BASIC_BCEID_FORM_GUID = "CHEFS - Adjustment - Basic BCeID Form GUID";

  public static final String CHEFS_ADJUSTMENT_BASIC_BCEID_FORM_API_KEY = "CHEFS - Adjustment - Basic BCeID Form API Key";
  
  public static final String CHEFS_SUPPLEMENTAL_IDIR_FORM_GUID = "CHEFS - Supplemental - IDIR Form GUID";

  public static final String CHEFS_SUPPLEMENTAL_IDIR_FORM_API_KEY = "CHEFS - Supplemental - IDIR Form API Key";

  public static final String CHEFS_SUPPLEMENTAL_BASIC_BCEID_FORM_GUID = "CHEFS - Supplemental - Basic BCeID Form GUID";

  public static final String CHEFS_SUPPLEMENTAL_BASIC_BCEID_FORM_API_KEY = "CHEFS - Supplemental - Basic BCeID Form API Key";
  
  public static final String CHEFS_COVERAGE_IDIR_FORM_GUID = "CHEFS - Coverage - IDIR Form GUID";

  public static final String CHEFS_COVERAGE_IDIR_FORM_API_KEY = "CHEFS - Coverage - IDIR Form API Key";

  public static final String CHEFS_COVERAGE_BASIC_BCEID_FORM_GUID = "CHEFS - Coverage - Basic BCeID Form GUID";

  public static final String CHEFS_COVERAGE_BASIC_BCEID_FORM_API_KEY = "CHEFS - Coverage - Basic BCeID Form API Key";
  
  public static final String CHEFS_CASH_MARGINS_IDIR_FORM_GUID = "CHEFS - Cash Margins - IDIR Form GUID";

  public static final String CHEFS_CASH_MARGINS_IDIR_FORM_API_KEY = "CHEFS - Cash Margins - IDIR Form API Key";
  
  public static final String CHEFS_CASH_MARGINS_BASIC_BCEID_FORM_GUID = "CHEFS - Cash Margins - Basic BCeID Form GUID";

  public static final String CHEFS_CASH_MARGINS_BASIC_BCEID_FORM_API_KEY = "CHEFS - Cash Margins - Basic BCeID Form API Key";
  
  public static final String CHEFS_STATEMENT_A_IDIR_FORM_GUID = "CHEFS - Statement A - IDIR Form GUID";

  public static final String CHEFS_STATEMENT_A_IDIR_FORM_API_KEY = "CHEFS - Statement A - IDIR Form API Key";

  public static final String CHEFS_STATEMENT_A_BASIC_BCEID_FORM_GUID = "CHEFS - Statement A - Basic BCeID Form GUID";

  public static final String CHEFS_STATEMENT_A_BASIC_BCEID_FORM_API_KEY = "CHEFS - Statement A - Basic BCeID Form API Key";
  
  public static final String CHEFS_BASIC_BCEID_FORMS_ENABLED = "CHEFS - Basic BCeID Forms Enabled";

  /************** CDOGS ********************/
  public static final String CDOGS_BASE_URL = "CDOGS - Base URL";

  public static final String CDOGS_API_VERSION = "CDOGS - Api Version";

  public static final String CDOGS_AUTHORITY_URL = "CDOGS - Authority URL";

  public static final String CDOGS_CLIENT_ID = "CDOGS - Client Id";

  public static final String CDOGS_CLIENT_SECRET = "CDOGS - Client Secret";

  public static final String CDOGS_NOL_TEMPLATE_GUID = "CDOGS - NOL - Template GUID";

  public static final String CDOGS_INTERIM_TEMPLATE_GUID = "CDOGS - Interim - Template GUID";

  public static final String CDOGS_INTERIM_TEMPLATE_GUID_V2 = "CDOGS - Interim - Template GUID V2";

  public static final String CDOGS_NPP_TEMPLATE_GUID = "CDOGS - NPP - Template GUID";

  public static final String CDOGS_ADJUSTMENT_TEMPLATE_GUID = "CDOGS - Adjustment - Template GUID";

  public static final String CDOGS_SUPPLEMENTAL_TEMPLATE_GUID = "CDOGS - Supplemental - Template GUID";

  public static final String CDOGS_COVERAGE_TEMPLATE_GUID = "CDOGS - Coverage - Template GUID";

  public static final String CDOGS_COVERAGE_REPORT_TEMPLATE_GUID = "CDOGS - Coverage Notice Report - Template GUID";

  public static final String CDOGS_CASH_MARGINS_TEMPLATE_GUID = "CDOGS - Cash Margins - Template GUID";

  public static final String CDOGS_STATEMENT_A_TEMPLATE_GUID = "CDOGS - Statement A - Template GUID";

  
  /******************************************************************************
   **** Parameters Configured by Year (FARM_YEAR_CONFIGURATION_PARAMS)       ****
   ******************************************************************************/
  public static final String BC_ENHANCED_BENEFIT_ENABLED =
      "BC Enhanced Benefit - Enabled";
  
  public static final String ENHANCED_BENEFIT_NEGATIVE_MARGIN_COMPENSATION_RATE =
      "BC Enhanced Benefit - Negative Margin Compensation Rate Percentage";
  
  public static final String ENHANCED_BENEFIT_POSITIVE_MARGIN_COMPENSATION_RATE =
      "BC Enhanced Benefit - Positive Margin Compensation Rate Percentage";
  
  public static final String STANDARD_BENEFIT_POSITIVE_MARGIN_COMPENSATION_RATE =
      "AgriStability Benefit - Positive Margin Compensation Rate Percentage";
  
  public static final String STANDARD_BENEFIT_NEGATIVE_MARGIN_COMPENSATION_RATE =
      "AgriStability Benefit - Negative Margin Compensation Rate Percentage";
  
  public static final String LATE_PARTICIPANT_PENALTY_PERCENTAGE =
      "Late Participant - Penalty Percentage";
  
  public static final String PAYMENT_LIMITATION_ENABLED =
      "Payment Limitation - Enabled";
  
  public static final String PAYMENT_LIMITATION_PERCENTAGE_OF_TOTAL_MARGIN_DECLINE =
      "Payment Limitation - Percentage of Total Margin Decline";
  
  public static final String AGRISTABILITY_MAX_BENEFIT =
      "AgriStability Benefit - Maximum Benefit";
  
  public static final String BC_ENHANCED_MAX_BENEFIT =
      "BC Enhanced Benefit - Maximum Benefit";
  
  public static final String NEGATIVE_MARGIN_PAYMENT_PERCENTAGE =
      "Negative Margin Payment Percentage";

  public static final String FIFO_VERIFIER_USER_EMAIL =
      "FIFO - Verifier User Email";
  
  public static final List<String> BENEFIT_CALCULATION_PARAMETERS =
      Arrays.asList(
          BC_ENHANCED_BENEFIT_ENABLED,
          ENHANCED_BENEFIT_NEGATIVE_MARGIN_COMPENSATION_RATE,
          ENHANCED_BENEFIT_POSITIVE_MARGIN_COMPENSATION_RATE,
          STANDARD_BENEFIT_POSITIVE_MARGIN_COMPENSATION_RATE,
          STANDARD_BENEFIT_NEGATIVE_MARGIN_COMPENSATION_RATE,
          LATE_PARTICIPANT_PENALTY_PERCENTAGE,
          PAYMENT_LIMITATION_ENABLED,
          PAYMENT_LIMITATION_PERCENTAGE_OF_TOTAL_MARGIN_DECLINE,
          AGRISTABILITY_MAX_BENEFIT,
          BC_ENHANCED_MAX_BENEFIT,
          NEGATIVE_MARGIN_PAYMENT_PERCENTAGE
      );

  
  // TIPS (Towards Increased Profits)
  public static final String TIPS_PREF_PREFIX =
      "TIPS";
  
  public static boolean isTipsConfigurationParameter(String name) {
    return name != null && name.startsWith(TIPS_PREF_PREFIX);
  }
}
