package ca.bc.gov.farms.service.api.v1.impl;

import java.util.List;
import java.util.regex.PatternSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.brmb.common.model.Message;
import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.service.api.ConflictException;
import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.brmb.common.service.api.ValidationFailureException;
import ca.bc.gov.brmb.common.service.api.code.UserUtil;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.FairMarketValue;
import ca.bc.gov.farms.model.v1.FairMarketValueList;
import ca.bc.gov.farms.persistence.v1.dao.FairMarketValueDao;
import ca.bc.gov.farms.persistence.v1.dto.FairMarketValueDto;
import ca.bc.gov.farms.service.api.v1.FairMarketValueService;
import ca.bc.gov.farms.service.api.v1.model.factory.FairMarketValueFactory;
import ca.bc.gov.farms.service.api.v1.validation.ModelValidator;

public class FairMarketValueServiceImpl implements FairMarketValueService {

    private static final Logger logger = LoggerFactory.getLogger(FairMarketValueServiceImpl.class);

    private ModelValidator modelValidator;

    // factories
    private FairMarketValueFactory fairMarketValueFactory;

    // daos
    private FairMarketValueDao fairMarketValueDao;

    public void setModelValidator(ModelValidator modelValidator) {
        this.modelValidator = modelValidator;
    }

    public void setFairMarketValueFactory(FairMarketValueFactory fairMarketValueFactory) {
        this.fairMarketValueFactory = fairMarketValueFactory;
    }

    public void setFairMarketValueDao(FairMarketValueDao fairMarketValueDao) {
        this.fairMarketValueDao = fairMarketValueDao;
    }

    @Override
    public FairMarketValueList<? extends FairMarketValue> getFairMarketValuesByProgramYear(Integer programYear,
            FactoryContext factoryContext) throws ServiceException {
        logger.debug("<getFairMarketValuesByProgramYear");

        FairMarketValueList<? extends FairMarketValue> result = null;

        try {
            List<FairMarketValueDto> dtos = fairMarketValueDao.fetchByProgramYear(programYear);

            result = fairMarketValueFactory.getFairMarketValueList(dtos, factoryContext);
        } catch (Exception e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">getFairMarketValuesByProgramYear");
        return result;
    }

    @Override
    public FairMarketValue getFairMarketValue(String fairMarketValueId, FactoryContext factoryContext)
            throws ServiceException, NotFoundException {
        logger.debug("<getFairMarketValue");

        FairMarketValue result = null;

        try {
            String[] parts = fairMarketValueId.split("_");
            Integer programYear = Integer.valueOf(parts[0]);
            FairMarketValueDto dto = fairMarketValueDao.fetch(programYear, fairMarketValueId);

            if (dto == null) {
                throw new NotFoundException("Did not find the fair market value: " + fairMarketValueId);
            }

            result = fairMarketValueFactory.getFairMarketValue(dto, factoryContext);

        } catch (PatternSyntaxException | NumberFormatException e) {
            throw new NotFoundException("Did not find the fair market value: " + fairMarketValueId);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">getFairMarketValue");
        return result;
    }

    @Override
    public FairMarketValue createFairMarketValue(FairMarketValue resource, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException, ConflictException {
        logger.debug("<createFairMarketValue");

        FairMarketValue result = null;
        String userId = UserUtil.toUserId(resource.getUserEmail());

        try {

            List<Message> errors = modelValidator.validateFairMarketValue(resource);
            if (!errors.isEmpty()) {
                throw new ValidationFailureException(errors);
            }

            List<FairMarketValueDto> dtos = fairMarketValueDao.fetchBy(resource.getProgramYear(),
                    resource.getInventoryItemCode(), resource.getMunicipalityCode(),
                    resource.getCropUnitCode());
            if (!dtos.isEmpty()) {
                throw new ConflictException("Fair market value already exists for the given parameters.");
            }

            FairMarketValueDto dto = new FairMarketValueDto();

            fairMarketValueFactory.updateFairMarketValue(dto, resource);
            fairMarketValueDao.insert(dto, userId);

            dto = fairMarketValueDao.fetch(dto.getProgramYear(), dto.getFairMarketValueId());
            result = fairMarketValueFactory.getFairMarketValue(dto, factoryContext);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">createFairMarketValue");
        return result;
    }

    @Override
    public FairMarketValue updateFairMarketValue(String fairMarketValueId, FairMarketValue fairMarketValue,
            FactoryContext factoryContext) throws ServiceException, ValidationFailureException, NotFoundException {
        logger.debug("<updateFairMarketValue");

        FairMarketValue result = null;
        String userId = UserUtil.toUserId(fairMarketValue.getUserEmail());

        try {

            List<Message> errors = modelValidator.validateFairMarketValue(fairMarketValue);
            if (!errors.isEmpty()) {
                throw new ValidationFailureException(errors);
            }

            String[] parts = fairMarketValueId.split("_");
            Integer programYear = Integer.valueOf(parts[0]);
            FairMarketValueDto dto = fairMarketValueDao.fetch(programYear, fairMarketValueId);

            if (dto == null) {
                throw new NotFoundException("Did not find the fair market value: " + fairMarketValueId);
            }

            fairMarketValueFactory.updateFairMarketValue(dto, fairMarketValue);
            fairMarketValueDao.update(dto, userId);

            dto = fairMarketValueDao.fetch(dto.getProgramYear(), dto.getFairMarketValueId());
            result = fairMarketValueFactory.getFairMarketValue(dto, factoryContext);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">updateFairMarketValue");
        return result;
    }

    @Override
    public void deleteFairMarketValue(String fairMarketValueId) throws ServiceException, NotFoundException {
        logger.debug("<deleteFairMarketValue");

        try {
            String[] parts = fairMarketValueId.split("_");
            Integer programYear = Integer.valueOf(parts[0]);
            FairMarketValueDto dto = fairMarketValueDao.fetch(programYear, fairMarketValueId);

            if (dto == null) {
                throw new NotFoundException("Did not find the fair market value: " + fairMarketValueId);
            }

            fairMarketValueDao.delete(programYear, fairMarketValueId);
        } catch (PatternSyntaxException | NumberFormatException e) {
            throw new NotFoundException("Did not find the fair market value: " + fairMarketValueId);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">deleteFairMarketValue");
    }
}
