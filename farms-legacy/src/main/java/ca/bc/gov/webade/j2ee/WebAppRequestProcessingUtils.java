package ca.bc.gov.webade.j2ee;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.webade.Application;
import ca.bc.gov.webade.Organization;
import ca.bc.gov.webade.WebADEDatabaseApplication;
import ca.bc.gov.webade.WebADEException;
import ca.bc.gov.webade.WebADEUtils;
import ca.bc.gov.webade.http.DefaultHtmlPageWriter;
import ca.bc.gov.webade.preferences.WebADEPreference;
import ca.bc.gov.webade.preferences.WebADEPreferences;
import ca.bc.gov.webade.security.WebADESecurityManager;
import ca.bc.gov.webade.tags.UserOrganizationSelectorTag;
import ca.bc.gov.webade.user.DefaultWebADECurrentUserPermissions;
import ca.bc.gov.webade.user.UserCredentials;
import ca.bc.gov.webade.user.WebADECurrentUserPermissions;
import ca.bc.gov.webade.user.WebADEUserInfo;
import ca.bc.gov.webade.user.WebADEUserPermissions;
import ca.bc.gov.webade.user.security.enterprise.SecurityConfiguration;
import ca.bc.gov.webade.user.service.UserInfoServiceException;

public final class WebAppRequestProcessingUtils {
    /**
     * The attribute name for the <code>UserCredentials</code> object in
     * the <code>HttpSession</code>
     */
    public static final String CURRENT_WEBADE_USER_CREDENTIALS = "webade.current.user.credentials";
    /**
     * The attribute name for the <code>WebADEUserPermissions</code> object in
     * the <code>HttpSession</code>
     */
    public static final String CURRENT_WEBADE_USER_PERMISSIONS = "webade.current.user.permissions";
    /**
     * The attribute name for the <code>WebADEUserInfo</code> object in the
     * <code>HttpSession</code>
     */
    public static final String CURRENT_WEBADE_USER_INFO = "webade.current.user.info";
    /**
     * The request attribute name for the flag indicating the WebADE has
     * pre-processed this request.
     */
    public static final String WEBADE_HAS_PREPROCESSED_REQUEST = "webade.has.preprocessed.request";
    /**
     * <code>BAD_REQUEST</code> is the HTTP error code for a bad request.
     */
    public static final int BAD_REQUEST = 400;

    private static final Logger log = LoggerFactory.getLogger(WebAppRequestProcessingUtils.class);

    /**
     * Preprocess an HTTP request. Checks to see if the application has been
     * disabled, or is improperly configured. Also checks to see if the
     * application supports organization-selection or if the user has agreements
     * to be agreed to.
     * 
     * @param context
     *                The servlet context.
     * @param req
     *                The incoming request.
     * @param res
     *                The outgoing response.
     * @return A boolean flag indicating whether to continue processing the
     *         request.
     * @throws ServletException
     */
    public static boolean preprocessRequest(ServletContext context,
            HttpServletRequest req, HttpServletResponse res)
            throws ServletException {
        Object flag = req.getAttribute(WEBADE_HAS_PREPROCESSED_REQUEST);
        if (flag != null) {
            log.trace("Incoming request has already been preprocessed.  "
                    + "Continue on to action processing.");
            return true;
        } else {
            log.trace("Preprocessing incoming request.");
            req.setAttribute(WEBADE_HAS_PREPROCESSED_REQUEST, new Boolean(
                    true));
        }

        clearCurrentUser(context);

        initializeSession(req, res);

        boolean forwarded = false;
        forwarded = checkInitializationError(context, req, res);
        if (forwarded) {
            return !forwarded;
        }
        WebADEDatabaseApplication app = (WebADEDatabaseApplication) WebAppUtils.getWebADEApplication(context);
        WebADEPreferences wdePreferences;
        try {
            wdePreferences = app.getWebADEApplicationPreferences();
        } catch (WebADEException e) {
            String message = "Error raised while loading the WebADE preferences for this application.";
            log.error(message, e);
            throw new ServletException(message, e);
        }
        forwarded = checkApplicationEnabled(context, req, res, wdePreferences);
        if (!forwarded) {
            try {
                checkCurrentUserSession(app, req);
            } catch (SecurityException e) {
                log.error("SecurityException raised while loading the user's session.", e);
                try {
                    res.sendError(BAD_REQUEST, e.getMessage());
                } catch (IOException ioe) {
                    throw new ServletException("Exception occured while sending a " + BAD_REQUEST
                            + " error to the requester.", ioe);
                }
                forwarded = true;
            } catch (WebADEException e) {
                throw new ServletException("Exception occured while checking current user's session.", e);
            }
            WebADECurrentUserPermissions userPerms = WebAppUtils.getCurrentUserPermissions(req);
            if (!forwarded && userPerms != null) {
                forwarded = checkUserDefaultOrganization(context, req, res, wdePreferences);
            }
        }
        return !forwarded;
    }

    public static final String WEBADE_APPLICATION_ACRONYM = "webade.application.acronym";

    /**
     * Clears the current user thread-associated values.
     */
    private static void clearCurrentUser(ServletContext context) {

        String appCode = context.getInitParameter(WEBADE_APPLICATION_ACRONYM);
        if (appCode == null) {
            appCode = System.getProperty(WEBADE_APPLICATION_ACRONYM);
        }

        if (appCode != null) {
            WebADESecurityManager manager = WebADESecurityManager.getWebADESecurityManager(appCode);
            manager.setCurrentUserCredentials(null);
            manager.setCurrentUserPermissions(null);
            manager.setCurrentUserInfo(null);
            manager.setProviderRequestorUserCredentials(null);
        }
    }

    /**
     * Perform the logic needed to allow Actions to access User information, and
     * ensures a session is created for all requests.
     * 
     * @param req
     * @param res
     */
    private static void initializeSession(HttpServletRequest req,
            HttpServletResponse res) {
        // perform session initialization
        boolean inSession = (req.getSession(false) != null);
        if (!inSession) {
            log.debug("Creating new user session.");

            // make a new session
            req.getSession(true);
        }
    }

    private static boolean checkInitializationError(ServletContext context,
            HttpServletRequest req, HttpServletResponse res)
            throws ServletException {
        boolean forwarded = false;
        Application app = WebAppUtils.getWebADEApplication(context);
        boolean isDisabled = false;
        if (WebAppInitializationUtils.hasInitializationFailed()) {
            log.error("Cannot process user request because WebADE failed "
                    + "initialization, due to error:" + WebAppInitializationUtils.getInitializationErrorMessage());
            isDisabled = true;
        } else if (app == null) {
            log.error("WebADE is not initialized.  The Application singleton "
                    + "is not in the servlet context.  The WebADE Servlet "
                    + "Context Listener configuration may be missing from "
                    + "the web.xml file.");
            isDisabled = true;
        }
        if (isDisabled) {
            String defaultPage = DefaultHtmlPageWriter.writeInitializationErrorPage();
            forwarded = writeDefaultPageToStream(defaultPage, res);
        }
        return forwarded;
    }

    private static boolean checkApplicationEnabled(ServletContext context,
            HttpServletRequest req, HttpServletResponse res, WebADEPreferences wdePreferences)
            throws ServletException {
        boolean forwarded = false;
        Application app = WebAppUtils.getWebADEApplication(context);
        String flagString = getPreferenceValue(
                WebADEPreferences.WEBADE_APPLICATION_CHECK_FOR_DISABLED_FLAG, wdePreferences);
        boolean shouldCheck = (flagString == null) ? false
                : Boolean.valueOf(
                        flagString).booleanValue();
        boolean isDisabled;
        boolean errorOnDisabledCheck = false;
        try {
            isDisabled = app.isDisabled();
        } catch (WebADEException e) {
            log.error("An error occured while checking to see if " +
                    "the application is disabled.", e);
            isDisabled = true;
            errorOnDisabledCheck = true;
        }
        if (shouldCheck && isDisabled) {
            String page = getPreferenceValue(
                    WebADEPreferences.WEBADE_APPLICATION_DISABLED_PAGE, wdePreferences);
            if (page == null) {
                String appDisabledMessage = getPreferenceValue(
                        WebADEPreferences.WEBADE_APPLICATION_DISABLED_MESSAGE, wdePreferences);
                String defaultPage = DefaultHtmlPageWriter
                        .writeApplicationDisabledPage(appDisabledMessage, errorOnDisabledCheck);
                forwarded = writeDefaultPageToStream(defaultPage, res);
            } else {
                RequestDispatcher dispatcher = req.getRequestDispatcher(page);
                if (dispatcher != null) {
                    try {
                        dispatcher.forward(req, res);
                    } catch (IOException e) {
                        throw new ServletException(e.getMessage());
                    }
                    forwarded = true;
                }
            }
        }
        return forwarded;
    }

    private static boolean checkUserDefaultOrganization(ServletContext context,
            HttpServletRequest req, HttpServletResponse res, WebADEPreferences wdePreferences)
            throws ServletException {
        boolean forwarded = false;
        Application app = WebAppUtils.getWebADEApplication(context);
        String orgValue = req
                .getParameter(UserOrganizationSelectorTag.WEBADE_USER_CONTEXT_SWITCH_PARAMETER);
        String flagString = getPreferenceValue(
                WebADEPreferences.WEBADE_DEFAULT_ORGANIZATION_ENABLED_FLAG, wdePreferences);
        boolean enabledFlag = (flagString == null) ? false
                : Boolean.valueOf(
                        flagString).booleanValue();
        flagString = getPreferenceValue(
                WebADEPreferences.WEBADE_USE_DEFAULT_ORGANIZATION_ENABLED_FLAG,
                wdePreferences);
        boolean useDefaultOrganizationFlag = (flagString == null) ? false
                : Boolean.valueOf(flagString).booleanValue();
        String byOrgParam = getPreferenceValue(
                WebADEPreferences.WEBADE_DEFAULT_ORGANIZATION_SELECT_BY_ORGANIZATION_TYPE,
                wdePreferences);
        if (byOrgParam == null) {
            byOrgParam = UserOrganizationSelectorTag.WEBADE_USER_SELECT_BY_ALL_ORGANIZATIONS;
        }

        WebADEUserPermissions user = WebAppUtils.getCurrentUserPermissions(req);
        if (user.getSelectedOrganization() == null && enabledFlag) {
            boolean presentOrganizationPage = true;
            if (orgValue != null) {
                long orgId = Long.parseLong(orgValue);
                Organization[] orgs = user.getOrganizations();
                for (int i = 0; i < orgs.length; i++) {
                    if (orgs[i] != null && orgs[i].getOrganizationId() == orgId) {
                        try {
                            String saveAsDefault = req
                                    .getParameter(UserOrganizationSelectorTag.SAVE_AS_DEFAULT_PARAMETER);
                            if (saveAsDefault != null
                                    && Boolean.valueOf(saveAsDefault)
                                            .booleanValue()) {
                                app.setUserDefaultOrganization(user
                                        .getUserCredentials(), orgs[i]);
                            }
                            user.setSelectedOrganization(orgs[i]);
                            presentOrganizationPage = false;
                        } catch (WebADEException e) {
                            log.error(e.getMessage());
                            throw new ServletException(e.getMessage());
                        }
                        break;
                    }
                }
            } else if (useDefaultOrganizationFlag) {
                try {
                    Organization org = app.getUserDefaultOrganization(user
                            .getUserCredentials());
                    if (org != null) {
                        user.setSelectedOrganization(org);
                    }
                    if (user.getSelectedOrganization() != null) {
                        presentOrganizationPage = false;
                    }
                } catch (WebADEException e) {
                    log.error(e.getMessage());
                    throw new ServletException(e.getMessage());
                }
            } else if (byOrgParam == UserOrganizationSelectorTag.WEBADE_USER_SELECT_BY_ALL_ORGANIZATIONS) {
                Organization[] orgs = user.getOrganizations();
                if (orgs.length == 0) {
                    presentOrganizationPage = false;
                } else if (orgs.length == 1) {
                    user.setSelectedOrganization(orgs[0]);
                    presentOrganizationPage = false;
                }
            } else if (byOrgParam == UserOrganizationSelectorTag.WEBADE_USER_SELECT_BY_GOVERNMENT_ORGANIZATION) {
                Organization[] orgs = user.getGovernmentOrganizations();
                if (orgs.length == 0) {
                    presentOrganizationPage = false;
                } else if (orgs.length == 1) {
                    user.setSelectedOrganization(orgs[0]);
                    presentOrganizationPage = false;
                }
            } else if (byOrgParam == UserOrganizationSelectorTag.WEBADE_USER_SELECT_BY_NON_GOVERNMENT_ORGANIZATION) {
                Organization[] orgs = user.getNonGovernmentOrganizations();
                if (orgs.length == 0) {
                    presentOrganizationPage = false;
                } else if (orgs.length == 1) {
                    user.setSelectedOrganization(orgs[0]);
                    presentOrganizationPage = false;
                }
            }
            if (presentOrganizationPage) {
                try {
                    Organization defaultOrg = app
                            .getUserDefaultOrganization(user
                                    .getUserCredentials());
                    forwarded = presentOrganizationPage(req, res, app, user,
                            defaultOrg, byOrgParam, wdePreferences);
                } catch (UnsupportedEncodingException e) {
                    log.error(e.getMessage());
                    throw new ServletException(e.getMessage());
                } catch (WebADEException e) {
                    log.error(e.getMessage());
                    throw new ServletException(e.getMessage());
                }
            }
        }
        return forwarded;
    }

    private static boolean presentOrganizationPage(HttpServletRequest req,
            HttpServletResponse res, Application app,
            WebADEUserPermissions user, Organization defaultOrg, String byOrgParam, WebADEPreferences wdePreferences)
            throws ServletException, UnsupportedEncodingException {
        boolean forwarded = false;
        String path = URLEncoder.encode(req.getRequestURI(),
                WebADEUtils.DEFAULT_ENCODING);
        req.setAttribute(UserOrganizationSelectorTag.PATH, path);
        req.setAttribute(
                UserOrganizationSelectorTag.WEBADE_USER_SELECT_BY_ORGANIZATION_TYPE_PARAMETER,
                byOrgParam);

        Enumeration<String> enumeration = req.getParameterNames();
        String params = null;
        if (enumeration.hasMoreElements()) {
            params = "";
            do {
                String name = enumeration.nextElement();
                if (name.equals(UserOrganizationSelectorTag.WEBADE_USER_SELECT_BY_ORGANIZATION_TYPE_PARAMETER)
                        || name.equals(UserOrganizationSelectorTag.WEBADE_USER_CONTEXT_SWITCH_PARAMETER)
                        || name.equals(UserOrganizationSelectorTag.SAVE_AS_DEFAULT_PARAMETER)) {
                    continue;
                }
                String[] values = req.getParameterValues(name);
                for (int x = 0; x < values.length; x++) {
                    if (params.length() != 0) {
                        params += "&";
                    }
                    params += name + "=" + values[x];
                }
            } while (enumeration.hasMoreElements());
            params = URLEncoder.encode(params, WebADEUtils.DEFAULT_ENCODING);
            req.setAttribute(UserOrganizationSelectorTag.PARAMETERS, params);
        }
        String page = getPreferenceValue(
                WebADEPreferences.WEBADE_DEFAULT_ORGANIZATION_SWITCH_PAGE, wdePreferences);
        if (page == null) {
            String formPage = DefaultHtmlPageWriter.writeUserOrganizationPage(
                    user, byOrgParam, path, params, defaultOrg);
            forwarded = writeDefaultPageToStream(formPage, res);
        } else {
            req.setAttribute(
                    UserOrganizationSelectorTag.WEBADE_USER_DEFAULT_ORGANIZATION_PARAMETER,
                    defaultOrg);
            RequestDispatcher dispatcher = req.getRequestDispatcher(page);
            if (dispatcher != null) {
                try {
                    dispatcher.forward(req, res);
                } catch (IOException e) {
                    throw new ServletException(e.getMessage());
                }
                forwarded = true;
            }
        }
        return forwarded;
    }

    private static String getPreferenceValue(String prefName, WebADEPreferences prefs) {
        String value = null;
        WebADEPreference pref = prefs.getPreference(WebADEPreferences.APPLICATION_CONFIG_SUBTYPE, prefName);
        if (pref != null) {
            value = pref.getPreferenceValue();
        }
        return value;
    }

    private static boolean writeDefaultPageToStream(String defaultPage,
            HttpServletResponse res) throws ServletException {
        boolean forwarded = false;
        try {
            OutputStream out = res.getOutputStream();
            String contentType = "text/html; charset=UTF-8";
            res.setContentType(contentType);
            InputStream in = new ByteArrayInputStream(defaultPage.getBytes());

            byte[] bytes = new byte[100000];
            int bytesRead = 0;
            while ((bytesRead = in.read(bytes)) > 0) {
                out.write(bytes, 0, bytesRead);
            }
            in.close();
            out.flush();
            out.close();
            forwarded = true;

        } catch (IOException e) {
            throw new ServletException(e.getMessage());
        }
        return forwarded;
    }

    public static void postprocessRequest(ServletContext context,
            HttpServletRequest req, HttpServletResponse res)
            throws ServletException {
    }

    private static boolean areValid(UserCredentials creds) {
        return creds != null && !UserCredentials.areUnauthenticated(creds);
    }

    /**
     * Checks and sets the current request user's session credentials.
     * 
     * @param app
     *            The WebADE application singleton.
     * @param req
     *            The incoming request.
     * @throws WebADEException
     * @throws InvalidRequestException
     */
    private static void checkCurrentUserSession(WebADEDatabaseApplication app, HttpServletRequest req)
            throws WebADEException {

        SecurityConfiguration config = app.getSecurityConfiguration();
        UserCredentials userCreds = config.identifyUser(req);
        HttpSession session = req.getSession();

        WebADECurrentUserPermissions currentUserPerms = WebAppUtils.getCurrentUserPermissions(req);

        if (currentUserPerms == null || !userCreds.compareCredentials(currentUserPerms.getUserCredentials())) {
            WebADEUserPermissions userPerms = app.getWebADEUserPermissions(userCreds);
            if (userPerms == null) {
                return;
            }

            userCreds = userPerms.getUserCredentials();
            currentUserPerms = new DefaultWebADECurrentUserPermissions(userPerms,
                    !UserCredentials.areUnauthenticated(userCreds), areValid(userCreds));
            session.setAttribute(CURRENT_WEBADE_USER_PERMISSIONS, currentUserPerms);
            session.setAttribute(CURRENT_WEBADE_USER_CREDENTIALS, currentUserPerms.getUserCredentials());
        } else {
            userCreds = currentUserPerms.getUserCredentials();
        }
        WebADEUserInfo currentUserInfo = null;
        if (currentUserPerms.isUserAuthenticated()) {
            currentUserInfo = WebAppUtils.getCurrentUserInfo(req);
            if (currentUserInfo == null) {

                try {
                    currentUserInfo = app.getUserInfoService().getWebADEUserInfo(userCreds);
                } catch (UserInfoServiceException e) {
                    throw new WebADEException(e);
                }

                session.setAttribute(CURRENT_WEBADE_USER_INFO, currentUserInfo);
            }
        }

        WebADESecurityManager.getWebADESecurityManager(app.getApplicationCode())
                .setCurrentUserCredentials(currentUserPerms.getUserCredentials());
        WebADESecurityManager.getWebADESecurityManager(app.getApplicationCode())
                .setCurrentUserPermissions(currentUserPerms);
        WebADESecurityManager.getWebADESecurityManager(app.getApplicationCode()).setCurrentUserInfo(currentUserInfo);
    }
}
