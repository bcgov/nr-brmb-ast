package ca.bc.gov.farms.api.rest.v1.resource;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.farms.model.v1.InventoryItemAttribute;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.INVENTORY_ITEM_ATTRIBUTE_NAME)
@XmlSeeAlso({ InventoryItemAttributeRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class InventoryItemAttributeRsrc extends BaseResource implements InventoryItemAttribute {

    private static final long serialVersionUID = 1L;

    private Long inventoryItemAttributeId;
    private String inventoryItemCode;
    private String inventoryItemDesc;
    private String rollupInventoryItemCode;
    private String rollupInventoryItemDesc;

    @Override
    public Long getInventoryItemAttributeId() {
        return inventoryItemAttributeId;
    }

    @Override
    public void setInventoryItemAttributeId(Long inventoryItemAttributeId) {
        this.inventoryItemAttributeId = inventoryItemAttributeId;
    }

    @Override
    public String getInventoryItemCode() {
        return inventoryItemCode;
    }

    @Override
    public void setInventoryItemCode(String inventoryItemCode) {
        this.inventoryItemCode = inventoryItemCode;
    }

    @Override
    public String getInventoryItemDesc() {
        return inventoryItemDesc;
    }

    @Override
    public void setInventoryItemDesc(String inventoryItemDesc) {
        this.inventoryItemDesc = inventoryItemDesc;
    }

    @Override
    public String getRollupInventoryItemCode() {
        return rollupInventoryItemCode;
    }

    @Override
    public void setRollupInventoryItemCode(String rollupInventoryItemCode) {
        this.rollupInventoryItemCode = rollupInventoryItemCode;
    }

    @Override
    public String getRollupInventoryItemDesc() {
        return rollupInventoryItemDesc;
    }

    @Override
    public void setRollupInventoryItemDesc(String rollupInventoryItemDesc) {
        this.rollupInventoryItemDesc = rollupInventoryItemDesc;
    }
}
