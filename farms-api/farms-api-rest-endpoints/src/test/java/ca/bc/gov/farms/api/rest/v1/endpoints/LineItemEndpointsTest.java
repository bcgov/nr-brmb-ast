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
import ca.bc.gov.farms.api.rest.v1.endpoints.impl.LineItemEndpointsImpl;
import ca.bc.gov.farms.api.rest.v1.resource.LineItemRsrc;
import ca.bc.gov.farms.api.rest.v1.spring.EndpointsSpringConfigTest;
import ca.bc.gov.farms.api.rest.v1.spring.ResourceFactorySpringConfig;
import ca.bc.gov.farms.service.api.v1.spring.ServiceApiSpringConfig;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.json.JSONObject;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LineItemEndpointsTest extends JerseyTest {

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
        config.register(LineItemEndpointsImpl.class);
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
    public void testCreateLineItem() throws Exception {
        LineItemRsrc lineItem = new LineItemRsrc();
        lineItem.setProgramYear(2025);
        lineItem.setLineItem(9798);
        lineItem.setDescription("Agricultural Contract work");
        lineItem.setProvince("BC");
        lineItem.setEligibilityInd("N");
        lineItem.setEligibilityForRefYearsInd("N");
        lineItem.setYardageInd("N");
        lineItem.setProgramPaymentInd("N");
        lineItem.setContractWorkInd("N");
        lineItem.setSupplyManagedCommodityInd("N");
        lineItem.setExcludeFromRevenueCalcInd("N");
        lineItem.setIndustryAverageExpenseInd("N");
        lineItem.setCommodityTypeCode(null);
        lineItem.setFruitVegTypeCode(null);
        lineItem.setUserEmail("jsmith@gmail.com");

        Response response = target("/lineItems").request().post(Entity.json(lineItem));
        assertEquals(201, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);
        assertEquals("LineItemRsrc", jsonObject.getString("@type"));
        assertEquals(39909, jsonObject.getInt("lineItemId"));
        assertEquals(2025, jsonObject.getInt("programYear"));
        assertEquals(9798, jsonObject.getInt("lineItem"));
        assertEquals("Agricultural Contract work", jsonObject.getString("description"));
        assertEquals("BC", jsonObject.getString("province"));
        assertEquals("N", jsonObject.getString("eligibilityInd"));
        assertEquals("N", jsonObject.getString("eligibilityForRefYearsInd"));
        assertEquals("N", jsonObject.getString("yardageInd"));
        assertEquals("N", jsonObject.getString("programPaymentInd"));
        assertEquals("N", jsonObject.getString("contractWorkInd"));
        assertEquals("N", jsonObject.getString("supplyManagedCommodityInd"));
        assertEquals("N", jsonObject.getString("excludeFromRevenueCalcInd"));
        assertEquals("N", jsonObject.getString("industryAverageExpenseInd"));
        assertEquals("null", jsonObject.getString("commodityTypeCode"));
        assertEquals("null", jsonObject.getString("fruitVegTypeCode"));
        assertEquals("null", jsonObject.getString("userEmail"));
    }
}
