package ca.bc.gov.farms.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.farms.model.v1.FairMarketValueList;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.FAIR_MARKET_VALUE_LIST_NAME)
@XmlSeeAlso({ FairMarketValueRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class FairMarketValueListRsrc extends BaseResource implements FairMarketValueList<FairMarketValueRsrc> {

    private static final long serialVersionUID = 1L;

    private List<FairMarketValueRsrc> fairMarketValueList;

    public FairMarketValueListRsrc() {
        this.fairMarketValueList = new ArrayList<>();
    }

    @Override
    public List<FairMarketValueRsrc> getFairMarketValueList() {
        return fairMarketValueList;
    }

    @Override
    public void setFairMarketValueList(List<FairMarketValueRsrc> fairMarketValueList) {
        this.fairMarketValueList = fairMarketValueList;
    }

}
