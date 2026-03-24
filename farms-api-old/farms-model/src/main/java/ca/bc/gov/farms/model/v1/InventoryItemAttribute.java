package ca.bc.gov.farms.model.v1;

import java.io.Serializable;

public interface InventoryItemAttribute extends Serializable {

    public Long getInventoryItemAttributeId();
    public void setInventoryItemAttributeId(Long inventoryItemAttributeId);

    public String getInventoryItemCode();
    public void setInventoryItemCode(String inventoryItemCode);

    public String getInventoryItemDesc();
    public void setInventoryItemDesc(String inventoryItemDesc);

    public String getRollupInventoryItemCode();
    public void setRollupInventoryItemCode(String rollupInventoryItemCode);

    public String getRollupInventoryItemDesc();
    public void setRollupInventoryItemDesc(String rollupInventoryItemDesc);

    public String getUserEmail();
    public void setUserEmail(String userEmail);
}
