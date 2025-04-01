package ca.bc.gov.farms.api.rest.v1.jersey;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import ca.bc.gov.nrs.wfone.common.api.rest.code.endpoints.impl.CodeTableEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.api.rest.code.endpoints.impl.CodeTableListEndpointsImpl;
import ca.bc.gov.nrs.wfone.common.rest.endpoints.jersey.JerseyResourceConfig;

public class JerseyApplication extends JerseyResourceConfig {

    private static final Logger logger = LoggerFactory.getLogger(JerseyApplication.class);

    /**
     * Register JAX-RS application components.
     */
    public JerseyApplication(@Context ServletConfig servletConfig) {
        super();

        logger.debug("<JerseyApplication");

        register(CodeTableEndpointsImpl.class);
        register(CodeTableListEndpointsImpl.class);

        logger.debug(">JerseyApplication");
    }

    public static void configureJerseyWithSpring(ServletContext servletContext) {
        WebApplicationContext springContext = WebApplicationContextUtils
                .getRequiredWebApplicationContext(servletContext);
        servletContext.addListener(new ContextLoaderListener(springContext));
    }
}
