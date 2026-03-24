package ca.bc.gov.farms.api.rest.v1.resource;

import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.farms.model.v1.FruitVegTypeDetailList;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.FRUIT_VEG_TYPE_DETAIL_LIST_NAME)
@XmlSeeAlso({ FruitVegTypeDetailListRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class FruitVegTypeDetailListRsrc extends BaseResource implements FruitVegTypeDetailList<FruitVegTypeDetailRsrc> {

    private static final long serialVersionUID = 1L;

    private List<FruitVegTypeDetailRsrc> fruitVegTypeDetails;

    @Override
    public List<FruitVegTypeDetailRsrc> getFruitVegTypeDetailList() {
        return fruitVegTypeDetails;
    }

    @Override
    public void setFruitVegTypeDetailList(List<FruitVegTypeDetailRsrc> fruitVegTypeDetails) {
        this.fruitVegTypeDetails = fruitVegTypeDetails;
    }

}
