package ca.bc.gov.farms.api.rest.v1.endpoints;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ca.bc.gov.brmb.common.api.rest.code.endpoints.spring.CodeEndpointsSpringConfig;
import ca.bc.gov.farms.api.rest.v1.endpoints.impl.FairMarketValueEndpointsImpl;
import ca.bc.gov.farms.api.rest.v1.endpoints.impl.InventoryItemDetailEndpointsImpl;
import ca.bc.gov.farms.api.rest.v1.resource.FairMarketValueRsrc;
import ca.bc.gov.farms.api.rest.v1.resource.InventoryItemDetailRsrc;
import ca.bc.gov.farms.api.rest.v1.spring.EndpointsSpringConfigTest;
import ca.bc.gov.farms.api.rest.v1.spring.ResourceFactorySpringConfig;
import ca.bc.gov.farms.service.api.v1.spring.ServiceApiSpringConfig;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.json.JSONArray;
import org.json.JSONObject;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FairMarketValueEndpointsTest extends JerseyTest {

    @Override
    protected Application configure() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("X-Forwarded-Proto")).thenReturn("http");
        when(mockRequest.getHeader("X-Forwarded-Host")).thenReturn("localhost");
        when(mockRequest.getHeader("If-Match")).thenReturn("*");

        // Load Spring context manually
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(EndpointsSpringConfigTest.class,
                ServiceApiSpringConfig.class, ResourceFactorySpringConfig.class, CodeEndpointsSpringConfig.class);

        // Create Jersey ResourceConfig and integrate Spring context
        ResourceConfig config = new ResourceConfig();
        config.register(ObjectMapperContextResolver.class);
        config.register(FairMarketValueEndpointsImpl.class);
        config.property("contextConfig", applicationContext);

        // Bind the mockRequest so it can be injected
        config.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(mockRequest).to(HttpServletRequest.class);
            }
        });

        return config;
    }

    @Test
    @Order(1)
    public void testCreateFairMarketValue() throws Exception {
        FairMarketValueRsrc fairMarketValue = new FairMarketValueRsrc();
        fairMarketValue.setProgramYear(2025);
        fairMarketValue.setInventoryItemCode("5560");
        fairMarketValue.setMunicipalityCode("41");
        fairMarketValue.setCropUnitCode("1");
        fairMarketValue.setDefaultCropUnitCode("2");
        fairMarketValue.setPeriod01Price(new BigDecimal("8.66"));
        fairMarketValue.setPeriod02Price(new BigDecimal("8.66"));
        fairMarketValue.setPeriod03Price(new BigDecimal("8.66"));
        fairMarketValue.setPeriod04Price(new BigDecimal("8.01"));
        fairMarketValue.setPeriod05Price(new BigDecimal("8.01"));
        fairMarketValue.setPeriod06Price(new BigDecimal("8.01"));
        fairMarketValue.setPeriod07Price(new BigDecimal("5.88"));
        fairMarketValue.setPeriod08Price(new BigDecimal("5.88"));
        fairMarketValue.setPeriod09Price(new BigDecimal("5.88"));
        fairMarketValue.setPeriod10Price(new BigDecimal("5.45"));
        fairMarketValue.setPeriod11Price(new BigDecimal("5.45"));
        fairMarketValue.setPeriod12Price(new BigDecimal("5.45"));
        fairMarketValue.setPeriod01Variance(new BigDecimal("46.00"));
        fairMarketValue.setPeriod02Variance(new BigDecimal("46.00"));
        fairMarketValue.setPeriod03Variance(new BigDecimal("46.00"));
        fairMarketValue.setPeriod04Variance(new BigDecimal("46.00"));
        fairMarketValue.setPeriod05Variance(new BigDecimal("46.00"));
        fairMarketValue.setPeriod06Variance(new BigDecimal("46.00"));
        fairMarketValue.setPeriod07Variance(new BigDecimal("46.00"));
        fairMarketValue.setPeriod08Variance(new BigDecimal("46.00"));
        fairMarketValue.setPeriod09Variance(new BigDecimal("46.00"));
        fairMarketValue.setPeriod10Variance(new BigDecimal("46.00"));
        fairMarketValue.setPeriod11Variance(new BigDecimal("46.00"));
        fairMarketValue.setPeriod12Variance(new BigDecimal("46.00"));
        fairMarketValue.setUserEmail("jsmith@gmail.com");

        Response response = target("/fairMarketValues").request().post(Entity.json(fairMarketValue));
        assertEquals(201, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);
        assertEquals("FairMarketValueRsrc", jsonObject.getString("@type"));
        assertEquals("2025_5560_41_1", jsonObject.getString("fairMarketValueId"));
        assertEquals(2025, jsonObject.getInt("programYear"));
        assertEquals("5560", jsonObject.getString("inventoryItemCode"));
        assertEquals("Alfalfa Dehy", jsonObject.getString("inventoryItemDesc"));
        assertEquals("41", jsonObject.getString("municipalityCode"));
        assertEquals("Cariboo", jsonObject.getString("municipalityDesc"));
        assertEquals("1", jsonObject.getString("cropUnitCode"));
        assertEquals("Pounds", jsonObject.getString("cropUnitDesc"));
        assertEquals("2", jsonObject.getString("defaultCropUnitCode"));
        assertEquals("Tonnes", jsonObject.getString("defaultCropUnitDesc"));
        assertEquals(8.66, jsonObject.getDouble("period01Price"));
        assertEquals(8.66, jsonObject.getDouble("period02Price"));
        assertEquals(8.66, jsonObject.getDouble("period03Price"));
        assertEquals(8.01, jsonObject.getDouble("period04Price"));
        assertEquals(8.01, jsonObject.getDouble("period05Price"));
        assertEquals(8.01, jsonObject.getDouble("period06Price"));
        assertEquals(5.88, jsonObject.getDouble("period07Price"));
        assertEquals(5.88, jsonObject.getDouble("period08Price"));
        assertEquals(5.88, jsonObject.getDouble("period09Price"));
        assertEquals(5.45, jsonObject.getDouble("period10Price"));
        assertEquals(5.45, jsonObject.getDouble("period11Price"));
        assertEquals(5.45, jsonObject.getDouble("period12Price"));
        assertEquals(46.00, jsonObject.getDouble("period01Variance"));
        assertEquals(46.00, jsonObject.getDouble("period02Variance"));
        assertEquals(46.00, jsonObject.getDouble("period03Variance"));
        assertEquals(46.00, jsonObject.getDouble("period04Variance"));
        assertEquals(46.00, jsonObject.getDouble("period05Variance"));
        assertEquals(46.00, jsonObject.getDouble("period06Variance"));
        assertEquals(46.00, jsonObject.getDouble("period07Variance"));
        assertEquals(46.00, jsonObject.getDouble("period08Variance"));
        assertEquals(46.00, jsonObject.getDouble("period09Variance"));
        assertEquals(46.00, jsonObject.getDouble("period10Variance"));
        assertEquals(46.00, jsonObject.getDouble("period11Variance"));
        assertEquals(46.00, jsonObject.getDouble("period12Variance"));
        assertEquals("null", jsonObject.getString("userEmail"));
    }

    @Test
    @Order(2)
    public void testGetFairMarketValuesByProgramYear() throws Exception {
        Response response = target("/fairMarketValues").queryParam("programYear", 2025).request().get();
        assertEquals(200, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);

        assertEquals("FairMarketValueListRsrc", jsonObject.getString("@type"));

        JSONArray fairMarketValueList = jsonObject.getJSONArray("fairMarketValueList");
        JSONObject fairMarketValue = fairMarketValueList.getJSONObject(0);

        assertEquals("FairMarketValueRsrc", fairMarketValue.getString("@type"));
        assertEquals("2025_5560_41_1", fairMarketValue.getString("fairMarketValueId"));
        assertEquals(2025, fairMarketValue.getInt("programYear"));
        assertEquals("5560", fairMarketValue.getString("inventoryItemCode"));
        assertEquals("Alfalfa Dehy", fairMarketValue.getString("inventoryItemDesc"));
        assertEquals("41", fairMarketValue.getString("municipalityCode"));
        assertEquals("Cariboo", fairMarketValue.getString("municipalityDesc"));
        assertEquals("1", fairMarketValue.getString("cropUnitCode"));
        assertEquals("Pounds", fairMarketValue.getString("cropUnitDesc"));
        assertEquals("2", fairMarketValue.getString("defaultCropUnitCode"));
        assertEquals("Tonnes", fairMarketValue.getString("defaultCropUnitDesc"));
        assertEquals(8.66, fairMarketValue.getDouble("period01Price"));
        assertEquals(8.66, fairMarketValue.getDouble("period02Price"));
        assertEquals(8.66, fairMarketValue.getDouble("period03Price"));
        assertEquals(8.01, fairMarketValue.getDouble("period04Price"));
        assertEquals(8.01, fairMarketValue.getDouble("period05Price"));
        assertEquals(8.01, fairMarketValue.getDouble("period06Price"));
        assertEquals(5.88, fairMarketValue.getDouble("period07Price"));
        assertEquals(5.88, fairMarketValue.getDouble("period08Price"));
        assertEquals(5.88, fairMarketValue.getDouble("period09Price"));
        assertEquals(5.45, fairMarketValue.getDouble("period10Price"));
        assertEquals(5.45, fairMarketValue.getDouble("period11Price"));
        assertEquals(5.45, fairMarketValue.getDouble("period12Price"));
        assertEquals(46.00, fairMarketValue.getDouble("period01Variance"));
        assertEquals(46.00, fairMarketValue.getDouble("period02Variance"));
        assertEquals(46.00, fairMarketValue.getDouble("period03Variance"));
        assertEquals(46.00, fairMarketValue.getDouble("period04Variance"));
        assertEquals(46.00, fairMarketValue.getDouble("period05Variance"));
        assertEquals(46.00, fairMarketValue.getDouble("period06Variance"));
        assertEquals(46.00, fairMarketValue.getDouble("period07Variance"));
        assertEquals(46.00, fairMarketValue.getDouble("period08Variance"));
        assertEquals(46.00, fairMarketValue.getDouble("period09Variance"));
        assertEquals(46.00, fairMarketValue.getDouble("period10Variance"));
        assertEquals(46.00, fairMarketValue.getDouble("period11Variance"));
        assertEquals(46.00, fairMarketValue.getDouble("period12Variance"));
        assertEquals("null", fairMarketValue.getString("userEmail"));
    }
}
