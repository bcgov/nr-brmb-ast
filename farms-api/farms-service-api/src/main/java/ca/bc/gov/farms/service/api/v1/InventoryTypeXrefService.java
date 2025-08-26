package ca.bc.gov.farms.service.api.v1;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.brmb.common.service.api.ValidationFailureException;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.InventoryTypeXref;
import ca.bc.gov.farms.model.v1.InventoryTypeXrefList;

public interface InventoryTypeXrefService {

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    InventoryTypeXrefList<? extends InventoryTypeXref> getInventoryTypeXrefsByInventoryClassCode(String inventoryClassCode, FactoryContext factoryContext)
            throws ServiceException;

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    InventoryTypeXref getInventoryTypeXref(Long agristabilityCommodityXrefId, FactoryContext factoryContext)
            throws ServiceException, NotFoundException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    InventoryTypeXref createInventoryTypeXref(InventoryTypeXref inventoryTypeXref, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    InventoryTypeXref updateInventoryTypeXref(Long agristabilityCommodityXrefId, InventoryTypeXref inventoryTypeXref, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException, NotFoundException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    void deleteInventoryTypeXref(Long agristabilityCommodityXrefId)
            throws ServiceException, NotFoundException;
}
