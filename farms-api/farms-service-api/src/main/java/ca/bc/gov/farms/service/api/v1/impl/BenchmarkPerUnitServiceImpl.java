package ca.bc.gov.farms.service.api.v1.impl;

import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.brmb.common.model.Message;
import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.brmb.common.service.api.ValidationFailureException;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.BenchmarkPerUnit;
import ca.bc.gov.farms.model.v1.BenchmarkPerUnitList;
import ca.bc.gov.farms.persistence.v1.dao.BenchmarkPerUnitDao;
import ca.bc.gov.farms.persistence.v1.dto.BenchmarkPerUnitDto;
import ca.bc.gov.farms.service.api.v1.BenchmarkPerUnitService;
import ca.bc.gov.farms.service.api.v1.model.factory.BenchmarkPerUnitFactory;
import ca.bc.gov.farms.service.api.v1.validation.ModelValidator;

public class BenchmarkPerUnitServiceImpl implements BenchmarkPerUnitService {

    private static final Logger logger = LoggerFactory.getLogger(BenchmarkPerUnitServiceImpl.class);

    private Properties applicationProperties;

    private ModelValidator modelValidator;

    // factories
    private BenchmarkPerUnitFactory benchmarkPerUnitFactory;

    // daos
    private BenchmarkPerUnitDao benchmarkPerUnitDao;

    public void setApplicationProperties(Properties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public void setModelValidator(ModelValidator modelValidator) {
        this.modelValidator = modelValidator;
    }

    public void setBenchmarkPerUnitFactory(BenchmarkPerUnitFactory benchmarkPerUnitFactory) {
        this.benchmarkPerUnitFactory = benchmarkPerUnitFactory;
    }

    public void setBenchmarkPerUnitDao(BenchmarkPerUnitDao benchmarkPerUnitDao) {
        this.benchmarkPerUnitDao = benchmarkPerUnitDao;
    }

    @Override
    public BenchmarkPerUnitList<? extends BenchmarkPerUnit> getBenchmarkPerUnitsByProgramYear(Integer programYear,
            FactoryContext factoryContext) throws ServiceException {
        logger.debug("<getBenchmarkPerUnitsByProgramYear");

        BenchmarkPerUnitList<? extends BenchmarkPerUnit> result = null;

        try {
            List<BenchmarkPerUnitDto> dtos = benchmarkPerUnitDao.fetchByProgramYear(programYear);

            result = benchmarkPerUnitFactory.getBenchmarkPerUnitList(dtos, factoryContext);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">getBenchmarkPerUnitsByProgramYear");
        return result;
    }

    @Override
    public BenchmarkPerUnit getBenchmarkPerUnit(Long benchmarkPerUnitId, FactoryContext factoryContext)
            throws ServiceException, NotFoundException {
        logger.debug("<getBenchmarkPerUnit");

        BenchmarkPerUnit result = null;

        try {
            BenchmarkPerUnitDto dto = benchmarkPerUnitDao.fetch(benchmarkPerUnitId);

            if (dto == null) {
                throw new NotFoundException("Did not find the benchmark per unit: " + benchmarkPerUnitId);
            }

            result = benchmarkPerUnitFactory.getBenchmarkPerUnit(dto, factoryContext);

        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">getBenchmarkPerUnit");
        return result;
    }

    @Override
    public BenchmarkPerUnit createBenchmarkPerUnit(BenchmarkPerUnit resource, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException {
        logger.debug("<createBenchmarkPerUnit");

        BenchmarkPerUnit result = null;
        String userId = "UserId";

        try {

            List<Message> errors = modelValidator.validateBenchmarkPerUnit(resource);
            if (!errors.isEmpty()) {
                throw new ValidationFailureException(errors);
            }

            BenchmarkPerUnitDto dto = new BenchmarkPerUnitDto();

            benchmarkPerUnitFactory.updateBenchmarkPerUnit(dto, resource);
            benchmarkPerUnitDao.insert(dto, userId);

            dto = benchmarkPerUnitDao.fetch(dto.getBenchmarkPerUnitId());
            result = benchmarkPerUnitFactory.getBenchmarkPerUnit(dto, factoryContext);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">createBenchmarkPerUnit");
        return result;
    }

    @Override
    public BenchmarkPerUnit updateBenchmarkPerUnit(Long benchmarkPerUnitId, BenchmarkPerUnit benchmarkPerUnit,
            FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException, NotFoundException {
        logger.debug("<updateBenchmarkPerUnit");

        BenchmarkPerUnit result = null;
        String userId = "UserId";

        try {

            List<Message> errors = modelValidator.validateBenchmarkPerUnit(benchmarkPerUnit);
            if (!errors.isEmpty()) {
                throw new ValidationFailureException(errors);
            }

            BenchmarkPerUnitDto dto = benchmarkPerUnitDao.fetch(benchmarkPerUnitId);

            if (dto == null) {
                throw new NotFoundException("Did not find the benchmark per unit: "
                        + benchmarkPerUnitId);
            }

            benchmarkPerUnitFactory.updateBenchmarkPerUnit(dto, benchmarkPerUnit);
            benchmarkPerUnitDao.update(dto, userId);

            dto = benchmarkPerUnitDao.fetch(benchmarkPerUnitId);
            result = benchmarkPerUnitFactory.getBenchmarkPerUnit(dto, factoryContext);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">updateBenchmarkPerUnit");
        return result;
    }

    @Override
    public void deleteBenchmarkPerUnit(Long benchmarkPerUnitId)
            throws ServiceException, NotFoundException {
        logger.debug("<deleteBenchmarkPerUnit");

        try {
            BenchmarkPerUnitDto dto = benchmarkPerUnitDao.fetch(benchmarkPerUnitId);

            if (dto == null) {
                throw new NotFoundException("Did not find the benchmark per unit: " + benchmarkPerUnitId);
            }

            benchmarkPerUnitDao.delete(benchmarkPerUnitId);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">deleteBenchmarkPerUnit");
    }

}
