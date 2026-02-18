/**
 * @(#)WebADESecurityManager.java
 * Copyright (c) 2005, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.webade.user.UserCredentials;
import ca.bc.gov.webade.user.WebADECurrentUserPermissions;
import ca.bc.gov.webade.user.WebADEUserInfo;
import ca.bc.gov.webade.user.WebADEUserPermissions;

/**
 * @author jross
 */
public final class WebADESecurityManager {
    private static final Map<String, WebADESecurityManager> managers = new HashMap<String, WebADESecurityManager>();
	
	private static final Logger log = LoggerFactory.getLogger(WebADESecurityManager.class);
    private static final Class<?>[] PERMITTED_SET_USER_CLASSES;
    
    static {
        ArrayList<Class<?>> classList;
        String[] permittedClassNames;
        classList = new ArrayList<Class<?>>();
        permittedClassNames = new String[] {
            "ca.bc.gov.webade.http.HttpRequestUtils",
            "ca.bc.gov.webade.remote.WebADERemoteServiceImpl",
            "ca.bc.gov.webade.remote.WebADERemoteRepository",
            "ca.bc.gov.webade.j2ee.WebAppRequestProcessingUtils",
            "ca.bc.gov.webade.ApplicationUserInfoService",
            "ca.bc.gov.webade.remote.WebADERemoteRepositoryTest",
            "ca.bceid.webservices.BCeIDWebServicesProviderTest"
        };
        for (int i = 0; i < permittedClassNames.length; i++) {
            try {
                Class<?> classObj = Class.forName(permittedClassNames[i]);
                classList.add(classObj);
            } catch (ClassNotFoundException e) {
                if (log.isTraceEnabled()) {
                    log.trace("Security manager could not load class '" + permittedClassNames[i] + "'");
                }
            }
        }
        PERMITTED_SET_USER_CLASSES = classList.toArray(new Class[classList.size()]);
    }

    private InheritableThreadLocal<UserCredentials> currentUserCredentials = new InheritableThreadLocal<UserCredentials>();
    private InheritableThreadLocal<WebADEUserPermissions> currentUserPermissions = new InheritableThreadLocal<WebADEUserPermissions>();
    private InheritableThreadLocal<WebADEUserInfo> currentUserInfo = new InheritableThreadLocal<WebADEUserInfo>();

    private InheritableThreadLocal<UserCredentials> providerRequestorUserCredentials = new InheritableThreadLocal<UserCredentials>();

    /**
     * Returns the WebADESecurityManager singleton, or throws a Security
     * exception if the requesting class is not permitted to access the security
     * manager.
     * 
     * @return The WebADESecurityManager singleton.
     * @throws WebADESecurityException
     *             Thrown if the requesting class is not permitted to access
     *             this method.
     */
    public static final WebADESecurityManager getWebADESecurityManager(String appCode)
            throws WebADESecurityException {
    	WebADESecurityManager result = managers.get(appCode);
    	if(result==null) {
    		result = new WebADESecurityManager();
    		managers.put(appCode, result);
    	}
        return result;
    }

    /**
     * Returns the current user for this thread. This is an internal method, and
     * should not be called by non-WebADE code.
     * 
     * @return The current user credentials object.
     * @throws SecurityException
     *             Thrown if called by a class that is not permitted to call
     *             this method.
     */
    public UserCredentials getCurrentUserCredentials() throws SecurityException {
        return this.currentUserCredentials.get();
    }

    /**
     * Sets the current user for this thread. This is an internal method, and
     * should not be called by non-WebADE code.
     * 
     * @param userCredentials
     *            The thread's associated user's credentials object.
     * @throws SecurityException
     *             Thrown if called by a class that is not permitted to call
     *             this method.
     */
    public void setCurrentUserCredentials(UserCredentials userCredentials) throws SecurityException {
        log.trace("> setCurrentUserCredentials() = " + userCredentials);
        StackTraceElement[] stack = new Throwable().getStackTrace();
        boolean result = SecurityUtils.checkStackCallAccess(stack, PERMITTED_SET_USER_CLASSES);
        if (!result) {
            throw new SecurityException("Calling class '" + stack[1].getClassName()
                    + "' is not an instance of a class that is "
                    + "authorized to call this method.");
        }
        this.currentUserCredentials.set(userCredentials);
        if (this.currentUserInfo.get() != null) {
            WebADEUserInfo info = this.currentUserInfo.get();
            UserCredentials infoCreds = info.getUserCredentials();
            boolean credsMatch = false;
            if (infoCreds != null) {
                if (UserCredentials.UNAUTHENTICATED_USER_CREDENTIALS.equals(infoCreds)
                        && UserCredentials.UNAUTHENTICATED_USER_CREDENTIALS.equals(userCredentials)) {
                    credsMatch = true;
                } else {
                    credsMatch = infoCreds.equals(userCredentials);
                }
            }
            if (!credsMatch) {
                this.currentUserInfo.set(null);
            }
        }
        if (this.currentUserPermissions.get() != null) {
            WebADEUserPermissions perms = this.currentUserPermissions.get();
            UserCredentials permCreds = perms.getUserCredentials();
            boolean credsMatch = false;
            if (permCreds != null) {
                if (UserCredentials.UNAUTHENTICATED_USER_CREDENTIALS.equals(permCreds)
                        && UserCredentials.UNAUTHENTICATED_USER_CREDENTIALS.equals(userCredentials)) {
                    credsMatch = true;
                } else {
                    credsMatch = permCreds.equals(userCredentials);
                }
            }
            if (!credsMatch) {
                this.currentUserPermissions.set(null);
            }
        }
        log.trace("< setCurrentUserCredentials()");
    }

    /**
     * Returns the current user for this thread. This is an internal method, and
     * should not be called by non-WebADE code.
     * 
     * @return The current user credentials object.
     * @throws SecurityException
     *             Thrown if called by a class that is not permitted to call
     *             this method.
     */
    public WebADECurrentUserPermissions getCurrentUserPermissions() throws SecurityException {
        return (WebADECurrentUserPermissions)this.currentUserPermissions.get();
    }

    /**
     * Sets the current user for this thread. This is an internal method, and
     * should not be called by non-WebADE code.
     * 
     * @param userPermissions
     *            The thread's associated user's permissions object.
     * @throws SecurityException
     *             Thrown if called by a class that is not permitted to call
     *             this method.
     */
    public void setCurrentUserPermissions(WebADECurrentUserPermissions userPermissions) throws SecurityException {
        log.trace("> setCurrentUserPermissions() = " + userPermissions);
        StackTraceElement[] stack = new Throwable().getStackTrace();
        boolean result = SecurityUtils.checkStackCallAccess(stack, PERMITTED_SET_USER_CLASSES);
        if (!result) {
            throw new SecurityException("Calling class '" + stack[1].getClassName()
                    + "' is not an instance of a class that is "
                    + "authorized to call this method.");
        }
        this.currentUserPermissions.set(userPermissions);
        log.trace("< setCurrentUserPermissions()");
    }

    /**
     * Returns the current user for this thread. This is an internal method, and
     * should not be called by non-WebADE code.
     * 
     * @return The current user credentials object.
     * @throws SecurityException
     *             Thrown if called by a class that is not permitted to call
     *             this method.
     */
    public WebADEUserInfo getCurrentUserInfo() throws SecurityException {
        return this.currentUserInfo.get();
    }

    /**
     * Sets the current user for this thread. This is an internal method, and
     * should not be called by non-WebADE code.
     * 
     * @param userInfo
     *            The thread's associated user's user info object.
     * @throws SecurityException
     *             Thrown if called by a class that is not permitted to call
     *             this method.
     */
    public void setCurrentUserInfo(WebADEUserInfo userInfo) throws SecurityException {
        log.trace("> setCurrentUserInfo() = " + userInfo);
        StackTraceElement[] stack = new Throwable().getStackTrace();
        boolean result = SecurityUtils.checkStackCallAccess(stack, PERMITTED_SET_USER_CLASSES);
        if (!result) {
            throw new SecurityException("Calling class '" + stack[1].getClassName()
                    + "' is not an instance of a class that is "
                    + "authorized to call this method.");
        }
        this.currentUserInfo.set(userInfo);
        log.trace("< setCurrentUserInfo()");
    }

    /**
     * Returns the user that will be used as the requestor by the user provider
     * for this thread. This is an internal method, and
     * should not be called by non-WebADE code.
     * 
     * @return The current user credentials object.
     * @throws SecurityException
     *             Thrown if called by a class that is not permitted to call
     *             this method.
     */
    public UserCredentials getProviderRequestorUserCredentials() throws SecurityException {
        return this.providerRequestorUserCredentials.get();
    }

    /**
     * Sets the user that will be used as the requestor by the user provider
     * for this thread. This is an internal method, and
     * should not be called by non-WebADE code.
     * 
     * @param userCredentials
     *            The thread's associated user's credentials object.
     * @throws SecurityException
     *             Thrown if called by a class that is not permitted to call
     *             this method.
     */
    public void setProviderRequestorUserCredentials(UserCredentials userCredentials) throws SecurityException {
        log.trace("> setProviderRequestorUserCredentials() = " + userCredentials);
        StackTraceElement[] stack = new Throwable().getStackTrace();
        boolean result = SecurityUtils.checkStackCallAccess(stack, PERMITTED_SET_USER_CLASSES);
        if (!result) {
            throw new SecurityException("Calling class '" + stack[1].getClassName()
                    + "' is not an instance of a class that is "
                    + "authorized to call this method.");
        }
        this.providerRequestorUserCredentials.set(userCredentials);

        log.trace("< setProviderRequestorUserCredentials()");
    }
}

