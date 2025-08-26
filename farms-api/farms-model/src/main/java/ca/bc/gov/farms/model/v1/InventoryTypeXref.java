package ca.bc.gov.farms.model.v1;

import java.io.Serializable;

public interface InventoryTypeXref extends Serializable {

    public Long getAgristabilityCommodityXrefId();
    public void setAgristabilityCommodityXrefId(Long agristabilityCommodityXrefId);

    public String getMarketCommodityInd();
    public void setMarketCommodityInd(String marketCommodityInd);

    public String getInventoryItemCode();
    public void setInventoryItemCode(String inventoryItemCode);

    public String getInventoryItemDesc();
    public void setInventoryItemDesc(String inventoryItemDesc);

    public String getInventoryGroupCode();
    public void setInventoryGroupCode(String inventoryGroupCode);

    public String getInventoryGroupDesc();
    public void setInventoryGroupDesc(String inventoryGroupDesc);

    public String getInventoryClassCode();
    public void setInventoryClassCode(String inventoryClassCode);

    public String getInventoryClassDesc();
    public void setInventoryClassDesc(String inventoryClassDesc);

    public String getUserEmail();
    public void setUserEmail(String userEmail);
}
