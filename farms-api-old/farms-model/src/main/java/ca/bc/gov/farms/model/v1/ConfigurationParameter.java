package ca.bc.gov.farms.model.v1;

import java.io.Serializable;

public interface ConfigurationParameter extends Serializable {

    public Long getConfigurationParameterId();
    public void setConfigurationParameterId(Long configurationParameterId);

    public String getParameterName();
    public void setParameterName(String parameterName);

    public String getParameterValue();
    public void setParameterValue(String parameterValue);

    public String getSensitiveDataInd();
    public void setSensitiveDataInd(String sensitiveDataInd);

    public String getConfigParamTypeCode();
    public void setConfigParamTypeCode(String configParamTypeCode);

    public String getUserEmail();
    public void setUserEmail(String userEmail);
}
