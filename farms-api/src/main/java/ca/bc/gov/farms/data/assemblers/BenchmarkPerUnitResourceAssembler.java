package ca.bc.gov.farms.data.assemblers;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import ca.bc.gov.farms.data.entities.BenchmarkPerUnitEntity;
import ca.bc.gov.farms.data.models.BenchmarkPerUnitListRsrc;
import ca.bc.gov.farms.data.models.BenchmarkPerUnitRsrc;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BenchmarkPerUnitResourceAssembler extends BaseResourceAssembler {

    public BenchmarkPerUnitRsrc getBenchmarkPerUnit(@NonNull BenchmarkPerUnitEntity entity) {

        URI baseUri = getBaseURI();

        BenchmarkPerUnitRsrc resource = new BenchmarkPerUnitRsrc();

        BeanUtils.copyProperties(entity, resource);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getBenchmarkPerUnitId(), resource, baseUri);

        return resource;
    }

    public BenchmarkPerUnitListRsrc getBenchmarkPerUnitList(List<BenchmarkPerUnitEntity> entities) {

        URI baseUri = getBaseURI();

        BenchmarkPerUnitListRsrc result = null;

        @SuppressWarnings("null")
        List<BenchmarkPerUnitRsrc> resources = entities.stream().filter(Objects::nonNull).map(entity -> {
            BenchmarkPerUnitRsrc resource = new BenchmarkPerUnitRsrc();
            BeanUtils.copyProperties(entity, resource);
            setSelfLink(entity.getBenchmarkPerUnitId(), resource, baseUri);
            return resource;
        }).collect(Collectors.toList());

        result = new BenchmarkPerUnitListRsrc();
        result.setBenchmarkPerUnitList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    public void updateBenchmarkPerUnit(@NonNull BenchmarkPerUnitRsrc resource,
            @NonNull BenchmarkPerUnitEntity entity) {
        BeanUtils.copyProperties(resource, entity);
    }
}
