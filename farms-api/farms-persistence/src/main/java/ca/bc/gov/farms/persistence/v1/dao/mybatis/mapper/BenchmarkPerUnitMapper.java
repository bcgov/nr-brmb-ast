package ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.farms.persistence.v1.dto.BenchmarkPerUnitDto;

public interface BenchmarkPerUnitMapper {

    BenchmarkPerUnitDto fetch(Map<String, Object> parameters);

    List<BenchmarkPerUnitDto> fetchAll(Map<String, Object> parameters);

    int insert(Map<String, Object> parameters);

    int update(Map<String, Object> parameters);

    int delete(Map<String, Object> parameters);
}
