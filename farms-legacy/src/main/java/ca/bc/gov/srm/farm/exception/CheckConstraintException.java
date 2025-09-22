/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.exception;

/**
 * CheckConstraintException.
 *
 * @author   $Author: jjobson $
 * @version  $Revision: 385 $
 */
public class CheckConstraintException extends DataAccessException {

  /** serialVersionUID. */
  private static final long serialVersionUID = 1169765648983530667L;

  /** Creates a new CheckConstraintException object. */
  public CheckConstraintException() {
    super();
  }

  /**
   * Creates a new CheckConstraintException object.
   *
   * @param  reason  Input parameter to initialize class.
   */
  public CheckConstraintException(final String reason) {
    super(reason);
  }

  /**
   * Creates a new CheckConstraintException object.
   *
   * @param  cause  Input parameter to initialize class.
   */
  public CheckConstraintException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new CheckConstraintException object.
   *
   * @param  reason  Input parameter to initialize class.
   * @param  cause   Input parameter to initialize class.
   */
  public CheckConstraintException(final String reason, final Throwable cause) {
    super(reason, cause);
  }

}
