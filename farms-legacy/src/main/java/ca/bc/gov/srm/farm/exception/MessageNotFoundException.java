/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.exception;

/**
 * MessageNotFoundException.
 *
 * @author   $Author: jjobson $
 * @version  $Revision: 385 $
 */
public class MessageNotFoundException extends ProviderException {

  /** serialVersionUID. */
  private static final long serialVersionUID = 5444199256963204644L;

  /** Creates a new MessageNotFoundException object. */
  public MessageNotFoundException() {
    super();
  }

  /**
   * Creates a new MessageNotFoundException object.
   *
   * @param  reason  Input parameter to initialize class.
   */
  public MessageNotFoundException(final String reason) {
    super(reason);
  }

  /**
   * Creates a new MessageNotFoundException object.
   *
   * @param  cause  Input parameter to initialize class.
   */
  public MessageNotFoundException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new MessageNotFoundException object.
   *
   * @param  reason  Input parameter to initialize class.
   * @param  cause   Input parameter to initialize class.
   */
  public MessageNotFoundException(final String reason, final Throwable cause) {
    super(reason, cause);
  }

}
