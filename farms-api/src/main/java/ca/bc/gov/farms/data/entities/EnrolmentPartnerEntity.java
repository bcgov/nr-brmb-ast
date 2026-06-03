package ca.bc.gov.farms.data.entities;

import java.io.Serializable;
import java.math.BigDecimal;

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
public class EnrolmentPartnerEntity implements Serializable {
    private static final long serialVersionUID = 1L;

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
