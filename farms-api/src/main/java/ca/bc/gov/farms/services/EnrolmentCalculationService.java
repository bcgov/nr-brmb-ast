package ca.bc.gov.farms.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.farms.data.assemblers.EnrolmentCalculationResourceAssembler;
import ca.bc.gov.farms.data.entities.EnrolmentCalculationEntity;
import ca.bc.gov.farms.data.entities.EnrolmentCalculationMarginEntity;
import ca.bc.gov.farms.data.entities.EnrolmentCalculationProductiveUnitEntity;
import ca.bc.gov.farms.data.mappers.EnrolmentCalculationMapper;
import ca.bc.gov.farms.data.models.EnrolmentCalculationRsrc;

@Component
public class EnrolmentCalculationService {

    private static final String YES = "Y";
    private static final String NO = "N";
    private static final String IN_PROGRESS = "IP";
    private static final String BENEFIT = "BENEFIT";
    private static final String PROXY = "PROXY";
    private static final String MANUAL = "MANUAL";
    private static final String DEFAULT_USER_ID = "FARMS_API";
    private static final BigDecimal ZERO = BigDecimal.ZERO;
    private static final BigDecimal MIN_ENROLMENT_FEE = new BigDecimal("45");
    private static final BigDecimal ENROLMENT_FEE_FACTOR_2013_FORWARD = new BigDecimal("0.00315");
    private static final BigDecimal ENROLMENT_FEE_FACTOR_2012_PREVIOUS = new BigDecimal("0.003825");
    private static final BigDecimal OVERSIZE_MARGIN_LIMIT = new BigDecimal("999999999.99");

    @Autowired
    private EnrolmentCalculationMapper enrolmentCalculationMapper;

    @Autowired
    private EnrolmentCalculationResourceAssembler enrolmentCalculationResourceAssembler;

    @Transactional
    public EnrolmentCalculationRsrc getEnrolmentCalculation(Integer participantPin, Integer programYear)
            throws ServiceException, NotFoundException {

        if (participantPin == null || programYear == null) {
            throw new IllegalArgumentException("participantPin and programYear are required");
        }

        EnrolmentCalculationRsrc result = null;

        try {
            EnrolmentCalculationEntity entity = enrolmentCalculationMapper
                    .fetchLatestEnwByPinAndProgramYear(participantPin, programYear);

            if (entity == null) {
                throw new NotFoundException("Did not find an ENW enrolment calculation for participant PIN "
                        + participantPin + " and program year " + programYear);
            }

            initNonEditableFields(entity);

            if (isEditable(entity)) {
                List<String> benefitCalculationErrors = calculateAndSave(entity);

                entity = enrolmentCalculationMapper
                        .fetchLatestEnwByPinAndProgramYear(participantPin, programYear);
                initNonEditableFields(entity);
                entity.setBenefitCalculationErrors(benefitCalculationErrors);
            }

            result = enrolmentCalculationResourceAssembler.getEnrolmentCalculation(entity);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    private void initNonEditableFields(EnrolmentCalculationEntity entity) {
        if (entity.getEnrolmentYear() == null && entity.getProgramYear() != null) {
            entity.setEnrolmentYear(entity.getProgramYear() + 2);
        }

        if (Boolean.TRUE.equals(entity.getInCombinedFarm()) && entity.getCombinedFarmPercent() == null) {
            entity.setCombinedFarmPercent(entity.getBenefitCombinedFarmPercent());
        }

        List<EnrolmentCalculationProductiveUnitEntity> productiveUnits = enrolmentCalculationMapper
                .fetchProductiveUnits(entity.getAgristabilityScenarioId(), entity.getProgramYear());

        calculateProductiveUnitValues(productiveUnits);
        entity.setProductiveUnits(productiveUnits);
        entity.setHasProductiveUnitsInd(productiveUnits.isEmpty() ? NO : YES);
        entity.setHasBenchmarkPerUnitsInd(productiveUnitsHaveBpus(productiveUnits) ? YES : NO);

        if (isYes(entity.getHasBenchmarkPerUnitsInd())) {
            entity.setProductiveValueYearMinus4(sum(productiveUnits,
                    EnrolmentCalculationProductiveUnitEntity::getProductiveValueYearMinus4));
            entity.setProductiveValueYearMinus3(sum(productiveUnits,
                    EnrolmentCalculationProductiveUnitEntity::getProductiveValueYearMinus3));
            entity.setProductiveValueYearMinus2(sum(productiveUnits,
                    EnrolmentCalculationProductiveUnitEntity::getProductiveValueYearMinus2));
        }

        if (entity.getBenefitCalculationErrors() == null) {
            entity.setBenefitCalculationErrors(new ArrayList<>());
        }
    }

    private List<String> calculateAndSave(EnrolmentCalculationEntity entity) throws ServiceException {
        List<EnrolmentCalculationProductiveUnitEntity> productiveUnits = entity.getProductiveUnits();
        List<String> benefitCalculationErrors = new ArrayList<>();

        clearCalculatedFields(entity);
        calculateBenefitMargins(entity, benefitCalculationErrors);
        calculateProxyMargins(entity, productiveUnits);
        calculateManualMargins(entity);
        defaultCalculationType(entity);
        calculateSelectedMargins(entity);

        entity.setBenefitCalculationErrors(benefitCalculationErrors);

        boolean benefitSelectedWithErrors = BENEFIT.equals(entity.getEnrolmentCalcTypeCode())
                && !benefitCalculationErrors.isEmpty();
        if (!selectedCalculationHasValues(entity) && !benefitSelectedWithErrors) {
            throw new ServiceException("Unable to recalculate ENW enrolment values for scenario "
                    + entity.getAgristabilityScenarioId()
                    + " using calculation type " + entity.getEnrolmentCalcTypeCode());
        }

        String userId = entity.getAssignedToUserId() == null ? DEFAULT_USER_ID : entity.getAssignedToUserId();
        int rowCount;
        if (entity.getScenarioEnrolmentId() == null) {
            rowCount = enrolmentCalculationMapper.insertScenarioEnrolment(entity, userId);
        } else {
            rowCount = enrolmentCalculationMapper.updateScenarioEnrolment(entity, userId);
        }

        if (rowCount != 1) {
            throw new ServiceException("Unable to save ENW enrolment calculation for scenario "
                    + entity.getAgristabilityScenarioId());
        }

        return benefitCalculationErrors;
    }

    private boolean selectedCalculationHasValues(EnrolmentCalculationEntity entity) {
        if (BENEFIT.equals(entity.getEnrolmentCalcTypeCode())) {
            return entity.getBenefitContributionMargin() != null && entity.getBenefitEnrolmentFee() != null;
        }
        if (PROXY.equals(entity.getEnrolmentCalcTypeCode())) {
            return entity.getProxyContributionMargin() != null
                    && entity.getProxyEnrolmentFee() != null
                    && entity.getProxyMarginYearMinus2() != null
                    && entity.getProxyMarginYearMinus3() != null
                    && entity.getProxyMarginYearMinus4() != null;
        }
        if (MANUAL.equals(entity.getEnrolmentCalcTypeCode())) {
            return entity.getManualContributionMargin() != null
                    && entity.getManualEnrolmentFee() != null
                    && entity.getManualMarginYearMinus2() != null
                    && entity.getManualMarginYearMinus3() != null
                    && entity.getManualMarginYearMinus4() != null;
        }
        return false;
    }

    private void clearCalculatedFields(EnrolmentCalculationEntity entity) {
        entity.setEnrolmentFee(null);
        entity.setContributionMargin(null);
        entity.setBenefitCalculatedInd(NO);
        entity.setProxyMarginsCalculatedInd(NO);
        entity.setManualCalculatedInd(NO);
        entity.setBenefitEnrolmentFee(null);
        entity.setBenefitContributionMargin(null);
        entity.setProxyEnrolmentFee(null);
        entity.setProxyContributionMargin(null);
        entity.setManualEnrolmentFee(null);
        entity.setManualContributionMargin(null);
        entity.setMarginYearMinus2(null);
        entity.setMarginYearMinus3(null);
        entity.setMarginYearMinus4(null);
        entity.setMarginYearMinus5(null);
        entity.setMarginYearMinus6(null);
        entity.setMarginYearMinus2Ind(NO);
        entity.setMarginYearMinus3Ind(NO);
        entity.setMarginYearMinus4Ind(NO);
        entity.setMarginYearMinus5Ind(NO);
        entity.setMarginYearMinus6Ind(NO);
        entity.setBenefitMarginYearMinus2(null);
        entity.setBenefitMarginYearMinus3(null);
        entity.setBenefitMarginYearMinus4(null);
        entity.setBenefitMarginYearMinus5(null);
        entity.setBenefitMarginYearMinus6(null);
        entity.setBenefitMargnYearMinus2Ind(NO);
        entity.setBenefitMargnYearMinus3Ind(NO);
        entity.setBenefitMargnYearMinus4Ind(NO);
        entity.setBenefitMargnYearMinus5Ind(NO);
        entity.setBenefitMargnYearMinus6Ind(NO);
        entity.setProxyMarginYearMinus2(null);
        entity.setProxyMarginYearMinus3(null);
        entity.setProxyMarginYearMinus4(null);
    }

    private void calculateBenefitMargins(EnrolmentCalculationEntity entity, List<String> benefitCalculationErrors) {
        List<EnrolmentCalculationMarginEntity> margins = enrolmentCalculationMapper
                .fetchBenefitMargins(entity.getAgristabilityScenarioId());
        if (margins == null || margins.isEmpty()) {
            benefitCalculationErrors.add(EnrolmentCalculationMessages.BENEFIT_NO_MARGIN_DATA);
            return;
        }

        Map<Integer, EnrolmentCalculationMarginEntity> marginByYear = margins.stream()
                .filter(margin -> margin.getReferenceYear() != null)
                .collect(Collectors.toMap(EnrolmentCalculationMarginEntity::getReferenceYear,
                        Function.identity(), (left, right) -> right));

        EnrolmentCalculationMarginEntity programYearMargin = marginByYear.get(entity.getProgramYear());
        boolean programYearMissingProductiveUnits = programYearMargin == null
                || !isYes(programYearMargin.getHasProductiveUnitsInd());
        List<BenefitMargin> averageMargins = new ArrayList<>();
        boolean[] marginFound = new boolean[7];
        boolean zeroMarginInThreeMostRecentYears = false;

        int enrolmentYear = entity.getEnrolmentYear();
        int oldestYear = enrolmentYear - 6;
        int newestYear = enrolmentYear - 2;

        for (EnrolmentCalculationMarginEntity margin : margins.stream()
                .sorted(Comparator.comparing(EnrolmentCalculationMarginEntity::getReferenceYear))
                .toList()) {
            Integer referenceYear = margin.getReferenceYear();
            if (referenceYear == null || referenceYear < oldestYear || referenceYear > newestYear) {
                continue;
            }

            BigDecimal currentMargin = getBenefitMargin(entity, margin, programYearMissingProductiveUnits);
            currentMargin = roundCurrency(currentMargin);
            if (currentMargin == null || !hasIncomeOrExpense(margin)) {
                continue;
            }

            if (Boolean.TRUE.equals(entity.getInCombinedFarm())) {
                BigDecimal percent = entity.getCombinedFarmPercent();
                if (percent == null) {
                    benefitCalculationErrors.add(
                            EnrolmentCalculationMessages.BENEFIT_MISSING_COMBINED_FARM_PERCENT);
                    return;
                }
                currentMargin = roundCurrency(currentMargin.multiply(percent));
            }

            int offset = enrolmentYear - referenceYear;
            setBenefitMarginField(entity, offset, currentMargin, true);
            if (offset >= 2 && offset <= 6) {
                marginFound[offset] = true;
            }
            if (offset >= 2 && offset <= 4 && isZero(currentMargin)) {
                zeroMarginInThreeMostRecentYears = true;
            }

            if (!isZero(currentMargin)) {
                averageMargins.add(new BenefitMargin(offset, currentMargin));
            }
        }

        if (averageMargins.stream().anyMatch(margin -> margin.value().abs().compareTo(OVERSIZE_MARGIN_LIMIT) > 0)) {
            clearBenefitMargins(entity);
            benefitCalculationErrors.add(
                    EnrolmentCalculationMessages.BENEFIT_MARGIN_OUTSIDE_SUPPORTED_RANGE);
            return;
        }

        boolean hasThreeMostRecentYears = marginFound[2] && marginFound[3] && marginFound[4];
        if (hasThreeMostRecentYears && zeroMarginInThreeMostRecentYears) {
            clearBenefitMargins(entity);
            benefitCalculationErrors.add(EnrolmentCalculationMessages.BENEFIT_ZERO_MARGIN_DATA);
            return;
        }
        if (!hasThreeMostRecentYears || averageMargins.size() < 3) {
            clearBenefitMargins(entity);
            benefitCalculationErrors.add(EnrolmentCalculationMessages.BENEFIT_INSUFFICIENT_MARGIN_DATA);
            return;
        }
        if (averageMargins.size() > 5) {
            clearBenefitMargins(entity);
            benefitCalculationErrors.add(EnrolmentCalculationMessages.BENEFIT_TOO_MANY_REFERENCE_MARGINS);
            return;
        }

        List<BenefitMargin> marginsToAverage = new ArrayList<>(averageMargins);
        if (marginsToAverage.size() == 5) {
            BigDecimal min = marginsToAverage.stream().map(BenefitMargin::value).min(BigDecimal::compareTo).orElse(ZERO);
            BigDecimal max = marginsToAverage.stream().map(BenefitMargin::value).max(BigDecimal::compareTo).orElse(ZERO);
            boolean minRemoved = false;
            boolean maxRemoved = false;
            List<BenefitMargin> kept = new ArrayList<>();
            for (BenefitMargin margin : marginsToAverage) {
                if (!minRemoved && margin.value().compareTo(min) == 0) {
                    minRemoved = true;
                    setBenefitMarginUsed(entity, margin.offset(), false);
                } else if (!maxRemoved && margin.value().compareTo(max) == 0) {
                    maxRemoved = true;
                    setBenefitMarginUsed(entity, margin.offset(), false);
                } else {
                    kept.add(margin);
                }
            }
            marginsToAverage = kept;
        } else if (marginsToAverage.size() == 4) {
            marginsToAverage = marginsToAverage.stream()
                    .filter(margin -> margin.offset() >= 2 && margin.offset() <= 4)
                    .toList();
            setBenefitMarginUsed(entity, 5, false);
            setBenefitMarginUsed(entity, 6, false);
        }

        BigDecimal average = average(marginsToAverage.stream().map(BenefitMargin::value).toList());
        BigDecimal fee = calculateEnrolmentFee(entity, average);
        entity.setBenefitContributionMargin(average);
        entity.setBenefitEnrolmentFee(fee);
        entity.setBenefitCalculatedInd(YES);
    }

    private BigDecimal getBenefitMargin(
            EnrolmentCalculationEntity entity,
            EnrolmentCalculationMarginEntity margin,
            boolean programYearMissingProductiveUnits) {
        if (margin.getReferenceYear().equals(entity.getProgramYear())
                || programYearMissingProductiveUnits
                || !isYes(margin.getHasProductiveUnitsInd())) {
            return margin.getProductionMarginAccrualAdjustments();
        }
        BigDecimal structuralChangeMargin = margin.getProductionMarginAfterStructuralChanges();
        if (isZero(structuralChangeMargin) && !isZero(margin.getProductionMarginAccrualAdjustments())) {
            return margin.getProductionMarginAccrualAdjustments();
        }
        return structuralChangeMargin;
    }

    private void calculateProxyMargins(
            EnrolmentCalculationEntity entity,
            List<EnrolmentCalculationProductiveUnitEntity> productiveUnits) {
        if (productiveUnits == null || productiveUnits.isEmpty() || !isYes(entity.getHasBenchmarkPerUnitsInd())) {
            return;
        }
        entity.setProxyMarginsCalculatedInd(YES);
        if (Boolean.TRUE.equals(entity.getInCombinedFarm()) && entity.getCombinedFarmPercent() == null) {
            return;
        }

        BigDecimal marginYearMinus4 = entity.getProductiveValueYearMinus4();
        BigDecimal marginYearMinus3 = entity.getProductiveValueYearMinus3();
        BigDecimal marginYearMinus2 = entity.getProductiveValueYearMinus2();

        if (Boolean.TRUE.equals(entity.getInCombinedFarm())) {
            BigDecimal percent = entity.getCombinedFarmPercent();
            marginYearMinus4 = applyPercent(marginYearMinus4, percent);
            marginYearMinus3 = applyPercent(marginYearMinus3, percent);
            marginYearMinus2 = applyPercent(marginYearMinus2, percent);
        }

        BigDecimal contributionMargin = average(List.of(marginYearMinus2, marginYearMinus3, marginYearMinus4));
        BigDecimal enrolmentFee = calculateEnrolmentFee(entity, contributionMargin);

        entity.setProxyMarginYearMinus2(marginYearMinus2);
        entity.setProxyMarginYearMinus3(marginYearMinus3);
        entity.setProxyMarginYearMinus4(marginYearMinus4);
        entity.setProxyContributionMargin(contributionMargin);
        entity.setProxyEnrolmentFee(enrolmentFee);
    }

    private void calculateManualMargins(EnrolmentCalculationEntity entity) {
        if (entity.getManualMarginYearMinus2() == null
                || entity.getManualMarginYearMinus3() == null
                || entity.getManualMarginYearMinus4() == null) {
            return;
        }

        BigDecimal contributionMargin = average(List.of(
                entity.getManualMarginYearMinus2(),
                entity.getManualMarginYearMinus3(),
                entity.getManualMarginYearMinus4()));
        BigDecimal enrolmentFee = calculateEnrolmentFee(entity, contributionMargin);

        entity.setManualContributionMargin(contributionMargin);
        entity.setManualEnrolmentFee(enrolmentFee);
        entity.setManualCalculatedInd(YES);
    }

    private void defaultCalculationType(EnrolmentCalculationEntity entity) {
        if (entity.getEnrolmentCalcTypeCode() != null) {
            return;
        }
        if (isYes(entity.getBenefitCalculatedInd())) {
            entity.setEnrolmentCalcTypeCode(BENEFIT);
        } else if (isYes(entity.getProxyMarginsCalculatedInd())) {
            entity.setEnrolmentCalcTypeCode(PROXY);
        } else {
            entity.setEnrolmentCalcTypeCode(MANUAL);
        }
    }

    private void calculateSelectedMargins(EnrolmentCalculationEntity entity) {
        if (BENEFIT.equals(entity.getEnrolmentCalcTypeCode())) {
            entity.setContributionMargin(entity.getBenefitContributionMargin());
            entity.setEnrolmentFee(entity.getBenefitEnrolmentFee());
            entity.setMarginYearMinus2(entity.getBenefitMarginYearMinus2());
            entity.setMarginYearMinus3(entity.getBenefitMarginYearMinus3());
            entity.setMarginYearMinus4(entity.getBenefitMarginYearMinus4());
            entity.setMarginYearMinus5(entity.getBenefitMarginYearMinus5());
            entity.setMarginYearMinus6(entity.getBenefitMarginYearMinus6());
            entity.setMarginYearMinus2Ind(entity.getBenefitMargnYearMinus2Ind());
            entity.setMarginYearMinus3Ind(entity.getBenefitMargnYearMinus3Ind());
            entity.setMarginYearMinus4Ind(entity.getBenefitMargnYearMinus4Ind());
            entity.setMarginYearMinus5Ind(entity.getBenefitMargnYearMinus5Ind());
            entity.setMarginYearMinus6Ind(entity.getBenefitMargnYearMinus6Ind());
        } else if (PROXY.equals(entity.getEnrolmentCalcTypeCode())) {
            entity.setContributionMargin(entity.getProxyContributionMargin());
            entity.setEnrolmentFee(entity.getProxyEnrolmentFee());
            entity.setMarginYearMinus2(entity.getProxyMarginYearMinus2());
            entity.setMarginYearMinus3(entity.getProxyMarginYearMinus3());
            entity.setMarginYearMinus4(entity.getProxyMarginYearMinus4());
            entity.setMarginYearMinus2Ind(YES);
            entity.setMarginYearMinus3Ind(YES);
            entity.setMarginYearMinus4Ind(YES);
        } else if (MANUAL.equals(entity.getEnrolmentCalcTypeCode())) {
            entity.setContributionMargin(entity.getManualContributionMargin());
            entity.setEnrolmentFee(entity.getManualEnrolmentFee());
            entity.setMarginYearMinus2(entity.getManualMarginYearMinus2());
            entity.setMarginYearMinus3(entity.getManualMarginYearMinus3());
            entity.setMarginYearMinus4(entity.getManualMarginYearMinus4());
            entity.setMarginYearMinus2Ind(YES);
            entity.setMarginYearMinus3Ind(YES);
            entity.setMarginYearMinus4Ind(YES);
        }
    }

    private void calculateProductiveUnitValues(List<EnrolmentCalculationProductiveUnitEntity> productiveUnits) {
        productiveUnits.forEach(unit -> {
            unit.setProductiveValueYearMinus4(roundCurrency(multiply(unit.getProductiveCapacity(),
                    unit.getBpuMarginYearMinus4())));
            unit.setProductiveValueYearMinus3(roundCurrency(multiply(unit.getProductiveCapacity(),
                    unit.getBpuMarginYearMinus3())));
            unit.setProductiveValueYearMinus2(roundCurrency(multiply(unit.getProductiveCapacity(),
                    unit.getBpuMarginYearMinus2())));
        });
    }

    private boolean productiveUnitsHaveBpus(List<EnrolmentCalculationProductiveUnitEntity> productiveUnits) {
        return !productiveUnits.isEmpty()
                && productiveUnits.stream().allMatch(unit -> !isZero(unit.getBpuMarginYearMinus4())
                        && !isZero(unit.getBpuMarginYearMinus3())
                        && !isZero(unit.getBpuMarginYearMinus2()));
    }

    private BigDecimal calculateEnrolmentFee(EnrolmentCalculationEntity entity, BigDecimal contributionMargin) {
        if (contributionMargin == null) {
            return null;
        }
        BigDecimal fee = roundCurrency(contributionMargin.multiply(getEnrolmentFeeCalculationFactor(entity)));
        return fee.compareTo(MIN_ENROLMENT_FEE) < 0 ? MIN_ENROLMENT_FEE : fee;
    }

    private BigDecimal getEnrolmentFeeCalculationFactor(EnrolmentCalculationEntity entity) {
        if (entity.getEnrolmentYear() != null && entity.getEnrolmentYear() < 2013) {
            return ENROLMENT_FEE_FACTOR_2012_PREVIOUS;
        }
        return ENROLMENT_FEE_FACTOR_2013_FORWARD;
    }

    private void setBenefitMarginField(EnrolmentCalculationEntity entity, int offset, BigDecimal value, boolean used) {
        switch (offset) {
        case 2:
            entity.setBenefitMarginYearMinus2(value);
            entity.setBenefitMargnYearMinus2Ind(toInd(used));
            break;
        case 3:
            entity.setBenefitMarginYearMinus3(value);
            entity.setBenefitMargnYearMinus3Ind(toInd(used));
            break;
        case 4:
            entity.setBenefitMarginYearMinus4(value);
            entity.setBenefitMargnYearMinus4Ind(toInd(used));
            break;
        case 5:
            entity.setBenefitMarginYearMinus5(value);
            entity.setBenefitMargnYearMinus5Ind(toInd(used));
            break;
        case 6:
            entity.setBenefitMarginYearMinus6(value);
            entity.setBenefitMargnYearMinus6Ind(toInd(used));
            break;
        default:
            break;
        }
    }

    private void setBenefitMarginUsed(EnrolmentCalculationEntity entity, int offset, boolean used) {
        switch (offset) {
        case 2:
            entity.setBenefitMargnYearMinus2Ind(toInd(used));
            break;
        case 3:
            entity.setBenefitMargnYearMinus3Ind(toInd(used));
            break;
        case 4:
            entity.setBenefitMargnYearMinus4Ind(toInd(used));
            break;
        case 5:
            entity.setBenefitMargnYearMinus5Ind(toInd(used));
            break;
        case 6:
            entity.setBenefitMargnYearMinus6Ind(toInd(used));
            break;
        default:
            break;
        }
    }

    private void clearBenefitMargins(EnrolmentCalculationEntity entity) {
        entity.setBenefitMarginYearMinus2(null);
        entity.setBenefitMarginYearMinus3(null);
        entity.setBenefitMarginYearMinus4(null);
        entity.setBenefitMarginYearMinus5(null);
        entity.setBenefitMarginYearMinus6(null);
        entity.setBenefitMargnYearMinus2Ind(NO);
        entity.setBenefitMargnYearMinus3Ind(NO);
        entity.setBenefitMargnYearMinus4Ind(NO);
        entity.setBenefitMargnYearMinus5Ind(NO);
        entity.setBenefitMargnYearMinus6Ind(NO);
    }

    private boolean hasIncomeOrExpense(EnrolmentCalculationMarginEntity margin) {
        return !isZero(margin.getTotalAllowableIncome()) || !isZero(margin.getTotalUnallowableExpenses());
    }

    private BigDecimal average(List<BigDecimal> values) {
        return roundCurrency(values.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(new BigDecimal(values.size()), 10, RoundingMode.HALF_UP));
    }

    private BigDecimal sum(
            List<EnrolmentCalculationProductiveUnitEntity> units,
            Function<EnrolmentCalculationProductiveUnitEntity, BigDecimal> accessor) {
        return roundCurrency(units.stream()
                .map(accessor)
                .filter(value -> value != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    private BigDecimal multiply(BigDecimal left, BigDecimal right) {
        if (left == null || right == null) {
            return null;
        }
        return left.multiply(right);
    }

    private BigDecimal applyPercent(BigDecimal value, BigDecimal percent) {
        if (value == null || percent == null) {
            return null;
        }
        return roundCurrency(value.multiply(percent));
    }

    private BigDecimal roundCurrency(BigDecimal value) {
        return value == null ? null : value.setScale(2, RoundingMode.HALF_UP);
    }

    private boolean isYes(String value) {
        return YES.equals(value);
    }

    private boolean isEditable(EnrolmentCalculationEntity entity) {
        return IN_PROGRESS.equals(entity.getScenarioStateCode()) && entity.getAssignedToUserId() != null;
    }

    private boolean isZero(BigDecimal value) {
        return value == null || value.compareTo(ZERO) == 0;
    }

    private String toInd(boolean value) {
        return value ? YES : NO;
    }

    private record BenefitMargin(int offset, BigDecimal value) {
    }
}
