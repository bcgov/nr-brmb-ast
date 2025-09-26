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
 */
public final class ImportJobTypes {

  /**  */
  private ImportJobTypes() {
  }

  /** A standard import */
  public static final String STANDARD = "STANDARD";
  
  /** Enrolment */
  public static final String ENROL = "ENROL";
  
  /** Transfer to CRM */
  public static final String TRANSFER = "TRANSFER";
  
  /** Tip Report */
  public static final String TIP_REPORT = "TIP_REPORT";

}
