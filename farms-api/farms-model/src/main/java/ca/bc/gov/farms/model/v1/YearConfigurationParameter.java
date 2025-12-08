package ca.bc.gov.farms.model.v1;

import java.io.Serializable;

public interface YearConfigurationParameter extends Serializable {

    public Long getYearConfigurationParameterId();
    public void setYearConfigurationParameterId(Long yearConfigurationParameterId);

    public Integer getProgramYear();
    public void setProgramYear(Integer programYear);

    public String getParameterName();
    public void setParameterName(String parameterName);

    public String getParameterValue();
    public void setParameterValue(String parameterValue);

    public String getConfigParamTypeCode();
    public void setConfigParamTypeCode(String configParamTypeCode);

    public String getUserEmail();
    public void setUserEmail(String userEmail);
}
