package ca.bc.gov.farms.service.api.v1;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.ProductiveUnitCode;
import ca.bc.gov.farms.model.v1.ProductiveUnitCodeList;

public interface ProductiveUnitCodeService {

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    ProductiveUnitCodeList<? extends ProductiveUnitCode> getAllProductiveUnitCodes(FactoryContext factoryContext)
            throws ServiceException;
}
