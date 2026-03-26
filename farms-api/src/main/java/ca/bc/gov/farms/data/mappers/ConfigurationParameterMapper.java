package ca.bc.gov.farms.data.mappers;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import ca.bc.gov.farms.data.entities.ConfigurationParameterEntity;

@Mapper
public interface ConfigurationParameterMapper {

    ConfigurationParameterEntity fetch(Long ConfigurationParameterId);

    List<ConfigurationParameterEntity> fetchAll();

    List<ConfigurationParameterEntity> fetchByParameterNamePrefix(String parameterNamePrefix);

    int insertConfigurationParameter(ConfigurationParameterEntity dto, String userId);

    int updateConfigurationParameter(ConfigurationParameterEntity dto, String userId);

    int deleteConfigurationParameter(Long configurationParameterId);
}
