package ca.bc.gov.farms.data.models;

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
public class EnrolmentCalculationRsrc extends BaseResource {

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
    private Boolean editable;
    private List<String> benefitCalculationErrors;
    private EnwEnrolmentRsrc enwEnrolment;
}
