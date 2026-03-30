package ca.bc.gov.farms.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.farms.data.assemblers.StructureGroupAttributeResourceAssembler;
import ca.bc.gov.farms.data.entities.StructureGroupAttributeEntity;
import ca.bc.gov.farms.data.mappers.StructureGroupAttributeMapper;
import ca.bc.gov.farms.data.models.StructureGroupAttributeModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StructureGroupAttributeService {

    @Autowired
    private StructureGroupAttributeMapper structureGroupAttributeMapper;

    @Autowired
    private StructureGroupAttributeResourceAssembler structureGroupAttributeResourceAssembler;

    @Autowired
    private Validator validator;

    public StructureGroupAttributeModel getStructureGroupAttributesByStructureGroupCode(String structureGroupCode)
            throws ServiceException {

        StructureGroupAttributeModel result = null;

        try {
            StructureGroupAttributeEntity entity = structureGroupAttributeMapper
                    .fetchByStructureGroupCode(structureGroupCode);

            result = structureGroupAttributeResourceAssembler.getStructureGroupAttribute(entity);
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    public StructureGroupAttributeModel getStructureGroupAttribute(Long structureGroupAttributeId)
            throws ServiceException, NotFoundException {

        StructureGroupAttributeModel result = null;

        try {
            StructureGroupAttributeEntity entity = structureGroupAttributeMapper.fetch(structureGroupAttributeId);

            if (entity == null) {
                throw new NotFoundException("Did not find the structure group attribute: " + structureGroupAttributeId);
            }

            result = structureGroupAttributeResourceAssembler.getStructureGroupAttribute(entity);
        } catch (NotFoundException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public StructureGroupAttributeModel createStructureGroupAttribute(StructureGroupAttributeModel resource)
            throws ServiceException, ConstraintViolationException {

        Set<ConstraintViolation<StructureGroupAttributeModel>> violations = validator.validate(resource);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        StructureGroupAttributeModel result = null;
        String userId = resource.getUserEmail();

        try {

            StructureGroupAttributeEntity dto = new StructureGroupAttributeEntity();

            structureGroupAttributeResourceAssembler.updateStructureGroupAttribute(resource, dto);
            int count = structureGroupAttributeMapper.insertStructureGroupAttribute(dto, userId);
            if (count == 0) {
                throw new ServiceException("Record not inserted: " + count);
            }

            dto = structureGroupAttributeMapper.fetch(dto.getStructureGroupAttributeId());
            result = structureGroupAttributeResourceAssembler.getStructureGroupAttribute(dto);
        } catch (ServiceException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public StructureGroupAttributeModel updateStructureGroupAttribute(Long structureGroupAttributeId,
            StructureGroupAttributeModel resource)
            throws ServiceException, ConstraintViolationException, NotFoundException {

        Set<ConstraintViolation<StructureGroupAttributeModel>> violations = validator.validate(resource);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        StructureGroupAttributeModel result = null;
        String userId = resource.getUserEmail();

        try {

            StructureGroupAttributeEntity dto = structureGroupAttributeMapper.fetch(structureGroupAttributeId);

            if (dto == null) {
                throw new NotFoundException("Did not find the structure group attribute: " + structureGroupAttributeId);
            }

            structureGroupAttributeResourceAssembler.updateStructureGroupAttribute(resource, dto);
            int count = structureGroupAttributeMapper.updateStructureGroupAttribute(dto, userId);
            if (count == 0) {
                throw new ServiceException("Record not updated: " + count);
            }

            dto = structureGroupAttributeMapper.fetch(dto.getStructureGroupAttributeId());
            result = structureGroupAttributeResourceAssembler.getStructureGroupAttribute(dto);
        } catch (ServiceException | NotFoundException ex) {
            throw ex;
        } catch (Throwable t) {
            throw new ServiceException("Mapper threw an exception", t);
        }

        return result;
    }

    @Transactional
    public void deleteStructureGroupAttribute(Long structureGroupAttributeId)
            throws ServiceException, NotFoundException {

        try {
            StructureGroupAttributeEntity entity = structureGroupAttributeMapper.fetch(structureGroupAttributeId);

            if (entity == null) {
                throw new NotFoundException("Did not find the structure group attribute: " + structureGroupAttributeId);
            }

            int count = structureGroupAttributeMapper.deleteStructureGroupAttribute(structureGroupAttributeId);
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
