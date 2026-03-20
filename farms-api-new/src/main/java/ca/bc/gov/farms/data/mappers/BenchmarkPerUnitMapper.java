package ca.bc.gov.farms.data.mappers;

import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import ca.bc.gov.farms.data.entities.BenchmarkPerUnitEntity;

@Mapper
public interface BenchmarkPerUnitMapper {

    BenchmarkPerUnitEntity fetch(Long benchmarkPerUnitId);

    List<BenchmarkPerUnitEntity> fetchByProgramYear(Integer programYear);

    int insertBenchmarkPerUnit(BenchmarkPerUnitEntity dto, String userId);

    int insertBenchmarkYear(Long benchmarkPerUnitId, Integer benchmarkYear, BigDecimal averageMargin,
            BigDecimal averageExpense, String userId);

    int updateBenchmarkPerUnit(BenchmarkPerUnitEntity dto, String userId);

    int updateBenchmarkYear(Long benchmarkPerUnitId, Integer benchmarkYear, BigDecimal averageMargin,
            BigDecimal averageExpense, String userId);

    int deleteBenchmarkYear(Long benchmarkPerUnitId);

    int deleteBenchmarkPerUnit(Long benchmarkPerUnitId);
}
