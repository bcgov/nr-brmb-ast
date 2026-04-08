package ca.bc.gov.farms.services;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.farms.data.assemblers.FruitVegTypeDetailResourceAssembler;
import ca.bc.gov.farms.data.entities.FruitVegTypeDetailEntity;
import ca.bc.gov.farms.data.mappers.FruitVegTypeDetailMapper;
import ca.bc.gov.farms.data.models.FruitVegTypeDetailListModel;
import ca.bc.gov.farms.data.models.FruitVegTypeDetailModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FruitVegTypeDetailService {

    @Autowired
    private FruitVegTypeDetailMapper fruitVegTypeDetailMapper;

    @Autowired
    private FruitVegTypeDetailResourceAssembler fruitVegTypeDetailResourceAssembler;

    @Autowired
    private Validator validator;

    public FruitVegTypeDetailListModel getAllFruitVegTypeDetails() throws ServiceException {

        FruitVegTypeDetailListModel result = null;

        try {
            List<FruitVegTypeDetailEntity> entities = fruitVegTypeDetailMapper.fetchAll();

            result = fruitVegTypeDetailResourceAssembler.getFruitVegTypeDetailList(entities);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    public FruitVegTypeDetailModel getFruitVegTypeDetail(String fruitVegTypeCode)
            throws ServiceException, NotFoundException {

        FruitVegTypeDetailModel result = null;

        try {
            FruitVegTypeDetailEntity entity = fruitVegTypeDetailMapper.fetch(fruitVegTypeCode);

            if (entity == null) {
                throw new NotFoundException("Did not find the fruit and veg type detail: " + fruitVegTypeCode);
            }

            result = fruitVegTypeDetailResourceAssembler.getFruitVegTypeDetail(entity);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public FruitVegTypeDetailModel createFruitVegTypeDetail(FruitVegTypeDetailModel resource)
            throws ServiceException, ConstraintViolationException {

        Set<ConstraintViolation<FruitVegTypeDetailModel>> violations = validator.validate(resource);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        FruitVegTypeDetailModel result = null;
        String userId = resource.getUserEmail();

        try {

            FruitVegTypeDetailEntity dto = new FruitVegTypeDetailEntity();

            fruitVegTypeDetailResourceAssembler.updateFruitVegTypeDetail(resource, dto);
            fruitVegTypeDetailMapper.insertFruitVegTypeCode(dto, userId);
            int count = fruitVegTypeDetailMapper.insertFruitVegTypeDetail(dto, userId);
            if (count == 0) {
                throw new ServiceException("Record not inserted: " + count);
            }

            dto = fruitVegTypeDetailMapper.fetch(dto.getFruitVegTypeCode());
            result = fruitVegTypeDetailResourceAssembler.getFruitVegTypeDetail(dto);
        } catch (ServiceException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public FruitVegTypeDetailModel updateFruitVegTypeDetail(String fruitVegTypeCode, FruitVegTypeDetailModel resource)
            throws ServiceException, ConstraintViolationException, NotFoundException {

        Set<ConstraintViolation<FruitVegTypeDetailModel>> violations = validator.validate(resource);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        FruitVegTypeDetailModel result = null;
        String userId = resource.getUserEmail();

        try {

            FruitVegTypeDetailEntity dto = fruitVegTypeDetailMapper.fetch(fruitVegTypeCode);

            if (dto == null) {
                throw new NotFoundException("Did not find the fruit and veg type detail: " + fruitVegTypeCode);
            }

            fruitVegTypeDetailResourceAssembler.updateFruitVegTypeDetail(resource, dto);
            fruitVegTypeDetailMapper.updateFruitVegTypeCode(dto, userId);
            int count = fruitVegTypeDetailMapper.updateFruitVegTypeDetail(dto, userId);
            if (count == 0) {
                throw new ServiceException("Record not updated: " + count);
            }

            dto = fruitVegTypeDetailMapper.fetch(dto.getFruitVegTypeCode());
            result = fruitVegTypeDetailResourceAssembler.getFruitVegTypeDetail(dto);
        } catch (ServiceException | NotFoundException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public void deleteFruitVegTypeDetail(String fruitVegTypeCode) throws ServiceException, NotFoundException {

        try {
            FruitVegTypeDetailEntity entity = fruitVegTypeDetailMapper.fetch(fruitVegTypeCode);

            if (entity == null) {
                throw new NotFoundException("Did not find the fruit and veg type detail: " + fruitVegTypeCode);
            }

            int count = fruitVegTypeDetailMapper.deleteFruitVegTypeDetail(fruitVegTypeCode);
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
