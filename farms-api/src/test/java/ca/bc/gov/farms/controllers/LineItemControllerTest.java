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

import ca.bc.gov.farms.data.models.LineItemModel;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LineItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("null")
    @Test
    @Order(1)
    public void testCreateLineItem() throws Exception {

        LineItemModel resource = new LineItemModel();
        resource.setProgramYear(2025);
        resource.setLineItem(9798);
        resource.setDescription("Agricultural Contract work");
        resource.setProvince("BC");
        resource.setEligibilityInd("N");
        resource.setEligibilityForRefYearsInd("N");
        resource.setYardageInd("N");
        resource.setProgramPaymentInd("N");
        resource.setContractWorkInd("N");
        resource.setSupplyManagedCommodityInd("N");
        resource.setExcludeFromRevenueCalcInd("N");
        resource.setIndustryAverageExpenseInd("N");
        resource.setCommodityTypeCode(null);
        resource.setFruitVegTypeCode(null);
        resource.setUserEmail("jsmith@gmail.com");

        mockMvc.perform(post("/lineItems")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.@type").value("LineItemModel"))
                .andExpect(jsonPath("$.lineItemId").value(39909))
                .andExpect(jsonPath("$.programYear").value(2025))
                .andExpect(jsonPath("$.lineItem").value(9798))
                .andExpect(jsonPath("$.description").value("Agricultural Contract work"))
                .andExpect(jsonPath("$.province").value("BC"))
                .andExpect(jsonPath("$.eligibilityInd").value("N"))
                .andExpect(jsonPath("$.eligibilityForRefYearsInd").value("N"))
                .andExpect(jsonPath("$.yardageInd").value("N"))
                .andExpect(jsonPath("$.programPaymentInd").value("N"))
                .andExpect(jsonPath("$.contractWorkInd").value("N"))
                .andExpect(jsonPath("$.supplyManagedCommodityInd").value("N"))
                .andExpect(jsonPath("$.excludeFromRevenueCalcInd").value("N"))
                .andExpect(jsonPath("$.industryAverageExpenseInd").value("N"))
                .andExpect(jsonPath("$.commodityTypeCode", nullValue()))
                .andExpect(jsonPath("$.fruitVegTypeCode", nullValue()))
                .andExpect(jsonPath("$.userEmail", nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(2)
    public void testGetLineItemsByProgramYear() throws Exception {

        mockMvc.perform(get("/lineItems")
                .param("programYear", "2025"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("LineItemListRsrc"))
                .andExpect(jsonPath("$.lineItemList[0].@type").value("LineItemModel"))
                .andExpect(jsonPath("$.lineItemList[0].lineItemId").value(39909))
                .andExpect(jsonPath("$.lineItemList[0].programYear").value(2025))
                .andExpect(jsonPath("$.lineItemList[0].lineItem").value(9798))
                .andExpect(jsonPath("$.lineItemList[0].description").value("Agricultural Contract work"))
                .andExpect(jsonPath("$.lineItemList[0].province").value("BC"))
                .andExpect(jsonPath("$.lineItemList[0].eligibilityInd").value("N"))
                .andExpect(jsonPath("$.lineItemList[0].eligibilityForRefYearsInd").value("N"))
                .andExpect(jsonPath("$.lineItemList[0].yardageInd").value("N"))
                .andExpect(jsonPath("$.lineItemList[0].programPaymentInd").value("N"))
                .andExpect(jsonPath("$.lineItemList[0].contractWorkInd").value("N"))
                .andExpect(jsonPath("$.lineItemList[0].supplyManagedCommodityInd").value("N"))
                .andExpect(jsonPath("$.lineItemList[0].excludeFromRevenueCalcInd").value("N"))
                .andExpect(jsonPath("$.lineItemList[0].industryAverageExpenseInd").value("N"))
                .andExpect(jsonPath("$.lineItemList[0].commodityTypeCode", nullValue()))
                .andExpect(jsonPath("$.lineItemList[0].fruitVegTypeCode", nullValue()))
                .andExpect(jsonPath("$.lineItemList[0].userEmail", nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(3)
    public void testGetLineItem() throws Exception {

        mockMvc.perform(get("/lineItems/39909"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("LineItemModel"))
                .andExpect(jsonPath("$.lineItemId").value(39909))
                .andExpect(jsonPath("$.programYear").value(2025))
                .andExpect(jsonPath("$.lineItem").value(9798))
                .andExpect(jsonPath("$.description").value("Agricultural Contract work"))
                .andExpect(jsonPath("$.province").value("BC"))
                .andExpect(jsonPath("$.eligibilityInd").value("N"))
                .andExpect(jsonPath("$.eligibilityForRefYearsInd").value("N"))
                .andExpect(jsonPath("$.yardageInd").value("N"))
                .andExpect(jsonPath("$.programPaymentInd").value("N"))
                .andExpect(jsonPath("$.contractWorkInd").value("N"))
                .andExpect(jsonPath("$.supplyManagedCommodityInd").value("N"))
                .andExpect(jsonPath("$.excludeFromRevenueCalcInd").value("N"))
                .andExpect(jsonPath("$.industryAverageExpenseInd").value("N"))
                .andExpect(jsonPath("$.commodityTypeCode", nullValue()))
                .andExpect(jsonPath("$.fruitVegTypeCode", nullValue()))
                .andExpect(jsonPath("$.userEmail", nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(4)
    public void testUpdateLineItem() throws Exception {

        LineItemModel resource = new LineItemModel();
        resource.setLineItemId(39909L);
        resource.setProgramYear(2025);
        resource.setLineItem(9798);
        resource.setDescription("Agricultural Contract works");
        resource.setProvince("AB");
        resource.setEligibilityInd("Y");
        resource.setEligibilityForRefYearsInd("Y");
        resource.setYardageInd("Y");
        resource.setProgramPaymentInd("Y");
        resource.setContractWorkInd("Y");
        resource.setSupplyManagedCommodityInd("Y");
        resource.setExcludeFromRevenueCalcInd("Y");
        resource.setIndustryAverageExpenseInd("Y");
        resource.setCommodityTypeCode("GRAIN");
        resource.setFruitVegTypeCode("APPLE");
        resource.setUserEmail("jsmith@gmail.com");

        mockMvc.perform(put("/lineItems/39909")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("LineItemModel"))
                .andExpect(jsonPath("$.lineItemId").value(39910))
                .andExpect(jsonPath("$.programYear").value(2025))
                .andExpect(jsonPath("$.lineItem").value(9798))
                .andExpect(jsonPath("$.description").value("Agricultural Contract works"))
                .andExpect(jsonPath("$.province").value("AB"))
                .andExpect(jsonPath("$.eligibilityInd").value("Y"))
                .andExpect(jsonPath("$.eligibilityForRefYearsInd").value("Y"))
                .andExpect(jsonPath("$.yardageInd").value("Y"))
                .andExpect(jsonPath("$.programPaymentInd").value("Y"))
                .andExpect(jsonPath("$.contractWorkInd").value("Y"))
                .andExpect(jsonPath("$.supplyManagedCommodityInd").value("Y"))
                .andExpect(jsonPath("$.excludeFromRevenueCalcInd").value("Y"))
                .andExpect(jsonPath("$.industryAverageExpenseInd").value("Y"))
                .andExpect(jsonPath("$.commodityTypeCode").value("GRAIN"))
                .andExpect(jsonPath("$.fruitVegTypeCode").value("APPLE"))
                .andExpect(jsonPath("$.userEmail", nullValue()));
    }

    @SuppressWarnings("null")
    @Test
    @Order(5)
    public void testCopyLineItems() throws Exception {

        mockMvc.perform(post("/lineItems/copy/2026")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.@type").value("LineItemListRsrc"))
                .andExpect(jsonPath("$.lineItemList[0].@type").value("LineItemModel"))
                .andExpect(jsonPath("$.lineItemList[0].lineItemId").value(39911))
                .andExpect(jsonPath("$.lineItemList[0].programYear").value(2026))
                .andExpect(jsonPath("$.lineItemList[0].lineItem").value(9798))
                .andExpect(jsonPath("$.lineItemList[0].description").value("Agricultural Contract works"))
                .andExpect(jsonPath("$.lineItemList[0].province").value("AB"))
                .andExpect(jsonPath("$.lineItemList[0].eligibilityInd").value("Y"))
                .andExpect(jsonPath("$.lineItemList[0].eligibilityForRefYearsInd").value("Y"))
                .andExpect(jsonPath("$.lineItemList[0].yardageInd").value("Y"))
                .andExpect(jsonPath("$.lineItemList[0].programPaymentInd").value("Y"))
                .andExpect(jsonPath("$.lineItemList[0].contractWorkInd").value("Y"))
                .andExpect(jsonPath("$.lineItemList[0].supplyManagedCommodityInd").value("Y"))
                .andExpect(jsonPath("$.lineItemList[0].excludeFromRevenueCalcInd").value("Y"))
                .andExpect(jsonPath("$.lineItemList[0].industryAverageExpenseInd").value("Y"))
                .andExpect(jsonPath("$.lineItemList[0].commodityTypeCode").value("GRAIN"))
                .andExpect(jsonPath("$.lineItemList[0].fruitVegTypeCode").value("APPLE"))
                .andExpect(jsonPath("$.lineItemList[0].userEmail", nullValue()));
    }

    @Test
    @Order(6)
    public void testDeleteLineItem() throws Exception {

        mockMvc.perform(delete("/lineItems/39910"))
                .andExpect(status().isNoContent());
    }
}
