package ca.bc.gov.farms.service.api.v1.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;

import ca.bc.gov.farms.service.api.v1.validation.ModelValidator;

@Configuration
@Import({
        CodeTableSpringConfig.class
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

    // Imported Spring Config
    @Autowired
    private CodeTableSpringConfig codeTableSpringConfig;

    @Bean
    public ModelValidator modelValidator() {
        ModelValidator result;

        result = new ModelValidator();
        result.setCachedCodeTables(codeTableSpringConfig.cachedCodeTables());
        result.setMessageSource(messageSource);

        return result;
    }
}
