package ca.bc.gov.farms.service.api.v1.spring;

import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;

import ca.bc.gov.farms.persistence.v1.spring.PersistenceSpringConfig;
import ca.bc.gov.farms.service.api.v1.BenchmarkPerUnitService;
import ca.bc.gov.farms.service.api.v1.FairMarketValueService;
import ca.bc.gov.farms.service.api.v1.ImportBPUService;
import ca.bc.gov.farms.service.api.v1.ImportService;
import ca.bc.gov.farms.service.api.v1.impl.BenchmarkPerUnitServiceImpl;
import ca.bc.gov.farms.service.api.v1.impl.FairMarketValueServiceImpl;
import ca.bc.gov.farms.service.api.v1.impl.ImportBPUServiceImpl;
import ca.bc.gov.farms.service.api.v1.impl.ImportServiceImpl;
import ca.bc.gov.farms.service.api.v1.model.factory.BenchmarkPerUnitFactory;
import ca.bc.gov.farms.service.api.v1.model.factory.FairMarketValueFactory;
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
    @Autowired
    private FairMarketValueFactory fairMarketValueFactory;

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

    @Bean
    public FairMarketValueService fairMarketValueService() {
        FairMarketValueServiceImpl result;

        result = new FairMarketValueServiceImpl();
        result.setModelValidator(modelValidator());
        result.setApplicationProperties(applicationProperties);

        result.setFairMarketValueFactory(fairMarketValueFactory);

        result.setFairMarketValueDao(persistenceSpringConfig.fairMarketValueDao());

        return result;
    }

    @Bean
    public ImportBPUService importBPUService(DataSource farmsDataSource) {
        ImportBPUServiceImpl result;

        result = new ImportBPUServiceImpl();
        result.setDataSource(farmsDataSource);

        return result;
    }

    @Bean
    public ImportService importService(DataSource farmsDataSource) {
        ImportServiceImpl result;

        result = new ImportServiceImpl();
        result.setDataSource(farmsDataSource);

        return result;
    }
}
