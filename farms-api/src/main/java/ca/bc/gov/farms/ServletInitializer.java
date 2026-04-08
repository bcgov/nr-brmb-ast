package ca.bc.gov.farms;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import ca.bc.gov.brmb.common.rest.endpoints.filters.RequestMetricsFilter;
import ca.bc.gov.brmb.common.rest.endpoints.filters.VersionForwardingFilter;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(FarmsApiApplication.class);
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		logger.info("<onStartup");

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
