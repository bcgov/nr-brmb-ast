package ca.bc.gov.farms.data.models;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class YearConfigurationParameterModel extends BaseResource {

    private Long yearConfigurationParameterId;

    @NotNull(message = "YearConfigurationParameter programYear must not be null")
    private Integer programYear;

    @NotBlank(message = "YearConfigurationParameter parameterName must not be blank")
    @Size(min = 0, max = 200, message = "YearConfigurationParameter parameterName must be between 0 and 200 characters")
    private String parameterName;

    @NotBlank(message = "YearConfigurationParameter parameterValue must not be blank")
    @Size(min = 0, max = 4000, message = "YearConfigurationParameter parameterValue must be between 0 and 4000 characters")
    private String parameterValue;

    @NotBlank(message = "YearConfigurationParameter configParamTypeCode must not be blank")
    @Size(min = 0, max = 10, message = "YearConfigurationParameter configParamTypeCode must be between 0 and 10 characters")
    private String configParamTypeCode;
    private String userEmail;
}
