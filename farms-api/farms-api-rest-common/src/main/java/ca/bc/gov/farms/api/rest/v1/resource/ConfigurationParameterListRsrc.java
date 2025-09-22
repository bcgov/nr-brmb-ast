package ca.bc.gov.farms.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.farms.model.v1.ConfigurationParameterList;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.CONFIGURATION_PARAMETER_LIST_NAME)
@XmlSeeAlso({ ConfigurationParameterListRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class ConfigurationParameterListRsrc extends BaseResource
        implements ConfigurationParameterList<ConfigurationParameterRsrc> {

    private static final long serialVersionUID = 1L;

    private List<ConfigurationParameterRsrc> configurationParameterList;

    public ConfigurationParameterListRsrc() {
        this.configurationParameterList = new ArrayList<>();
    }

    @Override
    public List<ConfigurationParameterRsrc> getConfigurationParameterList() {
        return configurationParameterList;
    }

    @Override
    public void setConfigurationParameterList(List<ConfigurationParameterRsrc> configurationParameterList) {
        this.configurationParameterList = configurationParameterList;
    }

}
