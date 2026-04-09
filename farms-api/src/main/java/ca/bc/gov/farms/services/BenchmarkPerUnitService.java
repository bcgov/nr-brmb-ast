package ca.bc.gov.farms.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.farms.data.assemblers.BenchmarkPerUnitResourceAssembler;
import ca.bc.gov.farms.data.entities.BenchmarkPerUnitEntity;
import ca.bc.gov.farms.data.mappers.BenchmarkPerUnitMapper;
import ca.bc.gov.farms.data.models.BenchmarkPerUnitListModel;
import ca.bc.gov.farms.data.models.BenchmarkPerUnitRsrc;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BenchmarkPerUnitService {

    @Autowired
    private BenchmarkPerUnitMapper benchmarkPerUnitMapper;

    @Autowired
    private BenchmarkPerUnitResourceAssembler benchmarkPerUnitResourceAssembler;

    @Autowired
    private Validator validator;

    public BenchmarkPerUnitListModel getBenchmarkPerUnitsByProgramYear(Integer programYear) throws ServiceException {

        BenchmarkPerUnitListModel result = null;

        try {
            List<BenchmarkPerUnitEntity> entities = benchmarkPerUnitMapper.fetchByProgramYear(programYear);

            result = benchmarkPerUnitResourceAssembler.getBenchmarkPerUnitList(entities);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    public BenchmarkPerUnitRsrc getBenchmarkPerUnit(Long benchmarkPerUnitId)
            throws ServiceException, NotFoundException {

        BenchmarkPerUnitRsrc result = null;

        try {
            BenchmarkPerUnitEntity entity = benchmarkPerUnitMapper.fetch(benchmarkPerUnitId);

            if (entity == null) {
                throw new NotFoundException("Did not find the benchmark per unit: " + benchmarkPerUnitId);
            }

            result = benchmarkPerUnitResourceAssembler.getBenchmarkPerUnit(entity);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public BenchmarkPerUnitRsrc createBenchmarkPerUnit(BenchmarkPerUnitRsrc resource)
            throws ServiceException, ConstraintViolationException {

        Set<ConstraintViolation<BenchmarkPerUnitRsrc>> violations = validator.validate(resource);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        BenchmarkPerUnitRsrc result = null;
        String userId = resource.getUserEmail();

        try {
            BenchmarkPerUnitEntity entity = new BenchmarkPerUnitEntity();

            benchmarkPerUnitResourceAssembler.updateBenchmarkPerUnit(resource, entity);

            int count = benchmarkPerUnitMapper.insertBenchmarkPerUnit(entity, userId);
            if (count == 0) {
                throw new ServiceException("Record not inserted: " + count);
            }

            Long benchmarkPerUnitId = entity.getBenchmarkPerUnitId();
            BigDecimal[] margins = { entity.getYearMinus1Margin(), entity.getYearMinus2Margin(),
                    entity.getYearMinus3Margin(),
                    entity.getYearMinus4Margin(), entity.getYearMinus5Margin(), entity.getYearMinus6Margin() };
            BigDecimal[] expenses = { entity.getYearMinus1Expense(), entity.getYearMinus2Expense(),
                    entity.getYearMinus3Expense(), entity.getYearMinus4Expense(), entity.getYearMinus5Expense(),
                    entity.getYearMinus6Expense() };
            for (int i = 0; i < margins.length; i++) {
                Integer benchmarkYear = entity.getProgramYear() - (i + 1);
                BigDecimal averageMargin = margins[i] != null ? margins[i] : BigDecimal.ZERO;
                BigDecimal averageExpense = expenses[i];
                count = benchmarkPerUnitMapper.insertBenchmarkYear(benchmarkPerUnitId, benchmarkYear, averageMargin,
                        averageExpense, userId);
                if (count == 0) {
                    throw new ServiceException("Record not inserted: " + count);
                }
            }

            entity = benchmarkPerUnitMapper.fetch(benchmarkPerUnitId);
            result = benchmarkPerUnitResourceAssembler.getBenchmarkPerUnit(entity);
        } catch (ServiceException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public BenchmarkPerUnitRsrc updateBenchmarkPerUnit(Long benchmarkPerUnitId, BenchmarkPerUnitRsrc resource)
            throws ServiceException, ConstraintViolationException, NotFoundException {

        Set<ConstraintViolation<BenchmarkPerUnitRsrc>> violations = validator.validate(resource);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        BenchmarkPerUnitRsrc result = null;
        String userId = resource.getUserEmail();

        try {
            BenchmarkPerUnitEntity entity = benchmarkPerUnitMapper.fetch(benchmarkPerUnitId);
            if (entity == null) {
                throw new NotFoundException("Did not find the benchmark per unit: " + benchmarkPerUnitId);
            }

            benchmarkPerUnitResourceAssembler.updateBenchmarkPerUnit(resource, entity);

            int count = benchmarkPerUnitMapper.updateBenchmarkPerUnit(entity, userId);
            if (count == 0) {
                throw new NotFoundException("Record not updated: " + count);
            }

            BigDecimal[] margins = { entity.getYearMinus1Margin(), entity.getYearMinus2Margin(),
                    entity.getYearMinus3Margin(), entity.getYearMinus4Margin(), entity.getYearMinus5Margin(),
                    entity.getYearMinus6Margin() };
            BigDecimal[] expenses = { entity.getYearMinus1Expense(), entity.getYearMinus2Expense(),
                    entity.getYearMinus3Expense(), entity.getYearMinus4Expense(), entity.getYearMinus5Expense(),
                    entity.getYearMinus6Expense() };
            for (int i = 0; i < margins.length; i++) {
                Integer benchmarkYear = entity.getProgramYear() - (i + 1);
                BigDecimal averageMargin = margins[i] != null ? margins[i] : BigDecimal.ZERO;
                BigDecimal averageExpense = expenses[i];
                count = benchmarkPerUnitMapper.updateBenchmarkYear(benchmarkPerUnitId, benchmarkYear, averageMargin,
                        averageExpense, userId);
                if (count == 0) {
                    throw new NotFoundException("Record not updated: " + count);
                }
            }

            entity = benchmarkPerUnitMapper.fetch(benchmarkPerUnitId);
            result = benchmarkPerUnitResourceAssembler.getBenchmarkPerUnit(entity);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public void deleteBenchmarkPerUnit(Long benchmarkPerUnitId) throws ServiceException, NotFoundException {

        try {
            int count = benchmarkPerUnitMapper.deleteBenchmarkYear(benchmarkPerUnitId);
            if (count == 0) {
                throw new NotFoundException("Record not deleted: " + count);
            }

            count = benchmarkPerUnitMapper.deleteBenchmarkPerUnit(benchmarkPerUnitId);
            if (count == 0) {
                throw new NotFoundException("Record not deleted: " + count);
            }
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }
    }
}
