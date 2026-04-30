package ca.bc.gov.farms.data.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnrolmentCalculationEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer participantPin;
    private Integer programYear;
    private String producerName;
    private Long agristabilityScenarioId;
    private Integer scenarioNumber;
    private String scenarioStateCode;
    private String scenarioCategoryCode;
    private String scenarioClassCode;
    private String assignedToUserId;
    private String assignedToUserGuid;
    private Boolean inCombinedFarm;
    private BigDecimal benefitCombinedFarmPercent;

    private Long scenarioEnrolmentId;
    private Integer enrolmentYear;
    private BigDecimal enrolmentFee;
    private BigDecimal contributionMargin;
    private String benefitCalculatedInd;
    private String proxyMarginsCalculatedInd;
    private String manualCalculatedInd;
    private String hasProductiveUnitsInd;
    private String hasBenchmarkPerUnitsInd;
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
    private String marginYearMinus2Ind;
    private String marginYearMinus3Ind;
    private String marginYearMinus4Ind;
    private String marginYearMinus5Ind;
    private String marginYearMinus6Ind;
    private BigDecimal benefitMarginYearMinus2;
    private BigDecimal benefitMarginYearMinus3;
    private BigDecimal benefitMarginYearMinus4;
    private BigDecimal benefitMarginYearMinus5;
    private BigDecimal benefitMarginYearMinus6;
    private String benefitMargnYearMinus2Ind;
    private String benefitMargnYearMinus3Ind;
    private String benefitMargnYearMinus4Ind;
    private String benefitMargnYearMinus5Ind;
    private String benefitMargnYearMinus6Ind;
    private BigDecimal proxyMarginYearMinus2;
    private BigDecimal proxyMarginYearMinus3;
    private BigDecimal proxyMarginYearMinus4;
    private BigDecimal manualMarginYearMinus2;
    private BigDecimal manualMarginYearMinus3;
    private BigDecimal manualMarginYearMinus4;
    private BigDecimal combinedFarmPercent;
    private String enrolmentCalcTypeCode;
    private Integer revisionCount;

    private BigDecimal productiveValueYearMinus2;
    private BigDecimal productiveValueYearMinus3;
    private BigDecimal productiveValueYearMinus4;
    private List<EnrolmentCalculationProductiveUnitEntity> productiveUnits;
    private List<String> benefitCalculationErrors;
}
