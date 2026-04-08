package ca.bc.gov.farms.api.rest.v1.resource.factory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.core.UriBuilder;

import ca.bc.gov.brmb.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.brmb.common.rest.resource.RelLink;
import ca.bc.gov.brmb.common.rest.resource.types.BaseResourceTypes;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.api.rest.v1.endpoints.ConfigurationParameterEndpoints;
import ca.bc.gov.farms.api.rest.v1.resource.ConfigurationParameterListRsrc;
import ca.bc.gov.farms.api.rest.v1.resource.ConfigurationParameterRsrc;
import ca.bc.gov.farms.model.v1.ConfigurationParameter;
import ca.bc.gov.farms.model.v1.ConfigurationParameterList;
import ca.bc.gov.farms.persistence.v1.dto.ConfigurationParameterDto;
import ca.bc.gov.farms.service.api.v1.model.factory.ConfigurationParameterFactory;

public class ConfigurationParameterRsrcFactory extends BaseResourceFactory implements ConfigurationParameterFactory {

    @Override
    public ConfigurationParameter getConfigurationParameter(ConfigurationParameterDto dto, FactoryContext context) {

        URI baseUri = getBaseURI(context);

        ConfigurationParameterRsrc resource = new ConfigurationParameterRsrc();

        populateDefaultResource(resource, dto);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getConfigurationParameterId(), resource, baseUri);

        return resource;
    }

    private void populateDefaultResource(ConfigurationParameterRsrc resource, ConfigurationParameterDto dto) {
        resource.setConfigurationParameterId(dto.getConfigurationParameterId());
        resource.setParameterName(dto.getParameterName());
        resource.setParameterValue(dto.getParameterValue());
        resource.setSensitiveDataInd(dto.getSensitiveDataInd());
        resource.setConfigParamTypeCode(dto.getConfigParamTypeCode());
    }

    @Override
    public ConfigurationParameterList<? extends ConfigurationParameter> getConfigurationParameterList(
            List<ConfigurationParameterDto> dtos,
            FactoryContext context) {

        URI baseUri = getBaseURI(context);

        ConfigurationParameterListRsrc result = null;

        List<ConfigurationParameterRsrc> resources = new ArrayList<>();

        for (ConfigurationParameterDto dto : dtos) {
            ConfigurationParameterRsrc resource = populate(dto);
            setSelfLink(dto.getConfigurationParameterId(), resource, baseUri);
            resources.add(resource);
        }

        result = new ConfigurationParameterListRsrc();
        result.setConfigurationParameterList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    static ConfigurationParameterRsrc populate(ConfigurationParameterDto dto) {

        ConfigurationParameterRsrc result = new ConfigurationParameterRsrc();

        result.setConfigurationParameterId(dto.getConfigurationParameterId());
        result.setParameterName(dto.getParameterName());
        result.setParameterValue(dto.getParameterValue());
        result.setSensitiveDataInd(dto.getSensitiveDataInd());
        result.setConfigParamTypeCode(dto.getConfigParamTypeCode());

        return result;
    }

    public static void setSelfLink(Long configurationParameterId, ConfigurationParameterRsrc resource, URI baseUri) {

        String selfUri = getConfigurationParameterSelfUri(configurationParameterId, baseUri);

        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }

    public static String getConfigurationParameterSelfUri(Long configurationParameterId, URI baseUri) {

        String result = UriBuilder.fromUri(baseUri)
                .path(ConfigurationParameterEndpoints.class)
                .build(configurationParameterId).toString();

        return result;
    }

    public static void setSelfLink(ConfigurationParameterListRsrc resource, URI baseUri) {

        String selfUri = UriBuilder.fromUri(baseUri)
                .path(ConfigurationParameterEndpoints.class)
                .build().toString();

        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }

    @Override
    public void updateConfigurationParameter(ConfigurationParameterDto dto, ConfigurationParameter model) {
        dto.setConfigurationParameterId(model.getConfigurationParameterId());
        dto.setParameterName(model.getParameterName());
        dto.setParameterValue(model.getParameterValue());
        dto.setSensitiveDataInd(model.getSensitiveDataInd());
        dto.setConfigParamTypeCode(model.getConfigParamTypeCode());
    }
}
