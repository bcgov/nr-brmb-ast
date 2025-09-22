package ca.bc.gov.farms.service.api.v1;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.brmb.common.service.api.ValidationFailureException;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.FruitVegTypeDetail;
import ca.bc.gov.farms.model.v1.FruitVegTypeDetailList;

public interface FruitVegTypeDetailService {

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    FruitVegTypeDetailList<? extends FruitVegTypeDetail> getAllFruitVegTypeDetails(FactoryContext factoryContext)
            throws ServiceException;

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    FruitVegTypeDetail getFruitVegTypeDetail(Long fruitVegTypeDetailId, FactoryContext factoryContext)
            throws ServiceException, NotFoundException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    FruitVegTypeDetail createFruitVegTypeDetail(FruitVegTypeDetail fruitVegTypeDetail,
            FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    FruitVegTypeDetail updateFruitVegTypeDetail(Long fruitVegTypeDetailId,
            FruitVegTypeDetail fruitVegTypeDetail, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException, NotFoundException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    void deleteFruitVegTypeDetail(Long fruitVegTypeDetailId)
            throws ServiceException, NotFoundException;
}
