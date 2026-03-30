package ca.bc.gov.farms.data.assemblers;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import ca.bc.gov.farms.data.entities.CropUnitConversionEntity;
import ca.bc.gov.farms.data.models.CropUnitConversionListModel;
import ca.bc.gov.farms.data.models.CropUnitConversionModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CropUnitConversionResourceAssembler extends BaseResourceAssembler {

    public CropUnitConversionModel getCropUnitConversion(@NonNull CropUnitConversionEntity entity) {

        URI baseUri = getBaseURI();

        CropUnitConversionModel resource = new CropUnitConversionModel();

        BeanUtils.copyProperties(entity, resource);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getCropUnitDefaultId(), resource, baseUri);

        return resource;
    }

    public CropUnitConversionListModel getCropUnitConversionList(List<CropUnitConversionEntity> entities) {

        URI baseUri = getBaseURI();

        CropUnitConversionListModel result = null;

        @SuppressWarnings("null")
        List<CropUnitConversionModel> resources = entities.stream().filter(Objects::nonNull).map(entity -> {
            CropUnitConversionModel resource = new CropUnitConversionModel();
            BeanUtils.copyProperties(entity, resource);
            setSelfLink(entity.getCropUnitDefaultId(), resource, baseUri);
            return resource;
        }).collect(Collectors.toList());

        result = new CropUnitConversionListModel();
        result.setCropUnitConversionList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    public void updateCropUnitConversion(@NonNull CropUnitConversionModel resource,
            @NonNull CropUnitConversionEntity entity) {
        BeanUtils.copyProperties(resource, entity);
    }
}
