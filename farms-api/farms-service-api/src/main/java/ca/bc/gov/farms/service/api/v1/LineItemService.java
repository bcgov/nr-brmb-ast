package ca.bc.gov.farms.service.api.v1;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.brmb.common.service.api.ValidationFailureException;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.LineItem;
import ca.bc.gov.farms.model.v1.LineItemList;

public interface LineItemService {

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    LineItemList<? extends LineItem> getLineItemsByProgramYear(Integer programYear, FactoryContext factoryContext)
            throws ServiceException;

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    LineItem getLineItem(Long lineItemId, FactoryContext factoryContext) throws ServiceException, NotFoundException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    LineItem createLineItem(LineItem lineItem, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    LineItem updateLineItem(Long lineItemId, LineItem lineItem, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException, NotFoundException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    void deleteLineItem(Long lineItemId) throws ServiceException, NotFoundException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    LineItemList<? extends LineItem> copyLineItems(Integer currentYear, FactoryContext factoryContext)
            throws ServiceException;
}
