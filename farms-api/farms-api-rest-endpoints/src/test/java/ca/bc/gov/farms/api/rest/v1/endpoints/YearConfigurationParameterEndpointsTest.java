package ca.bc.gov.farms.api.rest.v1.endpoints;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import ca.bc.gov.farms.api.rest.v1.endpoints.impl.YearConfigurationParameterEndpointsImpl;
import ca.bc.gov.farms.api.rest.v1.resource.YearConfigurationParameterRsrc;
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
public class YearConfigurationParameterEndpointsTest extends JerseyTest {

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
        config.register(YearConfigurationParameterEndpointsImpl.class);
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
    public void testCreateYearConfigurationParameter() throws Exception {
        YearConfigurationParameterRsrc yearConfigurationParameter = new YearConfigurationParameterRsrc();
        yearConfigurationParameter.setProgramYear(2023);
        yearConfigurationParameter.setParameterName("Payment Limitation - Percentage of Total Margin Decline");
        yearConfigurationParameter.setParameterValue("70");
        yearConfigurationParameter.setConfigParamTypeCode("DECIMAL");
        yearConfigurationParameter.setUserEmail("jsmith@gmail.com");

        Response response = target("/yearConfigurationParameters").request()
                .post(Entity.json(yearConfigurationParameter));
        assertEquals(201, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);
        assertEquals("YearConfigurationParameterRsrc", jsonObject.getString("@type"));
        assertEquals(241, jsonObject.getInt("yearConfigurationParameterId"));
        assertEquals(2023, jsonObject.getInt("programYear"));
        assertEquals("Payment Limitation - Percentage of Total Margin Decline", jsonObject.getString("parameterName"));
        assertEquals("70", jsonObject.getString("parameterValue"));
        assertEquals("DECIMAL", jsonObject.getString("configParamTypeCode"));
        assertEquals("null", jsonObject.getString("userEmail"));
    }

    @Test
    @Order(2)
    public void testGetAllYearConfigurationParameters() throws Exception {
        Response response = target("/yearConfigurationParameters").request().get();
        assertEquals(200, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);

        assertEquals("YearConfigurationParameterListRsrc", jsonObject.getString("@type"));

        JSONArray yearConfigurationParameterList = jsonObject.getJSONArray("yearConfigurationParameterList");
        JSONObject yearConfigurationParameter = yearConfigurationParameterList.getJSONObject(0);
        assertEquals("YearConfigurationParameterRsrc", yearConfigurationParameter.getString("@type"));
        assertEquals(241, yearConfigurationParameter.getInt("yearConfigurationParameterId"));
        assertEquals(2023, yearConfigurationParameter.getInt("programYear"));
        assertEquals("Payment Limitation - Percentage of Total Margin Decline",
                yearConfigurationParameter.getString("parameterName"));
        assertEquals("70", yearConfigurationParameter.getString("parameterValue"));
        assertEquals("DECIMAL", yearConfigurationParameter.getString("configParamTypeCode"));
        assertEquals("null", yearConfigurationParameter.getString("userEmail"));
    }

    @Test
    @Order(3)
    public void testGetYearConfigurationParameter() throws Exception {
        Response response = target("/yearConfigurationParameters/241").request().get();
        assertEquals(200, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);
        assertEquals("YearConfigurationParameterRsrc", jsonObject.getString("@type"));
        assertEquals(241, jsonObject.getInt("yearConfigurationParameterId"));
        assertEquals(2023, jsonObject.getInt("programYear"));
        assertEquals("Payment Limitation - Percentage of Total Margin Decline", jsonObject.getString("parameterName"));
        assertEquals("70", jsonObject.getString("parameterValue"));
        assertEquals("DECIMAL", jsonObject.getString("configParamTypeCode"));
        assertEquals("null", jsonObject.getString("userEmail"));
    }

    @Test
    @Order(4)
    public void testUpdateYearConfigurationParameter() throws Exception {
        YearConfigurationParameterRsrc yearConfigurationParameter = new YearConfigurationParameterRsrc();
        yearConfigurationParameter.setYearConfigurationParameterId(241L);
        yearConfigurationParameter.setProgramYear(2023);
        yearConfigurationParameter.setParameterName("Payment Limitation - Percentage of Total Margin Decline");
        yearConfigurationParameter.setParameterValue("700");
        yearConfigurationParameter.setConfigParamTypeCode("DECIMAL");
        yearConfigurationParameter.setUserEmail("jsmith@gmail.com");

        Response response = target("/yearConfigurationParameters/241").request()
                .put(Entity.json(yearConfigurationParameter));
        assertEquals(200, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);
        assertEquals("YearConfigurationParameterRsrc", jsonObject.getString("@type"));
        assertEquals(241, jsonObject.getInt("yearConfigurationParameterId"));
        assertEquals(2023, jsonObject.getInt("programYear"));
        assertEquals("Payment Limitation - Percentage of Total Margin Decline", jsonObject.getString("parameterName"));
        assertEquals("700", jsonObject.getString("parameterValue"));
        assertEquals("DECIMAL", jsonObject.getString("configParamTypeCode"));
        assertEquals("null", jsonObject.getString("userEmail"));
    }

    @Test
    @Order(5)
    public void testDeleteYearConfigurationParameter() throws Exception {
        Response response = target("/yearConfigurationParameters/241").request().delete();
        assertEquals(204, response.getStatus());
    }
}
