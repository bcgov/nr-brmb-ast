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
package ca.bc.gov.srm.farm.exception;

/**
 * @author awilkinson
 * @created Mar 30, 2011
 */
public class LineItemNotFoundException extends DataAccessException {

  /** serialVersionUID. */
  private static final long serialVersionUID = -3754917823954846023L;

  /** Creates a new LineItemNotFoundException object. */
  public LineItemNotFoundException() {
    super();
  }

  /**
   * Creates a new LineItemNotFoundException object.
   *
   * @param  reason  Input parameter to initialize class.
   */
  public LineItemNotFoundException(final String reason) {
    super(reason);
  }

  /**
   * Creates a new LineItemNotFoundException object.
   *
   * @param  cause  Input parameter to initialize class.
   */
  public LineItemNotFoundException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new LineItemNotFoundException object.
   *
   * @param  reason  Input parameter to initialize class.
   * @param  cause   Input parameter to initialize class.
   */
  public LineItemNotFoundException(final String reason, final Throwable cause) {
    super(reason, cause);
  }

}
