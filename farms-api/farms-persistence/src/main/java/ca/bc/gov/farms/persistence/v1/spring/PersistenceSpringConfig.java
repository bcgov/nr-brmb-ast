package ca.bc.gov.farms.persistence.v1.spring;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.sql.DataSource;

import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ca.bc.gov.brmb.common.persistence.dao.mybatis.BooleanTypeHandler;
import ca.bc.gov.brmb.common.persistence.dao.mybatis.InstantTypeHandler;
import ca.bc.gov.brmb.common.persistence.dao.mybatis.LocalDateTimeTypeHandler;
import ca.bc.gov.brmb.common.persistence.dao.mybatis.LocalDateTypeHandler;
import ca.bc.gov.brmb.common.persistence.dao.mybatis.LocalTimeTypeHandler;
import ca.bc.gov.brmb.common.persistence.dao.mybatis.ResetDirtyInterceptor;
import ca.bc.gov.farms.persistence.v1.dao.BenchmarkPerUnitDao;
import ca.bc.gov.farms.persistence.v1.dao.ConfigurationParameterDao;
import ca.bc.gov.farms.persistence.v1.dao.FairMarketValueDao;
import ca.bc.gov.farms.persistence.v1.dao.FruitVegTypeDetailDao;
import ca.bc.gov.farms.persistence.v1.dao.InventoryItemAttributeDao;
import ca.bc.gov.farms.persistence.v1.dao.InventoryItemDetailDao;
import ca.bc.gov.farms.persistence.v1.dao.InventoryTypeXrefDao;
import ca.bc.gov.farms.persistence.v1.dao.LineItemDao;
import ca.bc.gov.farms.persistence.v1.dao.MarketRatePremiumDao;
import ca.bc.gov.farms.persistence.v1.dao.StructureGroupAttributeDao;
import ca.bc.gov.farms.persistence.v1.dao.YearConfigurationParameterDao;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.BenchmarkPerUnitDaoImpl;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.ConfigurationParameterDaoImpl;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.FairMarketValueDaoImpl;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.FruitVegTypeDetailDaoImpl;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.InventoryItemAttributeDaoImpl;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.InventoryItemDetailDaoImpl;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.InventoryTypeXrefDaoImpl;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.LineItemDaoImpl;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.MarketRatePremiumDaoImpl;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.StructureGroupAttributeDaoImpl;
import ca.bc.gov.farms.persistence.v1.dao.mybatis.YearConfigurationParameterDaoImpl;

@Configuration
@EnableTransactionManagement
public class PersistenceSpringConfig {

    private static final Logger logger = LoggerFactory.getLogger(PersistenceSpringConfig.class);

    public PersistenceSpringConfig() {
        logger.debug("<PersistenceSpringConfig");

        logger.debug(">PersistenceSpringConfig");
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource farmsDataSource) {
        return new DataSourceTransactionManager(farmsDataSource);
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource farmsDataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(farmsDataSource);

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();

        configuration.setCacheEnabled(true);
        configuration.setLazyLoadingEnabled(true);
        configuration.setAggressiveLazyLoading(false);
        configuration.setLocalCacheScope(LocalCacheScope.SESSION);

        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        typeHandlerRegistry.register(Boolean.class, JdbcType.VARCHAR, BooleanTypeHandler.class);
        typeHandlerRegistry.register(LocalTime.class, JdbcType.VARCHAR, LocalTimeTypeHandler.class);
        typeHandlerRegistry.register(LocalDate.class, JdbcType.DATE, LocalDateTypeHandler.class);
        typeHandlerRegistry.register(LocalDateTime.class, JdbcType.TIMESTAMP, LocalDateTimeTypeHandler.class);
        typeHandlerRegistry.register(Instant.class, JdbcType.TIMESTAMP, InstantTypeHandler.class);

        sessionFactory.setConfiguration(configuration);

        sessionFactory.setPlugins(new ResetDirtyInterceptor());

        return sessionFactory;
    }

    @Bean
    public static MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage("ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper");
        configurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        return configurer;
    }

    @Bean
    public BenchmarkPerUnitDao benchmarkPerUnitDao() {
        return new BenchmarkPerUnitDaoImpl();
    }

    @Bean
    public FairMarketValueDao fairMarketValueDao() {
        return new FairMarketValueDaoImpl();
    }

    @Bean
    public InventoryItemDetailDao inventoryItemDetailDao() {
        return new InventoryItemDetailDaoImpl();
    }

    @Bean
    public InventoryTypeXrefDao inventoryTypeXrefDao() {
        return new InventoryTypeXrefDaoImpl();
    }

    @Bean
    public InventoryItemAttributeDao inventoryItemAttributeDao() {
        return new InventoryItemAttributeDaoImpl();
    }

    @Bean
    public StructureGroupAttributeDao structureGroupAttributeDao() {
        return new StructureGroupAttributeDaoImpl();
    }

    @Bean
    public ConfigurationParameterDao configurationParameterDao() {
        return new ConfigurationParameterDaoImpl();
    }

    @Bean
    public LineItemDao lineItemDao() {
        return new LineItemDaoImpl();
    }

    @Bean
    public FruitVegTypeDetailDao fruitVegTypeDetailDao() {
        return new FruitVegTypeDetailDaoImpl();
    }

    @Bean
    public YearConfigurationParameterDao yearConfigurationParameterDao() {
        return new YearConfigurationParameterDaoImpl();
    }

    @Bean
    public MarketRatePremiumDao marketRatePremiumDao() {
        return new MarketRatePremiumDaoImpl();
    }
}
