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
import ca.bc.gov.farms.model.v1.ExpectedProduction;
import ca.bc.gov.farms.model.v1.ExpectedProductionList;
import ca.bc.gov.farms.persistence.v1.dao.ExpectedProductionDao;
import ca.bc.gov.farms.persistence.v1.dto.ExpectedProductionDto;
import ca.bc.gov.farms.service.api.v1.ExpectedProductionService;
import ca.bc.gov.farms.service.api.v1.model.factory.ExpectedProductionFactory;
import ca.bc.gov.farms.service.api.v1.validation.ModelValidator;

public class ExpectedProductionServiceImpl implements ExpectedProductionService {

    private static final Logger logger = LoggerFactory.getLogger(ExpectedProductionServiceImpl.class);

    private Properties applicationProperties;

    private ModelValidator modelValidator;

    // factories
    private ExpectedProductionFactory expectedProductionFactory;

    // daos
    private ExpectedProductionDao expectedProductionDao;

    public void setApplicationProperties(Properties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public void setModelValidator(ModelValidator modelValidator) {
        this.modelValidator = modelValidator;
    }

    public void setExpectedProductionFactory(ExpectedProductionFactory expectedProductionFactory) {
        this.expectedProductionFactory = expectedProductionFactory;
    }

    public void setExpectedProductionDao(ExpectedProductionDao expectedProductionDao) {
        this.expectedProductionDao = expectedProductionDao;
    }

    @Override
    public ExpectedProductionList<? extends ExpectedProduction> getAllExpectedProductions(FactoryContext factoryContext)
            throws ServiceException {
        logger.debug("<getAllExpectedProductions");

        ExpectedProductionList<? extends ExpectedProduction> result = null;

        try {
            List<ExpectedProductionDto> dtos = expectedProductionDao.fetchAll();

            result = expectedProductionFactory.getExpectedProductionList(dtos, factoryContext);
        } catch (Exception e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">getAllExpectedProductions");
        return result;
    }

    @Override
    public ExpectedProductionList<? extends ExpectedProduction> getExpectedProductionByInventoryItemCode(
            String inventoryItemCode, FactoryContext factoryContext) throws ServiceException {
        logger.debug("<getExpectedProductionByInventoryItemCode");

        ExpectedProductionList<? extends ExpectedProduction> result = null;

        try {
            List<ExpectedProductionDto> dtos = expectedProductionDao.fetchByInventoryItemCode(inventoryItemCode);

            result = expectedProductionFactory.getExpectedProductionList(dtos, factoryContext);
        } catch (Exception e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">getExpectedProductionByInventoryItemCode");
        return result;
    }

    @Override
    public ExpectedProduction getExpectedProduction(Long expectedProductionId, FactoryContext factoryContext)
            throws ServiceException, NotFoundException {
        logger.debug("<getExpectedProduction");

        ExpectedProduction result = null;

        try {
            ExpectedProductionDto dto = expectedProductionDao.fetch(expectedProductionId);

            if (dto == null) {
                throw new NotFoundException("Did not find the expected production: " + expectedProductionId);
            }

            result = expectedProductionFactory.getExpectedProduction(dto, factoryContext);

        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">getExpectedProduction");
        return result;
    }

    @Override
    public ExpectedProduction createExpectedProduction(ExpectedProduction resource, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException {
        logger.debug("<createExpectedProduction");

        ExpectedProduction result = null;
        String userId = UserUtil.toUserId(resource.getUserEmail());

        try {

            List<Message> errors = modelValidator.validateExpectedProduction(resource);
            if (!errors.isEmpty()) {
                throw new ValidationFailureException(errors);
            }

            ExpectedProductionDto dto = new ExpectedProductionDto();

            expectedProductionFactory.updateExpectedProduction(dto, resource);
            expectedProductionDao.insert(dto, userId);

            dto = expectedProductionDao.fetch(dto.getExpectedProductionId());
            result = expectedProductionFactory.getExpectedProduction(dto, factoryContext);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">createExpectedProduction");
        return result;
    }

    @Override
    public ExpectedProduction updateExpectedProduction(Long expectedProductionId,
            ExpectedProduction expectedProduction, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException, NotFoundException {
        logger.debug("<updateExpectedProduction");

        ExpectedProduction result = null;
        String userId = UserUtil.toUserId(expectedProduction.getUserEmail());

        try {

            List<Message> errors = modelValidator.validateExpectedProduction(expectedProduction);
            if (!errors.isEmpty()) {
                throw new ValidationFailureException(errors);
            }

            ExpectedProductionDto dto = expectedProductionDao.fetch(expectedProductionId);

            if (dto == null) {
                throw new NotFoundException("Did not find the expected production: " + expectedProductionId);
            }

            expectedProductionFactory.updateExpectedProduction(dto, expectedProduction);
            expectedProductionDao.update(dto, userId);

            dto = expectedProductionDao.fetch(dto.getExpectedProductionId());
            result = expectedProductionFactory.getExpectedProduction(dto, factoryContext);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">updateExpectedProduction");
        return result;
    }

    @Override
    public void deleteExpectedProduction(Long expectedProductionId) throws ServiceException, NotFoundException {
        logger.debug("<deleteExpectedProduction");

        try {
            ExpectedProductionDto dto = expectedProductionDao.fetch(expectedProductionId);

            if (dto == null) {
                throw new NotFoundException("Did not find the expected production: " + expectedProductionId);
            }

            expectedProductionDao.delete(expectedProductionId);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">deleteExpectedProduction");
    }

}
