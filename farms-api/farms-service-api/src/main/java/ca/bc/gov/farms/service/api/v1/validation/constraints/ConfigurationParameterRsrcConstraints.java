package ca.bc.gov.farms.service.api.v1.validation.constraints;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import ca.bc.gov.farms.service.api.v1.validation.Errors;

public interface ConfigurationParameterRsrcConstraints {

    @NotBlank(message = Errors.PARAMETER_NAME_NOTBLANK, groups = ConfigurationParameterRsrcConstraints.class)
    @Size(min = 0, max = 200, message = Errors.PARAMETER_NAME_SIZE, groups = ConfigurationParameterRsrcConstraints.class)
    public String getParameterName();

    @NotBlank(message = Errors.PARAMETER_VALUE_NOTBLANK, groups = ConfigurationParameterRsrcConstraints.class)
    @Size(min = 0, max = 4000, message = Errors.PARAMETER_VALUE_SIZE, groups = ConfigurationParameterRsrcConstraints.class)
    public String getParameterValue();

    @NotBlank(message = Errors.SENSITIVE_DATA_IND_NOTBLANK, groups = ConfigurationParameterRsrcConstraints.class)
    @Size(min = 0, max = 1, message = Errors.SENSITIVE_DATA_IND_SIZE, groups = ConfigurationParameterRsrcConstraints.class)
    public String getSensitiveDataInd();

    @NotBlank(message = Errors.CONFIG_PARAM_TYPE_CODE_NOTBLANK, groups = ConfigurationParameterRsrcConstraints.class)
    @Size(min = 0, max = 10, message = Errors.CONFIG_PARAM_TYPE_CODE_SIZE, groups = ConfigurationParameterRsrcConstraints.class)
    public String getConfigParamTypeCode();
}
