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

import ca.bc.gov.farms.data.models.InventoryItemAttributeRsrc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InventoryItemAttributeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("null")
    @Test
    @Order(1)
    public void testCreateInventoryItemAttribute() throws Exception {

        InventoryItemAttributeRsrc resource = new InventoryItemAttributeRsrc();
        resource.setInventoryItemCode("73");
        resource.setRollupInventoryItemCode("73");
        resource.setUserEmail("jsmith@gmail.com");

        mockMvc.perform(post("/inventoryItemAttributes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.@type").value("InventoryItemAttributeRsrc"))
                .andExpect(jsonPath("$.inventoryItemAttributeId").value(3961))
                .andExpect(jsonPath("$.inventoryItemCode").value("73"))
                .andExpect(jsonPath("$.inventoryItemDesc").value("Strawberries"))
                .andExpect(jsonPath("$.rollupInventoryItemCode").value("73"))
                .andExpect(jsonPath("$.rollupInventoryItemDesc").value("Strawberries"))
                .andExpect(jsonPath("$.userEmail").value(nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(2)
    public void testGetInventoryItemAttributeByInventoryItemCode() throws Exception {

        mockMvc.perform(get("/inventoryItemAttributes")
                .param("inventoryItemCode", "73"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("InventoryItemAttributeRsrc"))
                .andExpect(jsonPath("$.inventoryItemAttributeId").value(3961))
                .andExpect(jsonPath("$.inventoryItemCode").value("73"))
                .andExpect(jsonPath("$.inventoryItemDesc").value("Strawberries"))
                .andExpect(jsonPath("$.rollupInventoryItemCode").value("73"))
                .andExpect(jsonPath("$.rollupInventoryItemDesc").value("Strawberries"))
                .andExpect(jsonPath("$.userEmail").value(nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(3)
    public void testGetInventoryItemAttribute() throws Exception {

        mockMvc.perform(get("/inventoryItemAttributes/3961"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("InventoryItemAttributeRsrc"))
                .andExpect(jsonPath("$.inventoryItemAttributeId").value(3961))
                .andExpect(jsonPath("$.inventoryItemCode").value("73"))
                .andExpect(jsonPath("$.inventoryItemDesc").value("Strawberries"))
                .andExpect(jsonPath("$.rollupInventoryItemCode").value("73"))
                .andExpect(jsonPath("$.rollupInventoryItemDesc").value("Strawberries"))
                .andExpect(jsonPath("$.userEmail").value(nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(4)
    public void testUpdateInventoryItemAttribute() throws Exception {

        InventoryItemAttributeRsrc resource = new InventoryItemAttributeRsrc();
        resource.setInventoryItemAttributeId(3961L);
        resource.setInventoryItemCode("73");
        resource.setRollupInventoryItemCode("5560");
        resource.setUserEmail("jsmith@gmail.com");

        mockMvc.perform(put("/inventoryItemAttributes/3961")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("InventoryItemAttributeRsrc"))
                .andExpect(jsonPath("$.inventoryItemAttributeId").value(3961))
                .andExpect(jsonPath("$.inventoryItemCode").value("73"))
                .andExpect(jsonPath("$.inventoryItemDesc").value("Strawberries"))
                .andExpect(jsonPath("$.rollupInventoryItemCode").value("5560"))
                .andExpect(jsonPath("$.rollupInventoryItemDesc").value("Alfalfa Dehy"))
                .andExpect(jsonPath("$.userEmail").value(nullValue()));
    }

    @Test
    @Order(5)
    public void testDeleteInventoryItemAttribute() throws Exception {

        mockMvc.perform(delete("/inventoryItemAttributes/3961"))
                .andExpect(status().isNoContent());
    }
}
