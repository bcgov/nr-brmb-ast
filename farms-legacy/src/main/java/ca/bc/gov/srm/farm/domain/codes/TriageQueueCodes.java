/**
 * Copyright (c) 2021,
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
 * @created Sep 21, 2021
 */
public final class TriageQueueCodes {

  private TriageQueueCodes() {
    // private constructor
  }
  
  // ------------------------------------------------
  // Abbotsford
  // ------------------------------------------------
  
  /** Data Assessment: Zero Payment - Pass */
  public static final String ZERO_PAYMENT_PASS = "DA_ZPP";
  
  /** Data Assessment: Abbotsford - Zero - Fail */
  public static final String ABBOTSFORD_ZERO_FAIL = "DA_AZF";
  
  /** Data Assessment: Abbotsford - Under 100K */
  public static final String ABBOTSFORD_UNDER_100K = "DA_AU100K";
  
  /** Data Assessment: Kelowna - Zero - Fail */
  public static final String KELOWNA_ZERO_FAIL = "DA_KZF";
  
  /** Data Assessment: Kelowna - Under 100K */
  public static final String KELOWNA_UNDER_100K = "DA_KU100K";
  
  /** Data Assessment: Verification Specialist */
  public static final String VERIFICATION_SPECIALIST = "DA_VS";

}
