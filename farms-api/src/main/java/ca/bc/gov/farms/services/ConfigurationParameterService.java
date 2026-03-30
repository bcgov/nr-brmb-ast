package ca.bc.gov.farms.services;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.farms.data.assemblers.ConfigurationParameterResourceAssembler;
import ca.bc.gov.farms.data.entities.ConfigurationParameterEntity;
import ca.bc.gov.farms.data.mappers.ConfigurationParameterMapper;
import ca.bc.gov.farms.data.models.ConfigurationParameterListModel;
import ca.bc.gov.farms.data.models.ConfigurationParameterModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ConfigurationParameterService {

    @Autowired
    private ConfigurationParameterMapper configurationParameterMapper;

    @Autowired
    private ConfigurationParameterResourceAssembler configurationParameterResourceAssembler;

    @Autowired
    private Validator validator;

    public ConfigurationParameterListModel getAllConfigurationParameters() throws ServiceException {

        ConfigurationParameterListModel result = null;

        try {
            List<ConfigurationParameterEntity> entities = configurationParameterMapper.fetchAll();

            result = configurationParameterResourceAssembler.getConfigurationParameterList(entities);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    public ConfigurationParameterListModel getConfigurationParametersByParameterNamePrefix(String parameterNamePrefix)
            throws ServiceException {

        ConfigurationParameterListModel result = null;

        try {
            List<ConfigurationParameterEntity> entities = configurationParameterMapper
                    .fetchByParameterNamePrefix(parameterNamePrefix);

            result = configurationParameterResourceAssembler.getConfigurationParameterList(entities);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    public ConfigurationParameterModel getConfigurationParameter(Long configurationParameterId)
            throws ServiceException, NotFoundException {

        ConfigurationParameterModel result = null;

        try {
            ConfigurationParameterEntity entity = configurationParameterMapper.fetch(configurationParameterId);

            if (entity == null) {
                throw new NotFoundException("Did not find the configuration parameter: " + configurationParameterId);
            }

            result = configurationParameterResourceAssembler.getConfigurationParameter(entity);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public ConfigurationParameterModel createConfigurationParameter(ConfigurationParameterModel resource)
            throws ServiceException, ConstraintViolationException {

        Set<ConstraintViolation<ConfigurationParameterModel>> violations = validator.validate(resource);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        ConfigurationParameterModel result = null;
        String userId = resource.getUserEmail();

        try {

            ConfigurationParameterEntity dto = new ConfigurationParameterEntity();

            configurationParameterResourceAssembler.updateConfigurationParameter(resource, dto);
            int count = configurationParameterMapper.insertConfigurationParameter(dto, userId);
            if (count == 0) {
                throw new ServiceException("Record not inserted: " + count);
            }

            dto = configurationParameterMapper.fetch(dto.getConfigurationParameterId());
            result = configurationParameterResourceAssembler.getConfigurationParameter(dto);
        } catch (ServiceException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public ConfigurationParameterModel updateConfigurationParameter(Long configurationParameterId,
            ConfigurationParameterModel resource)
            throws ServiceException, ConstraintViolationException, NotFoundException {

        Set<ConstraintViolation<ConfigurationParameterModel>> violations = validator.validate(resource);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        ConfigurationParameterModel result = null;
        String userId = resource.getUserEmail();

        try {

            ConfigurationParameterEntity dto = configurationParameterMapper.fetch(configurationParameterId);

            if (dto == null) {
                throw new NotFoundException("Did not find the configuration parameter: " + configurationParameterId);
            }

            configurationParameterResourceAssembler.updateConfigurationParameter(resource, dto);
            int count = configurationParameterMapper.updateConfigurationParameter(dto, userId);
            if (count == 0) {
                throw new ServiceException("Record not updated: " + count);
            }

            dto = configurationParameterMapper.fetch(dto.getConfigurationParameterId());
            result = configurationParameterResourceAssembler.getConfigurationParameter(dto);
        } catch (ServiceException | NotFoundException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public void deleteConfigurationParameter(Long configurationParameterId) throws ServiceException, NotFoundException {

        try {
            ConfigurationParameterEntity entity = configurationParameterMapper.fetch(configurationParameterId);

            if (entity == null) {
                throw new NotFoundException("Did not find the configuration parameter: " + configurationParameterId);
            }

            int count = configurationParameterMapper.deleteConfigurationParameter(configurationParameterId);
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
