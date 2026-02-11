/**
 * @(#)UserProviderException.java
 * Copyright (c) 2007, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.user.provider;

/**
 * @author jross
 */
public final class WebADEUserProviderException extends Exception {
    private static final long serialVersionUID = 3963547831906059815L;

    /**
     * 
     */
    public WebADEUserProviderException() {
        super();
    }

    /**
     * @param message
     */
    public WebADEUserProviderException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public WebADEUserProviderException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public WebADEUserProviderException(String message, Throwable cause) {
        super(message, cause);
    }

}
