package ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.farms.persistence.v1.dto.FruitVegTypeDetailDto;

public interface FruitVegTypeDetailMapper {

    FruitVegTypeDetailDto fetch(Map<String, Object> parameters);

    List<FruitVegTypeDetailDto> fetchByProgramYear(Map<String, Object> parameters);

    int insertFruitVegTypeDetail(Map<String, Object> parameters);

    int updateFruitVegTypeDetail(Map<String, Object> parameters);

    int deleteFruitVegTypeDetail(Map<String, Object> parameters);
}
