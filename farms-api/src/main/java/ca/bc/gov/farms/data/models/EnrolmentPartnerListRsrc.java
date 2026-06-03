package ca.bc.gov.farms.data.models;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnrolmentPartnerListRsrc extends BaseResource {

    private Integer participantPin;
    private Integer programYear;
    private Long agristabilityScenarioId;
    private Integer scenarioNumber;
    private Boolean inCombinedFarm;
    private Integer combinedFarmNumber;
    private BigDecimal combinedFarmPercent;
    private List<EnrolmentPartnerRsrc> enrolmentPartnerList;
}
