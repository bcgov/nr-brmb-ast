/**
 * @(#)SecurityConfiguration.java
 * Copyright (c) 2007, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.user.security.enterprise;

import javax.servlet.http.HttpServletRequest;

import ca.bc.gov.webade.user.UserCredentials;

/**
 * @author jross
 */
public interface SecurityConfiguration {
    /**
     * Initializes the security configuration with the given configuration
     * properties set.
     * @throws SecurityException
     *             Thrown if the configuration fails on initialization.
     */
    public void initialize() throws SecurityException;
    
    /**
     * Identifies the authenticated user from the request.
     * @param request
     *            The incoming web request.
     * @return The user credentials loaded by the specific security
     *         configuration.
     * @throws SecurityException
     *             Thrown if the request details do not conform to the
     *             requirements of the specific security configuration.
     */
    public UserCredentials identifyUser(HttpServletRequest request) throws SecurityException;
}
