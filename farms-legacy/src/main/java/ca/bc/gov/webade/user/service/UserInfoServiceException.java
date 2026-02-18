/**
 * @(#)UserInfoServiceException.java
 * Copyright (c) 2007, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.user.service;

/**
 * Exception class raised by the methods of the UserInfoService API.
 * @author jross
 */
public final class UserInfoServiceException extends Exception {
    private static final long serialVersionUID = 648143352262686582L;

    /**
     * Default Constructor
     */
    public UserInfoServiceException() {
    }

    /**
     * @param message
     */
    public UserInfoServiceException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public UserInfoServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public UserInfoServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
