package ca.bc.gov.farms.api.rest.v1.resource.factory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import ca.bc.gov.brmb.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.brmb.common.rest.resource.RelLink;
import ca.bc.gov.brmb.common.rest.resource.types.BaseResourceTypes;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.api.rest.v1.endpoints.StructureGroupAttributeEndpoints;
import ca.bc.gov.farms.api.rest.v1.resource.StructureGroupAttributeListRsrc;
import ca.bc.gov.farms.api.rest.v1.resource.StructureGroupAttributeRsrc;
import ca.bc.gov.farms.model.v1.StructureGroupAttribute;
import ca.bc.gov.farms.model.v1.StructureGroupAttributeList;
import ca.bc.gov.farms.persistence.v1.dto.StructureGroupAttributeDto;
import ca.bc.gov.farms.service.api.v1.model.factory.StructureGroupAttributeFactory;

public class StructureGroupAttributeRsrcFactory extends BaseResourceFactory implements StructureGroupAttributeFactory {

    @Override
    public StructureGroupAttribute getStructureGroupAttribute(StructureGroupAttributeDto dto, FactoryContext context) {

        URI baseUri = getBaseURI(context);

        StructureGroupAttributeRsrc resource = new StructureGroupAttributeRsrc();

        populateDefaultResource(resource, dto);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getStructureGroupAttributeId(), resource, baseUri);

        return resource;
    }

    private void populateDefaultResource(StructureGroupAttributeRsrc resource, StructureGroupAttributeDto dto) {
        resource.setStructureGroupAttributeId(dto.getStructureGroupAttributeId());
        resource.setStructureGroupCode(dto.getStructureGroupCode());
        resource.setStructureGroupDesc(dto.getStructureGroupDesc());
        resource.setRollupStructureGroupCode(dto.getRollupStructureGroupCode());
        resource.setRollupStructureGroupDesc(dto.getRollupStructureGroupDesc());
    }

    @Override
    public StructureGroupAttributeList<? extends StructureGroupAttribute> getStructureGroupAttributeList(
            List<StructureGroupAttributeDto> dtos,
            FactoryContext context) {

        URI baseUri = getBaseURI(context);

        StructureGroupAttributeListRsrc result = null;

        List<StructureGroupAttributeRsrc> resources = new ArrayList<>();

        for (StructureGroupAttributeDto dto : dtos) {
            StructureGroupAttributeRsrc resource = populate(dto);
            setSelfLink(dto.getStructureGroupAttributeId(), resource, baseUri);
            resources.add(resource);
        }

        result = new StructureGroupAttributeListRsrc();
        result.setStructureGroupAttributeList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    static StructureGroupAttributeRsrc populate(StructureGroupAttributeDto dto) {

        StructureGroupAttributeRsrc result = new StructureGroupAttributeRsrc();

        result.setStructureGroupAttributeId(dto.getStructureGroupAttributeId());
        result.setStructureGroupCode(dto.getStructureGroupCode());
        result.setStructureGroupDesc(dto.getStructureGroupDesc());
        result.setRollupStructureGroupCode(dto.getRollupStructureGroupCode());
        result.setRollupStructureGroupDesc(dto.getRollupStructureGroupDesc());

        return result;
    }

    public static void setSelfLink(Long structureGroupAttributeId, StructureGroupAttributeRsrc resource, URI baseUri) {

        String selfUri = getStructureGroupAttributeSelfUri(structureGroupAttributeId, baseUri);

        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }

    public static String getStructureGroupAttributeSelfUri(Long structureGroupAttributeId, URI baseUri) {

        String result = UriBuilder.fromUri(baseUri)
                .path(StructureGroupAttributeEndpoints.class)
                .build(structureGroupAttributeId).toString();

        return result;
    }

    public static void setSelfLink(StructureGroupAttributeListRsrc resource, URI baseUri) {

        String selfUri = UriBuilder.fromUri(baseUri)
                .path(StructureGroupAttributeEndpoints.class)
                .build().toString();

        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }

    @Override
    public void updateStructureGroupAttribute(StructureGroupAttributeDto dto, StructureGroupAttribute model) {
        dto.setStructureGroupAttributeId(model.getStructureGroupAttributeId());
        dto.setStructureGroupCode(model.getStructureGroupCode());
        dto.setStructureGroupDesc(model.getStructureGroupDesc());
        dto.setRollupStructureGroupCode(model.getRollupStructureGroupCode());
        dto.setRollupStructureGroupDesc(model.getRollupStructureGroupDesc());
    }
}
