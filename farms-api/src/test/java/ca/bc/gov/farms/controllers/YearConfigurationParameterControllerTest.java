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

import ca.bc.gov.farms.data.models.YearConfigurationParameterRsrc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class YearConfigurationParameterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("null")
    @Test
    @Order(1)
    public void testCreateYearConfigurationParameter() throws Exception {

        YearConfigurationParameterRsrc resource = new YearConfigurationParameterRsrc();
        resource.setProgramYear(2023);
        resource.setParameterName("Payment Limitation - Percentage of Total Margin Decline");
        resource.setParameterValue("70");
        resource.setConfigParamTypeCode("DECIMAL");
        resource.setUserEmail("jsmith@gmail.com");

        mockMvc.perform(post("/yearConfigurationParameters")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.@type").value("YearConfigurationParameterRsrc"))
                .andExpect(jsonPath("$.yearConfigurationParameterId").value(241))
                .andExpect(jsonPath("$.programYear").value(2023))
                .andExpect(jsonPath("$.parameterName").value("Payment Limitation - Percentage of Total Margin Decline"))
                .andExpect(jsonPath("$.parameterValue").value("70"))
                .andExpect(jsonPath("$.configParamTypeCode").value("DECIMAL"))
                .andExpect(jsonPath("$.userEmail").value(nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(2)
    public void testGetAllYearConfigurationParameters() throws Exception {

        mockMvc.perform(get("/yearConfigurationParameters")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("YearConfigurationParameterListRsrc"))
                .andExpect(
                        jsonPath("$.yearConfigurationParameterList[0].@type").value("YearConfigurationParameterRsrc"))
                .andExpect(jsonPath("$.yearConfigurationParameterList[0].yearConfigurationParameterId").value(241))
                .andExpect(jsonPath("$.yearConfigurationParameterList[0].programYear").value(2023))
                .andExpect(jsonPath("$.yearConfigurationParameterList[0].parameterName")
                        .value("Payment Limitation - Percentage of Total Margin Decline"))
                .andExpect(jsonPath("$.yearConfigurationParameterList[0].parameterValue").value("70"))
                .andExpect(jsonPath("$.yearConfigurationParameterList[0].configParamTypeCode").value("DECIMAL"))
                .andExpect(jsonPath("$.yearConfigurationParameterList[0].userEmail").value(nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(3)
    public void testGetYearConfigurationParameter() throws Exception {

        mockMvc.perform(get("/yearConfigurationParameters/241")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("YearConfigurationParameterRsrc"))
                .andExpect(jsonPath("$.yearConfigurationParameterId").value(241))
                .andExpect(jsonPath("$.programYear").value(2023))
                .andExpect(jsonPath("$.parameterName").value("Payment Limitation - Percentage of Total Margin Decline"))
                .andExpect(jsonPath("$.parameterValue").value("70"))
                .andExpect(jsonPath("$.configParamTypeCode").value("DECIMAL"))
                .andExpect(jsonPath("$.userEmail").value(nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(4)
    public void testUpdateYearConfigurationParameter() throws Exception {

        YearConfigurationParameterRsrc resource = new YearConfigurationParameterRsrc();
        resource.setYearConfigurationParameterId(241L);
        resource.setProgramYear(2023);
        resource.setParameterName("Payment Limitation - Percentage of Total Margin Decline");
        resource.setParameterValue("700");
        resource.setConfigParamTypeCode("DECIMAL");
        resource.setUserEmail("jsmith@gmail.com");

        mockMvc.perform(put("/yearConfigurationParameters/241")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("YearConfigurationParameterRsrc"))
                .andExpect(jsonPath("$.yearConfigurationParameterId").value(241))
                .andExpect(jsonPath("$.programYear").value(2023))
                .andExpect(jsonPath("$.parameterName").value("Payment Limitation - Percentage of Total Margin Decline"))
                .andExpect(jsonPath("$.parameterValue").value("700"))
                .andExpect(jsonPath("$.configParamTypeCode").value("DECIMAL"))
                .andExpect(jsonPath("$.userEmail").value(nullValue()));
    }

    @Test
    @Order(5)
    public void testDeleteYearConfigurationParameter() throws Exception {

        mockMvc.perform(delete("/yearConfigurationParameters/241"))
                .andExpect(status().isNoContent());
    }
}
