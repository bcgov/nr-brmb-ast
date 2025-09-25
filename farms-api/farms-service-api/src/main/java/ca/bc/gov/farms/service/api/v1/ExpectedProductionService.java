package ca.bc.gov.farms.service.api.v1;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.brmb.common.service.api.ValidationFailureException;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.ExpectedProduction;

public interface ExpectedProductionService {

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    ExpectedProduction getExpectedProductionsByInventoryItemCode(String inventoryItemCode,
            FactoryContext factoryContext) throws ServiceException;

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    ExpectedProduction getExpectedProduction(Long expectedProductionId, FactoryContext factoryContext)
            throws ServiceException, NotFoundException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    ExpectedProduction createExpectedProduction(ExpectedProduction expectedProduction,
            FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    ExpectedProduction updateExpectedProduction(Long expectedProductionId,
            ExpectedProduction expectedProduction, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException, NotFoundException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    void deleteExpectedProduction(Long expectedProductionId)
            throws ServiceException, NotFoundException;
}
