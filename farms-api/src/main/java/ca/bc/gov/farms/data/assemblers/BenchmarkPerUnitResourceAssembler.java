package ca.bc.gov.farms.data.assemblers;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import ca.bc.gov.farms.data.entities.BenchmarkPerUnitEntity;
import ca.bc.gov.farms.data.models.BenchmarkPerUnitListModel;
import ca.bc.gov.farms.data.models.BenchmarkPerUnitModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BenchmarkPerUnitResourceAssembler extends BaseResourceAssembler {

    public BenchmarkPerUnitModel getBenchmarkPerUnit(@NonNull BenchmarkPerUnitEntity entity) {

        URI baseUri = getBaseURI();

        BenchmarkPerUnitModel resource = new BenchmarkPerUnitModel();

        BeanUtils.copyProperties(entity, resource);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getBenchmarkPerUnitId(), resource, baseUri);

        return resource;
    }

    public BenchmarkPerUnitListModel getBenchmarkPerUnitList(List<BenchmarkPerUnitEntity> entities) {

        URI baseUri = getBaseURI();

        BenchmarkPerUnitListModel result = null;

        @SuppressWarnings("null")
        List<BenchmarkPerUnitModel> resources = entities.stream().filter(Objects::nonNull).map(entity -> {
            BenchmarkPerUnitModel resource = new BenchmarkPerUnitModel();
            BeanUtils.copyProperties(entity, resource);
            setSelfLink(entity.getBenchmarkPerUnitId(), resource, baseUri);
            return resource;
        }).collect(Collectors.toList());

        result = new BenchmarkPerUnitListModel();
        result.setBenchmarkPerUnitList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    public void updateBenchmarkPerUnit(@NonNull BenchmarkPerUnitModel resource,
            @NonNull BenchmarkPerUnitEntity entity) {
        BeanUtils.copyProperties(resource, entity);
    }
}
