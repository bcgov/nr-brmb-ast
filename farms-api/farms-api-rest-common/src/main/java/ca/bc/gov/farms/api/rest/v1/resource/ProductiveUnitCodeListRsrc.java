package ca.bc.gov.farms.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.farms.model.v1.ProductiveUnitCodeList;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.PRODUCTIVE_UNIT_CODE_LIST_NAME)
@XmlSeeAlso({ ProductiveUnitCodeListRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class ProductiveUnitCodeListRsrc extends BaseResource implements ProductiveUnitCodeList<ProductiveUnitCodeRsrc> {

    private static final long serialVersionUID = 1L;

    private List<ProductiveUnitCodeRsrc> productiveUnitCodeList;

    public ProductiveUnitCodeListRsrc() {
        this.productiveUnitCodeList = new ArrayList<>();
    }

    @Override
    public List<ProductiveUnitCodeRsrc> getProductiveUnitCodeList() {
        return productiveUnitCodeList;
    }

    @Override
    public void setProductiveUnitCodeList(List<ProductiveUnitCodeRsrc> productiveUnitCodeList) {
        this.productiveUnitCodeList = productiveUnitCodeList;
    }
}
