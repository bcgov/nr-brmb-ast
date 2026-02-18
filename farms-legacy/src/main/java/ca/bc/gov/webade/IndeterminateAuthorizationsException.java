/**
 * @(#)IndeterminateAuthorizationsException.java
 * Copyright (c) 2006, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade;

/**
 * Thrown when a lookup for users cannot be fully evaluated, due to WebADE being
 * unable to evaluate authorizations for a WebADE Role being granted to a group
 * or rule that cannot be fully traversed.
 * 
 * @author jross
 */
public class IndeterminateAuthorizationsException extends WebADEException {
    private static final long serialVersionUID = 451546970918753990L;

    /**
     * @see ca.bc.gov.webade.WebADEException#WebADEException()
     */
    public IndeterminateAuthorizationsException() {
        super();
    }

    /**
     * @see ca.bc.gov.webade.WebADEException#WebADEException(java.lang.String)
     */
    public IndeterminateAuthorizationsException(String msg) {
        super(msg);
    }

    /**
     * @see ca.bc.gov.webade.WebADEException#WebADEException(java.lang.String, java.lang.Exception)
     */
    public IndeterminateAuthorizationsException(String msg, Exception ex) {
        super(msg, ex);
    }

}
