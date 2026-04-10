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

import ca.bc.gov.farms.data.models.InventoryItemDetailRsrc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InventoryItemDetailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("null")
    @Test
    @Order(1)
    public void testCreateInventoryItemDetail() throws Exception {

        InventoryItemDetailRsrc resource = new InventoryItemDetailRsrc();
        resource.setProgramYear(2025);
        resource.setEligibilityInd("Y");
        resource.setLineItem(null);
        resource.setInsurableValue(new BigDecimal("0.112"));
        resource.setPremiumRate(new BigDecimal("0.1200"));
        resource.setInventoryItemCode("73");
        resource.setCommodityTypeCode("GRAIN");
        resource.setFruitVegTypeCode("APPLE");
        resource.setMultiStageCommdtyCode(null);
        resource.setUrlId(1L);
        resource.setUserEmail("jsmith@gmail.com");

        mockMvc.perform(post("/inventoryItemDetails")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.@type").value("InventoryItemDetailRsrc"))
                .andExpect(jsonPath("$.inventoryItemDetailId").value(55361))
                .andExpect(jsonPath("$.programYear").value(2025))
                .andExpect(jsonPath("$.eligibilityInd").value("Y"))
                .andExpect(jsonPath("$.lineItem").value(nullValue()))
                .andExpect(jsonPath("$.insurableValue").value(0.112))
                .andExpect(jsonPath("$.premiumRate").value(0.1200))
                .andExpect(jsonPath("$.inventoryItemCode").value("73"))
                .andExpect(jsonPath("$.inventoryItemDesc").value("Strawberries"))
                .andExpect(jsonPath("$.commodityTypeCode").value("GRAIN"))
                .andExpect(jsonPath("$.commodityTypeDesc").value("Grain"))
                .andExpect(jsonPath("$.fruitVegTypeCode").value("APPLE"))
                .andExpect(jsonPath("$.fruitVegTypeDesc").value("Apples"))
                .andExpect(jsonPath("$.multiStageCommdtyCode").value(nullValue()))
                .andExpect(jsonPath("$.multiStageCommdtyDesc").value(nullValue()))
                .andExpect(jsonPath("$.urlId").value(1))
                .andExpect(jsonPath("$.url").value("https://google.com"))
                .andExpect(jsonPath("$.userEmail").value(nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(2)
    public void testGetInventoryItemDetailsByInventoryItemCode() throws Exception {

        mockMvc.perform(get("/inventoryItemDetails")
                .param("inventoryItemCode", "73"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("InventoryItemDetailListRsrc"))
                .andExpect(jsonPath("$.inventoryItemDetailList[0].@type").value("InventoryItemDetailRsrc"))
                .andExpect(jsonPath("$.inventoryItemDetailList[0].inventoryItemDetailId").value(55361))
                .andExpect(jsonPath("$.inventoryItemDetailList[0].programYear").value(2025))
                .andExpect(jsonPath("$.inventoryItemDetailList[0].eligibilityInd").value("Y"))
                .andExpect(jsonPath("$.inventoryItemDetailList[0].lineItem").value(nullValue()))
                .andExpect(jsonPath("$.inventoryItemDetailList[0].insurableValue").value(0.112))
                .andExpect(jsonPath("$.inventoryItemDetailList[0].premiumRate").value(0.1200))
                .andExpect(jsonPath("$.inventoryItemDetailList[0].inventoryItemCode").value("73"))
                .andExpect(jsonPath("$.inventoryItemDetailList[0].inventoryItemDesc").value("Strawberries"))
                .andExpect(jsonPath("$.inventoryItemDetailList[0].commodityTypeCode").value("GRAIN"))
                .andExpect(jsonPath("$.inventoryItemDetailList[0].commodityTypeDesc").value("Grain"))
                .andExpect(jsonPath("$.inventoryItemDetailList[0].fruitVegTypeCode").value("APPLE"))
                .andExpect(jsonPath("$.inventoryItemDetailList[0].fruitVegTypeDesc").value("Apples"))
                .andExpect(jsonPath("$.inventoryItemDetailList[0].multiStageCommdtyCode").value(nullValue()))
                .andExpect(jsonPath("$.inventoryItemDetailList[0].multiStageCommdtyDesc").value(nullValue()))
                .andExpect(jsonPath("$.inventoryItemDetailList[0].urlId").value(1))
                .andExpect(jsonPath("$.inventoryItemDetailList[0].url").value("https://google.com"))
                .andExpect(jsonPath("$.inventoryItemDetailList[0].userEmail").value(nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(3)
    public void testGetInventoryItemDetail() throws Exception {

        mockMvc.perform(get("/inventoryItemDetails/55361"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("InventoryItemDetailRsrc"))
                .andExpect(jsonPath("$.inventoryItemDetailId").value(55361))
                .andExpect(jsonPath("$.programYear").value(2025))
                .andExpect(jsonPath("$.eligibilityInd").value("Y"))
                .andExpect(jsonPath("$.lineItem").value(nullValue()))
                .andExpect(jsonPath("$.insurableValue").value(0.112))
                .andExpect(jsonPath("$.premiumRate").value(0.1200))
                .andExpect(jsonPath("$.inventoryItemCode").value("73"))
                .andExpect(jsonPath("$.inventoryItemDesc").value("Strawberries"))
                .andExpect(jsonPath("$.commodityTypeCode").value("GRAIN"))
                .andExpect(jsonPath("$.commodityTypeDesc").value("Grain"))
                .andExpect(jsonPath("$.fruitVegTypeCode").value("APPLE"))
                .andExpect(jsonPath("$.fruitVegTypeDesc").value("Apples"))
                .andExpect(jsonPath("$.multiStageCommdtyCode").value(nullValue()))
                .andExpect(jsonPath("$.multiStageCommdtyDesc").value(nullValue()))
                .andExpect(jsonPath("$.urlId").value(1))
                .andExpect(jsonPath("$.url").value("https://google.com"))
                .andExpect(jsonPath("$.userEmail").value(nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(4)
    public void testUpdateInventoryItemDetail() throws Exception {

        InventoryItemDetailRsrc resource = new InventoryItemDetailRsrc();
        resource.setInventoryItemDetailId(55361L);
        resource.setProgramYear(2026);
        resource.setEligibilityInd("N");
        resource.setLineItem(null);
        resource.setInsurableValue(new BigDecimal("0.212"));
        resource.setPremiumRate(new BigDecimal("0.2200"));
        resource.setInventoryItemCode("5560");
        resource.setCommodityTypeCode("FORAGE");
        resource.setFruitVegTypeCode("APRICOT");
        resource.setMultiStageCommdtyCode(null);
        resource.setUrlId(2L);
        resource.setUserEmail("jsmith@gmail.com");

        mockMvc.perform(put("/inventoryItemDetails/55361")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("InventoryItemDetailRsrc"))
                .andExpect(jsonPath("$.inventoryItemDetailId").value(55361))
                .andExpect(jsonPath("$.programYear").value(2026))
                .andExpect(jsonPath("$.eligibilityInd").value("N"))
                .andExpect(jsonPath("$.lineItem").value(nullValue()))
                .andExpect(jsonPath("$.insurableValue").value(0.212))
                .andExpect(jsonPath("$.premiumRate").value(0.2200))
                .andExpect(jsonPath("$.inventoryItemCode").value("5560"))
                .andExpect(jsonPath("$.inventoryItemDesc").value("Alfalfa Dehy"))
                .andExpect(jsonPath("$.commodityTypeCode").value("FORAGE"))
                .andExpect(jsonPath("$.commodityTypeDesc").value("Forage"))
                .andExpect(jsonPath("$.fruitVegTypeCode").value("APRICOT"))
                .andExpect(jsonPath("$.fruitVegTypeDesc").value("Apricots"))
                .andExpect(jsonPath("$.multiStageCommdtyCode").value(nullValue()))
                .andExpect(jsonPath("$.multiStageCommdtyDesc").value(nullValue()))
                .andExpect(jsonPath("$.urlId").value(2))
                .andExpect(jsonPath("$.url").value("https://microsoft.com"))
                .andExpect(jsonPath("$.userEmail").value(nullValue()));
    }

    @Test
    @Order(5)
    public void testDeleteInventoryItemDetail() throws Exception {

        mockMvc.perform(delete("/inventoryItemDetails/55361"))
                .andExpect(status().isNoContent());
    }
}
