package ca.bc.gov.farms.data.assemblers;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import ca.bc.gov.farms.data.entities.ExpectedProductionEntity;
import ca.bc.gov.farms.data.models.ExpectedProductionListRsrc;
import ca.bc.gov.farms.data.models.ExpectedProductionRsrc;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ExpectedProductionResourceAssembler extends BaseResourceAssembler {

    public ExpectedProductionRsrc getExpectedProduction(@NonNull ExpectedProductionEntity entity) {

        URI baseUri = getBaseURI();

        ExpectedProductionRsrc resource = new ExpectedProductionRsrc();

        BeanUtils.copyProperties(entity, resource);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getExpectedProductionId(), resource, baseUri);

        return resource;
    }

    public ExpectedProductionListRsrc getExpectedProductionList(List<ExpectedProductionEntity> entities) {

        URI baseUri = getBaseURI();

        ExpectedProductionListRsrc result = null;

        @SuppressWarnings("null")
        List<ExpectedProductionRsrc> resources = entities.stream().filter(Objects::nonNull).map(entity -> {
            ExpectedProductionRsrc resource = new ExpectedProductionRsrc();
            BeanUtils.copyProperties(entity, resource);
            setSelfLink(entity.getExpectedProductionId(), resource, baseUri);
            return resource;
        }).collect(Collectors.toList());

        result = new ExpectedProductionListRsrc();
        result.setExpectedProductionList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    public void updateExpectedProduction(@NonNull ExpectedProductionRsrc resource,
            @NonNull ExpectedProductionEntity entity) {
        BeanUtils.copyProperties(resource, entity);
    }
}
