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
import ca.bc.gov.farms.model.v1.InventoryTypeXref;
import ca.bc.gov.farms.model.v1.InventoryTypeXrefList;
import ca.bc.gov.farms.persistence.v1.dao.InventoryTypeXrefDao;
import ca.bc.gov.farms.persistence.v1.dto.InventoryTypeXrefDto;
import ca.bc.gov.farms.service.api.v1.InventoryTypeXrefService;
import ca.bc.gov.farms.service.api.v1.model.factory.InventoryTypeXrefFactory;
import ca.bc.gov.farms.service.api.v1.validation.ModelValidator;

public class InventoryTypeXrefServiceImpl implements InventoryTypeXrefService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryTypeXrefServiceImpl.class);

    private ModelValidator modelValidator;

    // factories
    private InventoryTypeXrefFactory inventoryTypeXrefFactory;

    // daos
    private InventoryTypeXrefDao inventoryTypeXrefDao;

    public void setModelValidator(ModelValidator modelValidator) {
        this.modelValidator = modelValidator;
    }

    public void setInventoryTypeXrefFactory(InventoryTypeXrefFactory inventoryTypeXrefFactory) {
        this.inventoryTypeXrefFactory = inventoryTypeXrefFactory;
    }

    public void setInventoryTypeXrefDao(InventoryTypeXrefDao inventoryTypeXrefDao) {
        this.inventoryTypeXrefDao = inventoryTypeXrefDao;
    }

    @Override
    public InventoryTypeXrefList<? extends InventoryTypeXref> getInventoryTypeXrefsByInventoryClassCode(
            String inventoryClassCode, FactoryContext factoryContext) throws ServiceException {
        logger.debug("<getInventoryTypeXrefsByInventoryClassCode");

        InventoryTypeXrefList<? extends InventoryTypeXref> result = null;

        try {
            List<InventoryTypeXrefDto> dtos = inventoryTypeXrefDao.fetchByInventoryClassCode(inventoryClassCode);

            result = inventoryTypeXrefFactory.getInventoryTypeXrefList(dtos, factoryContext);
        } catch (Exception e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">getInventoryTypeXrefsByInventoryClassCode");
        return result;
    }

    @Override
    public InventoryTypeXref getInventoryTypeXref(Long agristabilityCommodityXrefId, FactoryContext factoryContext)
            throws ServiceException, NotFoundException {
        logger.debug("<getInventoryTypeXref");

        InventoryTypeXref result = null;

        try {
            InventoryTypeXrefDto dto = inventoryTypeXrefDao.fetch(agristabilityCommodityXrefId);

            if (dto == null) {
                throw new NotFoundException("Did not find the inventory type xref: " + agristabilityCommodityXrefId);
            }

            result = inventoryTypeXrefFactory.getInventoryTypeXref(dto, factoryContext);

        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">getInventoryTypeXref");
        return result;
    }

    @Override
    public InventoryTypeXref createInventoryTypeXref(InventoryTypeXref resource, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException {
        logger.debug("<createInventoryTypeXref");

        InventoryTypeXref result = null;
        String userId = UserUtil.toUserId(resource.getUserEmail());

        try {

            List<Message> errors = modelValidator.validateInventoryTypeXref(resource);
            if (!errors.isEmpty()) {
                throw new ValidationFailureException(errors);
            }

            InventoryTypeXrefDto dto = new InventoryTypeXrefDto();

            inventoryTypeXrefFactory.updateInventoryTypeXref(dto, resource);
            inventoryTypeXrefDao.insert(dto, userId);

            dto = inventoryTypeXrefDao.fetch(dto.getAgristabilityCommodityXrefId());
            result = inventoryTypeXrefFactory.getInventoryTypeXref(dto, factoryContext);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">createInventoryTypeXref");
        return result;
    }

    @Override
    public InventoryTypeXref updateInventoryTypeXref(Long agristabilityCommodityXrefId,
            InventoryTypeXref inventoryTypeXref, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException, NotFoundException {
        logger.debug("<updateInventoryTypeXref");

        InventoryTypeXref result = null;
        String userId = UserUtil.toUserId(inventoryTypeXref.getUserEmail());

        try {

            List<Message> errors = modelValidator.validateInventoryTypeXref(inventoryTypeXref);
            if (!errors.isEmpty()) {
                throw new ValidationFailureException(errors);
            }

            InventoryTypeXrefDto dto = inventoryTypeXrefDao.fetch(agristabilityCommodityXrefId);

            if (dto == null) {
                throw new NotFoundException("Did not find the inventory type xref: " + agristabilityCommodityXrefId);
            }

            inventoryTypeXrefFactory.updateInventoryTypeXref(dto, inventoryTypeXref);
            inventoryTypeXrefDao.update(dto, userId);

            dto = inventoryTypeXrefDao.fetch(dto.getAgristabilityCommodityXrefId());
            result = inventoryTypeXrefFactory.getInventoryTypeXref(dto, factoryContext);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">updateInventoryTypeXref");
        return result;
    }

    @Override
    public void deleteInventoryTypeXref(Long agristabilityCommodityXrefId) throws ServiceException, NotFoundException {
        logger.debug("<deleteInventoryTypeXref");

        try {
            InventoryTypeXrefDto dto = inventoryTypeXrefDao.fetch(agristabilityCommodityXrefId);

            if (dto == null) {
                throw new NotFoundException("Did not find the inventory type xref: " + agristabilityCommodityXrefId);
            }

            inventoryTypeXrefDao.delete(agristabilityCommodityXrefId);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">deleteInventoryTypeXref");
    }

}
