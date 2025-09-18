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
import ca.bc.gov.farms.model.v1.LineItem;
import ca.bc.gov.farms.model.v1.LineItemList;
import ca.bc.gov.farms.persistence.v1.dao.LineItemDao;
import ca.bc.gov.farms.persistence.v1.dto.LineItemDto;
import ca.bc.gov.farms.service.api.v1.LineItemService;
import ca.bc.gov.farms.service.api.v1.model.factory.LineItemFactory;
import ca.bc.gov.farms.service.api.v1.validation.ModelValidator;

public class LineItemServiceImpl implements LineItemService {

    private static final Logger logger = LoggerFactory.getLogger(LineItemServiceImpl.class);

    private Properties applicationProperties;

    private ModelValidator modelValidator;

    // factories
    private LineItemFactory lineItemFactory;

    // daos
    private LineItemDao lineItemDao;

    public void setApplicationProperties(Properties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public void setModelValidator(ModelValidator modelValidator) {
        this.modelValidator = modelValidator;
    }

    public void setLineItemFactory(LineItemFactory lineItemFactory) {
        this.lineItemFactory = lineItemFactory;
    }

    public void setLineItemDao(LineItemDao lineItemDao) {
        this.lineItemDao = lineItemDao;
    }

    @Override
    public LineItemList<? extends LineItem> getLineItemsByProgramYear(Integer programYear,
            FactoryContext factoryContext) throws ServiceException {
        logger.debug("<getLineItemsByProgramYear");

        LineItemList<? extends LineItem> result = null;

        try {
            List<LineItemDto> dtos = lineItemDao.fetchByProgramYear(programYear);

            result = lineItemFactory.getLineItemList(dtos, factoryContext);
        } catch (Exception e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">getLineItemsByProgramYear");
        return result;
    }

    @Override
    public LineItem getLineItem(Long lineItemId, FactoryContext factoryContext)
            throws ServiceException, NotFoundException {
        logger.debug("<getLineItem");

        LineItem result = null;

        try {
            LineItemDto dto = lineItemDao.fetch(lineItemId);

            if (dto == null) {
                throw new NotFoundException("Did not find the line item: " + lineItemId);
            }

            result = lineItemFactory.getLineItem(dto, factoryContext);

        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">getLineItem");
        return result;
    }

    @Override
    public LineItem createLineItem(LineItem resource, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException {
        logger.debug("<createLineItem");

        LineItem result = null;
        String userId = UserUtil.toUserId(resource.getUserEmail());

        try {

            List<Message> errors = modelValidator.validateLineItem(resource);
            if (!errors.isEmpty()) {
                throw new ValidationFailureException(errors);
            }

            LineItemDto dto = new LineItemDto();

            lineItemFactory.updateLineItem(dto, resource);
            lineItemDao.insert(dto, userId);

            dto = lineItemDao.fetch(dto.getLineItemId());
            result = lineItemFactory.getLineItem(dto, factoryContext);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">createLineItem");
        return result;
    }

    @Override
    public LineItem updateLineItem(Long lineItemId, LineItem lineItem, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException, NotFoundException {
        logger.debug("<updateLineItem");

        LineItem result = null;
        String userId = UserUtil.toUserId(lineItem.getUserEmail());

        try {

            List<Message> errors = modelValidator.validateLineItem(lineItem);
            if (!errors.isEmpty()) {
                throw new ValidationFailureException(errors);
            }

            LineItemDto dto = lineItemDao.fetch(lineItemId);

            if (dto == null) {
                throw new NotFoundException("Did not find the line item: " + lineItemId);
            }

            lineItemFactory.updateLineItem(dto, lineItem);
            lineItemDao.update(dto, userId);

            dto = lineItemDao.fetch(dto.getLineItemId());
            result = lineItemFactory.getLineItem(dto, factoryContext);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">updateLineItem");
        return result;
    }

    @Override
    public void deleteLineItem(Long lineItemId) throws ServiceException, NotFoundException {
        logger.debug("<deleteLineItem");

        try {
            LineItemDto dto = lineItemDao.fetch(lineItemId);

            if (dto == null) {
                throw new NotFoundException("Did not find the line item: " + lineItemId);
            }

            lineItemDao.delete(lineItemId);
        } catch (DaoException e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">deleteLineItem");
    }

    @Override
    public LineItemList<? extends LineItem> copyLineItems(Integer currentYear, FactoryContext factoryContext)
            throws ServiceException {
        logger.debug("<copyLineItems");

        LineItemList<? extends LineItem> result = null;
        Integer previousYear = currentYear - 1;

        try {
            List<LineItemDto> dtos = lineItemDao.fetchByProgramYear(previousYear);

            for (LineItemDto dto : dtos) {
                dto.setLineItemId(null);
                dto.setProgramYear(currentYear);
                lineItemDao.insert(dto, dto.getCreateUser());
            }

            dtos = lineItemDao.fetchByProgramYear(currentYear);

            result = lineItemFactory.getLineItemList(dtos, factoryContext);
        } catch (Exception e) {
            throw new ServiceException("DAO threw an exception", e);
        }

        logger.debug(">copyLineItems");
        return result;
    }

}
