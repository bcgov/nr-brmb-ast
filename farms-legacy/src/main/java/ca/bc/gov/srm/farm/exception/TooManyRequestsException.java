package ca.bc.gov.srm.farm.exception;

public class TooManyRequestsException extends Exception {

  private static final long serialVersionUID = -3870850288816145743L;
  
  /**
   * Creates a new TooManyRequestsException object.
   *
   * @param reason
   *          Input parameter to initialize class.
   */
  public TooManyRequestsException(final String reason) {
    super(reason);
  }

  /**
   * Creates a new TooManyRequestsException object.
   *
   * @param cause
   *          Input parameter to initialize class.
   */
  public TooManyRequestsException(final Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new TooManyRequestsException object.
   *
   * @param reason
   *          Input parameter to initialize class.
   * @param cause
   *          Input parameter to initialize class.
   */
  public TooManyRequestsException(final String reason, final Throwable cause) {
    super(reason, cause);
  }

}
