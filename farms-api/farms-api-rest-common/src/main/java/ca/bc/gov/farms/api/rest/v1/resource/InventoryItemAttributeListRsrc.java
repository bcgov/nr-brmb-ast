package ca.bc.gov.farms.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.farms.model.v1.InventoryItemAttributeList;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.INVENTORY_ITEM_ATTRIBUTE_LIST_NAME)
@XmlSeeAlso({ InventoryItemAttributeListRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class InventoryItemAttributeListRsrc extends BaseResource
        implements InventoryItemAttributeList<InventoryItemAttributeRsrc> {

    private static final long serialVersionUID = 1L;

    private List<InventoryItemAttributeRsrc> inventoryItemAttributeList;

    public InventoryItemAttributeListRsrc() {
        this.inventoryItemAttributeList = new ArrayList<>();
    }

    @Override
    public List<InventoryItemAttributeRsrc> getInventoryItemAttributeList() {
        return inventoryItemAttributeList;
    }

    @Override
    public void setInventoryItemAttributeList(List<InventoryItemAttributeRsrc> inventoryItemAttributeList) {
        this.inventoryItemAttributeList = inventoryItemAttributeList;
    }

}
