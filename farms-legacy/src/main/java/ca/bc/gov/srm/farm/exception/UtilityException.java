/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.exception;

/**
 * UtilityException.
 *
 * @author   $Author: jjobson $
 * @version  $Revision: 385 $
 */
public class UtilityException extends ServiceException {

  /** serialVersionUID. */
  private static final long serialVersionUID = -8097618755999907897L;

  /** Creates a new UtilityException object. */
  public UtilityException() {
    super();
  }

  /**
   * Creates a new UtilityException object.
   *
   * @param  reason  Input parameter to initialize class.
   */
  public UtilityException(final String reason) {
    super(reason);
  }

  /**
   * Creates a new UtilityException object.
   *
   * @param  cause  Input parameter to initialize class.
   */
  public UtilityException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new UtilityException object.
   *
   * @param  reason  Input parameter to initialize class.
   * @param  cause   Input parameter to initialize class.
   */
  public UtilityException(final String reason, final Throwable cause) {
    super(reason, cause);
  }

}
