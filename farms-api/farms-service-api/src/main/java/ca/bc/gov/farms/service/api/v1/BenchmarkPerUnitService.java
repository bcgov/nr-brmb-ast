package ca.bc.gov.farms.service.api.v1;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.brmb.common.service.api.ValidationFailureException;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.BenchmarkPerUnit;
import ca.bc.gov.farms.model.v1.BenchmarkPerUnitList;

public interface BenchmarkPerUnitService {

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    BenchmarkPerUnitList<? extends BenchmarkPerUnit> getBenchmarkPerUnitsByProgramYear(Integer programYear, FactoryContext factoryContext)
            throws ServiceException;

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    BenchmarkPerUnit getBenchmarkPerUnit(Long benchmarkPerUnitId, FactoryContext factoryContext)
            throws ServiceException, NotFoundException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    BenchmarkPerUnit createBenchmarkPerUnit(BenchmarkPerUnit benchmarkPerUnit, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    BenchmarkPerUnit updateBenchmarkPerUnit(Long benchmarkPerUnitId, BenchmarkPerUnit benchmarkPerUnit, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException, NotFoundException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    void deleteBenchmarkPerUnit(Long benchmarkPerUnitId)
            throws ServiceException, NotFoundException;
}
