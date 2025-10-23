package ca.bc.gov.farms.service.api.v1.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.brmb.common.model.Message;
import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.brmb.common.service.api.ValidationFailureException;
import ca.bc.gov.brmb.common.service.api.code.UserUtil;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.ConfigurationParameter;
import ca.bc.gov.farms.model.v1.ConfigurationParameterList;
import ca.bc.gov.farms.persistence.v1.dao.ConfigurationParameterDao;
import ca.bc.gov.farms.persistence.v1.dto.ConfigurationParameterDto;
import ca.bc.gov.farms.service.api.v1.ConfigurationParameterService;
import ca.bc.gov.farms.service.api.v1.model.factory.ConfigurationParameterFactory;
import ca.bc.gov.farms.service.api.v1.validation.ModelValidator;

public class ConfigurationParameterServiceImpl implements ConfigurationParameterService {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationParameterServiceImpl.class);

    private ModelValidator modelValidator;

    // factories
    private ConfigurationParameterFactory configurationParameterFactory;

    // daos
    private ConfigurationParameterDao configurationParameterDao;

    public void setModelValidator(ModelValidator modelValidator) {
        this.modelValidator = modelValidator;
    }

    public void setConfigurationParameterFactory(ConfigurationParameterFactory configurationParameterFactory) {
        this.configurationParameterFactory = configurationParameterFactory;
    }

    public void setConfigurationParameterDao(ConfigurationParameterDao configurationParameterDao) {
        this.configurationParameterDao = configurationParameterDao;
    }

    @Override
    public ConfigurationParameterList<? extends ConfigurationParameter> getAllConfigurationParameters(
            FactoryContext factoryContext) throws ServiceException {
        logger.debug("<getAllConfigurationParameters");

        ConfigurationParameterList<? extends ConfigurationParameter> result = null;

        try {
            List<ConfigurationParameterDto> dtos = configurationParameterDao.fetchAll();

            result = configurationParameterFactory.getConfigurationParameterList(dtos, factoryContext);
        } catch (Exception e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">getAllConfigurationParameters");
        return result;
    }

    @Override
    public ConfigurationParameterList<? extends ConfigurationParameter> getConfigurationParametersByParameterNamePrefix(
            String parameterNamePrefix, FactoryContext factoryContext) throws ServiceException {
        logger.debug("<getConfigurationParametersByParameterNamePrefix");

        ConfigurationParameterList<? extends ConfigurationParameter> result = null;

        try {
            List<ConfigurationParameterDto> dtos = configurationParameterDao
                    .fetchByParameterNamePrefix(parameterNamePrefix);

            result = configurationParameterFactory.getConfigurationParameterList(dtos, factoryContext);
        } catch (Exception e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">getConfigurationParametersByParameterNamePrefix");
        return result;
    }

    @Override
    public ConfigurationParameter getConfigurationParameter(Long configurationParameterId,
            FactoryContext factoryContext) throws ServiceException, NotFoundException {
        logger.debug("<getConfigurationParameter");

        ConfigurationParameter result = null;

        try {
            ConfigurationParameterDto dto = configurationParameterDao.fetch(configurationParameterId);

            if (dto == null) {
                throw new NotFoundException("Did not find the configuration parameter: " + configurationParameterId);
            }

            result = configurationParameterFactory.getConfigurationParameter(dto, factoryContext);

        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">getConfigurationParameter");
        return result;
    }

    @Override
    public ConfigurationParameter createConfigurationParameter(ConfigurationParameter resource,
            FactoryContext factoryContext) throws ServiceException, ValidationFailureException {
        logger.debug("<createConfigurationParameter");

        ConfigurationParameter result = null;
        String userId = UserUtil.toUserId(resource.getUserEmail());

        try {

            List<Message> errors = modelValidator.validateConfigurationParameter(resource);
            if (!errors.isEmpty()) {
                throw new ValidationFailureException(errors);
            }

            ConfigurationParameterDto dto = new ConfigurationParameterDto();

            configurationParameterFactory.updateConfigurationParameter(dto, resource);
            configurationParameterDao.insert(dto, userId);

            dto = configurationParameterDao.fetch(dto.getConfigurationParameterId());
            result = configurationParameterFactory.getConfigurationParameter(dto, factoryContext);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">createConfigurationParameter");
        return result;
    }

    @Override
    public ConfigurationParameter updateConfigurationParameter(Long configurationParameterId,
            ConfigurationParameter configurationParameter, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException, NotFoundException {
        logger.debug("<updateConfigurationParameter");

        ConfigurationParameter result = null;
        String userId = UserUtil.toUserId(configurationParameter.getUserEmail());

        try {

            List<Message> errors = modelValidator.validateConfigurationParameter(configurationParameter);
            if (!errors.isEmpty()) {
                throw new ValidationFailureException(errors);
            }

            ConfigurationParameterDto dto = configurationParameterDao.fetch(configurationParameterId);

            if (dto == null) {
                throw new NotFoundException("Did not find the configuration parameter: " + configurationParameterId);
            }

            configurationParameterFactory.updateConfigurationParameter(dto, configurationParameter);
            configurationParameterDao.update(dto, userId);

            dto = configurationParameterDao.fetch(dto.getConfigurationParameterId());
            result = configurationParameterFactory.getConfigurationParameter(dto, factoryContext);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">updateConfigurationParameter");
        return result;
    }

    @Override
    public void deleteConfigurationParameter(Long configurationParameterId)
            throws ServiceException, NotFoundException {
        logger.debug("<deleteConfigurationParameter");

        try {
            ConfigurationParameterDto dto = configurationParameterDao.fetch(configurationParameterId);

            if (dto == null) {
                throw new NotFoundException("Did not find the configuration parameter: " + configurationParameterId);
            }

            configurationParameterDao.delete(configurationParameterId);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">deleteConfigurationParameter");
    }

}
