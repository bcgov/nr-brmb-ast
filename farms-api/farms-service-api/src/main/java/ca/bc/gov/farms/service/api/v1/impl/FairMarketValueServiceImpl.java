package ca.bc.gov.farms.service.api.v1.impl;

import java.util.List;
import java.util.Properties;
import java.util.regex.PatternSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.brmb.common.model.Message;
import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.brmb.common.service.api.ValidationFailureException;
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

    private Properties applicationProperties;

    private ModelValidator modelValidator;

    // factories
    private FairMarketValueFactory fairMarketValueFactory;

    // daos
    private FairMarketValueDao fairMarketValueDao;

    public void setApplicationProperties(Properties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

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
            throws ServiceException, ValidationFailureException {
        logger.debug("<createFairMarketValue");

        FairMarketValue result = null;
        String userId = "UserId";

        try {

            List<Message> errors = modelValidator.validateFairMarketValue(resource);
            if (!errors.isEmpty()) {
                throw new ValidationFailureException(errors);
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
}
