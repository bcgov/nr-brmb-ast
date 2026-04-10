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
import ca.bc.gov.farms.data.models.LineItemListRsrc;
import ca.bc.gov.farms.data.models.LineItemRsrc;
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

    public LineItemListRsrc getLineItemsByProgramYear(Integer programYear) throws ServiceException {

        LineItemListRsrc result = null;

        try {
            List<LineItemEntity> entities = lineItemMapper.fetchByProgramYear(programYear);

            result = lineItemResourceAssembler.getLineItemList(entities);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    public LineItemRsrc getLineItem(Long lineItemId) throws ServiceException, NotFoundException {

        LineItemRsrc result = null;

        try {
            LineItemEntity entity = lineItemMapper.fetch(lineItemId);

            if (entity == null) {
                throw new NotFoundException("Did not find the line item: " + lineItemId);
            }

            result = lineItemResourceAssembler.getLineItem(entity);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public LineItemRsrc createLineItem(LineItemRsrc resource) throws ServiceException, ConstraintViolationException {

        Set<ConstraintViolation<LineItemRsrc>> violations = validator.validate(resource);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        LineItemRsrc result = null;
        String userId = resource.getUserEmail();

        try {

            LineItemEntity dto = new LineItemEntity();

            lineItemResourceAssembler.updateLineItem(resource, dto);
            int count = lineItemMapper.insertLineItem(dto, userId);
            if (count == 0) {
                throw new ServiceException("Record not inserted: " + count);
            }

            dto = lineItemMapper.fetch(dto.getLineItemId());
            result = lineItemResourceAssembler.getLineItem(dto);
        } catch (ServiceException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public LineItemRsrc updateLineItem(Long lineItemId, LineItemRsrc resource)
            throws ServiceException, ConstraintViolationException, NotFoundException {

        Set<ConstraintViolation<LineItemRsrc>> violations = validator.validate(resource);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        LineItemRsrc result = null;
        String userId = resource.getUserEmail();

        try {

            LineItemEntity dto = lineItemMapper.fetch(lineItemId);

            if (dto == null) {
                throw new NotFoundException("Did not find the line item: " + lineItemId);
            }

            // expire old line item
            int count = lineItemMapper.deleteLineItem(dto.getLineItemId());
            if (count == 0) {
                throw new ServiceException("Record not deleted: " + count);
            }

            // insert new line item
            lineItemResourceAssembler.updateLineItem(resource, dto);
            dto.setLineItemId(null);
            count = lineItemMapper.insertLineItem(dto, userId);
            if (count == 0) {
                throw new ServiceException("Record not inserted: " + count);
            }

            dto = lineItemMapper.fetch(dto.getLineItemId());
            result = lineItemResourceAssembler.getLineItem(dto);
        } catch (ServiceException | NotFoundException ex) {
            throw ex;
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

            int count = lineItemMapper.deleteLineItem(lineItemId);
            if (count == 0) {
                throw new ServiceException("Record not deleted: " + count);
            }
        } catch (ServiceException | NotFoundException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }
    }

    @Transactional
    public LineItemListRsrc copyLineItems(Integer currentYear) throws ServiceException {

        LineItemListRsrc result = null;
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
                    int count = lineItemMapper.insertLineItem(dto, dto.getCreateUser());
                    if (count == 0) {
                        throw new ServiceException("Record not inserted: " + count);
                    }
                }
            }

            List<LineItemEntity> currentYearDtos = lineItemMapper.fetchByProgramYear(currentYear);

            result = lineItemResourceAssembler.getLineItemList(currentYearDtos);
        } catch (ServiceException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }
}
