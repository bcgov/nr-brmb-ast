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

/**
 * @author awilkinson
 * @created Mar 17, 2011
 */
public final class ScenarioStateCodes {

  private ScenarioStateCodes() {
    // private constructor
  }

  /** Received */
  public static final String RECEIVED = "REC";

  /** In Progress */
  public static final String IN_PROGRESS = "IP";

  /** Verified */
  public static final String VERIFIED = "COMP";

  /** Amended */
  public static final String AMENDED = "AMEND";

  /** Closed */
  public static final String CLOSED = "CLO";

  /** Pre-Verified (for Pre-Verification (PRE_VERIFI) scenario category only) */
  public static final String PRE_VERIFIED = "PREVERIFID";
  
  /** EN Complete (for ENW scenario category only). EN is Enrolment Notification. */
  public static final String ENROLMENT_NOTICE_COMPLETE = "EN_COMP";
  
  /** Pending Complete. This state is expired and no longer used. */
  public static final String PENDING_COMPLETE = "PEND";

  /** Completed */
  public static final String COMPLETED = "COMPLETED";

  /** Failed */
  public static final String FAILED = "FAILED";

  public static final String FAILED_DESCRIPTION = "Failed";

}
