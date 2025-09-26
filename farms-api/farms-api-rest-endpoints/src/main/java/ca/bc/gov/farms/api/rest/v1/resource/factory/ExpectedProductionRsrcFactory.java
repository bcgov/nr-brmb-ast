package ca.bc.gov.farms.api.rest.v1.resource.factory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import ca.bc.gov.brmb.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.brmb.common.rest.resource.RelLink;
import ca.bc.gov.brmb.common.rest.resource.types.BaseResourceTypes;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.api.rest.v1.endpoints.ExpectedProductionEndpoints;
import ca.bc.gov.farms.api.rest.v1.resource.ExpectedProductionListRsrc;
import ca.bc.gov.farms.api.rest.v1.resource.ExpectedProductionRsrc;
import ca.bc.gov.farms.model.v1.ExpectedProduction;
import ca.bc.gov.farms.model.v1.ExpectedProductionList;
import ca.bc.gov.farms.persistence.v1.dto.ExpectedProductionDto;
import ca.bc.gov.farms.service.api.v1.model.factory.ExpectedProductionFactory;

public class ExpectedProductionRsrcFactory extends BaseResourceFactory implements ExpectedProductionFactory {

    @Override
    public ExpectedProduction getExpectedProduction(ExpectedProductionDto dto, FactoryContext context) {

        URI baseUri = getBaseURI(context);

        ExpectedProductionRsrc resource = new ExpectedProductionRsrc();

        populateDefaultResource(resource, dto);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getExpectedProductionId(), resource, baseUri);

        return resource;
    }

    private void populateDefaultResource(ExpectedProductionRsrc resource, ExpectedProductionDto dto) {
        resource.setExpectedProductionId(dto.getExpectedProductionId());
        resource.setExpectedProductionPerProdUnit(dto.getExpectedProductionPerProdUnit());
        resource.setInventoryItemCode(dto.getInventoryItemCode());
        resource.setInventoryItemDesc(dto.getInventoryItemDesc());
        resource.setCropUnitCode(dto.getCropUnitCode());
        resource.setCropUnitDesc(dto.getCropUnitDesc());
    }

    @Override
    public ExpectedProductionList<? extends ExpectedProduction> getExpectedProductionList(
            List<ExpectedProductionDto> dtos,
            FactoryContext context) {

        URI baseUri = getBaseURI(context);

        ExpectedProductionListRsrc result = null;

        List<ExpectedProductionRsrc> resources = new ArrayList<>();

        for (ExpectedProductionDto dto : dtos) {
            ExpectedProductionRsrc resource = populate(dto);
            setSelfLink(dto.getExpectedProductionId(), resource, baseUri);
            resources.add(resource);
        }

        result = new ExpectedProductionListRsrc();
        result.setExpectedProductionList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    static ExpectedProductionRsrc populate(ExpectedProductionDto dto) {

        ExpectedProductionRsrc result = new ExpectedProductionRsrc();

        result.setExpectedProductionId(dto.getExpectedProductionId());
        result.setExpectedProductionPerProdUnit(dto.getExpectedProductionPerProdUnit());
        result.setInventoryItemCode(dto.getInventoryItemCode());
        result.setInventoryItemDesc(dto.getInventoryItemDesc());
        result.setCropUnitCode(dto.getCropUnitCode());
        result.setCropUnitDesc(dto.getCropUnitDesc());

        return result;
    }

    public static void setSelfLink(Long expectedProductionId, ExpectedProductionRsrc resource, URI baseUri) {

        String selfUri = getExpectedProductionSelfUri(expectedProductionId, baseUri);

        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }

    public static String getExpectedProductionSelfUri(Long expectedProductionId, URI baseUri) {

        String result = UriBuilder.fromUri(baseUri)
                .path(ExpectedProductionEndpoints.class)
                .build(expectedProductionId).toString();

        return result;
    }

    public static void setSelfLink(ExpectedProductionListRsrc resource, URI baseUri) {

        String selfUri = UriBuilder.fromUri(baseUri)
                .path(ExpectedProductionEndpoints.class)
                .build().toString();

        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }

    @Override
    public void updateExpectedProduction(ExpectedProductionDto dto, ExpectedProduction model) {
        dto.setExpectedProductionId(model.getExpectedProductionId());
        dto.setExpectedProductionPerProdUnit(model.getExpectedProductionPerProdUnit());
        dto.setInventoryItemCode(model.getInventoryItemCode());
        dto.setInventoryItemDesc(model.getInventoryItemDesc());
        dto.setCropUnitCode(model.getCropUnitCode());
        dto.setCropUnitDesc(model.getCropUnitDesc());
    }
}
