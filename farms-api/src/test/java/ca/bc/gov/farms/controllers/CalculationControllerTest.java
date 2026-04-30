package ca.bc.gov.farms.controllers;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.farms.data.models.EnrolmentCalculationRsrc;
import ca.bc.gov.farms.data.models.EnwEnrolmentRsrc;
import ca.bc.gov.farms.services.EnrolmentCalculationService;

@ExtendWith(MockitoExtension.class)
class CalculationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EnrolmentCalculationService enrolmentCalculationService;

    @InjectMocks
    private CalculationController calculationController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(calculationController).build();
    }

    @Test
    void getEnrolmentNoticeWorkflowCalculationReturnsResource() throws Exception {
        EnrolmentCalculationRsrc resource = EnrolmentCalculationRsrc.builder()
                .participantPin(26139667)
                .programYear(2021)
                .producerName("DZFIRECXE G EMYBNYFI")
                .agristabilityScenarioId(1052223L)
                .scenarioNumber(6)
                .scenarioStateCode("EN_COMP")
                .scenarioCategoryCode("ENW")
                .scenarioClassCode("USER")
                .editable(false)
                .benefitCalculationErrors(Collections.emptyList())
                .enwEnrolment(EnwEnrolmentRsrc.builder()
                        .enwEnrolmentId(3538L)
                        .enrolmentFee(new BigDecimal("45.00"))
                        .contributionMargin(new BigDecimal("12778.02"))
                        .enrolmentCalculationTypeCode("PROXY")
                        .build())
                .build();

        when(enrolmentCalculationService.getEnrolmentCalculation(26139667, 2021)).thenReturn(resource);

        mockMvc.perform(get("/calculations/enrolment-notice-workflow")
                .param("participantPin", "26139667")
                .param("programYear", "2021"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.participantPin").value(26139667))
                .andExpect(jsonPath("$.programYear").value(2021))
                .andExpect(jsonPath("$.agristabilityScenarioId").value(1052223))
                .andExpect(jsonPath("$.enwEnrolment.enrolmentCalculationTypeCode").value("PROXY"))
                .andExpect(jsonPath("$.enwEnrolment.contributionMargin").value(12778.02))
                .andExpect(jsonPath("$.enwEnrolment.enrolmentFee").value(45.00));
    }

    @Test
    void getEnrolmentNoticeWorkflowCalculationReturnsBadRequestWhenQueryParamMissing() throws Exception {
        mockMvc.perform(get("/calculations/enrolment-notice-workflow")
                .param("participantPin", "26139667"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(enrolmentCalculationService);
    }

    @Test
    void getEnrolmentNoticeWorkflowCalculationReturnsNotFoundWhenServiceHasNoScenario() throws Exception {
        when(enrolmentCalculationService.getEnrolmentCalculation(99999999, 2021))
                .thenThrow(new NotFoundException("not found"));

        mockMvc.perform(get("/calculations/enrolment-notice-workflow")
                .param("participantPin", "99999999")
                .param("programYear", "2021"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getEnrolmentNoticeWorkflowCalculationReturnsServerErrorWhenServiceFails() throws Exception {
        when(enrolmentCalculationService.getEnrolmentCalculation(26139667, 2021))
                .thenThrow(new RuntimeException("boom"));

        mockMvc.perform(get("/calculations/enrolment-notice-workflow")
                .param("participantPin", "26139667")
                .param("programYear", "2021"))
                .andExpect(status().isInternalServerError());
    }
}
