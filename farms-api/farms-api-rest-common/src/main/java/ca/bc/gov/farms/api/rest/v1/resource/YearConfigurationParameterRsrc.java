package ca.bc.gov.farms.api.rest.v1.resource;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.farms.model.v1.YearConfigurationParameter;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.YEAR_CONFIGURATION_PARAMETER_NAME)
@XmlSeeAlso({ YearConfigurationParameterRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class YearConfigurationParameterRsrc extends BaseResource implements YearConfigurationParameter {

    private static final long serialVersionUID = 1L;

    private Long yearConfigurationParameterId;
    private Integer programYear;
    private String parameterName;
    private String parameterValue;
    private String configParamTypeCode;
    private String userEmail;

    @Override
    public Long getYearConfigurationParameterId() {
        return yearConfigurationParameterId;
    }

    @Override
    public void setYearConfigurationParameterId(Long yearConfigurationParameterId) {
        this.yearConfigurationParameterId = yearConfigurationParameterId;
    }

    @Override
    public Integer getProgramYear() {
        return programYear;
    }

    @Override
    public void setProgramYear(Integer programYear) {
        this.programYear = programYear;
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
