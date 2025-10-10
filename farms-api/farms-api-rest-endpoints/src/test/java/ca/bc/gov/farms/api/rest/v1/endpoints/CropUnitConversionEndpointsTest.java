package ca.bc.gov.farms.api.rest.v1.endpoints;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
import ca.bc.gov.farms.api.rest.v1.endpoints.impl.CropUnitConversionEndpointsImpl;
import ca.bc.gov.farms.api.rest.v1.resource.ConversionUnitRsrc;
import ca.bc.gov.farms.api.rest.v1.resource.CropUnitConversionRsrc;
import ca.bc.gov.farms.api.rest.v1.spring.EndpointsSpringConfigTest;
import ca.bc.gov.farms.api.rest.v1.spring.ResourceFactorySpringConfig;
import ca.bc.gov.farms.model.v1.ConversionUnit;
import ca.bc.gov.farms.service.api.v1.spring.ServiceApiSpringConfig;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.json.JSONArray;
import org.json.JSONObject;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CropUnitConversionEndpointsTest extends JerseyTest {

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
        config.register(CropUnitConversionEndpointsImpl.class);
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
    public void testCreateCropUnitConversion() throws Exception {
        CropUnitConversionRsrc cropUnitConversion = new CropUnitConversionRsrc();
        cropUnitConversion.setInventoryItemCode("5560");
        cropUnitConversion.setCropUnitCode("2");
        List<ConversionUnit> conversionUnits = new ArrayList<>();
        ConversionUnit conversionUnit = new ConversionUnitRsrc();
        conversionUnit.setConversionFactor(new BigDecimal("2204.622600"));
        conversionUnit.setTargetCropUnitCode("1");
        conversionUnits.add(conversionUnit);
        cropUnitConversion.setConversionUnits(conversionUnits);
        cropUnitConversion.setUserEmail("jsmith@gmail.com");

        Response response = target("/cropUnitConversions").request().post(Entity.json(cropUnitConversion));
        assertEquals(201, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);
        assertEquals("CropUnitConversionRsrc", jsonObject.getString("@type"));
        assertEquals(1681, jsonObject.getInt("cropUnitDefaultId"));
        assertEquals("5560", jsonObject.getString("inventoryItemCode"));
        assertEquals("Alfalfa Dehy", jsonObject.getString("inventoryItemDesc"));
        assertEquals("2", jsonObject.getString("cropUnitCode"));
        assertEquals("Tonnes", jsonObject.getString("cropUnitDesc"));

        JSONArray conversionUnitList = jsonObject.getJSONArray("conversionUnits");
        JSONObject conversionUnitJson = conversionUnitList.getJSONObject(0);

        assertEquals(1981, conversionUnitJson.getInt("cropUnitConversionFactorId"));
        assertEquals(2204.622600, conversionUnitJson.getDouble("conversionFactor"));
        assertEquals("1", conversionUnitJson.getString("targetCropUnitCode"));
        assertEquals("Pounds", conversionUnitJson.getString("targetCropUnitDesc"));
    }

    @Test
    @Order(2)
    public void testGetAllCropUnitConversions() throws Exception {
        Response response = target("/cropUnitConversions").request().get();
        assertEquals(200, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);

        assertEquals("CropUnitConversionListRsrc", jsonObject.getString("@type"));

        JSONArray cropUnitConversionList = jsonObject.getJSONArray("cropUnitConversionList");
        JSONObject cropUnitConversion = cropUnitConversionList.getJSONObject(0);
        assertEquals("CropUnitConversionRsrc", cropUnitConversion.getString("@type"));
        assertEquals(1681, cropUnitConversion.getInt("cropUnitDefaultId"));
        assertEquals("5560", cropUnitConversion.getString("inventoryItemCode"));
        assertEquals("Alfalfa Dehy", cropUnitConversion.getString("inventoryItemDesc"));
        assertEquals("2", cropUnitConversion.getString("cropUnitCode"));
        assertEquals("Tonnes", cropUnitConversion.getString("cropUnitDesc"));

        JSONArray conversionUnitList = cropUnitConversion.getJSONArray("conversionUnits");
        JSONObject conversionUnitJson = conversionUnitList.getJSONObject(0);

        assertEquals(1981, conversionUnitJson.getInt("cropUnitConversionFactorId"));
        assertEquals(2204.622600, conversionUnitJson.getDouble("conversionFactor"));
        assertEquals("1", conversionUnitJson.getString("targetCropUnitCode"));
        assertEquals("Pounds", conversionUnitJson.getString("targetCropUnitDesc"));
    }
}
