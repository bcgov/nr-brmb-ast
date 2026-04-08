package ca.bc.gov.farms.api.rest.v1.resource;

import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.farms.model.v1.InventoryItemDetailList;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.INVENTORY_ITEM_DETAIL_LIST_NAME)
@XmlSeeAlso({ InventoryItemDetailListRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class InventoryItemDetailListRsrc extends BaseResource
        implements InventoryItemDetailList<InventoryItemDetailRsrc> {

    private static final long serialVersionUID = 1L;

    private List<InventoryItemDetailRsrc> inventoryItemDetailList;

    @Override
    public List<InventoryItemDetailRsrc> getInventoryItemDetailList() {
        return inventoryItemDetailList;
    }

    @Override
    public void setInventoryItemDetailList(List<InventoryItemDetailRsrc> inventoryItemDetailList) {
        this.inventoryItemDetailList = inventoryItemDetailList;
    }

}
