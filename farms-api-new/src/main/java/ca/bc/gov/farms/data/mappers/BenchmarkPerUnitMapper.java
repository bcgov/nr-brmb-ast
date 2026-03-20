package ca.bc.gov.farms.data.mappers;

import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import ca.bc.gov.farms.data.entities.BenchmarkPerUnitEntity;

@Mapper
public interface BenchmarkPerUnitMapper {

    BenchmarkPerUnitEntity fetch(Long benchmarkPerUnitId);

    List<BenchmarkPerUnitEntity> fetchByProgramYear(Integer programYear);

    void insertBenchmarkPerUnit(BenchmarkPerUnitEntity dto, String userId);

    void insertBenchmarkYear(Long benchmarkPerUnitId, Integer benchmarkYear, BigDecimal averageMargin,
            BigDecimal averageExpense, String userId);

    void updateBenchmarkPerUnit(BenchmarkPerUnitEntity dto, String userId);

    void updateBenchmarkYear(Long benchmarkPerUnitId, Integer benchmarkYear, BigDecimal averageMargin,
            BigDecimal averageExpense, String userId);

    void deleteBenchmarkYear(Long benchmarkPerUnitId);

    void deleteBenchmarkPerUnit(Long benchmarkPerUnitId);
}
