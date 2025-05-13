package ca.bc.gov.farms.service.api.v1.spring;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;

import ca.bc.gov.farms.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.farms.service.api.v1.BenchmarkPerUnitService;
import ca.bc.gov.farms.service.api.v1.impl.BenchmarkPerUnitServiceImpl;
import ca.bc.gov.farms.service.api.v1.model.factory.BenchmarkPerUnitFactory;
import ca.bc.gov.farms.service.api.v1.validation.ModelValidator;

@Configuration
@Import({
        CodeTableSpringConfig.class,
        PersistenceSpringConfig.class
})
public class ServiceApiSpringConfig {

    private static final Logger logger = LoggerFactory.getLogger(ServiceApiSpringConfig.class);

    public ServiceApiSpringConfig() {
        logger.info("<ServiceApiSpringConfig");

        logger.info(">ServiceApiSpringConfig");
    }

    // Beans provided by EndpointsSpringConfig
    @Autowired
    private ResourceBundleMessageSource messageSource;
    @Autowired
    private Properties applicationProperties;

    // Beans provided by ResourceFactorySpringConfig
    @Autowired
    private BenchmarkPerUnitFactory benchmarkPerUnitFactory;

    // Imported Spring Config
    @Autowired
    private CodeTableSpringConfig codeTableSpringConfig;
    @Autowired
    private PersistenceSpringConfig persistenceSpringConfig;

    @Bean
    public ModelValidator modelValidator() {
        ModelValidator result;

        result = new ModelValidator();
        result.setCachedCodeTables(codeTableSpringConfig.cachedCodeTables());
        result.setMessageSource(messageSource);

        return result;
    }

    @Bean
    public BenchmarkPerUnitService benchmarkPerUnitService() {
        BenchmarkPerUnitServiceImpl result;

        result = new BenchmarkPerUnitServiceImpl();
        result.setModelValidator(modelValidator());
        result.setApplicationProperties(applicationProperties);

        result.setBenchmarkPerUnitFactory(benchmarkPerUnitFactory);

        result.setBenchmarkPerUnitDao(persistenceSpringConfig.benchmarkPerUnitDao());

        return result;
    }
}
