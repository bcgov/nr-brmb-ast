package ca.bc.gov.farms.data.assemblers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ca.bc.gov.brmb.common.rest.resource.RelLink;
import ca.bc.gov.brmb.common.rest.resource.types.BaseResourceTypes;
import ca.bc.gov.farms.data.entities.EnrolmentCalculationEntity;
import ca.bc.gov.farms.data.entities.EnrolmentCalculationProductiveUnitEntity;
import ca.bc.gov.farms.data.models.EnrolmentCalculationRsrc;
import ca.bc.gov.farms.data.models.EnwEnrolmentRsrc;
import ca.bc.gov.farms.data.models.EnwProductiveUnitRsrc;
import ca.bc.gov.farms.data.models.EnwProductiveValueRsrc;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EnrolmentCalculationResourceAssembler extends BaseResourceAssembler {

    private static final String YES = "Y";
    private static final String IN_PROGRESS = "IP";
    private static final BigDecimal MIN_ENROLMENT_FEE = new BigDecimal("45");
    private static final BigDecimal ENROLMENT_FEE_FACTOR_2013_FORWARD = new BigDecimal("0.00315");
    private static final BigDecimal ENROLMENT_FEE_FACTOR_2012_PREVIOUS = new BigDecimal("0.003825");

    public EnrolmentCalculationRsrc getEnrolmentCalculation(@NonNull EnrolmentCalculationEntity entity) {

        EnrolmentCalculationRsrc resource = EnrolmentCalculationRsrc.builder()
                .participantPin(entity.getParticipantPin())
                .programYear(entity.getProgramYear())
                .producerName(entity.getProducerName())
                .agristabilityScenarioId(entity.getAgristabilityScenarioId())
                .scenarioNumber(entity.getScenarioNumber())
                .scenarioStateCode(entity.getScenarioStateCode())
                .scenarioCategoryCode(entity.getScenarioCategoryCode())
                .scenarioClassCode(entity.getScenarioClassCode())
                .assignedToUserId(entity.getAssignedToUserId())
                .assignedToUserGuid(entity.getAssignedToUserGuid())
                .editable(IN_PROGRESS.equals(entity.getScenarioStateCode()) && entity.getAssignedToUserId() != null)
                .benefitCalculationErrors(entity.getBenefitCalculationErrors() == null
                        ? Collections.emptyList()
                        : entity.getBenefitCalculationErrors())
                .enwEnrolment(getEnwEnrolment(entity))
                .build();

        String eTag = getEtag(resource);
        resource.setETag(eTag);
        setCalculationSelfLink(resource);

        return resource;
    }

    private void setCalculationSelfLink(EnrolmentCalculationRsrc resource) {
        String selfUri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }

    private EnwEnrolmentRsrc getEnwEnrolment(EnrolmentCalculationEntity entity) {
        Boolean hasProductiveUnits = toBoolean(entity.getHasProductiveUnitsInd());
        Boolean hasBenchmarkPerUnits = toBoolean(entity.getHasBenchmarkPerUnitsInd());

        return EnwEnrolmentRsrc.builder()
                .enwEnrolmentId(entity.getScenarioEnrolmentId())
                .enrolmentYear(entity.getEnrolmentYear())
                .enrolmentFee(entity.getEnrolmentFee())
                .contributionMargin(entity.getContributionMargin())
                .benefitCalculated(toBoolean(entity.getBenefitCalculatedInd()))
                .proxyMarginsCalculated(toBoolean(entity.getProxyMarginsCalculatedInd()))
                .manualMarginsCalculated(toBoolean(entity.getManualCalculatedInd()))
                .hasProductiveUnits(hasProductiveUnits)
                .hasBpus(hasBenchmarkPerUnits)
                .canCalculateProxyMargins(Boolean.TRUE.equals(hasProductiveUnits) && Boolean.TRUE.equals(hasBenchmarkPerUnits))
                .programYear(entity.getProgramYear())
                .enrolmentFeeMinimum(MIN_ENROLMENT_FEE)
                .enrolmentFeeCalculationFactor(getEnrolmentFeeCalculationFactor(entity.getEnrolmentYear()))
                .benefitMarginYears(getBenefitMarginYears(entity.getProgramYear()))
                .productiveValueYearMinus2(entity.getProductiveValueYearMinus2())
                .productiveValueYearMinus3(entity.getProductiveValueYearMinus3())
                .productiveValueYearMinus4(entity.getProductiveValueYearMinus4())
                .proxyMarginYears(getProxyMarginYears(entity.getProgramYear()))
                .proxyMargins(Arrays.asList(entity.getProductiveValueYearMinus4(),
                        entity.getProductiveValueYearMinus3(), entity.getProductiveValueYearMinus2()))
                .enwProductiveUnits(getProductiveUnits(entity))
                .inCombinedFarm(entity.getInCombinedFarm())
                .benefitEnrolmentFee(entity.getBenefitEnrolmentFee())
                .benefitContributionMargin(entity.getBenefitContributionMargin())
                .proxyEnrolmentFee(entity.getProxyEnrolmentFee())
                .proxyContributionMargin(entity.getProxyContributionMargin())
                .manualEnrolmentFee(entity.getManualEnrolmentFee())
                .manualContributionMargin(entity.getManualContributionMargin())
                .marginYearMinus2(entity.getMarginYearMinus2())
                .marginYearMinus3(entity.getMarginYearMinus3())
                .marginYearMinus4(entity.getMarginYearMinus4())
                .marginYearMinus5(entity.getMarginYearMinus5())
                .marginYearMinus6(entity.getMarginYearMinus6())
                .marginYearMinus2Used(toBoolean(entity.getMarginYearMinus2Ind()))
                .marginYearMinus3Used(toBoolean(entity.getMarginYearMinus3Ind()))
                .marginYearMinus4Used(toBoolean(entity.getMarginYearMinus4Ind()))
                .marginYearMinus5Used(toBoolean(entity.getMarginYearMinus5Ind()))
                .marginYearMinus6Used(toBoolean(entity.getMarginYearMinus6Ind()))
                .benefitMarginYearMinus2(entity.getBenefitMarginYearMinus2())
                .benefitMarginYearMinus3(entity.getBenefitMarginYearMinus3())
                .benefitMarginYearMinus4(entity.getBenefitMarginYearMinus4())
                .benefitMarginYearMinus5(entity.getBenefitMarginYearMinus5())
                .benefitMarginYearMinus6(entity.getBenefitMarginYearMinus6())
                .benefitMarginYearMinus2Used(toBoolean(entity.getBenefitMargnYearMinus2Ind()))
                .benefitMarginYearMinus3Used(toBoolean(entity.getBenefitMargnYearMinus3Ind()))
                .benefitMarginYearMinus4Used(toBoolean(entity.getBenefitMargnYearMinus4Ind()))
                .benefitMarginYearMinus5Used(toBoolean(entity.getBenefitMargnYearMinus5Ind()))
                .benefitMarginYearMinus6Used(toBoolean(entity.getBenefitMargnYearMinus6Ind()))
                .proxyMarginYearMinus2(entity.getProxyMarginYearMinus2())
                .proxyMarginYearMinus3(entity.getProxyMarginYearMinus3())
                .proxyMarginYearMinus4(entity.getProxyMarginYearMinus4())
                .manualMarginYearMinus2(entity.getManualMarginYearMinus2())
                .manualMarginYearMinus3(entity.getManualMarginYearMinus3())
                .manualMarginYearMinus4(entity.getManualMarginYearMinus4())
                .combinedFarmPercent(entity.getCombinedFarmPercent())
                .enrolmentCalculationTypeCode(entity.getEnrolmentCalcTypeCode())
                .revisionCount(entity.getRevisionCount())
                .build();
    }

    private List<EnwProductiveUnitRsrc> getProductiveUnits(EnrolmentCalculationEntity entity) {
        if (entity.getProductiveUnits() == null || !Boolean.TRUE.equals(toBoolean(entity.getHasBenchmarkPerUnitsInd()))) {
            return Collections.emptyList();
        }

        return entity.getProductiveUnits().stream()
                .map(this::getProductiveUnit)
                .toList();
    }

    private EnwProductiveUnitRsrc getProductiveUnit(EnrolmentCalculationProductiveUnitEntity entity) {
        return EnwProductiveUnitRsrc.builder()
                .code(entity.getCode())
                .description(entity.getDescription())
                .productiveCapacity(entity.getProductiveCapacity())
                .productiveValues(Arrays.asList(
                        getProductiveValue(entity.getBpuMarginYearMinus4(), entity.getProductiveValueYearMinus4()),
                        getProductiveValue(entity.getBpuMarginYearMinus3(), entity.getProductiveValueYearMinus3()),
                        getProductiveValue(entity.getBpuMarginYearMinus2(), entity.getProductiveValueYearMinus2())))
                .build();
    }

    private EnwProductiveValueRsrc getProductiveValue(BigDecimal bpuMargin, BigDecimal productiveValue) {
        return EnwProductiveValueRsrc.builder()
                .bpuMargin(bpuMargin)
                .productiveValue(productiveValue)
                .build();
    }

    private static Boolean toBoolean(String value) {
        return value == null ? null : YES.equals(value);
    }

    private static BigDecimal getEnrolmentFeeCalculationFactor(Integer enrolmentYear) {
        if (enrolmentYear != null && enrolmentYear < 2013) {
            return ENROLMENT_FEE_FACTOR_2012_PREVIOUS;
        }
        return ENROLMENT_FEE_FACTOR_2013_FORWARD;
    }

    private static java.util.List<Integer> getBenefitMarginYears(Integer programYear) {
        if (programYear == null) {
            return Collections.emptyList();
        }
        return IntStream.rangeClosed(programYear - 4, programYear).boxed().toList();
    }

    private static java.util.List<Integer> getProxyMarginYears(Integer programYear) {
        if (programYear == null) {
            return Collections.emptyList();
        }
        return IntStream.rangeClosed(programYear - 2, programYear).boxed().toList();
    }
}
