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
import ca.bc.gov.farms.model.v1.InventoryItemAttribute;
import ca.bc.gov.farms.persistence.v1.dao.InventoryItemAttributeDao;
import ca.bc.gov.farms.persistence.v1.dto.InventoryItemAttributeDto;
import ca.bc.gov.farms.service.api.v1.InventoryItemAttributeService;
import ca.bc.gov.farms.service.api.v1.model.factory.InventoryItemAttributeFactory;
import ca.bc.gov.farms.service.api.v1.validation.ModelValidator;

public class InventoryItemAttributeServiceImpl implements InventoryItemAttributeService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryItemAttributeServiceImpl.class);

    private Properties applicationProperties;

    private ModelValidator modelValidator;

    // factories
    private InventoryItemAttributeFactory inventoryItemAttributeFactory;

    // daos
    private InventoryItemAttributeDao inventoryItemAttributeDao;

    public void setApplicationProperties(Properties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public void setModelValidator(ModelValidator modelValidator) {
        this.modelValidator = modelValidator;
    }

    public void setInventoryItemAttributeFactory(InventoryItemAttributeFactory inventoryItemAttributeFactory) {
        this.inventoryItemAttributeFactory = inventoryItemAttributeFactory;
    }

    public void setInventoryItemAttributeDao(InventoryItemAttributeDao inventoryItemAttributeDao) {
        this.inventoryItemAttributeDao = inventoryItemAttributeDao;
    }

    @Override
    public InventoryItemAttribute getInventoryItemAttributesByInventoryItemCode(
            String inventoryItemCode, FactoryContext factoryContext) throws ServiceException {
        logger.debug("<getInventoryItemAttributesByInventoryItemCode");

        InventoryItemAttribute result = null;

        try {
            InventoryItemAttributeDto dto = inventoryItemAttributeDao.fetchByInventoryItemCode(inventoryItemCode);

            result = inventoryItemAttributeFactory.getInventoryItemAttribute(dto, factoryContext);
        } catch (Exception e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">getInventoryItemAttributesByInventoryItemCode");
        return result;
    }

    @Override
    public InventoryItemAttribute getInventoryItemAttribute(Long inventoryItemAttributeId, FactoryContext factoryContext)
            throws ServiceException, NotFoundException {
        logger.debug("<getInventoryItemAttribute");

        InventoryItemAttribute result = null;

        try {
            InventoryItemAttributeDto dto = inventoryItemAttributeDao.fetch(inventoryItemAttributeId);

            if (dto == null) {
                throw new NotFoundException("Did not find the inventory item attribute: " + inventoryItemAttributeId);
            }

            result = inventoryItemAttributeFactory.getInventoryItemAttribute(dto, factoryContext);

        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">getInventoryItemAttribute");
        return result;
    }

    @Override
    public InventoryItemAttribute createInventoryItemAttribute(InventoryItemAttribute resource, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException {
        logger.debug("<createInventoryItemAttribute");

        InventoryItemAttribute result = null;
        String userId = UserUtil.toUserId(resource.getUserEmail());

        try {

            List<Message> errors = modelValidator.validateInventoryItemAttribute(resource);
            if (!errors.isEmpty()) {
                throw new ValidationFailureException(errors);
            }

            InventoryItemAttributeDto dto = new InventoryItemAttributeDto();

            inventoryItemAttributeFactory.updateInventoryItemAttribute(dto, resource);
            inventoryItemAttributeDao.insert(dto, userId);

            dto = inventoryItemAttributeDao.fetch(dto.getInventoryItemAttributeId());
            result = inventoryItemAttributeFactory.getInventoryItemAttribute(dto, factoryContext);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">createInventoryItemAttribute");
        return result;
    }

    @Override
    public InventoryItemAttribute updateInventoryItemAttribute(Long inventoryItemAttributeId,
            InventoryItemAttribute inventoryItemAttribute, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException, NotFoundException {
        logger.debug("<updateInventoryItemAttribute");

        InventoryItemAttribute result = null;
        String userId = UserUtil.toUserId(inventoryItemAttribute.getUserEmail());

        try {

            List<Message> errors = modelValidator.validateInventoryItemAttribute(inventoryItemAttribute);
            if (!errors.isEmpty()) {
                throw new ValidationFailureException(errors);
            }

            InventoryItemAttributeDto dto = inventoryItemAttributeDao.fetch(inventoryItemAttributeId);

            if (dto == null) {
                throw new NotFoundException("Did not find the inventory item attribute: " + inventoryItemAttributeId);
            }

            inventoryItemAttributeFactory.updateInventoryItemAttribute(dto, inventoryItemAttribute);
            inventoryItemAttributeDao.update(dto, userId);

            dto = inventoryItemAttributeDao.fetch(dto.getInventoryItemAttributeId());
            result = inventoryItemAttributeFactory.getInventoryItemAttribute(dto, factoryContext);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">updateInventoryItemAttribute");
        return result;
    }

    @Override
    public void deleteInventoryItemAttribute(Long inventoryItemAttributeId) throws ServiceException, NotFoundException {
        logger.debug("<deleteInventoryItemAttribute");

        try {
            InventoryItemAttributeDto dto = inventoryItemAttributeDao.fetch(inventoryItemAttributeId);

            if (dto == null) {
                throw new NotFoundException("Did not find the inventory item attribute: " + inventoryItemAttributeId);
            }

            inventoryItemAttributeDao.delete(inventoryItemAttributeId);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">deleteInventoryItemAttribute");
    }

}
