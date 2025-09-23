package ca.bc.gov.farms.service.api.v1;

import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.brmb.common.service.api.ValidationFailureException;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.MarketRatePremium;
import ca.bc.gov.farms.model.v1.MarketRatePremiumList;

public interface MarketRatePremiumService {

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    MarketRatePremiumList<? extends MarketRatePremium> getAllMarketRatePremiums(FactoryContext factoryContext)
            throws ServiceException;

    @Transactional(readOnly = true, rollbackFor = Exception.class)
    MarketRatePremium getMarketRatePremium(Long marketRatePremiumId, FactoryContext factoryContext)
            throws ServiceException, NotFoundException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    MarketRatePremium createMarketRatePremium(MarketRatePremium marketRatePremium,
            FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    MarketRatePremium updateMarketRatePremium(Long marketRatePremiumId,
            MarketRatePremium marketRatePremium, FactoryContext factoryContext)
            throws ServiceException, ValidationFailureException, NotFoundException;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    void deleteMarketRatePremium(Long marketRatePremiumId)
            throws ServiceException, NotFoundException;
}
