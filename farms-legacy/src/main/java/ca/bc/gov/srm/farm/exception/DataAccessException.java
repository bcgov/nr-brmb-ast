/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.exception;

/**
 * DataAccessException.
 *
 * @author   $Author: jjobson $
 * @version  $Revision: 385 $
 */
public class DataAccessException extends ProviderException {

  /** serialVersionUID. */
  private static final long serialVersionUID = 353691043582305927L;

  /** errorCode. */
  private Long errorCode = null;

  /** Creates a new DataAccessException object. */
  public DataAccessException() {
    super();
  }

  /**
   * Creates a new DataAccessException object.
   *
   * @param  reason  Input parameter to initialize class.
   */
  public DataAccessException(final String reason) {
    super(reason);
  }

  /**
   * Creates a new DataAccessException object.
   *
   * @param  cause  Input parameter to initialize class.
   */
  public DataAccessException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new DataAccessException object.
   *
   * @param  reason  Input parameter to initialize class.
   * @param  cause   Input parameter to initialize class.
   */
  public DataAccessException(final String reason, final Throwable cause) {
    super(reason, cause);
  }

  /**
   * getErrorCode.
   *
   * @return  The return value.
   */
  public Long getErrorCode() {
    return errorCode;
  }

  /**
   * Sets the value for error code.
   *
   * @param  errCode  Input parameter.
   */
  public void setErrorCode(final Long errCode) {
    this.errorCode = errCode;
  }

}
