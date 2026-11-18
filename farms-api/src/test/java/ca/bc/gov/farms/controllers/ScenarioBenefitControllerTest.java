package ca.bc.gov.farms.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.farms.data.assemblers.ScenarioBenefitResourceAssembler;
import ca.bc.gov.farms.data.entities.ScenarioBenefitEntity;
import ca.bc.gov.farms.data.mappers.ScenarioBenefitMapper;
import ca.bc.gov.farms.services.ScenarioBenefitService;

@ExtendWith(MockitoExtension.class)
class ScenarioBenefitControllerTest {

    private static final String PATH = "/calculations/scenario-benefits";
    private static final String REQUEST = PATH + "?participantPin=1234567&programYear=2024&scenarioNumber=3";
    private final ObjectMapper json = new ObjectMapper();

    @Mock
    private ScenarioBenefitMapper mapper;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        ScenarioBenefitService service = new ScenarioBenefitService(mapper, new ScenarioBenefitResourceAssembler());
        mockMvc = MockMvcBuilders.standaloneSetup(new ScenarioBenefitController(service)).build();
    }

    @Test
    void returnsScenarioWithBothMarginsAndNullableAmounts() throws Exception {
        ScenarioBenefitEntity entity = new ScenarioBenefitEntity();
        entity.setPin(1234567);
        entity.setProgramYear(2024);
        entity.setScenarioCategoryCode("INT");
        entity.setProgramYearMargin(new BigDecimal("-45000.75"));
        entity.setReferenceMargin(new BigDecimal("125000.25"));
        entity.setLateEnrolmentPenalty(new BigDecimal("250.50"));
        when(mapper.fetchScenarioBenefits(1234567, 2024, 3)).thenReturn(List.of(entity));

        String body = mockMvc.perform(get(REQUEST)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JsonNode response = json.readTree(body);
        JsonNode rows = response.required("scenarioBenefitList");
        assertEquals(1, rows.size());
        JsonNode row = rows.get(0);
        assertEquals(1234567, row.required("pin").intValue());
        assertEquals(2024, row.required("programYear").intValue());
        assertEquals("INT", row.required("scenarioCategoryCode").textValue());
        assertEquals(-45000.75, row.required("programYearMargin").doubleValue());
        assertEquals(125000.25, row.required("referenceMargin").doubleValue());
        assertEquals(250.50, row.required("lateEnrolmentPenalty").doubleValue());
        assertTrue(row.required("paymentLimitationAmount").isNull());
        assertEquals("self", response.required("links").get(0).required("rel").textValue());
    }

    @Test
    void returnsEmptyListWhenScenarioDoesNotMatch() throws Exception {
        when(mapper.fetchScenarioBenefits(1234567, 2024, 3)).thenReturn(List.of());
        String body = mockMvc.perform(get(REQUEST)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertEquals(json.createArrayNode(), json.readTree(body).required("scenarioBenefitList"));
    }

    @Test
    void requiresAllThreeParameters() throws Exception {
        for (String query : List.of("programYear=2024&scenarioNumber=3", "participantPin=1234567&scenarioNumber=3",
                "participantPin=1234567&programYear=2024")) {
            mockMvc.perform(get(PATH + "?" + query)).andExpect(status().isBadRequest());
        }
        verifyNoInteractions(mapper);
    }

    @Test
    void rejectsNonNumericScenarioNumber() throws Exception {
        mockMvc.perform(get(PATH + "?participantPin=1234567&programYear=2024&scenarioNumber=abc"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(mapper);
    }

    @Test
    void returnsServerErrorWhenMapperFails() throws Exception {
        when(mapper.fetchScenarioBenefits(1234567, 2024, 3)).thenThrow(new IllegalStateException("test failure"));
        mockMvc.perform(get(REQUEST)).andExpect(status().isInternalServerError());
    }
}
