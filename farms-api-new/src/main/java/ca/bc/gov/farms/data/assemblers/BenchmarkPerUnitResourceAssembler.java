package ca.bc.gov.farms.data.assemblers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import ca.bc.gov.farms.data.entities.BenchmarkPerUnitEntity;
import ca.bc.gov.farms.data.models.BenchmarkPerUnitListModel;
import ca.bc.gov.farms.data.models.BenchmarkPerUnitModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BenchmarkPerUnitResourceAssembler extends BaseResourceAssembler {

    @SuppressWarnings("null")
    public BenchmarkPerUnitModel getBenchmarkPerUnit(BenchmarkPerUnitEntity entity) {

        URI baseUri = getBaseURI();

        BenchmarkPerUnitModel resource = new BenchmarkPerUnitModel();

        BeanUtils.copyProperties(entity, resource);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getBenchmarkPerUnitId(), resource, baseUri);

        return resource;
    }

    @SuppressWarnings("null")
    public BenchmarkPerUnitListModel getBenchmarkPerUnitList(List<BenchmarkPerUnitEntity> entities) {

        URI baseUri = getBaseURI();

        BenchmarkPerUnitListModel result = null;

        List<BenchmarkPerUnitModel> resources = new ArrayList<>();

        for (BenchmarkPerUnitEntity entity : entities) {
            BenchmarkPerUnitModel resource = new BenchmarkPerUnitModel();
            BeanUtils.copyProperties(entity, resource);
            setSelfLink(entity.getBenchmarkPerUnitId(), resource, baseUri);
            resources.add(resource);
        }

        result = new BenchmarkPerUnitListModel();
        result.setBenchmarkPerUnitList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    public void updateBenchmarkPerUnit(BenchmarkPerUnitModel resource, BenchmarkPerUnitEntity entity) {
        BeanUtils.copyProperties(resource, entity);
    }
}
