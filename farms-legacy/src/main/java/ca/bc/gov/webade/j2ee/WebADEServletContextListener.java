/**
 * @(#)WebADEServletContextListener.java 
 * Copyright (c) 2005, B.C. Government. 
 * All rights reserved
 */
package ca.bc.gov.webade.j2ee;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.webade.Application;
import ca.bc.gov.webade.http.HttpRequestUtils;

/**
 * @author jross
 */
public final class WebADEServletContextListener implements
        ServletContextListener {

    /**
     * The name of the filter init-parameter containing the application acronym.
     */
    public static final String WEBADE_APPLICATION_ACRONYM = "webade.application.acronym";
	
	private static final Logger log = LoggerFactory.getLogger(WebADEServletContextListener.class);

    /**
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    @Override
	public final void contextInitialized(ServletContextEvent event) {
        log.trace("> init()");
        ServletContext context = event.getServletContext();

        String appCode = context.getInitParameter(WEBADE_APPLICATION_ACRONYM);
        if (appCode == null) {
            appCode = System.getProperty(WEBADE_APPLICATION_ACRONYM);
        }
        if (appCode == null) {
            log.error("No application acronym defined as servlet "
                    + "context init-param or system property called '"
                    + WEBADE_APPLICATION_ACRONYM + "'.");
        } else {
            log.debug("Initializing WebADE application.");
            WebAppInitializationUtils.initializeWebApp(context, appCode);
            Application app = HttpRequestUtils.getApplication(context);
            if (app == null) {
                log.error("WebADE application singleton '" + appCode
                        + "' not loaded.");
            } else {
                log.debug("WebADE application singleton '"
                        + app.getApplicationCode()
                        + "' loaded in servlet context.");
            }
        }
        log.trace("< init()");
    }

    /**
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
	public final void contextDestroyed(ServletContextEvent event) {
        log.trace("Starting shutting down application singleton.");
        ServletContext context = event.getServletContext();
        Application app = HttpRequestUtils.getApplication(context);
        if (app != null) {
            app.shutdown();
        }
        log.trace("Completed shutting down application singleton.");
    }
}
