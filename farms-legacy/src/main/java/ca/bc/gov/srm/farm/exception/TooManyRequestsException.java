package ca.bc.gov.srm.farm.exception;

public class TooManyRequestsException extends Exception {

  private static final long serialVersionUID = -3870850288816145743L;

  /** Seconds the server asked us to wait before retrying, or null if it didn't say. */
  private final Integer retryAfterSeconds;

  /**
   * Creates a new TooManyRequestsException object.
   *
   * @param reason
   *          Input parameter to initialize class.
   * @param retryAfterSeconds
   *          Seconds to wait before retrying, or null if unknown.
   */
  public TooManyRequestsException(final String reason, final Integer retryAfterSeconds) {
    super(reason);
    this.retryAfterSeconds = retryAfterSeconds;
  }

  /**
   * Creates a new TooManyRequestsException object.
   *
   * @param reason
   *          Input parameter to initialize class.
   */
  public TooManyRequestsException(final String reason) {
    this(reason, (Integer) null);
  }

  /**
   * Creates a new TooManyRequestsException object.
   *
   * @param cause
   *          Input parameter to initialize class.
   */
  public TooManyRequestsException(final Throwable cause) {
    super(cause);
    this.retryAfterSeconds = null;
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
    this.retryAfterSeconds = null;
  }

  public Integer getRetryAfterSeconds() {
    return retryAfterSeconds;
  }

  /**
   * @param e exception to search
   * @return the TooManyRequestsException in the cause chain of e, or null if there isn't one
   */
  public static TooManyRequestsException findIn(Throwable e) {
    for(Throwable t = e; t != null; t = t.getCause()) {
      if(t instanceof TooManyRequestsException) {
        return (TooManyRequestsException) t;
      }
    }
    return null;
  }

}
