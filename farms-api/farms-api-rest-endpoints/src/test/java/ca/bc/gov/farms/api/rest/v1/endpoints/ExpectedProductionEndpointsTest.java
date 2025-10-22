package ca.bc.gov.farms.api.rest.v1.endpoints;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ca.bc.gov.brmb.common.api.rest.code.endpoints.spring.CodeEndpointsSpringConfig;
import ca.bc.gov.farms.api.rest.v1.endpoints.impl.ExpectedProductionEndpointsImpl;
import ca.bc.gov.farms.api.rest.v1.resource.ExpectedProductionRsrc;
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
public class ExpectedProductionEndpointsTest extends JerseyTest {

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
        config.register(ExpectedProductionEndpointsImpl.class);
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
    public void testCreateExpectedProduction() throws Exception {
        ExpectedProductionRsrc expectedProduction = new ExpectedProductionRsrc();
        expectedProduction.setExpectedProductionPerProdUnit(new BigDecimal("0.907"));
        expectedProduction.setInventoryItemCode("73");
        expectedProduction.setCropUnitCode("1");
        expectedProduction.setUserEmail("jsmith@gmail.com");

        Response response = target("/expectedProductions").request().post(Entity.json(expectedProduction));
        assertEquals(201, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);
        assertEquals("ExpectedProductionRsrc", jsonObject.getString("@type"));
        assertEquals(341, jsonObject.getInt("expectedProductionId"));
        assertEquals(0.907, jsonObject.getDouble("expectedProductionPerProdUnit"));
        assertEquals("73", jsonObject.getString("inventoryItemCode"));
        assertEquals("Strawberries", jsonObject.getString("inventoryItemDesc"));
        assertEquals("1", jsonObject.getString("cropUnitCode"));
        assertEquals("Pounds", jsonObject.getString("cropUnitDesc"));
        assertEquals("null", jsonObject.getString("userEmail"));
    }

    @Test
    @Order(2)
    public void testGetAllExpectedProductions() throws Exception {
        Response response = target("/expectedProductions").request().get();
        assertEquals(200, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);

        assertEquals("ExpectedProductionListRsrc", jsonObject.getString("@type"));

        JSONArray expectedProductionList = jsonObject.getJSONArray("expectedProductionList");
        JSONObject expectedProduction = expectedProductionList.getJSONObject(0);
        assertEquals("ExpectedProductionRsrc", expectedProduction.getString("@type"));
        assertEquals(341, expectedProduction.getInt("expectedProductionId"));
        assertEquals(0.907, expectedProduction.getDouble("expectedProductionPerProdUnit"));
        assertEquals("73", expectedProduction.getString("inventoryItemCode"));
        assertEquals("Strawberries", expectedProduction.getString("inventoryItemDesc"));
        assertEquals("1", expectedProduction.getString("cropUnitCode"));
        assertEquals("Pounds", expectedProduction.getString("cropUnitDesc"));
        assertEquals("null", expectedProduction.getString("userEmail"));
    }

    @Test
    @Order(3)
    public void testGetAllExpectedProductions1() throws Exception {
        Response response = target("/expectedProductions").queryParam("inventoryItemCode", "73").request().get();
        assertEquals(200, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);

        assertEquals("ExpectedProductionListRsrc", jsonObject.getString("@type"));

        JSONArray expectedProductionList = jsonObject.getJSONArray("expectedProductionList");
        JSONObject expectedProduction = expectedProductionList.getJSONObject(0);
        assertEquals("ExpectedProductionRsrc", expectedProduction.getString("@type"));
        assertEquals(341, expectedProduction.getInt("expectedProductionId"));
        assertEquals(0.907, expectedProduction.getDouble("expectedProductionPerProdUnit"));
        assertEquals("73", expectedProduction.getString("inventoryItemCode"));
        assertEquals("Strawberries", expectedProduction.getString("inventoryItemDesc"));
        assertEquals("1", expectedProduction.getString("cropUnitCode"));
        assertEquals("Pounds", expectedProduction.getString("cropUnitDesc"));
        assertEquals("null", expectedProduction.getString("userEmail"));
    }

    @Test
    @Order(4)
    public void testGetExpectedProduction() throws Exception {
        Response response = target("/expectedProductions/341").request().get();
        assertEquals(200, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);
        assertEquals("ExpectedProductionRsrc", jsonObject.getString("@type"));
        assertEquals(341, jsonObject.getInt("expectedProductionId"));
        assertEquals(0.907, jsonObject.getDouble("expectedProductionPerProdUnit"));
        assertEquals("73", jsonObject.getString("inventoryItemCode"));
        assertEquals("Strawberries", jsonObject.getString("inventoryItemDesc"));
        assertEquals("1", jsonObject.getString("cropUnitCode"));
        assertEquals("Pounds", jsonObject.getString("cropUnitDesc"));
        assertEquals("null", jsonObject.getString("userEmail"));
    }
}
