package ca.bc.gov.farms.persistence.v1.dto;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.brmb.common.persistence.dto.BaseDto;
import ca.bc.gov.brmb.common.persistence.utils.DtoUtils;

public class ConfigurationParameterDto extends BaseDto<ConfigurationParameterDto> {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationParameterDto.class);

    private Long configurationParameterId;
    private String parameterName;
    private String parameterValue;
    private String sensitiveDataInd;
    private String configParamTypeCode;

    private Integer revisionCount;
    private String createUser;
    private Date createDate;
    private String updateUser;
    private Date updateDate;

    public ConfigurationParameterDto() {
    }

    public ConfigurationParameterDto(ConfigurationParameterDto dto) {
        this.configurationParameterId = dto.configurationParameterId;
        this.parameterName = dto.parameterName;
        this.parameterValue = dto.parameterValue;
        this.sensitiveDataInd = dto.sensitiveDataInd;
        this.configParamTypeCode = dto.configParamTypeCode;

        this.revisionCount = dto.revisionCount;
        this.createUser = dto.createUser;
        this.createDate = dto.createDate;
        this.updateUser = dto.updateUser;
        this.updateDate = dto.updateDate;
    }

    @Override
    public ConfigurationParameterDto copy() {
        return new ConfigurationParameterDto(this);
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public boolean equalsBK(ConfigurationParameterDto other) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    @Override
    public boolean equalsAll(ConfigurationParameterDto other) {
        boolean result = false;

        if (other != null) {
            result = true;
            DtoUtils dtoUtils = new DtoUtils(getLogger());
            result = result && dtoUtils.equals("configurationParameterId", this.configurationParameterId,
                    other.configurationParameterId);
            result = result && dtoUtils.equals("parameterName", this.parameterName, other.parameterName);
            result = result && dtoUtils.equals("parameterValue", this.parameterValue, other.parameterValue);
            result = result && dtoUtils.equals("sensitiveDataInd", this.sensitiveDataInd, other.sensitiveDataInd);
            result = result
                    && dtoUtils.equals("configParamTypeCode", this.configParamTypeCode, other.configParamTypeCode);
        }

        return result;
    }

    public Long getConfigurationParameterId() {
        return configurationParameterId;
    }

    public void setConfigurationParameterId(Long configurationParameterId) {
        this.configurationParameterId = configurationParameterId;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    public String getSensitiveDataInd() {
        return sensitiveDataInd;
    }

    public void setSensitiveDataInd(String sensitiveDataInd) {
        this.sensitiveDataInd = sensitiveDataInd;
    }

    public String getConfigParamTypeCode() {
        return configParamTypeCode;
    }

    public void setConfigParamTypeCode(String configParamTypeCode) {
        this.configParamTypeCode = configParamTypeCode;
    }

    public Integer getRevisionCount() {
        return revisionCount;
    }

    public void setRevisionCount(Integer revisionCount) {
        this.revisionCount = revisionCount;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

}
