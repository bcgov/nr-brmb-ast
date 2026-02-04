/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.exception;

/**
 * DuplicateDataException.
 *
 * @author   $Author: jjobson $
 * @version  $Revision: 385 $
 */
public class DuplicateDataException extends DataAccessException {

  /** serialVersionUID. */
  private static final long serialVersionUID = -3360055920285375513L;

  /** Creates a new DuplicateDataException object. */
  public DuplicateDataException() {
    super();
  }

  /**
   * Creates a new DuplicateDataException object.
   *
   * @param  reason  Input parameter to initialize class.
   */
  public DuplicateDataException(final String reason) {
    super(reason);
  }

  /**
   * Creates a new DuplicateDataException object.
   *
   * @param  cause  Input parameter to initialize class.
   */
  public DuplicateDataException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new DuplicateDataException object.
   *
   * @param  reason  Input parameter to initialize class.
   * @param  cause   Input parameter to initialize class.
   */
  public DuplicateDataException(final String reason, final Throwable cause) {
    super(reason, cause);
  }

}
