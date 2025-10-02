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
import ca.bc.gov.farms.model.v1.FruitVegTypeDetail;
import ca.bc.gov.farms.model.v1.FruitVegTypeDetailList;
import ca.bc.gov.farms.persistence.v1.dao.FruitVegTypeDetailDao;
import ca.bc.gov.farms.persistence.v1.dto.FruitVegTypeDetailDto;
import ca.bc.gov.farms.service.api.v1.FruitVegTypeDetailService;
import ca.bc.gov.farms.service.api.v1.model.factory.FruitVegTypeDetailFactory;
import ca.bc.gov.farms.service.api.v1.validation.ModelValidator;

public class FruitVegTypeDetailServiceImpl implements FruitVegTypeDetailService {

    private static final Logger logger = LoggerFactory.getLogger(FruitVegTypeDetailServiceImpl.class);

    private Properties applicationProperties;

    private ModelValidator modelValidator;

    // factories
    private FruitVegTypeDetailFactory fruitVegTypeDetailFactory;

    // daos
    private FruitVegTypeDetailDao fruitVegTypeDetailDao;

    public void setApplicationProperties(Properties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public void setModelValidator(ModelValidator modelValidator) {
        this.modelValidator = modelValidator;
    }

    public void setFruitVegTypeDetailFactory(FruitVegTypeDetailFactory fruitVegTypeDetailFactory) {
        this.fruitVegTypeDetailFactory = fruitVegTypeDetailFactory;
    }

    public void setFruitVegTypeDetailDao(FruitVegTypeDetailDao fruitVegTypeDetailDao) {
        this.fruitVegTypeDetailDao = fruitVegTypeDetailDao;
    }

    @Override
    public FruitVegTypeDetailList<? extends FruitVegTypeDetail> getAllFruitVegTypeDetails(FactoryContext factoryContext)
            throws ServiceException {
        logger.debug("<getAllFruitVegTypeDetails");

        FruitVegTypeDetailList<? extends FruitVegTypeDetail> result = null;

        try {
            List<FruitVegTypeDetailDto> dtos = fruitVegTypeDetailDao.fetchAll();

            result = fruitVegTypeDetailFactory.getFruitVegTypeDetailList(dtos, factoryContext);
        } catch (Exception e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">getAllFruitVegTypeDetails");
        return result;
    }

    @Override
    public FruitVegTypeDetail getFruitVegTypeDetail(String fruitVegTypeCode,
            FactoryContext factoryContext) throws ServiceException, NotFoundException {
        logger.debug("<getFruitVegTypeDetail");

        FruitVegTypeDetail result = null;

        try {
            FruitVegTypeDetailDto dto = fruitVegTypeDetailDao.fetch(fruitVegTypeCode);

            if (dto == null) {
                throw new NotFoundException("Did not find the fruit and veg type detail: " + fruitVegTypeCode);
            }

            result = fruitVegTypeDetailFactory.getFruitVegTypeDetail(dto, factoryContext);

        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">getFruitVegTypeDetail");
        return result;
    }

    @Override
    public FruitVegTypeDetail createFruitVegTypeDetail(FruitVegTypeDetail resource,
            FactoryContext factoryContext) throws ServiceException, ValidationFailureException {
        logger.debug("<createFruitVegTypeDetail");

        FruitVegTypeDetail result = null;
        String userId = UserUtil.toUserId(resource.getUserEmail());

        try {

            List<Message> errors = modelValidator.validateFruitVegTypeDetail(resource);
            if (!errors.isEmpty()) {
                throw new ValidationFailureException(errors);
            }

            FruitVegTypeDetailDto dto = new FruitVegTypeDetailDto();

            fruitVegTypeDetailFactory.updateFruitVegTypeDetail(dto, resource);
            fruitVegTypeDetailDao.insert(dto, userId);

            dto = fruitVegTypeDetailDao.fetch(dto.getFruitVegTypeCode());
            result = fruitVegTypeDetailFactory.getFruitVegTypeDetail(dto, factoryContext);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">createFruitVegTypeDetail");
        return result;
    }

    @Override
    public FruitVegTypeDetail updateFruitVegTypeDetail(String fruitVegTypeCode,
            FruitVegTypeDetail fruitVegTypeDetail, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException, NotFoundException {
        logger.debug("<updateFruitVegTypeDetail");

        FruitVegTypeDetail result = null;
        String userId = UserUtil.toUserId(fruitVegTypeDetail.getUserEmail());

        try {

            List<Message> errors = modelValidator.validateFruitVegTypeDetail(fruitVegTypeDetail);
            if (!errors.isEmpty()) {
                throw new ValidationFailureException(errors);
            }

            FruitVegTypeDetailDto dto = fruitVegTypeDetailDao.fetch(fruitVegTypeCode);

            if (dto == null) {
                throw new NotFoundException("Did not find the fruit and veg type detail: " + fruitVegTypeCode);
            }

            fruitVegTypeDetailFactory.updateFruitVegTypeDetail(dto, fruitVegTypeDetail);
            fruitVegTypeDetailDao.update(dto, userId);

            dto = fruitVegTypeDetailDao.fetch(dto.getFruitVegTypeCode());
            result = fruitVegTypeDetailFactory.getFruitVegTypeDetail(dto, factoryContext);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">updateFruitVegTypeDetail");
        return result;
    }

    @Override
    public void deleteFruitVegTypeDetail(String fruitVegTypeCode)
            throws ServiceException, NotFoundException {
        logger.debug("<deleteFruitVegTypeDetail");

        try {
            FruitVegTypeDetailDto dto = fruitVegTypeDetailDao.fetch(fruitVegTypeCode);

            if (dto == null) {
                throw new NotFoundException("Did not find the fruit and veg type detail: " + fruitVegTypeCode);
            }

            fruitVegTypeDetailDao.delete(fruitVegTypeCode);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">deleteFruitVegTypeDetail");
    }

}
