package ca.bc.gov.farms.services;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.farms.data.assemblers.InventoryTypeXrefResourceAssembler;
import ca.bc.gov.farms.data.entities.InventoryTypeXrefEntity;
import ca.bc.gov.farms.data.mappers.InventoryTypeXrefMapper;
import ca.bc.gov.farms.data.models.InventoryTypeXrefListRsrc;
import ca.bc.gov.farms.data.models.InventoryTypeXrefModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InventoryTypeXrefService {

    @Autowired
    private InventoryTypeXrefMapper inventoryTypeXrefMapper;

    @Autowired
    private InventoryTypeXrefResourceAssembler inventoryTypeXrefResourceAssembler;

    @Autowired
    private Validator validator;

    public InventoryTypeXrefListRsrc getInventoryTypeXrefsByInventoryClassCode(String inventoryClassCode)
            throws ServiceException {

        InventoryTypeXrefListRsrc result = null;

        try {
            List<InventoryTypeXrefEntity> entities = inventoryTypeXrefMapper
                    .fetchByInventoryClassCode(inventoryClassCode);

            result = inventoryTypeXrefResourceAssembler.getInventoryTypeXrefList(entities);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    public InventoryTypeXrefModel getInventoryTypeXref(Long agristabilityCommodityXrefId)
            throws ServiceException, NotFoundException {

        InventoryTypeXrefModel result = null;

        try {
            InventoryTypeXrefEntity entity = inventoryTypeXrefMapper.fetch(agristabilityCommodityXrefId);

            if (entity == null) {
                throw new NotFoundException("Did not find the inventory type xref: " + agristabilityCommodityXrefId);
            }

            result = inventoryTypeXrefResourceAssembler.getInventoryTypeXref(entity);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public InventoryTypeXrefModel createInventoryTypeXref(InventoryTypeXrefModel resource)
            throws ServiceException, ConstraintViolationException {

        Set<ConstraintViolation<InventoryTypeXrefModel>> violations = validator.validate(resource);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        InventoryTypeXrefModel result = null;
        String userId = resource.getUserEmail();

        try {

            InventoryTypeXrefEntity dto = new InventoryTypeXrefEntity();

            inventoryTypeXrefResourceAssembler.updateInventoryTypeXref(resource, dto);
            int count = inventoryTypeXrefMapper.insertInventoryTypeXref(dto, userId);
            if (count == 0) {
                throw new ServiceException("Record not inserted: " + count);
            }

            dto = inventoryTypeXrefMapper.fetch(dto.getAgristabilityCommodityXrefId());
            result = inventoryTypeXrefResourceAssembler.getInventoryTypeXref(dto);
        } catch (ServiceException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public InventoryTypeXrefModel updateInventoryTypeXref(Long agristabilityCommodityXrefId,
            InventoryTypeXrefModel resource) throws ServiceException, ConstraintViolationException, NotFoundException {

        Set<ConstraintViolation<InventoryTypeXrefModel>> violations = validator.validate(resource);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        InventoryTypeXrefModel result = null;
        String userId = resource.getUserEmail();

        try {

            InventoryTypeXrefEntity dto = inventoryTypeXrefMapper.fetch(agristabilityCommodityXrefId);

            if (dto == null) {
                throw new NotFoundException("Did not find the inventory type xref: " + agristabilityCommodityXrefId);
            }

            inventoryTypeXrefResourceAssembler.updateInventoryTypeXref(resource, dto);
            int count = inventoryTypeXrefMapper.updateInventoryTypeXref(dto, userId);
            if (count == 0) {
                throw new ServiceException("Record not updated: " + count);
            }

            dto = inventoryTypeXrefMapper.fetch(dto.getAgristabilityCommodityXrefId());
            result = inventoryTypeXrefResourceAssembler.getInventoryTypeXref(dto);
        } catch (ServiceException | NotFoundException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public void deleteInventoryTypeXref(Long agristabilityCommodityXrefId) throws ServiceException, NotFoundException {

        try {
            InventoryTypeXrefEntity entity = inventoryTypeXrefMapper.fetch(agristabilityCommodityXrefId);

            if (entity == null) {
                throw new NotFoundException("Did not find the inventory type xref: " + agristabilityCommodityXrefId);
            }

            int count = inventoryTypeXrefMapper.deleteInventoryTypeXref(agristabilityCommodityXrefId);
            if (count == 0) {
                throw new ServiceException("Record not deleted: " + count);
            }
        } catch (ServiceException | NotFoundException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }
    }
}
