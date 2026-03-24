package ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.farms.persistence.v1.dto.YearConfigurationParameterDto;

public interface YearConfigurationParameterMapper {

    YearConfigurationParameterDto fetch(Map<String, Object> parameters);

    List<YearConfigurationParameterDto> fetchAll();

    int insertYearConfigurationParameter(Map<String, Object> parameters);

    int updateYearConfigurationParameter(Map<String, Object> parameters);

    int deleteYearConfigurationParameter(Map<String, Object> parameters);
}
