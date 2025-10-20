package ca.bc.gov.farms.api.rest.v1.resource.factory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.core.UriBuilder;

import ca.bc.gov.brmb.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.brmb.common.rest.resource.RelLink;
import ca.bc.gov.brmb.common.rest.resource.types.BaseResourceTypes;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.api.rest.v1.endpoints.InventoryItemDetailEndpoints;
import ca.bc.gov.farms.api.rest.v1.resource.InventoryItemDetailListRsrc;
import ca.bc.gov.farms.api.rest.v1.resource.InventoryItemDetailRsrc;
import ca.bc.gov.farms.model.v1.InventoryItemDetail;
import ca.bc.gov.farms.model.v1.InventoryItemDetailList;
import ca.bc.gov.farms.persistence.v1.dto.InventoryItemDetailDto;
import ca.bc.gov.farms.service.api.v1.model.factory.InventoryItemDetailFactory;

public class InventoryItemDetailRsrcFactory extends BaseResourceFactory implements InventoryItemDetailFactory {

    @Override
    public InventoryItemDetail getInventoryItemDetail(InventoryItemDetailDto dto, FactoryContext context) {

        URI baseUri = getBaseURI(context);

        InventoryItemDetailRsrc resource = new InventoryItemDetailRsrc();

        populateDefaultResource(resource, dto);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getInventoryItemDetailId(), resource, baseUri);

        return resource;
    }

    private void populateDefaultResource(InventoryItemDetailRsrc resource, InventoryItemDetailDto dto) {
        resource.setInventoryItemDetailId(dto.getInventoryItemDetailId());
        resource.setProgramYear(dto.getProgramYear());
        resource.setEligibilityInd(dto.getEligibilityInd());
        resource.setLineItem(dto.getLineItem());
        resource.setInsurableValue(dto.getInsurableValue());
        resource.setPremiumRate(dto.getPremiumRate());
        resource.setInventoryItemCode(dto.getInventoryItemCode());
        resource.setInventoryItemDesc(dto.getInventoryItemDesc());
        resource.setCommodityTypeCode(dto.getCommodityTypeCode());
        resource.setCommodityTypeDesc(dto.getCommodityTypeDesc());
        resource.setFruitVegTypeCode(dto.getFruitVegTypeCode());
        resource.setFruitVegTypeDesc(dto.getFruitVegTypeDesc());
        resource.setMultiStageCommdtyCode(dto.getMultiStageCommdtyCode());
        resource.setMultiStageCommdtyDesc(dto.getMultiStageCommdtyDesc());
    }

    @Override
    public InventoryItemDetailList<? extends InventoryItemDetail> getInventoryItemDetailList(
            List<InventoryItemDetailDto> dtos, FactoryContext context) {

        URI baseUri = getBaseURI(context);

        InventoryItemDetailListRsrc result = null;

        List<InventoryItemDetailRsrc> resources = new ArrayList<>();

        for (InventoryItemDetailDto dto : dtos) {
            InventoryItemDetailRsrc resource = populate(dto);
            setSelfLink(dto.getInventoryItemDetailId(), resource, baseUri);
            resources.add(resource);
        }

        result = new InventoryItemDetailListRsrc();
        result.setInventoryItemDetailList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    static InventoryItemDetailRsrc populate(InventoryItemDetailDto dto) {

        InventoryItemDetailRsrc resource = new InventoryItemDetailRsrc();

        resource.setInventoryItemDetailId(dto.getInventoryItemDetailId());
        resource.setProgramYear(dto.getProgramYear());
        resource.setEligibilityInd(dto.getEligibilityInd());
        resource.setLineItem(dto.getLineItem());
        resource.setInsurableValue(dto.getInsurableValue());
        resource.setPremiumRate(dto.getPremiumRate());
        resource.setInventoryItemCode(dto.getInventoryItemCode());
        resource.setInventoryItemDesc(dto.getInventoryItemDesc());
        resource.setCommodityTypeCode(dto.getCommodityTypeCode());
        resource.setCommodityTypeDesc(dto.getCommodityTypeDesc());
        resource.setFruitVegTypeCode(dto.getFruitVegTypeCode());
        resource.setFruitVegTypeDesc(dto.getFruitVegTypeDesc());
        resource.setMultiStageCommdtyCode(dto.getMultiStageCommdtyCode());
        resource.setMultiStageCommdtyDesc(dto.getMultiStageCommdtyDesc());

        return resource;
    }

    public static void setSelfLink(Long inventoryItemDetailId, InventoryItemDetailRsrc resource, URI baseUri) {

        String selfUri = getInventoryItemDetailSelfUri(inventoryItemDetailId, baseUri);

        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }

    public static String getInventoryItemDetailSelfUri(Long inventoryItemDetailId, URI baseUri) {

        String result = UriBuilder.fromUri(baseUri)
                .path(InventoryItemDetailEndpoints.class)
                .build(inventoryItemDetailId).toString();

        return result;
    }

    public static void setSelfLink(InventoryItemDetailListRsrc resource, URI baseUri) {

        String selfUri = UriBuilder.fromUri(baseUri)
                .path(InventoryItemDetailEndpoints.class)
                .build().toString();

        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }

    @Override
    public void updateInventoryItemDetail(InventoryItemDetailDto dto, InventoryItemDetail model) {
        dto.setInventoryItemDetailId(model.getInventoryItemDetailId());
        dto.setProgramYear(model.getProgramYear());
        dto.setEligibilityInd(model.getEligibilityInd());
        dto.setLineItem(model.getLineItem());
        dto.setInsurableValue(model.getInsurableValue());
        dto.setPremiumRate(model.getPremiumRate());
        dto.setInventoryItemCode(model.getInventoryItemCode());
        dto.setInventoryItemDesc(model.getInventoryItemDesc());
        dto.setCommodityTypeCode(model.getCommodityTypeCode());
        dto.setCommodityTypeDesc(model.getCommodityTypeDesc());
        dto.setFruitVegTypeCode(model.getFruitVegTypeCode());
        dto.setFruitVegTypeDesc(model.getFruitVegTypeDesc());
        dto.setMultiStageCommdtyCode(model.getMultiStageCommdtyCode());
        dto.setMultiStageCommdtyDesc(model.getMultiStageCommdtyDesc());
    }

}
