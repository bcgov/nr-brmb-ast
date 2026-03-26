package ca.bc.gov.farms.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.farms.data.assemblers.LineItemResourceAssembler;
import ca.bc.gov.farms.data.entities.LineItemEntity;
import ca.bc.gov.farms.data.mappers.LineItemMapper;
import ca.bc.gov.farms.data.models.LineItemListModel;
import ca.bc.gov.farms.data.models.LineItemModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LineItemService {

    @Autowired
    private LineItemMapper lineItemMapper;

    @Autowired
    private LineItemResourceAssembler lineItemResourceAssembler;

    @Autowired
    private Validator validator;

    public LineItemListModel getLineItemsByProgramYear(Integer programYear) throws ServiceException {

        LineItemListModel result = null;

        try {
            List<LineItemEntity> entities = lineItemMapper.fetchByProgramYear(programYear);

            result = lineItemResourceAssembler.getLineItemList(entities);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    public LineItemModel getLineItem(Long lineItemId) throws ServiceException, NotFoundException {

        LineItemModel result = null;

        try {
            LineItemEntity entity = lineItemMapper.fetch(lineItemId);

            if (entity == null) {
                throw new NotFoundException("Did not find the line item: " + lineItemId);
            }

            result = lineItemResourceAssembler.getLineItem(entity);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public LineItemModel createLineItem(LineItemModel resource) throws ServiceException, ConstraintViolationException {

        Set<ConstraintViolation<LineItemModel>> violations = validator.validate(resource);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        LineItemModel result = null;
        String userId = resource.getUserEmail();

        try {

            LineItemEntity dto = new LineItemEntity();

            lineItemResourceAssembler.updateLineItem(resource, dto);
            lineItemMapper.insertLineItem(dto, userId);

            dto = lineItemMapper.fetch(dto.getLineItemId());
            result = lineItemResourceAssembler.getLineItem(dto);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public LineItemModel updateLineItem(Long lineItemId, LineItemModel resource)
            throws ServiceException, ConstraintViolationException, NotFoundException {

        Set<ConstraintViolation<LineItemModel>> violations = validator.validate(resource);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        LineItemModel result = null;
        String userId = resource.getUserEmail();

        try {

            LineItemEntity dto = lineItemMapper.fetch(lineItemId);

            if (dto == null) {
                throw new NotFoundException("Did not find the line item: " + lineItemId);
            }

            // expire old line item
            lineItemMapper.deleteLineItem(dto.getLineItemId());

            // insert new line item
            lineItemResourceAssembler.updateLineItem(resource, dto);
            dto.setLineItemId(null);
            lineItemMapper.insertLineItem(dto, userId);

            dto = lineItemMapper.fetch(dto.getLineItemId());
            result = lineItemResourceAssembler.getLineItem(dto);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public void deleteLineItem(Long lineItemId) throws ServiceException, NotFoundException {

        try {
            LineItemEntity entity = lineItemMapper.fetch(lineItemId);

            if (entity == null) {
                throw new NotFoundException("Did not find the line item: " + lineItemId);
            }

            lineItemMapper.deleteLineItem(lineItemId);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }
    }

    @Transactional
    public LineItemListModel copyLineItems(Integer currentYear) throws ServiceException {

        LineItemListModel result = null;
        Integer previousYear = currentYear - 1;

        try {
            List<LineItemEntity> previousYearDtos = lineItemMapper.fetchByProgramYear(previousYear);

            Map<Integer, LineItemEntity> currentYearDtoMap = new HashMap<>();
            for (LineItemEntity dto : lineItemMapper.fetchByProgramYear(currentYear)) {
                currentYearDtoMap.put(dto.getLineItem(), dto);
            }

            for (LineItemEntity dto : previousYearDtos) {
                if (!currentYearDtoMap.containsKey(dto.getLineItem())) {
                    dto.setLineItemId(null);
                    dto.setProgramYear(currentYear);
                    lineItemMapper.insertLineItem(dto, dto.getCreateUser());
                }
            }

            List<LineItemEntity> currentYearDtos = lineItemMapper.fetchByProgramYear(currentYear);

            result = lineItemResourceAssembler.getLineItemList(currentYearDtos);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }
}
