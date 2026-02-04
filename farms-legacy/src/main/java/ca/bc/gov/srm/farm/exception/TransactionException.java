/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.exception;


/**
 * TransactionException.
 *
 * @author   $Author: jjobson $
 * @version  $Revision: 385 $
 */
public class TransactionException extends ProviderException {

  /** serialVersionUID. */
  private static final long serialVersionUID = 3211692699986254807L;

  /** Creates a new TransactionException object. */
  public TransactionException() {
    super();
  }

  /**
   * Creates a new TransactionException object.
   *
   * @param  reason  Input parameter to initialize class.
   */
  public TransactionException(final String reason) {
    super(reason);
  }

  /**
   * Creates a new TransactionException object.
   *
   * @param  cause  Input parameter to initialize class.
   */
  public TransactionException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new TransactionException object.
   *
   * @param  reason  Input parameter to initialize class.
   * @param  cause   Input parameter to initialize class.
   */
  public TransactionException(final String reason, final Throwable cause) {
    super(reason, cause);
  }
}
