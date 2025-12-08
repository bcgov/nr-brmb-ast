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

public final class ScenarioTypeCodes {

  /**  */
  private ScenarioTypeCodes() {
    // private constructor
  }

  /** CRA */
  public static final String CRA = "CRA";

  /** Reference */
  public static final String REF = "REF";

  /** Generated */
  public static final String GEN = "GEN";
  
  /** User */
  public static final String USER = "USER";
  
  /** Local Data Entry */
  public static final String LOCAL = "LOCAL";

  /** CHEFS Form */
  public static final String CHEF = "CHEF";

  /** First In First Out benefit calculation
   *  scenarios auto-generated on CRA import */
  public static final String FIFO = "FIFO";
  
  /**
   * Base data is the initially reported data witout any adjustments by verifiers.
   * Adjustments are only made on USER and REF scenarios.
   */
  public static boolean isBaseData(String type) {
    return StringUtils.isOneOf(type, CRA, LOCAL, CHEF, GEN);
  }

}
