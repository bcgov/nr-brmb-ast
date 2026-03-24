package ca.bc.gov.farms.data.assemblers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import ca.bc.gov.farms.data.entities.FairMarketValueEntity;
import ca.bc.gov.farms.data.models.FairMarketValueListModel;
import ca.bc.gov.farms.data.models.FairMarketValueModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FairMarketValueResourceAssembler extends BaseResourceAssembler {

    @SuppressWarnings("null")
    public FairMarketValueModel getFairMarketValue(FairMarketValueEntity entity) {

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

        List<FairMarketValueModel> resources = new ArrayList<>();

        for (FairMarketValueEntity entity : entities) {
            FairMarketValueModel resource = new FairMarketValueModel();
            BeanUtils.copyProperties(entity, resources);
            setSelfLink(entity.getFairMarketValueId(), resource, baseUri);
            resources.add(resource);
        }

        result = new FairMarketValueListModel();
        result.setFairMarketValueList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    public void updateFairMarketValue(FairMarketValueModel resource, FairMarketValueEntity entity) {
        BeanUtils.copyProperties(resource, entity);
    }
}
