package ca.bc.gov.farms.data.assemblers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import ca.bc.gov.farms.data.entities.InventoryTypeXrefEntity;
import ca.bc.gov.farms.data.models.InventoryTypeXrefListModel;
import ca.bc.gov.farms.data.models.InventoryTypeXrefModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InventoryTypeXrefResourceAssembler extends BaseResourceAssembler {

    @SuppressWarnings("null")
    public InventoryTypeXrefModel getInventoryTypeXref(InventoryTypeXrefEntity entity) {

        URI baseUri = getBaseURI();

        InventoryTypeXrefModel resource = new InventoryTypeXrefModel();

        BeanUtils.copyProperties(entity, resource);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getAgristabilityCommodityXrefId(), resource, baseUri);

        return resource;
    }

    @SuppressWarnings("null")
    public InventoryTypeXrefListModel getInventoryTypeXrefList(List<InventoryTypeXrefEntity> entities) {

        URI baseUri = getBaseURI();

        InventoryTypeXrefListModel result = null;

        List<InventoryTypeXrefModel> resources = new ArrayList<>();

        for (InventoryTypeXrefEntity entity : entities) {
            InventoryTypeXrefModel resource = new InventoryTypeXrefModel();
            BeanUtils.copyProperties(entity, resource);
            setSelfLink(entity.getAgristabilityCommodityXrefId(), resource, baseUri);
            resources.add(resource);
        }

        result = new InventoryTypeXrefListModel();
        result.setInventoryTypeXrefList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    public void updateInventoryTypeXref(InventoryTypeXrefModel resource, InventoryTypeXrefEntity entity) {
        BeanUtils.copyProperties(resource, entity);
    }
}
