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

import ca.bc.gov.farms.data.models.MarketRatePremiumModel;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MarketRatePremiumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("null")
    @Test
    @Order(1)
    public void testCreateMarketRatePremium() throws Exception {

        MarketRatePremiumModel resource = new MarketRatePremiumModel();
        resource.setMinTotalPremiumAmount(new BigDecimal("0.00"));
        resource.setMaxTotalPremiumAmount(new BigDecimal("1.00"));
        resource.setRiskChargeFlatAmount(new BigDecimal("2.00"));
        resource.setRiskChargePctPremium(new BigDecimal("3.00"));
        resource.setAdjustChargeFlatAmount(new BigDecimal("4.00"));
        resource.setUserEmail("jsmith@gmail.com");

        mockMvc.perform(post("/marketRatePremiums")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.@type").value("MarketRatePremiumModel"))
                .andExpect(jsonPath("$.marketRatePremiumId").value(21))
                .andExpect(jsonPath("$.minTotalPremiumAmount").value(0.00))
                .andExpect(jsonPath("$.maxTotalPremiumAmount").value(1.00))
                .andExpect(jsonPath("$.riskChargeFlatAmount").value(2.00))
                .andExpect(jsonPath("$.riskChargePctPremium").value(3.00))
                .andExpect(jsonPath("$.adjustChargeFlatAmount").value(4.00))
                .andExpect(jsonPath("$.userEmail", nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(2)
    public void testGetAllMarketRatePremiums() throws Exception {

        mockMvc.perform(get("/marketRatePremiums")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("MarketRatePremiumListModel"))
                .andExpect(jsonPath("$.marketRatePremiumList[0].@type").value("MarketRatePremiumModel"))
                .andExpect(jsonPath("$.marketRatePremiumList[0].marketRatePremiumId").value(21))
                .andExpect(jsonPath("$.marketRatePremiumList[0].minTotalPremiumAmount").value(0.00))
                .andExpect(jsonPath("$.marketRatePremiumList[0].maxTotalPremiumAmount").value(1.00))
                .andExpect(jsonPath("$.marketRatePremiumList[0].riskChargeFlatAmount").value(2.00))
                .andExpect(jsonPath("$.marketRatePremiumList[0].riskChargePctPremium").value(3.00))
                .andExpect(jsonPath("$.marketRatePremiumList[0].adjustChargeFlatAmount").value(4.00))
                .andExpect(jsonPath("$.marketRatePremiumList[0].userEmail", nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(3)
    public void testGetMarketRatePremium() throws Exception {

        mockMvc.perform(get("/marketRatePremiums/21")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("MarketRatePremiumModel"))
                .andExpect(jsonPath("$.marketRatePremiumId").value(21))
                .andExpect(jsonPath("$.minTotalPremiumAmount").value(0.00))
                .andExpect(jsonPath("$.maxTotalPremiumAmount").value(1.00))
                .andExpect(jsonPath("$.riskChargeFlatAmount").value(2.00))
                .andExpect(jsonPath("$.riskChargePctPremium").value(3.00))
                .andExpect(jsonPath("$.adjustChargeFlatAmount").value(4.00))
                .andExpect(jsonPath("$.userEmail", nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(4)
    public void testUpdateMarketRatePremium() throws Exception {

        MarketRatePremiumModel resource = new MarketRatePremiumModel();
        resource.setMarketRatePremiumId(21L);
        resource.setMinTotalPremiumAmount(new BigDecimal("1.00"));
        resource.setMaxTotalPremiumAmount(new BigDecimal("2.00"));
        resource.setRiskChargeFlatAmount(new BigDecimal("3.00"));
        resource.setRiskChargePctPremium(new BigDecimal("4.00"));
        resource.setAdjustChargeFlatAmount(new BigDecimal("5.00"));
        resource.setUserEmail("jsmith@gmail.com");

        mockMvc.perform(put("/marketRatePremiums/21")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("MarketRatePremiumModel"))
                .andExpect(jsonPath("$.marketRatePremiumId").value(21))
                .andExpect(jsonPath("$.minTotalPremiumAmount").value(1.00))
                .andExpect(jsonPath("$.maxTotalPremiumAmount").value(2.00))
                .andExpect(jsonPath("$.riskChargeFlatAmount").value(3.00))
                .andExpect(jsonPath("$.riskChargePctPremium").value(4.00))
                .andExpect(jsonPath("$.adjustChargeFlatAmount").value(5.00))
                .andExpect(jsonPath("$.userEmail", nullValue()));
    }

    @Test
    @Order(5)
    public void testDeleteMarketRatePremium() throws Exception {

        mockMvc.perform(delete("/marketRatePremiums/21"))
                .andExpect(status().isNoContent());
    }
}
