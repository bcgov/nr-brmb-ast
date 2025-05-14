package ca.bc.gov.farms.api.rest.v1.jersey;

import javax.servlet.ServletConfig;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.farms.api.rest.v1.endpoints.impl.BenchmarkPerUnitEndpointsImpl;
import ca.bc.gov.farms.api.rest.v1.endpoints.impl.CodeTableEndpointsImpl;
import ca.bc.gov.farms.api.rest.v1.endpoints.impl.CodeTableListEndpointsImpl;
import ca.bc.gov.farms.api.rest.v1.endpoints.impl.TopLevelEndpointsImpl;
import ca.bc.gov.brmb.common.api.rest.code.endpoints.impl.CodeEndpointsImpl;
import ca.bc.gov.brmb.common.rest.endpoints.jersey.JerseyResourceConfig;

public class JerseyApplication extends JerseyResourceConfig {

    private static final Logger logger = LoggerFactory.getLogger(JerseyApplication.class);

    /**
     * Register JAX-RS application components.
     */
    public JerseyApplication(@Context ServletConfig servletConfig) {
        super();

        logger.debug("<JerseyApplication");

        register(TopLevelEndpointsImpl.class);
        register(CodeTableEndpointsImpl.class);
        register(CodeTableListEndpointsImpl.class);
        register(CodeEndpointsImpl.class);

        register(BenchmarkPerUnitEndpointsImpl.class);

        logger.debug(">JerseyApplication");
    }
}
