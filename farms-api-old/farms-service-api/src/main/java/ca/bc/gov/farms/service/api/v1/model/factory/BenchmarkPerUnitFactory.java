package ca.bc.gov.farms.service.api.v1.model.factory;

import java.util.List;

import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.BenchmarkPerUnit;
import ca.bc.gov.farms.model.v1.BenchmarkPerUnitList;
import ca.bc.gov.farms.persistence.v1.dto.BenchmarkPerUnitDto;

public interface BenchmarkPerUnitFactory {

    BenchmarkPerUnit getBenchmarkPerUnit(BenchmarkPerUnitDto dto, FactoryContext context);

    BenchmarkPerUnitList<? extends BenchmarkPerUnit> getBenchmarkPerUnitList(
            List<BenchmarkPerUnitDto> dtos, FactoryContext context);

    void updateBenchmarkPerUnit(BenchmarkPerUnitDto dto, BenchmarkPerUnit model);
}
