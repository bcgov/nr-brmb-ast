package ca.bc.gov.farms.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductiveUnitCodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    public void testGetAllProductiveUnitCodes() throws Exception {

        mockMvc.perform(get("/productiveUnitCodes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("ProductiveUnitCodeListModel"))
                .andExpect(jsonPath("$.productiveUnitCodeList[0].@type").value("ProductiveUnitCodeModel"))
                .andExpect(jsonPath("$.productiveUnitCodeList[0].code").value("100"))
                .andExpect(jsonPath("$.productiveUnitCodeList[0].description").value("Alpaca"));
    }
}
