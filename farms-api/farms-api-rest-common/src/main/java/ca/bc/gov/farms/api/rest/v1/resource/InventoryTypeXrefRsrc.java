package ca.bc.gov.farms.api.rest.v1.resource;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.farms.model.v1.InventoryTypeXref;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.INVENTORY_TYPE_XREF_NAME)
@XmlSeeAlso({ InventoryTypeXrefRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class InventoryTypeXrefRsrc extends BaseResource implements InventoryTypeXref {

    private static final long serialVersionUID = 1L;

    private Long agristabilityCommodityXrefId;
    private String marketCommodityInd;
    private String inventoryItemCode;
    private String inventoryItemDesc;
    private String inventoryGroupCode;
    private String inventoryGroupDesc;
    private String inventoryClassCode;
    private String inventoryClassDesc;
    private String userEmail;

    @Override
    public Long getAgristabilityCommodityXrefId() {
        return agristabilityCommodityXrefId;
    }

    @Override
    public void setAgristabilityCommodityXrefId(Long agristabilityCommodityXrefId) {
        this.agristabilityCommodityXrefId = agristabilityCommodityXrefId;
    }

    @Override
    public String getMarketCommodityInd() {
        return marketCommodityInd;
    }

    @Override
    public void setMarketCommodityInd(String marketCommodityInd) {
        this.marketCommodityInd = marketCommodityInd;
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
    public String getInventoryGroupCode() {
        return inventoryGroupCode;
    }

    @Override
    public void setInventoryGroupCode(String inventoryGroupCode) {
        this.inventoryGroupCode = inventoryGroupCode;
    }

    @Override
    public String getInventoryGroupDesc() {
        return inventoryGroupDesc;
    }

    @Override
    public void setInventoryGroupDesc(String inventoryGroupDesc) {
        this.inventoryGroupDesc = inventoryGroupDesc;
    }

    @Override
    public String getInventoryClassCode() {
        return inventoryClassCode;
    }

    @Override
    public void setInventoryClassCode(String inventoryClassCode) {
        this.inventoryClassCode = inventoryClassCode;
    }

    @Override
    public String getInventoryClassDesc() {
        return inventoryClassDesc;
    }

    @Override
    public void setInventoryClassDesc(String inventoryClassDesc) {
        this.inventoryClassDesc = inventoryClassDesc;
    }

    @Override
    public String getUserEmail() {
        return userEmail;
    }

    @Override
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
