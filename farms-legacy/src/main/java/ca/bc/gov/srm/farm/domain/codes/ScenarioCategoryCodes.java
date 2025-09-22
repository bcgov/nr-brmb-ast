/**
 * Copyright (c) 2013,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain.codes;

/**
 * @author awilkinson
 * @created November 5, 2013
 */
public final class ScenarioCategoryCodes {

  private ScenarioCategoryCodes() {
    // private constructor
  }

  /** CRA Received */
  public static final String CRA_RECEIVED = "CRAR";

  /** New CRA Data */
  public static final String NEW_CRA_DATA = "NCRA";

  /** Comparison Scenario */
  public static final String COMPARISON_SCENARIO = "CS";

  /** Enrolment Notice Workflow */
  public static final String ENROLMENT_NOTICE_WORKFLOW = "ENW";

  /** Administrative Adjustment */
  public static final String ADMINISTRATIVE_ADJUSTMENT = "AADJ";

  /** Producer Adjustment */
  public static final String PRODUCER_ADJUSTMENT = "PADJ";

  /** Interim */
  public static final String INTERIM = "INT";

  /** Final */
  public static final String FINAL = "FIN";
  
  /** Unknown */
  public static final String UNKNOWN = "UNK";
  
  /** Local Data Entry */
  public static final String LOCAL_DATA_ENTRY = "LOCL_ENTRY";
  
  /** Pre-Verification */
  public static final String PRE_VERIFICATION = "PRE_VERIFI";
  
  /** Notice of Loss */
  public static final String NOL = "NOL";
  
  /** Coverage Notice */
  public static final String COVERAGE_NOTICE = "CN";
  
  /** CHEFS Received */
  public static final String CHEF_RECEIVED = "CHEFR";
  
  /** CHEFS Adjustment */
  public static final String CHEF_ADJ = "CHEF_ADJ";
  
  /** CHEFS Coverage Notice */
  public static final String CHEF_CN = "CHEF_CN";
  
  /** CHEFS Cash Margins */
  public static final String CHEF_CM = "CHEF_CM";
  
  /** CHEFS Interim */
  public static final String CHEF_INTRM = "CHEF_INTRM";
  
  /** CHEFS Local Supplemental */
  public static final String CHEF_SUPP = "CHEF_SUPP";
  
  /** CHEFS New Participant Package */
  public static final String CHEF_NPP = "CHEF_NPP";
  
  /** CHEFS Notice of Loss */
  public static final String CHEF_NOL = "CHEF_NOL";

  /** CHEFS Statement A */
  public static final String CHEF_STA = "CHEF_STA";

  /** First In First Out */
  public static final String FIFO = "FIFO";

}
