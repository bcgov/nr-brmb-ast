package ca.bc.gov.farms.service.api.v1;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.brmb.common.service.api.ValidationFailureException;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.BenchmarkPerUnit;

public interface BenchmarkPerUnitService {

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    BenchmarkPerUnit getBenchmarkPerUnit(Long benchmarkPerUnitId, FactoryContext factoryContext)
            throws ServiceException, NotFoundException;

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    List<BenchmarkPerUnit> getAllBenchmarkPerUnits(FactoryContext factoryContext)
            throws ServiceException;

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    BenchmarkPerUnit createBenchmarkPerUnit(BenchmarkPerUnit benchmarkPerUnit, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException;

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    BenchmarkPerUnit updateBenchmarkPerUnit(BenchmarkPerUnit benchmarkPerUnit, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException, NotFoundException;

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    void deleteBenchmarkPerUnit(Long benchmarkPerUnitId)
            throws ServiceException, NotFoundException;
}
