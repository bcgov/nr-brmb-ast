package ca.bc.gov.farms.data.assemblers;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import ca.bc.gov.farms.data.entities.FruitVegTypeDetailEntity;
import ca.bc.gov.farms.data.models.FruitVegTypeDetailListRsrc;
import ca.bc.gov.farms.data.models.FruitVegTypeDetailModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FruitVegTypeDetailResourceAssembler extends BaseResourceAssembler {

    public FruitVegTypeDetailModel getFruitVegTypeDetail(@NonNull FruitVegTypeDetailEntity entity) {

        URI baseUri = getBaseURI();

        FruitVegTypeDetailModel resource = new FruitVegTypeDetailModel();

        BeanUtils.copyProperties(entity, resource);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getFruitVegTypeCode(), resource, baseUri);

        return resource;
    }

    public FruitVegTypeDetailListRsrc getFruitVegTypeDetailList(List<FruitVegTypeDetailEntity> entities) {

        URI baseUri = getBaseURI();

        FruitVegTypeDetailListRsrc result = null;

        @SuppressWarnings("null")
        List<FruitVegTypeDetailModel> resources = entities.stream().filter(Objects::nonNull).map(entity -> {
            FruitVegTypeDetailModel resource = new FruitVegTypeDetailModel();
            BeanUtils.copyProperties(entity, resource);
            setSelfLink(entity.getFruitVegTypeCode(), resource, baseUri);
            return resource;
        }).collect(Collectors.toList());

        result = new FruitVegTypeDetailListRsrc();
        result.setFruitVegTypeDetailList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    public void updateFruitVegTypeDetail(@NonNull FruitVegTypeDetailModel resource,
            @NonNull FruitVegTypeDetailEntity entity) {
        BeanUtils.copyProperties(resource, entity);
    }
}
