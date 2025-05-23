package ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.farms.persistence.v1.dto.FairMarketValueDto;

public interface FairMarketValueMapper {

    FairMarketValueDto fetch(Map<String, Object> parameters);

    List<FairMarketValueDto> fetchBy(Map<String, Object> parameters);

    int insertFairMarketValue(Map<String, Object> parameters);

    int updateFairMarketValue(Map<String, Object> parameters);

    int deleteFairMarketValue(Map<String, Object> parameters);
}
