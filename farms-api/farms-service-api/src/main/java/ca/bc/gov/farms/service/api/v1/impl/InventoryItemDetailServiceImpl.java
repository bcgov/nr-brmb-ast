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
import ca.bc.gov.farms.model.v1.InventoryItemDetail;
import ca.bc.gov.farms.model.v1.InventoryItemDetailList;
import ca.bc.gov.farms.persistence.v1.dao.InventoryItemDetailDao;
import ca.bc.gov.farms.persistence.v1.dto.InventoryItemDetailDto;
import ca.bc.gov.farms.service.api.v1.InventoryItemDetailService;
import ca.bc.gov.farms.service.api.v1.model.factory.InventoryItemDetailFactory;
import ca.bc.gov.farms.service.api.v1.validation.ModelValidator;

public class InventoryItemDetailServiceImpl implements InventoryItemDetailService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryItemDetailServiceImpl.class);

    private Properties applicationProperties;

    private ModelValidator modelValidator;

    // factories
    private InventoryItemDetailFactory inventoryItemDetailFactory;

    // daos
    private InventoryItemDetailDao inventoryItemDetailDao;

    public void setApplicationProperties(Properties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public void setModelValidator(ModelValidator modelValidator) {
        this.modelValidator = modelValidator;
    }

    public void setInventoryItemDetailFactory(InventoryItemDetailFactory inventoryItemDetailFactory) {
        this.inventoryItemDetailFactory = inventoryItemDetailFactory;
    }

    public void setInventoryItemDetailDao(InventoryItemDetailDao inventoryItemDetailDao) {
        this.inventoryItemDetailDao = inventoryItemDetailDao;
    }

    @Override
    public InventoryItemDetailList<? extends InventoryItemDetail> getInventoryItemDetailsByInventoryItemCode(
            String inventoryItemCode, FactoryContext factoryContext) throws ServiceException {
        logger.debug("<getInventoryItemDetailsByInventoryItemCode");

        InventoryItemDetailList<? extends InventoryItemDetail> result = null;

        try {
            List<InventoryItemDetailDto> dtos = inventoryItemDetailDao.fetchByInventoryItemCode(inventoryItemCode);

            result = inventoryItemDetailFactory.getInventoryItemDetailList(dtos, factoryContext);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">getInventoryItemDetailsByInventoryItemCode");
        return result;
    }

    @Override
    public InventoryItemDetail getInventoryItemDetail(Long inventoryItemDetailId, FactoryContext factoryContext)
            throws ServiceException, NotFoundException {
        logger.debug("<getInventoryItemDetail");

        InventoryItemDetail result = null;

        try {
            InventoryItemDetailDto dto = inventoryItemDetailDao.fetch(inventoryItemDetailId);

            if (dto == null) {
                throw new NotFoundException("Did not find the inventory item detail: " + inventoryItemDetailId);
            }

            result = inventoryItemDetailFactory.getInventoryItemDetail(dto, factoryContext);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">getInventoryItemDetail");
        return result;
    }

    @Override
    public InventoryItemDetail createInventoryItemDetail(InventoryItemDetail resource, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException {
        logger.debug("<createInventoryItemDetail");

        InventoryItemDetail result = null;
        String userId = UserUtil.toUserId(resource.getUserEmail());

        try {

            List<Message> errors = modelValidator.validateInventoryItemDetail(resource);
            if (!errors.isEmpty()) {
                throw new ValidationFailureException(errors);
            }

            InventoryItemDetailDto dto = new InventoryItemDetailDto();

            inventoryItemDetailFactory.updateInventoryItemDetail(dto, resource);
            inventoryItemDetailDao.insert(dto, userId);

            dto = inventoryItemDetailDao.fetch(dto.getInventoryItemDetailId());
            result = inventoryItemDetailFactory.getInventoryItemDetail(dto, factoryContext);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">createInventoryItemDetail");
        return result;
    }

    @Override
    public InventoryItemDetail updateInventoryItemDetail(Long inventoryItemDetailId,
            InventoryItemDetail inventoryItemDetail, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException, NotFoundException {
        logger.debug("<updateInventoryItemDetail");

        InventoryItemDetail result = null;
        String userId = UserUtil.toUserId(inventoryItemDetail.getUserEmail());

        try {

            List<Message> errors = modelValidator.validateInventoryItemDetail(inventoryItemDetail);
            if (!errors.isEmpty()) {
                throw new ValidationFailureException(errors);
            }

            InventoryItemDetailDto dto = inventoryItemDetailDao.fetch(inventoryItemDetailId);

            if (dto == null) {
                throw new NotFoundException("Did not find the inventory item detail: " + inventoryItemDetailId);
            }

            inventoryItemDetailFactory.updateInventoryItemDetail(dto, inventoryItemDetail);
            inventoryItemDetailDao.update(dto, userId);

            dto = inventoryItemDetailDao.fetch(inventoryItemDetailId);
            result = inventoryItemDetailFactory.getInventoryItemDetail(dto, factoryContext);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">updateInventoryItemDetail");
        return result;
    }

    @Override
    public void deleteInventoryItemDetail(Long inventoryItemDetailId) throws ServiceException, NotFoundException {
        logger.debug("<deleteInventoryItemDetail");

        try {
            InventoryItemDetailDto dto = inventoryItemDetailDao.fetch(inventoryItemDetailId);

            if (dto == null) {
                throw new NotFoundException("Did not find the inventory item detail: " + inventoryItemDetailId);
            }

            inventoryItemDetailDao.delete(inventoryItemDetailId);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">deleteInventoryItemDetail");
    }

}
