package ca.bc.gov.farms.data.assemblers;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import ca.bc.gov.farms.data.entities.InventoryItemAttributeEntity;
import ca.bc.gov.farms.data.models.InventoryItemAttributeListRsrc;
import ca.bc.gov.farms.data.models.InventoryItemAttributeRsrc;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InventoryItemAttributeResourceAssembler extends BaseResourceAssembler {

    public InventoryItemAttributeRsrc getInventoryItemAttribute(@NonNull InventoryItemAttributeEntity entity) {

        URI baseUri = getBaseURI();

        InventoryItemAttributeRsrc resource = new InventoryItemAttributeRsrc();

        BeanUtils.copyProperties(entity, resource);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getInventoryItemAttributeId(), resource, baseUri);

        return resource;
    }

    public InventoryItemAttributeListRsrc getInventoryItemAttributeList(List<InventoryItemAttributeEntity> entities) {

        URI baseUri = getBaseURI();

        InventoryItemAttributeListRsrc result = null;

        @SuppressWarnings("null")
        List<InventoryItemAttributeRsrc> resources = entities.stream().filter(Objects::nonNull).map(entity -> {
            InventoryItemAttributeRsrc resource = new InventoryItemAttributeRsrc();
            BeanUtils.copyProperties(entity, resource);
            setSelfLink(entity.getInventoryItemAttributeId(), resource, baseUri);
            return resource;
        }).collect(Collectors.toList());

        result = new InventoryItemAttributeListRsrc();
        result.setInventoryItemAttributeList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    public void updateInventoryItemAttribute(@NonNull InventoryItemAttributeRsrc resource,
            @NonNull InventoryItemAttributeEntity entity) {
        BeanUtils.copyProperties(resource, entity);
    }
}
