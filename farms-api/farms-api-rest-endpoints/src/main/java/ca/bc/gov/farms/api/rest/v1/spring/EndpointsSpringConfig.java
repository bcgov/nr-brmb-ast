package ca.bc.gov.farms.api.rest.v1.spring;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import ca.bc.gov.farms.service.api.v1.spring.ServiceApiSpringConfig;
import ca.bc.gov.nrs.wfone.common.checkhealth.CheckHealthValidator;
import ca.bc.gov.nrs.wfone.common.checkhealth.CompositeValidator;
import ca.bc.gov.nrs.wfone.common.utils.ApplicationContextProvider;

@Configuration
@Import({
        PropertiesSpringConfig.class,
        ServiceApiSpringConfig.class,
        WebConfig.class,
        CorsFilter.class
})
public class EndpointsSpringConfig {

    private static final Logger logger = LoggerFactory.getLogger(EndpointsSpringConfig.class);

    public EndpointsSpringConfig() {
        logger.info("<EndpointsSpringConfig");

        logger.info(">EndpointsSpringConfig");
    }

    @Bean
    public ApplicationContextProvider applicationContextProvider() {
        return new ApplicationContextProvider();
    }

    @Bean(initMethod = "init")
    public CompositeValidator checkHealthValidator() {
        CompositeValidator result;

        result = new CompositeValidator();
        result.setComponentIdentifier("FARMS_REST");
        result.setComponentName("Farmer Access to Risk Management Service V1 Rest API");
        result.setValidators(healthCheckValidators());

        return result;
    }

    @Bean
    public List<CheckHealthValidator> healthCheckValidators() {
        List<CheckHealthValidator> result = new ArrayList<>();

        return result;
    }
}
