package ca.bc.gov.farms.data.assemblers;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import ca.bc.gov.farms.data.entities.ConfigurationParameterEntity;
import ca.bc.gov.farms.data.models.ConfigurationParameterListRsrc;
import ca.bc.gov.farms.data.models.ConfigurationParameterModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ConfigurationParameterResourceAssembler extends BaseResourceAssembler {

    public ConfigurationParameterModel getConfigurationParameter(@NonNull ConfigurationParameterEntity entity) {

        URI baseUri = getBaseURI();

        ConfigurationParameterModel resource = new ConfigurationParameterModel();

        BeanUtils.copyProperties(entity, resource);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getConfigurationParameterId(), resource, baseUri);

        return resource;
    }

    public ConfigurationParameterListRsrc getConfigurationParameterList(List<ConfigurationParameterEntity> entities) {

        URI baseUri = getBaseURI();

        ConfigurationParameterListRsrc result = null;

        @SuppressWarnings("null")
        List<ConfigurationParameterModel> resources = entities.stream().filter(Objects::nonNull).map(entity -> {
            ConfigurationParameterModel resource = new ConfigurationParameterModel();
            BeanUtils.copyProperties(entity, resource);
            setSelfLink(entity.getConfigurationParameterId(), resource, baseUri);
            return resource;
        }).collect(Collectors.toList());

        result = new ConfigurationParameterListRsrc();
        result.setConfigurationParameterList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    public void updateConfigurationParameter(@NonNull ConfigurationParameterModel resource,
            @NonNull ConfigurationParameterEntity entity) {
        BeanUtils.copyProperties(resource, entity);
    }
}
