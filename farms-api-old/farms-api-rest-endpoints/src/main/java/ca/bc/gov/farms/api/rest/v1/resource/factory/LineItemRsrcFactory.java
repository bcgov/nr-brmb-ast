package ca.bc.gov.farms.api.rest.v1.resource.factory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.core.UriBuilder;

import ca.bc.gov.brmb.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.brmb.common.rest.resource.RelLink;
import ca.bc.gov.brmb.common.rest.resource.types.BaseResourceTypes;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.api.rest.v1.endpoints.LineItemEndpoints;
import ca.bc.gov.farms.api.rest.v1.resource.LineItemListRsrc;
import ca.bc.gov.farms.api.rest.v1.resource.LineItemRsrc;
import ca.bc.gov.farms.model.v1.LineItem;
import ca.bc.gov.farms.model.v1.LineItemList;
import ca.bc.gov.farms.persistence.v1.dto.LineItemDto;
import ca.bc.gov.farms.service.api.v1.model.factory.LineItemFactory;

public class LineItemRsrcFactory extends BaseResourceFactory implements LineItemFactory {

    @Override
    public LineItem getLineItem(LineItemDto dto, FactoryContext context) {

        URI baseUri = getBaseURI(context);

        LineItemRsrc resource = new LineItemRsrc();

        populateDefaultResource(resource, dto);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getLineItemId(), resource, baseUri);

        return resource;
    }

    private void populateDefaultResource(LineItemRsrc resource, LineItemDto dto) {
        resource.setLineItemId(dto.getLineItemId());
        resource.setProgramYear(dto.getProgramYear());
        resource.setLineItem(dto.getLineItem());
        resource.setDescription(dto.getDescription());
        resource.setProvince(dto.getProvince());
        resource.setEligibilityInd(dto.getEligibilityInd());
        resource.setEligibilityForRefYearsInd(dto.getEligibilityForRefYearsInd());
        resource.setYardageInd(dto.getYardageInd());
        resource.setProgramPaymentInd(dto.getProgramPaymentInd());
        resource.setContractWorkInd(dto.getContractWorkInd());
        resource.setSupplyManagedCommodityInd(dto.getSupplyManagedCommodityInd());
        resource.setExcludeFromRevenueCalcInd(dto.getExcludeFromRevenueCalcInd());
        resource.setIndustryAverageExpenseInd(dto.getIndustryAverageExpenseInd());
        resource.setCommodityTypeCode(dto.getCommodityTypeCode());
        resource.setFruitVegTypeCode(dto.getFruitVegTypeCode());
    }

    @Override
    public LineItemList<? extends LineItem> getLineItemList(List<LineItemDto> dtos, FactoryContext context) {

        URI baseUri = getBaseURI(context);

        LineItemListRsrc result = null;

        List<LineItemRsrc> resources = new ArrayList<>();

        for (LineItemDto dto : dtos) {
            LineItemRsrc resource = populate(dto);
            setSelfLink(dto.getLineItemId(), resource, baseUri);
            resources.add(resource);
        }

        result = new LineItemListRsrc();
        result.setLineItemList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    static LineItemRsrc populate(LineItemDto dto) {

        LineItemRsrc result = new LineItemRsrc();

        result.setLineItemId(dto.getLineItemId());
        result.setProgramYear(dto.getProgramYear());
        result.setLineItem(dto.getLineItem());
        result.setDescription(dto.getDescription());
        result.setProvince(dto.getProvince());
        result.setEligibilityInd(dto.getEligibilityInd());
        result.setEligibilityForRefYearsInd(dto.getEligibilityForRefYearsInd());
        result.setYardageInd(dto.getYardageInd());
        result.setProgramPaymentInd(dto.getProgramPaymentInd());
        result.setContractWorkInd(dto.getContractWorkInd());
        result.setSupplyManagedCommodityInd(dto.getSupplyManagedCommodityInd());
        result.setExcludeFromRevenueCalcInd(dto.getExcludeFromRevenueCalcInd());
        result.setIndustryAverageExpenseInd(dto.getIndustryAverageExpenseInd());
        result.setCommodityTypeCode(dto.getCommodityTypeCode());
        result.setFruitVegTypeCode(dto.getFruitVegTypeCode());

        return result;
    }

    public static void setSelfLink(Long lineItemId, LineItemRsrc resource, URI baseUri) {

        String selfUri = getLineItemSelfUri(lineItemId, baseUri);

        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }

    public static String getLineItemSelfUri(Long lineItemId, URI baseUri) {

        String result = UriBuilder.fromUri(baseUri)
                .path(LineItemEndpoints.class)
                .build(lineItemId).toString();

        return result;
    }

    public static void setSelfLink(LineItemListRsrc resource, URI baseUri) {

        String selfUri = UriBuilder.fromUri(baseUri)
                .path(LineItemEndpoints.class)
                .build().toString();

        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }

    @Override
    public void updateLineItem(LineItemDto dto, LineItem model) {
        dto.setLineItemId(model.getLineItemId());
        dto.setProgramYear(model.getProgramYear());
        dto.setLineItem(model.getLineItem());
        dto.setDescription(model.getDescription());
        dto.setProvince(model.getProvince());
        dto.setEligibilityInd(model.getEligibilityInd());
        dto.setEligibilityForRefYearsInd(model.getEligibilityForRefYearsInd());
        dto.setYardageInd(model.getYardageInd());
        dto.setProgramPaymentInd(model.getProgramPaymentInd());
        dto.setContractWorkInd(model.getContractWorkInd());
        dto.setSupplyManagedCommodityInd(model.getSupplyManagedCommodityInd());
        dto.setExcludeFromRevenueCalcInd(model.getExcludeFromRevenueCalcInd());
        dto.setIndustryAverageExpenseInd(model.getIndustryAverageExpenseInd());
        dto.setCommodityTypeCode(model.getCommodityTypeCode());
        dto.setFruitVegTypeCode(model.getFruitVegTypeCode());
    }
}
