/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.exception;

/**
 * DataNotCurrentException.
 *
 * @author   $Author: jjobson $
 * @version  $Revision: 385 $
 */
public class DataNotCurrentException extends DataAccessException {

  /** serialVersionUID. */
  private static final long serialVersionUID = -3754907823994846023L;

  /** Creates a new DataNotCurrentException object. */
  public DataNotCurrentException() {
    super();
  }

  /**
   * Creates a new DataNotCurrentException object.
   *
   * @param  reason  Input parameter to initialize class.
   */
  public DataNotCurrentException(final String reason) {
    super(reason);
  }

  /**
   * Creates a new DataNotCurrentException object.
   *
   * @param  cause  Input parameter to initialize class.
   */
  public DataNotCurrentException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new DataNotCurrentException object.
   *
   * @param  reason  Input parameter to initialize class.
   * @param  cause   Input parameter to initialize class.
   */
  public DataNotCurrentException(final String reason, final Throwable cause) {
    super(reason, cause);
  }
}
