package ca.bc.gov.farms.service.api.v1;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.brmb.common.service.api.ValidationFailureException;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.StructureGroupAttribute;

public interface StructureGroupAttributeService {

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    StructureGroupAttribute getStructureGroupAttributesByStructureGroupCode(String structureGroupCode,
            FactoryContext factoryContext) throws ServiceException;

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    StructureGroupAttribute getStructureGroupAttribute(Long structureGroupAttributeId, FactoryContext factoryContext)
            throws ServiceException, NotFoundException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    StructureGroupAttribute createStructureGroupAttribute(StructureGroupAttribute structureGroupAttribute,
            FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    StructureGroupAttribute updateStructureGroupAttribute(Long structureGroupAttributeId,
            StructureGroupAttribute structureGroupAttribute, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException, NotFoundException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    void deleteStructureGroupAttribute(Long structureGroupAttributeId)
            throws ServiceException, NotFoundException;
}
