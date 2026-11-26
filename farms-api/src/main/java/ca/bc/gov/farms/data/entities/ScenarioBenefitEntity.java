package ca.bc.gov.farms.data.entities;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ScenarioBenefitEntity {
    private Integer pin;
    private Integer programYear;
    private BigDecimal programYearMargin;
    private BigDecimal referenceMargin;
    private String scenarioCategoryCode;
    private String scenarioCategory;
    private BigDecimal benefitBeforeDeductions;
    private BigDecimal benefitAfterProdInsDeduction;
    private BigDecimal wholeFarmBenefit;
    private BigDecimal deemedPiDeduction;
    private BigDecimal combinedPercent;
    private BigDecimal benefitAfterCombinedPercent;
    private BigDecimal bcEnhancement;
    private BigDecimal combinedFarmBcEnhancedBenefit;
    private BigDecimal interimPaymentPercentage;
    private BigDecimal benefitAfterInterimDeduction;
    private BigDecimal lateEnrolmentPenalty;
    private BigDecimal paymentLimitationAmount;
}
