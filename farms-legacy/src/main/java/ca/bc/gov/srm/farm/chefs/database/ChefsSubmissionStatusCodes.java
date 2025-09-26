/**
 * Copyright (c) 2023,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.chefs.database;

/**
 * @author awilkinson
 */
public final class ChefsSubmissionStatusCodes {

  /** Submitted */
  public static final String SUBMITTED = "SUBMITTED";

  /** Processed */
  public static final String PROCESSED = "PROCESSED";

  /** Validation Failed */
  public static final String INVALID = "INVALID";

  /** Cancelled */
  public static final String CANCELLED = "CANCELLED";

  /** Unexpected Error */
  public static final String SYSTEM_ERROR = "SYSTEM_ERR";

  /** Data Parsing Error */
  public static final String PARSE_ERROR = "PARSE_ERR";

  /** Not for this environment */
  public static final String OTHER_ENV = "OTHER_ENV";

  /** Duplicate
   * This means there is another submission that was previously processed
   * for this PIN and Program Year. If we want to ignore the fact that
   * this submission is a duplicate and process it anyway, we can set the
   * status to SUBMITTED.
   */
  public static final String DUPLICATE = "DUPLICATE";

  private ChefsSubmissionStatusCodes() {
    // private constructor
  }
}
