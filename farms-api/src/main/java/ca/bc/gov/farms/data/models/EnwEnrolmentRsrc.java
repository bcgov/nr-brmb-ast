package ca.bc.gov.farms.data.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnwEnrolmentRsrc implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long enwEnrolmentId;
    private Integer enrolmentYear;
    private BigDecimal enrolmentFee;
    private BigDecimal contributionMargin;
    private Boolean benefitCalculated;
    private Boolean proxyMarginsCalculated;
    private Boolean manualMarginsCalculated;
    private Boolean hasProductiveUnits;
    private Boolean hasBpus;
    private Boolean canCalculateProxyMargins;
    private Integer programYear;
    private BigDecimal enrolmentFeeMinimum;
    private BigDecimal enrolmentFeeCalculationFactor;
    private List<Integer> benefitMarginYears;
    private BigDecimal productiveValueYearMinus2;
    private BigDecimal productiveValueYearMinus3;
    private BigDecimal productiveValueYearMinus4;
    private List<Integer> proxyMarginYears;
    private List<BigDecimal> proxyMargins;
    private List<EnwProductiveUnitRsrc> enwProductiveUnits;
    private Boolean inCombinedFarm;
    private BigDecimal benefitEnrolmentFee;
    private BigDecimal benefitContributionMargin;
    private BigDecimal proxyEnrolmentFee;
    private BigDecimal proxyContributionMargin;
    private BigDecimal manualEnrolmentFee;
    private BigDecimal manualContributionMargin;
    private BigDecimal marginYearMinus2;
    private BigDecimal marginYearMinus3;
    private BigDecimal marginYearMinus4;
    private BigDecimal marginYearMinus5;
    private BigDecimal marginYearMinus6;
    private Boolean marginYearMinus2Used;
    private Boolean marginYearMinus3Used;
    private Boolean marginYearMinus4Used;
    private Boolean marginYearMinus5Used;
    private Boolean marginYearMinus6Used;
    private BigDecimal benefitMarginYearMinus2;
    private BigDecimal benefitMarginYearMinus3;
    private BigDecimal benefitMarginYearMinus4;
    private BigDecimal benefitMarginYearMinus5;
    private BigDecimal benefitMarginYearMinus6;
    private Boolean benefitMarginYearMinus2Used;
    private Boolean benefitMarginYearMinus3Used;
    private Boolean benefitMarginYearMinus4Used;
    private Boolean benefitMarginYearMinus5Used;
    private Boolean benefitMarginYearMinus6Used;
    private BigDecimal proxyMarginYearMinus2;
    private BigDecimal proxyMarginYearMinus3;
    private BigDecimal proxyMarginYearMinus4;
    private BigDecimal manualMarginYearMinus2;
    private BigDecimal manualMarginYearMinus3;
    private BigDecimal manualMarginYearMinus4;
    private BigDecimal combinedFarmPercent;
    private String enrolmentCalculationTypeCode;
    private Integer revisionCount;
}
