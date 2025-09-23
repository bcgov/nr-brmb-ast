package ca.bc.gov.farms.service.api.v1.validation.constraints;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import ca.bc.gov.farms.service.api.v1.validation.Errors;

public interface YearConfigurationParameterRsrcConstraints {

    @NotNull(message = Errors.PROGRAM_YEAR_NOTNULL, groups = YearConfigurationParameterRsrcConstraints.class)
    public Integer getProgramYear();

    @NotBlank(message = Errors.PARAMETER_NAME_NOTBLANK, groups = YearConfigurationParameterRsrcConstraints.class)
    @Size(min = 0, max = 200, message = Errors.PARAMETER_NAME_SIZE, groups = YearConfigurationParameterRsrcConstraints.class)
    public String getParameterName();

    @NotBlank(message = Errors.PARAMETER_VALUE_NOTBLANK, groups = YearConfigurationParameterRsrcConstraints.class)
    @Size(min = 0, max = 4000, message = Errors.PARAMETER_VALUE_SIZE, groups = YearConfigurationParameterRsrcConstraints.class)
    public String getParameterValue();

    @NotBlank(message = Errors.CONFIG_PARAM_TYPE_CODE_NOTBLANK, groups = YearConfigurationParameterRsrcConstraints.class)
    @Size(min = 0, max = 10, message = Errors.CONFIG_PARAM_TYPE_CODE_SIZE, groups = YearConfigurationParameterRsrcConstraints.class)
    public String getConfigParamTypeCode();
}
