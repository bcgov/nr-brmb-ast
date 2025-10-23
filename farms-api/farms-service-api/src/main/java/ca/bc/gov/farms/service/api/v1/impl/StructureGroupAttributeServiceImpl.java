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
import ca.bc.gov.farms.model.v1.StructureGroupAttribute;
import ca.bc.gov.farms.persistence.v1.dao.StructureGroupAttributeDao;
import ca.bc.gov.farms.persistence.v1.dto.StructureGroupAttributeDto;
import ca.bc.gov.farms.service.api.v1.StructureGroupAttributeService;
import ca.bc.gov.farms.service.api.v1.model.factory.StructureGroupAttributeFactory;
import ca.bc.gov.farms.service.api.v1.validation.ModelValidator;

public class StructureGroupAttributeServiceImpl implements StructureGroupAttributeService {

    private static final Logger logger = LoggerFactory.getLogger(StructureGroupAttributeServiceImpl.class);

    private ModelValidator modelValidator;

    // factories
    private StructureGroupAttributeFactory structureGroupAttributeFactory;

    // daos
    private StructureGroupAttributeDao structureGroupAttributeDao;

    public void setModelValidator(ModelValidator modelValidator) {
        this.modelValidator = modelValidator;
    }

    public void setStructureGroupAttributeFactory(StructureGroupAttributeFactory structureGroupAttributeFactory) {
        this.structureGroupAttributeFactory = structureGroupAttributeFactory;
    }

    public void setStructureGroupAttributeDao(StructureGroupAttributeDao structureGroupAttributeDao) {
        this.structureGroupAttributeDao = structureGroupAttributeDao;
    }

    @Override
    public StructureGroupAttribute getStructureGroupAttributesByStructureGroupCode(
            String structureGroupCode, FactoryContext factoryContext) throws ServiceException {
        logger.debug("<getStructureGroupAttributesByStructureGroupCode");

        StructureGroupAttribute result = null;

        try {
            StructureGroupAttributeDto dto = structureGroupAttributeDao.fetchByStructureGroupCode(structureGroupCode);

            result = structureGroupAttributeFactory.getStructureGroupAttribute(dto, factoryContext);
        } catch (Exception e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">getStructureGroupAttributesByStructureGroupCode");
        return result;
    }

    @Override
    public StructureGroupAttribute getStructureGroupAttribute(Long structureGroupAttributeId,
            FactoryContext factoryContext)
            throws ServiceException, NotFoundException {
        logger.debug("<getStructureGroupAttribute");

        StructureGroupAttribute result = null;

        try {
            StructureGroupAttributeDto dto = structureGroupAttributeDao.fetch(structureGroupAttributeId);

            if (dto == null) {
                throw new NotFoundException("Did not find the structure group attribute: " + structureGroupAttributeId);
            }

            result = structureGroupAttributeFactory.getStructureGroupAttribute(dto, factoryContext);

        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">getStructureGroupAttribute");
        return result;
    }

    @Override
    public StructureGroupAttribute createStructureGroupAttribute(StructureGroupAttribute resource,
            FactoryContext factoryContext) throws ServiceException, ValidationFailureException {
        logger.debug("<createStructureGroupAttribute");

        StructureGroupAttribute result = null;
        String userId = UserUtil.toUserId(resource.getUserEmail());

        try {

            List<Message> errors = modelValidator.validateStructureGroupAttribute(resource);
            if (!errors.isEmpty()) {
                throw new ValidationFailureException(errors);
            }

            StructureGroupAttributeDto dto = new StructureGroupAttributeDto();

            structureGroupAttributeFactory.updateStructureGroupAttribute(dto, resource);
            structureGroupAttributeDao.insert(dto, userId);

            dto = structureGroupAttributeDao.fetch(dto.getStructureGroupAttributeId());
            result = structureGroupAttributeFactory.getStructureGroupAttribute(dto, factoryContext);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">createStructureGroupAttribute");
        return result;
    }

    @Override
    public StructureGroupAttribute updateStructureGroupAttribute(Long structureGroupAttributeId,
            StructureGroupAttribute structureGroupAttribute, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException, NotFoundException {
        logger.debug("<updateStructureGroupAttribute");

        StructureGroupAttribute result = null;
        String userId = UserUtil.toUserId(structureGroupAttribute.getUserEmail());

        try {

            List<Message> errors = modelValidator.validateStructureGroupAttribute(structureGroupAttribute);
            if (!errors.isEmpty()) {
                throw new ValidationFailureException(errors);
            }

            StructureGroupAttributeDto dto = structureGroupAttributeDao.fetch(structureGroupAttributeId);

            if (dto == null) {
                throw new NotFoundException("Did not find the structure group attribute: " + structureGroupAttributeId);
            }

            structureGroupAttributeFactory.updateStructureGroupAttribute(dto, structureGroupAttribute);
            structureGroupAttributeDao.update(dto, userId);

            dto = structureGroupAttributeDao.fetch(dto.getStructureGroupAttributeId());
            result = structureGroupAttributeFactory.getStructureGroupAttribute(dto, factoryContext);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">updateStructureGroupAttribute");
        return result;
    }

    @Override
    public void deleteStructureGroupAttribute(Long structureGroupAttributeId)
            throws ServiceException, NotFoundException {
        logger.debug("<deleteStructureGroupAttribute");

        try {
            StructureGroupAttributeDto dto = structureGroupAttributeDao.fetch(structureGroupAttributeId);

            if (dto == null) {
                throw new NotFoundException("Did not find the structure group attribute: " + structureGroupAttributeId);
            }

            structureGroupAttributeDao.delete(structureGroupAttributeId);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">deleteStructureGroupAttribute");
    }

}
