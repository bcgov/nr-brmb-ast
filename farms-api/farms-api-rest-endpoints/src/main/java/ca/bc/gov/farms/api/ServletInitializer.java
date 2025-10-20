package ca.bc.gov.farms.api;

import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;

import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import ca.bc.gov.farms.api.rest.v1.jersey.JerseyApplication;
import ca.bc.gov.farms.api.rest.v1.spring.CorsFilter;
import ca.bc.gov.brmb.common.rest.endpoints.filters.RequestMetricsFilter;
import ca.bc.gov.brmb.common.rest.endpoints.filters.VersionForwardingFilter;

public class ServletInitializer extends SpringBootServletInitializer {

    private static final Logger logger = LoggerFactory.getLogger(ServletInitializer.class);
    private static final String PAR_NAME_CTX_CONFIG_LOCATION = "contextConfigLocation";

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(FarmApiRestEndpointsApplication.class);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        logger.info("<onStartup");

        // Disable Jersey Spring Context Loader
        servletContext.setInitParameter(PAR_NAME_CTX_CONFIG_LOCATION, "java configuration");

        ServletRegistration.Dynamic restServlet = servletContext.addServlet("Rest Servlet", ServletContainer.class);
        restServlet.setInitParameter("jakarta.ws.rs.Application", JerseyApplication.class.getName());
        restServlet.setLoadOnStartup(1);

        restServlet.addMapping("/*");

        FilterRegistration.Dynamic requestMetricsFilter = servletContext.addFilter("Request Metrics Filter",
                RequestMetricsFilter.class);
        requestMetricsFilter.setInitParameter("id_source", "FARMSAPI");
        requestMetricsFilter.addMappingForUrlPatterns(null, false, "/*");

        FilterRegistration.Dynamic versionForwardingFilter = servletContext.addFilter("Version Forwarding Filter",
                VersionForwardingFilter.class);
        versionForwardingFilter.setInitParameter(VersionForwardingFilter.RESPONSE_VERSION_PARAM, "1");
        versionForwardingFilter.setInitParameter(VersionForwardingFilter.DEFAULT_REQUEST_VERSION_PARAM, "1");
        versionForwardingFilter.addMappingForUrlPatterns(null, false, "/*");

        FilterRegistration.Dynamic corsFilter = servletContext.addFilter("CORS Filter", CorsFilter.class);
        corsFilter.addMappingForUrlPatterns(null, false, "/*");

        super.onStartup(servletContext);

        logger.info(">onStartup");

    }
}
