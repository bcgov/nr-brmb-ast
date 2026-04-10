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

import ca.bc.gov.farms.data.models.ExpectedProductionModel;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExpectedProductionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("null")
    @Test
    @Order(1)
    public void testCreateExpectedProduction() throws Exception {
        ExpectedProductionModel resource = new ExpectedProductionModel();
        resource.setExpectedProductionPerProdUnit(new BigDecimal("0.907"));
        resource.setInventoryItemCode("73");
        resource.setCropUnitCode("1");
        resource.setUserEmail("jsmith@gmail.com");

        mockMvc.perform(post("/expectedProductions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.@type").value("ExpectedProductionModel"))
                .andExpect(jsonPath("$.expectedProductionId").value(341))
                .andExpect(jsonPath("$.expectedProductionPerProdUnit").value(0.907))
                .andExpect(jsonPath("$.inventoryItemCode").value("73"))
                .andExpect(jsonPath("$.inventoryItemDesc").value("Strawberries"))
                .andExpect(jsonPath("$.cropUnitCode").value("1"))
                .andExpect(jsonPath("$.cropUnitDesc").value("Pounds"))
                .andExpect(jsonPath("$.userEmail").value(nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(2)
    public void testGetAllExpectedProductions() throws Exception {

        mockMvc.perform(get("/expectedProductions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("ExpectedProductionListRsrc"))
                .andExpect(jsonPath("$.expectedProductionList[0].@type").value("ExpectedProductionModel"))
                .andExpect(jsonPath("$.expectedProductionList[0].expectedProductionId").value(341))
                .andExpect(jsonPath("$.expectedProductionList[0].expectedProductionPerProdUnit").value(0.907))
                .andExpect(jsonPath("$.expectedProductionList[0].inventoryItemCode").value("73"))
                .andExpect(jsonPath("$.expectedProductionList[0].inventoryItemDesc").value("Strawberries"))
                .andExpect(jsonPath("$.expectedProductionList[0].cropUnitCode").value("1"))
                .andExpect(jsonPath("$.expectedProductionList[0].cropUnitDesc").value("Pounds"))
                .andExpect(jsonPath("$.expectedProductionList[0].userEmail").value(nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(3)
    public void testGetAllExpectedProductions1() throws Exception {

        mockMvc.perform(get("/expectedProductions")
                .param("inventoryItemCode", "73"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("ExpectedProductionListRsrc"))
                .andExpect(jsonPath("$.expectedProductionList[0].@type").value("ExpectedProductionModel"))
                .andExpect(jsonPath("$.expectedProductionList[0].expectedProductionId").value(341))
                .andExpect(jsonPath("$.expectedProductionList[0].expectedProductionPerProdUnit").value(0.907))
                .andExpect(jsonPath("$.expectedProductionList[0].inventoryItemCode").value("73"))
                .andExpect(jsonPath("$.expectedProductionList[0].inventoryItemDesc").value("Strawberries"))
                .andExpect(jsonPath("$.expectedProductionList[0].cropUnitCode").value("1"))
                .andExpect(jsonPath("$.expectedProductionList[0].cropUnitDesc").value("Pounds"))
                .andExpect(jsonPath("$.expectedProductionList[0].userEmail").value(nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(4)
    public void testGetExpectedProduction() throws Exception {

        mockMvc.perform(get("/expectedProductions/341"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("ExpectedProductionModel"))
                .andExpect(jsonPath("$.expectedProductionId").value(341))
                .andExpect(jsonPath("$.expectedProductionPerProdUnit").value(0.907))
                .andExpect(jsonPath("$.inventoryItemCode").value("73"))
                .andExpect(jsonPath("$.inventoryItemDesc").value("Strawberries"))
                .andExpect(jsonPath("$.cropUnitCode").value("1"))
                .andExpect(jsonPath("$.cropUnitDesc").value("Pounds"))
                .andExpect(jsonPath("$.userEmail").value(nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(5)
    public void testUpdateExpectedProduction() throws Exception {

        ExpectedProductionModel resource = new ExpectedProductionModel();
        resource.setExpectedProductionId(341L);
        resource.setExpectedProductionPerProdUnit(new BigDecimal("5113.000"));
        resource.setInventoryItemCode("73");
        resource.setCropUnitCode("1");
        resource.setUserEmail("jsmith@gmail.com");

        mockMvc.perform(put("/expectedProductions/341")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(jsonPath("$.@type").value("ExpectedProductionModel"))
                .andExpect(jsonPath("$.expectedProductionId").value(341))
                .andExpect(jsonPath("$.expectedProductionPerProdUnit").value(5113.000))
                .andExpect(jsonPath("$.inventoryItemCode").value("73"))
                .andExpect(jsonPath("$.inventoryItemDesc").value("Strawberries"))
                .andExpect(jsonPath("$.cropUnitCode").value("1"))
                .andExpect(jsonPath("$.cropUnitDesc").value("Pounds"))
                .andExpect(jsonPath("$.userEmail").value(nullValue()));
    }

    @Test
    @Order(6)
    public void testDeleteExpectedProduction() throws Exception {

        mockMvc.perform(delete("/expectedProductions/341"))
                .andExpect(status().isNoContent());
    }
}
