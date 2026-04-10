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

import ca.bc.gov.farms.data.models.InventoryTypeXrefModel;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InventoryTypeXrefControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("null")
    @Test
    @Order(1)
    public void testCreateInventoryTypeXref() throws Exception {

        InventoryTypeXrefModel resource = new InventoryTypeXrefModel();
        resource.setMarketCommodityInd("Y");
        resource.setInventoryItemCode("73");
        resource.setInventoryGroupCode("3");
        resource.setInventoryClassCode("4");
        resource.setUserEmail("jsmith@gmail.com");

        mockMvc.perform(post("/inventoryTypeXrefs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.@type").value("InventoryTypeXrefModel"))
                .andExpect(jsonPath("$.agristabilityCommodityXrefId").value(233520))
                .andExpect(jsonPath("$.marketCommodityInd").value("Y"))
                .andExpect(jsonPath("$.inventoryItemCode").value("73"))
                .andExpect(jsonPath("$.inventoryItemDesc").value("Strawberries"))
                .andExpect(jsonPath("$.inventoryGroupCode").value("3"))
                .andExpect(jsonPath("$.inventoryGroupDesc").value("Berries"))
                .andExpect(jsonPath("$.inventoryClassCode").value("4"))
                .andExpect(jsonPath("$.inventoryClassDesc").value("Deferred Income and Receivables"))
                .andExpect(jsonPath("$.userEmail").value(nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(2)
    public void testGetInventoryTypeXrefsByInventoryClassCode() throws Exception {

        mockMvc.perform(get("/inventoryTypeXrefs")
                .param("inventoryClassCode", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("InventoryTypeXrefListRsrc"))
                .andExpect(jsonPath("$.inventoryTypeXrefList[0].@type").value("InventoryTypeXrefModel"))
                .andExpect(jsonPath("$.inventoryTypeXrefList[0].agristabilityCommodityXrefId").value(233520))
                .andExpect(jsonPath("$.inventoryTypeXrefList[0].marketCommodityInd").value("Y"))
                .andExpect(jsonPath("$.inventoryTypeXrefList[0].inventoryItemCode").value("73"))
                .andExpect(jsonPath("$.inventoryTypeXrefList[0].inventoryItemDesc").value("Strawberries"))
                .andExpect(jsonPath("$.inventoryTypeXrefList[0].inventoryGroupCode").value("3"))
                .andExpect(jsonPath("$.inventoryTypeXrefList[0].inventoryGroupDesc").value("Berries"))
                .andExpect(jsonPath("$.inventoryTypeXrefList[0].inventoryClassCode").value("4"))
                .andExpect(jsonPath("$.inventoryTypeXrefList[0].inventoryClassDesc")
                        .value("Deferred Income and Receivables"))
                .andExpect(jsonPath("$.inventoryTypeXrefList[0].userEmail").value(nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(3)
    public void testGetInventoryTypeXref() throws Exception {

        mockMvc.perform(get("/inventoryTypeXrefs/233520"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("InventoryTypeXrefModel"))
                .andExpect(jsonPath("$.agristabilityCommodityXrefId").value(233520))
                .andExpect(jsonPath("$.marketCommodityInd").value("Y"))
                .andExpect(jsonPath("$.inventoryItemCode").value("73"))
                .andExpect(jsonPath("$.inventoryItemDesc").value("Strawberries"))
                .andExpect(jsonPath("$.inventoryGroupCode").value("3"))
                .andExpect(jsonPath("$.inventoryGroupDesc").value("Berries"))
                .andExpect(jsonPath("$.inventoryClassCode").value("4"))
                .andExpect(jsonPath("$.inventoryClassDesc").value("Deferred Income and Receivables"))
                .andExpect(jsonPath("$.userEmail").value(nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(4)
    public void testUpdateInventoryTypeXref() throws Exception {

        InventoryTypeXrefModel resource = new InventoryTypeXrefModel();
        resource.setAgristabilityCommodityXrefId(233520L);
        resource.setMarketCommodityInd("N");
        resource.setInventoryItemCode("5560");
        resource.setInventoryGroupCode("4");
        resource.setInventoryClassCode("5");
        resource.setUserEmail("jsmith@gmail.com");

        mockMvc.perform(put("/inventoryTypeXrefs/233520")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("InventoryTypeXrefModel"))
                .andExpect(jsonPath("$.agristabilityCommodityXrefId").value(233520))
                .andExpect(jsonPath("$.marketCommodityInd").value("N"))
                .andExpect(jsonPath("$.inventoryItemCode").value("5560"))
                .andExpect(jsonPath("$.inventoryItemDesc").value("Alfalfa Dehy"))
                .andExpect(jsonPath("$.inventoryGroupCode").value("4"))
                .andExpect(jsonPath("$.inventoryGroupDesc").value("Buckwheat"))
                .andExpect(jsonPath("$.inventoryClassCode").value("5"))
                .andExpect(jsonPath("$.inventoryClassDesc").value("Accounts Payable"))
                .andExpect(jsonPath("$.userEmail").value(nullValue()));
    }

    @Test
    @Order(5)
    public void testDeleteInventoryTypeXref() throws Exception {

        mockMvc.perform(delete("/inventoryTypeXrefs/233520"))
                .andExpect(status().isNoContent());
    }
}
