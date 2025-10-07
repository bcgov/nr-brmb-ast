package ca.bc.gov.farms.api.rest.v1.endpoints;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ca.bc.gov.brmb.common.api.rest.code.endpoints.impl.CodeTableEndpointsImpl;
import ca.bc.gov.brmb.common.api.rest.code.endpoints.spring.CodeEndpointsSpringConfig;
import ca.bc.gov.farms.api.rest.v1.spring.EndpointsSpringConfigTest;
import ca.bc.gov.farms.api.rest.v1.spring.ResourceFactorySpringConfig;
import ca.bc.gov.farms.service.api.v1.spring.ServiceApiSpringConfig;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.json.JSONObject;

public class CodeTableEndpointsTest extends JerseyTest {

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
        config.register(CodeTableEndpointsImpl.class);
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

    @ParameterizedTest
    @CsvSource({
            "'farm_chef_form_type_codes', 'farm_chef_form_type_codes'",
            "'farm_chef_submssn_status_codes', 'farm_chef_submssn_status_codes'",
            "'farm_commodity_type_codes', 'farm_commodity_type_codes'",
            "'farm_config_param_type_codes', 'farm_config_param_type_codes'",
            "'farm_crm_entity_type_codes', 'farm_crm_entity_type_codes'",
            "'farm_crop_unit_codes', 'farm_crop_unit_codes'",
            "'farm_enrolment_calc_type_codes', 'farm_enrolment_calc_type_codes'",
            "'farm_farm_type_codes', 'Farm Type Codes'",
            "'farm_federal_accounting_codes', 'Federal Accounting'",
            "'farm_federal_status_codes', 'Federal Status'",
            "'farm_fruit_veg_type_codes', 'farm_fruit_veg_type_codes'",
            "'farm_import_class_codes', 'farm_import_class_codes'",
            "'farm_import_state_codes', 'farm_import_state_codes'",
            "'farm_inventory_class_codes', 'farm_inventory_class_codes'",
            "'farm_inventory_group_codes', 'farm_inventory_group_codes'",
            "'farm_inventory_item_codes', 'farm_inventory_item_codes'",
            "'farm_message_type_codes', 'farm_message_type_codes'",
            "'farm_multi_stage_commdty_codes', 'farm_multi_stage_commdty_codes'",
            "'farm_municipality_codes', 'farm_municipality_codes'",
            "'farm_participant_class_codes', 'Participant Class'",
            "'farm_participnt_data_src_codes', 'farm_participnt_data_src_codes'",
            "'farm_participant_lang_codes', 'Participant Language'",
            "'farm_participant_profile_codes', 'Participant Profile'",
            "'farm_regional_office_codes', 'farm_regional_office_codes'"
    })
    public void testGetCodeTable(String codeTableName, String codeTableDescriptiveName) throws Exception {
        Response response = target("/codeTables/" + codeTableName).request().get();
        assertEquals(200, response.getStatus());

        String jsonString = response.readEntity(String.class);
        JSONObject jsonObject = new JSONObject(jsonString);
        assertEquals("http://common.brmb.nrs.gov.bc.ca/v1/codeTable", jsonObject.getString("@type"));
        assertEquals(codeTableName, jsonObject.getString("codeTableName"));
        assertEquals(codeTableDescriptiveName, jsonObject.getString("codeTableDescriptiveName"));
    }
}
