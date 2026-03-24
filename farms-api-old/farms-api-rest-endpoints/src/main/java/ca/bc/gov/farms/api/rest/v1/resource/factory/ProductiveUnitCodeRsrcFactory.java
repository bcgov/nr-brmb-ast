package ca.bc.gov.farms.api.rest.v1.resource.factory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.brmb.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.brmb.common.rest.resource.RelLink;
import ca.bc.gov.brmb.common.rest.resource.types.BaseResourceTypes;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.api.rest.v1.resource.ProductiveUnitCodeListRsrc;
import ca.bc.gov.farms.api.rest.v1.resource.ProductiveUnitCodeRsrc;
import ca.bc.gov.farms.api.rest.v1.endpoints.ProductiveUnitCodeEndpoints;
import ca.bc.gov.farms.model.v1.ProductiveUnitCode;
import ca.bc.gov.farms.model.v1.ProductiveUnitCodeList;
import ca.bc.gov.farms.persistence.v1.dto.ProductiveUnitCodeDto;
import ca.bc.gov.farms.service.api.v1.model.factory.ProductiveUnitCodeFactory;
import jakarta.ws.rs.core.UriBuilder;

public class ProductiveUnitCodeRsrcFactory extends BaseResourceFactory implements ProductiveUnitCodeFactory {

    @Override
    public ProductiveUnitCodeList<? extends ProductiveUnitCode> getProductiveUnitCodeList(
            List<ProductiveUnitCodeDto> dtos, FactoryContext context) {

        URI baseUri = getBaseURI(context);

        ProductiveUnitCodeListRsrc result = null;

        List<ProductiveUnitCodeRsrc> resources = new ArrayList<>();

        for (ProductiveUnitCodeDto dto : dtos) {
            ProductiveUnitCodeRsrc resource = populate(dto);
            setSelfLink(dto.getCode(), resource, baseUri);
            resources.add(resource);
        }

        result = new ProductiveUnitCodeListRsrc();
        result.setProductiveUnitCodeList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    static ProductiveUnitCodeRsrc populate(ProductiveUnitCodeDto dto) {

        ProductiveUnitCodeRsrc result = new ProductiveUnitCodeRsrc();

        result.setCode(dto.getCode());
        result.setDescription(dto.getDescription());

        return result;
    }

    public static void setSelfLink(String code, ProductiveUnitCodeRsrc resource, URI baseUri) {

        String selfUri = getProductiveUnitCodeSelfUri(code, baseUri);

        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }

    public static String getProductiveUnitCodeSelfUri(String code, URI baseUri) {

        String result = UriBuilder.fromUri(baseUri)
                .path(ProductiveUnitCodeEndpoints.class)
                .build(code).toString();

        return result;
    }

    public static void setSelfLink(ProductiveUnitCodeListRsrc resource, URI baseUri) {

        String selfUri = UriBuilder.fromUri(baseUri)
                .path(ProductiveUnitCodeEndpoints.class)
                .build().toString();

        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }
}
