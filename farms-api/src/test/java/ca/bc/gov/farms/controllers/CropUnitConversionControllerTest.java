package ca.bc.gov.farms.controllers;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

import ca.bc.gov.farms.data.models.ConversionUnitRsrc;
import ca.bc.gov.farms.data.models.CropUnitConversionModel;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CropUnitConversionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("null")
    @Test
    @Order(1)
    public void testCreateCropUnitConversion() throws Exception {

        CropUnitConversionModel resource = new CropUnitConversionModel();
        resource.setInventoryItemCode("5560");
        resource.setCropUnitCode("2");
        List<ConversionUnitRsrc> conversionUnits = new ArrayList<>();
        ConversionUnitRsrc conversionUnit = new ConversionUnitRsrc();
        conversionUnit.setConversionFactor(new BigDecimal("2204.622600"));
        conversionUnit.setTargetCropUnitCode("1");
        conversionUnits.add(conversionUnit);
        resource.setConversionUnits(conversionUnits);
        resource.setUserEmail("jsmith@gmail.com");

        mockMvc.perform(post("/cropUnitConversions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.@type").value("CropUnitConversionModel"))
                .andExpect(jsonPath("$.cropUnitDefaultId").value(1701))
                .andExpect(jsonPath("$.inventoryItemCode").value("5560"))
                .andExpect(jsonPath("$.inventoryItemDesc").value("Alfalfa Dehy"))
                .andExpect(jsonPath("$.cropUnitCode").value("2"))
                .andExpect(jsonPath("$.cropUnitDesc").value("Tonnes"))
                .andExpect(jsonPath("$.userEmail").value(nullValue()))
                .andExpect(jsonPath("$.conversionUnits[0].cropUnitConversionFactorId").value(2001))
                .andExpect(jsonPath("$.conversionUnits[0].conversionFactor").value(2204.622600))
                .andExpect(jsonPath("$.conversionUnits[0].targetCropUnitCode").value("1"))
                .andExpect(jsonPath("$.conversionUnits[0].targetCropUnitDesc").value("Pounds"));
    }

    @SuppressWarnings("null")
    @Test
    @Order(2)
    public void testGetAllCropUnitConversions() throws Exception {

        mockMvc.perform(get("/cropUnitConversions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("CropUnitConversionListModel"))
                .andExpect(jsonPath("$.cropUnitConversionList[1].@type")
                        .value("CropUnitConversionModel"))
                .andExpect(jsonPath("$.cropUnitConversionList[1].cropUnitDefaultId").value(1701))
                .andExpect(jsonPath("$.cropUnitConversionList[1].inventoryItemCode").value("5560"))
                .andExpect(jsonPath("$.cropUnitConversionList[1].inventoryItemDesc")
                        .value("Alfalfa Dehy"))
                .andExpect(jsonPath("$.cropUnitConversionList[1].cropUnitCode").value("2"))
                .andExpect(jsonPath("$.cropUnitConversionList[1].cropUnitDesc").value("Tonnes"))
                .andExpect(jsonPath("$.cropUnitConversionList[1].userEmail").value(nullValue()))
                .andExpect(jsonPath(
                        "$.cropUnitConversionList[1].conversionUnits[0].cropUnitConversionFactorId")
                        .value(2001))
                .andExpect(
                        jsonPath("$.cropUnitConversionList[1].conversionUnits[0].conversionFactor")
                                .value(2204.622600))
                .andExpect(jsonPath("$.cropUnitConversionList[1].conversionUnits[0].targetCropUnitCode")
                        .value("1"))
                .andExpect(
                        jsonPath("$.cropUnitConversionList[1].conversionUnits[0].targetCropUnitDesc")
                                .value("Pounds"));
    }

    @SuppressWarnings("null")
    @Test
    @Order(3)
    public void testGetAllCropUnitConversions1() throws Exception {

        mockMvc.perform(get("/cropUnitConversions")
                .param("inventoryItemCode", "5560"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("CropUnitConversionListModel"))
                .andExpect(jsonPath("$.cropUnitConversionList[0].@type")
                        .value("CropUnitConversionModel"))
                .andExpect(jsonPath("$.cropUnitConversionList[0].cropUnitDefaultId").value(1701))
                .andExpect(jsonPath("$.cropUnitConversionList[0].inventoryItemCode").value("5560"))
                .andExpect(jsonPath("$.cropUnitConversionList[0].inventoryItemDesc")
                        .value("Alfalfa Dehy"))
                .andExpect(jsonPath("$.cropUnitConversionList[0].cropUnitCode").value("2"))
                .andExpect(jsonPath("$.cropUnitConversionList[0].cropUnitDesc").value("Tonnes"))
                .andExpect(jsonPath("$.cropUnitConversionList[0].userEmail").value(nullValue()))
                .andExpect(jsonPath(
                        "$.cropUnitConversionList[0].conversionUnits[0].cropUnitConversionFactorId")
                        .value(2001))
                .andExpect(
                        jsonPath("$.cropUnitConversionList[0].conversionUnits[0].conversionFactor")
                                .value(2204.622600))
                .andExpect(jsonPath("$.cropUnitConversionList[0].conversionUnits[0].targetCropUnitCode")
                        .value("1"))
                .andExpect(
                        jsonPath("$.cropUnitConversionList[0].conversionUnits[0].targetCropUnitDesc")
                                .value("Pounds"));
    }

    @SuppressWarnings("null")
    @Test
    @Order(4)
    public void testGetCropUnitConversion() throws Exception {

        mockMvc.perform(get("/cropUnitConversions/1701"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("CropUnitConversionModel"))
                .andExpect(jsonPath("$.cropUnitDefaultId").value(1701))
                .andExpect(jsonPath("$.inventoryItemCode").value("5560"))
                .andExpect(jsonPath("$.inventoryItemDesc").value("Alfalfa Dehy"))
                .andExpect(jsonPath("$.cropUnitCode").value("2"))
                .andExpect(jsonPath("$.cropUnitDesc").value("Tonnes"))
                .andExpect(jsonPath("$.userEmail").value(nullValue()))
                .andExpect(jsonPath("$.conversionUnits[0].cropUnitConversionFactorId").value(2001))
                .andExpect(jsonPath("$.conversionUnits[0].conversionFactor").value(2204.622600))
                .andExpect(jsonPath("$.conversionUnits[0].targetCropUnitCode").value("1"))
                .andExpect(jsonPath("$.conversionUnits[0].targetCropUnitDesc").value("Pounds"));
    }

    @SuppressWarnings("null")
    @Test
    @Order(5)
    public void testUpdateCropUnitConversion() throws Exception {

        CropUnitConversionModel resource = new CropUnitConversionModel();
        resource.setCropUnitDefaultId(1701L);
        resource.setInventoryItemCode("5560");
        resource.setCropUnitCode("1");
        List<ConversionUnitRsrc> conversionUnits = new ArrayList<>();
        ConversionUnitRsrc conversionUnit = new ConversionUnitRsrc();
        conversionUnit.setCropUnitConversionFactorId(2001L);
        conversionUnit.setConversionFactor(new BigDecimal("3204.622600"));
        conversionUnit.setTargetCropUnitCode("2");
        conversionUnits.add(conversionUnit);
        resource.setConversionUnits(conversionUnits);
        resource.setUserEmail("jsmith@gmail.com");

        mockMvc.perform(put("/cropUnitConversions/1701")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("CropUnitConversionModel"))
                .andExpect(jsonPath("$.cropUnitDefaultId").value(1701))
                .andExpect(jsonPath("$.inventoryItemCode").value("5560"))
                .andExpect(jsonPath("$.inventoryItemDesc").value("Alfalfa Dehy"))
                .andExpect(jsonPath("$.cropUnitCode").value("1"))
                .andExpect(jsonPath("$.cropUnitDesc").value("Pounds"))
                .andExpect(jsonPath("$.userEmail").value(nullValue()))
                .andExpect(jsonPath("$.conversionUnits[0].cropUnitConversionFactorId").value(2001))
                .andExpect(jsonPath("$.conversionUnits[0].conversionFactor").value(3204.622600))
                .andExpect(jsonPath("$.conversionUnits[0].targetCropUnitCode").value("2"))
                .andExpect(jsonPath("$.conversionUnits[0].targetCropUnitDesc").value("Tonnes"));
    }

    @Test
    @Order(6)
    public void testDeleteCropUnitConversion() throws Exception {

        mockMvc.perform(delete("/cropUnitConversions/1701"))
                .andExpect(status().isNoContent());
    }
}
