package ca.bc.gov.farms.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.farms.model.v1.YearConfigurationParameterList;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.YEAR_CONFIGURATION_PARAMETER_LIST_NAME)
@XmlSeeAlso({ YearConfigurationParameterListRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class YearConfigurationParameterListRsrc extends BaseResource
        implements YearConfigurationParameterList<YearConfigurationParameterRsrc> {

    private static final long serialVersionUID = 1L;

    private List<YearConfigurationParameterRsrc> yearConfigurationParameterList;

    public YearConfigurationParameterListRsrc() {
        this.yearConfigurationParameterList = new ArrayList<>();
    }

    @Override
    public List<YearConfigurationParameterRsrc> getYearConfigurationParameterList() {
        return yearConfigurationParameterList;
    }

    @Override
    public void setYearConfigurationParameterList(List<YearConfigurationParameterRsrc> yearConfigurationParameterList) {
        this.yearConfigurationParameterList = yearConfigurationParameterList;
    }

}
