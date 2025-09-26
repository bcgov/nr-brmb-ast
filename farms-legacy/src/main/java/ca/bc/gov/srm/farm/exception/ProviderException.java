/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.exception;

/**
 * ProviderException.
 *
 * @author   $Author: jjobson $
 * @version  $Revision: 385 $
 */
public class ProviderException extends UtilityException {

  /** serialVersionUID. */
  private static final long serialVersionUID = 6185738108877365754L;

  /** Creates a new ProviderException object. */
  public ProviderException() {
    super();
  }

  /**
   * Creates a new ProviderException object.
   *
   * @param  reason  Input parameter to initialize class.
   */
  public ProviderException(final String reason) {
    super(reason);
  }

  /**
   * Creates a new ProviderException object.
   *
   * @param  cause  Input parameter to initialize class.
   */
  public ProviderException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new ProviderException object.
   *
   * @param  reason  Input parameter to initialize class.
   * @param  cause   Input parameter to initialize class.
   */
  public ProviderException(final String reason, final Throwable cause) {
    super(reason, cause);
  }

}
