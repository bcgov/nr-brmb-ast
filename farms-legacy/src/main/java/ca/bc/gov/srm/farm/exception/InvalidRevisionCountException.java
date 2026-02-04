/**
 *
 * Copyright (c) 2010,
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
 * 
 * @author awilkinson
 *
 */
public class InvalidRevisionCountException extends DataAccessException {

  /** serialVersionUID. */
  private static final long serialVersionUID = -3754917823954846023L;

  /** Creates a new DataNotCurrentException object. */
  public InvalidRevisionCountException() {
    super();
  }

  /**
   * Creates a new InvalidRevisionCountException object.
   *
   * @param  reason  Input parameter to initialize class.
   */
  public InvalidRevisionCountException(final String reason) {
    super(reason);
  }

  /**
   * Creates a new InvalidRevisionCountException object.
   *
   * @param  cause  Input parameter to initialize class.
   */
  public InvalidRevisionCountException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new InvalidRevisionCountException object.
   *
   * @param  reason  Input parameter to initialize class.
   * @param  cause   Input parameter to initialize class.
   */
  public InvalidRevisionCountException(final String reason, final Throwable cause) {
    super(reason, cause);
  }

}
