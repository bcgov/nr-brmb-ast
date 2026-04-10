package ca.bc.gov.farms.services;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.farms.data.assemblers.InventoryItemDetailResourceAssembler;
import ca.bc.gov.farms.data.entities.InventoryItemDetailEntity;
import ca.bc.gov.farms.data.mappers.InventoryItemDetailMapper;
import ca.bc.gov.farms.data.models.InventoryItemDetailListRsrc;
import ca.bc.gov.farms.data.models.InventoryItemDetailModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InventoryItemDetailService {

    @Autowired
    private InventoryItemDetailMapper inventoryItemDetailMapper;

    @Autowired
    private InventoryItemDetailResourceAssembler inventoryItemDetailResourceAssembler;

    @Autowired
    private Validator validator;

    public InventoryItemDetailListRsrc getInventoryItemDetailsByInventoryItemCode(String inventoryItemCode)
            throws ServiceException {

        InventoryItemDetailListRsrc result = null;

        try {
            List<InventoryItemDetailEntity> entities = inventoryItemDetailMapper
                    .fetchByInventoryItemCode(inventoryItemCode);

            result = inventoryItemDetailResourceAssembler.getInventoryItemDetailList(entities);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    public InventoryItemDetailModel getInventoryItemDetail(Long inventoryItemDetailId)
            throws ServiceException, NotFoundException {

        InventoryItemDetailModel result = null;

        try {
            InventoryItemDetailEntity entity = inventoryItemDetailMapper.fetch(inventoryItemDetailId);

            if (entity == null) {
                throw new NotFoundException("Did not find the inventory item detail: " + inventoryItemDetailId);
            }

            result = inventoryItemDetailResourceAssembler.getInventoryItemDetail(entity);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public InventoryItemDetailModel createInventoryItemDetail(InventoryItemDetailModel resource)
            throws ServiceException, ConstraintViolationException {

        Set<ConstraintViolation<InventoryItemDetailModel>> violations = validator.validate(resource);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        InventoryItemDetailModel result = null;
        String userId = resource.getUserEmail();

        try {

            InventoryItemDetailEntity dto = new InventoryItemDetailEntity();

            inventoryItemDetailResourceAssembler.updateInventoryItemDetail(resource, dto);
            int count = inventoryItemDetailMapper.insertInventoryItemDetail(dto, userId);
            if (count == 0) {
                throw new ServiceException("Record not inserted: " + count);
            }

            dto = inventoryItemDetailMapper.fetch(dto.getInventoryItemDetailId());
            result = inventoryItemDetailResourceAssembler.getInventoryItemDetail(dto);
        } catch (ServiceException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public InventoryItemDetailModel updateInventoryItemDetail(Long inventoryItemDetailId,
            InventoryItemDetailModel resource)
            throws ServiceException, ConstraintViolationException, NotFoundException {

        Set<ConstraintViolation<InventoryItemDetailModel>> violations = validator.validate(resource);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        InventoryItemDetailModel result = null;
        String userId = resource.getUserEmail();

        try {

            InventoryItemDetailEntity dto = inventoryItemDetailMapper.fetch(inventoryItemDetailId);

            if (dto == null) {
                throw new NotFoundException("Did not find the inventory item detail: " + inventoryItemDetailId);
            }

            inventoryItemDetailResourceAssembler.updateInventoryItemDetail(resource, dto);
            int count = inventoryItemDetailMapper.updateInventoryItemDetail(dto, userId);
            if (count == 0) {
                throw new ServiceException("Record not updated: " + dto.getInventoryItemDetailId());
            }

            dto = inventoryItemDetailMapper.fetch(inventoryItemDetailId);
            result = inventoryItemDetailResourceAssembler.getInventoryItemDetail(dto);
        } catch (ServiceException | NotFoundException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public void deleteInventoryItemDetail(Long inventoryItemDetailId) throws ServiceException, NotFoundException {

        try {
            InventoryItemDetailEntity entity = inventoryItemDetailMapper.fetch(inventoryItemDetailId);

            if (entity == null) {
                throw new NotFoundException("Did not find the inventory item detail: " + inventoryItemDetailId);
            }

            int count = inventoryItemDetailMapper.deleteInventoryItemDetail(inventoryItemDetailId);
            if (count == 0) {
                throw new ServiceException("Record not deleted: " + inventoryItemDetailId);
            }
        } catch (ServiceException | NotFoundException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }
    }
}
