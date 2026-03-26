package ca.bc.gov.farms.data.assemblers;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import ca.bc.gov.farms.data.entities.InventoryItemAttributeEntity;
import ca.bc.gov.farms.data.models.InventoryItemAttributeListModel;
import ca.bc.gov.farms.data.models.InventoryItemAttributeModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InventoryItemAttributeResourceAssembler extends BaseResourceAssembler {

    public InventoryItemAttributeModel getInventoryItemAttribute(@NonNull InventoryItemAttributeEntity entity) {

        URI baseUri = getBaseURI();

        InventoryItemAttributeModel resource = new InventoryItemAttributeModel();

        BeanUtils.copyProperties(entity, resource);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getInventoryItemAttributeId(), resource, baseUri);

        return resource;
    }

    public InventoryItemAttributeListModel getInventoryItemAttributeList(List<InventoryItemAttributeEntity> entities) {

        URI baseUri = getBaseURI();

        InventoryItemAttributeListModel result = null;

        @SuppressWarnings("null")
        List<InventoryItemAttributeModel> resources = entities.stream().filter(Objects::nonNull).map(entity -> {
            InventoryItemAttributeModel resource = new InventoryItemAttributeModel();
            BeanUtils.copyProperties(entity, resource);
            setSelfLink(entity.getInventoryItemAttributeId(), resource, baseUri);
            return resource;
        }).collect(Collectors.toList());

        result = new InventoryItemAttributeListModel();
        result.setInventoryItemAttributeList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    public void updateInventoryItemAttribute(@NonNull InventoryItemAttributeModel resource,
            @NonNull InventoryItemAttributeEntity entity) {
        BeanUtils.copyProperties(resource, entity);
    }
}
