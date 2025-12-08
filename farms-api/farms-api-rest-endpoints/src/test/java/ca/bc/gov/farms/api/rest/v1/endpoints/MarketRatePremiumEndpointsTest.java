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
import ca.bc.gov.farms.api.rest.v1.endpoints.impl.MarketRatePremiumEndpointsImpl;
import ca.bc.gov.farms.api.rest.v1.resource.MarketRatePremiumRsrc;
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
public class MarketRatePremiumEndpointsTest extends JerseyTest {

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
        config.register(MarketRatePremiumEndpointsImpl.class);
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
    public void testCreateMarketRatePremium() throws Exception {
        MarketRatePremiumRsrc marketRatePremium = new MarketRatePremiumRsrc();
        marketRatePremium.setMinTotalPremiumAmount(new BigDecimal("0.00"));
        marketRatePremium.setMaxTotalPremiumAmount(new BigDecimal("1.00"));
        marketRatePremium.setRiskChargeFlatAmount(new BigDecimal("2.00"));
        marketRatePremium.setRiskChargePctPremium(new BigDecimal("3.00"));
        marketRatePremium.setAdjustChargeFlatAmount(new BigDecimal("4.00"));
        marketRatePremium.setUserEmail("jsmith@gmail.com");

        Response response = target("/marketRatePremiums").request().post(Entity.json(marketRatePremium));
        assertEquals(201, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);
        assertEquals("MarketRatePremiumRsrc", jsonObject.getString("@type"));
        assertEquals(21, jsonObject.getInt("marketRatePremiumId"));
        assertEquals(0.00, jsonObject.getDouble("minTotalPremiumAmount"));
        assertEquals(1.00, jsonObject.getDouble("maxTotalPremiumAmount"));
        assertEquals(2.00, jsonObject.getDouble("riskChargeFlatAmount"));
        assertEquals(3.00, jsonObject.getDouble("riskChargePctPremium"));
        assertEquals(4.00, jsonObject.getDouble("adjustChargeFlatAmount"));
        assertEquals("null", jsonObject.getString("userEmail"));
    }

    @Test
    @Order(2)
    public void testGetAllMarketRatePremiums() throws Exception {
        Response response = target("/marketRatePremiums").request().get();
        assertEquals(200, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);

        assertEquals("MarketRatePremiumListRsrc", jsonObject.getString("@type"));

        JSONArray marketRatePremiumList = jsonObject.getJSONArray("marketRatePremiumList");
        JSONObject marketRatePremium = marketRatePremiumList.getJSONObject(0);
        assertEquals("MarketRatePremiumRsrc", marketRatePremium.getString("@type"));
        assertEquals(21, marketRatePremium.getInt("marketRatePremiumId"));
        assertEquals(0.00, marketRatePremium.getDouble("minTotalPremiumAmount"));
        assertEquals(1.00, marketRatePremium.getDouble("maxTotalPremiumAmount"));
        assertEquals(2.00, marketRatePremium.getDouble("riskChargeFlatAmount"));
        assertEquals(3.00, marketRatePremium.getDouble("riskChargePctPremium"));
        assertEquals(4.00, marketRatePremium.getDouble("adjustChargeFlatAmount"));
        assertEquals("null", marketRatePremium.getString("userEmail"));
    }

    @Test
    @Order(3)
    public void testGetMarketRatePremium() throws Exception {
        Response response = target("/marketRatePremiums/21").request().get();
        assertEquals(200, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);
        assertEquals("MarketRatePremiumRsrc", jsonObject.getString("@type"));
        assertEquals(21, jsonObject.getInt("marketRatePremiumId"));
        assertEquals(0.00, jsonObject.getDouble("minTotalPremiumAmount"));
        assertEquals(1.00, jsonObject.getDouble("maxTotalPremiumAmount"));
        assertEquals(2.00, jsonObject.getDouble("riskChargeFlatAmount"));
        assertEquals(3.00, jsonObject.getDouble("riskChargePctPremium"));
        assertEquals(4.00, jsonObject.getDouble("adjustChargeFlatAmount"));
        assertEquals("null", jsonObject.getString("userEmail"));
    }

    @Test
    @Order(4)
    public void testUpdateMarketRatePremium() throws Exception {
        MarketRatePremiumRsrc marketRatePremium = new MarketRatePremiumRsrc();
        marketRatePremium.setMarketRatePremiumId(21L);
        marketRatePremium.setMinTotalPremiumAmount(new BigDecimal("1.00"));
        marketRatePremium.setMaxTotalPremiumAmount(new BigDecimal("2.00"));
        marketRatePremium.setRiskChargeFlatAmount(new BigDecimal("3.00"));
        marketRatePremium.setRiskChargePctPremium(new BigDecimal("4.00"));
        marketRatePremium.setAdjustChargeFlatAmount(new BigDecimal("5.00"));
        marketRatePremium.setUserEmail("jsmith@gmail.com");

        Response response = target("/marketRatePremiums/21").request().put(Entity.json(marketRatePremium));
        assertEquals(200, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);
        assertEquals("MarketRatePremiumRsrc", jsonObject.getString("@type"));
        assertEquals(21, jsonObject.getInt("marketRatePremiumId"));
        assertEquals(1.00, jsonObject.getDouble("minTotalPremiumAmount"));
        assertEquals(2.00, jsonObject.getDouble("maxTotalPremiumAmount"));
        assertEquals(3.00, jsonObject.getDouble("riskChargeFlatAmount"));
        assertEquals(4.00, jsonObject.getDouble("riskChargePctPremium"));
        assertEquals(5.00, jsonObject.getDouble("adjustChargeFlatAmount"));
        assertEquals("null", jsonObject.getString("userEmail"));
    }

    @Test
    @Order(5)
    public void testDeleteMarketRatePremium() throws Exception {
        Response response = target("/marketRatePremiums/21").request().delete();
        assertEquals(204, response.getStatus());
    }
}
