/**
 * Copyright (c) 2019,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.jasper;

/**
 * @author awilkinson
 */
public class SessionInitializationException extends ReportingException {
  private static final long serialVersionUID = 1L;

  public SessionInitializationException() {
    super();
  }

  public SessionInitializationException(String msg) {
    super(msg);
  }
}