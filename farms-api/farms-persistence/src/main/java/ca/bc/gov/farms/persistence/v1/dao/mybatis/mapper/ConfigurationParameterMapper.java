package ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper;

import java.util.Map;

import ca.bc.gov.farms.persistence.v1.dto.ConfigurationParameterDto;

public interface ConfigurationParameterMapper {

    ConfigurationParameterDto fetch(Map<String, Object> parameters);

    ConfigurationParameterDto fetchAll();

    int insertConfigurationParameter(Map<String, Object> parameters);

    int updateConfigurationParameter(Map<String, Object> parameters);

    int deleteConfigurationParameter(Map<String, Object> parameters);
}
