package ca.bc.gov.farms.api.rest.v1.endpoints;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import ca.bc.gov.farms.api.rest.v1.endpoints.impl.InventoryItemAttributeEndpointsImpl;
import ca.bc.gov.farms.api.rest.v1.resource.InventoryItemAttributeRsrc;
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
public class InventoryItemAttributeEndpointsTest extends JerseyTest {

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
        config.register(InventoryItemAttributeEndpointsImpl.class);
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
    public void testCreateInventoryItemAttribute() throws Exception {
        InventoryItemAttributeRsrc inventoryItemAttribute = new InventoryItemAttributeRsrc();
        inventoryItemAttribute.setInventoryItemCode("73");
        inventoryItemAttribute.setRollupInventoryItemCode("73");
        inventoryItemAttribute.setUserEmail("jsmith@gmail.com");

        Response response = target("/inventoryItemAttributes").request().post(Entity.json(inventoryItemAttribute));
        assertEquals(201, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);
        assertEquals("InventoryItemAttributeRsrc", jsonObject.getString("@type"));
        assertEquals(3961, jsonObject.getInt("inventoryItemAttributeId"));
        assertEquals("73", jsonObject.getString("inventoryItemCode"));
        assertEquals("Strawberries", jsonObject.getString("inventoryItemDesc"));
        assertEquals("73", jsonObject.getString("rollupInventoryItemCode"));
        assertEquals("Strawberries", jsonObject.getString("rollupInventoryItemDesc"));
        assertEquals("null", jsonObject.getString("userEmail"));
    }

    @Test
    @Order(2)
    public void testGetInventoryItemAttributeByInventoryItemCode() throws Exception {
        Response response = target("/inventoryItemAttributes").queryParam("inventoryItemCode", "73").request().get();
        assertEquals(200, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);
        assertEquals("InventoryItemAttributeRsrc", jsonObject.getString("@type"));
        assertEquals(3961, jsonObject.getInt("inventoryItemAttributeId"));
        assertEquals("73", jsonObject.getString("inventoryItemCode"));
        assertEquals("Strawberries", jsonObject.getString("inventoryItemDesc"));
        assertEquals("73", jsonObject.getString("rollupInventoryItemCode"));
        assertEquals("Strawberries", jsonObject.getString("rollupInventoryItemDesc"));
        assertEquals("null", jsonObject.getString("userEmail"));
    }

    @Test
    @Order(3)
    public void testGetInventoryItemAttribute() throws Exception {
        Response response = target("/inventoryItemAttributes/3961").request().get();
        assertEquals(200, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);
        assertEquals("InventoryItemAttributeRsrc", jsonObject.getString("@type"));
        assertEquals(3961, jsonObject.getInt("inventoryItemAttributeId"));
        assertEquals("73", jsonObject.getString("inventoryItemCode"));
        assertEquals("Strawberries", jsonObject.getString("inventoryItemDesc"));
        assertEquals("73", jsonObject.getString("rollupInventoryItemCode"));
        assertEquals("Strawberries", jsonObject.getString("rollupInventoryItemDesc"));
        assertEquals("null", jsonObject.getString("userEmail"));
    }

    @Test
    @Order(4)
    public void testUpdateInventoryItemAttribute() throws Exception {
        InventoryItemAttributeRsrc inventoryItemAttribute = new InventoryItemAttributeRsrc();
        inventoryItemAttribute.setInventoryItemAttributeId(3961L);
        inventoryItemAttribute.setInventoryItemCode("73");
        inventoryItemAttribute.setRollupInventoryItemCode("5560");
        inventoryItemAttribute.setUserEmail("jsmith@gmail.com");

        Response response = target("/inventoryItemAttributes/3961").request().put(Entity.json(inventoryItemAttribute));
        assertEquals(200, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);
        assertEquals("InventoryItemAttributeRsrc", jsonObject.getString("@type"));
        assertEquals(3961, jsonObject.getInt("inventoryItemAttributeId"));
        assertEquals("73", jsonObject.getString("inventoryItemCode"));
        assertEquals("Strawberries", jsonObject.getString("inventoryItemDesc"));
        assertEquals("5560", jsonObject.getString("rollupInventoryItemCode"));
        assertEquals("Alfalfa Dehy", jsonObject.getString("rollupInventoryItemDesc"));
        assertEquals("null", jsonObject.getString("userEmail"));
    }

    @Test
    @Order(5)
    public void testDeleteInventoryItemAttribute() throws Exception {
        Response response = target("/inventoryItemAttributes/3961").request().delete();
        assertEquals(204, response.getStatus());
    }
}
