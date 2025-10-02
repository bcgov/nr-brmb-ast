package ca.bc.gov.farms.api.rest.v1.endpoints;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ca.bc.gov.farms.api.rest.v1.endpoints.impl.TopLevelEndpointsImpl;
import ca.bc.gov.farms.api.rest.v1.spring.EndpointsSpringConfigTest;
import ca.bc.gov.farms.persistence.v1.spring.PersistenceSpringConfig;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.json.JSONObject;

public class TopLevelEndpointsTest extends JerseyTest {

    @Override
    protected Application configure() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getHeader("X-Forwarded-Proto")).thenReturn("http");
        when(mockRequest.getHeader("X-Forwarded-Host")).thenReturn("localhost");
        when(mockRequest.getHeader("If-Match")).thenReturn("*");

        // Load Spring context manually
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(EndpointsSpringConfigTest.class,
                PersistenceSpringConfig.class);

        // Create Jersey ResourceConfig and integrate Spring context
        ResourceConfig config = new ResourceConfig();
        config.register(TopLevelEndpointsImpl.class);
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
    public void testGetTopLevel() throws Exception {
        Response response = target("/").request().get();
        assertEquals(200, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);
        assertEquals("http://farms.gov.bc.ca/v1/endpoints", jsonObject.getString("@type"));
    }
}
