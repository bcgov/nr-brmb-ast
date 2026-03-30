package ca.bc.gov.farms.data.assemblers;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import ca.bc.gov.farms.data.entities.YearConfigurationParameterEntity;
import ca.bc.gov.farms.data.models.YearConfigurationParameterListModel;
import ca.bc.gov.farms.data.models.YearConfigurationParameterModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class YearConfigurationParameterResourceAssembler extends BaseResourceAssembler {

    public YearConfigurationParameterModel getYearConfigurationParameter(
            @NonNull YearConfigurationParameterEntity entity) {

        URI baseUri = getBaseURI();

        YearConfigurationParameterModel resource = new YearConfigurationParameterModel();

        BeanUtils.copyProperties(entity, resource);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getYearConfigurationParameterId(), resource, baseUri);

        return resource;
    }

    public YearConfigurationParameterListModel getYearConfigurationParameterList(
            List<YearConfigurationParameterEntity> entities) {

        URI baseUri = getBaseURI();

        YearConfigurationParameterListModel result = null;

        @SuppressWarnings("null")
        List<YearConfigurationParameterModel> resources = entities.stream().filter(Objects::nonNull).map(entity -> {
            YearConfigurationParameterModel resource = new YearConfigurationParameterModel();
            BeanUtils.copyProperties(entity, resource);
            setSelfLink(entity.getYearConfigurationParameterId(), resource, baseUri);
            return resource;
        }).collect(Collectors.toList());

        result = new YearConfigurationParameterListModel();
        result.setYearConfigurationParameterList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    public void updateYearConfigurationParameter(@NonNull YearConfigurationParameterModel resource,
            @NonNull YearConfigurationParameterEntity entity) {
        BeanUtils.copyProperties(resource, entity);
    }
}
