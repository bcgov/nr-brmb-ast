package ca.bc.gov.farms.data.models;

import java.math.BigDecimal;

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
public class EnrolmentPartnerRsrc extends BaseResource {

    private Long agristabilityScenarioId;
    private Integer scenarioNumber;
    private String operationSchedule;
    private String partnershipName;
    private Integer operationPartnershipPin;
    private BigDecimal operationPartnershipPercent;
    private Long farmingOperationPartnerId;
    private BigDecimal partnerPercent;
    private Integer partnerParticipantPin;
    private BigDecimal partnerEnrolmentFee;
    private String firstName;
    private String lastName;
    private String corporationName;
}
