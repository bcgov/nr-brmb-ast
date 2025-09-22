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
import ca.bc.gov.brmb.common.service.api.code.UserUtil;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.YearConfigurationParameter;
import ca.bc.gov.farms.model.v1.YearConfigurationParameterList;
import ca.bc.gov.farms.persistence.v1.dao.YearConfigurationParameterDao;
import ca.bc.gov.farms.persistence.v1.dto.YearConfigurationParameterDto;
import ca.bc.gov.farms.service.api.v1.YearConfigurationParameterService;
import ca.bc.gov.farms.service.api.v1.model.factory.YearConfigurationParameterFactory;
import ca.bc.gov.farms.service.api.v1.validation.ModelValidator;

public class YearConfigurationParameterServiceImpl implements YearConfigurationParameterService {

    private static final Logger logger = LoggerFactory.getLogger(YearConfigurationParameterServiceImpl.class);

    private Properties applicationProperties;

    private ModelValidator modelValidator;

    // factories
    private YearConfigurationParameterFactory yearConfigurationParameterFactory;

    // daos
    private YearConfigurationParameterDao yearConfigurationParameterDao;

    public void setApplicationProperties(Properties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public void setModelValidator(ModelValidator modelValidator) {
        this.modelValidator = modelValidator;
    }

    public void setYearConfigurationParameterFactory(
            YearConfigurationParameterFactory yearConfigurationParameterFactory) {
        this.yearConfigurationParameterFactory = yearConfigurationParameterFactory;
    }

    public void setYearConfigurationParameterDao(YearConfigurationParameterDao yearConfigurationParameterDao) {
        this.yearConfigurationParameterDao = yearConfigurationParameterDao;
    }

    @Override
    public YearConfigurationParameterList<? extends YearConfigurationParameter> getAllYearConfigurationParameters(
            FactoryContext factoryContext) throws ServiceException {
        logger.debug("<getAllYearConfigurationParameters");

        YearConfigurationParameterList<? extends YearConfigurationParameter> result = null;

        try {
            List<YearConfigurationParameterDto> dtos = yearConfigurationParameterDao.fetchAll();

            result = yearConfigurationParameterFactory.getYearConfigurationParameterList(dtos, factoryContext);
        } catch (Exception e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">getAllYearConfigurationParameters");
        return result;
    }

    @Override
    public YearConfigurationParameter getYearConfigurationParameter(Long yearConfigurationParameterId,
            FactoryContext factoryContext) throws ServiceException, NotFoundException {
        logger.debug("<getYearConfigurationParameter");

        YearConfigurationParameter result = null;

        try {
            YearConfigurationParameterDto dto = yearConfigurationParameterDao.fetch(yearConfigurationParameterId);

            if (dto == null) {
                throw new NotFoundException(
                        "Did not find the year configuration parameter: " + yearConfigurationParameterId);
            }

            result = yearConfigurationParameterFactory.getYearConfigurationParameter(dto, factoryContext);

        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">getYearConfigurationParameter");
        return result;
    }

    @Override
    public YearConfigurationParameter createYearConfigurationParameter(YearConfigurationParameter resource,
            FactoryContext factoryContext) throws ServiceException, ValidationFailureException {
        logger.debug("<createYearConfigurationParameter");

        YearConfigurationParameter result = null;
        String userId = UserUtil.toUserId(resource.getUserEmail());

        try {

            List<Message> errors = modelValidator.validateYearConfigurationParameter(resource);
            if (!errors.isEmpty()) {
                throw new ValidationFailureException(errors);
            }

            YearConfigurationParameterDto dto = new YearConfigurationParameterDto();

            yearConfigurationParameterFactory.updateYearConfigurationParameter(dto, resource);
            yearConfigurationParameterDao.insert(dto, userId);

            dto = yearConfigurationParameterDao.fetch(dto.getYearConfigurationParameterId());
            result = yearConfigurationParameterFactory.getYearConfigurationParameter(dto, factoryContext);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">createYearConfigurationParameter");
        return result;
    }

    @Override
    public YearConfigurationParameter updateYearConfigurationParameter(Long yearConfigurationParameterId,
            YearConfigurationParameter yearConfigurationParameter, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException, NotFoundException {
        logger.debug("<updateYearConfigurationParameter");

        YearConfigurationParameter result = null;
        String userId = UserUtil.toUserId(yearConfigurationParameter.getUserEmail());

        try {

            List<Message> errors = modelValidator.validateYearConfigurationParameter(yearConfigurationParameter);
            if (!errors.isEmpty()) {
                throw new ValidationFailureException(errors);
            }

            YearConfigurationParameterDto dto = yearConfigurationParameterDao.fetch(yearConfigurationParameterId);

            if (dto == null) {
                throw new NotFoundException(
                        "Did not find the year configuration parameter: " + yearConfigurationParameterId);
            }

            yearConfigurationParameterFactory.updateYearConfigurationParameter(dto, yearConfigurationParameter);
            yearConfigurationParameterDao.update(dto, userId);

            dto = yearConfigurationParameterDao.fetch(dto.getYearConfigurationParameterId());
            result = yearConfigurationParameterFactory.getYearConfigurationParameter(dto, factoryContext);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">updateYearConfigurationParameter");
        return result;
    }

    @Override
    public void deleteYearConfigurationParameter(Long yearConfigurationParameterId)
            throws ServiceException, NotFoundException {
        logger.debug("<deleteYearConfigurationParameter");

        try {
            YearConfigurationParameterDto dto = yearConfigurationParameterDao.fetch(yearConfigurationParameterId);

            if (dto == null) {
                throw new NotFoundException(
                        "Did not find the year configuration parameter: " + yearConfigurationParameterId);
            }

            yearConfigurationParameterDao.delete(yearConfigurationParameterId);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">deleteYearConfigurationParameter");
    }

}
