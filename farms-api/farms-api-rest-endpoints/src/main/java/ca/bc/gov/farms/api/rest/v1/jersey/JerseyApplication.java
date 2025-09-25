package ca.bc.gov.farms.api.rest.v1.jersey;

import javax.servlet.ServletConfig;
import javax.ws.rs.core.Context;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ca.bc.gov.farms.api.rest.v1.endpoints.impl.BenchmarkPerUnitEndpointsImpl;
import ca.bc.gov.farms.api.rest.v1.endpoints.impl.ConfigurationParameterEndpointsImpl;
import ca.bc.gov.farms.api.rest.v1.endpoints.impl.CropUnitConversionEndpointsImpl;
import ca.bc.gov.farms.api.rest.v1.endpoints.impl.ExpectedProductionEndpointsImpl;
import ca.bc.gov.farms.api.rest.v1.endpoints.impl.FairMarketValueEndpointsImpl;
import ca.bc.gov.farms.api.rest.v1.endpoints.impl.FruitVegTypeDetailEndpointsImpl;
import ca.bc.gov.farms.api.rest.v1.endpoints.impl.ImportEndpointsImpl;
import ca.bc.gov.farms.api.rest.v1.endpoints.impl.InventoryItemAttributeEndpointsImpl;
import ca.bc.gov.farms.api.rest.v1.endpoints.impl.InventoryItemDetailEndpointsImpl;
import ca.bc.gov.farms.api.rest.v1.endpoints.impl.InventoryTypeXrefEndpointsImpl;
import ca.bc.gov.farms.api.rest.v1.endpoints.impl.LineItemEndpointsImpl;
import ca.bc.gov.farms.api.rest.v1.endpoints.impl.MarketRatePremiumEndpointsImpl;
import ca.bc.gov.farms.api.rest.v1.endpoints.impl.StructureGroupAttributeEndpointsImpl;
import ca.bc.gov.farms.api.rest.v1.endpoints.impl.TopLevelEndpointsImpl;
import ca.bc.gov.farms.api.rest.v1.endpoints.impl.YearConfigurationParameterEndpointsImpl;
import ca.bc.gov.brmb.common.api.rest.code.endpoints.impl.CodeEndpointsImpl;
import ca.bc.gov.brmb.common.api.rest.code.endpoints.impl.CodeTableEndpointsImpl;
import ca.bc.gov.brmb.common.api.rest.code.endpoints.impl.CodeTableListEndpointsImpl;
import ca.bc.gov.brmb.common.rest.endpoints.jersey.JerseyResourceConfig;

public class JerseyApplication extends JerseyResourceConfig {

    private static final Logger logger = LoggerFactory.getLogger(JerseyApplication.class);

    /**
     * Register JAX-RS application components.
     */
    public JerseyApplication(@Context ServletConfig servletConfig) {
        super();

        logger.debug("<JerseyApplication");

        register(TopLevelEndpointsImpl.class);
        register(CodeTableEndpointsImpl.class);
        register(CodeTableListEndpointsImpl.class);
        register(CodeEndpointsImpl.class);

        register(BenchmarkPerUnitEndpointsImpl.class);
        register(FairMarketValueEndpointsImpl.class);
        register(InventoryItemDetailEndpointsImpl.class);
        register(InventoryTypeXrefEndpointsImpl.class);
        register(InventoryItemAttributeEndpointsImpl.class);
        register(StructureGroupAttributeEndpointsImpl.class);
        register(ConfigurationParameterEndpointsImpl.class);
        register(LineItemEndpointsImpl.class);
        register(FruitVegTypeDetailEndpointsImpl.class);
        register(YearConfigurationParameterEndpointsImpl.class);
        register(MarketRatePremiumEndpointsImpl.class);
        register(CropUnitConversionEndpointsImpl.class);
        register(ExpectedProductionEndpointsImpl.class);

        register(MultiPartFeature.class);
        register(ImportEndpointsImpl.class);

        logger.debug(">JerseyApplication");
    }
}
