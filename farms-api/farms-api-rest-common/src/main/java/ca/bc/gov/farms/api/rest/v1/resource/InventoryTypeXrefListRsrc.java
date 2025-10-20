package ca.bc.gov.farms.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.farms.model.v1.InventoryTypeXrefList;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.INVENTORY_ITEM_DETAIL_LIST_NAME)
@XmlSeeAlso({ InventoryTypeXrefListRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class InventoryTypeXrefListRsrc extends BaseResource implements InventoryTypeXrefList<InventoryTypeXrefRsrc> {

    private static final long serialVersionUID = 1L;

    private List<InventoryTypeXrefRsrc> inventoryTypeXrefList;

    public InventoryTypeXrefListRsrc() {
        this.inventoryTypeXrefList = new ArrayList<>();
    }

    @Override
    public List<InventoryTypeXrefRsrc> getInventoryTypeXrefList() {
        return inventoryTypeXrefList;
    }

    @Override
    public void setInventoryTypeXrefList(List<InventoryTypeXrefRsrc> inventoryTypeXrefList) {
        this.inventoryTypeXrefList = inventoryTypeXrefList;
    }

}
