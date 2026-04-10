package ca.bc.gov.farms.controllers;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
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

import ca.bc.gov.farms.data.models.FairMarketValueModel;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FairMarketValueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("null")
    @Test
    @Order(1)
    public void testCreateFairMarketValue() throws Exception {

        FairMarketValueModel resource = new FairMarketValueModel();
        resource.setProgramYear(2025);
        resource.setInventoryItemCode("5562");
        resource.setMunicipalityCode("41");
        resource.setCropUnitCode("1");
        resource.setDefaultCropUnitCode("2");
        resource.setPeriod01Price(new BigDecimal("8.66"));
        resource.setPeriod02Price(new BigDecimal("8.66"));
        resource.setPeriod03Price(new BigDecimal("8.66"));
        resource.setPeriod04Price(new BigDecimal("8.01"));
        resource.setPeriod05Price(new BigDecimal("8.01"));
        resource.setPeriod06Price(new BigDecimal("8.01"));
        resource.setPeriod07Price(new BigDecimal("5.88"));
        resource.setPeriod08Price(new BigDecimal("5.88"));
        resource.setPeriod09Price(new BigDecimal("5.88"));
        resource.setPeriod10Price(new BigDecimal("5.45"));
        resource.setPeriod11Price(new BigDecimal("5.45"));
        resource.setPeriod12Price(new BigDecimal("5.45"));
        resource.setPeriod01Variance(new BigDecimal("46.00"));
        resource.setPeriod02Variance(new BigDecimal("46.00"));
        resource.setPeriod03Variance(new BigDecimal("46.00"));
        resource.setPeriod04Variance(new BigDecimal("46.00"));
        resource.setPeriod05Variance(new BigDecimal("46.00"));
        resource.setPeriod06Variance(new BigDecimal("46.00"));
        resource.setPeriod07Variance(new BigDecimal("46.00"));
        resource.setPeriod08Variance(new BigDecimal("46.00"));
        resource.setPeriod09Variance(new BigDecimal("46.00"));
        resource.setPeriod10Variance(new BigDecimal("46.00"));
        resource.setPeriod11Variance(new BigDecimal("46.00"));
        resource.setPeriod12Variance(new BigDecimal("46.00"));
        resource.setUrlId(1L);
        resource.setUserEmail("jsmith@gmail.com");

        mockMvc.perform(post("/fairMarketValues")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.@type").value("FairMarketValueModel"))
                .andExpect(jsonPath("$.fairMarketValueId").value("2025_5562_41_1"))
                .andExpect(jsonPath("$.programYear").value(2025))
                .andExpect(jsonPath("$.inventoryItemCode").value("5562"))
                .andExpect(jsonPath("$.inventoryItemDesc").value("Greenfeed"))
                .andExpect(jsonPath("$.municipalityCode").value("41"))
                .andExpect(jsonPath("$.municipalityDesc").value("Cariboo"))
                .andExpect(jsonPath("$.cropUnitCode").value("1"))
                .andExpect(jsonPath("$.cropUnitDesc").value("Pounds"))
                .andExpect(jsonPath("$.defaultCropUnitCode").value("2"))
                .andExpect(jsonPath("$.defaultCropUnitDesc").value("Tonnes"))
                .andExpect(jsonPath("$.period01Price").value(8.66))
                .andExpect(jsonPath("$.period02Price").value(8.66))
                .andExpect(jsonPath("$.period03Price").value(8.66))
                .andExpect(jsonPath("$.period04Price").value(8.01))
                .andExpect(jsonPath("$.period05Price").value(8.01))
                .andExpect(jsonPath("$.period06Price").value(8.01))
                .andExpect(jsonPath("$.period07Price").value(5.88))
                .andExpect(jsonPath("$.period08Price").value(5.88))
                .andExpect(jsonPath("$.period09Price").value(5.88))
                .andExpect(jsonPath("$.period10Price").value(5.45))
                .andExpect(jsonPath("$.period11Price").value(5.45))
                .andExpect(jsonPath("$.period12Price").value(5.45))
                .andExpect(jsonPath("$.period01Variance").value(46.00))
                .andExpect(jsonPath("$.period02Variance").value(46.00))
                .andExpect(jsonPath("$.period03Variance").value(46.00))
                .andExpect(jsonPath("$.period04Variance").value(46.00))
                .andExpect(jsonPath("$.period05Variance").value(46.00))
                .andExpect(jsonPath("$.period06Variance").value(46.00))
                .andExpect(jsonPath("$.period07Variance").value(46.00))
                .andExpect(jsonPath("$.period08Variance").value(46.00))
                .andExpect(jsonPath("$.period09Variance").value(46.00))
                .andExpect(jsonPath("$.period10Variance").value(46.00))
                .andExpect(jsonPath("$.period11Variance").value(46.00))
                .andExpect(jsonPath("$.period12Variance").value(46.00))
                .andExpect(jsonPath("$.urlId").value(1))
                .andExpect(jsonPath("$.url").value("https://google.com"))
                .andExpect(jsonPath("$.userEmail").value(nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(2)
    public void testGetFairMarketValuesByProgramYear() throws Exception {

        mockMvc.perform(get("/fairMarketValues")
                .param("programYear", "2025"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("FairMarketValueListRsrc"))
                .andExpect(jsonPath("$.fairMarketValueList[0].@type").value("FairMarketValueModel"))
                .andExpect(jsonPath("$.fairMarketValueList[0].fairMarketValueId").value("2025_5562_41_1"))
                .andExpect(jsonPath("$.fairMarketValueList[0].programYear").value(2025))
                .andExpect(jsonPath("$.fairMarketValueList[0].inventoryItemCode").value("5562"))
                .andExpect(jsonPath("$.fairMarketValueList[0].inventoryItemDesc").value("Greenfeed"))
                .andExpect(jsonPath("$.fairMarketValueList[0].municipalityCode").value("41"))
                .andExpect(jsonPath("$.fairMarketValueList[0].municipalityDesc").value("Cariboo"))
                .andExpect(jsonPath("$.fairMarketValueList[0].cropUnitCode").value("1"))
                .andExpect(jsonPath("$.fairMarketValueList[0].cropUnitDesc").value("Pounds"))
                .andExpect(jsonPath("$.fairMarketValueList[0].defaultCropUnitCode").value("2"))
                .andExpect(jsonPath("$.fairMarketValueList[0].defaultCropUnitDesc").value("Tonnes"))
                .andExpect(jsonPath("$.fairMarketValueList[0].period01Price").value(8.66))
                .andExpect(jsonPath("$.fairMarketValueList[0].period02Price").value(8.66))
                .andExpect(jsonPath("$.fairMarketValueList[0].period03Price").value(8.66))
                .andExpect(jsonPath("$.fairMarketValueList[0].period04Price").value(8.01))
                .andExpect(jsonPath("$.fairMarketValueList[0].period05Price").value(8.01))
                .andExpect(jsonPath("$.fairMarketValueList[0].period06Price").value(8.01))
                .andExpect(jsonPath("$.fairMarketValueList[0].period07Price").value(5.88))
                .andExpect(jsonPath("$.fairMarketValueList[0].period08Price").value(5.88))
                .andExpect(jsonPath("$.fairMarketValueList[0].period09Price").value(5.88))
                .andExpect(jsonPath("$.fairMarketValueList[0].period10Price").value(5.45))
                .andExpect(jsonPath("$.fairMarketValueList[0].period11Price").value(5.45))
                .andExpect(jsonPath("$.fairMarketValueList[0].period12Price").value(5.45))
                .andExpect(jsonPath("$.fairMarketValueList[0].period01Variance").value(46.00))
                .andExpect(jsonPath("$.fairMarketValueList[0].period02Variance").value(46.00))
                .andExpect(jsonPath("$.fairMarketValueList[0].period03Variance").value(46.00))
                .andExpect(jsonPath("$.fairMarketValueList[0].period04Variance").value(46.00))
                .andExpect(jsonPath("$.fairMarketValueList[0].period05Variance").value(46.00))
                .andExpect(jsonPath("$.fairMarketValueList[0].period06Variance").value(46.00))
                .andExpect(jsonPath("$.fairMarketValueList[0].period07Variance").value(46.00))
                .andExpect(jsonPath("$.fairMarketValueList[0].period08Variance").value(46.00))
                .andExpect(jsonPath("$.fairMarketValueList[0].period09Variance").value(46.00))
                .andExpect(jsonPath("$.fairMarketValueList[0].period10Variance").value(46.00))
                .andExpect(jsonPath("$.fairMarketValueList[0].period11Variance").value(46.00))
                .andExpect(jsonPath("$.fairMarketValueList[0].period12Variance").value(46.00))
                .andExpect(jsonPath("$.fairMarketValueList[0].urlId").value(1))
                .andExpect(jsonPath("$.fairMarketValueList[0].url").value("https://google.com"))
                .andExpect(jsonPath("$.fairMarketValueList[0].userEmail").value(nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(3)
    public void testGetFairMarketValue() throws Exception {

        mockMvc.perform(get("/fairMarketValues/2025_5562_41_1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("FairMarketValueModel"))
                .andExpect(jsonPath("$.fairMarketValueId").value("2025_5562_41_1"))
                .andExpect(jsonPath("$.programYear").value(2025))
                .andExpect(jsonPath("$.inventoryItemCode").value("5562"))
                .andExpect(jsonPath("$.inventoryItemDesc").value("Greenfeed"))
                .andExpect(jsonPath("$.municipalityCode").value("41"))
                .andExpect(jsonPath("$.municipalityDesc").value("Cariboo"))
                .andExpect(jsonPath("$.cropUnitCode").value("1"))
                .andExpect(jsonPath("$.cropUnitDesc").value("Pounds"))
                .andExpect(jsonPath("$.defaultCropUnitCode").value("2"))
                .andExpect(jsonPath("$.defaultCropUnitDesc").value("Tonnes"))
                .andExpect(jsonPath("$.period01Price").value(8.66))
                .andExpect(jsonPath("$.period02Price").value(8.66))
                .andExpect(jsonPath("$.period03Price").value(8.66))
                .andExpect(jsonPath("$.period04Price").value(8.01))
                .andExpect(jsonPath("$.period05Price").value(8.01))
                .andExpect(jsonPath("$.period06Price").value(8.01))
                .andExpect(jsonPath("$.period07Price").value(5.88))
                .andExpect(jsonPath("$.period08Price").value(5.88))
                .andExpect(jsonPath("$.period09Price").value(5.88))
                .andExpect(jsonPath("$.period10Price").value(5.45))
                .andExpect(jsonPath("$.period11Price").value(5.45))
                .andExpect(jsonPath("$.period12Price").value(5.45))
                .andExpect(jsonPath("$.period01Variance").value(46.00))
                .andExpect(jsonPath("$.period02Variance").value(46.00))
                .andExpect(jsonPath("$.period03Variance").value(46.00))
                .andExpect(jsonPath("$.period04Variance").value(46.00))
                .andExpect(jsonPath("$.period05Variance").value(46.00))
                .andExpect(jsonPath("$.period06Variance").value(46.00))
                .andExpect(jsonPath("$.period07Variance").value(46.00))
                .andExpect(jsonPath("$.period08Variance").value(46.00))
                .andExpect(jsonPath("$.period09Variance").value(46.00))
                .andExpect(jsonPath("$.period10Variance").value(46.00))
                .andExpect(jsonPath("$.period11Variance").value(46.00))
                .andExpect(jsonPath("$.period12Variance").value(46.00))
                .andExpect(jsonPath("$.urlId").value(1))
                .andExpect(jsonPath("$.url").value("https://google.com"))
                .andExpect(jsonPath("$.userEmail").value(nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(4)
    public void testUpdateFairMarketValue() throws Exception {

        FairMarketValueModel resource = new FairMarketValueModel();
        resource.setFairMarketValueId("2025_5562_41_1");
        resource.setProgramYear(2025);
        resource.setInventoryItemCode("5562");
        resource.setMunicipalityCode("41");
        resource.setCropUnitCode("1");
        resource.setDefaultCropUnitCode("2");
        resource.setPeriod01Price(new BigDecimal("1008.66"));
        resource.setPeriod02Price(new BigDecimal("1008.66"));
        resource.setPeriod03Price(new BigDecimal("1008.66"));
        resource.setPeriod04Price(new BigDecimal("1008.01"));
        resource.setPeriod05Price(new BigDecimal("1008.01"));
        resource.setPeriod06Price(new BigDecimal("1008.01"));
        resource.setPeriod07Price(new BigDecimal("1005.88"));
        resource.setPeriod08Price(new BigDecimal("1005.88"));
        resource.setPeriod09Price(new BigDecimal("1005.88"));
        resource.setPeriod10Price(new BigDecimal("1005.45"));
        resource.setPeriod11Price(new BigDecimal("1005.45"));
        resource.setPeriod12Price(new BigDecimal("1005.45"));
        resource.setPeriod01Variance(new BigDecimal("56.00"));
        resource.setPeriod02Variance(new BigDecimal("56.00"));
        resource.setPeriod03Variance(new BigDecimal("56.00"));
        resource.setPeriod04Variance(new BigDecimal("56.00"));
        resource.setPeriod05Variance(new BigDecimal("56.00"));
        resource.setPeriod06Variance(new BigDecimal("56.00"));
        resource.setPeriod07Variance(new BigDecimal("56.00"));
        resource.setPeriod08Variance(new BigDecimal("56.00"));
        resource.setPeriod09Variance(new BigDecimal("56.00"));
        resource.setPeriod10Variance(new BigDecimal("56.00"));
        resource.setPeriod11Variance(new BigDecimal("56.00"));
        resource.setPeriod12Variance(new BigDecimal("56.00"));
        resource.setUrlId(2L);
        resource.setUserEmail("jsmith@gmail.com");

        mockMvc.perform(put("/fairMarketValues/2025_5562_41_1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("FairMarketValueModel"))
                .andExpect(jsonPath("$.fairMarketValueId").value("2025_5562_41_1"))
                .andExpect(jsonPath("$.programYear").value(2025))
                .andExpect(jsonPath("$.inventoryItemCode").value("5562"))
                .andExpect(jsonPath("$.inventoryItemDesc").value("Greenfeed"))
                .andExpect(jsonPath("$.municipalityCode").value("41"))
                .andExpect(jsonPath("$.municipalityDesc").value("Cariboo"))
                .andExpect(jsonPath("$.cropUnitCode").value("1"))
                .andExpect(jsonPath("$.cropUnitDesc").value("Pounds"))
                .andExpect(jsonPath("$.defaultCropUnitCode").value("2"))
                .andExpect(jsonPath("$.defaultCropUnitDesc").value("Tonnes"))
                .andExpect(jsonPath("$.period01Price").value(1008.66))
                .andExpect(jsonPath("$.period02Price").value(1008.66))
                .andExpect(jsonPath("$.period03Price").value(1008.66))
                .andExpect(jsonPath("$.period04Price").value(1008.01))
                .andExpect(jsonPath("$.period05Price").value(1008.01))
                .andExpect(jsonPath("$.period06Price").value(1008.01))
                .andExpect(jsonPath("$.period07Price").value(1005.88))
                .andExpect(jsonPath("$.period08Price").value(1005.88))
                .andExpect(jsonPath("$.period09Price").value(1005.88))
                .andExpect(jsonPath("$.period10Price").value(1005.45))
                .andExpect(jsonPath("$.period11Price").value(1005.45))
                .andExpect(jsonPath("$.period12Price").value(1005.45))
                .andExpect(jsonPath("$.period01Variance").value(56.00))
                .andExpect(jsonPath("$.period02Variance").value(56.00))
                .andExpect(jsonPath("$.period03Variance").value(56.00))
                .andExpect(jsonPath("$.period04Variance").value(56.00))
                .andExpect(jsonPath("$.period05Variance").value(56.00))
                .andExpect(jsonPath("$.period06Variance").value(56.00))
                .andExpect(jsonPath("$.period07Variance").value(56.00))
                .andExpect(jsonPath("$.period08Variance").value(56.00))
                .andExpect(jsonPath("$.period09Variance").value(56.00))
                .andExpect(jsonPath("$.period10Variance").value(56.00))
                .andExpect(jsonPath("$.period11Variance").value(56.00))
                .andExpect(jsonPath("$.period12Variance").value(56.00))
                .andExpect(jsonPath("$.urlId").value(2))
                .andExpect(jsonPath("$.url").value("https://microsoft.com"))
                .andExpect(jsonPath("$.userEmail").value(nullValue()));
    }

    @Test
    @Order(6)
    public void testDeleteFairMarketValue() throws Exception {

        mockMvc.perform(delete("/fairMarketValues/2025_5562_41_1"))
                .andExpect(status().isNoContent());
    }
}
