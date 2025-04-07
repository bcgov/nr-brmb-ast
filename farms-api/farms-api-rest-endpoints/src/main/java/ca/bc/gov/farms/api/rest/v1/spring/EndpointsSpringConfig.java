package ca.bc.gov.farms.api.rest.v1.spring;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import ca.bc.gov.farms.service.api.v1.spring.ServiceApiSpringConfig;
import ca.bc.gov.nrs.wfone.common.checkhealth.CheckHealthValidator;
import ca.bc.gov.nrs.wfone.common.checkhealth.CompositeValidator;
import ca.bc.gov.nrs.wfone.common.utils.ApplicationContextProvider;

@Configuration
@Import({
        PropertiesSpringConfig.class,
        ServiceApiSpringConfig.class,
        CodeEndpointsSpringConfig.class,
        SecurityConfig.class,
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

    @Primary
    @Bean
    public DataSource farmsDataSource() {
        DataSource result;

        final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
        dsLookup.setResourceRef(true);
        result = dsLookup.getDataSource("java:comp/env/jdbc/farms_rest");

        return result;
    }

    @Bean
    public DataSource codeTableDataSource() {
        DataSource result;

        result = farmsDataSource();

        return result;
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource result;

        result = new ResourceBundleMessageSource();
        result.setBasename("messages");

        return result;
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

        result.add(databaseCheckHealthValidator());

        return result;
    }

    @Bean(initMethod = "init")
    public FarmsDatabaseCheckHealthValidator databaseCheckHealthValidator() {
        FarmsDatabaseCheckHealthValidator result;

        result = new FarmsDatabaseCheckHealthValidator();
        result.setUsername("proxy_farms_rest");
        result.setDescription("java:comp/env/jdbc/farms_rest");
        result.setDataSource(farmsDataSource());

        return result;
    }

}
