/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.exception;

/**
 * ConfigurationNotFoundException.
 *
 * @author   $Author: jjobson $
 * @version  $Revision: 385 $
 */
public class ConfigurationNotFoundException extends ProviderException {

  /** serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** Creates a new ConfigurationNotFoundException object. */
  public ConfigurationNotFoundException() {
    super();
  }

  /**
   * Creates a new ConfigurationNotFoundException object.
   *
   * @param  reason  Input parameter to initialize class.
   */
  public ConfigurationNotFoundException(final String reason) {
    super(reason);
  }

  /**
   * Creates a new ConfigurationNotFoundException object.
   *
   * @param  cause  Input parameter to initialize class.
   */
  public ConfigurationNotFoundException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new ConfigurationNotFoundException object.
   *
   * @param  reason  Input parameter to initialize class.
   * @param  cause   Input parameter to initialize class.
   */
  public ConfigurationNotFoundException(final String reason,
    final Throwable cause) {
    super(reason, cause);
  }

}
