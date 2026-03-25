package ca.bc.gov.farms.data.assemblers;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import ca.bc.gov.farms.data.entities.FairMarketValueEntity;
import ca.bc.gov.farms.data.models.FairMarketValueListModel;
import ca.bc.gov.farms.data.models.FairMarketValueModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FairMarketValueResourceAssembler extends BaseResourceAssembler {

    public FairMarketValueModel getFairMarketValue(@NonNull FairMarketValueEntity entity) {

        URI baseUri = getBaseURI();

        FairMarketValueModel resource = new FairMarketValueModel();

        BeanUtils.copyProperties(entity, resource);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getFairMarketValueId(), resource, baseUri);

        return resource;
    }

    @SuppressWarnings("null")
    public FairMarketValueListModel getFairMarketValueList(List<FairMarketValueEntity> entities) {

        URI baseUri = getBaseURI();

        FairMarketValueListModel result = null;

        List<FairMarketValueModel> resources = entities.stream().filter(Objects::nonNull).map(entity -> {
            FairMarketValueModel resource = new FairMarketValueModel();
            BeanUtils.copyProperties(entity, resource);
            setSelfLink(entity.getFairMarketValueId(), resource, baseUri);
            return resource;
        }).collect(Collectors.toList());

        result = new FairMarketValueListModel();
        result.setFairMarketValueList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    public void updateFairMarketValue(@NonNull FairMarketValueModel resource, @NonNull FairMarketValueEntity entity) {
        BeanUtils.copyProperties(resource, entity);
    }
}
