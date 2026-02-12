/**
 * @(#)WebADECurrentUserPermissions.java
 * Copyright (c) 2005, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.user;

/**
 * User permissions for the current user has a couple of extra attributes
 * related to the session environment.
 * 
 * @author jross
 */
public interface WebADECurrentUserPermissions extends WebADEUserPermissions {

    /**
     * Returns a boolean indicating whether the current user has passed
     * authentication to access the WebADE application.
     * 
     * @return <code>true</code> if the user is authenticated.
     */
    public boolean isUserAuthenticated();

    /**
     * Returns a boolean indicating whether the current user has been located in
     * the WebADE system. If the user has been authenticated but cannot be
     * located within the WebADE itself, this value will be false.
     * 
     * @return <code>true</code> if the user is a valid WebADE user.
     */
    public boolean isWebADEUser();

}
