package ca.bc.gov.farms.api.rest.v1.spring;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class PropertiesSpringConfig {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesSpringConfig.class);

    public PropertiesSpringConfig() {
        logger.info("<PropertiesSpringConfig");

        logger.info(">PropertiesSpringConfig");
    }

    @Bean
    public static Properties applicationProperties() throws IOException, SQLException {
        Properties result;

        PropertiesFactoryBean propertiesFactory = new PropertiesFactoryBean();
        propertiesFactory.setLocalOverride(true);
        propertiesFactory.setLocation(new ClassPathResource("static.properties"));
        propertiesFactory.afterPropertiesSet();

        result = propertiesFactory.getObject();

        return result;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer()
            throws IOException, SQLException {
        PropertySourcesPlaceholderConfigurer result;

        result = new PropertySourcesPlaceholderConfigurer();
        result.setProperties(applicationProperties());

        return result;
    }
}
