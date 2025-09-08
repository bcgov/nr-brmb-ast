package ca.bc.gov.farms.api.rest.v1.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.bc.gov.farms.api.rest.v1.resource.factory.BenchmarkPerUnitRsrcFactory;
import ca.bc.gov.farms.api.rest.v1.resource.factory.FairMarketValueRsrcFactory;
import ca.bc.gov.farms.api.rest.v1.resource.factory.InventoryItemAttributeRsrcFactory;
import ca.bc.gov.farms.api.rest.v1.resource.factory.InventoryItemDetailRsrcFactory;
import ca.bc.gov.farms.api.rest.v1.resource.factory.InventoryTypeXrefRsrcFactory;
import ca.bc.gov.farms.api.rest.v1.resource.factory.StructureGroupAttributeRsrcFactory;
import ca.bc.gov.farms.service.api.v1.model.factory.BenchmarkPerUnitFactory;
import ca.bc.gov.farms.service.api.v1.model.factory.FairMarketValueFactory;
import ca.bc.gov.farms.service.api.v1.model.factory.InventoryItemAttributeFactory;
import ca.bc.gov.farms.service.api.v1.model.factory.InventoryItemDetailFactory;
import ca.bc.gov.farms.service.api.v1.model.factory.InventoryTypeXrefFactory;
import ca.bc.gov.farms.service.api.v1.model.factory.StructureGroupAttributeFactory;

@Configuration
public class ResourceFactorySpringConfig {

    private static final Logger logger = LoggerFactory.getLogger(ResourceFactorySpringConfig.class);

    public ResourceFactorySpringConfig() {
        logger.info("<ResourceFactorySpringConfig");

        logger.info(">ResourceFactorySpringConfig");
    }

    @Bean
    public BenchmarkPerUnitFactory benchmarkPerUnitFactory() {
        return new BenchmarkPerUnitRsrcFactory();
    }

    @Bean
    public FairMarketValueFactory fairMarketValueFactory() {
        return new FairMarketValueRsrcFactory();
    }

    @Bean
    public InventoryItemDetailFactory inventoryItemDetailFactory() {
        return new InventoryItemDetailRsrcFactory();
    }

    @Bean
    public InventoryTypeXrefFactory inventoryTypeFactory() {
        return new InventoryTypeXrefRsrcFactory();
    }

    @Bean
    public InventoryItemAttributeFactory inventoryItemAttributeFactory() {
        return new InventoryItemAttributeRsrcFactory();
    }

    @Bean
    public StructureGroupAttributeFactory structureGroupAttributeFactory() {
        return new StructureGroupAttributeRsrcFactory();
    }
}
