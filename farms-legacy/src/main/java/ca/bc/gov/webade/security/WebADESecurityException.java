/**
 * @(#)WebADESecurityException.java
 * Copyright (c) 2005, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.security;

/**
 * @author jross
 */
public final class WebADESecurityException extends SecurityException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Default Constructor
     */
    public WebADESecurityException() {
        super();
    }

    /**
     * @param message
     *            Security exception message.
     */
    public WebADESecurityException(String message) {
        super(message);
    }

}
