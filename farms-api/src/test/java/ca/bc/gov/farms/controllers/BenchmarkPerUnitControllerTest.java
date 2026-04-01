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

import ca.bc.gov.farms.data.models.BenchmarkPerUnitModel;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BenchmarkPerUnitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("null")
    @Test
    @Order(1)
    public void testCreateBenchmarkPerUnit() throws Exception {

        BenchmarkPerUnitModel resource = new BenchmarkPerUnitModel();
        resource.setProgramYear(2024);
        resource.setUnitComment("Alfalfa Dehy");
        resource.setExpiryDate(null);
        resource.setMunicipalityCode("41");
        resource.setInventoryCode("5560");
        resource.setYearMinus6Margin(new BigDecimal("106.43"));
        resource.setYearMinus5Margin(new BigDecimal("128.79"));
        resource.setYearMinus4Margin(new BigDecimal("127.41"));
        resource.setYearMinus3Margin(new BigDecimal("109.64"));
        resource.setYearMinus2Margin(new BigDecimal("95.13"));
        resource.setYearMinus1Margin(new BigDecimal("0.00"));
        resource.setYearMinus6Expense(new BigDecimal("151.44"));
        resource.setYearMinus5Expense(new BigDecimal("156.59"));
        resource.setYearMinus4Expense(new BigDecimal("140.79"));
        resource.setYearMinus3Expense(new BigDecimal("186.58"));
        resource.setYearMinus2Expense(new BigDecimal("258.28"));
        resource.setYearMinus1Expense(new BigDecimal("258.28"));
        resource.setUrlId(1L);
        resource.setUserEmail("jsmith@gmail.com");

        mockMvc.perform(post("/benchmarkPerUnits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.@type").value("BenchmarkPerUnitModel"))
                .andExpect(jsonPath("$.benchmarkPerUnitId").value(60584))
                .andExpect(jsonPath("$.programYear").value(2024))
                .andExpect(jsonPath("$.unitComment").value("Alfalfa Dehy"))
                .andExpect(jsonPath("$.expiryDate").value(nullValue()))
                .andExpect(jsonPath("$.municipalityCode").value("41"))
                .andExpect(jsonPath("$.municipalityDesc").value("Cariboo"))
                .andExpect(jsonPath("$.inventoryItemCode").value("5560"))
                .andExpect(jsonPath("$.inventoryItemDesc").value("Alfalfa Dehy"))
                .andExpect(jsonPath("$.structureGroupCode").value(nullValue()))
                .andExpect(jsonPath("$.structureGroupDesc").value(nullValue()))
                .andExpect(jsonPath("$.inventoryCode").value("5560"))
                .andExpect(jsonPath("$.inventoryDesc").value("Alfalfa Dehy"))
                .andExpect(jsonPath("$.yearMinus6Margin").value(106.43))
                .andExpect(jsonPath("$.yearMinus5Margin").value(128.79))
                .andExpect(jsonPath("$.yearMinus4Margin").value(127.41))
                .andExpect(jsonPath("$.yearMinus3Margin").value(109.64))
                .andExpect(jsonPath("$.yearMinus2Margin").value(95.13))
                .andExpect(jsonPath("$.yearMinus1Margin").value(0.00))
                .andExpect(jsonPath("$.yearMinus6Expense").value(151.44))
                .andExpect(jsonPath("$.yearMinus5Expense").value(156.59))
                .andExpect(jsonPath("$.yearMinus4Expense").value(140.79))
                .andExpect(jsonPath("$.yearMinus3Expense").value(186.58))
                .andExpect(jsonPath("$.yearMinus2Expense").value(258.28))
                .andExpect(jsonPath("$.yearMinus1Expense").value(258.28))
                .andExpect(jsonPath("$.urlId").value(1))
                .andExpect(jsonPath("$.url").value("https://google.com"))
                .andExpect(jsonPath("$.userEmail").value(nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(2)
    public void testGetBenchmarkPerUnitsByProgramYear() throws Exception {

        mockMvc.perform(get("/benchmarkPerUnits")
                .param("programYear", "2024"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("BenchmarkPerUnitListModel"))
                .andExpect(jsonPath("$.benchmarkPerUnitList[0].@type").value("BenchmarkPerUnitModel"))
                .andExpect(jsonPath("$.benchmarkPerUnitList[0].benchmarkPerUnitId").value(60584))
                .andExpect(jsonPath("$.benchmarkPerUnitList[0].programYear").value(2024))
                .andExpect(jsonPath("$.benchmarkPerUnitList[0].unitComment").value("Alfalfa Dehy"))
                .andExpect(jsonPath("$.benchmarkPerUnitList[0].expiryDate").value(nullValue()))
                .andExpect(jsonPath("$.benchmarkPerUnitList[0].municipalityCode").value("41"))
                .andExpect(jsonPath("$.benchmarkPerUnitList[0].municipalityDesc").value("Cariboo"))
                .andExpect(jsonPath("$.benchmarkPerUnitList[0].inventoryItemCode").value("5560"))
                .andExpect(jsonPath("$.benchmarkPerUnitList[0].inventoryItemDesc").value("Alfalfa Dehy"))
                .andExpect(jsonPath("$.benchmarkPerUnitList[0].structureGroupCode").value(nullValue()))
                .andExpect(jsonPath("$.benchmarkPerUnitList[0].structureGroupDesc").value(nullValue()))
                .andExpect(jsonPath("$.benchmarkPerUnitList[0].inventoryCode").value("5560"))
                .andExpect(jsonPath("$.benchmarkPerUnitList[0].inventoryDesc").value("Alfalfa Dehy"))
                .andExpect(jsonPath("$.benchmarkPerUnitList[0].yearMinus6Margin").value(106.43))
                .andExpect(jsonPath("$.benchmarkPerUnitList[0].yearMinus5Margin").value(128.79))
                .andExpect(jsonPath("$.benchmarkPerUnitList[0].yearMinus4Margin").value(127.41))
                .andExpect(jsonPath("$.benchmarkPerUnitList[0].yearMinus3Margin").value(109.64))
                .andExpect(jsonPath("$.benchmarkPerUnitList[0].yearMinus2Margin").value(95.13))
                .andExpect(jsonPath("$.benchmarkPerUnitList[0].yearMinus1Margin").value(0.00))
                .andExpect(jsonPath("$.benchmarkPerUnitList[0].yearMinus6Expense").value(151.44))
                .andExpect(jsonPath("$.benchmarkPerUnitList[0].yearMinus5Expense").value(156.59))
                .andExpect(jsonPath("$.benchmarkPerUnitList[0].yearMinus4Expense").value(140.79))
                .andExpect(jsonPath("$.benchmarkPerUnitList[0].yearMinus3Expense").value(186.58))
                .andExpect(jsonPath("$.benchmarkPerUnitList[0].yearMinus2Expense").value(258.28))
                .andExpect(jsonPath("$.benchmarkPerUnitList[0].yearMinus1Expense").value(258.28))
                .andExpect(jsonPath("$.benchmarkPerUnitList[0].urlId").value(1))
                .andExpect(jsonPath("$.benchmarkPerUnitList[0].url").value("https://google.com"))
                .andExpect(jsonPath("$.benchmarkPerUnitList[0].userEmail").value(nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(3)
    public void testGetBenchmarkPerUnit() throws Exception {

        mockMvc.perform(get("/benchmarkPerUnits/60584"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("BenchmarkPerUnitModel"))
                .andExpect(jsonPath("$.benchmarkPerUnitId").value(60584))
                .andExpect(jsonPath("$.programYear").value(2024))
                .andExpect(jsonPath("$.unitComment").value("Alfalfa Dehy"))
                .andExpect(jsonPath("$.expiryDate").value(nullValue()))
                .andExpect(jsonPath("$.municipalityCode").value("41"))
                .andExpect(jsonPath("$.municipalityDesc").value("Cariboo"))
                .andExpect(jsonPath("$.inventoryItemCode").value("5560"))
                .andExpect(jsonPath("$.inventoryItemDesc").value("Alfalfa Dehy"))
                .andExpect(jsonPath("$.structureGroupCode").value(nullValue()))
                .andExpect(jsonPath("$.structureGroupDesc").value(nullValue()))
                .andExpect(jsonPath("$.inventoryCode").value("5560"))
                .andExpect(jsonPath("$.inventoryDesc").value("Alfalfa Dehy"))
                .andExpect(jsonPath("$.yearMinus6Margin").value(106.43))
                .andExpect(jsonPath("$.yearMinus5Margin").value(128.79))
                .andExpect(jsonPath("$.yearMinus4Margin").value(127.41))
                .andExpect(jsonPath("$.yearMinus3Margin").value(109.64))
                .andExpect(jsonPath("$.yearMinus2Margin").value(95.13))
                .andExpect(jsonPath("$.yearMinus1Margin").value(0.00))
                .andExpect(jsonPath("$.yearMinus6Expense").value(151.44))
                .andExpect(jsonPath("$.yearMinus5Expense").value(156.59))
                .andExpect(jsonPath("$.yearMinus4Expense").value(140.79))
                .andExpect(jsonPath("$.yearMinus3Expense").value(186.58))
                .andExpect(jsonPath("$.yearMinus2Expense").value(258.28))
                .andExpect(jsonPath("$.yearMinus1Expense").value(258.28))
                .andExpect(jsonPath("$.urlId").value(1))
                .andExpect(jsonPath("$.url").value("https://google.com"))
                .andExpect(jsonPath("$.userEmail").value(nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(4)
    public void testUpdateBenchmarkPerUnit() throws Exception {

        BenchmarkPerUnitModel resource = new BenchmarkPerUnitModel();
        resource.setBenchmarkPerUnitId(60584L);
        resource.setProgramYear(2024);
        resource.setUnitComment("Greenfeed");
        resource.setExpiryDate(null);
        resource.setMunicipalityCode("43");
        resource.setInventoryCode("5562");
        resource.setYearMinus6Margin(new BigDecimal("1106.43"));
        resource.setYearMinus5Margin(new BigDecimal("1128.79"));
        resource.setYearMinus4Margin(new BigDecimal("1127.41"));
        resource.setYearMinus3Margin(new BigDecimal("1109.64"));
        resource.setYearMinus2Margin(new BigDecimal("1095.13"));
        resource.setYearMinus1Margin(new BigDecimal("1000.00"));
        resource.setYearMinus6Expense(new BigDecimal("1151.44"));
        resource.setYearMinus5Expense(new BigDecimal("1156.59"));
        resource.setYearMinus4Expense(new BigDecimal("1140.79"));
        resource.setYearMinus3Expense(new BigDecimal("1186.58"));
        resource.setYearMinus2Expense(new BigDecimal("1258.28"));
        resource.setYearMinus1Expense(new BigDecimal("1258.28"));
        resource.setUrlId(2L);
        resource.setUserEmail("jsmith@gmail.com");

        mockMvc.perform(put("/benchmarkPerUnits/60584")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("BenchmarkPerUnitModel"))
                .andExpect(jsonPath("$.benchmarkPerUnitId").value(60584))
                .andExpect(jsonPath("$.programYear").value(2024))
                .andExpect(jsonPath("$.unitComment").value("Greenfeed"))
                .andExpect(jsonPath("$.expiryDate").value(nullValue()))
                .andExpect(jsonPath("$.municipalityCode").value("43"))
                .andExpect(jsonPath("$.municipalityDesc").value("Mount Waddington (Island part)"))
                .andExpect(jsonPath("$.inventoryItemCode").value("5562"))
                .andExpect(jsonPath("$.inventoryItemDesc").value("Greenfeed"))
                .andExpect(jsonPath("$.structureGroupCode").value(nullValue()))
                .andExpect(jsonPath("$.structureGroupDesc").value(nullValue()))
                .andExpect(jsonPath("$.inventoryCode").value("5562"))
                .andExpect(jsonPath("$.inventoryDesc").value("Greenfeed"))
                .andExpect(jsonPath("$.yearMinus6Margin").value(1106.43))
                .andExpect(jsonPath("$.yearMinus5Margin").value(1128.79))
                .andExpect(jsonPath("$.yearMinus4Margin").value(1127.41))
                .andExpect(jsonPath("$.yearMinus3Margin").value(1109.64))
                .andExpect(jsonPath("$.yearMinus2Margin").value(1095.13))
                .andExpect(jsonPath("$.yearMinus1Margin").value(1000.00))
                .andExpect(jsonPath("$.yearMinus6Expense").value(1151.44))
                .andExpect(jsonPath("$.yearMinus5Expense").value(1156.59))
                .andExpect(jsonPath("$.yearMinus4Expense").value(1140.79))
                .andExpect(jsonPath("$.yearMinus3Expense").value(1186.58))
                .andExpect(jsonPath("$.yearMinus2Expense").value(1258.28))
                .andExpect(jsonPath("$.yearMinus1Expense").value(1258.28))
                .andExpect(jsonPath("$.urlId").value(2))
                .andExpect(jsonPath("$.url").value("https://microsoft.com"))
                .andExpect(jsonPath("$.userEmail").value(nullValue()));
    }

    @Test
    @Order(5)
    public void testDeleteBenchmarkPerUnit() throws Exception {

        mockMvc.perform(delete("/benchmarkPerUnits/60584"))
                .andExpect(status().isNoContent());
    }
}
