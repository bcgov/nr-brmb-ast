package ca.bc.gov.farms.api.rest.v1.resource.factory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import ca.bc.gov.brmb.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.brmb.common.rest.resource.RelLink;
import ca.bc.gov.brmb.common.rest.resource.types.BaseResourceTypes;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.api.rest.v1.endpoints.YearConfigurationParameterEndpoints;
import ca.bc.gov.farms.api.rest.v1.resource.YearConfigurationParameterListRsrc;
import ca.bc.gov.farms.api.rest.v1.resource.YearConfigurationParameterRsrc;
import ca.bc.gov.farms.model.v1.YearConfigurationParameter;
import ca.bc.gov.farms.model.v1.YearConfigurationParameterList;
import ca.bc.gov.farms.persistence.v1.dto.YearConfigurationParameterDto;
import ca.bc.gov.farms.service.api.v1.model.factory.YearConfigurationParameterFactory;

public class YearConfigurationParameterRsrcFactory extends BaseResourceFactory
        implements YearConfigurationParameterFactory {

    @Override
    public YearConfigurationParameter getYearConfigurationParameter(YearConfigurationParameterDto dto,
            FactoryContext context) {

        URI baseUri = getBaseURI(context);

        YearConfigurationParameterRsrc resource = new YearConfigurationParameterRsrc();

        populateDefaultResource(resource, dto);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getYearConfigurationParameterId(), resource, baseUri);

        return resource;
    }

    private void populateDefaultResource(YearConfigurationParameterRsrc resource, YearConfigurationParameterDto dto) {
        resource.setYearConfigurationParameterId(dto.getYearConfigurationParameterId());
        resource.setProgramYear(dto.getProgramYear());
        resource.setParameterName(dto.getParameterName());
        resource.setParameterValue(dto.getParameterValue());
        resource.setConfigParamTypeCode(dto.getConfigParamTypeCode());
    }

    @Override
    public YearConfigurationParameterList<? extends YearConfigurationParameter> getYearConfigurationParameterList(
            List<YearConfigurationParameterDto> dtos,
            FactoryContext context) {

        URI baseUri = getBaseURI(context);

        YearConfigurationParameterListRsrc result = null;

        List<YearConfigurationParameterRsrc> resources = new ArrayList<>();

        for (YearConfigurationParameterDto dto : dtos) {
            YearConfigurationParameterRsrc resource = populate(dto);
            setSelfLink(dto.getYearConfigurationParameterId(), resource, baseUri);
            resources.add(resource);
        }

        result = new YearConfigurationParameterListRsrc();
        result.setYearConfigurationParameterList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    static YearConfigurationParameterRsrc populate(YearConfigurationParameterDto dto) {

        YearConfigurationParameterRsrc result = new YearConfigurationParameterRsrc();

        result.setYearConfigurationParameterId(dto.getYearConfigurationParameterId());
        result.setProgramYear(dto.getProgramYear());
        result.setParameterName(dto.getParameterName());
        result.setParameterValue(dto.getParameterValue());
        result.setConfigParamTypeCode(dto.getConfigParamTypeCode());

        return result;
    }

    public static void setSelfLink(Long yearConfigurationParameterId, YearConfigurationParameterRsrc resource,
            URI baseUri) {

        String selfUri = getYearConfigurationParameterSelfUri(yearConfigurationParameterId, baseUri);

        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }

    public static String getYearConfigurationParameterSelfUri(Long yearConfigurationParameterId, URI baseUri) {

        String result = UriBuilder.fromUri(baseUri)
                .path(YearConfigurationParameterEndpoints.class)
                .build(yearConfigurationParameterId).toString();

        return result;
    }

    public static void setSelfLink(YearConfigurationParameterListRsrc resource, URI baseUri) {

        String selfUri = UriBuilder.fromUri(baseUri)
                .path(YearConfigurationParameterEndpoints.class)
                .build().toString();

        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }

    @Override
    public void updateYearConfigurationParameter(YearConfigurationParameterDto dto, YearConfigurationParameter model) {
        dto.setYearConfigurationParameterId(model.getYearConfigurationParameterId());
        dto.setProgramYear(model.getProgramYear());
        dto.setParameterName(model.getParameterName());
        dto.setParameterValue(model.getParameterValue());
        dto.setConfigParamTypeCode(model.getConfigParamTypeCode());
    }
}
