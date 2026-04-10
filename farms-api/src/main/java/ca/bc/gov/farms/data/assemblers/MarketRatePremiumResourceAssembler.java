package ca.bc.gov.farms.data.assemblers;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import ca.bc.gov.farms.data.entities.MarketRatePremiumEntity;
import ca.bc.gov.farms.data.models.MarketRatePremiumListRsrc;
import ca.bc.gov.farms.data.models.MarketRatePremiumModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MarketRatePremiumResourceAssembler extends BaseResourceAssembler {

    public MarketRatePremiumModel getMarketRatePremium(@NonNull MarketRatePremiumEntity entity) {

        URI baseUri = getBaseURI();

        MarketRatePremiumModel resource = new MarketRatePremiumModel();

        BeanUtils.copyProperties(entity, resource);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getMarketRatePremiumId(), resource, baseUri);

        return resource;
    }

    public MarketRatePremiumListRsrc getMarketRatePremiumList(List<MarketRatePremiumEntity> entities) {

        URI baseUri = getBaseURI();

        MarketRatePremiumListRsrc result = null;

        @SuppressWarnings("null")
        List<MarketRatePremiumModel> resources = entities.stream().filter(Objects::nonNull).map(entity -> {
            MarketRatePremiumModel resource = new MarketRatePremiumModel();
            BeanUtils.copyProperties(entity, resource);
            setSelfLink(entity.getMarketRatePremiumId(), resource, baseUri);
            return resource;
        }).collect(Collectors.toList());

        result = new MarketRatePremiumListRsrc();
        result.setMarketRatePremiumList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    public void updateMarketRatePremium(@NonNull MarketRatePremiumModel resource,
            @NonNull MarketRatePremiumEntity entity) {
        BeanUtils.copyProperties(resource, entity);
    }
}
