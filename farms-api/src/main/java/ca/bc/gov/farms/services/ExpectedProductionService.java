package ca.bc.gov.farms.services;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.farms.data.assemblers.ExpectedProductionResourceAssembler;
import ca.bc.gov.farms.data.entities.ExpectedProductionEntity;
import ca.bc.gov.farms.data.mappers.ExpectedProductionMapper;
import ca.bc.gov.farms.data.models.ExpectedProductionListRsrc;
import ca.bc.gov.farms.data.models.ExpectedProductionRsrc;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ExpectedProductionService {

    @Autowired
    private ExpectedProductionMapper expectedProductionMapper;

    @Autowired
    private ExpectedProductionResourceAssembler expectedProductionResourceAssembler;

    @Autowired
    private Validator validator;

    public ExpectedProductionListRsrc getAllExpectedProductions() throws ServiceException {

        ExpectedProductionListRsrc result = null;

        try {
            List<ExpectedProductionEntity> entities = expectedProductionMapper.fetchAll();

            result = expectedProductionResourceAssembler.getExpectedProductionList(entities);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    public ExpectedProductionListRsrc getExpectedProductionByInventoryItemCode(String inventoryItemCode)
            throws ServiceException {

        ExpectedProductionListRsrc result = null;

        try {
            List<ExpectedProductionEntity> entities = expectedProductionMapper
                    .fetchByInventoryItemCode(inventoryItemCode);

            result = expectedProductionResourceAssembler.getExpectedProductionList(entities);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    public ExpectedProductionRsrc getExpectedProduction(Long expectedProductionId)
            throws ServiceException, NotFoundException {

        ExpectedProductionRsrc result = null;

        try {
            ExpectedProductionEntity entity = expectedProductionMapper.fetch(expectedProductionId);

            if (entity == null) {
                throw new NotFoundException("Did not find the expected production: " + expectedProductionId);
            }

            result = expectedProductionResourceAssembler.getExpectedProduction(entity);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public ExpectedProductionRsrc createExpectedProduction(ExpectedProductionRsrc resource)
            throws ServiceException, ConstraintViolationException {

        Set<ConstraintViolation<ExpectedProductionRsrc>> violations = validator.validate(resource);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        ExpectedProductionRsrc result = null;
        String userId = resource.getUserEmail();

        try {

            ExpectedProductionEntity dto = new ExpectedProductionEntity();

            expectedProductionResourceAssembler.updateExpectedProduction(resource, dto);
            int count = expectedProductionMapper.insertExpectedProduction(dto, userId);
            if (count == 0) {
                throw new ServiceException("Record not inserted: " + count);
            }

            dto = expectedProductionMapper.fetch(dto.getExpectedProductionId());
            result = expectedProductionResourceAssembler.getExpectedProduction(dto);
        } catch (ServiceException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public ExpectedProductionRsrc updateExpectedProduction(Long expectedProductionId, ExpectedProductionRsrc resource)
            throws ServiceException, ConstraintViolationException, NotFoundException {

        Set<ConstraintViolation<ExpectedProductionRsrc>> violations = validator.validate(resource);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        ExpectedProductionRsrc result = null;
        String userId = resource.getUserEmail();

        try {

            ExpectedProductionEntity dto = expectedProductionMapper.fetch(expectedProductionId);

            if (dto == null) {
                throw new NotFoundException("Did not find the expected production: " + expectedProductionId);
            }

            expectedProductionResourceAssembler.updateExpectedProduction(resource, dto);
            int count = expectedProductionMapper.updateExpectedProduction(dto, userId);
            if (count == 0) {
                throw new ServiceException("Record not updated: " + count);
            }

            dto = expectedProductionMapper.fetch(dto.getExpectedProductionId());
            result = expectedProductionResourceAssembler.getExpectedProduction(dto);
        } catch (ServiceException | NotFoundException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public void deleteExpectedProduction(Long expectedProductionId) throws ServiceException, NotFoundException {

        try {
            ExpectedProductionEntity entity = expectedProductionMapper.fetch(expectedProductionId);

            if (entity == null) {
                throw new NotFoundException("Did not find the expected production: " + expectedProductionId);
            }

            int count = expectedProductionMapper.deleteExpectedProduction(expectedProductionId);
            if (count == 0) {
                throw new ServiceException("Record not deleted: " + count);
            }
        } catch (ServiceException | NotFoundException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }
    }
}
