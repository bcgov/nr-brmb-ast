package ca.bc.gov.farms.data.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import ca.bc.gov.farms.data.entities.YearConfigurationParameterEntity;

@Mapper
public interface YearConfigurationParameterMapper {

    YearConfigurationParameterEntity fetch(Long yearConfigurationParameterId);

    List<YearConfigurationParameterEntity> fetchAll();

    int insertYearConfigurationParameter(YearConfigurationParameterEntity dto, String userId);

    int updateYearConfigurationParameter(YearConfigurationParameterEntity dto, String userId);

    int deleteYearConfigurationParameter(Long yearConfigurationParameterId);
}
