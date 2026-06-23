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
import ca.bc.gov.farms.data.models.EnrolmentCombinedFarmClientRsrc;
import ca.bc.gov.farms.data.models.EnrolmentPartnerListRsrc;
import ca.bc.gov.farms.data.models.EnrolmentPartnerRsrc;
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

    @Test
    void getEnrolmentPartnersReturnsResource() throws Exception {
        EnrolmentPartnerListRsrc resource = EnrolmentPartnerListRsrc.builder()
                .participantPin(23198443)
                .programYear(2021)
                .agristabilityScenarioId(1048121L)
                .scenarioNumber(5)
                .inCombinedFarm(true)
                .combinedFarmNumber(123)
                .combinedFarmPercent(new BigDecimal("0.6000"))
                .combinedFarmClientList(Collections.singletonList(
                        EnrolmentCombinedFarmClientRsrc.builder()
                                .agristabilityScenarioId(1048122L)
                                .scenarioNumber(2)
                                .participantPin(25306184)
                                .corporationName("Combined Farm")
                                .build()))
                .enrolmentPartnerList(Collections.singletonList(EnrolmentPartnerRsrc.builder()
                        .agristabilityScenarioId(969183L)
                        .scenarioNumber(1)
                        .operationSchedule("A")
                        .partnershipName("Christopher Stockdale")
                        .operationPartnershipPin(4380606)
                        .operationPartnershipPercent(new BigDecimal("0.6000"))
                        .farmingOperationPartnerId(458515L)
                        .partnerPercent(new BigDecimal("0.4000"))
                        .partnerParticipantPin(25306184)
                        .partnerEnrolmentFee(new BigDecimal("265.83"))
                        .firstName("Lynn")
                        .lastName("Stockdale")
                        .build()))
                .build();

        when(enrolmentCalculationService.getEnrolmentPartners(23198443, 2021)).thenReturn(resource);

        mockMvc.perform(get("/calculations/enrolment-partners")
                .param("participantPin", "23198443")
                .param("programYear", "2021"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.participantPin").value(23198443))
                .andExpect(jsonPath("$.programYear").value(2021))
                .andExpect(jsonPath("$.agristabilityScenarioId").value(1048121))
                .andExpect(jsonPath("$.scenarioNumber").value(5))
                .andExpect(jsonPath("$.inCombinedFarm").value(true))
                .andExpect(jsonPath("$.combinedFarmNumber").value(123))
                .andExpect(jsonPath("$.combinedFarmPercent").value(0.6000))
                .andExpect(jsonPath("$.combinedFarmClientList[0].agristabilityScenarioId").value(1048122))
                .andExpect(jsonPath("$.combinedFarmClientList[0].scenarioNumber").value(2))
                .andExpect(jsonPath("$.combinedFarmClientList[0].participantPin").value(25306184))
                .andExpect(jsonPath("$.combinedFarmClientList[0].corporationName").value("Combined Farm"))
                .andExpect(jsonPath("$.enrolmentPartnerList[0].agristabilityScenarioId").value(969183))
                .andExpect(jsonPath("$.enrolmentPartnerList[0].operationSchedule").value("A"))
                .andExpect(jsonPath("$.enrolmentPartnerList[0].partnerParticipantPin").value(25306184))
                .andExpect(jsonPath("$.enrolmentPartnerList[0].partnerEnrolmentFee").value(265.83))
                .andExpect(jsonPath("$.enrolmentPartnerList[0].partnerPercent").value(0.4000))
                .andExpect(jsonPath("$.enrolmentPartnerList[0].firstName").value("Lynn"))
                .andExpect(jsonPath("$.enrolmentPartnerList[0].lastName").value("Stockdale"));
    }

    @Test
    void getEnrolmentPartnersReturnsCombinedSummaryWithEmptyPartnerList() throws Exception {
        EnrolmentPartnerListRsrc resource = EnrolmentPartnerListRsrc.builder()
                .participantPin(25180167)
                .programYear(2021)
                .agristabilityScenarioId(1032849L)
                .scenarioNumber(7)
                .inCombinedFarm(true)
                .combinedFarmNumber(12385)
                .combinedFarmPercent(new BigDecimal("0.215"))
                .combinedFarmClientList(Collections.emptyList())
                .enrolmentPartnerList(Collections.emptyList())
                .build();

        when(enrolmentCalculationService.getEnrolmentPartners(25180167, 2021)).thenReturn(resource);

        mockMvc.perform(get("/calculations/enrolment-partners")
                .param("participantPin", "25180167")
                .param("programYear", "2021"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.participantPin").value(25180167))
                .andExpect(jsonPath("$.programYear").value(2021))
                .andExpect(jsonPath("$.agristabilityScenarioId").value(1032849))
                .andExpect(jsonPath("$.scenarioNumber").value(7))
                .andExpect(jsonPath("$.inCombinedFarm").value(true))
                .andExpect(jsonPath("$.combinedFarmNumber").value(12385))
                .andExpect(jsonPath("$.combinedFarmPercent").value(0.215))
                .andExpect(jsonPath("$.combinedFarmClientList").isArray())
                .andExpect(jsonPath("$.combinedFarmClientList").isEmpty())
                .andExpect(jsonPath("$.enrolmentPartnerList").isArray())
                .andExpect(jsonPath("$.enrolmentPartnerList").isEmpty());
    }

    @Test
    void getEnrolmentPartnersReturnsBadRequestWhenQueryParamMissing() throws Exception {
        mockMvc.perform(get("/calculations/enrolment-partners")
                .param("participantPin", "23198443"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(enrolmentCalculationService);
    }

    @Test
    void getEnrolmentPartnersReturnsServerErrorWhenServiceFails() throws Exception {
        when(enrolmentCalculationService.getEnrolmentPartners(23198443, 2021))
                .thenThrow(new RuntimeException("boom"));

        mockMvc.perform(get("/calculations/enrolment-partners")
                .param("participantPin", "23198443")
                .param("programYear", "2021"))
                .andExpect(status().isInternalServerError());
    }
}
