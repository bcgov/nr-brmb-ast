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
import ca.bc.gov.farms.service.api.v1.ConfigurationParameterService;
import ca.bc.gov.farms.service.api.v1.CropUnitConversionService;
import ca.bc.gov.farms.service.api.v1.ExpectedProductionService;
import ca.bc.gov.farms.service.api.v1.FairMarketValueService;
import ca.bc.gov.farms.service.api.v1.FruitVegTypeDetailService;
import ca.bc.gov.farms.service.api.v1.ImportBPUService;
import ca.bc.gov.farms.service.api.v1.ImportCRAService;
import ca.bc.gov.farms.service.api.v1.ImportFMVService;
import ca.bc.gov.farms.service.api.v1.ImportIVPRService;
import ca.bc.gov.farms.service.api.v1.ImportService;
import ca.bc.gov.farms.service.api.v1.InventoryItemAttributeService;
import ca.bc.gov.farms.service.api.v1.InventoryItemDetailService;
import ca.bc.gov.farms.service.api.v1.InventoryTypeXrefService;
import ca.bc.gov.farms.service.api.v1.LineItemService;
import ca.bc.gov.farms.service.api.v1.MarketRatePremiumService;
import ca.bc.gov.farms.service.api.v1.ProductiveUnitCodeService;
import ca.bc.gov.farms.service.api.v1.StructureGroupAttributeService;
import ca.bc.gov.farms.service.api.v1.YearConfigurationParameterService;
import ca.bc.gov.farms.service.api.v1.impl.BenchmarkPerUnitServiceImpl;
import ca.bc.gov.farms.service.api.v1.impl.ConfigurationParameterServiceImpl;
import ca.bc.gov.farms.service.api.v1.impl.CropUnitConversionServiceImpl;
import ca.bc.gov.farms.service.api.v1.impl.ExpectedProductionServiceImpl;
import ca.bc.gov.farms.service.api.v1.impl.FairMarketValueServiceImpl;
import ca.bc.gov.farms.service.api.v1.impl.FruitVegTypeDetailServiceImpl;
import ca.bc.gov.farms.service.api.v1.impl.ImportBPUServiceImpl;
import ca.bc.gov.farms.service.api.v1.impl.ImportCRAServiceImpl;
import ca.bc.gov.farms.service.api.v1.impl.ImportFMVServiceImpl;
import ca.bc.gov.farms.service.api.v1.impl.ImportIVPRServiceImpl;
import ca.bc.gov.farms.service.api.v1.impl.ImportServiceImpl;
import ca.bc.gov.farms.service.api.v1.impl.InventoryItemAttributeServiceImpl;
import ca.bc.gov.farms.service.api.v1.impl.InventoryItemDetailServiceImpl;
import ca.bc.gov.farms.service.api.v1.impl.InventoryTypeXrefServiceImpl;
import ca.bc.gov.farms.service.api.v1.impl.LineItemServiceImpl;
import ca.bc.gov.farms.service.api.v1.impl.MarketRatePremiumServiceImpl;
import ca.bc.gov.farms.service.api.v1.impl.ProductiveUnitCodeServiceImpl;
import ca.bc.gov.farms.service.api.v1.impl.StructureGroupAttributeServiceImpl;
import ca.bc.gov.farms.service.api.v1.impl.YearConfigurationParameterServiceImpl;
import ca.bc.gov.farms.service.api.v1.model.factory.BenchmarkPerUnitFactory;
import ca.bc.gov.farms.service.api.v1.model.factory.ConfigurationParameterFactory;
import ca.bc.gov.farms.service.api.v1.model.factory.CropUnitConversionFactory;
import ca.bc.gov.farms.service.api.v1.model.factory.ExpectedProductionFactory;
import ca.bc.gov.farms.service.api.v1.model.factory.FairMarketValueFactory;
import ca.bc.gov.farms.service.api.v1.model.factory.FruitVegTypeDetailFactory;
import ca.bc.gov.farms.service.api.v1.model.factory.InventoryItemAttributeFactory;
import ca.bc.gov.farms.service.api.v1.model.factory.InventoryItemDetailFactory;
import ca.bc.gov.farms.service.api.v1.model.factory.InventoryTypeXrefFactory;
import ca.bc.gov.farms.service.api.v1.model.factory.LineItemFactory;
import ca.bc.gov.farms.service.api.v1.model.factory.MarketRatePremiumFactory;
import ca.bc.gov.farms.service.api.v1.model.factory.ProductiveUnitCodeFactory;
import ca.bc.gov.farms.service.api.v1.model.factory.StructureGroupAttributeFactory;
import ca.bc.gov.farms.service.api.v1.model.factory.YearConfigurationParameterFactory;
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
    @Autowired
    private InventoryItemDetailFactory inventoryItemDetailFactory;
    @Autowired
    private InventoryTypeXrefFactory inventoryTypeXrefFactory;
    @Autowired
    private InventoryItemAttributeFactory inventoryItemAttributeFactory;
    @Autowired
    private StructureGroupAttributeFactory structureGroupAttributeFactory;
    @Autowired
    private ConfigurationParameterFactory configurationParameterFactory;
    @Autowired
    private LineItemFactory lineItemFactory;
    @Autowired
    private FruitVegTypeDetailFactory fruitVegTypeDetailFactory;
    @Autowired
    private YearConfigurationParameterFactory yearConfigurationParameterFactory;
    @Autowired
    private MarketRatePremiumFactory marketRatePremiumFactory;
    @Autowired
    private CropUnitConversionFactory cropUnitConversionFactory;
    @Autowired
    private ExpectedProductionFactory expectedProductionFactory;
    @Autowired
    private ProductiveUnitCodeFactory productiveUnitCodeFactory;

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
    public InventoryItemDetailService inventoryItemDetailService() {
        InventoryItemDetailServiceImpl result;

        result = new InventoryItemDetailServiceImpl();
        result.setModelValidator(modelValidator());
        result.setApplicationProperties(applicationProperties);

        result.setInventoryItemDetailFactory(inventoryItemDetailFactory);

        result.setInventoryItemDetailDao(persistenceSpringConfig.inventoryItemDetailDao());

        return result;
    }

    @Bean
    public InventoryTypeXrefService inventoryTypeXrefService() {
        InventoryTypeXrefServiceImpl result;

        result = new InventoryTypeXrefServiceImpl();
        result.setModelValidator(modelValidator());
        result.setApplicationProperties(applicationProperties);

        result.setInventoryTypeXrefFactory(inventoryTypeXrefFactory);

        result.setInventoryTypeXrefDao(persistenceSpringConfig.inventoryTypeXrefDao());

        return result;
    }

    @Bean
    public InventoryItemAttributeService inventoryItemAttributeService() {
        InventoryItemAttributeServiceImpl result;

        result = new InventoryItemAttributeServiceImpl();
        result.setModelValidator(modelValidator());
        result.setApplicationProperties(applicationProperties);

        result.setInventoryItemAttributeFactory(inventoryItemAttributeFactory);

        result.setInventoryItemAttributeDao(persistenceSpringConfig.inventoryItemAttributeDao());

        return result;
    }

    @Bean
    public StructureGroupAttributeService structureGroupAttributeService() {
        StructureGroupAttributeServiceImpl result;

        result = new StructureGroupAttributeServiceImpl();
        result.setModelValidator(modelValidator());
        result.setApplicationProperties(applicationProperties);

        result.setStructureGroupAttributeFactory(structureGroupAttributeFactory);

        result.setStructureGroupAttributeDao(persistenceSpringConfig.structureGroupAttributeDao());

        return result;
    }

    @Bean
    public ConfigurationParameterService configurationParameterService() {
        ConfigurationParameterServiceImpl result;

        result = new ConfigurationParameterServiceImpl();
        result.setModelValidator(modelValidator());
        result.setApplicationProperties(applicationProperties);

        result.setConfigurationParameterFactory(configurationParameterFactory);

        result.setConfigurationParameterDao(persistenceSpringConfig.configurationParameterDao());

        return result;
    }

    @Bean
    public LineItemService lineItemService() {
        LineItemServiceImpl result;

        result = new LineItemServiceImpl();
        result.setModelValidator(modelValidator());
        result.setApplicationProperties(applicationProperties);

        result.setLineItemFactory(lineItemFactory);

        result.setLineItemDao(persistenceSpringConfig.lineItemDao());

        return result;
    }

    @Bean
    public FruitVegTypeDetailService fruitVegTypeDetailService() {
        FruitVegTypeDetailServiceImpl result;

        result = new FruitVegTypeDetailServiceImpl();
        result.setModelValidator(modelValidator());
        result.setApplicationProperties(applicationProperties);

        result.setFruitVegTypeDetailFactory(fruitVegTypeDetailFactory);

        result.setFruitVegTypeDetailDao(persistenceSpringConfig.fruitVegTypeDetailDao());

        return result;
    }

    @Bean
    public YearConfigurationParameterService yearConfigurationParameterService() {
        YearConfigurationParameterServiceImpl result;

        result = new YearConfigurationParameterServiceImpl();
        result.setModelValidator(modelValidator());
        result.setApplicationProperties(applicationProperties);

        result.setYearConfigurationParameterFactory(yearConfigurationParameterFactory);

        result.setYearConfigurationParameterDao(persistenceSpringConfig.yearConfigurationParameterDao());

        return result;
    }

    @Bean
    public MarketRatePremiumService marketRatePremiumService() {
        MarketRatePremiumServiceImpl result;

        result = new MarketRatePremiumServiceImpl();
        result.setModelValidator(modelValidator());
        result.setApplicationProperties(applicationProperties);

        result.setMarketRatePremiumFactory(marketRatePremiumFactory);

        result.setMarketRatePremiumDao(persistenceSpringConfig.marketRatePremiumDao());

        return result;
    }

    @Bean
    public CropUnitConversionService cropUnitConversionService() {
        CropUnitConversionServiceImpl result;

        result = new CropUnitConversionServiceImpl();
        result.setModelValidator(modelValidator());
        result.setApplicationProperties(applicationProperties);

        result.setCropUnitConversionFactory(cropUnitConversionFactory);

        result.setCropUnitConversionDao(persistenceSpringConfig.cropUnitConversionDao());

        return result;
    }

    @Bean
    public ExpectedProductionService expectedProductionService() {
        ExpectedProductionServiceImpl result;

        result = new ExpectedProductionServiceImpl();
        result.setModelValidator(modelValidator());
        result.setApplicationProperties(applicationProperties);

        result.setExpectedProductionFactory(expectedProductionFactory);

        result.setExpectedProductionDao(persistenceSpringConfig.expectedProductionDao());

        return result;
    }

    @Bean
    public ProductiveUnitCodeService productiveUnitCodeService() {
        ProductiveUnitCodeServiceImpl result;

        result = new ProductiveUnitCodeServiceImpl();
        result.setModelValidator(modelValidator());
        result.setApplicationProperties(applicationProperties);

        result.setProductiveUnitCodeFactory(productiveUnitCodeFactory);

        result.setProductiveUnitCodeDao(persistenceSpringConfig.productiveUnitCodeDao());

        return result;
    }

    @Bean
    public ImportBPUService importBPUService(DataSource farmsDataSource) {
        return new ImportBPUServiceImpl(farmsDataSource);
    }

    @Bean
    public ImportCRAService importCRAService(DataSource farmsDataSource) {
        return new ImportCRAServiceImpl(farmsDataSource);
    }

    @Bean
    public ImportFMVService importFMVService(DataSource farmsDataSource) {
        return new ImportFMVServiceImpl(farmsDataSource);
    }

    @Bean
    public ImportService importService(DataSource farmsDataSource) {
        return new ImportServiceImpl(farmsDataSource);
    }

    @Bean
    public ImportIVPRService importIVPRService(DataSource farmsDataSource) {
        return new ImportIVPRServiceImpl(farmsDataSource);
    }
}
