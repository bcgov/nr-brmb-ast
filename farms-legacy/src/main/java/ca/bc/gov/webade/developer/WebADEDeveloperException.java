package ca.bc.gov.webade.developer;

/**
 * Signifies an error in the configuration and/or normal operation of the
 * developer module.
 * 
 * @author Vivid Solutions Inc
 */
public class WebADEDeveloperException extends Exception {

	private static final long serialVersionUID = -6012417007479726220L;

	/**
     * @see java.lang.Exception#Exception()
     */
	public WebADEDeveloperException() {
		super();
	}

	/**
     * @see java.lang.Exception#Exception(java.lang.String)
     */
	public WebADEDeveloperException(String message) {
		super(message);
	}

	/**
     * @see java.lang.Exception#Exception(java.lang.String, java.lang.Throwable)
     */
	public WebADEDeveloperException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
     * @see java.lang.Exception#Exception(java.lang.Throwable)
     */
	public WebADEDeveloperException(Throwable cause) {
		super(cause);
	}

}
