/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.exception;

/**
 * ChildRecordsExistException.
 *
 * @author   $Author: jjobson $
 * @version  $Revision: 385 $
 */
public class ChildRecordsExistException extends DataAccessException {

  /** serialVersionUID. */
  private static final long serialVersionUID = -8010581783891173278L;

  /** Creates a new ChildRecordsExistException object. */
  public ChildRecordsExistException() {
    super();
  }

  /**
   * Creates a new ChildRecordsExistException object.
   *
   * @param  reason  Input parameter to initialize class.
   */
  public ChildRecordsExistException(final String reason) {
    super(reason);
  }

  /**
   * Creates a new ChildRecordsExistException object.
   *
   * @param  cause  Input parameter to initialize class.
   */
  public ChildRecordsExistException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new ChildRecordsExistException object.
   *
   * @param  reason  Input parameter to initialize class.
   * @param  cause   Input parameter to initialize class.
   */
  public ChildRecordsExistException(final String reason,
    final Throwable cause) {
    super(reason, cause);
  }

}
