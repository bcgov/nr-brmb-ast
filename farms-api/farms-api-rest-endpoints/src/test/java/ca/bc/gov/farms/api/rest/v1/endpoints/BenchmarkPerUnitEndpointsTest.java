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
import ca.bc.gov.farms.api.rest.v1.endpoints.impl.BenchmarkPerUnitEndpointsImpl;
import ca.bc.gov.farms.api.rest.v1.resource.BenchmarkPerUnitRsrc;
import ca.bc.gov.farms.api.rest.v1.spring.EndpointsSpringConfigTest;
import ca.bc.gov.farms.api.rest.v1.spring.ResourceFactorySpringConfig;
import ca.bc.gov.farms.service.api.v1.spring.ServiceApiSpringConfig;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.json.JSONObject;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BenchmarkPerUnitEndpointsTest extends JerseyTest {

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
        config.register(BenchmarkPerUnitEndpointsImpl.class);
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
    public void testCreateBenchmarkPerUnit() throws Exception {
        BenchmarkPerUnitRsrc benchmarkPerUnit = new BenchmarkPerUnitRsrc();
        benchmarkPerUnit.setProgramYear(2024);
        benchmarkPerUnit.setUnitComment("Alfalfa Dehy");
        benchmarkPerUnit.setExpiryDate(null);
        benchmarkPerUnit.setMunicipalityCode("41");
        benchmarkPerUnit.setInventoryCode("5560");
        benchmarkPerUnit.setYearMinus6Margin(new BigDecimal("106.43"));
        benchmarkPerUnit.setYearMinus5Margin(new BigDecimal("128.79"));
        benchmarkPerUnit.setYearMinus4Margin(new BigDecimal("127.41"));
        benchmarkPerUnit.setYearMinus3Margin(new BigDecimal("109.64"));
        benchmarkPerUnit.setYearMinus2Margin(new BigDecimal("95.13"));
        benchmarkPerUnit.setYearMinus1Margin(new BigDecimal("0.00"));
        benchmarkPerUnit.setYearMinus6Expense(new BigDecimal("151.44"));
        benchmarkPerUnit.setYearMinus5Expense(new BigDecimal("156.59"));
        benchmarkPerUnit.setYearMinus4Expense(new BigDecimal("140.79"));
        benchmarkPerUnit.setYearMinus3Expense(new BigDecimal("186.58"));
        benchmarkPerUnit.setYearMinus2Expense(new BigDecimal("258.28"));
        benchmarkPerUnit.setYearMinus1Expense(new BigDecimal("258.28"));
        benchmarkPerUnit.setUserEmail("jsmith@gmail.com");

        Response response = target("/benchmarkPerUnits").request().post(Entity.json(benchmarkPerUnit));
        assertEquals(201, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);
        assertEquals("BenchmarkPerUnitRsrc", jsonObject.getString("@type"));
        assertEquals(60584, jsonObject.getInt("benchmarkPerUnitId"));
        assertEquals(2024, jsonObject.getInt("programYear"));
        assertEquals("Alfalfa Dehy", jsonObject.getString("unitComment"));
        assertEquals("null", jsonObject.getString("expiryDate"));
        assertEquals("41", jsonObject.getString("municipalityCode"));
        assertEquals("Cariboo", jsonObject.getString("municipalityDesc"));
        assertEquals("5560", jsonObject.getString("inventoryItemCode"));
        assertEquals("Alfalfa Dehy", jsonObject.getString("inventoryItemDesc"));
        assertEquals("null", jsonObject.getString("structureGroupCode"));
        assertEquals("null", jsonObject.getString("structureGroupDesc"));
        assertEquals("5560", jsonObject.getString("inventoryCode"));
        assertEquals("Alfalfa Dehy", jsonObject.getString("inventoryDesc"));
        assertEquals(106.43, jsonObject.getDouble("yearMinus6Margin"));
        assertEquals(128.79, jsonObject.getDouble("yearMinus5Margin"));
        assertEquals(127.41, jsonObject.getDouble("yearMinus4Margin"));
        assertEquals(109.64, jsonObject.getDouble("yearMinus3Margin"));
        assertEquals(95.13, jsonObject.getDouble("yearMinus2Margin"));
        assertEquals(0.00, jsonObject.getDouble("yearMinus1Margin"));
        assertEquals(151.44, jsonObject.getDouble("yearMinus6Expense"));
        assertEquals(156.59, jsonObject.getDouble("yearMinus5Expense"));
        assertEquals(140.79, jsonObject.getDouble("yearMinus4Expense"));
        assertEquals(186.58, jsonObject.getDouble("yearMinus3Expense"));
        assertEquals(258.28, jsonObject.getDouble("yearMinus2Expense"));
        assertEquals(258.28, jsonObject.getDouble("yearMinus1Expense"));
        assertEquals("null", jsonObject.getString("userEmail"));
    }
}
