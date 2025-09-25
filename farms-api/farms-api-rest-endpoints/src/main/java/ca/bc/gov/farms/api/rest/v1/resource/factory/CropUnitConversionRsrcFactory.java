package ca.bc.gov.farms.api.rest.v1.resource.factory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import ca.bc.gov.brmb.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.brmb.common.rest.resource.RelLink;
import ca.bc.gov.brmb.common.rest.resource.types.BaseResourceTypes;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.api.rest.v1.endpoints.CropUnitConversionEndpoints;
import ca.bc.gov.farms.api.rest.v1.resource.CropUnitConversionListRsrc;
import ca.bc.gov.farms.api.rest.v1.resource.CropUnitConversionRsrc;
import ca.bc.gov.farms.model.v1.CropUnitConversion;
import ca.bc.gov.farms.model.v1.CropUnitConversionList;
import ca.bc.gov.farms.persistence.v1.dto.CropUnitConversionDto;
import ca.bc.gov.farms.service.api.v1.model.factory.CropUnitConversionFactory;

public class CropUnitConversionRsrcFactory extends BaseResourceFactory implements CropUnitConversionFactory {

    @Override
    public CropUnitConversion getCropUnitConversion(CropUnitConversionDto dto, FactoryContext context) {

        URI baseUri = getBaseURI(context);

        CropUnitConversionRsrc resource = new CropUnitConversionRsrc();

        populateDefaultResource(resource, dto);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getCropUnitConversionFactorId(), resource, baseUri);

        return resource;
    }

    private void populateDefaultResource(CropUnitConversionRsrc resource, CropUnitConversionDto dto) {
        resource.setCropUnitConversionFactorId(dto.getCropUnitConversionFactorId());
        resource.setInventoryItemCode(dto.getInventoryItemCode());
        resource.setInventoryItemDesc(dto.getInventoryItemDesc());
        resource.setCropUnitCode(dto.getCropUnitCode());
        resource.setCropUnitDesc(dto.getCropUnitDesc());
        resource.setConversionFactor(dto.getConversionFactor());
        resource.setTargetCropUnitCode(dto.getTargetCropUnitCode());
        resource.setTargetCropUnitDesc(dto.getTargetCropUnitDesc());
    }

    @Override
    public CropUnitConversionList<? extends CropUnitConversion> getCropUnitConversionList(
            List<CropUnitConversionDto> dtos,
            FactoryContext context) {

        URI baseUri = getBaseURI(context);

        CropUnitConversionListRsrc result = null;

        List<CropUnitConversionRsrc> resources = new ArrayList<>();

        for (CropUnitConversionDto dto : dtos) {
            CropUnitConversionRsrc resource = populate(dto);
            setSelfLink(dto.getCropUnitConversionFactorId(), resource, baseUri);
            resources.add(resource);
        }

        result = new CropUnitConversionListRsrc();
        result.setCropUnitConversionList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    static CropUnitConversionRsrc populate(CropUnitConversionDto dto) {

        CropUnitConversionRsrc result = new CropUnitConversionRsrc();

        result.setCropUnitConversionFactorId(dto.getCropUnitConversionFactorId());
        result.setInventoryItemCode(dto.getInventoryItemCode());
        result.setInventoryItemDesc(dto.getInventoryItemDesc());
        result.setCropUnitCode(dto.getCropUnitCode());
        result.setCropUnitDesc(dto.getCropUnitDesc());
        result.setConversionFactor(dto.getConversionFactor());
        result.setTargetCropUnitCode(dto.getTargetCropUnitCode());
        result.setTargetCropUnitDesc(dto.getTargetCropUnitDesc());

        return result;
    }

    public static void setSelfLink(Long cropUnitConversionFactorId, CropUnitConversionRsrc resource, URI baseUri) {

        String selfUri = getCropUnitConversionSelfUri(cropUnitConversionFactorId, baseUri);

        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }

    public static String getCropUnitConversionSelfUri(Long cropUnitConversionFactorId, URI baseUri) {

        String result = UriBuilder.fromUri(baseUri)
                .path(CropUnitConversionEndpoints.class)
                .build(cropUnitConversionFactorId).toString();

        return result;
    }

    public static void setSelfLink(CropUnitConversionListRsrc resource, URI baseUri) {

        String selfUri = UriBuilder.fromUri(baseUri)
                .path(CropUnitConversionEndpoints.class)
                .build().toString();

        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }

    @Override
    public void updateCropUnitConversion(CropUnitConversionDto dto, CropUnitConversion model) {
        dto.setCropUnitConversionFactorId(model.getCropUnitConversionFactorId());
        dto.setInventoryItemCode(model.getInventoryItemCode());
        dto.setInventoryItemDesc(model.getInventoryItemDesc());
        dto.setCropUnitCode(model.getCropUnitCode());
        dto.setCropUnitDesc(model.getCropUnitDesc());
        dto.setConversionFactor(model.getConversionFactor());
        dto.setTargetCropUnitCode(model.getTargetCropUnitCode());
        dto.setTargetCropUnitDesc(model.getTargetCropUnitDesc());
    }
}
