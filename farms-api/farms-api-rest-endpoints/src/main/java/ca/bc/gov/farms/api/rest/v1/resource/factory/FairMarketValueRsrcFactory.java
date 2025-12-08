package ca.bc.gov.farms.api.rest.v1.resource.factory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.core.UriBuilder;

import ca.bc.gov.brmb.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.brmb.common.rest.resource.RelLink;
import ca.bc.gov.brmb.common.rest.resource.types.BaseResourceTypes;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.api.rest.v1.endpoints.FairMarketValueEndpoints;
import ca.bc.gov.farms.api.rest.v1.resource.FairMarketValueListRsrc;
import ca.bc.gov.farms.api.rest.v1.resource.FairMarketValueRsrc;
import ca.bc.gov.farms.model.v1.FairMarketValue;
import ca.bc.gov.farms.model.v1.FairMarketValueList;
import ca.bc.gov.farms.persistence.v1.dto.FairMarketValueDto;
import ca.bc.gov.farms.service.api.v1.model.factory.FairMarketValueFactory;

public class FairMarketValueRsrcFactory extends BaseResourceFactory implements FairMarketValueFactory {

    @Override
    public FairMarketValue getFairMarketValue(FairMarketValueDto dto, FactoryContext context) {

        URI baseUri = getBaseURI(context);

        FairMarketValueRsrc resource = new FairMarketValueRsrc();

        populateDefaultResource(resource, dto);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getFairMarketValueId(), resource, baseUri);

        return resource;
    }

    private void populateDefaultResource(FairMarketValueRsrc resource, FairMarketValueDto dto) {
        resource.setFairMarketValueId(dto.getFairMarketValueId());
        resource.setProgramYear(dto.getProgramYear());
        resource.setInventoryItemCode(dto.getInventoryItemCode());
        resource.setInventoryItemDesc(dto.getInventoryItemDesc());
        resource.setMunicipalityCode(dto.getMunicipalityCode());
        resource.setMunicipalityDesc(dto.getMunicipalityDesc());
        resource.setCropUnitCode(dto.getCropUnitCode());
        resource.setCropUnitDesc(dto.getCropUnitDesc());
        resource.setDefaultCropUnitCode(dto.getDefaultCropUnitCode());
        resource.setDefaultCropUnitDesc(dto.getDefaultCropUnitDesc());
        resource.setPeriod01Price(dto.getPeriod01Price());
        resource.setPeriod02Price(dto.getPeriod02Price());
        resource.setPeriod03Price(dto.getPeriod03Price());
        resource.setPeriod04Price(dto.getPeriod04Price());
        resource.setPeriod05Price(dto.getPeriod05Price());
        resource.setPeriod06Price(dto.getPeriod06Price());
        resource.setPeriod07Price(dto.getPeriod07Price());
        resource.setPeriod08Price(dto.getPeriod08Price());
        resource.setPeriod09Price(dto.getPeriod09Price());
        resource.setPeriod10Price(dto.getPeriod10Price());
        resource.setPeriod11Price(dto.getPeriod11Price());
        resource.setPeriod12Price(dto.getPeriod12Price());
        resource.setPeriod01Variance(dto.getPeriod01Variance());
        resource.setPeriod02Variance(dto.getPeriod02Variance());
        resource.setPeriod03Variance(dto.getPeriod03Variance());
        resource.setPeriod04Variance(dto.getPeriod04Variance());
        resource.setPeriod05Variance(dto.getPeriod05Variance());
        resource.setPeriod06Variance(dto.getPeriod06Variance());
        resource.setPeriod07Variance(dto.getPeriod07Variance());
        resource.setPeriod08Variance(dto.getPeriod08Variance());
        resource.setPeriod09Variance(dto.getPeriod09Variance());
        resource.setPeriod10Variance(dto.getPeriod10Variance());
        resource.setPeriod11Variance(dto.getPeriod11Variance());
        resource.setPeriod12Variance(dto.getPeriod12Variance());
        resource.setUrlId(dto.getUrlId());
        resource.setUrl(dto.getUrl());
    }

    @Override
    public FairMarketValueList<? extends FairMarketValue> getFairMarketValueList(List<FairMarketValueDto> dtos,
            FactoryContext context) {

        URI baseUri = getBaseURI(context);

        FairMarketValueListRsrc result = null;

        List<FairMarketValueRsrc> resources = new ArrayList<>();

        for (FairMarketValueDto dto : dtos) {
            FairMarketValueRsrc resource = populate(dto);
            setSelfLink(dto.getFairMarketValueId(), resource, baseUri);
            resources.add(resource);
        }

        result = new FairMarketValueListRsrc();
        result.setFairMarketValueList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    static FairMarketValueRsrc populate(FairMarketValueDto dto) {

        FairMarketValueRsrc result = new FairMarketValueRsrc();

        result.setFairMarketValueId(dto.getFairMarketValueId());
        result.setProgramYear(dto.getProgramYear());
        result.setInventoryItemCode(dto.getInventoryItemCode());
        result.setInventoryItemDesc(dto.getInventoryItemDesc());
        result.setMunicipalityCode(dto.getMunicipalityCode());
        result.setMunicipalityDesc(dto.getMunicipalityDesc());
        result.setCropUnitCode(dto.getCropUnitCode());
        result.setCropUnitDesc(dto.getCropUnitDesc());
        result.setDefaultCropUnitCode(dto.getDefaultCropUnitCode());
        result.setDefaultCropUnitDesc(dto.getDefaultCropUnitDesc());
        result.setPeriod01Price(dto.getPeriod01Price());
        result.setPeriod02Price(dto.getPeriod02Price());
        result.setPeriod03Price(dto.getPeriod03Price());
        result.setPeriod04Price(dto.getPeriod04Price());
        result.setPeriod05Price(dto.getPeriod05Price());
        result.setPeriod06Price(dto.getPeriod06Price());
        result.setPeriod07Price(dto.getPeriod07Price());
        result.setPeriod08Price(dto.getPeriod08Price());
        result.setPeriod09Price(dto.getPeriod09Price());
        result.setPeriod10Price(dto.getPeriod10Price());
        result.setPeriod11Price(dto.getPeriod11Price());
        result.setPeriod12Price(dto.getPeriod12Price());
        result.setPeriod01Variance(dto.getPeriod01Variance());
        result.setPeriod02Variance(dto.getPeriod02Variance());
        result.setPeriod03Variance(dto.getPeriod03Variance());
        result.setPeriod04Variance(dto.getPeriod04Variance());
        result.setPeriod05Variance(dto.getPeriod05Variance());
        result.setPeriod06Variance(dto.getPeriod06Variance());
        result.setPeriod07Variance(dto.getPeriod07Variance());
        result.setPeriod08Variance(dto.getPeriod08Variance());
        result.setPeriod09Variance(dto.getPeriod09Variance());
        result.setPeriod10Variance(dto.getPeriod10Variance());
        result.setPeriod11Variance(dto.getPeriod11Variance());
        result.setPeriod12Variance(dto.getPeriod12Variance());
        result.setUrlId(dto.getUrlId());
        result.setUrl(dto.getUrl());

        return result;
    }

    public static void setSelfLink(String fairMarketValueId, FairMarketValueRsrc resource, URI baseUri) {

        String selfUri = getFairMarketValueSelfUri(fairMarketValueId, baseUri);

        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }

    public static String getFairMarketValueSelfUri(String fairMarketValueId, URI baseUri) {

        String result = UriBuilder.fromUri(baseUri)
                .path(FairMarketValueEndpoints.class)
                .build(fairMarketValueId).toString();

        return result;
    }

    public static void setSelfLink(FairMarketValueListRsrc resource, URI baseUri) {

        String selfUri = UriBuilder.fromUri(baseUri)
                .path(FairMarketValueEndpoints.class)
                .build().toString();

        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }

    @Override
    public void updateFairMarketValue(FairMarketValueDto dto, FairMarketValue model) {
        dto.setFairMarketValueId(model.getFairMarketValueId());
        dto.setProgramYear(model.getProgramYear());
        dto.setInventoryItemCode(model.getInventoryItemCode());
        dto.setInventoryItemDesc(model.getInventoryItemDesc());
        dto.setMunicipalityCode(model.getMunicipalityCode());
        dto.setMunicipalityDesc(model.getMunicipalityDesc());
        dto.setCropUnitCode(model.getCropUnitCode());
        dto.setCropUnitDesc(model.getCropUnitDesc());
        dto.setDefaultCropUnitCode(model.getDefaultCropUnitCode());
        dto.setDefaultCropUnitDesc(model.getDefaultCropUnitDesc());
        dto.setPeriod01Price(model.getPeriod01Price());
        dto.setPeriod02Price(model.getPeriod02Price());
        dto.setPeriod03Price(model.getPeriod03Price());
        dto.setPeriod04Price(model.getPeriod04Price());
        dto.setPeriod05Price(model.getPeriod05Price());
        dto.setPeriod06Price(model.getPeriod06Price());
        dto.setPeriod07Price(model.getPeriod07Price());
        dto.setPeriod08Price(model.getPeriod08Price());
        dto.setPeriod09Price(model.getPeriod09Price());
        dto.setPeriod10Price(model.getPeriod10Price());
        dto.setPeriod11Price(model.getPeriod11Price());
        dto.setPeriod12Price(model.getPeriod12Price());
        dto.setPeriod01Variance(model.getPeriod01Variance());
        dto.setPeriod02Variance(model.getPeriod02Variance());
        dto.setPeriod03Variance(model.getPeriod03Variance());
        dto.setPeriod04Variance(model.getPeriod04Variance());
        dto.setPeriod05Variance(model.getPeriod05Variance());
        dto.setPeriod06Variance(model.getPeriod06Variance());
        dto.setPeriod07Variance(model.getPeriod07Variance());
        dto.setPeriod08Variance(model.getPeriod08Variance());
        dto.setPeriod09Variance(model.getPeriod09Variance());
        dto.setPeriod10Variance(model.getPeriod10Variance());
        dto.setPeriod11Variance(model.getPeriod11Variance());
        dto.setPeriod12Variance(model.getPeriod12Variance());
        dto.setUrlId(model.getUrlId());
        dto.setUrl(model.getUrl());
    }

}
