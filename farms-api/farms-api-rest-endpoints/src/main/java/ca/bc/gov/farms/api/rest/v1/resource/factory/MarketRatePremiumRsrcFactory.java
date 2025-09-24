package ca.bc.gov.farms.api.rest.v1.resource.factory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import ca.bc.gov.brmb.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.brmb.common.rest.resource.RelLink;
import ca.bc.gov.brmb.common.rest.resource.types.BaseResourceTypes;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.api.rest.v1.endpoints.MarketRatePremiumEndpoints;
import ca.bc.gov.farms.api.rest.v1.resource.MarketRatePremiumListRsrc;
import ca.bc.gov.farms.api.rest.v1.resource.MarketRatePremiumRsrc;
import ca.bc.gov.farms.model.v1.MarketRatePremium;
import ca.bc.gov.farms.model.v1.MarketRatePremiumList;
import ca.bc.gov.farms.persistence.v1.dto.MarketRatePremiumDto;
import ca.bc.gov.farms.service.api.v1.model.factory.MarketRatePremiumFactory;

public class MarketRatePremiumRsrcFactory extends BaseResourceFactory implements MarketRatePremiumFactory {

    @Override
    public MarketRatePremium getMarketRatePremium(MarketRatePremiumDto dto, FactoryContext context) {

        URI baseUri = getBaseURI(context);

        MarketRatePremiumRsrc resource = new MarketRatePremiumRsrc();

        populateDefaultResource(resource, dto);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getMarketRatePremiumId(), resource, baseUri);

        return resource;
    }

    private void populateDefaultResource(MarketRatePremiumRsrc resource, MarketRatePremiumDto dto) {
        resource.setMarketRatePremiumId(dto.getMarketRatePremiumId());
        resource.setMinTotalPremiumAmount(dto.getMinTotalPremiumAmount());
        resource.setMaxTotalPremiumAmount(dto.getMaxTotalPremiumAmount());
        resource.setRiskChargeFlatAmount(dto.getRiskChargeFlatAmount());
        resource.setRiskChargePctPremium(dto.getRiskChargePctPremium());
        resource.setAdjustChargeFlatAmount(dto.getAdjustChargeFlatAmount());
    }

    @Override
    public MarketRatePremiumList<? extends MarketRatePremium> getMarketRatePremiumList(
            List<MarketRatePremiumDto> dtos,
            FactoryContext context) {

        URI baseUri = getBaseURI(context);

        MarketRatePremiumListRsrc result = null;

        List<MarketRatePremiumRsrc> resources = new ArrayList<>();

        for (MarketRatePremiumDto dto : dtos) {
            MarketRatePremiumRsrc resource = populate(dto);
            setSelfLink(dto.getMarketRatePremiumId(), resource, baseUri);
            resources.add(resource);
        }

        result = new MarketRatePremiumListRsrc();
        result.setMarketRatePremiumList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    static MarketRatePremiumRsrc populate(MarketRatePremiumDto dto) {

        MarketRatePremiumRsrc result = new MarketRatePremiumRsrc();

        result.setMarketRatePremiumId(dto.getMarketRatePremiumId());
        result.setMinTotalPremiumAmount(dto.getMinTotalPremiumAmount());
        result.setMaxTotalPremiumAmount(dto.getMaxTotalPremiumAmount());
        result.setRiskChargeFlatAmount(dto.getRiskChargeFlatAmount());
        result.setRiskChargePctPremium(dto.getRiskChargePctPremium());
        result.setAdjustChargeFlatAmount(dto.getAdjustChargeFlatAmount());

        return result;
    }

    public static void setSelfLink(Long marketRatePremiumId, MarketRatePremiumRsrc resource, URI baseUri) {

        String selfUri = getMarketRatePremiumSelfUri(marketRatePremiumId, baseUri);

        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }

    public static String getMarketRatePremiumSelfUri(Long marketRatePremiumId, URI baseUri) {

        String result = UriBuilder.fromUri(baseUri)
                .path(MarketRatePremiumEndpoints.class)
                .build(marketRatePremiumId).toString();

        return result;
    }

    public static void setSelfLink(MarketRatePremiumListRsrc resource, URI baseUri) {

        String selfUri = UriBuilder.fromUri(baseUri)
                .path(MarketRatePremiumEndpoints.class)
                .build().toString();

        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }

    @Override
    public void updateMarketRatePremium(MarketRatePremiumDto dto, MarketRatePremium model) {
        dto.setMarketRatePremiumId(model.getMarketRatePremiumId());
        dto.setMinTotalPremiumAmount(model.getMinTotalPremiumAmount());
        dto.setMaxTotalPremiumAmount(model.getMaxTotalPremiumAmount());
        dto.setRiskChargeFlatAmount(model.getRiskChargeFlatAmount());
        dto.setRiskChargePctPremium(model.getRiskChargePctPremium());
        dto.setAdjustChargeFlatAmount(model.getAdjustChargeFlatAmount());
    }
}
