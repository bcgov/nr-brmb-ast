package ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.farms.persistence.v1.dto.BenchmarkPerUnitDto;

public interface BenchmarkPerUnitMapper {

    BenchmarkPerUnitDto fetch(Map<String, Object> parameters);

    List<BenchmarkPerUnitDto> fetchBy(Map<String, Object> parameters);

    int insertBenchmarkPerUnit(Map<String, Object> parameters);
    int insertBenchmarkYear(Map<String, Object> parameters);

    int update(Map<String, Object> parameters);

    int deleteBenchmarkYear(Map<String, Object> parameters);
    int deleteBenchmarkPerUnit(Map<String, Object> parameters);
}
