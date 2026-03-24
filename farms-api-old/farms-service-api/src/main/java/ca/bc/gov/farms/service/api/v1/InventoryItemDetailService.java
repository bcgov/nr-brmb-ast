package ca.bc.gov.farms.service.api.v1;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.brmb.common.service.api.ValidationFailureException;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.InventoryItemDetail;
import ca.bc.gov.farms.model.v1.InventoryItemDetailList;

public interface InventoryItemDetailService {

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    InventoryItemDetailList<? extends InventoryItemDetail> getInventoryItemDetailsByInventoryItemCode(
            String inventoryItemCode, FactoryContext factoryContext) throws ServiceException;

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    InventoryItemDetail getInventoryItemDetail(Long inventoryItemDetailId, FactoryContext factoryContext)
            throws ServiceException, NotFoundException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    InventoryItemDetail createInventoryItemDetail(InventoryItemDetail inventoryItemDetail,
            FactoryContext factoryContext) throws ServiceException, ValidationFailureException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    InventoryItemDetail updateInventoryItemDetail(Long inventoryItemDetailId, InventoryItemDetail inventoryItemDetail,
            FactoryContext factoryContext) throws ServiceException, ValidationFailureException, NotFoundException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    void deleteInventoryItemDetail(Long inventoryItemDetailId) throws ServiceException, NotFoundException;
}
