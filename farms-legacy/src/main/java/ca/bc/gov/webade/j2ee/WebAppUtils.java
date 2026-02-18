/**
 * @(#)WebAppUtils.java
 * Copyright (c) 2007, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.j2ee;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.webade.Application;
import ca.bc.gov.webade.user.WebADECurrentUserPermissions;
import ca.bc.gov.webade.user.WebADEUserInfo;

/**
 * @author jross
 */
final class WebAppUtils {
    /**
     * The attribute name for the <code>Application</code> object in the
     * <code>HttpSession</code>
     */
    static final String WEBADE_APPLICATION = WebAppInitializationUtils.WEBADE_APPLICATION;
	
	private static final Logger log = LoggerFactory.getLogger(WebAppUtils.class);

    /**
     * A convenience method for JSPs needing to access the Application object.
     * In a JSP the call is: <code>
     *    Application app = WebAppUtils.getApplication(application);
     * </code>
     * @param context
     *            Servlet context to create the application singleton in.
     * @return The application singleton.
     */
    static Application getWebADEApplication(ServletContext context) {
        return (Application) context.getAttribute(WEBADE_APPLICATION);
    }

    /**
     * Returns the <code>WebADEUserPermissions</code> object for the user
     * making the request. The <code>WebADEUserPermissions</code> object is
     * obtained from the session if it exists, otherwise it is obtained from the
     * request. This method assumes that the User object already exists (as will
     * be the case in a WebADE application).
     * @param req
     *            The request object.
     * @return The current user permissions object.
     */
    static WebADECurrentUserPermissions getCurrentUserPermissions(HttpServletRequest req) {
        WebADECurrentUserPermissions currentUser = (WebADECurrentUserPermissions) req.getSession()
                .getAttribute(WebAppRequestProcessingUtils.CURRENT_WEBADE_USER_PERMISSIONS);
        if (currentUser != null) {
            log.trace("Returning request user permissions for user "
                    + currentUser.getUserCredentials() + ".");
        } else {
            log.warn("Returning request null user permissions.  "
                    + "The WebADE Filter may not be pre-processing "
                    + "the requests.");
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
    static WebADEUserInfo getCurrentUserInfo(HttpServletRequest req) {
        WebADEUserInfo currentUser = (WebADEUserInfo)req.getSession()
                .getAttribute(WebAppRequestProcessingUtils.CURRENT_WEBADE_USER_INFO);
        if (currentUser != null) {
            log.trace("Returning request user info for user "
                    + currentUser.getUserCredentials() + ".");
        } else {
            log.trace("Returning request null user info.");
        }
        return currentUser;
    }

    private WebAppUtils() {
    }
}
