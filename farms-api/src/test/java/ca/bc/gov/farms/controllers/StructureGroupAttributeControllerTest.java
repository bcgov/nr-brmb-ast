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

import ca.bc.gov.farms.data.models.StructureGroupAttributeRsrc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StructureGroupAttributeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("null")
    @Test
    @Order(1)
    public void testCreateStructureGroupAttribute() throws Exception {

        StructureGroupAttributeRsrc resource = new StructureGroupAttributeRsrc();
        resource.setStructureGroupCode("100");
        resource.setRollupStructureGroupCode("120");
        resource.setUserEmail("jsmith@gmail.com");

        mockMvc.perform(post("/structureGroupAttributes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.@type").value("StructureGroupAttributeRsrc"))
                .andExpect(jsonPath("$.structureGroupAttributeId").value(21))
                .andExpect(jsonPath("$.structureGroupCode").value("100"))
                .andExpect(jsonPath("$.structureGroupDesc").value("Alpaca"))
                .andExpect(jsonPath("$.rollupStructureGroupCode").value("120"))
                .andExpect(jsonPath("$.rollupStructureGroupDesc").value("Other Livestock"))
                .andExpect(jsonPath("$.userEmail", nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(2)
    public void testGetStructureGroupAttributesByStructureGroupCode() throws Exception {

        mockMvc.perform(get("/structureGroupAttributes")
                .param("structureGroupCode", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("StructureGroupAttributeRsrc"))
                .andExpect(jsonPath("$.structureGroupAttributeId").value(21))
                .andExpect(jsonPath("$.structureGroupCode").value("100"))
                .andExpect(jsonPath("$.structureGroupDesc").value("Alpaca"))
                .andExpect(jsonPath("$.rollupStructureGroupCode").value("120"))
                .andExpect(jsonPath("$.rollupStructureGroupDesc").value("Other Livestock"))
                .andExpect(jsonPath("$.userEmail", nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(3)
    public void testGetStructureGroupAttribute() throws Exception {

        mockMvc.perform(get("/structureGroupAttributes/21"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("StructureGroupAttributeRsrc"))
                .andExpect(jsonPath("$.structureGroupAttributeId").value(21))
                .andExpect(jsonPath("$.structureGroupCode").value("100"))
                .andExpect(jsonPath("$.structureGroupDesc").value("Alpaca"))
                .andExpect(jsonPath("$.rollupStructureGroupCode").value("120"))
                .andExpect(jsonPath("$.rollupStructureGroupDesc").value("Other Livestock"))
                .andExpect(jsonPath("$.userEmail", nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(4)
    public void testUpdateStructureGroupAttribute() throws Exception {

        StructureGroupAttributeRsrc resource = new StructureGroupAttributeRsrc();
        resource.setStructureGroupAttributeId(21L);
        resource.setStructureGroupCode("100");
        resource.setRollupStructureGroupCode("300");
        resource.setUserEmail("jsmith@gmail.com");

        mockMvc.perform(put("/structureGroupAttributes/21")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("StructureGroupAttributeRsrc"))
                .andExpect(jsonPath("$.structureGroupAttributeId").value(21))
                .andExpect(jsonPath("$.structureGroupCode").value("100"))
                .andExpect(jsonPath("$.structureGroupDesc").value("Alpaca"))
                .andExpect(jsonPath("$.rollupStructureGroupCode").value("300"))
                .andExpect(jsonPath("$.rollupStructureGroupDesc").value("Bovine"))
                .andExpect(jsonPath("$.userEmail", nullValue()));
    }

    @Test
    @Order(5)
    public void testDeleteStructureGroupAttribute() throws Exception {

        mockMvc.perform(delete("/structureGroupAttributes/21"))
                .andExpect(status().isNoContent());
    }
}
