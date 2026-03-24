package ca.bc.gov.farms.service.api.v1.model.factory;

import java.util.List;

import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.MarketRatePremium;
import ca.bc.gov.farms.model.v1.MarketRatePremiumList;
import ca.bc.gov.farms.persistence.v1.dto.MarketRatePremiumDto;

public interface MarketRatePremiumFactory {

    MarketRatePremium getMarketRatePremium(MarketRatePremiumDto dto, FactoryContext context);

    MarketRatePremiumList<? extends MarketRatePremium> getMarketRatePremiumList(
            List<MarketRatePremiumDto> dtos, FactoryContext context);

    void updateMarketRatePremium(MarketRatePremiumDto dto, MarketRatePremium model);
}
