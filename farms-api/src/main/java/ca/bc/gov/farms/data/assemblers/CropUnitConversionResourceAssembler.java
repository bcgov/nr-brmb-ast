package ca.bc.gov.farms.data.assemblers;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import ca.bc.gov.farms.data.entities.ConversionUnitEntity;
import ca.bc.gov.farms.data.entities.CropUnitConversionEntity;
import ca.bc.gov.farms.data.models.ConversionUnitRsrc;
import ca.bc.gov.farms.data.models.CropUnitConversionListRsrc;
import ca.bc.gov.farms.data.models.CropUnitConversionRsrc;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CropUnitConversionResourceAssembler extends BaseResourceAssembler {

    private void populate(CropUnitConversionEntity entity, CropUnitConversionRsrc resource) {

        @SuppressWarnings("null")
        List<ConversionUnitRsrc> conversionUnitResources = entity.getConversionUnits().stream().map(e -> {
            ConversionUnitRsrc r = new ConversionUnitRsrc();
            BeanUtils.copyProperties(e, r);
            return r;
        }).collect(Collectors.toList());

        resource.setConversionUnits(conversionUnitResources);
    }

    public CropUnitConversionRsrc getCropUnitConversion(@NonNull CropUnitConversionEntity entity) {

        URI baseUri = getBaseURI();

        CropUnitConversionRsrc resource = new CropUnitConversionRsrc();

        BeanUtils.copyProperties(entity, resource);
        populate(entity, resource);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getCropUnitDefaultId(), resource, baseUri);

        return resource;
    }

    public CropUnitConversionListRsrc getCropUnitConversionList(List<CropUnitConversionEntity> entities) {

        URI baseUri = getBaseURI();

        CropUnitConversionListRsrc result = null;

        @SuppressWarnings("null")
        List<CropUnitConversionRsrc> resources = entities.stream().filter(Objects::nonNull).map(entity -> {
            CropUnitConversionRsrc resource = new CropUnitConversionRsrc();
            BeanUtils.copyProperties(entity, resource);
            populate(entity, resource);
            setSelfLink(entity.getCropUnitDefaultId(), resource, baseUri);
            return resource;
        }).collect(Collectors.toList());

        result = new CropUnitConversionListRsrc();
        result.setCropUnitConversionList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    public void updateCropUnitConversion(@NonNull CropUnitConversionRsrc resource,
            @NonNull CropUnitConversionEntity entity) {
        BeanUtils.copyProperties(resource, entity);

        @SuppressWarnings("null")
        List<ConversionUnitEntity> entities = resource.getConversionUnits().stream().map(r -> {
            ConversionUnitEntity e = new ConversionUnitEntity();
            BeanUtils.copyProperties(r, e);
            return e;
        }).collect(Collectors.toList());

        entity.setConversionUnits(entities);
    }
}
