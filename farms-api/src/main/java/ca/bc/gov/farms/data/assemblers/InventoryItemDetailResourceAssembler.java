package ca.bc.gov.farms.data.assemblers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import ca.bc.gov.farms.data.entities.InventoryItemDetailEntity;
import ca.bc.gov.farms.data.models.InventoryItemDetailListModel;
import ca.bc.gov.farms.data.models.InventoryItemDetailModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InventoryItemDetailResourceAssembler extends BaseResourceAssembler {

    @SuppressWarnings("null")
    public InventoryItemDetailModel getInventoryItemDetail(InventoryItemDetailEntity entity) {

        URI baseUri = getBaseURI();

        InventoryItemDetailModel resource = new InventoryItemDetailModel();

        BeanUtils.copyProperties(entity, resource);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getInventoryItemDetailId(), resource, baseUri);

        return resource;
    }

    @SuppressWarnings("null")
    public InventoryItemDetailListModel getInventoryItemDetailList(List<InventoryItemDetailEntity> entities) {

        URI baseUri = getBaseURI();

        InventoryItemDetailListModel result = null;

        List<InventoryItemDetailModel> resources = new ArrayList<>();

        for (InventoryItemDetailEntity entity : entities) {
            InventoryItemDetailModel resource = new InventoryItemDetailModel();
            BeanUtils.copyProperties(entity, resource);
            setSelfLink(entity.getInventoryItemDetailId(), resource, baseUri);
            resources.add(resource);
        }

        result = new InventoryItemDetailListModel();
        result.setInventoryItemDetailList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    public void updateInventoryItemDetail(InventoryItemDetailModel resource, InventoryItemDetailEntity entity) {
        BeanUtils.copyProperties(resource, entity);
    }
}
