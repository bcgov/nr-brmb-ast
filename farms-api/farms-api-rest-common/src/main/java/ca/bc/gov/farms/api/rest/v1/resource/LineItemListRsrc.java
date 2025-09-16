package ca.bc.gov.farms.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.farms.model.v1.LineItemList;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.LINE_ITEM_ATTRIBUTE_LIST_NAME)
@XmlSeeAlso({ LineItemListRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class LineItemListRsrc extends BaseResource implements LineItemList<LineItemRsrc> {

    private static final long serialVersionUID = 1L;

    private List<LineItemRsrc> lineItemList;

    public LineItemListRsrc() {
        this.lineItemList = new ArrayList<>();
    }

    @Override
    public List<LineItemRsrc> getLineItemList() {
        return lineItemList;
    }

    @Override
    public void setLineItemList(List<LineItemRsrc> lineItemList) {
        this.lineItemList = lineItemList;
    }

}
