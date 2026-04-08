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
import ca.bc.gov.farms.api.rest.v1.endpoints.impl.InventoryItemDetailEndpointsImpl;
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
public class InventoryItemDetailEndpointsTest extends JerseyTest {

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
        config.register(InventoryItemDetailEndpointsImpl.class);
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
    public void testCreateInventoryItemDetail() throws Exception {
        InventoryItemDetailRsrc inventoryItemDetail = new InventoryItemDetailRsrc();
        inventoryItemDetail.setProgramYear(2025);
        inventoryItemDetail.setEligibilityInd("Y");
        inventoryItemDetail.setLineItem(null);
        inventoryItemDetail.setInsurableValue(new BigDecimal("0.112"));
        inventoryItemDetail.setPremiumRate(new BigDecimal("0.1200"));
        inventoryItemDetail.setInventoryItemCode("73");
        inventoryItemDetail.setCommodityTypeCode("GRAIN");
        inventoryItemDetail.setFruitVegTypeCode("APPLE");
        inventoryItemDetail.setMultiStageCommdtyCode(null);
        inventoryItemDetail.setUrlId(1L);
        inventoryItemDetail.setUserEmail("jsmith@gmail.com");

        Response response = target("/inventoryItemDetails").request().post(Entity.json(inventoryItemDetail));
        assertEquals(201, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);
        assertEquals("InventoryItemDetailRsrc", jsonObject.getString("@type"));
        assertEquals(55361, jsonObject.getInt("inventoryItemDetailId"));
        assertEquals(2025, jsonObject.getInt("programYear"));
        assertEquals("Y", jsonObject.getString("eligibilityInd"));
        assertEquals("null", jsonObject.getString("lineItem"));
        assertEquals(0.112, jsonObject.getDouble("insurableValue"));
        assertEquals(0.1200, jsonObject.getDouble("premiumRate"));
        assertEquals("73", jsonObject.getString("inventoryItemCode"));
        assertEquals("Strawberries", jsonObject.getString("inventoryItemDesc"));
        assertEquals("GRAIN", jsonObject.getString("commodityTypeCode"));
        assertEquals("Grain", jsonObject.getString("commodityTypeDesc"));
        assertEquals("APPLE", jsonObject.getString("fruitVegTypeCode"));
        assertEquals("Apples", jsonObject.getString("fruitVegTypeDesc"));
        assertEquals("null", jsonObject.getString("multiStageCommdtyCode"));
        assertEquals("null", jsonObject.getString("multiStageCommdtyDesc"));
        assertEquals(1, jsonObject.getInt("urlId"));
        assertEquals("https://google.com", jsonObject.getString("url"));
        assertEquals("null", jsonObject.getString("userEmail"));
    }

    @Test
    @Order(2)
    public void testGetInventoryItemDetailsByInventoryItemCode() throws Exception {
        Response response = target("/inventoryItemDetails").queryParam("inventoryItemCode", "73").request().get();
        assertEquals(200, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);

        assertEquals("InventoryItemDetailListRsrc", jsonObject.getString("@type"));

        JSONArray inventoryItemDetailList = jsonObject.getJSONArray("inventoryItemDetailList");
        JSONObject inventoryItemDetail = inventoryItemDetailList.getJSONObject(0);

        assertEquals("InventoryItemDetailRsrc", inventoryItemDetail.getString("@type"));
        assertEquals(55361, inventoryItemDetail.getInt("inventoryItemDetailId"));
        assertEquals(2025, inventoryItemDetail.getInt("programYear"));
        assertEquals("Y", inventoryItemDetail.getString("eligibilityInd"));
        assertEquals("null", inventoryItemDetail.getString("lineItem"));
        assertEquals(0.112, inventoryItemDetail.getDouble("insurableValue"));
        assertEquals(0.1200, inventoryItemDetail.getDouble("premiumRate"));
        assertEquals("73", inventoryItemDetail.getString("inventoryItemCode"));
        assertEquals("Strawberries", inventoryItemDetail.getString("inventoryItemDesc"));
        assertEquals("GRAIN", inventoryItemDetail.getString("commodityTypeCode"));
        assertEquals("Grain", inventoryItemDetail.getString("commodityTypeDesc"));
        assertEquals("APPLE", inventoryItemDetail.getString("fruitVegTypeCode"));
        assertEquals("Apples", inventoryItemDetail.getString("fruitVegTypeDesc"));
        assertEquals("null", inventoryItemDetail.getString("multiStageCommdtyCode"));
        assertEquals("null", inventoryItemDetail.getString("multiStageCommdtyDesc"));
        assertEquals(1, inventoryItemDetail.getInt("urlId"));
        assertEquals("https://google.com", inventoryItemDetail.getString("url"));
        assertEquals("null", inventoryItemDetail.getString("userEmail"));
    }

    @Test
    @Order(3)
    public void testGetInventoryItemDetail() throws Exception {
        Response response = target("/inventoryItemDetails/55361").request().get();
        assertEquals(200, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);

        assertEquals("InventoryItemDetailRsrc", jsonObject.getString("@type"));
        assertEquals(55361, jsonObject.getInt("inventoryItemDetailId"));
        assertEquals(2025, jsonObject.getInt("programYear"));
        assertEquals("Y", jsonObject.getString("eligibilityInd"));
        assertEquals("null", jsonObject.getString("lineItem"));
        assertEquals(0.112, jsonObject.getDouble("insurableValue"));
        assertEquals(0.1200, jsonObject.getDouble("premiumRate"));
        assertEquals("73", jsonObject.getString("inventoryItemCode"));
        assertEquals("Strawberries", jsonObject.getString("inventoryItemDesc"));
        assertEquals("GRAIN", jsonObject.getString("commodityTypeCode"));
        assertEquals("Grain", jsonObject.getString("commodityTypeDesc"));
        assertEquals("APPLE", jsonObject.getString("fruitVegTypeCode"));
        assertEquals("Apples", jsonObject.getString("fruitVegTypeDesc"));
        assertEquals("null", jsonObject.getString("multiStageCommdtyCode"));
        assertEquals("null", jsonObject.getString("multiStageCommdtyDesc"));
        assertEquals(1, jsonObject.getInt("urlId"));
        assertEquals("https://google.com", jsonObject.getString("url"));
        assertEquals("null", jsonObject.getString("userEmail"));
    }

    @Test
    @Order(4)
    public void testUpdateInventoryItemDetail() throws Exception {
        InventoryItemDetailRsrc inventoryItemDetail = new InventoryItemDetailRsrc();
        inventoryItemDetail.setInventoryItemDetailId(55361L);
        inventoryItemDetail.setProgramYear(2026);
        inventoryItemDetail.setEligibilityInd("N");
        inventoryItemDetail.setLineItem(null);
        inventoryItemDetail.setInsurableValue(new BigDecimal("0.212"));
        inventoryItemDetail.setPremiumRate(new BigDecimal("0.2200"));
        inventoryItemDetail.setInventoryItemCode("5560");
        inventoryItemDetail.setCommodityTypeCode("FORAGE");
        inventoryItemDetail.setFruitVegTypeCode("APRICOT");
        inventoryItemDetail.setMultiStageCommdtyCode(null);
        inventoryItemDetail.setUrlId(2L);
        inventoryItemDetail.setUserEmail("jsmith@gmail.com");

        Response response = target("/inventoryItemDetails/55361").request().put(Entity.json(inventoryItemDetail));
        assertEquals(200, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);

        assertEquals("InventoryItemDetailRsrc", jsonObject.getString("@type"));
        assertEquals(55361, jsonObject.getInt("inventoryItemDetailId"));
        assertEquals(2026, jsonObject.getInt("programYear"));
        assertEquals("N", jsonObject.getString("eligibilityInd"));
        assertEquals("null", jsonObject.getString("lineItem"));
        assertEquals(0.212, jsonObject.getDouble("insurableValue"));
        assertEquals(0.2200, jsonObject.getDouble("premiumRate"));
        assertEquals("5560", jsonObject.getString("inventoryItemCode"));
        assertEquals("Alfalfa Dehy", jsonObject.getString("inventoryItemDesc"));
        assertEquals("FORAGE", jsonObject.getString("commodityTypeCode"));
        assertEquals("Forage", jsonObject.getString("commodityTypeDesc"));
        assertEquals("APRICOT", jsonObject.getString("fruitVegTypeCode"));
        assertEquals("Apricots", jsonObject.getString("fruitVegTypeDesc"));
        assertEquals("null", jsonObject.getString("multiStageCommdtyCode"));
        assertEquals("null", jsonObject.getString("multiStageCommdtyDesc"));
        assertEquals(2, jsonObject.getInt("urlId"));
        assertEquals("https://microsoft.com", jsonObject.getString("url"));
        assertEquals("null", jsonObject.getString("userEmail"));
    }

    @Test
    @Order(5)
    public void testDeleteInventoryItemDetail() throws Exception {
        Response response = target("/inventoryItemDetails/55361").request().delete();
        assertEquals(204, response.getStatus());
    }
}
