package ca.bc.gov.farms.data.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import ca.bc.gov.farms.data.entities.BenchmarkPerUnitEntity;

@Mapper
public interface BenchmarkPerUnitMapper {

    BenchmarkPerUnitEntity fetch(Long benchmarkPerUnitId);

    List<BenchmarkPerUnitEntity> fetchByProgramYear(Integer programYear);

    void insert(BenchmarkPerUnitEntity dto, String userId);

    void update(BenchmarkPerUnitEntity dto, String userId);

    void delete(Long benchmarkPerUnitId);
}
