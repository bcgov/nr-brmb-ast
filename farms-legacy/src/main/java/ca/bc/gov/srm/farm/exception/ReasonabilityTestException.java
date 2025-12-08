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
package ca.bc.gov.srm.farm.exception;

/**
 * @author awilkinson
 */
public class ReasonabilityTestException extends DataAccessException {

  private static final long serialVersionUID = 1L;

  /** Creates a new LineItemNotFoundException object. */
  public ReasonabilityTestException() {
    super();
  }

  /**
   * Creates a new LineItemNotFoundException object.
   *
   * @param  reason  Input parameter to initialize class.
   */
  public ReasonabilityTestException(final String reason) {
    super(reason);
  }

  /**
   * Creates a new LineItemNotFoundException object.
   *
   * @param  cause  Input parameter to initialize class.
   */
  public ReasonabilityTestException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new LineItemNotFoundException object.
   *
   * @param  reason  Input parameter to initialize class.
   * @param  cause   Input parameter to initialize class.
   */
  public ReasonabilityTestException(final String reason, final Throwable cause) {
    super(reason, cause);
  }

}
