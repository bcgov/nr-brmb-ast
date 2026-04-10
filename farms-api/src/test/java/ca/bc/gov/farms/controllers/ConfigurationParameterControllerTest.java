package ca.bc.gov.farms.controllers;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.farms.data.models.ConfigurationParameterRsrc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ConfigurationParameterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("null")
    @Test
    @Order(1)
    public void testCreateConfigurationParameter() throws Exception {

        ConfigurationParameterRsrc resource = new ConfigurationParameterRsrc();
        resource.setParameterName("CDOGS - Api Version");
        resource.setParameterValue("2");
        resource.setSensitiveDataInd("N");
        resource.setConfigParamTypeCode("STRING");
        resource.setUserEmail("jsmith@gmail.com");

        mockMvc.perform(post("/configurationParameters")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.@type").value("ConfigurationParameterRsrc"))
                .andExpect(jsonPath("$.configurationParameterId").value(481))
                .andExpect(jsonPath("$.parameterName").value("CDOGS - Api Version"))
                .andExpect(jsonPath("$.parameterValue").value("2"))
                .andExpect(jsonPath("$.sensitiveDataInd").value("N"))
                .andExpect(jsonPath("$.configParamTypeCode").value("STRING"))
                .andExpect(jsonPath("$.userEmail").value(nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(2)
    public void testGetAllConfigurationParameters() throws Exception {

        mockMvc.perform(get("/configurationParameters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("ConfigurationParameterListRsrc"))
                .andExpect(jsonPath("$.configurationParameterList[0].@type").value("ConfigurationParameterRsrc"))
                .andExpect(jsonPath("$.configurationParameterList[0].configurationParameterId").value(481))
                .andExpect(jsonPath("$.configurationParameterList[0].parameterName").value("CDOGS - Api Version"))
                .andExpect(jsonPath("$.configurationParameterList[0].parameterValue").value("2"))
                .andExpect(jsonPath("$.configurationParameterList[0].sensitiveDataInd").value("N"))
                .andExpect(jsonPath("$.configurationParameterList[0].configParamTypeCode").value("STRING"))
                .andExpect(jsonPath("$.configurationParameterList[0].userEmail").value(nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(3)
    public void testGetAllConfigurationParameters1() throws Exception {

        mockMvc.perform(get("/configurationParameters")
                .param("nameStartsWith", "CDOGS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("ConfigurationParameterListRsrc"))
                .andExpect(jsonPath("$.configurationParameterList[0].@type").value("ConfigurationParameterRsrc"))
                .andExpect(jsonPath("$.configurationParameterList[0].configurationParameterId").value(481))
                .andExpect(jsonPath("$.configurationParameterList[0].parameterName").value("CDOGS - Api Version"))
                .andExpect(jsonPath("$.configurationParameterList[0].parameterValue").value("2"))
                .andExpect(jsonPath("$.configurationParameterList[0].sensitiveDataInd").value("N"))
                .andExpect(jsonPath("$.configurationParameterList[0].configParamTypeCode").value("STRING"))
                .andExpect(jsonPath("$.configurationParameterList[0].userEmail").value(nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(4)
    public void testGetConfigurationParameter() throws Exception {

        mockMvc.perform(get("/configurationParameters/481"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("ConfigurationParameterRsrc"))
                .andExpect(jsonPath("$.configurationParameterId").value(481))
                .andExpect(jsonPath("$.parameterName").value("CDOGS - Api Version"))
                .andExpect(jsonPath("$.parameterValue").value("2"))
                .andExpect(jsonPath("$.sensitiveDataInd").value("N"))
                .andExpect(jsonPath("$.configParamTypeCode").value("STRING"))
                .andExpect(jsonPath("$.userEmail").value(nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(5)
    public void testUpdateConfigurationParameter() throws Exception {

        ConfigurationParameterRsrc resource = new ConfigurationParameterRsrc();
        resource.setConfigurationParameterId(481L);
        resource.setParameterName("CDOGS - Api Version");
        resource.setParameterValue("3");
        resource.setSensitiveDataInd("N");
        resource.setConfigParamTypeCode("STRING");
        resource.setUserEmail("jsmith@gmail.com");

        mockMvc.perform(put("/configurationParameters/481")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("ConfigurationParameterRsrc"))
                .andExpect(jsonPath("$.configurationParameterId").value(481))
                .andExpect(jsonPath("$.parameterName").value("CDOGS - Api Version"))
                .andExpect(jsonPath("$.parameterValue").value("3"))
                .andExpect(jsonPath("$.sensitiveDataInd").value("N"))
                .andExpect(jsonPath("$.configParamTypeCode").value("STRING"))
                .andExpect(jsonPath("$.userEmail").value(nullValue()));
    }

    @Test
    @Order(6)
    public void testDeleteConfigurationParameter() throws Exception {

        mockMvc.perform(delete("/configurationParameters/481"))
                .andExpect(status().isNoContent());
    }
}
