package ca.bc.gov.farms.api.rest.v1.resource.factory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import ca.bc.gov.brmb.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.brmb.common.rest.resource.RelLink;
import ca.bc.gov.brmb.common.rest.resource.types.BaseResourceTypes;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.api.rest.v1.endpoints.FruitVegTypeDetailEndpoints;
import ca.bc.gov.farms.api.rest.v1.resource.FruitVegTypeDetailListRsrc;
import ca.bc.gov.farms.api.rest.v1.resource.FruitVegTypeDetailRsrc;
import ca.bc.gov.farms.model.v1.FruitVegTypeDetail;
import ca.bc.gov.farms.model.v1.FruitVegTypeDetailList;
import ca.bc.gov.farms.persistence.v1.dto.FruitVegTypeDetailDto;
import ca.bc.gov.farms.service.api.v1.model.factory.FruitVegTypeDetailFactory;

public class FruitVegTypeDetailRsrcFactory extends BaseResourceFactory implements FruitVegTypeDetailFactory {

    @Override
    public FruitVegTypeDetail getFruitVegTypeDetail(FruitVegTypeDetailDto dto, FactoryContext context) {

        URI baseUri = getBaseURI(context);

        FruitVegTypeDetailRsrc resource = new FruitVegTypeDetailRsrc();

        populateDefaultResource(resource, dto);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getFruitVegTypeCode(), resource, baseUri);

        return resource;
    }

    private void populateDefaultResource(FruitVegTypeDetailRsrc resource, FruitVegTypeDetailDto dto) {
        resource.setFruitVegTypeCode(dto.getFruitVegTypeCode());
        resource.setFruitVegTypeDesc(dto.getFruitVegTypeDesc());
        resource.setEstablishedDate(dto.getEstablishedDate());
        resource.setExpiryDate(dto.getExpiryDate());
        resource.setRevenueVarianceLimit(dto.getRevenueVarianceLimit());
    }

    @Override
    public FruitVegTypeDetailList<? extends FruitVegTypeDetail> getFruitVegTypeDetailList(
            List<FruitVegTypeDetailDto> dtos, FactoryContext context) {

        URI baseUri = getBaseURI(context);

        FruitVegTypeDetailListRsrc result = null;

        List<FruitVegTypeDetailRsrc> resources = new ArrayList<>();

        for (FruitVegTypeDetailDto dto : dtos) {
            FruitVegTypeDetailRsrc resource = populate(dto);
            setSelfLink(dto.getFruitVegTypeCode(), resource, baseUri);
            resources.add(resource);
        }

        result = new FruitVegTypeDetailListRsrc();
        result.setFruitVegTypeDetailList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    static FruitVegTypeDetailRsrc populate(FruitVegTypeDetailDto dto) {

        FruitVegTypeDetailRsrc result = new FruitVegTypeDetailRsrc();

        result.setFruitVegTypeCode(dto.getFruitVegTypeCode());
        result.setFruitVegTypeDesc(dto.getFruitVegTypeDesc());
        result.setEstablishedDate(dto.getEstablishedDate());
        result.setExpiryDate(dto.getExpiryDate());
        result.setRevenueVarianceLimit(dto.getRevenueVarianceLimit());

        return result;
    }

    public static void setSelfLink(String fruitVegTypeCode, FruitVegTypeDetailRsrc resource, URI baseUri) {

        String selfUri = getFruitVegTypeDetailSelfUri(fruitVegTypeCode, baseUri);

        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }

    public static String getFruitVegTypeDetailSelfUri(String fruitVegTypeCode, URI baseUri) {

        String result = UriBuilder.fromUri(baseUri)
                .path(FruitVegTypeDetailEndpoints.class)
                .build(fruitVegTypeCode).toString();

        return result;
    }

    public static void setSelfLink(FruitVegTypeDetailListRsrc resource, URI baseUri) {

        String selfUri = UriBuilder.fromUri(baseUri)
                .path(FruitVegTypeDetailEndpoints.class)
                .build().toString();

        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }

    @Override
    public void updateFruitVegTypeDetail(FruitVegTypeDetailDto dto, FruitVegTypeDetail model) {
        dto.setFruitVegTypeCode(model.getFruitVegTypeCode());
        dto.setFruitVegTypeDesc(model.getFruitVegTypeDesc());
        dto.setEstablishedDate(model.getEstablishedDate());
        dto.setExpiryDate(model.getExpiryDate());
        dto.setRevenueVarianceLimit(model.getRevenueVarianceLimit());
    }
}
