/**
 * @(#)SessionUserUtils.java
 * Copyright (c) 2007, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.user.security.enterprise;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.webade.user.UserCredentials;
import ca.bc.gov.webade.user.WebADEUserInfo;

/**
 * @author jross
 */
final class SessionUserInfoUtils {
	
	private static final Logger log = LoggerFactory.getLogger(SessionUserInfoUtils.class);
    /**
     * The attribute name for the <code>UserCredentials</code> object in
     * the <code>HttpSession</code>
     */
    public static final String CURRENT_WEBADE_USER_CREDENTIALS = "webade.current.user.credentials";
    /**
     * The attribute name for the <code>WebADEUserInfo</code> object in the
     * <code>HttpSession</code>
     */
    public static final String CURRENT_WEBADE_USER_INFO = "webade.current.user.info";

    /**
     * Returns the <code>WebADEUserInfo</code> object for the user making the
     * request. The <code>WebADEUserInfo</code> object is obtained from the
     * session if it exists, otherwise it is obtained from the request. This
     * method assumes that the User object already exists (as will be the case
     * in a WebADE application).
     * @param session
     *            The user's session.
     * @return The current user info object.
     */
    public static WebADEUserInfo getCurrentUserInfo(HttpSession session) {
        WebADEUserInfo currentUser = retrieveCurrentUserInfo(session);
        if (currentUser != null) {
            log.trace("Returning session user info for user "
                    + currentUser.getUserCredentials() + ".");
        } else {
            log.trace("Returning session null user info.");
        }
        return currentUser;
    }

    /**
     * Returns the <code>WebADEUserInfo</code> object for the user making the
     * request. The <code>WebADEUserInfo</code> object is obtained from the
     * session if it exists, otherwise it is obtained from the request. This
     * method assumes that the User object already exists (as will be the case
     * in a WebADE application).
     * @param req
     *            The request object.
     * @return The current user info object.
     */
    public static WebADEUserInfo getCurrentUserInfo(HttpServletRequest req) {
        WebADEUserInfo currentUser = retrieveCurrentUserInfo(req);
        if (currentUser != null) {
            log.trace("Returning request user info for user "
                    + currentUser.getUserCredentials() + ".");
        } else {
            log.trace("Returning request null user info.");
        }
        return currentUser;
    }

    /**
     * Returns the <code>UserCredentials</code> object for the user
     * making the request. The <code>UserCredentials</code> object is
     * obtained from the session if it exists, otherwise it is obtained from the
     * request. This method assumes that the User object already exists (as will
     * be the case in a WebADE application).
     * @param session
     *            The user's session.
     * @return The current user permissions object.
     */
    public static UserCredentials getCurrentUserCredentials(
            HttpSession session) {
        UserCredentials currentUser = retrieveCurrentUserCredentials(session);
        if (currentUser != null) {
            log.trace("Returning session user credentials for user "
                    + currentUser + ".");
        } else {
            log.warn("Returning session null user credentials.");
        }
        return currentUser;
    }

    /**
     * Returns the <code>UserCredentials</code> object for the user
     * making the request. The <code>UserCredentials</code> object is
     * obtained from the session if it exists, otherwise it is obtained from the
     * request. This method assumes that the User object already exists (as will
     * be the case in a WebADE application).
     * @param req
     *            The request object.
     * @return The current user permissions object.
     */
    public static UserCredentials getCurrentUserCredentials(
            HttpServletRequest req) {
        UserCredentials currentUser = retrieveCurrentUserCredentials(req);
        if (currentUser != null) {
            log.trace("Returning request user credentials for user "
                    + currentUser + ".");
        } else {
            log.trace("Returning request null user credentials.");
        }
        return currentUser;
    }

    private static WebADEUserInfo retrieveCurrentUserInfo(HttpSession session) {
        WebADEUserInfo currentUser = null;
        if (session != null) {
            currentUser = (WebADEUserInfo) session.getAttribute(CURRENT_WEBADE_USER_INFO);
        }
        return currentUser;
    }

    private static WebADEUserInfo retrieveCurrentUserInfo(HttpServletRequest req) {
        WebADEUserInfo currentUser = null;
        HttpSession session = req.getSession(false);
        // Get the user object from session if it exists, otherwise get it from
        // the request.
        currentUser = retrieveCurrentUserInfo(session);
        if (currentUser == null) {
            currentUser = (WebADEUserInfo) req.getAttribute(CURRENT_WEBADE_USER_INFO);
        }
        return currentUser;
    }

    private static UserCredentials retrieveCurrentUserCredentials(
            HttpSession session) {
        UserCredentials currentUser = null;
        if (session != null) {
            currentUser = (UserCredentials) session
                    .getAttribute(CURRENT_WEBADE_USER_CREDENTIALS);
        }
        return currentUser;
    }

    private static UserCredentials retrieveCurrentUserCredentials(
            HttpServletRequest req) {
        UserCredentials currentUser = null;
        HttpSession session = req.getSession(false);
        // Get the user object from session if it exists, otherwise get it from
        // the request.
        currentUser = retrieveCurrentUserCredentials(session);
        if (currentUser == null) {
            currentUser = (UserCredentials) req
                    .getAttribute(CURRENT_WEBADE_USER_CREDENTIALS);
        }
        return currentUser;
    }

}
