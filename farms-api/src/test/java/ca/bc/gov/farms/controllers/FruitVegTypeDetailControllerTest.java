package ca.bc.gov.farms.controllers;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
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

import ca.bc.gov.farms.data.models.FruitVegTypeDetailModel;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FruitVegTypeDetailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("null")
    @Test
    @Order(1)
    public void testCreateFruitVegTypeDetail() throws Exception {

        FruitVegTypeDetailModel resource = new FruitVegTypeDetailModel();
        resource.setFruitVegTypeCode("SALAK");
        resource.setFruitVegTypeDesc("Tropical Fruit");
        resource.setRevenueVarianceLimit(new BigDecimal("20.000"));
        resource.setUserEmail("jsmith@gmail.com");

        mockMvc.perform(post("/fruitVegTypeDetails")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.@type").value("FruitVegTypeDetailModel"))
                .andExpect(jsonPath("$.fruitVegTypeCode").value("SALAK"))
                .andExpect(jsonPath("$.fruitVegTypeDesc").value("Tropical Fruit"))
                .andExpect(jsonPath("$.establishedDate").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.expiryDate").value("9999-12-31"))
                .andExpect(jsonPath("$.revenueVarianceLimit").value(20.000))
                .andExpect(jsonPath("$.userEmail").value(nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(2)
    public void testGetAllFruitVegTypeDetails() throws Exception {

        mockMvc.perform(get("/fruitVegTypeDetails")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("FruitVegTypeDetailListModel"))
                .andExpect(jsonPath("$.fruitVegTypeDetailList[0].@type").value("FruitVegTypeDetailModel"))
                .andExpect(jsonPath("$.fruitVegTypeDetailList[0].fruitVegTypeCode").value("SALAK"))
                .andExpect(jsonPath("$.fruitVegTypeDetailList[0].fruitVegTypeDesc").value("Tropical Fruit"))
                .andExpect(jsonPath("$.fruitVegTypeDetailList[0].establishedDate").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.fruitVegTypeDetailList[0].expiryDate").value("9999-12-31"))
                .andExpect(jsonPath("$.fruitVegTypeDetailList[0].revenueVarianceLimit").value(20.000))
                .andExpect(jsonPath("$.fruitVegTypeDetailList[0].userEmail").value(nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(3)
    public void testGetFruitVegTypeDetail() throws Exception {

        mockMvc.perform(get("/fruitVegTypeDetails/SALAK")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("FruitVegTypeDetailModel"))
                .andExpect(jsonPath("$.fruitVegTypeCode").value("SALAK"))
                .andExpect(jsonPath("$.fruitVegTypeDesc").value("Tropical Fruit"))
                .andExpect(jsonPath("$.establishedDate").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.expiryDate").value("9999-12-31"))
                .andExpect(jsonPath("$.revenueVarianceLimit").value(20.000))
                .andExpect(jsonPath("$.userEmail").value(nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(4)
    public void testUpdateFruitVegTypeDetail() throws Exception {

        FruitVegTypeDetailModel resource = new FruitVegTypeDetailModel();
        resource.setFruitVegTypeCode("SALAK");
        resource.setFruitVegTypeDesc("King of Fruits");
        resource.setRevenueVarianceLimit(new BigDecimal("30.000"));
        resource.setUserEmail("jsmith@gmail.com");

        mockMvc.perform(put("/fruitVegTypeDetails/SALAK")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("FruitVegTypeDetailModel"))
                .andExpect(jsonPath("$.fruitVegTypeCode").value("SALAK"))
                .andExpect(jsonPath("$.fruitVegTypeDesc").value("King of Fruits"))
                .andExpect(jsonPath("$.establishedDate").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.expiryDate").value("9999-12-31"))
                .andExpect(jsonPath("$.revenueVarianceLimit").value(30.000))
                .andExpect(jsonPath("$.userEmail").value(nullValue()));
    }

    @Test
    @Order(5)
    public void testDeleteFruitVegTypeDetail() throws Exception {

        mockMvc.perform(delete("/fruitVegTypeDetails/SALAK"))
                .andExpect(status().isNoContent());
    }
}
