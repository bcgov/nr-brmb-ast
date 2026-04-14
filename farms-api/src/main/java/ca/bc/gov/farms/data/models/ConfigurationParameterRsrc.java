package ca.bc.gov.farms.data.models;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import jakarta.validation.constraints.NotBlank;
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
public class ConfigurationParameterRsrc extends BaseResource {

    private Long configurationParameterId;

    @NotBlank(message = "ConfigurationParameter parameterName must not be blank")
    @Size(min = 0, max = 200, message = "ConfigurationParameter parameterName must be between 0 and 200 characters")
    private String parameterName;

    @NotBlank(message = "ConfigurationParameter parameterValue must not be blank")
    @Size(min = 0, max = 4000, message = "ConfigurationParameter parameterValue must be between 0 and 4000 characters")
    private String parameterValue;

    @NotBlank(message = "ConfigurationParameter sensitiveDataInd must not be blank")
    @Size(min = 1, max = 1, message = "ConfigurationParameter sensitiveDataInd must be 1 character")
    private String sensitiveDataInd;

    @NotBlank(message = "ConfigurationParameter configParamTypeCode must not be blank")
    @Size(min = 0, max = 10, message = "ConfigurationParameter configParamTypeCode must be between 0 and 10 characters")
    private String configParamTypeCode;
    private String userEmail;
}
