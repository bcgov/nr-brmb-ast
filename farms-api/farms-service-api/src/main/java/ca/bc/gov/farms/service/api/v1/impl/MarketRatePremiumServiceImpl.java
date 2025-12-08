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
import ca.bc.gov.farms.model.v1.MarketRatePremium;
import ca.bc.gov.farms.model.v1.MarketRatePremiumList;
import ca.bc.gov.farms.persistence.v1.dao.MarketRatePremiumDao;
import ca.bc.gov.farms.persistence.v1.dto.MarketRatePremiumDto;
import ca.bc.gov.farms.service.api.v1.MarketRatePremiumService;
import ca.bc.gov.farms.service.api.v1.model.factory.MarketRatePremiumFactory;
import ca.bc.gov.farms.service.api.v1.validation.ModelValidator;

public class MarketRatePremiumServiceImpl implements MarketRatePremiumService {

    private static final Logger logger = LoggerFactory.getLogger(MarketRatePremiumServiceImpl.class);

    private ModelValidator modelValidator;

    // factories
    private MarketRatePremiumFactory marketRatePremiumFactory;

    // daos
    private MarketRatePremiumDao marketRatePremiumDao;

    public void setModelValidator(ModelValidator modelValidator) {
        this.modelValidator = modelValidator;
    }

    public void setMarketRatePremiumFactory(MarketRatePremiumFactory marketRatePremiumFactory) {
        this.marketRatePremiumFactory = marketRatePremiumFactory;
    }

    public void setMarketRatePremiumDao(MarketRatePremiumDao marketRatePremiumDao) {
        this.marketRatePremiumDao = marketRatePremiumDao;
    }

    @Override
    public MarketRatePremiumList<? extends MarketRatePremium> getAllMarketRatePremiums(FactoryContext factoryContext) throws ServiceException {
        logger.debug("<getAllMarketRatePremiums");

        MarketRatePremiumList<? extends MarketRatePremium> result = null;

        try {
            List<MarketRatePremiumDto> dtos = marketRatePremiumDao.fetchAll();

            result = marketRatePremiumFactory.getMarketRatePremiumList(dtos, factoryContext);
        } catch (Exception e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">getAllMarketRatePremiums");
        return result;
    }

    @Override
    public MarketRatePremium getMarketRatePremium(Long marketRatePremiumId,
            FactoryContext factoryContext) throws ServiceException, NotFoundException {
        logger.debug("<getMarketRatePremium");

        MarketRatePremium result = null;

        try {
            MarketRatePremiumDto dto = marketRatePremiumDao.fetch(marketRatePremiumId);

            if (dto == null) {
                throw new NotFoundException("Did not find the market rate premium: " + marketRatePremiumId);
            }

            result = marketRatePremiumFactory.getMarketRatePremium(dto, factoryContext);

        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">getMarketRatePremium");
        return result;
    }

    @Override
    public MarketRatePremium createMarketRatePremium(MarketRatePremium resource,
            FactoryContext factoryContext) throws ServiceException, ValidationFailureException {
        logger.debug("<createMarketRatePremium");

        MarketRatePremium result = null;
        String userId = UserUtil.toUserId(resource.getUserEmail());

        try {

            List<Message> errors = modelValidator.validateMarketRatePremium(resource);
            if (!errors.isEmpty()) {
                throw new ValidationFailureException(errors);
            }

            MarketRatePremiumDto dto = new MarketRatePremiumDto();

            marketRatePremiumFactory.updateMarketRatePremium(dto, resource);
            marketRatePremiumDao.insert(dto, userId);

            dto = marketRatePremiumDao.fetch(dto.getMarketRatePremiumId());
            result = marketRatePremiumFactory.getMarketRatePremium(dto, factoryContext);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">createMarketRatePremium");
        return result;
    }

    @Override
    public MarketRatePremium updateMarketRatePremium(Long marketRatePremiumId,
            MarketRatePremium marketRatePremium, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException, NotFoundException {
        logger.debug("<updateMarketRatePremium");

        MarketRatePremium result = null;
        String userId = UserUtil.toUserId(marketRatePremium.getUserEmail());

        try {

            List<Message> errors = modelValidator.validateMarketRatePremium(marketRatePremium);
            if (!errors.isEmpty()) {
                throw new ValidationFailureException(errors);
            }

            MarketRatePremiumDto dto = marketRatePremiumDao.fetch(marketRatePremiumId);

            if (dto == null) {
                throw new NotFoundException("Did not find the market rate premium: " + marketRatePremiumId);
            }

            marketRatePremiumFactory.updateMarketRatePremium(dto, marketRatePremium);
            marketRatePremiumDao.update(dto, userId);

            dto = marketRatePremiumDao.fetch(dto.getMarketRatePremiumId());
            result = marketRatePremiumFactory.getMarketRatePremium(dto, factoryContext);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">updateMarketRatePremium");
        return result;
    }

    @Override
    public void deleteMarketRatePremium(Long marketRatePremiumId)
            throws ServiceException, NotFoundException {
        logger.debug("<deleteMarketRatePremium");

        try {
            MarketRatePremiumDto dto = marketRatePremiumDao.fetch(marketRatePremiumId);

            if (dto == null) {
                throw new NotFoundException("Did not find the market rate premium: " + marketRatePremiumId);
            }

            marketRatePremiumDao.delete(marketRatePremiumId);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">deleteMarketRatePremium");
    }

}
