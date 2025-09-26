/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.exception;


/**
 * ServiceException.
 *
 * @author   $Author: jjobson $
 * @version  $Revision: 385 $
 */
public class ServiceException extends Exception {

  /** serialVersionUID. */
  private static final long serialVersionUID = -5744045217668618471L;

  /** Creates a new ServiceException object. */
  public ServiceException() {
    super();
  }

  /**
   * Creates a new ServiceException object.
   *
   * @param  reason  Input parameter to initialize class.
   */
  public ServiceException(final String reason) {
    super(reason);
  }

  /**
   * Creates a new ServiceException object.
   *
   * @param  cause  Input parameter to initialize class.
   */
  public ServiceException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new ServiceException object.
   *
   * @param  reason  Input parameter to initialize class.
   * @param  cause   Input parameter to initialize class.
   */
  public ServiceException(final String reason, final Throwable cause) {
    super(reason, cause);
  }

}
