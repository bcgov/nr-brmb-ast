package ca.bc.gov.farms.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CodeTableControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String TABLE_NAME = "farm_municipality_codes";
    private static final LocalDate EFFECTIVE_DATE = LocalDate.now();

    @Test
    @Order(1)
    public void testGetCodeTable() throws Exception {

        mockMvc.perform(get("/codeTables/" + TABLE_NAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("CodeTableModel"))
                .andExpect(jsonPath("$.codeTableName").value(TABLE_NAME))
                .andExpect(jsonPath("$.codeTableDescriptiveName").value(TABLE_NAME))
                .andExpect(jsonPath("$.codes[0].code").value("41"))
                .andExpect(jsonPath("$.codes[0].description").value("Cariboo"))
                .andExpect(jsonPath("$.codes[0].effectiveDate").value(EFFECTIVE_DATE.toString()))
                .andExpect(jsonPath("$.codes[0].expiryDate").value("9999-12-31"))
                .andExpect(jsonPath("$.codes[1].code").value("43"))
                .andExpect(jsonPath("$.codes[1].description").value("Mount Waddington (Island part)"))
                .andExpect(jsonPath("$.codes[1].effectiveDate").value(EFFECTIVE_DATE.toString()))
                .andExpect(jsonPath("$.codes[1].expiryDate").value("9999-12-31"));
    }
}
