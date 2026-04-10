package ca.bc.gov.farms.data.assemblers;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import ca.bc.gov.farms.data.entities.StructureGroupAttributeEntity;
import ca.bc.gov.farms.data.models.StructureGroupAttributeListRsrc;
import ca.bc.gov.farms.data.models.StructureGroupAttributeModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StructureGroupAttributeResourceAssembler extends BaseResourceAssembler {

    public StructureGroupAttributeModel getStructureGroupAttribute(@NonNull StructureGroupAttributeEntity entity) {

        URI baseUri = getBaseURI();

        StructureGroupAttributeModel resource = new StructureGroupAttributeModel();

        BeanUtils.copyProperties(entity, resource);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getStructureGroupAttributeId(), resource, baseUri);

        return resource;
    }

    public StructureGroupAttributeListRsrc getStructureGroupAttributeList(
            List<StructureGroupAttributeEntity> entities) {

        URI baseUri = getBaseURI();

        StructureGroupAttributeListRsrc result = null;

        @SuppressWarnings("null")
        List<StructureGroupAttributeModel> resources = entities.stream().filter(Objects::nonNull).map(entity -> {
            StructureGroupAttributeModel resource = new StructureGroupAttributeModel();
            BeanUtils.copyProperties(entity, resource);
            setSelfLink(entity.getStructureGroupAttributeId(), resource, baseUri);
            return resource;
        }).collect(Collectors.toList());

        result = new StructureGroupAttributeListRsrc();
        result.setStructureGroupAttributeList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    public void updateStructureGroupAttribute(@NonNull StructureGroupAttributeModel resource,
            @NonNull StructureGroupAttributeEntity entity) {
        BeanUtils.copyProperties(resource, entity);
    }
}
