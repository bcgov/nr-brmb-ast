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
import ca.bc.gov.farms.api.rest.v1.endpoints.impl.StructureGroupAttributeEndpointsImpl;
import ca.bc.gov.farms.api.rest.v1.resource.StructureGroupAttributeRsrc;
import ca.bc.gov.farms.api.rest.v1.spring.EndpointsSpringConfigTest;
import ca.bc.gov.farms.api.rest.v1.spring.ResourceFactorySpringConfig;
import ca.bc.gov.farms.service.api.v1.spring.ServiceApiSpringConfig;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.json.JSONObject;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StructureGroupAttributeEndpointsTest extends JerseyTest {

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
        config.register(StructureGroupAttributeEndpointsImpl.class);
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
    public void testCreateStructureGroupAttribute() throws Exception {
        StructureGroupAttributeRsrc structureGroupAttribute = new StructureGroupAttributeRsrc();
        structureGroupAttribute.setStructureGroupCode("100");
        structureGroupAttribute.setRollupStructureGroupCode("120");
        structureGroupAttribute.setUserEmail("jsmith@gmail.com");

        Response response = target("/structureGroupAttributes").request().post(Entity.json(structureGroupAttribute));
        assertEquals(201, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);
        assertEquals("StructureGroupAttributeRsrc", jsonObject.getString("@type"));
        assertEquals(21, jsonObject.getInt("structureGroupAttributeId"));
        assertEquals("100", jsonObject.getString("structureGroupCode"));
        assertEquals("Alpaca", jsonObject.getString("structureGroupDesc"));
        assertEquals("120", jsonObject.getString("rollupStructureGroupCode"));
        assertEquals("Other Livestock", jsonObject.getString("rollupStructureGroupDesc"));
        assertEquals("null", jsonObject.getString("userEmail"));
    }

    @Test
    @Order(2)
    public void testGetStructureGroupAttributesByStructureGroupCode() throws Exception {
        Response response = target("/structureGroupAttributes").queryParam("structureGroupCode", "100").request().get();
        assertEquals(200, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);
        assertEquals("StructureGroupAttributeRsrc", jsonObject.getString("@type"));
        assertEquals(21, jsonObject.getInt("structureGroupAttributeId"));
        assertEquals("100", jsonObject.getString("structureGroupCode"));
        assertEquals("Alpaca", jsonObject.getString("structureGroupDesc"));
        assertEquals("120", jsonObject.getString("rollupStructureGroupCode"));
        assertEquals("Other Livestock", jsonObject.getString("rollupStructureGroupDesc"));
        assertEquals("null", jsonObject.getString("userEmail"));
    }

    @Test
    @Order(3)
    public void testGetStructureGroupAttribute() throws Exception {
        Response response = target("/structureGroupAttributes/21").request().get();
        assertEquals(200, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);
        assertEquals("StructureGroupAttributeRsrc", jsonObject.getString("@type"));
        assertEquals(21, jsonObject.getInt("structureGroupAttributeId"));
        assertEquals("100", jsonObject.getString("structureGroupCode"));
        assertEquals("Alpaca", jsonObject.getString("structureGroupDesc"));
        assertEquals("120", jsonObject.getString("rollupStructureGroupCode"));
        assertEquals("Other Livestock", jsonObject.getString("rollupStructureGroupDesc"));
        assertEquals("null", jsonObject.getString("userEmail"));
    }

    @Test
    @Order(4)
    public void testUpdateStructureGroupAttribute() throws Exception {
        StructureGroupAttributeRsrc structureGroupAttribute = new StructureGroupAttributeRsrc();
        structureGroupAttribute.setStructureGroupAttributeId(21L);
        structureGroupAttribute.setStructureGroupCode("100");
        structureGroupAttribute.setRollupStructureGroupCode("300");
        structureGroupAttribute.setUserEmail("jsmith@gmail.com");

        Response response = target("/structureGroupAttributes/21").request().put(Entity.json(structureGroupAttribute));
        assertEquals(200, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);
        assertEquals("StructureGroupAttributeRsrc", jsonObject.getString("@type"));
        assertEquals(21, jsonObject.getInt("structureGroupAttributeId"));
        assertEquals("100", jsonObject.getString("structureGroupCode"));
        assertEquals("Alpaca", jsonObject.getString("structureGroupDesc"));
        assertEquals("300", jsonObject.getString("rollupStructureGroupCode"));
        assertEquals("Bovine", jsonObject.getString("rollupStructureGroupDesc"));
        assertEquals("null", jsonObject.getString("userEmail"));
    }
}
