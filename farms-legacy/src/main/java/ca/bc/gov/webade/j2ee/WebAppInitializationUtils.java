/**
 * @(#)WebAppInitializationUtils.java
 * Copyright (c) 2006, B.C. Government.
 * All rights reserved
 */
package ca.bc.gov.webade.j2ee;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.webade.Application;
import ca.bc.gov.webade.WebADEApplicationUtils;
import ca.bc.gov.webade.WebADEException;

/**
 * @author jross
 */
public final class WebAppInitializationUtils {
    /**
     * The name of the servlet context init-parameter containing the application acronym.
     */
    public static final String WEBADE_APPLICATION_ACRONYM = "webade.application.acronym";
    /**
     * The attribute name for the <code>Application</code> object in the
     * <code>HttpSession</code>
     */
    public static final String WEBADE_APPLICATION = "webade.application.singleton";
    /**
     * The attribute name for the <code>UserInfoService</code> object in the
     * <code>HttpSession</code>
     */
    public static final String WEBADE_USER_INFO_SERVICE = "webade.user.info.service.singleton";

    private static boolean INITIALIZATION_ERROR = false;
    private static String INITIALIZATION_ERROR_MESSAGE;
    private static String INITIALIZATION_CALLER;
	
	private static final Logger log = LoggerFactory.getLogger(WebAppInitializationUtils.class);

    /**
     * Initialize the WebADE web application's core settings.
     * @param context
     *            Servlet context to create the application singleton in.
     * @param appCode
     *            The application code used to load WebADE info for.
     */
    public static synchronized void initializeWebApp(ServletContext context,
            String appCode) {
        log.trace("> initializeWebApp(" + appCode + ")");
        Application app = (Application) context
                .getAttribute(WEBADE_APPLICATION);
        if (app == null) {
            log.debug("Initializing the WebADE");
            StackTraceElement[] stack = new Throwable().getStackTrace();
            if (stack.length > 1) {
                INITIALIZATION_CALLER = stack[1].getClassName();
            }
            if (appCode == null || appCode.trim().equals("")) {
                String errorMessage = "Cannot initialize the WebADE.  "
                        + "The application code is not "
                        + "properly set for this WebADE application.";
                log.error(errorMessage);
                INITIALIZATION_ERROR = true;
                INITIALIZATION_ERROR_MESSAGE = errorMessage;
                return;
            }
            try {
                app = WebADEApplicationUtils.createApplication(appCode);
                context.setAttribute(WEBADE_APPLICATION, app);
                context.setAttribute(WEBADE_USER_INFO_SERVICE, app.getUserInfoService());
            } catch (WebADEException e) {
                log.error(e.getMessage(), e);
                INITIALIZATION_ERROR = true;
                INITIALIZATION_ERROR_MESSAGE = e.getMessage();
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
                INITIALIZATION_ERROR = true;
                INITIALIZATION_ERROR_MESSAGE = e.getMessage();
            }
        } else if (INITIALIZATION_CALLER == null) {
            log.error("WebADE is already initialized, but was not "
                    + "initialized by WebAppInitializationUtils initializeWebApp "
                    + "method.");
        } else {
            String callerClass = null;
            StackTraceElement[] stack = new Throwable().getStackTrace();
            if (stack.length > 1) {
                callerClass = stack[1].getClassName();
            }
            log.warn("WebADE is already initialized by "
                    + INITIALIZATION_CALLER + ".  Caller class " + callerClass
                    + " is not permitted to re-initialize the WebADE.");
        }
        log.trace("< initializeWebApp(" + appCode + ")");
    }

    /**
     * Returns whether the WebADE web application has been initialized. Even if
     * the initialization failed on error, this method will return true.
     * @return True if the WebAppInitializationUtils initializeWebApp method has been
     *         called.
     */
    public static boolean isWebAppInitialized() {
        return (INITIALIZATION_CALLER != null);
    }

    /**
     * Returns whether the WebADE web application initialization failed on
     * error.
     * @return True if the WebAppInitializationUtils initializeWebApp method has been
     *         called, and the call failed on error.
     */
    public static boolean hasInitializationFailed() {
        return INITIALIZATION_ERROR;
    }

    /**
     * Returns the initialization error message when the WebADE web application
     * initialization failed on error.
     * @return The error message to display.
     */
    public static String getInitializationErrorMessage() {
        return INITIALIZATION_ERROR_MESSAGE;
    }

    private WebAppInitializationUtils() {
    }

}
