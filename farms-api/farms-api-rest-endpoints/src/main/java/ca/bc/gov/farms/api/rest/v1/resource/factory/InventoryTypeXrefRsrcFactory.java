package ca.bc.gov.farms.api.rest.v1.resource.factory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import ca.bc.gov.brmb.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.brmb.common.rest.resource.RelLink;
import ca.bc.gov.brmb.common.rest.resource.types.BaseResourceTypes;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.api.rest.v1.endpoints.InventoryTypeXrefEndpoints;
import ca.bc.gov.farms.api.rest.v1.resource.InventoryTypeXrefListRsrc;
import ca.bc.gov.farms.api.rest.v1.resource.InventoryTypeXrefRsrc;
import ca.bc.gov.farms.model.v1.InventoryTypeXref;
import ca.bc.gov.farms.model.v1.InventoryTypeXrefList;
import ca.bc.gov.farms.persistence.v1.dto.InventoryTypeXrefDto;
import ca.bc.gov.farms.service.api.v1.model.factory.InventoryTypeXrefFactory;

public class InventoryTypeXrefRsrcFactory extends BaseResourceFactory implements InventoryTypeXrefFactory {

    @Override
    public InventoryTypeXref getInventoryTypeXref(InventoryTypeXrefDto dto, FactoryContext context) {

        URI baseUri = getBaseURI(context);

        InventoryTypeXrefRsrc resource = new InventoryTypeXrefRsrc();

        populateDefaultResource(resource, dto);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getAgristabilityCommodityXrefId(), resource, baseUri);

        return resource;
    }

    private void populateDefaultResource(InventoryTypeXrefRsrc resource, InventoryTypeXrefDto dto) {
        resource.setAgristabilityCommodityXrefId(dto.getAgristabilityCommodityXrefId());
        resource.setMarketCommodityInd(dto.getMarketCommodityInd());
        resource.setInventoryItemCode(dto.getInventoryItemCode());
        resource.setInventoryItemDesc(dto.getInventoryItemDesc());
        resource.setInventoryGroupCode(dto.getInventoryGroupCode());
        resource.setInventoryGroupDesc(dto.getInventoryGroupDesc());
        resource.setInventoryClassCode(dto.getInventoryClassCode());
        resource.setInventoryClassDesc(dto.getInventoryClassDesc());
    }

    @Override
    public InventoryTypeXrefList<? extends InventoryTypeXref> getInventoryTypeXrefList(List<InventoryTypeXrefDto> dtos,
            FactoryContext context) {

        URI baseUri = getBaseURI(context);

        InventoryTypeXrefListRsrc result = null;

        List<InventoryTypeXrefRsrc> resources = new ArrayList<>();

        for (InventoryTypeXrefDto dto : dtos) {
            InventoryTypeXrefRsrc resource = populate(dto);
            setSelfLink(dto.getAgristabilityCommodityXrefId(), resource, baseUri);
            resources.add(resource);
        }

        result = new InventoryTypeXrefListRsrc();
        result.setInventoryTypeXrefList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    static InventoryTypeXrefRsrc populate(InventoryTypeXrefDto dto) {

        InventoryTypeXrefRsrc result = new InventoryTypeXrefRsrc();

        result.setAgristabilityCommodityXrefId(dto.getAgristabilityCommodityXrefId());
        result.setMarketCommodityInd(dto.getMarketCommodityInd());
        result.setInventoryItemCode(dto.getInventoryItemCode());
        result.setInventoryItemDesc(dto.getInventoryItemDesc());
        result.setInventoryGroupCode(dto.getInventoryGroupCode());
        result.setInventoryGroupDesc(dto.getInventoryGroupDesc());
        result.setInventoryClassCode(dto.getInventoryClassCode());
        result.setInventoryClassDesc(dto.getInventoryClassDesc());

        return result;
    }

    public static void setSelfLink(Long agristabilityCommodityXrefId, InventoryTypeXrefRsrc resource, URI baseUri) {

        String selfUri = getInventoryTypeXrefSelfUri(agristabilityCommodityXrefId, baseUri);

        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }

    public static String getInventoryTypeXrefSelfUri(Long agristabilityCommodityXrefId, URI baseUri) {

        String result = UriBuilder.fromUri(baseUri)
                .path(InventoryTypeXrefEndpoints.class)
                .build(agristabilityCommodityXrefId).toString();

        return result;
    }

    public static void setSelfLink(InventoryTypeXrefListRsrc resource, URI baseUri) {

        String selfUri = UriBuilder.fromUri(baseUri)
                .path(InventoryTypeXrefEndpoints.class)
                .build().toString();

        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }

    @Override
    public void updateInventoryTypeXref(InventoryTypeXrefDto dto, InventoryTypeXref model) {
        dto.setAgristabilityCommodityXrefId(model.getAgristabilityCommodityXrefId());
        dto.setMarketCommodityInd(model.getMarketCommodityInd());
        dto.setInventoryItemCode(model.getInventoryItemCode());
        dto.setInventoryItemDesc(model.getInventoryItemDesc());
        dto.setInventoryGroupCode(model.getInventoryGroupCode());
        dto.setInventoryGroupDesc(model.getInventoryGroupDesc());
        dto.setInventoryClassCode(model.getInventoryClassCode());
        dto.setInventoryClassDesc(model.getInventoryClassDesc());
    }
}
