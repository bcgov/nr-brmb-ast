package ca.bc.gov.farms.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.farms.model.v1.MarketRatePremiumList;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.MARKET_RATE_PREMIUM_LIST_NAME)
@XmlSeeAlso({ MarketRatePremiumListRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class MarketRatePremiumListRsrc extends BaseResource implements MarketRatePremiumList<MarketRatePremiumRsrc> {

    private static final long serialVersionUID = 1L;

    private List<MarketRatePremiumRsrc> marketRatePremiumList;

    public MarketRatePremiumListRsrc() {
        this.marketRatePremiumList = new ArrayList<>();
    }

    @Override
    public List<MarketRatePremiumRsrc> getMarketRatePremiumList() {
        return marketRatePremiumList;
    }

    @Override
    public void setMarketRatePremiumList(List<MarketRatePremiumRsrc> marketRatePremiumList) {
        this.marketRatePremiumList = marketRatePremiumList;
    }

}
