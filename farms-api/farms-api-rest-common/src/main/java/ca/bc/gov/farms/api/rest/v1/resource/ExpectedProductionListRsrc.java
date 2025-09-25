package ca.bc.gov.farms.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.farms.model.v1.ExpectedProductionList;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.EXPECTED_PRODUCTION_LIST_NAME)
@XmlSeeAlso({ ExpectedProductionListRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class ExpectedProductionListRsrc extends BaseResource implements ExpectedProductionList<ExpectedProductionRsrc> {

    private static final long serialVersionUID = 1L;

    private List<ExpectedProductionRsrc> expectedProductionList;

    public ExpectedProductionListRsrc() {
        this.expectedProductionList = new ArrayList<>();
    }

    @Override
    public List<ExpectedProductionRsrc> getExpectedProductionList() {
        return expectedProductionList;
    }

    @Override
    public void setExpectedProductionList(List<ExpectedProductionRsrc> expectedProductionList) {
        this.expectedProductionList = expectedProductionList;
    }

}
