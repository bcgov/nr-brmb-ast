package ca.bc.gov.farms.data.assemblers;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import ca.bc.gov.farms.data.entities.InventoryItemDetailEntity;
import ca.bc.gov.farms.data.models.InventoryItemDetailListRsrc;
import ca.bc.gov.farms.data.models.InventoryItemDetailRsrc;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InventoryItemDetailResourceAssembler extends BaseResourceAssembler {

    public InventoryItemDetailRsrc getInventoryItemDetail(@NonNull InventoryItemDetailEntity entity) {

        URI baseUri = getBaseURI();

        InventoryItemDetailRsrc resource = new InventoryItemDetailRsrc();

        BeanUtils.copyProperties(entity, resource);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getInventoryItemDetailId(), resource, baseUri);

        return resource;
    }

    public InventoryItemDetailListRsrc getInventoryItemDetailList(List<InventoryItemDetailEntity> entities) {

        URI baseUri = getBaseURI();

        InventoryItemDetailListRsrc result = null;

        @SuppressWarnings("null")
        List<InventoryItemDetailRsrc> resources = entities.stream().filter(Objects::nonNull).map(entity -> {
            InventoryItemDetailRsrc resource = new InventoryItemDetailRsrc();
            BeanUtils.copyProperties(entity, resource);
            setSelfLink(entity.getInventoryItemDetailId(), resource, baseUri);
            return resource;
        }).collect(Collectors.toList());

        result = new InventoryItemDetailListRsrc();
        result.setInventoryItemDetailList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    public void updateInventoryItemDetail(@NonNull InventoryItemDetailRsrc resource,
            @NonNull InventoryItemDetailEntity entity) {
        BeanUtils.copyProperties(resource, entity);
    }
}
