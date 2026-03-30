package ca.bc.gov.farms.data.assemblers;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import ca.bc.gov.farms.data.entities.ExpectedProductionEntity;
import ca.bc.gov.farms.data.models.ExpectedProductionListModel;
import ca.bc.gov.farms.data.models.ExpectedProductionModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ExpectedProductionResourceAssembler extends BaseResourceAssembler {

    public ExpectedProductionModel getExpectedProduction(@NonNull ExpectedProductionEntity entity) {

        URI baseUri = getBaseURI();

        ExpectedProductionModel resource = new ExpectedProductionModel();

        BeanUtils.copyProperties(entity, resource);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getExpectedProductionId(), resource, baseUri);

        return resource;
    }

    public ExpectedProductionListModel getExpectedProductionList(List<ExpectedProductionEntity> entities) {

        URI baseUri = getBaseURI();

        ExpectedProductionListModel result = null;

        @SuppressWarnings("null")
        List<ExpectedProductionModel> resources = entities.stream().filter(Objects::nonNull).map(entity -> {
            ExpectedProductionModel resource = new ExpectedProductionModel();
            BeanUtils.copyProperties(entity, resource);
            setSelfLink(entity.getExpectedProductionId(), resource, baseUri);
            return resource;
        }).collect(Collectors.toList());

        result = new ExpectedProductionListModel();
        result.setExpectedProductionList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    public void updateExpectedProduction(@NonNull ExpectedProductionModel resource,
            @NonNull ExpectedProductionEntity entity) {
        BeanUtils.copyProperties(resource, entity);
    }
}
