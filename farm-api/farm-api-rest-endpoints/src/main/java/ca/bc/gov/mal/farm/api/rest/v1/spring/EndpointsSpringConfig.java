package ca.bc.gov.farms.api.rest.v1.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EndpointsSpringConfig {

    private static final Logger logger = LoggerFactory.getLogger(EndpointsSpringConfig.class);

    public EndpointsSpringConfig() {
        logger.info("<EndpointsSpringConfig");

        logger.info(">EndpointsSpringConfig");
    }
}
