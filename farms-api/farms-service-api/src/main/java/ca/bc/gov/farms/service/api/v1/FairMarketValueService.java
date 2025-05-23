package ca.bc.gov.farms.service.api.v1;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.brmb.common.service.api.ValidationFailureException;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.FairMarketValue;
import ca.bc.gov.farms.model.v1.FairMarketValueList;

public interface FairMarketValueService {

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    FairMarketValueList<? extends FairMarketValue> getFairMarketValuesByProgramYear(Integer programYear,
            FactoryContext factoryContext) throws ServiceException;

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    FairMarketValue getFairMarketValue(String fairMarketValueId, FactoryContext factoryContext)
            throws ServiceException, NotFoundException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    FairMarketValue createFairMarketValue(FairMarketValue fairMarketValue, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    FairMarketValue updateFairMarketValue(String fairMarketValueId, FairMarketValue fairMarketValue,
            FactoryContext factoryContext) throws ServiceException, ValidationFailureException, NotFoundException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    void deleteFairMarketValue(String fairMarketValueId) throws ServiceException, NotFoundException;
}
