package ca.bc.gov.webade;


/**
 * This exception indicates that an error was found while attempting to access
 * some component of the WebADE framework. It likely indicates a problem with
 * the WebADE configuration on the server.
 * <p>
 * To aid in debugging this exception may contain lower-level exceptions chained
 * to it.
 */
public class WebADEException extends Exception {
    private static final long serialVersionUID = 3977301018746828855L;

    /**
     * Constructs a <code>WebADEException</code> object; msg defaults to null
     */
    public WebADEException() {
        super();
    }

    /**
     * Constructs a <code>WebADEException</code> object with a message
     * 
     * @param msg
     *            The text message.
     */
    public WebADEException(String msg) {
        super(msg);
    }

    /**
     * Constructs a <code>WebADEException</code> object with a chained
     * exception.
     * @param ex
     *            The chained exception.
     */
    public WebADEException(Throwable ex) {
        super(ex);
    }

    /**
     * Constructs a <code>WebADEException</code> object with a message and a
     * chained exception.
     * 
     * @param msg
     *            The text message.
     * @param ex
     *            The chained exception.
     */
    public WebADEException(String msg, Exception ex) {
        super(msg, ex);
    }

}