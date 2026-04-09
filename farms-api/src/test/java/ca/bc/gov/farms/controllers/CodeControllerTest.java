package ca.bc.gov.farms.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
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

import ca.bc.gov.farms.data.models.CodeRsrc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String TABLE_NAME = "farm_municipality_codes";
    private static final LocalDate EFFECTIVE_DATE = LocalDate.now();
    private static final LocalDate EXPIRY_DATE = EFFECTIVE_DATE.plusYears(1);

    @SuppressWarnings("null")
    @Test
    @Order(1)
    public void testCreateCode() throws Exception {

        CodeRsrc resource = new CodeRsrc();
        resource.setCode("51");
        resource.setDescription("Test Municipality");
        resource.setEffectiveDate(EFFECTIVE_DATE);
        resource.setExpiryDate(EXPIRY_DATE);
        resource.setUserEmail("testUser");

        mockMvc.perform(post("/codeTables/" + TABLE_NAME + "/codes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.@type").value("CodeRsrc"))
                .andExpect(jsonPath("$.code").value("51"))
                .andExpect(jsonPath("$.description").value("Test Municipality"))
                .andExpect(jsonPath("$.effectiveDate").value(EFFECTIVE_DATE.toString()))
                .andExpect(jsonPath("$.expiryDate").value(EXPIRY_DATE.toString()));
    }

    @Test
    @Order(2)
    public void testGetCode() throws Exception {

        mockMvc.perform(get("/codeTables/" + TABLE_NAME + "/codes/51"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("CodeRsrc"))
                .andExpect(jsonPath("$.code").value("51"))
                .andExpect(jsonPath("$.description").value("Test Municipality"))
                .andExpect(jsonPath("$.effectiveDate").value(EFFECTIVE_DATE.toString()))
                .andExpect(jsonPath("$.expiryDate").value(EXPIRY_DATE.toString()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(3)
    public void testUpdateCode() throws Exception {

        CodeRsrc resource = new CodeRsrc();
        resource.setCode("51");
        resource.setDescription("Municipality Test");
        resource.setEffectiveDate(EXPIRY_DATE);
        resource.setExpiryDate(EFFECTIVE_DATE);
        resource.setUserEmail("testUser");

        mockMvc.perform(put("/codeTables/" + TABLE_NAME + "/codes/51")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(jsonPath("$.@type").value("CodeRsrc"))
                .andExpect(jsonPath("$.code").value("51"))
                .andExpect(jsonPath("$.description").value("Municipality Test"))
                .andExpect(jsonPath("$.effectiveDate").value(EXPIRY_DATE.toString()))
                .andExpect(jsonPath("$.expiryDate").value(EFFECTIVE_DATE.toString()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(4)
    public void testDeleteCode() throws Exception {

        CodeRsrc resource = new CodeRsrc();
        resource.setCode("51");
        resource.setDescription("Municipality Test");
        resource.setEffectiveDate(EXPIRY_DATE);
        resource.setExpiryDate(EFFECTIVE_DATE);
        resource.setUserEmail("testUser");

        mockMvc.perform(delete("/codeTables/" + TABLE_NAME + "/codes/51")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isNoContent());
    }
}
