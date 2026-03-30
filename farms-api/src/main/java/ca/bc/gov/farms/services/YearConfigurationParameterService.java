package ca.bc.gov.farms.services;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.farms.data.assemblers.YearConfigurationParameterResourceAssembler;
import ca.bc.gov.farms.data.entities.YearConfigurationParameterEntity;
import ca.bc.gov.farms.data.mappers.YearConfigurationParameterMapper;
import ca.bc.gov.farms.data.models.YearConfigurationParameterListModel;
import ca.bc.gov.farms.data.models.YearConfigurationParameterModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class YearConfigurationParameterService {

    @Autowired
    private YearConfigurationParameterMapper yearConfigurationParameterMapper;

    @Autowired
    private YearConfigurationParameterResourceAssembler yearConfigurationParameterResourceAssembler;

    @Autowired
    private Validator validator;

    public YearConfigurationParameterListModel getAllYearConfigurationParameters() throws ServiceException {

        YearConfigurationParameterListModel result = null;

        try {
            List<YearConfigurationParameterEntity> entities = yearConfigurationParameterMapper.fetchAll();

            result = yearConfigurationParameterResourceAssembler.getYearConfigurationParameterList(entities);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    public YearConfigurationParameterModel getYearConfigurationParameter(Long yearConfigurationParameterId)
            throws ServiceException, NotFoundException {

        YearConfigurationParameterModel result = null;

        try {
            YearConfigurationParameterEntity entity = yearConfigurationParameterMapper
                    .fetch(yearConfigurationParameterId);

            if (entity == null) {
                throw new NotFoundException(
                        "Did not find the year configuration parameter: " + yearConfigurationParameterId);
            }

            result = yearConfigurationParameterResourceAssembler.getYearConfigurationParameter(entity);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public YearConfigurationParameterModel createYearConfigurationParameter(YearConfigurationParameterModel resource)
            throws ServiceException, ConstraintViolationException {

        Set<ConstraintViolation<YearConfigurationParameterModel>> violations = validator.validate(resource);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        YearConfigurationParameterModel result = null;
        String userId = resource.getUserEmail();

        try {

            YearConfigurationParameterEntity dto = new YearConfigurationParameterEntity();

            yearConfigurationParameterResourceAssembler.updateYearConfigurationParameter(resource, dto);
            int count = yearConfigurationParameterMapper.insertYearConfigurationParameter(dto, userId);
            if (count == 0) {
                throw new ServiceException("Record not inserted: " + count);
            }

            dto = yearConfigurationParameterMapper.fetch(dto.getYearConfigurationParameterId());
            result = yearConfigurationParameterResourceAssembler.getYearConfigurationParameter(dto);
        } catch (ServiceException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public YearConfigurationParameterModel updateYearConfigurationParameter(Long yearConfigurationParameterId,
            YearConfigurationParameterModel resource)
            throws ServiceException, ConstraintViolationException, NotFoundException {

        Set<ConstraintViolation<YearConfigurationParameterModel>> violations = validator.validate(resource);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        YearConfigurationParameterModel result = null;
        String userId = resource.getUserEmail();

        try {

            YearConfigurationParameterEntity dto = yearConfigurationParameterMapper.fetch(yearConfigurationParameterId);

            if (dto == null) {
                throw new NotFoundException(
                        "Did not find the year configuration parameter: " + yearConfigurationParameterId);
            }

            yearConfigurationParameterResourceAssembler.updateYearConfigurationParameter(resource, dto);
            int count = yearConfigurationParameterMapper.updateYearConfigurationParameter(dto, userId);
            if (count == 0) {
                throw new ServiceException("Record not updated: " + count);
            }

            dto = yearConfigurationParameterMapper.fetch(dto.getYearConfigurationParameterId());
            result = yearConfigurationParameterResourceAssembler.getYearConfigurationParameter(dto);
        } catch (ServiceException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public void deleteYearConfigurationParameter(Long yearConfigurationParameterId)
            throws ServiceException, NotFoundException {

        try {
            YearConfigurationParameterEntity entity = yearConfigurationParameterMapper
                    .fetch(yearConfigurationParameterId);

            if (entity == null) {
                throw new NotFoundException(
                        "Did not find the year configuration parameter: " + yearConfigurationParameterId);
            }

            int count = yearConfigurationParameterMapper.deleteYearConfigurationParameter(yearConfigurationParameterId);
            if (count == 0) {
                throw new ServiceException("Record not deleted: " + count);
            }
        } catch (ServiceException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }
    }
}
