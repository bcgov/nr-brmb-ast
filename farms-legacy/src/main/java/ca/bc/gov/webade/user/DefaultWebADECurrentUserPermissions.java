/**
 * @(#)DefaultWebADECurrentUserPermissions.java
 * Copyright (c) 2005, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.user;


/**
 * @author jross
 */
public class DefaultWebADECurrentUserPermissions extends DefaultWebADEUserPermissions implements
        WebADECurrentUserPermissions {
    private static final long serialVersionUID = 3705789878418110531L;
    private boolean isUserAuthenticated;
    private boolean isWebADEUser;

    /**
     * @param permissions
     * @param isUserAuthenticated
     * @param isWebADEUser
     */
    public DefaultWebADECurrentUserPermissions(WebADEUserPermissions permissions,
            boolean isUserAuthenticated, boolean isWebADEUser) {
        super(permissions);
        this.isUserAuthenticated = isUserAuthenticated;
        this.isWebADEUser = isWebADEUser;
    }

    /**
     * @see ca.bc.gov.webade.user.WebADECurrentUserPermissions#isUserAuthenticated()
     */
    @Override
	public boolean isUserAuthenticated() {
        return this.isUserAuthenticated;
    }

    /**
     * @see ca.bc.gov.webade.user.WebADECurrentUserPermissions#isWebADEUser()
     */
    @Override
	public boolean isWebADEUser() {
        return this.isWebADEUser;
    }

}
