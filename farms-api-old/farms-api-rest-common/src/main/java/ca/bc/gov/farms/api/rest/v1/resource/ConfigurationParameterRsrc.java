package ca.bc.gov.farms.api.rest.v1.resource;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.farms.model.v1.ConfigurationParameter;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.CONFIGURATION_PARAMETER_NAME)
@XmlSeeAlso({ ConfigurationParameterRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class ConfigurationParameterRsrc extends BaseResource implements ConfigurationParameter {

    private static final long serialVersionUID = 1L;

    private Long configurationParameterId;
    private String parameterName;
    private String parameterValue;
    private String sensitiveDataInd;
    private String configParamTypeCode;
    private String userEmail;

    @Override
    public Long getConfigurationParameterId() {
        return configurationParameterId;
    }

    @Override
    public void setConfigurationParameterId(Long configurationParameterId) {
        this.configurationParameterId = configurationParameterId;
    }

    @Override
    public String getParameterName() {
        return parameterName;
    }

    @Override
    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    @Override
    public String getParameterValue() {
        return parameterValue;
    }

    @Override
    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    @Override
    public String getSensitiveDataInd() {
        return sensitiveDataInd;
    }

    @Override
    public void setSensitiveDataInd(String sensitiveDataInd) {
        this.sensitiveDataInd = sensitiveDataInd;
    }

    @Override
    public String getConfigParamTypeCode() {
        return configParamTypeCode;
    }

    @Override
    public void setConfigParamTypeCode(String configParamTypeCode) {
        this.configParamTypeCode = configParamTypeCode;
    }

    @Override
    public String getUserEmail() {
        return userEmail;
    }

    @Override
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
