package ca.bc.gov.farms.data.assemblers;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import ca.bc.gov.farms.data.entities.InventoryTypeXrefEntity;
import ca.bc.gov.farms.data.models.InventoryTypeXrefListRsrc;
import ca.bc.gov.farms.data.models.InventoryTypeXrefModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InventoryTypeXrefResourceAssembler extends BaseResourceAssembler {

    public InventoryTypeXrefModel getInventoryTypeXref(@NonNull InventoryTypeXrefEntity entity) {

        URI baseUri = getBaseURI();

        InventoryTypeXrefModel resource = new InventoryTypeXrefModel();

        BeanUtils.copyProperties(entity, resource);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getAgristabilityCommodityXrefId(), resource, baseUri);

        return resource;
    }

    public InventoryTypeXrefListRsrc getInventoryTypeXrefList(List<InventoryTypeXrefEntity> entities) {

        URI baseUri = getBaseURI();

        InventoryTypeXrefListRsrc result = null;

        @SuppressWarnings("null")
        List<InventoryTypeXrefModel> resources = entities.stream().filter(Objects::nonNull).map(entity -> {
            InventoryTypeXrefModel resource = new InventoryTypeXrefModel();
            BeanUtils.copyProperties(entity, resource);
            setSelfLink(entity.getAgristabilityCommodityXrefId(), resource, baseUri);
            return resource;
        }).collect(Collectors.toList());

        result = new InventoryTypeXrefListRsrc();
        result.setInventoryTypeXrefList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    public void updateInventoryTypeXref(@NonNull InventoryTypeXrefModel resource,
            @NonNull InventoryTypeXrefEntity entity) {
        BeanUtils.copyProperties(resource, entity);
    }
}
