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
import ca.bc.gov.farms.api.rest.v1.endpoints.impl.InventoryTypeXrefEndpointsImpl;
import ca.bc.gov.farms.api.rest.v1.resource.InventoryTypeXrefRsrc;
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
public class InventoryTypeXrefEndpointsTest extends JerseyTest {

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
        config.register(InventoryTypeXrefEndpointsImpl.class);
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
    public void testCreateInventoryTypeXref() throws Exception {
        InventoryTypeXrefRsrc inventoryTypeXref = new InventoryTypeXrefRsrc();
        inventoryTypeXref.setMarketCommodityInd("Y");
        inventoryTypeXref.setInventoryItemCode("73");
        inventoryTypeXref.setInventoryGroupCode("3");
        inventoryTypeXref.setInventoryClassCode("4");
        inventoryTypeXref.setUserEmail("jsmith@gmail.com");

        Response response = target("/inventoryTypeXrefs").request().post(Entity.json(inventoryTypeXref));
        assertEquals(201, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);
        assertEquals("InventoryTypeXrefRsrc", jsonObject.getString("@type"));
        assertEquals(233520, jsonObject.getInt("agristabilityCommodityXrefId"));
        assertEquals("Y", jsonObject.getString("marketCommodityInd"));
        assertEquals("73", jsonObject.getString("inventoryItemCode"));
        assertEquals("Strawberries", jsonObject.getString("inventoryItemDesc"));
        assertEquals("3", jsonObject.getString("inventoryGroupCode"));
        assertEquals("Berries", jsonObject.getString("inventoryGroupDesc"));
        assertEquals("4", jsonObject.getString("inventoryClassCode"));
        assertEquals("Deferred Income and Receivables", jsonObject.getString("inventoryClassDesc"));
        assertEquals("null", jsonObject.getString("userEmail"));
    }

    @Test
    @Order(2)
    public void testGetInventoryTypeXrefsByInventoryClassCode() throws Exception {
        Response response = target("/inventoryTypeXrefs").queryParam("inventoryClassCode", "4").request().get();
        assertEquals(200, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);

        assertEquals("InventoryTypeXrefListRsrc", jsonObject.getString("@type"));

        JSONArray inventoryTypeXrefList = jsonObject.getJSONArray("inventoryTypeXrefList");
        JSONObject inventoryTypeXref = inventoryTypeXrefList.getJSONObject(0);
        assertEquals("InventoryTypeXrefRsrc", inventoryTypeXref.getString("@type"));
        assertEquals(233520, inventoryTypeXref.getInt("agristabilityCommodityXrefId"));
        assertEquals("Y", inventoryTypeXref.getString("marketCommodityInd"));
        assertEquals("73", inventoryTypeXref.getString("inventoryItemCode"));
        assertEquals("Strawberries", inventoryTypeXref.getString("inventoryItemDesc"));
        assertEquals("3", inventoryTypeXref.getString("inventoryGroupCode"));
        assertEquals("Berries", inventoryTypeXref.getString("inventoryGroupDesc"));
        assertEquals("4", inventoryTypeXref.getString("inventoryClassCode"));
        assertEquals("Deferred Income and Receivables", inventoryTypeXref.getString("inventoryClassDesc"));
        assertEquals("null", inventoryTypeXref.getString("userEmail"));
    }

    @Test
    @Order(3)
    public void testGetInventoryTypeXref() throws Exception {
        Response response = target("/inventoryTypeXrefs/233520").request().get();
        assertEquals(200, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);
        assertEquals("InventoryTypeXrefRsrc", jsonObject.getString("@type"));
        assertEquals(233520, jsonObject.getInt("agristabilityCommodityXrefId"));
        assertEquals("Y", jsonObject.getString("marketCommodityInd"));
        assertEquals("73", jsonObject.getString("inventoryItemCode"));
        assertEquals("Strawberries", jsonObject.getString("inventoryItemDesc"));
        assertEquals("3", jsonObject.getString("inventoryGroupCode"));
        assertEquals("Berries", jsonObject.getString("inventoryGroupDesc"));
        assertEquals("4", jsonObject.getString("inventoryClassCode"));
        assertEquals("Deferred Income and Receivables", jsonObject.getString("inventoryClassDesc"));
        assertEquals("null", jsonObject.getString("userEmail"));
    }

    @Test
    @Order(4)
    public void testUpdateInventoryTypeXref() throws Exception {
        InventoryTypeXrefRsrc inventoryTypeXref = new InventoryTypeXrefRsrc();
        inventoryTypeXref.setAgristabilityCommodityXrefId(233520L);
        inventoryTypeXref.setMarketCommodityInd("N");
        inventoryTypeXref.setInventoryItemCode("5560");
        inventoryTypeXref.setInventoryGroupCode("4");
        inventoryTypeXref.setInventoryClassCode("5");
        inventoryTypeXref.setUserEmail("jsmith@gmail.com");

        Response response = target("/inventoryTypeXrefs/233520").request().put(Entity.json(inventoryTypeXref));
        assertEquals(200, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);
        assertEquals("InventoryTypeXrefRsrc", jsonObject.getString("@type"));
        assertEquals(233520, jsonObject.getInt("agristabilityCommodityXrefId"));
        assertEquals("N", jsonObject.getString("marketCommodityInd"));
        assertEquals("5560", jsonObject.getString("inventoryItemCode"));
        assertEquals("Alfalfa Dehy", jsonObject.getString("inventoryItemDesc"));
        assertEquals("4", jsonObject.getString("inventoryGroupCode"));
        assertEquals("Buckwheat", jsonObject.getString("inventoryGroupDesc"));
        assertEquals("5", jsonObject.getString("inventoryClassCode"));
        assertEquals("Accounts Payable", jsonObject.getString("inventoryClassDesc"));
        assertEquals("null", jsonObject.getString("userEmail"));
    }

    @Test
    @Order(5)
    public void testDeleteInventoryTypeXref() throws Exception {
        Response response = target("/inventoryTypeXrefs/233520").request().delete();
        assertEquals(204, response.getStatus());
    }
}
