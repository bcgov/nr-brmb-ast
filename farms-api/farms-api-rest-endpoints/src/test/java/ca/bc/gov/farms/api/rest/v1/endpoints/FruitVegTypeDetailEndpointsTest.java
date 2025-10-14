package ca.bc.gov.farms.api.rest.v1.endpoints;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import ca.bc.gov.farms.api.rest.v1.endpoints.impl.FruitVegTypeDetailEndpointsImpl;
import ca.bc.gov.farms.api.rest.v1.resource.FruitVegTypeDetailRsrc;
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
public class FruitVegTypeDetailEndpointsTest extends JerseyTest {

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
        config.register(FruitVegTypeDetailEndpointsImpl.class);
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
    public void testCreateFruitVegTypeDetail() throws Exception {
        FruitVegTypeDetailRsrc fruitVegTypeDetail = new FruitVegTypeDetailRsrc();
        fruitVegTypeDetail.setFruitVegTypeCode("LYCHEE");
        fruitVegTypeDetail.setFruitVegTypeDesc("Tropical Fruit");
        fruitVegTypeDetail.setRevenueVarianceLimit(new BigDecimal("20.000"));
        fruitVegTypeDetail.setUserEmail("jsmith@gmail.com");

        Response response = target("/fruitVegTypeDetails").request().post(Entity.json(fruitVegTypeDetail));
        assertEquals(201, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);
        assertEquals("LYCHEE", jsonObject.getString("fruitVegTypeCode"));
        assertEquals("Tropical Fruit", jsonObject.getString("fruitVegTypeDesc"));
        assertEquals(LocalDate.now().toString(), jsonObject.getString("establishedDate"));
        assertEquals("9999-12-31", jsonObject.getString("expiryDate"));
        assertEquals(20.000, jsonObject.getDouble("revenueVarianceLimit"));
    }

    @Test
    @Order(2)
    public void testGetAllFruitVegTypeDetails() throws Exception {
        Response response = target("/fruitVegTypeDetails").request().get();
        assertEquals(200, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);

        assertEquals("FruitVegTypeDetailListRsrc", jsonObject.getString("@type"));

        JSONArray fruitVegTypeDetailList = jsonObject.getJSONArray("fruitVegTypeDetailList");
        JSONObject fruitVegTypeDetail = fruitVegTypeDetailList.getJSONObject(0);
        assertEquals("FruitVegTypeDetailRsrc", fruitVegTypeDetail.getString("@type"));
        assertEquals("LYCHEE", fruitVegTypeDetail.getString("fruitVegTypeCode"));
        assertEquals("Tropical Fruit", fruitVegTypeDetail.getString("fruitVegTypeDesc"));
        assertEquals(LocalDate.now().toString(), fruitVegTypeDetail.getString("establishedDate"));
        assertEquals("9999-12-31", fruitVegTypeDetail.getString("expiryDate"));
        assertEquals(20.000, fruitVegTypeDetail.getDouble("revenueVarianceLimit"));
    }

    @Test
    @Order(3)
    public void testGetFruitVegTypeDetail() throws Exception {
        Response response = target("/fruitVegTypeDetails/LYCHEE").request().get();
        assertEquals(200, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);
        assertEquals("FruitVegTypeDetailRsrc", jsonObject.getString("@type"));
        assertEquals("LYCHEE", jsonObject.getString("fruitVegTypeCode"));
        assertEquals("Tropical Fruit", jsonObject.getString("fruitVegTypeDesc"));
        assertEquals(LocalDate.now().toString(), jsonObject.getString("establishedDate"));
        assertEquals("9999-12-31", jsonObject.getString("expiryDate"));
        assertEquals(20.000, jsonObject.getDouble("revenueVarianceLimit"));
    }

    @Test
    @Order(4)
    public void testUpdateFruitVegTypeDetail() throws Exception {
        FruitVegTypeDetailRsrc fruitVegTypeDetail = new FruitVegTypeDetailRsrc();
        fruitVegTypeDetail.setFruitVegTypeCode("LYCHEE");
        fruitVegTypeDetail.setFruitVegTypeDesc("King of Fruits");
        fruitVegTypeDetail.setRevenueVarianceLimit(new BigDecimal("30.000"));
        fruitVegTypeDetail.setUserEmail("jsmith@gmail.com");

        Response response = target("/fruitVegTypeDetails/LYCHEE").request().put(Entity.json(fruitVegTypeDetail));
        assertEquals(200, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);
        assertEquals("FruitVegTypeDetailRsrc", jsonObject.getString("@type"));
        assertEquals("LYCHEE", jsonObject.getString("fruitVegTypeCode"));
        assertEquals("King of Fruits", jsonObject.getString("fruitVegTypeDesc"));
        assertEquals(LocalDate.now().toString(), jsonObject.getString("establishedDate"));
        assertEquals("9999-12-31", jsonObject.getString("expiryDate"));
        assertEquals(30.000, jsonObject.getDouble("revenueVarianceLimit"));
    }

    @Test
    @Order(5)
    public void testDeleteFruitVegTypeDetail() throws Exception {
        Response response = target("/fruitVegTypeDetails/LYCHEE").request().delete();
        assertEquals(204, response.getStatus());
    }
}
