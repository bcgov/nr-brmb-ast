package ca.bc.gov.farms.data.models;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@Data
@EqualsAndHashCode(callSuper = false)
public class ScenarioBenefitRsrc extends BaseResource {
    private Integer pin;
    private Integer programYear;
    private BigDecimal programYearMargin;
    private BigDecimal referenceMargin;
    private String scenarioCategoryCode;
    private String scenarioCategory;
    private BigDecimal wholeFarmBenefit;
    private BigDecimal deemedPiDeduction;
    private BigDecimal combinedPercent;
    private BigDecimal benefitAfterCombinedPercent;
    private BigDecimal bcEnhancement;
    private BigDecimal interimPaymentPercentage;
    private BigDecimal lateEnrolmentPenalty;
    private BigDecimal paymentLimitationAmount;
}
