package ca.bc.gov.farms.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.farms.data.assemblers.EnrolmentCalculationResourceAssembler;
import ca.bc.gov.farms.data.entities.EnrolmentCalculationEntity;
import ca.bc.gov.farms.data.entities.EnrolmentCalculationMarginEntity;
import ca.bc.gov.farms.data.entities.EnrolmentCalculationProductiveUnitEntity;
import ca.bc.gov.farms.data.mappers.EnrolmentCalculationMapper;
import ca.bc.gov.farms.data.models.EnrolmentCalculationRsrc;

@ExtendWith(MockitoExtension.class)
class EnrolmentCalculationServiceTest {

    @Mock
    private EnrolmentCalculationMapper enrolmentCalculationMapper;

    @Mock
    private EnrolmentCalculationResourceAssembler enrolmentCalculationResourceAssembler;

    @InjectMocks
    private EnrolmentCalculationService enrolmentCalculationService;

    @Test
    void getEnrolmentCalculationReturnsStoredValuesWhenScenarioIsNotEditable() throws Exception {
        EnrolmentCalculationEntity entity = baseEntity()
                .scenarioStateCode("EN_COMP")
                .assignedToUserId("YTUT\\AWXRRFU")
                .scenarioEnrolmentId(3538L)
                .enrolmentFee(new BigDecimal("45.00"))
                .contributionMargin(new BigDecimal("12778.02"))
                .enrolmentCalcTypeCode("PROXY")
                .proxyEnrolmentFee(new BigDecimal("45.00"))
                .proxyContributionMargin(new BigDecimal("12778.02"))
                .build();
        EnrolmentCalculationRsrc expectedResource = EnrolmentCalculationRsrc.builder()
                .participantPin(26139667)
                .programYear(2021)
                .build();

        when(enrolmentCalculationMapper.fetchLatestEnwByPinAndProgramYear(26139667, 2021))
                .thenReturn(entity);
        when(enrolmentCalculationMapper.fetchProductiveUnits(1052223L, 2021))
                .thenReturn(List.of(proxyProductiveUnit()));
        when(enrolmentCalculationResourceAssembler.getEnrolmentCalculation(entity))
                .thenReturn(expectedResource);

        EnrolmentCalculationRsrc result = enrolmentCalculationService
                .getEnrolmentCalculation(26139667, 2021);

        assertThat(result).isSameAs(expectedResource);
        assertThat(entity.getEnrolmentYear()).isEqualTo(2023);
        assertThat(entity.getHasProductiveUnitsInd()).isEqualTo("Y");
        assertThat(entity.getHasBenchmarkPerUnitsInd()).isEqualTo("Y");
        assertThat(entity.getProductiveValueYearMinus4()).isEqualByComparingTo(new BigDecimal("16765.38"));
        assertThat(entity.getProductiveValueYearMinus3()).isEqualByComparingTo(new BigDecimal("10784.34"));
        assertThat(entity.getProductiveValueYearMinus2()).isEqualByComparingTo(new BigDecimal("10784.34"));
        verify(enrolmentCalculationMapper, never()).insertScenarioEnrolment(any(), any());
        verify(enrolmentCalculationMapper, never()).updateScenarioEnrolment(any(), any());
    }

    @Test
    void getEnrolmentCalculationRecalculatesAndSavesProxyValuesWhenScenarioIsEditable() throws Exception {
        EnrolmentCalculationEntity entity = baseEntity()
                .scenarioStateCode("IP")
                .assignedToUserId("BHIC\\KLFUFQNN")
                .scenarioEnrolmentId(3229L)
                .enrolmentCalcTypeCode("PROXY")
                .build();
        EnrolmentCalculationRsrc expectedResource = EnrolmentCalculationRsrc.builder()
                .participantPin(26139667)
                .programYear(2021)
                .build();

        when(enrolmentCalculationMapper.fetchLatestEnwByPinAndProgramYear(26139667, 2021))
                .thenReturn(entity, entity);
        when(enrolmentCalculationMapper.fetchProductiveUnits(1052223L, 2021))
                .thenReturn(List.of(proxyProductiveUnit()), List.of(proxyProductiveUnit()));
        when(enrolmentCalculationMapper.fetchBenefitMargins(1052223L))
                .thenReturn(Collections.emptyList());
        when(enrolmentCalculationMapper.updateScenarioEnrolment(any(), eq("BHIC\\KLFUFQNN")))
                .thenReturn(1);
        when(enrolmentCalculationResourceAssembler.getEnrolmentCalculation(entity))
                .thenReturn(expectedResource);

        EnrolmentCalculationRsrc result = enrolmentCalculationService
                .getEnrolmentCalculation(26139667, 2021);

        assertThat(result).isSameAs(expectedResource);

        ArgumentCaptor<EnrolmentCalculationEntity> entityCaptor =
                ArgumentCaptor.forClass(EnrolmentCalculationEntity.class);
        verify(enrolmentCalculationMapper).updateScenarioEnrolment(entityCaptor.capture(), eq("BHIC\\KLFUFQNN"));

        EnrolmentCalculationEntity savedEntity = entityCaptor.getValue();
        assertThat(savedEntity.getBenefitCalculatedInd()).isEqualTo("N");
        assertThat(savedEntity.getProxyMarginsCalculatedInd()).isEqualTo("Y");
        assertThat(savedEntity.getManualCalculatedInd()).isEqualTo("N");
        assertThat(savedEntity.getProxyMarginYearMinus4()).isEqualByComparingTo(new BigDecimal("16765.38"));
        assertThat(savedEntity.getProxyMarginYearMinus3()).isEqualByComparingTo(new BigDecimal("10784.34"));
        assertThat(savedEntity.getProxyMarginYearMinus2()).isEqualByComparingTo(new BigDecimal("10784.34"));
        assertThat(savedEntity.getProxyContributionMargin()).isEqualByComparingTo(new BigDecimal("12778.02"));
        assertThat(savedEntity.getProxyEnrolmentFee()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(savedEntity.getContributionMargin()).isEqualByComparingTo(new BigDecimal("12778.02"));
        assertThat(savedEntity.getEnrolmentFee()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(savedEntity.getMarginYearMinus2Ind()).isEqualTo("Y");
        assertThat(savedEntity.getMarginYearMinus3Ind()).isEqualTo("Y");
        assertThat(savedEntity.getMarginYearMinus4Ind()).isEqualTo("Y");
        assertThat(savedEntity.getBenefitCalculationErrors())
                .containsExactly(EnrolmentCalculationMessages.REASON_INSUFFICIENT_REFERENCE_MARGIN_DATA);
    }

    @Test
    void getEnrolmentCalculationRecalculatesAndSavesManualValuesWhenScenarioIsEditable() throws Exception {
        EnrolmentCalculationEntity entity = baseEntity()
                .participantPin(25006677)
                .agristabilityScenarioId(1073290L)
                .scenarioNumber(7)
                .scenarioStateCode("IP")
                .assignedToUserId("IDIR\\AHOPKINS")
                .scenarioEnrolmentId(3721L)
                .enrolmentCalcTypeCode("MANUAL")
                .manualMarginYearMinus2(new BigDecimal("85710.00"))
                .manualMarginYearMinus3(new BigDecimal("100000.00"))
                .manualMarginYearMinus4(new BigDecimal("100000.00"))
                .build();
        EnrolmentCalculationRsrc expectedResource = EnrolmentCalculationRsrc.builder()
                .participantPin(25006677)
                .programYear(2021)
                .build();

        when(enrolmentCalculationMapper.fetchLatestEnwByPinAndProgramYear(25006677, 2021))
                .thenReturn(entity, entity);
        when(enrolmentCalculationMapper.fetchProductiveUnits(1073290L, 2021))
                .thenReturn(Collections.emptyList(), Collections.emptyList());
        when(enrolmentCalculationMapper.fetchBenefitMargins(1073290L))
                .thenReturn(Collections.emptyList());
        when(enrolmentCalculationMapper.updateScenarioEnrolment(any(), eq("IDIR\\AHOPKINS")))
                .thenReturn(1);
        when(enrolmentCalculationResourceAssembler.getEnrolmentCalculation(entity))
                .thenReturn(expectedResource);

        EnrolmentCalculationRsrc result = enrolmentCalculationService
                .getEnrolmentCalculation(25006677, 2021);

        assertThat(result).isSameAs(expectedResource);

        ArgumentCaptor<EnrolmentCalculationEntity> entityCaptor =
                ArgumentCaptor.forClass(EnrolmentCalculationEntity.class);
        verify(enrolmentCalculationMapper).updateScenarioEnrolment(entityCaptor.capture(), eq("IDIR\\AHOPKINS"));

        EnrolmentCalculationEntity savedEntity = entityCaptor.getValue();
        assertThat(savedEntity.getManualCalculatedInd()).isEqualTo("Y");
        assertThat(savedEntity.getManualContributionMargin()).isEqualByComparingTo(new BigDecimal("95236.67"));
        assertThat(savedEntity.getManualEnrolmentFee()).isEqualByComparingTo(new BigDecimal("300.00"));
        assertThat(savedEntity.getContributionMargin()).isEqualByComparingTo(new BigDecimal("95236.67"));
        assertThat(savedEntity.getEnrolmentFee()).isEqualByComparingTo(new BigDecimal("300.00"));
        assertThat(savedEntity.getMarginYearMinus2()).isEqualByComparingTo(new BigDecimal("85710.00"));
        assertThat(savedEntity.getMarginYearMinus3()).isEqualByComparingTo(new BigDecimal("100000.00"));
        assertThat(savedEntity.getMarginYearMinus4()).isEqualByComparingTo(new BigDecimal("100000.00"));
    }

    @Test
    void getEnrolmentCalculationRecalculatesAndSavesBenefitValuesWhenScenarioIsEditable() throws Exception {
        EnrolmentCalculationEntity entity = baseEntity()
                .participantPin(3786019)
                .agristabilityScenarioId(1040604L)
                .scenarioNumber(4)
                .scenarioStateCode("IP")
                .assignedToUserId("BHIC\\KLFUFQNN")
                .scenarioEnrolmentId(3201L)
                .enrolmentCalcTypeCode("BENEFIT")
                .build();
        EnrolmentCalculationRsrc expectedResource = EnrolmentCalculationRsrc.builder()
                .participantPin(3786019)
                .programYear(2021)
                .build();

        when(enrolmentCalculationMapper.fetchLatestEnwByPinAndProgramYear(3786019, 2021))
                .thenReturn(entity, entity);
        when(enrolmentCalculationMapper.fetchProductiveUnits(1040604L, 2021))
                .thenReturn(Collections.emptyList(), Collections.emptyList());
        when(enrolmentCalculationMapper.fetchBenefitMargins(1040604L))
                .thenReturn(List.of(
                        benefitMargin(2017, "30259.00"),
                        benefitMargin(2018, "14302.50"),
                        benefitMargin(2019, "3337.50"),
                        benefitMargin(2020, "7274.00"),
                        benefitMargin(2021, "1791.00")));
        when(enrolmentCalculationMapper.updateScenarioEnrolment(any(), eq("BHIC\\KLFUFQNN")))
                .thenReturn(1);
        when(enrolmentCalculationResourceAssembler.getEnrolmentCalculation(entity))
                .thenReturn(expectedResource);

        EnrolmentCalculationRsrc result = enrolmentCalculationService
                .getEnrolmentCalculation(3786019, 2021);

        assertThat(result).isSameAs(expectedResource);

        ArgumentCaptor<EnrolmentCalculationEntity> entityCaptor =
                ArgumentCaptor.forClass(EnrolmentCalculationEntity.class);
        verify(enrolmentCalculationMapper).updateScenarioEnrolment(entityCaptor.capture(), eq("BHIC\\KLFUFQNN"));

        EnrolmentCalculationEntity savedEntity = entityCaptor.getValue();
        assertThat(savedEntity.getBenefitCalculatedInd()).isEqualTo("Y");
        assertThat(savedEntity.getBenefitContributionMargin()).isEqualByComparingTo(new BigDecimal("8304.67"));
        assertThat(savedEntity.getBenefitEnrolmentFee()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(savedEntity.getContributionMargin()).isEqualByComparingTo(new BigDecimal("8304.67"));
        assertThat(savedEntity.getEnrolmentFee()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(savedEntity.getBenefitMargnYearMinus2Ind()).isEqualTo("N");
        assertThat(savedEntity.getBenefitMargnYearMinus3Ind()).isEqualTo("Y");
        assertThat(savedEntity.getBenefitMargnYearMinus4Ind()).isEqualTo("Y");
        assertThat(savedEntity.getBenefitMargnYearMinus5Ind()).isEqualTo("Y");
        assertThat(savedEntity.getBenefitMargnYearMinus6Ind()).isEqualTo("N");
        assertThat(savedEntity.getBenefitCalculationErrors()).isEmpty();
    }

    @Test
    void getEnrolmentCalculationThrowsNotFoundWhenScenarioDoesNotExist() {
        when(enrolmentCalculationMapper.fetchLatestEnwByPinAndProgramYear(99999999, 2021))
                .thenReturn(null);

        assertThatThrownBy(() -> enrolmentCalculationService.getEnrolmentCalculation(99999999, 2021))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("participant PIN 99999999");
    }

    @Test
    void getEnrolmentCalculationThrowsIllegalArgumentExceptionWhenRequiredInputMissing() {
        assertThatThrownBy(() -> enrolmentCalculationService.getEnrolmentCalculation(null, 2021))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("participantPin and programYear are required");

        assertThatThrownBy(() -> enrolmentCalculationService.getEnrolmentCalculation(26139667, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("participantPin and programYear are required");
    }

    @Test
    void getEnrolmentCalculationThrowsServiceExceptionWhenSelectedEditableManualHasNoValues() {
        EnrolmentCalculationEntity entity = baseEntity()
                .participantPin(23723331)
                .programYear(2022)
                .agristabilityScenarioId(1074678L)
                .scenarioStateCode("IP")
                .assignedToUserId("IDIR\\AHOPKINS")
                .scenarioEnrolmentId(3721L)
                .scenarioNumber(6)
                .enrolmentCalcTypeCode("MANUAL")
                .build();

        when(enrolmentCalculationMapper.fetchLatestEnwByPinAndProgramYear(23723331, 2022))
                .thenReturn(entity);
        when(enrolmentCalculationMapper.fetchProductiveUnits(1074678L, 2022))
                .thenReturn(Collections.emptyList());
        when(enrolmentCalculationMapper.fetchBenefitMargins(1074678L))
                .thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> enrolmentCalculationService.getEnrolmentCalculation(23723331, 2022))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("Mapper threw an exception")
                .hasRootCauseMessage("Unable to recalculate ENW enrolment values for scenario 1074678 using calculation type MANUAL");
    }

    private EnrolmentCalculationEntity.EnrolmentCalculationEntityBuilder baseEntity() {
        return EnrolmentCalculationEntity.builder()
                .participantPin(26139667)
                .programYear(2021)
                .producerName("DZFIRECXE G EMYBNYFI")
                .agristabilityScenarioId(1052223L)
                .scenarioNumber(6)
                .scenarioCategoryCode("ENW")
                .scenarioClassCode("USER")
                .inCombinedFarm(false);
    }

    private EnrolmentCalculationProductiveUnitEntity proxyProductiveUnit() {
        return EnrolmentCalculationProductiveUnitEntity.builder()
                .code("4998")
                .description("Grapes (Year 2+ of production)")
                .productiveCapacity(new BigDecimal("4.500"))
                .bpuMarginYearMinus4(new BigDecimal("3725.64"))
                .bpuMarginYearMinus3(new BigDecimal("2396.52"))
                .bpuMarginYearMinus2(new BigDecimal("2396.52"))
                .build();
    }

    private EnrolmentCalculationMarginEntity benefitMargin(Integer referenceYear, String margin) {
        BigDecimal marginValue = new BigDecimal(margin);
        return EnrolmentCalculationMarginEntity.builder()
                .referenceYear(referenceYear)
                .productionMarginAccrualAdjustments(marginValue)
                .productionMarginAfterStructuralChanges(marginValue)
                .totalAllowableIncome(BigDecimal.ONE)
                .totalUnallowableExpenses(BigDecimal.ZERO)
                .hasProductiveUnitsInd("Y")
                .build();
    }
}
