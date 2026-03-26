package ca.bc.gov.farms.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.farms.data.assemblers.InventoryItemAttributeResourceAssembler;
import ca.bc.gov.farms.data.entities.InventoryItemAttributeEntity;
import ca.bc.gov.farms.data.mappers.InventoryItemAttributeMapper;
import ca.bc.gov.farms.data.models.InventoryItemAttributeModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InventoryItemAttributeService {

    @Autowired
    private InventoryItemAttributeMapper inventoryItemAttributeMapper;

    @Autowired
    private InventoryItemAttributeResourceAssembler inventoryItemAttributeResourceAssembler;

    @Autowired
    private Validator validator;

    public InventoryItemAttributeModel getInventoryItemAttributeByInventoryItemCode(String inventoryItemCode)
            throws ServiceException {

        InventoryItemAttributeModel result = null;

        try {
            InventoryItemAttributeEntity entity = inventoryItemAttributeMapper
                    .fetchByInventoryItemCode(inventoryItemCode);

            result = inventoryItemAttributeResourceAssembler.getInventoryItemAttribute(entity);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    public InventoryItemAttributeModel getInventoryItemAttribute(Long inventoryItemAttributeId)
            throws ServiceException, NotFoundException {

        InventoryItemAttributeModel result = null;

        try {
            InventoryItemAttributeEntity entity = inventoryItemAttributeMapper.fetch(inventoryItemAttributeId);

            if (entity == null) {
                throw new NotFoundException("Did not find the inventory item attribute: " + inventoryItemAttributeId);
            }

            result = inventoryItemAttributeResourceAssembler.getInventoryItemAttribute(entity);

        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public InventoryItemAttributeModel createInventoryItemAttribute(InventoryItemAttributeModel resource)
            throws ServiceException, ConstraintViolationException {

        Set<ConstraintViolation<InventoryItemAttributeModel>> violations = validator.validate(resource);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        InventoryItemAttributeModel result = null;
        String userId = resource.getUserEmail();

        try {

            InventoryItemAttributeEntity dto = new InventoryItemAttributeEntity();

            inventoryItemAttributeResourceAssembler.updateInventoryItemAttribute(resource, dto);
            inventoryItemAttributeMapper.insertInventoryItemAttribute(dto, userId);

            dto = inventoryItemAttributeMapper.fetch(dto.getInventoryItemAttributeId());
            result = inventoryItemAttributeResourceAssembler.getInventoryItemAttribute(dto);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public InventoryItemAttributeModel updateInventoryItemAttribute(Long inventoryItemAttributeId,
            InventoryItemAttributeModel resource)
            throws ServiceException, ConstraintViolationException, NotFoundException {

        Set<ConstraintViolation<InventoryItemAttributeModel>> violations = validator.validate(resource);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        InventoryItemAttributeModel result = null;
        String userId = resource.getUserEmail();

        try {

            InventoryItemAttributeEntity dto = inventoryItemAttributeMapper.fetch(inventoryItemAttributeId);

            if (dto == null) {
                throw new NotFoundException("Did not find the inventory item attribute: " + inventoryItemAttributeId);
            }

            inventoryItemAttributeResourceAssembler.updateInventoryItemAttribute(resource, dto);
            inventoryItemAttributeMapper.updateInventoryItemAttribute(dto, userId);

            dto = inventoryItemAttributeMapper.fetch(dto.getInventoryItemAttributeId());
            result = inventoryItemAttributeResourceAssembler.getInventoryItemAttribute(dto);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public void deleteInventoryItemAttribute(Long inventoryItemAttributeId) throws ServiceException, NotFoundException {

        try {
            InventoryItemAttributeEntity entity = inventoryItemAttributeMapper.fetch(inventoryItemAttributeId);

            if (entity == null) {
                throw new NotFoundException("Did not find the inventory item attribute: " + inventoryItemAttributeId);
            }

            inventoryItemAttributeMapper.deleteInventoryItemAttribute(inventoryItemAttributeId);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }
    }
}
