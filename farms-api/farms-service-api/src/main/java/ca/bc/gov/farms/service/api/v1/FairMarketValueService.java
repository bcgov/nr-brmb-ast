package ca.bc.gov.farms.service.api.v1;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.FairMarketValue;
import ca.bc.gov.farms.model.v1.FairMarketValueList;

public interface FairMarketValueService {

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    FairMarketValueList<? extends FairMarketValue> getFairMarketValuesByProgramYear(Integer programYear,
            FactoryContext factoryContext) throws ServiceException;
}
