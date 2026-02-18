package ca.bc.gov.webade.http;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.webade.Application;
import ca.bc.gov.webade.j2ee.WebAppInitializationUtils;
import ca.bc.gov.webade.j2ee.WebAppRequestProcessingUtils;
import ca.bc.gov.webade.user.WebADEUserInfo;

public class HttpRequestUtils {
    /**
     * The attribute name for the <code>Application</code> object in the
     * <code>HttpSession</code>
     */
    public static final String WEBADE_APPLICATION = WebAppInitializationUtils.WEBADE_APPLICATION;
    /**
     * The attribute name for the <code>UserInfoService</code> object in the
     * <code>HttpSession</code>
     */
    public static final String WEBADE_USER_INFO_SERVICE = WebAppInitializationUtils.WEBADE_USER_INFO_SERVICE;
    /**
     * The attribute name for the <code>WebADEUserPermissions</code> object in
     * the <code>HttpSession</code>
     */
    public static final String CURRENT_WEBADE_USER_PERMISSIONS = WebAppRequestProcessingUtils.CURRENT_WEBADE_USER_PERMISSIONS;
    /**
     * The attribute name for the <code>WebADEUserInfo</code> object in the
     * <code>HttpSession</code>
     */
    public static final String CURRENT_WEBADE_USER_INFO = WebAppRequestProcessingUtils.CURRENT_WEBADE_USER_INFO;
    /**
     * The attribute name for the flag used by the tag
     * <code>FlowControlTag</code> to prevent users from calling JSPs
     * directly.
     */
    public static final String WEBADE_FLOW_CONTROL_FLAG = "webade.via.controller";
    /**
     * The request attribute name for the flag indicating the WebADE has
     * pre-processed this request.
     */
    public static final String WEBADE_HAS_PREPROCESSED_REQUEST = WebAppRequestProcessingUtils.WEBADE_HAS_PREPROCESSED_REQUEST;
    
    /**
     * Using this constant will hide bugs that may exist in legacy code because 
     * it implies that a <code>User</code> object can be retrieved from the 
     * <code>HttpSession</code> when that is not the case.  The legacy code 
     * that I have seen that references this constant works only because the 
     * reference is not used.
     * 
     * @deprecated Update the application to use WebadeUserInfo objects instead. 
     */
    @Deprecated
    public static final String WEBADE_USER = "webade.user.singleton";

    private static final Logger log = LoggerFactory.getLogger(HttpRequestUtils.class);

    /**
     * A convenience method for web application code needing to access the
     * Application object. The call is: <code>
     *    Application app = HttpRequestUtils.getApplication(context);
     * </code>
     * @param context
     *            Servlet context to retrieve the application singleton from.
     * @return The application singleton.
     */
    public static Application getApplication(ServletContext context) {
        return (Application) context.getAttribute(WEBADE_APPLICATION);
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

    private static WebADEUserInfo retrieveCurrentUserInfo(HttpSession session) {
        WebADEUserInfo currentUser = null;
        if (session != null) {
            currentUser = (WebADEUserInfo) session
                    .getAttribute(CURRENT_WEBADE_USER_INFO);
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
            currentUser = (WebADEUserInfo) req
                    .getAttribute(CURRENT_WEBADE_USER_INFO);
        }
        return currentUser;
    }
}
