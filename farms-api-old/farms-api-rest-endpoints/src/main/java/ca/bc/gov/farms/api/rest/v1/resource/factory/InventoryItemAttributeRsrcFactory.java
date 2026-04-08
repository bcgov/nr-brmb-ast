package ca.bc.gov.farms.api.rest.v1.resource.factory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.core.UriBuilder;

import ca.bc.gov.brmb.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.brmb.common.rest.resource.RelLink;
import ca.bc.gov.brmb.common.rest.resource.types.BaseResourceTypes;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.api.rest.v1.endpoints.InventoryItemAttributeEndpoints;
import ca.bc.gov.farms.api.rest.v1.resource.InventoryItemAttributeListRsrc;
import ca.bc.gov.farms.api.rest.v1.resource.InventoryItemAttributeRsrc;
import ca.bc.gov.farms.model.v1.InventoryItemAttribute;
import ca.bc.gov.farms.model.v1.InventoryItemAttributeList;
import ca.bc.gov.farms.persistence.v1.dto.InventoryItemAttributeDto;
import ca.bc.gov.farms.service.api.v1.model.factory.InventoryItemAttributeFactory;

public class InventoryItemAttributeRsrcFactory extends BaseResourceFactory implements InventoryItemAttributeFactory {

    @Override
    public InventoryItemAttribute getInventoryItemAttribute(InventoryItemAttributeDto dto, FactoryContext context) {

        URI baseUri = getBaseURI(context);

        InventoryItemAttributeRsrc resource = new InventoryItemAttributeRsrc();

        populateDefaultResource(resource, dto);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getInventoryItemAttributeId(), resource, baseUri);

        return resource;
    }

    private void populateDefaultResource(InventoryItemAttributeRsrc resource, InventoryItemAttributeDto dto) {
        resource.setInventoryItemAttributeId(dto.getInventoryItemAttributeId());
        resource.setInventoryItemCode(dto.getInventoryItemCode());
        resource.setInventoryItemDesc(dto.getInventoryItemDesc());
        resource.setRollupInventoryItemCode(dto.getRollupInventoryItemCode());
        resource.setRollupInventoryItemDesc(dto.getRollupInventoryItemDesc());
    }

    @Override
    public InventoryItemAttributeList<? extends InventoryItemAttribute> getInventoryItemAttributeList(
            List<InventoryItemAttributeDto> dtos,
            FactoryContext context) {

        URI baseUri = getBaseURI(context);

        InventoryItemAttributeListRsrc result = null;

        List<InventoryItemAttributeRsrc> resources = new ArrayList<>();

        for (InventoryItemAttributeDto dto : dtos) {
            InventoryItemAttributeRsrc resource = populate(dto);
            setSelfLink(dto.getInventoryItemAttributeId(), resource, baseUri);
            resources.add(resource);
        }

        result = new InventoryItemAttributeListRsrc();
        result.setInventoryItemAttributeList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    static InventoryItemAttributeRsrc populate(InventoryItemAttributeDto dto) {

        InventoryItemAttributeRsrc result = new InventoryItemAttributeRsrc();

        result.setInventoryItemAttributeId(dto.getInventoryItemAttributeId());
        result.setInventoryItemCode(dto.getInventoryItemCode());
        result.setInventoryItemDesc(dto.getInventoryItemDesc());
        result.setRollupInventoryItemCode(dto.getRollupInventoryItemCode());
        result.setRollupInventoryItemDesc(dto.getRollupInventoryItemDesc());

        return result;
    }

    public static void setSelfLink(Long inventoryItemAttributeId, InventoryItemAttributeRsrc resource, URI baseUri) {

        String selfUri = getInventoryItemAttributeSelfUri(inventoryItemAttributeId, baseUri);

        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }

    public static String getInventoryItemAttributeSelfUri(Long inventoryItemAttributeId, URI baseUri) {

        String result = UriBuilder.fromUri(baseUri)
                .path(InventoryItemAttributeEndpoints.class)
                .build(inventoryItemAttributeId).toString();

        return result;
    }

    public static void setSelfLink(InventoryItemAttributeListRsrc resource, URI baseUri) {

        String selfUri = UriBuilder.fromUri(baseUri)
                .path(InventoryItemAttributeEndpoints.class)
                .build().toString();

        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }

    @Override
    public void updateInventoryItemAttribute(InventoryItemAttributeDto dto, InventoryItemAttribute model) {
        dto.setInventoryItemAttributeId(model.getInventoryItemAttributeId());
        dto.setInventoryItemCode(model.getInventoryItemCode());
        dto.setInventoryItemDesc(model.getInventoryItemDesc());
        dto.setRollupInventoryItemCode(model.getRollupInventoryItemCode());
        dto.setRollupInventoryItemDesc(model.getRollupInventoryItemDesc());
    }
}
